package com.ybau.transaction.controller;


import com.ybau.transaction.util.CodeUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/code")
@Slf4j
public class CodeController {


    @Autowired
    private RedissonClient client;


    /**
     * 生成验证码发送到前端
     *
     * @param request
     * @param response
     */
    @GetMapping("/getCode")
    public Map<String, Object> getCode(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = CodeUtil.generateCodeAndPic();//调用工具类获取验证码
        RBucket<Object> clientBucket = client.getBucket((String) map.get("uuid"));//用UUID作为redis的key
        clientBucket.set(map.get("code"),5, TimeUnit.MINUTES);//把验证码的值存入到redis
        map.remove("code");
        return map;

    }





}
