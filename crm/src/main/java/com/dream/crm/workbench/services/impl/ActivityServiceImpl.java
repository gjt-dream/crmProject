package com.dream.crm.workbench.services.impl;

import com.dream.crm.workbench.mapper.ActivityMapper;
import com.dream.crm.workbench.pojo.Activity;
import com.dream.crm.workbench.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public int SaveCreateActivity(Activity activity) {
        int insertActivity = activityMapper.insertActivity(activity);
        return insertActivity;
    }

    @Override
    public List<Activity> QueryActivityConditionForPage(Map<String, Object> map) {
        List<Activity> activityList = activityMapper.selectActivityByConditionForPage(map);
        return activityList;
    }

    @Override
    public int QueryCountOfActivityByCondition(Map<String, Object> map) {
        int count = activityMapper.selectCountOfActivityByCondition(map);
        return count;
    }

    @Override
    public int deleteActivityById(String[] ids) {
        return activityMapper.deleteByPrimaryKey(ids);
    }

    @Override
    public Activity selectByPrimaryKey(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateByPrimaryKey(activity);
    }

    @Override
    public List<Activity> queryAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public List<Activity> queryActivitiesById(String[] ids) {
        return activityMapper.selectActivitiesById(ids);
    }

    @Override
    public int saveCreateActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }


}
