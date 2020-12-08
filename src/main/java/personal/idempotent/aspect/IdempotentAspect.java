package personal.idempotent.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import personal.idempotent.annotation.Idempotent;
import personal.idempotent.config.Application;
import personal.idempotent.contant.CacheContant;
import personal.idempotent.exception.IdempotentException;
import personal.idempotent.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: lazecoding
 * @date: 2020/12/8 20:07
 * @description: 注解切面
 */
@Aspect
@Component
public class IdempotentAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Application application;

    @Pointcut("@annotation(personal.idempotent.annotation.Idempotent)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void beforePointCut(JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (!method.isAnnotationPresent(Idempotent.class)) {
            return;
        }
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        boolean isIdempotent = idempotent.isIdempotent();
        if (!isIdempotent) {
            return;
        }
        // cache key
        String appName = application.getName();
        String url = request.getRequestURL().toString();
        String argString = Arrays.asList(joinPoint.getArgs()).toString();
        String key = appName + ":" + url + argString;
        // 过期时间
        long expireTime = idempotent.expireTime();
        TimeUnit timeUnit = idempotent.timeUnit();
        // 异常信息
        String info = idempotent.info();
        boolean flag = redisTemplate.opsForValue()
                .setIfAbsent(key, CacheContant.IDEMPOTENTANNOTATION.getDefaultValue(), expireTime, timeUnit);
        if (!flag) {
            logger.warn(info + ":" + key);
            throw new IdempotentException(info);
        }
        // 业务执行完毕是否删除key
        boolean delKey = idempotent.delKey();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("delKey", delKey);
        threadLocal.set(map);
    }

    @After("pointCut()")
    public void afterPointCut(JoinPoint joinPoint) {
        // afterPointCut 根据设置，决定要不要删除缓存
        Map<String, Object> map = threadLocal.get();
        if (map != null && map.size() > 0) {
            if (map.get("delKey") != null && (Boolean) map.get("delKey")) {
                redisTemplate.delete(StringUtil.getString(map.get("key")));
            }
        }
    }
}
