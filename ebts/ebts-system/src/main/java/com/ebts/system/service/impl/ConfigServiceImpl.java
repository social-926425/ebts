package com.ebts.system.service.impl;

import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.common.annotation.DataSource;
import com.ebts.common.constant.Constants;
import com.ebts.common.constant.UserConstants;
import com.ebts.common.core.redis.RedisCache;
import com.ebts.common.core.text.Convert;
import com.ebts.common.enums.DataSourceType;
import com.ebts.common.exception.CustomException;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.entity.Config;
import com.ebts.system.dao.ConfigDao;
import com.ebts.system.service.ConfigService;

/**
 * 参数配置 服务层实现
 *
 * @author binlin
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    private Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
    @Autowired
    private ConfigDao configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        try {
            List<Config> configsList = configMapper.selectConfigList(new Config());
            for (Config config : configsList) {
                redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public Config selectConfigById(Long configId) {
        try {
            Config config = new Config();
            config.setConfigId(configId);
            return configMapper.selectConfig(config);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        try {
            String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
            if (StringUtils.isNotEmpty(configValue)) {
                return configValue;
            }
            Config config = new Config();
            config.setConfigKey(configKey);
            Config retConfig = configMapper.selectConfig(config);
            if (StringUtils.isNotNull(retConfig)) {
                redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
                return retConfig.getConfigValue();
            }
            return StringUtils.EMPTY;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<Config> selectConfigList(Config config) {
        try {
            return configMapper.selectConfigList(config);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(Config config) {
        try {
            int row = configMapper.insertConfig(config);
            if (row > 0) {
                redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
            }
            return row;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(Config config) {
        try {
            int row = configMapper.updateConfig(config);
            if (row > 0) {
                redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
            }
            return row;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(Long[] configIds) {
        try {
            for (Long configId : configIds) {
                Config config = selectConfigById(configId);
                if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                    throw new CustomException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
                }
            }
            int count = configMapper.deleteConfigByIds(configIds);
            if (count > 0) {
                Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
                redisCache.deleteObject(keys);
            }
            return count;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        try {
            Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
            redisCache.deleteObject(keys);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(Config config) {
        try {
            Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
            Config info = configMapper.checkConfigKeyUnique(config.getConfigKey());
            if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        try {
            return Constants.SYS_CONFIG_KEY + configKey;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.DB_EX);
        }
    }
}
