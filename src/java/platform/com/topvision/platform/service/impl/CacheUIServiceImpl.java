/***********************************************************************
 * $Id: CacheUIServiceImpl.java,v 1.1 Oct 25, 2009 7:50:49 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.platform.domain.MenuItem;
import com.topvision.platform.domain.ToolbarButton;

@Service("uiService")
public class CacheUIServiceImpl extends UIServiceImpl {

    private Map<Integer, MenuItem> menuItemMapping = new HashMap<Integer, MenuItem>();
    private List<MenuItem> menus = new ArrayList<MenuItem>();
    private List<MenuItem> menuItems;
    private List<ToolbarButton> toolbarButtons;

    @Override
    @PreDestroy
    public void destroy() {
        if (menuItems != null) {
            menuItems.clear();
        }
        if (menus != null) {
            menus.clear();
        }
        if (toolbarButtons != null) {
            toolbarButtons.clear();
        }
        if (menuItemMapping != null) {
            menuItemMapping.clear();
        }
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        try {
            menuItems = super.loadMenuItem();
            toolbarButtons = super.loadToolbarButton();
            MenuItem menuitem = null;
            int size = menuItems.size();
            for (int i = 0; i < size; i++) {
                menuitem = menuItems.get(i);
                menuItemMapping.put(menuitem.getItemId(), menuitem);
                if (menuitem.getParentId() == 0) {
                    menus.add(menuitem);
                }
                MenuItem parentItem = menuItemMapping.get(menuitem.getParentId());
                if (parentItem != null) {
                    if (parentItem.getChildren() == null) {
                        parentItem.setChildren(new ArrayList<MenuItem>());
                    }
                    parentItem.getChildren().add(menuitem);
                }
            }
        } catch (DataAccessException ex) {
            getLogger().error("Init UIService error.", ex);
        }
    }

    @Override
    public List<MenuItem> loadMenuItem() {
        return menuItems;
    }

    @Override
    public List<MenuItem> loadMenus() {
        return menus;
    }

    @Override
    public List<ToolbarButton> loadToolbarButton() {
        return toolbarButtons;
    }

}
