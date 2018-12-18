package com.topvision.ems.epon.cm.dao;

import java.util.List;

import com.topvision.ems.epon.domain.EponCmNumStatic;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.performance.domain.CmtsCmNum;
import com.topvision.ems.epon.performance.domain.PonCmNum;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

public interface EponCmListDao extends BaseEntityDao<Entity> {

    /**
     * 加载指定OLT下的PON口
     *
     * @param entityId
     * @return
     */
    List<OltPonAttribute> loadPonOfOlt(Long entityId);

    /**
     * 根据8800A的cmcId去获取其所在的PON口
     *
     * @param cmcId
     * @return
     */
    OltPonAttribute loadPonAttrByCmcId(Long cmcId);

    /**
     * 刷新epon下的关联CM信息
     *
     * @param entityId
     * @return
     */
    EponCmNumStatic getEponCmNumStatic(Long entityId);

    /**
     * 获取olt下所有Pon口下CM个数
     * @param entityId
     * @return
     */
    List<PonCmNum> getPonCmNumList(Long entityId);

    /**
     * 获取olt下所有Cmts下CM个数
     * @param entityId
     * @return
     */
    List<CmtsCmNum> getCmtsCmNumList(Long entityId);
}