package org.example.domain.award.service.distribute;

import org.example.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author: Hancong Zhang
 * @description: 分发奖品接口
 * @create: 11/28/25 11:34 AM
 */
public interface IDistributeAward   {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}
