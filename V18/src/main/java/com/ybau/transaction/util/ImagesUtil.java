package com.ybau.transaction.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImagesUtil {

    /**
     * 图片路径替换
     *
     * @param url
     * @return
     */
    @Value("${ftp.bastPath}")
    private String bastPath;

    @Value("${ftp.visitUrl}")
    private String visitUrl;

    public  String getUrl(String url) {
        String url1 = url.replace(visitUrl + bastPath + "/", "");
        return url1;
    }

}
