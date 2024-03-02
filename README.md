


微信相关组件，还在不断完善中
感谢 JetBrains 的对这个项目的认可  
项目地址：https://github.com/lcy19930619/wechat-spring-boot-starter  
wiki地址: https://github.com/lcy19930619/wechat-spring-boot-starter/wiki

# 为什么会有这个组件的出现？
微信文档写的一些东西，感觉比较乱，而且很多东西需要找来找去，百度、csdn等等  
代码复用性也不高，所以写了这个组件，希望通用性好一些
# 适用版本
- jdk 21
- spring boot 3.2.0+ 以上


# 基本功能
## 接收微信用户与公众号交互的信息
- 文本信息
- 图片信息
- 音频信息
- 视频信息
- 小视频信息
- 地理位置信息
- 链接信息

## 接收微信用户与公众号触发的事件信息
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

## 用户与微信公众号信息加密传输
通过配置文件开启,开启后，会对信息进行加密和解密，需要在微信后台---基本配置---服务器配置 ，设置 消息加解密方式为：安全模式



# 使用方法
## 添加 maven 版本管理依赖
```
 <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>net.jlxxw</groupId>
                <artifactId>wechat-spring-boot-starter</artifactId>
                 <version>${最新版本请查看仓库}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
## 选择添加需要的组件

### 微信事件、消息接入模块
#### 以 netty 方式，接入微信公众号
```xml
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-event-netty-spring-boot-starter</artifactId>
</dependency>
```
#### 以 http 方式，接入微信公众号
```xml
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-event-web-spring-boot-starter</artifactId>
</dependency>
```
##### 使用示例一：添加自定义消息处理器
步骤：
- 继承 AbstractWeChatMessageListener (抽象消息处理监听器)
- 实现对应方法
- 交给 Spring 管理 bean，添加 `@Component` 注解
 
示例代码如下:
```java
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.springframework.stereotype.Component;

/**
 * 用户发送消息监听器
 * @author chunyang.leng
 * @date 2021-12-18 6:19 下午
 */
@Component
public class TextMessageListener extends AbstractWeChatMessageListener {
    /**
     * 支持的消息类型
     *
     * @return
     */
    @Override
    public WeChatMessageTypeEnum supportMessageType() {

//      文本信息 WeChatMessageTypeEnum.TEXT
//      图片信息 WeChatMessageTypeEnum.IMAGE
//      语音信息 WeChatMessageTypeEnum.VOICE
//      视频信息 WeChatMessageTypeEnum.VIDEO
//      小视频信息 WeChatMessageTypeEnum.SHORT_VIDEO
//      地理位置信息 WeChatMessageTypeEnum.LOCATION
//      链接信息 WeChatMessageTypeEnum.LINK
//      更多类型，参考枚举 WeChatMessageTypeEnum
        return WeChatMessageTypeEnum.TEXT;
    }
    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        // 类型转化参考 supportMessageType() 方法中枚举定义
        TextMessage textMessage = (TextMessage)abstractWeChatMessage;
        // 用户发送的内容
        String content = textMessage.getContent();
        // 用户openId
        String fromUserName = textMessage.getFromUserName();

      // 返回图文信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildArticle();
      // 返回图片信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildImage();
      // 返回音乐信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildMusic();
      // 返回文本信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildText();
      // 返回视频信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildVideo();
      // 返回音频信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildVoice();
        return WeChatMessageResponse.buildText("回复用户一个文本信息");
    }
}

```
##### 使用示例二：添加自定义事件处理器
步骤：
- 继承 AbstractWeChatEventListener (抽象事件处理监听器)
- 实现对应方法
- 交给 Spring 管理 bean，添加 `@Component` 注解  
示例代码如下:
```java

import net.jlxxw.wechat.dto.message.event.SubscribeEventMessage;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * 用户关注事件监听器
 * @author chunyang.leng
 * @date 2021-12-19 7:08 下午
 */
