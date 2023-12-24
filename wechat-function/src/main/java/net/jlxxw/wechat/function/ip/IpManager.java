package net.jlxxw.wechat.function.ip;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.ip.IpListResponse;
import net.jlxxw.wechat.response.material.MaterialCountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static net.jlxxw.wechat.constant.UrlConstant.WECHAT_CALL_BACK_SERVER_IP_PREFIX;

public class IpManager {
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    public IpManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }

    /**
     * 获取微信回调服务器ip地址
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_the_WeChat_server_IP_address.html">文档地址</a>
     * @return
     * @throws WeChatException
     */
    public IpListResponse getCallbackIp() throws WeChatException {
        String url = MessageFormat.format(WECHAT_CALL_BACK_SERVER_IP_PREFIX,weChatTokenRepository.get());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String json = response.getBody();
        IpListResponse body = JSON.toJavaObject(JSON.parseObject(json),IpListResponse.class);
        if (!body.isSuccessful()){
            throw new WeChatException(body);
        }
        return body;
    }
}
