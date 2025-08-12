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
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
public class DraftManagerTest {


    private String DRAFT_MEDIA_ID = null;

    @Autowired
    private DraftManager draftManager;

    @Autowired
    private PermanentMaterialManager permanentMaterialManager;


    @Test
    public void test_0_addDraft() throws IOException {
        DraftAddDTO draftAddDTO = new DraftAddDTO();
        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        article.setArticleType("news");
        article.setTitle("单元测试标题");
        article.setAuthor("单元测试作者");
        article.setDigest("单元测试摘要");
        article.setContent("单元测试内容");

        ClassPathResource classPathResource = new ClassPathResource("/mock/data/base64/test.jpg.base64");
        InputStream inputStream = classPathResource.getInputStream();
        byte[] bytes = IOUtils.readFully(inputStream, inputStream.available());
        File file = new File(".","test.jpg");
        ImageUtils.saveImage(bytes,file);
        Assertions.assertTrue(file.exists(), "文件应该转化成功");

        PermanentMaterialResponse upload = permanentMaterialManager.upload(MaterialEnum.IMAGE, file, null, null);
        Assertions.assertTrue(upload.isSuccessful(), "上传成功");
        String mediaId = upload.getMediaId();



        ImageInfo imageInfo = new ImageInfo();
        InnerImage innerImage = new InnerImage();
        innerImage.setImageMediaId(mediaId);
        imageInfo.setImageList(Collections.singletonList(innerImage));
        article.setImageInfo(imageInfo);
        articles.add(article);
        draftAddDTO.setArticles(articles);

        DraftAddResponse response = draftManager.addDraft(draftAddDTO);
        Assert.assertTrue("创建草稿不应该失败", response.isSuccessful());
        DRAFT_MEDIA_ID = response.getMediaId();
        Assert.assertNotNull("草稿media_id不应该为空", DRAFT_MEDIA_ID);


        WeChatResponse weChatResponse = permanentMaterialManager.deleteMaterial(mediaId);
        Assert.assertTrue("删除永久素材不应该失败", weChatResponse.isSuccessful());
    }

    @Test
    public void test_1_getDraft() {
        DraftGetResponse response = draftManager.getDraft(DRAFT_MEDIA_ID);
        Assert.assertTrue("获取草稿详情不应该失败", response.isSuccessful());
        Assert.assertNotNull("获取的草稿详情不应该为空", response.getNewsItem());
    }

    @Test
    public void test_2_updateDraft() {
        DraftUpdateDTO draftUpdateDTO = new DraftUpdateDTO();
        draftUpdateDTO.setMediaId(DRAFT_MEDIA_ID);
        draftUpdateDTO.setIndex(0);

        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        article.setTitle("单元测试更新标题");
        article.setAuthor("单元测试更新作者");
        article.setDigest("单元测试更新摘要");
        article.setContent("单元测试更新内容");
        articles.add(article);
        draftUpdateDTO.setArticles(articles);

        WeChatResponse response = draftManager.updateDraft(draftUpdateDTO);
        Assert.assertTrue("更新草稿不应该失败", response.isSuccessful());
    }

    @Test
    public void test_3_getDraftList() {
        DraftListDTO draftListDTO = new DraftListDTO();
        draftListDTO.setOffset(0);
        draftListDTO.setCount(10);
        draftListDTO.setNoContent(0);

        DraftListResponse response = draftManager.getDraftList(draftListDTO);
        Assert.assertTrue("获取草稿列表不应该失败", response.isSuccessful());
        Assert.assertNotNull("获取的草稿列表不应该为空", response.getItem());
    }

    @Test
    public void test_4_getDraftCount() {
        DraftCountResponse response = draftManager.getDraftCount();
        Assert.assertTrue("获取草稿总数不应该失败", response.isSuccessful());
        Assert.assertNotNull("获取的草稿总数不应该为空", response.getTotalCount());
    }

    @Test
    public void test_5_deleteDraft() {
        WeChatResponse response = draftManager.deleteDraft(DRAFT_MEDIA_ID);
        Assert.assertTrue("删除草稿不应该失败", response.isSuccessful());
    }
}