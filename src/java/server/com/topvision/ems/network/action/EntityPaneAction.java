package com.topvision.ems.network.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("entityPaneAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityPaneAction extends BaseAction {
    private static final long serialVersionUID = -1407596340787837553L;
    @Autowired
    private PortService portService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    private Entity entity;
    private EntityType entityType;
    private String bitmap;
    private List<Port> ports;
    private long entityId;
    private long portId;
    private List<Long> portIds;
    private List<String> x;
    private List<String> y;
    private boolean physical;

    /**
     * 打开端口.
     * 
     * @return
     */
    public String openPort() {
        return NONE;
    }

    /**
     * 更新面板状态.
     * 
     * @return
     */
    public String refreshPaneState() {
        return NONE;
    }

    /**
     * 保存面板坐标信息.
     * 
     * @return
     */
    public String savePaneCoord() {
        List<Port> list = new ArrayList<Port>();
        int size = portIds == null ? 0 : portIds.size();
        Port p = null;
        for (int i = 0; i < size; i++) {
            p = new Port();
            p.setPortId(portIds.get(i));
            p.setX(x.get(i));
            p.setY(y.get(i));
            list.add(p);
        }
        portService.savePortCoord(list);

        return NONE;
    }

    /**
     * 关闭端口.
     * 
     * @return
     */
    public String closePort() {
        return NONE;
    }

    /**
     * 显示逻辑面板.
     * 
     * @return
     */
    public String showLogicPane() {
        entity = entityService.getEntity(entityId);
        entityType = entityTypeService.getEntityType(entity.getTypeId());
        ports = portService.getPortCoord(entityId);
        if (entityType.getLogicPane() == null || "".equals(entityType.getLogicPane())) {
            bitmap = "common.jpg";
            int x = 20;
            int y = 20;
            int size = ports.size();
            Port port = null;
            for (int i = 0; i < size; i++) {
                if (i != 0 && i % 10 == 0) {
                    x = 20;
                    y = y + 40;
                }
                port = ports.get(i);
                port.setX(String.valueOf(x));
                port.setY(String.valueOf(y));
                x = x + 40;
            }
        }
        return SUCCESS;
    }

    /**
     * 显示物理面板.
     * 
     * @return
     */
    public String showPhysicalPane() {
        entity = entityService.getEntity(entityId);
        entityType = entityTypeService.getEntityType(entity.getTypeId());
        if (entityType.getPhysicPane() == null) {
            return "notfound";
        }
        return SUCCESS;
    }

    public String getBitmap() {
        return bitmap;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getEntityId() {
        return entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public long getPortId() {
        return portId;
    }

    public List<Long> getPortIds() {
        return portIds;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public List<String> getX() {
        return x;
    }

    public List<String> getY() {
        return y;
    }

    public boolean isPhysical() {
        return physical;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
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

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public void setPhysical(boolean physical) {
        this.physical = physical;
    }

    public void setPortId(long portId) {
        this.portId = portId;
    }

    public void setPortIds(List<Long> portIds) {
        this.portIds = portIds;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public void setX(List<String> x) {
        this.x = x;
    }

    public void setY(List<String> y) {
        this.y = y;
    }

}
