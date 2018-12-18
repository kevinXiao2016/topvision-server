/***********************************************************************
 * $Id: cc8800ASystemTimeFacadeImpl.java,v1.0 2013-11-23 下午5:30:03 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.engine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.performance.facade.CC8800ASystemTimeFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.telnet.TelnetParam;
import com.topvision.framework.telnet.TelnetVty;

/**
 * @author Victor
 * @created @2013-11-23-下午5:30:03
 *
 */
@Facade("cc8800ASystemTimeFacade")
public class CC8800ASystemTimeFacadeImpl extends EmsFacade implements CC8800ASystemTimeFacade<TelnetParam> {
    public static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.facade.CCSystemTimeFacade#test(com.topvision.framework.domain.DeviceParam)
     */
    @Override
    public Map<String, Long> getUptime(TelnetParam param) {
        Map<String, Long> times = new HashMap<String, Long>();
        try {
            TelnetVty telnet = new TelnetVty();
            telnet.connect(param.getIpAddress(), 23);
            String s = telnet.sendLine(param.getPassword());
            if (s.trim().endsWith("assword:")) {
                s = telnet.sendLine(param.getSuperPasswd());
            }
            s = telnet.sendLine("enable");
            s = telnet.sendLine(param.getEnablePasswd());
            if (s.trim().endsWith("assword:")) {
                s = telnet.sendLine(param.getSuperPasswd());
            }
            s = telnet.sendLine("super");
            s = telnet.sendLine(param.getSuperPasswd());
            s = telnet.sendLine("test");
            for (String mac : param.attributes().keySet()) {
                s = telnet.sendLine(String.format("show ccmts %s system-time", mac));
                if (logger.isDebugEnabled()) {
                    logger.debug("{}====>s", param, s);
                }
                times.put(mac, getTime(s));
            }
            telnet.disconnect();
        } catch (Exception e) {
            logger.error("CC8800ASystemTimeFacade.getUptime:{}", param, e);
        }
        return times;
    }

    private synchronized Long getTime(String sTime) {
        try {
            int index1 = sTime.indexOf("date:");
            int index2 = sTime.indexOf("time:");
            if (index1 == -1 || index2 == -1) {
                return null;
            }
            Calendar c = Calendar.getInstance();
            sTime = sTime.substring(index1 + 6, index1 + 16) + " " + sTime.substring(index2 + 6, index2 + 14);
            if (logger.isDebugEnabled()) {
                logger.debug("{}====>s", sTime);
            }
            c.setTime(FULL_FORMAT.parse(sTime));
            return c.getTimeInMillis() + (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET));
        } catch (ParseException e) {
            logger.error(sTime, e);
        }
        return null;
    }
}
