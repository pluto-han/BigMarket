package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityCount;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 4:03 PM
 **/
@Mapper
public interface IRaffleActivityCountDao {
    RaffleActivityCount queryRaffleActivityCountByActivityId(Long activityId);
}
