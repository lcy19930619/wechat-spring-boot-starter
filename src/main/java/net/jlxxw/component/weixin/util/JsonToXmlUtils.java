package net.jlxxw.component.weixin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.xml.sax.SAXException;

import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021/1/20 1:18 下午
 */
public class JsonToXmlUtils {

    /**
     * JSON对象转xml字符串
     *
     * @param json JSON对象
     * @return xml字符串
     * @throws SAXException
     */
    public static String jsonToXml(JSONObject json)  {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<xml>");
         String str = jsonToDocument(json, buffer);
        return str +"</xml>";
    }

    /**
     * JSON对象转Document对象
     *
     * @param json JSON对象
     * @return Document对象
     * @throws SAXException
     */
    private static String jsonToDocument(JSONObject json,StringBuffer stringBuffer){

        json.forEach((key,v)->{
            Object child = json.get(key);
            if (child instanceof JSONObject) {
                stringBuffer
                        .append("<").append(key).append(">")
                        .append(jsonToDocument(json.getJSONObject(key),stringBuffer))
                        .append("</").append(key).append(">");
            }else{
                if(Objects.nonNull(v)){
                    stringBuffer
                            .append("<").append(firstCharToUpCase(key)).append(">")
                            .append("<![CDATA[").append(v).append("]]>")
                            .append("</").append(firstCharToUpCase(key)).append(">");
                }
            }
        });
        return stringBuffer.toString();
    }

    /**
     * 字符串首字母转大写
     * @param str
     * @return
     */
    private static String firstCharToUpCase(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        TextMessageResponse response = new TextMessageResponse();
        ReflectUtils.setDefaultValue(response);

        final String json = JSON.toJSONString(response);
        JSONObject jsonObject = JSON.parseObject(json);

        System.out.println(jsonToXml(jsonObject));
    }
}
