
微信相关组件，还在不断完善中
感谢 JetBrains 的对这个项目的认可
项目地址：https://github.com/lcy19930619/weixin-spring-boot-starter
wiki地址: https://github.com/lcy19930619/weixin-spring-boot-starter/wiki

# 为什么会有这个组件的出现？
微信文档写的一些东西，感觉比较乱，而且很多东西需要找来找去，百度、csdn等等  
代码复用性也不高，所以写了这个组件，希望通用性好一些
# 基础讲解
### spring boot项目
方法一：通过mvn install: 将依赖包安装到本地仓库，而后pom.xml加入依赖坐标即可  
方法二：通过私服，将本项目打包，部署扔到私服中，需要使用的项目部署maven坐标即可  
方法三：引入maven中央仓库坐标
```
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-spring-boot-starter</artifactId>
    <version>1.0.2.20220725</version>
</dependency>
```

### spring mvc
暂不支持
## 执行原理
通过spring Boot的spi机制（即：扫描/resources/META-INF/spring.factories）文件注册spring bean 加载全部组件
然后通过EventBus进行相关处理  
一个message只能使用一个listener  
一个event只能使用一个listener  
如果内置的处理器不能满足业务需求，或者微信扩展了新的消息和事件，可以注册以下两个bean进行处理
UnKnowWeChatEventListener
UnKnowWeChatMessageListener
## 执行流程
### netty模式
1、WeChatCoreComponent 随着系统启动进行初始化，启动netty server  
2、微信请求到达时，WeChatChannel 开始工作，并进行白名单认证  
3、请求转发到EventBus  
4、解析微信请求数据，根据event以及MsgType 查找注册到ioc容器中到监听器（即自己写的业务监听器）  
5、调用监听器的handler方法，执行相关业务  
6、向微信服务器返回数据

### 传统模式
1、微信数据请求发送到WeChatMessageController，并进行白名单认证。
2、请求转发到EventBus  
3、解析微信请求数据，根据event以及MsgType 查找注册到ioc容器中到监听器（即自己写的业务监听器）  
4、调用监听器的handler方法，执行相关业务  
5、向微信服务器返回数据

## 基本功能
### 微信开发者服务器认证接口
/verifyToken
可以参考`net.jlxxw.wechat.controller.WeChatDevelopmentCertification#verifyToken`
### 接收微信用户与公众号交互的信息

- 文本信息
- 图片信息
- 音频信息
- 视频信息
- 小视频信息
- 地理位置信息
- 链接信息

### 接收微信用户与公众号触发的事件信息
- 用户关注
- 用户取消关注
- 用户未关注时
- 用户已关注时的扫描带参数二维码
- 上报地理位置
- 模板送达结果
- 点击菜单拉取消息时的事件推送
- 点击菜单跳转链接时的事件推送
- 菜单扫码推事件的事件推送
- 扫码推事件且弹出“消息接收中”提示框的事件推送
- 弹出系统拍照发图的事件推送
- 弹出拍照或者相册发图的事件推送
- 弹出微信相册发图器的事件推送
- 弹出地理位置选择器的事件推送
- 点击菜单跳转小程序的事件推送

### 用户与微信公众号信息加密传输

通过配置文件开启,开启后，会对信息进行加密和解密，需要在微信后台---基本配置---服务器配置 ，设置 消息加解密方式为：安全模式
```yml
we-chat:
  # 是否启用微信加密传输
  enable-message-enc: true
  # 公众平台配置的消息加解密密钥，长度43位
  encoding-aes-key: xxxx
```
yml配置文件如下
```yml
we-chat:
  app-id: xxx
  secret: xxxx
  grant-type: client_credential
  # 是否启用微信加密传输
  enable-message-enc: true
  # 公众平台配置的消息加解密密钥，长度43位
  encoding-aes-key: xxxx
  # 微信服务器认证时配置的token,https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
  verify-token: xxxxxxxxxxx
 
  # 是否启用回调接口安全检查，如果启用，每3小时更新一次微信回调接口ip白名单
  enable-we-chat-call-back-server-security-check: true
  # 是否启用默认的token管理策略，如果使用自定义策略，需要实现 WeChatTokenManager 接口
  enable-default-token-manager: true
  netty:
    server:
      # 是否启用netty作为微信回调接口处理器，如果启用，可在nginx中配置相关转发策略
      enable-netty: true
      # 最大线程数
      max-thread-size: 200
      # netty监听端口
      netty-port: 19191
      # netty 队列大小
      queue-size: 200
```

