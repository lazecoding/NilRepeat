package personal.idempotent.service;

import personal.idempotent.exception.IdempotentException;

/**
 * 幂等接口
 */
public interface IdempotentCore {

    /**
     * 获取 幂等事件 唯一Id
     *
     * @return 事件唯一Id
     */
    String getIdempotentId() throws IdempotentException;

    /**
     * 校验 事件幂等性
     * @param idempotenId  事件唯一Id
     * @return 是否幂等
     */
    boolean checkIdempotent(String idempotenId);
}
