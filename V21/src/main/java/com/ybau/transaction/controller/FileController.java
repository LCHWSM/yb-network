package com.ybau.transaction.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.fastjson.JSONObject;
import com.ybau.transaction.domain.ExcelOrder;
import com.ybau.transaction.domain.ExcelProduct;
import com.ybau.transaction.mapper.OrderMapper;
import com.ybau.transaction.mapper.ProductMapper;
import com.ybau.transaction.service.FileService;
import com.ybau.transaction.util.*;

import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传web层
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    FileService fileService;

    @Value("${ftp.bastPath}")
    String baseDir;
    //app/yb_network

    @Autowired
    FtpUpload ftpUpload;

    @Value("${ftp.visitUrl}")
    String visitUrl;
    //http://172.16.68.3:8069/

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ProductMapper productMapper;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)

    public ResponseData uploadMachineImg(@RequestParam(name = "file") MultipartFile[] file) throws IOException {
        StringBuilder builder = new StringBuilder();
        ResponseData responseData = new ResponseData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String format = sdf.format(date);
        String[] split = format.split("/");
        for (MultipartFile file1 : file) {
            String fileName = file1.getOriginalFilename();
            String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
            String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
            String url = baseDir + "/" + split[0] + "/" + split[1] + "/" + split[2] + "/";
            boolean result = ftpUpload.uploadFile(url, uploadFileName, file1.getInputStream());
            if (result) {
                builder.append(visitUrl + url + uploadFileName);
            }
            if (!result) {
                responseData.setMsg("上传失败");
                responseData.setCode(400);
                return responseData;
            }
        }
        responseData.setData(builder.toString());
        responseData.setCode(200);
        return responseData;
    }


    @RequestMapping(value = "/deleteMachineImg", method = RequestMethod.GET)
    public ResponseData deleteMachineImg(String fileName) {
        ResponseData responseData = new ResponseData();
        int i1 = fileName.lastIndexOf("/");
        String pathName = fileName.substring(i1 + 1);
        String[] split = fileName.split("/");
        String url = baseDir + "/" + split[5] + "/" + split[6] + "/" + split[7];
        boolean result = ftpUpload.deleteFile(url, pathName);
        if (!result) {
            responseData.setData("删除失败");
            responseData.setCode(400);
            return responseData;
        }
        responseData.setData("删除成功");
        responseData.setCode(200);
        return responseData;
    }

    /**
     * 上传合同文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadContract", method = RequestMethod.POST)
    public ResponseData uploadContract(@RequestParam(name = "file") MultipartFile[] file) throws IOException {
        StringBuilder builder = new StringBuilder();
        ResponseData responseData = new ResponseData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Map<String, Object> map = new HashMap<>();
        String format = sdf.format(date);
        String[] split = format.split("/");
        for (MultipartFile file1 : file) {
            String fileName = file1.getOriginalFilename();
            String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
            String uploadFileName = UUID.randomUUID().toString().substring(0, 20) + "." + fileExtensionName;
            String url = baseDir + "/" + split[0] + "/" + split[1] + "/" + split[2] + "/";
            boolean result = ftpUpload.uploadFile(url, uploadFileName, file1.getInputStream());
            if (result) {
                builder.append(visitUrl + url + uploadFileName);
            }
            if (!result) {
                responseData.setData("上传失败");
                responseData.setCode(400);
                return responseData;
            }
            map.put("fileName", fileName);
        }
        map.put("path", builder.toString());
        responseData.setData(map);
        responseData.setCode(200);
        return responseData;
    }

    /**
     * 文件下载
     *
     * @param path
     * @param filename
     * @param response
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/downloadFile")
    public HttpServletResponse downloadNet(String path, String filename, HttpServletResponse response) throws MalformedURLException {
        URL url = new URL(path);
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("UTF-8"), "ISO8859_1"));
            URLConnection conn = url.openConnection();
            InputStream fis = conn.getInputStream();
            ServletOutputStream os = response.getOutputStream();
            IOUtils.copy(fis, os);
            os.flush();
            os.close();
            return null;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    /**
     * 订单导入功能
     *
     * @param file
     * @return
     */

    @PostMapping("/doImportExcel")
    public ResponseData doImportExcel(MultipartFile file, HttpServletRequest request) throws Exception {
        return fileService.doImportExcel(file, request);
    }


    /**
     * 根据时间订单导出功能
     *
     * @param startDate
     * @param endDate
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/complete")
    public void exportXlsComplete(@RequestParam(name = "startDate") String startDate,
                                  @RequestParam(name = "endDate") String endDate,
                                  @RequestParam(name = "proposer") String proposer,
                                  @RequestParam(name = "goodsName") String goodsName, HttpServletResponse response) throws Exception {

        String s = DateUtli.addOneday(endDate);
        List<ExcelOrder> startByEnd = orderMapper.findStartByEnd(startDate, s, proposer, goodsName);
        for (ExcelOrder excelOrder : startByEnd) {
            excelOrder.setAddUser(excelOrder.getAddUser() + "(" + excelOrder.getName() + ")");
        }
        // 简单模板导出方法
        cn.afterturn.easypoi.excel.entity.ExportParams params = new cn.afterturn.easypoi.excel.entity.ExportParams();
        params.setStyle(ExcelExportStyler.class);
        params.setTitle(startDate + "---" + endDate + "订单数据汇总");//设置表头
        params.setSheetName("订单数据");//设置sheet名
        Workbook workbook = ExcelExportUtil.exportExcel(params, ExcelOrder.class, startByEnd);
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("sheet1");
        HSSFSheet sheet2 = (HSSFSheet) workbook.createSheet("sheet2");
        this.setExportExcelFormat(response, workbook, startDate + "---" + endDate + "订单数据汇总");
    }

    /**
     * 响应数据 * @param response * @param workbook * @param fileName * @throws Exception
     */
    public void setExportExcelFormat(HttpServletResponse response, Workbook workbook, String fileName) throws Exception {
        ServletOutputStream out = null;
        try {

            out = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859_1"));
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            out.write(baos.toByteArray());
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 商品信息导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadProduct")
    public ResponseData uploadProduct(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return fileService.uploadProduct(file, request);
    }

    /**
     * 录入退款文件
     *
     * @param file    上传文件
     * @param request token
     * @return
     */
    @PostMapping("/uploadRefund")
    public ResponseData uploadRefund(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return fileService.uploadRefund(file, request);
    }

    /**
     * 商品导出功能
     * @param classifyId
     * @throws Exception
     */
    @GetMapping(value = "/exportProduct")
    public void exportProduct(@RequestParam(name = "classifyId") String classifyId,HttpServletResponse response) throws Exception {
        List<ExcelProduct> excelProducts = productMapper.exportProduct(classifyId);//查询数据
        for (ExcelProduct excelProduct : excelProducts) {
            excelProduct.setAddUser(excelProduct.getAddUser() + "(" + excelProduct.getAddUserName() + ")");
        }
        cn.afterturn.easypoi.excel.entity.ExportParams params = new cn.afterturn.easypoi.excel.entity.ExportParams();
        params.setStyle(ExcelExportStyler.class);
        params.setTitle( "商品数据汇总");//设置表头
        params.setSheetName("商品数据");//设置sheet名
        Workbook workbook = ExcelExportUtil.exportExcel(params, ExcelProduct.class, excelProducts);
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("sheet1");
        HSSFSheet sheet2 = (HSSFSheet) workbook.createSheet("sheet2");
        this.setExportExcelFormat(response, workbook, "订单数据汇总");
    }

}



