package org.example.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.model.entity.TaskEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @program: BigMarket
 * @description: award & task aggregate
 * @author: Hancong Zhang
 * @create: 11/23/25 2:23 PM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {

    private UserAwardRecordEntity userAwardRecordEntity;

    private TaskEntity taskEntity;
}
