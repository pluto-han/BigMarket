package org.example.domain.credit.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/28/25 1:35 PM
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {

    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),

    ;

    private final String name;

}
