package net.jlxxw.wechat.dto.customer;


import net.jlxxw.wechat.dto.customer.news.ArticlesDTO;
import net.jlxxw.wechat.dto.customer.news.ImageDTO;
import net.jlxxw.wechat.dto.customer.news.NewsDTO;
import net.jlxxw.wechat.dto.customer.news.TextDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 客服信息基础类
 *
 * @author chunyang.leng
 * @date 2020/11/11 12:51
 */
public class CustomerMessageDTO {
    /**
     * 接收用户的openId
     */
    private String touser;
    /**
     * 信息类型 news music video voice image
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

    public TextDTO getText() {
        return text;
    }

    public void setText(TextDTO text) {
        this.text = text;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
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
     *
     * @param openId      接收用户
     * @param articlesDTO 图文信息，只能有一个
     * @return
     */
    public static CustomerMessageDTO buildNews(String openId, ArticlesDTO articlesDTO) {
        NewsDTO newsDTO = new NewsDTO();
        List<ArticlesDTO> articlesDTOList = new ArrayList<>();
        articlesDTOList.add(articlesDTO);
        newsDTO.setArticles(articlesDTOList);
        return new CustomerMessageDTO(openId, "news", newsDTO);
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
}
