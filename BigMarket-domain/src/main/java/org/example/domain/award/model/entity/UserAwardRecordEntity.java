package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.model.valobj.AwardStateVO;

import java.util.Date;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 1:59 PM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordEntity {
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖订单ID【作为幂等使用】 */
    private String orderId;
    /** 奖品ID */
    private Integer awardId;
    /** 奖品标题（名称） */
    private String awardTitle;
    /** 中奖时间 */
    private Date awardTime;
    /** 奖品状态；create-创建、completed-发奖完成 */
    private AwardStateVO awardState;
}
