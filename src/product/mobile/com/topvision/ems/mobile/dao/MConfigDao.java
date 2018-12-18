package com.topvision.ems.mobile.dao;

import java.util.List;

import com.topvision.ems.mobile.domain.MobileDeviceType;

public interface MConfigDao {

    /**
     * @return
     */
    List<MobileDeviceType> getMobileDeviceTypeList();

    /**
     * @param typeId
     * @param frequency
     * @param powerlevel
     */
    void modifyMobileDeviceType(Long typeId, String frequency, String powerlevel);

    /**
     * @param typeId
     */
    void delMobileDeviceType(Long typeId);

    /**
     * @param typeId
     */
    void setDefaultMobileDeviceType(Long typeId);

    /**
     * @param deviceType
     * @param corporation
     * @param powerlevel
     */
    void addMobileDeviceType(String deviceType, String corporation, String frequency, String powerlevel);

    /**
     * @param typeId
     * @return
     */
    MobileDeviceType getMobileDeviceTypeByTypeId(Long typeId);

}
