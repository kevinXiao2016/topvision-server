/***********************************************************************
 * $Id: IpRoute.java,v1.0 2013-11-16 下午02:44:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-11-16-下午02:44:17
 * 
 */
public class IpRoute implements AliasesSuperType {
    private static final long serialVersionUID = 6016801460747564680L;

    public static final Integer STATIC_IPROUTE = 3;
    private Long entityId;
    private String ipAddressDst;
    private String ipMaskDst;
    private String nextHop;
    private Integer distance;
    private Integer track;
    private Integer routeType;
    private Integer flag;

    public IpRoute() {

    }

    public IpRoute(Long entityId, String ipAddressDst, String ipMaskDst, String nextHop, Integer distance,
            Integer routeType, Integer flag) {
        this.entityId = entityId;
        this.ipAddressDst = ipAddressDst;
        this.ipMaskDst = ipMaskDst;
        this.nextHop = nextHop;
        this.distance = distance;
        this.routeType = routeType;
        this.flag = flag;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the ipAddressDst
     */
    public String getIpAddressDst() {
        return ipAddressDst;
    }

    /**
     * @param ipAddressDst
     *            the ipAddressDst to set
     */
    public void setIpAddressDst(String ipAddressDst) {
        this.ipAddressDst = ipAddressDst;
    }

    /**
     * @return the ipMaskDst
     */
    public String getIpMaskDst() {
        return ipMaskDst;
    }

    /**
     * @param ipMaskDst
     *            the ipMaskDst to set
     */
    public void setIpMaskDst(String ipMaskDst) {
        this.ipMaskDst = ipMaskDst;
    }

    /**
     * @return the nextHop
     */
    public String getNextHop() {
        return nextHop;
    }

    /**
     * @param nextHop
     *            the nextHop to set
     */
    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

    /**
     * @return the distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance
     *            the distance to set
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * @return the track
     */
    public Integer getTrack() {
        return track;
    }

    /**
     * @param track
     *            the track to set
     */
    public void setTrack(Integer track) {
        this.track = track;
    }

    /**
     * @return the routeType
     */
    public Integer getRouteType() {
        return routeType;
    }

    /**
     * @param routeType
     *            the routeType to set
     */
    public void setRouteType(Integer routeType) {
        this.routeType = routeType;
    }

    /**
     * @return the flag
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * @param flag
     *            the flag to set
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IpRoute [entityId=");
        builder.append(entityId);
        builder.append(", ipAddressDst=");
        builder.append(ipAddressDst);
        builder.append(", ipMaskDst=");
        builder.append(ipMaskDst);
        builder.append(", nextHop=");
        builder.append(nextHop);
        builder.append(", distance=");
        builder.append(distance);
        builder.append(", track=");
        builder.append(track);
        builder.append(", routeType=");
        builder.append(routeType);
        builder.append(", flag=");
        builder.append(flag);
        builder.append("]");
        return builder.toString();
    }

    public static List<IpRoute> getIpRoutesFromRecord(Long entityId, List<IpRouteRecord> records) {
        List<IpRoute> routes = new ArrayList<IpRoute>();
        for (IpRouteRecord record : records) {
            for (int i = 0; i < record.getTopIPRouteBlockRealCount(); i++) {
                byte[] info = record.getTopIPRouteBlockInfoList();
                String ipAddressDst = EponUtil.getIpAddressFormByte(info[i * 12], info[i * 12 + 1], info[i * 12 + 2],
                        info[i * 12 + 3]);
                Integer intMask = info[i * 12 + 4] & 0xFF;
                String ipMaskDst = parseIntMaskToIp(intMask);
                String nextHop = EponUtil.getIpAddressFormByte(info[i * 12 + 5], info[i * 12 + 6], info[i * 12 + 7],
                        info[i * 12 + 8]);
                Integer distance = info[i * 12 + 9] & 0xFF;
                Integer routeType = info[i * 12 + 10] & 0xFF;
                Integer flag = info[i * 12 + 11] & 0xFF;
                IpRoute tmp = new IpRoute(entityId, ipAddressDst, ipMaskDst, nextHop, distance, routeType, flag);
                routes.add(tmp);
            }
        }
        return routes;
    }

    /**
     * 将整数表示的掩码形式(如16,24,32)转换为IP表示方式(如255.255.0.0)
     * @param mask
     * @return
     */
    private static String parseIntMaskToIp(int imask) {
        if (imask < 0) {
            imask = 0;
        } else if (imask > 32) {
            imask = 32;
        }
        StringBuilder bmask = new StringBuilder();
        for (int i = 0; i < imask; i++) {
            bmask.append("1");
        }
        bmask.append("00000000000000000000000000000000");
        String mask = Integer.parseInt(bmask.substring(0, 8), 2) + "." + Integer.parseInt(bmask.substring(8, 16), 2)
                + "." + Integer.parseInt(bmask.substring(16, 24), 2) + "."
                + Integer.parseInt(bmask.substring(24, 32), 2);
        return mask;
    }

}
