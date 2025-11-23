package org.example.domain.award.service;

import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.award.model.entity.TaskEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.valobj.TaskStateVO;
import org.example.domain.award.repository.IAwardRepository;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/23/25 2:24 PM
 **/
@Service
public class AwardService implements IAwardService {

    @Resource
    private IAwardRepository awardRepository;

    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // build message
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEvnetMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        // build task
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEvnetMessage.getId());
        taskEntity.setMessage(sendAwardMessageEvnetMessage);
        taskEntity.setState(TaskStateVO.create);

        // build aggregate
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        // save aggregate( transactional )
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }
}
