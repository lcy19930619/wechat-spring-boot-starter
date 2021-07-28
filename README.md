
微信相关组件  
感谢 JetBrains 的对这个项目的认可，给了我一个全家桶激活码～～

## 为什么会有这个组件的出现？  
微信文档写的一些东西，感觉比较乱，而且很多东西需要找来找去，百度、csdn等等  
代码复用性也不高，所以写了这个组件，希望通用性好一些
## 使用方法  
### spring boot项目
方法一：通过mvn install: 将依赖包安装到本地仓库，而后pom.xml加入依赖坐标即可
方法二：通过私服，将本项目打包，部署扔到私服中，需要使用的项目部署maven坐标即可
### spring mvc
暂不支持
## 执行原理  
通过spring Boot的spi机制（即：扫描/resources/META-INF/spring.factories）文件注册spring bean 加载全部组件
然后通过EventBus进行相关处理  
一个message只能使用一个listener  
一个event只能使用一个listener  
如果内置的处理器不能满足业务需求，或者微信扩展了新的消息和事件，可以注册以下两个bean进行处理
UnKnowWeiXinEventListener
UnKnowWeiXinMessageListener
## 执行流程
### netty模式  
1、WeiXinCoreComponent 随着系统启动进行初始化，启动netty server  
2、微信请求到达时，WeiXinChannel 开始工作，并进行白名单认证  
3、请求转发到EventBus  
4、解析微信请求数据，根据event以及MsgType 查找注册到ioc容器中到监听器（即自己写的业务监听器）  
5、调用监听器的handler方法，执行相关业务  
6、向微信服务器返回数据  

### 传统模式
1、微信数据请求发送到WeiXinMessageController，并进行白名单认证。
2、请求转发到EventBus  
3、解析微信请求数据，根据event以及MsgType 查找注册到ioc容器中到监听器（即自己写的业务监听器）  
4、调用监听器的handler方法，执行相关业务  
5、向微信服务器返回数据

## 基本功能（还在不断完善中）
1、客服接口推送  
  ```
  使用 net.jlxxw.component.weixin.function.push.PushCustomer进行推送
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

2、微信模版推送
 ```
  使用 net.jlxxw.component.weixin.function.push.PushCustomer进行推送
  
    @Autowired
    private PushTemplate pushTemplate;
    
    String token = "";
    String openId = "xxxxx";
    String url = "xxxxxx";
    String templateId = "xxxxx";
    
    WxTemplate wxTemplate = new WxTemplate();
    wxTemplate
            .buildToUser(openId)
            .buildUrl(url)
            .buildTemplateCode(templateId)
            .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
            .buildKeyWord1Data("keyword1 DATA的具体值",null)
            .buildOtherData("abc","abc DATA的具体值",ColorEnums.ORANGE);
    pushTemplate.pushTemplate(wxTemplate,token);

  ```

3、自动管理token
 ```
    定时任务每两小时刷新一次token
    net.jlxxw.component.weixin.schedul.*
    执行逻辑：
    1、项目启动时，自动创建数据库token表 wei_xin_token
    2、定时任务执行时，获取数据库中最后一条存储的token的创建时间（使用行锁 for update）
       如果本地当前时间和数据时间误差小于三分钟，则不进行任何处理
       如果误差时间大于三分钟，则从微信服务器获取最新的token，并更新到数据库中
    
    在项目的任何位置获取token
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    
    String token = weiXinTokenManager.getTokenFromLocal();
    
 ```

4、微信回调接口:默认为netty接口，需要配置nginx反向代理，也提供/weixin/core 接口  
    nginx.conf
 ```
 
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
    
5、微信开发者服务器认证接口 /verifyToken

6、微信回调安全认证，非法Ip不予处理  
    通过配置文件进行开启,开启时，会在本地缓存微信服务器白名单ip列表，每次接收微信数据时  
 都会进行白名单检查，如果不在白名单中，返回403
