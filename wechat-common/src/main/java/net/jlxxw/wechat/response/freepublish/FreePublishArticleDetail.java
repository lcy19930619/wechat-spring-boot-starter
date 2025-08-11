package net.jlxxw.wechat.response.freepublish;

import java.util.List;
import java.util.Objects;

/**
 * 文章详情信息
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_get.html">文档地址</a>
 */
public class FreePublishArticleDetail {

    /**
     * 当发布状态为0时（即成功）时，返回文章数量
     */
    private Integer count;

    /**
     * 当发布状态为0时（即成功）时，返回文章详情
     */
    private List<FreePublishArticleItem> item;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<FreePublishArticleItem> getItem() {
        return item;
    }

    public void setItem(List<FreePublishArticleItem> item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishArticleDetail)) {
            return false;
        }
        FreePublishArticleDetail that = (FreePublishArticleDetail) o;
        return Objects.equals(count, that.count) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, item);
    }

    @Override
    public String toString() {
        return "FreePublishArticleDetail{" +
            "count=" + count +
            ", item=" + item +
            '}';
    }
}