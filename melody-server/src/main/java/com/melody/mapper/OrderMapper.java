package com.melody.mapper;

import com.melody.entity.Orders;
import com.melody.vo.OrderQueryVO;
import com.melody.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 学生下单,插入生成的订单数据(学生确认后生成订单)
     * @param orders
     */
    Long submitOrder(Orders orders);

    /**
     * 学生查询全部订单 - 通过学生ID
     * @param studentId
     * @return
     */
    @Select("SELECT id,goodsName,orderTime FROM orders where studentId = #{studentId} ")
    List<OrderQueryVO> queryOrdersByStudentId(Long studentId);

    /**
     * 学生获取具体订单信息
     * @param id
     * @return
     */
    @Select("SELECT id,orderNumber,studentId,goodsName,status,orderTime,checkoutTime,payMethod,payStatus,amount,cancelTime FROM orders where id = #{id}")
    OrderVO queryByOrderId(Long id);


    /**
     * 在班级作业订单表添加绑定
     * @param classHomeworkId 班级作业ID
     * @param ordersId 订单ID
     */
    @Insert("INSERT into homework_orders (homeworkId,ordersId) values (#{classHomeworkId},#{ordersId}) ")
    void appendHomeworkOrders(Long classHomeworkId,Long ordersId);

    /**
     * 修改订单相关属性
     * @param orders
     */
    void update(Orders orders);
}
