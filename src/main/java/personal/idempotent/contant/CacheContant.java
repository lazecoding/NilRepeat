package personal.idempotent.contant;

/**
 * @author: lazecoding
 * @date: 2020/12/7 22:24
 * @description: 缓存常量
 */
public enum CacheContant {
    /**
     * 幂等 - 常规
     */
    IDEMPOTENTCOMMON(":Idempotent:Common:", "IdempotentCommon"),
    /**
     * 幂等 - 控制器注解
     */
    IDEMPOTENTANNOTATION(":Idempotent:Annotation:", "IdempotentAnnotation");

    private String name;
    private String defaultValue;

    private CacheContant(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取缓存名
     * @param key
     * @return 缓存名
     */
    public String getCacheName(String key) {
        return this.name + key;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
