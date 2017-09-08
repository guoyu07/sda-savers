package com.sda.savers.web.controller;

import com.sda.savers.framework.util.GuidGeneratorUtil;
import com.sda.savers.model.dto.OrderDto;
import com.sda.savers.model.entity.ProductEntity;
import com.sda.savers.framework.common.RestResult;
import com.sda.savers.service.OrderService;
import com.sda.savers.service.ProductService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2017/8/24.
 */
@Controller
@RequestMapping("/maintain")
public class MaintainController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public ModelAndView maintain(){
        ModelAndView mv = new ModelAndView("maintain");
        List<ProductEntity> productList = productService.getAll();
        Map<String,ProductEntity> productMap = new HashMap<>();
        for (ProductEntity product:productList) {
            productMap.put(product.getGuid(),product);
        }
        mv.addObject("productList",productList);
        mv.addObject("productMap",productMap);
        return mv;
    }

    @PostMapping("/addProduct")
    @ResponseBody
    public RestResult addProduct(ProductEntity productEntity){
        productEntity.setGuid(GuidGeneratorUtil.newGuid());
        productEntity.setProId(productEntity.getGuid());
        productService.add(productEntity);
        return new RestResult(true,"添加产品成功",null,null);
    }

    @PostMapping("/saveProduct")
    @ResponseBody
    public RestResult saveProduct(ProductEntity productEntity){
        productEntity.setProId(productEntity.getGuid());
        productService.save(productEntity);
        return new RestResult(true,"更新成功",null,null);
    }

    @GetMapping("/excel")
    public void createExcel(HttpServletResponse response){
        List<OrderDto> list= orderService.getAll();//此为我要导出的数据，这里写你的要导出数据list
        HashMap<String,Integer> productMap = list.get(0).getProductMap();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("股份工会2017“中秋”慰问品领取统计表");
        for(int i=0; i< productMap.size()+8; i++){
            sheet.setColumnWidth((short) i, (short) 4300);
        }
        HSSFRow row = sheet.createRow((int) 0);
        row.setHeightInPoints(20);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font=wb.createFont();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//居左
        int cellIndex=0;
        HSSFCell cell = row.createCell((short) cellIndex++);
        cell.setCellValue("员工编号");
        cell = row.createCell((short) cellIndex++);
        cell.setCellValue("姓名");
        cell = row.createCell((short) cellIndex++);
        cell.setCellValue("身份证号");
        cell = row.createCell((short) cellIndex++);
        cell.setCellValue("所在部门");
        cell = row.createCell((short) cellIndex++);
        cell.setCellValue("所属单位");
        for (String key:productMap.keySet()) {
            cell = row.createCell((short) cellIndex++);
            cell.setCellValue(key);
        }
        cell = row.createCell((short) cellIndex++);
        cell.setCellValue("领取地点");
        cell = row.createCell((short) cellIndex++);
        cell.setCellValue("领取时间");
        cell = row.createCell((short) cellIndex);
        cell.setCellValue("物品总价");
        for(int i=0;i<cell.getColumnIndex()+1;i++){
            cell.getRow().getCell(i).setCellStyle(style);
        }
        for(int i=0;i<list.size();i++){
            cellIndex=0;
            wb.createCellStyle();
            row = sheet.createRow((int) i + 1);
            row.setHeightInPoints(20);
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getUserId());
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getUserName());
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getIdNumber());
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getDepartment());
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getCompany());
            for (String key:productMap.keySet()) {
                row.createCell((short) cellIndex++).setCellValue(list.get(i).getProductMap().get(key));
            }
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getTakePlace());
            row.createCell((short) cellIndex++).setCellValue(list.get(i).getTakeTime());
            row.createCell((short) cellIndex).setCellValue(list.get(i).getTotalPrice());
        }
        try
        {   //输出Excel文件
            OutputStream output=response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=gift_statistics.xls");
            response.setContentType("application/msexcel");
            wb.write(output);
            output.close();
            wb.close();
        }catch (Exception e)  {
            e.printStackTrace();
        }
    }

}
