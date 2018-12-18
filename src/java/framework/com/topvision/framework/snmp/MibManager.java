/***********************************************************************
 * $Id: MibManager.java,v1.0 2011-10-25 下午02:59:10 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;

/**
 * @author Victor
 * @created @2011-10-25-下午02:59:10
 * 
 */
public class MibManager {
    private static Logger logger = LoggerFactory.getLogger(MibManager.class);
    private static MibManager mm;
    private MibLoader mibLoader = null;
    private List<Mib> mibs;

    public static MibManager getInstance() {
        if (mm == null) {
            mm = new MibManager();
        }
        return mm;
    }

    private MibManager() {
        mibLoader = new MibLoader();
        // 默认的资源目录不用，清空以加快查找速度
        mibLoader.removeAllResourceDirs();
        mibs = new ArrayList<Mib>();
        File root = new File(EnvironmentConstants.getEnv(EnvironmentConstants.MIB_HOME));
        mibLoader.addAllDirs(root);
        File conf = new File(String.format("%s/mibs.conf", root.getPath()));
        if (conf != null && conf.exists()) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(conf));
                String line = null;
                while ((line = in.readLine()) != null) {
                    try {
                        line = line.trim();
                        if (line.length() == 0) {
                            continue;
                        } else if (line.startsWith("#")) {
                            continue;
                        } else {
                            File mib = new File(String.format("%s/%s", root.getPath(), line));
                            if (logger.isInfoEnabled()) {
                                logger.info("Begin to load mib:{}", mib);
                            }
                            mibs.add(mibLoader.load(mib));
                        }
                    } catch (MibLoaderException e) {
                        logger.error(line, e);
                    }
                }
            } catch (FileNotFoundException e) {
                logger.error(conf.getPath(), e);
            } catch (IOException e) {
                logger.error(conf.getPath(), e);
            } finally {
                try {
                    in.close();
                } catch (NullPointerException e) {
                    logger.error(conf.getPath(), e);
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        if (root != null && root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.getName().equalsIgnoreCase("mibs.conf")) {
                    continue;
                }
                if (mibLoader.getMib(file.getName()) != null) {
                    continue;
                }
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info("Begin to load mib:{}", file);
                    }
                    mibLoader.load(file);
                } catch (IOException e) {
                    logger.error(file.getPath(), e);
                } catch (MibLoaderException e) {
                    logger.error(file.getPath(), e);
                }
            }
        }
    }

    /**
     * @return mibs.conf文件中配置的MIB库
     */
    public List<Mib> getConfMibs() {
        return mibs;
    }

    /**
     * @return 所有导入的MIB库
     */
    public Mib[] getAllMibs() {
        return mibLoader.getAllMibs();
    }

    /**
     * @return 所有导入的MIB库
     */
    public Mib getMibByName(String name) {
        return mibLoader.getMib(name);
    }

    /**
     * 加载单个mib库文件。
     * 
     * @param name
     *            mib库文件名
     * @return Mib实例
     * @see #loadMibs(String mib)
     */
    public synchronized Mib loadMib(String name) {
        try {
            Mib mib = null;
            // mibLoader.getMib(name);
            // if (mib != null) {
            // return mib;
            // }
            // TODO 打开MIB文件，解密

            mib = mibLoader.load(name);
            if (mib != null) {
                return mib;
            }
            File file = new File(name);
            if (file != null && file.isFile()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(file.getName());
                }
                mibLoader.addAllDirs(new File(file.getParentFile().getPath()));
                mib = mibLoader.load(file.getName());
                return mib;
            }
        } catch (IOException ex) {
            logger.error(name, ex);
        } catch (MibLoaderException ex) {
            logger.error(name, ex);
        }
        return null;
    }

    /**
     * 加载mib库文件，可以同时加载多个mib库文件，以逗号隔开。
     * 
     * 注意：需要首先设置mib库目录参数。参考mibDir
     * 
     * @param mib
     *            单个或者以逗号隔开的多个mib库文件名
     * 
     * @see #setMibDir(String mibDir)
     * @see #loadMib(String name)
     */
    public void loadMibs(String mib) {
        if (mib == null) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Begin to load mibs:" + mib);
        }
        mib = mib.trim().replaceAll(";", ",");
        mib = mib.replaceAll(" ", ",");
        StringTokenizer tokens = new StringTokenizer(mib, ",");
        while (tokens.hasMoreTokens()) {
            try {
                String s = tokens.nextToken();
                loadMib(s);
            } catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("load mib debug:", ex);
                }
            }
        }
    }

    /**
     * @return the mibLoader
     */
    public MibLoader getMibLoader() {
        return mibLoader;
    }
}
