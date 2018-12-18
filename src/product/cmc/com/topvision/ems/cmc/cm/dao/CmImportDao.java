package com.topvision.ems.cmc.cm.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-上午9:02:29 从CmDao拆分
 * 
 */
public interface CmImportDao extends BaseEntityDao<Entity> {
    /**
     * 获取CM导入信息
     * 
     * @param map
     *            查询条件
     * @param start
     * @param limit
     * @return List<CmImportInfo> 返回CM导入信息列表
     */
    List<CmImportInfo> selectCmImportInfoList(Map<String, Object> map, Integer start, Integer limit);

    /**
     * 获取CM导入信息数量
     * 
     * @param map
     * @return
     */
    Long selectCmImportInfoNum(Map<String, Object> map);

    /**
     * 删除导入的CM信息
     */
    void deleteCmImportInfo();

    /**
     * 批量插入/更新CM导入信息
     * 
     * @param cmImportInfos
     */
    void batchInsertOrUpdateCmImportInfo(List<CmImportInfo> cmImportInfos);
}
