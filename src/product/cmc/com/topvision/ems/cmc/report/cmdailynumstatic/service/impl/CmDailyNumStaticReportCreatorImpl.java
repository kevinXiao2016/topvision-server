/***********************************************************************
 * $Id: CmDailyNumStaticReportCreatorImpl.java,v1.0 2013-10-30 上午8:37:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmdailynumstatic.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.report.cmdailynumstatic.dao.CmDailyNumStaticReportDao;
import com.topvision.ems.cmc.report.cmdailynumstatic.service.CmDailyNumStaticReportCreator;
import com.topvision.ems.cmc.report.domain.CmDailyNumStaticReport;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.network.domain.FolderCategory;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author haojie
 * @created @2013-10-30-上午8:37:08
 * 
 */
@Service("cmDailyNumStaticReportCreator")
public class CmDailyNumStaticReportCreatorImpl extends BaseService implements CmDailyNumStaticReportCreator {
    @Autowired
    private CmDailyNumStaticReportDao cmDailyNumStaticReportDao;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask reportTask) {
        // 图标导出不支持任务
    }

    @Override
    public JSONObject loadFolderOltCmcLists() {
        JSONObject json = new JSONObject();
        // 加载该用户所能查看的所有地域
        List<TopoFolder> list = topologyService.loadMyTopoFolder(CurrentRequest.getCurrentUser().getUserId(),
                FolderCategory.CLASS_NETWORK);
        JSONArray jsonArray = new JSONArray();
        for (TopoFolder folder : list) {
            JSONObject folderJsonObject = new JSONObject();
            folderJsonObject.put("id", folder.getFolderId());
            folderJsonObject.put("name", ResourcesUtil.getString(folder.getName().toString()));
            jsonArray.add(folderJsonObject);
        }
        /*
         * List<Map<String, String>> folders =
         * cmDailyNumStaticReportDao.loadIdAndNamePairsFromTable( CmDailyNumStaticReport.TOPOFOLDER,
         * null);
         */
        List<Map<String, String>> olts = cmDailyNumStaticReportDao.loadIdAndNamePairsFromTable(
                CmDailyNumStaticReport.ENTITY,entityTypeService.getOltType().intValue());
        List<Map<String, String>> cmcs = cmDailyNumStaticReportDao.loadIdAndNamePairsFromTable(
                CmDailyNumStaticReport.ENTITY, entityTypeService.getCcmtsandcmtsType().intValue());
        json.put("folders", jsonArray);
        json.put("olts", olts);
        json.put("cmcs", cmcs);
        return json;
    }

    @Override
    public JSONObject statCmDailyNumStaticReport(Map<String, Object> map) {
        List<CmDailyNumStaticReport> cmDailyNumStaticReports = cmDailyNumStaticReportDao.loadCmcDailyMaxCmNum(map);
        // 获取每一天
        // 格式化开始时间和结束时间
        String startTimeString = (String) map.get("startTime");
        startTimeString = startTimeString.split(" ")[0];
        String endTimeString = (String) map.get("endTime");
        endTimeString = endTimeString.split(" ")[0];
        List<String> days = getDaysBetweenRange(Date.valueOf(startTimeString), Date.valueOf(endTimeString));
        // 构建时间-CM个数的map
        Map<String, Long> onlineCmMap = new HashMap<String, Long>();
        Map<String, Long> offlineCmMap = new HashMap<String, Long>();
        Map<String, Long> otherCmMap = new HashMap<String, Long>();
        Map<String, Long> allCmMap = new HashMap<String, Long>();
        for (String day : days) {
            onlineCmMap.put(day, 0L);
            offlineCmMap.put(day, 0L);
            otherCmMap.put(day, 0L);
            allCmMap.put(day, 0L);
        }
        // 对指定区域中查询到的设备的CM个数信息进行汇总成每一天的总数
        // 获取当前用户权限下的所有CCMTS/CMTS的ID列表
//        List<Long> typeArray = new ArrayList<Long>();
//        typeArray.add(30000L);
//        typeArray.add(40000L);
        List<Long> authCmcList = entityService.getEntityIdsByAuthority(entityTypeService.getCcmtsandcmtsType());
        Map<String, List<Long>> cmcDaysMap = new HashMap<String, List<Long>>();
        for (CmDailyNumStaticReport report : cmDailyNumStaticReports) {
            // 如果不是该用户权限下，就不统计
            if (!authCmcList.contains(report.getCmcId())) {
                continue;
            }
            // 如果该天已经统计了该设备，则不统计
            if (cmcDaysMap.containsKey(report.getRealtime().toString())) {
                List<Long> cmcIds = cmcDaysMap.get(report.getRealtime().toString());
                if (cmcIds != null && cmcIds.contains(report.getCmcId())) {
                    continue;
                }
                if (cmcIds == null) {
                    cmcIds = new ArrayList<Long>();
                }
                cmcIds.add(report.getCmcId());
            } else {
                // 如果该天还未统计任何信息，则加入该天数据
                List<Long> cmcIdsList = new ArrayList<Long>();
                cmcIdsList.add(report.getCmcId());
                cmcDaysMap.put(report.getRealtime().toString(), cmcIdsList);
            }

            // 取出每一条数据，进行相应的汇总
            onlineCmMap.put(report.getRealtime().toString(),
                    onlineCmMap.get(report.getRealtime().toString()) + report.getCmNumOnlineMax());
            offlineCmMap.put(report.getRealtime().toString(), offlineCmMap.get(report.getRealtime().toString())
                    + report.getOfflineNum());
            otherCmMap.put(report.getRealtime().toString(),
                    otherCmMap.get(report.getRealtime().toString()) + report.getOtherNum());
            allCmMap.put(report.getRealtime().toString(),
                    allCmMap.get(report.getRealtime().toString()) + report.getTotalCmNum());
        }
        // 将汇总后的数据转换为供highstock使用的曲线
        List<Point> onlineCmLine = new ArrayList<Point>();
        List<Point> offlineCmLine = new ArrayList<Point>();
        List<Point> otherCmLine = new ArrayList<Point>();
        List<Point> allCmLine = new ArrayList<Point>();
        // 转成jsonarray
        JSONArray onlineCmArray = transferMapDateToLine(onlineCmMap, onlineCmLine);
        JSONArray offlineCmArray = transferMapDateToLine(offlineCmMap, offlineCmLine);
        JSONArray otherCmArray = transferMapDateToLine(otherCmMap, otherCmLine);
        JSONArray allCmArray = transferMapDateToLine(allCmMap, allCmLine);
        // 封装成jsonobject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CmDailyNumStaticReport.ONLINE, onlineCmArray);
        jsonObject.put(CmDailyNumStaticReport.OFFLINE, offlineCmArray);
        jsonObject.put(CmDailyNumStaticReport.OTHERSTATUS, otherCmArray);
        jsonObject.put(CmDailyNumStaticReport.ALLSTATUS, allCmArray);
        return jsonObject;
    }

    private List<String> getDaysBetweenRange(Date startTime, Date endTime) {
        List<String> days = new ArrayList<String>();

        while (!startTime.after(endTime)) {
            days.add(startTime.toString());
            startTime.setTime(startTime.getTime() + 86400 * 1000);
        }
        return days;
    }

    private JSONArray transferMapDateToLine(Map<String, Long> map, List<Point> line) {
        JSONArray jsonArray = new JSONArray();
        Iterator<Entry<String, Long>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iter.next();
            String date = (String) entry.getKey();
            Long num = (Long) entry.getValue();
            Point point = new Point();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(Date.valueOf(date));
            point.setXTime(Timestamp.valueOf(time));
            point.setY(Double.valueOf(Long.toString(num)));
            line.add(point);
        }
        // 排序
        Collections.sort(line);
        // 存入json数组中
        for (Point point : line) {
            JSONArray pointArray = new JSONArray();
            pointArray.add(point.getXTime().getTime());
            pointArray.add(point.getY());
            jsonArray.add(pointArray);
        }
        return jsonArray;
    }
}
