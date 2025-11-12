package org.example.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @program: BigMarket
 * @description: (implementation)Strategy assembly library, in order to initialize strategy
 * @author: Hancong Zhang
 * @create: 11/11/25 1:47 PM
 **/
@Service
@Slf4j
public class StrategyArmory implements IStrategyArmory, IStrategyDispatch {
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        // 1. query strategy configuration
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);

        // 2. get the minimum awardRate
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 3. get the total value of all awardRate
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. use 1 / 0.0001 to get the probability range (percentile, thousandths or ten-thousandths)
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, RoundingMode.CEILING);

        // 5. generate searchTable
        ArrayList<Integer> strategyAwardSearchTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId =  strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            // calculate how many units in searchTable that every awardRate needs
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchTables.add(awardId);
            }
        }

        // 6. shuffle search table
        Collections.shuffle(strategyAwardSearchTables);

        // 7. transform into collection
        HashMap<Integer, Integer> shuffledStrategyAwardRateTable = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchTables.size(); i++) {
            shuffledStrategyAwardRateTable.put(i, strategyAwardSearchTables.get(i));
        }

        // 8. restore in redis
        strategyRepository.storeStrategyAwardSearchRateTables(strategyId, rateRange, shuffledStrategyAwardRateTable);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }
}
