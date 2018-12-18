/**
 * 
 */
package com.topvision.ems.epon.tl1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.vlan.domain.UniPortVlan;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.service.PonLlidVlanService;
import com.topvision.ems.epon.vlan.service.VlanListService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.TL1QueryService;
import com.topvision.nbi.tl1.api.domain.BoardInfo;
import com.topvision.nbi.tl1.api.domain.LocationMerger;
import com.topvision.nbi.tl1.api.domain.OnuInfo;
import com.topvision.nbi.tl1.api.domain.OnuLocation;
import com.topvision.nbi.tl1.api.domain.PortVlanInfo;
import com.topvision.nbi.tl1.api.domain.User;
import com.topvision.nbi.tl1.api.exception.DDNSException;
import com.topvision.nbi.tl1.api.exception.IRNEException;
import com.topvision.platform.service.UserService;

/**
 * @author Administrator
 *
 */
@Service("tl1QueryService")
public class TL1QueryServiceImpl implements TL1QueryService {
    private static final Logger logger = LoggerFactory.getLogger(TL1QueryServiceImpl.class);
    @Autowired
    private UserService userService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OltSlotDao oltSlotDao;

    @Override
    public Long getOnuIndex(String oltIp, Long ponIndex, String authType, String onuId) {
        if (!isPonExist(oltIp, ponIndex)) {
            throw new IRNEException();
        }
        Long onuIndex = null;
        if (TL1Constant.AUTH_TYPE_MAC.equals(authType)) {
            onuIndex = getOnuIndexByMac(oltIp, ponIndex, onuId);
        } else if (TL1Constant.AUTH_TYPE_LOID.equals(authType)) {
            onuIndex = getOnuIndexByLoid(oltIp, ponIndex, onuId);
        } else {
            throw new DDNSException();
        }

        if (onuIndex == null) {
            throw new IRNEException();
        }
        if (!ponIndex.equals(EponIndex.getPonIndex(onuIndex))) {
            throw new IRNEException();
        }

        return onuIndex;
    }

    /**
     * @param oltIp
     * @param ponIndex
     * @param onuId
     * @return
     */
    private Long getOnuIndexByLoid(String oltIp, Long ponIndex, String onuId) {
        OltOnuAttribute onuAttribute = getOnuAttributeByLoid(oltIp, ponIndex, onuId);
        if (null == onuAttribute) {
            throw new IRNEException();
        }
        return onuService.getOnuAttribute(onuAttribute.getOnuId()).getOnuIndex();
    }

    /**
     * @param oltIp
     * @param ponIndex
     * @param onuId
     * @return
     */
    private Long getOnuIndexByMac(String oltIp, Long ponIndex, String onuId) {
        OltOnuAttribute onuAttribute = getOnuAttributeByMac(oltIp, ponIndex, onuId);
        if (null == onuAttribute) {
            throw new IRNEException();
        }
        return onuService.getOnuAttribute(onuAttribute.getOnuId()).getOnuIndex();
    }

    public OltOnuAttribute getOnuAttribute(String oltIp, Long ponIndex, String authType, String onuId) {
        if (!isPonExist(oltIp, ponIndex)) {
            throw new IRNEException();
        }
        OltOnuAttribute onuAttribute = null;
        if (TL1Constant.AUTH_TYPE_MAC.equals(authType)) {
            onuAttribute = getOnuAttributeByMac(oltIp, ponIndex, onuId);
        } else if (TL1Constant.AUTH_TYPE_LOID.equals(authType)) {
            onuAttribute = getOnuAttributeByLoid(oltIp, ponIndex, onuId);
        } else {
            throw new DDNSException();
        }
        if (onuAttribute == null) {
            throw new IRNEException();
        }
        return onuAttribute;
    }

    private OltOnuAttribute getOnuAttributeByLoid(String oltIp, Long ponIndex, String onuId) {
        Entity entity = entityService.getEntityByIp(oltIp);
        return onuService.getOnuAttributeByLoid(entity.getEntityId(), onuId);
    }

    private OltOnuAttribute getOnuAttributeByMac(String oltIp, Long ponIndex, String onuId) {
        String macaddress = MacUtils.formatMac(onuId);
        Entity entity = entityService.getEntityByIp(oltIp);
        return onuService.getOnuAttributeByMac(entity.getEntityId(), macaddress);
    }

    @Override
    public boolean isPonExist(String oltIp, Long ponIndex) {
        Entity entity = entityService.getEntityByIp(oltIp);
        if (entity == null) {
            return false;
        }
        Long ponId = oltPonService.getPonIdByIndex(entity.getEntityId(), ponIndex);
        return ponId != null;
    }

