package top.easyboot.handler;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.easyboot.sign.SignEntity;
import top.easyboot.sign.SignHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author: frank.huang
 * @date: 2021-11-07 17:11
 */
@Slf4j
public abstract class SignInterceptor implements HandlerInterceptor {

    public abstract SignHandler handler();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        return HttpStatus.valueOf(response.getStatus()).isError() || this.handler().checkSign(this.getRequestEntity(request));
    }


    /**
     * 解析出请求信息并封装成SignEntity {@link SignEntity}
     *
     * @param request
     * @return
     */
    public SignEntity getRequestEntity(HttpServletRequest request) {
        SignEntity signEntity = SignEntity.builder().build();
        signEntity.setMethod(request.getMethod());
        signEntity.setPath(request.getRequestURI());
        //获取parameters（对应@RequestParam）
        final Map<String, Collection<String>> parameterMap = Maps.newHashMap();
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        if(!CollectionUtils.isEmpty(requestParameterMap)) {
            requestParameterMap.forEach((key, value) -> {
                List<String> values = new ArrayList<>(Arrays.asList(value));
                parameterMap.put(key, Collections.unmodifiableList(values));
            });
        }
        signEntity.setPathParams(parameterMap);
        return signEntity;
    }

}
