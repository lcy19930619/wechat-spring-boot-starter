package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.function.token.WeChatTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 素材管理
 *
 * 永久素材的总数，也会计算公众平台官网素材管理中的素材
 * 图片和图文消息素材（包括单图文和多图文）的总数上限为100000，其他素材的总数上限为1000
 * @author chunyang.leng
 * @date 2022-08-12 5:25 PM
 */
@DependsOn( "weChatTokenManager")
@Component
public class MaterialManager {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

    /**
     * 统计素材使用情况
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Get_the_total_of_all_materials.html">接口文档地址</a>
     */
    public void materialCount(){

    }


}
