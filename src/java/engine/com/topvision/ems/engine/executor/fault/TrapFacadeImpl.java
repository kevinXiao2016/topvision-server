package com.topvision.ems.engine.executor.fault;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.fault.TrapFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpTrapManager;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.snmp.TrapCallback;
import com.topvision.framework.snmp.TrapServerParam;

@Facade("trapFacade")
public class TrapFacadeImpl extends BaseEngine implements TrapFacade {
    @Autowired
    private SnmpTrapManager snmpTrapManager = null;

    @Override
    public void addTrapListener() {
        TrapCallback l = getCallback(TrapCallback.class);
        if (l != null) {
            snmpTrapManager.addListener(l);
        }
    }

    @Override
    public void removeTrapListener() {
        TrapCallback l = getCallback(TrapCallback.class);
        if (l != null) {
            snmpTrapManager.removeListener(l);
        }
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.fault.TrapFacade#sendTrap(com.topvision.ems.facade.domain.Trap)
     */
    @Override
    public void sendTrap(Trap trap) {
        if (logger.isDebugEnabled()) {
            logger.debug(trap.toString());
        }
        snmpTrapManager.sendTrap(trap);
    }

    /**
     * @param snmpTrapReceiver
     *            the snmpTrapReceiver to set
     */
    public void setSnmpTrapManager(SnmpTrapManager snmpTrapManager) {
        this.snmpTrapManager = snmpTrapManager;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.fault.TrapFacade#setTrapServerParam(com.topvision.ems.facade.domain.TrapServerParam)
     */
    @Override
    public void setTrapServerParam(TrapServerParam param) {
        snmpTrapManager.reset(param);
    }
}
