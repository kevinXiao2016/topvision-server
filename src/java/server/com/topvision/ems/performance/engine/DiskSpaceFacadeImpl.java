package com.topvision.ems.performance.engine;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.performance.domain.ServerPerformanceInfo;
import com.topvision.ems.performance.facade.DiskSpaceFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.utils.CmdDiskSpace;

@Facade("diskSpaceFacade")
public class DiskSpaceFacadeImpl extends EmsFacade implements DiskSpaceFacade {

    @Override
    public ServerPerformanceInfo getDiskSpaceInfo() {
        CmdDiskSpace cmdDiskSpace = new CmdDiskSpace();
        Map<String, Integer> diskSpace = cmdDiskSpace.diskSpace();

        if (diskSpace == null) {
            return null;
        }

        ServerPerformanceInfo diskInfo = new ServerPerformanceInfo();
        diskInfo.setTotalDiskSize((long) diskSpace.get("total"));
        diskInfo.setFreeDiskSize((long) diskSpace.get("free"));
        try {
            if (isWindowsOS()) {
                diskInfo.setServerIp(InetAddress.getLocalHost().getHostAddress());
            } else {
                diskInfo.setServerIp(getLinuxIpv4());
            }
        } catch (UnknownHostException e) {
            logger.error("", e);
            diskInfo.setServerIp("IP Not Find");
        }
        return diskInfo;
    }

    private boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    private String getLinuxIpv4() {
        String ip = "IP Not Find";
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia.isSiteLocalAddress()
                            && ia instanceof Inet4Address) {
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {

        }
        return ip;
    }

}
