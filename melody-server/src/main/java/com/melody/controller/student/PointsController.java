package com.melody.controller.student;

import com.melody.dto.PointsPurchaseOrderPaymentDTO;
import com.melody.dto.PointsPurchaseOrderSubmitDTO;
import com.melody.dto.RatingExchangeDTO;
import com.melody.exception.BaseException;
import com.melody.result.Result;
import com.melody.service.PointsService;
import com.melody.vo.LeftoverPointsVO;
import com.melody.vo.PointsOrderPaymentVO;
import com.melody.vo.PointsOrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 积分系统入口
 */
@RestController
@Api(tags = "学生旗下积分系统")
@Slf4j
@RequestMapping("/student/points")
public class PointsController {

    @Autowired
    PointsService pointsService;

    @ApiOperation("查询当前学生剩余积分")
    @GetMapping("/check")
    public Result<LeftoverPointsVO> queryLeftoverPoints(){
        log.info("查询当前学生剩余积分");
        LeftoverPointsVO leftoverPointsVO = pointsService.queryLeftoverPointsVO();
        return Result.success(leftoverPointsVO);
    }

    @ApiOperation("学生下单购买积分")
    @PostMapping("/submitPointsOrder")
    public Result<PointsOrderSubmitVO> submitPointsOrder(@RequestBody PointsPurchaseOrderSubmitDTO pointsPurchaseOrderSubmitDTO){
        log.info("学生下单购买积分,{}",pointsPurchaseOrderSubmitDTO);
        PointsOrderSubmitVO pointsOrderSubmitVO = pointsService.submitPointsOrder(pointsPurchaseOrderSubmitDTO);
        return Result.success(pointsOrderSubmitVO);
    }

    @ApiOperation("学生确认支付积分订单")
    @PostMapping("/paymentPointsOrder")
    public Result<PointsOrderPaymentVO> paymentPointsOrder(@RequestBody PointsPurchaseOrderPaymentDTO ppopDTO){
        log.info("学生确认支付积分订单,{}",ppopDTO);
        PointsOrderPaymentVO pointsOrderPaymentVO = pointsService.paymentPointsOrder(ppopDTO);
        if (pointsOrderPaymentVO == null) {
            throw new BaseException("异常处理，返回信息为null，请联系管理员");
        }
        return Result.success(pointsOrderPaymentVO);
    }

//    @ApiOperation("用于测试回调后，对数据的修改")
//    @PostMapping("/fortest")
//    public Result TestForPaysuccess(@RequestBody PointsPurchaseOrderPaymentDTO ppopDTO){
//        log.info("测试paysuccess函数");
//        pointsService.paySuccess(ppopDTO.getOrderNumber());
//        return Result.success();
//    }

    @ApiOperation("学生兑换评价")
    @PostMapping("/exchangerating")
    public Result exchangeRating(@RequestBody RatingExchangeDTO ratingExchangeDTO){
        log.info("学生兑换评价:{}",ratingExchangeDTO);
        pointsService.exchangeRating(ratingExchangeDTO);
        return Result.success();
    }
}
