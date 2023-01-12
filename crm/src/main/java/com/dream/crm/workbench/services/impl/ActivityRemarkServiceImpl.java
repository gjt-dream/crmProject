package com.dream.crm.workbench.services.impl;

import com.dream.crm.workbench.mapper.ActivityRemarkMapper;
import com.dream.crm.workbench.pojo.ActivityRemark;
import com.dream.crm.workbench.services.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailById(String id) {
        return activityRemarkMapper.selectActivityRemarkForDetailById(id);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.insertActivityRemark(remark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ActivityRemark queryById(String id) {
        return activityRemarkMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveEditActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateActivityRemark(activityRemark);
    }
}
