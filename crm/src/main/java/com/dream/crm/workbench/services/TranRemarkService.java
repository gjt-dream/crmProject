package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.TranRemark;

import java.util.List;

public interface TranRemarkService {
    List<TranRemark> queryTranRemarkForDetailByTranId(String tranId);

}
