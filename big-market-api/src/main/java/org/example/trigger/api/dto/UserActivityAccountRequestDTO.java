package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/27/25 2:41 PM
 */
@Data
public class UserActivityAccountRequestDTO {

    private String userId;
    private Long activityId;

}
