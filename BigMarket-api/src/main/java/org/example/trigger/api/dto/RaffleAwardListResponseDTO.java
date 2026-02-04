package org.example.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: BigMarket
 * @description: 奖品抽奖列表，应答对象
 * @author: Hancong Zhang
 * @create: 11/17/25 11:02 AM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {
    // 奖品id
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖一次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 奖品次数规则 -抽奖N次后解锁，未配置为空
    private Integer awardRuleLockCount;
    // 奖品是否解锁 -true已解锁， false未解锁
    private Boolean isAwardUnlock;
    // 等待解锁次数 - 规则的抽奖N次解锁 减去 用户已经抽奖次数
    private Integer waitUnlockCount;

    private BigDecimal awardRate;
}
