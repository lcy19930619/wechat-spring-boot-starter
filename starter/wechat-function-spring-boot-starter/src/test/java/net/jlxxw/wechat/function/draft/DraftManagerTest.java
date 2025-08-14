package net.jlxxw.wechat.function.draft;

import net.jlxxw.wechat.dto.draft.*;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.function.material.PermanentMaterialManager;
import net.jlxxw.wechat.function.utils.ImageUtils;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.draft.DraftAddResponse;
import net.jlxxw.wechat.response.draft.DraftCountResponse;
import net.jlxxw.wechat.response.draft.DraftGetResponse;
import net.jlxxw.wechat.response.draft.DraftListResponse;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2022-08-29 12:50 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class DraftManagerTest {
    private String mediaId = "VGpFMieccgZX80T3qYuqLEekRiPsFp8XwSwCCCypq8oDrMucRYIue2u6ua9CYX6A";


    private String DRAFT_MEDIA_ID = "VGpFMieccgZX80T3qYuqLD4CYqEQQavAIm0XjFJWWMPXLmbv428GrfMb4D5F1e8H";

    @Autowired
    private DraftManager draftManager;

    @Autowired
    private PermanentMaterialManager permanentMaterialManager;


    @Test
    public void test_0_addDraft()  {
        DraftAddDTO draftAddDTO = new DraftAddDTO();
        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        article.setArticleType("news");
        article.setTitle("单元测试标题2");
        article.setAuthor("单元测试作者2");
        article.setDigest("单元测试摘要");
        article.setContent("单元测试内容");

        article.setThumbMediaId(mediaId);

        articles.add(article);
        draftAddDTO.setArticles(articles);

        DraftAddResponse response = draftManager.addDraft(draftAddDTO);
        Assertions.assertTrue(response.isSuccessful(), "创建草稿不应该失败");
        DRAFT_MEDIA_ID = response.getMediaId();
        Assertions.assertNotNull(DRAFT_MEDIA_ID, "草稿media_id不应该为空");


        WeChatResponse weChatResponse = permanentMaterialManager.deleteMaterial(mediaId);
        Assertions.assertTrue(weChatResponse.isSuccessful(), "删除永久素材不应该失败");
    }

    @Test
    public void test_1_getDraft() {
        DraftGetResponse response = draftManager.getDraft(DRAFT_MEDIA_ID);
        Assertions.assertTrue( response.isSuccessful(),"获取草稿详情不应该失败");
        Assertions.assertNotNull(response.getNewsItem(), "获取的草稿详情不应该为空");
    }

    @Test
    public void test_2_updateDraft() {
        DraftUpdateDTO draftUpdateDTO = new DraftUpdateDTO();
        draftUpdateDTO.setMediaId(DRAFT_MEDIA_ID);
        draftUpdateDTO.setIndex(0);

        Article article = new Article();
        article.setArticleType("news");
        article.setTitle("单元测试更新标题");
        article.setAuthor("单元测试更新作者");
        article.setDigest("单元测试更新摘要");
        article.setContent("单元测试更新内容");
        article.setThumbMediaId("VGpFMieccgZX80T3qYuqLJQslJRWpVn9ixxO7cSV_KQB1VharIddCGe-76lyuolb");
        draftUpdateDTO.setArticles(article);

        WeChatResponse response = draftManager.updateDraft(draftUpdateDTO);
        Assertions.assertTrue(response.isSuccessful(), "更新草稿不应该失败");
    }

    @Test
    public void test_3_getDraftList() {
        DraftListDTO draftListDTO = new DraftListDTO();
        draftListDTO.setOffset(0);
        draftListDTO.setCount(10);
        draftListDTO.setNoContent(0);

        DraftListResponse response = draftManager.getDraftList(draftListDTO);
        Assertions.assertTrue(response.isSuccessful(), "获取草稿列表不应该失败");
        Assertions.assertNotNull(response.getItem(), "获取的草稿列表不应该为空");
    }

    @Test
    public void test_4_getDraftCount() {
        DraftCountResponse response = draftManager.getDraftCount();
        Assertions.assertTrue(response.isSuccessful(), "获取草稿总数不应该失败");
        Assertions.assertNotNull(response.getTotalCount(), "获取的草稿总数不应该为空");
    }

    @Test
    public void test_5_deleteDraft() {
        WeChatResponse response = draftManager.deleteDraft(DRAFT_MEDIA_ID);
        Assertions.assertTrue( response.isSuccessful(),"删除草稿不应该失败");
    }
}