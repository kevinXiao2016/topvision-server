package com.topvision.ems.cmc.spectrum.facade.domain;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author bryan
 */
public class SpectrumIIIData implements Serializable {
    private static final long serialVersionUID = 9143821457510086278L;
    public static final Byte UdpVersion = 0;

    public static final Byte GetRequest = 0;
    public static final Byte GetNextRequest = 1;
    public static final Byte GetResponse = 2;

    public static final Integer NoError = 0;
    public static final Integer ToBigError = 1;
    public static final Integer NoSuchNameError = 2;
    public static final Integer GenError = 3;

    public static final int byteLegth = 1;
    public static final int shortLegth = 2;
    public static final int intLength = 4;
    public static final int requestPacketLength = 46;

    public static final int versionLength = byteLegth;
    public static final int communityLength = byteLegth * 32;
    public static final int reservedLength = shortLegth;
    public static final int pduTypeLength = byteLegth;
    public static final int requestIdLength = shortLegth;
    public static final int errorStatusLength = intLength;
    public static final int cmcIndexLength = intLength;
    public static final int dataLengthLength = shortLegth;

    public static final int startIndex = 0;
    public static final int versionIndex = startIndex;
    public static final int communityIndex = versionIndex + versionLength;
    public static final int reservedIndex = communityIndex + communityLength;
    public static final int pduTypeIndex = reservedIndex + reservedLength;
    public static final int requestIdIndex = pduTypeIndex + pduTypeLength;
    public static final int errorStatusIndex = requestIdIndex + requestIdLength;
    public static final int cmcIndexIndex = errorStatusIndex + errorStatusLength;
    public static final int dataLengthIndex = errorStatusIndex + errorStatusLength;
    public static final int dataIndex = errorStatusIndex + errorStatusLength;

    public static short nextRequestId = 0;

    private Long cmcId;

    private ByteBuffer bytes;
    private Integer length;

    public SpectrumIIIData(Integer length) {
        setLength(length);
    }

    public SpectrumIIIData(byte[] bytes) {
        wrap(bytes);
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Byte getVersion() {
        return this.bytes.get(versionIndex);
    }

    public void setVersion(Byte version) {
        this.bytes.put(versionIndex,version);
    }

    public String getCommunity() {
        byte[] str = new byte[communityLength];
        for	(int i = 0; i < str.length; i++) {
        	str[i] = this.bytes.get(i + communityIndex);
        }
        return new String(str);
    }

    public void setCommunity(String community) {
        byte[] str = community.getBytes();
        //this.bytes.put(str, communityIndex, str.length);
        for	(int i = 0; i < str.length; i++) {
        	this.bytes.put(i + communityIndex,str[i]);
        }
    }

    public Byte getPduType() {
        return this.bytes.get(pduTypeIndex);
    }

    public void setPduType(Byte pduType) {
        this.bytes.put(pduTypeIndex,pduType);
    }

    public Short getRequestId() {
        return this.bytes.getShort(requestIdIndex);
    }

    public void setRequestId(Short requestId) {
        this.bytes.putShort(requestIdIndex, requestId);
    }

    public Integer getErrorStatus() {
        return this.bytes.getInt(errorStatusIndex);
    }

    public void setErrorStatus(Integer errorStatus) {
        this.bytes.putInt(errorStatusIndex, errorStatus);
    }

    public Integer getCmcIndex() {
        return this.bytes.getInt(cmcIndexIndex);
    }

    public void setCmcIndex(Integer cmcIndex) {
        this.bytes.putInt(cmcIndexIndex, cmcIndex);
    }

    public String getDataBuffer(Short dataLength) {
        return readNByteString(dataIndex,dataLength + dataLengthLength);
    }

    public Integer getLength() {
        return length;
    }

    private void setLength(Integer length) {
        this.length = length;
        this.bytes = ByteBuffer.allocate(length);
        for (int i = 0; i < length ; i++) {
            bytes.put(i,(byte)0);
        }
    }

    public byte[] getBytes() {
        return bytes.array();
    }

    public void setBytes(byte[] bytes) {
        this.bytes.wrap(bytes);
    }

    public short getDataLength() {
        return this.bytes.getShort(dataLengthIndex);
    }

    public static short getNextRequestId() {
        short re = nextRequestId;
        if (nextRequestId == Short.MAX_VALUE ) {
            nextRequestId = 0;
        } else {
            nextRequestId++;
        }
        return re;
    }

    public String readNByteString(int index,int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = index ;i < index + n ; i++) {
            String stmp = String.format("%02X",this.bytes.get(i));
            sb.append(stmp);
        }
        long l = sb.length();
        return sb.toString();
    }

    private void wrap(byte[] spectrumData) {
        this.bytes = ByteBuffer.wrap(spectrumData);
        this.length = spectrumData.length;
    }
}
