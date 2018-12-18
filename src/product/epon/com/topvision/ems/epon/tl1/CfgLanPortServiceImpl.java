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
import com.topvision.ems.epon.onu.service.ElectricityOnuService;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.tl1.TL1CommandCode;
import com.topvision.ems.tl1.TL1ExecutorService;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.nbi.tl1.api.TL1Constant;
import com.topvision.nbi.tl1.api.domain.LanProperty;
import com.topvision.nbi.tl1.api.exception.IIPEException;

/**
 * @author Administrator
 *         CFG-LANPORT::OLTID=10.1.9.2,PONID=1-1-2-8,ONUIDTYPE=LOID,ONUID=9000001172,ONUPORT=1-1-1-1
 *         :CTAG::VLANMOD=TAG,PVID=500,PCOS=5;
 */
@TL1CommandCode(code = TL1Constant.CFG_LANPORT)
@Service("cfgLanPortService")
public class CfgLanPortServiceImpl implements TL1ExecutorService<LanProperty> {

    private Logger logger = LoggerFactory.getLogger(CfgLanPortServiceImpl.class);

    @Autowired
    private UniDao uniDao;
    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private ElectricityOnuService electricityOnuService;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;

    @Override
    public void execute(Long entityId, Long uniIndex, LanProperty config) {
        Long uniId = uniDao.getUniIdByIndex(entityId, uniIndex);
        // 删除即设置透传,根据刘俊的意见,不直接设置，而是绑定透传模板
        String vlanMode = config.getVlanMode();
        if (null != vlanMode) {
            if (!PortVlanAttribute.vlanModeProfileMap.containsKey(vlanMode)) {
                throw new IIPEException();// 模式关键字错误
            }
            Integer profileId = null;
            Integer profileMode = PortVlanAttribute.vlanModeProfileMap.get(vlanMode);
            List<UniVlanProfileTable> profileList = uniVlanProfileService.getProfileList(entityId);
            for (UniVlanProfileTable profile : profileList) {
                if (profileMode.equals(profile.getProfileMode())) {
                    profileId = profile.getProfileId();
                    break;
                }
            }
            if (null == profileId) {
                // 说明设备没有建立对应的 vlan模板，此时建立UNI VLAN模板
                profileId = profileList.size() + 1;
                UniVlanProfileTable profile = new UniVlanProfileTable();
                profile.setEntityId(entityId);
                profile.setProfileId(profileId);
                profile.setProfileName("vlan-profile-" + profileId);
                profile.setProfileMode(profileMode);
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

        // 设置pvid
        if (config.getPvid() != null) {
            PortVlanAttribute uniVlan = new PortVlanAttribute();
            uniVlan.setEntityId(entityId);
            uniVlan.setPortIndex(uniIndex);
            uniVlan.setPortId(uniId);
            uniVlan.setVlanPVid(config.getPvid());
            // 记录操作结果
            try {
                uniVlanService.updateUniVlanAttribute(uniVlan);
            } catch (SnmpSetException e) {
                logger.error("Modify Uni[{}] Vlan mode failed : {}", uniId, e);
            }
        }

        // 设置优先级
        /*
         * if (config.getPortCos() != null) { electricityOnuService.setUniUSUtgPri(entityId, uniId,
         * config.getPortCos()); }
         */
    }

}
