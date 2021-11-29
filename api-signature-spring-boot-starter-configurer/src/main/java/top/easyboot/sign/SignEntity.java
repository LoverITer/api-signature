package top.easyboot.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * @author: frank.huang
 * @date: 2021-11-13 14:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignEntity {

    private String method;
    private String path;
    private Map<String, Collection<String>> bodyParams;
    private Map<String, String> headers;
    private Map<String, Collection<String>> pathParams;

}
