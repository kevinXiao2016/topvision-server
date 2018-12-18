/***********************************************************************
 * $Id: OltPerfAction.java,v1.0 2013-10-25 下午3:41:18 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.OltPerf;
import com.topvision.ems.epon.domain.OltPerfThreshold;
import com.topvision.ems.epon.exception.ModifyPerfThresholdException;
import com.topvision.ems.epon.exception.RefreshPerfThresholdException;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.performance.service.OltPerfService;
import com.topvision.ems.performance.action.PerfThresholdAction;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author flack
 * @created @2013-10-25-下午3:41:18
 *
 */
@Controller("oltPerfThresholdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPerfThresholdAction extends PerfThresholdAction {
    private static final long serialVersionUID = 2636440424182411261L;
    private static final Logger logger = LoggerFactory.getLogger(OltPerfThresholdAction.class);

    @Autowired
    private OltService oltService;
    @Autowired
    private OltPerfService oltPerfService;
    @Autowired
    private EntityTypeService entityTypeService;

    private Long entityId;
    private String deviceType;
    private String thresholdType;
    private Integer perfThresholdTypeIndex;
    private Integer perfThresholdObject;
    private String perfThresholdUpper;
    private String perfThresholdLower;
    private Integer operationResult;
    private String perfName;
    private String entityIp;
    private Integer tempRela;

    /**
     * OLT性能阈值模板
     * @return
     */
    public String showOltPerfTemp() {
        return SUCCESS;
    }

    /**
     * 
     * 显示阈值配置页面
     * 
     * @return
     */
    public String showPerfThresholdJsp() {
        OltAttribute oltAttribute = oltService.getOltAttribute(entityId);
        deviceType = oltAttribute.getOltType();
        // if (deviceType == null) {
        // deviceType = "";
        // }
        return SUCCESS;
    }

    /**
     * 
     * 显示阈值配置修改页面
     * 
     * @return
     */
    public String showPerfThresholdModifyJsp() {
        return SUCCESS;
    }

    /**
     * 通过对象类型载入阈值设置 JSON调用
     * 
     * @return
     */
    public String loadPerfThresholdByType() throws IOException {
        // 通过阈值类型读取阈值列表
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        // 获得阈值列表
        List<OltPerfThreshold> re = oltPerfService.getPerfThresholdByType(entityId, thresholdType);
        // 通过自定义方法，将阈值对象中的指标英文名称转换为中文名称，读取国际化文件
        for (Iterator<OltPerfThreshold> it = re.iterator(); it.hasNext();) {
            OltPerfThreshold oltPerfThreshold = it.next();
            if (deviceType != null && !deviceType.equalsIgnoreCase("") && deviceType.equalsIgnoreCase("PN8602")
                    && oltPerfThreshold.getPerfThresholdTypeIndex().equals(50)
                    && oltPerfThreshold.getPerfThresholdObject().equals(3)) {
                it.remove();
            } else {
                oltPerfThreshold.setPerfName(OltPerf.getPerfNameById(oltPerfThreshold.getPerfThresholdTypeIndex(),
                        oltPerfThreshold.getPerfThresholdObject(), uc.getUser().getLanguage()));
                // 返回给界面数据
            }
        }
        jsonMap.put("data", re);
        writeDataToAjax(JSONObject.fromObject(jsonMap));
        return NONE;
    }

    /**
     * 修改阈值
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltPerfThresholdAction", operationName = "modifyPerfThreshold")
    public String modifyPerfThreshold() throws IOException {
        String message;
        // 通过阈值类型和修改值修改阈值配置
        OltPerfThreshold oltPerfThreshold = new OltPerfThreshold();
        oltPerfThreshold.setEntityId(entityId);
        // 阈值类型
        oltPerfThreshold.setThresholdType(thresholdType);
        // 阈值指标index
        oltPerfThreshold.setPerfThresholdTypeIndex(perfThresholdTypeIndex);
        oltPerfThreshold.setPerfThresholdObject(perfThresholdObject);
        oltPerfThreshold.setPerfThresholdUpper(perfThresholdUpper);
        oltPerfThreshold.setPerfThresholdLower(perfThresholdLower);
        try {
            oltPerfService.modifyPerfThreshold(oltPerfThreshold);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (ModifyPerfThresholdException e) {
            message = "error[" + e.getMessage() + "]";
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyPerfThreshold error: {}", e);
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 从设备上刷新阈值到数据库
     * 
     * @return
     */
    public String refreshPerfThreshold() throws IOException {
        String message;
        // 从设备上刷新阈值到数据库
        try {
            oltPerfService.refreshPerfThreshold(entityId);
            message = "success";
        } catch (RefreshPerfThresholdException e) {
            message = "error[" + e.getMessage() + "]";
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 加载OLT与绑定的阈值模板的列表
     * @return
     * @throws IOException
     */
    public String loadOltThresholdTemplate() throws IOException {
        JSONObject json = new JSONObject();
        Long oltType = entityTypeService.getOltType();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityType", oltType);
        map.put("entityName", entityName);
        map.put("entityIp", entityIp);
        map.put("templateName", templateName);
        map.put("start", start);
        map.put("limit", limit);
        if (tempRela != null && tempRela != -1) {
            map.put("tempRela", tempRela);
        }
        perfThresholdList = perfThresholdService.selectEntityPerfThresholdTmeplate(map);
        Long perfThresholdCount = perfThresholdService.selectPerfTmeplateCount(map);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (PerfThresholdEntity thresholdEntity : perfThresholdList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(thresholdEntity.getMac(), macRule);
            thresholdEntity.setMac(formatedMac);
        }
        json.put("data", perfThresholdList);
        json.put("rowCount", perfThresholdCount);
        json.write(response.getWriter());
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }

    public Integer getPerfThresholdTypeIndex() {
        return perfThresholdTypeIndex;
    }

    public void setPerfThresholdTypeIndex(Integer perfThresholdTypeIndex) {
        this.perfThresholdTypeIndex = perfThresholdTypeIndex;
    }

    public Integer getPerfThresholdObject() {
        return perfThresholdObject;
    }

    public void setPerfThresholdObject(Integer perfThresholdObject) {
        this.perfThresholdObject = perfThresholdObject;
    }

    public String getPerfThresholdUpper() {
        return perfThresholdUpper;
    }

    public void setPerfThresholdUpper(String perfThresholdUpper) {
        this.perfThresholdUpper = perfThresholdUpper;
    }

    public String getPerfThresholdLower() {
        return perfThresholdLower;
    }

    public void setPerfThresholdLower(String perfThresholdLower) {
        this.perfThresholdLower = perfThresholdLower;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getPerfName() {
        return perfName;
    }

    public void setPerfName(String perfName) {
        this.perfName = perfName;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public Integer getTempRela() {
        return tempRela;
    }

    public void setTempRela(Integer tempRela) {
        this.tempRela = tempRela;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

}
