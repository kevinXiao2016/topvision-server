/***********************************************************************
 * $Id: EntityImportAction.java,v1.0 2013-10-28 上午10:12:42 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.EntityImport;
import com.topvision.ems.network.service.EntityImportService;
import com.topvision.framework.common.ExcelUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;

import net.sf.json.JSONObject;

/**
 * @author loyal
 * @created @2013-10-28-上午10:12:42
 * 
 */
@Controller("entityImportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityImportAction extends BaseAction {
	private static final long serialVersionUID = 3551296185051223746L;
	@Resource(name = "entityImportService")
	private EntityImportService entityImportService;
	private Logger logger = LoggerFactory.getLogger(EntityImportAction.class);
	private boolean overwrite;

	/**
	 * 显示导出页面
	 * 
	 * @return
	 */
	public String showEntityNameImport() {
		return SUCCESS;
	}

	/**
	 * 显示预览页面
	 * 
	 * @return
	 */
	public String showPreviewEntityName() {
		return SUCCESS;
	}

	/**
	 * 获取预览信息
	 * 
	 * @return
	 */
	public String getPreviewEntityName() {
		return NONE;
	}

	/**
	 * 导入设备名称
	 * 
	 * @return
	 */
	public String importEntityName() {
		List<EntityImport> importEntityList = new ArrayList<EntityImport>();
		String[][] excelData = null;
		Map<String, Object> json = new HashMap<String, Object>();
		// 读取excel文件
		try {
			MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
			Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
			while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
				String inputName = fileParameterNames.nextElement();
				String[] contentType = multiWrapper.getContentTypes(inputName);
				if (isNonEmpty(contentType)) {
					String[] fileName = multiWrapper.getFileNames(inputName);
					if (isNonEmpty(fileName)) {
						File[] files = multiWrapper.getFiles(inputName);
						if (files != null) {
							for (File file1 : files) {
								try {
									excelData = ExcelUtil.getExcelDataFor2007(file1).get(0);
									break;
								} catch (Exception e) {
									try {
										String fileNameStr = file1.getPath();
										excelData = ExcelUtil.getExcelDataFor2003(fileNameStr).get(0);
										break;
									} catch (Exception e1) {
										logger.error("", e);
										json.put("message", "FileWrong");
										writeDataToAjax(JSONObject.fromObject(json));
										return NONE;
									}
								}
							}
						}
					}
				}
			}
			// 判断文件数据是否为空
			if (excelData == null) {
				json.put("message", "FileWrong");
				writeDataToAjax(JSONObject.fromObject(json));
				return NONE;
			} else {
				// 确定列参数
				String[] titleRow = excelData[0];
				int aliasColumnNum = -1;
				int ipColumnNum = -1;
				int macColumnNum = -1;
				int contactColumnNum = -1;
				int locationColumnNum = -1;
				int noteColumnNum = -1;
				for (int i = 0; i < titleRow.length; i++) {
					if (titleRow[i] != null && "Alias".equals(titleRow[i])) {
						aliasColumnNum = i;
					}
					if (titleRow[i] != null && "MAC".equals(titleRow[i])) {
						macColumnNum = i;
					}
					if (titleRow[i] != null && "IP".equals(titleRow[i])) {
						ipColumnNum = i;
					}
					if (titleRow[i] != null && "Contact".equals(titleRow[i])) {
						contactColumnNum = i;
					}
					if (titleRow[i] != null && "Location".equals(titleRow[i])) {
						locationColumnNum = i;
					}
					 if (titleRow[i] != null && "Note".equals(titleRow[i])) {
					 noteColumnNum = i;
					 }
				}

				if (aliasColumnNum < 0 || contactColumnNum < 0 || locationColumnNum < 0
						|| (macColumnNum < 0 && ipColumnNum < 0)) {
					json.put("message", "FileWrong");
					writeDataToAjax(JSONObject.fromObject(json));
					return NONE;
				}
				for (int i = 1; i < excelData.length; i++) {
					if (excelData[i] == null) {
						continue;
					}
					EntityImport entityImport = new EntityImport();
					entityImport.setRowId(i+1);
					if (aliasColumnNum >= 0) {
						String alias = "";
						if (excelData[i].length > aliasColumnNum && excelData[i][aliasColumnNum] != null) {
							alias = excelData[i][aliasColumnNum].trim();
						}
						entityImport.setName(alias);
					}
					if (ipColumnNum >= 0) {
						String ip = "";
						if (excelData[i].length > ipColumnNum && excelData[i][ipColumnNum] != null) {
							ip = excelData[i][ipColumnNum].trim();
						}
						entityImport.setIp(ip);
					}
					if (macColumnNum >= 0) {
						String mac = "";
						if (excelData[i].length > macColumnNum && excelData[i][macColumnNum] != null) {
							mac = excelData[i][macColumnNum].toUpperCase().trim();
						}
						entityImport.setMac(mac);
					}
					if (("".equals(entityImport.getIp()) && "".equals(entityImport.getMac()))
							|| "".equals(entityImport.getName())) {
						continue;
					}
					if (contactColumnNum >= 0) {
						String contact = "";
						if (excelData[i].length > contactColumnNum && excelData[i][contactColumnNum] != null) {
							contact = excelData[i][contactColumnNum].trim();
						}
						entityImport.setContact(contact);
					}
					if (locationColumnNum >= 0) {
						String location = "";
						if (excelData[i].length > locationColumnNum && excelData[i][locationColumnNum] != null) {
							location = excelData[i][locationColumnNum].trim();
						}
						entityImport.setLocation(location);
					}
					if (noteColumnNum >= 0) {
						String note = "";
						if (excelData[i].length > noteColumnNum && excelData[i][noteColumnNum] != null) {
							note = excelData[i][noteColumnNum].trim();
						}
						entityImport.setNote(note);
					}
					importEntityList.add(entityImport);
				}
				Map<String, Object> map = entityImportService.importEntityName(importEntityList, overwrite);
				json.put("successList", map.get("successList"));
				json.put("errorList", map.get("errorList"));
				json.put("notFindList", map.get("notFindList"));
				json.put("successNum", map.get("successNum"));
				json.put("errorNum", map.get("errorNum"));
				json.put("notFindNum", map.get("notFindNum"));
				json.put("message", "success");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			json.put("message", "fail");
		}
		writeDataToAjax(JSONObject.fromObject(json));
		return NONE;
	}

	/**
	 * 下载导入模板
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String downLoadTemplate() throws UnsupportedEncodingException {
		String tmpFile = "entityNameImportTemplate.xlsx";
		StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
		fileName.append("META-INF/");
		fileName.append(tmpFile);
		int i;
		byte[] b = new byte[1024];
		OutputStream out = null;
		FileInputStream fis = null;
		ServletActionContext.getResponse().setContentType("application/x-download");
		ServletActionContext.getResponse().addHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(tmpFile, "UTF-8"));
		try {
			File rFile = new File(fileName.toString());
			if (rFile != null) {
				fis = new FileInputStream(rFile);
				out = ServletActionContext.getResponse().getOutputStream();
				while ((i = fis.read(b)) > 0) {
					out.write(b, 0, i);
				}
			}
		} catch (Exception e) {
			logger.debug("downLoadCmInfoFileTemplate is error:{}", e);
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
				logger.debug("downLoadCmInfoFileTemplate is error:{}", e);
			}
		}
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
		for (int i = 0; i < objArray.length && !result; i++) {
			if (objArray[i] != null) {
				result = true;
			}
		}
		return result;
	}

	public EntityImportService getEntityImportService() {
		return entityImportService;
	}

	public void setEntityImportService(EntityImportService entityImportService) {
		this.entityImportService = entityImportService;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
}
