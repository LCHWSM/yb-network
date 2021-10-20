package com.ybau.transaction.controller;

import com.ybau.transaction.service.ClassifyService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 分类控制层
 */
@RestController
@RequestMapping("/classify")
public class ClassifyController {
    @Autowired
    ClassifyService classifyService;

    /**
     * 查询所有分类
     *
     * @return
     */
    @GetMapping("/findAll")
    public ResponseData findAll(int pageSize, int pageNum, String classifyName) {
        return classifyService.findAll(pageSize, pageNum, classifyName);
    }

    /**
     * 新增分类
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveClassify")
    public ResponseData saveClassify(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return classifyService.saveClassify(map, request);
    }

    /**
     * 根据ID删除分类
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public ResponseData deleteById(int id) {
        return classifyService.deleteById(id);
    }

    /**
     * 修改分类信息
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateClassify")
    public ResponseData updateClassify(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return classifyService.updateClassify(map, request);
    }

    /**
     * 更改分类排序顺序
     *
     * @return
     */
    @PostMapping("/updateSort")
    public ResponseData updateSort(@RequestBody Map<String, Object> map) {
        return classifyService.updateSort(map);
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @GetMapping("/findClassify")
    public ResponseData findClassify(int pageSize, int pageNum, String classifyName) {
        return classifyService.findAll(pageSize, pageNum, classifyName);
    }

}
