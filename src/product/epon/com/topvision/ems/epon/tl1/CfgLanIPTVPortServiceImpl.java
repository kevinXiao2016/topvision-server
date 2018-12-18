/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.stereotype.Service;

import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.LanIPTVPortInfo;
import com.topvision.nbi.tl1.api.exception.DDNSException;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.CFG_LAN_IPTVPORT)
@Service("cfgLanIPTVPortService")
public class CfgLanIPTVPortServiceImpl implements TL1ExecutorService<LanIPTVPortInfo> {

    /* (non-Javadoc)
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long, java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long index, LanIPTVPortInfo config) {
        throw new DDNSException();
    }

}
