package com.ybau.transaction.controller;

import com.ybau.transaction.service.ProcessGroupService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/processGroup")
public class ProcessGroupController {

    @Autowired
    private ProcessGroupService processGroupService;


    /**
     * 根据公司ID查询所有流程组
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String,Object> map){
        return processGroupService.findAll(map);
    }


    /**
     * 切换公司目前所使用的流程组
     * @param organizationId  公司ID
     * @param processGroupId   流程组ID
     * @return
     */
    @GetMapping("/saveOrganization")
    public ResponseData saveOrganization(Integer organizationId,Integer processGroupId){
        return processGroupService.saveOrganization(organizationId,processGroupId);
    }

    /**
     * 根据流程组ID修改流程组名字
     * @param processGroupId 流程组ID
     * @param processGroupName 流程组名字
     * @return
     */
    @GetMapping("/updateName")
    public ResponseData updateName(Integer processGroupId,String processGroupName){
        return processGroupService.updateName(processGroupId,processGroupName);
    }

}
