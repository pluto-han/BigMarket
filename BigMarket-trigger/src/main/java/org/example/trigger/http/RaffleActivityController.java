package org.example.trigger.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.valobj.OrderTradeTypeVO;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.activity.service.IRaffleActivityPartakeService;
import org.example.domain.activity.service.IRaffleActivitySkuProductService;
import org.example.domain.activity.service.armory.IActivityArmory;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.valobj.AwardStateVO;
import org.example.domain.award.service.IAwardService;
import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.TradeEntity;
import org.example.domain.credit.model.valobj.TradeNameVO;
import org.example.domain.credit.model.valobj.TradeTypeVO;
import org.example.domain.credit.service.ICreditAdjustService;
import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;
import org.example.domain.rebate.service.BehaviorRebateService;
import org.example.domain.rebate.service.IBehaviorRebateService;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.service.IRaffleAward;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.trigger.api.IRaffleActivityService;
import org.example.trigger.api.dto.*;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity/")
public class RaffleActivityController implements IRaffleActivityService {
    private final SimpleDateFormat dataFormatDay = new SimpleDateFormat("yyyyMMdd");

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IAwardService awardService;
    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IBehaviorRebateService behaviorRebateService;
    @Resource
    private ICreditAdjustService creditAdjustService;
    @Resource
    private IRaffleActivitySkuProductService raffleActivitySkuProductService;

