package org.example.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

import static org.example.types.enums.ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL;

/**
 * @program: BigMarket
 * @description: (implementation)Strategy assembly library, in order to initialize strategy
 * @author: Hancong Zhang
 * @create: 11/11/25 1:47 PM
 **/
@Service
@Slf4j
public class StrategyArmoryAndDispatch implements IStrategyArmory, IStrategyDispatch {
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. query strategy configuration
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 2. Strategy Weight Assemble -- apply to rule weight
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);

        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) {
            return true;
        }

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRule(strategyId, ruleWeight);
        if(null == strategyRuleEntity){
            throw new AppException(STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }

        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. get the minimum awardRate
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 2. get the total value of all awardRate
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. use 1 / 0.0001 to get the probability range (percentile, thousandths or ten-thousandths)
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, RoundingMode.CEILING);

        // 4. generate searchTable
        ArrayList<Integer> strategyAwardSearchTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId =  strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            // calculate how many units in searchTable that every awardRate needs
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchTables.add(awardId);
            }
        }

        // 5. shuffle search table
        Collections.shuffle(strategyAwardSearchTables);

        // 6. transform into collection
        HashMap<Integer, Integer> shuffledStrategyAwardRateTable = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchTables.size(); i++) {
            shuffledStrategyAwardRateTable.put(i, strategyAwardSearchTables.get(i));
        }

        // 7. restore in redis
        strategyRepository.storeStrategyAwardSearchRateTables(key, rateRange, shuffledStrategyAwardRateTable);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        int rateRange = strategyRepository.getRateRange(key);
        return strategyRepository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
