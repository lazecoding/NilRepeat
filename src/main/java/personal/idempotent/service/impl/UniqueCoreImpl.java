package personal.idempotent.service.impl;

import org.springframework.stereotype.Component;
import personal.idempotent.service.UniqueCore;

import java.util.UUID;

/**
 * @author: lazecoding
 * @date: 2020/12/7 22:48
 * @description: 唯一Id生成器实现
 */
@Component("UniqueCoreImpl")
public class UniqueCoreImpl implements UniqueCore {
    @Override
    public String getUniqueId() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
