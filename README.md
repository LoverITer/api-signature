# 📚api-signature-spring-boot-starter

在web开发的过程当中，当对外提供的接口可以被随意调用时，可能存在非法用户可以对咱们的服务器进行恶意攻击，致使服务器不能有效处理正常的业务请求，所以须要考虑对这些暴露出去的http接口作防刷限制。其中一个行之有效的方法就是给对外接口加验证身份的签名（即验签）。具体到业务中，当调用者B请求调用服务者A的接口时，服务者A须要验证调用者B的身份，并经过签名来验证调用者的身份，整个流程大体如下：

<a href="http://image.easyblog.top/16382389875331bfc0d91-12e2-4562-b7cd-00bed63a428b.png><img src="http://image.easyblog.top/16382389875331bfc0d91-12e2-4562-b7cd-00bed63a428b.png></a>

## ⭐️ 1、快速开始
### 1.1 引入依赖
工具基于Spring Boot自动配置原理开发，已经打包成场景启动器 `api-signature-spring-boot-starter`，使用时首先需要在项目POM文件中引入以下依赖：
```xml
<dependency>
    <groupId>top.easyboot</groupId>
    <artifactId>api-signature-spring-boot-starter</artifactId>
    <version>${api-signature-version}</version>
</dependency>
```

### 1.2 写配置
目前提供有一下配置项，配置不是必须的，都有默认值
```properties
application.signature.enable  #是否开启验签自动配置，默认值为true，即默认自动开启
application.signature.app-id  #配置分配的app_id
application.signature.secret  #配置分配的secret
application.signature.hash-type  #使用何种摘要算法，目前支持MD5、SHA、以及HAMC，默认值SHA256
```

### 1.3 注册验签拦截器
在项目中实现spring mvc提供的`WebMvcConfigurer`接口并实现方法`addInterceptor(InterceptorRestry)`注册验签拦截器
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.easyboot.handler.SignInterceptor;
import top.easyboot.titan.handler.LogInterceptor;


@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    private SignInterceptor signInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInterceptor);
    }
}
```

