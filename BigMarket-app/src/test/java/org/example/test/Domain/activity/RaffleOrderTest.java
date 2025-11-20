package org.example.test.Domain.activity;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;
import org.example.domain.activity.service.IRaffleOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 6:35 PM
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {
    @Resource
    private IRaffleOrder raffleOrder;

    @Test
    public void test_createRaffleActivityOrder() {
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("hancong");
        activityShopCartEntity.setSku(9011L);
        ActivityOrderEntity raffleActivityOrder = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("raffle order result : {}", JSON.toJSONString(raffleActivityOrder));
    }
}
