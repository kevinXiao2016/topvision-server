/***********************************************************************
 * $Id: CmdFacade.java,v1.0 2013-11-12 下午3:19:50 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import com.topvision.framework.annotation.EngineFacade;

/**
 * @author Victor
 * @created @2013-11-12-下午3:19:50
 *
 */
@EngineFacade(serviceName = "CmdFacade", beanName = "cmdFacade")
public interface CmdFacade {
    /**
     * 此方法很特殊，请注意！！！！！
     * 
     * 执行采集端系统的命令行ping，此方法可以多次调用，如果ip为空表示获取上一次的值，如果ip不为空，则进行新的ping命令。命令结束后悔在返回字符串最后附加一个#OK#字符串。
     * 
     * @param ip ping的ip
     * @param timeout 超时时间
     * @param count 发送次数
     * @return 操作系统执行ping命令的返回结果
     */
    String ping(String ip, Integer timeout, Integer count);

    /**
     * 此方法很特殊，请注意！！！！！
     * 
     * 执行采集端系统的命令行tracert，此方法可以多次调用，如果ip为空表示获取上一次的值，如果ip不为空，则进行新的tracert命令。命令结束后悔在返回字符串最后附加一个#OK#字符串。
     * 
     * @param ip tracert的ip
     * @return 操作系统执行tracert命令的返回结果
     */
    String tracert(String ip);

    /**
     * 此方法很特殊，请注意！！！！！
     * 
     * @return 此方法返回ping || tracert命令调用的结果，根据上次调用命令返回结果，命令结束后悔在返回字符串最后附加一个#OK#字符串。
     */
    String getResult();
}
