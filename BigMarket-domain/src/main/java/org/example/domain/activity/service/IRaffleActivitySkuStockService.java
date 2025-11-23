package org.example.domain.activity.service;

import org.example.domain.activity.model.valobj.ActivitySkuStockKeyVO;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/22/25 11:04 AM
 **/
public interface IRaffleActivitySkuStockService {
    // 获取活动sku库存消耗队列
    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    //清空队列
    void clearQueueValue();

    // 延迟队列 + 任务趋势更新活动sku库存
    void updateActivitySkuStock(Long sku);

    // 缓存库存已消耗完毕， 清空数据库库存
    void clearActivitySkuStock(Long sku);
}
