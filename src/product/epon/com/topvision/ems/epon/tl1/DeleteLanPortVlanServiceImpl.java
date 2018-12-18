/**
 * 
 */
package com.topvision.ems.epon.tl1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.DelPortVlan;

/**
 * @author Administrator
 *
 */
@TL1CommandCode(code = TL1Constant.DEL_LAN_PORTVLAN)
@Service("deleteLanPortVlanService")
public class DeleteLanPortVlanServiceImpl implements TL1ExecutorService<DelPortVlan> {

    private Logger logger = LoggerFactory.getLogger(DeleteLanPortVlanServiceImpl.class);

    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;

    @Override
    public void execute(Long entityId, Long uniIndex, DelPortVlan config) {
        Long uniId = uniDao.getUniIdByIndex(entityId, uniIndex);
        // 删除即设置透传,根据刘俊的意见,不直接设置，而是绑定透传模板
        Integer profileId = null;
        List<UniVlanProfileTable> profileList = uniVlanProfileService.getProfileList(entityId);
        for (UniVlanProfileTable profile : profileList) {
            if (PortVlanAttribute.PROFILE_TRANSPARENT.equals(profile.getProfileMode())) {
                profileId = profile.getProfileId();
                break;
            }
        }

        if (null == profileId) {
            // 说明设备没有建立UNI VLAN 透传模板
            profileId = profileList.size() + 1;
            UniVlanProfileTable profile = new UniVlanProfileTable();
            profile.setEntityId(entityId);
            profile.setProfileId(profileId);
            profile.setProfileName("vlan-profile-" + profileId);
            profile.setProfileMode(PortVlanAttribute.PROFILE_TRANSPARENT);
            uniVlanProfileService.addProfile(profile);
        }

        UniVlanBindTable uniBindInfo = uniVlanProfileService.getUniVlanBindInfo(entityId, uniIndex);

        UniVlanBindTable uniBindInfoSet = new UniVlanBindTable();
        uniBindInfoSet.setEntityId(entityId);
        uniBindInfoSet.setUniIndex(uniIndex);
        uniBindInfoSet.setBindProfileId(profileId);
        uniBindInfoSet.setBindProfAttr(uniBindInfo.getBindProfAttr());

        uniVlanProfileService.replaceBindProfile(uniBindInfoSet);
        // 修改绑定成功后刷新UNI绑定信息
        uniVlanProfileService.refreshUniVlanInfo(entityId, uniIndex);
        try {
            // TODO 同步UNI端口数据
            uniVlanService.refreshSingleUniVlanAttribute(entityId, uniId);
        } catch (Exception e) {
            logger.error("Sync Uni[{}] Vlan Data failed:{}", uniId, e);
        }
    }

}
