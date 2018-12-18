/**
 * 
 */
package com.topvision.ems.realtime.service;

import com.topvision.ems.network.domain.PortPerf;
import com.topvision.framework.remote.rmi.ClientCallback;

/**
 * @author niejun
 * 
 */
public interface LinkFlowCallback extends ClientCallback {

    void sendPortPerf(PortPerf perf);

}
