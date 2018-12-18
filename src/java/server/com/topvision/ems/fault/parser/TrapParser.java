/***********************************************************************
 * $Id: SyslogParser.java,v1.0 2012-1-13 下午01:38:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventSource;
import com.topvision.ems.fault.exception.TrapInstanceException;
import com.topvision.framework.snmp.Trap;

/**
 * @author Victor
 * @created @2012-1-13-下午01:38:41
 * 
 *          Trap解析接口，实现此接口并把实现注册到TrapService中，接收到Trap会顺序解析
 */
public interface TrapParser {

    /**
     * 判断trap是否匹配当前trapParser
     * 
     * @param entityId
     * @param trap
     * @return
     */
    public boolean match(Long entityId, Trap trap);

    /**
     * 解析告警，所有trap的解析都按照规定的5个步骤的模版来执行，各子类只需实现其中的五步即可</br>
     * 1. parseTrapCode 解析trapCode </br>
     * 2. convertToEmsCode 获取网管对应的code </br>
     * 3. parseEventSource 根据trap解析对应的位置</br>
     * 4. buildEvent 根据trap构造event </br>
     * 5. sendEvent 发送转换后的事件
     * 
     * @param entityId
     *            来源设备ID
     * @param trap
     *            接收的Trap
     */
    public void parse(Long entityId, Trap trap);

    /**
     * 解析trapCode
     * 
     * @param trap
     * @return
     */
    public Long parseTrapCode(Trap trap);

    /**
     * 根据trap获取网管对应的code
     * 
     * @param trap
     *            接收到的trap
     * @param trapCode
     *            trap的code
     * @return
     */
    public Integer convertToEmsCode(Long entityId, Trap trap, Long trapCode);

    /**
     * 根据trap解析对应的位置
     * 
     * @param trap
     *            接收到的trap
     * @param entityId
     *            entityId
     * @param trapCode
     *            trap的code
     * @param emsCode
     *            网管对应的code
     * @return
     * @throws TrapInstanceException
     */
    public EventSource parseEventSource(Trap trap, Long entityId, Long trapCode, Integer emsCode)
            throws TrapInstanceException;

    /**
     * 根据trap构造event
     * 
     * @param trap
     *            接收到的trap
     * @param trapCode
     *            trap的code
     * @param emsCode
     *            网管对应的code
     * @param eventSource
     *            trap对应设备上的位置
     * @return
     */
    public Event buildEvent(Trap trap, Long trapCode, Integer emsCode, EventSource eventSource);

    /**
     * 发送转换后的事件
     * 
     * @param trap
     * @param event
     */
    public void sendEvent(Trap trap, Event event);

    /**
     * 获取该trapParser对应的优先级
     * 
     * @return
     */
    public Integer getTrapCos();
}
