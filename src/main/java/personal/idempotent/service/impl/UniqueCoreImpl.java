package personal.idempotent.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import personal.idempotent.config.ProjectProperties;
import personal.idempotent.exception.IdempotentException;
import personal.idempotent.service.UniqueCore;
import personal.idempotent.util.StringUtil;

/**
 * @author: lazecoding
 * @date: 2020/12/7 22:48
 * @description: 唯一Id生成器实现
 */
@Component("UniqueCoreImpl")
public class UniqueCoreImpl implements UniqueCore {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectProperties projectProperties;

    @Autowired
    private RestTemplate restTemplate;

    /**
     *  获取 事件唯一Id
     *
     * @return 事件唯一Id
     * @throws IdempotentException
     */
    @Override
    public String getUniqueId() throws IdempotentException {
        int type = projectProperties.getType();
        if (type == 1) {
            return StringUtil.getUUIDString();
        } else if (type == 2) {
            if (projectProperties.getUrl() == null || projectProperties.getUrl().trim().length() == 0) {
                logger.info("请输入正确的URL");
                throw new IdempotentException("请输入正确的URL");
            }
            Long idempotentId = null;
            try {
                idempotentId = restTemplate.getForObject(projectProperties.getUrl(), Long.class);
            } catch (RestClientException e) {
                logger.warn("Remote接口异常:" + projectProperties.getUrl());
                throw new IdempotentException("Remote接口异常");
            }
            return idempotentId.toString();
        } else {
            logger.info("请输入正确的Type");
            throw new IdempotentException("请输入正确的Type");
        }

    }
}
