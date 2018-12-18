/***********************************************************************
 * $Id: PnmpCmDataServiceImpl.java,v1.0 2017年8月9日 上午9:56:12 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.topvision.ems.cm.pnmp.facade.PnmpPollFacade;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.dao.PnmpCmDataDao;
import com.topvision.ems.cm.pnmp.facade.domain.CorrelationGroup;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateEvent;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateListener;
import com.topvision.ems.cm.pnmp.service.PnmpCmDataService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;

import javax.annotation.Resource;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午9:56:12
 *
 */
@Service("pnmpCmDataService")
public class PnmpCmDataServiceImpl extends CmcBaseCommonService implements PnmpCmDataService, PnmpPollStateListener,
        BeanFactoryAware {

    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private PnmpCmDataDao pnmpCmDataDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;
    @Autowired
    private PingExecutorService pingExecutorService;
    private BeanFactory beanFactory;
    private ThreadPoolExecutor threadPoolExecutor;
    @Value("${CorrelationGroup.maxPoolsize:2}")
    private Integer maxPoolsize;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(PnmpPollStateListener.class, this);
        threadPoolExecutor = new ThreadPoolExecutor(maxPoolsize, maxPoolsize, maxPoolsize, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxPoolsize * 10));

    }

    @Override
    public void getCorrelationGroupsByCmcId(Long cmcId) {
    }

    @Override
    public List<PnmpCmData> getDataByGroup(Map<String, Object> queryMap) {
        return pnmpCmDataDao.selectDataByGroup(queryMap);
    }

    @Override
    public void insertData(PnmpCmData data) {
    }

    @Override
    public List<PnmpCmData> getHistoryData(Map<String, Object> queryMap) {
        return pnmpCmDataDao.selectHistoryData(queryMap);
    }

    @Override
    public List<String> getSpectrumResponsesByMac(List<String> cmMacList) {
        return pnmpCmDataDao.selectSpectrumResponsesByMac(cmMacList);
    }

    @Override
    public Long getMaxUpChannelWidthByCmcId(Long cmcId) {
        return pnmpCmDataDao.selectMaxUpChannelWidthByCmcId(cmcId);
    }

    @Override
    public List<CorrelationGroup> getCorrelationGroup(Long cmcId) {
        return pnmpCmDataDao.selectCorrelationGroup(cmcId);
    }

    @Override
    public String pingDocsis(Long cmtsId, String cmMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmtsId);

        String ip = new IpUtils(snmpParam.getIpAddress()).toString();
        TelnetLogin telnetLogin;
        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }

        CcmtsTelnetUtil telnetUtil = null;
        boolean pingCheck = checkPingReachable(ip);
        if (!pingCheck) {
            return "% CMTS is not reachable";
        }
        try {
            telnetUtil = (CcmtsTelnetUtil) telnetUtilFactory.getCcmtsTelnetUtil(ip);
            telnetUtil.setPrompt("#,>,:,[n],(yes/no)?", "");
            telnetUtil.connect(ip);
            if (telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),
                    telnetLogin.getIsAAA())) {
                return telnetUtil.execCmd("ping docsis " + cmMac);
            } else {
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
                return "% Telnet login error";
            }
        } catch (Exception e) {
            if (telnetUtil != null) {
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            }
            return "% Telnet error " + e.getMessage();
        }
    }

    @Override
    public String moveCmChannel(Long cmtsId, Long channelId, String cmMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmtsId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmtsId);
        String location = CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                + CmcIndexUtils.getCmcId(cmcIndex).toString();

        String ip = new IpUtils(snmpParam.getIpAddress()).toString();
        TelnetLogin telnetLogin;
        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }
        CcmtsTelnetUtil telnetUtil = null;
        boolean pingCheck = checkPingReachable(ip);
        if (!pingCheck) {
            return "% CMTS is not reachable";
        }
        try {
            telnetUtil = (CcmtsTelnetUtil) telnetUtilFactory.getCcmtsTelnetUtil(ip);
            telnetUtil.setPrompt("#,>,:,[n],(yes/no)?", "");
            telnetUtil.connect(ip);
            if (telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),
                    telnetLogin.getIsAAA())) {
                StringBuilder sb = new StringBuilder();
                sb.append(telnetUtil.execCmd("configure terminal"));
                sb.append(telnetUtil.execCmd("interface ccmts " + location));
                String re = telnetUtil.execCmd("cable upstream move cm " + cmMac + " to " + channelId);
                sb.append(re);
                logger.trace(sb.toString());
                return re;

            } else {
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
                return "% Telnet login error";
            }
        } catch (Exception e) {
            if (telnetUtil != null) {
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            }
            return "% Telnet error " + e.getMessage();
        }
    }

    @Override
    public PnmpCmData realPnmp(Long cmtsId, String cmMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmtsId);

        SnmpParam cmSnmpParam = new SnmpParam();
        cmSnmpParam.setCommunity(getParamsStringValue("cmcTerminal", "cmReadCommunity", "public"));
        cmSnmpParam.setWriteCommunity(getParamsStringValue("cmcTerminal", "cmWriteCommunity", "private"));
        cmSnmpParam.setTimeout(5000L);
        cmSnmpParam.setRetry((byte) 2);
        return getPnmpPollFacade(snmpParam.getIpAddress()).realPnmp(cmtsId,snmpParam,cmSnmpParam, cmMac);
    }

    private String getParams(String module, String name) {
        SystemPreferences perferences = systemPreferencesDao.selectByModuleAndName(module, name);
        if (perferences != null) {
            return perferences.getValue();
        } else {
            return null;
        }
    }

    private String getParamsStringValue(String module, String name, String defaultString) {
        String str = getParams(module, name);
        if (str != null) {
            return str;
        } else {
            return defaultString;
        }
    }
    @Override
    public void completeRoundStatistics(PnmpPollStateEvent event) {
        logger.info("PnmpCmDataServiceImpl.completeRoundStatistics event:" + event);
        if (event.getType().equalsIgnoreCase(PnmpPollStateEvent.MIDDLE)
                || event.getType().equalsIgnoreCase(PnmpPollStateEvent.HIGH)) {
            try {
                PnmpCorrelationGroupThreadPool pnmpCorrelationGroupThreadPool = (PnmpCorrelationGroupThreadPool) beanFactory
                        .getBean("pnmpCorrelationGroupThreadPool");
                pnmpCorrelationGroupThreadPool.setType(event.getType());
                threadPoolExecutor.execute(pnmpCorrelationGroupThreadPool);
            } catch (Exception e) {
                logger.debug("PnmpCmDataServiceImpl.completeRoundStatistics.Exception", e);
            }
        }
    }

    @Override
    public void startRoundStatistics(PnmpPollStateEvent event) {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public PnmpCmData getDataByGroupForMobile(Map<String, Object> queryMap) {
        return pnmpCmDataDao.selectDataByGroupForMobile(queryMap);
    }


    private PnmpPollFacade getPnmpPollFacade(String ip) {
        return facadeFactory.getFacade(ip, PnmpPollFacade.class);
    }

    public boolean checkPingReachable(String ip) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        // TODO 需要使用数据库配置来进行控制
        // worker.setCount(count);
        // worker.setTimeout(timeout);
        try {
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            return f.get().available();
        } catch (InterruptedException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (ExecutionException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (Exception e) {
            logger.debug("checkPingReachable" + ip, e);
        }
        return false;
    }
}
