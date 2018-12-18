/***********************************************************************
 * $Id: TopologyParam.java,v 1.1 May 11, 2008 11:07:51 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date May 11, 2008 11:07:51 PM
 * 
 * @author kelers
 * 
 */
public class TopologyParam implements Serializable {
    /**
     * 
     * @Create Date May 22, 2008 2:49:06 PM
     * 
     * @author kelers
     * 
     */
    public static class Seed implements Serializable {
        private static final long serialVersionUID = -9131656130234024341L;
        private String seed;
        private String community;

        public Seed() {
        }

        public Seed(String s) {
            StringTokenizer token = new StringTokenizer(s, DELIMITER);
            if (token.hasMoreTokens()) {
                seed = token.nextToken();
            }
            if (token.hasMoreTokens()) {
                community = token.nextToken();
            }
        }

        public Seed(String s, String c) {
            seed = s;
            community = c;
        }

        /**
         * @return the community
         */
        public String getCommunity() {
            return community;
        }

        /**
         * @return the seed
         */
        public String getSeed() {
            return seed;
        }

        /**
         * @param community
         *            the community to set
         */
        public void setCommunity(String community) {
            this.community = community.trim();
        }

        /**
         * @param seed
         *            the seed to set
         */
        public void setSeed(String seed) {
            this.seed = seed;
        }

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(seed).append(DELIMITER).append(community);
            return sb.toString();
        }
    }

    /**
     * 
     * @Create Date May 22, 2008 2:49:11 PM
     * 
     * @author kelers
     * 
     */
    public static class Segment implements Serializable {
        private static final long serialVersionUID = -6393391136432773800L;
        private String ip;
        private String mask;
        private String community;

        public Segment() {
        }

        public Segment(String s) {
            StringTokenizer token = new StringTokenizer(s, DELIMITER);
            if (token.hasMoreTokens()) {
                ip = token.nextToken();
            }
            if (token.hasMoreTokens()) {
                mask = token.nextToken();
            }
            if (token.hasMoreTokens()) {
                community = token.nextToken();
            }
        }

        public Segment(String ip, String mask) {
            this.ip = ip;
            this.mask = mask;
        }

        public Segment(String i, String m, String c) {
            this.ip = i;
            this.mask = m;
            this.community = c;
        }

        /**
         * @return the community
         */
        public String getCommunity() {
            return community;
        }

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @return the mask
         */
        public String getMask() {
            return mask;
        }

        /**
         * @param community
         *            the community to set
         */
        public void setCommunity(String community) {
            this.community = community;
        }

        /**
         * @param ip
         *            the ip to set
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * @param mask
         *            the mask to set
         */
        public void setMask(String mask) {
            this.mask = mask;
        }

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ip).append(DELIMITER);
            sb.append(mask).append(DELIMITER);
            sb.append(community);
            return sb.toString();
        }
    }

    protected transient Logger logger = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 5601182533880311617L;
    public static final String DELIMITER = "$#$";
    private List<Seed> seeds;

    private List<Segment> segments;
    private String mibs;
    private long folderId = 2;
    private String target = null;

    private String excludeTarget = null;
    private String seed = null;

    private boolean discoveryService = true;
    private List<Integer> servicePorts = null;
    private boolean onlyDiscoverySnmp = true;
    private int snmpTimeout = 5000;
    private int snmpRetry = 1;
    private int snmpPort = 161;
    private String community = "public,private";

    private List<String> communities;
    private List<SnmpParam> snmpParams;
    private boolean telnetDetected = false;
    private boolean sshDetected = false;
    private String username = "guest";
    private String passwd = "guest";
    private String cmdPrompt = "$";

    private String passwdPrompt = ":";
    private String loginPrompt = "$";

    private int pingTimeout = 100;
    private int pingRetry = 0;

    private boolean hasFirewall = false;
    private boolean discoveryWireless = true;
    private boolean autoDiscovery = false;
    private String autoDiscoveryTarget = null;

    private long autoDiscoveryInterval = 24 * 60 * 60 * 1000;
    private String autoDiscoveryIntervalStr = "";

    // 是否自动创建设备监视器
    private boolean autoCreateMonitor = true;

    private boolean autoRefeshIpAddrBook = true;

    /**
     * @param c
     *            the community
     */
    public void addCommunity(String c) {
        if (communities == null) {
            communities = new ArrayList<String>();
        }
        communities.add(c.trim());
    }

    /**
     * @param s
     *            the seed to add
     */
    public void addSeed(Seed s) {
        if (seeds == null) {
            seeds = new ArrayList<Seed>();
        }
        seeds.add(s);
    }

    /**
     * @param s
     *            the segment to add
     */
    public void addSegment(Segment s) {
        if (segments == null) {
            segments = new ArrayList<Segment>();
        }
        segments.add(s);
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public TopologyParam clone() {
        TopologyParam param = new TopologyParam();
        param.setAutoDiscovery(autoDiscovery);
        param.setAutoDiscoveryInterval(autoDiscoveryInterval);
        param.setAutoDiscoveryTarget(autoDiscoveryTarget);
        param.setCmdPrompt(cmdPrompt);
        param.setCommunities(communities);
        param.setCommunity(community.trim());
        param.setDiscoveryService(discoveryService);
        param.setDiscoveryWireless(discoveryWireless);
        param.setExcludeTarget(excludeTarget);
        param.setFolderId(folderId);
        param.setHasFirewall(hasFirewall);
        param.setLoginPrompt(loginPrompt);
        param.setMibs(mibs);
        param.setOnlyDiscoverySnmp(onlyDiscoverySnmp);
        param.setPasswd(passwd);
        param.setPasswdPrompt(passwdPrompt);
        param.setPingRetry(pingRetry);
        param.setPingTimeout(pingTimeout);
        param.setSeed(seed);
        param.setSeeds(seeds);
        param.setSegments(segments);
        param.setServicePorts(servicePorts);
        param.setSnmpPort(snmpPort);
        param.setSnmpRetry(snmpRetry);
        param.setSnmpTimeout(snmpTimeout);
        param.setSshDetected(sshDetected);
        param.setTarget(target);
        param.setTelnetDetected(telnetDetected);
        param.setUsername(username);
        param.setAutoCreateMonitor(autoCreateMonitor);
        param.setAutoRefeshIpAddrBook(autoRefeshIpAddrBook);
        return param;
    }

    public long getAutoDiscoveryInterval() {
        return autoDiscoveryInterval;
    }

    public String getAutoDiscoveryIntervalStr() {
        autoDiscoveryIntervalStr = String.valueOf(autoDiscoveryInterval / 3600000);
        return autoDiscoveryIntervalStr;
    }

    public String getAutoDiscoveryTarget() {
        return autoDiscoveryTarget;
    }

    public String getCmdPrompt() {
        return cmdPrompt;
    }

    /**
     * @return the communities
     */
    public List<String> getCommunities() {
        return communities;
    }

    public String getCommunity() {
        return community;
    }

    public String getExcludeTarget() {
        return excludeTarget;
    }

    /**
     * @return the folderId
     */
    public long getFolderId() {
        return folderId;
    }

    public String getLoginPrompt() {
        return loginPrompt;
    }

    /**
     * @return the mibs
     */
    public String getMibs() {
        return mibs;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getPasswdPrompt() {
        return passwdPrompt;
    }

    /**
     * @return the pingRetry
     */
    public int getPingRetry() {
        return pingRetry;
    }

    /**
     * @return the pingTimeout
     */
    public int getPingTimeout() {
        return pingTimeout;
    }

    public String getSeed() {
        return seed;
    }

    /**
     * @return the seeds
     */
    public List<Seed> getSeeds() {
        if ((seeds == null || seeds.isEmpty()) && (seed != null && !seed.equalsIgnoreCase("null"))) {
            StringTokenizer token = new StringTokenizer(seed);
            seeds = new ArrayList<Seed>(token.countTokens());
            while (token.hasMoreTokens()) {
                seeds.add(new Seed(token.nextToken()));
            }
        }
        return seeds;
    }

    /**
     * @return the segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    public List<Integer> getServicePorts() {
        return servicePorts;
    }

    /**
     * @return the snmpParams
     */
    public List<SnmpParam> getSnmpParams() {
        if (snmpParams == null) {
            snmpParams = new ArrayList<SnmpParam>();
            for (String community : getCommunities()) {
                SnmpParam sp = new SnmpParam();
                sp.setTimeout(getSnmpTimeout());
                sp.setRetry((byte) getSnmpRetry());
                sp.setPort(getSnmpPort());
                sp.setMibs(getMibs());
                sp.setCommunity(community);
                snmpParams.add(sp);
            }
            // TODO snmp v3 param add here.
        }
        return snmpParams;
    }

    /**
     * @return the snmpPort
     */
    public int getSnmpPort() {
        return snmpPort;
    }

    /**
     * @return the snmpRetry
     */
    public int getSnmpRetry() {
        return snmpRetry;
    }

    /**
     * @return the snmpTimeout
     */
    public int getSnmpTimeout() {
        return snmpTimeout;
    }

    public String getTarget() {
        return target;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAutoCreateMonitor() {
        return autoCreateMonitor;
    }

    public boolean isAutoDiscovery() {
        return autoDiscovery;
    }

    public boolean isAutoRefeshIpAddrBook() {
        return autoRefeshIpAddrBook;
    }

    public boolean isDiscoveryService() {
        return discoveryService;
    }

    public boolean isDiscoveryWireless() {
        return discoveryWireless;
    }

    public boolean isHasFirewall() {
        return hasFirewall;
    }

    public boolean isOnlyDiscoverySnmp() {
        return onlyDiscoverySnmp;
    }

    public boolean isSshDetected() {
        return sshDetected;
    }

    public boolean isTelnetDetected() {
        return telnetDetected;
    }

    /**
     * 从数据库到JavaBean
     * 
     * @param map
     */
    public void parseMap(Properties prop) {
        setTarget(prop.getProperty("target"));
        setExcludeTarget(prop.getProperty("excludeTarget"));
        setSeed(prop.getProperty("seed"));
        setFolderId(Long.parseLong(prop.getProperty("folderId", String.valueOf(folderId))));
        setCommunity(prop.getProperty("community", community.trim()));

        setDiscoveryService("true".equals(prop.getProperty("discoveryService")));

        setOnlyDiscoverySnmp("true".equals(prop.getProperty("onlyDiscoverySnmp")));
        setSnmpTimeout(Integer.parseInt(prop.getProperty("snmpTimeout", String.valueOf(snmpTimeout))));
        setSnmpRetry(Integer.parseInt(prop.getProperty("snmpRetry", String.valueOf(snmpRetry))));
        setSnmpPort(Integer.parseInt(prop.getProperty("snmpPort", String.valueOf(snmpPort))));

        setTelnetDetected("true".equals(prop.getProperty("telnetDetected")));
        setSshDetected("true".equals(prop.getProperty("sshDetected")));
        setUsername(prop.getProperty("username", "guest"));
        setPasswd(prop.getProperty("passwd", "guest"));
        setCmdPrompt(prop.getProperty("cmdPrompt"));
        setLoginPrompt(prop.getProperty("loginPrompt"));
        setPasswdPrompt(prop.getProperty("passwdPrompt"));

        setPingTimeout(Integer.parseInt(prop.getProperty("pingTimeout", String.valueOf(pingTimeout))));
        setHasFirewall("true".equals(prop.getProperty("hasFirewall")));
        setAutoDiscovery("true".equals(prop.getProperty("autoDiscovery")));
        setAutoDiscoveryTarget(prop.getProperty("autoDiscoveryTarget"));
        setAutoDiscoveryInterval(Long.parseLong(prop.getProperty("autoDiscoveryInterval", "3600000")));

        setAutoCreateMonitor("true".equals(prop.get("autoCreateMonitor")));
        setAutoRefeshIpAddrBook("true".equals(prop.get("autoRefeshIpAddrBook")));

        int communityCount = Integer.parseInt(prop.getProperty("communities", "0"));
        int seedCount = Integer.parseInt(prop.getProperty("seeds", "0"));
        int segmentCount = Integer.parseInt(prop.getProperty("segments", "0"));
        for (int i = 0; i < communityCount; i++) {
            addCommunity(prop.getProperty("community" + i));
        }
        for (int i = 0; i < seedCount; i++) {
            addSeed(new Seed(prop.getProperty("seed" + i)));
        }
        for (int i = 0; i < segmentCount; i++) {
            addSegment(new Segment(prop.getProperty("segment" + i)));
        }
    }

    public void setAutoCreateMonitor(boolean autoCreateMonitor) {
        this.autoCreateMonitor = autoCreateMonitor;
    }

    public void setAutoDiscovery(boolean autoDiscovery) {
        this.autoDiscovery = autoDiscovery;
    }

    public void setAutoDiscoveryInterval(long autoDiscoveryInterval) {
        this.autoDiscoveryInterval = autoDiscoveryInterval;
    }

    public void setAutoDiscoveryIntervalStr(String autoDiscoveryIntervalStr) {
        this.autoDiscoveryIntervalStr = autoDiscoveryIntervalStr;
    }

    public void setAutoDiscoveryTarget(String autoDiscoveryTarget) {
        this.autoDiscoveryTarget = autoDiscoveryTarget;
    }

    public void setAutoRefeshIpAddrBook(boolean autoRefeshIpAddrBook) {
        this.autoRefeshIpAddrBook = autoRefeshIpAddrBook;
    }

    public void setCmdPrompt(String cmdPrompt) {
        this.cmdPrompt = cmdPrompt;
    }

    /**
     * @param communities
     *            the communities to set
     */
    public void setCommunities(List<String> communities) {
        this.communities = communities;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public void setDiscoveryService(boolean discoveryService) {
        this.discoveryService = discoveryService;
    }

    public void setDiscoveryWireless(boolean discoveryWireless) {
        this.discoveryWireless = discoveryWireless;
    }

    public void setExcludeTarget(String excludeTarget) {
        this.excludeTarget = excludeTarget;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setHasFirewall(boolean hasFirewall) {
        this.hasFirewall = hasFirewall;
    }

    public void setLoginPrompt(String loginPrompt) {
        this.loginPrompt = loginPrompt;
    }

    /**
     * @param mibs
     *            the mibs to set
     */
    public void setMibs(String mibs) {
        this.mibs = mibs;
    }

    public void setOnlyDiscoverySnmp(boolean onlyDiscoverySnmp) {
        this.onlyDiscoverySnmp = onlyDiscoverySnmp;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setPasswdPrompt(String passwdPrompt) {
        this.passwdPrompt = passwdPrompt;
    }

    /**
     * @param pingRetry
     *            the pingRetry to set
     */
    public void setPingRetry(int pingRetry) {
        this.pingRetry = pingRetry;
    }

    /**
     * @param pingTimeout
     *            the pingTimeout to set
     */
    public void setPingTimeout(int pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    /**
     * @param seeds
     *            the seeds to set
     */
    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }

    /**
     * @param segments
     *            the segments to set
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public void setServicePorts(List<Integer> servicePorts) {
        this.servicePorts = servicePorts;
    }

    /**
     * @param snmpPort
     *            the snmpPort to set
     */
    public void setSnmpPort(int snmpPort) {
        this.snmpPort = snmpPort;
    }

    /**
     * @param snmpRetry
     *            the snmpRetry to set
     */
    public void setSnmpRetry(int snmpRetry) {
        this.snmpRetry = snmpRetry;
    }

    /**
     * @param snmpTimeout
     *            the snmpTimeout to set
     */
    public void setSnmpTimeout(int snmpTimeout) {
        this.snmpTimeout = snmpTimeout;
    }

    public void setSshDetected(boolean sshDetected) {
        this.sshDetected = sshDetected;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTelnetDetected(boolean telnetDetected) {
        this.telnetDetected = telnetDetected;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 从JavaBean到数据库需要的List<Attribute>
     * 
     * @return
     */
    public List<Attribute> toList() {
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("topology", "folderId", "" + folderId));
        list.add(new Attribute("topology", "target", target));
        list.add(new Attribute("topology", "excludeTarget", excludeTarget));
        list.add(new Attribute("topology", "seed", seed));

        list.add(new Attribute("topology", "discoveryService", String.valueOf(discoveryService)));

        list.add(new Attribute("topology", "onlyDiscoverySnmp", String.valueOf(onlyDiscoverySnmp)));
        list.add(new Attribute("topology", "community", community.trim()));
        list.add(new Attribute("topology", "snmpPort", String.valueOf(snmpPort)));
        list.add(new Attribute("topology", "snmpTimeout", String.valueOf(snmpTimeout)));
        list.add(new Attribute("topology", "snmpRetry", String.valueOf(snmpRetry)));

        list.add(new Attribute("topology", "telnetDetected", String.valueOf(telnetDetected)));
        list.add(new Attribute("topology", "sshDetected", String.valueOf(sshDetected)));
        list.add(new Attribute("topology", "username", username));
        list.add(new Attribute("topology", "passwd", passwd));
        list.add(new Attribute("topology", "cmdPrompt", String.valueOf(cmdPrompt)));
        list.add(new Attribute("topology", "loginPrompt", String.valueOf(loginPrompt)));
        list.add(new Attribute("topology", "passwdPrompt", String.valueOf(passwdPrompt)));

        list.add(new Attribute("topology", "pingTimeout", String.valueOf(pingTimeout)));

        list.add(new Attribute("topology", "hasFirewall", String.valueOf(hasFirewall)));

        list.add(new Attribute("topology", "discoveryWireless", String.valueOf(discoveryWireless)));

        list.add(new Attribute("topology", "autoDiscovery", String.valueOf(autoDiscovery)));
        list.add(new Attribute("topology", "autoDiscoveryTarget", autoDiscoveryTarget));
        list.add(new Attribute("topology", "autoDiscoveryInterval", String.valueOf(autoDiscoveryInterval)));

        list.add(new Attribute("topology", "autoCreateMonitor", String.valueOf(autoCreateMonitor)));
        list.add(new Attribute("topology", "autoRefeshIpAddrBook", String.valueOf(autoRefeshIpAddrBook)));

        if (communities != null && !communities.isEmpty()) {
            list.add(new Attribute("topology", "communities", String.valueOf(communities.size())));
            for (int i = 0; i < communities.size(); i++) {
                list.add(new Attribute("topology", "community" + i, communities.get(i).trim()));
            }
        }
        if (seeds != null && !seeds.isEmpty()) {
            list.add(new Attribute("topology", "seeds", String.valueOf(seeds.size())));
            for (int i = 0; i < seeds.size(); i++) {
                Seed s = seeds.get(i);
                list.add(new Attribute("topology", "seed" + i, s.toString()));
            }
        }
        if (segments != null && !segments.isEmpty()) {
            list.add(new Attribute("topology", "segments", String.valueOf(segments.size())));
            for (int i = 0; i < segments.size(); i++) {
                Segment s = segments.get(i);
                list.add(new Attribute("topology", "segment" + i, s.toString()));
            }
        }
        return list;
    }
}
