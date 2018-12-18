package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

@Alias("imageFile")
public class ImageFile implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 2633043652108854216L;

    private Long fileId;
    private Long directoryId;
    private String name;
    private String format;
    private String path;

    public Long getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(Long directoryId) {
        this.directoryId = directoryId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ImageFile{" + "directoryId=" + directoryId + ", fileId=" + fileId + ", name='" + name + '\''
                + ", format='" + format + '\'' + ", path='" + path + '\'' + '}';
    }
}
