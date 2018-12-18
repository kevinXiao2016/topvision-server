package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author xiaoyue
 * @created @2017年12月6日-下午7:25:59
 *
 */
public class AddStaticIpException extends ServiceException {
    private static final long serialVersionUID = -3548517220042255592L;

    public AddStaticIpException() {
    }

    public AddStaticIpException(String s) {
        super(s);
    }

    public AddStaticIpException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddStaticIpException(Throwable throwable) {
        super(throwable);
    }
}
