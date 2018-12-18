/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.epon.qos.service.OltQosService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.OnuBandWidth;
import com.topvision.nbi.tl1.api.exception.IIPEException;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.CFG_ONUBW)
@Service("cfgOnuBandService")
public class CfgOnuBandServiceImpl implements TL1ExecutorService<OnuBandWidth> {

    @Autowired
    private OltQosService oltQosService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long,
     * java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long index, OnuBandWidth config) {
        SlaTable slaTable = new SlaTable();
        slaTable.setEntityId(entityId);
        slaTable.setOnuIndex(index);
        try {
            slaTable.setSlaDsCommittedBW(Long.valueOf(config.getDownBandwidth()));
            slaTable.setSlaUsCommittedBW(Long.valueOf(config.getUpBandwidth()));
        } catch (Exception e) {
            throw new IIPEException();
        }
        oltQosService.modifyOnuSlaList(slaTable);

    }

}
