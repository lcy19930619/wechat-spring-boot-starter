package net.jlxxw.wechat.component.listener;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 未知到微信事件处理器
 *
 * @author chunyang.leng
 * @date 2021-03-08 4:38 下午
 */
public interface UnKnowWeChatEventListener {
    /**
     * 处理其他类型信息
     *
     * @param objectNode 微信传过来到数据
     * @return 微信返回值, xml格式信息，需要自己处理加解密
     */
    String handlerOtherType(ObjectNode objectNode);
}
