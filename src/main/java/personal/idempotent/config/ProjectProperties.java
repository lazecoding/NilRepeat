package personal.idempotent.config;

/**
 * @author: lazecoding
 * @date: 2020/12/7 22:54
 * @description:
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: lazecoding
 * @date: 2020/12/7 20:47
 * @description: 项目配置类
 */
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectProperties {
    /**
     *  唯一ID生成类型
     */
    private String type;

    /**
     * 请求URL
     */
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProjectProperties{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
