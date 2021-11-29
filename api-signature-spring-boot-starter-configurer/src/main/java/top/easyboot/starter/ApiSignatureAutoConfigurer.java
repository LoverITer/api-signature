package top.easyboot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import top.easyboot.dao.auto.mapper.SignatureMapper;
import top.easyboot.handler.CommonSignInterceptor;
import top.easyboot.handler.SignInterceptor;
import top.easyboot.sign.SignHandler;
import top.easyboot.sign.impl.CommonSignHandler;

/**
 * @author: frank.huang
 * @date: 2021-11-27 22:03
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(ApiSignatureProperties.class)
@ConditionalOnProperty(
        prefix = "application.signature",
        name = "enable",
        havingValue = "true",
        matchIfMissing = true
)
public class ApiSignatureAutoConfigurer {

    private final ApiSignatureProperties signatureProperties;

    private final SignatureMapper signatureMapper;

    public ApiSignatureAutoConfigurer(ApiSignatureProperties signatureProperties,SignatureMapper signatureMapper) {
        this.signatureProperties = signatureProperties;
        this.signatureMapper=signatureMapper;
    }


    @Bean(value = "defaultSignHandler")
    @ConditionalOnMissingBean(CommonSignHandler.class)
    public SignHandler signHandler() {
        CommonSignHandler commonSignHandler = new CommonSignHandler(signatureProperties,signatureMapper);
        log.info("Registered the default sign handler[{}] to Spring container successfully.", commonSignHandler.getClass().getTypeName());
        return commonSignHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public SignInterceptor signInterceptor(SignHandler defaultSignHandler) {
        CommonSignInterceptor signInterceptor = null;
        if (signatureProperties.isEnable()) {
            signInterceptor = new CommonSignInterceptor(defaultSignHandler);
            log.info("Registered the default sign interceptor[{}] to Spring container successfully.", signInterceptor.getClass().getTypeName());
        }
        return signInterceptor;
    }

}
