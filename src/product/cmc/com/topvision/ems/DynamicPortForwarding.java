package com.topvision.ems;

import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.framework.telnet.TelnetVty;

import java.io.IOException;

/**
 * Created by jay on 17-4-12.
 */
public class DynamicPortForwarding {
    private TelnetVty telnetVty;

    public static void main(String[] args) {
        DynamicPortForwarding dynamicPortForwarding = new DynamicPortForwarding();
        dynamicPortForwarding.go();

    }

    private void go() {
//        TelnetLogin telnetLogin;
//        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(snmpParam.getIpAddress()).longValue());
//        if (telnetLogin == null) {
//            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
//        }
        String re = "";
        try {
            telnetVty = new TelnetVty();
            telnetVty.setPrompt("#,>,:,[n],(yes/no)?","");
            telnetVty.connect("172.17.2.148");
            re = telnetVty.sendLine("");
            System.out.println("" + re);
            re = telnetVty.sendLine("admin");
            System.out.println("" + re);
            re =  telnetVty.sendLine("admin");
            System.out.println("" + re);
            re =  telnetVty.sendLine("enable");
            System.out.println("" + re);
            re =  telnetVty.sendLine("configure terminal");
            System.out.println("" + re);
            re =  telnetVty.sendLine("super");
            System.out.println("" + re);
            re =  telnetVty.sendLine("8ik,(OL>");
            System.out.println("" + re);
            re =  telnetVty.sendLine("shell");
            System.out.println("" + re);
            re =  telnetVty.sendLine("ssh -g -D 7000 admin@172.17.2.148");
            System.out.println("" + re);
            re =  telnetVty.sendLine("yes");
            System.out.println("" + re);
            re =  telnetVty.sendLine("admin");
            System.out.println("" + re);
            int i = 0;
            while (true) {
                String s = telnetVty.sendLine("");
                System.out.println("s = " + s);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                System.out.println("i : " + i);
//                if (i > 20) {
//                    telnetVty.sendLine("exit");
//                    System.out.println("com.topvision.ems.DynamicPortForwarding.go");
//                    break;
//                }
            }
//            System.out.println("com.topvision.ems.DynamicPortForwarding.go2");
//            telnetVty.disconnect();
//            System.out.println("com.topvision.ems.DynamicPortForwarding.go3");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

}
