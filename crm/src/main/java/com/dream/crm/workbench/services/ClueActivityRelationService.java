package com.dream.crm.workbench.services;

import com.dream.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);


    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);

}
