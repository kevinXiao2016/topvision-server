package com.topvision.ems.network.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ResultContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.LinkSnap;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.event.FlowEvent;
import com.topvision.platform.message.event.FlowListener;
import com.topvision.platform.message.service.MessageService;

/**
 * 拓扑状态管理, 用于对WEB提供快速获取设备和线路的当前状态.
 * 
 */
public class NetworkSnapManager implements EntityValueListener, FlowListener {
    protected static Logger logger = LoggerFactory.getLogger(NetworkSnapManager.class);
    private static final int entityMappingSize = SystemConstants.getInstance().getIntParam("entitySnap.mapping.size",
            1001);
    private static final int linkMappingSize = SystemConstants.getInstance().getIntParam("linkSnap.mapping.size", 301);
    private final Map<Long, EntitySnap> snapMapping = new HashMap<Long, EntitySnap>(entityMappingSize);
    private final Map<Long, LinkSnap> flowMapping = new HashMap<Long, LinkSnap>(linkMappingSize);
    private static NetworkSnapManager manager = new NetworkSnapManager();
    private EntityDao entityDao = null;

    public static NetworkSnapManager getInstance() {
        return manager;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityValueListener#alertChanged(com.topvision.ems.message.event.EntityValueEvent)
     */
    @Override
    public void alertChanged(EntityValueEvent event) {
        EntitySnap snap = snapMapping.get(event.getEntityId());
        if (snap == null) {
            snap = new EntitySnap();
            snap.setEntityId(event.getEntityId());
            snapMapping.put(event.getEntityId(), snap);
        }
        snap.setAlertLevel(event.getMaxAlertLevel().byteValue());
        snap.setAlertDesc(event.getMaxAlertDesc());
        snap.setAlertId(event.getAlertId());
        snap.setAlertTime(event.getMaxAlertTime());
    }

    /**
     * 是否包含给定的设备.
     * 
     * @param entityId
     * @return
     */
    public Boolean containsEntity(Long entityId) {
        return snapMapping.containsKey(entityId);
    }

    /**
     * 
     * @param linkId
     * @return
     */
    public Boolean containsLink(Long linkId) {
        return flowMapping.containsKey(linkId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.FlowListener#flowChanged(com.topvision.ems.message.event.FlowEvent)
     */
    @Override
    public void flowChanged(FlowEvent event) {
        LinkSnap snap = flowMapping.get(event.getLinkId());
        if (snap == null) {
            snap = new LinkSnap();
            snap.setLinkId(event.getLinkId());
            flowMapping.put(event.getLinkId(), snap);
        }
        snap.setUsage(event.getUsage());
        snap.setRate(event.getRate());
        snap.setFlow(event.getFlow());
        snap.setSnapTimeMillis(System.currentTimeMillis());
    }

    public EntitySnap getEntitySnap(long entityId) {
        return snapMapping.get(entityId);
    }

    /**
     * 获取给定线路的流量, 如果没有数据则为0.
     * 
     * @param linkId
     * @return
     */
    public LinkSnap getLinkSnapByLinkId(Long linkId) {
        return flowMapping.get(linkId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityValueListener#performanceChanged(com.topvision.ems.message.event.EntityValueEvent)
     */
    @Override
    public void performanceChanged(EntityValueEvent event) {
        // Update Topology Cache
        EntitySnap snap = snapMapping.get(event.getEntityId());
        if (snap == null) {
            snap = new EntitySnap();
            snap.setEntityId(event.getEntityId());
            snapMapping.put(event.getEntityId(), snap);
        }
        snap.setState(event.isState());
        if (event.getCpu() != null) {
            snap.setCpu(event.getCpu());
        }
        if (event.getMem() != null) {
            snap.setMem(event.getMem());
        }
        snap.setVmem(event.getVmem());
        if (event.getDisk() != null) {
            snap.setDisk(event.getDisk());
        }
        //EMS-14994 RMD设备这个节点读到的是"noSuchObject",这个此段只接受数字类字符串
        if (StringUtils.isNumeric(event.getSysUpTime())) {
            snap.setSysUpTime(event.getSysUpTime());
        }
        snap.setSnapTimeMillis(System.currentTimeMillis());
        // Update EntitySnap Database
        entityDao.updateEntitySnap(snap);
    }

    public void setAlertService(AlertService alertService) {
        // 还原设备的最新告警状态
        alertService.restoreEntityCurrentAlertState(new MyResultHandler() {
            @Override
            public void handleResult(ResultContext resultcontext) {
                EntitySnap snap = (EntitySnap) resultcontext.getResultObject();
                if (!snapMapping.containsKey(snap.getEntityId())) {
                    snapMapping.put(snap.getEntityId(), snap);
                }
            }

            @Override
            public void complete() {
            }

            @Override
            public void prepare() {
            }
        });
    }

    /**
     * 
     * @param entityService
     */
    public void setEntityService(EntityService entityService) {
        String v = SystemConstants.getInstance().getStringParam("version.level", "enterprise");
        // EMS-6809 拓扑管理页面下的CMTS无法显示运行时间等参数  @haojie
        if ("enterprise".equalsIgnoreCase(v)) {
            // 如果是DEMO版本, 则还原设备快照
            entityService.restoreEntityCurrentState(new MyResultHandler() {
                @Override
                public void handleResult(ResultContext resultcontext) {
                    EntitySnap snap = (EntitySnap) resultcontext.getResultObject();
                    EntitySnap entity = snapMapping.get(snap.getEntityId());
                    if (entity == null) {
                        snapMapping.put(snap.getEntityId(), snap);
                    } else if (snap.isState()) {
                        entity.setCpu(snap.getCpu());
                        entity.setMem(snap.getMem());
                        entity.setSysUpTime(snap.getSysUpTime());
                    }
                }

                @Override
                public void complete() {
                }

                @Override
                public void prepare() {
                }
            });
        }
    }

    /**
     * 
     * @param linkService
     */
    public void setLinkService(LinkService linkService) {
        String v = SystemConstants.getInstance().getStringParam("version.level", "enterprise");
        if ("demo".equalsIgnoreCase(v)) {
            // 如果是DEMO版本, 则还原线路状态
            linkService.restoreLinkCurrentState(new MyResultHandler() {
                @Override
                public void handleResult(ResultContext resultcontext) {
                    LinkEx link = (LinkEx) resultcontext.getResultObject();
                    LinkSnap snap = new LinkSnap();
                    snap.setLinkId(link.getLinkId());
                    if (link.getSrcIfOctetsRate() != null) {
                        snap.setRate(link.getSrcIfOctetsRate());
                    } else {
                        snap.setRate(0.0);
                    }
                    if (link.getSrcIfOctets() != null) {
                        snap.setFlow(link.getSrcIfOctets());
                    } else {
                        snap.setFlow(0.0);
                    }
                    snap.setUsage(0.0);
                    flowMapping.put(link.getLinkId(), snap);
                }

                @Override
                public void complete() {
                }

                @Override
                public void prepare() {
                }
            });
        }
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public final void setMessageService(MessageService messageService) {
        messageService.addListener(EntityValueListener.class, this);
        messageService.addListener(FlowListener.class, this);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityValueListener#stateChanged(com.topvision.ems.message.event.EntityValueEvent)
     */
    @Override
    public void stateChanged(EntityValueEvent event) {
        EntitySnap snap = snapMapping.get(event.getEntityId());
        if (snap == null) {
            snap = new EntitySnap();
            snap.setEntityId(event.getEntityId());
            snapMapping.put(event.getEntityId(), snap);
        }
        snap.setState(event.isState());
        if (event.getSysUpTime() != null) {
            snap.setSysUpTime(event.getSysUpTime());
        }
        snap.setSnapTimeMillis(System.currentTimeMillis());
    }

    public EntityDao getEntityDao() {
        return entityDao;
    }

    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

}