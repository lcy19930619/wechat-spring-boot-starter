package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import net.jlxxw.wechat.response.material.TempMaterialResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chunyang.leng
 * @date 2022-08-16 11:24 AM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class PermanentMaterialManagerTest  {
    @Autowired
    private PermanentMaterialManager permanentMaterialManager;


    @Test
    public void uploadFileTest() {
        ClassPathResource classPathResource = new ClassPathResource("./mock/data/png/img.png");
        TempMaterialResponse upload = null;
        String currentDir = System.getProperty("user.dir");
        String filePath = currentDir + "/temp/image.png";
        File file = new File(filePath);
        try (InputStream inputStream = classPathResource.getInputStream()
        ) {
            FileUtils.copyToFile(inputStream, file);
            PermanentMaterialResponse response = permanentMaterialManager.upload(MaterialEnum.IMAGE, file,null,null);
            Assertions.assertNotNull(response, "应答不应该为空");
            Assertions.assertTrue(StringUtils.isNotBlank(response.getMediaId()), "文件id不应该为空");
            Assertions.assertTrue(StringUtils.isNotBlank(response.getUrl()), "文件url不应该为空");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            file.delete();
        }
    }

    @Test
    public void uploadTest() {
        String mediaId = upload();
        Assertions.assertTrue(StringUtils.isNotBlank(mediaId), "上传素材mediaId不应该为null");
    }


    @Test
    public void uploadExceptionTest1() {


        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            permanentMaterialManager.upload(MaterialEnum.IMAGE, null,null,null);
        });
    }

    @Test
    public void uploadExceptionTest2() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            permanentMaterialManager.upload(null, null, null, null,null);
        });
    }


    private String upload() {
        ClassPathResource classPathResource = new ClassPathResource("./mock/data/png/img.png");
        PermanentMaterialResponse upload = null;
        try (InputStream inputStream = classPathResource.getInputStream()) {
            byte[] bytes = IOUtils.readFully(inputStream, inputStream.available());
            upload = permanentMaterialManager.upload(MaterialEnum.IMAGE, bytes, "img.png",null,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return upload.getMediaId();
    }


    @Test
    public void downloadTest(){
        String mediaId = upload();
        byte[] download = permanentMaterialManager.download(mediaId);
        Assertions.assertNotNull(download, "下载数据不应为空");
    }


    @Test
    public void downloadExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            permanentMaterialManager.download(null);
        });
    }

    @Test
    public void deleteTest(){
        String mediaId = upload();
        WeChatResponse weChatResponse = permanentMaterialManager.deleteMaterial(mediaId);
        Assertions.assertTrue(weChatResponse.isSuccessful(), "删除应该成功");
    }

    @Test
    public void deleteMaterialExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            permanentMaterialManager.deleteMaterial(null);
        });
    }

}
