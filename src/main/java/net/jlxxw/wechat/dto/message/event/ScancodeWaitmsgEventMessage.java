package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;
import net.jlxxw.wechat.dto.message.event.other.ScanCodeInfo;

/**
 * 扫码推事件且弹出“消息接收中”提示框的事件推送
 *
 * @author chunyang.leng
 * @date 2021-12-17 8:14 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#3">文档地址</a>
 */
public class ScancodeWaitmsgEventMessage extends AbstractWeiXinMessage {
    /**
     * 事件KEY值，由开发者在创建菜单时设定
     */
    private String eventKey;

    /**
     * 扫描信息
     */
    private ScanCodeInfo scanCodeInfo;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public ScanCodeInfo getScanCodeInfo() {
        return scanCodeInfo;
    }

    public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
        this.scanCodeInfo = scanCodeInfo;
    }
}
