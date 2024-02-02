package com.melody.mapper;

import com.melody.entity.Orders;
import com.melody.vo.OrderSubmitVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    /**
     * 学生下单,插入生成的订单数据(学生确认后生成订单)
     * @param orders
     */
    Long submitOrder(Orders orders);

}
