package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


/**
 * 新增草稿请求DTO
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_add.html">原始文档地址</>
 */
public class DraftAddDTO {

    /**
     * 图文消息的描述，如本字段为空，则默认抓取正文前54个字
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