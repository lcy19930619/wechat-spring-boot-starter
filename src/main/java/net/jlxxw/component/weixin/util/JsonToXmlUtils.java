package net.jlxxw.component.weixin.util;

import com.alibaba.fastjson.JSONObject;
import org.xml.sax.SAXException;

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
                stringBuffer.append("<").append(key).append(">").append(
                        jsonToDocument(json.getJSONObject(key),stringBuffer)
                ).append("</").append(key).append(">");
            }else{
                stringBuffer.append("<").append(key).append(">").append(v).append("</").append(key).append(">");
            }
        });
        return stringBuffer.toString();
    }
}
