package com.ybau.transaction.util;

import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.OrganizationMapper;
import com.ybau.transaction.mapper.PermissionMapper;
import com.ybau.transaction.mapper.UserMapper;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PermissionUtil {

    @Autowired
    RedissonClient client;
//    @Autowired
//    OrganizationMapper organizationMapper;
//    @Autowired
//    PermissionMapper permissionMapper;
//
//    @Value("${Company.Order}")
//    private String CompanyOrder;
//
//    @Autowired
//    UserMapper userMapper;
//
//    public Map ifPermission(Map<String, Object> map, String id, int organizationId) {
//        //查询用户所有的权限
//        List<Permission> permissions = permissionMapper.findByUserId(id);
//        for (Permission permission : permissions) {
//            if (permission.getUrl().equals("seeAllOrder")) {
//                //如果有发货权限权限Break掉把addUser,addCompany置空结束循环
//                map.put("addUser", null);
//                map.put("addCompany", null);
//                return map;
//            }
//            if (CompanyOrder.equals(permission.getUrl())) {
//                //有订单管理员权限，把addCompany赋值为公司名字addUser置空
//                for (Permission permission1 : permissions) {
//                    //如果有订单管理员的权限，继续循环，查看是否有查看所有订单的权限，如果有则返回全部，如果没有则返回该公司订单信息
//                    if (permission1.getUrl().equals("seeAllOrder")) {
//                        map.put("addUser", null);
//                        map.put("addCompany", null);
//                        return map;
//                    }
//                }
//                map.put("addCompany", organizationId);
//                map.put("addUser", null);
//                map.put("organizationName", null);
//                //公司订单管理员权限，只可查看本公司订单公司筛选条件置空
//                return map;
//            }
//        }
//        //上述权限都没有只能查看自己订单，传入用户名，用户名/公司筛选条件置空
//        map.put("addUser", id);
//        map.put("name",null);
//        map.put("organizationName", null);
//        return map;
//    }

    public String findByShow(String id) {
        String name = null;
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出每个下单用户的权限
        String permissionStr = (String) bucket1.get();
        List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
        for (Permission permission : permissions) {
            switch (permission.getUrl()) {
                case "externalShow":
                    name = name + "1";
                    break;
                case "externalNoShow":
                    name = name + "2";
                    break;
            }
        }
        return name;
    }

    /**
     * 查询用户是否有新增合同的权限
     * @param id
     * @return
     */
    public boolean findBySaveContract(String id){
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出每个下单用户的权限
        String permissionStr = (String) bucket1.get();
        List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
        for (Permission permission : permissions) {
            if (permission.getUrl().equals("saveContract")){
                return true;
            }
        }
        return false;
    }
}
