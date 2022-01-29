package com.ebts.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.utils.ServerResult;
import com.ebts.system.dao.RealQueryServiceDao;
import com.ebts.system.entity.RealUniCon;
import com.ebts.system.entity.RealUniQuery;
import com.ebts.system.service.RealQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/22 18:04
 * @Version 1.0
 */
@Service
public class RealQueryServiceImpl implements RealQueryService {
    private Logger logger = LoggerFactory.getLogger(RealQueryServiceImpl.class);
    @Autowired
    private RealQueryServiceDao realQueryServiceDao;


    @Override
    public ServerResult<List<RealUniQuery>> RealInfo(Long id) {
        try {
            List<RealUniQuery> uniCons = realQueryServiceDao.queryRealInfo(id);
            return new ServerResult<>(true, uniCons);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.DB_EX);
        }
    }

    @Override
    public ServerResult<List<Map<String, Object>>> RealData(RealUniQuery realUniQuery, Integer type) {
        try {
            List<RealUniCon> realUniCons = realUniQuery.getUniCons();
            Integer pageNum = null, pageSize = null;
            if (type == 2) {
                pageNum = realUniQuery.getPageNum();
                pageSize = realUniQuery.getPageSize();
            }
            realUniQuery = realQueryServiceDao.selectRealUniQueryById(realUniQuery.getId());
            StringBuilder sql = new StringBuilder(realUniQuery.getUqSql().toLowerCase());
            if (realUniQuery.getIsRelease() == 0) {
                return new ServerResult<>(false, ReturnConstants.STATE_ERROR);
            } else {
                if (realUniCons.size() > 0) {
                    sql.append(" where 1 = 1 ");
                    for (RealUniCon realUniCon : realUniCons) {
                        sql.append(conversionReal(realUniCon));
                    }
                }
                if (type == 2) {
                    PageHelper.startPage(pageNum, pageSize, "");
                }
                List<Map<String, Object>> dataMap = realQueryServiceDao.realUniQuery(sql.toString());
                return new ServerResult<>(true, dataMap, realUniQuery.getUqName());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.SYS_FAILL);
        }
    }

    public String conversionReal(RealUniCon realUniCon) {
        if (realUniCon.getUcReal() == null&&!realUniCon.getUcReal().equals("")) {
            return "";
        } else {
            String sql = " and ";
            switch (realUniCon.getUcCon()) {
                case "EQ":
                    sql += realUniCon.getUcKey() + " = '" + realUniCon.getUcReal() + "'";
                    break;
                case "NE":
                    sql += realUniCon.getUcKey() + " != '" + realUniCon.getUcReal() + "'";
                    break;
                case "GT":
                    sql += realUniCon.getUcKey() + " > '" + realUniCon.getUcReal() + "'";
                    break;
                case "GTE":
                    sql += realUniCon.getUcKey() + " >= '" + realUniCon.getUcReal() + "'";
                    break;
                case "LT":
                    sql += realUniCon.getUcKey() + " < '" + realUniCon.getUcReal() + "'";
                    break;
                case "LTE":
                    sql += realUniCon.getUcKey() + " <= '" + realUniCon.getUcReal() + "'";
                    break;
                case "LIKE":
                    sql += realUniCon.getUcKey() + " like '%" + realUniCon.getUcReal() + "%'";
                    break;
                case "BETWEEN":
                    if (realUniCon.getUcType().equals("input")) {
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(realUniCon.getUcReal());
                        Object begin = jsonObject.get("begin");
                        Object end = jsonObject.get("end");
                        if (begin != null && end != null) {
                            sql += realUniCon.getUcKey() + " between '" + begin + "' AND '" + end + "'";
                        } else {
                            sql = "";
                        }
                    } else if (realUniCon.getUcType().equals("datetime")) {
                        List<String> list = (ArrayList<String>) realUniCon.getUcReal();
                        if (list.size() == 2) {
                            String startTime = list.get(0);
                            String endTime = list.get(1);
                            sql += realUniCon.getUcKey() + " between '" + startTime + "' AND '" + endTime + "'";
                        } else {
                            sql = "";
                        }
                    }
                    break;
            }
            return sql;
        }
    }

}
