/**
 * 
 */
package com.topvision.ems.tl1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.nbi.tl1.api.TL1QueryService;
import com.topvision.nbi.tl1.api.TL1RequestService;
import com.topvision.nbi.tl1.api.domain.LocationMerger;
import com.topvision.nbi.tl1.api.domain.OnuLocation;
import com.topvision.nbi.tl1.api.domain.PonLocation;
import com.topvision.nbi.tl1.api.domain.Target;
import com.topvision.nbi.tl1.api.domain.UniLocation;
import com.topvision.nbi.tl1.api.exception.DDOFException;
import com.topvision.nbi.tl1.api.exception.IRNEException;
import com.topvision.nbi.tl1.api.exception.SEOFException;
import com.topvision.nbi.tl1.api.exception.TopvisionTL1Exception;

/**
 * @author Administrator
 *
 */
@Service("tl1RequestService")
public class TL1RequestServiceImpl implements TL1RequestService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TL1ExecutorRegistryService tl1ExecutorRegistryService;
    @Autowired
    private TL1QueryService tl1QueryService;
    @Autowired
    private EntityService entityService;

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Target, K> void applyTL1ConfigCommand(T access, final K config, String commandCode) {
        final Long entityId = getEntityId(access);
        final Long index = getIndex(access);
        final TL1ExecutorService<K> tl1ExecutorService = (TL1ExecutorService<K>) tl1ExecutorRegistryService
                .getService(commandCode);
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("execute tl1 request: {} ", commandCode);
            }
            tl1ExecutorService.execute(entityId, index, config);
        } catch (SnmpException e) {
            logger.error("", e);
            throw new DDOFException();
        } catch (TopvisionTL1Exception e) {
            logger.error("", e);
            throw e;
        } catch (Exception e) {
            logger.error("", e);
            throw new SEOFException();
        }

    }

    private Long getEntityId(Target target) {
        if (target instanceof PonLocation) {
            target.setIpAddress(((PonLocation) target).getOltName());
        }
        if (target instanceof OnuLocation) {
            target.setIpAddress(((OnuLocation) target).getOltName());
        }
        Entity entity = entityService.getEntityByIp(target.getIpAddress());
        if (entity == null) {
            throw new IRNEException();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("tl1 request for olt:{}", entity.getIp());
        }
        return entity.getEntityId();
    }

    private Long getIndex(Target target) {
        if (target instanceof PonLocation) {
            return LocationMerger.getPonIndex(((PonLocation) target).getPonId());
        }
        Long onuIndex = null;
        if (target instanceof OnuLocation) {
            OnuLocation onuLocation = (OnuLocation) target;
            String ipAddress = target.getIpAddress();
            Long ponIndex = LocationMerger.getPonIndex((onuLocation.getPonId()));
            onuIndex = tl1QueryService.getOnuIndex(ipAddress, ponIndex, onuLocation.getOnuIdType(),
                    onuLocation.getOnuId());
        }
        if (target instanceof UniLocation) {
            UniLocation uniLocation = (UniLocation) target;
            return LocationMerger.mergeUniIndex(onuIndex, uniLocation.getOnuPort());
        }
        return onuIndex;
    }

    /*
     * @Test public void sds() { String[] array = "1-1-3-4".split("-"); Integer slot =
     * Integer.valueOf(Integer.parseInt(array[2])); Integer pon =
     * Integer.valueOf(Integer.parseInt(array[3])); Integer[] id = new Integer[] { slot, pon }; Long
     * index = Long.valueOf(0L); for (int i = 0; i < id.length; ++i) { index =
     * Long.valueOf(index.longValue() + (id[i].longValue() << (4 - i) * 8)); }
     * System.out.print(index.longValue() & 1099494850560L); }
     */
}
