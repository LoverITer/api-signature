package top.easyboot.sign.impl;

import org.apache.commons.lang3.StringUtils;
import top.easyboot.dao.auto.mapper.SignatureMapper;
import top.easyboot.dao.auto.model.Signature;
import top.easyboot.exception.SignatureArgumentException;
import top.easyboot.sign.SignHandler;
import top.easyboot.starter.ApiSignatureProperties;

import java.util.Objects;

/**
 * @author: frank.huang
 * @date: 2021-11-13 15:34
 */
public class CommonSignHandler implements SignHandler {

    private final ApiSignatureProperties signatureProperties;

    private final SignatureMapper signatureMapper;

    public CommonSignHandler(ApiSignatureProperties signatureProperties, SignatureMapper signatureMapper) {
        this.signatureProperties = signatureProperties;
        this.signatureMapper = signatureMapper;
    }


    @Override
    public String getSecret() {
        String secret = signatureProperties.getSecret();
        if (StringUtils.isBlank(secret)) {
            throw new SignatureArgumentException("Can not find parameter 'secret'");
        }
        return secret;
    }

    @Override
    public String getSecretByAppId(String appId) {
        if(StringUtils.isBlank(appId)){
            throw new SignatureArgumentException("Required parameter `app_id` not present");
        }
        Signature signature = signatureMapper.selectByAppId(appId);
        if(Objects.isNull(signature)){
            throw new SignatureArgumentException(String.format("Can not found signature record by app_id[%s]",appId));
        }
        return signature.getSecret();
    }

    @Override
    public String getAppId() {
        String appId = signatureProperties.getAppId();
        if (StringUtils.isBlank(appId)) {
            throw new SignatureArgumentException("Can not find parameter 'app_id'");
        }
        return appId;
    }

    @Override
    public String getHashAlgorithm() {
        String hashType = signatureProperties.getHashType();
        if (StringUtils.isBlank(hashType)) {
            throw new SignatureArgumentException("Can not find parameter 'hash_type'");
        }
        return hashType;
    }
}
