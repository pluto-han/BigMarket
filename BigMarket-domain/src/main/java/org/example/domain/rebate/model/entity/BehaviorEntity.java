package org.example.domain.rebate.model.entity;

import lombok.*;
import org.checkerframework.framework.qual.NoQualifierParameter;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:34 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    private String userId;

    private BehaviorTypeVO behaviorType;

    private String outBusinessId;
}
