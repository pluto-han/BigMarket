package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: BigMarket
 * @description: Raffle Award Entity (strategyId and some properties of award po)
 * @author: Hancong Zhang
 * @create: 11/12/25 5:05 PM
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {
    /** 抽奖奖品ID - 内部流转使用 */
    private Integer awardId;

    /** 奖品配置信息 */
    private String awardConfig;

    /** 奖品顺序号 */
    private Integer sort;
}
