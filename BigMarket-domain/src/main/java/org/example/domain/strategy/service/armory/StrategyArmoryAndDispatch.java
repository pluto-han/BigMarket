package org.example.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.types.common.Constants;
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
    private IStrategyRepository repository;

    /**
     * assemble strategy
     * @param strategyId
     * @return boolean
     */
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置（也就是查询策略下的所有奖品）
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 2. 缓存奖品库存
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId =  strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }

        // 3.1 默认装配配置 【全量抽奖概率】
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 3.2 权重策略配置 - 适用于 rule_weight 权重规则配置
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) return true;

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
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

    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    /**
     * core idea of assembling strategy: Trading space for time:
     * Probability values are represented by "slots". For example, if A has a prob of 60%, B has 30%, C has 10%. Total prob is 100%
     * Then, A occupies 60 slots, with each slot returning A, and so on.
     * @param key
     * @param strategyAwardEntities
     */
    /*
        N.B. The total probability is not always 1, this is acceptable.

        calculate steps:
        1. Find the minimum probability value (i.e. 0.1, 0.02, 0.003, the minimum probability value is 0.003)
        2. Based on the minimum value found in step 1, which is 0.03, the integer values for percentages and per mille
        can be calculated. In this case, it is 1000. (1 / 0.001 = 1000)
        3. According to [slots = probability * 1000], each probability has 100, 20 and 3 slots, respectively.
        In total, there are 123 slots.
        4. We use [0, 123] as the range of random value. (i.e. There are 100 awards that has probability value of 0.1,
        20 awards that has probability value of 0.02, 3 awards that has probability value of 0.003.)
     */
    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. Get the minimum probability value
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 2. Find the probability range value using iterative calculations
        BigDecimal rateRange = BigDecimal.valueOf(convert(minAwardRate.doubleValue()));

        // 3. Generate StrategyAwardSearchRateTable
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            // calculate how many slots that per probability value need, fill in cyclically
            for (int i = 0; i < rateRange.multiply(awardRate).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 4. shuffle the StrategyAwardSearchRateTable
        Collections.shuffle(strategyAwardSearchRateTables);

        // 5. Generate a map, where each key corresponds to a probability value. The awardId is then determined based on these probabilities.
        Map<Integer, Integer> shuffledStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 6. store in Redis
        repository.storeStrategyAwardSearchRateTables(key, shuffledStrategyAwardSearchRateTable.size(), shuffledStrategyAwardSearchRateTable);
    }

    /**
     * Convert calculation only considers the decimal places.
     * i.e. [0.01 returns 100], [0.009 returns 1000], and [0.0018 returns 10000].
     */
    private double convert(double min){
        double current = min;
        double max = 1;
        while (current < 1){
            current = current * 10;
            max = max * 10;
        }
        return max;
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    /**
     * 库存扣减
     * @param strategyId
     * @param awardId
     * @return
     */
    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endDataTime) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.substractionAwardStock(cacheKey);
    }

    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(cacheKey, awardCount);
    }
}
