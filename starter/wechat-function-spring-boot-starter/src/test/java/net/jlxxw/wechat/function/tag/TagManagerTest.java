package net.jlxxw.wechat.function.tag;

import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.tag.Tag;
import net.jlxxw.wechat.response.tag.TagResponse;
import net.jlxxw.wechat.response.tag.TagUserResponse;
import net.jlxxw.wechat.response.tag.User;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2022-08-29 12:50 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TagManagerTest  {

    @Value("${wechat.test.openid}")
    private  String openId;

    private static final String tagValue = "单元测试使用的tagValue";
    private static final String tagValue2 = "单元测试使用的tagValue2";

    private static Integer TAG_ID = null;
    @Autowired
    private TagManager tagManager;

    @Test
    public void test_0_createTag() {
        TagResponse tag = tagManager.createTag(tagValue);
        Assert.assertTrue("创建标签不应该失败", tag.isSuccessful());
        Tag tag1 = tag.getTag();
        TAG_ID = tag1.getId();
        Assert.assertNotNull("标签id不应该为空", TAG_ID);
    }

    @Test
    public void test_1_getTagTest() {
        List<Tag> tag = tagManager.getTag();
        Assert.assertNotNull("标签不应该为空", tag);
    }

    @Test
    public void test_2_batchTagging() {
        List<String> list = Collections.singletonList(openId);
        WeChatResponse weChatResponse = tagManager.batchTagging(list, TAG_ID);
        Assert.assertTrue("批量打标签不应该失败", weChatResponse.isSuccessful());
    }

    @Test
    public void test_3_getTagUserTest() {
        TagUserResponse user = tagManager.getTagUser(TAG_ID, "");
        Assert.assertTrue("获取用户标签不应该失败", user.isSuccessful());
        User data = user.getData();
        List<String> id = data.getOpenId();
        Assert.assertFalse("获取的用户数量，不应该为空", CollectionUtils.isEmpty(id));
    }

    @Test
    public void test_4_getUserTags() {
        List<Integer> tags = tagManager.getUserTags(openId);
        Assert.assertFalse("获取的用户标签数量，不应该为空", CollectionUtils.isEmpty(tags));
    }


    @Test
    public void test_5_updateTag() {
        WeChatResponse weChatResponse = tagManager.updateTag(TAG_ID, tagValue2);
        Assert.assertTrue("更新标签不应该失败", weChatResponse.isSuccessful());
    }


    @Test
    public void test_6_batchUnTagging() {
        List<String> list = Collections.singletonList(openId);
        WeChatResponse weChatResponse = tagManager.batchUnTagging(list, TAG_ID);
        Assert.assertTrue("批量取消标签不应该失败", weChatResponse.isSuccessful());
    }

    @Test
    public void test_7_deleteTag() {
        WeChatResponse weChatResponse = tagManager.delete(TAG_ID);
        Assert.assertTrue("删除标签不应该失败", weChatResponse.isSuccessful());
    }

}
