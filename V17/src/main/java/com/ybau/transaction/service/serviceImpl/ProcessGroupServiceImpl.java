package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.AuditFlow;
import com.ybau.transaction.domain.ProcessGroup;
import com.ybau.transaction.mapper.ProcessGroupMapper;
import com.ybau.transaction.service.ProcessGroupService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ProcessGroupServiceImpl implements ProcessGroupService {

    @Autowired
    ProcessGroupMapper processGroupMapper;


    /**
     * 根据公司ID查询所有流程组
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        PageHelper.startPage(pageNum, pageSize);
        List<ProcessGroup> processGroups = processGroupMapper.findAll(map);
        PageInfo pageInfo = new PageInfo(processGroups);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 切换公司目前所使用的流程组
     *
     * @param organizationId
     * @param processGroupId
     * @return
     */
    @Override
    public ResponseData saveOrganization(Integer organizationId, Integer processGroupId) {
        processGroupMapper.saveOrganization(organizationId, processGroupId);
        return new ResponseData(200, "切换成功", null);
    }

    /**
     * 根据流程组ID 修改流程组名字
     *
     * @param processGroupId
     * @param processGroupName
     * @return
     */
    @Override
    public ResponseData updateName(Integer processGroupId, String processGroupName) {
        processGroupMapper.updateName(processGroupId, processGroupName);
        return new ResponseData(200,"修改成功",null);
    }
}