### 微信回调安全认证
微信回调安全认证，非法Ip不予处理  
通过配置文件进行开启，开启时，会在本地缓存微信服务器白名单ip列表，每次接收微信数据时  
都会进行白名单检查，如果不在白名单中，返回403
```yml
we-chat:
  # 是否启用回调接口安全检查，如果启用，每3小时更新一次微信回调接口ip白名单
  enable-we-chat-call-back-server-security-check: true
```
#### 自定义白名单存储器
```java

import net.jlxxw.wechat.security.WeChatSecurityIpStore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;


/**
 * @author chunyang.leng
 * @date 2021-12-21 2:32 下午
 */
@Component
public class MyStore implements WeChatSecurityIpStore {
        
    // todo 可以更换为redis / MySQL等非内存数据库，用于解决集群共享问题
    private Set<String> ipStore = new HashSet<>();

    /**
     * 新增一个微信的服务器ip
     *
     * @param ip 微信服务器ip
     */
    @Override
    public void addSecurityIp(String ip) {
        ipStore.add(ip);
    }

    /**
     * 判断当前访问ip是否是安全的
     *
     * @param ip
     * @return
     */
    @Override
    public boolean isSecurityIp(String ip) {
        return ipStore.contains(ip);
    }

    /**
     * 新增一组微信的服务器ip
     *
     * @param ipList 微信服务器ip
     */
    @Override
    public void addSecurityIp(List<String> ipList) {
        ipStore.addAll(ipList);
    }
}
```

### 推送业务
#### 客服接口推送
  ```java 
  // 使用 net.jlxxw.component.function.push.PushCustomer进行推送
    @Autowired
    public PushCustomer pushCustomer;
    
    String token = "xxxxxxx";
    String openId = "xxx";
    ArticlesDTO articlesDTO = new ArticlesDTO();
    articlesDTO.setPicurl("测试图片url");
    articlesDTO.setTitle("这里一个测试的标题");
    articlesDTO.setUrl("测试跳转链接");
    articlesDTO.setDescription("测试描述信息");
    CustomerMessageDTO dto = CustomerMessageDTO.buildNews(openId,articlesDTO);
    pushCustomer.pushCustomer(dto,token);
  
  ```

#### 微信模版推送
 ```java
  // 使用 net.jlxxw.component.function.push.PushCustomer进行推送
  
    @Autowired
    private PushTemplate pushTemplate;
    
 
    String openId = "xxxxx";
    String url = "xxxxxx";
    String templateId = "xxxxx";
    
    WxTemplate weChatTemplateDTO = new WxTemplate();
    weChatTemplateDTO
            .buildToUser(openId)
            .buildUrl(url)
            .buildTemplateCode(templateId)
            .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
            .buildKeyWord1Data("keyword1 DATA的具体值",null)
            .buildOtherData("abc","abc DATA的具体值",ColorEnums.ORANGE);
    pushTemplate.pushTemplate(weChatTemplateDTO);

  ```

### 自动管理token和JsApiTicket
 ```
    定时任务每两小时刷新一次token
    net.jlxxw.component.schedul.*
    执行逻辑：
    1、项目启动时，自动创建数据库token表 wei_xin_token
    2、定时任务执行时，获取数据库中最后一条存储的token的创建时间（使用行锁 for update）
       如果本地当前时间和数据时间误差小于三分钟，则不进行任何处理
       如果误差时间大于三分钟，则从微信服务器获取最新的token，并更新到数据库中
    
    在项目的任何位置获取token
    @Autowired
    private WeChatTokenManager weChatTokenManager;
    
    String token = weChatTokenManager.getTokenFromLocal();
    
 ```
