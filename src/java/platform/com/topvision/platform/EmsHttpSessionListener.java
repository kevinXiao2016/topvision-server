/**
 *
 */
package com.topvision.platform;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niejun
 */
public class EmsHttpSessionListener implements HttpSessionListener {
    private static Logger logger = LoggerFactory.getLogger(EmsHttpSessionListener.class);
    private static List<HttpSession> sessions = new ArrayList<HttpSession>();

    @Override
    public void sessionCreated(HttpSessionEvent evt) {
        logger.debug("EmsHttpSessionListener.sessionCreated:{}", evt);
        HttpSession session = evt.getSession();
        sessions.add(session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent evt) {
        logger.debug("EmsHttpSessionListener.sessionDestroyed:{}", evt);
        HttpSession session = evt.getSession();
        if (sessions.contains(session)) {
            sessions.remove(session);
        }
    }

    /**
     * @return the sessions
     */
    public static List<HttpSession> getSessions() {
        logger.debug("EmsHttpSessionListener.getSessions:{}", sessions);
        for (HttpSession session : sessions) {
            logger.debug("EmsHttpSessionListener.getSessions:{}", session);
        }
        return sessions;
    }
}
