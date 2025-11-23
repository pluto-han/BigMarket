package org.example.domain.activity.service.quota;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.activity.service.quota.rule.IActionChain;
import org.example.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

/**
 * @program: BigMarket
 * @description: Quota Distribution Abstract Class
 * When user checks in or buy certain amount of credits, system will allocate quota to user account.
 * This abstract class defines the common process of quota allocation, using Template Method pattern.
 * Subclasses need to implement certain methods. i.e. saving sku order and building order aggregate.
 * This class also uses Chain of Responsibility pattern to handle various activity rules. i.e. activity time, stock amount, etc.
 * @author: Hancong Zhang
 * @create: 11/20/25 6:12 PM
 **/
@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {
    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, IActivityRepository activityRepository1) {
        super(activityRepository, defaultActivityChainFactory);
    }

    /**
     * create sku recharge order
     * @param skuRechargeEntity
     * @return
     */
    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1. Parameter validation
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(outBusinessNo) || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. search basic information
        // 2.1 search sku entity
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        // 2.2  search activity entity
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 2.3 search activity count entity
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        // 3. strat validation check using chain of responsibility pattern
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        actionChain.action(activitySkuEntity, activityCountEntity, activityEntity);

        // 4. build order aggregate
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityCountEntity, activityEntity);
        
        // 5. save order
        doSaveOrder(createQuotaOrderAggregate);

        // 6. return orderId
        return createQuotaOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityCountEntity activityCountEntity, ActivityEntity activityEntity);
}
