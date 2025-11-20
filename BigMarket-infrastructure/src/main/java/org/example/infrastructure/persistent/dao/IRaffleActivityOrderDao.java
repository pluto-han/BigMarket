package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityOrder;

import java.util.List;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 4:01 PM
 **/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityOrderDao {
    @DBRouter(key = "userId")
    void insert(RaffleActivityOrder raffleActivityOrder);

    @DBRouter
    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId(String userId);

}
