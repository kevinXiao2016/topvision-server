package com.topvision.ems.license.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.report.domain.Report;
import com.topvision.ems.report.service.ReportCoreService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.Version;
import com.topvision.framework.web.dhtmlx.DefaultDhtmlxHandler;
import com.topvision.framework.web.dhtmlx.DhtmlxTreeOutputter;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.license.common.domain.Module;
import com.topvision.license.common.exception.DuplicateLicenseException;
import com.topvision.license.common.exception.IllegalLicenseSignatureException;
import com.topvision.license.common.exception.InvalidProductKeyException;
import com.topvision.license.common.exception.LicenseExpiredException;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemVersion;
import com.topvision.platform.domain.LicenseView;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.zetaframework.util.ZetaUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("licenseAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LicenseAction extends BaseAction {
    private static final long serialVersionUID = 1383993303240328701L;

    private static Logger logger = LoggerFactory.getLogger(LicenseAction.class);
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private ReportCoreService reportCoreService;
    private Version version = null;
    private LicenseView licView;
    private File licFile;
    private Integer importResultCode;// license导入结果 编码
    // license申请参数
    private String licenseType;
    private String user;
    private String phone;
    private String email;
    private String selectEntityTypes;
    private String organisation;
    private String numberOfDays;
    private String numberOfUsers;
    private String numberOfEngines;
    private String selectReports;
    private String unSelectReports;
    private JSONArray typeArrs;
    private String mobile;
    private Boolean mobileSurport;
    private Boolean pnmpSurport;
    private Boolean reportSurport;
    private Integer oltNum;
    private Integer cmcNum;
    private Integer onuNum;
    private Integer cmtsNum;
    private static final String SERVER_RESOURCE_PATH = "com.topvision.ems.resources.resources";
    private Boolean licenseValid;

    /**
     * 显示关于
     * 
     * @return
     */
    public String showAbout() {
        try {
            version = new SystemVersion();
        } catch (Exception e) {
            logger.error("get version error", e);
        }
        licView = licenseService.getLicenseView();
        return SUCCESS;
    }

    /**
     * 显示License
     * 
     * @return
     */
    public String showLicense() {
        licenseValid = licenseService.verifyLicense();

        licView = licenseService.getLicenseView();
        List<Module> modules = licView.getModules();
        // 将集中型和独立型设备类型合并为一个展示在license页面，如将CC8800E(强集中)和CC8800E(独立)合并展示为CC8800E 解决EMS-11431 by
        // fanzhong
        for (Iterator<Module> iter = modules.iterator(); iter.hasNext();) {
            Module module = iter.next();
            if ("cmc".equals(module.getName())) {
                module.setDisplayName(getString("license.topvisionCMTS"));
                if (module.getExcludeStr() != null && !"".equals(module.getExcludeStr().trim())) {
                    Set<String> types = new HashSet<String>();
                    String[] typesArray = module.getExcludeStr().split(",");
                    for (String type : typesArray) {
                        // 去掉括号及括号中内容
                        types.add(type.replaceAll("\\([^)]+\\)", ""));
                    }
                    module.setExcludeStr(types.toString().replace("[", "").replace("]", ""));
                }
            }
            if ("cmts".equals(module.getName())) {
                module.setDisplayName(getString("license.otherCMTS"));
            }
            if ("report".equals(module.getName())) {
                module.setExcludeStr(formatDisplay(module.getExclude()));
                module.setIncludeStr(formatDisplay(module.getInclude()));
                module.setDisplayName(getString("license.report"));
            }
            if ("mobile".equals(module.getName())) {
                licView.setMobileSurport(module.getEnabled());
                iter.remove();
            }
            if ("pnmp".equals(module.getName())) {
                licView.setPnmpSurport(module.getEnabled());
                iter.remove();
            }
        }
        return SUCCESS;
    }

    public String showLicenseApply() {
        licenseValid = licenseService.verifyLicense();
        return SUCCESS;
    }

    /**
     * 拼接html 显示加载更多效果
     * 
     * @param display
     * @return
     */
    private String formatDisplay(List<String> display) {
        String newDisplay = "";
        if (display != null && display.size() > 0) {
            for (int i = 0; i < display.size(); i++) {
                if (reportCoreService.fetchReportById(display.get(i).trim()) != null) {
                    newDisplay += reportCoreService.fetchReportById(display.get(i).trim()).getTitle() + "#";
                }
            }
        }
        return newDisplay;
    }

    /**
     * 显示许可证提示页面
     * 
     * @return
     */
    public String showLicenseTip() {
        licView = licenseService.getLicenseView();
        List<EntityType> types = licenseService.getAllEntityTypes();
        typeArrs = JSONArray.fromObject(types);
        return SUCCESS;
    }

    /**
     * 获取机器码
     * 
     * @throws Exception
     */
    public void getProductKey() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=product.key");
        licView = new LicenseView();
        licView.setLicenseType(licenseType);
        licView.setUser(user);
        licView.setPhone(phone);
        licView.setMobile(mobile);
        licView.setEmail(email);
        licView.setOrganisation(organisation);
        licView.setNumberOfDays(numberOfDays);
        licView.setNumberOfUsers(numberOfUsers);
        licView.setNumberOfEngines(numberOfEngines);
        licView.setSelectEntityTypes(selectEntityTypes);
        licView.setSelectReports(selectReports);
        licView.setUnSelectReports(unSelectReports);
        licView.setMobileSurport(mobileSurport);
        licView.setPnmpSurport(pnmpSurport);
        licView.setReportSurport(reportSurport);
        licView.setOltNum(oltNum);
        licView.setOnuNum(onuNum);
        licView.setCmcNum(cmcNum);
        licView.setCmtsNum(cmtsNum);
        byte[] productKey = licenseService.getProductKey(licView);
        OutputStream out = response.getOutputStream();
        out.write(productKey);
        out.flush();
        out.close();
    }

    public String onlineApply() throws Exception {
        licView = new LicenseView();
        licView.setLicenseType(licenseType);
        licView.setUser(user);
        licView.setPhone(phone);
        licView.setEmail(email);
        licView.setOrganisation(organisation);
        licView.setSelectEntityTypes(selectEntityTypes);
        licenseService.onlineApply(licView);
        return SUCCESS;
    }

    public String importLicense() {
        try {
            FileInputStream fis = new FileInputStream(this.licFile);
            byte[] lic = new byte[fis.available()];
            fis.read(lic);
            fis.close();

            try {
                this.licenseService.importLicense(lic);
                this.importResultCode = 100;// 成功
            } catch (DuplicateLicenseException e) {
                logger.debug(e.getMessage());
                this.importResultCode = 101;
            } catch (IllegalLicenseSignatureException e) {
                logger.debug(e.getMessage());
                this.importResultCode = 102;
            } catch (InvalidProductKeyException e) {
                logger.debug(e.getMessage());
                this.importResultCode = 103;
            } catch (LicenseExpiredException e) {
                logger.debug(e.getMessage());
                this.importResultCode = 104;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.importResultCode = -100; // 服务器端错误
        }

        return SUCCESS;
    }

    public Integer getImportResultCode() {
        return importResultCode;
    }

    public String loadAllDeviceTypes() throws Exception {
        List<EntityType> types = licenseService.getAllEntityTypes();
        String displayName;
        for (EntityType entityType : types) {
            displayName = entityType.getDisplayName().replaceAll("\\([^)]+\\)", "");
            if ("CMC".equalsIgnoreCase(displayName)) {
                displayName = ZetaUtil.getStaticString("license.topvisionCMTS", "resources");
            } else if ("CMTS".equalsIgnoreCase(displayName)) {
                displayName = ZetaUtil.getStaticString("license.otherCMTS", "resources");
            }
            entityType.setDisplayName(displayName);
        }
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(types) {
            @Override
            public Element buildElement(Object obj) {
                EntityType item = (EntityType) obj;
                String id = item.getId();
                Element el = new DefaultElement("item");
                el.addAttribute("id", id);
                el.addAttribute("text", item.getText());
                el.addAttribute("open", "1");
                el.addAttribute("im0", "portlet.gif");
                el.addAttribute("im1", "portlet.gif");
                el.addAttribute("im2", "portlet.gif");
                el.addAttribute("checked", "1");
                return el;
            }
        };
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    /**
     * 加载所有报表 add by loyal
     * 
     * @return
     * @throws Exception
     */
    public String loadAllReports() throws Exception {
        List<Report> reports = reportCoreService.loadAllReports();
        List<Report> reporWithRoot = addPathReports(reports);
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(reporWithRoot) {
            @Override
            public Element buildElement(Object obj) {
                Report item = (Report) obj;
                String id = item.getId();
                Element el = new DefaultElement("item");
                el.addAttribute("id", id + "-" + item.getModule().replace(",", "&"));
                el.addAttribute("text", item.getTitle());
                el.addAttribute("open", "1");
                el.addAttribute("im0", "portlet.gif");
                el.addAttribute("im1", "portlet.gif");
                el.addAttribute("im2", "portlet.gif");
                el.addAttribute("checked", "1");
                return el;
            }
        };
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    /**
     * 通过遍历报表路径，构造报表根
     * 
     * @param reports
     * @return
     */
    private List<Report> addPathReports(List<Report> reports) {
        List<Report> reporWithRoot = new ArrayList<Report>();
        Set<String> paths = new HashSet<String>();
        for (Report report : reports) {
            paths.add(report.getPath());
        }

        for (String path : paths) {
            Report report = new Report();
            report.setPath(path);
            report.setTitle(getString(path));
            report.setId(path);
            report.setModule("");
            reporWithRoot.add(report);
        }
        reporWithRoot.addAll(reports);
        return reporWithRoot;
    }

    /**
     * 加载license信息，初始化页面时使用
     * 
     * @return
     */
    public String getCuLicView() {
        JSONObject licJson = new JSONObject();
        // 获取设备类型，用于前台页面树级联判断
        List<EntityType> CMCI = entityTypeService.loadAllSubType(entityTypeService.getCcmtswithagentType());
        List<EntityType> CMCII = entityTypeService.loadAllSubType(entityTypeService.getCcmtswithoutagentType());
        List<EntityType> CMTS = entityTypeService.loadAllSubType(entityTypeService.getCmtsType());
        List<EntityType> OLT = entityTypeService.loadAllSubType(entityTypeService.getOltType());
        List<EntityType> ONU = entityTypeService.loadAllSubType(entityTypeService.getOnuType());
        licView = licenseService.getLicenseView();
        version = new SystemVersion();
        licJson.put("licView", licView);
        licJson.put("version", version);
        licJson.put("CMCI", transferEntitytypeToNames(CMCI));
        licJson.put("CMCII", transferEntitytypeToNames(CMCII));
        licJson.put("CMTS", transferEntitytypeToNames(CMTS));
        licJson.put("OLT", transferEntitytypeToNames(OLT));
        licJson.put("ONU", transferEntitytypeToNames(ONU));
        writeDataToAjax(licJson);
        return NONE;
    }

    private String transferEntitytypeToNames(List<EntityType> entityTypes) {
        if (entityTypes == null) {
            return "";
        }
        String names = "";
        for (EntityType entityType : entityTypes) {
            names += entityType.getName() + ",";
        }
        return names;
    }

    private String getString(String key) {
        ResourceManager resourceManager = ResourceManager.getResourceManager(SERVER_RESOURCE_PATH);
        return resourceManager.getNullString(key);
    }

    public void setLicFile(File licFile) {
        this.licFile = licFile;
    }

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public LicenseView getLicView() {
        return licView;
    }

    public File getLicFile() {
        return licFile;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setLicView(LicenseView licView) {
        this.licView = licView;
    }

    public void setImportResultCode(Integer importResultCode) {
        this.importResultCode = importResultCode;
    }

    public Version getVersion() {
        return version;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelectEntityTypes() {
        return selectEntityTypes;
    }

    public void setSelectEntityTypes(String selectEntityTypes) {
        this.selectEntityTypes = selectEntityTypes;
    }

    /**
     * @return the organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation
     *            the organisation to set
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public JSONArray getTypeArrs() {
        return typeArrs;
    }

    public void setTypeArrs(JSONArray typeArrs) {
        this.typeArrs = typeArrs;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(String numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public String getNumberOfEngines() {
        return numberOfEngines;
    }

    public void setNumberOfEngines(String numberOfEngines) {
        this.numberOfEngines = numberOfEngines;
    }

    public String getSelectReports() {
        return selectReports;
    }

    public void setSelectReports(String selectReports) {
        this.selectReports = selectReports;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUnSelectReports() {
        return unSelectReports;
    }

    public void setUnSelectReports(String unSelectReports) {
        this.unSelectReports = unSelectReports;
    }

    public Boolean getMobileSurport() {
        return mobileSurport;
    }

    public void setMobileSurport(Boolean mobileSurport) {
        this.mobileSurport = mobileSurport;
    }

    public Boolean getReportSurport() {
        return reportSurport;
    }

    public void setReportSurport(Boolean reportSurport) {
        this.reportSurport = reportSurport;
    }

    public Integer getOltNum() {
        return oltNum;
    }

    public void setOltNum(Integer oltNum) {
        this.oltNum = oltNum;
    }

    public Integer getCmcNum() {
        return cmcNum;
    }

    public void setCmcNum(Integer cmcNum) {
        this.cmcNum = cmcNum;
    }

    public Integer getOnuNum() {
        return onuNum;
    }

    public void setOnuNum(Integer onuNum) {
        this.onuNum = onuNum;
    }

    public Integer getCmtsNum() {
        return cmtsNum;
    }

    public void setCmtsNum(Integer cmtsNum) {
        this.cmtsNum = cmtsNum;
    }

    public Boolean getLicenseValid() {
        return licenseValid;
    }

    public void setLicenseValid(Boolean licenseValid) {
        this.licenseValid = licenseValid;
    }

    public Boolean getPnmpSurport() {
        return pnmpSurport;
    }

    public void setPnmpSurport(Boolean pnmpSurport) {
        this.pnmpSurport = pnmpSurport;
    }

}
