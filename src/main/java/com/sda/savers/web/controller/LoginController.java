package com.sda.savers.web.controller;

import com.sda.savers.framework.common.RestResult;
import com.sda.savers.model.entity.UserEntity;
import com.sda.savers.web.exception.AuthenticationException;
import com.sda.savers.framework.util.JwtUtil;
import com.sda.savers.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Allen on 2017/8/24.
 */
@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String login(){
        return "login";
    }


    @PostMapping("/doLogin")
    public void doLogin(String userId, String password, HttpServletResponse response){
        UserEntity user = userService.checkAccount(userId, password);
        if(null==user){
            throw new AuthenticationException("用户认证失败！");
        }
        String token = JwtUtil.sign(user, 30L * 24L * 3600L * 1000L);
        Cookie tokenCookie = new Cookie("accessToken", token);
        tokenCookie.setMaxAge(20*60);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
        if(userId.equalsIgnoreCase("admin")){
            response.addHeader("NEXT_PATH","1");
        }
    }

    @PostMapping("/check")
    @ResponseBody
    public RestResult doCheck(String userId, String password){
        UserEntity user = userService.checkAccount(userId, password);
        if(null==user){
            throw new AuthenticationException("用户认证失败！");
        }
        return new RestResult(true,"登录成功",null,null);
    }
}
