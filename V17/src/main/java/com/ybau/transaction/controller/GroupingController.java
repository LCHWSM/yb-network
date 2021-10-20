package com.ybau.transaction.controller;


import com.ybau.transaction.service.GroupingService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 分组WEB层
 */


@RestController
@RequestMapping("/grouping")
public class GroupingController {

    @Autowired
    GroupingService groupingService;

    /**
     * 新增部门分组
     *
     * @param map
     * @return
     */
    @PostMapping("/saveGrouping")
    public ResponseData saveGrouping(@RequestBody Map<String, Object> map) {
        return groupingService.saveGrouping(map);
    }

    /**
     * 修改分组信息
     *
     * @param map
     * @return
     */
    @PostMapping("/updateGrouping")
    public ResponseData updateGrouping(@RequestBody Map<String, Object> map) {
        return groupingService.updateGrouping(map);
    }

    /**
     * 根据分组ID删除分组
     *
     * @param groupingId
     * @return
     */
    @GetMapping("/deleteGrouping")
    public ResponseData deleteGrouping(int groupingId) {
        return groupingService.deleteGrouping(groupingId);
    }

    /**
     * 根据分组查询用户
     *
     * @return
     */
    @GetMapping("/findByUser")
    public ResponseData findByUser() {
        return groupingService.findByUser();
    }
}
