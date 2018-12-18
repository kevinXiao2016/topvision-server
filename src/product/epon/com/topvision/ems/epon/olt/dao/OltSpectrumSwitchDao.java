package com.topvision.ems.epon.olt.dao;

import com.topvision.ems.epon.olt.domain.OltSpectrumSwitch;
import com.topvision.framework.dao.BaseEntityDao;

public interface OltSpectrumSwitchDao extends BaseEntityDao<Object> {

    /**
     * 插入olt频谱开关
     * 
     * @param oltSpectrumSwitch
     */
    void insertSpectrumOltSwitch(OltSpectrumSwitch oltSpectrumSwitch);

}
