package net.jlxxw.wechat.dto.message.event;

/**
 * @author chunyang.leng todo
 * @date 2021-12-18 4:33 下午
 */
public class Item {
    /**
     * 图片的MD5值，开发者若需要，可用于验证接收到图片
     */
    private String picMd5Sum;

    public String getPicMd5Sum() {
        return picMd5Sum;
    }

    public void setPicMd5Sum(String picMd5Sum) {
        this.picMd5Sum = picMd5Sum;
    }

}
