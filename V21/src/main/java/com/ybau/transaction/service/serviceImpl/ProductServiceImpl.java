package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.ExcelProduct;
import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.domain.Product;
import com.ybau.transaction.domain.Universal;
import com.ybau.transaction.mapper.GoodsMapper;
import com.ybau.transaction.mapper.ProductMapper;
import com.ybau.transaction.service.LogService;
import com.ybau.transaction.service.ProductService;
import com.ybau.transaction.util.ImagesUtil;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品service层
 */
@Service
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    LogService logService;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    @Autowired
    ImagesUtil imagesUtil;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    RedissonClient client;


    /**
     * 新增商品
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveProduct(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");//获取token
        String id = jwtUtil.getId(token);//获取添加人用户名并存入Map
        try {
            Double price = Double.valueOf((String) map.get("price"));
            if (price < 1) {
                return new ResponseData(400, "商品价格不可低于1元", null);
            }
        } catch (Exception e) {
            log.error("金额参数有误{}", e);
            return new ResponseData(400, "金额设置有误", null);
        }
        map.put("addUser", id);
        if (map.containsKey("images")) {
            String images = imagesUtil.getUrl((String) map.get("images"));
            map.put("images", images);
        }
        try {
            map.put("addTime", df.parse(df.format(new Date())));//存入Map
            productMapper.saveProduct(map);
            return new ResponseData(200, "新增成功", null);
        } catch (Exception e) {
            log.error("商品名字或编码重复{}", e);
            return new ResponseData(400, "新增失败，商品名字或编码已存在", null);
        }
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData deleteProduct(int id) {
        int count = goodsMapper.findByPId(id);
        if (count > 0) {
            return new ResponseData(400, "该商品已被下单，不可删除", null);
        }
        productMapper.deleteProduct(id);
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 修改商品
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateProduct(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");//取出请求头token获取用户名
        String id = jwtUtil.getId(token);
        try {
            Double price = Double.valueOf((String) map.get("price"));
            if (price < 1) {
                return new ResponseData(400, "商品价格不可低于1元", null);
            }
        } catch (Exception e) {
            return new ResponseData(400, "金额设置有误", null);
        }
        map.put("updateUser", id);
        if (map.containsKey("images")) {
            String images = imagesUtil.getUrl((String) map.get("images"));
            map.put("images", images);
        }
        try {
            map.put("updateTime", df.parse(df.format(new Date())));//修改时间存入Map
            productMapper.updateProduct(map);
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            log.error("字段内容重复{}", e);
            return new ResponseData(400, "商品名字或编码已存在，修改失败", null);
        }
    }

    /**
     * 商品查询
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        int pageNum = (int) map.get("pageNum");
        int pageSize = (int) map.get("pageSize");
        String warehouse = (String) map.get("warehouse");
        Universal universal = productMapper.findUniversal();
        if (warehouse != null) {
            if (warehouse != null && warehouse.equals("1")) {
                //等于1查询库存充足商品
                map.put("yellow", universal.getYellow());
            } else if (warehouse != null && warehouse.equals("2")) {
                //等于2查询库存预警商品
                map.put("yellow", universal.getYellow());
                map.put("red", universal.getRed());
            } else if (warehouse != null && warehouse.equals("3")) {
                //等于3查询库存不足商品
                map.put("red", universal.getRed());
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.findAll(map);
        for (Product product : products) {
            product.setImages(prefixUrl + bastPath + "/" + product.getImages());
        }
        PageInfo pageInfo = new PageInfo(products);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据ID查询商品
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData findById(int id) {
        Product product = productMapper.findById(id);
        product.setImages(prefixUrl + bastPath + "/" + product.getImages());
        return new ResponseData(200, "查询成功", product);
    }

    /**
     * 查询所有商品（分类 前台展示）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findProduct(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        boolean flag = true;
        RBucket<Object> bucket = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket.get();
        if (permissionStr == null) {
            return new ResponseData(401, "无权限下单", null);
        }
        List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
        for (Permission permission : permissions) {
            switch (permission.getUrl()) {
                case "/order/saveOrder":
                    flag = false;
                    break;
            }
        }
        if (flag) {
            return new ResponseData(401, "无权限下单", null);
        }
        int pageNum = (int) map.get("pageNum");
        int pageSize = (int) map.get("pageSize");
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.findProduct(map);
        for (Product product : products) {
            product.setImages(prefixUrl + bastPath + "/" + product.getImages());
        }
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 更新库存
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateWarehouse(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);//根据token获取用户名
        int flag = (int) map.get("flag");
        map.put("logSubject", map.get("productName"));
        int now = 0;
        try {
            map.put("logTime", df.parse(df.format(new Date())));
            //如果转换异常则直接返回
            now = (int) map.get("now");//获取改变的库存
        } catch (Exception e) {
            log.error("时间转换异常");
            return new ResponseData(400, "库存不可设置为小数", null);
        }
        if (now < 1) {
            //增加或减少库存如果为负数直接返回
            return new ResponseData(400, "库存设置需大于0", null);
        }
        if (flag == 1) {
            //等于1增加库存
            productMapper.updateAddWarehouse(map);
            map.put("logName", "+" + map.get("now"));//增加操作日志
            map.put("logUser", id);
            logService.saveProductLog(map);//调用日志service
            return new ResponseData(200, "修改成功", null);
        } else {
            int i = productMapper.updateSubWarehouse(map);
            if (i < 1) {
                //小于1减少的库存小于已有的库存
                return new ResponseData(400, "减少库存失败，更改的库存不可小于已有库存", null);
            }
            map.put("logName", "-" + now);//增加操作日志
            map.put("logUser", id);
            logService.saveProductLog(map);//调用日志service
            return new ResponseData(200, "修改成功", null);
        }
    }


    /**
     * 修改富文本
     *
     * @param oldUrl
     * @param newUrl
     * @return
     */
    @Override
    public ResponseData updateEditor(String oldUrl, String newUrl) {
        try {
            List<Integer> integers = productMapper.selectProductIdList();
            for (Integer integer : integers) {
                String ss = productMapper.selectDescriptionById(integer);
                String s = updateUrl(ss, oldUrl, newUrl);
                productMapper.updateDescriptionUrl(s, integer);
            }
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "修改失败", null);
        }
    }

    /**
     * 导入商品
     *
     * @param excelProducts
     * @return
     */
    @Override
    public ResponseData leadProduct(List<ExcelProduct> excelProducts) {
        for (ExcelProduct excelProduct : excelProducts) {
            productMapper.leadProduct(excelProduct);
        }
        return new ResponseData(200, "导入成功", null);
    }


    public String updateUrl(String oldUrl, String oldAddress, String newAddress) {
        String replace = oldUrl.replace(oldAddress, newAddress);
        return replace;
    }

}
