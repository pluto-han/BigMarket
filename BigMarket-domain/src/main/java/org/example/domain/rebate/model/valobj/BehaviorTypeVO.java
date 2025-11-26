package org.example.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Hancong Zhang
 * @description: behavior type enum valobj
 * @create: 11/26/25 12:34 PM
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {

    CHECK_IN("check_in", "User Check-In"),
    PURCHASE("purchase", "User Purchase"),
    ;

    private final String code;
    private final String info;
}
