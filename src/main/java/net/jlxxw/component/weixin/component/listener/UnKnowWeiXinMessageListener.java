package net.jlxxw.component.weixin.component.listener;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 未知的微信消息处理器
 * @author chunyang.leng
 * @date 2021-03-08 4:38 下午
 */
public interface UnKnowWeiXinMessageListener {
    /**
     * 处理其他类型信息
     * @param objectNode 微信传过来到数据
     * @return 微信返回值,xml格式信息，需要自己处理加解密
     */
     String handlerOtherType(ObjectNode objectNode);
}
