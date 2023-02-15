package com.dream.crm.workbench.services.impl;

import com.dream.crm.workbench.mapper.TranRemarkMapper;
import com.dream.crm.workbench.pojo.TranRemark;
import com.dream.crm.workbench.services.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public List<TranRemark> queryTranRemarkForDetailByTranId(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }
}
