package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);

}
