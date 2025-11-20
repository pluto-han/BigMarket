package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivity;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 4:01 PM
 **/
@Mapper
public interface IRaffleActivityDao {
    RaffleActivity queryRaffleActivityByActivityId(Long activityId);
}
