/***********************************************************************
 * $Id: TrapUtil.java,v 1.1 Aug 27, 2008 3:18:39 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.util.Date;
import java.util.Vector;

import org.snmp4j.CommandResponderEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;

/**
 * @Create Date Aug 27, 2008 3:18:39 PM
 * 
 * @author kelers
 * 
 */
public class TrapUtil {
    private static String getAddress(Address peerAddress) {
        if (peerAddress instanceof UdpAddress) {
            return ((UdpAddress) peerAddress).getInetAddress().getHostAddress();
        } else if (peerAddress instanceof TcpAddress) {
            return ((TcpAddress) peerAddress).getInetAddress().getHostAddress();
        } else if (peerAddress instanceof TransportIpAddress) {
            return ((TransportIpAddress) peerAddress).getInetAddress().getHostAddress();
        } else if (peerAddress instanceof IpAddress) {
            return ((IpAddress) peerAddress).getInetAddress().getHostAddress();
            // } else if (peerAddress instanceof GenericAddress) {
            // } else if (peerAddress instanceof SMIAddress) {
            // } else {
        }
        String address = peerAddress.toString();
        int index = address.indexOf('/');
        return index == -1 ? address : address.substring(0, index);
    }

    /**
     * 解析Trap
     * 
     * @param event
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Trap parseTrap(CommandResponderEvent event) {
        Trap trap = new Trap();
        trap.setTrapTime(new Date());
        trap.setAddress(getAddress(event.getPeerAddress()));
        trap.setProcessed(event.isProcessed());
        trap.setSecurityLevel(event.getSecurityLevel());
        trap.setSecurityModel(event.getSecurityModel());
        trap.setSecurityName(event.getSecurityName());
        trap.setSource(event.getSource());
        trap.setCommandResponderEvent(event);
        for (VariableBinding vb : (Vector<VariableBinding>) event.getPDU().getVariableBindings()) {
            trap.addVariableBinding(vb.getOid().toString(), vb.getVariable().toString());
        }
        return trap;
    }
    
    public static String getVisiableCharFromTrapByte(String byteString) {
        String[] byteStrings = byteString.split(":");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteStrings.length; i++) {
            byte tmp = Byte.parseByte(byteStrings[i], 16);
            stringBuilder.append((char) tmp);
        }
        return stringBuilder.toString();
    }

}
