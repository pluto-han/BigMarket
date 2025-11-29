package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.valobj.OrderStateVO;

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
public class CreateQuotaOrderAggregate {
    private String userId;

    private Long activityId;

    private Integer totalCount;

    private Integer dayCount;

    private Integer monthCount;
    
    private ActivityOrderEntity activityOrderEntity;

    public void setOrderState(OrderStateVO orderState) {
        this.activityOrderEntity.setState(orderState);
    }
}
