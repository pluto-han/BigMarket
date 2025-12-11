package org.example.domain.activity.service.quota;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.valobj.OrderTradeTypeVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.activity.service.quota.policy.ITradePolicy;
import org.example.domain.activity.service.quota.rule.IActionChain;
import org.example.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

import java.math.BigDecimal;
import java.util.Map;

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
    // 不同类型的交易策略实现类，通过构造函数注入到 Map 中，教程；https://bugstack.cn/md/road-map/spring-dependency-injection.html
    private final Map<String, ITradePolicy> tradePolicyGroup;

    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, Map<String, ITradePolicy> tradePolicyGroup) {
        super(activityRepository, defaultActivityChainFactory);
        this.tradePolicyGroup = tradePolicyGroup;
    }

    /**
     * create sku recharge order
     * @param skuRechargeEntity
     * @return
     */
    @Override
    public UnpaidActivityOrderEntity createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1. Parameter validation
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(outBusinessNo) || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 查询未支付订单「一个月以内的未支付订单」
        UnpaidActivityOrderEntity unpaidCreditOrder =  activityRepository.queryUnpaidActivityOrder(skuRechargeEntity);
        if (null != unpaidCreditOrder) return unpaidCreditOrder;

        // 3. 查询基础信息「sku、活动、次数」
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        // 4. 账户额度 【交易属性的兑换，需要校验额度账户】
        if (OrderTradeTypeVO.credit_pay_trade.equals(skuRechargeEntity.getOrderTradeType())){
            BigDecimal availableAmount = activityRepository.queryUserCreditAccountAmount(userId);
            if (availableAmount.compareTo(activitySkuEntity.getProductAmount()) < 0) {
                throw new AppException(ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getCode(), ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getInfo());
            }
        }

        // 5. 活动动作规则校验 「过滤失败则直接抛异常」- 责任链扣减sku库存
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        actionChain.action(activitySkuEntity, activityCountEntity, activityEntity);

        // 6. build order aggregate
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityCountEntity, activityEntity);

        // 7. 交易策略 - 【积分兑换，支付类订单】【返利无支付交易订单，直接充值到账】【订单状态变更交易类型策略】
        ITradePolicy tradePolicy = tradePolicyGroup.get(skuRechargeEntity.getOrderTradeType().getCode());
        tradePolicy.trade(createQuotaOrderAggregate);

        // 8. return order info
        ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
        return UnpaidActivityOrderEntity.builder()
                .userId(userId)
                .orderId(activityOrderEntity.getOrderId())
                .outBusinessNo(activityOrderEntity.getOutBusinessNo())
                .payAmount(activityOrderEntity.getPayAmount())
                .build();
    }

//    protected abstract void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityCountEntity activityCountEntity, ActivityEntity activityEntity);
}
