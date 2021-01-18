package net.jlxxw.component.weixin.dto.customer.news;

import java.util.List;

/**

 * @author chunyang.leng
 * @date 2020/11/11 12:53
 */
public class NewsDTO {
    private List<ArticlesDTO> articles;

    public List<ArticlesDTO> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesDTO> articles) {
        this.articles = articles;
    }
}
