package personal.idempotent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import personal.idempotent.annotation.Idempotent;
import personal.idempotent.exception.IdempotentException;
import personal.idempotent.service.IdempotentCore;

import java.util.concurrent.TimeUnit;

/**
 * demo
 */
@Controller
public class IdempotentController {

    @Autowired
    private IdempotentCore idempotentCore;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * http://localhost:9000/idempotent?key=111&value=222
     * <p>
     * 注解测试
     */
    @Idempotent(isIdempotent = true, expireTime = 3, timeUnit = TimeUnit.SECONDS, info = "请勿重复添加Key", delKey = false)
    @RequestMapping("idempotent")
    @ResponseBody
    public String idempotent(String key,String value) {
        // dothings
        return "添加：" + key + "-" + value;
    }

    /**
     * http://localhost:9000/getIdempotentId
     * <p>
     * 仅以HTTP请求触发为例
     *
     * @return
     */
    @RequestMapping("getIdempotentId")
    @ResponseBody
    public String getIdempotentId() {
        String idempotentId = "";
        try {
            idempotentId = idempotentCore.getIdempotentId();
        } catch (IdempotentException e) {
            e.printStackTrace();
        }
        return idempotentId;
    }

    /**
     * http://localhost:9000/tryIdempotentId?idempotentId=999
     * <p>
     * 仅以HTTP请求触发为例
     *
     * @return
     */
    @RequestMapping("tryIdempotentId")
    @ResponseBody
    public String tryIdempotentId(String idempotentId) {
        // 获取事件是否成功
        boolean tryIdempotent = false;
        // 业务操作是否成功
        boolean isSucces = false;
        try {
            tryIdempotent = idempotentCore.tryIdempotent(idempotentId);
        } catch (IdempotentException e) {
            e.printStackTrace();
            // 出了啥异常
            tryIdempotent = false;
        }
        // 如果获取事件成功，进行业务操作
        if (tryIdempotent) {
            try {
                //do things;
                isSucces = true;
            } catch (Exception e) {
                // 如果出异常
                e.printStackTrace();
                isSucces = false;
            }
        }
        // 获取事件成功，而且执行业务失败才回滚 切记！
        if (tryIdempotent && !isSucces) {
            // 事件ID回滚还原是否成功
            boolean rollbackIdempotent = false;
            try {
                idempotentCore.rollbackIdempotent(idempotentId);
                rollbackIdempotent = true;
            } catch (IdempotentException e) {
                e.printStackTrace();
                rollbackIdempotent = false;
            }
            if (rollbackIdempotent) {
                logger.warn("事件:" + idempotentId + "  回滚成功");
            } else {
                logger.warn("事件:" + idempotentId + "  回滚失败");
            }
        }
        return "tryIdempotent:" + tryIdempotent;
    }

}
