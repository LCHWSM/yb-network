package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import java.util.Map;

public interface ProcessGroupService {
    /**
     * 根据公司ID查询所有流程组
     * @param map
     * @return
     */
    ResponseData findAll(Map<String, Object> map);

    /**
     * 切换公司目前所使用的流程组
     * @param organizationId
     * @param processGroupId
     * @return
     */
    ResponseData saveOrganization(Integer organizationId, Integer processGroupId);

    /**
     * 根据流程组ID 更改流程组名字
     * @param processGroupId
     * @param processGroupName
     * @return
     */
    ResponseData updateName(Integer processGroupId, String processGroupName);
}
