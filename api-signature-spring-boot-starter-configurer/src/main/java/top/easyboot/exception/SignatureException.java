package top.easyboot.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: frank.huang
 * @date: 2021-11-29 22:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SignatureException extends RuntimeException{

    public SignatureException(String reason){
        super(reason);
    }


}
