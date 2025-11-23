package org.example.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 2:19 PM
 **/
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "task created"),
    complete("complete", "task send completed"),
    fail("fail", "task send failed"),
    ;


    private final String code;
    private final String desc;
}
