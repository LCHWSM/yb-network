package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Supplier;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.SupplierMapper;
import com.ybau.transaction.mapper.SupplierOrderMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.SupplierService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierMapper supplierMapper;


    @Autowired
    JwtUtil jwtUtil;

    //手机号正则表达式
    @Value("${user.userPhone}")
    String phone;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SupplierOrderMapper supplierOrderMapper;

    /**
     * 新增供应商
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveSupplier(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("addUser", id);
        map.put("addTime", df.parse(df.format(new Date())));
        try {
            supplierMapper.saveSupplier(map);
        } catch (DuplicateKeyException e) {
            return new ResponseData(400, "该供应商已存在，新增失败", null);
        }
        return new ResponseData(200, "新增成功", null);
    }

    /**
     * 查询所有供应商 （分页  筛选）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Supplier> suppliers = supplierMapper.findAll(map);
        PageInfo pageInfo = new PageInfo(suppliers);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改供应商信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateSupplier(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        try {
            supplierMapper.updateSupplier(map);
        } catch (DuplicateKeyException e) {
            return new ResponseData(400, "该供应商已存在，修改失败", null);
        }
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 根据供应商ID删除供应商信息
     *
     * @param supplierId
     * @return
     */
    @Override
    public ResponseData deleteById(int supplierId) {
        int userCount = userMapper.findBySupplierId(supplierId);//根据供应商ID查询该供应商是否存在下属用户
        int supplierCount = supplierMapper.findBySupplierId(supplierId);
        if (userCount > 0) {
            return new ResponseData(400, "该供应商存在下属用户，无法删除", null);
        }
        if (supplierCount > 0) {
            return new ResponseData(400, "该供应商存在下属订单，无法删除", null);
        }
        supplierMapper.deleteById(supplierId);
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 查询供应商信息
     *
     * @return
     */
    @Override
    public ResponseData findSupplier(String supplierName, Integer pageSize, Integer pageNum, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        User user = userMapper.findById(id);
        PageHelper.startPage(pageNum, pageSize);
        List<Supplier> suppliers = supplierMapper.findSupplier(supplierName, user.getOrganizationId());
        Iterator<Supplier> iterator = suppliers.iterator();
        while (iterator.hasNext()){
            Supplier supplier = iterator.next();
            if (supplier==null){
                iterator.remove();
            }
        }
        PageInfo pageInfo = new PageInfo(suppliers);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据供应商名字查询
     *
     * @param supplierName
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public ResponseData findBySupplier(String supplierName, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Supplier> suppliers = supplierMapper.findBySupplier(supplierName);
        PageInfo pageInfo = new PageInfo(suppliers);
        return new ResponseData(200, "查询成功", pageInfo);
    }
}
