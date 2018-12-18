/***********************************************************************
 * $Id: IpqamRefreshDao.java,v1.0 2016年5月7日 下午2:58:15 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamProgram;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamStatus;
import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.ProgramIn;
import com.topvision.ems.cmc.ipqam.domain.ProgramOut;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2016年5月7日-下午2:58:15
 * 
 */
public interface IpqamRefreshDao extends BaseEntityDao<Ipqam> {
    /**
     * 插入类B型节目流信息，先删除后添加
     * 
     * @param cmcEqamProgramList
     * @param cmcId
     */
    void batchInsertCmcBEqamProgram(List<CmcEqamProgram> cmcEqamProgramList, Long cmcId);

    /**
     * 插入类B型输入节目流数据，先删除后添加
     * 
     * @param programInList
     * @param cmcId
     */
    void batchInsertCmcBProgramIn(List<ProgramIn> programInList, Long cmcId);

    /**
     * 插入类B型输出节目流数据，先删除后添加
     * 
     * @param programOutList
     * @param cmcId
     */
    void batchInsertCmcBProgramOut(List<ProgramOut> programOutList, Long cmcId);

    /**
     * 插入类B型EQAM信道信息，先删除后添加
     * 
     * @param cmcEqamStatusList
     * @param cmcId
     */
    void batchInsertCmcBIpqam(List<CmcEqamStatus> cmcEqamStatusList, Long cmcId);

    /**
     * 插入类A型节目流信息，先删除后添加
     * 
     * @param cmcEqamProgramList
     * @param cmcId
     * @param cmcIndex
     */
    void batchInsertCmcAEqamProgram(List<CmcEqamProgram> cmcEqamProgramList, Long cmcId, Long cmcIndex);

    /**
     * 插入类A型输入节目流数据，先删除后添加
     * 
     * @param programOutList
     * @param cmcId
     * @param cmcIndex
     */
    void batchInsertCmcAProgramIn(List<ProgramIn> programInList, Long cmcId, Long cmcIndex);

    /**
     * 插入类A型输出节目流数据，先删除后添加
     * 
     * @param programOutList
     * @param cmcId
     * @param cmcIndex
     */
    void batchInsertCmcAProgramOut(List<ProgramOut> programOutList, Long cmcId, Long cmcIndex);

    /**
     * 插入类A型EQAM信道信息，先删除后添加
     * 
     * @param cmcEqamStatusList
     * @param cmcId
     * @param cmcIndex
     */
    void batchInsertCmcAIpqam(List<CmcEqamStatus> cmcEqamStatusList, Long cmcId, Long cmcIndex);

    /**
     * 根据信道index和cmcId获取portId
     * 
     * @param map
     * @return
     */
    Long getCmcPortIdByIfIndexAndCmcId(Map<String, Long> map);

    /**
     * 根据索引和entityId 获取cmcId
     * 
     * @param map
     * @return
     */
    Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map);

    /**
     * http方式的ipqam信道存入数据库
     * @param cc8800bHttpFpgaSpecification
     * @param cmcId
     */
    void batchInsertCC8800BFPGAInfo(CmcFpgaSpecification cc8800bHttpFpgaSpecification, Long cmcId);
}
