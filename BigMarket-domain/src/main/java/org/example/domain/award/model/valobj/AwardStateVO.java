package org.example.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 2:00 PM
 **/
@Getter
@AllArgsConstructor
public enum AwardStateVO {

    create("create", "Award Created"),
    complete("complete", "Award Completed"),
    fail("fail", "Award Failed"),
    ;

    private final String code;
    private final String desc;
}
