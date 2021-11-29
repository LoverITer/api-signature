package top.easyboot.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author: frank.huang
 * @date: 2021-10-31 22:54
 */
public final class Constants {

    //通用
    public static final String UNDERSCORE = "_";
    public static final String DELIMETER = "&";
    public static final String SHARP = "#";
    public static final String COMMA = ",";
    public static final String EQUAL_SIGN = "=";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    //请求类
    public static final String REQUEST_ID = "RequestId";
    public static final String TX_ID = "TxId";
    public static final String TRACE_ID = "TraceId";
    public static final String SPAN_ID = "SpanId";
    public static final String SPAN_EXPORT = "SpanExport";
    public static final String REQUEST_ID_HEADER = "RequestId";
    public static final String IP = "IP";
    public static final String UNKNOWN_IP = "unknown";
    public static final String REQUEST_URL = "url";
    public static final String SIGN = "sign";
    public static final String APP_ID="app_id";
    public static final String SECRET="secret";
    public static final String TIMESTAMP="timestamp";
    public static final String LOCAL_HOST="localhost";
    public static final String LOOPBACK_ADDRESS="127.0.0.1";

    //缓存类
    public static final String REDIS_USER_KEY = "easyblog-cli:user:name:%s";

    //时间
    public static final long TEN_MINUS = 10*60*1000;
}
