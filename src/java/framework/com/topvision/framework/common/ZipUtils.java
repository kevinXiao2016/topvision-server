/**
 * 
 */
package com.topvision.framework.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 为了避免中文乱码, 使用ANT实现ZIP.
 * 
 * @author kelers
 */
public class ZipUtils {
    private static Logger logger = LoggerFactory.getLogger("ZipUtils");
    private final static int BUFFER_SIZE = 1024;

    /**
     * 解压缩。
     * 
     * @param zipFileName
     *            源文件
     * @param outputDirectory
     *            解压缩后文件存放的目录
     * @throws IOException
     */
    public static void unzip(String zipFileName, String outputDirectory) throws IOException {
        ZipUtils.unzip(zipFileName, outputDirectory, null);
    }

    /**
     * 
     * @param zipFileName
     *            源文件
     * @param outputDirectory
     *            解压缩后文件存放的目录
     * @param encoding
     *            编码类型
     * @throws IOException
     */
    public static void unzip(String zipFileName, String outputDirectory, String encoding) throws IOException {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFileName, encoding);
            Enumeration<ZipEntry> e = zipFile.getEntries();
            ZipEntry zipEntry = null;
            File dest = new File(outputDirectory);
            if (!dest.exists()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(dest + ":" + dest.mkdirs());
                }
            }

            while (e.hasMoreElements()) {
                zipEntry = e.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = entryName.substring(0, entryName.length() - 1);
                        File f = new File(outputDirectory + File.separator + name);
                        if (!f.exists()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(f + ":" + f.mkdirs());
                            }
                        }
                    } else {
                        File f = new File(outputDirectory + File.separator + zipEntry.getName());
                        if (!f.getParentFile().exists()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(f.getParentFile() + ":" + f.getParentFile().mkdirs());
                            }
                        }
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e1) {
                        }

                        int c;
                        byte[] by = new byte[BUFFER_SIZE];
                        StringBuilder sb = new StringBuilder();
                        while ((c = in.read(by)) != -1) {
                            sb.append(c).append(".");
                            out.write(by, 0, c);
                        }
                        if (logger.isDebugEnabled()) {
                            logger.debug(f + ":" + sb);
                        }
                        out.flush();
                    }
                } finally {
                    FileUtils.closeQuitely(in);
                    FileUtils.closeQuitely(out);
                }
            }
        } finally {
            if (zipFile != null) {
                ZipFile.closeQuietly(zipFile);
            }
        }
    }

    /**
     * 压缩。
     * 
     * @param src
     *            源文件或者目录
     * @param dest
     *            压缩文件路径
     * @throws IOException
     */
    public static void zip(String src, String dest) throws IOException {
        ZipOutputStream out = null;
        try {
            File outFile = new File(dest);
            out = new ZipOutputStream(outFile);
            File fileOrDirctory = new File(src);
            zipFileOrDirectory(out, fileOrDirctory, "");
        } finally {
            FileUtils.closeQuitely(out);
        }
    }

    /**
     * 递归压缩文件或目录
     * 
     * @param out
     *            压缩输出流对象
     * @param fileOrDirectory
     *            要压缩的文件或目录对象
     * @param curPath
     *            当前压缩条目的路径，用于指定条目名称的前缀
     * @throws IOException
     */
    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)
            throws IOException {
        FileInputStream in = null;
        try {
            if (fileOrDirectory.isDirectory()) {
                // 压缩目录
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + File.separator);
                }
            } else {
                // 压缩文件
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            FileUtils.closeQuitely(in);
        }
    }

}
