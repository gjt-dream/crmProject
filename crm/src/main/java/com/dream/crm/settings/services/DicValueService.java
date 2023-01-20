package com.dream.crm.settings.services;

import com.dream.crm.settings.pojo.DicValue;

import java.util.List;

public interface DicValueService {

    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
