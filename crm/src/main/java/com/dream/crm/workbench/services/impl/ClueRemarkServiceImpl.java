package com.dream.crm.workbench.services.impl;

import com.dream.crm.workbench.mapper.ClueRemarkMapper;
import com.dream.crm.workbench.pojo.ClueRemark;
import com.dream.crm.workbench.services.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailById(String id) {
        return clueRemarkMapper.selectClueRemarkForDetailById(id);
    }

    @Override
    public int saveCreateClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return clueRemarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int saveEditByPrimaryKey(ClueRemark clueRemark) {
        return clueRemarkMapper.updateClueRemark(clueRemark);
    }
}
