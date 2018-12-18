/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.service.OnuReplaceService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.framework.common.MacUtils;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.ConfigOnuAuth;
import com.topvision.nbi.tl1.api.exception.DDNSException;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.CFG_ONU)
@Service("cfgOnuAuthService")
public class CfgOnuAuthServiceImpl implements TL1ExecutorService<ConfigOnuAuth> {

    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OnuReplaceService onuReplaceService;

    /* (non-Javadoc)
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long, java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long index, ConfigOnuAuth config) {
        Long onuId = onuDao.getOnuIdByIndex(entityId, index);
        if (TL1Constant.AUTH_TYPE_MAC.equals(config.getAuthType())) {
            onuReplaceService.replaceOnuEntityByMac(entityId, onuId, index, MacUtils.formatMac(config.getOnuId()), 1);
        } else {
            throw new DDNSException();
        }
    }
}
