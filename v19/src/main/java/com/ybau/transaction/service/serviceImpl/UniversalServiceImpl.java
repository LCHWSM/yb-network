package com.ybau.transaction.service.serviceImpl;

import com.ybau.transaction.domain.Universal;
import com.ybau.transaction.mapper.UniversalMapper;
import com.ybau.transaction.service.UniversalService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversalServiceImpl implements UniversalService {


    @Autowired
    UniversalMapper universalMapper;

    /**
     * 修改通用预警
     *
     * @param red
     * @param yellow
     * @return
     */
    @Override
    public ResponseData updateWarning(int red, int yellow) {
        ResponseData result = new ResponseData();
        universalMapper.updateWarning(red, yellow);
        result.setCode(200);
        result.setMsg("修改成功");
        return result;
    }

    /**
     * 查询通用库存预警
     *
     * @return
     */
    @Override
    public ResponseData findAll() {
        Universal universal = universalMapper.findAll();
        return new ResponseData(200,"查询成功",universal);
    }
}