#### 不使用默认管理器，自定义token管理器
**<font color="red">注意事项
实现自定义token管理器，则不会启动定时刷新token任务，需要自己去实现刷新token job**</font>
<font color="red">**获取token次数有限，请妥善保存token**</font>
集群部署 可以使用elastic-job 或这 xxl-job

以下例子基于 elastic-job 3.0.0-RC1版本

```java
package net.jlxxw.wechat.job;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.mapper.TokenMapper;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Objects;

import static net.jlxxw.wechat.constant.UrlConstant.GET_JS_API_TICKET_URL;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author chunyang.leng
 * @date 2021-12-21 2:32 下午
 */
@Component
public class TokenJob implements WeChatTokenManager, SimpleJob {

    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private RestTemplate restTemplate;


    /**
     * elastic job 任务实现
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        String jsApiTicketFromWeiXin = getJsApiTicketFromWeiXin();
        String tokenFromWeiXin = getTokenFromWeiXin();
        saveToken(tokenFromWeiXin);
        saveJsApiTicket(jsApiTicketFromWeiXin);
    }

    /**
     * 保存token
     *
     * @param token
     */
    @Override
    public void saveToken(String token) {
        tokenMapper.insertToken(token);
    }

    /**
     * 定时从微信获取token
     *
     * @return token
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">文档地址</a>
     */
    @Override
    public String getTokenFromWeiXin() throws WeChatException {
        String url = MessageFormat.format(UrlConstant.TOKEN_URL, weChatProperties.getAppId(), weChatProperties.getSecret());
        WeChatTokenResponse response = restTemplate.getForObject(url, WeChatTokenResponse.class);
        if (Objects.nonNull(response.getErrcode()) && 0 != response.getErrcode()) {
            WeChatException weChatException = new WeChatException(JSON.toJSONString(response));
            weChatException.setErrorCode(response.getErrcode());
            throw weChatException;
        }
        return response.getAccessToken();
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getTokenFromLocal() {
        return tokenMapper.getToken();
    }


    /**
     * 保存JsApiTicket
     *
     * @param jsApiTicket
     */
    @Override
    public void saveJsApiTicket(String jsApiTicket) {
        tokenMapper.insertJsApiTicket(jsApiTicket);
    }

    /**
     * 定时从微信获取JsApiTicket
     *
     * @return JsApiTicket
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html">文档地址</a>
     */
    @Override
    public String getJsApiTicketFromWeiXin() throws WeChatException {
        String url = MessageFormat.format(GET_JS_API_TICKET_URL, getTokenFromLocal());
        ResponseEntity<WeChatTokenResponse> response = restTemplate.getForEntity(url, WeChatTokenResponse.class);
        WeChatTokenResponse body = response.getBody();
        if (Objects.nonNull(body.getErrcode()) && 0 != body.getErrcode()) {
            WeChatException weChatException = new WeChatException(JSON.toJSONString(response));
            weChatException.setErrorCode(body.getErrcode());
            throw weChatException;
        }
        return body.getTicket();
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getJsApiTicketFromLocal() {
        return tokenMapper.getJsApiTicket();
    }
}
```

```yaml
elasticjob:
  reg-center:
    server-lists: 127.0.0.1:12181
    namespace: elasticjob-spring-cloud-wechat
  jobs:
    tokenJob:
      elasticJobClass: net.jlxxw.wechat.job.TokenJob
      cron: 0 0/30 * * * ?
      shardingTotalCount: 1
      description: 获取token
      overwrite: false
```

