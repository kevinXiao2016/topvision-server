package com.topvision.exception.service;

import com.topvision.framework.exception.service.ServiceException;

public class SendMailException extends ServiceException {
    private static final long serialVersionUID = -114705382997095667L;

    public SendMailException() {
    }

    public SendMailException(String s) {
        super(s);
    }

    public SendMailException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SendMailException(Throwable throwable) {
        super(throwable);
    }
}
