package com.melody.controller.student;

import com.melody.dto.OrderPaymentDTO;
import com.melody.dto.OrderSubmitDTO;
import com.melody.result.Result;
import com.melody.service.OrderService;
import com.melody.vo.OrderPaymentVO;
import com.melody.vo.OrderQueryVO;
import com.melody.vo.OrderSubmitVO;
import com.melody.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController("studentOrderController")
@RequestMapping("/student/orders")
@Api(tags = "用户端订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("学生下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO){
        log.info("学生下单:{}",orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    //TODO:全局异常处理
    public Result<OrderPaymentVO> payment(@RequestBody OrderPaymentDTO orderPaymentDTO) throws IOException {
        log.info("学生支付订单:{}",orderPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(orderPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    @ApiOperation("学生查询全部订单")
    @GetMapping("/query")
    public Result<List<OrderQueryVO>> queryOrdersByStudentId(){
        List<OrderQueryVO> list = orderService.queryOrdersByStudentId();
        return Result.success(list);
    }

    @ApiOperation("学生获取具体订单信息")
    @GetMapping("/query/{id}")
    public Result<OrderVO> queryByOrderId(@PathVariable("id") Long id){
        log.info("订单id:{}",id);
        OrderVO orderVO = orderService.queryByOrderId(id);
        return Result.success(orderVO);
    }


}
