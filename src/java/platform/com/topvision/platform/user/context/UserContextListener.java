package com.topvision.platform.user.context;


public interface UserContextListener {

    void registry(String host);

    void unregistry(String host);


}
