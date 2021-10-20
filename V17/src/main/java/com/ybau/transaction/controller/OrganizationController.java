package com.ybau.transaction.controller;

import com.ybau.transaction.service.OrganizationService;
import com.ybau.transaction.util.ResponseData;
import org.apache.http.protocol.ResponseDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 机构控制层
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    /**
     * 查询所有机构（筛选，分页)
     *
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map) {
        return organizationService.findAll(map);
    }

    /**
     * 根据用户ID查询公司信息
     *
     * @param request
     * @return
     */
    @GetMapping("/findByUId")
    public ResponseData findByUId(HttpServletRequest request) {
        return organizationService.findByUId(request);
    }

    /**
     * 新增公司
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveOrganization")
    public ResponseData saveOrganization(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return organizationService.saveOrganization(map, request);
    }

    /**
     * 根据ID删除公司
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public ResponseData deleteById(int id, String name) {
        return organizationService.deleteById(id, name);
    }

    /**
     * 修改公司信息
     *
     * @param name
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/updateById")
    public ResponseData updateOrganization(String name, HttpServletRequest request, int id, Integer settingTime, String way) {
        return organizationService.updateOrganization(name, request, id, settingTime, way);
    }

    /**
     * 查询所有公司（未分页）
     *
     * @return
     */
    @GetMapping("/findOrganization")
    public ResponseData findOrganization() {
        return organizationService.findOrganization();
    }

    /**
     * 更新额度
     *
     * @return
     */
    @PostMapping("/updateLimit")
    public ResponseData updateLimit(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return organizationService.updateLimit(map, request);
    }

    /**
     * 核销额度
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateUsable")
    public ResponseData updateUsable(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return organizationService.updateUsable(map, request);
    }

    /**
     * 校验额度方法
     *
     * @return
     */
    @GetMapping("/verifyLimit")
    public ResponseData verifyLimit(int organizationId) {
        return organizationService.verifyLimit(organizationId);
    }

    /**
     * 直接购买订单 结算接口
     *
     * @param map
     * @return
     */
    @PostMapping("/paymentStatus")
    public ResponseData paymentStatus(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException, IllegalAccessException {
        return organizationService.paymentStatus(map, request);
    }

    /**
     *   查询公司额度  分页  （筛选）
     * @param organizationName (公司名字筛选 条件)
     * @param pageNum （分页）
     * @param pageSize （分页）
     * @return
     */
    @GetMapping("/findLimit")
    public ResponseData findLimit(String organizationName,Integer pageNum,Integer pageSize){
        return organizationService.findLimit(organizationName,pageNum,pageSize);
    }

    /**
     * 公司关联可下单的供应商
     * @param map
     * @return
     */
    @PostMapping("/saveSupplier")
    public ResponseData saveSupplier(@RequestBody Map<String,Object> map){
        return organizationService.saveSupplier(map);
    }

}
