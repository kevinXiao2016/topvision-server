package com.topvision.ems.network.message;

import com.topvision.platform.message.event.EmsListener;

public interface LinkListener extends EmsListener {
    void linkAdded(LinkEvent evt);

    void linkRemoved(LinkEvent evt);
}
