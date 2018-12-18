package com.topvision.ems.network.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;

@Repository("entityAddressDao")
public class EntityAddressDaoImpl extends MyBatisDaoSupport<EntityAddress> implements EntityAddressDao {
    // Add by Victor@20170601增加缓存，selectByAddress通过第一次查询缓存，后面查询采用缓存数据
    private Map<String, EntityAddress> caches = new HashMap<>();

    @Override
    public void deleteByEntityId(Long entityId) {
        getSqlSession().update(getNameSpace() + "deleteByEntityId", entityId);
        caches.clear();
    }

    @Override
    public void insertOrUpdate(EntityAddress ea) {
        EntityAddress temp = selectByAddress(ea.getIp());
        if (temp == null) {
            insertEntity(ea);
        } else {
            updateEntity(ea);
        }
        caches.clear();
    }

    @Override
    public void insertOrUpdate(List<EntityAddress> eas) {
        List<EntityAddress> eas4Insert = new ArrayList<EntityAddress>();
        List<EntityAddress> eas4Update = new ArrayList<EntityAddress>();
        EntityAddress temp;
        for (EntityAddress ea : eas) {
            temp = selectByAddress(ea.getIp());
            if (temp == null) {
                eas4Insert.add(ea);
            } else {
                eas4Update.add(ea);
            }
        }

        if (eas4Insert.size() > 0) {
            insertEntityAddressByBatch(eas4Insert);
        }
        if (eas4Update.size() > 0) {
            updateEntityAddressByBatch(eas4Update);
        }
        caches.clear();
    }

    /**
     * 批量插入设备地址信息.
     * 
     * @param eas4Insert
     */
    private void insertEntityAddressByBatch(final List<EntityAddress> eas4Insert) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (EntityAddress ea : eas4Insert) {
                sqlSession.update(getNameSpace() + "insertEntity", ea);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 批量更新设备信息.
     * 
     * @param eas4Update
     */
    private void updateEntityAddressByBatch(final List<EntityAddress> eas4Update) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (EntityAddress ea : eas4Update) {
                sqlSession.update(getNameSpace() + "updateEntity", ea);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public EntityAddress selectByAddress(String ip) {
        if (caches.containsKey(ip)) {
            return caches.get(ip);
        }
        EntityAddress ea = null;
        try {
            ea = getSqlSession().selectOne(getNameSpace("selectByAddress"), ip);
        } catch (MyBatisSystemException e) {
            ea = (EntityAddress) (getSqlSession().selectList(getNameSpace("selectByAddress"), ip).get(0));
        }
        // 未查到也缓存null,主要用于类似trap处理时查询设备是否存在网管
        caches.put(ip, ea);
        return ea;
    }

    @Override
    public List<EntityAddress> selectByAddressList(String ip) {
        return getSqlSession().selectList(getNameSpace("selectByAddressList"), ip);
    }

    @Override
    public void updateAddress(Long entityId, String ip, String oldIp) {
        EntityAddress address = new EntityAddress();
        address.setEntityId(entityId);
        address.setIp(ip);
        address.setOldIp(oldIp);
        getSqlSession().update(getNameSpace() + "updateAddress", address);
        caches.clear();
    }

    @Override
    public void deleteEntityAddressWithoutEntityIp(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteEntityAddressWithoutEntityIp", entityId);
        caches.clear();
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.EntityAddress";
    }

    @Override
    public void clearCache() {
        synchronized (caches) {
            caches.clear();
        }
    }
}
