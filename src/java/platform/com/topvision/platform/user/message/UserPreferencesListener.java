package com.topvision.platform.user.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * 监听用户个性设置改变
 * @author xiaoyue
 * @created @2017年9月28日-上午11:37:53
 *
 */
public interface UserPreferencesListener extends EmsListener{
    void userPreferencesChanged(UserEvent envet);
}
