package top.easyboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.easyboot.handler.SignInterceptor;
import top.easyboot.starter.ApiSignatureProperties;

/**
 * @author: frank.huang
 * @date: 2021-11-28 11:02
 */
@Slf4j
@Configuration
public class SignInterceptorRegistry implements WebMvcConfigurer {

    @Autowired
    private SignInterceptor defaultSignHandler;

    @Autowired
    private ApiSignatureProperties signatureProperties;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
       if(signatureProperties.isEnable()){
           registry.addInterceptor(defaultSignHandler);
           log.info("Registered the default sign interceptor[{}] to InterceptorRegistry successfully",defaultSignHandler.getClass().getTypeName());
       }
    }

}