```
weixin:
  # 是否启用回调接口安全检查，如果启用，每3小时更新一次微信回调接口ip白名单
  enable-wei-xin-call-back-server-security-check: true
```
    
7、简单的用户管理  
     net.jlxxw.component.weixin.function.user.UserManager
    
8、微信信息加密传输
     通过配置文件开启,开启后，会对信息进行加密和解密，需要在微信后台---基本配置---服务器配置  
     设置 消息加解密方式为：安全模式
```
weixin:
  # 是否启用微信加密传输
  enable-message-enc: true
  # 公众平台配置的消息加解密密钥，长度43位
  encoding-aes-key: xxxx
```
# yml配置文件如下
```
weixin:
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
  enable-wei-xin-call-back-server-security-check: true
  # 是否启用默认的token管理策略，如果使用自定义策略，需要实现 net.jlxxw.component.weixin.function.token.WeiXinTokenManager 接口
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

# 使用说明
微信回调事件均使用事件总线进行管理，参考代码net.jlxxw.component.weixin.component.EventBus  
在使用netty时，不推荐在业务逻辑中启用异步处理

## 消息处理器
### 注册普通消息处理器
#### 消息包括以下类型
文本信息 WeiXinMessageTypeEnum.TEXT  
图片信息 WeiXinMessageTypeEnum.IMAGE  
语音信息 WeiXinMessageTypeEnum.VOICE  
视频信息 WeiXinMessageTypeEnum.VIDEO  
小视频信息 WeiXinMessageTypeEnum.SHORT_VIDEO  
地理位置信息 WeiXinMessageTypeEnum.LOCATION  
链接信息 WeiXinMessageTypeEnum.LINK  
参考枚举：```net.jlxxw.component.weixin.enums.WeiXinMessageTypeEnum```

数据传输入参，即  
```net.jlxxw.component.weixin.component.listener.WeiXinMessageListener.handler```方法参数  
可以参考```net.jlxxw.component.weixin.dto.message.*Message``` 进行数据类型强制转换

返回信息类型可使用```net.jlxxw.component.weixin.response.WeiXinMessageResponse``` 进行创建
例如:
```
    // 返回图文信息，具体内容补充方法参数即可
    WeiXinMessageResponse.buildArticle();
    // 返回图片信息，具体内容补充方法参数即可
    WeiXinMessageResponse.buildImage();
    // 返回音乐信息，具体内容补充方法参数即可
    WeiXinMessageResponse.buildMusic();
    // 返回文本信息，具体内容补充方法参数即可
    WeiXinMessageResponse.buildText();
    // 返回视频信息，具体内容补充方法参数即可
    WeiXinMessageResponse.buildVideo();
    // 返回音频信息，具体内容补充方法参数即可
    WeiXinMessageResponse.buildVoice();
```
#### 使用方法
1、新建class

2、继承 net.jlxxw.component.weixin.component.listener.WeiXinMessageListener

3、添加 @Component注解  
例如，接收用户传输对文字内容
```
@Component
public class TextListener extends WeiXinMessageListener {

    /**
     * 支持的消息事件类型
     *
     * @return
     */
    @Override
    public WeiXinMessageTypeEnum supportMessageType() {
        return WeiXinMessageTypeEnum.TEXT;
    }
    
    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param weiXinMessage
     * @return
     */
    @Override
    public WeiXinMessageResponse handler(WeiXinMessage weiXinMessage) {
        TextMessage textMessage = (TextMessage)weiXinMessage;
        // 用户发送的内容
        String content = textMessage.getContent();
        // 用户的openId
        String fromUserName = textMessage.getFromUserName();
        saveToDataBase(fromUserName,content);
        String returnContent = "服务端已收到";
        WeiXinMessageResponse response = WeiXinMessageResponse.buildText(returnContent);
        return response;
    }
    
    public void saveToDataBase(String openId,String content){
    
    }
}
```



### 注册微信回调事件处理器（同消息处理器）
1、新建class

2、继承 net.jlxxw.component.weixin.component.listener.WeiXinEventListener

3、添加 @Component注解

