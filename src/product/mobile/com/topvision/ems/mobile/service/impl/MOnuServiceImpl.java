/***********************************************************************
 * $Id: MOnuServiceImpl.java,v1.0 2016年7月16日 下午4:21:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.mobile.dao.MOnuDao;
import com.topvision.ems.mobile.domain.MobileOnu;
import com.topvision.ems.mobile.service.MOnuService;
import com.topvision.ems.mobile.util.MobileUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016年7月16日-下午4:21:08
 *
 */
@Service("mOnuService")
public class MOnuServiceImpl extends BaseService implements MOnuService {

    @Autowired
    private OnuService onuService;
    @Autowired
    private MOnuDao mOnuDao;
    private OltOnuAttribute onuAttribute;

    @Override
    public MobileOnu getOnuBaseInfo(Long onuId) {
        MobileOnu mOnu = mOnuDao.queryOnuBaseInfo(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if (mOnu != null) {
            Long currentTime = System.currentTimeMillis();
            Long uptime = (currentTime - mOnu.getChangeTime().getTime()) / 1000 + mOnu.getOnuTimeSinceLastRegister();
            String location = EponIndex.getOnuStringByIndex(mOnu.getOnuIndex()).toString();
            mOnu.setLocation(location);
            mOnu.setSysUpTime(uptime);
            mOnu.setSysUpTimeString(MobileUtil.convertSysUpTime(uptime));
            if(GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())){
                mOnu.setEorG("G");
                mOnu.setGponSN(onuAttribute.getOnuUniqueIdentification());
                mOnu.setAdminStatus(onuAttribute.getOnuAdminStatus());
                mOnu.setOnuTestDistance(onuAttribute.getOnuTestDistance());
            }else{
                mOnu.setEorG("E");
                String llidStr = "0x" + Integer.toHexString(mOnu.getOnuLlid());
                mOnu.setOnuLlidStr(llidStr);
            }
        }
        return mOnu;
    }

    @Override
    public OltOnuCapability getOnuCapabilityInfo(Long onuId) {
        return onuService.getOltOnuCapabilityByOnuId(onuId);
    }

    @Override
    public Long getOnuPonIndex(Long entityId, Long onuId) {
        return mOnuDao.getOnuPonIndex(entityId, onuId);
    }

    @Override
    public OltPonOptical getOnuLinkPonOptical(Long onuId) {
        return mOnuDao.getOnuLinkPonOptical(onuId);
    }

}
