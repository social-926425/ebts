package com.ebts.generator.utils.page;


import com.ebts.generator.utils.GenServletUtils;

/**
 * 表格数据处理
 *
 * @author binlin
 */
public class GenTableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 封装分页对象
     */
    public static GenPageDomain getPageDomain() {
        GenPageDomain genPageDomain = new GenPageDomain();
        genPageDomain.setPageNum(GenServletUtils.getParameterToInt(PAGE_NUM));
        genPageDomain.setPageSize(GenServletUtils.getParameterToInt(PAGE_SIZE));
        genPageDomain.setOrderByColumn(GenServletUtils.getParameter(ORDER_BY_COLUMN));
        genPageDomain.setIsAsc(GenServletUtils.getParameter(IS_ASC));
        return genPageDomain;
    }

    public static GenPageDomain buildPageRequest() {
        return getPageDomain();
    }
}
