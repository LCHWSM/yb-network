package com.ybau.transaction.service.serviceImpl;

import com.alibaba.druid.util.StringUtils;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.FileService;
import com.ybau.transaction.service.OrderService;
import com.ybau.transaction.service.ProductService;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${import.customerName}")
    int customerName;

    @Value("${import.customerMobile}")
    int customerMobile;
    @Value("${import.receiverName}")
    int receiverName;
    @Value("${import.receiverMobile}")
    int receiverMobile;
    @Value("${import.receiverProvince}")
    int receiverProvince;
    @Value("${import.receiverCity}")
    int receiverCity;
    @Value("${import.receiverDistrict}")
    int receiverDistrict;
    @Value("${import.receiverStreet}")
    int receiverStreet;
    @Value("${import.receiverAddress}")
    int receiverAddress;
    @Value("${import.site}")
    int site;
    @Value("${import.addUser}")
    int addUser;
    @Value("${import.addCompany}")
    int addCompany;
    @Value("${import.audit}")
    int audit;
    @Value("${import.sumMoney}")
    int sumMoney;
    @Value("${import.paymentStatus}")
    int paymentStatus;
    @Value("${import.paymentMethod}")
    int paymentMethod;
    @Value("${import.actualMoney}")
    int actualMoney;
    @Value("${import.freight}")
    int freight;
    @Value("${import.retreatCargo}")
    int retreatCargo;
    @Value("${import.orderRemark}")
    int orderRemark;
    @Value("${import.settlementAmount}")
    int settlementAmount;
    @Value("${import.goodsPrice}")
    int goodsPrice;
    @Value("${import.goodsName}")
    int goodsName;
    @Value("${import.goodsNumber}")
    int goodsNumber;
    @Value("${import.goodsCore}")
    int goodsCore;
    @Value("${import.unitName}")
    int unitName;
    @Value("${import.invoiceNumber}")
    int invoiceNumber;
    @Value("${import.unitSite}")
    int unitSite;
    @Value("${import.flag}")
    int flag;
    @Value("${import.bankDeposit}")
    int bankDeposit;
    @Value("${import.email}")
    int email;
    @Value("${import.invoiceType}")
    int invoiceType;
    @Value("${import.ticketSite}")
    int ticketSite;

    @Autowired
    OrderMapper orderMapper;


    @Autowired
    OrderService orderService;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ClassifyMapper classifyMapper;

    @Autowired
    ProductService productService;

    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public ResponseData doImportExcel(MultipartFile file, HttpServletRequest request) throws Exception {
        String uId = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<StringBuffer> list = new ArrayList<>();
        // 判断文件版本
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        InputStream ins = file.getInputStream();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins);
        } else if (suffix.equals("xls")) {
            wb = new HSSFWorkbook(ins);
        } else {
            //如果不是表格类型 提示文件类型错误
            return new ResponseData(400, "需要上传Xlsx或xls格式文件", null);
        }
        int x = 10;
        StringBuffer name = new StringBuffer();
        /**
         * 获取excel表单
         */
        Sheet sheet = wb.getSheetAt(0);
        //line = 9 :从表的第10行开始获取记录
        LeadOrder order = new LeadOrder();
        List<LeadOrder> orders = new ArrayList<>();
        name.append("第");
        name.append(x);
        name.append("条数据,");
        try {
            if (null != sheet) {
                for (int line = 9; line <= sheet.getLastRowNum(); line++) {
                    List<Goods> goodsList = new ArrayList<>();
                    Goods goods = new Goods();
                    Express express = new Express();
                    LeadInvoice invoice = new LeadInvoice();
                    Row row = sheet.getRow(line);
                    if (null == row) {
                        name.delete(0, name.length());
                        name.append("第");
                        name.append(x);
                        name.append("条数据,");
                        name.append("未按要求输入数据。");
                        x++;
                        if (name.length() - String.valueOf(x).length() > 7) {
                            //如果有错误信息在插入到list
                            list.add(name);
                        }
                        name = new StringBuffer();
                        continue;
                    }
                    name.delete(0, name.length());
                    name.append("第");
                    name.append(x);
                    name.append("条数据,");
                    x++;
                    /**
                     * 获取第一个单元格的内容
                     */
                    try {
                        String orderId = "";
                        Cell cell9 = row.getCell(0);
                        if (cell9 != null) {
                            cell9.setCellType(Cell.CELL_TYPE_STRING);
                            orderId = cell9.getStringCellValue();
                        }
                        if (order.getOrderId() != null && !order.getOrderId().equals("") && orderId.equals(order.getOrderId())) {
                            //如果条订单为空 则下一条订单不比对
                            //如果ordersMap存在 则此次为订单的商品 则直接解析商品  存入对象后重新插入
                            //如果一致 则是子订单 直接解析商品
                            row.getCell(34).setCellType(Cell.CELL_TYPE_STRING);
                            String gName = row.getCell(34).getStringCellValue();
                            if (gName == null || gName.length() > goodsName || gName == "") {
                                name.append("商品名字为空或长度过长。");
                            } else {
                                Product product = productMapper.findByPName(gName);
                                if (product == null || product.getName() == null) {
                                    name.append("未查询到商品。");
                                } else {
                                    goods.setGoodsId(String.valueOf(product.getId()));
                                    goods.setGoodsName(product.getName());
                                    goods.setGoodsCore(product.getCore());
                                }
                            }
                            row.getCell(35).setCellType(Cell.CELL_TYPE_STRING);
                            String gPrice = row.getCell(35).getStringCellValue();
                            try {
                                if (gPrice == null || gPrice.length() > goodsPrice || gPrice == "") {
                                    name.append("商品价格为空或长度过长。");
                                } else {
                                    goods.setGoodsPrice(Double.parseDouble(gPrice));
                                }
                            } catch (Exception e) {
                                name.append("商品价格格式错误。");
                            }
                            row.getCell(36).setCellType(Cell.CELL_TYPE_STRING);
                            String gNumber = row.getCell(36).getStringCellValue();
                            try {
                                if (gNumber == null || gNumber == "") {
                                    name.append("商品数量不可为空。");
                                } else {
                                    goods.setGoodsNumber(Integer.parseInt(gNumber));
                                }
                            } catch (Exception e) {
                                name.append("商品数量格式错误。");
                            }
                            List<Goods> goodsDetail = order.getGoodsDetail();
                            if (goodsDetail != null) {
                                goodsDetail.add(goods);
                                order.setGoodsDetail(goodsDetail);
                            }
                        } else {
                            //order无数据清空order 并重新创建对象
                            if (order.getOrderTime() != null) {
                                List<Goods> goodsDetail = order.getGoodsDetail();
                                //取出所有商品信息
                                double now = 0;
                                for (Goods goods1 : goodsDetail) {
                                    //计算商品总金额
                                    if (!StringUtils.isEmpty(goods1.getGoodsName())) {
                                        double mul = BigDecimalUtil.mul(goods1.getGoodsPrice(), goods1.getGoodsNumber());
                                        now = BigDecimalUtil.add(now, mul);
                                    }
                                }
                                order.setSumMoney(now);
                                orders.add(order);
                            }
                            order = null;
                            order = new LeadOrder();
                            //如果order为null 则为第一次解析数据
                            order.setOrderId(orderId);
                            order.setAddUser(uId);//上传人即为下单人
                            Integer organizationId = userMapper.findUserByName(uId);//查询所属公司
                            order.setAddCompany(organizationId);
                            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                            order.setOrderId(orderId);
                            String orderTime = row.getCell(1).getStringCellValue();
                            try {
                                int i = BigDecimalUtil.calcJulianDays(HSSFDateUtil.getJavaDate(Double.parseDouble(orderTime)), df.parse(df.format(new Date())));
                                if (i > 0) {
                                    name.append("下单时间不可超过当前时间。");
                                }
                                order.setOrderTime(HSSFDateUtil.getJavaDate(Double.parseDouble(orderTime)));
                            } catch (Exception e) {
                                name.append("下单时间格式错误。");
                            }
                            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                            String rName = row.getCell(2).getStringCellValue();
                            if (rName == null || rName.length() > receiverName || rName == "") {
                                name.append("收件人姓名为空或长度过长。");
                            } else {
                                order.setReceiverName(rName);
                            }
                            Cell cell12 = row.getCell(3);
                            if (cell12 != null) {
                                cell12.setCellType(Cell.CELL_TYPE_STRING);
                                String rMobile = cell12.getStringCellValue();
                                if (rMobile == null || rMobile.length() > receiverMobile) {
                                    name.append("收件人电话号码错误。");
                                } else {
                                    order.setReceiverMobile(rMobile);
                                }
                            }
                            Cell cell13 = row.getCell(4);
                            String rProvince = "";
                            if (cell13 != null) {
                                cell13.setCellType(Cell.CELL_TYPE_STRING);
                                rProvince = cell13.getStringCellValue();
                                if (rProvince == null || rProvince.length() > receiverProvince) {
                                    name.append("收件人地址省份格式错误。");
                                } else {
                                    order.setReceiverProvince(rProvince);
                                }
                            }
                            Cell cell14 = row.getCell(5);
                            String rCity = "";
                            if (cell14 != null) {
                                cell14.setCellType(Cell.CELL_TYPE_STRING);
                                rCity = cell14.getStringCellValue();
                                if (rCity == null || rCity.length() > receiverCity) {
                                    name.append("收件人地址“市“格式错误。");
                                } else {
                                    order.setReceiverCity(rCity);
                                }
                            }
                            Cell cell15 = row.getCell(6);
                            String rDistrict = "";
                            if (cell15 != null) {
                                cell15.setCellType(Cell.CELL_TYPE_STRING);
                                rDistrict = cell15.getStringCellValue();
                                if (rDistrict == null || rDistrict.length() > receiverDistrict) {
                                    name.append("收件人区县格式错误。");
                                } else {
                                    order.setReceiverDistrict(rDistrict);
                                }
                            }
                            Cell cell2 = row.getCell(7);
                            String rStreet = "";
                            if (cell2 != null) {
                                cell2.setCellType(Cell.CELL_TYPE_STRING);
                                rStreet = cell2.getStringCellValue();
                                if (rStreet != null && rStreet.length() > receiverStreet) {
                                    name.append("收件人街道过长。");
                                } else {
                                    order.setReceiverStreet(rStreet);
                                }
                            }
                            Cell cell16 = row.getCell(8);
                            String rAddress = "";
                            if (cell16 != null) {
                                cell16.setCellType(Cell.CELL_TYPE_STRING);
                                rAddress = cell16.getStringCellValue();
                                if (rAddress == null || rAddress.length() > receiverAddress) {
                                    name.append("收件人详情地址格式错误。");
                                } else {
                                    order.setReceiverAddress(rAddress);
                                }
                            }
                            String location = rProvince + rCity + rDistrict + rStreet + rAddress;
                            if (location.length() > site) {
                                name.append("收件人地址过长。");
                            } else {
                                order.setSite(location);
                            }
                            row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
                            String pMethod = row.getCell(9).getStringCellValue();
                            try {
                                if (pMethod == null || pMethod == "") {
                                    name.append("下单方式不可为空。");
                                } else {
                                    order.setPaymentMethodStr(pMethod);
                                }
                            } catch (Exception e) {
                                name.append("下单方式格式错误。");
                            }
                            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
                            String aud = row.getCell(10).getStringCellValue();
                            try {
                                if (aud == null || aud == "") {
                                    name.append("审核状态不可为空。");
                                } else {
                                    order.setAuditStr(aud);
                                }
                            } catch (Exception e) {
                                name.append("审核状态格式错误。");
                            }
                            row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
                            String pStatus = row.getCell(11).getStringCellValue();
                            try {
                                if (pStatus == null || pStatus == "") {
                                    name.append("付款状态不可为空。");
                                } else {
                                    order.setPaymentStatusStr(pStatus);
                                }
                            } catch (Exception e) {
                                name.append("付款状态格式错误。");
                            }
                            row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
                            String eOnly = row.getCell(12).getStringCellValue();
                            try {
                                if (eOnly == null || eOnly == "") {
                                    name.append("发货标识不可为空。");
                                } else {
                                    order.setExpressOnlyStr(eOnly);
                                }
                            } catch (Exception e) {
                                name.append("发货标识格式错误。");
                            }
                            row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
                            String rCargo = row.getCell(13).getStringCellValue();
                            try {
                                if (rCargo == null || rCargo == "") {
                                    name.append("退货标识格式不可为空。");
                                } else {
                                    order.setRetreatCargoStr(rCargo);
                                }
                            } catch (Exception e) {
                                name.append("退货标识格式错误。");
                            }
                            row.getCell(14).setCellType(Cell.CELL_TYPE_STRING);
                            String orderSignStr = row.getCell(14).getStringCellValue();
                            try {
                                if (orderSignStr == null || orderSignStr == "") {
                                    name.append("是否采集到数据仪表盘状态不可为空。");
                                } else {
                                    order.setOrderSignStr(orderSignStr);
                                }
                            } catch (Exception e) {
                                name.append("是否采集到数据仪表盘状态格式错误。");
                            }
                            Cell cell = row.getCell(15);
                            if (cell != null) {
                                cell.setCellType(CellType.STRING);
                                String proposer = cell.getStringCellValue();
                                if (proposer != null && proposer.equals("") && proposer.length() > 50) {
                                    name.append("申请人字段过长。");
                                }
                                order.setProposer(proposer);
                            }
                            Cell cell10 = row.getCell(16);
                            if (cell10 != null) {
                                cell10.setCellType(CellType.STRING);
                                String dingNumber = cell10.getStringCellValue();
                                if (dingNumber != null && dingNumber.equals("") && dingNumber.length() > 500) {
                                    name.append("钉钉单号字段过长。");
                                }
                                order.setDingNumber(dingNumber);
                            }
                            if (order.getPaymentMethod() == 2) {
                                try {
                                    Cell cell21 = row.getCell(17);
                                    if (cell21 != null) {
                                        cell21.setCellType(CellType.STRING);
                                        String rTime = cell21.getStringCellValue();
                                        if (!StringUtils.isEmpty(rTime)) {
                                            order.setReturnTime(HSSFDateUtil.getJavaDate(Double.parseDouble(rTime)));
                                        }
                                    }
                                } catch (Exception e) {
                                    name.append("借样归还时间格式错误。");
                                }
                            }
                            row.getCell(18).setCellType(Cell.CELL_TYPE_STRING);
                            String aMoney = row.getCell(18).getStringCellValue();
                            try {
                                if (aMoney == null || aMoney.length() > actualMoney || aMoney == "") {
                                    name.append("订单实收金额为空或实收金额过长。");
                                } else {
                                    order.setActualMoney(Double.parseDouble(aMoney));
                                }
                            } catch (Exception e) {
                                name.append("订单实收金额格式错误。");
                            }
                            Cell cell3 = row.getCell(19);
                            if (cell3 != null && cell3.toString() != "") {
                                cell3.setCellType(Cell.CELL_TYPE_STRING);
                                String fight = cell3.getStringCellValue();
                                try {
                                    if (fight.length() > freight) {
                                        name.append("其他金额格式错误。");
                                    } else if (fight != null) {
                                        order.setFreight(Double.parseDouble(fight));
                                    }
                                } catch (Exception e) {
                                    name.append("其他金额格式错误。");
                                }
                            }
                            Cell cell11 = row.getCell(20);
                            if (cell11 != null && cell11.toString() != "") {
                                cell11.setCellType(Cell.CELL_TYPE_STRING);
                                String sAmount = cell11.getStringCellValue();
                                try {
                                    if (sAmount.length() > settlementAmount) {
                                        name.append("已结算金额超出限制。");
                                    } else if (sAmount != null) {
                                        order.setSettlementAmount(Double.parseDouble(sAmount));
                                    }
                                } catch (Exception e) {
                                    name.append("已结算金额格式错误。");
                                }
                            }
                            Cell cell20 = row.getCell(21);
                            if (cell20 != null && cell20.toString() != "") {
                                cell20.setCellType(cell20.CELL_TYPE_STRING);
                                String serveCost = cell20.getStringCellValue();
                                try {
                                    if (serveCost.length() > 12) {
                                        name.append("服务费金额超出限制。");
                                    } else if (serveCost != null) {
                                        order.setServeCost(Double.parseDouble(serveCost));
                                    }
                                } catch (Exception e) {
                                    name.append("服务费金额格式错误。");
                                }
                            }
                            Cell cell1 = row.getCell(22);
                            if (cell1 != null) {
                                cell1.setCellType(Cell.CELL_TYPE_STRING);
                                String oRemark = cell1.getStringCellValue();
                                if (oRemark != null && oRemark.length() > orderRemark) {
                                    name.append("备注过长。");
                                } else {
                                    order.setOrderRemark(oRemark);
                                }
                            }
                            row.getCell(23).setCellType(Cell.CELL_TYPE_STRING);
                            String invoiceFlag = row.getCell(23).getStringCellValue();
                            try {
                                if (invoiceFlag == null || invoiceFlag == "") {
                                    name.append("是否需要发票不可为空。");
                                } else {
                                    invoice.setInvoiceFlag(invoiceFlag);
                                }
                            } catch (Exception e) {
                                name.append("是否需要发票格式错误。");
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                row.getCell(24).setCellType(Cell.CELL_TYPE_STRING);
                                String uName = row.getCell(24).getStringCellValue();
                                if (uName == null || uName.length() > unitName || uName == "") {
                                    name.append("发票抬头为空或长度过长。");
                                } else {
                                    invoice.setUnitName(uName);
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                row.getCell(25).setCellType(Cell.CELL_TYPE_STRING);
                                String fg = row.getCell(25).getStringCellValue();
                                try {
                                    if (fg == null || fg == "") {
                                        name.append("抬头类型不可为空。");
                                    } else {
                                        invoice.setFlagStr(fg);
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                Cell cell4 = row.getCell(26);
                                if (cell4 != null) {
                                    cell4.setCellType(cell4.CELL_TYPE_STRING);
                                    String iNumber = cell4.getStringCellValue();
                                    if (iNumber != null && iNumber.length() > invoiceNumber) {
                                        name.append("纳税人识别号格式错误。");
                                    } else {
                                        invoice.setInvoiceNumber(iNumber);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                Cell cell5 = row.getCell(27);
                                if (cell5 != null) {
                                    cell5.setCellType(cell5.CELL_TYPE_STRING);
                                    String uSite = cell5.getStringCellValue();
                                    if (uSite != null && uSite.length() > invoiceNumber) {
                                        name.append("地址/电话格式错误。");
                                    } else {
                                        invoice.setUnitSite(uSite);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                Cell cell6 = row.getCell(28);
                                if (cell6 != null) {
                                    cell6.setCellType(cell6.CELL_TYPE_STRING);
                                    String bDeposit = cell6.getStringCellValue();
                                    if (bDeposit != null && bDeposit.length() > bankDeposit) {
                                        name.append("开户行及账号格式错误。");
                                    } else {
                                        invoice.setBankDeposit(bDeposit);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                Cell cell7 = row.getCell(29);
                                if (cell7 != null) {
                                    cell7.setCellType(cell7.CELL_TYPE_STRING);
                                    String tSite = cell7.getStringCellValue();
                                    if (tSite != null && tSite.length() > ticketSite) {
                                        name.append("收票地址格式错误。");
                                    } else {
                                        invoice.setTicketSite(tSite);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                Cell cell8 = row.getCell(30);
                                if (cell8 != null) {
                                    cell8.setCellType(cell8.CELL_TYPE_STRING);
                                    String ema = cell8.getStringCellValue();
                                    if (ema != null && ema.length() > email) {
                                        name.append("电子邮箱格式错误。");
                                    } else {
                                        invoice.setEmail(ema);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("需要")) {
                                row.getCell(31).setCellType(Cell.CELL_TYPE_STRING);
                                String iType = row.getCell(31).getStringCellValue();
                                try {
                                    if (iType == null || iType == "") {
                                        name.append("发票类型不可为空。");
                                    } else {
                                        invoice.setInvoiceTypeStr(iType);
                                    }
                                } catch (Exception e) {
                                    name.append("发票类型只可为数字。");
                                }
                            }
                            if (order.getExpressOnly() == 1) {
                                Cell cell4 = row.getCell(32);
                                if (cell4 != null) {
                                    cell4.setCellType(cell4.CELL_TYPE_STRING);
                                    String expressName = cell4.getStringCellValue();
                                    if (expressName.length() > 11) {
                                        name.append("快递公司名字格式错误。");
                                    } else {
                                        express.setExpressName(expressName);
                                    }
                                }
                                Cell cell5 = row.getCell(33);
                                if (cell5 != null) {
                                    cell5.setCellType(cell5.CELL_TYPE_STRING);
                                    String expressNumbers = cell5.getStringCellValue();
                                    if (expressNumbers.length() > 200) {
                                        name.append("快递公司名字格式错误。");
                                    } else {
                                        express.setExpressNumbers(expressNumbers);
                                    }
                                }
                            }
                            row.getCell(34).setCellType(Cell.CELL_TYPE_STRING);
                            String gName = row.getCell(34).getStringCellValue();
                            if (gName == null || gName.length() > goodsName || gName == "") {
                                name.append("商品名字为空或长度过长。");
                            } else {
                                Product product = productMapper.findByPName(gName);
                                if (product == null || product.getName() == null) {
                                    name.append("未查询到商品。");
                                } else {
                                    goods.setGoodsId(String.valueOf(product.getId()));
                                    goods.setGoodsName(product.getName());
                                    goods.setGoodsCore(product.getCore());
                                }
                            }
                            row.getCell(35).setCellType(Cell.CELL_TYPE_STRING);
                            String gPrice = row.getCell(35).getStringCellValue();
                            try {
                                if (gPrice == null || gPrice.length() > goodsPrice || gPrice == "") {
                                    name.append("商品价格为空或长度过长。");
                                } else {
                                    goods.setGoodsPrice(Double.parseDouble(gPrice));
                                }
                            } catch (Exception e) {
                                name.append("商品价格格式错误。");
                            }
                            row.getCell(36).setCellType(Cell.CELL_TYPE_STRING);
                            String gNumber = row.getCell(36).getStringCellValue();
                            try {
                                if (gNumber == null || gNumber == "") {
                                    name.append("商品数量不可为空。");
                                } else {
                                    goods.setGoodsNumber(Integer.parseInt(gNumber));
                                }
                            } catch (Exception e) {
                                name.append("商品数量格式错误。");
                            }
                            order.setExpress(express);
                            order.setInvoice(invoice);
                            goodsList.add(goods);
                            order.setGoodsDetail(goodsList);
                        }
                    } catch (Exception e) {
                        log.error("{}", e);
                        name.append("未按要求输入数据。");
                        if (name.length() - String.valueOf(x).length() > 7) {
                            //如果有错误信息在插入到list
                            list.add(name);
                        }
                        name = null;
                        name = new StringBuffer();
                    }
                    if (name.length() - String.valueOf(x).length() > 7) {
                        //如果有错误信息在插入到list
                        list.add(name);
                    }
                    name = null;
                    name = new StringBuffer();
                }
                List<Goods> goodsDetail = order.getGoodsDetail();
                //取出所有商品信息
                double now = 0;
                if (goodsDetail != null) {
                    for (Goods goods1 : goodsDetail) {
                        //计算商品总金额
                        if (!StringUtils.isEmpty(goods1.getGoodsName())) {
                            double mul = BigDecimalUtil.mul(goods1.getGoodsPrice(), goods1.getGoodsNumber());
                            now = BigDecimalUtil.add(now, mul);
                        }
                    }
                }
                if (order.getOrderTime() == null) {
                    name.append("未按要求输入数据。");
                    if (name.length() - String.valueOf(x).length() > 7) {
                        //如果有错误信息在插入到list
                        list.add(name);
                    }
                    name = null;
                }
                order.setSumMoney(now);
                orders.add(order);
                x = 0;
                if (list.size() < 1) {
                    //小于1数据没有问题 执行插入操作
                    //插入订单
                    if (orders != null && orders.size() > 0) {
                        return orderService.leadOrder(orders, uId, fileName);
                    }
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "excel格式异常,请检查excel格式有无数据缺失,无效数据行!", null);
        }
        return new ResponseData(400, "未查询到数据或数据有误，导入失败", list);
    }

    /**
     * 商品导入
     *
     * @param file
     * @param request
     * @return
     */
    @Override
    public ResponseData uploadProduct(MultipartFile file, HttpServletRequest request) throws IOException {
        String uId = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        List<StringBuffer> list = new ArrayList<>();
        // 判断文件版本
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        InputStream ins = file.getInputStream();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins);
        } else if (suffix.equals("xls")) {
            wb = new HSSFWorkbook(ins);
        } else {
            //如果不是表格类型 提示文件类型错误
            return new ResponseData(400, "需要上传Xlsx或xls格式文件", null);
        }
        StringBuffer name = new StringBuffer();
        int x = 3;
        /**
         * 获取excel表单
         */
        Sheet sheet = wb.getSheetAt(0);
        //line = 1 :从表的第2行开始获取记录
        ExcelProduct excelProduct = new ExcelProduct();
        List<ExcelProduct> excelProducts = new ArrayList<>();
        try {
            if (null != sheet) {
                for (int line = 2; line <= sheet.getLastRowNum(); line++) {
                    Row row = sheet.getRow(line);
                    if (null == row) {
                        name.delete(0, name.length());
                        name.append("第");
                        name.append(x);
                        name.append("条数据,");
                        name.append("未按要求输入数据。");
                        x++;
                        if (name.length() > 7) {
                            //如果有错误信息在插入到list
                            list.add(name);
                        }
                        name = new StringBuffer();
                        continue;
                    }
                    name.append("第");
                    name.append(x);
                    name.append("条数据,");
                    x++;
                    excelProduct.setAddUser(uId);//赋值创建人
                    excelProduct.setAddTime(df.parse(df.format(new Date())));//赋值创建时间
                    Cell cell2 = row.getCell(0);
                    String productName = "";
                    if (cell2 == null) {
                        name.append("商品名字不可为空。");
                        excelProduct.setName(productName);
                    } else {
                        cell2.setCellType(Cell.CELL_TYPE_STRING);
                        productName = cell2.getStringCellValue();
                        if (productName == null || productName.equals("")) {
                            name.append("商品名字不可为空。");
                            excelProduct.setName(productName);
                        } else {
                            excelProduct.setName(productName);
                        }
                    }
                    String productCore = "";
                    Cell cell3 = row.getCell(1);
                    if (cell3 == null) {
                        name.append("商品编码不可为空。");
                        excelProduct.setCore(productCore);
                    } else {
                        cell3.setCellType(Cell.CELL_TYPE_STRING);
                        productCore = cell3.getStringCellValue();
                        if (productCore == null || productCore.equals("")) {
                            name.append("商品编码不可为空。");
                            excelProduct.setCore(productCore);
                        } else {
                            excelProduct.setCore(productCore);
                        }
                    }
                    Cell cell = row.getCell(2);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String productPrice = cell.getStringCellValue();
                        try {
                            if (productPrice != null && !productPrice.equals("")) {
                                if (productPrice.length() > 13) {
                                    name.append("商品价格过长。");
                                }
                                excelProduct.setPrice(Double.parseDouble(productPrice));
                            }
                        } catch (Exception e) {
                            name.append("商品价格格式有误。");
                        }
                    }
                    Cell cell4 = row.getCell(3);
                    if (cell4 == null) {
                        name.append("是否启用不可为空。");
                    } else {
                        cell4.setCellType(Cell.CELL_TYPE_STRING);
                        String productStatus = cell4.getStringCellValue();
                        if (productStatus == null || productStatus.equals("")) {
                            name.append("是否启用不可为空。");
                        } else {
                            excelProduct.setStatusStr(productStatus);
                        }
                    }
                    Cell cell1 = row.getCell(4);
                    if (cell1 != null) {
                        cell1.setCellType(Cell.CELL_TYPE_STRING);
                        String productWarehouse = cell1.getStringCellValue();
                        try {
                            if (productWarehouse != null && !productWarehouse.equals("")) {
                                excelProduct.setWarehouse(Integer.parseInt(productWarehouse));
                            }
                        } catch (Exception e) {
                            name.append("库存格式有误。");
                        }
                    }
                    Cell cell5 = row.getCell(5);
                    if (cell5 == null) {
                        name.append("商品分类不可为空。");
                    } else {
                        cell5.setCellType(Cell.CELL_TYPE_STRING);
                        String classifyStr = cell5.getStringCellValue();
                        if (classifyStr == null || classifyStr.equals("")) {
                            name.append("商品分类不可为空。");
                        } else {
                            Integer classifyId = classifyMapper.findById(classifyStr);//根据分类名字查询分类ID
                            if (classifyId == null) {
                                name.append("系统中不存在的分类，请重新输入。");
                            } else {
                                excelProduct.setClassify_id(classifyId);
                            }
                        }
                    }
                    //查询所有系统中所有商品校验是否有重复的商品名称或编号
                    List<Product> products = productMapper.getAll();
                    if (RepetitionUtil.checkForDuplicates(productName, productCore, products)) {
                        name.append("系统中已存在的商品名称或商品编号。");
                    }
                    if (name.length() - String.valueOf(x).length() > 7) {
                        //如果有错误信息在插入到list
                        list.add(name);
                    }
                    //重新创建name对象
                    name = new StringBuffer();
                    //把取出的数据放到集合中
                    excelProducts.add(excelProduct);
                    //重新创建excelProduct对象
                    excelProduct = new ExcelProduct();
                }
                long count = RepetitionUtil.ifNameByCore(excelProducts);
                if (count > 0) {
                    list.add(new StringBuffer("商品名称或商品编号存在重复行"));
                }
            }
            if (list.size() > 0) {
                //如果list长度小于0则存在错误行
                return new ResponseData(400, "数据有误，导入失败", list);
            }
            //如果不大于0则插入商品
            if (excelProducts != null && excelProducts.size() > 0) {
                return productService.leadProduct(excelProducts);
            }
            return new ResponseData(400, "未查询到数据或数据有误，导入失败", list);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "excel格式异常,请检查excel格式有无数据缺失,无效数据行!", null);
        }
    }

    /**
     * 录入退款文件
     *
     * @param file    上传文件
     * @param request token
     * @return
     */
    @Override
    public ResponseData uploadRefund(MultipartFile file, HttpServletRequest request) throws IOException {
        String uId = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<StringBuffer> list = new ArrayList<>();
        // 判断文件版本
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        InputStream ins = file.getInputStream();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins);
        } else if (suffix.equals("xls")) {
            wb = new HSSFWorkbook(ins);
        } else {
            //如果不是表格类型 提示文件类型错误
            return new ResponseData(400, "需要上传Xlsx或xls格式文件", null);
        }
        int x = 6;
        StringBuffer name = new StringBuffer();
        /**
         * 获取excel表单
         */
        Sheet sheet = wb.getSheetAt(0);
        //line = 1 :从表的第2行开始获取记录
        Map<String, Object> orderMap = new HashMap<>();
        List<Map<String, Object>> orders = new ArrayList<>();
        try {
            if (null != sheet) {
                for (int line = 5; line <= sheet.getLastRowNum(); line++) {
                    List<Goods> goodsList = new ArrayList<>();
                    Goods goods = new Goods();
                    Row row = sheet.getRow(line);
                    if (null == row) {
                        name.delete(0, name.length());
                        name.append("第");
                        name.append(x);
                        name.append("条数据,");
                        name.append("未按要求输入数据。");
                        x++;
                        if (name.length() - String.valueOf(x).length() > 7) {
                            //如果有错误信息在插入到list
                            list.add(name);
                        }
                        name = new StringBuffer();
                        continue;
                    }
                    name.delete(0, name.length());
                    name.append("第");
                    name.append(x);
                    name.append("条数据,");
                    x++;
                    /**
                     * 获取第一个单元格的内容
                     */
                    try {
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        String orderId = row.getCell(0).getStringCellValue();
                        if (orderId.equals(orderMap.get("orderId"))) {
                            //如果ordersMap存在 则此次为订单的商品 则直接解析商品  存入对象后重新插入
                            //如果一致 则是子订单 直接解析商品
                            row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                            String goodsName = row.getCell(5).getStringCellValue();
                            if (goodsName == null || goodsName == "") {
                                name.append("商品名字不可为空。");
                            } else {
                                goods.setGoodsName(goodsName);
                            }
                            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                            String goodsNumber = row.getCell(6).getStringCellValue();
                            List<Goods> orderGoods = goodsMapper.findByOrdersId((String) orderMap.get("orderId"));
                            boolean flag = true;
                            for (Goods orderGood : orderGoods) {
                                if (orderGood.getGoodsName().equals(goodsName)) {
                                    flag = false;
                                    //如果有该商品  判断可退货数量是否足够
                                    if (Integer.parseInt(goodsNumber) > orderGood.getGoodsNumber() - (orderGood.getReturnNumber() + orderGood.getSellNumber())) {
                                        name.append("此订单可退款商品不足。");
                                    } else {
                                        //如果足够  把此商品ID 数量计入
                                        goods.setGoodsId(orderGood.getGoodsId());
                                        goods.setReturnNumber(Integer.parseInt(goodsNumber) + orderGood.getReturnNumber());
                                        goods.setId(orderGood.getId());
                                    }
                                }
                            }
                            if (flag) {
                                name.append("该订单未查询到此商品，请重新输入。");
                            }
                            List<Goods> goodsDetail = (List<Goods>) orderMap.get("goodsList");
                            if (goodsDetail != null) {
                                goodsDetail.add(goods);
                                orderMap.put("goodsList", goodsDetail);
                            }
                        } else {
                            //order无数据清空order 并重新创建对象
                            if (orderMap.get("orderId") != null && orderMap.get("orderId") != "") {
                                orders.add(orderMap);
                            }
                            orderMap = null;
                            orderMap = new HashMap<>();
                            Order order = orderMapper.findOrderId(orderId);
                            if (order == null) {
                                name.append("未查询到此订单，请重新输入订单编号。");
                            } else if (order.getExpressOnly() != 1) {
                                name.append("该订单还未发货，无法退货。");
                            }
                            //如果order为null 则为第一次解析数据
                            orderMap.put("orderId", orderId);
                            orderMap.put("paymentMethod", order.getPaymentMethod());
                            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                            String expressName = row.getCell(1).getStringCellValue();
                            orderMap.put("expressName", expressName);
                            Cell cell1 = row.getCell(2);
                            if (cell1 != null) {
                                cell1.setCellType(Cell.CELL_TYPE_STRING);
                                String expressNum = cell1.getStringCellValue();
                                orderMap.put("expressNum", expressNum);
                            }
                            if (order.getPaymentMethod() != 2) {
                                //非借样订单才取退款金额
                                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                                String refundAmount = row.getCell(3).getStringCellValue();
                                if (Double.valueOf(refundAmount) > BigDecimalUtil.sub(BigDecimalUtil.add(order.getActualMoney(), order.getFreight()), order.getRefundAmount())) {
                                    name.append("退款金额不可大于未退款金额。");
                                } else {
                                    orderMap.put("refundAmount", BigDecimalUtil.add(Double.valueOf(refundAmount), order.getRefundAmount()));
                                }
                            }
                            if (order.getPaymentMethod() == 2) {
                                Cell cell2 = row.getCell(4);
                                if (cell2 != null) {
                                    cell2.setCellType(Cell.CELL_TYPE_STRING);
                                    String returnTime = cell2.getStringCellValue();
                                    try {
                                        int i = BigDecimalUtil.calcJulianDays(HSSFDateUtil.getJavaDate(Double.parseDouble(returnTime)), df.parse(df.format(new Date())));
                                        if (i > 0) {
                                            name.append("实际归还时间不可超过当前时间。");
                                        }
                                        orderMap.put("returnTime", HSSFDateUtil.getJavaDate(Double.parseDouble(returnTime)));
                                    } catch (Exception e) {
                                        name.append("下单时间格式错误。");
                                    }
                                }
                            }
                            row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                            String goodsName = row.getCell(5).getStringCellValue();
                            if (goodsName == null || goodsName == "") {
                                name.append("商品名字不可为空。");
                            } else {
                                goods.setGoodsName(goodsName);
                            }
                            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                            String goodsNumber = row.getCell(6).getStringCellValue();
                            List<Goods> orderGoods = goodsMapper.findByOrdersId((String) orderMap.get("orderId"));
                            boolean flag = true;
                            for (Goods orderGood : orderGoods) {
                                if (orderGood.getGoodsName().equals(goodsName)) {
                                    flag = false;
                                    //如果有该商品  判断可退货数量是否足够

                                    if (Integer.parseInt(goodsNumber) > orderGood.getGoodsNumber() - (orderGood.getReturnNumber() + orderGood.getSellNumber())) {
                                        name.append("此订单可退款商品不足。");
                                    } else {
                                        //如果足够  把此商品ID 数量计入
                                        goods.setGoodsId(orderGood.getGoodsId());
                                        goods.setReturnNumber(Integer.parseInt(goodsNumber) + orderGood.getReturnNumber());
                                        goods.setId(orderGood.getId());
                                    }
                                }
                            }
                            if (flag) {
                                name.append("该订单未查询到此商品，请重新输入。");
                            }
                            goodsList.add(goods);
                            orderMap.put("goodsList", goodsList);
                        }
                    } catch (Exception e) {
                        log.error("{}", e);
                        name.append("未按要求输入数据。");
                        if (name.length() - String.valueOf(x).length() > 7) {
                            //如果有错误信息在插入到list
                            list.add(name);
                        }
                        name = new StringBuffer();
                    }
                    if (name.length() - String.valueOf(x).length() > 7) {
                        //如果有错误信息在插入到list
                        list.add(name);
                    }
                    name = new StringBuffer();
                }
                if (orderMap.get("orderId") != null && orderMap.get("orderId") != "") {
                    orders.add(orderMap);
                }
                if (list.size() < 1) {
                    //小于1数据没有问题 执行修改操作
                    //修改订单退货信息
                    if (orders != null && orders.size() > 0) {
                        return orderService.leadSales(orders, uId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "excel格式异常,请检查excel格式有无数据缺失,无效数据行!", null);
        }
        return new ResponseData(400, "未查询到数据或数据有误，导入失败", list);
    }
}
