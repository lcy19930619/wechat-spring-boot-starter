package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.response.material.TempMaterialResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2022-08-12 3:55 PM
 */

public class TempMaterialManagerTest extends BaseTest {

    @Autowired
    private TempMaterialManager tempMaterialManager;

    @Test
    public void uploadTest() {
        String mediaId = upload();
        Assert.assertTrue("上传临时素材mediaId不应该为null", StringUtils.isNotBlank(mediaId));

    }

    @Test
    public void downloadTest(){
        String mediaId = upload();
        byte[] bytes = tempMaterialManager.downloadMaterial(mediaId);
        Assert.assertTrue("文件不应该为空", Objects.nonNull(bytes));
    }



    private String upload() {
        ClassPathResource classPathResource = new ClassPathResource("./mock/data/png/img.png");
        TempMaterialResponse upload = null;
        try (InputStream inputStream = classPathResource.getInputStream()) {
            byte[] bytes = IOUtils.readFully(inputStream, inputStream.available());
            upload = tempMaterialManager.upload(MaterialEnum.IMAGE, bytes, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return upload.getMediaId();
    }


}
