package com.topvision.ems.facade.fault;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.snmp.TrapServerParam;

@EngineFacade(serviceName = "TrapFacade", beanName = "trapFacade", category = "Trap")
public interface TrapFacade extends Facade {
    /**
     * 添加trap监视器
     * 
     */
    void addTrapListener();

    /**
     * 删除trap监视器
     * 
     */
    void removeTrapListener();

    /**
     * 用户通过engine端发送trap
     * 
     * @param trap
     */
    void sendTrap(Trap trap);

    /**
     * 设置trap server的参数
     * 
     * @param param
     */
    void setTrapServerParam(TrapServerParam param);
}
