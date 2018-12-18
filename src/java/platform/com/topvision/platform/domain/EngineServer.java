/***********************************************************************
 * $Id: EngineServer.java,v 1.1 Jul 19, 2009 10:28:41 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

@Alias("engineServer")
public class EngineServer implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 6037993284026314912L;

    public final static byte CONNECTED = 1;
    public final static byte DISCONNECT = 2;
    public final static byte CONNECTING = 3;
    public final static byte START = 1;
    public final static byte STOP = 2;

    public final static String TYPE_ALL = "Default,Trap,CmPoll,Performance,PNMP";
    public final static String TYPE_DEFAULT = "Default";
    public final static String TYPE_CM_POLL = "CmPoll";
    public final static String TYPE_PNMP = "PNMP";
    public final static String TYPE_PERFORMANCE = "Performance";
    public final static String TYPE_Trap = "Trap";

    private Integer id;
    private String name;
    private String ip;
    private String ipGroupDisplay;
    private Integer port = 3004;
    private String note;
    private byte adminStatus = 1;// 1-start, 2-stop
    private byte linkStatus = 2;// 2-disconnect, 3-connecting, 1-connected
    // Engine的类型，由server分配，目前支持Default,Trap,CmPoll,Performance
    private String type = TYPE_ALL;

    private String version;
    private byte manageStatus = 2;
    private Integer xmx;
    private Integer xms;

    public EngineServer() {
        super();
    }

    public EngineServer(String name, String ip, Integer port, Integer xmx, Integer xms, String note, String type) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.xmx = xmx;
        this.xms = xms;
        this.note = note;
        this.type = type;
    }

    public EngineServer(String name, String ip, Integer port, Integer xmx, Integer xms, String note, String type,
            byte adminStatus, byte linkStatus) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.xmx = xmx;
        this.xms = xms;
        this.note = note;
        this.type = type;
        this.adminStatus = adminStatus;
        this.linkStatus = linkStatus;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public String getEngineId() {
        return String.valueOf(id);
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the adminStatus
     */
    public byte getAdminStatus() {
        return adminStatus;
    }

    /**
     * @param adminStatus
     *            the adminStatus to set
     */
    public void setAdminStatus(byte adminStatus) {
        this.adminStatus = adminStatus;
        if (adminStatus == STOP) {
            linkStatus = DISCONNECT;
        }
    }

    /**
     * @return the linkStatus
     */
    public byte getLinkStatus() {
        if (adminStatus == STOP) {
            linkStatus = DISCONNECT;
        }
        return linkStatus;
    }

    /**
     * @return the linkStatus
     */
    public String getLinkStatusStr() {
        if (adminStatus == STOP) {
            linkStatus = DISCONNECT;
        }
        switch (linkStatus) {
        case DISCONNECT:
            return "Disconnected";
        case CONNECTED:
            return "Connected";
        case CONNECTING:
            return "Conntecting";
        default:
            return "Unknown";
        }
    }

    /**
     * @param linkStatus
     *            the linkStatus to set
     */
    public void setLinkStatus(byte linkStatus) {
        this.linkStatus = linkStatus;
    }

    /**
     * @return the manageStatus
     */
    public byte getManageStatus() {
        return manageStatus;
    }

    /**
     * @param manageStatus
     *            the manageStatus to set
     */
    public void setManageStatus(byte manageStatus) {
        this.manageStatus = manageStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EngineServer [id=").append(id).append(", name=").append(name).append(", ip=").append(ip)
                .append(", port=").append(port).append(", note=").append(note).append(", adminStatus=")
                .append(adminStatus).append(", linkStatus=").append(linkStatus).append(", type=").append(type)
                .append("]");
        return builder.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean hasType(String type) {
        return this.type.indexOf(type) != -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EngineServer)) {
            return false;
        }
        return id.equals(((EngineServer) obj).getId());
    }

    /**
     * @return the xmx
     */
    public Integer getXmx() {
        return xmx;
    }

    /**
     * @param xmx
     *            the xmx to set
     */
    public void setXmx(Integer xmx) {
        this.xmx = xmx;
    }

    /**
     * @return the xms
     */
    public Integer getXms() {
        return xms;
    }

    /**
     * @param xms
     *            the xms to set
     */
    public void setXms(Integer xms) {
        this.xms = xms;
    }

    /**
     * @return the ipGroupDisplay
     */
    public String getIpGroupDisplay() {
        if (this.getManageStatus() == EngineServer.CONNECTED) {
            this.ipGroupDisplay = this.ip + " <img nm3ktip=online class=nm3kTip src=../images/yes.png />";
        } else {
            this.ipGroupDisplay = this.ip + " <img nm3ktip=offline class=nm3kTip src=../images/wrong.png />";
        }
        return ipGroupDisplay;
    }

    /**
     * @param ipGroupDisplay
     *            the ipGroupDisplay to set
     */
    public void setIpGroupDisplay(String ipGroupDisplay) {
        this.ipGroupDisplay = ipGroupDisplay;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
