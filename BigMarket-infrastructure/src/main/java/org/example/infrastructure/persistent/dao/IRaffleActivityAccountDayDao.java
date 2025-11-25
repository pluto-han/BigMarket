package org.example.infrastructure.persistent.dao;


import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccountDay;

/**
 * @author Hancong Zhang
 * @description 抽奖活动账户表-日次数
 * @create 2024-04-03 15:56
 */
@Mapper
public interface IRaffleActivityAccountDayDao {

    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDay);

    void insertActivityAccountDay(RaffleActivityAccountDay build);

    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay build);

    @DBRouter
    Integer queryRaffleActivityAccountDayPartakeCount(RaffleActivityAccountDay raffleActivityAccountDayReq);
}
