package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.io.File;

/**
 * @author chunyang.leng
 * @date 2022-06-18 9:45 AM
 */

public class AsyncPermanentMaterialManagerTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncPermanentMaterialManagerTest.class);
    @Autowired
    private AsyncPermanentMaterialManager asyncPermanentMaterialManager;

    @Test
    public void uploadTest(){
        File file = new File("./mock/data/png/img.png");
        Mono<PermanentMaterialResponse> upload = asyncPermanentMaterialManager.upload(MaterialEnum.IMAGE, file);
        try {
            Thread.sleep(6 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        upload.subscribe(o->{
            logger.info("永久素材上传结果:{}" ,JSON.toJSONString(o));
            Assert.assertNotNull("mediaId 不应为空",o.getMediaId());
        });

    }

}
