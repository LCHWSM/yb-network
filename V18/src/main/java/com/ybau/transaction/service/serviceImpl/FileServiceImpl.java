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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @Override
    public ResponseData doImportExcel(MultipartFile file, HttpServletRequest request) throws Exception {
        String uId = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<StringBuffer> list = new ArrayList<>();
        // ??????????????????
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        InputStream ins = file.getInputStream();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins);
        } else if (suffix.equals("xls")) {
            wb = new HSSFWorkbook(ins);
        } else {
            //???????????????????????? ????????????????????????
            return new ResponseData(400, "????????????Xlsx???xls????????????", null);
        }
        int x = 10;
        StringBuffer name = new StringBuffer();
        /**
         * ??????excel??????
         */
        Sheet sheet = wb.getSheetAt(0);
        //line = 1 :????????????2?????????????????????
        LeadOrder order = new LeadOrder();
        List<LeadOrder> orders = new ArrayList<>();
        name.append("???");
        name.append(x);
        name.append("?????????,");
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
                        name.append("???");
                        name.append(x);
                        name.append("?????????,");
                        name.append("???????????????????????????");
                        x++;
                        if (name.length() > 7) {
                            //?????????????????????????????????list
                            list.add(name);
                        }
                        name = null;
                        name = new StringBuffer();
                        continue;
                    }
                    name.delete(0, name.length());
                    name.append("???");
                    name.append(x);
                    name.append("?????????,");
                    x++;
                    /**
                     * ?????????????????????????????????
                     */
                    try {
                        String orderId = "";
                        Cell cell9 = row.getCell(0);
                        if (cell9 != null) {
                            cell9.setCellType(Cell.CELL_TYPE_STRING);
                            orderId = cell9.getStringCellValue();
                        }
                        if (order.getOrderId() != null && !order.getOrderId().equals("")) {
                            //????????????????????? ???????????????????????????
                            if (orderId.equals(order.getOrderId())) {
                                //??????ordersMap?????? ??????????????????????????? ?????????????????????  ???????????????????????????
                                //???????????? ??????????????? ??????????????????
                                row.getCell(32).setCellType(Cell.CELL_TYPE_STRING);
                                String gName = row.getCell(32).getStringCellValue();
                                if (gName == null || gName.length() > goodsName || gName == "") {
                                    name.append("????????????????????????????????????");
                                } else {
                                    Product product = productMapper.findByPName(gName);
                                    if (product.getName() == null) {
                                        name.append("?????????????????????");
                                    } else {
                                        goods.setGoodsId(String.valueOf(product.getId()));
                                        goods.setGoodsName(product.getName());
                                        goods.setGoodsCore(product.getCore());
                                    }
                                }
                            }
                            row.getCell(33).setCellType(Cell.CELL_TYPE_STRING);
                            String gPrice = row.getCell(33).getStringCellValue();
                            try {
                                if (gPrice == null || gPrice.length() > goodsPrice || gPrice == "") {
                                    name.append("????????????????????????????????????");
                                } else {
                                    goods.setGoodsPrice(Double.parseDouble(gPrice));
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(34).setCellType(Cell.CELL_TYPE_STRING);
                            String gNumber = row.getCell(34).getStringCellValue();
                            try {
                                if (gNumber == null || gNumber == "") {
                                    name.append("???????????????????????????");
                                } else {
                                    goods.setGoodsNumber(Integer.parseInt(gNumber));
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            List<Goods> goodsDetail = order.getGoodsDetail();
                            if (goodsDetail != null) {
                                goodsDetail.add(goods);
                                order.setGoodsDetail(goodsDetail);
                            }
                        } else {
                            //order???????????????order ?????????????????????
                            if (order.getOrderTime() != null) {
                                List<Goods> goodsDetail = order.getGoodsDetail();
                                //????????????????????????
                                double now = 0;
                                for (Goods goods1 : goodsDetail) {
                                    //?????????????????????
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
                            //??????order???null ???????????????????????????
                            order.setOrderId(orderId);
                            order.setAddUser(uId);//????????????????????????
                            Integer organizationId = userMapper.findUserByName(uId);//??????????????????
                            order.setAddCompany(organizationId);
                            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                            order.setOrderId(orderId);
                            String orderTime = row.getCell(1).getStringCellValue();
                            try {
                                int i = BigDecimalUtil.calcJulianDays(HSSFDateUtil.getJavaDate(Double.parseDouble(orderTime)), df.parse(df.format(new Date())));
                                if (i > 0) {
                                    name.append("???????????????????????????????????????");
                                }
                                order.setOrderTime(HSSFDateUtil.getJavaDate(Double.parseDouble(orderTime)));
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                            String rName = row.getCell(2).getStringCellValue();
                            if (rName == null || rName.length() > receiverName || rName == "") {
                                name.append("???????????????????????????????????????");
                            } else {
                                order.setReceiverName(rName);
                            }
                            Cell cell12 = row.getCell(3);
                            if (cell12 != null) {
                                cell12.setCellType(Cell.CELL_TYPE_STRING);
                                String rMobile = cell12.getStringCellValue();
                                if (rMobile == null || rMobile.length() > receiverMobile) {
                                    name.append("??????????????????????????????");
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
                                    name.append("????????????????????????????????????");
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
                                    name.append("???????????????????????????????????????");
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
                                    name.append("??????????????????????????????");
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
                                    name.append("????????????????????????");
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
                                    name.append("????????????????????????????????????");
                                } else {
                                    order.setReceiverAddress(rAddress);
                                }
                            }
                            String location = rProvince + rCity + rDistrict + rStreet + rAddress;
                            if (location.length() > site) {
                                name.append("????????????????????????");
                            } else {
                                order.setSite(location);
                            }
                            row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
                            String pMethod = row.getCell(9).getStringCellValue();
                            try {
                                if (pMethod == null || pMethod == "") {
                                    name.append("???????????????????????????");
                                } else {
                                    order.setPaymentMethodStr(pMethod);
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
                            String aud = row.getCell(10).getStringCellValue();
                            try {
                                if (aud == null || aud == "") {
                                    name.append("???????????????????????????");
                                } else {
                                    order.setAuditStr(aud);
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
                            String pStatus = row.getCell(11).getStringCellValue();
                            try {
                                if (pStatus == null || pStatus == "") {
                                    name.append("???????????????????????????");
                                } else {
                                    order.setPaymentStatusStr(pStatus);
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
                            String eOnly = row.getCell(12).getStringCellValue();
                            try {
                                if (eOnly == null || eOnly == "") {
                                    name.append("???????????????????????????");
                                } else {
                                    order.setExpressOnlyStr(eOnly);
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
                            String rCargo = row.getCell(13).getStringCellValue();
                            try {
                                if (rCargo == null || rCargo == "") {
                                    name.append("?????????????????????????????????");
                                } else {
                                    order.setRetreatCargoStr(rCargo);
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(14).setCellType(Cell.CELL_TYPE_STRING);
                            String orderSignStr = row.getCell(14).getStringCellValue();
                            try {
                                if (orderSignStr == null || orderSignStr == "") {
                                    name.append("????????????????????????????????????");
                                } else {
                                    order.setOrderSignStr(orderSignStr);
                                }
                            } catch (Exception e) {
                                name.append("????????????????????????????????????");
                            }
                            if (order.getPaymentMethod() == 2) {
                                try {
                                    Cell cell21 = row.getCell(15);
                                    cell21.setCellType(CellType.STRING);
                                    if (cell21 != null) {
                                        String rTime = cell21.getStringCellValue();
                                        if (!StringUtils.isEmpty(rTime)) {
                                            order.setReturnTime(HSSFDateUtil.getJavaDate(Double.parseDouble(rTime)));
                                        }
                                    }
                                } catch (Exception e) {
                                    name.append("?????????????????????????????????");
                                }
                            }
                            row.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
                            String aMoney = row.getCell(16).getStringCellValue();
                            try {
                                if (aMoney == null || aMoney.length() > actualMoney || aMoney == "") {
                                    name.append("????????????????????????????????????????????????");
                                } else {
                                    order.setActualMoney(Double.parseDouble(aMoney));
                                }
                            } catch (Exception e) {
                                name.append("?????????????????????????????????");
                            }
                            Cell cell3 = row.getCell(17);
                            if (cell3 != null && cell3.toString() != "") {
                                cell3.setCellType(Cell.CELL_TYPE_STRING);
                                String fight = cell3.getStringCellValue();
                                try {
                                    if (fight.length() > freight) {
                                        name.append("???????????????????????????");
                                    } else if (fight != null) {
                                        order.setFreight(Double.parseDouble(fight));
                                    }
                                } catch (Exception e) {
                                    name.append("???????????????????????????");
                                }
                            }
                            Cell cell11 = row.getCell(18);
                            if (cell11 != null && cell11.toString() != "") {
                                cell11.setCellType(Cell.CELL_TYPE_STRING);
                                String sAmount = cell11.getStringCellValue();
                                try {
                                    if (sAmount.length() > settlementAmount) {
                                        name.append("??????????????????????????????");
                                    } else if (sAmount != null) {
                                        order.setSettlementAmount(Double.parseDouble(sAmount));
                                    }
                                } catch (Exception e) {
                                    name.append("??????????????????????????????");
                                }
                            }
                            Cell cell20 = row.getCell(19);
                            if (cell20 != null && cell20.toString() != "") {
                                cell20.setCellType(cell20.CELL_TYPE_STRING);
                                String serveCost = cell20.getStringCellValue();
                                try {
                                    if (serveCost.length() > 12) {
                                        name.append("??????????????????????????????");
                                    } else if (serveCost != null) {
                                        order.setServeCost(Double.parseDouble(serveCost));
                                    }
                                } catch (Exception e) {
                                    name.append("??????????????????????????????");
                                }
                            }
                            Cell cell1 = row.getCell(20);
                            if (cell1 != null) {
                                cell1.setCellType(Cell.CELL_TYPE_STRING);
                                String oRemark = cell1.getStringCellValue();
                                if (oRemark != null && oRemark.length() > orderRemark) {
                                    name.append("???????????????");
                                } else {
                                    order.setOrderRemark(oRemark);
                                }
                            }
                            row.getCell(21).setCellType(Cell.CELL_TYPE_STRING);
                            String invoiceFlag = row.getCell(21).getStringCellValue();
                            try {
                                if (invoiceFlag == null || invoiceFlag == "") {
                                    name.append("?????????????????????????????????");
                                } else {
                                    invoice.setInvoiceFlag(invoiceFlag);
                                }
                            } catch (Exception e) {
                                name.append("?????????????????????????????????");
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                row.getCell(22).setCellType(Cell.CELL_TYPE_STRING);
                                String uName = row.getCell(22).getStringCellValue();
                                if (uName == null || uName.length() > unitName || uName == "") {
                                    name.append("????????????????????????????????????");
                                } else {
                                    invoice.setUnitName(uName);
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                row.getCell(23).setCellType(Cell.CELL_TYPE_STRING);
                                String fg = row.getCell(23).getStringCellValue();
                                try {
                                    if (fg == null || fg == "") {
                                        name.append("???????????????????????????");
                                    } else {
                                        invoice.setFlagStr(fg);
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                Cell cell4 = row.getCell(24);
                                if (cell4 != null) {
                                    cell4.setCellType(cell4.CELL_TYPE_STRING);
                                    String iNumber = cell4.getStringCellValue();
                                    if (iNumber != null && iNumber.length() > invoiceNumber) {
                                        name.append("?????????????????????????????????");
                                    } else {
                                        invoice.setInvoiceNumber(iNumber);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                Cell cell5 = row.getCell(25);
                                if (cell5 != null) {
                                    cell5.setCellType(cell5.CELL_TYPE_STRING);
                                    String uSite = cell5.getStringCellValue();
                                    if (uSite != null && uSite.length() > invoiceNumber) {
                                        name.append("??????/?????????????????????");
                                    } else {
                                        invoice.setUnitSite(uSite);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                Cell cell6 = row.getCell(26);
                                if (cell6 != null) {
                                    cell6.setCellType(cell6.CELL_TYPE_STRING);
                                    String bDeposit = cell6.getStringCellValue();
                                    if (bDeposit != null && bDeposit.length() > bankDeposit) {
                                        name.append("?????????????????????????????????");
                                    } else {
                                        invoice.setBankDeposit(bDeposit);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                Cell cell7 = row.getCell(27);
                                if (cell7 != null) {
                                    cell7.setCellType(cell7.CELL_TYPE_STRING);
                                    String tSite = cell7.getStringCellValue();
                                    if (tSite != null && tSite.length() > ticketSite) {
                                        name.append("???????????????????????????");
                                    } else {
                                        invoice.setTicketSite(tSite);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                Cell cell8 = row.getCell(28);
                                if (cell8 != null) {
                                    cell8.setCellType(cell8.CELL_TYPE_STRING);
                                    String ema = cell8.getStringCellValue();
                                    if (ema != null && ema.length() > email) {
                                        name.append("???????????????????????????");
                                    } else {
                                        invoice.setEmail(ema);
                                    }
                                }
                            }
                            if (invoice.getInvoiceFlag().equals("??????")) {
                                row.getCell(29).setCellType(Cell.CELL_TYPE_STRING);
                                String iType = row.getCell(29).getStringCellValue();
                                try {
                                    if (iType == null || iType == "") {
                                        name.append("???????????????????????????");
                                    } else {
                                        invoice.setInvoiceTypeStr(iType);
                                    }
                                } catch (Exception e) {
                                    name.append("??????????????????????????????");
                                }
                            }
                            if (order.getExpressOnly() == 1) {
                                Cell cell4 = row.getCell(30);
                                if (cell4 != null) {
                                    cell4.setCellType(cell4.CELL_TYPE_STRING);
                                    String expressName = cell4.getStringCellValue();
                                    if (expressName.length() > 11) {
                                        name.append("?????????????????????????????????");
                                    } else {
                                        express.setExpressName(expressName);
                                    }
                                }
                                Cell cell5 = row.getCell(31);
                                if (cell5 != null) {
                                    cell5.setCellType(cell5.CELL_TYPE_STRING);
                                    String expressNumbers = cell5.getStringCellValue();
                                    if (expressNumbers.length() > 200) {
                                        name.append("?????????????????????????????????");
                                    } else {
                                        express.setExpressNumbers(expressNumbers);
                                    }
                                }
                            }
                            row.getCell(32).setCellType(Cell.CELL_TYPE_STRING);
                            String gName = row.getCell(32).getStringCellValue();
                            if (gName == null || gName.length() > goodsName || gName == "") {
                                name.append("????????????????????????????????????");
                            } else {
                                Product product = productMapper.findByPName(gName);
                                if (product.getName() == null) {
                                    name.append("?????????????????????");
                                } else {
                                    goods.setGoodsId(String.valueOf(product.getId()));
                                    goods.setGoodsName(product.getName());
                                    goods.setGoodsCore(product.getCore());
                                }
                            }
                            row.getCell(33).setCellType(Cell.CELL_TYPE_STRING);
                            String gPrice = row.getCell(33).getStringCellValue();
                            try {
                                if (gPrice == null || gPrice.length() > goodsPrice || gPrice == "") {
                                    name.append("????????????????????????????????????");
                                } else {
                                    goods.setGoodsPrice(Double.parseDouble(gPrice));
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            row.getCell(34).setCellType(Cell.CELL_TYPE_STRING);
                            String gNumber = row.getCell(34).getStringCellValue();
                            try {
                                if (gNumber == null || gNumber == "") {
                                    name.append("???????????????????????????");
                                } else {
                                    goods.setGoodsNumber(Integer.parseInt(gNumber));
                                }
                            } catch (Exception e) {
                                name.append("???????????????????????????");
                            }
                            order.setExpress(express);
                            order.setInvoice(invoice);
                            goodsList.add(goods);
                            order.setGoodsDetail(goodsList);
                        }
                    } catch (Exception e) {
                        log.error("{}", e);
                        name.append("???????????????????????????");
                        if (name.length() > 7) {
                            //?????????????????????????????????list
                            list.add(name);
                        }
                        name = null;
                        name = new StringBuffer();
                    }
                    if (name.length()-String.valueOf(x).length() > 7) {
                        //?????????????????????????????????list
                        list.add(name);
                    }
                    name = null;
                    name = new StringBuffer();
                }
                List<Goods> goodsDetail = order.getGoodsDetail();
                //????????????????????????
                double now = 0;
                if (goodsDetail != null) {
                    for (Goods goods1 : goodsDetail) {
                        //?????????????????????
                        if (!StringUtils.isEmpty(goods1.getGoodsName())) {
                            double mul = BigDecimalUtil.mul(goods1.getGoodsPrice(), goods1.getGoodsNumber());
                            now = BigDecimalUtil.add(now, mul);
                        }
                    }
                }
                if (order.getOrderTime() == null) {
                    name.append("???????????????????????????");
                    if (name.length() > 7) {
                        //?????????????????????????????????list
                        list.add(name);
                    }
                    name = null;
                }
                order.setSumMoney(now);
                orders.add(order);
                x = 0;
                if (list.size() < 1) {
                    //??????1?????????????????? ??????????????????
                    //????????????
                    if (orders != null && orders.size() > 0) {
                        return orderService.leadOrder(orders, uId, fileName);
                    }
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "excel????????????,?????????excel????????????????????????,???????????????!", null);
        }
        return new ResponseData(400, "????????????????????????????????????????????????", list);
    }

    /**
     * ????????????
     *
     * @param file
     * @param request
     * @return
     */
    @Override
    public ResponseData uploadProduct(MultipartFile file, HttpServletRequest request) throws IOException {
        String uId = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        List<StringBuffer> list = new ArrayList<>();
        // ??????????????????
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        InputStream ins = file.getInputStream();
        Workbook wb = null;
        if (suffix.equals("xlsx")) {
            wb = new XSSFWorkbook(ins);
        } else if (suffix.equals("xls")) {
            wb = new HSSFWorkbook(ins);
        } else {
            //???????????????????????? ????????????????????????
            return new ResponseData(400, "????????????Xlsx???xls????????????", null);
        }
        StringBuffer name = new StringBuffer();
        int x = 3;
        /**
         * ??????excel??????
         */
        Sheet sheet = wb.getSheetAt(0);
        //line = 1 :????????????2?????????????????????
        ExcelProduct excelProduct = new ExcelProduct();
        List<ExcelProduct> excelProducts = new ArrayList<>();
        try {
            if (null != sheet) {
                for (int line = 2; line <= sheet.getLastRowNum(); line++) {
                    Row row = sheet.getRow(line);
                    if (null == row) {
                        name.delete(0, name.length());
                        name.append("???");
                        name.append(x);
                        name.append("?????????,");
                        name.append("???????????????????????????");
                        x++;
                        if (name.length() > 7) {
                            //?????????????????????????????????list
                            list.add(name);
                        }
                        name = new StringBuffer();
                        continue;
                    }
                    name.append("???");
                    name.append(x);
                    name.append("?????????,");
                    x++;
                    excelProduct.setAddUser(uId);//???????????????
                    excelProduct.setAddTime(df.parse(df.format(new Date())));//??????????????????
                    Cell cell2 = row.getCell(0);
                    String productName = "";
                    if (cell2 == null) {
                        name.append("???????????????????????????");
                        excelProduct.setName(productName);
                    } else {
                        cell2.setCellType(Cell.CELL_TYPE_STRING);
                        productName = cell2.getStringCellValue();
                        if (productName == null || productName.equals("")) {
                            name.append("???????????????????????????");
                            excelProduct.setName(productName);
                        } else {
                            excelProduct.setName(productName);
                        }
                    }
                    String productCore = "";
                    Cell cell3 = row.getCell(1);
                    if (cell3 == null) {
                        name.append("???????????????????????????");
                        excelProduct.setCore(productCore);
                    } else {
                        cell3.setCellType(Cell.CELL_TYPE_STRING);
                        productCore = cell3.getStringCellValue();
                        if (productCore == null || productCore.equals("")) {
                            name.append("???????????????????????????");
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
                                    name.append("?????????????????????");
                                }
                                excelProduct.setPrice(Double.parseDouble(productPrice));
                            }
                        } catch (Exception e) {
                            name.append("???????????????????????????");
                        }
                    }
                    Cell cell4 = row.getCell(3);
                    if (cell4 == null) {
                        name.append("???????????????????????????");
                    } else {
                        cell4.setCellType(Cell.CELL_TYPE_STRING);
                        String productStatus = cell4.getStringCellValue();
                        if (productStatus == null || productStatus.equals("")) {
                            name.append("???????????????????????????");
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
                            name.append("?????????????????????");
                        }
                    }
                    Cell cell5 = row.getCell(5);
                    if (cell5 == null) {
                        name.append("???????????????????????????");
                    } else {
                        cell5.setCellType(Cell.CELL_TYPE_STRING);
                        String classifyStr = cell5.getStringCellValue();
                        if (classifyStr == null || classifyStr.equals("")) {
                            name.append("???????????????????????????");
                        } else {
                            Integer classifyId = classifyMapper.findById(classifyStr);//??????????????????????????????ID
                            if (classifyId == null) {
                                name.append("????????????????????????????????????????????????");
                            } else {
                                excelProduct.setClassify_id(classifyId);
                            }
                        }
                    }
                    //??????????????????????????????????????????????????????????????????????????????
                    List<Product> products = productMapper.getAll();
                    if (RepetitionUtil.checkForDuplicates(productName, productCore, products)) {
                        name.append("???????????????????????????????????????????????????");
                    }
                    if (name.length()-String.valueOf(x).length() > 7) {
                        //?????????????????????????????????list
                        list.add(name);
                    }
                    //????????????name??????
                    name = new StringBuffer();
                    //?????????????????????????????????
                    excelProducts.add(excelProduct);
                    //????????????excelProduct??????
                    excelProduct = new ExcelProduct();
                }
                long count = RepetitionUtil.ifNameByCore(excelProducts);
                if (count > 0) {
                    list.add(new StringBuffer("??????????????????????????????????????????"));
                }
            }
            if (list.size() > 0) {
                //??????list????????????0??????????????????
                return new ResponseData(400, "???????????????????????????", list);
            }
            //???????????????0???????????????
            if (excelProducts != null && excelProducts.size() > 0) {
                return productService.leadProduct(excelProducts);
            }
            return new ResponseData(400,"????????????????????????????????????????????????",list);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "excel????????????,?????????excel????????????????????????,???????????????!", null);
        }
    }
}
