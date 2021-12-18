package net.jlxxw.wechat.function.token;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.WeiXinException;
import net.jlxxw.wechat.mapper.TokenMapper;
import net.jlxxw.wechat.properties.WeiXinProperties;
import net.jlxxw.wechat.response.WeiXinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:30 下午
 */
@DependsOn({"tokenMapper","weiXinProperties"})
@ConditionalOnProperty(prefix = "weixin", value = "enable-default-token-manager", havingValue = "true")
@Component
public class DefaultWeiXinTokenManagerImpl implements WeiXinTokenManager{
    private static final Logger logger = LoggerFactory.getLogger(DefaultWeiXinTokenManagerImpl.class);
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
    public String getTokenFromWeiXin() throws WeiXinException{
        String url = MessageFormat.format(UrlConstant.TOKEN_URL,weiXinProperties.getAppId(),weiXinProperties.getSecret());
        WeiXinResponse response = restTemplate.getForObject(url, WeiXinResponse.class);
        if(Objects.nonNull(response.getErrcode()) && 0!=response.getErrcode()){
        	logger.error("微信获取token返回值:{}",JSON.toJSONString(response));
            WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(response));
            weiXinException.setErrorCode(response.getErrcode());
            throw weiXinException;
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
