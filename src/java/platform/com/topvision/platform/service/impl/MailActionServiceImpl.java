/***********************************************************************
 * $Id: MailActionServiceImpl.java,v 1.1 Sep 25, 2008 10:44:03 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.topvision.exception.service.SendMailException;
import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.MailMsg;
import com.topvision.platform.domain.User;
import com.topvision.platform.service.MailService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserService;

/**
 * 邮件处理, 为了解决性能问题, 自定义邮件队列处理邮件发送.
 * 
 * @author kelers, niejun
 * @Create Date Sep 25, 2008 10:44:03 PM
 */
@Service("mailActionService")
public class MailActionServiceImpl extends BaseService implements MailService {
	@Autowired
	private UserService userService;
	@Autowired
	private SystemPreferencesService systemPreferencesService = null;
	@Resource(name = "mailSender")
	private JavaMailSenderImpl mailSender = null;
	@Resource(name = "bakMailSender")
	private JavaMailSenderImpl bakMailSender = null;
	@Autowired
	private SimpleMailMessage mailMessage = null;

	/**
	 * @return the bakMailSender
	 */
	public JavaMailSenderImpl getBakMailSender() {
		return bakMailSender;
	}

	/**
	 * @return the mailMessage
	 */
	public SimpleMailMessage getMailMessage() {
		return mailMessage;
	}

