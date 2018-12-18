/***********************************************************************
 * $Id: AbstractTrapParser.java,v1.0 2017年11月16日 下午7:01:22 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventSource;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.fault.service.TrapService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.snmp.Trap;

/**
 * @author vanzand
 * @created @2017年11月16日-下午7:01:22
 *
 */
public abstract class AbstractTrapParser implements TrapParser {

    @Autowired
    protected TrapService trapService;
    @Autowired
    protected EntityTypeService entityTypeService;
    @Autowired
    protected EntityDao entityDao;

    @PostConstruct
    public void initialize() {
        // setTrapCos(cos);
        trapService.registeryParser(this);
    }

    @PreDestroy
    public void destroy() {
        trapService.unregisteryParser(this);
    }

    /**
     * 所有trap的解析都按照规定的5个步骤的模版来执行，各子类只需实现其中的五步即可
     */
    @Override
    public void parse(Long entityId, Trap trap) {
        // 获取设备的trapCode
        Long trapCode = parseTrapCode(trap);

        // 将设备Code转换成网管Code
        Integer emsCode = convertToEmsCode(entityId, trap, trapCode);

        // 转换source
        EventSource eventSource = parseEventSource(trap, trapCode, entityId, emsCode);

        // 创建event
        Event event = buildEvent(trap, trapCode, emsCode, eventSource);

        // 发送event
        sendEvent(trap, event);
    }

    @Override
    public void sendEvent(Trap trap, Event event) {
        EventSender.getInstance().send(event);
    }

    /**
     * 解析Trap发送过来的日期,解析错误则采用系统时间(System.currentTimeMillis())
     * 
     * @param time
     *            日期字符串
     * @return 时间
     */
    protected Long parseTrapTime(String time) {
        try {
            String[] data = time.split(":");
            int year = (Integer.parseInt(data[0], 16) << 8) + Integer.parseInt(data[1], 16);
            if (Integer.parseInt(data[1], 16) < 0) {
                year += 256;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(year);
            sb.append("-");
            sb.append(Integer.parseInt(data[2], 16));
            sb.append("-");
            sb.append(Integer.parseInt(data[3], 16));
            sb.append(" ");
            sb.append(Integer.parseInt(data[4], 16));
            sb.append(":");
            sb.append(Integer.parseInt(data[5], 16));
            sb.append(":");
            sb.append(Integer.parseInt(data[6], 16));
            sb.append(".");
            sb.append(Integer.parseInt(data[7], 16));
            if (data.length == 11) {
                sb.append(",").append(Integer.parseInt(data[8], 16)).append(Integer.parseInt(data[9], 16)).append(":")
                        .append(Integer.parseInt(data[10], 16));
            }
            return DateUtils.FULL_S_FORMAT.parse(sb.toString()).getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }
}
