package org.example.test.Domain;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.service.armory.StrategyArmoryAndDispatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/11/25 3:13 PM
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryAndDispatchTest {
    @Resource
    private StrategyArmoryAndDispatch strategyArmoryAndDispatch;

    @Before
    public void test_strategyArmory(){
        boolean success = strategyArmoryAndDispatch.assembleLotteryStrategy(100001L);
        log.info("Test Result: {}", success);
    }

    @Test
    public void test_getAssembleRandomVal(){
        log.info("测试结果：{} - 奖品id值", strategyArmoryAndDispatch.getRandomAwardId(100001L));
//        log.info("测试结果：{} - 奖品id值", strategyArmoryAndDispatch.getRandomAwardId(100002L));
//        log.info("测试结果：{} - 奖品id值", strategyArmoryAndDispatch.getRandomAwardId(100002L));
    }

    @Test
    public void test_getAssembleRandomVal_ruleWeightValue(){
        log.info("测试结果：{} - 60 策略配置", strategyArmoryAndDispatch.getRndomAwardId(100001L, "60:102,103,104,105"));
        log.info("测试结果：{} - 200 策略配置", strategyArmoryAndDispatch.getRndomAwardId(100001L, "200:106,107"));
        log.info("测试结果：{} - 1000 策略配置", strategyArmoryAndDispatch.getRndomAwardId(100001L, "1000:105"));

    }
}
