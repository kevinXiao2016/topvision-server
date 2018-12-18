package com.topvision.ems.congfigbackup.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.common.FileUtils;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.util.StringUtil;

/**
 * 配置备份工具类
 * @author Bravin
 * @created @2016年5月21日-上午10:39:50
 *
 */
public class ConfigBackupUtil {
    public static final DateFormat DIRECTORY_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
    private static final String STRING_TEMPLATE_NEWFILE = "{0}META-INF{1}startConfig{1}{2}{1}{3}";

    /**
     * 构建任务执行的表达式
     * @param autoWriteTime
     * @return
     */
    public static String bulidCronExpression(String autoWriteTime) {
        String[] split = autoWriteTime.split("_");
        String datetime = split[0];
        String weekdays = split[1];
        String[] time = datetime.split(":");
        String hour = time[0];
        String min = time[1];
        StringBuilder sb = new StringBuilder();
        sb.append("0 ").append(min).append(" ").append(hour).append(" ? * ").append(weekdays);
        return sb.toString();
    }

    /**
     * 统一获取startconfig下的文件目录
     * @param entityId
     * @param fileName
     * @return
     */
    private static File formatFile(long entityId, String fileName) {
        String path = StringUtil.format(STRING_TEMPLATE_NEWFILE, SystemConstants.ROOT_REAL_PATH, File.separator,
                entityId, fileName);
        return new File(path);
    }

    /**
     * 获取设备的日期目录
     * 
     * @param entityId
     * @return
     */
    public static String getDirectory(long entityId) {
        String dateDirectory = DIRECTORY_FORMAT.format(new Date());
        File thisDirectory = formatFile(entityId, dateDirectory);
        // 如果当前目录存在并是一个目录,则直接往该目录内写文件，如果没有该目录，则新建一个目录，并写文件
        if (!thisDirectory.exists()) {
            thisDirectory.mkdirs();
        }
        return thisDirectory.getAbsolutePath() + File.separator;
    }

    /**
     * 设置文件为最新的 首先需要清空最新文件的内容，再将当前内容写到最新文件夹中 最新文件 -- 一个文件名，放在entity目录下,名字都叫 startcon.cfg
     * 
     * @param file
     * @param entityId
     * @param configFileName 
     * @throws IOException 
     */
    public static void setFileNewest(File file, long entityId, String configFileName) throws IOException {
        File newFile = formatFile(entityId, configFileName);
        if (file.exists()) {
            FileUtils.copy(file, newFile);
        } else {
            if (newFile.exists()) {
                newFile.delete();
            }
        }
    }

    /**
     * 删除一个文件夹以及目录下的所有文件
     * 
     * @param fPath
     * @return
     */
    public static boolean deleteDirectory(File file) {
        boolean flag = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File $file : files) {
                if ($file.isDirectory()) {
                    if (!deleteDirectory($file)) {
                        flag = false;
                    }
                } else if ($file.exists() && $file.isFile()) {
                    if (!$file.delete()) {
                        flag = false;
                    }
                }
            }
            if (!file.delete()) {
                flag = false;
            }
        } else if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 删除一个文件夹以及目录下的所有文件
     * 
     * @param path
     * @return
     */
    public static boolean clearDirectory(String path) {
        File $directory = new File(path);
        if ($directory.isDirectory()) {
            File[] files = $directory.listFiles();
            for (File $file : files) {
                if ($file.isDirectory()) {
                    if (!deleteDirectory($file)) {
                        return false;
                    }
                } else if ($file.exists() && $file.isFile()) {
                    if (!$file.delete()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 删除tftp下CC配置备份文件
     * 
     * @param path
     * @return
     */
    public static boolean clearCmtsBackUpFile(String path, String cc_config, String cm_config, String eqam_config) {
        File $directory = new File(path);
        if ($directory.isDirectory()) {
            File[] files = $directory.listFiles();
            for (File $file : files) {
                if ($file.exists()
                        && $file.isFile()
                        && (cc_config.equals($file.getName()) || cm_config.equals($file.getName()) || eqam_config
                                .equals($file.getName()))) {
                    if (!$file.delete()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 对比当前文件是最新的
     * @param fileName 
     * 
     * @param originFile
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static boolean compareFile(File currentFile, long entityId, String fileName) throws IOException {
        File lastedFile = formatFile(entityId, fileName);
        return compareFile(lastedFile, currentFile);
    }

    /**
     * @param lastedFile
     * @param currentFile
     * @return true:需要备份, false:不需要备份
     * @throws IOException 
     */
    public static boolean compareFile(File lastedFile, File currentFile) throws IOException {
        if (!currentFile.exists()) {
            return false;
        }
        if (!lastedFile.exists()) {
            return true;
        }
        BufferedReader latestReader = new BufferedReader(new FileReader(lastedFile));
        BufferedReader newestReader = new BufferedReader(new FileReader(currentFile));
        StringBuffer latestText = new StringBuffer();
        StringBuffer newestText = new StringBuffer();
        String cursor;
        try {
            while ((cursor = latestReader.readLine()) != null) {
                if (cursor.indexOf("! system datetime:") == -1) {
                    latestText.append(cursor + "\n");
                }
            }
            while ((cursor = newestReader.readLine()) != null) {
                if (cursor.indexOf("! system datetime:") == -1) {
                    newestText.append(cursor + "\n");
                }
            }
            String text1 = latestText.toString();
            String text2 = newestText.toString();
            return !text1.equals(text2);
        } catch (Exception e) {
            return true;
        } finally {
            latestReader.close();
            newestReader.close();
        }
    }
}
