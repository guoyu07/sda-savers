package com.sda.savers.service;

import com.sda.savers.model.entity.ProductEntity;
import com.sda.savers.framework.util.GuidGeneratorUtil;
import com.sda.savers.dao.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2017/8/24.
 */
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<ProductEntity> getAll(){
        return productMapper.getAll();
    }

    public List<ProductEntity> getAllAvailable(){
        return productMapper.getAllAvailable();
    }


    public void add(ProductEntity productEntity){
        productEntity.setGuid(GuidGeneratorUtil.newGuid());
        productEntity.setProId(productEntity.getGuid());
        productMapper.insert(productEntity);
    }

    public void save(ProductEntity productEntity){
        productMapper.update(productEntity);
    }
}
