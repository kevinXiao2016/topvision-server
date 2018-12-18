package com.topvision.ems.mobile.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.mobile.dao.MUIOnuDao;
import com.topvision.ems.mobile.domain.MUIOnu;
import com.topvision.ems.mobile.domain.OnuAroundInfo;
import com.topvision.ems.mobile.domain.OnuHealthyThreshold;
import com.topvision.ems.mobile.domain.OnuOpenReport;
import com.topvision.ems.mobile.domain.OnuPreconfigInfo;
import com.topvision.ems.mobile.domain.PageView;
import com.topvision.ems.mobile.domain.VisitView;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
@Repository("mUIOnuDao")
public class MUIOnuDaoImpl extends MyBatisDaoSupport<Object> implements MUIOnuDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.mobile.domain.MUIOnu";
    }

    @Override
    public List<OnuPreconfigInfo> getOnuPreconfigInfo() {
        return getSqlSession().selectList(getNameSpace("queryAllPreconfigInfo"));
    }

    @Override
    public List<OnuOpenReport> getOnuOpenReport() {
        return getSqlSession().selectList(getNameSpace("queryAllOpenReport"));
    }

    @Override
    public void clearPreconfig() {
        this.getSqlSession().delete(getNameSpace("clearTablePreconfig"));
    }

    @Override
    public void savePreconfigList(List<OnuPreconfigInfo> preconfigInfosFromDB) {
        this.getSqlSession().insert(getNameSpace("savePreconfigList"), preconfigInfosFromDB);
    }

    @Override
    public void clearOpenreport() {
        this.getSqlSession().delete(getNameSpace("clearTableOpenreport"));
    }

    @Override
    public void saveOpenreportList(List<OnuOpenReport> onuOpenReportsFromDB) {
        SqlSession session = getBatchSession();
        try {
            for (OnuOpenReport onuOpenReport : onuOpenReportsFromDB) {
                session.insert(getNameSpace("saveOpenreportList"), onuOpenReport);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<MUIOnu> selectOnuList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectOnuList"), queryMap);
    }

    @Override
    public OltOnuAttribute selectOnlineOnuAttributeByUniqueId(String uniqueId) {
        return getSqlSession().selectOne(getNameSpace("selectOnlineOnuAttr"), uniqueId);
    }

    @Override
    public void saveVisitView(List<VisitView> visitViewList) {
        SqlSession session = getBatchSession();
        try {
            for (VisitView visitView : visitViewList) {
                session.insert(getNameSpace("saveVisitView"), visitView);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    
    @Override
    public List<OltOnuAttribute> selectAllOnuAttributeByUniqueId(String uniqueId) {
        return getSqlSession().selectList(getNameSpace("selectAllOnuAttr"), uniqueId);
    }
    
    @Override
    public List<OnuAroundInfo> selectOnuAroundInfo(){
        return getSqlSession().selectList(getNameSpace("selectOnuAroundInfo"));
    }
    
    public List<OnuHealthyThreshold> selectOnuHeathyThreshold(){
        return getSqlSession().selectList(getNameSpace("selectOnuThreshold"));
    }
    @Override
    public void savePageView(List<PageView> pageViewList) {
        SqlSession session = getBatchSession();
        try {
            for (PageView pageView : pageViewList) {
                session.insert(getNameSpace("savePageView"), pageView);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdatePreconfigInfo(List<OnuPreconfigInfo> onuPreconfigInfos) {
        SqlSession session = getBatchSession();
        try {
            for (OnuPreconfigInfo preconfigInfo : onuPreconfigInfos) {
                session.insert(getNameSpace("batchInsertOrUpdatePreconfigInfo"), preconfigInfo);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOpenreport(List<OnuOpenReport> onuOpenreportList) {
        SqlSession session = getBatchSession();
        try {
            for (OnuOpenReport openReport : onuOpenreportList) {
                session.insert(getNameSpace("batchInsertOrUpdateOpenreport"), openReport);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }


}
