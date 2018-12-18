/***********************************************************************
 * $ SameScheduleException.java,v1.0 2012-5-2 17:31:51 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.engine;

import com.topvision.framework.exception.engine.EngineException;

/**
 * @author jay
 * @created @2012-5-2-17:31:51
 */
public class SameScheduleException extends EngineException {
    private static final long serialVersionUID = 7342180206202447869L;

    public SameScheduleException() {
    }

    public SameScheduleException(Throwable cause) {
        super(cause);
    }

    public SameScheduleException(String message) {
        super(message);
    }

    public SameScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
