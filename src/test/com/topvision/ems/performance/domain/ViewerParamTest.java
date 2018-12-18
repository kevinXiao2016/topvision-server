/***********************************************************************
 * $ ViewerParamTest.java,v1.0 2012-7-16 9:46:41 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

/**
 * @author jay
 * @created @2012-7-16-9:46:41
 *
 */

import org.junit.Test;
import org.junit.Before;
import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.performance.domain.ViewerParam;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.sql.Date;

public class ViewerParamTest {

    @Test
    public void testSetTimeType() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        ViewerParam viewerParam = new ViewerParam();
        String type = ViewerParam.TODAY;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.YESTERDAY;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THE_LAST_TWO_DAYS;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THE_LAST_SEVEN_DAYS;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THIS_MONTH;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.LAST_MONTH;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THE_LAST_THIRTY_DAYS;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THE_LAST_THREE_MONTHS;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THE_LAST_TWELVE_MONTHS;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
        type = ViewerParam.THIS_YEAR;
        System.out.println("*******************" + type + "*************************");
        viewerParam.setTimeType(type);
        System.out.println("st = " + sdf.format(new Date(viewerParam.getStLong())));
        System.out.println("et = " + sdf.format(new Date(viewerParam.getEtLong())));
    }
}