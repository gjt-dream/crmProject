package com.dream.crm.workbench.services.impl;

import com.dream.crm.workbench.mapper.TranHistoryMapper;
import com.dream.crm.workbench.pojo.TranHistory;
import com.dream.crm.workbench.services.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranHistoryServiceImpl implements TranHistoryService {
    @Autowired
    private TranHistoryMapper tranHistoryMapper;
    @Override
    public List<TranHistory> queryTranHistoryForDetailByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryForDetailByTranId(tranId);
    }
}
