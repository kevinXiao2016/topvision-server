package com.topvision.ems.performance.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.performance.domain.ServerPerformanceInfo;
import com.topvision.ems.performance.facade.DiskSpaceFacade;
import com.topvision.ems.performance.service.ServerPerformanceService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author xy
 * @created @2018年6月28日-上午9:36:07
 *
 */
public class ServerDiskJob extends AbstractJob {

    // private static String osName = System.getProperty("os.name");

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        ServerPerformanceService serverPerformanceService = getService(ServerPerformanceService.class);
        FacadeFactory facadeFactory = getService(FacadeFactory.class);

        // 从engine获取磁盘数据
        List<DiskSpaceFacade> allFacade = facadeFactory.getAllFacade(DiskSpaceFacade.class);
        for (DiskSpaceFacade diskSpaceFacade : allFacade) {
            try {
                ServerPerformanceInfo diskSpaceInfo = diskSpaceFacade.getDiskSpaceInfo();
                logger.info("GetDiskSpaceInfo:" + diskSpaceInfo.toString());
                serverPerformanceService.addDiskInfo(diskSpaceInfo);
            } catch (Exception e) {
                logger.error("GetDiskSpaceInfo Error", e);
            }
        }

    }

    /*
     * private ServerPerformanceInfo getDiskInfo() {
     * 
     * if (osName.toLowerCase().contains("windows") || osName.toLowerCase().contains("win")) {
     * return getWindowsDiskInfo(); } try { return getLinuxDiskInfo(); } catch (IOException e) {
     * logger.error("getDiskInfo Error",e); return null; } }
     * 
     * private ServerPerformanceInfo getWindowsDiskInfo() { String dir =
     * System.getProperty("user.dir"); ServerPerformanceInfo diskInfo = new ServerPerformanceInfo();
     * InetAddress addr = null; try { addr = InetAddress.getLocalHost(); } catch
     * (UnknownHostException e) { logger.error("",e); } File[] disks = File.listRoots(); for (File
     * file : disks) { if (dir.startsWith(file.getPath())) {
     * diskInfo.setTotalDiskSize(file.getTotalSpace());
     * diskInfo.setFreeDiskSize(file.getFreeSpace()); break; } }
     * diskInfo.setServerIp(addr.getHostAddress()); return diskInfo; }
     * 
     * private ServerPerformanceInfo getLinuxDiskInfo() throws IOException { Runtime rt =
     * Runtime.getRuntime(); Process p = rt.exec("df -hl");// df -hl 查看硬盘空间 BufferedReader in =
     * null; double totalHD = 0; double usedHD = 0; try { in = new BufferedReader(new
     * InputStreamReader(p.getInputStream())); String str = null; String[] strArray = null; while
     * ((str = in.readLine()) != null) { int m = 0; strArray = str.split(" "); for (String tmp :
     * strArray) { if (tmp.trim().length() == 0) continue; ++m; if (tmp.indexOf("G") != -1) { if (m
     * == 2) { if (!tmp.equals("") && !tmp.equals("0")) totalHD +=
     * Double.parseDouble(tmp.substring(0, tmp.length() - 1)) * 1024; } if (m == 3) { if
     * (!tmp.equals("none") && !tmp.equals("0")) usedHD += Double.parseDouble(tmp.substring(0,
     * tmp.length() - 1)) * 1024; } } if (tmp.indexOf("M") != -1) { if (m == 2) { if
     * (!tmp.equals("") && !tmp.equals("0")) totalHD += Double.parseDouble(tmp.substring(0,
     * tmp.length() - 1)); } if (m == 3) { if (!tmp.equals("none") && !tmp.equals("0")) usedHD +=
     * Double.parseDouble(tmp.substring(0, tmp.length() - 1)); } } } } } catch (Exception e) {
     * logger.debug("linux get diskInfo error:", e); } finally { in.close(); }
     * 
     * //保留2位小数 double precent = (usedHD / totalHD) * 100; BigDecimal b1 = new BigDecimal(precent);
     * precent = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
     * 
     * ServerPerformanceInfo diskInfo = new ServerPerformanceInfo();
     * diskInfo.setTotalDiskSize((long) totalHD * 1024 * 1024); diskInfo.setFreeDiskSize((long)
     * usedHD * 1024 * 1024); return diskInfo; }
     */

}
