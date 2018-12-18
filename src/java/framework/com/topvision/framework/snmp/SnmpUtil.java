/***********************************************************************
 * $Id: SnmpUtil.java,v 1.1 2007-8-24 下午05:46:58 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2006 WantTo All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpIndex;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.ChoiceType;
import net.percederberg.mibble.type.ElementType;
import net.percederberg.mibble.type.IntegerType;
import net.percederberg.mibble.type.ObjectIdentifierType;
import net.percederberg.mibble.type.SizeConstraint;
import net.percederberg.mibble.type.StringType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeListener;
import org.snmp4j.util.TreeUtils;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.SnmpV3ErrorInfo;
import com.topvision.framework.domain.IpsAddress;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.exception.AnnotationException;
import com.topvision.framework.exception.engine.IllegalCommunityException;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpGetException;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.exception.engine.SnmpNoSuchObjectException;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.exception.engine.SnmpV3Exception;
import com.topvision.framework.exception.engine.UnknownSnmpSetException;

/**
 * 此程序封装了snmp采集细节，把SNMP4J和mib解析组合起来，可以根据名称来获取mib值。 <br/>
 * <ul>
 * 支持此类的第三方包需要
 * <li>SNMP4J.jar</li>
 * <li>grammatica-bin-1.4.jar</li>
 * <li>mibble-parser-2.8.jar</li>
 * <li>log4j.jar</li>
 * </ul>
 * <ul>
 * 还需要mib库文件，默认mib库文件放置于工作目录的下的mib/文件夹下.
 * </ul>
 * 
 * <h3>Example</h3> <blockquote>
 * 
 * <pre>
 * SnmpUtil snmpUtil = new SnmpUtil();
 * snmpUtil.setTargetHost(&quot;192.168.0.10&quot;);
 * snmpUtil.setCommunity(&quot;private&quot;);
 * if (!snmpUtil.verifySnmpVersion()) {
 *     StringBuffer sb = new StringBuffer(&quot;Snmp error:\n\tThe dest[&quot;);
 *     System.out.println(sb.append(snmpUtil.getTargetHost()).append(&quot;] not support snmp protocol &quot;)
 *             .append(&quot;\n\tor snmpd service not active \n\tor snmp port[&quot;).append(snmpUtil.getTargetPort())
 *             .append(&quot;] error \n\tor community[&quot;).append(snmpUtil.getCommunity()).append(&quot;] error!&quot;));
 *     return;
 * }
 * snmpUtil.loadMibs(&quot;RFC1213-MIB,BRIDGE-MIB&quot;);
 * String[] ifTableBaseOids = { &quot;1.3.6.1.2.1.2.2.1.1&quot;, &quot;1.3.6.1.2.1.2.2.1.2&quot;, &quot;1.3.6.1.2.1.2.2.1.3&quot;,
 *         &quot;1.3.6.1.2.1.2.2.1.4&quot;, &quot;1.3.6.1.2.1.2.2.1.5&quot;, &quot;1.3.6.1.2.1.2.2.1.6&quot;, &quot;1.3.6.1.2.1.2.2.1.7&quot;,
 *         &quot;1.3.6.1.2.1.2.2.1.8&quot;, &quot;1.3.6.1.2.1.2.2.1.9&quot; };
 * String[][] ifTableBase = snmpUtil.getTable(ifTableBaseOids);
 * String[][] ifTable = snmpUtil.getTable(&quot;ifTable&quot;);
 * String[][] dot1dTpFdbTable = snmpUtil.getTable(&quot;dot1dTpFdbTable&quot;);
 * System.out.println(&quot;ifTableBase\n&quot; + snmpUtil.table2String(ifTableBase));
 * System.out.println(&quot;ifTable\n&quot; + snmpUtil.table2String(ifTable));
 * System.out.println(&quot;dot1dTpFdbTable\n&quot; + snmpUtil.table2String(dot1dTpFdbTable));
 * </pre>
 * 
 * </blockquote>
 * 
 * <b>注意</b>
 * <ul>
 * <li>此版本只支持snmp V1和V2C版本，不支持V3版本。</li>
 * <li>所有获取方法都有可能抛出SnmpException异常</li>
 * </ul>
 * 
 * @Create Date 2007-8-24 下午05:46:58
 * 
 * @author kelers
 * 
 * 
 */
public class SnmpUtil implements Serializable {
    private static final long serialVersionUID = 8889537901870046810L;
    public static final Pattern oidPattern = Pattern.compile("[.[0-9]++]++");
    public static final Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([:][0-9a-fA-F]{2})+$");
    public static final Pattern hexStringPattern0x = Pattern.compile("^0x[0-9a-fA-F]{2}$");
    private static Logger logger = LoggerFactory.getLogger(SnmpUtil.class);
    // private static Logger loggerBoard = LoggerFactory.getLogger("loggerBoard");

    /**
     * snmp协议端口
     */
    private int snmpPort = 161; // snmp port;
    private Snmp snmp = null;
    private Target target = null;
    private DefaultPDUFactory pduFactory = null;
    private MibManager mibManager;
    private List<Mib> mibs = null;
    /**
     * 对于unicode进行匹配，如果匹配后会重新转换gbk。
     */
    private Pattern pattern = null;
    private String community = null;
    private String writeCommunity = null;
    private String deviceVersion = null;

    /**
     * 构造默认参数的snmpUtil.
     * 
     */
    public SnmpUtil() throws SnmpException {
        this(new SnmpParam());
    }

