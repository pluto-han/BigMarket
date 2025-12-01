package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @author: Hancong Zhang
 * @description: 商品购物车请求对象
 * @create: 12/1/25 10:47 AM
 */
@Data
public class SkuProductShopCartRequestDTO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * sku 商品
     */
    private Long sku;

}
