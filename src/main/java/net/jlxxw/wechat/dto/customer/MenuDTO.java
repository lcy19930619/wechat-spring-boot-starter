package net.jlxxw.wechat.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author chunyang.leng
 * @date 2022-06-28 3:21 PM
 */
public class MenuDTO {

    /**
     * 首部内容
     */
    @JsonProperty(value = "head_content")
    private String headContent;

    /**
     * 尾部内容
     */
    @JsonProperty(value = "tail_content")
    private String tailContent;

    /**
     * 菜单列表
     */
    private List<Menu> list;

    public String getHeadContent() {
        return headContent;
    }

    public void setHeadContent(String headContent) {
        this.headContent = headContent;
    }

    public String getTailContent() {
        return tailContent;
    }

    public void setTailContent(String tailContent) {
        this.tailContent = tailContent;
    }

    public List<Menu> getList() {
        return list;
    }

    public void setList(List<Menu> list) {
        this.list = list;
    }

    static class Menu{
        /**
         * 菜单id
         */
        private String id;
        /**
         * 菜单名称
         */
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
