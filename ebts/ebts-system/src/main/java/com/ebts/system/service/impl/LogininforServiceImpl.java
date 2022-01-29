package com.ebts.system.service.impl;

import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.system.entity.Logininfor;
import com.ebts.system.dao.LogininforDao;
import com.ebts.system.service.LogininforService;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author binlin
 */
@Service
public class LogininforServiceImpl implements LogininforService {
    private Logger logger = LoggerFactory.getLogger(LogininforServiceImpl.class);

    @Autowired
    private LogininforDao logininforMapper;

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(Logininfor logininfor) {
        try {
            logininforMapper.insertLogininfor(logininfor);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor) {
        try {
            return logininforMapper.selectLogininforList(logininfor);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        try {
            return logininforMapper.deleteLogininforByIds(infoIds);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        try {
            logininforMapper.cleanLogininfor();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }
}
