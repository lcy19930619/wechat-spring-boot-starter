package net.jlxxw.wechat.function.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.menu.MenuDTO;
import net.jlxxw.wechat.response.WeiXinResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-12-13 3:15 下午
 */
public class AsyncMenuManagerTest extends BaseTest {
    @Autowired
    private AsyncMenuManager asyncMenuManager;

    public void createMenuTest(){
        JSONObject jsonObject = JSON.parseObject(getMockMenuData());
        JSONArray jsonArray = jsonObject.getJSONArray("button");
        List<MenuDTO> menuList = jsonArray.toJavaList(MenuDTO.class);
        Mono<WeiXinResponse> menu = asyncMenuManager.createMenu(menuList);
        menu.subscribe((response) -> {
            Integer errcode = response.getErrcode();
            Assert.assertEquals(0, (int) errcode);
        });
    }


    private static String getMockMenuData(){
        return "{\n" +
                "     \"button\":[\n" +
                "     {\t\n" +
                "          \"type\":\"click\",\n" +
                "          \"name\":\"今日歌曲\",\n" +
                "          \"key\":\"V1001_TODAY_MUSIC\"\n" +
                "      },\n" +
                "      {\n" +
                "           \"name\":\"菜单\",\n" +
                "           \"sub_button\":[\n" +
                "           {\t\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"搜索\",\n" +
                "               \"url\":\"http://www.soso.com/\"\n" +
                "            },\n" +
                "            {\n" +
                "                 \"type\":\"miniprogram\",\n" +
                "                 \"name\":\"wxa\",\n" +
                "                 \"url\":\"http://mp.weixin.qq.com\",\n" +
                "                 \"appid\":\"wx286b93c14bbf93aa\",\n" +
                "                 \"pagepath\":\"pages/lunar/index\"\n" +
                "             },\n" +
                "            {\n" +
                "               \"type\":\"click\",\n" +
                "               \"name\":\"赞一下我们\",\n" +
                "               \"key\":\"V1001_GOOD\"\n" +
                "            }]\n" +
                "       }]\n" +
                " }";
    }
}
