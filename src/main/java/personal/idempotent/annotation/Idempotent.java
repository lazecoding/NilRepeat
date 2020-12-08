package personal.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author: lazecoding
 * @date: 2020/12/8 20:00
 * @description: 幂等注解 作用于Controller的方法上
 * <p>
 * 用法如下:
 * @Idempotent(isIdempotent = true, expireTime = 3, timeUnit = TimeUnit.SECONDS, info = "请勿重复添加Key", delKey = false)
 * @RequestMapping("idempotent")
 * @ResponseBody public String idempotent(String key) {
 * // dothings
 * return "添加：" + key;
 * }
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 是否做幂等处理
     * true：幂等
     * false：非幂等
     **/
    boolean isIdempotent() default false;

    /**
     * 有效期
     * 默认：1
     * 有效期要大于程序执行时间，否则请求还是可能会进来
     **/
    int expireTime() default 1;

    /**
     * 时间单位  默认：s
     **/
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 提示信息
     **/
    String info() default "重复请求，请稍后重试";

    /**
     * 是否在业务完成后删除key
     * true:删除
     * false:不删除
     **/
    boolean delKey() default false;
}
