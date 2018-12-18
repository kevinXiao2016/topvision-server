package com.topvision.ems.cmc.topology.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author bryan
 * @created @2012-7-2-上午11:25:43
 * 
 * @param <T>
 */
public class CmcRefreshCC8800ASnmpWorker<T extends CmcDiscoveryData> extends BaseCmcRefreshCC8800ASnmpWorker<T> {

    private static final long serialVersionUID = 6604815692258403612L;

    public CmcRefreshCC8800ASnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    @Override
    protected void exec() throws Exception {
        CmcAttribute cmcAttribute = result.getCmcAttributes().get(0);
        Long cmcIndex = cmcAttribute.getCmcIndex();
        result.getCmcIndexs().add(cmcIndex);
        logger.info("begin to refresh cmc({})...", cmcIndex);
        snmpUtil.reset(snmpParam);
        super.exec();

        List<CmAttribute> cmAttributes = new ArrayList<CmAttribute>();
        CmAttribute cmAttribute = new CmAttribute();
        Long nextCmcIndex = CmcIndexUtils.getNextCmcIndex(cmcIndex);
        try {
            cmAttributes = snmpUtil.getTableRangeLines(cmAttribute, cmcIndex, nextCmcIndex);
            result.setCmAttributes(cmAttributes);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology cmAttributes finished!");

        try {
            List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatus = new ArrayList<DocsIf3CmtsCmUsStatus>();
            for (CmAttribute cm_tmp : cmAttributes) {
                DocsIf3CmtsCmUsStatus status = new DocsIf3CmtsCmUsStatus();
                status.setCmRegStatusId(cm_tmp.getStatusIndex());
                status.setCmUsStatusChIfIndex(cm_tmp.getStatusUpChannelIfIndex());
                status = snmpUtil.getTableLine(status);
                docsIf3CmtsCmUsStatus.add(status);
            }
            result.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatus);
        } catch (Exception e1) {
            logger.error("", e1);
        }
        logger.info("topology docsIf3CmtsCmUsStatus finished!");

    }

}
