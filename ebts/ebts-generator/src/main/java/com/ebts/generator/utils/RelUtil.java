package com.ebts.generator.utils;

import com.ebts.generator.entity.RelColumn;
import com.ebts.generator.utils.constant.GenConstants;

/**
 * @Author 18209
 * @Date 2021/2/24 22:46
 * @Version 1.0
 */
public class RelUtil {


    public static void initRelColumn(RelColumn relColumn, Long relId) {
        //获取数据库数据类型
        String dataType = GenUtils.getDbType(relColumn.getColumnType());
        //获取字段名称
        String columnName = relColumn.getColumnName();
        //设置tableid
        relColumn.setRelId(relId);
        //设置创建者id
        relColumn.setCreateBy(1);
        //转为设置java字段名称
        relColumn.setJavaField(GenStringUtils.toCamelCase(columnName));
        //默认类型
        relColumn.setJavaType(GenConstants.TYPE_STRING);
        //默认查询条件 精确查询
        relColumn.setQueryType(GenConstants.QUERY_EQ);

        if (GenUtils.arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || GenUtils.arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType)) {
            //字符串长度超500设置htmlType为文本域
            Integer columnLength = GenUtils.getColumnLength(relColumn.getColumnType());
            String htmlType = columnLength >= 500 || GenUtils.arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            relColumn.setHtmlType(htmlType);
        } else if (GenUtils.arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            relColumn.setJavaType(GenConstants.TYPE_DATE);
            relColumn.setHtmlType(GenConstants.HTML_DATETIME);
        } else if (GenUtils.arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            relColumn.setHtmlType(GenConstants.HTML_INPUT);

            // 如果是浮点型 统一用BigDecimal
            String[] str = GenStringUtils.split(GenStringUtils.substringBetween(relColumn.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                relColumn.setJavaType(GenConstants.TYPE_BIGDECIMAL);
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                relColumn.setJavaType(GenConstants.TYPE_INTEGER);
            }
            // 长整形
            else {
                relColumn.setJavaType(GenConstants.TYPE_LONG);
            }
        }
        // 列表字段
        relColumn.setIsList(GenConstants.REQUIRE);
        // 查询字段
        relColumn.setIsQuery(GenConstants.NO_REQUIRE);
        //初始化字典
        relColumn.setDictType("");
        // 查询字段类型
        if (GenStringUtils.endsWithIgnoreCase(columnName, "name")) {
            relColumn.setQueryType(GenConstants.QUERY_LIKE);
        }
        // 状态字段设置单选框
        if (GenStringUtils.endsWithIgnoreCase(columnName, "status")) {
            relColumn.setHtmlType(GenConstants.HTML_RADIO);
        }
        // 类型&性别字段设置下拉框
        else if (GenStringUtils.endsWithIgnoreCase(columnName, "type")
                || GenStringUtils.endsWithIgnoreCase(columnName, "sex")) {
            relColumn.setHtmlType(GenConstants.HTML_SELECT);
        }
    }

}
