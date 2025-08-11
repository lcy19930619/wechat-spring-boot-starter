package net.jlxxw.wechat.dto.draft;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 更新草稿请求DTO
 */
public class DraftUpdateDTO {
    /**
     * 要修改的图文消息的id
     */
    @NotBlank(message = "要修改的图文消息的id不能为空")
    private String mediaId;
    
    /**
     * 要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
     */
    @NotNull(message = "要更新的文章在图文消息中的位置不能为空")
    private Integer index;

    @NotEmpty(message = "要更新的文章列表不能为空")
    private List<Article> articles;

    public @NotBlank(message = "要修改的图文消息的id不能为空") String getMediaId() {
        return mediaId;
    }

    public void setMediaId(@NotBlank(message = "要修改的图文消息的id不能为空") String mediaId) {
        this.mediaId = mediaId;
    }

    public @NotNull(message = "要更新的文章在图文消息中的位置不能为空") Integer getIndex() {
        return index;
    }

    public void setIndex(@NotNull(message = "要更新的文章在图文消息中的位置不能为空") Integer index) {
        this.index = index;
    }

    public @NotEmpty(message = "要更新的文章列表不能为空") List<Article> getArticles() {
        return articles;
    }

    public void setArticles(@NotEmpty(message = "要更新的文章列表不能为空") List<Article> articles) {
        this.articles = articles;
    }
}