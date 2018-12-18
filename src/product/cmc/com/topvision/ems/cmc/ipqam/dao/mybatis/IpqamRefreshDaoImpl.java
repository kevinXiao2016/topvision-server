/***********************************************************************
 * $Id: IpqamRefreshDaoImpl.java,v1.0 2016年5月7日 下午2:58:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.ipqam.dao.IpqamRefreshDao;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamProgram;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamStatus;
import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.ProgramIn;
import com.topvision.ems.cmc.ipqam.domain.ProgramOut;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.exception.dao.DaoException;

/**
 * @author loyal
 * @created @2016年5月7日-下午2:58:45
 * 
 */
@Repository("ipqamRefreshDao")
public class IpqamRefreshDaoImpl extends MyBatisDaoSupport<Ipqam> implements IpqamRefreshDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.ipqam.domain.IpqamRefresh";
    }

    @Override
    public void batchInsertCmcBEqamProgram(List<CmcEqamProgram> cmcEqamProgramList, Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCmcEqamProgram", cmcId);
            for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                map.put("ifIndex",
                        CmcIndexUtils.getChannelIndexFromEqamIndex(cmcEqamProgram.getMpegVideoSessionIndex()));
                cmcEqamProgram.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                cmcEqamProgram.setCmcId(cmcId);
                session.insert(getNameSpace() + "insertCmcEqamProgram", cmcEqamProgram);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcBEqamProgram error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcBIpqam(List<CmcEqamStatus> cmcEqamStatusList, Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCmcEqamStatus", cmcId);
            for (CmcEqamStatus cmcEqamStatus : cmcEqamStatusList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                map.put("ifIndex", cmcEqamStatus.getIfIndex());
                cmcEqamStatus.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                cmcEqamStatus.setCmcId(cmcId);
                session.insert(getNameSpace() + "insertCmcEqamStatus", cmcEqamStatus);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcBIpqam error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcBProgramIn(List<ProgramIn> programInList, Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCmcProgramIn", cmcId);
            for (ProgramIn programIn : programInList) {
                //只有正在推送的才显示节目信息
                if(programIn.getMpegInputUdpOriginationPacketsDetected() == 1L){
                    Map<String, Long> map = new HashMap<String, Long>();
                    map.put("cmcId", cmcId);
                    map.put("ifIndex",
                            CmcIndexUtils.getChannelIndexFromEqamIndex(programIn.getMpegInputUdpOriginationIndex()));
                    programIn.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                    programIn.setCmcId(cmcId);
                    programIn.setSessionId(
                            CmcIndexUtils.getSessionIdFromSessionIndex(programIn.getMpegInputUdpOriginationIndex()));
                    session.insert(getNameSpace() + "insertCmcProgramIn", programIn);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcBProgramIn error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcBProgramOut(List<ProgramOut> programOutList, Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCmcProgramOut", cmcId);
            for (ProgramOut programOut : programOutList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                map.put("ifIndex", programOut.getMpegOutputTSIndex());
                programOut.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                programOut.setCmcId(cmcId);
                programOut.setSessionId(programOut.getMpegOutputProgIndex());
                session.insert(getNameSpace() + "insertCmcProgramOut", programOut);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcBProgramOut error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcAEqamProgram(List<CmcEqamProgram> cmcEqamProgramList, Long entityId, Long cmcIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("entityId", entityId);
            queryMap.put("cmcIndex", cmcIndex);
            session.delete(getNameSpace() + "deleteOltEqamProgram", queryMap);
            for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", entityId);
                Long ifIndex = CmcIndexUtils.getChannelIndexFromEqamIndex(cmcEqamProgram.getMpegVideoSessionIndex());
                map.put("ifIndex", ifIndex);
                map.put("cmcIndex", CmcIndexUtils.getCmcIndexFromChannelIndex(ifIndex));
                cmcEqamProgram.setCmcId(getCmcIdByCmcIndexAndEntityId(map));
                map.put("cmcId", cmcEqamProgram.getCmcId());
                cmcEqamProgram.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                session.insert(getNameSpace() + "insertCmcEqamProgram", cmcEqamProgram);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcAEqamProgram error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcAProgramIn(List<ProgramIn> programInList, Long entityId, Long cmcIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("entityId", entityId);
            queryMap.put("cmcIndex", cmcIndex);
            session.delete(getNameSpace() + "deleteOltProgramIn", queryMap);
            for (ProgramIn programIn : programInList) {
              //只有正在推送的才显示节目信息
                if(programIn.getMpegInputUdpOriginationPacketsDetected() == 1L){
                    Map<String, Long> map = new HashMap<String, Long>();
                    map.put("entityId", entityId);
                    map.put("ifIndex",
                            CmcIndexUtils.getChannelIndexFromEqamIndex(programIn.getMpegInputUdpOriginationIndex()));
                    map.put("cmcIndex",
                            CmcIndexUtils.getCmcIndexFromChannelIndex(programIn.getMpegInputUdpOriginationIndex()));
                    programIn.setCmcId(getCmcIdByCmcIndexAndEntityId(map));
                    map.put("cmcId", programIn.getCmcId());
                    programIn.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                    programIn.setSessionId(
                            CmcIndexUtils.getSessionIdFromSessionIndex(programIn.getMpegInputUdpOriginationIndex()));
    
                    // add by fanzidong， IP地址由16进制转为10进制
                    programIn.setMpegInputUdpOriginationDestInetAddr(
                            IpUtils.formatInetAddress(programIn.getMpegInputUdpOriginationDestInetAddr()));
                    programIn.setMpegInputUdpOriginationSrcInetAddr(
                            IpUtils.formatInetAddress(programIn.getMpegInputUdpOriginationSrcInetAddr()));
    
                    session.insert(getNameSpace() + "insertCmcProgramIn", programIn);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcAProgramIn error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcAIpqam(List<CmcEqamStatus> cmcEqamStatusList, Long entityId, Long cmcIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("entityId", entityId);
            queryMap.put("cmcIndex", cmcIndex);
            session.delete(getNameSpace() + "deleteOltEqamStatus", queryMap);
            for (CmcEqamStatus cmcEqamStatus : cmcEqamStatusList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", entityId);
                map.put("ifIndex", cmcEqamStatus.getIfIndex());
                map.put("cmcIndex", CmcIndexUtils.getCmcIndexFromChannelIndex(cmcEqamStatus.getIfIndex()));
                cmcEqamStatus.setCmcId(getCmcIdByCmcIndexAndEntityId(map));
                map.put("cmcId", cmcEqamStatus.getCmcId());
                cmcEqamStatus.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                session.insert(getNameSpace() + "insertCmcEqamStatus", cmcEqamStatus);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcAIpqam error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcAProgramOut(List<ProgramOut> programOutList, Long entityId, Long cmcIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("entityId", entityId);
            queryMap.put("cmcIndex", cmcIndex);
            session.delete(getNameSpace() + "deleteOltProgramOut", queryMap);
            for (ProgramOut programOut : programOutList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", entityId);
                map.put("ifIndex", programOut.getMpegOutputTSIndex());
                map.put("cmcIndex", CmcIndexUtils.getCmcIndexFromChannelIndex(programOut.getMpegOutputTSIndex()));
                programOut.setCmcId(getCmcIdByCmcIndexAndEntityId(map));
                map.put("cmcId", programOut.getCmcId());
                programOut.setPortId(getCmcPortIdByIfIndexAndCmcId(map));
                programOut.setSessionId(programOut.getMpegOutputProgIndex());
                session.insert(getNameSpace() + "insertCmcProgramOut", programOut);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertCmcAProgramOut error", e);
            session.rollback();
            throw new DaoException();
        } finally {
            session.close();
        }
    }

    @Override
    public Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcIdByCmcIndexAndEntityId", map);
    }

    @Override
    public Long getCmcPortIdByIfIndexAndCmcId(Map<String, Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcPortIdByIfIndexAndCmcId", map);
    }

    @Override
    public void batchInsertCC8800BFPGAInfo(CmcFpgaSpecification cc8800bHttpFpgaSpecification, Long cmcId) {
        SqlSession session = getSqlSession();
        cc8800bHttpFpgaSpecification.setCmcId(cmcId);
        session.delete(getNameSpace() + "deleteCC8800BFpgaSpecification", cmcId);
        session.insert(getNameSpace() + "insertCC8800BFpgaSpecification", cc8800bHttpFpgaSpecification);
    }

}
