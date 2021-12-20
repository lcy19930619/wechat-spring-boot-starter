package net.jlxxw.wechat.listener.event;

import net.jlxxw.wechat.component.listener.AbstractWeiXinEventListener;
import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;
import net.jlxxw.wechat.enums.WeiXinEventTypeEnum;
import net.jlxxw.wechat.response.WeiXinMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 7:09 下午
 */
@Component
public class UnSubscribeEventMessageListener extends AbstractWeiXinEventListener {
    /**
     * 支持的事件类型
     *
     * @return
     */
    @Override
    public WeiXinEventTypeEnum supportEventType() {
        return WeiXinEventTypeEnum.UNSUBSCRIBE;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeiXinMessage
     * @return
     */
    @Override
    public WeiXinMessageResponse handler(AbstractWeiXinMessage abstractWeiXinMessage) {
        Assert.assertNotNull("接收到的数据不应为空", abstractWeiXinMessage);
        return WeiXinMessageResponse.buildText(supportEventType().getDescription() + " done");
    }
}