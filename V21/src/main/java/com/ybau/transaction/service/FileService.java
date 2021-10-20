package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    ResponseData doImportExcel(MultipartFile file,HttpServletRequest request) throws Exception;


    ResponseData uploadProduct(MultipartFile file, HttpServletRequest request) throws IOException;

    ResponseData uploadRefund(MultipartFile file, HttpServletRequest request) throws IOException;

}
