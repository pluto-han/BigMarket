package org.example.domain.activity.service.partake;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.model.valobj.ActivityStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.IRaffleActivityPartakeService;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

import java.util.Date;

/**
 * @program: BigMarket
 * @description: 抽奖活动参与
 * @author: Hancong Zhang
 * @create: 11/22/25 4:26 PM
 **/
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {
    protected final IActivityRepository activityRepository;

    public AbstractRaffleActivityPartake(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createRaffleOrder(String userId, Long activityId) {
        return createRaffleOrder(PartakeRaffleActivityEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .build());
    }

    /**
     * create raffle order
     * @param partakeRaffleActivityEntity
     * @return
     */
    @Override
    public UserRaffleOrderEntity createRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        // 1. basic info
        String userId = partakeRaffleActivityEntity.getUserId();
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        Date currentDate = new Date();

        // 2. query activity
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        // 2.1 check activity state
        if (!ActivityStateVO.open.equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        // 2.2 check activity date
        if (currentDate.before(activityEntity.getBeginDateTime()) || currentDate.after(activityEntity.getEndDateTime())) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }

        // 3. query unused raffle order.
        UserRaffleOrderEntity userRaffleOrderEntity = activityRepository.queryNoUsedRaffleOrder(partakeRaffleActivityEntity);
        if (null != userRaffleOrderEntity) {
            // 3.1 If exists, return unused order directly
            log.info("Create raffle order. [find unused raffle order]  userId:{} activityId:{} userRaffleOrderEntity:{}", userId, activityId, userRaffleOrderEntity);
            return userRaffleOrderEntity;
        }

        // 4. filter account and build aggregate object
        CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentDate);

        // 5. build order entity
        userRaffleOrderEntity = this.buildUserRaffleOrder(userId, activityId, currentDate);

        // 6. fill in createPartakeOrderAggregate object (with order entity)
        createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrderEntity);

        // 7. save aggregate object - transaction operation
        activityRepository.saveCreateRaffleOrderAggregate(createPartakeOrderAggregate);

        return userRaffleOrderEntity;
    }

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);

    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);
}
