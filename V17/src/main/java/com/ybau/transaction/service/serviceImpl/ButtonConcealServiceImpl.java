package com.ybau.transaction.service.serviceImpl;

import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.mapper.PermissionMapper;
import com.ybau.transaction.service.ButtonConcealService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Transactional
public class ButtonConcealServiceImpl implements ButtonConcealService {

    @Autowired
    RedissonClient client;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * 判断订单中心按钮是否展示
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData orderCenter(HttpServletRequest request) {
        String token = request.getHeader("token");
        List<String> list = new ArrayList<>();
        String id = jwtUtil.getId(token);
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/express/updateExpress":
                        list.add("修改快递信息");
                        break;
                    case "/organization/borrowSample":
                        list.add("借样归还");
                        break;
                    case "/order/findByAudit":
                        list.add("查询已审核待发货订单");
                        break;
                    case "/order/findOrder":
                        list.add("查询已审核待结算订单");
                        break;
                    case "/order/cancelOrder":
                        list.add("取消订单");
                        break;
                    case "/order/findCancelOrder":
                        list.add("查询已取消订单");
                        break;
                    case "/order/findCompanyOrder":
                        list.add("查询本公司订单");
                        break;
                    case "/order/findAll":
                        list.add("查询所有订单");
                        break;
                    case "/order/findShipments":
                        list.add("查询已发货订单");
                        break;
                    case "/order/findBorrow":
                        list.add("查询借样订单");
                        break;
                    case "/order/saveOrder":
                        list.add("下单");
                        break;
                    case "/customizedOrder/findAll":
                        list.add("查询定制订单");
                        break;
                    case "/order/refund":
                        list.add("完成退款");
                        break;
                    case "/order/updateOrder":
                        list.add("修改订单信息");
                        break;
                    case "/express/refundCargo":
                        list.add("填写退货信息");
                        break;
                    case "/express/updateRefundCargo":
                        list.add("修改退货信息");
                        break;
                    case "/express/refundCargoSucceed":
                        list.add("完成退货");
                        break;
                    case "/customizedOrder/updateFlag":
                        list.add("新增反馈客户记录");
                        break;
                    case "/log/saveCoLog":
                        list.add("内部反馈记录");
                        list.add("我跟进的定制订单");
                        break;
                    case "/customizedOrder/updateOrder":
                        list.add("定制订单修改");
                        break;
                    case "generalOrder":
                        list.add("常规订单");
                        break;
                    case "/supplierOrder/saveOrder":
                        list.add("向供应商下单");
                        break;
                    case "/supplierOrder/findUIdOrder":
                        list.add("我的供应商订单");
                        break;
                    case "/supplierOrder/updateOrderById":
                        list.add("修改供应商订单");
                        break;
                    case "/supplierOrder/findBySupplier":
                        list.add("供应商订单");
                        break;
                    case "/supplierOrder/updateQuotation":
                        list.add("修改报价");
                        break;
                    case "/supplierOrder/updateDispose":
                        list.add("供应商反馈");
                        break;
                    case "/supplierOrder/closeSum":
                        list.add("供应商订单结算");
                        break;
                    case "/supplierOrder/updateAmount":
                        list.add("修改已结算金额");
                        break;
                    case "/supplierOrder/findByOrganId":
                        list.add("本公司供应商订单");
                        break;
                    case "/supplierOrder/companyCloseSum":
                        list.add("公司订单结算");
                        break;
                    case "/supplierOrder/findAll":
                        list.add("所有供应商订单");
                        break;
                    case "/file/doImportExcel":
                        list.add("导入订单");
                        break;
                    case "/order/deleteOrder":
                        list.add("删除订单");
                        break;
                    case "/log/findLeadOrder":
                        list.add("用户导入记录");
                        break;
                    case "/log/findLeadOrderAll":
                        list.add("所有导入记录");
                        break;
                    case "/order/deleteLeadOrder":
                        list.add("删除导入订单");
                        break;
                    case "/file/complete":
                        list.add("导出订单");
                        break;
                    case "saveContract":
                        list.add("1");//合同上传
                        break;
                    case "/contract/updateContract":
                        list.add("2");//修改合同信息
                        break;
                    case "/contract/deleteContract":
                        list.add("3");//删除合同信息
                        break;
                    case "/order/findByOrder":
                        list.add("4");//订单关联合同
                        break;
                    case "/supplierContract/saveContract":
                        list.add("5");//新增合同（供应商）
                        break;
                    case "/supplierContract/deleteContract":
                        list.add("7");//删除合同（供应商）
                        break;
                    case "/supplierContract/updateContract":
                        list.add("6");//修改合同（供应商）
                        break;
                    case "/supplierContract/findByCId":
                        list.add("8");//关联合同   （供应商）
                        break;
                    case "/contract/updateConByOrderId":
                        list.add("9");//取消订单关联合同
                        break;
                    case "/contract/saveContractId":
                        list.add("11");//新增关联
                        break;
                    case "/supplierContract/updateSOId":
                        list.add("12");//解除订单关联合同 （供应商）
                        break;
                    case "/supplierContract/relevanceSRAll":
                        list.add("13");//新增关联  （供应商）
                        break;
                    case "/orderInvoice/saveInvoice":
                        list.add("14");//订单中心-新增发票附件（所有订单）
                        break;
                    case "/orderInvoice/updateInvoice":
                        list.add("15");//订单中心-修改发票附件（所有订单）
                        break;
                    case "/orderInvoice/cancelRelevance":
                        list.add("16");//订单中心-取消订单与发票附件关联（所有订单）
                        break;
                    case "/orderInvoice/findNotOrder":
                        list.add("17");//订单中心-查询未关联发票附件订单（所有订单）
                        break;
                    case "/orderInvoice/relevanceInvoiceId":
                        list.add("18");//订单中心-查询已关联发票附件订单（所有订单）
                        break;
                    case "/orderInvoice/relevanceOrderId":
                        list.add("19");//订单中心-订单关联发票附件（所有订单）
                        break;
                    case "/orderInvoice/deleteInvoice":
                        list.add("20");//订单中心-删除发票附件（所有订单）
                        break;
                    case "/supplierInvoice/saveInvoice":
                        list.add("21");//供应商-新增发票附件（所有订单）
                        break;
                    case "/supplierInvoice/updateInvoice":
                        list.add("22");//供应商-修改发票附件（所有订单）
                        break;
                    case "/supplierInvoice/cancelRelevance":
                        list.add("23");//供应商-取消订单与发票附件关联（所有订单）
                        break;
                    case "/supplierInvoice/findNotOrder":
                        list.add("24");//供应商-查询未关联发票附件订单（所有订单）
                        break;
                    case "/supplierInvoice/relevanceInvoiceId":
                        list.add("25");//供应商-查询已关联发票附件订单（所有订单）
                        break;
                    case "/supplierInvoice/relevanceOrderId":
                        list.add("26");//供应商-订单关联发票附件（所有订单）
                        break;
                    case "/supplierInvoice/deleteInvoice":
                        list.add("27");//供应商-删除发票附件（所有订单）
                        break;
                    case "/order/turnSell":
                        list.add("28");//借样转销售订单
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 判断目录是否展示
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findCatalog(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> list = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/main/findByUser":
                        list.add("用户管理");
                        break;
                    case "/role/findAll":
                        list.add("角色管理");
                        break;
                    case "/organization/findAll":
                        list.add("公司管理");
                        break;
                    case "/product/findAll":
                        list.add("商品列表");
                        break;
                    case "/product/findInventory":
                        list.add("库存管理");
                        break;
                    case "/organization/findLimit":
                        list.add("公司额度管理");
                        break;
                    case "/classify/findClassify":
                        list.add("商品分类管理");
                        break;
                    case "/contract/findAll":
                        list.add("合同管理");
                        break;
                    case "/universal/updateWarning":
                        list.add("库存预警调整");
                        break;
                    case "/processGroup/findAll":
                        list.add("流程管理");
                        break;
                    case "/supplier/findAll":
                        list.add("供应商管理");
                        break;
                }
            }

        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 查看是否有修改 新增用户权限
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findByUser(HttpServletRequest request) {
        String id = jwtUtil.getId( request.getHeader("token"));
        List<String> list = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/main/saveUser":
                        list.add("新增用户");
                        break;
                    case "/main/findByRole":
                        list.add("查看角色");
                        break;
                    case "/main/updateUser":
                        list.add("修改");
                        break;
                    case "/role/updateRoleByUId":
                        list.add("角色修改");
                        break;
                    case "/supplierUser/supplierByUser":
                        list.add("查询供应商用户");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 查询是否有修改角色权限
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findByRole(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> list = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/role/saveRole":
                        list.add("新增角色");
                        break;
                    case "/role/updateRole":
                        list.add("修改角色");
                        break;
                    case "/role/deleteRoleById":
                        list.add("删除角色");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 额度相关按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findQuota(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> list = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/organization/updateLimit":
                        list.add("额度调整");
                        break;
                    case "/organization/verifyLimit":
                        list.add("校正额度");
                        break;
                    case "/organization/updateUsable":
                        list.add("额度核销");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }


    /**
     * 公司相关按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findOrganization(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> list = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/organization/saveOrganization":
                        list.add("新增公司");
                        break;
                    case "/organization/updateById":
                        list.add("修改公司");
                        break;
                    case "/organization/deleteById":
                        list.add("删除公司");
                        break;
                    case "/department/findByOId":
                        list.add("所有部门");
                        break;
                    case "/organization/saveSupplier":
                        list.add("配置供应商");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 分类相关按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findClassify(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> list = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/classify/saveClassify":
                        list.add("新增分类");
                        break;
                    case "/classify/updateClassify":
                        list.add("修改分类");
                        break;
                    case "/classify/deleteById":
                        list.add("删除分类");
                        break;
                    case "/classify/updateSort":
                        list.add("分类排序");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 商品按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findProduct(HttpServletRequest request) {
        String id = jwtUtil.getId( request.getHeader("token"));
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);
        List<String> list = new ArrayList<>();
        String permissionStr = (String) bucket.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/product/saveProduct":
                        list.add("新增商品");
                        break;
                    case "/product/updateProduct":
                        list.add("修改商品");
                        break;
                    case "/product/deleteProduct":
                        list.add("删除商品");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 库存按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findInventory(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);
        List<String> list = new ArrayList<>();
        String permissionStr = (String) bucket.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/product/updateWarehouse":
                        list.add("库存调整");
                        break;
                    case "/universal/updateWarning":
                        list.add("库存预警设置");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 合同相关按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findContract(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);
        List<String> list = new ArrayList<>();
        String permissionStr = (String) bucket.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "saveContract":
                        list.add("合同上传");
                        break;
                    case "/contract/updateContract":
                        list.add("修改合同信息");
                        break;
                    case "/contract/deleteContract":
                        list.add("删除合同信息");
                        break;
                    case "/contract/saveContractId":
                        list.add("订单关联合同");
                        break;
                    case "/contract/updateConByOrderId":
                        list.add("取消订单与合同关联");
                        break;
                    case "/order/findByOrder":
                        list.add("查询合同关联订单");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 流程相关按钮控制
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findFlow(HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);
        List<String> list = new ArrayList<>();
        String permissionStr = (String) bucket.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/processGroup/saveOrganization":
                        list.add("saveOrganization");
                        break;
                    case "/nodeDraft/findAll":
                        list.add("findAll");
                        break;
                    case "/nodeDraft/saveNodeDraft":
                        list.add("saveNodeDraft");
                        break;
                    case "/nodeDraft/updateNodeDraft":
                        list.add("updateNodeDraft");
                        break;
                    case "/processGroup/updateName":
                        list.add("updateName");
                        break;
                    case "/nodeDraft/deleteByNodeDraftId":
                        list.add("deleteByNodeDraftId");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 供应商相关按钮控制
     * @param request
     * @return
     */
    @Override
    public ResponseData findSupplier(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);
        List<String> list = new ArrayList<>();
        String permissionStr = (String) bucket.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/supplier/saveSupplier":
                        list.add("saveSupplier");
                        break;
                    case "/supplier/updateSupplier":
                        list.add("updateSupplier");
                        break;
                    case "/supplier/deleteById":
                        list.add("deleteById");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 供应商用户相关按钮控制
     * @param request
     * @return
     */
    @Override
    public ResponseData findSupplierUser(HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);
        List<String> list = new ArrayList<>();
        String permissionStr = (String) bucket.get();
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
            for (Permission permission : permissions) {
                switch (permission.getUrl()) {
                    case "/supplierUser/saveSupplierUser":
                        list.add("saveSupplierUser");
                        break;
                    case "/supplierUser/updateSByUser":
                        list.add("updateSByUser");
                        break;
                    case "/supplierUser/findByRole":
                        list.add("findByRole");
                        break;
                    case "/supplierUser/updateUidByRid":
                        list.add("updateUidByRid");
                        break;
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }


}
