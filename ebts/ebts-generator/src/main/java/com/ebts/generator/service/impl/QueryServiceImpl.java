package com.ebts.generator.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ebts.generator.dao.QueryDao;
import com.ebts.generator.dao.UniQueryDao;
import com.ebts.generator.entity.UniCon;
import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.service.QueryService;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenSecurityUtils;
import com.ebts.generator.utils.GenServerResult;
import com.ebts.generator.utils.entity.GenMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

/**
 * Author 18209
 * Date 2021/1/30 18:54
 * Version 1.0
 */
@Service
public class QueryServiceImpl implements QueryService {
    private final Logger logger = LoggerFactory.getLogger(QueryServiceImpl.class);

    @Autowired
    private QueryDao queryDao;

    @Autowired
    private UniQueryDao uniQueryDao;


    /**
     * 查询万能查询的基本信息和条件信息
     */
    @Override
    public GenServerResult<UniQuery> selectQueryById(Long id) {
        try {
            UniQuery uniQuery = queryDao.selectQueryInfo(id);
            if (uniQuery != null) {
                return new GenServerResult<>(true, uniQuery);
            } else {
                return new GenServerResult<>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.SYS_FAILL);
        }
    }

    @Override
    @Transactional
    public GenServerResult<Integer> Release(UniQuery uniQuery) {
        try {
            Integer isRelease = queryDao.Release(uniQuery.getId());
            if ((isRelease == 1 && uniQuery.getIsRelease() == 2) || (isRelease == 2 && uniQuery.getIsRelease() == 1)) {
                if (uniQuery.getIsRelease() == 1) {
                    UniQuery query = uniQueryDao.selectUniQueryById(uniQuery.getId());
                    GenMenu genMenu = new GenMenu(query.getId(), query.getUqName(), GenSecurityUtils.getUserId());
                    Integer insermenu = queryDao.insertMenu(genMenu);
                    if (insermenu == 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return new GenServerResult<>(false, GenReturnConstants.OP_ERROR);
                    }
                } else {
                    Integer deleteMenu = queryDao.deleteMenu("data/" + uniQuery.getId());
                    if (deleteMenu == 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return new GenServerResult<>(false, GenReturnConstants.OP_ERROR);
                    }
                }
                Integer release = queryDao.changeRelease(uniQuery);
                if (release > 0) {
                    return new GenServerResult<>(true);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new GenServerResult<>(false, GenReturnConstants.OP_ERROR);
                }
            } else {
                return new GenServerResult<>(false, GenReturnConstants.STATE_ERROR);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new GenServerResult<>(false, GenReturnConstants.SYS_FAILL);
        }
    }

    /**
     * 更新万能查询基本信息和条件
     */
    @Override
    @Transactional
    public GenServerResult<Integer> updateQueryInfo(UniQuery uniQuery) {
        try {
            int uqrenewal = uniQueryDao.updateUniQuery(uniQuery);
            if (uqrenewal > 0) {
                if (uniQuery.getUniCons() != null && uniQuery.getUniCons().size() > 0) {
                    queryDao.deleteUniCon(uniQuery.getId());
                    Integer ucrenewal = queryDao.insertUniCon(uniQuery.getUniCons());
                    if (ucrenewal == 0) {
                        return new GenServerResult<>(false, GenReturnConstants.OP_ERROR);
                    }
                }
                return new GenServerResult<>(true);
            }
            return new GenServerResult<>(false, GenReturnConstants.OP_ERROR);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new GenServerResult<>(false, GenReturnConstants.SYS_FAILL);
        }
    }

    @Override
    public GenServerResult<List<Map<String, Object>>> previewQuery(UniQuery uniQuery) {
        try {
            StringBuilder sql = new StringBuilder(uniQuery.getUqSql().toLowerCase());
            List<UniCon> uniConList = uniQuery.getUniCons();
            if (sql.toString().contains("insert") || sql.toString().contains("delete ") || sql.toString().contains("update ") ||
                    sql.toString().contains("drop ") || sql.toString().contains("database ") || sql.toString().contains("create ") ||
                    sql.toString().contains("view ") || sql.toString().contains("alter ") || sql.toString().contains("gen_")) {
                return new GenServerResult<>(false, "sql语句含有insert,delete,update,drop,database,view,alter,gen_等特殊字符!");
            }
            if (uniConList.size() > 0) {
                sql.append(" where 1 = 1");
                for (UniCon uniCon : uniConList) {
                    if (!uniCon.getUcMock().equals("") && uniCon.getUcMock() != null)
                    sql.append(conversionPreview(uniCon));
                }
            }
            List<Map<String, Object>> dataMap = queryDao.UniQuery(sql.toString());
            return new GenServerResult<>(true, dataMap);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, e.getMessage());
        }
    }

    /**
     * 预览模式下拼接sql
     *
     * @param uniCon
     * @return
     */
    public String conversionPreview(UniCon uniCon) {
        String sql = " and ";
        switch (uniCon.getUcCon()) {
            case "EQ":
                sql += uniCon.getUcKey() + " = '" + uniCon.getUcMock() + "'";
                break;
            case "NE":
                sql += uniCon.getUcKey() + " != '" + uniCon.getUcMock() + "'";
                break;
            case "GT":
                sql += uniCon.getUcKey() + " > '" + uniCon.getUcMock() + "'";
                break;
            case "GTE":
                sql += uniCon.getUcKey() + " >= '" + uniCon.getUcMock() + "'";
                break;
            case "LT":
                sql += uniCon.getUcKey() + " < '" + uniCon.getUcMock() + "'";
                break;
            case "LTE":
                sql += uniCon.getUcKey() + " <= '" + uniCon.getUcMock() + "'";
                break;
            case "LIKE":
                sql += uniCon.getUcKey() + " like '%" + uniCon.getUcMock() + "%'";
                break;
            case "BETWEEN":
                try {
                    JSONObject jsonObject = JSONObject.parseObject(uniCon.getUcMock());
                    if (uniCon.getUcType().equals("input")) {
                        Object begin = jsonObject.get("begin");
                        Object end = jsonObject.get("end");
                        if (begin != null && end != null) {
                            sql += uniCon.getUcKey() + " between '" + begin + "' AND '" + end + "'";
                        } else {
                            sql = "";
                        }
                    } else if (uniCon.getUcType().equals("datetime")) {
                        Object startTime = jsonObject.get("startTime");
                        Object endTime = jsonObject.get("endTime");
                        sql += uniCon.getUcKey() + " between '" + startTime + "' AND '" + endTime + "'";
                    }
                } catch (RuntimeException e) {
                    logger.error(e.getMessage());
                    sql = "";
                }
                break;
        }
        return sql;
    }
}
