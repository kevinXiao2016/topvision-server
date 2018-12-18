package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.FavouriteFolder;
import com.topvision.platform.domain.FavouriteTreeNode;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;

@Controller("favouriteFolderAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FavouriteFolderAction extends BaseAction {
    private static final long serialVersionUID = -7695898494488874613L;

    private boolean hasFavouriteLink = true;
    private long folderId;
    private String name;
    private String folderPath;
    private FavouriteFolder favouriteFolder = null;
    @Autowired
    private UserService userService = null;

    /**
     * 新建收藏夹
     * 
     * @return
     */
    public String createFavouriteFolder() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        favouriteFolder.setUserId(uc.getUserId());
        favouriteFolder.setType(FavouriteFolder.FOLDER);
        userService.insertFavouriteFolder(favouriteFolder);
        return NONE;
    }

    /**
     * 新建链接
     * 
     * @return
     */
    public String createFavouriteLink() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        favouriteFolder.setUserId(uc.getUserId());
        favouriteFolder.setType(FavouriteFolder.LINK);
        userService.insertFavouriteFolder(favouriteFolder);
        return NONE;
    }

    /**
     * 删除收藏夹
     * 
     * @return
     */
    public String deleteFavouriteFolder() {
        userService.deleteFavouriteFolder(folderPath);
        return NONE;
    }

    /**
     * 载入收藏夹
     * 
     * @return
     * @throws Exception
     */
    public String loadFavouriteFolder() throws Exception {
        //JSONArray array = new JSONArray();
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>();
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<FavouriteFolder> list = userService.loadFavouriteFolder(uc.getUserId());
        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            FavouriteFolder folder = list.get(i);
            if (folder.getType() == FavouriteFolder.LINK && !hasFavouriteLink) {
                continue;
            }
            FavouriteTreeNode treeNode = new FavouriteTreeNode();
            treeNode.setText(folder.getName());
            //treeNode.put("text", folder.getName());
            treeNode.setExpanded(true);
            //treeNode.put("expanded", true);
            treeNode.setId(folder.getFolderId());
            //treeNode.put("id", folder.getFolderId());
            treeNode.setChildren(new ArrayList<TreeNode>());
            //treeNode.put("children", new JSONArray());
            treeNode.setType(Integer.valueOf(folder.getType()));
            //treeNode.put("type", folder.getType());
            treeNode.setShare(folder.getShared());
            //treeNode.put("share", folder.getShared());
            treeNode.setUserId(folder.getUserId());
            //treeNode.put("userId", folder.getUserId());
            treeNode.setFolderPath(folder.getPath());
            //treeNode.put("folderPath", folder.getPath());
            if (folder.getType() == FavouriteFolder.FOLDER) {
                treeNode.setIconCls("favouriteFolderIcon");
                //treeNode.put("iconCls", "favouriteFolderIcon");
            } else {
                treeNode.setIconCls("favouriteLinkIcon");
                //treeNode.put("iconCls", "favouriteLinkIcon");
                treeNode.setUrl(folder.getUrl());
                //treeNode.put("url", folder.getUrl());
                treeNode.setIsTarget(false);
                //treeNode.put("isTarget", false);
            }
            map.put(String.valueOf(folder.getFolderId()), treeNode);
            TreeNode parent = map.get(String.valueOf(folder.getSuperiorId()));
            if (parent == null) {
                treeNodeList.add(treeNode);
            } else {
                parent.getChildren().add(treeNode);
            }
        }
        writeDataToAjax(treeNodeList);
        return NONE;
    }

    /**
     * 移动收藏夹
     * 
     * @return
     */
    public String moveFavouriteFolder() {
        userService.txMoveFavouriteFolder(favouriteFolder);
        return NONE;
    }

    /**
     * 重命名文件夹
     * 
     * @return
     */
    public String renameFavouriteFolder() {
        favouriteFolder = new FavouriteFolder();
        favouriteFolder.setFolderId(folderId);
        favouriteFolder.setName(name);
        userService.txRenameFavouriteFolder(favouriteFolder);
        return NONE;
    }

    /**
     * 显示文件夹
     * 
     * @return
     */
    public String showFavouriteFolder() {
        favouriteFolder = userService.getFavouriteFolder(folderId);
        if (favouriteFolder.getType() == FavouriteFolder.FOLDER) {
            return "folder";
        } else {
            return "link";
        }
    }

    /**
     * 修改文件夹
     * 
     * @return
     */
    public String updateFavouriteFolder() {
        userService.updateFavouriteFolder(favouriteFolder);
        return NONE;
    }

    public String newFavouriteLinkJsp() {
        return SUCCESS;
    }

    public FavouriteFolder getFavouriteFolder() {
        return favouriteFolder;
    }

    public long getFolderId() {
        return folderId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getName() {
        return name;
    }

    public boolean isHasFavouriteLink() {
        return hasFavouriteLink;
    }

    public void setFavouriteFolder(FavouriteFolder favouriteFolder) {
        this.favouriteFolder = favouriteFolder;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setHasFavouriteLink(boolean hasFavouriteLink) {
        this.hasFavouriteLink = hasFavouriteLink;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
