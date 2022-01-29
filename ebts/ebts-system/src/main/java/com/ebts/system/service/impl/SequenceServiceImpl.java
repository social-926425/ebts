package com.ebts.system.service.impl;

import com.ebts.system.dao.SequenceDao;
import com.ebts.system.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 18209
 * @Date 2021/2/24 17:56
 * @Version 1.0
 */
@Service
public class SequenceServiceImpl implements SequenceService {
    private Logger logger = LoggerFactory.getLogger(SequenceServiceImpl.class);

    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Long currval(String tableName) {
        return sequenceDao.currval(tableName);
    }

    @Override
    public Long nextval(String tableName) {
        return sequenceDao.nextval(tableName);
    }
}
