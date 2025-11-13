package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @program: BigMarket
 * @description: Raffle Strategy Interface
 * @author: Hancong Zhang
 * @create: 11/12/25 4:56 PM
 **/
public interface IRaffleStrategy {
    /**
     * perform raffle and then return award info
     * @param raffleFactorEntity
     * @return
     */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
