package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;
import java.util.List;
import java.util.Objects;

/**
 * 获取发布状态的响应结果
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_get.html">文档地址</a>
 */
public class FreePublishGetResponse extends WeChatResponse {

    /**
     * 发布任务id
     */
    @JSONField(name = "publish_id")
    @JsonProperty(value = "publish_id")
    private String publishId;

    /**
     * 发布状态(0:成功,1:发布中,2:原创失败,3:常规失败,4:平台审核不通过,5:成功后用户删除所有文章,6:成功后系统封禁所有文章)
     */
    @JSONField(name = "publish_status")
    @JsonProperty(value = "publish_status")
    private Integer publishStatus;

    /**
     * 成功时的图文article_id
     */
    @JSONField(name = "article_id")
    @JsonProperty(value = "article_id")
    private String articleId;

    /**
     * 文章详情
     */
    @JSONField(name = "article_detail")
    @JsonProperty(value = "article_detail")
    private FreePublishArticleDetail articleDetail;

    /**
     * 失败文章编号
     */
    @JSONField(name = "fail_idx")
    @JsonProperty(value = "fail_idx")
    private List<Integer> failIdx;

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public FreePublishArticleDetail getArticleDetail() {
        return articleDetail;
    }

    public void setArticleDetail(FreePublishArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    public List<Integer> getFailIdx() {
        return failIdx;
    }

    public void setFailIdx(List<Integer> failIdx) {
        this.failIdx = failIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishGetResponse)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FreePublishGetResponse that = (FreePublishGetResponse) o;
        return Objects.equals(publishId, that.publishId) && Objects.equals(publishStatus, that.publishStatus)
            && Objects.equals(articleId, that.articleId) && Objects.equals(articleDetail, that.articleDetail)
            && Objects.equals(failIdx, that.failIdx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), publishId, publishStatus, articleId, articleDetail, failIdx);
    }

    @Override
    public String toString() {
        return "FreePublishGetResponse{" +
            "publishId='" + publishId + '\'' +
            ", publishStatus=" + publishStatus +
            ", articleId='" + articleId + '\'' +
            ", articleDetail=" + articleDetail +
            ", failIdx=" + failIdx +
            '}';
    }
}