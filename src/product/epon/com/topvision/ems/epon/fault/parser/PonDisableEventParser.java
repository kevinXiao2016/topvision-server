package com.topvision.ems.epon.fault.parser;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;

/**
 * 
 * @author w1992wishes
 * @created @2018年1月26日-下午5:42:27
 *
 */
@Service("ponDisableEventParser")
public class PonDisableEventParser extends PonAbstractEventParser {

    public PonDisableEventParser() {
        this.threadName = "PonDisableEventParser";
    }

    @Override
    protected int getPonAlarmCode() {
        return EponCode.PORT_PON_DISABLE;
    }

    @Override
    protected int getPonClearCode() {
        return EponCode.PORT_PON_DISABLE_OK;
    }

}
