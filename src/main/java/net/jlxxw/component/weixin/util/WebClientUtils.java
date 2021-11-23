package net.jlxxw.component.weixin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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


    public  <T> Mono<T>  sendPostJSON(String url , String json, Class<T> result){
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
                .bodyToMono(result);

    }
}
