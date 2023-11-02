package cn.yunshi.groupcontrol.middle.factory;

import cn.yunshi.groupcontrol.middle.IControlScriptService;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-11-02 17:11
 **/
@Component
public class PlatformBeanFactory extends PlatformBeanConfig {

    public IControlScriptService findPlatformBean(String key) {
        return platformBeanMap.get(key);
    }

}