```xml
            <dependency>
                <groupId>org.apache.shardingsphere.elasticjob</groupId>
                <artifactId>elasticjob-lite-core</artifactId>
                <version>${elastic-job.version}</version>
            </dependency>
            <dependency>
                <groupId>org.apache.shardingsphere.elasticjob</groupId>
                <artifactId>elasticjob-lite-spring-boot-starter</artifactId>
                <version>${elastic-job.version}</version>
            </dependency>
            <dependency>
                <groupId>org.apache.curator</groupId>
                <artifactId>curator-recipes</artifactId>
                <version>5.1.0</version>
            </dependency>
            <dependency>
                <groupId>org.apache.curator</groupId>
                <artifactId>curator-framework</artifactId>
                <version>5.1.0</version>
            </dependency>

```

# 集群配置

## 使用nginx做负载均衡
微信回调接口:默认为netty接口，需要配置nginx反向代理，也提供/weChat 接口  
nginx.conf 配置如下
 ```sh
 
worker_processes  1;

events {
    use epoll;
    worker_connections  1024;
}

http {
    client_body_buffer_size 32k;
    client_header_buffer_size 2k;
    client_max_body_size 100m;
    default_type application/octet-stream;
    log_not_found off;
    server_tokens off;
    include mime.types;
    gzip on;
    gzip_min_length  1k;
    gzip_buffers  4 16k;
    gzip_http_version 1.0;
    gzip_comp_level 2;
    gzip_types  text/plain text/css text/xml text/javascript application/x-javascript application/xml application/rss+xml application/xhtml+xml application/atom_xml;
    gzip_vary on;
	
	log_format access_json '{"@timestamp":"$time_iso8601",'
                           '"host":"$server_addr",'
                           '"clientip":"$remote_addr",'
                           '"size":$body_bytes_sent,'
                           '"responsetime":$request_time,'
                           '"upstreamtime":"$upstream_response_time",'
                           '"upstreamhost":"$upstream_addr",'
                           '"http_host":"$host",'
                           '"url":"$uri",'
                           '"domain":"$host",'
                           '"xff":"$http_x_forwarded_for",'
                           '"referer":"$http_referer",'
                           '"status":"$status",'
                           '"req_body":"$request_body",'
                           '"resp_body":"resp_body"'
                           '}';
    server {
        listen 443 ssl;
        #配置HTTPS的默认访问端口为443。
        #如果未在此处配置HTTPS的默认访问端口，可能会造成Nginx无法启动。
        #如果您使用Nginx 1.15.0及以上版本，请使用listen 443 ssl代替listen 443和ssl on。
        server_name yourdomain.com; #需要将yourdomain.com替换成证书绑定的域名。
        root html;
        index index.html index.htm;
        ssl_certificate /cert/xxx.pem;  #需要将cert-file-name.pem替换成已上传的证书文件的名称。
        ssl_certificate_key /cert/xxxx.key;#需要将cert-file-name.key替换成已上传的证书密钥文件的名称。
        ssl_session_timeout 5m;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
        #表示使用的加密套件的类型。
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2; #表示使用的TLS协议的类型。
        ssl_prefer_server_ciphers on;
        location / {
            root html;  #站点目录。
            index index.html index.htm;
        }
         # 启用nginx 代理，指向netty端口，
         # 注意，校验token时，请指向服务器的verifyToken接口
         location /weixin {
            # proxy_pass   http://127.0.0.1:8080/verifyToken;
            proxy_pass   http://127.0.0.1:19191;
            proxy_read_timeout              5000;
            proxy_set_header                Host    $http_host;
            proxy_set_header                X-Real-IP          $remote_addr;
            proxy_set_header                X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Real-IP $remote_addr;
		  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        }
       error_log  /var/log/nginx/error.log  error;
	   access_log  /var/log/nginx/access.log  access_json;
    }
}

  ```
