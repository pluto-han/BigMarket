package org.example.domain.rebate.model.valobj;

import lombok.*;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:35 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBehaviorRebateVO {
    /**
     * 行为类型（sign 签到、openai_pay 支付）
     */
    private String behaviorType;
    /**
     * 返利描述
     */
    private String rebateDesc;
    /**
     * 返利类型（sku 活动库存充值商品、integral 用户活动积分）
     */
    private String rebateType;
    /**
     * 返利配置
     */
    private String rebateConfig;
    /**
     * 状态（open 开启、close 关闭）
     */
    private String state;
}