@Component
public class SubscribeEventMessageListener extends AbstractWeChatEventListener {
    /**
     * 支持的事件类型
     */
    @Override
    public WeChatEventTypeEnum supportEventType() {

//      文本信息 WeChatMessageTypeEnum.TEXT
//      图片信息 WeChatMessageTypeEnum.IMAGE
//      语音信息 WeChatMessageTypeEnum.VOICE
//      视频信息 WeChatMessageTypeEnum.VIDEO
//      小视频信息 WeChatMessageTypeEnum.SHORT_VIDEO
//      地理位置信息 WeChatMessageTypeEnum.LOCATION
//      链接信息 WeChatMessageTypeEnum.LINK
//      更多类型，参考枚举 WeChatMessageTypeEnum

      return WeChatEventTypeEnum.SUBSCRIBE;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        Assert.assertNotNull("接收到的数据不应为空", abstractWeChatMessage);
        // 类型转化参考 supportMessageType() 方法中枚举定义
        SubscribeEventMessage subscribeEventMessage = (SubscribeEventMessage)abstractWeChatMessage;
        // 用户关注 event key
        String eventKey = subscribeEventMessage.getEventKey();
        // 用户openId
        String fromUserName = subscribeEventMessage.getFromUserName();


      // 返回图文信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildArticle();
      // 返回图片信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildImage();
      // 返回音乐信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildMusic();
      // 返回文本信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildText();
      // 返回视频信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildVideo();
      // 返回音频信息，具体内容补充方法参数即可
      // WeChatMessageResponse.buildVoice();
        return WeChatMessageResponse.buildText(supportEventType().getDescription() + " done");
    }
}
```


### 微信公众号各种函数入口（需要引入token管理实现）
适用场景:  
- 需要调用微信各种接口
- 微信文档看不懂

#### maven 坐标
```xml
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-function-spring-boot-starter</artifactId>
</dependency>
```
#### 示例: 获取用户基本信息
```java
import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.user.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class UserManagerTest  {

    @Autowired
    public UserManager userManager;

    public void getUserInfoTest(String openId) {
        
        SubscriptionResponse userInfo = userManager.getUserInfo(openId, LanguageEnum.ZH_CN);
        Assert.assertTrue("查询到的用户信息不应为空", Objects.nonNull(userInfo));
    }
}

```




### token 管理模块，默认 mysql 实现
#### 适用场景：
- 需要持久化token、js api ticket
- 需要调用微信接口
- 具有 mysql 环境
- 无自定义实现持久层
- 自动化管理token（不具备自动更新 token 能力）
#### maven 坐标
```xml
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-repository-mysql-spring-boot-starter</artifactId>
</dependency>
```
##### 扩展点
- [WeChatTokenRepository](wechat-repository%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Frepository%2Ftoken%2FWeChatTokenRepository.java)token 持久层入口
- [WeChatJsApiTicketRepository](wechat-repository%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Frepository%2Fjsapi%2FWeChatJsApiTicketRepository.java) ticket 持久层入口
- [IpSegmentRepository](wechat-repository%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Frepository%2Fip%2FIpSegmentRepository.java) 可信 ip 管理入口，用于判断请求数据是否来自微信
- [WeChatAiBotTokenRepository](wechat-repository%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Frepository%2Faibot%2FWeChatAiBotTokenRepository.java) 微信的开源ai机器人（为啥不使用gpt？你能保证请求在5s内完全返回么？公众号也不支持SSE协议）
###### 代码示例:自定义持久层 token 管理
步骤:
- 实现接口
- 交给 Spring 管理

```java
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2024-01-12 17:45
 */
@Component
public class MyTokenRepositoryImpl implements WeChatTokenRepository {

