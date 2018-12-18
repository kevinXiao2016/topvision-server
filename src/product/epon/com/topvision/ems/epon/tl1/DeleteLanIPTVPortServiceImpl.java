/**
 * 
 */
package com.topvision.ems.epon.tl1;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.service.OnuIgmpConfigService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.LanIPTVPortInfo;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.DEL_LAN_IPTVPORT)
@Service("deleteLanIPTVPortService")
public class DeleteLanIPTVPortServiceImpl implements TL1ExecutorService<LanIPTVPortInfo> {
    @Autowired
    private OnuIgmpConfigService onuIgmpConfigService;

    /* (non-Javadoc)
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long, java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long index, LanIPTVPortInfo config) {
        IgmpUniConfig igmpUniConfig = onuIgmpConfigService.getIgmpUniConfig(entityId, index);
        List<Integer> uniVlanArray = igmpUniConfig.getUniVlanArray();
        if (config.getMvlan() == null) {
            uniVlanArray.clear();
        } else {
            Iterator<Integer> iterator = uniVlanArray.iterator();
            while (iterator.hasNext()) {
                int mvlanId = iterator.next();
                if (mvlanId == config.getMvlan()) {
                    iterator.remove();
                }
            }
        }
        igmpUniConfig.setUniVlanArray(uniVlanArray);
        onuIgmpConfigService.modifyIgmpUniConfig(igmpUniConfig);
    }
}
