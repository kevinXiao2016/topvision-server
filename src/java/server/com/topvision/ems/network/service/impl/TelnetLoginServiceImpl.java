/***********************************************************************
 * $Id: TelnetLoginServiceImpl.java,v1.0 2014年7月16日 上午9:10:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.TelnetLoginDao;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author loyal
 * @created @2014年7月16日-上午9:10:01
 *
 */
@Service("telnetLoginService")
public class TelnetLoginServiceImpl extends BaseService implements TelnetLoginService {
    @Autowired
    private TelnetLoginDao telnetLoginDao;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public List<TelnetLogin> getTelnetLoginConfig(Map<String, Object> map) {
        return telnetLoginDao.getTelnetLoginConfig(map);
    }

    @Override
    public Long getTelnetLoginConfigCount(Map<String, Object> map) {
        return telnetLoginDao.getTelnetLoginConfigCount(map);
    }

    @Override
    public void deleteTelnetLogin(Long ip) {
        telnetLoginDao.deleteTelnetLogin(ip);
    }

    @Override
    public void addTelnetLogin(TelnetLogin telnetLogin) {
        telnetLoginDao.addTelnetLogin(telnetLogin);
    }

    @Override
    public void modifyTelnetLogin(TelnetLogin telnetLogin) {
        telnetLoginDao.modifyTelnetLogin(telnetLogin);
    }

    @Override
    public TelnetLogin getTelnetLoginConfigByIp(Long ip) {
        return telnetLoginDao.getTelnetLoginConfigByIp(ip);
    }

    @Override
    public TelnetLogin txGetTelnetLoginConfigByIp(Long ip) {
        return telnetLoginDao.getTelnetLoginConfigByIp(ip);
    }

    @Override
    public TelnetLogin getGlobalTelnetLogin() {
        TelnetLogin telnetLogin = new TelnetLogin();
        List<SystemPreferences> perferences = systemPreferencesDao.selectByModule("telnet");
        if (perferences != null && perferences.size() > 0) {
            for (SystemPreferences systemPreferences : perferences) {
                if ("telnetUserName".equals(systemPreferences.getName())) {
                    telnetLogin.setUserName(systemPreferences.getValue());
                } else if ("telnetPassword".equals(systemPreferences.getName())) {
                    telnetLogin.setPassword(systemPreferences.getValue());
                } else if ("telnetEnablePassword".equals(systemPreferences.getName())) {
                    telnetLogin.setEnablePassword(systemPreferences.getValue());
                } else if ("telnetIsAAA".equals(systemPreferences.getName())) {
                    telnetLogin.setIsAAA("1".equalsIgnoreCase(systemPreferences.getValue()));
                }

            }
        }
        return telnetLogin;
    }

    @Override
    public TelnetLogin txGetGlobalTelnetLogin() {
        TelnetLogin telnetLogin = new TelnetLogin();
        List<SystemPreferences> perferences = systemPreferencesDao.selectByModule("telnet");
        if (perferences != null && perferences.size() > 0) {
            for (SystemPreferences systemPreferences : perferences) {
                if ("telnetUserName".equals(systemPreferences.getName())) {
                    telnetLogin.setUserName(systemPreferences.getValue());
                } else if ("telnetPassword".equals(systemPreferences.getName())) {
                    telnetLogin.setPassword(systemPreferences.getValue());
                } else if ("telnetEnablePassword".equals(systemPreferences.getName())) {
                    telnetLogin.setEnablePassword(systemPreferences.getValue());
                } else if ("telnetIsAAA".equals(systemPreferences.getName())) {
                    telnetLogin.setIsAAA("1".equalsIgnoreCase(systemPreferences.getValue()));
                }

            }
        }
        return telnetLogin;
    }

    @Override
    public void modifyGlobalTelnetLogin(TelnetLogin telnetLogin) {
        List<SystemPreferences> perferences = new ArrayList<SystemPreferences>();
        SystemPreferences userName = new SystemPreferences();
        userName.setModule("telnet");
        userName.setName("telnetUserName");
        userName.setValue(telnetLogin.getUserName());
        perferences.add(userName);

        SystemPreferences password = new SystemPreferences();
        password.setModule("telnet");
        password.setName("telnetPassword");
        password.setValue(telnetLogin.getPassword());
        perferences.add(password);

        SystemPreferences enablePassword = new SystemPreferences();
        enablePassword.setModule("telnet");
        enablePassword.setName("telnetEnablePassword");
        enablePassword.setValue(telnetLogin.getEnablePassword());
        perferences.add(enablePassword);

        SystemPreferences isAAA = new SystemPreferences();
        isAAA.setModule("telnet");
        isAAA.setName("telnetIsAAA");
        isAAA.setValue(telnetLogin.getIsAAA() ? "1":"0" );
        perferences.add(isAAA);

        systemPreferencesDao.updateEntity(perferences);
    }

    @Override
    public void exportToExcel(List<TelnetLogin> telnetLogins) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode("TelnetLogin" + "-" + fileNameFormatter.format(new Date()) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createEntityExcelFile(telnetLogins, workbook);
        } catch (UnsupportedEncodingException e) {
            logger.debug("unsupported Encoding Exception:", e);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("close fileStream error:{}", e);
            }
        }
    }

    /**
     * 导出设备
     * 
     * @param entityList
     * @param workbook
     */
    private void createEntityExcelFile(List<TelnetLogin> telnetLogins, WritableWorkbook workbook) {
        // 创建sheet页和一些格式化信息
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);

        sheet.setColumnView(0, 30);
        sheet.setColumnView(1, 30);
        sheet.setColumnView(2, 20);
        sheet.setColumnView(3, 25);
        sheet.setColumnView(4, 25);
        Label label;
        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 0, "IP", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 0, "UserName", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 0, "Password", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 0, "EnablePassword", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 0, "isAAA", titleCellFormat);
            sheet.addCell(label);

            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            if (telnetLogins == null) {
                return;
            }
            int rowNum = 1;
            for (TelnetLogin telnetLogin : telnetLogins) {
                // 输出数据行
                label = new Label(0, rowNum, telnetLogin.getIpString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, telnetLogin.getUserName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, telnetLogin.getPassword(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, telnetLogin.getEnablePassword(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, String.valueOf(telnetLogin.getIsAAA()), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createCurrentAlarmReportExcelFile method is error:{}", e);
        }
    }

    @Override
    public void importTelnetLogin(List<TelnetLogin> importTelnetLogins, boolean overwrite) {
        if (overwrite) {
            telnetLoginDao.batchInsertOrUpdateTelnetLogin(importTelnetLogins);
        } else {
            telnetLoginDao.batchInsertTelnetLogin(importTelnetLogins);
        }
    }

    @Override
    public TelnetLogin getEntityTelnetLogin(String ip) {
        TelnetLogin telnetLogin = this.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = this.getGlobalTelnetLogin();
        }
        return telnetLogin;
    }

}
