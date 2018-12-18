/**
 *
 */
package com.topvision.platform.dao;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.FavouriteFolder;

/**
 * @author niejun
 */
public interface FavouriteFolderDao extends BaseEntityDao<FavouriteFolder> {
    /**
     * 删除收藏夹
     * 
     * @param folderPath
     */
    void deleteFavouriteFolder(String folderPath);

    /**
     * 移动收藏夹
     * 
     * @param folder
     */
    void moveFavouriteFolder(FavouriteFolder folder);

    /**
     * 给收藏夹改名
     * 
     * @param folder
     */
    void renameFavouriteFolder(FavouriteFolder folder);

}
