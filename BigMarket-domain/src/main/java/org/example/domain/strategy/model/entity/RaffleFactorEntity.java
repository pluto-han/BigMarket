package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: BigMarket
 * @description: Raffle Factor Entity
 * @author: Hancong Zhang
 * @create: 11/12/25 5:00&#x202F;PM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {
    private String userId;
    private Long strategyId;
    private Date endDateTime;
}
