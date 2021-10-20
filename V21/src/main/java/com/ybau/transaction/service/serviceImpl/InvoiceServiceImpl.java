package com.ybau.transaction.service.serviceImpl;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Invoice;
import com.ybau.transaction.mapper.InvoiceMapper;
import com.ybau.transaction.service.InvoiceService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Slf4j
@Transactional
public class InvoiceServiceImpl implements InvoiceService {


    @Autowired
    InvoiceMapper invoiceMapper;

    @Autowired
    JwtUtil jwtUtil;


    //邮箱正则表达式
    @Value("${user.RULE_EMAIL}")
    String RULE_EMAIL;

    //手机号正则表达式
    @Value("${user.userPhone}")
    String userPhone;

    /**
     * 插入发票信息
     *
     * @param invoice
     * @param orderId
     * @return
     */
    @Override
    public Map<String, Object> saveInvoice(Invoice invoice, String orderId, String id) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(invoice.getUnitName())) {
            //判断发票信息是否为空
            map.put("flag", 1);
            map.put("name", "发票抬头不可为空");
            return map;
        }
        invoiceMapper.insertInvoice(invoice, orderId);//插入发票信息
        int count = invoiceMapper.findByCount(invoice,id);//查询发票记录是否已经插入
        if (count < 1) {
            //如果不存在并且为公司开具发票时 则插入
            invoiceMapper.saveInvoiceByRecord(invoice, id);//存入发票记录
        }
        map.put("flag", 0);
        return map;
    }

    /**
     * 查询已插入发票记录
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findInvoice(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Invoice> invoices = invoiceMapper.findInvoice(map, id);
        PageInfo pageInfo = new PageInfo(invoices);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 删除发票记录地址
     *
     * @param invoicerecordId
     * @return
     */
    @Override
    public ResponseData deleteInvoice(Integer invoicerecordId, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        int count = invoiceMapper.findIdByUserId(invoicerecordId, id);//根据记录ID和用户Id查看删除的记录是否是登录用户插入的
        if (count < 1) {
            return new ResponseData(400, "参数有误，删除失败", null);
        }
        invoiceMapper.deleteInvoice(invoicerecordId);
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 修改发票记录信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateInvoice(Map<String, Object> map) {
        String unitName = (String) map.get("unitName");
        if (unitName == null || unitName == "") {
            return new ResponseData(400, "发票抬头不可为空", null);
        }
        invoiceMapper.updateInvoice(map);
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 根据发票名字查询发票信息
     *
     * @param unitName
     * @return
     */
    @Override
    public ResponseData findByUnitName(String unitName, Integer flag,HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<Invoice> invoices = invoiceMapper.findByUnitName(unitName, flag,id);
        return new ResponseData(200, "查询成功", invoices);
    }
}
