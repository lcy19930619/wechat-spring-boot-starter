package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
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
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2022-08-12 3:55 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class TempMaterialManagerTest  {

    @Autowired
    private TempMaterialManager tempMaterialManager;

    @Test
    public void uploadTest() {
        String mediaId = upload();
        Assertions.assertTrue(StringUtils.isNotBlank(mediaId), "上传临时素材mediaId不应该为null");
    }

    @Test
    public void uploadExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            tempMaterialManager.upload(MaterialEnum.IMAGE, null);
        });
    }

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
            TempMaterialResponse response = tempMaterialManager.upload(MaterialEnum.IMAGE, file);
            Assertions.assertTrue(Objects.nonNull(response) && StringUtils.isNotBlank(response.getMediaId()), "文件id不应该为空");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            file.delete();
        }
    }

    @Test
    public void downloadTest(){
        String mediaId = upload();
        byte[] bytes = tempMaterialManager.downloadMaterial(mediaId);
        Assertions.assertTrue(Objects.nonNull(bytes), "文件不应该为空");
    }



    private String upload() {
        ClassPathResource classPathResource = new ClassPathResource("./mock/data/png/img.png");
        TempMaterialResponse upload = null;
        try (InputStream inputStream = classPathResource.getInputStream()) {
            byte[] bytes = IOUtils.readFully(inputStream, inputStream.available());
            upload = tempMaterialManager.upload(MaterialEnum.IMAGE, bytes, "img.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return upload.getMediaId();
    }


}
