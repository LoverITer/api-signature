package top.easyboot.exception;

/**
 * @author: frank.huang
 * @date: 2021-11-29 22:13
 */
public class SignatureArgumentException extends IllegalArgumentException{

    public SignatureArgumentException(String reason){
        super(reason);
    }

}
