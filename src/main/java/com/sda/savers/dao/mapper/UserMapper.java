package com.sda.savers.dao.mapper;

import com.sda.savers.model.entity.UserEntity;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Allen on 2017/8/25.
 */
@Repository
public interface UserMapper {

    @Select("SELECT * FROM user WHERE user_id=#{userId}")
    @Results({
            @Result(property = "userId",  column = "user_id"),
            @Result(property = "idNumber", column = "id_number"),
    })
    List<UserEntity> select(String userId);

}
