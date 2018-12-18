/***********************************************************************
 * $Id: MenuUtil.java,v1.0 2013-4-12 下午13:13:17 $
 *
 * @author: lzt
 *
 * (c)Copyright 2013 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.network.domain.MenuControl;
import com.topvision.platform.SystemConstants;

/**
 * @author lzt
 * @created @2013-4-12 下午13:13:17
 */
public class MenuUtil {

    public static List<MenuControl> getDeviceSoftVersionMenu(String type) {
        List<MenuControl> menuControls = new ArrayList<MenuControl>();
        StringBuilder path = new StringBuilder();
        path.append(SystemConstants.WEB_INF_REAL_PATH);
        path.append(File.separator);
        path.append("menuControl");
        File rootDir = new File(path.toString());
        File[] files = rootDir.listFiles();
        File file;
        for (int i = files.length - 1; i >= 0; i--) {
            file = files[i];
            if (!file.isDirectory() && file.getName().contains(type)) {
                String[] parms = (file.toString()).split("\\.");
                if ("xml".equals(parms[parms.length - 1])) {
                    MenuControl menu = (MenuControl) XmlOperationUtil.readObject(file);
                    menuControls.add(menu);
                }
            }
        }
        return menuControls;
    }
}
