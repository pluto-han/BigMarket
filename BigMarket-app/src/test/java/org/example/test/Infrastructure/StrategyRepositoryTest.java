package org.example.test.Infrastructure;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.valobj.RuleTreeVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description: 仓储数据库查询测试
 * @author: Hancong Zhang
 * @create: 11/14/25 6:27 PM
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRepositoryTest {
    @Resource
    private IStrategyRepository strategyRepository;

    @Test
    public void queryRuleTreeVOByTreeId() {
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock_1");
        log.info("测试结果：{}", JSON.toJSONString(ruleTreeVO));
    }
}
