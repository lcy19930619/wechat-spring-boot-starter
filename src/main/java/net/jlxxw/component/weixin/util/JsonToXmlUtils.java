package net.jlxxw.component.weixin.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
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
            }else if(child instanceof JSONArray) {
                final JSONArray jsonArray = json.getJSONArray(key);
                if(!CollectionUtils.isEmpty(jsonArray)){
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Object obj = jsonArray.get(i);
                        if(obj instanceof JSONObject){
                            stringBuffer
                                    .append("<").append(key).append(">")
                                    .append(jsonToDocument(json.getJSONObject(key),stringBuffer))
                                    .append("</").append(key).append(">");
                        }else{
                            // todo jsonArray and other
                        }
                    }
                }
            }else {
                if(Objects.nonNull(v)){
                    stringBuffer
                            .append("<").append(firstCharToUpCase(key)).append(">")
                            .append(appendStr(v))
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


    private static StringBuffer appendStr(Object obj){
        StringBuffer stringBuffer  = new StringBuffer();
        if(Objects.nonNull(obj)){
            if(obj instanceof String){
                stringBuffer
                        .append("<![CDATA[").append(obj).append("]]>");
            }else{
                stringBuffer
                        .append("<").append(obj).append(">");
            }
        }
        return stringBuffer;
    }
}
