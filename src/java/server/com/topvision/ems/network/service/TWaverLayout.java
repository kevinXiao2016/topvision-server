/***********************************************************************
 * $Id: TWaverLayout.java,v1.0 2013-8-19 上午9:25:40 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twaver.Node;
import twaver.TDataBox;
import twaver.network.TNetwork;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Link;

/**
 * @author Victor
 * @created @2013-8-19-上午9:25:40
 * 
 */
public class TWaverLayout {
    public void doLayout(int height, int width, List<Entity> fixeds, List<Link> links, List<Entity> entities, int type) {
        TDataBox box = new TDataBox();
        TNetwork network = new TNetwork(box);
        Map<Long, Node> nodes = new HashMap<Long, Node>();
        for (Entity entity : entities) {
            Node node = new Node(entity.getEntityId());
            node.setLocation(entity.getX(), entity.getY());
            nodes.put(entity.getEntityId(), node);
            box.addElement(node);
        }
        for (Link l : links) {
            twaver.Link link = new twaver.Link(nodes.get(l.getSrcEntityId()), nodes.get(l.getDestEntityId()));
            box.addElement(link);
        }
        // addRadioButton("CIRCULAR LAYOUT", 1);
        // addRadioButton("SYMMETRIC LAYOUT", 4);
        // addRadioButton("TREE LAYOUT", 2);
        // addRadioButton("REVERSETREE LAYOUT", 3);
        // addRadioButton("EAST LAYOUT", 5);
        // addRadioButton("WEST LAYOUT", 6);
        // addRadioButton("HIERARCHIC LAYOUT", 7);
        network.setSize(width, height);
        network.setBounds(0, 0, width, height);
        network.setMaximumSize(new Dimension(width, height));
        network.doLayout(type, false);
        for (Entity entity : entities) {
            Node node = nodes.get(entity.getEntityId());
            entity.setX(node.getLocation().x);
            entity.setY(node.getLocation().y);
        }
    }
}
