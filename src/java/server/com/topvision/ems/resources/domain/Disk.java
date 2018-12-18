package com.topvision.ems.resources.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.topvision.framework.domain.BaseEntity;

/**
 * 
 * @Create Date Nov 1, 2008 6:26:43 PM
 * 
 * @author kelers
 * 
 */
public class Disk extends BaseEntity {
    private static final long serialVersionUID = -7526560880133166270L;
    private String name;
    private String label;
    private byte type;// 4-本地磁盘，5-移动磁盘，6-软驱，7-光驱
    private float capacity;
    private float used;
    private float spare;
    public static NumberFormat format = new DecimalFormat("###.##");

    public float getCapacity() {
        return capacity;
    }

    public String getCapacityString() {
        StringBuilder capacityStr = new StringBuilder();
        if (capacity < 1024) {
            capacityStr.append(format.format(capacity)).append("Bytes");
        } else if (capacity < 1048576) {
            capacityStr.append(format.format(capacity / 1024.0)).append("KB");
        } else if (capacity < 1073741824) {
            capacityStr.append(format.format(capacity / 1048576.0)).append("MB");
        } else {
            capacityStr.append(format.format(capacity / 1073741824.0)).append("GB");
        }
        return capacityStr.toString();
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    public String getName() {
        if (name == null || "".equals(name)) {
            return getTypeString() + " (" + label + ")";
        } else {
            return name + " (" + label + ")";
        }
    }

    public float getSpare() {
        return spare;
    }

    public String getSpareString() {
        StringBuilder usedStr = new StringBuilder();
        spare = capacity - used;
        if (spare < 0) {
            spare = 0;
        }
        if (spare < 1024) {
            usedStr.append(format.format(spare)).append("Bytes");
        } else if (spare < 1048576) {
            usedStr.append(format.format(spare / 1024.0)).append("KB");
        } else if (spare < 1073741824) {
            usedStr.append(format.format(spare / 1048576.0)).append("MB");
        } else {
            usedStr.append(format.format(spare / 1073741824.0)).append("GB");
        }
        return usedStr.toString();
    }

    public int getType() {
        return type;
    }

    public String getTypeString() {
        switch (type) {
        case 4:
            return "FixedDisk";
        case 5:
            return "RemovableDisk";
        case 6:
            return "FloppyDisk";
        case 7:
            return "CompactDisc";
        default:
            return "UnknownDisk";
        }
    }

    public float getUsage() {
        return used / (capacity == 0 ? 1 : capacity);
    }

    public String getUsageString() {
        return NumberFormat.getPercentInstance().format(used / (capacity == 0 ? 1 : capacity));
    }

    public float getUsed() {
        return used;
    }

    public String getUsedString() {
        StringBuilder usedStr = new StringBuilder();
        if (used < 1024) {
            usedStr.append(format.format(used)).append("Bytes");
        } else if (used < 1048576) {
            usedStr.append(format.format(used / 1024.0)).append("KB");
        } else if (used < 1073741824) {
            usedStr.append(format.format(used / 1048576.0)).append("MB");
        } else {
            usedStr.append(format.format(used / 1073741824.0)).append("GB");
        }
        return usedStr.toString();
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpare(float spare) {
        this.spare = spare;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setUsed(float used) {
        this.used = used;
    }

}
