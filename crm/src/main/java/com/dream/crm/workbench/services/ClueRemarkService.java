package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailById(String id);

    int saveCreateClueRemark(ClueRemark clueRemark);

    int deleteByPrimaryKey(String id);

    int saveEditByPrimaryKey(ClueRemark clueRemark);
}
