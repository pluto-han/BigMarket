package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/27/25 2:41 PM
 */
@Data
public class UserActivityAccountResponseDTO {

    private Integer totalCount;

    private Integer totalCountSurplus;

    private Integer monthCount;

    private Integer monthCountSurplus;

    private Integer dayCount;

    private Integer dayCountSurplus;

}
