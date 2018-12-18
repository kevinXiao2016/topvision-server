package com.topvision.platform.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.ImageDirectory;
import com.topvision.platform.domain.ImageFile;
import com.topvision.platform.domain.ImageItem;
import com.topvision.platform.service.ImageService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("imageChooserAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImageChooserAction extends BaseAction {
    private static final long serialVersionUID = -3883001504054354253L;

    public static String RESOURCES_IMG_PATH = "/images/";
    private List<ImageItem> paths = null;
    private List<ImageDirectory> imageDirectories;
    private String location;
    private String path;
    private String module;
    private String directory;
    private long directoryId;
    @Autowired
    private ImageService imageService;

    /**
     * 删除图片
     * 
     * @return
     */
    public String deleteImage() {
        File file = new File(SystemConstants.ROOT_REAL_PATH + "/" + path);
        if (file.exists()) {
            file.delete();
        }
        return NONE;
    }

    /**
     * 载入图片目录
     * 
     * @return
     * @throws Exception
     */
    public String loadImageDirectory() throws Exception {
        JSONArray array = new JSONArray();
        if (module == null || "".equals(module) || "network".equals(module)) {
            imageDirectories = imageService.getAllImageDirectory();
        } else if ("background".equals(module)) {
            imageDirectories = imageService.getImageDirectory(module);
        } else {
            imageDirectories = imageService.getImageDirectory("default");
            imageDirectories.addAll(imageService.getImageDirectory(module));
        }
        ImageDirectory dir = null;
        HashMap<Long, JSONObject> map = new HashMap<Long, JSONObject>();
        JSONObject json = new JSONObject();
        JSONObject parent = null;
        for (int i = 0; i < imageDirectories.size(); i++) {
            dir = imageDirectories.get(i);
            json = new JSONObject();
            json.put("id", dir.getDirectoryId());
            json.put("text", getResourceManager().getString(dir.getName()));
            json.put("expanded", true);
            json.put("children", new JSONArray());
            json.put("iconCls", "directoryIcon");
            json.put("path", dir.getPath());
            json.put("module", dir.getModule());
            map.put(dir.getDirectoryId(), json);
            parent = map.get(dir.getSuperiorId());
            if (parent == null) {
                array.add(json);
            } else {
                parent.getJSONArray("children").add(json);
            }
        }
        array.write(response.getWriter());
        return NONE;
    }

    /**
     * 载入图片
     * 
     * @return
     * @throws Exception
     */
    public String loadImages() throws Exception {
        String root = ".." + RESOURCES_IMG_PATH + directory + "/";
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        File dir = new File(SystemConstants.ROOT_REAL_PATH + RESOURCES_IMG_PATH + directory);
        File[] files = dir.listFiles();
        JSONObject temp = null;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && !files[i].isHidden()) {
                    temp = new JSONObject();
                    temp.put("name", files[i].getName());
                    temp.put("url", root + files[i].getName());
                    array.add(temp);
                }
            }
        }
        List<ImageFile> images = imageService.getImageFiles(directoryId);
        int size = images.size();
        if (size > 0) {
            ImageFile file;
            for (int i = 0; i < size; i++) {
                file = images.get(i);
                temp = new JSONObject();
                temp.put("name", file.getName());
                temp.put("url", ".." + file.getPath());
                // temp.put("size", 0);
                // temp.put("date", 0);
                array.add(temp);
            }
        }
        json.put("images", array);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示选择图片
     * 
     * @return
     */
    public String showImageChooser() {
        return SUCCESS;
    }

    public String getDirectory() {
        return directory;
    }

    public long getDirectoryId() {
        return directoryId;
    }

    public List<ImageDirectory> getImageDirectories() {
        return imageDirectories;
    }

    public String getLocation() {
        return location;
    }

    public String getModule() {
        return module;
    }

    public List<ImageItem> getPaths() {
        return paths;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setDirectoryId(long directoryId) {
        this.directoryId = directoryId;
    }

    public void setImageDirectories(List<ImageDirectory> imageDirectories) {
        this.imageDirectories = imageDirectories;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setPaths(List<ImageItem> paths) {
        this.paths = paths;
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }
}
