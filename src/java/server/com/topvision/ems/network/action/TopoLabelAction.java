package com.topvision.ems.network.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("topoLabelAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopoLabelAction extends BaseAction {
    private static final long serialVersionUID = 1567239943902633623L;
    private List<TopoLabel> flowLabels;
    private List<TopoLabel> rateLabels;
    private List<TopoLabel> cpuLabels;
    private List<TopoLabel> memLabels;
    private long folderId;
    private TopoFolder topoFolder;
    @Autowired
    private TopologyService topologyService;

    public String showTopoLabelJsp() {
        topoFolder = topologyService.getTopoFolder(folderId);
        flowLabels = topologyService.getTopoLabel(folderId, TopoLabel.TYPE_LINKFLOW);
        rateLabels = topologyService.getTopoLabel(folderId, TopoLabel.TYPE_LINKRATE);
        cpuLabels = topologyService.getTopoLabel(folderId, TopoLabel.TYPE_CPU);
        memLabels = topologyService.getTopoLabel(folderId, TopoLabel.TYPE_MEM);
        return SUCCESS;
    }

    public List<TopoLabel> getCpuLabels() {
        return cpuLabels;
    }

    public List<TopoLabel> getFlowLabels() {
        return flowLabels;
    }

    public long getFolderId() {
        return folderId;
    }

    public List<TopoLabel> getMemLabels() {
        return memLabels;
    }

    public List<TopoLabel> getRateLabels() {
        return rateLabels;
    }

    public TopoFolder getTopoFolder() {
        return topoFolder;
    }

    public void setCpuLabels(List<TopoLabel> cpuLabels) {
        this.cpuLabels = cpuLabels;
    }

    public void setFlowLabels(List<TopoLabel> flowLabels) {
        this.flowLabels = flowLabels;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setMemLabels(List<TopoLabel> memLabels) {
        this.memLabels = memLabels;
    }

    public void setRateLabels(List<TopoLabel> rateLabels) {
        this.rateLabels = rateLabels;
    }

    public void setTopoFolder(TopoFolder topoFolder) {
        this.topoFolder = topoFolder;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

}
