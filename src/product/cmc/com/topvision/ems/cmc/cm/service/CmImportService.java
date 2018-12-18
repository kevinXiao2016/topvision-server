/***********************************************************************
 * $Id: CmImportService.java,v1.0 2013-10-30 上午9:38:15 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.domain.CmImportError;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.framework.service.Service;

/**
 * @author YangYi
 * @created @2013-10-30-上午9:38:15
 * 
 */
public interface CmImportService extends Service {
    /**
     * 获取CM导入信息
     * 
     * @param map
     *            查询条件
     * @return List<CmImportInfo> 返回CM导入信息列表
     */
    public List<CmImportInfo> getCmImportInfoList(Map<String, Object> map, Integer start, Integer limit);

    /**
     * 获取CM导入信息数量
     * 
     * @param map
     * @return
     */
    public Long getCmImportInfoNum(Map<String, Object> map);

    /**
     * 导入CM导入信息
     * 
     * @param cmImportInfos
     *            cmImportInfos信息
     * @param delete
     *            是否删除之前导入信息，true删除，false不删除
     */
    public void addCmImportInfo(List<CmImportInfo> cmImportInfos, Boolean delete);

    /**
     * 导出错误日志到EXCEL
     * @param errors
     * @throws IOException 
     */
    public void importErrorLog(List<CmImportError> errors) throws IOException;

}
