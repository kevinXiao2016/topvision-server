/***********************************************************************
 * $Id: Ssh.java,v 1.1 2009-11-21 上午12:05:08 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date 2009-11-21 上午12:05:08
 * 
 * @author kelers
 * 
 */
public class Ssh {
    protected static final Logger logger = LoggerFactory.getLogger(Ssh.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        SshWrapper ssh = new SshWrapper();
        try {
            ssh.connect("192.168.168.100", 22);
            ssh.login("root", "abc123");
            ssh.setPrompt("#");
            // ssh.waitfor("Terminal type?");
            ssh.send("dumb");
            System.out.println("=====================");
            System.out.println(ssh.send("ls -l"));
            System.out.println("=====================");
            System.out.println(ssh.send("ls"));
            System.out.println("=====================");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
