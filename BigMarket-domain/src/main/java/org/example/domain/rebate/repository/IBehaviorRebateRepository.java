package org.example.domain.rebate.repository;

import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;
import org.example.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:34 PM
 */
public interface IBehaviorRebateRepository {
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorType);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
