/***********************************************************************
 * $ LocalData.java,v1.0 2012-5-2 11:27:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author jay
 * @created @2012-5-2-11:27:46
 */
public class LocalFileData<T extends FileData> {
    private static Logger logger = LoggerFactory.getLogger(LocalFileData.class);
    private static ConcurrentNavigableMap<String, LocalFileData<PerformanceResult<OperClass>>> instance = new ConcurrentSkipListMap<String, LocalFileData<PerformanceResult<OperClass>>>();

    private List<String> fileNames = new ArrayList<String>();
    private static String path;
    private String name;

    static {
        try {
            init();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public static LocalFileData<PerformanceResult<OperClass>> createLocalFileData(String name) throws IOException {
        if (!instance.containsKey(name)) {
            LocalFileData<PerformanceResult<OperClass>> lfd = new LocalFileData<PerformanceResult<OperClass>>(name);
            instance.put(name, lfd);
        }
        return instance.get(name);
    }

    private LocalFileData(String name) throws IOException {
        this.name = name;
        File dir = new File(path + name);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void add(T t) throws IOException {
        synchronized (fileNames) {
            writeObject(t);
            fileNames.add(t.getFileName());
        }
    }

    public T first() throws ClassNotFoundException, IOException {
        T re = null;
        synchronized (fileNames) {
            if (fileNames.size() > 0) {
                String filename = getFilename();
                try {
                    re = readObject(filename);
                } catch (Exception e) {
                    logger.error(filename, e);
                    fileNames.remove(0);
                }
            }
        }
        return re;
    }

    public T takeFirst() throws ClassNotFoundException, IOException {
        T re = null;
        while (re == null) {
            synchronized (fileNames) {
                if (fileNames.size() > 0) {
                    String filename = getFilename();
                    try {
                        re = readObject(filename);
                    } catch (Exception e) {
                        logger.error(filename, e);
                        fileNames.remove(0);
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return re;
    }

    public long length() {
        synchronized (fileNames) {
            return fileNames.size();
        }
    }

    public void remove() {
        synchronized (fileNames) {
            String filename = fileNames.remove(0);
            File file = new File(getFilePath(filename));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public String getFilename() {
        return getFilePath(fileNames.get(0));
    }

    private static void init() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("./cache/");
        path = sb.toString();
        File root = new File(sb.toString());
        if (!root.exists()) {
            root.mkdir();
        }
        File[] dirs = root.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File dir : dirs) {
            File[] fileList = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isFile(); // To change body of implemented methods use File |
                                              // Settings | File Templates.
                }
            });
            LocalFileData<PerformanceResult<OperClass>> lfd = new LocalFileData<PerformanceResult<OperClass>>(
                    dir.getName());
            instance.put(dir.getName(), lfd);
            List<String> fns = new ArrayList<String>();
            for (File aFileList : fileList) {
                fns.add(aFileList.getName());
            }
            lfd.setFileNames(fns);
        }
    }

    private void writeObject(T v) throws IOException {
        FileOutputStream fos = new FileOutputStream(getFilePath(v.getFileName()));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(v);
        oos.close();
    }

    private T readObject(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        T v = (T) ois.readObject();
        ois.close();
        return v;
    }

    private String getFilePath(String filename) {
        return path + name + "/" + filename;
    }

    public static ConcurrentNavigableMap<String, LocalFileData<PerformanceResult<OperClass>>> getAllInstance() {
        return instance;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
