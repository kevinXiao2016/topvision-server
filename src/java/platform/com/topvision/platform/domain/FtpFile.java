package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/** FTP文件主要信息domain类
 * @version 1.0 2013-01-08 
 * @author  vanzand
 */
public class FtpFile extends BaseEntity{

    private static final long serialVersionUID = 8926492485419284542L;
    private final int FILE_TYPE = 0;
    private final int FOLDER_TYPE = 1;
    
    //文件名称
    private String name;
    //文件路径名称（不包括文件名）
    private String pathName;
    //文件类型（0是文件，1是文件夹）
    private int type;
    //文件大小
    private long size;
    //文件更新时间
    private String updateTime;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPathName() {
        return pathName;
    }
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public int getFILE_TYPE() {
        return FILE_TYPE;
    }
    public int getFOLDER_TYPE() {
        return FOLDER_TYPE;
    }
     
}
