package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserCreditAccount;
import cn.bugstack.middleware.db.router.annotation.DBRouter;

/**
 * @author: Hancong Zhang
 * @description: 用户积分账户
 * @create: 11/28/25 11:40 AM
 */
@Mapper
public interface IUserCreditAccountDao {
    void insert(UserCreditAccount userCreditAccountReq);

    int updateAddAmount(UserCreditAccount userCreditAccountReq);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccountReq);

    int updateSubtractionAmount(UserCreditAccount userCreditAccountReq);
}
