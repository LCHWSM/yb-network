package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Contract;
import com.ybau.transaction.domain.SupplierOrder;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.SupplierContractMapper;
import com.ybau.transaction.mapper.SupplierOrderMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.SupplierContractService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class SupplierContractServiceImpl implements SupplierContractService {


    @Autowired
    SupplierContractMapper supplierContractMapper;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SupplierOrderMapper supplierOrderMapper;

    /**
     * 根据订单ID查询相关联的合同  （用户）
     *
     * @param srId
     * @return
     */
    @Override
    public ResponseData findBySrId(String srId) {
        Contract Contract = supplierContractMapper.findBySrId(srId);
        if (Contract != null) {
            boolean json = JsonUtil.isJson(Contract.getContractSite());
            //判断类型是否为json
            if (json) {
                List<Map> list = JsonUtil.jsonToList(Contract.getContractSite(), Map.class);
                for (Map<String, Object> fileMap : list) {
                    String path = fileMap.get("path").toString().replace("[]", prefixUrl + bastPath + "/");
                    fileMap.put("path", path);
                }
                Contract.setContractSite(JsonUtil.listToJson(list));
            }
        }
        return new ResponseData(200, "查询成功", Contract);
    }

    /**
     * 新增供应商合同  （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveContr(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        SupplierOrder supplierOrder = supplierOrderMapper.findById((String) map.get("supplierOrderId"));
        if (!supplierOrder.getUserId().equals(id)) {
            return new ResponseData(400, "不可关联他人订单", null);
        }
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("supplierContractSite", JsonUtil.listToJson(files));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        User user = userMapper.findByUid(id);
        if (user == null) {
            return new ResponseData(400, "未查询到所属公司", null);
        }
        map.put("organizationId", user.getOrganizationId());
        map.put("addUser", id);//存入操作人
        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        supplierContractMapper.saveContract(map);//执行插入操作
        BigInteger contractId = (BigInteger) map.get("supplierContractId");
        supplierOrderMapper.updateContractId((String) map.get("supplierOrderId"), contractId.intValue());//修改订单合同信息
        return new ResponseData(200, "上传成功", null);
    }

    /**
     * 根据合同ID删除合同  并解除所有关联 （用户）
     *
     * @param supplierOrderId
     * @return
     */
    @Override
    public ResponseData cancelContr(String supplierOrderId, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        SupplierOrder supplierOrder = supplierOrderMapper.findById(supplierOrderId);
        if (!supplierOrder.getUserId().equals(id)) {
            return new ResponseData(400, "不可取消他人订单", null);
        }
        supplierOrderMapper.deleteOrderId(supplierOrderId);//解除订单与合同的关联
        return new ResponseData(200, "取消关联成功", null);
    }

    /**
     * 修改合同订单 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateContr(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));//根据token获取用户ID
        Contract contract = supplierContractMapper.findById((int) map.get("supplierContractId"));
        if (!contract.getAddUser().equals(id)) {
            return new ResponseData(400, "不可修改他人合同", null);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("supplierContractSite", JsonUtil.listToJson(files));
        try {
            map.put("updateUser", id);
            map.put("updateTime", df.parse(df.format(new Date())));
            supplierOrderMapper.updateContract(map);
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 查询供应商订单为关联合同订单  （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findSROrder(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findSROrder(map, id);
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }


    /**
     * 订单关联合同
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceSROrder(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> orderIds = (List<String>) map.get("orderId");
        for (String orderId : orderIds) {
            SupplierOrder supplierOrder = supplierOrderMapper.findById(orderId);
            if (!supplierOrder.getUserId().equals(id)) {
                return new ResponseData(400, "不可关联他人订单", null);
            }
        }
        for (String orderId : orderIds) {
            supplierOrderMapper.saveContractId(orderId, (int) map.get("supplierContractId"));
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 新增供应商合同  （公司）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveContract(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("supplierContractSite", JsonUtil.listToJson(files));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SupplierOrder supplierOrder = supplierOrderMapper.findById((String) map.get("supplierOrderId"));
        map.put("organizationId", supplierOrder.getOrganizationId());
        map.put("addUser", jwtUtil.getId(request.getHeader("token")));//存入操作人
        map.put("addTime", df.parse(df.format(new Date())));
        supplierContractMapper.saveContract(map);//执行插入操作
        BigInteger contractId = (BigInteger) map.get("supplierContractId");
        supplierOrderMapper.updateContractId((String) map.get("supplierOrderId"), contractId.intValue());//更新订单关联合同信息
        return new ResponseData(200, "上传成功", null);
    }

    /**
     * 根据合同ID删除合同 （所有订单）
     *
     * @param supplierContractId
     * @return
     */
    @Override
    public ResponseData deleteContract(int supplierContractId) {
        supplierContractMapper.deleteContr(supplierContractId);//根据合同ID删除合同
        supplierOrderMapper.deleteContractId(supplierContractId);//解除订单与合同的关联
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 修改合同信息  （所有订单）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateContract(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));//根据token获取用户ID
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("supplierContractSite", JsonUtil.listToJson(files));
        try {
            map.put("updateUser", id);
            map.put("updateTime", df.parse(df.format(new Date())));
            supplierOrderMapper.updateContract(map);
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);

        }
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 查询所有本公司未关联合同订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findSROrderAll(Map<String, Object> map) {
        Contract contract = supplierContractMapper.findById((Integer) map.get("supplierContractId"));//根据合同id查询合同信息
        map.put("organizationId", contract.getOrganizationId());
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findSROrder(map, null);
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单关联合同   （所有订单）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceSRAll(Map<String, Object> map) {
        List<String> orderIds = (List<String>) map.get("orderId");
        for (String orderId : orderIds) {
            supplierOrderMapper.saveContractId(orderId, (int) map.get("supplierContractId"));
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 判断该合同是否是该用户上传的
     *
     * @param supplierContractId
     * @param request
     * @return
     */
    @Override
    public ResponseData ifPermission(int supplierContractId, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        Contract contract = supplierContractMapper.findById(supplierContractId);
        if (contract.getAddUser().equals(id)) {
            return new ResponseData(200, "查询成功", true);
        }
        return new ResponseData(200, "查询成功", false);
    }

    /**
     * 根据合同ID查询该合同关联的订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findContractId(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findContractId(map);
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据供应商订单ID解除与合同的关联
     *
     * @param supplierOrderId
     * @return
     */
    @Override
    public ResponseData updateSOId(String supplierOrderId) {
        supplierOrderMapper.deleteOrderId(supplierOrderId);//解除与合同关联
        return new ResponseData(200, "取消关联成功", null);
    }


}
