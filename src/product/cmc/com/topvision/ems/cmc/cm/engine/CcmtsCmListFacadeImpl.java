package com.topvision.ems.cmc.cm.engine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.ccmts.domain.CmcClearCmOnTime;
import com.topvision.ems.cmc.cm.facade.CcmtsCmListFacade;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStatusForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannelForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQualityForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannelForContactCmc;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-下午8:28:55 从CmFacdeImpl拆分,CM级联列表
 * 
 */
@Facade("ccmtsCmListFacade")
public class CcmtsCmListFacadeImpl extends EmsFacade implements CcmtsCmListFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    @Override
    public void restartCmByCcmts(SnmpParam snmpParam, Long cmIndex) {
        snmpExecutorService.set(snmpParam, CmcConstants.TOPCMCTLDROPMODEM + Symbol.POINT + cmIndex, "1");
    }

    @Override
    public void restartAllCm(SnmpParam snmpParam, Long cmcIndex) {
        snmpExecutorService.set(snmpParam, CmcConstants.TOPCCMTSCMRESET + Symbol.POINT + cmcIndex, "1");
    }

    @Override
    public List<DocsIfSignalQualityForContactCmc> getDocsIfSignalQualityForContactCmc(SnmpParam snmpParam, String cmIp) {
        return snmpExecutorService.getTable(snmpParam, DocsIfSignalQualityForContactCmc.class);
    }

    @Override
    public List<DocsIfDownstreamChannelForContactCmc> getDocsIfDownstreamForContactCmc(SnmpParam snmpParam, String cmIp) {
        return snmpExecutorService.getTable(snmpParam, DocsIfDownstreamChannelForContactCmc.class);
    }

    @Override
    public List<DocsIfUpstreamChannelForContactCmc> getDocsIfUpstreamForContactCmc(SnmpParam snmpParam, String cmIp) {
        return snmpExecutorService.getTable(snmpParam, DocsIfUpstreamChannelForContactCmc.class);
    }

    @Override
    public CmStatusForContactCmc showCmStatusForContactCmc(SnmpParam snmpParam, String cmIp) {
        return snmpExecutorService.getData(snmpParam, CmStatusForContactCmc.class);
    }

    @Override
    public List<CmFlap> getCmFlap(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmFlap.class);
    }

    @Override
    public void clearAllOfflineCmsOnCC(SnmpParam snmpParam, Long cmcIndex) {
        snmpExecutorService.set(snmpParam, CmcConstants.TOPCCMTSCMOFFLINECLEAR + Symbol.POINT + cmcIndex, "1");
    }

    // CC刷新重构后的-单独对某个CC上的CM刷新
    @Override
    public List<CmAttribute> refreshContactedCmList(SnmpParam snmpParam, Long cmcIndex) {
        SnmpUtil snmpUtil = new SnmpUtil(snmpParam);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
            Date startTime = new Date();
            logger.info("**************startTime: " + sdf.format(startTime)
                    + "***********************************************");
            List<CmAttribute> cmAttrList = new ArrayList<CmAttribute>();
            String[] indexList = snmpUtil.getList(CmcConstants.DOCSIFCMTSCMSTATUSINDEX);// cmtscmstatusIndex
            int useCount = 1;
            for (String ix : indexList) {
                CmAttribute cmAttribute = new CmAttribute();
                Long index = Long.parseLong(ix);
                if (CmcIndexUtils.getCmcIndexFromCmIndex(index) == cmcIndex.longValue()) {
                    cmAttribute.setStatusIndex(index);
                    cmAttribute = snmpExecutorService.getTableLine(snmpParam, cmAttribute);
                    cmAttrList.add(cmAttribute);
                    logger.info("useCount size: " + useCount++);
                } else if (CmcIndexUtils.getCmcIndexFromCmIndex(index) > cmcIndex.longValue()) {
                    return cmAttrList;
                }
            }
            logger.info("CM info length:" + cmAttrList.size());
            Date endTime = new Date();
            logger.info("****************endTime: " + sdf.format(endTime)
                    + "**********************************************");
            return cmAttrList;
        } finally {
            try {
                snmpUtil.close();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void clearOnTimeCmOfCC(SnmpParam snmpParam, Integer time) {
        CmcClearCmOnTime cmcClearCmOnTime = new CmcClearCmOnTime();
        cmcClearCmOnTime.setCmcClearMode(1);
        cmcClearCmOnTime.setCmcClearTime(time);
        snmpExecutorService.setData(snmpParam, cmcClearCmOnTime);
    }
    @Override
    public CmcClearCmOnTime getCmcClearTime(SnmpParam snmpParam){
        return snmpExecutorService.getData(snmpParam, CmcClearCmOnTime.class);
    }
    
}
