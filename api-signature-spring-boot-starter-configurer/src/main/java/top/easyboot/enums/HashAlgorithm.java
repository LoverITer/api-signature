package top.easyboot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author: frank.huang
 * @date: 2021-11-27 20:33
 */
@Getter
@AllArgsConstructor
public enum HashAlgorithm {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA256"),
    HmacMD5("HmacMD5"),
    HmacSHA1("HmacSHA1"),
    HmacSHA256("HmacSHA256");

    private String hashType;

    public String getHashType() {
        if (StringUtils.isNotBlank(hashType)) {
            return this.hashType.toLowerCase();
        }
        return null;
    }

    public static HashAlgorithm of(String hashType) {
        if (StringUtils.isBlank(hashType)) {
            throw new IllegalArgumentException("invalid hash algorithm");
        }
        if (contains(hashType)) {
            return Arrays.stream(HashAlgorithm.values()).filter(type -> type.getHashType().equalsIgnoreCase(hashType)).findAny().orElse(null);
        }
        return null;
    }

    public static boolean contains(String hashType) {
        if (StringUtils.isBlank(hashType)) {
            throw new IllegalArgumentException("invalid hash algorithm");
        }
        return Arrays.stream(HashAlgorithm.values()).map(HashAlgorithm::getHashType).anyMatch(hashType::equalsIgnoreCase);
    }

}
