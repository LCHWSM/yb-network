package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.SORecord;
import com.ybau.transaction.mapper.SORecordMapper;
import com.ybau.transaction.service.SORecordService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional
public class SORecordServiceImpl implements SORecordService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    SORecordMapper soRecordMapper;

    /**
     * 根据用户ID查询所有用户填写的供应商商品信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findAllById(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        if (map.get("pageNum") != null && map.get("pageSize") != null) {
            PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        }
        List<SORecord> soRecords = soRecordMapper.findAllById(map, id);
        PageInfo pageInfo = new PageInfo(soRecords);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改记录的商品信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateRecord(Map<String, Object> map) {
        soRecordMapper.updateRecord(map);
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 删除已添加商品记录
     *
     * @param soRecordId
     * @return
     */
    @Override
    public ResponseData deleteRecord(int soRecordId) {
        soRecordMapper.deleteRecord(soRecordId);
        return new ResponseData(200,"删除成功",null);
    }
}
