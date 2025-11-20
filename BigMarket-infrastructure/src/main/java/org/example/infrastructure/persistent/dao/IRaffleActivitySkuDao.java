package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivity;
import org.example.infrastructure.persistent.po.RaffleActivitySku;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 5:26 PM
 **/
@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(Long sku);
}
