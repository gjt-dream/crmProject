package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailById(String id);

    int saveCreateActivityRemark(ActivityRemark remark);

    int deleteActivityRemarkById(String id);

    ActivityRemark queryById(String id);

    int saveEditActivityRemark(ActivityRemark activityRemark);
}
