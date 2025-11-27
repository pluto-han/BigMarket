package org.example.trigger.api;

import org.example.trigger.api.dto.ActivityDrawRequestDTO;
import org.example.trigger.api.dto.ActivityDrawResponseDTO;
import org.example.types.model.Response;

public interface IRaffleActivityService {
    /**
     * 活动装配，数据预热缓存
     * @param activityId 活动ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 日历签到返利
     * @param userId 用户ID
     * @return 返利结果
     */
    Response<Boolean> calendarCheckInRebate(String userId);

    /**
     * 检查用户是否签到并返利
     * @param userId 用户ID
     * @return 检查结果
     */
    Response<Boolean> isCheckInAndRebate(String userId);
}
