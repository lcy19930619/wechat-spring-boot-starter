package net.jlxxw.component.weixin.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author chunyang.leng
 * @date 2021-11-23 11:52 上午
 */
@Component
public class WebClientUtils {
    @Autowired
    private WebClient webClient;

    /**
     * 发送http post json 请求
     *
     * @param url    请求url
     * @param json   json数据
     * @param result 返回值结果类型
     * @param <T>    返回值类型
     * @return 返回订阅对象
     */
    public <T> Mono<T> sendPostJSON(String url, String json, Class<T> result) {
        // 发送请求
        return webClient
                // POST 请求
                .post()
                // 请求路径
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(String.class)
                .map(jsonString -> JSON.toJavaObject(JSON.parseObject(jsonString), result));
    }

    /**
     * 发送一个get请求
     * @param url 请求地址
     * @param result 应答数据类型
     * @param <T> 转换应答类型
     * @return 异步包装对象
     */
    public <T> Mono<T> sendGet(String url, Class<T> result) {
        // 发送请求
        return webClient
                // POST 请求
                .get()
                // 请求路径
                .uri(url)
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(String.class)
                .map(jsonString -> JSON.toJavaObject(JSON.parseObject(jsonString), result));

    }

    /**
     * 发送http post json 请求
     *
     * @param url    请求url
     * @param param   请求参数
     * @param result 返回值结果类型
     * @param <T>    返回值类型
     * @return 返回订阅对象
     */
    public <T> Mono<T> sendPostFormUrlEncoded(String url, MultiValueMap<String, Object> param, Class<T> result) {

        // 发送请求
        return webClient
                // POST 请求
                .post()
                // 请求路径
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(param))
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(result);
    }



    /**
     * 发送一个get请求
     * @param url 请求地址
     * @param result 应答数据类型
     * @param <T> 转换应答类型
     * @return 异步包装对象
     */
    public <T> Mono<T> sendGet(String url, Class<T> result,MediaType... mediaTypes) {
        // 发送请求
        return webClient
                // POST 请求
                .get()
                // 请求路径
                .uri(url)
                // 允许接收的参数类型
                .accept(mediaTypes)
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(String.class)
                .map(jsonString -> JSON.toJavaObject(JSON.parseObject(jsonString), result));

    }



}
