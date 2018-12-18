/***********************************************************************
 * $Id: OnuCpeStatusDBSaver.java,v1.0 2012-7-17 上午10:33:10 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.onucpe.facade.OnuCpeUtil;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuCpeIpInfo;
import com.topvision.ems.epon.performance.domain.OnuCpeStatusPerfResult;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.ems.epon.performance.engine.OnuPerfDao;
import com.topvision.ems.epon.performance.exception.DataFormatErrorException;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2012-7-17-上午10:33:10
 * 
 * @modify by Rod 性能采集优化与重构
 * 
 */
@Engine("onuCpeStatusDBSaver")
public class OnuCpeStatusDBSaver extends BaseEngine implements PerfEngineSaver<OnuCpeStatusPerfResult, OperClass> {
    private final Logger logger = LoggerFactory.getLogger(OnuCpeStatusDBSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(OnuCpeStatusPerfResult onuCpeStatusPerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("OnuCpeStatusDBSaver identifyKey[" + onuCpeStatusPerfResult.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        OnuPerfDao onuPerfDao = engineDaoFactory.getEngineDao(OnuPerfDao.class);
        Long entityId = onuCpeStatusPerfResult.getEntityId();
        if (onuCpeStatusPerfResult != null) {
            onuPerfDao.removeOnuCpeListByEntityId(entityId);
            onuPerfDao.removeOnuCpeCountByEntityId(entityId);
            if (!onuCpeStatusPerfResult.getArrayEmpty()) {
                List<OnuUniCpeCount> onuUniCpeCounts = new ArrayList<>();
                for (OnuUniCpeList onuUniCpeList : onuCpeStatusPerfResult.getOnuCpes()) {
                    try {
                        String dhcpIpInfoTotal = onuUniCpeList.getDhcpIpInfoTotal();
                        List<OnuCpeIpInfo> list = null;
                        if (dhcpIpInfoTotal != null) {
                            list = OnuCpeUtil.parseOnuCpeIpInfoList(dhcpIpInfoTotal);
                        }
                        List<OnuCpe> onuCpes = OnuCpeUtil.makeOnuCpeList(onuUniCpeList, entityId, list);
                        OnuUniCpeCount onuUniCpeCount = OnuCpeUtil.makeOnuUniCpeCount(onuUniCpeList, onuCpes);
                        onuUniCpeCount.setEntityId(entityId);
                        onuUniCpeCounts.add(onuUniCpeCount);
                        //由于一个OLT下的CPE过多，插入CPE是以UNI口为单位来处理的
                        onuPerfDao.batchInsertOnuCpeList(onuCpes);
                    } catch (DataFormatErrorException e) {
                        logger.debug("uniMacInfoTotal length:" + onuUniCpeList.getUniMacInfoTotal().length(), e);
                    }
                }
                //统计UNI口下的CPE个数是以OLT为单位来处理的
                onuPerfDao.batchInsertOnuCpeCount(onuUniCpeCounts);
            }
        }
    }
}
