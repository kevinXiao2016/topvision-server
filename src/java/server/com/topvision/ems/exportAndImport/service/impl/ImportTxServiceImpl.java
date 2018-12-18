/***********************************************************************
 * $Id: ImportTxServiceImpl.java,v1.0 2015-7-22 上午9:44:16 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.exportAndImport.dao.ExportDao;
import com.topvision.ems.exportAndImport.dao.ImportDao;
import com.topvision.ems.exportAndImport.domain.EIConstant;
import com.topvision.ems.exportAndImport.service.ImportTxService;
import com.topvision.ems.exportAndImport.util.EIUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntityFolderRela;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.PortletItem;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.RoleFunctionRela;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserAuthFolder;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserRoleRela;

/**
 * @author fanzidong
 * @created @2015-7-22-上午9:44:16
 * 
 */
@Service("importTxService")
public class ImportTxServiceImpl implements ImportTxService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityService entityService;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;
    @Autowired
    private ImportDao importDao;
    @Autowired
    private ExportDao exportDao;
    @Autowired
    private EntityDao entityDao;

    private ResourceManager rm;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.exportAndImport.service.ImportTxService#txImportDatas(java.util.Map,
     * java.lang.String, java.lang.String, com.topvision.platform.domain.UserContext)
     */
    @Override
    public void txImportDatas(Map<String, String[][]> sheetMap, String jconnectionId) {
        try {
            // 导入Role数据
            importSheetData(sheetMap, jconnectionId, EIConstant.ROLE, Role.class);
            // 导入roleFunctionRela数据
            importSheetData(sheetMap, jconnectionId, EIConstant.ROLEFUNCTIONRELA, RoleFunctionRela.class);
            // 导入topofolder数据
            importSheetData(sheetMap, jconnectionId, EIConstant.TOPOFOLDER, TopoFolder.class);
            // 导入mapnode数据
            importSheetData(sheetMap, jconnectionId, EIConstant.MAPNODE, MapNode.class);
            // 导入user表数据
            importSheetData(sheetMap, jconnectionId, EIConstant.USER, User.class);
            // 导入userrolerela数据
            importSheetData(sheetMap, jconnectionId, EIConstant.USERROLERELA, UserRoleRela.class);
            // 导入userauthfolder数据
            importSheetData(sheetMap, jconnectionId, EIConstant.USERAUTHFOLDER, UserAuthFolder.class);
            // 导入userportletrela数据
            importSheetData(sheetMap, jconnectionId, EIConstant.USERPORTLETRELA, PortletItem.class);
            // 导入用户个性化
            importSheetData(sheetMap, jconnectionId, EIConstant.USERPREFERENCES, UserPreferences.class);
        } catch (Exception e) {
            // 发送导入错误消息
            sendStepMessage(jconnectionId, getString("ImportError"));
            logger.debug("import data error", e);
        }

    }

    @Override
    public void recoveryAuthority(Map<String, String[][]> sheetMap, String jconnectionId) {
        // 导入entity
        importEntity(sheetMap, jconnectionId);
        // TODO 设备导入完成后，导入设备别名
        importEntityAlias(sheetMap, jconnectionId);
        // 恢复v_topo和t_entity
        recoveryFolderView(jconnectionId);
        // 恢复设备跟地域的联系
        recoveryEntityFolders(sheetMap, jconnectionId);
        // 完成恢复操作，发送成功消息
        sendStepMessage(jconnectionId, getString("ImportDone"));
    }

    private void importSheetData(Map<String, String[][]> sheetMap, String jconnectionId, String sheetName,
            Class<?> clazz) {
        // 发送消息，表示开始导入
        sendStepMessage(jconnectionId, getString(sheetName + EIConstant.PREFIX));
        // 获取封装后的sheet对象
        List<Object> list = packageObject(sheetMap, sheetName, clazz);
        // 如果有数据，则进行导入
        if (list.size() > 0) {
            importDao.importSheetData(sheetName, list);
        }
        // 发送消息，表示完成导入
        sendStepMessage(jconnectionId, getString(sheetName + EIConstant.SUFFIX));
    }

    private void importEntityAlias(Map<String, String[][]> sheetMap, String jconnectionId) {
        // 发送消息，表示开始导入设备别名
        sendStepMessage(jconnectionId, getString(EIConstant.ENTITYALIAS + EIConstant.PREFIX));
        // 获取管理ip、mac对应的entityId
        List<Entity> entities = exportDao.getAllEntityAlias();
        Map<String, Long> relaMap = new HashMap<String, Long>();
        for (Entity entity : entities) {
            String key = generateKeyByIpAndMac(entity.getIp(), entity.getMac());
            if (key != null) {
                relaMap.put(key, entity.getEntityId());
            }
        }
        // 获取封装后的sheet对象
        List<Object> list = packageObject(sheetMap, EIConstant.ENTITYALIAS, Entity.class);
        //遍历封装后的对象，加入对应的entityId
        List<Entity> entityAlias = new ArrayList<Entity>();
        Entity e;
        for(Object o : list){
            e = (Entity) o;
            String key = generateKeyByIpAndMac(e.getIp(), e.getMac());
            if(relaMap.containsKey(key)){
                e.setEntityId(relaMap.get(key));
                entityAlias.add(e);
            }
        }
        //更改别名
        if(entityAlias.size()>0){
            importDao.importEntityAlias(entityAlias);
        }
        // 发送消息，表示完成导入设备别名
        sendStepMessage(jconnectionId, getString(EIConstant.ENTITYALIAS + EIConstant.SUFFIX));
    }

    private String getString(String key) {
        try {
            if (rm == null) {
                rm = ResourceManager.getResourceManager("com.topvision.ems.exportAndImport.resources");
            }
            return rm.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    private void recoveryFolderView(String jconnectionId) {
        // 发送消息，表示开始恢复v_topo和t_entity
        sendStepMessage(jconnectionId, getString(EIConstant.FOLDERVIEW + EIConstant.PREFIX));

        importDao.reStoreTopoRela();

        // 发送消息，表示完成恢复v_topo和t_entity
        sendStepMessage(jconnectionId, getString(EIConstant.FOLDERVIEW + EIConstant.SUFFIX));
    }

    private void importEntity(Map<String, String[][]> sheetMap, String jconnectionId) {
        // 发送消息，表示开始导入
        sendStepMessage(jconnectionId, getString(EIConstant.ENTITY + EIConstant.PREFIX));
        // 获取系统中已存在的设备
        List<Entity> entities = entityDao.getEntityExportInfo(new HashMap<String, Object>());
        List<String> existedIps = new ArrayList<String>();
        for (Entity entity : entities) {
            if (entity.getIp() != null && !"".equals(entity.getIp())) {
                existedIps.add(entity.getIp());
            }
        }
        // 获取封装后的Entity对象
        List<Object> list = packageObject(sheetMap, EIConstant.ENTITY, Entity.class);
        // 转成Entity对象
        Entity entity;
        for (Object o : list) {
            entity = (Entity) o;
            // 只添加有合法ip且不存在的设备
            if (entity.getIp() != null && IpUtils.matches(entity.getIp()) && !existedIps.contains(entity.getIp())) {
                try {
                    // 设置SNMP参数
                    SnmpParam sp = new SnmpParam();
                    sp.setVersion(entity.getVersion());
                    sp.setCommunity(entity.getCommunity());
                    sp.setWriteCommunity(entity.getWriteCommunity());
                    sp.setUsername(entity.getUsername());
                    sp.setAuthProtocol(entity.getAuthProtocol());
                    sp.setAuthPassword(entity.getAuthPassword());
                    sp.setPrivProtocol(entity.getPrivProtocol());
                    sp.setPassword(entity.getPrivPassword());
                    entity.setParam(sp);
                    // 添加设备
                    entityService.txCreateEntity(entity);
                    // 设备添加后发送消息
                    entityService.txCreateMessage(entity);
                } catch (Exception e) {
                    // 该设备导入失败，可能已存在，也可能因为某些原因未能导入
                    logger.debug("new entity" + entity.getName() + " error:", e.getMessage());
                }

            }
        }
        // 发送消息，表示完成导入
        sendStepMessage(jconnectionId, getString(EIConstant.ENTITY + EIConstant.SUFFIX));
    }

    /**
     * 恢复设备地域权限
     */
    private void recoveryEntityFolders(Map<String, String[][]> sheetMap, String jconnectionId) {
        // 发送消息，表示开始恢复设备地域权限
        sendStepMessage(jconnectionId, getString(EIConstant.ENTITYFOLDERRELA + EIConstant.PREFIX));
        // 获取系统中设备ip，mac与entityId的对应关系
        List<Entity> entities = exportDao.getAllEntityAlias();
        Map<String, Long> relaMap = new HashMap<String, Long>();
        for (Entity entity : entities) {
            String key = generateKeyByIpAndMac(entity.getIp(), entity.getMac());
            if (key != null) {
                relaMap.put(key, entity.getEntityId());
            }
        }
        // 获取封装后的sheet对象
        List<Object> list = packageObject(sheetMap, EIConstant.ENTITYFOLDERRELA, EntityFolderRela.class);
        // 转成entityFolderRela对象
        List<EntityFolderRela> relas = new ArrayList<EntityFolderRela>();
        // 组织地域包含设备map
        Map<Long, List<Long>> folderEntityIdMap = new HashMap<Long, List<Long>>();
        EntityFolderRela rela;
        String entityKey;
        for (Object o : list) {
            rela = (EntityFolderRela) o;
            entityKey = generateKeyByIpAndMac(rela.getIp(), rela.getMac());
            if (entityKey != null && relaMap.containsKey(entityKey)) {
                rela.setEntityId(relaMap.get(entityKey));
                if (!folderEntityIdMap.containsKey(rela.getFolderId())) {
                    folderEntityIdMap.put(rela.getFolderId(), new ArrayList<Long>());
                }
                List<Long> entityIds = folderEntityIdMap.get(rela.getFolderId());
                entityIds.add(rela.getEntityId());
                relas.add(rela);
            }
        }
        // 导入entityFolderRela数据
        importDao.importEntityFolderRela(relas);
        //获取folderId的列表
        List<Long> entityIds = new ArrayList<Long>();
        for(EntityFolderRela relation : relas){
            if(!entityIds.contains(relation.getEntityId())){
                entityIds.add(relation.getEntityId());
            }
        }
        // 导入各t_entity表数据
        entityDao.reOrganizedAuthority(entityIds);
        // 发送消息，表示完成恢复设备地域权限
        sendStepMessage(jconnectionId, getString(EIConstant.ENTITYFOLDERRELA + EIConstant.SUFFIX));
    }

    private void sendStepMessage(String jconnectionId, String data) {
        Message message = new Message("importEntireExcel");
        message.setJconnectID(jconnectionId);
        message.setData(data);
        messagePusher.sendMessage(message);
    }

    /**
     * 将sheet的内容封装成list对象
     * 
     * @param sheetName
     * @param curSheet
     * @param clazz
     * @return
     */
    private List<Object> packageObject(Map<String, String[][]> sheetMap, String sheetName, Class<?> clazz) {
        List<Object> list = new ArrayList<Object>();
        // 获取对应的sheet
        String[][] curSheetData = sheetMap.get(sheetName);
        // 找到sheet对应的列
        List<String> columns = EIUtil.getColumnList(clazz);

        // 遍历当前sheet数据，构造对象
        Object o;
        PropertyDescriptor pd;
        Method setMethod;
        Class<?> pdType;
        
        // add by fanzidong, 需要知道各key对应的index
        int keyIndex=0;
        Map<String, Integer> keyMap = new HashMap<String, Integer>();
        while(keyIndex < curSheetData[0].length) {
            keyMap.put(curSheetData[0][keyIndex], keyIndex);
            keyIndex++;
        }
        
        for (int i = 1, len = curSheetData.length; i < len; i++) {
            try {
                if (curSheetData[i] == null || "".equals(curSheetData[i])) {
                    continue;
                }
                // 实例化对象
                o = clazz.newInstance();
                // 遍历对应的列，进行赋值
                for (int j = 0, jLen = columns.size(); j < jLen; j++) {
                    try{
                        String key = columns.get(j);
                        String value = curSheetData[i][keyMap.get(key)];
                        if (value == null || "".equals(value)) {
                            continue;
                        }
                        pd = new PropertyDescriptor(key, clazz);
                        pdType = pd.getPropertyType();
                        setMethod = pd.getWriteMethod();

                        // 对不同的类型进行转换处理
                        if (pdType.equals(Long.class)) {
                            setMethod.invoke(o, Long.valueOf(value));
                        } else if (pdType.equals(long.class)) {
                            setMethod.invoke(o, Long.valueOf(value).longValue());
                        } else if (pdType.equals(Integer.class)) {
                            setMethod.invoke(o, Integer.valueOf(value));
                        } else if (pdType.equals(int.class)) {
                            setMethod.invoke(o, Integer.valueOf(value).intValue());
                        } else if (pdType.equals(Double.class)) {
                            setMethod.invoke(o, Double.valueOf(value));
                        } else if (pdType.equals(double.class)) {
                            setMethod.invoke(o, Double.valueOf(value).doubleValue());
                        } else if (pdType.equals(Float.class)) {
                            setMethod.invoke(o, Float.valueOf(value));
                        } else if (pdType.equals(float.class)) {
                            setMethod.invoke(o, Float.valueOf(value).floatValue());
                        } else if (pdType.equals(Boolean.class)) {
                            setMethod.invoke(o, Boolean.valueOf(value));
                        } else if (pdType.equals(boolean.class)) {
                            setMethod.invoke(o, Boolean.valueOf(value).booleanValue());
                        } else if (pdType.equals(Timestamp.class)) {
                            setMethod.invoke(o, Timestamp.valueOf(value));
                        } else {
                            setMethod.invoke(o, value);
                        }
                    }catch(Exception e){
                        //当前属性赋值错误，继续下个属性赋值
                        continue;
                    }
                }
                list.add(o);
            } catch (Exception e) {
                // 此行组装异常，忽略
                logger.debug("convert" + clazz.getName() + " error", e);
            }
        }
        return list;
    }

    private String generateKeyByIpAndMac(String ip, String mac) {
        if ((ip == null || "".equals(ip)) && (mac == null || "".equals(mac))) {
            // 如果ip和mac都为空，不考虑
            return null;
        }
        if (ip == null || "".equals(ip)) {
            // 如果没有ip，用mac作为key
            return mac;
        }
        if (mac == null || "".equals(mac)) {
            // 如果没有mac，用ip作为key
            return ip;
        }
        // 如果ip和mac都存在，共同作为key
        return ip + "#" + mac;
    }

}
