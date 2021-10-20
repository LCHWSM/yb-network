package com.ybau.transaction.controller;

import com.ybau.transaction.service.ProductService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 商品web层
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    //TODO:查询公共商品时限制权限查看商品

    @Autowired
    ProductService productService;

    /**
     * 添加商品
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveProduct")
    public ResponseData saveProduct(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return productService.saveProduct(map, request);
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteProduct")
    public ResponseData deleteProduct(int id) {
        return productService.deleteProduct(id);
    }

    /**
     * 修改商品
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateProduct")
    public ResponseData updateProduct(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return productService.updateProduct(map, request);
    }

    /**
     * 查询所有（商品管理查询 条件 分页）
     *
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map) {
        return productService.findAll(map);
    }

    /**
     * 根据ID查询商品
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public ResponseData findById(int id) {
        return productService.findById(id);
    }

    /**
     * 查询所有商品（下单商品展示 分类查询）
     *
     * @return
     */
    @PostMapping("/findProduct")
    public ResponseData findProduct(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return productService.findProduct(map,request);
    }

    /**
     * 更新库存
     *
     * @return
     */
    @PostMapping("/updateWarehouse")
    public ResponseData updateWarehouse(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return productService.updateWarehouse(map, request);
    }


    /**
     * 详情页图片路径替换
     * @param oldUrl
     * @param newUrl
     * @return
     */
    @GetMapping("/updateEditor")
    public ResponseData updateEditor(@RequestParam String oldUrl, @RequestParam String newUrl) {
        return productService.updateEditor(oldUrl, newUrl);
    }

    /**
     * 查询商品库存
     *
     * @param map
     * @return
     */
    @PostMapping("/findInventory")
    public ResponseData findInventory(@RequestBody Map<String, Object> map) {
        return productService.findAll(map);
    }

}
