package net.jlxxw.wechat.event;

import net.jlxxw.wechat.vo.jsapi.v3.PayResultVO;
import org.springframework.context.ApplicationEvent;

/**
 * 预支付订单事件
 *
 * @author chunyang.leng
 * @date 2021-04-23 9:53 上午
 */
public class CreatePrePayEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CreatePrePayEvent(PayResultVO source) {
        super(source);
    }

    public PayResultVO getPayResultVO() {
        return (PayResultVO) super.getSource();
    }
}