	/**
	 * @return the mailSender
	 */
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}
    @Override
    public String checkConnection(String smsServiceIp, int smsServicePort) {
        return null;
    }
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.framework.service.BaseService#initialize()
	 */
	@Override
	@PostConstruct
	public void initialize() {
		try {
			reset();
		} catch (Exception e) {
			getLogger().debug(e.getMessage(), e);
		}
	}

	/**
	 * 判断邮件服务器是否设置
	 * 
	 * @return
	 */
	@Override
	public Boolean isServerSetting() {
		Properties prop = null;
		prop = systemPreferencesService.getModulePreferences(Action.MAIL_SERVER);
		return prop != null && !prop.isEmpty();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.platform.service.ActionService#reset()
	 */
	@Override
	public void reset() {
		Properties props = systemPreferencesService.getModulePreferences(Action.MAIL_SERVER);
		// Modify by victor @2012.12.14如果发送设置为空不能使用空字符串
		if (props.getProperty("senderEmail", "liyongcheng@sumavision.com").trim().length() != 0) {
			mailMessage.setFrom(props.getProperty("senderEmail", "liyongcheng@sumavision.com"));
		} else {
			mailMessage.setFrom(null);
		}
		mailSender.setHost(props.getProperty("smtpServer", "127.0.0.1"));
		mailSender.setPort(Integer.parseInt(props.getProperty("smtpPort", "25")));
		Properties jmp = new Properties();
		mailSender.setJavaMailProperties(jmp);
		if (props.getProperty("requireAuth", "true").equals("true")) {
			jmp.setProperty("mail.smtp.auth", "true");
			mailSender.setUsername(props.getProperty("username", ""));
			mailSender.setPassword(props.getProperty("password", ""));
		} else {
			jmp.setProperty("mail.smtp.auth", "false");
		}
		// backup mail server
		if (props.getProperty("bakEmailServer", "false").equals("false")) {
			return;
		}
		bakMailSender.setHost(props.getProperty("smtpServer1", "127.0.0.1"));
		bakMailSender.setPort(Integer.parseInt(props.getProperty("smtpPort1", "25")));
		Properties jmp1 = new Properties();
		bakMailSender.setJavaMailProperties(jmp1);
		if (props.getProperty("requireAuth1", "true").equals("true")) {
			jmp1.setProperty("mail.smtp.auth", "true");
			bakMailSender.setUsername(props.getProperty("username1", ""));
			bakMailSender.setPassword(props.getProperty("password1", ""));
		} else {
			jmp1.setProperty("mail.smtp.auth", "false");
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.platform.service.ActionService#
	 *      sendAction(com.topvision.platform.domain.server.fault.domain.Action,
	 *      java.lang.String)
	 */
	@Override
	public void sendAction(Action action, Object object, String msg) throws ServiceException {
		Properties props = systemPreferencesService.getModulePreferences(Action.MAIL_SERVER);
		MimeMessageHelper helper;
		MimeMessage mime = mailSender.createMimeMessage();
		try {
			helper = new MimeMessageHelper(mime, true, "utf-8");
			if (props.getProperty("senderEmail", "liyongcheng@sumavision.com").trim().length() != 0) {
				helper.setFrom(props.getProperty("senderEmail", "liyongcheng@sumavision.com"));
			}
			if (action.getUserId() != null) {
				User user = userService.getUserEx(action.getUserId());
				if (user.getEmail() == null || user.getEmail().length() == 0) {
					return;
				}
				helper.setTo(user.getEmail());
			} else {
				helper.setTo((String) action.getParamsObject());
			}
			helper.setSubject(msg);
			helper.setText(msg);
			mailSender.send(mime);
		} catch (MailException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug(action.toString(), ex);
			}
			try {
				bakMailSender.send(mime);
			} catch (MailException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(action.toString(), e);
				}
				throw new SendMailException(e);
			}
		} catch (MessagingException me) {
			if (logger.isDebugEnabled()) {
				logger.debug(action.toString(), me);
			}
		}
		/*
		 * mailMessage.setSubject(msg); mailMessage.setText(msg);
		 * mailMessage.setTo((String) action.getParamsObject()); try {
		 * mailSender.send(mailMessage); } catch (MailException ex) { if
		 * (logger.isDebugEnabled()) { getLogger().debug(action.toString(), ex);
		 * } try { bakMailSender.send(mailMessage); } catch (MailException e) {
		 * throw new SendMailException(e); } }
		 */
	}

	/**
	 * @param msg
	 */
	@Override
	public void sendMail(MailMsg msg) {
		// todo
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.platform.system.service.MailService#sendTestMail(java.lang
	 * .String, java.util.Properties)
	 */
	@Override
	public void sendTestMail(String testMsg, Properties props) throws SendMailException {
		// Modify by victor@2012.12.13解决备份服务器发送测试邮件不成功的问题，修改把公共部分提前
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties jmp = new Properties();
		mailSender.setProtocol("smtp");
		mailSender.setJavaMailProperties(jmp);
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(props.getProperty("testEmail"));
		mail.setText("This is a test mail from Topvision.");
		mail.setSubject("From Topvision");
		String senderEmail = props.getProperty("senderEmail");
		if (senderEmail != null && senderEmail.length() > 0) {
			mail.setFrom(senderEmail);
		}
		try {
			mailSender.setHost(props.getProperty("smtpServer"));
			int port = 0;
			try {
				port = Integer.parseInt(props.getProperty("smtpPort"));
			} catch (NumberFormatException e) {
				port = 25;
			}
			mailSender.setPort(port);
			if ("true".equals(props.getProperty("requireAuth"))) {
				jmp.setProperty("mail.smtp.auth", "true");
				mailSender.setUsername(props.getProperty("username"));
				mailSender.setPassword(props.getProperty("password"));
			} else {
				jmp.setProperty("mail.smtp.auth", "false");
			}
			mailSender.send(mail);
		} catch (MailException e) {
			if ("false".equals(props.getProperty("bakEmailServer"))) {
				throw new SendMailException(e);
			}
			if (logger.isInfoEnabled()) {
				logger.info("Send test mail by backup email server[{}]", props.getProperty("smtpServer1"));
			}
			mailSender.setHost(props.getProperty("smtpServer1"));
			int port = 0;
			try {
				port = Integer.parseInt(props.getProperty("smtpPort1"));
			} catch (NumberFormatException ex) {
				port = 25;
			}
			mailSender.setPort(port);
			if ("true".equals(props.getProperty("requireAuth1"))) {
				jmp.setProperty("mail.smtp.auth", "true");
				mailSender.setUsername(props.getProperty("username1"));
				mailSender.setPassword(props.getProperty("password1"));
			} else {
				jmp.setProperty("mail.smtp.auth", "false");
			}
			try {
				mailSender.send(mail);
			} catch (MailException e1) {
				throw new SendMailException(e1);
			}
		}
	}

	/**
	 * @param bakMailSender
	 *            the bakMailSender to set
	 */
	public void setBakMailSender(JavaMailSenderImpl bakMailSender) {
		this.bakMailSender = bakMailSender;
	}

	/**
	 * @param mailMessage
	 *            the mailMessage to set
	 */
	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	/**
	 * @param mailSender
	 *            the mailSender to set
	 */
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
		this.systemPreferencesService = systemPreferencesService;
	}

	@Override
	public String sendActionBak(Action action, Object object, String msg) throws ServiceException {
		return null;
	}

}
