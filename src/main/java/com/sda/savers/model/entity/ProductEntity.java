package com.sda.savers.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Allen on 2017/8/24.
 */
@Data
public class ProductEntity {
    private String guid;
    private String proName;
    private String proId;
    private String proUrl;
    private String proDescription;
    private BigDecimal proPrice;
    private int proNum;
    private int available;
}
