package org.example.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/28/25 11:31 AM
 */
@Getter
@AllArgsConstructor

public enum AccountStatusVO {
    open("open", "开启"),
    close("close", "冻结"),
    ;

    private final String code;
    private final String desc;

}
