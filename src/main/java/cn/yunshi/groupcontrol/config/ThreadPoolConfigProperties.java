package cn.yunshi.groupcontrol.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-20 16:21
 **/
@Data
@ConfigurationProperties(prefix = "group.thread")
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;

}
