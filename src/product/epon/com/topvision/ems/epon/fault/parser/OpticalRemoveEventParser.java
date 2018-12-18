package com.topvision.ems.epon.fault.parser;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;

/**
 * 
 * @author w1992wishes
 * @created @2018年1月26日-下午5:42:27
 *
 */
@Service("opticalRemoveEventParser")
public class OpticalRemoveEventParser extends PonAbstractEventParser {

    public OpticalRemoveEventParser() {
        this.threadName = "OpticalRemoveEventParser";
    }

    @Override
    protected int getPonAlarmCode() {
        return EponCode.PON_OPTICAL_REMOVE;
    }

    @Override
    protected int getPonClearCode() {
        return EponCode.PON_OPTICAL_INSERT;
    }

}
