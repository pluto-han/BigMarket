package org.example.domain.rebate.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import org.example.domain.rebate.model.valobj.TaskStateVO;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.types.common.Constants;
import org.example.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:33 PM
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {
    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    @Resource
    private SendRebateMessageEvent snedRebateMessageEvent;
    @Autowired
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createRebateOrder(BehaviorEntity behaviorEntity) {
        // 1. 查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorType());
        if (null == dailyBehaviorRebateVOS || dailyBehaviorRebateVOS.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 构建聚合对象
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            // 拼接业务id： 用户id_返利类型_幂等id
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessId();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessId())
                    .bizId(bizId)
                    .build();

            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            // 创建MQ消息
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorRebateOrderEntity.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();

            // 构建消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            // 组装task对象
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .topic(snedRebateMessageEvent.topic())
                    .messageId(rebateMessageEventMessage.getId())
                    .message(rebateMessageEventMessage)
                    .state(TaskStateVO.create)
                    .build();

            // 构建聚合对象
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(behaviorEntity.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .build();

            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        // 3. 存储聚合对象
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);

        // 4. 返回订单id集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
            return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);
    }
}
