package org.example.domain.activity.service.armory;

import java.util.Date;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/22/25 12:50 AM
 **/
public interface IActivityDispatch {
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
