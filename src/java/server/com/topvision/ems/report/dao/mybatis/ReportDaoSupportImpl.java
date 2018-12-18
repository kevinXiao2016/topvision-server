/***********************************************************************
 * $Id: ReportDaoSupportImpl.java,v1.0 2014-6-14 下午2:51:42 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao.mybatis;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.topvision.ems.report.domain.ReportNode;
import com.topvision.ems.report.service.impl.ReportCoreServiceImpl;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Administrator
 * @created @2014-6-14-下午2:51:42
 * 
 */

@Repository("reportDaoSupport")
public class ReportDaoSupportImpl extends MyBatisDaoSupport<Object> {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        // TODO Auto-generated method stub
        return "Report";
    }

    public static void main(String[] args) throws Exception {
        ReportDaoSupportImpl reportDaoSupportImpl = new ReportDaoSupportImpl();

        ReportCoreServiceImpl coreService = new ReportCoreServiceImpl();
        coreService.initialize();
        ReportNode baseNode = new ReportNode();
        // commonService.getReportNode(baseNode, entities, reportStructure);
        // ReportNode rootNode = commonService.step1(entities, reportStructure);
        // JSONObject object = JSONObject.fromObject(ReportXmlParser.handleJsonObject(rootNode));
        //Report report = coreService.fetchReportStructureInfo("CM");
        //JSONObject reportObject = JSONObject.fromObject(report.getReportCondition());   
        JSONObject object = coreService.getReportContent("CM", null);
        System.out.print(1);
    }

    private static void test(ReportNode node) {
        System.out.println(node.getNodeKey() + ":" + node.getNodeValue() + ":" + node.getChildrenNode().size());
        for (ReportNode tmpNode : node.getChildrenNode()) {
            test(tmpNode);
        }
    }
}
