package com.sda.savers.web.controller;

import com.sda.savers.config.GiftConfig;
import com.sda.savers.framework.cache.Cache;
import com.sda.savers.model.entity.OrderEntity;
import com.sda.savers.model.entity.ProductEntity;
import com.sda.savers.model.entity.UserEntity;
import com.sda.savers.web.exception.AuthenticationException;
import com.sda.savers.web.exception.PriceException;
import com.sda.savers.framework.cache.CacheManager;
import com.sda.savers.framework.common.RestResult;
import com.sda.savers.framework.util.CookieUtil;
import com.sda.savers.framework.util.GuidGeneratorUtil;
import com.sda.savers.service.OrderService;
import com.sda.savers.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2017/8/24.
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GiftConfig giftConfig;

    @GetMapping("/")
    public ModelAndView product(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("product");
        String jwtToken = CookieUtil.getCookieValue(request,"accessToken");
        UserEntity user = (UserEntity)CacheManager.getCacheInfo(jwtToken).getValue();
        List<ProductEntity> pros = productService.getAllAvailable();
        List<OrderEntity> odrs = orderService.getOrder(user.getUserId(),giftConfig.getActivityName());
        BigDecimal totalPrice = BigDecimal.ZERO;
        if(odrs.size()>0){
            OrderEntity orderEntity = odrs.get(0);
            for (OrderEntity order:odrs) {
                ProductEntity pro = pros.stream().filter(c->c.getProId().equalsIgnoreCase(order.getProId())).findFirst().get();
                pro.setProNum(order.getProNum());
                totalPrice = totalPrice.add(pro.getProPrice().multiply(new BigDecimal(order.getProNum())));
            }
            mv.addObject("takePlace",orderEntity.getTakePlace());
            mv.addObject("takeTime",orderEntity.getTakeTime());
            mv.addObject("isChosed",true);
        }else{
            totalPrice=new BigDecimal(196);
        }
        mv.addObject("totalPrice",totalPrice);
        mv.addObject("userName",user.getName());
        mv.addObject("quota",giftConfig.getQuota());
        mv.addObject("activityName",giftConfig.getActivityName());
        mv.addObject("productList",pros);
        return mv;
    }
    @PostMapping("/chooseProduct")
    @ResponseBody
    public RestResult chooseProduct(HttpServletRequest request){
        String takePlace = request.getParameter("takePlace");
        checkPig(request, takePlace);//校验黑猪肉只能济南，青岛，烟台，北京，上海领取
        String takeTime = request.getParameter("takeTime");
        BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice"));
        String jwtToken = CookieUtil.getCookieValue(request,"accessToken");
        checkClickNum(jwtToken);//限制点击次数
        UserEntity user = (UserEntity)CacheManager.getCacheInfo(jwtToken).getValue();
        List<ProductEntity> pros = productService.getAllAvailable();
        List<OrderEntity> odrList = new ArrayList<>();
        for (ProductEntity pro:pros) {
            int proNum = Integer.parseInt(request.getParameter(pro.getProId()));
            if(proNum > 0){
                OrderEntity orderEntity = new OrderEntity(
                        GuidGeneratorUtil.newGuid(),
                        user.getUserId(),
                        pro.getProId(),
                        pro.getProName(),
                        proNum, takePlace, takeTime, pro.getProPrice().multiply(new BigDecimal(proNum)),giftConfig.getActivityName(),user.getIdNumber(),user.getName(),user.getCompany(),user.getDepartment());
                odrList.add(orderEntity);
            }
        }
        checkPrice(odrList,totalPrice);
        orderService.saveOrder(odrList,user.getUserId(),giftConfig.getActivityName());
        return new RestResult(true,"提交成功",null,null);
    }

    private void checkPig(HttpServletRequest request, String takePlace) {
        int pigNum = Integer.parseInt(request.getParameter(giftConfig.getPigId()));
        if(pigNum>0){
            if(!giftConfig.getPigPlace().contains(takePlace)){
                throw new AuthenticationException("黑猪肉仅限济南，青岛，烟台，北京，上海领取！");
            }
        }
    }

    private void checkClickNum(String jwtToken) {
        String clickStr = "clickNum"+jwtToken;
        Cache clickNumCache = CacheManager.getCacheInfo(clickStr);
        int clickNum=1;
        if(clickNumCache==null){
            CacheManager.putCacheInfo(clickStr,clickNum,1000*60*10);
        }else{
            clickNum = (int)clickNumCache.getValue();
            clickNum++;
        }
        if(clickNum<3){
            CacheManager.putCacheInfo(clickStr,clickNum,1000*60*10);
        }else{
            throw new AuthenticationException("请隔一段时间再提交！");
        }
    }

    private void checkPrice(List<OrderEntity> orderEntities, BigDecimal totalPrice){
        BigDecimal addResult = BigDecimal.ZERO;
        for (OrderEntity odr: orderEntities) {
            addResult = addResult.add(odr.getTotalPrice());
        }
        if(addResult.compareTo(totalPrice)!=0){
            throw new PriceException("金额有误，请不要搞鬼！");
        }
        if(totalPrice.compareTo(new BigDecimal(giftConfig.getQuota()))==1){
            throw new PriceException("超出额度，请重新选择！");
        }
    }
}
