package com.topvision.ems.network.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.RunProcess;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.resources.domain.Software;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("entityAssetAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityAssetAction extends BaseAction {
    private static final long serialVersionUID = 162580350068127608L;
    private long entityId;
    private List<Software> softwares;
    private Entity entity;
    @Autowired
    private EntityService entityService;
    private List<RunProcess> processes;

    /**
     * 显示正在运行的进程信息.
     * 
     * @return
     */
    public String showActiveProcessJsp() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 显示已安装软件信息.
     * 
     * @return
     */
    public String showInstalledSoftwareJsp() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getEntityId() {
        return entityId;
    }

    public List<RunProcess> getProcesses() {
        return processes;
    }

    public List<Software> getSoftwares() {
        return softwares;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setProcesses(List<RunProcess> processes) {
        this.processes = processes;
    }

    public void setSoftwares(List<Software> softwares) {
        this.softwares = softwares;
    }

}
