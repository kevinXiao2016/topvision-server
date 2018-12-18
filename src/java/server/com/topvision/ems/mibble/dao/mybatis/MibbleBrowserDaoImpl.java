package com.topvision.ems.mibble.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.mibble.dao.MibbleBrowserDao;
import com.topvision.ems.mibble.domain.MibbleMessage;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author Bravin
 * 
 */
@Repository("mibbleBrowserDao")
public class MibbleBrowserDaoImpl extends MyBatisDaoSupport<MibbleMessage> implements MibbleBrowserDao {
    @Override
    public String getDomainName() {
        return "com.topvision.ems.mibble.domain.MibbleBrowser";
    }

    public List<String> loadMibbles(long userId) {
        return getSqlSession().selectList(getNameSpace("loadMibbles"), userId);
    }

    public void saveSelectedMib(final String[] mibs, final long userId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteMib", userId);
            for (String mib : mibs) {
                if (!"".equals(mib)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userId", userId + "");
                    map.put("mib", mib);
                    session.insert(getNameSpace() + "inserMib", map);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }
}
