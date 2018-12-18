/***********************************************************************
 * $Id: BillBoardController.java,v1.0 2013年9月24日 上午11:52:44 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.mgmt;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.json.JSONObject;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.billboard.dao.BillBoardDao;
import com.topvision.ems.billboard.domain.Notice;
import com.topvision.ems.billboard.exception.InvalidNoticeException;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.User;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.user.message.UserEvent;
import com.topvision.platform.user.message.UserInfoListener;
import com.topvision.platform.util.CurrentRequest;

/**
 * 公告调度器，维护DB的同时需要同步更新调度器，调度器决定公告的生存周期，状态（只有调度器能修改状态）， 分配公告向哪些用户推送，在有效时间内一旦用户登录则提示最新的公告等 算法如下： 1.
 * 创建公告时，判断公告是否有效，无效抛异常，有效则加入到超期队列，并同步到DB。 同时创建一个超期任务，任务触发时间就是公告的截止时间。 推送该公告到对应的前端 2.
 * 超期任务触发时从超期队列中移除对应的公告，并同步到DB 3. 用户登录时，从超期队列中筛选出符合该用户地域范围内的公告返回到前端 拓展：
 * 由于我们当前的决策是只可以选当前时间为起始时间，所以我们认为只要满足 当前时间 < 截止时间 则认定该公告有效，放入超期队列 而忽略 当前时间 < 开始时间
 * 这种无效公告，如果以后有需要，我们可以再做一个等待队列，存放这种类型的公告，并做一个等待任务，任务触发时 移入到超期队列。
 * 
 * @author Bravin
 * @created @2013年9月24日-上午11:52:44
 * 
 */
@Service("billBoardController")
public class BillBoardControllerImpl extends BaseService implements BillBoardController, UserInfoListener {
    private static final Logger logger = LoggerFactory.getLogger(BillBoardControllerImpl.class);
    private static List<Notice> expireQuene = new ArrayList<Notice>();
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private BillBoardDao billBoardDao;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private MessageService messageService;
    private static final boolean EXPIRED = false;

    @Override
    @PostConstruct
    public void initialize() {
        List<Notice> list = billBoardDao.selectAllValidNotice();
        for (Notice notice : list) {
            try {
                checkNotice(notice);
                createExpireJob(notice);
            } catch (InvalidNoticeException e) {
                // 有可能服务器在未开启的时候公告超期，而重启后无法接收到超期任务响应
                billBoardDao.expireNotice(notice, EXPIRED);
                logger.debug("notice: {} has already exipred!", notice);
            } catch (Exception e) {
                logger.error("add notice faild : {}", e);
            }
        }
        // addEventListeners
        messageService.addListener(UserInfoListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        messageService.removeListener(UserInfoListener.class, this);
        expireQuene.clear();
    }

    /**
     * 添加一个待管理的公告
     * 
     * @throws ParseException
     */
    @Override
    public void dispatchNotice(Notice notice) throws ParseException {
        // check notice if already expired
        checkNotice(notice);
        // synchonize DB
        insertNotice(notice);

        if (notice.getType() == Notice.SERVER_INFO) {
            // 系统自动发的公告，设置为admin用户发送
            notice.setUsername("admin");
            String content = notice.getContent();
            String clearStr = "[<a onclick='clearDiskBillboard(" + notice.getNoticeId()
                    + ")' href='javascript:;' style='color: red'>清除</a>]";
            content = content.concat(clearStr);
            notice.setContent(content);
        } else {
            notice.setUsername(CurrentRequest.getCurrentUser().getUser().getUserName());
        }
        // IN DEQUENE & START JOB
        createExpireJob(notice);
        // FIRE MESSAGE
        postToBillboard(notice);
    }

    /**
     * 通知客户端接收这一公告
     * 
     * @param notice
     */
    private void postToBillboard(Notice notice) {
        Message message = new Message(Message.NOTICE_TYPE);
        message.setData(JSONObject.fromObject(notice));
        messagePusher.sendMessage(message);
    }

    /**
     * 添加一个公告
     * 
     * @param notice
     * @throws ParseException
     */
    public void insertNotice(Notice notice) throws ParseException {
        // save to db
        Timestamp timestamp = DateUtils.getCurrentTimestamp();
        notice.setCreateTime(timestamp);
        notice.setStartTime(timestamp);
        billBoardDao.insertNotice(notice);
    }

    /**
     * 创建一个超期任务
     * 
     * @param notice
     */
    private void createExpireJob(Notice notice) {
        // add to quene
        expireQuene.add(notice);
        // create expire job
        JobDetail job = newJob(NoticeExpireJob.class).withIdentity(notice.getJobKey()).build();
        job.getJobDataMap().put("notice", notice);
        job.getJobDataMap().put("billBoardController", this);
        try {
            Trigger trg = newTrigger().withIdentity(notice.getTriggerKey()).startAt(notice.getDeadline()).build();
            schedulerService.scheduleJob(job, trg);
        } catch (SchedulerException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * 判断公告是否有效
     * 
     * @param notice
     */
    private void checkNotice(Notice notice) {
        // mark by @bravin:以后如果可以配置开始时间的话，则需要做判断，并决定是加入超期队列还是等待队列
        if (notice.getDeadline().getTime() < System.currentTimeMillis()) {
            throw new InvalidNoticeException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.billboard.mgmt.BillBoardController#removeExpireNotice(com.topvision.ems
     * .billboard.domain.Notice)
     */
    @Override
    public void removeExpireNotice(Notice notice) throws SchedulerException {
        // delete from expireQuene
        expireQuene.remove(notice);
        // 让这个公告超期，设置其状态为false
        billBoardDao.expireNotice(notice, EXPIRED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.billboard.mgmt.BillBoardController#removeExpireNotice(java.lang.Integer)
     */
    @Override
    public void removeExpireNotice(Integer noticeId) throws SchedulerException {
        Notice notice = new Notice();
        // TODO 从quene中获取这一公告实例
        // delete from expireQuene
        expireQuene.remove(notice);
        // delete from database
        billBoardDao.deleteByPrimaryKey(notice.getNoticeId());
        // delete from expireJob
        schedulerService.deleteJob(notice.getJobKey());
    }

    @Override
    public void userInfoChanged(UserEvent event) {
        User user = (User) event.getSource();
        for (Notice notice : expireQuene) {
            if (notice.getUserId().equals(user.getUserId())) {
                notice.setUsername(user.getUserName());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.billboard.mgmt.BillBoardController#loadAllValidNotice(java.lang.Long)
     */
    @Override
    public List<Notice> loadAllValidNotice(Long userId) {
        // mark by @bravin: 可以在这里添加权限过滤器等，筛选合适的公告
        return expireQuene;
    }

    @Override
    public void clearNoticeById(Integer noticeId) {
        for (Notice notice : expireQuene) {
            if (notice.getNoticeId().equals(noticeId)) {
                try {
                    removeExpireNotice(notice);
                } catch (SchedulerException e) {
                    logger.error("clear noticeById error", e);
                }
                break;
            }
        }
    }
}
