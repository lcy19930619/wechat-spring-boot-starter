package net.jlxxw.wechat.dto.customer;

import java.util.ArrayList;
import java.util.List;

/**
 * 客服信息基础类
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html">文档地址</a>
 * @author chunyang.leng
 * @date 2020/11/11 12:51
 */
public class CustomerMessageDTO {
    /**
     * 接收用户的openId
     */
    private String touser;
    /**
     * 信息类型
     * news 发送图文消息（小小的图文框，一张缩略图，带上几个文字）（点击跳转到外链） 图文消息条数限制在1条以内，注意，如果图文数超过1，则将会返回错误码45008。
     * music 发送音乐消息
     * video 发送视频消息
     * voice 发送语音消息
     * image 发送图片消息
     * mpnews 发送图文消息（聊天框100%宽度的那种）（点击跳转到图文消息页面） 图文消息条数限制在1条以内，注意，如果图文数超过1，则将会返回错误码45008。
     *
     * msgmenu 发送菜单消息
     *
     * wxcard 发送卡券
     *
     * miniprogrampage 发送小程序卡片
     */
    private String msgtype;

    /**
     * 图文类型
     */
    private NewsDTO news;

    /**
     * 文字类型
     */
    private TextDTO text;

    /**
     * 图片信息
     */
    private ImageDTO image;

    /**
     * 语音消息
     */
    private VoiceDTO voice;

    /**
     * 视频消息
     */
    private VideoDTO video;

    /**
     * 音乐消息
     */
    private MusicDTO music;
    /**
     * 大图文信息
     */
    private MpnewsDTO mpnews;
    /**
     * 菜单消息
      */
    private MenuDTO msgmenu;

    /**
     * 卡券消息
     */
    private WxCardDTO wxcard;

    /**
     * 小程序消息
     */
    private MiniProgramPageDTO miniprogrampage;

    public TextDTO getText() {
        return text;
    }

    public void setText(TextDTO text) {
        this.text = text;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public NewsDTO getNews() {
        return news;
    }

    public void setNews(NewsDTO newsDTO) {
        this.news = newsDTO;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public VoiceDTO getVoice() {
        return voice;
    }

    public void setVoice(VoiceDTO voice) {
        this.voice = voice;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    public MusicDTO getMusic() {
        return music;
    }

    public void setMusic(MusicDTO music) {
        this.music = music;
    }

    public MpnewsDTO getMpnews() {
        return mpnews;
    }

    public void setMpnews(MpnewsDTO mpnews) {
        this.mpnews = mpnews;
    }

    public MenuDTO getMsgmenu() {
        return msgmenu;
    }

    public void setMsgmenu(MenuDTO msgmenu) {
        this.msgmenu = msgmenu;
    }

    public WxCardDTO getWxcard() {
        return wxcard;
    }

    public void setWxcard(WxCardDTO wxcard) {
        this.wxcard = wxcard;
    }

    public MiniProgramPageDTO getMiniprogrampage() {
        return miniprogrampage;
    }

    public void setMiniprogrampage(MiniProgramPageDTO miniprogrampage) {
        this.miniprogrampage = miniprogrampage;
    }

    private CustomerMessageDTO() {

    }

    private CustomerMessageDTO(String touser, String msgtype, NewsDTO newsDTO) {
        this.touser = touser;
        this.msgtype = msgtype;
        this.news = newsDTO;
    }

    private CustomerMessageDTO(String touser, String msgtype, TextDTO text) {
        this.touser = touser;
        this.msgtype = msgtype;
        this.text = text;
    }

    private CustomerMessageDTO(String touser, String msgtype, ImageDTO image) {
        this.touser = touser;
        this.msgtype = msgtype;
        this.image = image;
    }

    /**
     * 构建图文类信息模板
     * @param openId 接收用户
     * @param articlesDTO 图文信息，只能有一个
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildNews(String openId , ArticlesDTO articlesDTO){
        NewsDTO newsDTO = new NewsDTO();
        List<ArticlesDTO> articlesDTOList = new ArrayList<>();
        articlesDTOList.add(articlesDTO);
        newsDTO.setArticles(articlesDTOList);
        return new CustomerMessageDTO(openId,"news", newsDTO);
    }

    /**
     * 构建文本传输对象
     * @param openId 接受的用户
     * @param text 传递的内容
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildText(String openId , String text){
        TextDTO dto = new TextDTO();
        dto.setContent(text);
        return new CustomerMessageDTO(openId,"text", dto);
    }

    /**
     * 构建图片传输对象
     * @param openId 接受的用户
     * @param mediaId 图片id
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildImage(String openId , String mediaId){
        ImageDTO dto = new ImageDTO();
        dto.setMediaId(mediaId);
        return new CustomerMessageDTO(openId,"image", dto);
    }


    /**
     * 构建语音消息信息模板
     * @param openId 接收用户
     * @param dto 语音消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildVoice(String openId , VoiceDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("voice");
        customerMessageDTO.setVoice(dto);
        return customerMessageDTO;
    }

    /**
     * 构建视频消息信息模板
     * @param openId 接收用户
     * @param dto 视频消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildVideo(String openId , VideoDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("video");
        customerMessageDTO.setVideo(dto);
        return customerMessageDTO;
    }

    /**
     * 构建音乐消息信息模板
     * @param openId 接收用户
     * @param dto 音乐消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildMusic(String openId , MusicDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("music");
        customerMessageDTO.setMusic(dto);
        return customerMessageDTO;
    }

    /**
     * 构建大图文消息信息模板
     * @param openId 接收用户
     * @param dto 大图文消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildMpNews(String openId , MpnewsDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("mpnews");
        customerMessageDTO.setMpnews(dto);
        return customerMessageDTO;
    }

    /**
     * 构建大图文消息信息模板
     * @param openId 接收用户
     * @param mediaId 大图文消息id
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildMpNews(String openId , String mediaId ){
        MpnewsDTO dto = new MpnewsDTO();
        dto.setMediaId(mediaId);
        return buildMpNews(openId,dto);
    }
    /**
     * 构建菜单消息信息模板
     * @param openId 接收用户
     * @param dto 菜单消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildMsgMenu(String openId , MenuDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("msgmenu");
        customerMessageDTO.setMsgmenu(dto);
        return customerMessageDTO;
    }

    /**
     * 构建卡券消息信息模板
     * @param openId 接收用户
     * @param dto 卡券消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildWxCard(String openId , WxCardDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("wxcard");
        customerMessageDTO.setWxcard(dto);
        return customerMessageDTO;
    }



    /**
     * 构建小程序信息模板
     * @param openId 接收用户
     * @param dto 小程序消息
     * @return 客服接口数据传输对象
     */
    public static CustomerMessageDTO buildMiniProgramPage(String openId , MiniProgramPageDTO dto){
        CustomerMessageDTO customerMessageDTO = new CustomerMessageDTO();
        customerMessageDTO.setTouser(openId);
        customerMessageDTO.setMsgtype("miniprogrampage");
        customerMessageDTO.setMiniprogrampage(dto);
        return customerMessageDTO;
    }

}
