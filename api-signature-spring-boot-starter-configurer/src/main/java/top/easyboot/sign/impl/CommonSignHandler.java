package top.easyboot.sign.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.easyboot.exception.SignatureArgumentException;
import top.easyboot.sign.SignHandler;
import top.easyboot.starter.ApiSignatureProperties;

import java.util.Set;

/**
 * @author: frank.huang
 * @date: 2021-11-13 15:34
 */
@Slf4j
public class CommonSignHandler implements SignHandler {

    private final ApiSignatureProperties signatureProperties;

    public CommonSignHandler(ApiSignatureProperties signatureProperties) {
        this.signatureProperties = signatureProperties;
    }

    @Override
    public String getSecret() {
        String secret = signatureProperties.getSecret();
        if (StringUtils.isBlank(secret)) {
            throw new SignatureArgumentException("Can not find parameter 'secret',consider config it in config file");
        }
        return secret;
    }

    @Override
    public String getAppId() {
        String appId = signatureProperties.getAppId();
        if (StringUtils.isBlank(appId)) {
            throw new SignatureArgumentException("Can not find parameter 'app_id',consider config it in config file");
        }
        return appId;
    }

    @Override
    public Set<String> excludes() {
        return signatureProperties.getExcludes();
    }

    @Override
    public String getHashAlgorithm() {
        String hashType = signatureProperties.getHashType();
        if (StringUtils.isBlank(hashType)) {
           log.error("Can not find parameter 'hash_type',default hash_type will be used");
           return SignHandler.super.getHashAlgorithm();
        }
        return hashType;
    }
}
