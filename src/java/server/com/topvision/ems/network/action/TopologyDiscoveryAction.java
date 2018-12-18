package com.topvision.ems.network.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.ems.network.domain.Services;
import com.topvision.ems.network.service.BfsxService;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.TopologyManager;
import com.topvision.ems.network.service.TopologyParamService;
import com.topvision.exception.service.MacConflictException;
import com.topvision.exception.service.NetworkException;
import com.topvision.exception.service.UnknownEntityTypeException;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.domain.CommonResponse;
import com.topvision.framework.domain.StatusCode;
import com.topvision.framework.exception.EntityRefreshException;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONObject;

@Controller("topologyDiscoveryAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopologyDiscoveryAction extends BaseAction {
    private static final long serialVersionUID = 5986806752053233307L;
    private final Logger logger = LoggerFactory.getLogger(TopologyDiscoveryAction.class);
    @Autowired
    private TopologyParamService topoParamService;
    @Autowired
    private BfsxService bfsxService;
    private TopologyManager topologyMgr;
    private TopologyParam topoParam;
    private List<Services> services;
    private boolean discoverying = false;
    // 当前拓扑使用者
    private String topologyUser;
    private long entityId;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;
    private Integer operationResult;

    /**
     * 刷新设备
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "topologyDiscoveryAction", operationName = "discoveryEntityAgain")
    public String discoveryEntityAgain() throws Exception {
        CommonResponse cr = new CommonResponse();
        try {
            logger.info("TopologyDiscoveryAction begin to Topology entityId:" + entityId);
            discoveryService.refresh(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            cr.setCode(StatusCode.InternalServerError.getCode());
            cr.setMsg(formatErrorMessage(e));
            logger.error("discoveryEntityAgain error: {}", e);
            operationResult = OperationLog.FAILURE;
        } finally {
            writeDataToAjax(cr);
        }
        logger.info("TopologyDiscoveryAction finish Topology entityId:" + entityId);
        return NONE;
    }

    private String formatErrorMessage(Exception e) {
        if (e instanceof PingException) {
            return getString("NETWORK.cannotConnectEntity");
        } else if (e instanceof MacConflictException) {
            return getString("NETWORK.MacConflict");
        } else if (e instanceof NetworkException) {
            return getString("NETWORK.snmpConnectFail");
        } else if (e instanceof UnknownEntityTypeException) {
            return getString("NETWORK.unknownEntityType");
        } else if (e instanceof EntityRefreshException) {
            return getString("NETWORK.reTopoEntity");
        } else {
            return getString("NETWORK.reTopoEr");
        }
    }

    private String getString(String key) {
        return ResourceManager.getResourceManager("com.topvision.ems.network.resources").getNotNullString(key);
    }

    /**
     * 刷新资源列表中的设备可见信息
     * 
     * @return
     */
    public String refreshEntity() {
        bfsxService.refreshEntity(entityId);
        return NONE;
    }

    public String getDiscoveryProgress() throws Exception {
        JSONObject json = new JSONObject();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.network.resources");
        try {
            json.put("progress", TopologyContext.getInstance().getProgress());
            json.put("msg", TopologyContext.getInstance().getAllMsg());
        } catch (Exception ex) {
            json.put("msg", resourceManager.getNotNullString("topology.progress.error"));
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String getDiscoveryState() throws Exception {
        JSONObject json = new JSONObject();
        try {
            json.put("state", TopologyContext.getInstance().getTopologyState() != TopologyContext.RUNNING);
        } catch (Exception ex) {
            json.put("state", false);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String showTopoDiscoveryGuide() {
        discoverying = (TopologyContext.getInstance().getTopologyState() == TopologyContext.RUNNING);
        topologyUser = TopologyContext.getInstance().getUsername();
        topoParam = topoParamService.txGetTopologyParam();

        return SUCCESS;
    }

    /**
     * 开始拓扑发现.
     * 
     * @return
     * @throws Exception
     */
    public String startTopologyDiscovery() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        topoParamService.updateParam(topoParam);
        // 注册拓扑消息
        TopologyContext tc = TopologyContext.getInstance();
        tc.setUsername(uc.getUser().getUserName());
        tc.reset();
        topologyMgr.removeTopologyListener(tc);
        topologyMgr.addTopologyListener(tc);
        topologyMgr.topology();
        return NONE;
    }

    public long getEntityId() {
        return entityId;
    }

    public List<Services> getServices() {
        return services;
    }

    public String getTopologyUser() {
        return topologyUser;
    }

    public TopologyParam getTopoParam() {
        return topoParam;
    }

    public boolean isDiscoverying() {
        return discoverying;
    }

    public void setDiscoverying(boolean discoverying) {
        this.discoverying = discoverying;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public void setTopologyMgr(TopologyManager topologyMgr) {
        this.topologyMgr = topologyMgr;
    }

    public void setTopologyUser(String topologyOwner) {
        this.topologyUser = topologyOwner;
    }

    public void setTopoParam(TopologyParam topoParam) {
        this.topoParam = topoParam;
    }

    public void setTopoParamService(TopologyParamService topoParamService) {
        this.topoParamService = topoParamService;
    }

    public DiscoveryService<DiscoveryData> getDiscoveryService() {
        return discoveryService;
    }

    public void setDiscoveryService(DiscoveryService<DiscoveryData> discoveryService) {
        this.discoveryService = discoveryService;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

}
