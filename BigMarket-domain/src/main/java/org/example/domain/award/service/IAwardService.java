package org.example.domain.award.service;

import org.example.domain.award.model.entity.DistributeAwardEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @program: BigMarket
 * @description: award service interface
 * @author: Hancong Zhang
 * @create: 11/23/25 1:57 PM
 **/
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    /**
     * 配送发货奖品
     */
    void distributeAward(DistributeAwardEntity distributeAwardEntity);

}
