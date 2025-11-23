package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.award.model.valobj.TaskStateVO;
import org.example.types.event.BaseEvent;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 2:03 PM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    /** 用户ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息ID */
    private String messageId;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStateVO state;
}
