/**
 * 
 */
package com.topvision.ems.realtime.service;

import com.topvision.framework.remote.rmi.ClientCallback;

/**
 * @author niejun
 * 
 */
public interface CpuAndMemCallback extends ClientCallback {

    /**
     * 发送设备CPU和内存使用.
     * 
     * @param cpu
     * @param pmem
     * @param vmem
     * @param umem
     *            已使用内存大小.
     * @param time
     */
    void sendCpuAndMem(float[] cpu, float pmem, float vmem, float umem, long time);

}
