/***********************************************************************
 * $Id: BillBoardAction.java,v1.0 2013年9月24日 上午11:12:57 $
 * 
 * @author: bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.billboard.domain.Notice;
import com.topvision.ems.billboard.service.BillBoardService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;

/**
 * 公告牌
 * 
 * @author bravin
 * @created @2013年9月24日-上午11:12:57
 *
 */
@Controller("billBoardAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BillBoardAction extends BaseAction {
    private static final long serialVersionUID = 7530934199478042503L;
    private final Logger logger = LoggerFactory.getLogger(BillBoardAction.class);

    @Autowired
    private BillBoardService billBoardService;
    private String title;
    private String content;
    private String deadline;
    private Integer noticeId;

    /**
     * 显示公告新建页面
     * 
     * @return
     */
    public String showNoticeCreate() {
        return SUCCESS;
    }

    /**
     * 创建一个新的公告
     * 
     * @return
     * @throws ParseException
     */
    public String createNotice() throws ParseException {
        Notice notice = new Notice();
        notice.setUserId(CurrentRequest.getCurrentUser().getUserId());
        // notice.setTitle(title);
        notice.setContent(content);
        notice.setDeadline(DateUtils.parseToTimestamp(deadline));
        billBoardService.createNotice(notice);
        return NONE;
    }

    /**
     * 加载所有的有效公告
     * 
     * @return
     * @throws IOException
     */
    public String loadAllValidNotice() throws IOException {
        Long userId = CurrentRequest.getCurrentUser().getUserId();
        List<Notice> quene = billBoardService.loadAllValidNotice(userId);
        JSONObject json = new JSONObject();
        json.put("data", quene);
        json.write(response.getWriter());
        return NONE;
    }
    
    public String clearNoticeById() {
        billBoardService.clearNoticeById(noticeId);
        return NONE;
    }

    /**
     * 修改一个公告
     * 
     * @return
     */
    public String modifyNotice() {
        return NONE;
    }

    /**
     * 删除一个公告
     * 
     * @return
     */
    public String deleteNotice() {
        return NONE;
    }

    /**
     * 查询所有的公告
     * 
     * @return
     */
    public String loadAllNotice() {
        return NONE;
    }

    /**
     * 按关键字查询公告
     * 
     * @return
     */
    public String queryNotice() {
        return NONE;
    }

    public BillBoardService getBillBoardService() {
        return billBoardService;
    }

    public void setBillBoardService(BillBoardService billBoardService) {
        this.billBoardService = billBoardService;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

}
