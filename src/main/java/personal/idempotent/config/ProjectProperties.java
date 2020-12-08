package personal.idempotent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
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
    private int type;

    /**
     * 请求URL
     */
    private String url;

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
                "type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
