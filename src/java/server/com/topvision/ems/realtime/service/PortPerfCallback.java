/**
 * 
 */
package com.topvision.ems.realtime.service;

import java.util.List;

import com.topvision.ems.network.domain.PortPerf;
import com.topvision.framework.remote.rmi.ClientCallback;

/**
 * @author niejun
 * 
 */
public interface PortPerfCallback extends ClientCallback {

    void sendPortPerf(List<PortPerf> perfs);

}
