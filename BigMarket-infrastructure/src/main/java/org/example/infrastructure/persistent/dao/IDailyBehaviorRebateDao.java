package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;

import java.util.List;

/**
 * @author: Hancong Zhang
 * @description: 日常行为返利活动配置Dao
 * @create: 11/26/25 12:28 PM
 */
@Mapper
public interface IDailyBehaviorRebateDao {
    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String code);
}
