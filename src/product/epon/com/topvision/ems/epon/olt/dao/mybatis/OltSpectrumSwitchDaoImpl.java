package com.topvision.ems.epon.olt.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.dao.OltSpectrumSwitchDao;
import com.topvision.ems.epon.olt.domain.OltSpectrumSwitch;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("oltSpectrumSwitchDao")
public class OltSpectrumSwitchDaoImpl extends MyBatisDaoSupport<Object> implements OltSpectrumSwitchDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.olt.domain.OltSpectrumSwitch";
    }

    @Override
    public void insertSpectrumOltSwitch(OltSpectrumSwitch oltSpectrumSwitch) {
        OltSpectrumSwitch os = getSqlSession().selectOne(getNameSpace("getOltSpectrumSwitch"),
                oltSpectrumSwitch.getEntityId());
        if (os != null) {
            getSqlSession().update(getNameSpace("updateOltSpectrumSwitch"), oltSpectrumSwitch);
        } else {
            getSqlSession().insert(getNameSpace("insertOltSpectrumSwitch"), oltSpectrumSwitch);
        }
    }

}
