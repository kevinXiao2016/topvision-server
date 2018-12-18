/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.DelOnuInfo;
import com.topvision.nbi.tl1.api.exception.IRNEException;
import com.topvision.nbi.tl1.api.exception.SENSException;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.DEL_ONU)
@Service("deleteOnuService")
public class DeleteOnuServiceImpl implements TL1ExecutorService<DelOnuInfo> {

    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OltPonDao oltPonDao;

    @Autowired
    private OnuAuthService onuAuthService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long,
     * java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long ponIndex, DelOnuInfo config) {
        long onuIndex = 0l;
        Integer ponAuthMode;
        if (TL1Constant.AUTH_TYPE_MAC.equals(config.getOnuIdType())) {
            OltOnuAttribute onuAttribute = onuDao.getOnuAttributeByMac(entityId, MacUtils.formatMac(config.getOnuId()));
            if (onuAttribute == null) {
                throw new IRNEException();
            }
            long onuId = onuAttribute.getOnuId();
            onuIndex = onuDao.getOnuIndex(onuId);
            ponAuthMode = EponConstants.OLT_AUTHEN_MAC;
        } else if (TL1Constant.AUTH_TYPE_LOID.equals(config.getOnuIdType())) {
            OltOnuAttribute onuAttribute = onuDao.getOnuAttributeByLoid(entityId, config.getOnuId());
            if (onuAttribute == null) {
                throw new IRNEException();
            }
            long onuId = onuAttribute.getOnuId();
            onuIndex = onuDao.getOnuIndex(onuId);
            ponAuthMode = EponConstants.OLT_AUTHEN_SN;
        } else {
            throw new SENSException();
        }

        Long ponIndexLocal = EponIndex.getPonIndex(onuIndex);
        if (!ponIndexLocal.equals(ponIndex)) {
            throw new IRNEException();
        }
        Long ponId = oltPonDao.getPonIdByPonIndex(entityId, ponIndex);
        // Integer ponAuthMode = onuAuthDao.getPonOnuAuthMode(entityId, index);
        // ponAuthMode = EponConstants.OLT_AUTHEN_MAC;
        onuAuthService.deleteAuthenPreConfig(entityId, ponId, onuIndex, ponAuthMode);
    }
}
