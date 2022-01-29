package com.ebts.generator.utils;

import com.ebts.generator.utils.constant.Constants;
import com.ebts.generator.utils.entity.GenDictData;
import com.ebts.generator.utils.redis.GenRedisCache;
import com.ebts.generator.utils.spring.GenSpringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 字典工具类
 *
 * @author binlin
 */
public class GenDictUtils {
    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param genDictData 字典数据列表
     */
    public static void setDictCache(String key, List<GenDictData> genDictData) {
        GenSpringUtils.getBean(GenRedisCache.class).setCacheObject(getCacheKey(key), genDictData);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<GenDictData> getDictCache(String key) {
        Object cacheObj = GenSpringUtils.getBean(GenRedisCache.class).getCacheObject(getCacheKey(key));
        if (GenStringUtils.isNotNull(cacheObj)) {
            List<GenDictData> genDictData = GenStringUtils.cast(cacheObj);
            return genDictData;
        }
        return null;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<GenDictData> datas = getDictCache(dictType);

        if (GenStringUtils.containsAny(separator, dictValue) && GenStringUtils.isNotEmpty(datas)) {
            for (GenDictData dict : datas) {
                for (String value : dictValue.split(separator)) {
                    if (value.equals(dict.getDictValue())) {
                        propertyString.append(dict.getDictLabel() + separator);
                        break;
                    }
                }
            }
        } else {
            for (GenDictData dict : datas) {
                if (dictValue.equals(dict.getDictValue())) {
                    return dict.getDictLabel();
                }
            }
        }
        return GenStringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<GenDictData> datas = getDictCache(dictType);

        if (GenStringUtils.containsAny(separator, dictLabel) && GenStringUtils.isNotEmpty(datas)) {
            for (GenDictData dict : datas) {
                for (String label : dictLabel.split(separator)) {
                    if (label.equals(dict.getDictLabel())) {
                        propertyString.append(dict.getDictValue() + separator);
                        break;
                    }
                }
            }
        } else {
            for (GenDictData dict : datas) {
                if (dictLabel.equals(dict.getDictLabel())) {
                    return dict.getDictValue();
                }
            }
        }
        return GenStringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = GenSpringUtils.getBean(GenRedisCache.class).keys(Constants.SYS_DICT_KEY + "*");
        GenSpringUtils.getBean(GenRedisCache.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return Constants.SYS_DICT_KEY + configKey;
    }
}
