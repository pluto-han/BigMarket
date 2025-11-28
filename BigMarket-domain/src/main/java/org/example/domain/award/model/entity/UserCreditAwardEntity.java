package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author: Hancong Zhang
 * @description: 用户积分奖品实体对象
 * @create: 11/28/25 11:29 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserCreditAwardEntity {

    /** 用户ID */
    private String userId;
    /** 积分值 */
    private BigDecimal creditAmount;

}
