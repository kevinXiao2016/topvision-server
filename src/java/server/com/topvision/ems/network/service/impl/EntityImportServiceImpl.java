/***********************************************************************
 * $Id: EntityImportServiceImpl.java,v1.0 2013-10-30 下午3:11:24 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.EntityImportDao;
import com.topvision.ems.network.domain.EntityImport;
import com.topvision.ems.network.service.EntityImportService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.service.BaseService;

/**
 * @author loyal
 * @created @2013-10-30-下午3:11:24
 * 
 */
@Service("entityImportService")
public class EntityImportServiceImpl extends BaseService implements EntityImportService {
    @Resource(name = "entityImportDao")
    private EntityImportDao entityImportDao;
    @Resource(name = "entityDao")
    private EntityDao entityDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityImportService#importEntityName( java.util.List,
     * boolean)
     */
    public Map<String, Object> importEntityName(List<EntityImport> importEntityList, boolean overwrite) {
        List<EntityImport> failEntityList = new ArrayList<EntityImport>();
        List<EntityImport> updateEntityList = new ArrayList<EntityImport>();
        List<EntityImport> repeatEntityList = new ArrayList<EntityImport>();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, EntityImport> ipMap = new HashMap<String, EntityImport>();
        Map<String, EntityImport> macMap = new HashMap<String, EntityImport>();
        Map<String, EntityImport> ipMacMap = new HashMap<String, EntityImport>();
        List<String> macRecord = new ArrayList<String>();
        List<String> ipmacRecord = new ArrayList<String>();
        int index = 1;
        // 校验处理
        for (int i = 0; i < importEntityList.size(); i++) {
            // 校验格式
            EntityImport importEntity = validateEntityImport(importEntityList.get(i));
            if (importEntity.getMacStatus() < 1) {
                // 将IP转换成标准格式
                importEntity.setMac(transformMac(importEntity.getMac()));
            }
            // 重复性校验
            /*
             * if (ipList.contains(importEntity.getIp())) { importEntity.setIpStatus(2); } else { if
             * (importEntity.getIp() != null && !"".equals(importEntity.getIp())) {
             * ipList.add(importEntity.getIp()); } } if (macList.contains(importEntity.getMac())) {
             * importEntity.setMacStatus(2); } else { if (importEntity.getMac() != null &&
             * !"".equals(importEntity.getMac())) { macList.add(importEntity.getMac()); } }
             */

            if (importEntity.getIpStatus() > 0 || importEntity.getMacStatus() > 0 || importEntity.getNameStatus() > 0
                    || importEntity.getContactStatus() > 0 || importEntity.getLocationStatus() > 0
                    || importEntity.getNoteStatus() > 0) {
                importEntity.setId(index++);
                failEntityList.add(importEntity);
                continue;
            } else if (importEntity.getIp() != null && !"".equals(importEntity.getIp())
                    && importEntity.getMac() != null && !"".equals(importEntity.getMac())) {
                //找到重复的
                if (ipmacRecord.contains(importEntity.getIp()+importEntity.getMac())) {
                    String tempIpMac = importEntity.getIp()+importEntity.getMac();
                    importEntity.setMac(importEntity.getMac() + 1);
                    repeatEntityList.add(ipMacMap.get(tempIpMac));
                    importEntity.setMac(importEntity.getMac().substring(0, importEntity.getMac().length() - 1));
                    ipMacMap.put(importEntity.getIp() + importEntity.getMac(), importEntity);
                } else {
                    ipMacMap.put(importEntity.getIp() + importEntity.getMac(), importEntity);
                }
                ipmacRecord.add(importEntity.getIp() + importEntity.getMac());                              
                continue;
            } else if (importEntity.getIp() != null && !"".equals(importEntity.getIp())) {
                ipMap.put(importEntity.getIp(), importEntity);
            } else if (importEntity.getMac() != null && !"".equals(importEntity.getMac())) {
              //找到重复的
                if (macRecord.contains(importEntity.getMac())) {
                    String tempMac = importEntity.getMac();
                    importEntity.setMac(importEntity.getMac() + 1);
                    repeatEntityList.add(macMap.get(tempMac));
                    importEntity.setMac(importEntity.getMac().substring(0, importEntity.getMac().length() - 1));
                    macMap.put(importEntity.getMac(), importEntity);
                } else {
                    macMap.put(importEntity.getMac(), importEntity);
                }
                macRecord.add(importEntity.getMac());
            }
        }

        // 与数据库中数据比较
        List<EntityImport> entityList = entityImportDao.selectEntity();
        List<String> repMacList = new ArrayList<String>();
        List<String> repIpMacList = new ArrayList<String>();
        //存储重复的条目
        List<EntityImport> repEntityList = new ArrayList<EntityImport>();
        for (int i = 0; i < entityList.size(); i++) {
            String macTemp = entityList.get(i).getMac();
            String ipMacTemp=entityList.get(i).getIp()+entityList.get(i).getMac();
            if (repMacList.contains(macTemp)) {
                for (int j = 0; j < i; j++) {
                    String macRec = entityList.get(j).getMac();
                    if (macRec != null && macRec.equals(macTemp)) {
                        repEntityList.add(entityList.get(j));
                    }
                }
            }
            if (repIpMacList.contains(ipMacTemp)) {
                for (int j = 0; j < i; j++) {
                    String ipMacRec = entityList.get(j).getIp()+entityList.get(j).getMac();
                    if (ipMacRec != null && ipMacRec.equals(ipMacTemp)) {
                        repEntityList.add(entityList.get(j));
                    }
                }
            }
            repMacList.add(macTemp);
            repIpMacList.add(ipMacTemp);
        }
        //去掉表格导入的mac或ip+mac重复
        for (EntityImport entityIt : repeatEntityList) {
            importEntityList.remove(entityIt);
        }
        //去掉数据库查到的mac或ip+mac重复
        for (EntityImport eni : repEntityList) {
            entityList.remove(eni);
        }
        for (EntityImport entityImport : entityList) {
            if (ipMacMap.containsKey(entityImport.getIp() + entityImport.getMac()) && overwrite) {
                EntityImport entityImportTemp = ipMacMap.get(entityImport.getIp() + entityImport.getMac());
                importEntityList.remove(entityImportTemp);
                entityImport.setName(entityImportTemp.getName());
                entityImport.setId(index++);
                entityImport.setRowId(entityImportTemp.getRowId());
                entityImport.setSysName(null);
                entityImport.setContact(entityImportTemp.getContact());
                entityImport.setLocation(entityImportTemp.getLocation());
                entityImport.setNote(entityImportTemp.getNote());
                updateEntityList.add(entityImport);
                continue;
            } else if (!ipMap.containsKey(entityImport.getIp()) && !macMap.containsKey(entityImport.getMac())
                    && !ipMacMap.containsKey(entityImport.getIp() + entityImport.getMac())) {
                continue;
            } else if (ipMap.containsKey(entityImport.getIp()) && overwrite) {
                EntityImport entityImportTemp = ipMap.get(entityImport.getIp());
                importEntityList.remove(entityImportTemp);
                entityImport.setName(entityImportTemp.getName());
                entityImport.setId(index++);
                entityImport.setRowId(entityImportTemp.getRowId());
                entityImport.setSysName(null);
                entityImport.setContact(entityImportTemp.getContact());
                entityImport.setLocation(entityImportTemp.getLocation());
                entityImport.setNote(entityImportTemp.getNote());
                updateEntityList.add(entityImport);
                continue;
            } else if (macMap.containsKey(entityImport.getMac()) && overwrite) {
                EntityImport entityImportTemp = macMap.get(entityImport.getMac());
                importEntityList.remove(entityImportTemp);
                entityImport.setName(entityImportTemp.getName());
                entityImport.setId(index++);
                entityImport.setRowId(entityImportTemp.getRowId());
                entityImport.setSysName(null);
                entityImport.setContact(entityImportTemp.getContact());
                entityImport.setLocation(entityImportTemp.getLocation());
                entityImport.setNote(entityImportTemp.getNote());
                updateEntityList.add(entityImport);
                continue;
            } else if (!overwrite&& ipMacMap.containsKey(entityImport.getIp() + entityImport.getMac())){
                EntityImport entityImportTemp = ipMacMap.get(entityImport.getIp() + entityImport.getMac());
                importEntityList.remove(entityImportTemp);
                if(entityImport.getName() == null || "".equals(entityImport.getName()) || entityImport.getName()
                            .equals(entityImport.getSysName())){
                    entityImport.setName(entityImportTemp.getName());
                }else{
                    entityImport.setName(entityImport.getName());
                }
                entityImport.setId(index++);
                entityImport.setRowId(entityImportTemp.getRowId());
                entityImport.setSysName(null);
                entityImport.setContact(entityImportTemp.getContact());
                entityImport.setLocation(entityImportTemp.getLocation());
                entityImport.setNote(entityImportTemp.getNote());
                updateEntityList.add(entityImport);
                continue;
            } else if (!overwrite&& ipMap.containsKey(entityImport.getIp())){
                EntityImport entityImportTemp = ipMap.get(entityImport.getIp());
                importEntityList.remove(entityImportTemp);
                if((entityImport.getName() == null || "".equals(entityImport.getName()) || entityImport.getName()
                            .equals(entityImport.getSysName()))){
                    entityImport.setName(entityImportTemp.getName());
                }else{
                    entityImport.setName(entityImport.getName());
                }
                entityImport.setId(index++);
                entityImport.setRowId(entityImportTemp.getRowId());
                entityImport.setSysName(null);
                entityImport.setContact(entityImportTemp.getContact());
                entityImport.setLocation(entityImportTemp.getLocation());
                entityImport.setNote(entityImportTemp.getNote());
                updateEntityList.add(entityImport);
                continue;
            } else if (!overwrite&& macMap.containsKey(entityImport.getMac())){
                EntityImport entityImportTemp = macMap.get(entityImport.getMac());
                importEntityList.remove(entityImportTemp);
                if((entityImport.getName() == null || "".equals(entityImport.getName()) || entityImport.getName()
                            .equals(entityImport.getSysName()))){
                    entityImport.setName(entityImportTemp.getName());
                }else{
                    entityImport.setName(entityImport.getName());
                }
                entityImport.setId(index++);
                entityImport.setRowId(entityImportTemp.getRowId());
                entityImport.setSysName(null);
                entityImport.setContact(entityImportTemp.getContact());
                entityImport.setLocation(entityImportTemp.getLocation());
                entityImport.setNote(entityImportTemp.getNote());
                updateEntityList.add(entityImport);
            }
        }

        // 去掉失败的，剩下的为未匹配的
        importEntityList.removeAll(failEntityList);
        for (EntityImport e : importEntityList) {
            e.setId(index++);
        }
        // 更新数据库
        entityDao.batchUpdateEntity(updateEntityList);
        // 返回结果,只显示前1000条，否则前台页面数据太多，导致卡死
        if (failEntityList.size() > 1000) {
            result.put("errorList", failEntityList.subList(0, 1000));
        } else {
            result.put("errorList", failEntityList);
        }

        if (updateEntityList.size() > 1000) {
            result.put("successList", updateEntityList.subList(0, 1000));
        } else {
            result.put("successList", updateEntityList);
        }

        if (importEntityList.size() > 1000) {
            result.put("notFindList", importEntityList.subList(0, 1000));
        } else {
            result.put("notFindList", importEntityList);
        }
        result.put("successNum", updateEntityList.size());
        result.put("errorNum", failEntityList.size());
        result.put("notFindNum", importEntityList.size());
        return result;

    }

    /**
     * 校验导入entityimport对象
     * 
     * @param entityImport
     * @return
     */
    private EntityImport validateEntityImport(EntityImport entityImport) {
        // 判断字段是否为空
        if (entityImport.getName() == null || "".equals(entityImport.getName().trim())) {
            entityImport.setNameStatus(1);
        }

        // 校验字段格式
        if (entityImport.getName() != null && !"".equals(entityImport.getName().trim())
                && entityImport.getName().length() > 64) {
            entityImport.setNameStatus(1);
        }

        if (!checkAlias(entityImport.getName())) {
            entityImport.setNameStatus(1);
        }

        if (entityImport.getIp() != null && !"".equals(entityImport.getIp().trim())
                && !IpUtils.matches(entityImport.getIp().trim())) {
            entityImport.setIpStatus(1);
        }

        if (entityImport.getMac() != null && !"".equals(entityImport.getMac().trim())
                && !validateMac(entityImport.getMac().trim())) {
            entityImport.setMacStatus(1);
        }

        if (entityImport.getContact() != null && !"".equals(entityImport.getContact().trim())
                && entityImport.getContact().length() > 64) {
            entityImport.setContactStatus(1);
        }

        if (entityImport.getContact() != null && !"".equals(entityImport.getContact().trim())
                && (!checkContact(entityImport.getContact()))) {
            entityImport.setContactStatus(1);
        }

        if (entityImport.getLocation() != null && !"".equals(entityImport.getLocation().trim())
                && entityImport.getLocation().length() > 128) {
            entityImport.setLocationStatus(1);
        }

        if (entityImport.getLocation() != null && !"".equals(entityImport.getLocation().trim())
                && (!checkLocation(entityImport.getLocation()))) {
            entityImport.setLocationStatus(1);
        }

        if (entityImport.getNote() != null && !"".equals(entityImport.getNote().trim())
                && entityImport.getNote().length() > 128) {
            entityImport.setNoteStatus(1);
        }

        if (entityImport.getNote() != null && !"".equals(entityImport.getNote().trim())
                && (!checkNote(entityImport.getNote()))) {
            entityImport.setNoteStatus(1);
        }

        return entityImport;
    }

    /**
     * MAC地址校验
     * 
     * @param mac
     * @return
     */
    private boolean validateMac(String mac) {
        String normalizeMac = mac.replaceAll("[^a-fA-F0-9]", "");
        Pattern p = Pattern.compile("^([a-fA-F0-9]{12})$");
        Matcher m = p.matcher(normalizeMac);
        return m.find();
    }

    /**
     * mac地址转换
     * 
     * @param mac
     * @return
     */
    private String transformMac(String mac) {
        String normalizeMac = mac.replaceAll("[^a-fA-F0-9]", "").trim().toUpperCase();
        StringBuilder sb = new StringBuilder();
        if (normalizeMac.length() != 12) {
            return null;
        } else {
            char[] macChars = normalizeMac.toCharArray();
            return (sb.append(macChars[0]).append(macChars[1]).append(":").append(macChars[2]).append(macChars[3])
                    .append(":").append(macChars[4]).append(macChars[5]).append(":").append(macChars[6])
                    .append(macChars[7]).append(":").append(macChars[8]).append(macChars[9]).append(":")
                    .append(macChars[10]).append(macChars[11])).toString();
        }
    }

    // 别名检查
    public static boolean checkAlias(String alias) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\d\u4e00-\u9fa5-_\\[\\]()\\/\\.:]{1,63}$");
        Matcher matcher = pattern.matcher(alias);
        return matcher.matches();
    }

    // 联系人检查
    public static boolean checkContact(String contact) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\d\u4e00-\u9fa5-_\\[\\]()\\/\\.:\\s\\@]{1,63}$");
        Matcher matcher = pattern.matcher(contact);
        return matcher.matches();
    }

    // 位置检查
    public static boolean checkLocation(String location) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\d\u4e00-\u9fa5-_\\[\\]()\\/\\.:\\s\\@]{1,127}$");
        Matcher matcher = pattern.matcher(location);
        return matcher.matches();
    }

    // 备注检查
    public static boolean checkNote(String note) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\d\u4e00-\u9fa5-_\\[\\]()\\/\\.:\\s\\@]{1,127}$");
        Matcher matcher = pattern.matcher(note);
        return matcher.matches();
    }

}
