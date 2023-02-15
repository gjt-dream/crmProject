package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.Clue;
import com.dream.crm.workbench.pojo.FunnelVO;
import com.dream.crm.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

    List<Clue> queryTranByConditionForPage(Map<String,Object> map);

    int queryCountOfTranByCondition(Map<String,Object> map);

    int deleteTranByPrimaryKey(String[] ids);

    Tran queryByPrimaryKey(String id);

    int saveEditTran(Tran tran);
}
