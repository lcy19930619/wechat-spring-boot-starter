package net.jlxxw.wechat.response;

/**
 * 微信应答对象
 *
 * @author chunyang.leng
 * @date 2020/11/11 13:08
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Global_Return_Code.html">错误码文档</a>
 */
public class WeChatResponse {
    /**
     * 正常时是0
     */
    private Integer errcode;
    /**
     * 正常是OK
     */
    private String errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * 判断操作是否成功
     * @return
     */
    public boolean isSuccessful(){
        return errcode == null || errcode == 0;
    }

}
