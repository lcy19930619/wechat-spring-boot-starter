package net.jlxxw.wechat.listener.message;

import net.jlxxw.wechat.component.listener.AbstractWeiXinMessageListener;
import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;
import net.jlxxw.wechat.enums.WeiXinMessageTypeEnum;
import net.jlxxw.wechat.response.WeiXinMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 6:57 下午
 */
@Component
public class LocaltionMessageListener extends AbstractWeiXinMessageListener {
    /**
     * 支持的消息事件类型
     *
     * @return
     */
    @Override
    public WeiXinMessageTypeEnum supportMessageType() {
        return WeiXinMessageTypeEnum.LOCATION;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeiXinMessage
     * @return
     */
    @Override
    public WeiXinMessageResponse handler(AbstractWeiXinMessage abstractWeiXinMessage) {
        Assert.assertNotNull("接收到的信息不应为空",abstractWeiXinMessage);
        return WeiXinMessageResponse.buildText(supportMessageType().getDescription()+"done");
    }
}
