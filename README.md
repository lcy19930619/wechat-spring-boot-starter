# WeiXinComponent
微信相关组件

1、客服接口推送

2、微信模版推送

3、自动管理token

4、微信回调接口:默认为netty接口，需要配置nginx反向代理，也提供/weixin/core 接口

5、微信开发者服务器认证接口 /verifyToken

6、微信回调安全认证，非法Ip不予处理
# yml配置文件如下
```
weixin:
  app-id: xxx
  secret: xxxx
  grant-type: xxx
  # 是否启用回调接口安全检查，如果启用，每3小时更新一次微信回调接口ip白名单
  enable-wei-xin-call-back-server-security-check: true
  # 是否启用默认的token管理策略，如果使用自定义策略，需要实现 net.jlxxw.component.weixin.function.token.WeiXinTokenManager 接口
  enable-default-token-manager: true
  # 微信服务器认证时配置的token,https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
  verify-token: xxx
  netty:
    server:
      # 是否启用netty作为微信回调接口处理器，如果启用，可移栽nginx中配置相关转发策略
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

## 注册事件处理器
### 注册普通信息事件处理器

1、新建class

2、继承 net.jlxxw.component.weixin.component.listener.WeiXinMessageListener

3、添加 @Component注解

### 注册微信回调事件处理器
1、新建class

2、继承 net.jlxxw.component.weixin.component.listener.WeiXinEventListener

3、添加 @Component注解

## 推送相关内容
### 客服接口信息推送
注入 net.jlxxw.component.weixin.function.push.PushCustomer

调用内置方法即可，可单个推送，或者批量推送

### 微信模版推送
注入 net.jlxxw.component.weixin.function.push.PushTemplate

调用内置方法即可，可单个推送，或者批量推送
