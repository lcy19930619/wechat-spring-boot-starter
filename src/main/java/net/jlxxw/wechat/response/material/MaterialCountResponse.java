package net.jlxxw.wechat.response.material;

import com.alibaba.fastjson.annotation.JSONField;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 素材统计应答对象
 *
 * @author chunyang.leng
 * @date 2022-08-12 5:26 PM
 */
public class MaterialCountResponse extends WeChatResponse {

    /**
     * 语音总数量
     */
    @JSONField(name = "voice_count")
    private Integer voiceCount;

    /**
     * 视频总数量
     */
    @JSONField(name = "video_count")
    private Integer videoCount;

    /**
     * 图片总数量
     */
    @JSONField(name = "image_count")
    private Integer imageCount;

    /**
     * 图文总数量
     */
    @JSONField(name = "news_count")
    private Integer newsCount;


    public Integer getVoiceCount() {
        return voiceCount;
    }

    public void setVoiceCount(Integer voiceCount) {
        this.voiceCount = voiceCount;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getImageCount() {
        return imageCount;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public Integer getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(Integer newsCount) {
        this.newsCount = newsCount;
    }
}
