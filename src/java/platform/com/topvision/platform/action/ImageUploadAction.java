package com.topvision.platform.action;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.common.ZipUtils;
import com.topvision.framework.web.struts2.FileUploadAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.ImageDirectory;
import com.topvision.platform.domain.ImageFile;
import com.topvision.platform.service.ImageService;

@Controller("imageUploadAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImageUploadAction extends FileUploadAction {
    private static final long serialVersionUID = -8465524736395688989L;
    public static String IMAGE_PATH = "/images/";
    public static String UPLOAD_PATH = "/images/upload/";
    public static String TEMP_PATH = "/images/tempfile/";
    private String module;
    private String directory = "";
    private long directoryId;
    private boolean uploadZip = false;
    @Autowired
    private ImageService imageService;

    /**
     * 上传图片
     * 
     * @return
     * @throws IOException
     */
    public String uploadImage() throws IOException {
        if (module == null || "".equals(module)) {
            module = "icon";
        }
        if (getUpload() != null) {
            String name = getUploadFileName();
            if (name.toLowerCase().endsWith("zip") || name.toLowerCase().endsWith("jar")) {
                String tempdir = SystemConstants.ROOT_REAL_PATH + TEMP_PATH;
                ZipUtils.unzip(getUpload().getAbsolutePath(), tempdir);
                // 解压缩入库
                int index = name.lastIndexOf(".");
                if (index != -1) {
                    name = name.substring(0, index);
                }
                File file = new File(tempdir, name);
                visit(directoryId, file);
                file.delete();
                uploadZip = true;
            } else {
                // 对单个文件入库
                ImageFile file = new ImageFile();
                file.setDirectoryId(directoryId);
                int index = name.lastIndexOf(".");
                if (index != -1) {
                    file.setName(name.substring(0, index));
                    file.setFormat(name.substring(index + 1));
                } else {
                    file.setName(name);
                    file.setFormat("gif");
                }
                imageService.insertImageFile(file);
                File newFile = new File(SystemConstants.ROOT_REAL_PATH + UPLOAD_PATH + directory, String.valueOf(file
                        .getFileId()) + "." + file.getFormat());
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                getUpload().renameTo(newFile);
                file.setPath(UPLOAD_PATH + directory + "/" + String.valueOf(file.getFileId()) + "." + file.getFormat());
                imageService.updateImagePath(file);
            }
        }
        return SUCCESS;
    }

    private void visit(long superiorId, File target) {
        ImageDirectory directory = new ImageDirectory();
        directory.setModule(module);
        directory.setName(target.getName());
        directory.setSuperiorId(superiorId);
        directory.setPath("upload/" + target.getName());
        imageService.insertImageDirectory(directory);
        File dir = new File(SystemConstants.ROOT_REAL_PATH + IMAGE_PATH + directory.getPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] list = target.listFiles();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    // visit(directory.getDirectoryId(), list[i], basePath);
                } else {
                    ImageFile file = new ImageFile();
                    file.setDirectoryId(directory.getDirectoryId());

                    String name = list[i].getName();
                    int index = name.lastIndexOf(".");
                    if (index != -1) {
                        file.setName(name.substring(0, index));
                        file.setFormat(name.substring(index + 1));
                    } else {
                        file.setName(name);
                        file.setFormat("gif");
                    }
                    imageService.insertImageFile(file);
                    list[i].renameTo(new File(dir, String.valueOf(file.getFileId()) + "." + file.getFormat()));
                    file.setPath(file.getName());
                    imageService.updateImagePath(file);
                }
            }
        }
    }

    public String getDirectory() {
        return directory;
    }

    public long getDirectoryId() {
        return directoryId;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public String getModule() {
        return module;
    }

    public boolean isUploadZip() {
        return uploadZip;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setDirectoryId(long directoryId) {
        this.directoryId = directoryId;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setUploadZip(boolean uploadZip) {
        this.uploadZip = uploadZip;
    }
}
