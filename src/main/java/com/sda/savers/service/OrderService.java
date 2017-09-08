package com.sda.savers.service;

import com.sda.savers.dao.mapper.OrderMapper;
import com.sda.savers.dao.mapper.ProductMapper;
import com.sda.savers.model.dto.OrderDto;
import com.sda.savers.model.entity.OrderEntity;
import com.sda.savers.model.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Allen on 2017/8/24.
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public void saveOrder(List<OrderEntity> orderEntities, String userId, String activityName){
        List<OrderEntity> oldOrders = orderMapper.query(userId,activityName);
        if(oldOrders.size()>0){
            orderMapper.delete(userId,activityName);
        }
        orderMapper.insertList(orderEntities);
    }

    public List<OrderEntity> getOrder(String userId, String activityName){
        return orderMapper.query(userId,activityName);
    }

    public List<OrderDto> getAll(){
        List orderDtoList = new ArrayList();
        List<OrderEntity> orders = orderMapper.queryAll();
        List<OrderEntity> distinctOrders = orders.stream().filter(distinctByKey(p -> p.getUserId())).collect(Collectors.toList());

        for (OrderEntity tmpOrder:distinctOrders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setUserId(tmpOrder.getUserId());
            orderDto.setIdNumber(tmpOrder.getIdNumber());
            orderDto.setUserName(tmpOrder.getUserName());
            orderDto.setCompany(tmpOrder.getCompany());
            orderDto.setDepartment(tmpOrder.getDepartment());
            orderDto.setTakePlace(tmpOrder.getTakePlace());
            orderDto.setTakeTime(tmpOrder.getTakeTime());
            BigDecimal totalPrice = BigDecimal.ZERO;
            HashMap<String,Integer> productMap = getProductMap();
            List<OrderEntity> userOrderList = orders.stream().filter(c->c.getUserId().equalsIgnoreCase(tmpOrder.getUserId())).collect(Collectors.toList());
            for (OrderEntity userOrder: userOrderList) {
                productMap.put(userOrder.getProName(),userOrder.getProNum());
                totalPrice = totalPrice.add(userOrder.getTotalPrice());
            }
            orderDto.setProductMap(productMap);
            orderDto.setTotalPrice(totalPrice.toString());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    private HashMap<String,Integer> getProductMap(){
        HashMap<String ,Integer> productMap = new HashMap<>();
        List<ProductEntity> productEntities = productMapper.getAllAvailable();
        for (ProductEntity tmpProduct : productEntities) {
            productMap.put(tmpProduct.getProName(), 0);
        }
        return productMap;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
