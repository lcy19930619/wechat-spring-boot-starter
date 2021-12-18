package net.jlxxw.wechat.function.user;

import net.jlxxw.wechat.dto.user.SubscriptionUser;
import net.jlxxw.wechat.enums.LanguageEnum;

import java.util.List;
import java.util.Set;

/**
 * @author chunyang.leng
 * @date 2021/1/25 6:27 下午
 */
public interface UserManager {

    /**
     * 获取全部用户的openId
     *
     * @return
     */
    Set<String> findAll();


    /**
     * 批量根据用户的openId获取用户的基本信息
     * @param openIdList openId列表
     * @param languageEnum 返回字体语言
     * @return 用户基本信息
     */
    List<SubscriptionUser> findUserInfo(List<String> openIdList, LanguageEnum languageEnum);

    /**
     * 获取一个用户的基本信息
     * @param openId 用户的openId
     * @param languageEnum 返回字体语言
     * @return 用户的基本信息
     */
    SubscriptionUser getUserInfo(String openId, LanguageEnum languageEnum);


}
