package personal.idempotent.service;

import personal.idempotent.exception.IdempotentException;

/**
 * 幂等接口
 */
public interface IdempotentCore {

    /**
     * 获取 事件唯一Id
     *
     * @return 事件唯一Id
     */
    String getIdempotentId() throws IdempotentException;

    /**
     * 尝试 获取事件
     * @param idempotenId  事件唯一Id
     * @return 是否幂等
     */
    boolean tryIdempotent(String idempotenId) throws IdempotentException;


    /**
     * 回滚 事件唯一Id
     * @param idempotentId  事件唯一Id
     * @return 回滚是否成功
     */
    boolean rollbackIdempotent(String idempotentId) throws IdempotentException;
}
