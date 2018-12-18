package com.topvision.ems.epon.fault.helper;

/**
 * 上联口 转换 辅助类
 * 
 * @author w1992wishes
 * @created @2018年1月18日-下午4:40:10
 *
 */
public interface PortConvertHelper {

    Integer convertPort(Long entityId, Integer port);

    Integer convertSlot(Long entityId, Integer slot);

}
