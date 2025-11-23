package org.example.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.task.model.entity.TaskEntity;
import org.example.domain.task.repository.ITaskRepository;
import org.example.infrastructure.event.EventPublisher;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.po.Task;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 3:12 PM
 **/
@Repository
@Slf4j
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> tasks = taskDao.queryNoSendMessageTaskList();
        List<TaskEntity> taskEntities = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(task.getUserId());
            taskEntity.setTopic(task.getTopic());
            taskEntity.setMessageId(task.getMessageId());
            taskEntity.setMessage(task.getMessage());
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task taskReq = new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageCompleted(taskReq);

    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        Task taskReq = new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageFail(taskReq);

    }
}
