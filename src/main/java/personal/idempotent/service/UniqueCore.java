package personal.idempotent.service;

import personal.idempotent.exception.IdempotentException;

/**
 * @author: lazecoding
 * @date: 2020/12/7 22:47
 * @description: 唯一ID生成武
 */
public interface UniqueCore {
    String getUniqueId() throws IdempotentException;
}
