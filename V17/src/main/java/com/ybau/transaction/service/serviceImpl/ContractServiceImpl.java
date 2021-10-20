package com.ybau.transaction.service.serviceImpl;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.javafx.collections.MappingChange;
import com.sun.org.apache.regexp.internal.RE;
import com.ybau.transaction.domain.Contract;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.ContractMapper;
import com.ybau.transaction.mapper.OrderMapper;
import com.ybau.transaction.mapper.SupplierContractMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.ContractService;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
@Slf4j
public class ContractServiceImpl implements ContractService {
    @Autowired
    ContractMapper contractMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    OrderMapper orderMapper;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    @Autowired
    ImagesUtil imagesUtil;

    @Autowired
    PermissionUtil permissionUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SupplierContractMapper supplierContractMapper;


    /**
     * 查询合同列表（分页）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        PageHelper.startPage(pageNum, pageSize);
        List<Contract> contracts = contractMapper.findAll(map);//执行查询操作
        if (contracts != null && contracts.size() > 0) {
            for (int i = 0; i < contracts.size(); i++) {
                String contractSite = contracts.get(i).getContractSite();
                boolean json = JsonUtil.isJson(contractSite);
                if (json) {
                    List<Map> files = JsonUtil.jsonToList(contractSite, Map.class);
                    for (Map<String, Object> fileMap : files) {
                        String path = fileMap.get("path").toString().replace("[]", prefixUrl + bastPath + "/");
                        fileMap.put("path", path);
                    }
                    contracts.get(i).setFileMap(files);
                }
            }
        }
        PageInfo pageInfo = new PageInfo(contracts);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 插入合同信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveContract(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        if (!permissionUtil.findBySaveContract(id)) {
            return new ResponseData(401, "您的权限不足，请联系管理员授权!", null);
        }
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("contractSite", JsonUtil.listToJson(files));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("addUser", id);//存入操作人
        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        contractMapper.saveContract(map);//执行插入操作
        return new ResponseData(200, "上传成功", null);
    }

    /**
     * 根据合同ID删除该合同
     *
     * @param contractId
     * @return
     */
    @Override
    public ResponseData deleteContract(int contractId) {
        contractMapper.deleteByContractId(contractId);//执行删除操作
        orderMapper.updateContractId(contractId);//修改订单合同信息
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 查询未关联合同订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findNotContract(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        Contract contract = contractMapper.findById((Integer) map.get("contractId"));//根据合同id查询合同信息
        map.put("organizationId", contract.getOrganizationId());
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.findNotContract(map);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单关联合同
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveContractId(Map<String, Object> map) {
        int contractId = (int) map.get("contractId");
        List<String> orderIds = (List<String>) map.get("orderId");
        for (String orderId : orderIds) {
            contractMapper.saveContractId(orderId, contractId);
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 修改合同信息
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData updateContract(HttpServletRequest request, Map<String, Object> map) {
        Contract contract = contractMapper.findById((Integer) map.get("contractId"));//根据合同id查询合同信息
        if (map.get("organizationId") != null) {
            if (contract.getOrganizationId() != (Integer) map.get("organizationId")) {
                int count = contractMapper.findByOrder((Integer) map.get("contractId"));//根据合同ID查询该合同下是否存在订单
                if (count > 0) {
                    return new ResponseData(400, "该合同存在关联订单，不可修改公司", null);
                }
            }
        }
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);//根据token获取用户ID
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("contractSite", JsonUtil.listToJson(files));
        try {
            map.put("updateUser", id);
            map.put("updateTime", df.parse(df.format(new Date())));
            contractMapper.updateContract(map);
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 根据订单ID查看该订单的合同信息
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData findByOrderId(String orderId) {
        Contract contract = contractMapper.findByOrderId(orderId);
        if (contract != null) {
            boolean json = JsonUtil.isJson(contract.getContractSite());
            //判断类型是否为json
            if (json) {
                List<Map> list = JsonUtil.jsonToList(contract.getContractSite(), Map.class);
                for (Map<String, Object> fileMap : list) {
                    String path = fileMap.get("path").toString().replace("[]", prefixUrl + bastPath + "/");
                    fileMap.put("path", path);
                }
                contract.setContractSite(JsonUtil.listToJson(list));
            }
        }
        return new ResponseData(200, "查询成功", contract);
    }

    /**
     * 所有订单新增合同
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveContractAll(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        if (!permissionUtil.findBySaveContract(id)) {
            //查询用户是否有新增合同相关联信息
            return new ResponseData(401, "您的权限不足，请联系管理员授权!", null);
        }
        List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("flies");
        if (files != null && files.size() > 0) {
            for (Map<String, Object> fileMap : files) {
                String path = fileMap.get("path").toString().replace(prefixUrl + bastPath + "/", "[]");
                fileMap.put("path", path);
            }
        }
        map.put("contractSite", JsonUtil.listToJson(files));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Order order = orderMapper.findByOrderId((String) map.get("orderId"));
        map.put("organizationId", order.getAddCompany());
        map.put("addUser", id);//存入操作人
        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        contractMapper.saveContract(map);//执行插入操作
        BigInteger contractId = (BigInteger) map.get("contractId");
        contractMapper.saveContractId((String) map.get("orderId"), contractId.intValue());//修改订单合同信息
        return new ResponseData(200, "上传成功", null);
    }

    /**
     * 取消订单关联合同
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData updateConByOrderId(String orderId) {
        contractMapper.updateConByOrderId(orderId);
        return new ResponseData(200, "取消关联成功", null);
    }


}
