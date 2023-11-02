package cn.yunshi.groupcontrol.middle.factory;

import cn.yunshi.groupcontrol.middle.IControlScriptService;
import cn.yunshi.groupcontrol.middle.impl.DouyinScriptServiceImpl;
import cn.yunshi.groupcontrol.middle.impl.WeixinVideoScriptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-11-02 17:09
 **/
public class PlatformBeanConfig {

    protected static Map<String, IControlScriptService> platformBeanMap = new HashMap<>();

    @Autowired
    @Qualifier("douyinScriptService")
    private DouyinScriptServiceImpl douyinScriptService;

    @Autowired
    @Qualifier("weixinScriptService")
    private WeixinVideoScriptServiceImpl weixinScriptService;


    @PostConstruct
    public void init(){
        platformBeanMap.put("douyin",douyinScriptService);
        platformBeanMap.put("weixin",weixinScriptService);

    }

}
