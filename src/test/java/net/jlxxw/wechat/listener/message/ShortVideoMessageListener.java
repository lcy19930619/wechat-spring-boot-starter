package net.jlxxw.wechat.listener.message;

import net.jlxxw.wechat.component.listener.AbstractWeiXinMessageListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 6:57 下午
 */
@Component
public class ShortVideoMessageListener extends AbstractWeiXinMessageListener {
    /**
     * 支持的消息事件类型
     *
     * @return
     */
    @Override
    public WeChatMessageTypeEnum supportMessageType() {
        return WeChatMessageTypeEnum.SHORT_VIDEO;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        Assert.assertNotNull("接收到的信息不应为空", abstractWeChatMessage);
        return WeChatMessageResponse.buildText(supportMessageType().getDescription() + "done");
    }
}