    @Override
    public boolean isUserExist(String username, String password) {
        try {
            userService.txLogin(username, password, null);
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }

    @Override
    public User isUserExist(User user) {
        try {
            userService.txLogin(user.getUsername(), user.getPassword(), null);
            return user;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    @Override
    public String getSid() {
        return null;
    }

    @Override
    public <T> List<T> selectList(Class<T> arg0, String arg1) {
        return null;
    }

    @Override
    public <T> T selectOne(Class<T> arg0, String arg1) {
        return null;
    }

    @Override
    public OnuInfo findOnu(OnuLocation onuLocation) {
        Long ponIndex = LocationMerger.getPonIndex((onuLocation.getPonId()));
        Long onuIndex = getOnuIndex(onuLocation.getOltName(), ponIndex, onuLocation.getOnuIdType(),
                onuLocation.getOnuId());
        Entity olt = entityService.getEntityByIp(onuLocation.getOltName());

        OnuInfo onuInfo = onuService.getOnuTl1InfoByIndex(olt.getEntityId(), onuIndex);
        onuInfo.setOnuNo(EponIndex.getOnuNo(onuIndex).intValue());
        onuInfo.setPonId(onuLocation.getPonId());
        onuInfo.setOltId(onuLocation.getOltName());
        String onuType = EponConstants.EPON_ONU_TYPE.get(Integer.valueOf(onuInfo.getOnuPreType()));
        onuInfo.setOnuType(onuType);
        if (EponConstants.OLT_AUTHEN_MAC.equals(Integer.valueOf(onuInfo.getAuthType()))) {
            onuInfo.setAuthType(TL1Constant.AUTH_TYPE_MAC);
        } else if (EponConstants.OLT_AUTHEN_SN.equals(Integer.valueOf(onuInfo.getAuthType()))) {
            onuInfo.setAuthType(TL1Constant.AUTH_TYPE_LOID);
        }
        return onuInfo;
    }

    @Override
    public List<BoardInfo> findBoardByOltIp(String ip) {
        Entity entity = entityService.getEntityByIp(ip);
        if (entity == null || !entityTypeService.isOlt(entity.getTypeId())) {
            throw new IRNEException();
        }

        Map<Long, Integer> ponMap = new HashMap<>();
        List<OltPonAttribute> oltPonAttributes = oltPonDao.getPonListByEntityId(entity.getEntityId());
        for (OltPonAttribute oltPonAttribute : oltPonAttributes) {
            Long slotId = oltPonAttribute.getSlotId();
            if (ponMap.containsKey(slotId)) {
                Integer num = ponMap.get(slotId);
                ponMap.put(slotId, ++num);
            } else {
                ponMap.put(slotId, 1);
            }
        }
        List<BoardInfo> slots = oltSlotDao.selectSlotListForTl1(entity.getEntityId());
        for (BoardInfo boardInfo : slots) {
            boardInfo.setPnum(ponMap.get(boardInfo.getSlotId()));
            if (EponConstants.GPON_BOARD_TYPE.contains(boardInfo.getBoardType())) {
                boardInfo.setBservice("GPON");
            } else if (EponConstants.EPON_BOARD_TYPE.contains(boardInfo.getBoardType())) {
                boardInfo.setBservice("EPON");
            } else {
                boardInfo.setBservice("Other");
            }
        }
        return slots;
    }

    @Autowired
    private VlanListService vlanListService;
    @Autowired
    private PonLlidVlanService ponLlidVlanService;

    @Override
    public List<PortVlanInfo> findOnuPortVlan(OnuLocation onuLocation) {
        Long ponIndex = LocationMerger.getPonIndex((onuLocation.getPonId()));
        OltOnuAttribute onuAttribute = getOnuAttribute(onuLocation.getOltName(), ponIndex, onuLocation.getOnuIdType(),
                onuLocation.getOnuId());
        Long slotNo = EponIndex.getSlotNo(onuAttribute.getOnuIndex());
        Long ponNo = EponIndex.getPonNo(onuAttribute.getOnuIndex());
        String[] split = onuLocation.getPonId().split("-");
        String ponIdStr = split[0] + "-" + split[1] + "-" + slotNo + "-" + ponNo;
        
        Long entityId = onuAttribute.getEntityId();
        Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);
        // 封装CVlan与SVlan的对应关系
        Map<Integer, Integer> svlanMap = new HashMap<>();
        List<VlanLlidQinQRule> llidQinQRule = ponLlidVlanService.getLlidQinQList(ponId, onuAttribute.getOnuMac());
        for (VlanLlidQinQRule vlanLlidQinQRule : llidQinQRule) {
            Integer topLqVlanStartCVid = vlanLlidQinQRule.getTopLqVlanStartCVid();
            Integer topLqVlanEndCVid = vlanLlidQinQRule.getTopLqVlanEndCVid();
            Integer topLqVlanSVlan = vlanLlidQinQRule.getTopLqVlanSVlan();
            for (int cvlan = topLqVlanStartCVid; cvlan <= topLqVlanEndCVid; cvlan++) {
                svlanMap.put(cvlan, topLqVlanSVlan);
            }
        }
        // 构造RMI传递对象
        List<UniPortVlan> list = vlanListService.loadUniPortVlan(onuAttribute.getOnuId());
        List<PortVlanInfo> vlanInfoList = new ArrayList<>();
        for (UniPortVlan uniPortVlan : list) {
            PortVlanInfo vlanInfo = new PortVlanInfo();
            vlanInfo.setOltId(onuLocation.getOltName());
            vlanInfo.setPonId(ponIdStr);
            vlanInfo.setOnuid(onuAttribute.getOnuMac().replace(":", ""));
            vlanInfo.setOnuPort(EponIndex.getUniNo(uniPortVlan.getUniIndex()).intValue());// UniNo
            vlanInfo.setInnerVlan(uniPortVlan.getVlanPVid());// CVLAN
            vlanInfo.setOuterVlan(svlanMap.get(uniPortVlan.getVlanPVid()));// SVLAN
            vlanInfo.setUserVlan(uniPortVlan.getVlanPVid());
            vlanInfoList.add(vlanInfo);
        }
        return vlanInfoList;
    }

}
