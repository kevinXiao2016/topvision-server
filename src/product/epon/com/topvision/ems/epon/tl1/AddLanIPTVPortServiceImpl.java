/**
 * 
 */
package com.topvision.ems.epon.tl1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.service.OnuIgmpConfigService;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.LanIPTVPortInfo;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.ADD_LAN_IPTVPORT)
@Service("addLanIPTVPortService")
public class AddLanIPTVPortServiceImpl implements TL1ExecutorService<LanIPTVPortInfo> {
    @Autowired
    private UniDao uniDao;
    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private OnuIgmpConfigService onuIgmpConfigService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long,
     * java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long index, LanIPTVPortInfo config) {
        /*
         * Long uniId = uniDao.getUniIdByIndex(entityId, index); VlanTranslationRule
         * vlanTranslationRule = new VlanTranslationRule();
         * vlanTranslationRule.setEntityId(entityId);
         * vlanTranslationRule.setVlanIndex(config.getUserVlan());
         * vlanTranslationRule.setTranslationNewVid(config.getMvlan());
         * vlanTranslationRule.setPortId(uniId);
         * uniVlanService.addVlanTranslationRule(vlanTranslationRule);
         */

        IgmpUniConfig igmpUniConfig = onuIgmpConfigService.getIgmpUniConfig(entityId, index);
        List<Integer> uniVlanArray = igmpUniConfig.getUniVlanArray();
        uniVlanArray.remove(new Integer(0));
        Iterator<Integer> iterator = uniVlanArray.iterator();

        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value.equals(0)) {
                iterator.remove();
            }
        }
        // 解决同一个VLAN口多次保存
        if (!uniVlanArray.contains(config.getMvlan())) {
            uniVlanArray.add(config.getMvlan());
        }
        Collections.sort(uniVlanArray);
        igmpUniConfig.setUniVlanArray(uniVlanArray);
        onuIgmpConfigService.modifyIgmpUniConfig(igmpUniConfig);
    }
}
