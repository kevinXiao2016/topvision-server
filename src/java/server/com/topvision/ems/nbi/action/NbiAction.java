package com.topvision.ems.nbi.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.nbi.domain.NbiBaseConfig;
import com.topvision.ems.nbi.domain.NbiTarget;
import com.topvision.ems.nbi.domain.NbiTargetGroup;
import com.topvision.ems.nbi.service.NbiService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.performance.nbi.api.NbiMultiPeriod;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("nbiAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NbiAction extends BaseAction {
    private static final long serialVersionUID = 4382523451410497638L;
    @Autowired
    private NbiService nbiService;
    private NbiBaseConfig nbiBaseConfig;
    private NbiMultiPeriod nbiMultiPeriod;
    private List<NbiTargetGroup> nbiTargetGroup;
    private Integer mode;
    private String ftpAddr;
    private Integer ftpPort;
    private String ftpUser;
    private String ftpPwd;
    private String filePath;
    private Integer recordMax;
    private Integer fileSaveTime;// 单位天
    private String nbiAddr;
    private Integer nbiPort;
    private String encoding;
    private Boolean five_enable;
    private Boolean ten_enable;
    private Boolean fifteen_enable;
    private Boolean thirty_enable;
    private Boolean sixty_enable;
    private Integer nbiSwitch;
    private String nbiArrStr;

    public String showNbiBaseConfigView() {
        nbiBaseConfig = nbiService.getNbiBaseConfig();
        return SUCCESS;
    }

    public String saveNbiBaseConfig() throws IOException {
        NbiBaseConfig nbiBaseConfig = new NbiBaseConfig();
        nbiBaseConfig.setMode(mode);
        nbiBaseConfig.setFtpAddr(ftpAddr);
        nbiBaseConfig.setFtpPort(ftpPort);
        nbiBaseConfig.setFtpUser(ftpUser);
        nbiBaseConfig.setFtpPwd(ftpPwd);
        nbiBaseConfig.setFilePath(filePath);
        nbiBaseConfig.setRecordMax(recordMax);
        nbiBaseConfig.setFileSaveTime(fileSaveTime);
        nbiBaseConfig.setNbiAddr(nbiAddr);
        nbiBaseConfig.setNbiPort(nbiPort);
        nbiBaseConfig.setEncoding(encoding);
        nbiBaseConfig.setNbiSwitch(nbiSwitch);
        nbiService.updateNbiBaseConfig(nbiBaseConfig);
        return NONE;
    }

    public String showNbiExportConfig() {
        nbiMultiPeriod = nbiService.getNbiMultiPeriod();
        return SUCCESS;
    }

    public String saveNbiExportConfig() throws IOException {
        NbiMultiPeriod multiPeriod = new NbiMultiPeriod();
        multiPeriod.setFive_enable(five_enable);
        multiPeriod.setTen_enable(ten_enable);
        multiPeriod.setFifteen_enable(fifteen_enable);
        multiPeriod.setThirty_enable(thirty_enable);
        multiPeriod.setSixty_enable(sixty_enable);
        nbiService.updateNbiMultiPeriod(multiPeriod);
        return NONE;
    }

    public String showNbiTargetConfig() {
        return SUCCESS;
    }

    public String loadNbiTargetList() throws IOException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        boolean hasSupportOlt = uc.hasSupportModule("olt");
        boolean hasSupportOnu = uc.hasSupportModule("onu");
        boolean hasSupportCmc = uc.hasSupportModule("cmc");
        nbiTargetGroup = nbiService.getNbiTargetGroup(hasSupportOlt, hasSupportOnu, hasSupportCmc);
        writeDataToAjax(nbiTargetGroup);
        return NONE;
    }

    public String updateNbiTargetConfig() throws IOException {
        JSONArray jsonArray = JSONArray.fromObject(nbiArrStr);
        List<NbiTarget> targets = new ArrayList<NbiTarget>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject o = (JSONObject) jsonArray.get(i);
            NbiTarget nbiTarget = new NbiTarget();
            nbiTarget.setPerfIndex(Integer.parseInt(o.getString("perfindex")));
            nbiTarget.setSelected(Boolean.parseBoolean(o.getString("selected")));
            nbiTarget.setPeriod(Integer.parseInt(o.getString("period")));
            targets.add(nbiTarget);
        }
        nbiService.updateNbiTarget(targets);
        return NONE;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getFtpAddr() {
        return ftpAddr;
    }

    public void setFtpAddr(String ftpAddr) {
        this.ftpAddr = ftpAddr;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPwd() {
        return ftpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getRecordMax() {
        return recordMax;
    }

    public void setRecordMax(Integer recordMax) {
        this.recordMax = recordMax;
    }

    public Integer getFileSaveTime() {
        return fileSaveTime;
    }

    public void setFileSaveTime(Integer fileSaveTime) {
        this.fileSaveTime = fileSaveTime;
    }

    public String getNbiAddr() {
        return nbiAddr;
    }

    public void setNbiAddr(String nbiAddr) {
        this.nbiAddr = nbiAddr;
    }

    public Boolean getFive_enable() {
        return five_enable;
    }

    public void setFive_enable(Boolean five_enable) {
        this.five_enable = five_enable;
    }

    public Boolean getTen_enable() {
        return ten_enable;
    }

    public void setTen_enable(Boolean ten_enable) {
        this.ten_enable = ten_enable;
    }

    public Boolean getFifteen_enable() {
        return fifteen_enable;
    }

    public void setFifteen_enable(Boolean fifteen_enable) {
        this.fifteen_enable = fifteen_enable;
    }

    public Boolean getThirty_enable() {
        return thirty_enable;
    }

    public void setThirty_enable(Boolean thirty_enable) {
        this.thirty_enable = thirty_enable;
    }

    public Boolean getSixty_enable() {
        return sixty_enable;
    }

    public void setSixty_enable(Boolean sixty_enable) {
        this.sixty_enable = sixty_enable;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public Integer getNbiPort() {
        return nbiPort;
    }

    public void setNbiPort(Integer nbiPort) {
        this.nbiPort = nbiPort;
    }

    public List<NbiTargetGroup> getNbiTargetGroup() {
        return nbiTargetGroup;
    }

    public void setNbiTargetGroup(List<NbiTargetGroup> nbiTargetGroup) {
        this.nbiTargetGroup = nbiTargetGroup;
    }

    public NbiBaseConfig getNbiBaseConfig() {
        return nbiBaseConfig;
    }

    public void setNbiBaseConfig(NbiBaseConfig nbiBaseConfig) {
        this.nbiBaseConfig = nbiBaseConfig;
    }

    public NbiMultiPeriod getNbiMultiPeriod() {
        return nbiMultiPeriod;
    }

    public void setNbiMultiPeriod(NbiMultiPeriod nbiMultiPeriod) {
        this.nbiMultiPeriod = nbiMultiPeriod;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getNbiSwitch() {
        return nbiSwitch;
    }

    public void setNbiSwitch(Integer nbiSwitch) {
        this.nbiSwitch = nbiSwitch;
    }

    public String getNbiArrStr() {
        return nbiArrStr;
    }

    public void setNbiArrStr(String nbiArrStr) {
        this.nbiArrStr = nbiArrStr;
    }

}
