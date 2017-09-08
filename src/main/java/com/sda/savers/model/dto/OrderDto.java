package com.sda.savers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * Created by Allen on 2017/9/5.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String idNumber;//员工身份证号
    private String userName;//姓名
    private String userId;//员工编号
    private String department;//所属部门
    private String company;//所属单位
    private HashMap<String,Integer> productMap;//产品名称数量map
    private String takePlace;//领取地点
    private String takeTime;//领取时间
    private String totalPrice;//物品总价
}
