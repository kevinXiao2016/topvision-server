/***********************************************************************
 * $Id: OnuUtil.java,v1.0 2016年6月7日 下午7:20:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年6月7日-下午7:20:37
 *
 */
public class OnuUtil {

    public static JSONObject getOnuListTree(List<OltOnuAttribute> oltOnuList, String displayStyle) {
        JSONObject jsonObject = new JSONObject();
        // initialize entityList
        JSONArray entityList = new JSONArray();
        Map<Long, Map<String, JSONArray>> dataMap = new TreeMap<Long, Map<String, JSONArray>>();
        for (OltOnuAttribute o : oltOnuList) {
            JSONObject json = new JSONObject();
            json.put("entityId", o.getOnuId());
            json.put("parentId", o.getEntityId());
            String mac = MacUtils.convertMacToDisplayFormat(o.getOnuMac(), displayStyle);
            json.put("onuMac", mac);
            json.put("sn", o.getOnuUniqueIdentification());
            json.put("entityType", o.getOnuPreType());
            json.put("typeId", o.getTypeId());
            json.put("adminStatus", o.getOnuAdminStatus());
            json.put("entityName", o.getOnuName());
            json.put("onuEorG", o.getOnuEorG());
            json.put("oprationStatus", o.getOnuOperationStatus());
            entityList.add(json);

            Long onuIndex = o.getOnuIndex();
            Long slotNo = EponIndex.getSlotNo(onuIndex);
            String portString = EponIndex.getPortStringByIndex(onuIndex).toString();
            if (dataMap.containsKey(slotNo)) {
                Map<String, JSONArray> portData = dataMap.get(slotNo);
                if (portData.containsKey(portString)) {
                    portData.get(portString).add(json);
                } else {
                    JSONArray portJson = new JSONArray();
                    portJson.add(json);
                    portData.put(portString, portJson);
                }
            } else {
                Map<String, JSONArray> portData = new LinkedHashMap<String, JSONArray>();
                JSONArray portJson = new JSONArray();
                portJson.add(json);
                portData.put(portString, portJson);
                dataMap.put(slotNo, portData);
            }
        }
        JSONArray subListArray = new JSONArray();
        for (Entry<Long, Map<String, JSONArray>> slotEntry : dataMap.entrySet()) {
            JSONObject slotObject = new JSONObject();
            Map<String, JSONArray> portMap = slotEntry.getValue();
            JSONArray portArray = new JSONArray();
            for (Entry<String, JSONArray> portEntry : portMap.entrySet()) {
                JSONObject portObject = new JSONObject();
                portObject.put("portNo", portEntry.getKey());
                portObject.put("children", portEntry.getValue());
                portArray.add(portObject);
            }
            slotObject.put("slotNo", slotEntry.getKey());
            slotObject.put("children", portArray);
            subListArray.add(slotObject);
        }
        jsonObject.put("tree", subListArray);
        jsonObject.put("entities", entityList);
        return jsonObject;
    }
}
