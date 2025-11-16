package org.example.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @program: BigMarket
 * @description: 策略奖品库存Key标识值对象
 * @author: Hancong Zhang
 * @create: 11/16/25 2:31 AM
 **/
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardStockKeyVO {
    // 策略ID
    private Long strategyId;
    // 奖品ID
    private Integer awardId;
}
