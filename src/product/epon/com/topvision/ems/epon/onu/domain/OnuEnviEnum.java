package com.topvision.ems.epon.onu.domain;

/**
 * ONU环境枚举类
 * 
 * @author w1992wishes
 * @created @2017年12月26日-下午4:43:53
 *
 */
public enum OnuEnviEnum {

    EPON("E"), // 纯EPON环境
    GPON("G"), // 纯GPON环境
    MIXTURE("M"); // 混合环境

    private String pattern;

    public String getPattern() {
        return pattern;
    }

    private OnuEnviEnum(String pattern) {
        this.pattern = pattern;
    }

}
