package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.FavouriteFolderDao;
import com.topvision.platform.domain.FavouriteFolder;

@Repository("favouriteFolderDao")
public class FavouriteFolderDaoImpl extends MyBatisDaoSupport<FavouriteFolder> implements FavouriteFolderDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return FavouriteFolder.class.getName();
    }

    /**
     * 通过目录ID获得目录路径
     * 
     * @param folderId
     * @return
     */
    public String getFolderPath(Long folderId) {
        return ((String) getSqlSession().selectOne(getNameSpace("getFolderPath"), folderId));
    }

    /**
     * 更新目录路径
     * 
     * @param folder
     */
    public void updateFolderPath(FavouriteFolder folder) {
        getSqlSession().update(getNameSpace("updateFolderPath"), folder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.FavouriteFolderDao#deleteFavouriteFolder(java .lang.String)
     */
    @Override
    public void deleteFavouriteFolder(String folderPath) {
        getSqlSession().delete(getNameSpace("deleteByPath"), folderPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#insertEntity(java .lang.Object)
     */
    @Override
    public void insertEntity(FavouriteFolder folder) {
        super.insertEntity(folder);
        // update folder tree path
        String path = getFolderPath(folder.getSuperiorId());
        if (path == null) {
            path = "";
        }
        folder.setPath(path + folder.getFolderId() + "/");
        updateFolderPath(folder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.FavouriteFolderDao#moveFavouriteFolder(com
     * .topvision.platform.domain.FavouriteFolder)
     */
    @Override
    public void moveFavouriteFolder(FavouriteFolder folder) {
        getSqlSession().update(getNameSpace("moveFavouriteFolder"), folder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.FavouriteFolderDao#renameFavouriteFolder(com
     * .topvision.platform.domain.FavouriteFolder)
     */
    @Override
    public void renameFavouriteFolder(FavouriteFolder folder) {
        getSqlSession().update(getNameSpace("renameFavouriteFolder"), folder);
    }

}
