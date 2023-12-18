package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.material.MaterialCountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * 素材管理
 *
 * 永久素材的总数，也会计算公众平台官网素材管理中的素材
 * 图片和图文消息素材（包括单图文和多图文）的总数上限为100000，其他素材的总数上限为1000
 * @author chunyang.leng
 * @date 2022-08-12 5:25 PM
 */
public class MaterialManager {
    private RestTemplate restTemplate;
    private WeChatTokenManager weChatTokenManager;

    public MaterialManager(RestTemplate restTemplate, WeChatTokenManager weChatTokenManager) {
        this.restTemplate = restTemplate;
        this.weChatTokenManager = weChatTokenManager;
    }

    /**
     * 统计素材使用情况
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Get_the_total_of_all_materials.html">接口文档地址</a>
     */
    public MaterialCountResponse materialCount() throws WeChatException{
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.MATERIAL_COUNT, tokenFromLocal);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String json = response.getBody();
        MaterialCountResponse body = JSON.toJavaObject(JSON.parseObject(json),MaterialCountResponse.class);
        if (!body.isSuccessful()){
            throw new WeChatException(body);
        }
        return body;
    }
}
