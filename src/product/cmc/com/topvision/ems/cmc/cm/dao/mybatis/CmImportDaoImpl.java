package com.topvision.ems.cmc.cm.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.dao.CmImportDao;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-上午9:03:57 从CmDaoImpl拆分
 * 
 */
@Repository("cmImportDao")
public class CmImportDaoImpl extends MyBatisDaoSupport<Entity> implements CmImportDao {
    
    private static final int BATCHSIZE = 1000;

    @Override
    public void batchInsertOrUpdateCmImportInfo(List<CmImportInfo> cmImportInfos) {
        // modify by fanzidong,优化插入性能,每一次插入BATCHSIZE条
        int batchNum = (cmImportInfos.size() / BATCHSIZE) + 1;
        for (int i = 0; i < batchNum; i++) {
            try{
                int start = i*BATCHSIZE;
                if(start > cmImportInfos.size()) {
                    break;
                }
                int end = (i+1)*BATCHSIZE;
                if(end > cmImportInfos.size()) {
                    end = cmImportInfos.size();
                }
                List<CmImportInfo> currentBatchInfos = cmImportInfos.subList(i*BATCHSIZE, end);
                getSqlSession().insert(getNameSpace("batchInsertCmImportInfo"), currentBatchInfos);
            }catch(Exception e) {
                logger.error("batchInsertOrUpdateCmImportInfo error: ", e);
            }
        }
    }

    @Override
    public void deleteCmImportInfo() {
        getSqlSession().delete(getNameSpace("deleteCmImportInfo"));
    }

    @Override
    public Long selectCmImportInfoNum(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("selectCmImportInfoNum"), map);
    }

    @Override
    public List<CmImportInfo> selectCmImportInfoList(Map<String, Object> map, Integer start, Integer limit) {
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("selectCmImportInfoList"), map);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cm.domain.CmImport";
    }

}
