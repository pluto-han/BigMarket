package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.ActivityAccountDayEntity;
import org.example.domain.activity.model.entity.ActivityAccountEntity;
import org.example.domain.activity.model.entity.ActivityAccountMonthEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @program: BigMarket
 * @description: 参与活动订单聚合对象
 * @author: Hancong Zhang
 * @create: 11/22/25 4:49 PM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {
    private String userId;

    private Long activityId;

    private ActivityAccountEntity activityAccountEntity;

    private boolean isExistAccountMonth = true;

    private ActivityAccountDayEntity activityAccountDayEntity;

    private boolean isExistAccountDay = true;

    private ActivityAccountMonthEntity activityAccountMonthEntity;

    private UserRaffleOrderEntity userRaffleOrderEntity;

}
