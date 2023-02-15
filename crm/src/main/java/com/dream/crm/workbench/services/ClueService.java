package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map<String,Object> map);

    int queryCountOfClueByCondition(Map<String,Object> map);

    int deleteClueById(String[] ids);

    Clue queryClueById(String id);

    int saveEditClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveConvertClue(Map<String,Object> map);


}
