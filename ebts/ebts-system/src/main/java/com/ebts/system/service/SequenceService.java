package com.ebts.system.service;

/**
 * @Author 18209
 * @Date 2021/2/24 17:56
 * @Version 1.0
 */
public interface SequenceService {
    /**
     * 当前值
     * @param tableName
     * @return
     */
    Long currval(String tableName);

    /**
     * 下一个值
     * @param tableName
     * @return
     */
    Long nextval(String tableName);

}
