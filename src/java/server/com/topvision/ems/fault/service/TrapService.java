package com.topvision.ems.fault.service;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.NbiConfig;
import com.topvision.ems.fault.parser.TrapParser;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.Trap;

public interface TrapService extends Service {
    /**
     * 发送Snmp Trap
     * 
     * @param trap
     */
    void sendTrap(Trap trap);

    /**
     * 
     * @param parser
     */
    void registeryParser(TrapParser parser);

    /**
     * 
     * @param index
     * @param parser
     */
    void registeryParser(int index, TrapParser parser);

    /**
     * 
     * @param parser
     */
    void unregisteryParser(TrapParser parser);
    
    /**
     * 
     * @return
     */
    NbiConfig getNbiConfig();
    
    /**
     * 
     * 
     * @param nbiConfig
     */
    void modifyNbiConfig(NbiConfig nbiConfig);
    
    
    /**
     *  Start North Heart Beat
     * 
     */
    void reStartNorthHeartBeatJob();
    
    /**
     * Send Alarm to NorthBound
     * 
     * @param alert
     */
    void sendAlarmToNbi(Alert alert);
    
    /**
     * Send Test Alarm to NorthBound
     * 
     * @param nbiConfig
     */
    void sendNorthBoundTestAlarm(NbiConfig nbiConfig);
}
