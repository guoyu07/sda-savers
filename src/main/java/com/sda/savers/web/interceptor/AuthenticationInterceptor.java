package com.sda.savers.web.interceptor;

import com.sda.savers.config.GiftConfig;
import com.sda.savers.framework.cache.Cache;
import com.sda.savers.model.entity.UserEntity;
import com.sda.savers.framework.cache.CacheManager;
import com.sda.savers.framework.util.CookieUtil;
import com.sda.savers.framework.util.JwtUtil;
import com.sda.savers.web.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Allen on 2017/8/25.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private GiftConfig giftConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if(request.getServletPath().toLowerCase().contains("login")){
            return true;
        }
        String jwtToken = CookieUtil.getCookieValue(request,"accessToken");
        String requestType = request.getHeader("X-Requested-With");
        if(StringUtils.isEmpty(jwtToken)){
            if(requestType!=null && requestType.equalsIgnoreCase("XMLHttpRequest")){
                response.addHeader("ACCESS","1");
            }else{
                response.sendRedirect("/login/");
            }
            return false;
        }
        checkClickNum(jwtToken);//限制请求次数
        UserEntity user = JwtUtil.unsign(jwtToken,UserEntity.class);
        if(null == user || StringUtils.isEmpty(user.getUserId())){
            response.sendRedirect("/login/");
            return false;
        }
        CacheManager.putCacheInfo(jwtToken,user,1000*60*30);
        if(user.getUserId().equalsIgnoreCase("admin")){
            if(request.getServletPath().toLowerCase().contains("/maintain/")){
                return true;
            }else{
                response.sendRedirect("/login/");
                return false;
            }
        }else if(StringUtils.isEmpty(user.getUserId())){
            response.sendRedirect("/login/");
            return false;
        }else{
            if(request.getServletPath().toLowerCase().contains("/product/")){
                return true;
            }else{
                response.sendRedirect("/login/");
                return false;
            }
        }
    }

    private void checkClickNum(String jwtToken) {
        String clickStr = "request"+jwtToken;
        Cache clickNumCache = CacheManager.getCacheInfo(clickStr);
        int clickNum=1;
        if(clickNumCache==null){
            CacheManager.putCacheInfo(clickStr,clickNum,1000*60*10);
        }else{
            clickNum = (int)clickNumCache.getValue();
            clickNum++;
        }
        if(clickNum < giftConfig.getRequestNum()){
            CacheManager.putCacheInfo(clickStr,clickNum,1000*60*10);
        }else{
            throw new AuthenticationException("请求过于频繁！");
        }
    }
}
