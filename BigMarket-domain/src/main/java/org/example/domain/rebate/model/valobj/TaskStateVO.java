package org.example.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:47 PM
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "message created"),
    complete("complete", "send complete"),
    failed("failed", "send failed"),
    ;


    private final String code;
    private final String desc;
}
