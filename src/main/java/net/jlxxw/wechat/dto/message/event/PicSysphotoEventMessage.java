package net.jlxxw.wechat.dto.message.event;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;

/**
 *
 * 弹出系统拍照发图的事件推送 todo
 * @author chunyang.leng
 * @date 2021-12-17 8:15 下午
 */
public class PicSysphotoEventMessage extends AbstractWeiXinMessage {

    /**
     * 事件KEY值，由开发者在创建菜单时设定
     */
    private String eventKey;

    /**
     * 发送的图片信息
     */
    private SendPicsInfo sendPicsInfo;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public SendPicsInfo getSendPicsInfo() {
        return sendPicsInfo;
    }

    public void setSendPicsInfo(SendPicsInfo sendPicsInfo) {
        this.sendPicsInfo = sendPicsInfo;
    }


    public static void main(String[] args) {
        String json = "{\n" +
                "  \"createTime\": 1408090651,\n" +
                "  \"event\": \"pic_sysphoto\",\n" +
                "  \"eventKey\": \"6\",\n" +
                "  \"fromUserName\": \"oMgHVjngRipVsoxg6TuX3vz6glDg\",\n" +
                "  \"msgType\": \"event\",\n" +
                "  \"sendPicsInfo\": {\n" +
                "    \"count\": 1,\n" +
                "    \"picList\": [\n" +
                "      {\n" +
                "        \"item\": {\n" +
                "          \"PicMd5Sum\": \"1b5f7c23b5bf75682a53e7b6d163e185\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"toUserName\": \"gh_e136c6e50636\"\n" +
                "}";

        PicSysphotoEventMessage picSysphotoEventMessage = JSON.parseObject(json).toJavaObject(PicSysphotoEventMessage.class);
        System.out.println();
    }
}