## spring cloud + spring boot admin
以nacos + spring cloud alibaba 为例子
```yaml
spring:
  cloud:
    nacos:
      # 注册中心链接地址
      discovery:
        server-addr: nacos-service.default.svc.cluster.local:8848
        group: wechat-group
        # nacos 心跳间隔。时间单位:毫秒
        heart-beat-interval: 1000
        # nacos 心跳暂停。时间单位:毫秒。 即服务端3秒收不到客户端心跳，会将该客户端注册的实例设为不健康
        heart-beat-timeout: 3000
        # nacos Ip删除超时。时间单位:秒。即服务端3秒收不到客户端心跳，会将该客户端注册的实例删除
        ip-delete-timeout: 3000
        # 元数据配置
        metadata:
          # spring boot admin 安全防护用户名
          user.name: ${spring.security.user.name}
          # spring boot admin 安全防护密码
          user.password: ${spring.security.user.password}
          management:
            # 心跳相关兼容处理
            context-path: ${server.servlet.context-path}/actuator
            port: ${server.port}
        # 自定义ip地址
        # ip: 127.0.0.1
        port: ${we-chat.netty.server.netty-port}
      # 配置中心地址
      config:
        server-addr: nacos-service.default.svc.cluster.local:8848
        file-extension: yml
        group: wechat-group
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
server:
  servlet:
    context-path: /
  port: 8080
```




# 使用说明
微信回调事件均使用事件总线进行管理，参考代码net.jlxxw.component.component.EventBus  
在使用netty时，不推荐在业务逻辑中启用异步处理

## 消息处理器
`消息处理器用于处理微信用户触发的各种事件，以及用户发送的各种信息`
### 注册普通消息处理器
#### 消息包括以下类型
文本信息 WeChatMessageTypeEnum.TEXT  
图片信息 WeChatMessageTypeEnum.IMAGE  
语音信息 WeChatMessageTypeEnum.VOICE  
视频信息 WeChatMessageTypeEnum.VIDEO  
小视频信息 WeChatMessageTypeEnum.SHORT_VIDEO  
地理位置信息 WeChatMessageTypeEnum.LOCATION  
链接信息 WeChatMessageTypeEnum.LINK  
更多类型，参考枚举：```WeChatMessageTypeEnum```

数据传输入参，即  
```AbstractWeChatMessageListener.handler```方法参数  
可以参考```net.jlxxw.component.dto.message.*Message```进行数据类型强制转换

返回信息类型可使用```WeChatMessageResponse```进行创建
例如:
```
    // 返回图文信息，具体内容补充方法参数即可
    WeChatMessageResponse.buildArticle();
    // 返回图片信息，具体内容补充方法参数即可
    WeChatMessageResponse.buildImage();
    // 返回音乐信息，具体内容补充方法参数即可
    WeChatMessageResponse.buildMusic();
    // 返回文本信息，具体内容补充方法参数即可
    WeChatMessageResponse.buildText();
    // 返回视频信息，具体内容补充方法参数即可
    WeChatMessageResponse.buildVideo();
    // 返回音频信息，具体内容补充方法参数即可
    WeChatMessageResponse.buildVoice();
    
```
#### 使用方法
1、新建class
2、继承 AbstractWeChatMessageListener
3、添加 @Component注解  
例如，接收用户传输的文字内容
```java

import net.jlxxw.wechat.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:19 下午
 */
@Component
public class TextMessageListener extends AbstractWeChatMessageListener {
    /**
     * 支持的消息事件类型
     *
     * @return
     */
    @Override
    public WeChatMessageTypeEnum supportMessageType() {
        return WeChatMessageTypeEnum.TEXT;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        Assert.assertNotNull("接收到的信息不应为空", abstractWeChatMessage);
        return WeChatMessageResponse.buildText("content");
    }
}
```



### 注册微信回调事件处理器（同消息处理器）
1、新建class
2、继承 AbstractWeChatEventListener
3、添加 @Component注解
例如：用户关注事件
```java
import net.jlxxw.wechat.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 7:08 下午
 */
@Component
public class SubscribeEventMessageListener extends AbstractWeChatEventListener {
    /**
     * 支持的事件类型
     *
     * @return
     */
    @Override
    public WeChatEventTypeEnum supportEventType() {
        return WeChatEventTypeEnum.SUBSCRIBE;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        Assert.assertNotNull("接收到的数据不应为空", abstractWeChatMessage);
        return WeChatMessageResponse.buildText(supportEventType().getDescription() + " done");
    }
}
```
