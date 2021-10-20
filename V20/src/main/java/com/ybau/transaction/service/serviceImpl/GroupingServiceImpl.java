package com.ybau.transaction.service.serviceImpl;

import com.ybau.transaction.domain.Grouping;
import com.ybau.transaction.mapper.GroupingMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.GroupingService;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class GroupingServiceImpl implements GroupingService {

    @Autowired
    GroupingMapper groupingMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 新增部门分组
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveGrouping(Map<String, Object> map) {
        try {
            groupingMapper.saveGrouping(map);
            return new ResponseData(200, "新增成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "分组名字已存在，新增失败", null);
        }

    }

    /**
     * 修改部门分组
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateGrouping(Map<String, Object> map) {
        try {
            groupingMapper.updateGrouping(map);
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "分组名字已存在，修改失败", null);
        }

    }

    /**
     * 根据分组ID删除分组
     *
     * @param groupingId
     * @return
     */
    @Override
    public ResponseData deleteGrouping(int groupingId) {
        int count = userMapper.findByGroupingId(groupingId);
        if (count > 0) {
            return new ResponseData(400, "该分组下存在用户删除失败", null);
        }
        groupingMapper.deleteGrouping(groupingId);
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 查询分组下用户列表
     *
     * @return
     */
    @Override
    public ResponseData findByUser() {
        List<Grouping> groupings = groupingMapper.findByUser();
        return new ResponseData(200, "查询成功", groupings);
    }
}
