package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @program: BigMarket
 * @description: 抽奖活动参与服务接口
 * @author: Hancong Zhang
 * @create: 11/22/25 4:26 PM
 **/
public interface IRaffleActivityPartakeService {

    UserRaffleOrderEntity createRaffleOrder(String userId, Long activityId);

    // 创建抽奖单： 用户参与抽奖活动，扣减活动账户库存，产生抽奖单。如存在未被使用的抽奖单号，则返回已经存在的抽奖单
    UserRaffleOrderEntity createRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
