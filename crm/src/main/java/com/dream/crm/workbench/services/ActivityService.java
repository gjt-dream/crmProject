package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int SaveCreateActivity(Activity activity);

    List<Activity> QueryActivityConditionForPage(Map<String,Object> map);

    int QueryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityById(String[] ids);

    Activity selectByPrimaryKey(String id);

    int saveEditActivity(Activity activity);

    List<Activity> queryAllActivities();

    List<Activity> queryActivitiesById(String[] ids);

    int saveCreateActivityByList(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);
}
