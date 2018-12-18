package com.topvision.ems.mobile.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.mobile.dao.MConfigDao;
import com.topvision.ems.mobile.domain.MobileDeviceType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("mConfigDao")
public class MConfigDaoImpl extends MyBatisDaoSupport<MobileDeviceType> implements MConfigDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        // TODO Auto-generated method stub
        return "com.topvision.ems.mobile.domain.MobileDeviceType";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.dao.MConfigDao#getMobileDeviceTypeList()
     */
    @Override
    public List<MobileDeviceType> getMobileDeviceTypeList() {
        return getSqlSession().selectList(getNameSpace("getMobileDeviceTypeList"));
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.dao.MConfigDao#getMobileDeviceTypeList()
     */
    @Override
    public MobileDeviceType getMobileDeviceTypeByTypeId(Long typeId) {
        return getSqlSession().selectOne(getNameSpace("getMobileDeviceTypeByTypeId"), typeId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.dao.MConfigDao#modifyMobileDeviceType()
     */
    @Override
    public void modifyMobileDeviceType(Long typeId, String frequency, String powerlevel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        map.put("frequency", frequency);
        map.put("powerlevel", powerlevel);
        getSqlSession().update(getNameSpace("modifyMobileDeviceType"), map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.dao.MConfigDao#modifyMobileDeviceType()
     */
    @Override
    public void addMobileDeviceType(String deviceType, String corporation, String frequency, String powerlevel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deviceType", deviceType);
        map.put("corporation", corporation);
        map.put("frequency", frequency);
        map.put("powerlevel", powerlevel);
        getSqlSession().insert(getNameSpace("addMobileDeviceType"), map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.dao.MConfigDao#delMobileDeviceType()
     */
    @Override
    public void delMobileDeviceType(Long typeId) {
        SqlSession sqlSession = getSqlSession();
        sqlSession.delete(getNameSpace("delMobileDeviceType"), typeId);
        sqlSession.delete(getNameSpace("delDefaultMobileDeviceType"), typeId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.dao.MConfigDao#setDefaultMobileDeviceType()
     */
    @Override
    public void setDefaultMobileDeviceType(Long typeId) {
        SqlSession sqlSession = getSqlSession();
        Integer count = sqlSession.selectOne(getNameSpace("isDefaultExist"));
        if (count == 0) {
            sqlSession.update(getNameSpace("insertDefaultMobileDeviceType"), typeId);
        } else {
            sqlSession.update(getNameSpace("updateDefaultMobileDeviceType"), typeId);
        }
    }
}
