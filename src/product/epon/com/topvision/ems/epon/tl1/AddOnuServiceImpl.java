/**
 * 
 */
package com.topvision.ems.epon.tl1;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.OnuInfo;
import com.topvision.nbi.tl1.api.exception.DDOFException;
import com.topvision.nbi.tl1.api.exception.IIPEException;
import com.topvision.nbi.tl1.api.exception.SENSException;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.ADD_ONU)
@Service("tl1AddOnuService")
public class AddOnuServiceImpl extends BaseService implements TL1ExecutorService<OnuInfo> {
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuService onuService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.nbi.tl1.service.TL1ExecutorService#execute(java.lang.Long, java.lang.Long,
     * java.lang.Object)
     */
    @Override
    public void execute(Long entityId, Long ponIndex, OnuInfo config) {
        String onuType = config.getOnuType();
        Integer preTypeId = EponConstants.EPON_ONU_PRETYPE.get(onuType);
        if (preTypeId == null) {
            // 没有对应的ONU类型,则抛出参数错误异常
            throw new IIPEException();
        }
        OltAuthentication oltAuthen = new OltAuthentication();
        long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);

        Long slotNo = EponIndex.getSlotNo(ponIndex);
        Long ponNo = EponIndex.getPonNo(ponIndex);
        // 若传递了OnuId，则使用传递值
        Integer onuNo = config.getOnuNo();
        if (null == onuNo) {
            List<OltOnuAttribute> preTypes = onuAuthService.getOnuPreTypeByPonId(ponId);

            // 寻找最小/最大可用算法： o(n)+o(n *log n)+o(n) = o(2n+n*log n)的时间复杂度
            AtomicInteger ai = new AtomicInteger(1);
            preTypes.stream().map(x -> EponIndex.getOnuNo(x.getOnuIndex())).sorted().anyMatch(x -> {
                // x == ai.get()?ai.incrementAngAge() && false : true
                if (x == ai.get()) {
                    ai.incrementAndGet();
                    return false;
                } else {
                    return true;
                }
            });
            int avaliable = ai.get();

            if (avaliable > EponConstants.OLT_PON_ONU_MAXNUM) {
                throw new DDOFException();
            }
            onuNo = avaliable;
        }
        Long onuIndex = EponIndex.getOnuIndex(slotNo.intValue(), ponNo.intValue(), onuNo);
        oltAuthen.setOnuIndex(onuIndex);
        oltAuthen.setEntityId(entityId);
        oltAuthen.setPonId(ponId);
        oltAuthen.setOnuPreType(preTypeId);
        oltAuthen.setAuthAction(EponConstants.OLT_AUTHEN_MACACTION_ACCEPT);
        if (TL1Constant.AUTH_TYPE_MAC.equals(config.getAuthType())) {
            oltAuthen.setAuthType(EponConstants.OLT_AUTHEN_MAC);
            oltAuthen.setOnuAuthenMacAddress(MacUtils.formatMac(config.getOnuId()));
        } else if (TL1Constant.AUTH_TYPE_LOID.equals(config.getAuthType())) {
            oltAuthen.setAuthType(EponConstants.OLT_AUTHEN_SN);
            oltAuthen.setTopOnuAuthLogicSn(config.getOnuId());
            if (null == config.getOnuPassword() || config.getOnuPassword().equals("")) {
                oltAuthen.setOnuSnMode(EponConstants.OLT_AUTHEN_PONMODE_SN);
            } else {
                oltAuthen.setOnuSnMode(EponConstants.OLT_AUTHEN_PONMODE_SNPWD);
                oltAuthen.setTopOnuAuthPassword(config.getOnuPassword());
            }
        } else {
            // LOIDONCEON暂不支持
            throw new SENSException();
        }
        onuAuthService.addOnuAuthenPreConfig(oltAuthen);
        onuAuthService.deleteOnuAuthBlock(ponId, onuIndex);
        onuAuthService.modifyOnuPreType(entityId, onuIndex, preTypeId.toString());
        discoveryService.refresh(entityId);
        try {
            if (config.getOnuName() != null) {
                Long onuId = onuService.getOnuIdByIndex(entityId, onuIndex);
                entityService.renameEntity(onuId, config.getOnuName());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        // ONU描述暂不支持    entity的sysName
    }
}
