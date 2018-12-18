package com.topvision.ems.mibble.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.tree.TreeModel;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.browser.MibNode;
import net.percederberg.mibble.browser.SnmpException;
import net.percederberg.mibble.browser.SnmpManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.snmp4j.mp.SnmpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.mibble.service.MibbleBrowserService;
import com.topvision.ems.mibble.service.MibbleLoader;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.snmp.MibManager;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * mibbleBrowser加载，操作等
 * 
 * @author Bravin
 * @created @2012-10-8-下午02:39:40
 */
@Controller("mibbleBrowserAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MibbleBrowserAction extends BaseAction {
    private static final long serialVersionUID = 5776800868388401397L;
    private long entityId;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    /**
     * Loader , load mib
     */
    @Autowired
    private MibbleLoader mibbleLoader;
    /**
     * Service
     */
    @Autowired
    private MibbleBrowserService mibbleBrowserService;

    /**
     * Manager Get/set mib value
     */
    private SnmpManager snmpManager;
    /**
     * SNMP Version : V1, V2, V3
     */
    private int version;
    /**
     * HOST IP
     */
    private String host;
    /**
     * HOST PORT
     */
    private int port;
    /**
     * ether read or write commnutiy
     */
    private String community;
    /**
     * Read Community
     */
    private String readCommunity;
    /**
     * Write Community
     */
    private String writeCommunity;
    /**
     * Context Name
     */
    private String contextName;
    /**
     * Context Engine
     */
    private String contextEngine;
    /**
     * User Name
     */
    private String userName;
    /**
     * Authentication Type
     */
    private String authType;
    /**
     * Authentication Password
     */
    private String authPassword;
    /**
     * Privacy Type
     */
    private String privacyType;
    /**
     * Privacy Password
     */
    private String privacyPassword;
    /**
     * OID
     */
    private String oid;
    /**
     * Value
     */
    private String value;

    /**
     * @deprecated 上传Mib名称
     */
    @Deprecated
    private String fileName;
    /**
     * Timeout
     */
    private long timeout;

    /**
     * Retry
     */
    private int retry;

    /**
     * 传递到前端供选择的mibs
     */
    private JSONArray mibArray;

    /**
     * 用户已经选择的mib列表
     */
    private JSONArray mibSelectedList;

    /**
     * 用户选择将要保存的mib文件，mib文件直接以 ，隔开。例如 RFC1271-MIB，DIFFSERV-DSCP-TC，INTEGRATED-SERVICES-MIB
     */
    private String selectedMibs;

    /**
     * YangYi Add @2013-11-15 判断需要显示的Mibble的类型,从资源列表和CM列表，加载的Mibble类型不同
     */
    private int mibbleType;

    /**
     * YangYi Add @2014-05-05 判断进入读取MIB树的次数
     */
    private Integer enterCount;
    @Autowired
    private EntityService entityService;

    /**
     * 进入mibblebrowser页面
     * 
     * @return
     */
    public String showMibbleBrowser() {
        if (host != null) {
            Entity entity = entityService.getEntityByIp(host);
            if (entity != null) {
                SnmpParam sp = entityService.getSnmpParamByEntity(entity.getEntityId());
                community = sp.getCommunity();
                writeCommunity = sp.getWriteCommunity();
            }
        }
        return SUCCESS;
    }

    /**
     * 选择需要展示的Mib
     * 
     * @return
     */
    public String showMibbleSelectJsp() {
        File[] mibbleDirs = MibManager.getInstance().getMibLoader().getDirs();
        mibArray = new JSONArray();
        for (File file : mibbleDirs) {
            File[] mibs = file.listFiles();
            for (File mib : mibs) {
                mibArray.add(mib.getName());
            }
        }
        UserContext uc = (UserContext) getSession().get("UserContext");
        List<String> selectedMibs = mibbleBrowserService.loadMibbles(uc.getUserId());
        mibSelectedList = new JSONArray();
        for (String mib : selectedMibs) {
            mibSelectedList.add(mib);
        }
        return SUCCESS;
    }

    /**
     * 保存用户选择的mib文件
     * 
     * @return
     */
    public String saveSelectedMib() {
        String[] mibs;
        // "".split(",")= "",算作一个对象，故如下处理
        if ("".equals(selectedMibs)) {
            mibs = new String[0];
        } else {
            mibs = selectedMibs.split(",");

        }
        UserContext uc = (UserContext) getSession().get("UserContext");
        mibbleBrowserService.saveSelectedMib(mibs, uc.getUserId());
        return NONE;
    }

    /**
     * 加载用户选择的MIB
     * 
     * @return
     * @throws MibLoaderException
     * @throws IOException
     * @throws JSONException
     */
    public String loadMibbles() throws IOException, MibLoaderException, JSONException {
        JSONArray jsonArray = new JSONArray();
        String rootPath = EnvironmentConstants.getEnv(EnvironmentConstants.MIB_HOME);
        UserContext uc = (UserContext) getSession().get("UserContext");
        List<String> mibs = new ArrayList<String>();
        // YangYi add @2013-11-15 从CM列表进入Mibble Browser
        if (mibbleType == 35000 && enterCount != null && enterCount == 1) {
            mibs.add("RFC1213-MIB");
            mibs.add("DOCS-IF-MIB");
            mibs.add("DOCS-CABLE-DEVICE-MIB");
        } else {
            mibs = mibbleBrowserService.loadMibbles(uc.getUserId());
            if (mibs.size() == 0) {
                mibs.add("RFC1213-MIB");
                mibs.add("NSCRTV-EPONEOC-EPON-MIB");
            }
        }
        while (mibs.size() > 0) {
            String mibName = mibs.remove(0);
            try {
                Mib mib = mibbleLoader.loadMib(rootPath + mibName);
                TreeModel model = mibbleLoader.parse(mib).getModel();
                JSONObject json = new JSONObject();
                MibNode root = (MibNode) model.getRoot();
                json.put("name", root.getName());
                json.put("oid", root.getOid());
                json.put("symbol", root.getSymbol());
                json.put("isLeaf", false);
                mibbleLoader.parseMib(root, json);
                jsonArray.add(json);
            } catch (Exception e) {
                JSONObject json = new JSONObject();
                json.put("name", mibName);
                json.put("oid", mibName);
                json.put("symbol", mibName);
                jsonArray.add(json);
            }
        }
        jsonArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载一个MIB文件，开发调试时使用 FIXME 开发完成时删除此action
     * 
     * @return
     * @throws MibLoaderException
     * @throws IOException
     * @throws JSONException
     */
    public String loadMibble() throws IOException, MibLoaderException, JSONException {
        String mibUrl = SystemConstants.ROOT_REAL_PATH + "META-INF\\mibbles\\" + fileName;
        JSONObject json = _loadMibble(mibUrl);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 按照URL加载一个MIB
     * 
     * @param mibUrl
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws MibLoaderException
     */
    private JSONObject _loadMibble(String mibUrl) throws JSONException, IOException, MibLoaderException {
        Mib mib = mibbleLoader.loadMib(mibUrl);
        TreeModel model = mibbleLoader.parse(mib).getModel();
        JSONObject json = new JSONObject();
        MibNode root = (MibNode) model.getRoot();
        json.put("name", root.getName());
        mibbleLoader.parseMib(root, json);
        return json;
    }

    /**
     * 上传一个mibble并保存到服务器 TODO 考虑是否只允许用户使用网管中存在的mib
     * 
     * @deprecated
     * @return
     * @throws MibLoaderException
     * @throws IOException
     * @throws JSONException
     */
    @Deprecated
    public String uploadMib() throws JSONException, IOException, MibLoaderException {
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(inputName);
            if (isNonEmpty(contentType)) {
                String[] fileNames = multiWrapper.getFileNames(inputName);
                if (isNonEmpty(fileNames)) {
                    File[] files = multiWrapper.getFiles(inputName);
                    if (files != null) {
                        for (File _file : files) {
                            File toFile = new File(SystemConstants.ROOT_REAL_PATH + "META-INF/mibbles/"
                                    + fileName.toString());
                            FileUtils.copy(_file, toFile);
                        }
                    }
                }
            }
        }
        // //把这个MIB返回到前端
        // String thisMib = SystemConstants.ROOT_REAL_PATH +
        // "META-INF\\mibbles\\" + fileName.toString();
        // JSONObject json = _loadMibble(thisMib);
        writeDataToAjax("hello world");
        return NONE;
    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 删除一个MIB文件，从服务器永久删除
     * 
     * @return
     */
    public String deleteMib() {
        return NONE;
    }

    /**
     * GET
     * 
     * @return
     * @throws SnmpException
     * @throws IOException
     * @throws JSONException
     */
    public String get() throws SnmpException, IOException, JSONException {
        // 读
        Map<String, String> data = mibbleBrowserService.get(getParam(), oid);
        JSONObject json = new JSONObject();
        json.put("oid", data.get("oid"));
        json.put("value", data.get("value"));
        json.put("name", data.get("name"));
        json.put("rawOid", data.get("realOid"));
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * GET NEXT
     * 
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String getNext() throws IOException, JSONException {

        Map<String, String> data = mibbleBrowserService.getNext(getParam(), oid);
        JSONObject json = new JSONObject();
        json.put("oid", data.get("oid"));
        json.put("value", data.get("value"));
        json.put("name", data.get("name"));
        json.put("rawOid", data.get("realOid"));
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 停止GETALL
     * 
     * @return
     */
    public String stopGetAll() {
        mibbleBrowserService.stopGetAll();
        return NONE;
    }

    /**
     * SET
     * 
     * @return
     */
    public String set() {
        return NONE;
    }

    /**
     * 得到 V1/V2/V3 Snmp 参数
     * 
     * @return
     */
    public SnmpParam getParam() {
        Properties snmpProperty = systemPreferencesService.getModulePreferences("Snmp");
        Integer snmpPort = Integer.parseInt(snmpProperty.getProperty("Snmp.port"));
        snmpPort = snmpPort != null ? snmpPort : 161;
        Integer snmpCount = Integer.parseInt(snmpProperty.getProperty("Snmp.retries"));
        snmpCount = snmpCount != null ? snmpCount : 0;
        Integer snmpTimeout = Integer.parseInt(snmpProperty.getProperty("Snmp.timeout"));
        snmpTimeout = snmpTimeout != null ? snmpTimeout : 15000;

        SnmpParam param = new SnmpParam();
        param.setIpAddress(host);
        param.setPort(port);
        param.setVersion(version);
        param.setPort(snmpPort);
        param.setTimeout(snmpTimeout);
        param.setRetry(Byte.parseByte(snmpCount.toString()));
        if (version < SnmpConstants.version3) {
            param.setCommunity(community);
        } else {// TODO V3后续支持
            // 如果 加密模式有选择
            if (authType != null) {
            }
            if (privacyType != null) {
                // privacy = new SnmpPrivacy(privacyType, privacyPassword);
                // param.setPrivProtocol(privacyType);
            }
        }
        return param;
    }

    // ////////////////// SETTER&GETTER /////////////////////////
    public MibbleLoader getMibbleLoader() {
        return mibbleLoader;
    }

    public void setMibbleLoader(MibbleLoader mibbleLoader) {
        this.mibbleLoader = mibbleLoader;
    }

    public SnmpManager getSnmpManager() {
        return snmpManager;
    }

    public void setSnmpManager(SnmpManager snmpManager) {
        this.snmpManager = snmpManager;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getContextEngine() {
        return contextEngine;
    }

    public void setContextEngine(String contextEngine) {
        this.contextEngine = contextEngine;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(String privacyType) {
        this.privacyType = privacyType;
    }

    public String getPrivacyPassword() {
        return privacyPassword;
    }

    public void setPrivacyPassword(String privacyPassword) {
        this.privacyPassword = privacyPassword;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public MibbleBrowserService getService() {
        return mibbleBrowserService;
    }

    public void setService(MibbleBrowserService service) {
        this.mibbleBrowserService = service;
    }

    public JSONArray getMibArray() {
        return mibArray;
    }

    public void setMibArray(JSONArray mibArray) {
        this.mibArray = mibArray;
    }

    public JSONArray getMibSelectedList() {
        return mibSelectedList;
    }

    public void setMibSelectedList(JSONArray mibSelectedList) {
        this.mibSelectedList = mibSelectedList;
    }

    public String getSelectedMibs() {
        return selectedMibs;
    }

    public void setSelectedMibs(String selectedMibs) {
        this.selectedMibs = selectedMibs;
    }

    public int getMibbleType() {
        return mibbleType;
    }

    public void setMibbleType(int mibbleType) {
        this.mibbleType = mibbleType;
    }

    public Integer getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Integer enterCount) {
        this.enterCount = enterCount;
    }

}
