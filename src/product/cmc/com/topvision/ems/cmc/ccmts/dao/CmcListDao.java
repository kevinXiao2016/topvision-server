package com.topvision.ems.cmc.ccmts.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.EntityPonRelation;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmcListDao extends BaseEntityDao<CmcAttribute> {
    /**
     * 获取所有entity设备信息，用作选择设备下拉框
     * 
     * @return
     */
    List<EntityPonRelation> getEntityPonInfoList();

    /**
     * 根据条件查询cmc 8800b列表
     * @param cmcQueryMap
     * @return
     */
    List<CmcAttribute> queryCmc8800BList(Map<String, Object> cmcQueryMap);

    /**
     * 获取cmc 8800b数
     * 
     * @param cmcQueryMap
     *            cmc查询信息
     * @return Long cmc数
     */
    Long queryCmc8800BNum(Map<String, Object> cmcQueryMap);

    /**
     * 查询cmc列表
     * 
     * @param cmcQueryMap
     *            olt设备id
     * @param start
     *            pon口id
     * @param limit
     *            cmc设备mac地址
     * @return List<CmcAttribute> cmc列表信息
     */
    List<CmcAttribute> queryCmcList(Map<String, Object> cmcQueryMap, int start, int limit);

    /**
     * 查询CMC列表,专用于设备管理CMTS列表查询使用
     * @param cmcQueryMap
     * @return
     */
    List<CmcAttribute> queryForCmcList(Map<String, Object> cmcQueryMap);

    /**
     * 获取cmc数
     * 
     * @param cmcQueryMap
     *            olt设备id
     * @return Long cmc数
     */
    Long getCmcNum(Map<String, Object> cmcQueryMap);

    /**
     * 获取当前olt设备上的所有CC的nmName
     * @param entityId
     */
    public Map<Object, Object> getAllOnuIdToCmcNmnameMap(Long entityId);

    /**
     * 获取当前系统中所有CMTS设备
     * 
     */
    public List<CmcAttribute> getAllCmcEntityList();
}
