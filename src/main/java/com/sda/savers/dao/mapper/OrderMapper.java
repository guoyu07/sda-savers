package com.sda.savers.dao.mapper;

import com.sda.savers.model.entity.OrderEntity;
import com.sda.savers.dao.provider.OrderProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/8/25.
 */
@Repository
public interface OrderMapper {

    @Select("SELECT * FROM `order` WHERE user_id=#{userId} AND activity_name=#{activityName}")
    @Results({
            @Result(property = "userId",  column = "user_id"),
            @Result(property = "proName",  column = "pro_name"),
            @Result(property = "proId", column = "pro_id"),
            @Result(property = "proNum", column = "pro_num"),
            @Result(property = "takePlace", column = "take_place"),
            @Result(property = "takeTime", column = "take_time"),
            @Result(property = "totalPrice", column = "total_price"),
            @Result(property = "activityName", column = "activity_name")
    })
    List<OrderEntity> query(@Param("userId") String userId, @Param("activityName") String activityName);

    @Select("SELECT * FROM `order`")
    @Results({
            @Result(property = "userId",  column = "user_id"),
            @Result(property = "proName",  column = "pro_name"),
            @Result(property = "proId", column = "pro_id"),
            @Result(property = "proNum", column = "pro_num"),
            @Result(property = "takePlace", column = "take_place"),
            @Result(property = "takeTime", column = "take_time"),
            @Result(property = "totalPrice", column = "total_price"),
            @Result(property = "activityName", column = "activity_name"),
            @Result(property = "idNumber", column = "id_number"),
            @Result(property = "userName", column = "user_name")
    })
    List<OrderEntity> queryAll();

    @Delete("DELETE FROM `order`WHERE user_id=#{userId} AND activity_name =#{activityName};")
    void delete(@Param("userId") String userId, @Param("activityName") String activityName);

    @InsertProvider(type=OrderProvider.class,method = "insertList")
    void insertList(@Param("getAll") List<OrderEntity> list);

}
