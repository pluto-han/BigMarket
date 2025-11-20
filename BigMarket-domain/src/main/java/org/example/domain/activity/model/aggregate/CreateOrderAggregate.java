package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.ActivityAccountEntity;
import org.example.domain.activity.model.entity.ActivityOrderEntity;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 6:09 PM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {

    /**
     * 活动账户实体
     */
    private ActivityAccountEntity activityAccountEntity;
    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrderEntity;
}
