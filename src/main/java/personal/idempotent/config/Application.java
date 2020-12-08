package personal.idempotent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: lazecoding
 * @date: 2020/12/8 21:27
 * @description: SpringApplication 属性
 */
@Component
@ConfigurationProperties(prefix = "spring.application")
public class Application {
    /**
     * 应用名
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
