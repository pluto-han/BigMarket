package org.example.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/28/25 1:36 PM
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {
    FORWARD("forward", "正向交易，+ 积分"),
    REVERSE("reverse", "逆向交易，- 积分"),
    ;

    private final String code;
    private final String info;

}
