package org.example.domain.task.service;

import org.example.domain.task.model.entity.TaskEntity;
import org.example.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 3:08 PM
 **/
@Service
public class TaskService implements ITaskService{
    @Resource
    private ITaskRepository taskRepository;

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskRepository.updateTaskSendMessageCompleted(userId, messageId);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFail(userId, messageId);
    }

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }
}