    /**
     * 保存token
     *
     * @param token
     */
    @Override
    public void save(String token) {
        
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String get() {
        return null;
    }
}

```


### 简单的安全防护模块
#### 适用场景
- 希望对接收的数据进行鉴权，该数据来自微信服务器
#### <font color='red'>特别注意</font>
- 如果没有引入定时器模块，则需要自行实现定时器，更新微信服务器ip地址信息
```xml
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-security-spring-boot-starter</artifactId>
</dependency>
```

### 简单的定时器模块
#### 适用场景
- <font color='red'>单体应用</font>
- 不想自己管理 token 和 ticket
```xml
<dependency>
    <groupId>net.jlxxw</groupId>
    <artifactId>wechat-simple-schedule-spring-boot-starter</artifactId>
</dependency>
```


### 如果仅需要部分底层模块，也可以按需引入
#### 微信接口出入参数对象集合
```xml
<dependency>
  <groupId>net.jlxxw</groupId>
  <artifactId>wechat-common</artifactId>
</dependency>
```

#### 微信事件回调处理
```xml
<dependency>
  <groupId>net.jlxxw</groupId>
  <artifactId>wechat-event</artifactId>
</dependency>
```

#### 微信接口方法模块
```xml
<dependency>
  <groupId>net.jlxxw</groupId>
  <artifactId>wechat-event</artifactId>
</dependency>
```






## 事件模式执行原理
- 通过spring Boot的spi机制（即：扫描/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports）文件注册spring bean 加载全部组件
- 接收微信事件回调，并通过EventBus进行相关处理  
### 注意事项
- 一个message只能使用一个listener  
- 一个event只能使用一个listener  
- 如果内置的处理器不能满足业务需求，或者微信扩展了新的消息和事件，可以注册以下两个bean进行处理
  - UnKnowWeChatEventListener
  - UnKnowWeChatMessageListener


## 事件执行流程
### netty模式
1、wechat-event-netty-spring-boot-starter 随着系统启动进行初始化，启动 [WeChatEventNettyServer.java](starter%2Fwechat-event-netty-spring-boot-starter%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Fevent%2Fnetty%2Fserver%2FWeChatEventNettyServer.java)netty server  
2、微信请求到达时，WeChatChannel[MessageHandler.java](starter%2Fwechat-event-netty-spring-boot-starter%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Fevent%2Fnetty%2Fhandler%2FMessageHandler.java) 开始工作
3、如果添加安全支持模块[wechat-security-spring-boot-starter](starter%2Fwechat-security-spring-boot-starter)，则通过[SecurityHandler.java](starter%2Fwechat-event-netty-spring-boot-starter%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Fevent%2Fnetty%2Fhandler%2FSecurityHandler.java)对进行请求ip检查
4、请求转发到 [EventBus.java](wechat-event%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Fevent%2Fcomponent%2FEventBus.java)
5、解析微信请求数据，根据 event 以及 MsgType 查找注册到ioc容器中到监听器（即自己写的业务监听器）  
6、调用监听器的 handler 方法，执行相关业务  
7、向微信服务器返回数据

### web模式
1、wechat-event-web-spring-boot-starter 随着系统启动进行初始化,注册 [WeChatMessageController](starter%2Fwechat-event-web-spring-boot-starter%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Fweb%2Fcontroller%2FWeChatMessageController.java) 作为程序入口
2、微信数据请求发送到 [WeChatMessageController](starter%2Fwechat-event-web-spring-boot-starter%2Fsrc%2Fmain%2Fjava%2Fnet%2Fjlxxw%2Fwechat%2Fweb%2Fcontroller%2FWeChatMessageController.java)，并进行白名单认证。
3、请求转发到 EventBus  
4、解析微信请求数据，根据 event 以及 MsgType 查找注册到ioc容器中到监听器（即自己写的业务监听器）  
5、调用监听器的 handler 方法，执行相关业务  
6、向微信服务器返回数据


# 完整配置文件
```yaml
wechat:
  # 需要替换为真实数据
  app-id: wx97562dcec7e78aca
  # 需要替换为真实数据
  secret: 995bc3e1c1e0d30884f3d2e1f033eced
  verify-token: mytoken123456
  # 需要替换为真实数据
  encoding-aes-key: 9hFP1tCoPIRB8rFk4ukaYn6bw1Gbxs53yIGVplNsxxx
  event:
    server:
      # 引入 event-netty 使用，与 event-web 互斥
      netty:
        codec: cipher_text
        port: 9999
        log:
          enable: true
          level: debug
        verify-token-url: /verify
        core-controller-url: /we
      # 引入 event-web 使用，与 event-netty 互斥
      web:
        core-controller-url: /we
        codec: cipher_text
  # 引入 wechat-repository-mysql-spring-boot-starter 使用
  repository:
    mysql:
      enable-auto-create-js-api-table: true
      enable-auto-create-token-table: true
```