package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增草稿请求DTO
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_add.html">原始文档地址</>
 */
public class DraftAddDTO {

    /**
     * 图文素材集合
     */
    @JSONField(name = "articles")
    @JsonProperty(value = "articles")
    private List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}