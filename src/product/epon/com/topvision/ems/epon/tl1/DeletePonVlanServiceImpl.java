/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.vlan.service.PonLlidVlanService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.DelPortVlan;
import com.topvision.nbi.tl1.api.exception.EEEHException;

/**
 * @author Administrator
 * 
 */
@TL1CommandCode(code = TL1Constant.DEL_PONVLAN)
@Service("deletePonVlanService")
public class DeletePonVlanServiceImpl implements TL1ExecutorService<DelPortVlan> {

    @Autowired
    private OnuService onuService;
    @Autowired
    private PonLlidVlanService ponLlidVlanService;

    /**
     * 设置LLID VLAN透传，采用规避方案,直接配置pon口透传 2018.11.6歌华测试,要在LLID模式下删除配置的VLAN
     */
    @Override
    public void execute(Long entityId, Long onuIndex, DelPortVlan config) {
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

        Integer userVlan = config.getUserVlan();
        ponLlidVlanService.deleteLlidOnuQinQRule(entityId, onuId, userVlan, userVlan);

        /*
         * List<VlanAttribute> oltVlanConfigList = sniVlanService.getOltVlanConfigList(entityId);
         * List<Integer> l1 = new ArrayList<>(); List<Integer> l2 = new ArrayList<>(); for
         * (VlanAttribute vlanAttribute : oltVlanConfigList) { l1.add(vlanAttribute.getVlanIndex());
         * l2.add(VlanTransparentRule.UNTAG); }
         * 
         * Long ponIndex = EponIndex.getPonIndex(onuIndex); Long ponId =
         * oltPonService.getPonIdByIndex(entityId, ponIndex);
         * 
         * VlanTransparentRule re = new VlanTransparentRule(); re.setEntityId(entityId);
         * re.setPortId(ponId); re.setPortIndex(ponIndex); re.setTransparentIdList(l1);
         * re.setTransparentModeList(l2); ponPortVlanService.modifyTransparentRule(re);
         */

    }
}
