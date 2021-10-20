package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import com.ybau.transaction.domain.Contract;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.ContractMapper;
import com.ybau.transaction.mapper.OrderMapper;
import com.ybau.transaction.mapper.UserContractMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.UserContractService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class UserContractServiceImpl implements UserContractService {

    @Autowired
    UserContractMapper userContractMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ContractMapper contractMapper;

    @Autowired
    OrderMapper orderMapper;

    /**
     * 根据用户ID查询合同列表  （筛选、排序）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Contract> contractList = userContractMapper.findAll(map, id);
        if (contractList != null && contractList.size() > 0) {
            for (int i = 0; i < contractList.size(); i++) {
                String contractSite = contractList.get(i).getContractSite();
                boolean json = JsonUtil.isJson(contractSite);
                if (json) {
                    List<Map> files = JsonUtil.jsonToList(contractSite, Map.class);
                    for (Map<String, Object> fileMap : files) {
                        String path = fileMap.get("path").toString().replace("[]", prefixUrl + bastPath + "/");
                        fileMap.put("path", path);
                    }
                    contractList.get(i).setFileMap(files);
                }
            }
        }
        PageInfo pageInfo = new PageInfo(contractList);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 新增用户合同
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveContract(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        Order order = orderMapper.findOrderId((String) map.get("orderId"));
        if (!order.getAddUser().equals(id)) {
            return new ResponseData(400, "不可关联他人订单", null);
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
        contractMapper.saveContract(map);//执行插入操作
        BigInteger contractId = (BigInteger) map.get("contractId");
        contractMapper.saveContractId((String) map.get("orderId"), contractId.intValue());//修改订单合同信息
        return new ResponseData(200, "添加成功", null);

    }

    /**
     * 根据订单ID删除合同信息
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData cancelContract(String orderId, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        Order order = orderMapper.findOrderId(orderId);
        if (!order.getAddUser().equals(id)) {
            return new ResponseData(400, "不可取消他人订单", null);
        }
        orderMapper.updateContract(orderId);//修改订单合同信息
        return new ResponseData(200, "取消关联成功", null);
    }

    /**
     * 根据合同ID修改合同信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateContract(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));//根据token获取用户ID
        Contract contract = contractMapper.findById((int) map.get("contractId"));
        if (!contract.getAddUser().equals(id)){
            return new ResponseData(400,"不可修改他人合同",null);
        }
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
     * 根据用户ID查询用户所下订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findOrder(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findOrderByUser(map, id);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 合同关联订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceOrder(Map<String, Object> map,HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> orderIds = (List<String>) map.get("orderId");
        for (String orderId : orderIds) {
            Order order = orderMapper.findOrderId(orderId);
            if (!order.getAddUser().equals(id)) {
                return new ResponseData(400, "不可关联他人订单", null);
            }
            contractMapper.saveContractId(orderId, (int) map.get("contractId"));
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 判断该合同是否是该用户上传的
     *
     * @param contractId
     * @param request
     * @return
     */
    @Override
    public ResponseData ifPermission(int contractId, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        Contract contract = contractMapper.findById(contractId);
        if (contract.getAddUser().equals(id)) {
            return new ResponseData(200, "查询成功", true);
        }
        return new ResponseData(200, "查询成功", false);

    }

    /**
     * 根据合同ID 查询该合同关联的订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByContract(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findIdByOrder(map);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }


}
