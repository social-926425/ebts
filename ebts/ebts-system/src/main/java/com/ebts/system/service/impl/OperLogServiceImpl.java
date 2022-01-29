package com.ebts.system.service.impl;

import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.system.entity.OperLog;
import com.ebts.system.dao.OperLogDao;
import com.ebts.system.service.OperLogService;

/**
 * 操作日志 服务层处理
 *
 * @author binlin
 */
@Service
public class OperLogServiceImpl implements OperLogService {
    private Logger logger = LoggerFactory.getLogger(OperLogServiceImpl.class);

    @Autowired
    private OperLogDao operLogMapper;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(OperLog operLog) {
        try {
            operLogMapper.insertOperlog(operLog);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<OperLog> selectOperLogList(OperLog operLog) {
        try {
            return operLogMapper.selectOperLogList(operLog);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        try {
            return operLogMapper.deleteOperLogByIds(operIds);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public OperLog selectOperLogById(Long operId) {
        try {
            return operLogMapper.selectOperLogById(operId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        try {
            operLogMapper.cleanOperLog();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }
}
