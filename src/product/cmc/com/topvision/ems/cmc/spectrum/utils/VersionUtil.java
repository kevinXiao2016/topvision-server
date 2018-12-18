/***********************************************************************
 * $ VersionUtil.java,v1.0 2013-3-19 19:56:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topvision.ems.cmc.spectrum.exception.WrongVersionStringException;
import com.topvision.ems.cmc.spectrum.video.SpectrumVideoTool;
import com.topvision.ems.cmc.spectrum.video.SpectrumVideoToolV1_0;
import com.topvision.ems.cmc.spectrum.video.SpectrumVideoToolV1_1;
import com.topvision.ems.cmc.spectrum.video.SpectrumVideoToolV1_2;

/**
 * @author jay
 * @created @2013-3-19-19:56:44
 */
public class VersionUtil {
    public static int getVersionLong(String version) {
        Pattern p = Pattern.compile("\\d*\\.\\d*\\.\\d*\\.\\d*");
        Matcher m = p.matcher(version);
        boolean b = m.find();
        String v;
        if (!b) {
            Pattern p2 = Pattern.compile("\\d*\\.\\d*\\.\\d*");
            Matcher m2 = p2.matcher(version);
            boolean b2 = m2.find();
            if (!b2) {
                throw new WrongVersionStringException(version);
            } else {
                v = m2.group() + ".0";
            }
        } else {
            v = m.group();
        }
        int l = 0;
        String[] strs = v.split("\\.");
        for (int i = 0; i < strs.length; i++) {
            l = l + Integer.parseInt(strs[i]) * (int) Math.pow(1000, (strs.length - i - 1));
        }
        return l;
    }

    public static String getVersionString(Integer version) {
        int subversion1 = version % 1000;
        version = version / 1000;
        int subversion2 = version % 1000;
        version = version / 1000;
        int subversion3 = version % 1000;
        int subversion4 = version / 1000;
        return "V" + subversion4 + "." + subversion3 + "." + subversion2 + "." + subversion1;
    }

    public static SpectrumVideoTool getVersionClass(RandomAccessFile rf) throws IOException {
        int version = SpectrumVideoToolV1_0.readInt(rf, SpectrumVideoToolV1_0.versionIndex);
        switch (version) {
        case 1000002000:
            return new SpectrumVideoToolV1_2();
        case 1000001000:
            return new SpectrumVideoToolV1_1();
        default:
            return new SpectrumVideoToolV1_0();
        }
    }

}