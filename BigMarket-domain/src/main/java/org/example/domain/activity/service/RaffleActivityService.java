package org.example.domain.activity.service;

import org.example.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 6:20 PM
 **/
@Service
public class RaffleActivityService extends AbstractRaffleActivity{
    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
