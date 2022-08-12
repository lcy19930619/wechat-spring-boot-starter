package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.function.token.WeChatTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 永久素材管理工具
 * @author chunyang.leng
 * @date 2022-08-12 4:44 PM
 */
@DependsOn( "weChatTokenManager")
@Component
public class PermanentMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(TempMaterialManager.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

}
