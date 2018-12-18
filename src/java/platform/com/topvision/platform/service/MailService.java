/**
 *
 */
package com.topvision.platform.service;

import java.util.Properties;

import com.topvision.exception.service.SendMailException;
import com.topvision.platform.domain.MailMsg;

/**
 * @author niejun
 */
public interface MailService extends ActionService {

    /**
     * 发送电子邮件.
     * 
     * @param msg
     */
    void sendMail(MailMsg msg);

    /**
     * 发送测试邮件.
     * 
     * @param testMsg
     * @throws SendMailException
     */
    void sendTestMail(String testMsg, Properties props) throws SendMailException;

}
