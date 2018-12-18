package com.topvision.ems.epon.fault.parser;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;

/**
 * 
 * @author w1992wishes
 * @created @2018年1月26日-下午5:42:27
 *
 */
@Service("ponLosEventParser")
public class PonLosEventParser extends PonAbstractEventParser {

    public PonLosEventParser() {
        this.threadName = "PonLosEventParser";
    }

    @Override
    protected int getPonAlarmCode() {
        return EponCode.PORT_PON_LOS;
    }

    @Override
    protected int getPonClearCode() {
        return EponCode.PORT_PON_LOS_RECOVERY;
    }

}
