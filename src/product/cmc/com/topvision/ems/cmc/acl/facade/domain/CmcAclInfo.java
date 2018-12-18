/***********************************************************************
 * $Id: CmcAclInfo.java,v1.0 2013-4-20 下午03:32:39 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.acl.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzs
 * @created @2013-4-20-下午03:32:39
 * 
 */
@Alias("cmcAclInfo")
public class CmcAclInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2589385251165608999L;
    private Long entityId;

    /**
     * 1..192
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.1", index = true)
    private Integer topCcmtsAclListIndex;

    /**
     * 长度小于64的字字符串
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.2", type = "OctetString", writable = true)
    private String topCcmtsAclDesc;

    /**
     * 0..15 default 5
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.3", type = "Integer32", writable = true)
    private Integer topCcmtsAclPrio;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.4", type = "OctetString", writable = true)
    private String topMatchlSrcMac;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.5", type = "OctetString", writable = true)
    private String topMatchSrcMacMask;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.6", type = "OctetString", writable = true)
    private String topMatchDstMac;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.7", type = "OctetString", writable = true)
    private String topMatchDstMacMask;

    /**
     * 1..4095
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.8", type = "Integer32", writable = true)
    private Integer topMatchVlanId;

    /**
     * 0..8
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.9", type = "Integer32", writable = true)
    private Integer topMatchVlanCos;

    /**
     * 0..65535
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.10", type = "Integer32", writable = true)
    private Integer topMatchEtherType;

    /**
     * 0..64
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.11", type = "Integer32", writable = true)
    private Integer topMatchDscp;

    /**
     * 0..256
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.12", type = "Integer32", writable = true)
    private Integer topMatchIpProtocol;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.13", type = "IpAddress", writable = true)
    private String topMatchSrcIp;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.14", type = "IpAddress", writable = true)
    private String topMatchSrcIpMask;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.15", type = "IpAddress", writable = true)
    private String topMatchDstIp;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.16", type = "IpAddress", writable = true)
    private String topMatchDstIpMask;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.17", type = "Integer32", writable = true)
    private Integer topMatchSrcPort;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.18", type = "Integer32", writable = true)
    private Integer topMatchDstPort;

    private StringBuffer actionMaskBuffer = new StringBuffer("0000000000000000");

    /**
     * 动作使用topActionMask的位标识，增加几个额外属性将这个Mask拆分，利于与页面交互。 0，关闭，1，打开
     */

    private int actReplaceCos = 0;
    private int actRepVlanTpid = 0;
    private int actRepVlanCfi = 0;
    private int actRepVlanId = 0;

    private int actRepIpTos = 0;
    private int actRepIpDscp = 0;

    private int actAddVlan = 0;
    private int actRemoveVlan = 0;
    private int actCopyToCpu = 0;

    /**
     * 这个值用来与页面交互，默认为0，无动作，1 permit, 2 ,deny
     */
    private int actDenyOrPermit = 0;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.20", type = "Integer32", writable = true)
    private Integer topActionAddVlanId;

    /**
     * 0..8 新增vlan cos
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.21", type = "Integer32", writable = true)
    private Integer topActionAddVlanCos;

    /**
     * 替换vlan cos
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.22", type = "Integer32", writable = true)
    private Integer topActionNewVlanCos;

    /**
     * 替换vlan tpid 0..65535
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.25", type = "Integer32", writable = true)
    private Integer topActionNewVlanTpid;

    private String hexTopActionNewVlanTpid;

    /**
     * 替换vlan cfi 0..1
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.26", type = "Integer32", writable = true)
    private Integer topActionNewVlanCfi;

    /**
     * 替换vlan id 1..4094
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.27", type = "Integer32", writable = true)
    private Integer topActionNewVlanId;

    /**
     * 新增vlan tpid 0..65535
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.28", type = "Integer32", writable = true)
    private Integer topActionAddVlanTpid;

    private String hexTopActionAddVlanTpid;

    /**
     * 新增vlan cfi 0..1
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.29", type = "Integer32", writable = true)
    private Integer topActionAddVlanCfi;

    /**
     * 替换ip tos 0..255
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.30", type = "Integer32", writable = true)
    private Integer topActionNewIpTos;

    /**
     * 替换ip dscp 0..63
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.31", type = "Integer32", writable = true)
    private Integer topActionNewIpDscp;
    /**
     * BIT0: install acl on uplink ingress BIT1: install acl on uplink egress BIT2: install acl on
     * cable ingress BIT3: install acl on cable egress bit set to 1 represent install，set to 0
     * represent uninstall
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.23", type = "OctetString", writable = true)
    private String topInstallPosition;

    /**
     * 二进制的放置点字符串，临时存储使用，用来将四个独立的int转换成 一个oid 字符串
     */
    private StringBuffer installPosition = new StringBuffer("0000");

    /**
     * 提供四个独立的整形用来与页面交互
     */
    private int installPosionUpIn = 0;
    private int installPosionUpOut = 0;
    private int installPosionCaIn = 0;
    private int installPosionCaOut = 0;

    /**
     * 放置点默认动作
     */
    private int defActPosion = 0;
    private int defAct = 1;

    /**
     * 1: Deny(0) 2: Permit(1) 3: replaceCos(2) 4: addVlan(3) 5: removeVlan(4) 6: copyToCpu(5) 7:
     * replaceTpid(6) 8: replaceCfi(7) 9: replaceVlanId(8) 10: addVlanTpid(9) 11: addVlanCfi(10) 12:
     * replaceiptos(11) 13: replaceipdscp(12) other bits currently reserved.
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.19", type = "OctetString", writable = true)
    private String topActionMask;

    /**
     * Add/Del/Set
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.2.1.24", type = "Integer32", writable = true)
    private Integer topAclRowStatus;

    public Integer getTopCcmtsAclListIndex() {
        return topCcmtsAclListIndex;
    }

    public void setTopCcmtsAclListIndex(Integer topCcmtsAclListIndex) {
        this.topCcmtsAclListIndex = topCcmtsAclListIndex;
    }

    public String getTopCcmtsAclDesc() {
        return topCcmtsAclDesc;
    }

    public void setTopCcmtsAclDesc(String topCcmtsAclDesc) {
        if (topCcmtsAclDesc != null) {
            this.topCcmtsAclDesc = topCcmtsAclDesc.trim();
        }
    }

    public Integer getTopCcmtsAclPrio() {
        return topCcmtsAclPrio;
    }

    public void setTopCcmtsAclPrio(Integer topCcmtsAclPrio) {
        this.topCcmtsAclPrio = topCcmtsAclPrio;
    }

    public String getTopMatchlSrcMac() {

        return topMatchlSrcMac;
    }

    public void setTopMatchlSrcMac(String topMatchlSrcMac) {
        if (topMatchlSrcMac != null && !"".equals(topMatchlSrcMac)) {
            this.topMatchlSrcMac = topMatchlSrcMac;
        }
    }

    public String getTopMatchSrcMacMask() {
        return topMatchSrcMacMask;
    }

    public void setTopMatchSrcMacMask(String topMatchSrcMacMask) {
        if (topMatchSrcMacMask != null && !"".equals(topMatchSrcMacMask)) {
            this.topMatchSrcMacMask = topMatchSrcMacMask;
        }
    }

    public String getTopMatchDstMac() {
        return topMatchDstMac;
    }

    public void setTopMatchDstMac(String topMatchDstMac) {
        if (topMatchDstMac != null && !"".equals(topMatchDstMac)) {
            this.topMatchDstMac = topMatchDstMac;
        }
    }

    public String getTopMatchDstMacMask() {
        return topMatchDstMacMask;
    }

    public void setTopMatchDstMacMask(String topMatchDstMacMask) {
        if (topMatchDstMacMask != null && !"".equals(topMatchDstMacMask)) {
            this.topMatchDstMacMask = topMatchDstMacMask;
        }
    }

    public Integer getTopMatchVlanId() {
        return topMatchVlanId;
    }

    public void setTopMatchVlanId(Integer topMatchVlanId) {

        this.topMatchVlanId = topMatchVlanId;

    }

    public Integer getTopMatchVlanCos() {
        return topMatchVlanCos;
    }

    public void setTopMatchVlanCos(Integer topMatchVlanCos) {

        this.topMatchVlanCos = topMatchVlanCos;

    }

    public Integer getTopMatchEtherType() {
        return topMatchEtherType;
    }

    public void setTopMatchEtherType(Integer topMatchEtherType) {

        this.topMatchEtherType = topMatchEtherType;

    }

    public Integer getTopMatchDscp() {
        return topMatchDscp;
    }

    public void setTopMatchDscp(Integer topMatchDscp) {

        this.topMatchDscp = topMatchDscp;

    }

    public Integer getTopMatchIpProtocol() {
        return topMatchIpProtocol;
    }

    public void setTopMatchIpProtocol(Integer topMatchIpProtocol) {

        this.topMatchIpProtocol = topMatchIpProtocol;

    }

    public String getTopMatchSrcIp() {
        return topMatchSrcIp;
    }

    public void setTopMatchSrcIp(String topMatchSrcIp) {
        if (topMatchSrcIp != null && !"".equals(topMatchSrcIp)) {
            this.topMatchSrcIp = topMatchSrcIp;
        }

    }

    public String getTopMatchSrcIpMask() {
        return topMatchSrcIpMask;
    }

    public void setTopMatchSrcIpMask(String topMatchSrcIpMask) {
        if (topMatchSrcIpMask != null && !"".equals(topMatchSrcIpMask)) {
            this.topMatchSrcIpMask = topMatchSrcIpMask;
        }
    }

    public String getTopMatchDstIp() {
        return topMatchDstIp;
    }

    public void setTopMatchDstIp(String topMatchDstIp) {
        if (topMatchDstIp != null && !"".equals(topMatchDstIp)) {
            this.topMatchDstIp = topMatchDstIp;
        }
    }

    public String getTopMatchDstIpMask() {
        return topMatchDstIpMask;
    }

    public void setTopMatchDstIpMask(String topMatchDstIpMask) {
        if (topMatchDstIpMask != null && !"".equals(topMatchDstIpMask)) {
            this.topMatchDstIpMask = topMatchDstIpMask;
        }
    }

    public Integer getTopMatchSrcPort() {
        return topMatchSrcPort;
    }

    public void setTopMatchSrcPort(Integer topMatchSrcPort) {

        this.topMatchSrcPort = topMatchSrcPort;

    }

    public Integer getTopMatchDstPort() {
        return topMatchDstPort;
    }

    public void setTopMatchDstPort(Integer topMatchDstPort) {

        this.topMatchDstPort = topMatchDstPort;

    }

    public String getTopActionMask() {
        return topActionMask;
    }

    public StringBuffer getActionMaskBuffer() {
        return actionMaskBuffer;
    }

    public void setActionMaskBuffer(StringBuffer actionMaskBuffer) {
        this.actionMaskBuffer = actionMaskBuffer;
    }

    public void setTopActionMask(String topActionMask) {
        this.topActionMask = topActionMask;
    }

    public void setInstallPosition(StringBuffer installPosition) {
        this.installPosition = installPosition;
    }

    public void setTopActionMask(String topActionMask, Boolean isNewAclActionMask) {
        int[] mask;
        if (isNewAclActionMask != null && isNewAclActionMask) {
            mask = bits2array(this.topActionMask, true);
        } else {
            mask = bits2array(this.topActionMask, false);
        }

        if (mask[0] == 1) {
            actDenyOrPermit = 2;
        } else if (mask[1] == 1) {
            actDenyOrPermit = 1;
        } else {
            actDenyOrPermit = 0;
        }
        actReplaceCos = mask[2];
        actAddVlan = mask[3];
        actRemoveVlan = mask[4];
        actCopyToCpu = mask[5];
        actRepVlanTpid = mask[6];
        actRepVlanCfi = mask[7];
        actRepVlanId = mask[8];

        actRepIpTos = mask[11];
        actRepIpDscp = mask[12];

        /**
         * String temp = null; if (this.topActionMask != null) { try { String[] tmp =
         * topActionMask.split("\\:"); int i = Integer.parseInt(tmp[3], 16); temp =
         * Integer.toBinaryString(i); while (temp.length() < 6) { temp = "0" + temp; } } catch
         * (Exception e) { temp = "000000"; } } if (temp != null) {
         * 
         * 
         * int bit0 = Integer.parseInt(String.valueOf(temp.charAt(5))); int bit1 =
         * Integer.parseInt(String.valueOf(temp.charAt(4))); int bit2 =
         * Integer.parseInt(String.valueOf(temp.charAt(3))); int bit3 =
         * Integer.parseInt(String.valueOf(temp.charAt(2))); int bit4 =
         * Integer.parseInt(String.valueOf(temp.charAt(1))); int bit5 =
         * Integer.parseInt(String.valueOf(temp.charAt(0)));
         * 
         * if (bit0 == 1) { actDenyOrPermit = 2; } else if (bit1 == 1) { actDenyOrPermit = 1; } else
         * { actDenyOrPermit = 0; } actReplaceCos = bit2; actAddVlan = bit3; actRemoveVlan = bit4;
         * actCopyToCpu = bit5; actionMaskBuffer = new StringBuffer(temp);
         **/
    }

    public Integer getTopActionAddVlanId() {
        return topActionAddVlanId;
    }

    public void setTopActionAddVlanId(Integer topActionAddVlanId) {
        this.topActionAddVlanId = topActionAddVlanId;
    }

    public Integer getTopActionAddVlanCos() {
        return topActionAddVlanCos;
    }

    public void setTopActionAddVlanCos(Integer topActionAddVlanCos) {
        this.topActionAddVlanCos = topActionAddVlanCos;
    }

    public Integer getTopActionNewVlanCos() {
        return topActionNewVlanCos;
    }

    public void setTopActionNewVlanCos(Integer topActionNewVlanCos) {
        this.topActionNewVlanCos = topActionNewVlanCos;
    }

    public String getTopInstallPosition() {
        return topInstallPosition;
    }

    public void setTopInstallPosition(String topInstallPosition) {
        this.topInstallPosition = topInstallPosition;
    }

    /**
     * 数值格式："00:00:00:01" # 0x00 0x00 0x00 0x01
     * 
     * @param topInstallPosition
     */
    public void setTopInstallPosition(String topInstallPosition, Boolean isNewAclActionMask) {
        this.topInstallPosition = topInstallPosition;
        if (!isNewAclActionMask) {
            String temp = null;
            if (this.topInstallPosition != null) {
                try {
                    String[] tmp = topInstallPosition.split("\\:");

                    int i = Integer.parseInt(tmp[3], 16);
                    temp = Integer.toBinaryString(i);
                    while (temp.length() < 4) {
                        temp = "0" + temp;
                    }
                } catch (Exception e) {
                    temp = "0000";
                }
            }
            if (temp != null) {
                installPosionUpIn = Integer.parseInt(String.valueOf(temp.charAt(3)));
                installPosionUpOut = Integer.parseInt(String.valueOf(temp.charAt(2)));
                installPosionCaOut = Integer.parseInt(String.valueOf(temp.charAt(1)));
                installPosionCaIn = Integer.parseInt(String.valueOf(temp.charAt(0)));
                installPosition = new StringBuffer(temp);

            }
        } else {
            String temp = null;
            if (this.topInstallPosition != null) {
                try {
                    String[] tmp = topInstallPosition.split("\\:");
                    int i = Integer.parseInt(tmp[0], 16);
                    temp = Integer.toBinaryString(i);
                    while (temp.length() < 8) {
                        temp = "0" + temp;
                    }
                    temp = temp.substring(0, 4);
                } catch (Exception e) {
                    temp = "0000";
                }
            }
            if (temp != null) {
                installPosionUpIn = Integer.parseInt(String.valueOf(temp.charAt(0)));
                installPosionUpOut = Integer.parseInt(String.valueOf(temp.charAt(1)));
                installPosionCaOut = Integer.parseInt(String.valueOf(temp.charAt(2)));
                installPosionCaIn = Integer.parseInt(String.valueOf(temp.charAt(3)));
                installPosition = new StringBuffer();
                installPosition.append(installPosionCaIn).append(installPosionCaOut).append(installPosionUpOut)
                        .append(installPosionUpIn);

            }
        }

    }

    public String getInstallPosition() {
        return installPosition.toString();
    }

    public void setInstallPosition(String installPosition) {
        installPosionUpIn = Integer.parseInt(String.valueOf(installPosition.charAt(3)));
        installPosionUpOut = Integer.parseInt(String.valueOf(installPosition.charAt(2)));
        installPosionCaIn = Integer.parseInt(String.valueOf(installPosition.charAt(0)));
        installPosionCaOut = Integer.parseInt(String.valueOf(installPosition.charAt(1)));
        topInstallPosition = toHexString(installPosition.toString());
        this.installPosition = new StringBuffer(installPosition);
    }

    public Integer getTopAclRowStatus() {
        return topAclRowStatus;
    }

    public void setTopAclRowStatus(Integer topAclRowStatus) {
        this.topAclRowStatus = topAclRowStatus;
    }

    private String toHexString(String binStr) {

        StringBuffer sb = new StringBuffer();

        int m = Integer.parseInt(binStr, 2);
        sb = new StringBuffer(Integer.toHexString(m));

        while (sb.length() < 8) {
            sb.insert(0, "0");
        }
        sb = sb.insert(2, ":");
        sb = sb.insert(5, ":");
        sb = sb.insert(8, ":");
        return sb.toString().toUpperCase();

    }

    public int getActReplaceCos() {
        return actReplaceCos;
    }

    public void setActReplaceCos(int actReplaceCos) {
        this.actReplaceCos = actReplaceCos;
        if (actReplaceCos == 1) {
            actionMaskBuffer.setCharAt(13, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }

    }

    public int getActRepVlanTpid() {
        return actRepVlanTpid;
    }

    public void setActRepVlanTpid(int actRepVlanTpid) {
        this.actRepVlanTpid = actRepVlanTpid;
        if (actRepVlanTpid == 1) {
            actionMaskBuffer.setCharAt(9, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActRepVlanCfi() {
        return actRepVlanCfi;
    }

    public void setActRepVlanCfi(int actRepVlanCfi) {
        this.actRepVlanCfi = actRepVlanCfi;
        if (actRepVlanCfi == 1) {
            actionMaskBuffer.setCharAt(8, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActRepVlanId() {
        return actRepVlanId;
    }

    public void setActRepVlanId(int actRepVlanId) {
        this.actRepVlanId = actRepVlanId;
        if (actRepVlanId == 1) {
            actionMaskBuffer.setCharAt(7, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActRepIpTos() {
        return actRepIpTos;
    }

    public void setActRepIpTos(int actRepIpTos) {
        this.actRepIpTos = actRepIpTos;
        if (actRepIpTos == 1) {
            actionMaskBuffer.setCharAt(4, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActRepIpDscp() {
        return actRepIpDscp;
    }

    public void setActRepIpDscp(int actRepIpDscp) {
        this.actRepIpDscp = actRepIpDscp;
        if (actRepIpDscp == 1) {
            actionMaskBuffer.setCharAt(3, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public Integer getTopActionNewVlanTpid() {
        return topActionNewVlanTpid;
    }

    public void setTopActionNewVlanTpid(Integer topActionNewVlanTpid) {
        this.topActionNewVlanTpid = topActionNewVlanTpid;
        if (topActionNewVlanTpid != null) {
            hexTopActionNewVlanTpid = Integer.toHexString(topActionNewVlanTpid);
        }
    }

    public String getHexTopActionNewVlanTpid() {
        return hexTopActionNewVlanTpid;
    }

    public void setHexTopActionNewVlanTpid(String hexTopActionNewVlanTpid) {
        this.hexTopActionNewVlanTpid = hexTopActionNewVlanTpid;
        if (hexTopActionNewVlanTpid != null) {
            topActionNewVlanTpid = Integer.parseInt(hexTopActionNewVlanTpid, 16);
        }
    }

    public Integer getTopActionNewVlanCfi() {
        return topActionNewVlanCfi;
    }

    public void setTopActionNewVlanCfi(Integer topActionNewVlanCfi) {
        this.topActionNewVlanCfi = topActionNewVlanCfi;
    }

    public Integer getTopActionNewVlanId() {
        return topActionNewVlanId;
    }

    public void setTopActionNewVlanId(Integer topActionNewVlanId) {
        this.topActionNewVlanId = topActionNewVlanId;
    }

    public Integer getTopActionAddVlanTpid() {
        return topActionAddVlanTpid;
    }

    public void setTopActionAddVlanTpid(Integer topActionAddVlanTpid) {
        this.topActionAddVlanTpid = topActionAddVlanTpid;

        if (topActionAddVlanTpid != null) {
            hexTopActionAddVlanTpid = Integer.toHexString(topActionAddVlanTpid);
        }
    }

    public String getHexTopActionAddVlanTpid() {
        return hexTopActionAddVlanTpid;
    }

    public void setHexTopActionAddVlanTpid(String hexTopActionAddVlanTpid) {
        this.hexTopActionAddVlanTpid = hexTopActionAddVlanTpid;

        if (hexTopActionAddVlanTpid != null) {
            topActionAddVlanTpid = Integer.parseInt(hexTopActionAddVlanTpid, 16);
        }
    }

    public Integer getTopActionAddVlanCfi() {
        return topActionAddVlanCfi;
    }

    public void setTopActionAddVlanCfi(Integer topActionAddVlanCfi) {
        this.topActionAddVlanCfi = topActionAddVlanCfi;
    }

    public Integer getTopActionNewIpTos() {
        return topActionNewIpTos;
    }

    public void setTopActionNewIpTos(Integer topActionNewIpTos) {
        this.topActionNewIpTos = topActionNewIpTos;
    }

    public Integer getTopActionNewIpDscp() {
        return topActionNewIpDscp;
    }

    public void setTopActionNewIpDscp(Integer topActionNewIpDscp) {
        this.topActionNewIpDscp = topActionNewIpDscp;
    }

    public int getActAddVlan() {
        return actAddVlan;
    }

    public void setActAddVlan(int actAddVlan) {
        this.actAddVlan = actAddVlan;
        if (actAddVlan == 1) {
            actionMaskBuffer.setCharAt(12, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActRemoveVlan() {
        return actRemoveVlan;
    }

    public void setActRemoveVlan(int actRemoveVlan) {
        this.actRemoveVlan = actRemoveVlan;
        if (actRemoveVlan == 1) {
            actionMaskBuffer.setCharAt(11, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActCopyToCpu() {
        return actCopyToCpu;
    }

    public void setActCopyToCpu(int actCopyToCpu) {
        this.actCopyToCpu = actCopyToCpu;
        if (actCopyToCpu == 1) {
            actionMaskBuffer.setCharAt(10, '1');
            topActionMask = toHexString(actionMaskBuffer.toString());
        }
    }

    public int getActDenyOrPermit() {
        return actDenyOrPermit;
    }

    public void setActDenyOrPermit(int actDenyOrPermit) {
        this.actDenyOrPermit = actDenyOrPermit;

        if (actDenyOrPermit == 1) {
            actionMaskBuffer.setCharAt(14, '1');
        } else if (actDenyOrPermit == 2) {
            actionMaskBuffer.setCharAt(15, '1');
        }
        // if (actDenyOrPermit != 0) {
        topActionMask = toHexString(actionMaskBuffer.toString());

        // }

    }

    public int getInstallPosionUpIn() {
        return installPosionUpIn;
    }

    public void setInstallPosionUpIn(int installPosionUpIn) {
        this.installPosionUpIn = installPosionUpIn;

        installPosition.replace(3, 4, String.valueOf(installPosionUpIn));
        topInstallPosition = toHexString(installPosition.toString());

    }

    public int getInstallPosionUpOut() {
        return installPosionUpOut;
    }

    public void setInstallPosionUpOut(int installPosionUpOut) {
        this.installPosionUpOut = installPosionUpOut;

        installPosition.replace(2, 3, String.valueOf(installPosionUpOut));
        topInstallPosition = toHexString(installPosition.toString());

    }

    public int getInstallPosionCaIn() {
        return installPosionCaIn;
    }

    public void setInstallPosionCaIn(int installPosionCaIn) {
        this.installPosionCaIn = installPosionCaIn;

        installPosition.replace(0, 1, String.valueOf(installPosionCaIn));
        topInstallPosition = toHexString(installPosition.toString());

    }

    public int getInstallPosionCaOut() {
        return installPosionCaOut;
    }

    public void setInstallPosionCaOut(int installPosionCaOut) {
        this.installPosionCaOut = installPosionCaOut;

        installPosition.replace(1, 2, String.valueOf(installPosionCaOut));
        topInstallPosition = toHexString(installPosition.toString());

    }

    public int getDefActPosion() {
        return defActPosion;
    }

    public void setDefActPosion(int defActPosion) {
        this.defActPosion = defActPosion;
    }

    public int getDefAct() {
        return defAct;
    }

    public void setDefAct(int defAct) {
        this.defAct = defAct;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * 当MIB节点的数据类型是BITS，采集时得到的是一个字符串，将这个字符串转为整形数组
     * 
     * 数值格式："00:00:00:01" MIBBrowser格式：# 0x00 0x00 0x00 0x01
     * 
     * @param bitStr
     * @param isLeftZero
     *            true:左边起是第零位，false: 右面起是第零位
     * @return
     */
    private int[] bits2array(String bitStr, boolean isLeftZero) {
        int[] array = null;
        String[] tmp = bitStr.split("\\:");
        if (tmp != null && tmp.length > 0) {
            array = new int[tmp.length * 8];
        }
        StringBuffer sb = new StringBuffer();
        for (String s : tmp) {
            int i = Integer.parseInt(s, 16);
            StringBuffer bStr = new StringBuffer(Integer.toBinaryString(i));
            while (bStr.length() < 8) {
                bStr = bStr.insert(0, "0");
            }
            sb.append(bStr);
        }
        if (!isLeftZero) {
            sb = sb.reverse();
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(String.valueOf(sb.charAt(i)));
        }
        return array;
    }

    public static void main(String[] args) {
        CmcAclInfo nn = new CmcAclInfo();
        nn.bits2array("00:00:01:10", true);
    }

    /**
     * 主要针对匹配规则中前端置空后传递给mib需要传递为一些默认值
     */
    public void convertToMibDefaultValue() {
        // 如果源IP地址和源IP地址掩码均为空，则将其全部置为0.0.0.0
        if ((null == this.topMatchSrcIp || "" == this.topMatchSrcIp)
                && (null == this.topMatchSrcIpMask || "" == this.topMatchSrcIpMask)) {
            this.topMatchSrcIp = "0.0.0.0";
            this.topMatchSrcIpMask = "0.0.0.0";
        }
        // 如果目的IP地址和目的IP地址掩码均为空，则将其全部置为0.0.0.0
        if ((null == this.topMatchDstIp || "" == this.topMatchDstIp)
                && (null == this.topMatchDstIpMask || "" == this.topMatchDstIpMask)) {
            this.topMatchDstIp = "0.0.0.0";
            this.topMatchDstIpMask = "0.0.0.0";
        }
        // 如果源MAC地址和源MAC地址掩码均为空，则将其全部置为00:00:00:00:00:00
        if ((null == this.topMatchlSrcMac || "" == this.topMatchlSrcMac)
                && (null == this.topMatchSrcMacMask || "" == this.topMatchSrcMacMask)) {
            this.topMatchlSrcMac = "00:00:00:00:00:00";
            this.topMatchSrcMacMask = "00:00:00:00:00:00";
        }
        // 如果目的MAC地址和目的MAC地址掩码均为空，则将其全部置为00:00:00:00:00:00
        if ((null == this.topMatchDstMac || "" == this.topMatchDstMac)
                && (null == this.topMatchDstMacMask || "" == this.topMatchDstMacMask)) {
            this.topMatchDstMac = "00:00:00:00:00:00";
            this.topMatchDstMacMask = "00:00:00:00:00:00";
        }
        // 如果源端口号为空，则置为65536
        if (null == this.topMatchSrcPort) {
            this.topMatchSrcPort = 65536;
        }
        // 如果目的端口号为空，则置为65536
        if (null == this.topMatchDstPort) {
            this.topMatchDstPort = 65536;
        }
        // 如果VLAN ID为空，则置为4095
        if (null == this.topMatchVlanId) {
            this.topMatchVlanId = 4095;
        }
        // 如果VLAN COS为空，则置为8
        if (null == this.topMatchVlanCos) {
            this.topMatchVlanCos = 8;
        }
        // 如果EtherType为空，则置为0
        if (null == this.topMatchEtherType) {
            this.topMatchEtherType = 0;
        }
        // 如果IP协议字段为空，则置为256
        if (null == this.topMatchIpProtocol) {
            this.topMatchIpProtocol = 256;
        }
        // 如果DSCP为空，则置为64
        if (null == this.topMatchDscp) {
            this.topMatchDscp = 64;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcAclInfo [entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsAclListIndex=");
        builder.append(topCcmtsAclListIndex);
        builder.append(", topCcmtsAclDesc=");
        builder.append(topCcmtsAclDesc);
        builder.append(", topCcmtsAclPrio=");
        builder.append(topCcmtsAclPrio);
        builder.append(", topMatchlSrcMac=");
        builder.append(topMatchlSrcMac);
        builder.append(", topMatchSrcMacMask=");
        builder.append(topMatchSrcMacMask);
        builder.append(", topMatchDstMac=");
        builder.append(topMatchDstMac);
        builder.append(", topMatchDstMacMask=");
        builder.append(topMatchDstMacMask);
        builder.append(", topMatchVlanId=");
        builder.append(topMatchVlanId);
        builder.append(", topMatchVlanCos=");
        builder.append(topMatchVlanCos);
        builder.append(", topMatchEtherType=");
        builder.append(topMatchEtherType);
        builder.append(", topMatchDscp=");
        builder.append(topMatchDscp);
        builder.append(", topMatchIpProtocol=");
        builder.append(topMatchIpProtocol);
        builder.append(", topMatchSrcIp=");
        builder.append(topMatchSrcIp);
        builder.append(", topMatchSrcIpMask=");
        builder.append(topMatchSrcIpMask);
        builder.append(", topMatchDstIp=");
        builder.append(topMatchDstIp);
        builder.append(", topMatchDstIpMask=");
        builder.append(topMatchDstIpMask);
        builder.append(", topMatchSrcPort=");
        builder.append(topMatchSrcPort);
        builder.append(", topMatchDstPort=");
        builder.append(topMatchDstPort);
        builder.append(", topActionMask=");
        builder.append(topActionMask);
        builder.append(", actionMaskBuffer=");
        builder.append(actionMaskBuffer);
        builder.append(", actReplaceCos=");
        builder.append(actReplaceCos);
        builder.append(", actRepVlanTpid=");
        builder.append(actRepVlanTpid);
        builder.append(", actRepVlanCfi=");
        builder.append(actRepVlanCfi);
        builder.append(", actRepVlanId=");
        builder.append(actRepVlanId);
        builder.append(", actRepIpTos=");
        builder.append(actRepIpTos);
        builder.append(", actRepIpDscp=");
        builder.append(actRepIpDscp);
        builder.append(", actAddVlan=");
        builder.append(actAddVlan);
        builder.append(", actRemoveVlan=");
        builder.append(actRemoveVlan);
        builder.append(", actCopyToCpu=");
        builder.append(actCopyToCpu);
        builder.append(", actDenyOrPermit=");
        builder.append(actDenyOrPermit);
        builder.append(", topActionAddVlanId=");
        builder.append(topActionAddVlanId);
        builder.append(", topActionAddVlanCos=");
        builder.append(topActionAddVlanCos);
        builder.append(", topActionNewVlanCos=");
        builder.append(topActionNewVlanCos);
        builder.append(", topActionNewVlanTpid=");
        builder.append(topActionNewVlanTpid);
        builder.append(", hexTopActionNewVlanTpid=");
        builder.append(hexTopActionNewVlanTpid);
        builder.append(", topActionNewVlanCfi=");
        builder.append(topActionNewVlanCfi);
        builder.append(", topActionNewVlanId=");
        builder.append(topActionNewVlanId);
        builder.append(", topActionAddVlanTpid=");
        builder.append(topActionAddVlanTpid);
        builder.append(", hexTopActionAddVlanTpid=");
        builder.append(hexTopActionAddVlanTpid);
        builder.append(", topActionAddVlanCfi=");
        builder.append(topActionAddVlanCfi);
        builder.append(", topActionNewIpTos=");
        builder.append(topActionNewIpTos);
        builder.append(", topActionNewIpDscp=");
        builder.append(topActionNewIpDscp);
        builder.append(", topInstallPosition=");
        builder.append(topInstallPosition);
        builder.append(", installPosition=");
        builder.append(installPosition);
        builder.append(", installPosionUpIn=");
        builder.append(installPosionUpIn);
        builder.append(", installPosionUpOut=");
        builder.append(installPosionUpOut);
        builder.append(", installPosionCaIn=");
        builder.append(installPosionCaIn);
        builder.append(", installPosionCaOut=");
        builder.append(installPosionCaOut);
        builder.append(", defActPosion=");
        builder.append(defActPosion);
        builder.append(", defAct=");
        builder.append(defAct);
        builder.append(", topAclRowStatus=");
        builder.append(topAclRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
