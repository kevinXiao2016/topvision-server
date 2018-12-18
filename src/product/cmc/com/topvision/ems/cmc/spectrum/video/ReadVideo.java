/***********************************************************************
 * $ ReadVideo.java,v1.0 2014-1-21 16:24:59 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideoFrameData;
import com.topvision.ems.cmc.spectrum.utils.VersionUtil;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;

/**
 * @author jay
 * @created @2014-1-21-16:24:59
 */
public class ReadVideo {
    private static String videoRootPath = "./video/his/";

    public static void main(String[] args) {
        ReadVideo readVideo = new ReadVideo();

        videoRootPath = "d:/";
        readVideo.read("30000005103_1470557411943");

        // String url = "E:/workspace_ems/topvision-console/video/his/146_1394235177834.txt";
        // File file = new File(url);
        // file.delete();
    }

    private void read(String videoName) {
        try {
            SpectrumVideoToolV1_1 videoVersionTool = new SpectrumVideoToolV1_1();
            RandomAccessFile rf = new RandomAccessFile(new File(videoRootPath + videoName + ".dat"), "r");
            Integer versionLong = videoVersionTool.readInt(rf, SpectrumVideoToolV1_1.versionIndex);
            System.out.println(versionLong);
            System.out.println("VerionUtil = " + VersionUtil.getVersionString(versionLong));
            Long entityId = videoVersionTool.readLong(rf, SpectrumVideoToolV1_1.entityIdIndex);
            Long cmcId = videoVersionTool.readLong(rf, SpectrumVideoToolV1_1.cmcIdIndex);
            Long dt = videoVersionTool.readLong(rf, SpectrumVideoToolV1_1.dtIndex);
            Integer pointCount = videoVersionTool.readInt(rf, SpectrumVideoToolV1_1.pointCountIndex);
            Long frameCount = videoVersionTool.readLong(rf, SpectrumVideoToolV1_1.frameCountIndex);
            System.out.println("entityId = " + entityId);
            System.out.println("cmcId = " + cmcId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("dt = " + sdf.format(new Date(dt)));
            System.out.println("pointCount = " + pointCount);
            System.out.println("frameCount = " + frameCount);
            System.out.println("No\tfrqIndex\tpower");
            // for (Long frameIndex = 1L; frameIndex <= frameCount; frameIndex++) {
            // System.out.println("************"+frameIndex+"************");
            // for (int pointIndex = 1; pointIndex <= pointCount && pointIndex < 10; pointIndex++) {
            // SpectrumPoint sp =
            // VideoVersionToolV1_0_0_0.readPoint(rf,frameIndex,pointCount,pointIndex);
            // System.out.println(pointIndex + "\t" + sp.getX() + "\t" + sp.getY());
            // }
            // }

            for (Long frameIndex = 1L; frameIndex <= frameCount; frameIndex++) {
                System.out.println("************" + frameIndex + "************");
                SpectrumVideoFrameData sd = videoVersionTool.readFrame(rf, frameIndex);
                List<CmcUpChannelBaseShowInfo> channelList = sd.getChannelList();
                for (int i = 0; i < 4; i++) {
                    System.out.println(channelList.get(i).getChannelFrequency());
                    System.out.println(channelList.get(i).getChannelWidth());
                    System.out.println(channelList.get(i).getChannelStatus());
                }
                List<List<Number>> list = sd.getDataList();
                System.out.println(list.size());
                for (int i = 0; i < 20; i++) {
                    // System.out.println(list.get(i).size());
                    System.out.print(list.get(i).get(0) + " ,");
                    System.out.print(list.get(i).get(1) + " |");
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
