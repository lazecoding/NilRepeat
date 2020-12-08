package personal.idempotent.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import personal.idempotent.config.Application;
import personal.idempotent.contant.CacheContant;
import personal.idempotent.exception.IdempotentException;
import personal.idempotent.service.IdempotentCore;
import personal.idempotent.service.UniqueCore;
import personal.idempotent.util.StringUtil;

import java.util.concurrent.TimeUnit;

/**
 * 幂等实现类
 */
@Component("IdempotenCoreImpl")
public class IdempotenCoreImpl implements IdempotentCore {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UniqueCore uniqueCore;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Application application;

    /**
     * 获取 幂等事件 唯一Id
     * (为了保证类型一致性，唯一Id统一采用String类型)
     *
     * @return 事件唯一Id
     */
    @Override
    public String getIdempotentId() throws IdempotentException {
        String idempotentId = uniqueCore.getUniqueId();
        //  事件唯一Id setIfAbsent:不存在该Key才写入

        boolean flag = redisTemplate.opsForValue()
                .setIfAbsent(application.getName() + CacheContant.IDEMPOTENTCOMMON.getCacheName(idempotentId),
                        CacheContant.IDEMPOTENTCOMMON.getDefaultValue(),
                        1, TimeUnit.HOURS);
        if (!flag) {
            logger.warn("获取事件唯一ID异常:" + idempotentId);
            throw new IdempotentException("获取事件唯一ID异常");
        }
        return idempotentId;
    }

    /**
     * 尝试 获取事件
     *
     * @return 事件是否存在
     */
    @Override
    public boolean tryIdempotent(String idempotentId) throws IdempotentException {
        if (StringUtil.isEmpty(idempotentId)) {
            throw new IdempotentException("请传入正确参数");
        }
        // redisTemplate.delete  符合删除要求
        // 只有真正删除了才返回true;如果key删除失败或者key不存在都返回false.
        return redisTemplate.delete(application.getName() + CacheContant.IDEMPOTENTCOMMON.getCacheName(idempotentId));
    }

    /**
     * 回滚 事件唯一Id
     *
     * @param idempotentId 事件唯一Id
     * @return 回滚是否成功
     */
    @Override
    public boolean rollbackIdempotent(String idempotentId) throws IdempotentException {
        if (StringUtil.isEmpty(idempotentId)) {
            throw new IdempotentException("请传入正确参数");
        }
        //  事件唯一Id 入Redis成功则返回，否则抛出异常
        boolean flag = redisTemplate.opsForValue()
                .setIfAbsent(application.getName() + CacheContant.IDEMPOTENTCOMMON.getCacheName(idempotentId),
                        CacheContant.IDEMPOTENTCOMMON.getDefaultValue(),
                        1, TimeUnit.HOURS);
        if (!flag) {
            logger.warn("获取事件唯一ID异常:" + idempotentId);
            throw new IdempotentException("获取事件唯一ID异常");
        }
        return flag;
    }
}
