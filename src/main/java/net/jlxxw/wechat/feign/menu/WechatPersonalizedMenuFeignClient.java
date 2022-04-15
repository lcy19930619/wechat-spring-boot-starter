package net.jlxxw.wechat.feign.menu;

import net.jlxxw.wechat.aop.check.group.Delete;
import net.jlxxw.wechat.aop.check.group.Select;
import net.jlxxw.wechat.dto.menu.PersonalizedMenuDTO;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.menu.MatchPersonalizedMenuResponse;
import net.jlxxw.wechat.response.menu.PersonalizedMenuResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 * 个性化菜单接口
 *
 * 为了帮助公众号实现灵活的业务运营，微信公众平台新增了个性化菜单接口，开发者可以通过该接口，让公众号的不同用户群体看到不一样的自定义菜单。该接口开放给已认证订阅号和已认证服务号。
 *
 * 开发者可以通过以下条件来设置用户看到的菜单：
 *
 * 用户标签（开发者的业务需求可以借助用户标签来完成）
 * <b><font color='red'>已废弃</font></b>性别
 * 手机操作系统
 * <b><font color='red'>已废弃</font></b>地区（用户在微信客户端设置的地区）
 * <b><font color='red'>已废弃</font></b>语言（用户在微信客户端设置的语言）
 *
 * <b>
 * 注意：为保护个人隐私，公众号个性化菜单将不再支持对性别、地区、语言这类涉及个人隐私数据的信息进行筛选的功能，具体调整如下：
 *
 * 创建时，只要匹配条件中包含隐私信息的，将被拒绝，并返回错误码 65320；
 * 已经创建的，如包含隐私信息的则自动失效，不包含的则正常匹配；
 * 开发者仍然可以正常通过测试接口，获取到粉丝看到的菜单；
 * 查询个性化菜单时，所有规则正常显示。
 *
 * </b>
 * 个性化菜单接口说明：
 *
 * 个性化菜单要求用户的微信客户端版本在iPhone6.2.2，Android 6.2.4以上，暂时不支持其他版本微信
 * 菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
 * 如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
 * 测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果
 * 普通公众号的个性化菜单的新增接口每日限制次数为2000次，删除接口也是2000次，测试个性化菜单匹配结果接口为20000次
 * 出于安全考虑，一个公众号的所有个性化菜单，最多只能设置为跳转到3个域名下的链接
 * 创建个性化菜单之前必须先创建默认菜单（默认菜单是指使用普通自定义菜单创建接口创建的菜单）。如果删除默认菜单，个性化菜单也会全部删除
 * 个性化菜单接口支持用户标签，请开发者注意，当用户身上的标签超过1个时，以最后打上的标签为匹配
 *
 * 个性化菜单匹配规则说明：
 *
 * 个性化菜单的更新是会被覆盖的。
 * 例如公众号先后发布了默认菜单，个性化菜单1，个性化菜单2，个性化菜单3。那么当用户进入公众号页面时，将从个性化菜单3开始匹配，
 * 如果个性化菜单3匹配成功，则直接返回个性化菜单3，否则继续尝试匹配个性化菜单2，直到成功匹配到一个菜单。
 * 根据上述匹配规则，为了避免菜单生效时间的混淆，决定不予提供个性化菜单编辑API，开发者需要更新菜单时，需将完整配置重新发布一轮。
 * </pre>
 *
 * @author chunyang.leng
 * @date 2021-04-15 02:12 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Personalized_menu_interface.html">文档地址</a>
 */
@FeignClient(value = "wechat-personalize-menu-client", url = "https://api.weixin.qq.com")
public interface WechatPersonalizedMenuFeignClient {

    /**
     * 创建个性化菜单
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Personalized_menu_interface.html">文档地址</a>
     * @param personalizedMenuDTO 个性化菜单数据
     * @param token token
     * @return
     * 正确时的返回JSON数据包 ,例如：{"menuid":"208379533"}
     * <br/>
     * 注意
     * 请留意参数说明表中已废止的字段，这些字段涉及公民个人隐私，如填写这些字段，接口将返回以下结果：
     * <pre>
     *     {"errcode":65320,"errmsg":"match rule violates privacy"}
     * </pre>
     *
     * 错误时的返回码请见接口返回码说明。
     */
    @PostMapping("cgi-bin/menu/addconditional?access_token={token}")
    PersonalizedMenuResponse createMenu(@Validated @RequestBody PersonalizedMenuDTO personalizedMenuDTO,
                                        @Validated @PathVariable("token") String token);


    /**
     * 删除菜单数据
     *
     * @param personalizedMenuDTO menu_id 要删除的个性化菜单条件id
     * @return 正确时的返回JSON数据包 {"errcode":0,"errmsg":"ok"}，错误时的返回码请见接口返回码说明。
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Personalized_menu_interface.html">文档地址</a>
     */
    @PostMapping("cgi-bin/menu/delconditional?access_token={token}")
    WeChatResponse deleteMenu(@Validated(Delete.class) @RequestBody PersonalizedMenuDTO personalizedMenuDTO,
                              @Validated @PathVariable("token") String token);


    /**
     * 尝试匹配用户信息
     *
     * @param personalizedMenuDTO user_id 可以是粉丝的OpenID，也可以是粉丝的微信号。
     * @param token token
     * @return 菜单信息列表
     */
    @PostMapping("cgi-bin/menu/trymatch?access_token={token}")
    MatchPersonalizedMenuResponse tryMatch(@Validated(Select.class) @RequestBody PersonalizedMenuDTO personalizedMenuDTO,
                                           @Validated @PathVariable("token") String token);
}
