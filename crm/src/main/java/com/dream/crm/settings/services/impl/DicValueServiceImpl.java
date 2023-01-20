package com.dream.crm.settings.services.impl;

import com.dream.crm.settings.mapper.DicValueMapper;
import com.dream.crm.settings.pojo.DicValue;
import com.dream.crm.settings.services.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
