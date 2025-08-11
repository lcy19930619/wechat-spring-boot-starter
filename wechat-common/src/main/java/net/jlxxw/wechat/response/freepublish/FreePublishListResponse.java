package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;
import java.util.List;
import java.util.Objects;

/**
 * 获取已发布文章列表的响应结果
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">文档地址</a>
 */
public class FreePublishListResponse extends WeChatResponse {

    /**
     * 成功发布素材的总数
     */
    @JSONField(name = "total_count")
    @JsonProperty(value = "total_count")
    private Integer totalCount;

    /**
     * 本次调用获取的素材的数量
     */
    @JSONField(name = "item_count")
    @JsonProperty(value = "item_count")
    private Integer itemCount;

    /**
     * 图文消息条目列表
     */
    private List<FreePublishItem> item;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public List<FreePublishItem> getItem() {
        return item;
    }

    public void setItem(List<FreePublishItem> item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishListResponse)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FreePublishListResponse that = (FreePublishListResponse) o;
        return Objects.equals(totalCount, that.totalCount) && Objects.equals(itemCount, that.itemCount)
            && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalCount, itemCount, item);
    }

    @Override
    public String toString() {
        return "FreePublishListResponse{" +
            "totalCount=" + totalCount +
            ", itemCount=" + itemCount +
            ", item=" + item +
            '}';
    }
}