package net.jlxxw.wechat.dto.feign.api;

import javax.validation.constraints.NotBlank;

/**
 * @author chunyang.leng
 * @date 2022-04-14 11:36 AM
 */
public class ApiRidDTO extends ApiDTO{

    /**
     * 信息id
     */
    @NotBlank(message = "rid 不能为空")
    private String rid;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
