/**
 * 
 */
package com.topvision.ems.network.message;

import com.topvision.ems.network.domain.Link;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author kelers
 * 
 */
public class LinkEvent extends EmsEventObject<LinkListener> {
    private static final long serialVersionUID = -5422774953410417395L;
    private Link link;
    private long folderId;

    public LinkEvent(Object source) {
        super(source);
    }

    public long getFolderId() {
        return folderId;
    }

    public Link getLink() {
        return link;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
