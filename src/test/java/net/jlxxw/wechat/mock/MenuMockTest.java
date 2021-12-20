package net.jlxxw.wechat.mock;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.base.MockBaseTest;
import net.jlxxw.wechat.function.menu.AsyncMenuManager;
import net.jlxxw.wechat.response.menu.MenuResponse;
import net.jlxxw.wechat.util.WebClientUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-20 11:24 上午
 */
public class MenuMockTest extends MockBaseTest {

    @InjectMocks
    private AsyncMenuManager asyncMenuManager;
    @Mock
    private WebClientUtils webClientUtils;

    @Test
    public void GetMenuInfoMockTest1() throws IOException {
        String mockData1 = getMockData1();
        MenuResponse menuResponse = JSON.parseObject(mockData1).toJavaObject(MenuResponse.class);
        Assert.assertNotNull("数据不应为空",menuResponse);
    }

    @Test
    public void GetMenuInfoMockTest2() throws IOException {
        String mockData1 = getMockData2();
        MenuResponse menuResponse = JSON.parseObject(mockData1).toJavaObject(MenuResponse.class);
        Assert.assertNotNull("数据不应为空",menuResponse);
    }

    private String getMockData1() throws IOException {
        return getJson("mock/data/json/GetMenuInfo-1.json");
    }
    private String getMockData2() throws IOException {
        return getJson("mock/data/json/GetMenuInfo-2.json");
    }



}
