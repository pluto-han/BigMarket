package org.example.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/17/25 11:14 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleResponseDTO {
    // 奖品id
    private Integer awardId;
    // 奖品编号
    private Integer awardIndex;


}
