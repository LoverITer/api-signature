package top.easyboot.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import top.easyboot.enums.HashAlgorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: frank.huang
 * @date: 2021-11-27 20:21
 */
@ConfigurationProperties(prefix = "application.signature")
public class ApiSignatureProperties {

    private String appId;
    private String secret;
    private boolean enable = true;
    /**
     * {@link HashAlgorithm}
     */
    private String hashType = HashAlgorithm.SHA256.getHashType();

    /**
     * 排除验签的接口
     */
    private final Set<String> excludes = new HashSet<>();

    public ApiSignatureProperties() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getHashType() {
        return hashType;
    }

    public void setHashType(String hashType){
        this.hashType=hashType;
    }

    public Set<String> getExcludes() {
        return excludes;
    }
}

