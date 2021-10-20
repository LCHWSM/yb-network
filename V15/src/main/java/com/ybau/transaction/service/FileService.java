package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {
    ResponseData doImportExcel(MultipartFile file,HttpServletRequest request) throws Exception;



}
