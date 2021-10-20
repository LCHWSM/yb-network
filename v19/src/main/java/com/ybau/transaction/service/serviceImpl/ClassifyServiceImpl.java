package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Classify;
import com.ybau.transaction.mapper.ClassifyMapper;
import com.ybau.transaction.mapper.ProductMapper;
import com.ybau.transaction.service.ClassifyService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分类Service层
 */
@Service
@Slf4j
public class ClassifyServiceImpl implements ClassifyService {
    @Autowired
    ClassifyMapper classifyMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ProductMapper productMapper;


    /**
     * 查询所有分类（未分页）
     *
     * @return
     */
    @Override
    public ResponseData findAll(int pageSize, int pageNum, String classifyName) {
        PageHelper.startPage(pageNum, pageSize);
        List<Classify> classifies = classifyMapper.findAll(classifyName);
        PageInfo pageInfo = new PageInfo(classifies);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改分类
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveClassify(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int count = classifyMapper.findByName((String) map.get("name"));//查询分类名字是否存在
        if (count < 1) {
            //未存在执行插入操作
            String token = request.getHeader("token");
            String id = jwtUtil.getId(token);
            map.put("addUser", id);
            try {
                map.put("addTime", df.parse(df.format(new Date())));
            } catch (Exception e) {
                log.error("时间转换异常" + e);
            }
            List<Classify> classifies = classifyMapper.findClassify();
            if (classifies.size() > 0) {
                for (int i = classifies.size() - 1; i < classifies.size(); i++) {
                    map.put("sort", classifies.get(i).getSort() + 1);
                }
            }else {
                map.put("sort", 1);
            }
            classifyMapper.saveClassify(map);
            return new ResponseData(200,"新增成功",null);
        }
        //大于1名字重复
        return new ResponseData(400,"该分类名字已存在，添加失败",null);
    }

    /**
     * 根据ID删除分类
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData deleteById(int id) {
        int count = productMapper.findByCid(id);//查询分类下是否存在商品
        if (count < 1) {
            //小于1无商品可以删除
            classifyMapper.deleteById(id);
            return new ResponseData(200,"删除成功",null);
        }
        //大于1存在商品直接返回
        return new ResponseData(400,"该分类下存在商品，无法删除",null);
    }

    /**
     * 修改分类
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateClassify(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int count = classifyMapper.findByName((String) map.get("name"));//查询分类名字是否存在
        if (count < 1) {
            //小于1不重复
            String token = request.getHeader("token");//获取用户token
            map.put("updateUser", jwtUtil.getId(token));//获取用户名并存入Map
            try {
                map.put("updateTime", df.parse(df.format(new Date())));//获取本地时间并存入Map
            } catch (Exception e) {
                log.error("时间转换异常" + e);
            }
            classifyMapper.updateClassify(map);
            return new ResponseData(200,"修改成功",null);
        }
        //大于1重复
        return new ResponseData(400,"该分类名字已存在，修改失败",null);
    }

    /**
     * 更改分类排序顺序
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateSort(Map<String, Object> map) {
        classifyMapper.updateSort((int) map.get("upClassifyId"), (int) map.get("nextSort"));
        classifyMapper.updateSort((int) map.get("nextClassifyId"), (int) map.get("upSort"));
        return new ResponseData(200, "调整成功", null);
    }
}
