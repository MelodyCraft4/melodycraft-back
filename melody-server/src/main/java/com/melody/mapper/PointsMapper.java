package com.melody.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointsMapper {
    /**
     *  查询获取当前剩余积分点数
     * @return 剩余积分点
     */
    @Select("SELECT points FROM PointsDepot WHERE studentId = #{studentId}")
    Long queryLeftoverPointsById(Long studentId);

    /**
     * 将学生添加进积分表
     * @param studentId
     */
    @Insert("INSERT INTO PointsDepot (studentid) values (#{studentId}) ")
    void addStudentToPointsDepot(Long studentId);

    /**
     *  根据学生id获取积分表id
     * @param studentId 学生id
     * @return 积分表id
     */
    @Select("SELECT id FROM  PointsDepot WHERE studentId = #{studentId}")
    Long queryIdBystudentId(Long studentId);

    /**
     * 绑定订单与积分表
     * @param ordersId 订单表id
     * @param pointsdepotId 积分表id
     * @param points 订单所购买的积分
     */
    @Insert("INSERT INTO " +
            "points_order (ordersId, pointsdepotId, points) " +
            "VALUES (#{ordersId},#{pointsdepotId},#{points})")
    void addOrderToPoints(Long ordersId,Long pointsdepotId,Long points);

    /**
     *  根据订单，给相应学生增加积分
     * @param id 积分表主键id
     * @param points 增加的分数
     */
    void IncreasePoints(Long id,@Param("pointsToAdd") Long points);


    /**
     *  根据id，给相应学生减少积分
     * @param id 订单表id
     * @param points 减少的分数
     */
    void SubtractPoints(Long id,@Param("pointsTominus")Long points);


    /**
     * 根据订单id获取积分表id
     * @param ordersId 订单id
     * @return 积分表对应的id
     */
    @Select("SELECT pointsdepotId FROM points_order WHERE ordersId = #{ordersId}")
    Long getPointsdepotIdByordersId(Long ordersId);



}
