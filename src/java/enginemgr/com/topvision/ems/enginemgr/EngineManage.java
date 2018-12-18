/***********************************************************************
 * $Id: EngineManage.java,v1.0 2016-6-1 下午3:43:42 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.enginemgr;

/**
 * @author Rod John
 * @created @2016-6-1-下午3:43:42
 *
 */
public interface EngineManage {

    /**
     * getEngineMgrPort
     * 
     * @return
     */
    public Integer getEngineMgrPort();

    /**
     * @return java.rmi.server.hostname
     */
    public String getHostname();

    /**
     * Shutdown Engine Manage
     * 
     */
    public void shutdownMgr();

    /**
     * Delete EngineDir
     * 
     * @param port
     */
    public void deleteEngineDir(String port);

    /**
     * Transafer EngineDir
     * 
     * @param filename
     * @param fileByte
     */
    public void transfer(String filename, byte[] fileByte);

    /**
     * Re Start Engine
     * 
     * @param port
     * 
     */
    public void reStartEngine(String port);

    /**
     * Shut down Engine
     * 
     * @param port
     */
    public void shutdownEngine(String port);

    /**
     * Update Engine Port
     * 
     * @param port
     * @param destPort
     */
    public void updatePort(String port, String destPort);

    /**
     * Update Engine Mem
     * 
     * @param port
     * @param max
     * @param min
     */
    public void updateMem(String port, Integer max, Integer min);
}
