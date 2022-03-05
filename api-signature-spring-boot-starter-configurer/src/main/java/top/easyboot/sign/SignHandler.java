package top.easyboot.sign;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import top.easyboot.constant.Constants;
import top.easyboot.enums.HashAlgorithm;
import top.easyboot.exception.SignatureArgumentException;
import top.easyboot.exception.SignatureException;
import top.easyboot.util.SignUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author: frank.huang
 * @date: 2021-11-13 14:27
 */
public interface SignHandler {

    String getSecret();

    String getAppId();

    Set<String> excludes();

    default String getHashAlgorithm() {
        return HashAlgorithm.SHA256.getHashType();
    }

    /**
     * 系统根据自己的app_id生成请求其他系统的验签值
     *
     * @param signEntity
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default String generateRequestSign(SignEntity signEntity) throws InvocationTargetException, IllegalAccessException {
        return generateSign(signEntity);
    }

    /**
     * 计算验签值
     *
     * @param signEntity
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default String generateSign(SignEntity signEntity) throws InvocationTargetException, IllegalAccessException {
        StringBuilder appender = new StringBuilder(String.format("%s %s?", signEntity.getMethod(), signEntity.getPath()));
        Map<String, Collection<String>> pathParams = signEntity.getPathParams();
        if (!CollectionUtils.isEmpty(pathParams)) {
            pathParams.entrySet().stream().filter(entry -> !Constants.SIGN.equalsIgnoreCase(entry.getKey()))
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String paramValue = String.join(Constants.COMMA, entry.getValue());
                        appender.append(entry.getKey()).append(Constants.EQUAL_SIGN).append(paramValue).append(Constants.DELIMETER);
                    });
        }
        return SignUtils.sign(getHashAlgorithm(), appender.substring(0, appender.length() - 1));
    }


    default boolean checkSign(SignEntity signEntity) throws InvocationTargetException, IllegalAccessException {
        if (Objects.isNull(signEntity)) {
            throw new SignatureArgumentException("sign entity can not be null");
        }
        AntPathMatcher matcher = new AntPathMatcher();
        Optional<String> needExclude = excludes().stream().filter(pattern -> matcher.match(pattern, signEntity.getPath())).findAny();
        if(needExclude.isPresent()){
            return true;
        }
        Map<String, Collection<String>> params = signEntity.getPathParams();
        querySignParam(Constants.TIMESTAMP, params);
        String oldSign = querySignParam(Constants.SIGN, params);
        params.put(Constants.SECRET, Lists.newArrayList(getSecret()));
        signEntity.setPathParams(params);
        String newSign = generateSign(signEntity);
        if (Boolean.FALSE.equals(oldSign.equals(newSign))) {
            throw new SignatureException("sign invalid");
        }
        return true;
    }

    default String querySignParam(String name, Map<String, Collection<String>> params) {
        Collection<String> paramCollection = params.get(name);
        Object[] paramArray = null;
        String paramValue = null;
        if (CollectionUtils.isEmpty(paramCollection) ||
                (paramArray = paramCollection.toArray()).length == 0 ||
                StringUtils.isBlank((paramValue = (String) paramArray[0]))) {
            throw new SignatureArgumentException(String.format("Required parameter '%s' is not present", name));
        }

        if (Constants.TIMESTAMP.equalsIgnoreCase(name)) {
            long timestamp = Long.parseLong(paramValue);
            long now = new Date().getTime();
            if (now - timestamp > Constants.TEN_MINUS) {
                throw new SignatureException("sign expire");
            }
        }
        return paramValue;
    }


}
