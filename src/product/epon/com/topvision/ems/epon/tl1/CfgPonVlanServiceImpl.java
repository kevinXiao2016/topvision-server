/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.service.PonLlidVlanService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.PortVlanInfo;
import com.topvision.nbi.tl1.api.exception.EEEHException;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.CFG_PON_VLAN)
@Service("tl1CfgPonVlan")
public class CfgPonVlanServiceImpl implements TL1ExecutorService<PortVlanInfo> {

    @Autowired
    private OnuService onuService;
    @Autowired
    private PonLlidVlanService ponLlidVlanService;
    @Autowired
    private OltPonService oltPonService;

    @Override
    public void execute(Long entityId, Long onuIndex, PortVlanInfo config) {
        Long onuId = null;
        boolean error = false;
        try {
            onuId = onuService.getOnuIdByIndex(entityId, onuIndex);
        } catch (Exception e) {
            error = true;
        }
        if (error) {
            throw new EEEHException();
        }
        OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
        Long ponIndex = EponIndex.getPonIndex(onuIndex);
        Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);

        VlanLlidOnuQinQRule onuQinQRule = new VlanLlidOnuQinQRule();
        onuQinQRule.setPonId(ponId);
        onuQinQRule.setEntityId(entityId);
        onuQinQRule.setPonIndex(ponIndex);
        onuQinQRule.setMac(onuAttribute.getOnuMac());
        onuQinQRule.setSlotNo(EponIndex.getSlotNo(onuIndex));
        onuQinQRule.setPortNo(EponIndex.getPonNo(onuIndex));
        onuQinQRule.setOnuNo(EponIndex.getOnuNo(onuIndex));
        onuQinQRule.setTopOnuQinQStartVlanId(config.getInnerVlan());
        onuQinQRule.setTopOnuQinQEndVlanId(config.getInnerVlan());
        onuQinQRule.setTopOnuQinQSVlanId(config.getOuterVlan());
        ponLlidVlanService.addLlidOnuQinQRule(onuQinQRule);

    }

}
