package net.jlxxw.wechat.dto.customer;


import net.jlxxw.wechat.dto.customer.news.ArticlesDTO;
import net.jlxxw.wechat.dto.customer.news.NewsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 客服信息基础类
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

    private CustomerMessageDTO(String touser, String msgtype, NewsDTO newsDTO) {
        this.touser = touser;
        this.msgtype = msgtype;
        this.news = newsDTO;
    }

    /**
     * 构建图文类信息模板
     * @param openId 接收用户
     * @param articlesDTO 图文信息，只能有一个
     * @return
     */
    public static CustomerMessageDTO buildNews(String openId , ArticlesDTO articlesDTO){
        NewsDTO newsDTO = new NewsDTO();
        List<ArticlesDTO> articlesDTOList = new ArrayList<>();
        articlesDTOList.add(articlesDTO);
        newsDTO.setArticles(articlesDTOList);
        return new CustomerMessageDTO(openId,"news", newsDTO);
    }
}
