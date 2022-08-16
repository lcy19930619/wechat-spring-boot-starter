package net.jlxxw.wechat.function.material;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import net.jlxxw.wechat.response.material.TempMaterialResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

/**
 * @author chunyang.leng
 * @date 2022-08-16 11:24 AM
 */
public class PermanentMaterialManagerTest extends BaseTest {
    @Autowired
    private PermanentMaterialManager permanentMaterialManager;

    @Test(expected = ParamCheckException.class)
    public void uploadExceptionTest() {
        permanentMaterialManager.upload(MaterialEnum.IMAGE, null,null,null);
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
            PermanentMaterialResponse response = permanentMaterialManager.upload(MaterialEnum.IMAGE, file,null,null);
            Assert.assertNotNull("应答不应该为空", response);
            Assert.assertTrue("文件id不应该为空",  StringUtils.isNotBlank(response.getMediaId()));
            Assert.assertTrue("文件url不应该为空", StringUtils.isNotBlank(response.getUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            file.delete();
        }
    }

    @Test
    public void uploadTest() {
        String mediaId = upload();
        Assert.assertTrue("上传素材mediaId不应该为null", StringUtils.isNotBlank(mediaId));
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
        Assert.assertNotNull("下载数据不应为空",download);
    }

    @Test
    public void deleteTest(){
        String mediaId = upload();
        WeChatResponse weChatResponse = permanentMaterialManager.deleteMaterial(mediaId);
        Assert.assertTrue("删除应该成功",weChatResponse.isSuccessful());
    }

}
