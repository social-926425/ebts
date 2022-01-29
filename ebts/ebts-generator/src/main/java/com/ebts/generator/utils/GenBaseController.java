package com.ebts.generator.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ebts.generator.utils.page.GenPageDomain;
import com.ebts.generator.utils.page.GenTableDataInfo;
import com.ebts.generator.utils.page.GenTableSupport;
import com.ebts.generator.utils.sql.GenSqlUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GenBaseController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(GenDateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        GenPageDomain genPageDomain = GenTableSupport.buildPageRequest();
        Integer pageNum = genPageDomain.getPageNum();
        Integer pageSize = genPageDomain.getPageSize();
        if (GenStringUtils.isNotNull(pageNum) && GenStringUtils.isNotNull(pageSize)) {
            String orderBy = GenSqlUtil.escapeOrderBySql(genPageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 设置请求分页数据
     */
    public void startPage(Map<String, Object> map) {
        GenPageDomain genPageDomain = GenTableSupport.buildPageRequest();
        Integer pageNum = (Integer) map.get("pageNum");
        Integer pageSize = (Integer) map.get("pageSize");
        if (GenStringUtils.isNotNull(pageNum) && GenStringUtils.isNotNull(pageSize)) {
            String orderBy = GenSqlUtil.escapeOrderBySql(genPageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected GenTableDataInfo getDataTable(List<?> list) {
        GenTableDataInfo rspData = new GenTableDataInfo();
        rspData.setCode(GenHttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }


//    /**
//     * 响应返回结果
//     *
//     * @param rows 影响行数
//     * @return 操作结果
//     */
//    protected AjaxResult toAjax(int rows)
//    {
//        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
//    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return GenStringUtils.format("redirect:{}", url);
    }
}
