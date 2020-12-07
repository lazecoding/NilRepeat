package personal.idempotent.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import personal.idempotent.contant.CacheContant;
import personal.idempotent.exception.IdempotentException;
import personal.idempotent.service.IdempotentCore;
import personal.idempotent.service.UniqueCore;

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

    /**
     * 获取 幂等事件 唯一Id
     * (为了保证类型一致性，唯一Id统一采用String类型)
     *
     * @return 事件唯一Id
     */
    @Override
    public String getIdempotentId() throws IdempotentException {
        String idempotentId = uniqueCore.getUniqueId();
        //  事件唯一Id 入Redis成功则返回，否则抛出异常
        boolean flag = redisTemplate.opsForValue()
                .setIfAbsent(CacheContant.IDEMPOTENTCOMMON.getCacheName(idempotentId),
                        CacheContant.IDEMPOTENTCOMMON.getDefaultValue(),
                        1000, TimeUnit.MILLISECONDS);
        if (flag) {
            return idempotentId;
        } else {
            throw new IdempotentException("获取事件唯一ID异常");
        }
    }

    /**
     * 校验 事件幂等性
     *
     * @return 是否幂等
     */
    @Override
    public boolean checkIdempotent(String idempotentId) {
        if (idempotentId == null || idempotentId.trim().length() == 0) {
            return false;
        }
        // TODO 处理业务-移除ID
        boolean flag = false;
        // 如果存在返回ture
        // 如果不存在false啥呀，直接抛异常。
        return flag;
    }
}
