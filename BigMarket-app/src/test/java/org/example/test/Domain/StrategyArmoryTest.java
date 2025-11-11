package org.example.test.Domain;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.service.armory.StrategyArmory;
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
public class StrategyArmoryTest {
    @Resource
    private StrategyArmory strategyArmory;

    @Test
    public void test_strategyArmory(){
        strategyArmory.assembleLotteryStrategy(100002L);
    }

    @Test
    public void test_getAssembleRandomVal(){
        log.info("测试结果：{} - 奖品id值", strategyArmory.getRandomAwardId(100002L));
        log.info("测试结果：{} - 奖品id值", strategyArmory.getRandomAwardId(100002L));
        log.info("测试结果：{} - 奖品id值", strategyArmory.getRandomAwardId(100002L));
    }
}
