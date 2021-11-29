package top.easyboot.handler;

import top.easyboot.sign.SignHandler;

/**
 * @author: frank.huang
 * @date: 2021-11-13 15:22
 */
public class CommonSignInterceptor extends SignInterceptor{

    private final SignHandler signHandler;

    public CommonSignInterceptor(SignHandler signHandler) {
        this.signHandler = signHandler;
    }

    @Override
    public SignHandler handler() {
        return signHandler;
    }
}
