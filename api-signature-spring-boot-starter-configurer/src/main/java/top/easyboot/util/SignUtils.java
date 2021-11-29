package top.easyboot.util;

import top.easyboot.enums.HashAlgorithm;
import top.easyboot.exception.SignatureArgumentException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: frank.huang
 * @date: 2021-11-07 17:48
 */
public class SignUtils {

    private static final Map<String, Method> METHODS_MAP;

    private static volatile SignUtils INSTANCE;

    private SignUtils() {
    }

    static {
        Class<SignUtils> clazz = SignUtils.class;
        Method[] methods = clazz.getMethods();
        METHODS_MAP = Arrays.stream(methods).collect(Collectors.toMap(Method::getName, Function.identity(), (o1, o2) -> o1));
    }

    public static SignUtils getInstance() {
        if (Objects.isNull(INSTANCE)) {
            synchronized (SignUtils.class) {
                if (Objects.isNull(INSTANCE)) {
                    INSTANCE = new SignUtils();
                }
            }
        }
        return INSTANCE;
    }


    public static String sign(String algorithm, String url) throws InvocationTargetException, IllegalAccessException {
        HashAlgorithm hashAlgorithm = HashAlgorithm.of(algorithm);
        if (Objects.nonNull(hashAlgorithm)) {
            String hashType = hashAlgorithm.getHashType();
            if (!METHODS_MAP.isEmpty()) {
                Method method = METHODS_MAP.get(hashType);
                if (Objects.isNull(method)) {
                    throw new SignatureArgumentException("Required parameter `hash_type` is not present");
                }
                SignUtils signUtils = getInstance();
                return (String) method.invoke(signUtils, url);
            }
        }
        return null;
    }

    public static String sha256(String url) {
        return EncryptUtils.SHA256(url);
    }

    public static String md5(String url) {
        return EncryptUtils.MD5(url);
    }
}
