package com.topvision.ems.epon.olt.constants;

/**
 * 风扇告警枚举类
 * 
 * @author w1992wishes
 * @created @2018年1月19日-下午5:38:29
 *
 */
public enum FanAlarmLevelEnum {

    CRITICAL(0, "critical"), MAJOR(1, "major"), MINOR(2, "minor"), WARNING(3, "warning"), NONE(4, "--");

    private FanAlarmLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    private String name;
    private int level;

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

}
