/**
 * 
 */
package com.topvision.ems.epon.tl1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.service.UniService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.nbi.tl1.api.TL1Constant;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.ACTIVATE_LANPORT)
@Service("actLanPortService")
public class ActLanPortServiceImpl implements TL1ExecutorService<Object> {

    @Autowired
    private UniService uniService;
    @Autowired
    private UniDao uniDao;

    /* (non-Javadoc)
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long, java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long uniIndex, Object config) {
        Long uniId = uniDao.getUniIdByIndex(entityId, uniIndex);
        uniService.setUniAdminStatus(entityId, uniId, EponConstants.PORT_STATUS_UP);
    }

}
