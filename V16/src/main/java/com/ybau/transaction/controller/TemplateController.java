package com.ybau.transaction.controller;

import com.ybau.transaction.service.TemplateService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    /**
     * 插入模板名称 和路径
     * @param map
     * @return
     */
    @PostMapping ("/saveTemplate")
    public ResponseData saveTemplate(@RequestBody Map<String,Object> map ){
        return templateService.saveTemplate(map);
    }

    /**
     * 查询模板
     * @return
     */
    @GetMapping("/findAll")
    public ResponseData findAll(){
        return templateService.findAll();
    }

}
