package net.jlxxw.component.weixin.function.token;

import java.text.MessageFormat;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;
import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.exception.WeiXinException;
import net.jlxxw.component.weixin.mapper.TokenMapper;
import net.jlxxw.component.weixin.properties.WeiXinProperties;
import net.jlxxw.component.weixin.response.WeiXinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:30 下午
 */
@ConditionalOnProperty(prefix = "weixin", value = "enable-default-token-manager", havingValue = "true")
@Component
public class WeiXinTokenManagerImpl implements WeiXinTokenManager{
    private static final Logger logger = LoggerFactory.getLogger(WeiXinTokenManagerImpl.class);
    @Autowired
    private WeiXinProperties weiXinProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenMapper tokenMapper;

    /**
     * 保存token
     *
     * @param token
     */
    @Override
    public void saveToken(String token) {
        tokenMapper.insert(token);
    }

    /**
     * 获取token
     *
     * @return token
     */
    @Override
    public String getToken() {
        String url = MessageFormat.format(UrlConstant.TOKEN_URL,weiXinProperties.getAppId(),weiXinProperties.getSecret());
        WeiXinResponse response = restTemplate.getForObject(url, WeiXinResponse.class);
        if(Objects.nonNull(response.getErrcode())){
        	logger.error("微信获取token返回值:{}",JSON.toJSONString(response));
            throw new WeiXinException(JSON.toJSONString(response));
        }
        return response.getAccess_token();
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getTokenFromLocal() {
        return tokenMapper.getToken();
    }

    @PostConstruct
    public void postConstruct(){
        tokenMapper.createTable();
    }
}
