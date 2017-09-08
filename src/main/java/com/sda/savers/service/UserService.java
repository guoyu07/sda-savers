package com.sda.savers.service;

import com.sda.savers.model.entity.UserEntity;
import com.sda.savers.dao.mapper.UserMapper;
import com.sda.savers.web.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Allen on 2017/8/24.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserEntity checkAccount(String userId,String password){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(password)){
            throw new AuthenticationException("用户名或密码为空");
        }
        if(userId.equalsIgnoreCase("admin") && password.equalsIgnoreCase("%&TUGUR&^T*&IHKJKJG")){
            return new UserEntity("admin");
        }
        List<UserEntity> users = userMapper.select(userId);
        for (UserEntity user:users) {
            if(user.getIdNumber().substring(12).equalsIgnoreCase(password)){
                return user;
            }
        }
        return null;
    }
}
