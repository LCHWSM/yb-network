package com.ybau.transaction.interceptor;

import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.mapper.PermissionMapper;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class
JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    RedissonClient client;
    @Autowired
    PermissionMapper permissionMapper;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();//获取访问的url
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            //解析token
            Claims claims = jwtUtil.parseJWT(token);
            if (claims == null) {
                //token错误，
                returnJson(response, "{\"code\":403,\"msg\":\"登录过期，请重新登录!\"}");
                return false;
            }
            String id = (String) claims.get("id");
            String username = (String) claims.get("username");
            Integer randomNum = (Integer) claims.get("randomNum");
            RBucket<Object> bucket = client.getBucket("data_ybnetwork." + username + id + randomNum);
            if (bucket != null) {
                String to = (String) bucket.get();
                //拿到redis中存放的token
                if (token.equals(to)) {
                    bucket.set(token, 60, TimeUnit.MINUTES);
                    //放行不需要权限的接口
                    if (requestURI.equals("/main/updatePwd")
                            || requestURI.equals("/express/findAll") || requestURI.equals("/main/findAll")
                            || requestURI.equals("/main/findOtherRole") || requestURI.equals("/main/findById")
                            || requestURI.equals("/permission/findAll") || requestURI.equals("/role/findByRId")
                            || requestURI.equals("/role/findOtherByRId") || requestURI.equals("/file/upload")
                            || requestURI.equals("/product/findById") || requestURI.equals("/product/findProduct")
                            || requestURI.equals("/organization/findByUId") || requestURI.equals("/organization/findOrganization")
                            || requestURI.equals("/universal/findAll") || requestURI.equals("/order/findByAwaitOrder")
                            || requestURI.equals("/collect/saveCollect") || requestURI.equals("/collect/findCollect")
                            || requestURI.equals("/classify/findAll") || requestURI.equals("/product/updateEditor")
                            || requestURI.equals("/collect/updateCollect") || requestURI.equals("/contract/findNotContract")
                            || requestURI.equals("/file/uploadContract") || requestURI.equals("/order/findSample")
                            || requestURI.equals("/main/updateEmailByPhone") || requestURI.equals("/auditFlow/findAuditFlow")
                            || requestURI.equals("/userSite/findUserSite") || requestURI.equals("/userSite/updateUserSite")
                            || requestURI.equals("/userSite/deleteUserSite") || requestURI.equals("/userSite/findByName")
                            || requestURI.equals("/order/findInventory") || requestURI.equals("/buttonConceal/orderCenter")
                            || requestURI.equals("/buttonConceal/findCatalog") || requestURI.equals("/buttonConceal/findByUser")
                            || requestURI.equals("/buttonConceal/findByRole") || requestURI.equals("/buttonConceal/findQuota")
                            || requestURI.equals("/buttonConceal/findOrganization") || requestURI.equals("/buttonConceal/findClassify")
                            || requestURI.equals("/buttonConceal/findProduct") || requestURI.equals("/buttonConceal/findInventory")
                            || requestURI.equals("/buttonConceal/findContract") || requestURI.equals("/department/findDepartment")
                            || requestURI.equals("/grouping/findByUser") || requestURI.equals("/main/findByOId")
                            || requestURI.equals("/sponsorAudit/findAll") || requestURI.equals("/nodeDraft/findById")
                            || requestURI.equals("/audit/findAll") || requestURI.equals("/audit/findByOperation")
                            || requestURI.equals("/auditLog/findAuditLog") || requestURI.equals("/sponsorAudit/repealAudit")
                            || requestURI.equals("/buttonConceal/findFlow") || requestURI.equals("/log/findOrderByLog")
                            || requestURI.equals("/order/findByOrderId") || requestURI.equals("/main/findSaveCoLog")
                            || requestURI.equals("/log/findCoLog") || requestURI.equals("/customizedOrder/findUser")
                            || requestURI.equals("/customizedOrder/findByUserId") || requestURI.equals("/customizedOrder/findById")
                            || requestURI.equals("/invoice/findInvoice") || requestURI.equals("/invoice/deleteInvoice")
                            || requestURI.equals("/invoice/updateInvoice") || requestURI.equals("/invoice/findByUnitName")
                            || requestURI.equals("/supplier/findBySupplier") || requestURI.equals("/soRecord/findAllById")
                            || requestURI.equals("/soRecord/updateRecord") || requestURI.equals("/soRecord/deleteRecord")
                            || requestURI.equals("/main/supplierUser") || requestURI.equals("/log/findSupplierLog")
                            || requestURI.equals("/supplier/findSupplier") || requestURI.equals("/buttonConceal/findSupplierUser")
                            || requestURI.equals("/buttonConceal/findSupplier") || requestURI.equals("/template/findAll")
                            || requestURI.equals("/file/complete") || requestURI.equals("/contract/findByOrderId")
                            || requestURI.equals("userContract/findAll") || requestURI.equals("/userContract/saveContract")
                            || requestURI.equals("/userContract/cancelContract") || requestURI.equals("/userContract/updateContract")
                            || requestURI.equals("/userContract/findOrder") || requestURI.equals("/userContract/relevanceOrder")
                            || requestURI.equals("/contract/saveContract") || requestURI.equals("/contract/saveContractAll")
                            || requestURI.equals("/supplierContract/findBySrId") || requestURI.equals("/supplierContract/saveContr")
                            || requestURI.equals("/supplierContract/cancelContr") || requestURI.equals("/supplierContract/updateContr")
                            || requestURI.equals("/supplierContract/findSROrder") || requestURI.equals("/supplierContract/relevanceSROrder")
                            || requestURI.equals("/supplierContract/ifPermission") || requestURI.equals("/userContract/ifPermission")
                            || requestURI.equals("/userContract/findByContract") || requestURI.equals("/supplierContract/findContractId")
                            || requestURI.equals("/order/updateCost") || requestURI.equals("/orderInvoice/findOrderId")
                            || requestURI.equals("/orderInvoice/saveInvoiceUser") || requestURI.equals("/orderInvoice/updateInvoiceUser")
                            || requestURI.equals("/orderInvoice/cancelReleUser") || requestURI.equals("/orderInvoice/findNotByUser")
                            || requestURI.equals("/orderInvoice/relevanceInvoice") || requestURI.equals("/orderInvoice/relevanceOrder")
                            || requestURI.equals("/supplierInvoice/findOrderId") || requestURI.equals("/supplierInvoice/saveInvoiceUser")
                            || requestURI.equals("/supplierInvoice/updateInvoiceUser") || requestURI.equals("/supplierInvoice/cancelReleUser")
                            || requestURI.equals("/supplierInvoice/findNotOrderUser") || requestURI.equals("/supplierInvoice/relevanceInvoiceUser")
                            || requestURI.equals("/supplierInvoice/relevanceOrderUser") || requestURI.equals("/orderInvoice/ifUserId")
                            || requestURI.equals("/supplierInvoice/ifUserId") || requestURI.equals("/order/proposerMoney")) {
                        return true;
                    }
                    //查询用户拥有的权限
                    RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
                    String permissionStr = (String) bucket1.get();
                    if (permissionStr != null) {
                        List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
                        for (Permission permission : permissions) {
                            if (requestURI.equals(permission.getUrl())) {
                                return true;
                            }
                        }
                    }
                    returnJson(response, "{\"code\":401,\"msg\":\"您的权限不足，请联系管理员授权!\"}");
                    return false;
                }
                returnJson(response, "{\"code\":403,\"msg\":\"登录过期，请重新登录!\"}");
                return false;
            }
        }
        returnJson(response, "{\"code\":403,\"msg\":\"登录过期，请重新登录!\"}");
        return false;
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
