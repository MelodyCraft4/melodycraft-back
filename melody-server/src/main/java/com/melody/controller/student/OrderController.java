package com.melody.controller.student;

import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.result.Result;
import com.melody.service.OrderService;
import com.melody.vo.OrderPaymentVO;
import com.melody.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学生下单控制层
 */
@Slf4j
@RestController("studentOrderController")
@RequestMapping("/student/order/")
@Api(tags = "学生用户端(订单)")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 学生下单
     * @param ordersSubmitDTO 选择的下单方式以及金额
     * @return 返回前端的订单信息(id,订单号,金额,时间)
     */
    @PostMapping("/submit")
    @ApiOperation("学生下单")
    public Result<OrderSubmitVO> sumbitOrder(@RequestBody OrderSubmitDTO ordersSubmitDTO){
        log.info("学生下单,付款方式与总金额:{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param orderPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrderPaymentDTO orderPaymentDTO) throws Exception {
        log.info("订单支付：{}", orderPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(orderPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentDTO);
        return Result.success(orderPaymentVO);
    }
}