    /**
     * 构造给定参数的snmpUtil
     * 
     * @param param
     *            参数值
     */
    public SnmpUtil(SnmpParam param) throws SnmpException {
        try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            LogFactory.setLogFactory(new Log4jLogFactory());
            pduFactory = new DefaultPDUFactory();
            transport.listen();
            mibManager = MibManager.getInstance();
            mibs = new ArrayList<Mib>();
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("create snmp error", ex);
            }
        }
        if (param != null) {
            reset(param);
        }
    }

    /**
     * 重新设置snmp 参数
     * 
     * @param param
     *            snmp参数
     */
    public void reset(SnmpParam param) {
        if (param.getVersion() == SnmpConstants.version3) {
            if (target == null || !(target instanceof UserTarget)) {
                target = new UserTarget();
            }
            UserTarget ut = (UserTarget) target;
            if (param.getAuthoritativeEngineID() != null) {
                ut.setAuthoritativeEngineID(param.getAuthoritativeEngineID().getBytes());
            }
            ut.setSecurityLevel(param.getSecurityLevel());
            ut.setSecurityName(new OctetString(param.getUsername()));

            // default is MD5.
            OID authID = AuthMD5.ID;
            if (param.getAuthProtocol().equalsIgnoreCase("SHA")) {
                authID = AuthSHA.ID;
            }
            // default is DES.
            OID privID = PrivDES.ID;
            if (param.getPrivProtocol().equalsIgnoreCase("3DES")) {
                privID = Priv3DES.ID;
            } else if (param.getPrivProtocol().equalsIgnoreCase("AES128")) {
                privID = PrivAES128.ID;
            } else if (param.getPrivProtocol().equalsIgnoreCase("AES192")) {
                privID = PrivAES192.ID;
            } else if (param.getPrivProtocol().equalsIgnoreCase("AES256")) {
                privID = PrivAES256.ID;
            }
            snmp.getUSM().removeAllUsers();
            snmp.getUSM().addUser(new OctetString(param.getUsername()),
                    new UsmUser(new OctetString(param.getUsername()), authID, new OctetString(param.getAuthPassword()),
                            privID, new OctetString(param.getPrivPassword())));
        } else {
            if (target == null || !(target instanceof CommunityTarget)) {
                target = new CommunityTarget();
            }
            community = param.getCommunity();
            writeCommunity = param.getWriteCommunity();
            deviceVersion = param.getDeviceVersion();
            setCommunity(community);
        }
        try {
            target.setAddress(GenericAddress.parse("udp:" + param.getIpAddress() + "/" + param.getPort()));
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("setAddress", ex);
            }
        }
        pattern = Pattern.compile("([0-9a-fA-F]{2}:)*[0-9a-fA-F]{2}");
        try {
            setRetries(param.getRetry());
            setVersion(param.getVersion());
            setTimeout(param.getTimeout());
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("set params error:", ex);
            }
        }
        // Modify by victor@20130224 修改为把配置的mib放到list最前面，优先查找到，然后是配置过的，最后是其他的。
        if (mibs != null) {
            mibs.clear();
        }
        if (param.getMibs() != null) {
            for (String name : param.getMibs().split("[,; \uff0c\uff1b]")) {
                Mib mib = mibManager.getMibByName(name);
                if (mib != null) {
                    mibs.add(mib);
                }
            }
            for (Mib mib : mibManager.getConfMibs()) {
                if (!mibs.contains(mib)) {
                    mibs.add(mib);
                }
            }
            for (Mib mib : mibManager.getAllMibs()) {
                if (!mibs.contains(mib)) {
                    mibs.add(mib);
                }
            }
        }
    }

    /**
     * 判断snmp版本和判断community是否正确。
     * 
     * 修改目标地址、端口、community、timeout、retries等参数后需要使用本方法判断设置参数是否正确
     * 
     * @return verify snmp.
     */
    public synchronized boolean verifySnmpVersion() {
        String result = null;
        PDU pdu = pduFactory.createPDU(target);
        pdu.add(new VariableBinding(SnmpConstants.sysObjectID));
        for (int index = 0; index < 2; index++) {
            try {
                result = snmp.get(pdu, target).getResponse().get(0).getVariable().toString();
            } catch (Exception e) {
            }
            if (result != null && !result.equalsIgnoreCase("null")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Device[{}] Snmp Version:{} true", getTargetHost(), getVersionName());
                }
                return true;
            }
            if (target.getVersion() == SnmpConstants.version3) {
                break;
            }
            changeVersion();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Device[{}] Snmp Version:{} false", getTargetHost(), getVersionName());
        }
        return false;
    }

    /**
     * 与getLeaf的区别是不会在oid后面加.0
     * 
     * @param oid
     *            完整的OID
     * @return
     * @throws SnmpException
     */
    public String get(String oid) throws SnmpException {
        try {
            oid = verifyOid(oid);
            if (oid == null) {
                return null;
            }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(new VariableBinding(new OID(oid)));
            pdu = snmp.get(pdu, target).getResponse();
            checkResponsePdu(pdu, oid);
            return variable2String(pdu.get(0));
        } catch (SnmpException ex) {
            throw ex;
        } catch (MessageException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("{}", ex.getStatusInformation());
                logger.debug(target.getAddress() + "\nSnmpUtil.get:" + oid, ex);
            }
            throw new SnmpException("Get[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.get:" + oid, ex);
            }
            throw new SnmpException("Get[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        }
    }

    /**
     * 与getLeaf的区别是不会在oid后面加.0
     * 
     * @param oid
     *            完整的OID
     * @return
     * @throws SnmpException
     */
    public VariableBinding getVB(String oid) throws SnmpException {
        try {
            oid = verifyOid(oid);
            if (oid == null) {
                return null;
            }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(new VariableBinding(new OID(oid)));
            pdu = snmp.get(pdu, target).getResponse();
            checkResponsePdu(pdu, oid);
            return pdu.get(0);
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.getVB:" + oid, ex);
            }
            throw new SnmpException("GetVB[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        }
    }

    /**
     * 获取给定OID的下一个oid的值
     * 
     * @param oid
     *            完整的OID
     * @return
     * @throws SnmpException
     */
    public String getNext(String oid) throws SnmpException {
        try {
            oid = verifyOid(oid);
            if (oid == null) {
                return null;
            }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(new VariableBinding(new OID(oid)));
            pdu = snmp.getNext(pdu, target).getResponse();
            checkResponsePdu(pdu, oid);
            return variable2String(pdu.get(0));
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.getNext:" + oid, ex);
            }
            throw new SnmpException("GetNext[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        }
    }

    /**
     * 获取给定OID的下一个oid的值，以原始SNMP4J的绑定返回
     * 
     * @param oid
     *            完整的OID
     * @return
     * @throws SnmpException
     */
    public VariableBinding getNextVB(String oid) throws SnmpException {
        try {
            oid = verifyOid(oid);
            if (oid == null) {
                return null;
            }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(new VariableBinding(new OID(oid)));
            pdu = snmp.getNext(pdu, target).getResponse();
            checkResponsePdu(pdu, oid);
            return pdu.get(0);
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.getNextVB:" + oid, ex);
            }
            throw new SnmpException("GetNextVB[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        }
    }

    /**
     * 如果V2C以上版本使用GetBulk一次性获取值，注意是GetBulk对所有OID都是调用GetNext。所以如果是叶子节点则不能添加.0。否则值将错位
     * 
     * @param oids
     *            需要获取的OID
     * @return oids所对应next值
     * @throws SnmpException
     */
    public String[] get(String[] oids) throws SnmpException {
        try {
            // 如果是V1版本则一个一个的去取
            if (target.getVersion() == SnmpConstants.version1) {
                String[] result = new String[oids.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = get(oids[i]);
                }
                return result;
            }
            // 如果是V2C以上版本则采用get方式取
            PDU pdu = pduFactory.createPDU(target);
            for (int i = 0; i < oids.length; i++) {
                String oid = verifyOid(oids[i]);
                if (oid == null) {
                    continue;
                }
                pdu.add(new VariableBinding(new OID(oid)));
            }
            pdu.setMaxRepetitions(1);
            pdu = snmp.get(pdu, target).getResponse();
            checkResponsePdu(pdu, oids[0]);

            String[] result = new String[pdu.getVariableBindings().size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = variable2String(pdu.get(i));
            }
            return result;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.get:" + array2String(oids), ex);
            }
            throw new SnmpException("Get[" + target.getAddress() + "][OID=" + array2String(oids) + "]Error", ex);
        }
    }

    /**
     * 返回给定节点相关的index SnmpIndex List，给定oid可以是table，table entry或者table column， 类型可以是OID或者名称
     * 
     * @param oid
     *            oid或者mib名称，可以是table，table entry或者table column
     * @return 给定oid的相关Index列表，没有则返回空
     * @see #loadMib(String)
     * @see #isIndex(String)
     * @see #getIndexOid(String)
     */
    @SuppressWarnings("unchecked")
    public List<SnmpIndex> getIndex(String oid) {
        if (oid == null) {
            return null;
        }
        if (oid.indexOf('.') == -1) {
            try {
                oid = getOidByName(oid);
            } catch (SnmpException ex) {
            }
            if (oid == null) {
                return null;
            }
        }
        MibValueSymbol symbol = getTableMibValueSymbol(oid);
        if (symbol == null) {
            return null;
        }
        // if (logger.isDebugEnabled()) {
        // logger.debug("getIndex.symbol=" + symbol);
        // }
        if (symbol.isScalar()) {
            return null;
        } else if (symbol.isTable()) {
            symbol = symbol.getChild(0);
        } else if (symbol.isTableColumn()) {
            symbol = symbol.getParent();
        }
        if (symbol.getType() != null && symbol.getType() instanceof SnmpObjectType) {
            SnmpObjectType type = (SnmpObjectType) symbol.getType();
            // Modify by Rod
            if (type.getAugments() == null && type.getIndex().size() > 0) {
                // Entry use Index
                return type.getIndex();
            }
            if (type.getAugments() != null && type.getIndex().size() == 0) {
                // Entry use Augments
                MibValueSymbol augmentsSymbol = getTableMibValueSymbol(type.getAugments());
                SnmpObjectType augmentsType = (SnmpObjectType) augmentsSymbol.getType();
                return augmentsType.getIndex();
            }
            // 根据RFC2578 定义不会出现第三种情况
            return type.getIndex();
        }
        return null;
    }

    /**
     * 获得表的节点的OID长度，不能用Index的长度，因为Index的长度不一定与节点的OID长度一致
     * 
     * @param oid
     * @return
     */
    public int getTableColumnLength(String oid) {
        if (oid == null) {
            return 0;
        }
        if (oid.indexOf('.') == -1) {
            try {
                oid = getOidByName(oid);
            } catch (SnmpException ex) {
            }
            if (oid == null) {
                return 0;
            }
        }
        // TODO 需要修改 采集设置mibs优先查找，查找不到使用配置文件，查找不到使用目录下所有MIB文件查找
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isTableColumn()) {
                break;
            }
        }
        if (symbol == null) {
            return 0;
        }
        // if (logger.isDebugEnabled()) {
        // logger.debug("getIndex.symbol=" + symbol);
        // }
        if (symbol.isTableColumn()) {
            return symbol.getValue().toString().length();
        }
        return 0;
    }

    /**
     * 返回给定节点相关的index OID数组，给定oid可以是table，table entry或者table column， 类型可以是OID或者名称
     * 
     * @param oid
     *            oid或者mib名称，可以是table，table entry或者table column
     * @return 给定oid的相关Index列表，没有则返回空
     * @see #loadMib(String)
     * @see #isIndex(String)
     * @see #getIndex(String)
     */
    public String[] getIndexOid(String oid) {
        List<SnmpIndex> list = getIndex(oid);
        if (list == null || list.isEmpty()) {
            return null;
        }
        // if (logger.isDebugEnabled()) {
        // logger.debug("getIndexOid.indexs:" + list);
        // }
        String[] indexs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            SnmpIndex index = list.get(i);
            indexs[i] = index.toString();
        }
        return indexs;
    }

    /**
     * 
     * @param list
     *            table value list
     * @return map of index OID with value
     */
    private Map<String, String> getIndexValues(VariableBinding[] vbs) {
        Map<String, String> values = new HashMap<String, String>();
        VariableBinding vb = null;
        for (int j = 0; j < vbs.length; j++) {
            vb = vbs[j];
            if (vb != null) {
                break;
            }
        }
        if (vb == null) {
            return values;
        }
        // if (logger.isDebugEnabled()) {
        // logger.debug("getInd exValues.oid=" + vb.getOid());
        // }
        List<SnmpIndex> indexs = getIndex(vb.getOid().toString());
        // if (logger.isDebugEnabled()) {
        // logger.debug("getIndexValues.indexs:" + indexs);
        // }
        if (indexs == null || indexs.size() == 0) {
            return values;
        }

        // 处理TrapServer表
        if (vb.getOid().toString().indexOf("1.3.6.1.4.1.17409.2.2.11.3.1.1") != -1) {
            int[] is = vb.getOid().getValue();
            SnmpIndex firstIndex = indexs.get(0);
            int oidLength = firstIndex.getValue().toString().split("\\.").length;
            int firstIndexLength = is[oidLength];
            if (firstIndexLength == is.length - oidLength - 1) {
                byte[] bytes = new byte[firstIndexLength];
                for (int i = 0; i < firstIndexLength; i++) {
                    // Index with Length
                    bytes[i] = (byte) (is[oidLength + i + 1]);
                }
                try {
                    values.put(firstIndex.getValue().toString(), new String(bytes, "ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("", e);
                }
            } else {
                byte[] bytes = new byte[is.length - oidLength];
                for (int i = 0; i < is.length - oidLength; i++) {
                    // Index with Length
                    bytes[i] = (byte) (is[oidLength + i]);
                }
                try {
                    values.put(firstIndex.getValue().toString(), new String(bytes, "ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("", e);
                }
            }
            return values;
        }
        // 处理fileInfoManagementTable表
        if (vb.getOid().toString().indexOf("1.3.6.1.4.1.17409.2.3.1.6.2.1") != -1) {
            int[] is = vb.getOid().getValue();
            SnmpIndex firstIndex = indexs.get(0);
            SnmpIndex secondIndex = indexs.get(1);
            int oidLength = firstIndex.getValue().toString().split("\\.").length;
            int firstIndexLength = is[oidLength];
            if (is.length == oidLength + 1 + firstIndexLength) {
                byte[] bytes = new byte[firstIndexLength];
                for (int i = 0; i < firstIndexLength; i++) {
                    bytes[i] = (byte) (is[oidLength + i + 1]);
                }
                try {
                    values.put(firstIndex.getValue().toString(), new String(bytes, "ISO-8859-1"));
                    values.put(secondIndex.getValue().toString(), null);
                } catch (UnsupportedEncodingException e) {
                    logger.warn("", e);
                }
            } else {
                byte[] firsetBytes = new byte[firstIndexLength];
                for (int i = 0; i < firstIndexLength; i++) {
                    firsetBytes[i] = (byte) (is[oidLength + i + 1]);
                }
                try {
                    values.put(firstIndex.getValue().toString(), new String(firsetBytes, "ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("", e);
                }
                int secondIndexLength = is[oidLength + firstIndexLength + 1];
                byte[] secondBytes = new byte[secondIndexLength];
                for (int i = 0; i < secondIndexLength; i++) {
                    secondBytes[i] = (byte) (is[oidLength + firstIndexLength + i + 2]);
                }
                try {
                    values.put(secondIndex.getValue().toString(), new String(secondBytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("", e);
                }
            }
            return values;
        }

        if (vb.getOid().toString().indexOf("1.3.6.1.4.1.32285.11.7.3.1") != -1) {
            int[] is = vb.getOid().getValue();
            SnmpIndex firstIndexOid = indexs.get(0);
            SnmpIndex secondIndexOid = indexs.get(1);
            int oidLength = firstIndexOid.getValue().toString().split("\\.").length;
            int firstIndex = is[oidLength];
            int secondIndexLength = is[oidLength + 1];
            // 第一个索引值
            values.put(firstIndexOid.getValue().toString(), String.valueOf(firstIndex));
            // 第二个索引值
            String secondIndex = "";
            if (secondIndexLength != 0) {
                StringBuffer secondIndexB = new StringBuffer();
                for (int i = 0; i < secondIndexLength + 1; i++) {
                    secondIndexB.append(is[oidLength + 1 + i]).append(".");
                }
                secondIndex = secondIndexB.substring(0, secondIndexB.length() - 1);
            } else {
                secondIndex = "0";
            }
            values.put(secondIndexOid.getValue().toString(), secondIndex);
            return values;
        }

        String oidWithIndex = vb.getOid().toString();
        int columnLength = getTableColumnLength(oidWithIndex);
        String[] oidIndexPart = oidWithIndex.substring(columnLength + 1).split("\\.");
        /*
         * for (int j = indexs.size() - 1; j >= 0; j--) { SnmpIndex index = indexs.get(j); int ind =
         * getType(index, oidWithIndex); // if (logger.isDebugEnabled()) { // logger.debug("index:"
         * + oidWithIndex.substring(ind + 1)); // } values.put(index.toString(),
         * oidWithIndex.substring(ind + 1)); oidWithIndex = oidWithIndex.substring(0, ind); // if
         * (logger.isDebugEnabled()) { // logger.debug("oidWithIndex:" + oidWithIndex); // } }
         */
        for (int j = 0; j < indexs.size(); j++) {
            SnmpIndex index = indexs.get(j);
            String[] result = getType(index, oidIndexPart);
            values.put(index.getValue().toString(), result[0]);
            oidIndexPart = handleOidIndex(oidIndexPart, Integer.parseInt(result[1]));
        }

        return values;
    }

    /**
     * TODO 处理索引时依据对象的字段属性进行反射, 暂时没用使用
     * 
     * @param vbs
     * @param fields
     * @return
     */
    @SuppressWarnings("unused")
    @Deprecated
    private Map<Field, String> getIndexValues(VariableBinding[] vbs, List<Field> fields) {
        Map<Field, String> values = new HashMap<Field, String>();
        VariableBinding vb = null;
        for (int j = 0; j < vbs.length; j++) {
            vb = vbs[j];
            if (vb != null) {
                break;
            }
        }
        if (vb == null) {
            return values;
        }
        List<SnmpIndex> indexs = getIndex(vb.getOid().toString());
        Map<SnmpIndex, Field> indexFieldMap = new HashMap<SnmpIndex, Field>();
        for (Field field : fields) {
            SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
            for (SnmpIndex tmpIndex : indexs) {
                if (tmpIndex.getValue().toString().equals(getOid(prop))) {
                    indexFieldMap.put(tmpIndex, field);
                    break;
                }
            }
        }
        if (indexs == null || indexs.size() == 0) {
            return values;
        }
        // String oidWithIndex = vb.getOid().toString();
        // int columnLength = getTableColumnLength(oidWithIndex);
        // String[] oidIndexPart = oidWithIndex.substring(columnLength + 1).split("\\.");

        // for (int j = 0; j < indexs.size(); j++) {
        // SnmpIndex index = indexs.get(j);
        // Field field = indexFieldMap.get(index);
        // String[] result = getIndexFieldValue(index, oidIndexPart);
        // values.put(field, result[0]);
        // oidIndexPart = handleOidIndex(oidIndexPart, Integer.parseInt(result[1]));
        // }
        return values;
    }

    private String[] handleOidIndex(String[] oidIndexPart, int handleSize) {
        String[] newOidWithoutIndex = new String[oidIndexPart.length - handleSize];
        for (int i = 0; i < newOidWithoutIndex.length; i++) {
            newOidWithoutIndex[i] = oidIndexPart[handleSize + i];
        }
        return newOidWithoutIndex;
    }

    /**
     * 获取mib中叶子节点的值，传入的参数可以叶子节点对应的OID或者名称
     * 
     * eg)以下两种方式都可以获取设备的ID。
     * 
     * 1.get("1.3.6.1.2.1.1.2.0");
     * 
     * 2.get("sysObjectID");
     * 
     * 注意：此方法不适用table中的节点，对于table中的节点，参见getTable.
     * 
     * @param oid
     *            OID或者oid对应得名称
     * @return the value of param oid
     */
    public String getLeaf(String oid) throws SnmpException {
        try {
            oid = verifyOid(oid);
            if (oid == null) {
                return null;
            } else if (!oid.endsWith(".0")) {
                oid = oid + ".0";
            }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(new VariableBinding(new OID(oid)));
            pdu = snmp.get(pdu, target).getResponse();
            checkResponsePdu(pdu, oid);
            return variable2String(pdu.get(0));
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.getLeaf:" + oid, ex);
            }
            throw new SnmpException("getLeaf[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        }
    }

    /**
     * 
     * @param oid
     * @return
     * @throws SnmpException
     */
    public String[] getList(String oid) throws SnmpException {
        try {
            TreeUtils util = new TreeUtils(snmp, pduFactory);
            List<TreeEvent> events = util.getSubtree(target, new OID(oid));
            List<VariableBinding> vbs = new ArrayList<VariableBinding>();
            for (TreeEvent te : events) {
                for (VariableBinding vb : te.getVariableBindings()) {
                    vbs.add(vb);
                }
            }
            String[] lists = new String[vbs.size()];
            for (int i = 0; i < vbs.size(); i++) {
                lists[i] = variable2String(vbs.get(i));
            }
            return lists;
        } catch (Exception ex) {
            logger.warn(target.getAddress() + "\n" + ex, ex);
        }
        return null;
    }

    /**
     * @Add by Rod
     * @param oid
     * @return
     * @throws SnmpException
     */
    public Map<String, String> getListWithOid(String oid) throws SnmpException {
        Map<String, String> rMap = new HashMap<String, String>();
        try {
            TreeUtils util = new TreeUtils(snmp, pduFactory);
            List<TreeEvent> events = util.getSubtree(target, new OID(oid));
            List<VariableBinding> vbs = new ArrayList<VariableBinding>();
            for (TreeEvent te : events) {
                for (VariableBinding vb : te.getVariableBindings()) {
                    vbs.add(vb);
                }
            }
            for (int i = 0; i < vbs.size(); i++) {
                rMap.put(vbs.get(i).getOid().toString(), variable2String(vbs.get(i)));
            }
            return rMap;
        } catch (Exception ex) {
            logger.warn(target.getAddress() + "\n" + ex, ex);
        }
        return null;
    }

    public MibValueSymbol getMibValueSymbol(String oid) throws SnmpException {
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            MibValueSymbol mvs = mib.getSymbolByOid(toMibOid(oid));
            if (mvs == null) {
                continue;
            }
            if (symbol == null) {
                symbol = mvs;
            } else if (mvs != null && mvs.getValue().toString().length() > symbol.getValue().toString().length()) {
                symbol = mvs;
            }
        }
        if (symbol != null) {
            return symbol;
        } else {
            return null;
        }
    }

    public MibValueSymbol getTableMibValueSymbol(String oid) throws SnmpException {
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));

            if (symbol != null && (symbol.isTable() || symbol.isTableRow() || symbol.isTableColumn())) {
                break;
            }
        }
        return symbol;
    }

    public MibValueSymbol getTableMibValueSymbol(MibValue mibValue) throws SnmpException {
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByValue(mibValue);
            if (symbol != null && (symbol.isTable() || symbol.isTableRow() || symbol.isTableColumn())) {
                break;
            }
        }
        return symbol;
    }

    /**
     * 根据mib解析转换oid和名称
     * 
     * 注意：此方法需要设置对应mib库文件
     * 
     * @param oid
     *            OID
     * @return 名称
     */
    public String getNameByOid(String oid) throws SnmpException {
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            MibValueSymbol mvs = mib.getSymbolByOid(toMibOid(oid));
            if (mvs == null) {
                continue;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("getNameByOid[" + mib.getName() + "]:" + mvs.getValue().toString());
            }
            if (symbol == null) {
                symbol = mvs;
            } else if (mvs != null && mvs.getValue().toString().length() > symbol.getValue().toString().length()) {
                symbol = mvs;
            }
        }
        if (symbol != null) {
            return symbol.getName();
        } else {
            return null;
        }
    }

    /**
     * 根据mib解析转换oid和名称
     * 
     * 注意：此方法需要设置对应mib库文件
     * 
     * @param name
     *            oid名称
     * @return OID
     */
    public String getOidByName(String name) throws SnmpException {
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = (MibValueSymbol) mib.findSymbol(name, true);
            if (symbol != null) {
                return symbol.getValue().toString();
            }
        }
        return null;
    }

    /**
     * 获取一个table的值，传入的参数可以table的OID或者名称。
     * 
     * <h3>Example</h3> 以下两种方式获取相同的值。
     * 
     * 1.getTable("1.3.6.1.2.1.2.2");
     * 
     * 2.getTable("ifTable");
     * 
     * <h2>注意</h2>
     * <ul>
     * <li>在mib库中定义的但是OID不在其下不能通过这种方法获取，而可以用<A HREF="#getTable(java.lang.String[])">
     * <CODE>getTable(String[])</CODE></A></li>
     * <h3>Example</h3> 在mib库中dot1qTpFdbTable（1.3.6.1.2.1.17.7.1.2.2）定义了dot1qFdbId（1.3.6.1.2.1.17
     * .7.1.2.1.1.1），但其OID不在 dot1qTpFdbTable之下，所以通过此方法获取的结果中没有dot1qFdbId对应的值。
     * </ul>
     * 
     * @param oid
     *            table的OID或者名称
     * @return table的值，二维数组中一行表示一组table列的值。
     * @see #getTable(String[])
     */
    public String[][] getTable(String oid) throws SnmpException {
        oid = verifyOid(oid);
        if (oid == null) {
            return null;
        }
        String[] columns = getTableColOid(oid);
        return getTable(columns);
    }

    /**
     * 获取一个table的值，传入的参数必须是table中的列OID或者名称。此方法适用于提取一个table中几个列中的值。
     * 
     * <h2>注意</h2>
     * <ul>
     * <li>传入的参数在mib库中必须是一个table中定义，OID不需要一定连续</li>
     * <h3>Example</h3> dot1qTpFdbTable中的dot1qFdbId（1.3.6.1.2.1.17.7.1.2.1.1.1）的OID实际不在
     * dot1qTpFdbTable （1.3.6.1.2.1.17.7.1.2.2）之下，但是在mib库中dot1qTpFdbTable下有定义。所以如下正确<br/>
     * <br/>
     * getTable({"dot1qFdbId","dot1qTpFdbAddress","dot1qTpFdbPort"}) 程序会正常返回结果.<br/>
     * <br/>
     * <li>传入参数不能仅限于index，除非设备对index的OID有正常返回值</li>
     * <h3>Example</h3> getTable({"dot1qFdbId","dot1qTpFdbAddress"})不会返回任何数据<br/>
     * getTable({"ifIndex"}) 程序会正常返回结果
     * </ul>
     * 
     * @param oid
     *            table的OID或者名称
     * @return table的值，二维数组中一行表示一组table列的值。
     * @see #getTable(String)
     */
    public String[][] getTable(String[] columns) throws SnmpException {
        if (columns == null) {
            return null;
        }
        try {
            TableUtils utils = new TableUtils(snmp, pduFactory);
            utils.setMaxNumColumnsPerPDU(columns.length);
            utils.setMaxNumRowsPerPDU(10);
            OID[] oids = new OID[columns.length];
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].indexOf('.') == -1) {
                    columns[i] = getOidByName(columns[i]);
                }
                oids[i] = new OID(columns[i]);
                // if (logger.isDebugEnabled()) {
                // logger.debug("getTable's " + i + " :" + oids[i]);
                // }
            }
            List<TableEvent> list = utils.getTable(target, oids, null, null);
            // if (logger.isDebugEnabled()) {
            // logger.debug(list.toString());
            // }
            String[][] tables = new String[list.size()][columns.length];
            for (int i = 0; i < list.size(); i++) {
                TableEvent event = list.get(i);
                if (event == null || event.getColumns() == null) {
                    continue;
                }
                // if (logger.isDebugEnabled()) {
                // logger.debug(event);
                // }
                VariableBinding[] vbs = event.getColumns();
                Map<String, String> indexValues = getIndexValues(vbs);
                // if (logger.isDebugEnabled()) {
                // logger.debug("indexValues:" + indexValues);
                // }
                for (int j = 0; vbs != null && j < vbs.length; j++) {
                    if (vbs[j] == null) {
                        tables[i][j] = indexValues.get(columns[j]);
                        continue;
                    }
                    // if (logger.isDebugEnabled()) {
                    // logger.debug(vbs[j]);
                    // }
                    tables[i][j] = variable2String(vbs[j]);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\n" + array2String(columns) + "\n" + table2String(tables));
            }
            return tables;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.getTable:" + array2String(columns), ex);
            }
            throw new SnmpException("getTable[" + target.getAddress() + "][OID=" + array2String(columns) + "]Error",
                    ex);
        }
    }

    /**
     * 
     * @param columns
     * @return
     * @throws SnmpException
     */
    public String[][] getTableWithIndex(String[] columns) throws SnmpException {
        if (columns == null) {
            return null;
        }
        try {
            TableUtils utils = new TableUtils(snmp, pduFactory);
            utils.setMaxNumColumnsPerPDU(columns.length);
            utils.setMaxNumRowsPerPDU(100 / utils.getMaxNumColumnsPerPDU());
            OID[] oids = new OID[columns.length - 1];
            for (int i = 0; i < oids.length; i++) {
                if (columns[i + 1].indexOf('.') == -1) {
                    columns[i + 1] = getOidByName(columns[i + 1]);
                }
                oids[i] = new OID(columns[i + 1]);
                // if (logger.isDebugEnabled()) {
                // logger.debug("getTable's " + i + " :" + oids[i]);
                // }
            }
            List<TableEvent> list = utils.getTable(target, oids, null, null);
            // if (logger.isDebugEnabled()) {
            // logger.debug(list.toString());
            // }
            String[][] tables = new String[list.size()][columns.length];
            for (int i = 0; i < list.size(); i++) {
                TableEvent event = list.get(i);
                if (event == null || event.getColumns() == null) {
                    continue;
                }
                // if (logger.isDebugEnabled()) {
                // logger.debug(event);
                // }
                VariableBinding[] vbs = event.getColumns();
                Map<String, String> indexValues = getIndexValues(vbs);
                // if (logger.isDebugEnabled()) {
                // logger.debug("indexValues:" + indexValues);
                // logger.debug("columns[0]:" + columns[0]);
                // }
                tables[i][0] = indexValues.get(columns[0]);
                for (int j = 0; vbs != null && j < vbs.length; j++) {
                    // if (logger.isDebugEnabled()) {
                    // logger.debug(vbs[j]);
                    // }
                    tables[i][j + 1] = variable2String(vbs[j]);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\n" + array2String(columns) + "\n" + table2String(tables));
            }
            return tables;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\n" + "SnmpUtil.getTableWithIndex:" + array2String(columns), ex);
            }
            throw new SnmpException(
                    "getTableWithIndex[" + target.getAddress() + "][OID=" + array2String(columns) + "]Error", ex);
        }
    }

    public String[][] getTableV2C(String[] columns) throws SnmpException {
        if (columns == null || columns.length == 0) {
            return null;
        }

        if (target.getVersion() == SnmpConstants.version1) {
            return getTable(columns);
        }
        try {
            TreeUtils util = new TreeUtils(snmp, pduFactory);
            List<TreeEvent> events = util.getSubtree(target, new OID(columns[0]));
            int rows = 0;
            for (TreeEvent event : events) {
                rows += event.getVariableBindings().length;
            }
            if (rows * columns.length > 100) {
                return getTable(columns);
            }
            PDU pdu = pduFactory.createPDU(target);
            OID[] oids = new OID[columns.length];
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].indexOf('.') == -1) {
                    columns[i] = getOidByName(columns[i]);
                }
                oids[i] = new OID(columns[i]);
                // if (logger.isDebugEnabled()) {
                // logger.debug("getTable's " + i + " :" + oids[i]);
                // }
                pdu.add(new VariableBinding(oids[i]));
            }
            pdu.setType(PDU.GETBULK);
            pdu.setNonRepeaters(0);
            pdu.setMaxRepetitions(rows);
            pdu = snmp.send(pdu, target).getResponse();
            checkResponsePdu(pdu, columns[0]);
            List<? extends VariableBinding> vbs = pdu.getVariableBindings();
            String[][] tables = new String[rows][columns.length];
            for (int i = 0; i < tables.length; i++) {
                for (int j = 0; j < columns.length; j++) {
                    // if (logger.isDebugEnabled()) {
                    // logger.debug(vbs.get(j).toString());
                    // }
                    tables[i][j] = variable2String(vbs.get(i * j + j));
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\n" + array2String(columns) + "\n" + table2String(tables));
            }
            return tables;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.getTableV2C:" + array2String(columns), ex);
            }
            throw new SnmpException("getTableV2C[" + target.getAddress() + "][OID=" + array2String(columns) + "]Error",
                    ex);
        }
    }

    /**
     * 根据给定table的OID获取table列的名称
     * 
     * 注意：此方法需要设置对应mib库文件
     * 
     * @param oid
     *            table的OID
     * @return table对应的所有列名
     */
    public String[] getTableColName(String oid) throws SnmpException {
        if (oid == null) {
            return null;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isTable()) {
                break;
            }
        }
        if (symbol == null) {
            return null;
        }
        symbol = symbol.getChild(0);
        String[] cols = new String[symbol.getChildCount()];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = symbol.getChild(i).getName();
        }
        return cols;
    }

    /**
     * 根据给定table的OID获取table列的OID
     * 
     * 注意：此方法需要设置对应mib库文件
     * 
     * @param oid
     *            table的OID
     * @return table对应的所有列的OID
     */
    public String[] getTableColOid(String oid) throws SnmpException {
        if (oid == null) {
            return null;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isTable()) {
                symbol = symbol.getChild(0);
                String[] cols = new String[symbol.getChildCount()];
                for (int j = 0; j < cols.length; j++) {
                    cols[j] = symbol.getChild(j).getValue().toString();
                }
                return cols;
            }
        }
        return null;
    }

    /**
     * target host
     * 
     * @return host
     */
    public String getTargetHost() {
        String address = target.getAddress().toString();
        int index = address.indexOf('/');
        return index == -1 ? address : address.substring(0, index);
    }

    /**
     * target port
     * 
     * @return port
     */
    public int getTargetPort() throws SnmpException {
        String address = target.getAddress().toString();
        int index = address.indexOf('/');
        return index == -1 ? -1 : Integer.parseInt(address.substring(index + 1));
    }

    private String[] getType(SnmpIndex snmpIndex, String[] oidIndexPart) {
        MibType mibType = snmpIndex.getType();
        if (mibType == null) {
            MibValueSymbol symbol = null;
            for (Mib mib : mibs) {
                symbol = mib.getSymbolByOid(toMibOid(snmpIndex.getValue().toString()));
                if (symbol != null && symbol.isTableColumn()) {
                    break;
                }
            }
            if (symbol != null) {
                mibType = symbol.getType();
            }
        }
        if (mibType == null) {
            return null;
        } else if (mibType instanceof SnmpObjectType) {
            SnmpObjectType type = (SnmpObjectType) mibType;
            if (type.getSyntax() instanceof IntegerType) {
                return new String[] { oidIndexPart[0], "1" };
            } else if (type.getSyntax() instanceof StringType) {
                StringType strType = (StringType) type.getSyntax();
                String tmp = null;
                if (strType.hasConstraint()) {
                    tmp = ((SizeConstraint) strType.getConstraint()).getValues().get(0).toString();
                }
                if ("4".equals(tmp)) {
                    // ipAddress Index
                    StringBuilder ipBuilder = new StringBuilder();
                    for (int i = 0; i < 4; i++) {
                        ipBuilder.append(".").append(Integer.parseInt(oidIndexPart[i]));
                    }
                    return new String[] { ipBuilder.toString().substring(1).toUpperCase(), String.valueOf(4) };
                } else if ("6".equals(tmp)) {
                    // phyAddress Index
                    StringBuilder macBuilder = new StringBuilder();
                    for (int i = 0; i < 6; i++) {
                        macBuilder.append(":").append(bytes2hex(Integer.parseInt(oidIndexPart[i])));
                    }
                    return new String[] { macBuilder.toString().substring(1).toUpperCase(), String.valueOf(6) };
                }
                int strLength;
                if (!snmpIndex.isImplied()) {
                    strLength = Integer.parseInt(oidIndexPart[0]);
                    byte[] bytes = new byte[strLength];
                    for (int i = 0; i < strLength; i++) {
                        // Index with Length
                        bytes[i] = (byte) Integer.parseInt(oidIndexPart[i + 1]);
                    }
                    try {
                        return new String[] { new String(bytes, "ISO-8859-1"), String.valueOf(1 + strLength) };
                    } catch (UnsupportedEncodingException e) {
                        logger.warn("", e);
                    }
                } else {
                    // StringType strType = (StringType) type.getSyntax();
                    // String tmp = ((SizeConstraint)
                    // strType.getConstraint()).getValues().get(0).toString();
                    strLength = oidIndexPart.length;
                    byte[] bytes = new byte[strLength];
                    for (int i = 0; i < strLength; i++) {
                        // Index without Length
                        bytes[i] = (byte) Integer.parseInt(oidIndexPart[i]);
                    }
                    try {
                        return new String[] { new String(bytes, "ISO-8859-1"), String.valueOf(strLength) };
                    } catch (UnsupportedEncodingException e) {
                        logger.warn("", e);
                    }
                }
            } else if (type.getSyntax() instanceof ObjectIdentifierType) {
                // oid格式的是否包括字符长度, 同样可以使用IMPLIED方式进行判断
                int strLength;
                if (!snmpIndex.isImplied()) {
                    strLength = Integer.parseInt(oidIndexPart[0]);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < strLength; i++) {
                        // Index with Length
                        if (i != strLength - 1) {
                            sb.append(oidIndexPart[i + 1]).append(".");
                        } else {
                            sb.append(oidIndexPart[i + 1]);
                        }
                    }
                    return new String[] { sb.toString(), String.valueOf(1 + strLength) };
                } else {
                    strLength = oidIndexPart.length;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < strLength; i++) {
                        // Index without Length
                        if (i != strLength - 1) {
                            sb.append(oidIndexPart[i]).append(".");
                        } else {
                            sb.append(oidIndexPart[i]);
                        }
                    }
                    return new String[] { sb.toString(), String.valueOf(strLength) };
                }
            } else if (type.getSyntax() instanceof ChoiceType) {
                ChoiceType chcType = (ChoiceType) type.getSyntax();
                for (int i = 0; i < chcType.getAllElements().length; i++) {
                    ElementType element = chcType.getAllElements()[i];
                    if (element.getType() instanceof StringType) {
                        try {
                            // TODO
                            // int size = Integer.parseInt(((SizeConstraint) ((StringType)
                            // element.getType()).getConstraint())
                            // .getValues().get(0).toString());
                            break;
                        } catch (Exception ex) {
                            continue;
                        }
                    }
                }
            } else {
                // TODO
                return null;
            }
        }
        // TODO
        return null;
    }

    /**
     * get version
     * 
     * @return version
     */
    public byte getVersion() {
        return (byte) target.getVersion();
    }

    /**
     * get current version string
     * 
     * @return version
     */
    public String getVersionName() {
        switch (target.getVersion()) {
        case SnmpConstants.version1:
            return "v1";
        case SnmpConstants.version2c:
            return "v2c";
        case SnmpConstants.version3:
            return "v3";
        default:
            return "unknown version";
        }
    }

    public String hexString2GBK(String hex) {
        try {
            if (!pattern.matcher(hex).matches()) {
                return hex;
            }
            byte[] value = hex.getBytes();
            byte[] tmp = new byte[value.length - 1];
            System.arraycopy(value, 0, tmp, 0, tmp.length);
            return new String(tmp, "GBK");
        } catch (UnsupportedEncodingException ex) {
            logger.warn(ex.toString());
        }
        return hex;
    }

    /**
     * 是否是中文或者可打印的字符串,强烈建议调用之前value调用过trim方法。因为此方法对char（0）不免疫
     * 
     * @param value
     * @return
     */
    public boolean isChineseCode(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            // 可打印的英文字符
            if (!((Character.isISOControl(c) || ((c & 0xFF) >= 0x80)) && (!Character.isWhitespace(c)))) {
                continue;
            }
            // 中文的范围是：\u4E00-\u9FA5

            if (!((c >= '\u4E00') && (c <= '\u9FA5'))) {
                return false;
            }
        }
        return true;

    }

    /**
     * 判断给定的OID是否是table的Index节点
     * 
     * @param oid
     *            OID
     * @return 如果给定OID是Index节点则返回true，反之返回false
     */
    public boolean isIndex(String oid) {
        if (oid == null) {
            return false;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && (symbol.isTable() || symbol.isTableRow() || symbol.isTableColumn())) {
                break;
            }
        }
        // if (logger.isDebugEnabled()) {
        // logger.debug("isIndex.oid:" + symbol);
        // }
        if (symbol == null || symbol.getParent() == null || symbol.getParent().getType() == null
                || !symbol.getParent().isTableRow()) {
            return false;
        }
        if (symbol.getParent().getType() instanceof SnmpObjectType) {
            SnmpObjectType type = (SnmpObjectType) symbol.getParent().getType();
            // if (logger.isDebugEnabled()) {
            // logger.debug("isIndex.index:" + type.getIndex());
            // }
            for (int i = 0; type.getIndex() != null && !type.getIndex().isEmpty() && i < type.getIndex().size(); i++) {
                SnmpIndex index = (SnmpIndex) type.getIndex().get(i);
                if (index.toString().equals(oid)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 判断给定的OID是否是叶子节点
     * 
     * @param oid
     *            OID
     * @return 如果给定的是叶子节点则返回true，反之返回false
     */
    public boolean isLeaf(String oid) {
        if (oid == null) {
            return false;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isScalar()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断给定的OID是否是table节点
     * 
     * @param oid
     *            OID
     * @return 如果OID是table节点则返回true，反之返回false
     */
    public boolean isTable(String oid) {
        if (oid == null) {
            return false;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isTable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断给定的OID是否是一个table的列
     * 
     * @param oid
     *            table的列OID
     * @return 如果给定的OID是table中的列OID则返回true，反之返回false
     */
    public boolean isTableColumn(String oid) {
        if (oid == null) {
            return false;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isTableColumn()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断给定的OID是否是table entry节点
     * 
     * @param oid
     *            OID
     * @return 如果给定OID是Entry节点则返回true，反之返回false
     */
    public boolean isTableEntry(String oid) {
        if (oid == null) {
            return false;
        }
        MibValueSymbol symbol = null;
        for (Mib mib : mibs) {
            symbol = mib.getSymbolByOid(toMibOid(oid));
            if (symbol != null && symbol.isTableRow()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 加载单个mib库文件。
     * 
     * @param name
     *            mib库文件名
     * @return Mib实例
     */
    public Mib loadMib(String name) {
        return MibManager.getInstance().loadMib(name);
    }

    /**
     * 释放资源
     */
    public void releaseResources() {
        try {
            snmp.close();
        } catch (IOException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(ex.getMessage());
            }
        }
    }

    /**
     * SnmpSet操作
     * 
     * @param oid
     *            oid字符串，会自动转化为协议OID
     * @param value
     *            设置值，会自动根据数据类型封装对应的snmp包绑定
     * @return 设置后的值
     * @throws SnmpException
     */
    public String set(String oid, String value) throws SnmpException {
        try {
            if (writeCommunity == null || writeCommunity.isEmpty()) {
                throw new IllegalCommunityException("write community not set error");
            }
            oid = verifyOid(oid);
            if (oid == null || value == null) {
                return null;
            }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(createVariableBinding(oid, value));
            setCommunity(writeCommunity);
            pdu = snmp.set(pdu, target).getResponse();
            /*
             * if (pdu == null) { throw new
             * IllegalCommunityException(target.getAddress().toString(), writeCommunity); } // if
             * (logger.isDebugEnabled()) { // logger.debug("SnmpUtil.set:" + pdu.get(0)); // } if
             * (pdu.getErrorStatus() != 0) { throw new SnmpSetException(pdu.getErrorStatusText() +
             * "(" + pdu.getErrorStatus() + ")" + pdu.getVariableBindings().get(pdu.getErrorIndex()
             * - 1)); }
             */
            checkSetResponsePdu(pdu, oid);
            return variable2String(pdu.get(0));
        } catch (IllegalCommunityException ice) {
            throw ice;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.set:" + oid, ex);
            }
            throw new SnmpException("Set[" + target.getAddress() + "][OID=" + oid + "]Error", ex);
        } finally {
            setCommunity(community);
        }
    }

    /**
     * SnmpSet操作
     * 
     * @param binding
     *            变量绑定
     * @return 设置值的返回值
     * @throws SnmpException
     */
    public String set(VariableBinding binding) throws SnmpException {
        try {
            setCommunity(writeCommunity);
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(binding);
            pdu = snmp.set(pdu, target).getResponse();
            if (pdu == null) {
                throw new IllegalCommunityException(target.getAddress().toString(), writeCommunity);
            }
            // if (logger.isDebugEnabled()) {
            // logger.debug("SnmpUtil.set:" + pdu.get(0));
            // }
            if (pdu.getErrorStatus() != 0) {
                throw new SnmpSetException(
                        target.getAddress().toString().split("/")[0], pdu.getErrorStatusText() + "("
                                + pdu.getErrorStatus() + ")" + pdu.getVariableBindings().get(pdu.getErrorIndex() - 1),
                        pdu.getErrorIndex());
            }
            return variable2String(pdu.get(0));
        } catch (SnmpException ex) {
            throw ex;
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\n" + "SnmpUtil.set:" + binding, e);
            }
            throw new SnmpException("Set[" + target.getAddress() + "][" + binding + "]Error", e);
        } finally {
            setCommunity(community);
        }
    }

    /**
     * 设置多个值，注意oid和value数组大小必须一致
     * 
     * @param oid
     *            oid数组
     * @param value
     *            值数据
     * @return 设置结果返回绑定
     * @throws SnmpException
     * @see set(Map<String,String>)
     */
    public Vector<? extends VariableBinding> set(String[] oid, String[] value) throws SnmpException {
        if (oid.length == 0 || oid.length != value.length) {
            return null;
        }
        Map<String, String> data = new HashMap<String, String>();
        for (int i = 0; i < oid.length; i++) {
            data.put(oid[i], value[i]);
        }
        return set(data);
    }

    /**
     * 多个值设置，一次通过snmp设置到agent上。
     * 
     * @param data
     *            oid和值对
     * @return 设置结果返回绑定
     * @throws SnmpException
     */
    public Vector<? extends VariableBinding> set(Map<String, String> data) throws SnmpException {
        if (data == null || data.isEmpty()) {
            return null;
        }
        try {
            if (writeCommunity == null || writeCommunity.isEmpty()) {
                throw new IllegalCommunityException("write community not set error");
            }
            PDU pdu = pduFactory.createPDU(target);
            for (String oid : data.keySet()) {
                String value = data.get(oid);
                pdu.add(createVariableBinding(oid, value));
            }
            setCommunity(writeCommunity);
            pdu = snmp.set(pdu, target).getResponse();
            if (pdu == null) {
                throw new IllegalCommunityException(target.getAddress().toString(), writeCommunity);
            }
            // if (logger.isDebugEnabled()) {
            // logger.debug("SnmpUtil.set:" + pdu.get(0));
            // }
            if (pdu.getErrorStatus() != 0) {
                throw new SnmpSetException(
                        target.getAddress().toString().split("/")[0], pdu.getErrorStatusText() + "("
                                + pdu.getErrorStatus() + ")" + pdu.getVariableBindings().get(pdu.getErrorIndex() - 1),
                        pdu.getErrorIndex());
            }
            return pdu.getVariableBindings();
        } catch (IllegalCommunityException ice) {
            throw ice;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\nSnmpUtil.set:" + data, ex);
            }
            throw new SnmpException("Set[" + target.getAddress() + "][" + data + "]Error", ex);
        } finally {
            setCommunity(community);
        }
    }

    /**
     * SnmpSet操作，设置多个值
     * 
     * @param bindings
     *            多个绑定集合
     * @throws SnmpException
     */
    public Vector<? extends VariableBinding> set(List<VariableBinding> bindings) throws SnmpException {
        try {
            if (writeCommunity == null || writeCommunity.isEmpty()) {
                throw new IllegalCommunityException("write community not set error");
            }
            setCommunity(writeCommunity);
            PDU pdu = pduFactory.createPDU(target);
            for (VariableBinding binding : bindings) {
                pdu.add(binding);
            }
            pdu = snmp.set(pdu, target).getResponse();
            if (pdu == null) {
                throw new IllegalCommunityException(target.getAddress().toString(), writeCommunity);
            }
            // if (logger.isDebugEnabled()) {
            // logger.debug("SnmpUtil.set:" + pdu.get(0));
            // }
            if (pdu.getErrorStatus() != 0) {
                throw new SnmpSetException(
                        target.getAddress().toString().split("/")[0], pdu.getErrorStatusText() + "("
                                + pdu.getErrorStatus() + ")" + pdu.getVariableBindings().get(pdu.getErrorIndex() - 1),
                        pdu.getErrorIndex());
            }
            return pdu.getVariableBindings();
        } catch (IllegalCommunityException ice) {
            throw ice;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(target.getAddress() + "\n" + "SnmpUtil.set:" + bindings, ex);
            }
            throw new SnmpException("Set[" + target.getAddress() + "][" + bindings + "]Error", ex);
        } finally {
            setCommunity(community);
        }
    }

    /**
     * Modify by Rod 原先的方法在V3环境下转换失败
     * 
     * set community
     * 
     * @param c
     */
    public void setCommunity(String c) {
        if (target instanceof CommunityTarget) {
            ((CommunityTarget) target).setCommunity(new OctetString(c));
        } else if (target instanceof UserTarget) {
            // 在SNMP V3的情况下预留
        }
    }

    /**
     * 设置重试次数
     * 
     * @param retry
     *            retries
     */
    public void setRetries(int retry) {
        target.setRetries(retry);
    }

    /**
     * set target host.
     * 
     * @param host
     */
    public void setTargetHost(String host) throws SnmpException {
        setTargetHost(host, snmpPort);
    }

    /**
     * set target host and port
     * 
     * @param host
     */
    public void setTargetHost(String host, int port) throws SnmpException {
        snmpPort = port;
        target.setAddress(GenericAddress.parse("udp:" + host + "/" + snmpPort));
    }

    /**
     * set target port.
     * 
     * @param host
     */
    public void setTargetPort(int port) throws SnmpException {
        setTargetHost(getTargetHost(), port);
    }

    /**
     * 设置超时时间
     * 
     * @param timeout
     *            超时时间
     */
    public void setTimeout(long timeout) {
        target.setTimeout(timeout);
    }

    /**
     * set Version
     * 
     * @param version
     *            version
     */
    public void setVersion(int version) {
        target.setVersion(version);
    }

    /**
     * 把一维数组转换成有一个\t隔开可打印的表字符串。
     * 
     * @param array
     *            一维数组
     * @return array转换的字符串
     */
    public String array2String(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuffer data = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            data.append(array[i]);
            data.append("\t");
        }
        return data.toString();
    }

    /**
     * 把table数组转换成有一个\t隔开可打印的表字符串。
     * 
     * @param table
     *            二维数组
     * @return table转换的字符串
     */
    public String table2String(String[][] table) {
        if (table == null || table.length == 0) {
            return "";
        }
        StringBuffer data = new StringBuffer();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                data.append(table[i][j]);
                data.append(";\t");
            }
            data.append("\n");
        }
        return data.toString();
    }

    private String toMibOid(String oid) {
        if (oid.startsWith(".")) {
            return oid.substring(1);
        } else {
            return oid;
        }
    }

    /**
     * mib值转换 支持中文字符串获取 DateAndTime解析
     * 
     * @param vb
     * @return
     */
    private String variable2String(VariableBinding vb) {
        if (vb == null) {
            return null;
        }
        Variable v = vb.getVariable();
        if (v instanceof OctetString && ((OctetString) v).isPrintable()) {
            String s = v.toString();
            if (s.length() == 6) {
                MibValueSymbol mvs = getMibValueSymbol(vb.getOid().toString());
                if (mvs != null && mvs.getType() instanceof SnmpObjectType) {
                    SnmpObjectType sot = (SnmpObjectType) mvs.getType();
                    if (sot.getSyntax().toString().indexOf("SIZE (6)") != -1) {
                        String result = "";
                        char[] charArray = s.toCharArray();
                        for (char c : charArray) {
                            result += bytes2hex(c) + ":";
                        }
                        return result.substring(0, result.length() - 1);
                    }
                }
            }
            return v.toString();
        }
        if (v instanceof OctetString) {
            try {
                byte[] data = ((OctetString) v).getValue();
                MibValueSymbol mvs = getMibValueSymbol(vb.getOid().toString());
                if (mvs != null && mvs.getType() instanceof SnmpObjectType) {
                    SnmpObjectType sot = (SnmpObjectType) mvs.getType();
                    // logger.debug(mvs.getName());
                    // DateAndTime
                    // 年月日时分秒毫秒-位数(2-1-1 1:1:1.1)
                    if (sot.getSyntax().toString().indexOf("SIZE (8 | 11)") != -1
                            && (data.length == 8 || data.length == 11)) {
                        int year = (data[0] << 8) + data[1];
                        if (data[1] < 0) {
                            year += 256;
                        }
                        // Modify by Rod 线程同步修改
                        // StringBuilder sb = new StringBuilder();
                        StringBuffer sb = new StringBuffer();
                        sb.append(year);
                        sb.append("-");
                        sb.append(data[2]);
                        sb.append("-");
                        sb.append(data[3]);
                        sb.append(" ");
                        sb.append(data[4]);
                        sb.append(":");
                        sb.append(data[5]);
                        sb.append(":");
                        sb.append(data[6]);
                        sb.append(".");
                        sb.append(data[7]);
                        // modify by victor@2013-5-30 由于SimpleDateFormat静态单实例线程不安全，改为多实例来保证线程安全。
                        // modify by Rod@2013-12-21 SimpleDateFormat属于单实例，不存在多实例情况，改为同步保证线程安全
                        synchronized (this) {
                            if (data.length == 11) {
                                sb.append(",").append(data[8]).append(data[9]).append(data[10]);
                                return String.valueOf(DateUtils.FULL_Z_FORMAT.parse(sb.toString()).getTime());
                            } else {
                                return String.valueOf(DateUtils.FULL_S_FORMAT.parse(sb.toString()).getTime());
                            }
                        }
                        // Modify by Rod
                        // PhysAddress 叶子节点为MAC地址的时候,此处适合数据类型为String
                        // 本类3407行适合数据类型定义为PhysAddress
                        // 两者都可以处理叶子节点为MAC地址的情况
                    } else if (data.length == 6 && (mvs.toString().indexOf("SIZE (6)") != -1
                            || mvs.toString().indexOf("PhysAddress") != -1)) {
                        return v.toString().toUpperCase();
                    }
                } else if (mvs == null && data.length == 6) {
                    return v.toString().toUpperCase();
                }
                // modify by victor修改为不支持中文
                // modify by Rod 支持中文，根据运行环境的编码方式，通过NM3000设置的中文可以正确回读
                // 但是如果通过其他工具设置的中文，需要保证工具所在操作系统的编码方式与NM3000运行环境的编码方式一致
                // modify by Rod 2013-3-27
                // 在刷新PON口LLID VLAN时处理位图出现问题 trim导致错误
                String os = new String(((OctetString) v).toByteArray());
                // String osInUTF8 = new String(((OctetString) v).toByteArray(), "UTF-8").trim();
                if (os != null && os.length() > 0 && isChineseCode(os)) {
                    return os;
                } /*
                   * else if(osInUTF8 != null && osInUTF8.length() > 0 && isChineseCode(osInUTF8)) {
                   * //return osInUTF8; } else { // 不属于中文 //return v.toString(); }
                   */
                return v.toString();
                // v.toString();
            } catch (Exception ex) {
                logger.debug(ex.getMessage(), ex);
                return v.toString();
            }
        } else if (v instanceof TimeTicks) {
            // @Modify By Rod
            return String.valueOf(((TimeTicks) v).getValue());
        } else {
            return v.toString();
        }
    }

    /**
     * 将字节数据转换为byte数组
     * 
     * @param vb
     * @return
     */
    private byte[] variable2Byte(VariableBinding vb) {
        if (vb == null) {
            return null;
        }
        Variable v = vb.getVariable();
        if (v instanceof OctetString) {
            return ((OctetString) v).getValue();
            // Byte[] B = new Byte[b.length];
            // for (int i = 0; i < b.length; i++) {
            // B[i] = new Byte(b[i]);
            // }
        }
        return null;
    }

    /**
     * 将字节数据转换为PhysAddress类型
     * 
     * @param vb
     * @return
     */
    private PhysAddress variable2PhysAddress(VariableBinding vb) {
        if (vb == null) {
            return null;
        }
        Variable v = vb.getVariable();
        if (v instanceof OctetString) {
            byte[] data = ((OctetString) v).getValue();
            MibValueSymbol mvs = getMibValueSymbol(vb.getOid().toString());
            if (mvs != null && mvs.getType() instanceof SnmpObjectType) {
                // PhysAddress
                if (data.length == 6
                        && (mvs.toString().indexOf("SIZE (6)") != -1 || mvs.toString().indexOf("PhysAddress") != -1)) {
                    return new PhysAddress(data);
                }
            }
        }
        return null;
    }

    /**
     * 将字节数据转换为16进制字符串
     * 
     * @param vb
     * @return
     */
    private String variable2Hex(VariableBinding vb) {
        if (vb == null) {
            return null;
        }
        Variable v = vb.getVariable();
        if (v instanceof OctetString) {
            String hex = ((OctetString) v).toHexString();
            return hex;
        }
        return null;
    }

    /**
     * 将字节数据转换为Date类型
     * 
     * @param vb
     * @return
     */
    private Date variable2Date(VariableBinding vb) {
        if (vb == null) {
            return null;
        }
        Variable v = vb.getVariable();
        if (v instanceof OctetString) {
            byte[] data = ((OctetString) v).getValue();
            MibValueSymbol mvs = getMibValueSymbol(vb.getOid().toString());
            if (mvs != null && mvs.getType() instanceof SnmpObjectType) {
                SnmpObjectType sot = (SnmpObjectType) mvs.getType();
                // logger.debug(mvs.getName());
                // DateAndTime
                // 年月日时分秒毫秒-位数(2-1-1 1:1:1.1)
                if (sot.getSyntax().toString().indexOf("SIZE (8 | 11)") != -1
                        && (data.length == 8 || data.length == 11)) {
                    int year = (data[0] << 8) + data[1];
                    if (data[1] < 0) {
                        year += 256;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(year);
                    sb.append("-");
                    sb.append(data[2]);
                    sb.append("-");
                    sb.append(data[3]);
                    sb.append(" ");
                    sb.append(data[4]);
                    sb.append(":");
                    sb.append(data[5]);
                    sb.append(":");
                    sb.append(data[6]);
                    sb.append(".");
                    sb.append(data[7]);
                    if (data.length == 11) {
                        sb.append(",").append(data[8]).append(data[9]).append(":").append(data[10]);
                    }
                    try {
                        return DateUtils.FULL_S_FORMAT.parse(sb.toString());
                    } catch (ParseException e) {
                        logger.warn("", e);
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * verify and change to oid
     * 
     * @param oid
     * @return
     */
    private String verifyOid(String oid) throws SnmpException {
        if (oid == null) {
            return null;
        }
        if (oid.indexOf('.') == -1) {
            oid = getOidByName(oid);
            if (oid == null) {
                return null;
            }
        }
        if (!oidPattern.matcher(oid).matches()) {
            oid = "." + oid;
        }
        return oid;
    }

    /**
     * 获取oid对应的节点MIB类型，主要用于在设置时需要用到的数据类型
     * 
     * 
     * @param oid
     *            OID值
     * @return OID所对应的类型 ， 数据类型包括： GenericAddress, Null, OID, Opaque, SMIAddress, TcpAddress,
     *         TimeTicks, TransportIpAddress, VariantVariable
     */
    private String getTypeByOid(String oid) {
        try {
            // MibValueSymbol symbol = getMibValueSymbol(oid);
            // if (symbol != null && symbol.getType() != null && symbol.getType() instanceof
            // SnmpObjectType) {
            // SnmpObjectType type = (SnmpObjectType) symbol.getType();
            // try {
            // logger.debug("{}>>>>>>>>>>>>>>>{}", oid,
            // type.getSyntax().getReferenceSymbol().getName());
            // types.add(type.getSyntax().getReferenceSymbol().getName());
            // } catch (Exception e) {
            // }
            // logger.debug(types.toString());
            // return null;
            // }
            PDU pdu = pduFactory.createPDU(target);
            pdu.add(new VariableBinding(new OID(oid)));
            pdu = snmp.get(pdu, target).getResponse();
            if (pdu == null) {
                throw new SnmpException("No Data");
            } else if (pdu.getErrorStatus() != 0) {
                throw new SnmpGetException(pdu.getErrorStatusText() + "(" + pdu.getErrorStatus() + ")"
                        + pdu.getVariableBindings().get(pdu.getErrorIndex() - 1));
            }
            VariableBinding vb = pdu.get(0);
            // if (logger.isDebugEnabled()) {
            // logger.debug("Set oid[{}]'s type is {}", oid, vb.getVariable().getClass().getName());
            // }
            return vb.getVariable().getClass().getName();
        } catch (SnmpException ex) {
            throw ex;
        } catch (IOException e) {
            logger.warn("", e);
            return OctetString.class.getName();
        }
    }

    /**
     * 根据OID的数据类型构造变量绑定，用于snmpSet
     * 
     * @param oid
     *            OID
     * @param value
     *            设置的值
     * @return SNMP4J所需要发送包的变量绑定
     */
    private VariableBinding createVariableBinding(String oid, String value) {
        String type = getTypeByOid(oid);
        if (type.equals(OctetString.class.getName())) {
            OctetString os = null;
            if (value.startsWith("0x")) {
                os = OctetString.fromHexString(value.substring(2));
            } else {
                os = new OctetString(value);
            }
            return new VariableBinding(new OID(oid), os);
        } else if (type.equals(Integer32.class.getName())) {
            return new VariableBinding(new OID(oid), new Integer32(Integer.parseInt(value)));
        } else if (type.equals(Counter32.class.getName())) {
            return new VariableBinding(new OID(oid), new Counter32(Long.parseLong(value)));
        } else if (type.equals(Counter64.class.getName())) {
            return new VariableBinding(new OID(oid), new Counter64(Long.parseLong(value)));
        } else if (type.equals(UnsignedInteger32.class.getName())) {
            return new VariableBinding(new OID(oid), new UnsignedInteger32(Integer.parseInt(value)));
        } else if (type.equals(Gauge32.class.getName())) {
            return new VariableBinding(new OID(oid), new Gauge32(Integer.parseInt(value)));
        } else if (type.equals(IpAddress.class.getName())) {
            return new VariableBinding(new OID(oid), new IpAddress(value));
        } else if (type.equals(UdpAddress.class.getName())) {
            return new VariableBinding(new OID(oid), new UdpAddress(value));
        } else if (type.equals(TimeTicks.class.getName())) {
            return new VariableBinding(new OID(oid), new TimeTicks(Long.parseLong(value)));
        } else {
            return new VariableBinding(new OID(oid), new OctetString(value));
        }
    }

    /**
     * 切换snmp版本
     */
    private void changeVersion() {
        if (target.getVersion() == SnmpConstants.version2c) {
            // Modify by Victor@20131119 解决EMS-6847问题，修改为不支持V1
            setVersion(SnmpConstants.version2c);
        } else if (target.getVersion() == SnmpConstants.version1) {
            setVersion(SnmpConstants.version2c);
        }
    }

    /**
     * 
     * snmp walk. walk the oid .1.3.6.1.2 and the oid .1.3.6.1.4
     */
    public void walk() {
        if (logger.isDebugEnabled()) {
            logger.debug("walk the device:" + getTargetHost());
        }
        walk("1.3.6.1.2", false);
        walk("1.3.6.1.4", true);
    }

    /**
     * 
     * snmp walk. walk the oid .1.3.6.1.2
     */
    public void walkPub() {
        if (logger.isDebugEnabled()) {
            logger.debug("walk the device:" + getTargetHost());
        }
        walk("1.3.6.1.2", false);
    }

    /**
     * 
     * snmp walk. walk the oid .1.3.6.1.4
     */
    public void walkPri() {
        if (logger.isDebugEnabled()) {
            logger.debug("walk the device:" + getTargetHost());
        }
        walk("1.3.6.1.4", false);
    }

    /**
     * snmp walk.
     * 
     * @param oid
     * @param append
     * @throws Exception
     */
    public void walk(String oid, boolean append) {
        FileOutputStream out = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Walk the device " + getTargetHost() + " by oid " + oid);
            }
            out = new FileOutputStream(new File("../logs/" + getTargetHost() + ".walk"), append);
            TreeUtils util = new TreeUtils(snmp, pduFactory);
            InternalTreeListener tl = new InternalTreeListener(out);
            util.getSubtree(target, new OID(oid), null, tl);
            while (!tl.isFinished) {
                Thread.sleep(100);
            }
            out.close();
            if (logger.isDebugEnabled()) {
                logger.debug("Walk the device " + getTargetHost() + " by oid " + oid + " finished)");
            }
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void listen() {
        try {
            snmp.listen();
        } catch (IOException e) {
            logger.debug("", e);
        }
    }

    public void close() {
        try {
            snmp.close();
        } catch (IOException e) {
            logger.debug("", e);
        }
    }

    /**
     * walk时的监听器
     * 
     * @author Victor
     * @created @2011-10-8-下午05:11:20
     * 
     */
    class InternalTreeListener implements TreeListener {
        boolean isFinished = false;

        public OutputStream out = null;

        public InternalTreeListener(OutputStream o) {
            out = o;
        }

        @Override
        public synchronized void finished(TreeEvent event) {
            VariableBinding[] vbs = event.getVariableBindings();
            if (vbs != null) {
                for (int i = 0; i < vbs.length; i++) {
                    try {
                        out.write(getVariable(vbs[i]).getBytes());
                        out.write(10);
                    } catch (IOException ex) {
                        logger.warn(ex.toString());
                    }
                }
            }
            isFinished = true;
            try {
                out.flush();
            } catch (IOException ex) {
                logger.warn(ex.toString());
            }
        }

        private String getVariable(VariableBinding vb) {
            StringBuffer data = new StringBuffer();
            data.append(vb.getOid());
            data.append(" = ");
            data.append(vb.getVariable().getSyntaxString());
            data.append(" : ");
            data.append(variable2String(vb));
            if (logger.isDebugEnabled()) {
                logger.debug(data.toString());
            }
            return data.toString();
        }

        @Override
        public boolean isFinished() {
            return isFinished;
        }

        @Override
        public synchronized boolean next(TreeEvent event) {
            VariableBinding[] vbs = event.getVariableBindings();
            for (int i = 0; i < vbs.length; i++) {
                try {
                    out.write(getVariable(vbs[i]).getBytes());
                    out.write(10);
                } catch (IOException ex) {
                    logger.warn(ex.toString());
                }
            }
            return true;
        }
    }

    public <T> T get(Class<T> clazz) {
        return get(clazz, null);
    }

    /**
     * 单个值获取，可以获取非表的叶子节点，或者表的一行数据，OID名称必须为实际的oid，采用get方法获取
     * 
     * @param <T>
     *            泛型定义
     * @param clazz
     *            泛型类名
     * @return 对象实体
     */
    public <T> T get(Class<T> clazz, List<String> excludeOids) {
        List<Field> fields = getFields(clazz);
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        try {
            T result = clazz.newInstance();
            PDU pdu = pduFactory.createPDU(target);
            for (Field field : fields) {
                // if (logger.isDebugEnabled()) {
                // logger.debug(field.toString());
                // }
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (prop != null) {
                    // if (logger.isDebugEnabled()) {
                    // logger.debug("SnmpProperty:" + prop);
                    // }
                    String oid = getOid(prop);
                    if (excludeOids != null && excludeOids.size() > 0
                            && (excludeOids.contains(oid) || getOid(prop).contains(excludeOids.get(0)))) {
                        continue;
                    }
                    pdu.add(new VariableBinding(new OID(oid)));
                }
            }
            if (pdu.size() == 0) {
                return result;
            }
            pdu.setMaxRepetitions(1);
            pdu.setErrorIndex(0);
            pdu.setErrorStatus(0);
            pdu = snmp.get(pdu, target).getResponse();
            checkResponsePdu(pdu, clazz);
            VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
            for (int i = 0; i < vbs.length; i++) {
                vbs[i] = pdu.getVariableBindings().get(i);
            }
            for (Field field : fields) {
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (prop != null) {
                    invoke(result, field.getType(), field, vbs, prop);
                }
            }
            return result;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 单个值获取，可以获取非表的叶子节点，或者表的一行数据，OID名称不必为实际的oid，采用getNext方法获取，取值下一个，叶子节点不能带.0
     * 
     * @param <T>
     *            泛型定义
     * @param clazz
     *            泛型类名
     * @return 对象实体
     */
    public <T> T getNext(Class<T> clazz) {
        List<Field> fields = getFields(clazz);
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        try {
            T result = clazz.newInstance();
            PDU pdu = pduFactory.createPDU(target);
            for (Field field : fields) {
                // if (logger.isDebugEnabled()) {
                // logger.debug(field.toString());
                // }
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (prop != null) {
                    // if (logger.isDebugEnabled()) {
                    // logger.debug("SnmpProperty:" + prop);
                    // }
                    String oid = getOid(prop);
                    pdu.add(new VariableBinding(new OID(oid)));
                }
            }
            pdu.setMaxRepetitions(1);
            pdu = snmp.getNext(pdu, target).getResponse();
            checkResponsePdu(pdu, clazz);
            VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
            for (int i = 0; i < vbs.length; i++) {
                vbs[i] = pdu.getVariableBindings().get(i);
            }
            for (Field field : fields) {
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (prop != null) {
                    invoke(result, field.getType(), field, vbs, prop);
                }
            }
            return result;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 获取表的特定行
     * 
     * @param <T>
     *            表的定义
     * @param line
     *            带index的表定义
     * @return 表的行数据
     */
    public <T> T getTableLine(T line) {
        return getTableLine(line, null);
    }

    /**
     * 获取表的特定行
     * 
     * @param <T>
     *            表的定义
     * @param line
     *            带index的表定义
     * @return 表的行数据
     */
    public <T> List<T> getTableLine(List<T> line) {
        return getTableLine(line, null);
    }

    /**
     * 获取表的特定行
     * 
     * @param <T>
     *            表的定义
     * @param line
     *            带index的表定义
     * @param excludeOids
     *            需要过滤的OID
     * @return 表的行数据
     */
    public <T> T getTableLine(T line, List<String> excludeOids) {
        List<Field> fields = getFields(line.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        String[] tables;
        TableProperty tableProperty = line.getClass().getAnnotation(TableProperty.class);
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        try {
            for (String tabName : tables) {
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder index = new StringBuilder();
                Method m;
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    String oid = getOid(prop);
                    if (excludeOids != null && excludeOids.size() > 0
                            && (excludeOids.contains(oid) || getOid(prop).contains(excludeOids.get(0)))) {
                        continue;
                    }
                    m = getGetMethod(line, field.getName());
                    if (field.getType().equals(PhysAddress.class)) {
                        // @Add by Rod 当Mac地址为索引的时候，必须按照PhysAddress字段类型
                        // 对象属性赋值不需要赋值为十进制带点的数据，直接下发MAC地址格式的六个字节
                        String macAddress = invoke(field, m, line).toString();
                        // Mac地址
                        if (macAddress.split(":").length != 6) {
                            SnmpException snmpException = new SnmpException("MacAddress Wrong " + field.getName());
                            throw snmpException;
                        }
                        for (String mac : macAddress.split(":")) {
                            index.append(".").append(Integer.parseInt(mac, 16));
                        }
                        continue;
                    }
                    if (invoke(field, m, line) == null) {
                        // index没有设置则不获取
                        index = null;
                        break;
                    }
                    index.append(".").append(invoke(field, m, line));
                }
                if (index == null || index.length() == 0) {
                    continue;
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    String oid = getOid(prop);
                    if (excludeOids != null && excludeOids.size() > 0
                            && (excludeOids.contains(oid) || getOid(prop).contains(excludeOids.get(0)))) {
                        continue;
                    }
                    pdu.add(new VariableBinding(new OID(oid + index.toString())));
                }
                pdu = snmp.get(pdu, target).getResponse();
                checkResponsePdu(pdu, line);
                VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int i = 0; i < vbs.length; i++) {
                    vbs[i] = pdu.getVariableBindings().get(i);
                    // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                    if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                        // throw new SnmpNoSuchInstanceException(vbs[i].getOid().toString());
                        if (logger.isDebugEnabled()) {
                            logger.debug("SnmpNoSuchInstance : {}", vbs[i].getOid());
                        }
                        vbs[i] = null;
                    } else if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                        // throw new SnmpNoSuchObjectException(vbs[i].getOid().toString());
                        if (logger.isDebugEnabled()) {
                            logger.debug("SnmpNoSuchObject : {}", vbs[i].getOid());
                        }
                        vbs[i] = null;
                    }
                }
                for (int i = 0; i < vbs.length; i++) {
                    if (vbs[i] != null) {
                        break;
                    }
                    if (i == vbs.length - 1) {
                        throw new SnmpNoSuchInstanceException(pdu.getVariableBindings().get(0).getOid().toString());
                    }
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    invoke(line, field.getType(), field, vbs, prop);
                }
            }
            return line;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 用于多个索引是字符串的getTable(example: pingCtlEntry)
     * 
     * @param line
     * @return
     */
    public <T> T getTableLineByMutiStringIndex(T line) {
        List<Field> fields = getFields(line.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        String[] tables;
        TableProperty tableProperty = line.getClass().getAnnotation(TableProperty.class);
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        try {
            for (String tabName : tables) {
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder index = new StringBuilder();
                Method m;
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    m = getGetMethod(line, field.getName());
                    if (field.getType().equals(String.class)) {
                        String indexString = invoke(field, m, line).toString();
                        index.append(".").append(indexString.length());
                        for (byte b : indexString.getBytes()) {
                            index.append(".").append(b);
                        }
                        continue;
                    }
                    if (invoke(field, m, line) == null) {
                        // index没有设置则不获取
                        index = null;
                        break;
                    }
                    index.append(".").append(invoke(field, m, line));
                }
                if (index == null || index.length() == 0) {
                    continue;
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    String oid = getOid(prop);
                    pdu.add(new VariableBinding(new OID(oid + index.toString())));
                }
                pdu = snmp.get(pdu, target).getResponse();
                checkResponsePdu(pdu, line);
                VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int i = 0; i < vbs.length; i++) {
                    vbs[i] = pdu.getVariableBindings().get(i);
                    // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                    if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                        throw new SnmpNoSuchInstanceException(vbs[i].getOid().toString());
                        /*
                         * if (logger.isDebugEnabled()) { logger.debug("SnmpNoSuchInstance : {}",
                         * vbs[i].getOid()); } vbs[i] = null;
                         */
                    } else if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                        throw new SnmpNoSuchObjectException(vbs[i].getOid().toString());
                        /*
                         * if (logger.isDebugEnabled()) { logger.debug("SnmpNoSuchObject : {}",
                         * vbs[i].getOid()); } vbs[i] = null;
                         */
                    }
                }
                for (int i = 0; i < vbs.length; i++) {
                    if (vbs[i] != null) {
                        break;
                    }
                    if (i == vbs.length - 1) {
                        throw new SnmpNoSuchInstanceException(pdu.getVariableBindings().get(0).getOid().toString());
                    }
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    invoke(line, field.getType(), field, vbs, prop);
                }
            }
            return line;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 
     * 
     * @param lines
     * @param excludeOids
     * @return
     */
    public <T> List<T> getTableLine(List<T> lines, List<String> excludeOids) {
        if (lines == null || lines.size() == 0) {
            return lines;
        }
        // First
        T line = lines.get(0);
        List<Field> fields = getFields(line.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        String[] tables;
        TableProperty tableProperty = line.getClass().getAnnotation(TableProperty.class);
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        try {
            for (String tabName : tables) {
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder[] indexs = new StringBuilder[lines.size()];
                // initialization indexs
                for (int i = 0; i < indexs.length; i++) {
                    indexs[i] = new StringBuilder();
                }
                Method m;
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    m = getGetMethod(line, field.getName());
                    for (int i = 0; i < lines.size(); i++) {
                        T object_line = lines.get(i);

                        if (field.getType().equals(PhysAddress.class)) {
                            // @Add by Rod 当Mac地址为索引的时候，必须按照PhysAddress字段类型
                            // 对象属性赋值不需要赋值为十进制带点的数据，直接下发MAC地址格式的六个字节
                            String macAddress = invoke(field, m, object_line).toString();
                            // Mac地址
                            if (macAddress.split(":").length != 6) {
                                SnmpException snmpException = new SnmpException("MacAddress Wrong " + field.getName());
                                throw snmpException;
                            }
                            for (String mac : macAddress.split(":")) {
                                indexs[i].append(".").append(Integer.parseInt(mac, 16));
                            }
                        } else {
                            if (invoke(field, m, object_line) == null) {
                                // index没有设置则不获取
                                SnmpException snmpException = new SnmpException(
                                        "Index Wrong Object " + i + " Field Name " + field.getName());
                                throw snmpException;
                            }
                            indexs[i].append(".").append(invoke(field, m, object_line));
                        }
                    }
                }
                for (StringBuilder index : indexs) {
                    for (Field field : fields) {
                        SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                        if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                            continue;
                        }
                        String oid = getOid(prop);
                        if (excludeOids != null && excludeOids.size() > 0
                                && (excludeOids.contains(oid) || getOid(prop).contains(excludeOids.get(0)))) {
                            continue;
                        }
                        pdu.add(new VariableBinding(new OID(oid + index.toString())));
                    }
                }
                int object_oid_size = pdu.size() / indexs.length;
                pdu = snmp.get(pdu, target).getResponse();
                checkResponsePdu(pdu, line);
                // VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int k = 0; k < lines.size(); k++) {
                    boolean exitNoSuchInstance = false;
                    VariableBinding[] vbs = new VariableBinding[object_oid_size];
                    for (int i = 0; i < object_oid_size; i++) {
                        vbs[i] = pdu.getVariableBindings().get(object_oid_size * k + i);
                        // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                        if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                            // throw new SnmpNoSuchInstanceException(vbs[i].getOid().toString());
                            // 捕获该异常防止某一行获取失败导致所有行都获取失败
                            try {
                                throw new SnmpNoSuchInstanceException();
                            } catch (SnmpNoSuchInstanceException e) {
                                logger.error("noSuchInstance : {}", vbs[i].getOid(), e);
                                exitNoSuchInstance = true;
                                break;
                            }
                        }
                        if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                            // throw new SnmpNoSuchObjectException(vbs[i].getOid().toString());
                            if (logger.isDebugEnabled()) {
                                logger.debug("SnmpNoSuchObject : {}", vbs[i].getOid());
                            }
                            vbs[i] = null;
                        }
                    }

                    // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                    if (exitNoSuchInstance) {
                        continue;
                    }

                    for (Field field : fields) {
                        SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                        if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                            continue;
                        }
                        invoke(lines.get(k), field.getType(), field, vbs, prop);
                    }
                }
            }
            return lines;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 对于多个index获取，指定前面几个，后面一个可以不指定，暂时仅支持最后一个index从1开始
     * 
     * 
     * @param <T>
     *            泛型定义
     * @param line
     *            获取对象实体，需要包括前面几个index值
     * @param firstIndex
     *            没有指定index的第一个值
     * @param length
     *            获取最大的长度，如果不限制则填入Integer.MAX_VALUE
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getTableLines(T line, int firstIndex, int length) {
        List<Field> fields = getFields(line.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        String[] tables;
        TableProperty tableProperty = line.getClass().getAnnotation(TableProperty.class);
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        List<T> lines = new ArrayList<T>();
        try {
            // TODO 暂时不支持多个表
            for (String tabName : tables) {
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder index = new StringBuilder();
                String prefix = null;
                Method m;
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    m = getGetMethod(line, field.getName());
                    if (invoke(field, m, line) == null) {
                        // index没有设置则不获取
                        // index = null;
                        prefix = index.toString();
                        index.append(".").append(firstIndex);
                        break;
                    } else {
                        if (field.getType().equals(PhysAddress.class)) {
                            // @Add by Rod 当Mac地址为索引的时候，必须按照PhysAddress字段类型
                            // 对象属性赋值不需要赋值为十进制带点的数据，直接下发MAC地址格式的六个字节
                            String macAddress = invoke(field, m, line).toString();
                            // Mac地址
                            if (macAddress.split(":").length != 6) {
                                SnmpException snmpException = new SnmpException("MacAddress Wrong " + field.getName());
                                throw snmpException;
                            }
                            for (String mac : macAddress.split(":")) {
                                index.append(".").append(Integer.parseInt(mac, 16));
                            }
                            continue;
                        }
                        index.append(".").append(invoke(field, m, line));
                    }
                }
                if (index == null || index.length() == 0) {
                    continue;
                }
                boolean prefixFlag = false;
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    String oid = getOid(prop);
                    if (!prefixFlag && prefix != null) {
                        prefix = oid + prefix;
                        prefixFlag = true;
                    }
                    pdu.add(new VariableBinding(new OID(oid + index.toString())));
                }
                boolean get = true;
                while (true) {
                    if (length <= 0) {
                        break;
                    }
                    pdu.setMaxRepetitions(1);
                    pdu.setNonRepeaters(0);
                    if (get) {
                        pdu = snmp.get(pdu, target).getResponse();
                        get = false;
                    } else {
                        pdu = snmp.getNext(pdu, target).getResponse();
                    }
                    checkResponsePdu(pdu, line);
                    if (!checkGetReponsePdu(pdu)) {
                        continue;
                    }
                    T l = ((Class<T>) line.getClass()).newInstance();
                    VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                    for (int i = 0; i < vbs.length; i++) {
                        vbs[i] = pdu.getVariableBindings().get(i);
                        // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                        if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                            /*
                             * logger.warn("SnmpNoSuchInstanceException : " + vbs[i].getOid());
                             * vbs[i] = null;
                             */
                            throw new SnmpNoSuchInstanceException(vbs[i].getOid().toString());
                        }
                        if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                            // throw new SnmpNoSuchObjectException(vbs[i].getOid().toString());
                            if (logger.isDebugEnabled()) {
                                logger.debug("SnmpNoSuchObject : {}", vbs[i].getOid());
                            }
                            vbs[i] = null;
                        }
                    }
                    if (vbs[0].getOid().toString().indexOf(prefix) == -1) {
                        break;
                    }
                    for (Field field : fields) {
                        SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                        if (prop == null || !prop.table().equals(tabName)) {
                            continue;
                        }
                        if (prop != null && prop.index() && prop.table().equals(tabName)) {
                            Map<String, String> indexs = getIndexValues(vbs);
                            if (field.getType().equals(Integer.class)) {
                                m = getSetMethod(l, field.getName(), Integer.class);
                                if (m != null) {
                                    m.invoke(l, Integer.parseInt(indexs.get(getOid(prop))));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, Integer.parseInt(indexs.get(getOid(prop))));
                                }
                            } else if (field.getType().equals(Long.class)) {
                                m = getSetMethod(l, field.getName(), Long.class);
                                if (m != null) {
                                    m.invoke(l, Long.parseLong(indexs.get(getOid(prop))));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, Long.parseLong(indexs.get(getOid(prop))));
                                }
                            } else if (field.getType().equals(byte[].class)) {
                                m = getSetMethod(l, field.getName(), byte[].class);
                                if (m != null) {
                                    m.invoke(l, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                                }
                            } else if (field.getType().equals(PhysAddress.class)) {
                                m = getSetMethod(l, field.getName(), PhysAddress.class);
                                if (m != null) {
                                    m.invoke(l, new PhysAddress(indexs.get(getOid(prop))));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, new PhysAddress(indexs.get(getOid(prop))));
                                }
                            } else {
                                m = getSetMethod(l, field.getName(), String.class);
                                if (m != null) {
                                    m.invoke(l, indexs.get(getOid(prop)));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, indexs.get(getOid(prop)));
                                }
                            }
                        } else {
                            invoke(l, field.getType(), field, vbs, prop);
                        }
                    }
                    lines.add(l);
                    length--;
                }
            }
            return lines;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 对于多个index获取，指定前面几个，后面一个可以不指定，暂时仅支持最后一个index从1开始
     * 设备现在存在bug，对不存在的index，getNext是错误的返回结果，需要待某个版本后解决掉这个bug，次方法才能可用
     * 
     * 
     * @param <T>
     *            泛型定义
     * @param line
     *            获取对象实体，需要包括前面几个index值
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getTableNextLine(T line) {
        List<Field> fields = getFields(line.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        String[] tables;
        TableProperty tableProperty = line.getClass().getAnnotation(TableProperty.class);
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        T l = null;
        try {
            // TODO 暂时不支持多个表
            for (String tabName : tables) {
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder index = new StringBuilder();
                Method m;
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    m = getGetMethod(line, field.getName());
                    if (invoke(field, m, line) == null) {
                        // // index没有设置则不获取
                        // index = null;
                        // break;
                    } else {
                        if (field.getType().equals(PhysAddress.class)) {
                            // @Add by Rod 当Mac地址为索引的时候，必须按照PhysAddress字段类型
                            // 对象属性赋值不需要赋值为十进制带点的数据，直接下发MAC地址格式的六个字节
                            String macAddress = invoke(field, m, line).toString();
                            // Mac地址
                            if (macAddress.split(":").length != 6) {
                                SnmpException snmpException = new SnmpException("MacAddress Wrong " + field.getName());
                                throw snmpException;
                            }
                            for (String mac : macAddress.split(":")) {
                                index.append(".").append(Integer.parseInt(mac, 16));
                            }
                            continue;
                        }
                        index.append(".").append(invoke(field, m, line));
                    }
                }
                // if (index == null || index.length() == 0) {
                // continue;
                // }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    String oid = getOid(prop);
                    pdu.add(new VariableBinding(new OID(oid + index.toString())));
                }
                pdu.setMaxRepetitions(1);
                pdu.setNonRepeaters(0);
                pdu = snmp.getNext(pdu, target).getResponse();
                checkResponsePdu(pdu, line);
                l = ((Class<T>) line.getClass()).newInstance();
                VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int i = 0; i < vbs.length; i++) {
                    vbs[i] = pdu.getVariableBindings().get(i);
                    // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                    if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                        /*
                         * logger.debug("SnmpNoSuchInstanceException : " + vbs[i].getOid()); vbs[i]
                         * = null;
                         */
                        throw new SnmpNoSuchInstanceException(vbs[i].getOid().toString());
                    }
                    if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                        // throw new SnmpNoSuchObjectException(vbs[i].getOid().toString());
                        if (logger.isDebugEnabled()) {
                            logger.debug("SnmpNoSuchObject : {}", vbs[i].getOid());
                        }
                        vbs[i] = null;
                    }
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.table().equals(tabName)) {
                        continue;
                    }
                    if (prop != null && prop.index() && prop.table().equals(tabName)) {
                        Map<String, String> indexs = getIndexValues(vbs);
                        if (field.getType().equals(Integer.class)) {
                            m = getSetMethod(l, field.getName(), Integer.class);
                            if (m != null) {
                                m.invoke(l, Integer.parseInt(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(l, Integer.parseInt(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(Long.class)) {
                            m = getSetMethod(l, field.getName(), Long.class);
                            if (m != null) {
                                m.invoke(l, Long.parseLong(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(l, Long.parseLong(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(byte[].class)) {
                            m = getSetMethod(l, field.getName(), byte[].class);
                            if (m != null) {
                                m.invoke(l, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                            } else {
                                field.setAccessible(true);
                                field.set(l, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                            }
                        } else if (field.getType().equals(PhysAddress.class)) {
                            m = getSetMethod(l, field.getName(), PhysAddress.class);
                            if (m != null) {
                                m.invoke(l, new PhysAddress(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(l, new PhysAddress(indexs.get(getOid(prop))));
                            }
                        } else {
                            m = getSetMethod(l, field.getName(), String.class);
                            if (m != null) {
                                m.invoke(l, indexs.get(getOid(prop)));
                            } else {
                                field.setAccessible(true);
                                field.set(l, indexs.get(getOid(prop)));
                            }
                        }
                    } else {
                        invoke(l, field.getType(), field, vbs, prop);
                    }
                }
            }
            return l;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 
     * 
     * @param line
     * @param startIndex
     * @param endIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getTableRangeLines(T line, long startIndex, long endIndex) {
        List<Field> fields = getFields(line.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        String[] tables;
        TableProperty tableProperty = line.getClass().getAnnotation(TableProperty.class);
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        List<T> lines = new ArrayList<T>();
        try {
            // TODO 暂时不支持多个表
            for (String tabName : tables) {
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder index = new StringBuilder();
                String prefix = null;
                Method m;
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    m = getGetMethod(line, field.getName());
                    if (invoke(field, m, line) == null) {
                        // index没有设置则不获取
                        // index = null;
                        prefix = index.toString();
                        index.append(".").append(startIndex);
                        break;
                    } else {
                        if (field.getType().equals(PhysAddress.class)) {
                            // @Add by Rod 当Mac地址为索引的时候，必须按照PhysAddress字段类型
                            // 对象属性赋值不需要赋值为十进制带点的数据，直接下发MAC地址格式的六个字节
                            String macAddress = invoke(field, m, line).toString();
                            // Mac地址
                            if (macAddress.split(":").length != 6) {
                                SnmpException snmpException = new SnmpException("MacAddress Wrong " + field.getName());
                                throw snmpException;
                            }
                            for (String mac : macAddress.split(":")) {
                                index.append(".").append(Integer.parseInt(mac, 16));
                            }
                            continue;
                        }
                        index.append(".").append(invoke(field, m, line));
                    }
                }
                if (index == null || index.length() == 0) {
                    continue;
                }
                boolean prefixFlag = false;
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || prop.index() || !prop.table().equals(tabName)) {
                        continue;
                    }
                    String oid = getOid(prop);
                    if (!prefixFlag && prefix != null) {
                        prefix = oid + prefix;
                        prefixFlag = true;
                    }
                    pdu.add(new VariableBinding(new OID(oid + index.toString())));
                }
                boolean get = true;
                while (true) {
                    pdu.setMaxRepetitions(1);
                    pdu.setNonRepeaters(0);
                    if (get) {
                        pdu = snmp.get(pdu, target).getResponse();
                        get = false;
                    } else {
                        pdu = snmp.getNext(pdu, target).getResponse();
                    }
                    checkResponsePdu(pdu, line);
                    if (!checkGetReponsePdu(pdu)) {
                        continue;
                    }
                    T l = ((Class<T>) line.getClass()).newInstance();
                    VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                    for (int i = 0; i < vbs.length; i++) {
                        vbs[i] = pdu.getVariableBindings().get(i);
                        // 如果存在一个值有noSuchInstance的情况 可认为该整行没值
                        if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                            // logger.debug("SnmpNoSuchInstanceException : " + vbs[i].getOid());
                            // vbs[i] = null;
                            throw new SnmpNoSuchInstanceException(vbs[i].getOid().toString());
                        }
                        if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                            // throw new SnmpNoSuchObjectException(vbs[i].getOid().toString());
                            if (logger.isDebugEnabled()) {
                                logger.debug("SnmpNoSuchObject : {}", vbs[i].getOid());
                            }
                            vbs[i] = null;
                        }
                    }
                    if (vbs[0].getOid().toString().indexOf(prefix + ".") == -1) {
                        break;
                    }
                    Integer pduIndex = Integer.parseInt(vbs[0].getOid().toString().substring(prefix.length() + 1));
                    if (pduIndex > endIndex) {
                        break;
                    }
                    for (Field field : fields) {
                        SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                        if (prop == null || !prop.table().equals(tabName)) {
                            continue;
                        }
                        if (prop != null && prop.index() && prop.table().equals(tabName)) {
                            Map<String, String> indexs = getIndexValues(vbs);
                            if (field.getType().equals(Integer.class)) {
                                m = getSetMethod(l, field.getName(), Integer.class);
                                if (m != null) {
                                    m.invoke(l, Integer.parseInt(indexs.get(getOid(prop))));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, Integer.parseInt(indexs.get(getOid(prop))));
                                }
                            } else if (field.getType().equals(Long.class)) {
                                m = getSetMethod(l, field.getName(), Long.class);
                                if (m != null) {
                                    m.invoke(l, Long.parseLong(indexs.get(getOid(prop))));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, Long.parseLong(indexs.get(getOid(prop))));
                                }
                            } else if (field.getType().equals(byte[].class)) {
                                m = getSetMethod(l, field.getName(), byte[].class);
                                if (m != null) {
                                    m.invoke(l, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                                }
                            } else if (field.getType().equals(PhysAddress.class)) {
                                m = getSetMethod(l, field.getName(), PhysAddress.class);
                                if (m != null) {
                                    m.invoke(l, new PhysAddress(indexs.get(getOid(prop))));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, new PhysAddress(indexs.get(getOid(prop))));
                                }
                            } else {
                                m = getSetMethod(l, field.getName(), String.class);
                                if (m != null) {
                                    m.invoke(l, indexs.get(getOid(prop)));
                                } else {
                                    field.setAccessible(true);
                                    field.set(l, indexs.get(getOid(prop)));
                                }
                            }
                        } else {
                            invoke(l, field.getType(), field, vbs, prop);
                        }
                    }
                    lines.add(l);
                }
            }
            return lines;
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
    }

    /**
     * 获取一个表的值，返回为List，定义clazz里面有多个表，只返回给定表名的列
     * 
     * @param <T>
     *            对象定义
     * @param clazz
     *            返回对象类名称
     * @param tableName
     *            需要采集的表定义
     * @return 对象list
     */
    public <T> List<T> getTable(Class<T> clazz, String tableName) {
        List<Field> fields = getFields(clazz);
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        List<T> result = new ArrayList<T>();
        try {
            PDU pdu = pduFactory.createPDU(target);
            // 第一个OID
            String oid = null;
            // 处理默认表
            for (java.util.Iterator<Field> itr = fields.iterator(); itr.hasNext();) {
                Field field = itr.next();
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (prop != null) {
                    // 不是默认表暂不处理
                    if (!prop.table().equals(tableName)) {
                        continue;
                    }
                    if (prop.index()) {
                    } else {
                        pdu.add(new VariableBinding(new OID(getOid(prop))));
                        if (oid == null) {
                            oid = getOid(prop);
                        }
                    }
                } else {
                    itr.remove();
                }
            }
            while (true) {
                pdu.setMaxRepetitions(1);
                pdu.setNonRepeaters(0);
                PDU resPdu = snmp.getBulk(pdu, target).getResponse();
                checkResponsePdu(resPdu, clazz);
                VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int i = 0; i < vbs.length; i++) {
                    vbs[i] = resPdu.getVariableBindings().get(i);
                }
                if (vbs[0].getOid().toString().indexOf(oid) == -1) {
                    break;
                }
                T t = clazz.newInstance();
                result.add(t);
                Method m;
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop != null && prop.index() && prop.table().equals(tableName)) {
                        Map<String, String> indexs = getIndexValues(vbs);
                        if (field.getType().equals(Integer.class)) {
                            m = getSetMethod(t, field.getName(), Integer.class);
                            if (m != null) {
                                m.invoke(t, Integer.parseInt(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(t, Integer.parseInt(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(Long.class)) {
                            m = getSetMethod(t, field.getName(), Long.class);
                            if (m != null) {
                                m.invoke(t, Long.parseLong(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(t, Long.parseLong(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(byte[].class)) {
                            m = getSetMethod(t, field.getName(), byte[].class);
                            if (m != null) {
                                m.invoke(t, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                            } else {
                                field.setAccessible(true);
                                field.set(t, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                            }
                        } else {
                            m = getSetMethod(t, field.getName(), String.class);
                            if (m != null) {
                                m.invoke(t, indexs.get(getOid(prop)));
                            } else {
                                field.setAccessible(true);
                                field.set(t, indexs.get(getOid(prop)));
                            }
                        }
                    } else {
                        invoke(t, field.getType(), field, vbs, prop);
                    }
                }
                pdu = resPdu;
            }
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
        return result;
    }

    /**
     * 获取一个表的值，返回为List
     * 
     * @param clazz
     * @param getIndex
     * @return
     */
    public <T> List<T> getTable(Class<T> clazz, boolean getIndex) {
        return getTable(clazz, getIndex, null);
    }

    /**
     * 获取一个表的值，返回为List
     * 
     * @param <T>
     *            泛型类型
     * @param clazz
     *            泛型类名
     * @param getIndex
     *            是否取index的值
     * 
     * @return 实体list
     */
    public <T> List<T> getTable(Class<T> clazz, boolean getIndex, List<String> excludeOids) {
        List<Field> fields = getFields(clazz);
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        List<T> result = new ArrayList<T>();
        try {
            PDU pdu = pduFactory.createPDU(target);
            // 第一个OID
            String oid = null;
            // 处理默认表
            String tableName = "default";
            for (java.util.Iterator<Field> itr = fields.iterator(); itr.hasNext();) {
                Field field = itr.next();
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (prop != null) {
                    // 不是默认表暂不处理
                    if (!prop.table().equals(tableName)) {
                        continue;
                    }
                    if (prop.index()) {
                        if (getIndex) {
                            // 处理INDEX
                        } else {
                            // 不处理INDEX
                            itr.remove();
                        }
                    } else {
                        if (excludeOids != null && excludeOids.size() > 0
                                && (excludeOids.contains(oid) || getOid(prop).contains(excludeOids.get(0)))) {
                            continue;
                        }
                        pdu.add(new VariableBinding(new OID(getOid(prop))));
                        if (oid == null) {
                            oid = getOid(prop);
                        }
                    }
                } else {
                    itr.remove();
                }
            }
            if (pdu.size() == 0) {
                return result;
            }
            while (true) {
                pdu.setMaxRepetitions(1);
                pdu.setNonRepeaters(0);
                PDU resPdu = snmp.getBulk(pdu, target).getResponse();
                checkResponsePdu(resPdu, clazz);
                VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int i = 0; i < vbs.length; i++) {
                    vbs[i] = resPdu.getVariableBindings().get(i);
                }
                if (vbs[0].getOid().toString().indexOf(oid) == -1) {
                    break;
                }
                T t = clazz.newInstance();
                result.add(t);
                Method m;
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (getIndex && prop != null && prop.index() && prop.table().equals(tableName)) {
                        Map<String, String> indexs = getIndexValues(vbs);
                        if (field.getType().equals(Integer.class)) {
                            m = getSetMethod(t, field.getName(), Integer.class);
                            if (m != null) {
                                m.invoke(t, Integer.parseInt(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(t, Integer.parseInt(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(Long.class)) {
                            m = getSetMethod(t, field.getName(), Long.class);
                            if (m != null) {
                                m.invoke(t, Long.parseLong(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(t, Long.parseLong(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(byte[].class)) {
                            m = getSetMethod(t, field.getName(), byte[].class);
                            if (m != null) {
                                m.invoke(t, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                            } else {
                                field.setAccessible(true);
                                field.set(t, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                            }
                        } else if (field.getType().equals(PhysAddress.class)) {
                            m = getSetMethod(t, field.getName(), PhysAddress.class);
                            if (m != null) {
                                m.invoke(t, new PhysAddress(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(t, new PhysAddress(indexs.get(getOid(prop))));
                            }
                        } else if (field.getType().equals(IpsAddress.class)) {
                            m = getSetMethod(t, field.getName(), IpsAddress.class);
                            if (m != null) {
                                m.invoke(t, new IpsAddress(indexs.get(getOid(prop))));
                            } else {
                                field.setAccessible(true);
                                field.set(t, new IpsAddress(indexs.get(getOid(prop))));
                            }
                        } else {
                            m = getSetMethod(t, field.getName(), String.class);
                            if (m != null) {
                                m.invoke(t, indexs.get(getOid(prop)));
                            } else {
                                field.setAccessible(true);
                                field.set(t, indexs.get(getOid(prop)));
                            }
                        }
                    } else {
                        invoke(t, field.getType(), field, vbs, prop);
                    }
                }
                pdu = resPdu;
            }
            TableProperty tableProperty = clazz.getAnnotation(TableProperty.class);
            if (tableProperty != null) {
                String[] tables = tableProperty.tables();
                for (String table : tables) {
                    if (table.equals("default")) {
                        continue;
                    }
                    // TODO 解决几个表获取时，后面表有问题时至少返回default表的值
                    try {
                        doTable(clazz, getIndex, fields, result, pduFactory.createPDU(target), table, excludeOids);
                    } catch (Exception e) {
                        logger.warn(table, e);
                    }
                }
            }
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpGetException(e);
        }
        return result;
    }

    private <T> void doTable(Class<T> clazz, boolean getIndex, List<Field> fields, List<T> result, PDU pdu,
            String tableName, List<String> excludeOids) throws Exception {
        // 第一个OID
        String oid = null;
        for (java.util.Iterator<Field> itr = fields.iterator(); itr.hasNext();) {
            Field field = itr.next();
            // if (logger.isDebugEnabled()) {
            // logger.debug(field.toString());
            // }
            SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
            if (prop != null) {
                // if (logger.isDebugEnabled()) {
                // logger.debug("SnmpProperty:" + prop);
                // }
                // 不是指定表不处理
                if (!prop.table().equals(tableName)) {
                    continue;
                }
                if (prop.index()) {
                    if (getIndex) {
                        // 处理INDEX
                    } else {
                        // 不处理INDEX
                        itr.remove();
                    }
                } else {
                    String tmpOid = getOid(prop);
                    if (excludeOids != null && excludeOids.size() > 0
                            && (excludeOids.contains(oid) || tmpOid.contains(excludeOids.get(0)))) {
                        continue;
                    }
                    pdu.add(new VariableBinding(new OID(tmpOid)));
                    if (oid == null) {
                        oid = tmpOid;
                    }
                }
            } else {
                itr.remove();
            }
        }
        if (pdu.getVariableBindings().size() == 0) {
            return;
        }
        int line = 0;
        if (pdu.size() == 0) {
            return;
        }
        while (true) {
            pdu.setMaxRepetitions(1);
            pdu.setNonRepeaters(0);
            // loggerBoard.info(pdu.toString());
            PDU resPdu = snmp.getBulk(pdu, target).getResponse();
            checkResponsePdu(resPdu, clazz);
            // loggerBoard.info(resPdu.toString());
            VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
            for (int i = 0; i < vbs.length; i++) {
                vbs[i] = resPdu.getVariableBindings().get(i);
            }
            if (vbs[0].getOid().toString().indexOf(oid) == -1) {
                break;
            }
            T t;
            if (line > result.size() - 1) {
                t = clazz.newInstance();
                result.add(t);
            } else {
                t = result.get(line);
            }
            // loggerBoard.info(t.toString());
            Method m;
            for (Field field : fields) {
                SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                if (getIndex && prop != null && prop.index() && prop.table().equals(tableName)) {
                    Map<String, String> indexs = getIndexValues(vbs);
                    if (field.getType().equals(Integer.class)) {
                        m = getSetMethod(t, field.getName(), Integer.class);
                        if (m != null) {
                            m.invoke(t, Integer.parseInt(indexs.get(getOid(prop))));
                        } else {
                            field.setAccessible(true);
                            field.set(t, Integer.parseInt(indexs.get(getOid(prop))));
                        }
                    } else if (field.getType().equals(Long.class)) {
                        m = getSetMethod(t, field.getName(), Long.class);
                        if (m != null) {
                            m.invoke(t, Long.parseLong(indexs.get(getOid(prop))));
                        } else {
                            field.setAccessible(true);
                            field.set(t, Long.parseLong(indexs.get(getOid(prop))));
                        }
                    } else if (field.getType().equals(byte[].class)) {
                        m = getSetMethod(t, field.getName(), byte[].class);
                        if (m != null) {
                            m.invoke(t, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                        } else {
                            field.setAccessible(true);
                            field.set(t, indexs.get(getOid(prop)).getBytes("ISO-8859-1"));
                        }
                    } else {
                        m = getSetMethod(t, field.getName(), String.class);
                        if (m != null) {
                            m.invoke(t, indexs.get(getOid(prop)));
                        } else {
                            field.setAccessible(true);
                            field.set(t, indexs.get(getOid(prop)));
                        }
                    }
                } else {
                    invoke(t, field.getType(), field, vbs, prop);
                }
            }
            // loggerBoard.info(t.toString());
            pdu = resPdu;
            line++;
        }
    }

    /**
     * 设置对象属性，对象必须在字段中添加SnmpProperty来定义
     * 
     * @param <T>
     *            对象泛型类型
     * @param obj
     *            对象实体，可设置的值不为null即设置到设备中
     * @return 返回值
     */
    public <T> T set(T obj) {
        if (obj == null) {
            throw new UnknownSnmpSetException();
        }
        List<Field> fields = getFields(obj.getClass());
        if (fields == null || fields.size() == 0) {
            throw new AnnotationException();
        }
        TableProperty tableProperty = obj.getClass().getAnnotation(TableProperty.class);
        String[] tables;
        if (tableProperty != null) {
            tables = tableProperty.tables();
        } else {
            tables = new String[] { "default" };
        }
        try {
            setCommunity(writeCommunity);
            for (String table : tables) {
                List<SnmpIndex> indexs = new ArrayList<SnmpIndex>();
                PDU pdu = pduFactory.createPDU(target);
                StringBuilder index = new StringBuilder();
                Method m;
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    m = getGetMethod(obj, field.getName());
                    if (prop == null || invoke(field, m, obj) == null) {
                        continue;
                    }
                    // 不能通过Index进行查找表的全部索引,因为可能存在外键索引的情况
                    if (prop.index()) {
                        continue;
                    }
                    indexs = getIndex(getOid(prop));
                    break;
                }
                // 处理index
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    m = getGetMethod(obj, field.getName());
                    SnmpIndex snmpIndex = null;
                    if (prop == null || invoke(field, m, obj) == null) {
                        continue;
                    }
                    if (!prop.index()) {
                        continue;
                    }
                    if (!prop.table().equals(table)) {
                        continue;
                    }
                    if (field.getType().equals(String.class)) {
                        String oid = invoke(field, m, obj).toString();
                        for (SnmpIndex tmpIndex : indexs) {
                            if (tmpIndex.getValue().toString().equals(getOid(prop))) {
                                snmpIndex = tmpIndex;
                                break;
                            }
                        }
                        if (!snmpIndex.isImplied()) {
                            // With Length
                            if (prop.type().equals("OctetString")) {
                                // Add by Rod 特殊处理eponManagementAddrName
                                if (snmpIndex.getValue().toString().equals("1.3.6.1.4.1.17409.2.2.11.3.1.1.1")) {
                                    byte[] values = oid.getBytes();
                                    for (int i = 0; i < values.length; i++) {
                                        index.append(".").append(values[i]);
                                    }
                                    continue;
                                }
                                // 字符串处理流程
                                byte[] values = oid.getBytes();
                                index.append(".").append(values.length);
                                for (int i = 0; i < values.length; i++) {
                                    index.append(".").append(values[i]);
                                }
                            } else if (prop.type().equals("OBJECT IDENTIFIER")) {
                                index.append(".").append(oid.split("\\.").length);
                                index.append(".").append(oid);
                            }
                        } else if (snmpIndex.isImplied()) {
                            if (prop.type().equals("OctetString")) {
                                byte[] values = oid.getBytes();
                                for (int i = 0; i < values.length; i++) {
                                    index.append(".").append(values[i]);
                                }
                            } else if (prop.type().equals("OBJECT IDENTIFIER")) {
                                index.append(".").append(oid);
                            }
                        }
                    } else if (field.getType().equals(IpsAddress.class)) {
                        String ipAddress = invoke(field, m, obj).toString();
                        // ip地址
                        if (ipAddress.split("\\.").length != 4) {
                            SnmpException snmpException = new SnmpException("IpAddress Wrong " + field.getName());
                            throw snmpException;
                        }
                        index.append(".").append(ipAddress);
                    } else if (field.getType().equals(PhysAddress.class)) {
                        // @Add by Rod 当Mac地址为索引的时候，必须按照PhysAddress字段类型
                        // 对象属性赋值不需要赋值为十进制带点的数据，直接下发MAC地址格式的六个字节
                        String macAddress = invoke(field, m, obj).toString();
                        // Mac地址
                        if (macAddress.split(":").length != 6) {
                            SnmpException snmpException = new SnmpException("MacAddress Wrong " + field.getName());
                            throw snmpException;
                        }
                        for (String mac : macAddress.split(":")) {
                            index.append(".").append(Integer.parseInt(mac, 16));
                        }
                    } else if (field.getType().equals(byte[].class)) {
                        byte[] bytes = (byte[]) invoke(field, m, obj);
                        for (SnmpIndex tmpIndex : indexs) {
                            if (tmpIndex.getValue().toString().equals(getOid(prop))) {
                                snmpIndex = tmpIndex;
                                break;
                            }
                        }
                        if (!snmpIndex.isImplied()) {
                            index.append(".").append(bytes.length);
                            for (byte b : bytes) {
                                // int bValue = (b < 0) ? 256 + b : b;
                                int bValue = b & 0xFF;
                                index.append(".").append(bValue);
                            }
                        } else if (snmpIndex.isImplied()) {
                            for (byte b : bytes) {
                                // int bValue = (b < 0) ? 256 + b : b;
                                int bValue = b & 0xFF;
                                index.append(".").append(bValue);
                            }
                        }
                    } else {
                        index.append(".").append(invoke(field, m, obj));
                    }
                }
                if (index.indexOf("null") != -1) {
                    throw new SnmpSetException("index is null");
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    // 这里的writable可以直接过滤Index,使用prop.index()更合适
                    if (prop == null || !prop.writable() || !prop.table().equals(table)) {
                        continue;
                    }
                    m = getGetMethod(obj, field.getName());
                    Object value = invoke(field, m, obj);
                    if (value == null) {
                        continue;
                    }
                    String type = prop.type();
                    String oid = getOid(prop) + index.toString();
                    if (type.equals("OctetString") && field.getType().equals(String.class)) {
                        OctetString os;
                        if (hexStringPattern.matcher(value.toString()).matches()) {
                            os = OctetString.fromHexString(value.toString());
                        } else if (hexStringPattern0x.matcher(value.toString()).matches()) {
                            os = OctetString.fromHexString(value.toString().substring(2));
                        } else {
                            os = new OctetString(value.toString());
                        }
                        pdu.add(new VariableBinding(new OID(oid), os));
                    }
                    // TODO
                    // if (type.equals("OctetString") && field.getType().equals(String.class)) {
                    // pdu.add(new VariableBinding(new OID(oid), new
                    // OctetString(value.toString())));
                    // }
                    else if (type.equals("OctetString") && field.getType().equals(byte[].class)) {
                        // 经过测试当byte数组中有大于127的byte，OctetString的转换不会存在问题
                        pdu.add(new VariableBinding(new OID(oid), OctetString.fromByteArray((byte[]) value)));
                    } else if (type.equals("Integer32")) {
                        pdu.add(new VariableBinding(new OID(oid), new Integer32(Integer.parseInt(value.toString()))));
                    } else if (type.equals("Gauge32")) {
                        pdu.add(new VariableBinding(new OID(oid), new Gauge32(Integer.parseInt(value.toString()))));
                    } else if (type.equals("UnsignedInteger32")) {
                        pdu.add(new VariableBinding(new OID(oid),
                                new UnsignedInteger32(Integer.parseInt(value.toString()))));
                    } else if (type.equals("Counter32")) {
                        pdu.add(new VariableBinding(new OID(oid), new Counter32(Long.parseLong(value.toString()))));
                    } else if (type.equals("Counter64")) {
                        pdu.add(new VariableBinding(new OID(oid), new Counter64(Long.parseLong(value.toString()))));
                    } else if (type.equals("IpAddress")) {
                        pdu.add(new VariableBinding(new OID(oid), new IpAddress(value.toString())));
                    } else if (type.equals("UdpAddress")) {
                        pdu.add(new VariableBinding(new OID(oid), new UdpAddress(value.toString())));
                    } else if (type.equals("TimeTicks")) {
                        pdu.add(new VariableBinding(new OID(oid), new TimeTicks(Long.parseLong(value.toString()))));
                    } else if (type.equals("OBJECT IDENTIFIER")) {
                        pdu.add(new VariableBinding(new OID(oid), new org.snmp4j.smi.OID(value.toString())));
                    } else if (type.equals("ByteIpAddress")) {
                        String ipValue = null;
                        String ip = value.toString();
                        if (ip.contains(".")) {
                            String[] ipArray = ip.split("\\.");
                            StringBuilder sb = new StringBuilder();
                            for (String tmp : ipArray) {
                                sb.append(bytes2hex(Integer.parseInt(tmp))).append(":");
                            }
                            ipValue = sb.substring(0, sb.length() - 1);
                            pdu.add(new VariableBinding(new OID(oid), OctetString.fromHexString(ipValue)));
                        }
                    }

                }
                if (pdu.getVariableBindings().size() == 0) {
                    continue;
                }
                pdu = snmp.set(pdu, target).getResponse();

                // if (pdu == null) {
                // throw new SnmpException("no response:" + obj);
                // } else if (pdu.getErrorStatus() != 0) {
                // throw new SnmpSetException(pdu.getErrorStatusText() + "(" + pdu.getErrorStatus()
                // + ")"
                // + pdu.getVariableBindings().get(pdu.getErrorIndex() - 1));
                // throw new SnmpSetException(pdu.getErrorStatusText(), pdu.getErrorStatus());
                // }

                checkSetResponsePdu(pdu, obj);
                VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
                for (int i = 0; i < vbs.length; i++) {
                    vbs[i] = pdu.getVariableBindings().get(i);
                }
                for (Field field : fields) {
                    SnmpProperty prop = field.getAnnotation(SnmpProperty.class);
                    if (prop == null || !prop.writable() || !prop.table().equals(table)) {
                        continue;
                    }
                    invoke(obj, field.getType(), field, vbs, prop);
                }
            }
        } catch (SnmpException ex) {
            throw ex;
        } catch (Exception e) {
            logger.warn("", e);
            throw new SnmpSetException(e);
        } finally {
            setCommunity(community);
        }
        return obj;
    }

    /**
     * 
     * @param clazz
     * @return
     */
    private List<Field> getFields(Class<?> clazz) {
        if (clazz.getSimpleName().equals("GponOnuAttribute") || clazz.getSimpleName().equals("GponUniAttribute")) {
            return getFields(clazz, false);
        } else {
            return getFields(clazz, true);
        }
    }

    /**
     * 
     * @param clazz
     * @return
     */
    private List<Field> getFields(Class<?> clazz, boolean superExclude) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> parent = clazz;
        while (parent != null && !parent.equals(Object.class)) {
            // if (logger.isDebugEnabled()) {
            // logger.debug(parent.toString());
            // }
            Field[] fs = parent.getDeclaredFields();
            for (int i = 0; fs != null && i < fs.length; i++) {
                // if (logger.isDebugEnabled()) {
                // logger.debug(fs[i].getName());
                // }
                if (fs[i].getAnnotation(SnmpProperty.class) != null) {
                    fields.add(fs[i]);
                }
            }
            if (!superExclude) {
                break;
            }
            parent = parent.getSuperclass();
        }
        return fields;
    }

    /**
     * 
     * @param obj
     * @param type
     * @param field
     * @param cols
     * @param oid
     * @throws Exception
     */
    private void invoke(Object obj, Class<?> type, Field field, VariableBinding[] cols, SnmpProperty prop) {
        OID oid = new OID(getOid(prop));
        VariableBinding vb = getVariableBinding(cols, oid);
        if (vb == null || vb.getVariable() == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("method:{} error", field.getName());
            }
            return;
        }
        // if (logger.isDebugEnabled()) {
        // logger.debug("set field " + field);
        // }
        Method m = null;
        try {
            if (type.equals(String.class)) {
                m = getSetMethod(obj, field.getName(), String.class);
                String value;
                if (prop.isHex()) {
                    value = variable2Hex(vb);
                } else {
                    value = variable2String(vb);
                }
                if (m != null) {
                    m.invoke(obj, value);
                } else {
                    field.setAccessible(true);
                    field.set(obj, value);
                }
            } else if (type.equals(byte[].class)) {
                m = getSetMethod(obj, field.getName(), byte[].class);
                if (m != null) {
                    m.invoke(obj, variable2Byte(vb));
                } else {
                    field.setAccessible(true);
                    field.set(obj, variable2Byte(vb));
                }
            } else if (type.equals(Integer.class)) {
                m = getSetMethod(obj, field.getName(), Integer.class);
                if (m != null) {
                    m.invoke(obj, vb.getVariable().toInt());
                } else {
                    field.setAccessible(true);
                    field.set(obj, vb.getVariable().toInt());
                }
            } else if (type.equals(Long.class)) {
                m = getSetMethod(obj, field.getName(), Long.class);
                if (m != null) {
                    m.invoke(obj, vb.getVariable().toLong());
                } else {
                    field.setAccessible(true);
                    field.set(obj, vb.getVariable().toLong());
                }
            } else if (type.equals(Byte.class)) {
                m = getSetMethod(obj, field.getName(), Byte.class);
                if (m != null) {
                    m.invoke(obj, Byte.parseByte(vb.getVariable().toString()));
                } else {
                    field.setAccessible(true);
                    field.set(obj, Byte.parseByte(vb.getVariable().toString()));
                }
            } else if (type.equals(Boolean.class)) {
                m = getSetMethod(obj, field.getName(), Boolean.class);
                if (m != null) {
                    m.invoke(obj, Boolean.parseBoolean(vb.getVariable().toString()));
                } else {
                    field.setAccessible(true);
                    field.set(obj, Byte.parseByte(vb.getVariable().toString()));
                }
            } else if (type.equals(Date.class)) {
                m = getSetMethod(obj, field.getName(), Date.class);
                if (m != null) {
                    m.invoke(obj, variable2Date(vb));
                } else {
                    field.setAccessible(true);
                    field.set(obj, variable2Date(vb));
                }
            } else if (type.equals(PhysAddress.class)) {
                m = getSetMethod(obj, field.getName(), PhysAddress.class);
                if (m != null) {
                    m.invoke(obj, variable2PhysAddress(vb));
                } else {
                    field.setAccessible(true);
                    field.set(obj, variable2PhysAddress(vb));
                }
            } else {
                m = getSetMethod(obj, field.getName(), String.class);
                String value;
                if (prop.isHex()) {
                    value = variable2Hex(vb);
                } else {
                    value = variable2String(vb);
                }
                if (m != null) {
                    m.invoke(obj, value);
                } else {
                    field.setAccessible(true);
                    field.set(obj, value);
                }
            }
        } catch (Exception e) {
            throw new SnmpException(field.getName(), e);
        }
    }

    /**
     * 获得字段的set方法，用于反射设置属性的value
     * 
     * @param o
     * @param field
     * @param type
     * @return
     */
    private Method getSetMethod(Object o, String field, Class<?> type) {
        try {
            return o.getClass().getMethod("set" + field.substring(0, 1).toUpperCase() + field.substring(1), type);
        } catch (SecurityException e) {
            logger.warn("", e);
        } catch (NoSuchMethodException e) {
            try {
                return o.getClass().getMethod("set" + field, type);
            } catch (SecurityException e1) {
                logger.warn("", e1);
            } catch (NoSuchMethodException e1) {
                logger.warn("", e1);
            }
        }
        return null;
    }

    /**
     * 获得字段的get方法，用于反射获得属性的value
     * 
     * @param o
     * @param field
     * @return
     */
    private Method getGetMethod(Object o, String field) {
        try {
            return o.getClass().getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
        } catch (SecurityException e) {
            logger.warn("", e);
        } catch (NoSuchMethodException e) {
            try {
                return o.getClass().getMethod("get" + field);
            } catch (SecurityException e1) {
                logger.warn("", e1);
            } catch (NoSuchMethodException e1) {
                logger.warn("", e1);
            }
        }
        return null;
    }

    /**
     * 
     * @param vbs
     * @param oid
     * @return
     */
    private VariableBinding getVariableBinding(VariableBinding[] vbs, OID oid) {
        if (vbs == null || vbs.length == 0) {
            return null;
        }
        for (VariableBinding vb : vbs) {
            try {
                if (vb.getOid().equals(oid) || vb.getOid().toString().startsWith(oid.toString())) {
                    return vb;
                }
            } catch (Exception e) {
                // logger.warn("",e);
            }
        }
        return null;
    }

    /**
     * 对返回的PDU包进行验证，暂时包括SNMP错误（没返回），以及采集的oid没有相应的值两种异常，后续可扩展 用在get方法中
     * 
     * @param <T>
     *            泛型
     * @param pdu
     * @param line
     */
    private <T> void checkResponsePdu(PDU pdu, T line) {
        if (pdu == null) {
            throw new SnmpNoResponseException("no response:" + line);
        } else if (pdu.getErrorStatus() != 0) {
            // Modify by Rod 存在getErrorStatus不为0并且getErrorIndex为0的情况,参考SnmpConstants
            // SNMP_ERROR_MESSAGES中的错误Authorization error
            if (pdu.getErrorIndex() != 0) {
                throw new SnmpGetException(pdu.getErrorStatusText() + "(" + pdu.getErrorStatus() + ")"
                        + pdu.getVariableBindings().get(pdu.getErrorIndex() - 1));
            } else {
                throw new SnmpGetException(pdu.getErrorStatusText() + "(" + pdu.getErrorStatus() + ")");
            }
        }
        pdu.setRequestID(null);
        if (target instanceof UserTarget) {
            // @Modify by Rod
            if (pdu.getType() == PDU.REPORT) {
                String result = SnmpV3ErrorInfo.getSnmpV3ErrorInfo(pdu.get(0).getOid().toString());
                if (result != null) {
                    // 这里的0代表错误码，对于这种异常没有意义，仅仅为了满足构造函数，将result作为errorText
                    throw new SnmpV3Exception(result, 0);
                }
            }
        }
    }

    /**
     * 对返回的PDU包进行验证，暂时包括SNMP错误（没返回），以及采集的oid没有相应的值两种异常，后续可扩展 用在set方法中
     * 
     * @param <T>
     *            泛型
     * @param pdu
     * @param line
     */
    private <T> void checkSetResponsePdu(PDU pdu, T line) {
        if (pdu == null) {
            throw new SnmpNoResponseException("no response:" + line);
        } else if (pdu.getErrorStatus() != 0) {
            throw new SnmpSetException(target.getAddress().toString().split("/")[0], pdu.getErrorStatusText(),
                    pdu.getErrorStatus());
        } else if (pdu.getType() == PDU.REPORT) {
            String result = SnmpV3ErrorInfo.getSnmpV3ErrorInfo(pdu.get(0).getOid().toString());
            if (result != null) {
                // 这里的0代表错误码，对于这种异常没有意义，仅仅为了满足构造函数，将result作为errorText
                throw new SnmpV3Exception(result, 0);
            }
        }
    }

    private boolean checkGetReponsePdu(PDU pdu) {
        VariableBinding[] vbs = new VariableBinding[pdu.getVariableBindings().size()];
        for (int i = 0; i < vbs.length; i++) {
            vbs[i] = pdu.getVariableBindings().get(i);
            if (vbs[i].getVariable().toString().equals("noSuchInstance") && vbs[i].getSyntax() == 129) {
                continue;
            }
            if (vbs[i].getVariable().toString().equals("noSuchObject") && vbs[i].getSyntax() == 128) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * 通过GET方式获取数据
     * 
     * @param field
     * @param method
     * @param T
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Object invoke(Field field, Method method, Object T)
            throws IllegalArgumentException, IllegalAccessException {
        try {
            Object object = method.invoke(T);
            return object;
        } catch (Exception e) {
            logger.info("invoke Snmp Domain GetMethod Wrong", e);
            field.setAccessible(true);
            return field.get(T);
        }
    }

    private static String bytes2hex(int bs) {
        StringBuilder hex = new StringBuilder();
        String s = "00" + Integer.toHexString(bs).toUpperCase();
        hex.append(s.substring(s.length() - 2));
        return hex.toString();
    }

    private String getOid(SnmpProperty prop) {
        String tmp = prop.oid();
        if (tmp.indexOf(",") != -1) {
            String[] oids = tmp.split(",");
            String defaultOid = null;
            Map<String, String> oidMap = new HashMap<>();
            for (String oid : oids) {
                int index = oid.indexOf(":");
                if (index != -1) {
                    String key = oid.substring(0, index);
                    String value = oid.substring(index + 1);
                    if ("default".equalsIgnoreCase(key)) {
                        defaultOid = value;
                    } else {
                        oidMap.put(key, value);
                    }
                } else {
                    defaultOid = oid;
                }
            }
            if (deviceVersion != null) {
                if (oidMap.containsKey(deviceVersion)) {
                    tmp = oidMap.get(deviceVersion);
                } else {
                    tmp = defaultOid;
                }
            } else {
                tmp = defaultOid;
            }
        } else {
            int index = tmp.indexOf(":");
            if (index != -1) {
                tmp = tmp.substring(index + 1);
            }
        }
        return tmp;
    }

}
