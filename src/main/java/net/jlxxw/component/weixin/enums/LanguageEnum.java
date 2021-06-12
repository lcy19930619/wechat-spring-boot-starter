package net.jlxxw.component.weixin.enums;

/**
 * @author chunyang.leng
 * @date 2021/1/25 6:47 下午
 */
public enum LanguageEnum {

    /**
     * 简体
     */
    ZH_CN("zh_CN"),
    /**
     * 繁体
     */
    ZH_TW("zh_TW"),
    /**
     * 英语
     */
    EN("en");


    private final String code;

    LanguageEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
