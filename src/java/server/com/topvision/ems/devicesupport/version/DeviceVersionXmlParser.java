/**
 * 
 */
package com.topvision.ems.devicesupport.version;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author dosion
 * 
 */
@Service("deviceVersionXmlParser")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DeviceVersionXmlParser {
    private static final String DEVICE_TYPE = "deviceType";
    private static final String DEVICE_NAME = "deviceName";
    private static final String VERSSIONS = "versions";
    private static final String VERSION = "version";
    private static final String NAME = "name";
    private static final String BASEVERSION_VALUE = "base";
    private static final String BASEVERSION = "baseVersion";
    private static final String FUNCTIONS = "functions";
    private static final String FUNCTION = "function";
    private static final String STATUS = "status";
    private static final String INDEX = "index";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final Pattern containNum = Pattern.compile("(\\D*+\\d++\\D*+)++");
    private static final int EQUAL = 0;
    private static final int LT = -1;
    private static final int GT = 1;
    private static final int ERROR = -2;
    private static final Logger logger = LoggerFactory.getLogger(DeviceVersionXmlParser.class);
    private static DeviceVersionXmlParser instance;
    private Map<Long, Map<String, DeviceVersionInfo>> allFuncs;
    private Map<Long, Map<String, DeviceVersion>> allVersions;
    @Value("classpath*:/com/topvision/ems/**/deviceVersionFunctions_*.xml")
    private Resource[] rs;

    private DeviceVersionXmlParser() {
    }

    @PostConstruct
    private void initialize() {
        allFuncs = new HashMap<Long, Map<String, DeviceVersionInfo>>();
        allVersions = new HashMap<Long, Map<String, DeviceVersion>>();
        parseVersionFunction();
        instance = this;
    }

    /**
     * 解析所有的设备版本功能XML文件
     */
    private void parseVersionFunction() {
        logger.debug("begin to init ");
        for (Resource r : rs) {
            try {
                loadOneTypeConfig(r);
            } catch (Throwable e) {
                logger.error("parse VersionControl file failed at:" + r.getFilename());
                logger.error("", e);
            }
        }
        logger.debug("finish init ");
    }

    /**
     * 加载一个设备类型的版本功能信息
     * 
     * @param deviceVersionFile
     *            版本文件信息，需要包括设备类型typeId、deviceVersionFile的文件全名， 如果设备类型与文件中的类型不一致则不进行解析，记录日志
     * @throws Throwable
     */
    private void loadOneTypeConfig(Resource resource) throws Throwable {
        // TODO 尽量改成SAX的解析方式
        String fileName = resource.getFilename();
        logger.debug("begin to load config: " + fileName);
        InputStream is = resource.getInputStream();
        SAXReader reader = new SAXReader();
        Document doc = reader.read(is);
        Element root = doc.getRootElement();
        Long typeId = Long.parseLong(root.attributeValue(DEVICE_TYPE));
        String deviceNameVal = root.attributeValue(DEVICE_NAME);
        logger.debug("begin to load config,device name:" + deviceNameVal);
        Map<String, DeviceVersion> versionMap = allVersions.get(typeId);
        Map<String, DeviceVersionInfo> funcMap = allFuncs.get(typeId);
        if (versionMap == null) {
            versionMap = new HashMap<String, DeviceVersion>();
            allVersions.put(typeId, versionMap);
        }
        if (funcMap == null) {
            funcMap = new HashMap<String, DeviceVersionInfo>();
            allFuncs.put(typeId, funcMap);
        }

        Element versions = root.element(VERSSIONS);
        Iterator<?> it = versions.elementIterator(VERSION);
        Element versionEle;
        String versionName;
        String baseVersion;
        Element functionsEle;
        Element funcEle;
        String funcName;
        String funcStatus;
        Element indexEle;
        String key;
        String value;
        DeviceVersion version;
        while (it.hasNext()) {
            versionEle = (Element) it.next();
            versionName = versionEle.attributeValue(NAME);
            baseVersion = versionEle.attributeValue(BASEVERSION);
            version = new DeviceVersion(typeId, versionName, baseVersion);
            versionMap.put(versionName, version);
            if (!funcMap.containsKey(versionName)) {
                funcMap.put(versionName, new DeviceVersionInfo());
            }
            DeviceVersionInfo versionInfo = funcMap.get(versionName);
            functionsEle = versionEle.element(FUNCTIONS);
            Iterator<?> itF = functionsEle.elementIterator(FUNCTION);
            while (itF.hasNext()) {
                funcEle = (Element) itF.next();
                funcName = funcEle.attributeValue(NAME);
                funcStatus = funcEle.attributeValue(STATUS);
                DeviceFunction deviceFunction = new DeviceFunction(typeId, versionName, funcName);
                deviceFunction.setStatus(funcStatus);
                Map<String, String> paramMap = null;
                paramMap = new HashMap<String, String>();
                @SuppressWarnings("rawtypes")
                Iterator itI = funcEle.elementIterator(INDEX);
                while (itI.hasNext()) {
                    indexEle = (Element) itI.next();
                    key = indexEle.attributeValue(KEY);
                    value = indexEle.attributeValue(VALUE);
                    paramMap.put(key, value);
                    if (logger.isDebugEnabled()) {
                        logger.debug("load config info: deviceType(" + typeId + "),deviceName(" + deviceNameVal
                                + "),versionName(" + versionName + "),baseVersion(" + baseVersion + "),funcName("
                                + funcName + "),funcStatus(" + funcStatus + "),key(" + key + "),value(" + value + ")");
                    }
                }// END of While
                deviceFunction.setDeviceVersionParam(paramMap);
                versionInfo.addFunction(deviceFunction);
            }// END OF While
             // 获取当前版本的父版本的DeviceVersionInfo信息，并将其合并到当前版本的versionInfo中
            DeviceVersionInfo parentVersion = new DeviceVersionInfo();
            // 去掉if else判断----if，else结果是一样的
            /*if (typeId.equals(30001L)) {
                parentVersion = funcMap.get(getNearestParentVersion(typeId, versionName));
            } else {*/
            parentVersion = funcMap.get(getNearestParentVersion(typeId, versionName));
            //}
            versionInfo.mergeDeviceVersionInfo(parentVersion);
        }
    }

    public JSONObject getVersionControl(Long typeId, String version) {
        JSONObject controls = new JSONObject();
        if (allVersions.containsKey(typeId)) {
            DeviceVersionInfo $dvinfo = getDeviceVersionInfo(typeId, version);
            Map<String, DeviceFunction> $funtions = $dvinfo.allFunction;
            for (String $fname : $funtions.keySet()) {
                JSONObject $param = new JSONObject();
                DeviceFunction $function = $funtions.get($fname);
                String $functionName = $function.getDeviceVersionFun();
                Map<String, String> $versionParams = $function.getDeviceVersionParam();
                for (String $versionParam : $versionParams.keySet()) {
                    $param.put($versionParam, $versionParams.get($versionParam));
                }
                $param.put("status", $function.getStatus());
                controls.put($functionName, $param);
            }
        }
        return controls;
    }

    private class DeviceVersionInfo {
        Map<String, DeviceFunction> allFunction;

        DeviceVersionInfo() {
            allFunction = new HashMap<String, DeviceFunction>();
        }

        void addFunction(DeviceFunction fun) {
            String key = fun.getDeviceVersionFun();
            if (allFunction.containsKey(key)) {
                DeviceFunction destFunction = allFunction.get(key);
                if (destFunction == null) {
                    DeviceFunction copy = new DeviceFunction(fun.getTypeId(), fun.getDeviceVersionName(),
                            fun.getDeviceVersionFun());
                    copy.setDeviceVersionParam(fun.getDeviceVersionParam());
                    allFunction.put(fun.getDeviceVersionFun(), copy);
                    return;
                }
                Map<String, String> dest = destFunction.getDeviceVersionParam();
                Map<String, String> src = fun.getDeviceVersionParam();
                Iterator<String> it = null;
                if (src == null) {
                    return;
                }
                it = src.keySet().iterator();
                if (dest == null) {
                    dest = new HashMap<String, String>();
                }
                while (it.hasNext()) {
                    String param = it.next();
                    if (!dest.containsKey(param)) {
                        dest.put(param, src.get(param));
                    }
                }
                destFunction.setDeviceVersionParam(dest);

            } else {
                allFunction.put(fun.getDeviceVersionFun(), fun);
            }
        }

        void mergeDeviceVersionInfo(DeviceVersionInfo parent) {
            if (parent == null) {
                return;
            }
            Map<String, DeviceFunction> map = parent.allFunction;
            if (map != null && map.size() > 0) {
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String funcName = it.next();
                    addFunction(map.get(funcName));
                }
            }
        }

        @Override
        public String toString() {
            return "DeviceVersionInfo[allFunction=" + allFunction.toString() + "]";
        }
    }

    /**
     * 比较两个版本的大小
     * 
     * @param srcVersion
     * @param dstVersion
     * @return EQUAL(0): srcVersion = dstVersion LT(-1): srcVersion < dstVersion GT(1): scrVersion >
     *         dstVersion ERROR(-2): 两个版本格式不一致，无法比较
     */
    public Integer compareVersion(String srcVersion, String dstVersion) {
        if (srcVersion == null || dstVersion == null) {
            return ERROR;
        }
        if (!containNum.matcher(srcVersion).matches() || !containNum.matcher(dstVersion).matches()) {
            if (srcVersion.equals(dstVersion)) {
                return EQUAL;
            } else {
                return ERROR;
            }
        }
        String[] sc = srcVersion.split("\\d+");
        String[] dc = dstVersion.split("\\d+");
        // 判断两个版本的命名规则是否一致，若不一致则返回错误
        for (int i = 0; i < sc.length && i < dc.length; i++) {
            if (!sc[i].equals(dc[i])) {
                return ERROR;
            }
        }
        String[] src = srcVersion.split("[\\.-]");
        String[] dst = dstVersion.split("[\\.-]");
        // 比较两个命名规则相同的版本号的大小
        for (int i = 0; i < src.length && i < dst.length; i++) {
            String s = src[i];
            String d = dst[i];
            s = s.replaceAll("\\D+", "");
            d = d.replaceAll("\\D+", "");
            if (!"".equals(s) && !"".equals(s)) {
                if (Integer.parseInt(s) > Integer.parseInt(d)) {
                    return GT;
                } else if (Integer.parseInt(s) < Integer.parseInt(d)) {
                    return LT;
                }
            }
        }
        if (src.length > dst.length) {
            return GT;
        } else if (src.length < dst.length) {
            return LT;
        } else {
            return EQUAL;
        }
    }

    /**
     * 获取小于指定版本名称最近的版本
     * 
     * @param typeId
     * @param versionName
     * @return 版本名称
     */
    private String getNearestParentVersion(Long typeId, String versionName) {
        String version = BASEVERSION_VALUE;
        Map<String, DeviceVersion> versions = allVersions.get(typeId);
        Iterator<String> it = versions.keySet().iterator();
        DeviceVersion deviceVersion = versions.get(versionName);
        if (deviceVersion != null && deviceVersion.getDeviceVersionParent() != null) {
            return deviceVersion.getDeviceVersionParent();
        }
        while (it.hasNext()) {
            String key = it.next();
            DeviceVersion v = versions.get(key);
            String vName = v.getDeviceVersionName();
            if (typeId.equals(v.getTypeId())) {
                if (compareVersion(versionName, vName) == GT && isParentVersion(versionName, vName)) {
                    // 判断versionName>vName,并且vName是versionName的祖先版本
                    int compare = compareVersion(vName, version);
                    if (compare == GT) {
                        version = vName;
                    } else if (BASEVERSION_VALUE.equals(version)) {
                        version = vName;
                    }
                }
            }
        }
        return version;
    }

    /**
     * 判断version2是否为version1的祖先版本，version1必须大于version2。 在如下条件下version2为version1的祖先版本。
     * 1.version2<version1 2.version2有一段与version1不一样，并且从该段之后version2的各段均为0
     * 
     * @param version1
     * @param version2
     * @return
     */
    private static Boolean isParentVersion(String version1, String version2) {
        String[] vs1 = version1.split("[\\.-]");
        String[] vs2 = version2.split("[\\.-]");
        if (vs2.length > vs1.length) {
            // 如果version2的长度长于version1的长度，并且多出来的长度不是0版本，则认为version2不是version1的祖先版本
            for (int i = vs1.length; i < vs2.length; i++) {
                String is = vs2[i].replaceAll("\\D+", "");
                String cs = vs2[i].replaceAll("\\d+", "");
                if (!is.equals("0") || !cs.equals("")) {
                    return false;
                }
            }
        }
        int l = vs2.length > vs1.length ? vs1.length : vs2.length;
        int i;
        // 查找version1与version2不相同的起始位
        for (i = 0; i < l; i++) {
            String is1 = vs1[i].replaceAll("\\D+", "");
            String is2 = vs2[i].replaceAll("\\D+", "");
            String cs1 = vs1[i].replaceAll("\\d+", "");
            String cs2 = vs2[i].replaceAll("\\d+", "");
            if (cs1.equals(cs2)) {
                if (!is1.equals(is2)) {
                    break;
                }
            } else {
                return false;
            }
        }
        for (i = i + 1; i < l; i++) {
            String is = vs2[i].replaceAll("\\D+", "");
            String cs = vs2[i].replaceAll("\\d+", "");
            if (!is.equals("0") || !cs.equals("")) {
                return false;
            }
        }
        return true;
    }

    private DeviceVersionInfo getDeviceVersionInfo(Long typeId, String versionName) {
        Map<String, DeviceVersion> deviceVersions = allVersions.get(typeId);
        Map<String, DeviceVersionInfo> deviceFuncs = allFuncs.get(typeId);
        if (deviceVersions == null || deviceFuncs == null) {
            return null;
        }
        DeviceVersion version = deviceVersions.get(versionName);
        if (version == null) {
            String parentStr = getNearestParentVersion(typeId, versionName);
            if (!deviceFuncs.containsKey(parentStr)) {
                parentStr = deviceVersions.get(parentStr).getDeviceVersionParent();
            }
            version = new DeviceVersion(typeId, versionName, parentStr);
            deviceVersions.put(versionName, version);
        }
        DeviceVersionInfo versionInfo = deviceFuncs.get(versionName);
        if (versionInfo == null) {
            versionInfo = deviceFuncs.get(version.getDeviceVersionParent());
        }
        return versionInfo;
    }

    public String getParamValue(Long typeId, String versionName, String functionName, String paramName) {
        String result = null;
        DeviceVersionInfo versionInfo = getDeviceVersionInfo(typeId, versionName);
        if (versionInfo != null) {
            DeviceFunction function = versionInfo.allFunction.get(functionName);
            if (function != null) {
                Map<String, String> param = function.getDeviceVersionParam();
                if (function != null && param != null) {
                    result = param.get(paramName);
                }
            }
        }
        return result;
    }

    public Boolean isSupportFunction(Long typeId, String versionName, String functionName) {
        boolean result = false;
        DeviceVersionInfo versionInfo = getDeviceVersionInfo(typeId, versionName);
        if (versionInfo != null) {
            DeviceFunction function = versionInfo.allFunction.get(functionName);
            if (function != null) {
                String status = function.getStatus();
                if (status != null && ("disabled".equals(status) || "hidden".equals(status))) {
                    result = false;
                } else {
                    result = true;
                }
            }
        }
        return result;

    }

    /**
     * @return the rs
     */
    public Resource[] getRs() {
        return rs;
    }

    /**
     * @param rs
     *            the rs to set
     */
    public void setRs(Resource[] rs) {
        this.rs = rs;
    }

    /**
     * @return the instance
     */
    public static DeviceVersionXmlParser getInstance() {
        return instance;
    }

    private class DeviceVersion implements Serializable {
        private static final long serialVersionUID = 7248322781471522328L;
        private Long typeId;
        private String deviceVersionName;
        private String deviceVersionParent;

        public DeviceVersion(Long typeId, String deviceVersionName, String deviceVersionParent) {
            this.typeId = typeId;
            this.deviceVersionName = deviceVersionName;
            this.deviceVersionParent = deviceVersionParent;
        }

        /**
         * @return the typeId
         */
        public Long getTypeId() {
            return typeId;
        }

        /**
         * @return the deviceVersionName
         */
        public String getDeviceVersionName() {
            return deviceVersionName;
        }

        /**
         * @return the deviceVersionParent
         */
        public String getDeviceVersionParent() {
            return deviceVersionParent;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj != null && DeviceVersion.class.isInstance(obj)) {
                if (typeId == null || deviceVersionName == null || ((DeviceVersion) obj).getTypeId() == null
                        || ((DeviceVersion) obj).getDeviceVersionName() == null) {
                    return false;
                }
                if (typeId.equals(((DeviceVersion) obj).getTypeId())
                        && deviceVersionName.equals(((DeviceVersion) obj).getDeviceVersionName())) {
                    return true;
                }
            }
            return false;
        }

    }

    private class DeviceFunction implements Serializable {
        private static final long serialVersionUID = 4679697767135473265L;
        private Long typeId;
        private String deviceVersionName;
        private String deviceVersionFun;
        private String status;
        /**
         * 保存设备版本功能支持的参数MAP，Key为参数名
         */
        private Map<String, String> deviceVersionParam;

        public DeviceFunction(Long typeId, String deviceVersionName, String deviceVersionFun) {
            this.typeId = typeId;
            this.deviceVersionName = deviceVersionName;
            this.deviceVersionFun = deviceVersionFun;
        }

        /**
         * @return the typeId
         */
        public Long getTypeId() {
            return typeId;
        }

        /**
         * @return the deviceVersionName
         */
        public String getDeviceVersionName() {
            return deviceVersionName;
        }

        /**
         * @return the deviceVersionFun
         */
        public String getDeviceVersionFun() {
            return deviceVersionFun;
        }

        /**
         * @return the deviceVersionParam
         */
        public Map<String, String> getDeviceVersionParam() {
            return deviceVersionParam;
        }

        /**
         * @param deviceVersionParam
         *            the deviceVersionParam to set
         * @throws IOException
         */
        public void setDeviceVersionParam(Map<String, String> deviceVersionParam) {
            this.deviceVersionParam = deviceVersionParam;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj != null && DeviceFunction.class.isInstance(obj)) {
                if (typeId == null || deviceVersionName == null || deviceVersionFun == null) {
                    return false;
                }
                if (typeId.equals(((DeviceFunction) obj).getTypeId())
                        && deviceVersionName.equals(((DeviceFunction) obj).getDeviceVersionName())
                        && deviceVersionFun.equals(((DeviceFunction) obj).getDeviceVersionFun())) {
                    return true;
                }
            }
            return false;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
