package com.ybau.transaction.service.serviceImpl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.ybau.transaction.domain.Template;
import com.ybau.transaction.mapper.TemplateMapper;
import com.ybau.transaction.service.TemplateService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    TemplateMapper templateMapper;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    /**
     * 插入模板路径
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveTemplate(Map<String, Object> map) {
        templateMapper.deleteTemplate();//删除原有模板
        Map<String, Object> mapUrl = (Map<String, Object>) map.get("templateUrl");
        String path = (String) mapUrl.get("path");
        String pathStr = path.replace(prefixUrl + bastPath + "/", "[]");
        mapUrl.put("path", pathStr);
        map.put("url", JSONUtils.toJSONString(mapUrl));
        templateMapper.saveTemplate(map);
        return new ResponseData(200, "上传成功", null);
    }

    /**
     * 查询已上传的订单模板
     *
     * @return
     */
    @Override
    public ResponseData findAll() {
        List<Template> templates = templateMapper.findAll();
        for (Template template : templates) {
            String templateUrl = template.getTemplateUrl();
            Map<String, Object> parse = (Map<String, Object>) JSONUtils.parse(templateUrl);
            String replace = parse.get("path").toString().replace( "[]",prefixUrl + bastPath + "/");
            parse.put("path", replace);
            template.setTemplateUrl(JSONUtils.toJSONString(parse));
        }

        return new ResponseData(200, "查询成功", templates);
    }
}
