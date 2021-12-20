package net.jlxxw.wechat.listener.event;

import net.jlxxw.wechat.component.listener.AbstractWeiXinEventListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.response.WeiXinMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 7:07 下午
 */
@Component
public class LocationSelectEventMessageListener extends AbstractWeiXinEventListener {
    /**
     * 支持的事件类型
     *
     * @return
     */
    @Override
    public WeChatEventTypeEnum supportEventType() {
        return WeChatEventTypeEnum.LOCATION_SELECT;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    @Override
    public WeiXinMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        Assert.assertNotNull("接收到的数据不应为空", abstractWeChatMessage);
        return WeiXinMessageResponse.buildText(supportEventType().getDescription() + " done");
    }
}