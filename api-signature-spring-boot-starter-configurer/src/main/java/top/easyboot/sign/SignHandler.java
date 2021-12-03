package top.easyboot.sign;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import top.easyboot.constant.Constants;
import top.easyboot.exception.SignatureArgumentException;
import top.easyboot.exception.SignatureException;
import top.easyboot.util.SignUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author: frank.huang
 * @date: 2021-11-13 14:27
 */
public interface SignHandler {


    default String getSecret() {
        throw new UnsupportedOperationException("method should be implemented by subclasses");
    }

    String getSecretByAppId(String appId);

    default String getAppId() {
        throw new UnsupportedOperationException("method should be implemented by subclasses");
    }

    default String getHashAlgorithm() {
        throw new UnsupportedOperationException("method should be implemented by subclasses");
    }

    /**
     * 系统根据自己的secret和app_id生成请求其他系统的验签值
     *
     * @param signEntity
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default String generateRequestSign(SignEntity signEntity) throws InvocationTargetException, IllegalAccessException {
        Map<String, Collection<String>> pathParams = signEntity.getPathParams();
        pathParams.put(Constants.APP_ID, Lists.newArrayList(getAppId()));
        pathParams.put(Constants.SECRET, Lists.newArrayList(getSecret()));
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
        Map<String, Collection<String>> params = signEntity.getPathParams();
        checkAndGetSignParam(Constants.TIMESTAMP, params);
        String appId = checkAndGetSignParam(Constants.APP_ID, params);
        String oldSign = checkAndGetSignParam(Constants.SIGN, params);
        params.put(Constants.SECRET, Lists.newArrayList(getSecretByAppId(appId)));
        signEntity.setPathParams(params);
        String newSign = generateSign(signEntity);
        if (!oldSign.equals(newSign)) {
            throw new SignatureException("sign invalid");
        }
        return true;
    }

    default String checkAndGetSignParam(String name, Map<String, Collection<String>> params) {
        Collection<String> paramCollection = params.get(name);
        if (CollectionUtils.isEmpty(paramCollection)) {
            throw new SignatureArgumentException(String.format("Required parameter '%s' is not present", name));
        }
        Object[] paramArray = paramCollection.toArray();
        if (paramArray.length == 0) {
            throw new SignatureArgumentException(String.format("Required parameter '%s' is not present", name));
        }
        String paramValue = (String) paramArray[0];
        if (StringUtils.isBlank(paramValue)) {
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
