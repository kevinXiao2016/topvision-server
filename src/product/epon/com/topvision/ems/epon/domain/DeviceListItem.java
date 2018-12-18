/**
 * 
 */
package com.topvision.ems.epon.domain;

import java.text.SimpleDateFormat;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author niejun
 * 
 */
public class DeviceListItem extends Entity implements AliasesSuperType {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final long serialVersionUID = 7517635609278086380L;

    @Override
    public String getName() {
        return getIp().equals(super.getName()) ? "" : super.getName();
    }

    public String getCreateTimeString() {
        if (getCreateTime() != null) {
            return sdf.format(getCreateTime());
        } else {
            return "";
        }
    }

}
