package org.example.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: BigMarket
 * @description: 订单状态枚举值对象
 * @author: Hancong Zhang
 * @create: 11/20/25 5:38 PM
 **/
@Getter
@AllArgsConstructor
public enum OrderStateVO {

    completed("completed", "完成");

    private final String code;
    private final String desc;
}