    @RequestMapping(value = "armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(@RequestParam Long activityId) {
        try {
            log.info("Assemble activity, data preheating begins.  activityId:{}", activityId);
            // 1. assemble activity
            activityArmory.assembleActivitySkuByActivityId(activityId);
            // 2. assemble strategy
            strategyArmory.assembleRaffleStrategyByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
            log.info("Assemble activity, data preheating finishes. activityId:{}", activityId);
            return response;
        } catch (Exception e) {
            log.error("Assemble activity, data preheating failed. activityId：{}", activityId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("Activity raffle begins. userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            // 1. parameter validation
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                // throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
                if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                    return Response.<ActivityDrawResponseDTO>builder()
                            .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                            .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                            .build();
                }
            }

            // 2. partake in the activity - create a participation record
            UserRaffleOrderEntity userRaffleOrderEntity = raffleActivityPartakeService.createRaffleOrder(request.getUserId(), request.getActivityId());
            log.info("Activity raffle, create order. userId:{} activityId:{} orderId:{}", request.getUserId(), request.getActivityId(), userRaffleOrderEntity.getOrderId());

            // 3. raffle strategy - perform raffle
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(request.getUserId())
                    .strategyId(userRaffleOrderEntity.getStrategyId())
                    .endDateTime(userRaffleOrderEntity.getEndDateTime())
                    .build());

            // 4. restore result - write award record
            UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                    .userId(userRaffleOrderEntity.getUserId())
                    .activityId(userRaffleOrderEntity.getActivityId())
                    .strategyId(userRaffleOrderEntity.getStrategyId())
                    .orderId(userRaffleOrderEntity.getOrderId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .awardConfig(raffleAwardEntity.getAwardConfig())
                    .build();

            awardService.saveUserAwardRecord(userAwardRecordEntity);

            // 5. return result
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
        } catch (AppException e) {
            log.error("活动抽奖失败 userId:{} activityId:{} code:{} info:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "calendar_check_in_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> calendarCheckInRebate(@RequestParam String userId) {
        try {
            log.info("用户签到，返利开始 userId:{}", userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorType(BehaviorTypeVO.CHECK_IN);
            behaviorEntity.setOutBusinessId(dataFormatDay.format(new Date()));
            List<String> orderIds = behaviorRebateService.createRebateOrder(behaviorEntity);
            log.info("用户签到，返利完成 userId:{} orderIds:{}", userId, JSON.toJSONString(orderIds));
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (AppException e) {
            log.info("签到返利异常 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.info("签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }


    }

    @RequestMapping(value = "is_check_in_and_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> isCheckInAndRebate(@RequestParam String userId) {
        try {
            log.info("查询用户是否完成签到返利，开始 userId:{}", userId);
            String outBusinessId = dataFormatDay.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = behaviorRebateService.queryOrderByOutBusinessNo(userId, outBusinessId);
            log.info("查询用户是否完成签到返利，完成 userId:{} orders:{}", userId, JSON.toJSONString(behaviorRebateOrderEntities));
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(!behaviorRebateOrderEntities.isEmpty())
                    .build();
        } catch (Exception e) {
            log.error("查询用户是否完成签到返利，失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @RequestMapping(value = "query_user_activity_account", method = RequestMethod.POST)
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO request) {
        try {
            log.info("查询用户活动账户信息，开始 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                return Response.<UserActivityAccountResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }
            // 2. 查询用户活动账户信息
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryActivityAccountEntity(request.getActivityId(), request.getUserId());
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = new UserActivityAccountResponseDTO();
            userActivityAccountResponseDTO.setTotalCount(activityAccountEntity.getTotalCount());
            userActivityAccountResponseDTO.setTotalCountSurplus(activityAccountEntity.getTotalCountSurplus());
            userActivityAccountResponseDTO.setMonthCount(activityAccountEntity.getMonthCount());
            userActivityAccountResponseDTO.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
            userActivityAccountResponseDTO.setDayCount(activityAccountEntity.getDayCount());
            userActivityAccountResponseDTO.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
            log.info("查询用户活动账户信息，完成 userId: {} activityId: {} dto: {}", request.getUserId(), request.getActivityId(), JSON.toJSONString(userActivityAccountResponseDTO)) ;

            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(userActivityAccountResponseDTO)
                    .build();
        } catch (Exception e) {
            log.error("查询用户活动账户信息，失败 userId: {} activityId: {}", request.getUserId(), request.getActivityId(), e);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "query_sku_product_list_by_activity_id", method = RequestMethod.POST)
    @Override
    public Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId) {
        try {
            log.info("查询sku商品集合开始 activityId:{}", activityId);
            // 1. 参数校验
            if (null == activityId) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            // 2. 查询商品&封装数据
            List<SkuProductEntity> skuProductEntities = raffleActivitySkuProductService.querySkuProductEntityListByActivityId(activityId);
            List<SkuProductResponseDTO> skuProductResponseDTOS = new ArrayList<>(skuProductEntities.size());
            for (SkuProductEntity skuProductEntity : skuProductEntities) {

                SkuProductResponseDTO.ActivityCount activityCount = new SkuProductResponseDTO.ActivityCount();
                activityCount.setTotalCount(skuProductEntity.getActivityCount().getTotalCount());
                activityCount.setMonthCount(skuProductEntity.getActivityCount().getMonthCount());
                activityCount.setDayCount(skuProductEntity.getActivityCount().getDayCount());

                SkuProductResponseDTO skuProductResponseDTO = new SkuProductResponseDTO();
                skuProductResponseDTO.setSku(skuProductEntity.getSku());
                skuProductResponseDTO.setActivityId(skuProductEntity.getActivityId());
                skuProductResponseDTO.setActivityCountId(skuProductEntity.getActivityCountId());
                skuProductResponseDTO.setStockCount(skuProductEntity.getStockCount());
                skuProductResponseDTO.setStockCountSurplus(skuProductEntity.getStockCountSurplus());
                skuProductResponseDTO.setProductAmount(skuProductEntity.getProductAmount());
                skuProductResponseDTO.setActivityCount(activityCount);
                skuProductResponseDTOS.add(skuProductResponseDTO);
            }

            log.info("查询sku商品集合完成 activityId:{} skuProductResponseDTOS:{}", activityId, JSON.toJSONString(skuProductResponseDTOS));
            return Response.<List<SkuProductResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(skuProductResponseDTOS)
                    .build();
        } catch (Exception e) {
            log.error("查询sku商品集合失败 activityId:{}", activityId, e);
            return Response.<List<SkuProductResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "query_user_credit_account", method = RequestMethod.POST)
    @Override
    public Response<BigDecimal> queryUserCreditAccount(String userId) {
        try {
            log.info("查询用户积分值开始 userId:{}", userId);
            CreditAccountEntity creditAccountEntity = creditAdjustService.queryUserCreditAccount(userId);
            log.info("查询用户积分值完成 userId:{} adjustAmount:{}", userId, creditAccountEntity.getAdjustAmount());
            return Response.<BigDecimal>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(creditAccountEntity.getAdjustAmount())
                    .build();
        } catch (Exception e) {
            log.error("查询用户积分值失败 userId:{}", userId, e);
            return Response.<BigDecimal>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "credit_pay_exchange_sku", method = RequestMethod.POST)
    @Override
    public Response<Boolean> creditPayExchangeSku(@RequestBody SkuProductShopCartRequestDTO request) {
        try {
            log.info("积分兑换商品开始 userId:{} sku:{}", request.getUserId(), request.getSku());
            // 1. 创建兑换商品sku订单，outBusinessNo 每次创建出一个单号。
            UnpaidActivityOrderEntity unpaidActivityOrder = raffleActivityAccountQuotaService.createSkuRechargeOrder(SkuRechargeEntity.builder()
                    .userId(request.getUserId())
                    .sku(request.getSku())
                    .outBusinessNo(RandomStringUtils.randomNumeric(12))
                    .orderTradeType(OrderTradeTypeVO.credit_pay_trade)
                    .build());
            log.info("积分兑换商品，创建订单完成 userId:{} sku:{} outBusinessNo:{}", request.getUserId(), request.getSku(), unpaidActivityOrder.getOutBusinessNo());

            // 2.支付兑换商品
            String orderId = creditAdjustService.createOrder(TradeEntity.builder()
                    .userId(unpaidActivityOrder.getUserId())
                    .tradeName(TradeNameVO.CONVERT_SKU)
                    .tradeType(TradeTypeVO.REVERSE)
                    .amount(unpaidActivityOrder.getPayAmount().negate())
                    .outBusinessNo(unpaidActivityOrder.getOutBusinessNo())
                    .build());
            log.info("积分兑换商品，支付订单完成  userId:{} sku:{} orderId:{}", request.getUserId(), request.getSku(), orderId);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        }catch (AppException e) {
            log.error("积分兑换商品失败 userId:{} activityId:{}",  request.getUserId(), request.getSku(), e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }  catch (Exception e) {
            log.error("积分兑换商品失败 userId:{} sku:{}", request.getUserId(), request.getSku(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }
}
