package org.example.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.task.model.entity.TaskEntity;
import org.example.domain.task.service.ITaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 3:04 PM
 **/
@Component()
@Slf4j
public class SendMessageTaskJob {

    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            dbRouter.setDBKey(1);
            dbRouter.setDBKey(0);
            List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
            log.info("测试结果:{}", taskEntities.size());
        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败", e);
        }
    }
}
