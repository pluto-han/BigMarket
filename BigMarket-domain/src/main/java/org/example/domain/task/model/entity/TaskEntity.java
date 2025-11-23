package org.example.domain.task.model.entity;

import lombok.Data;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 3:07 PM
 **/
@Data
public class TaskEntity {
    private String userId;

    private String topic;

    private String messageId;

    private String message;
}
