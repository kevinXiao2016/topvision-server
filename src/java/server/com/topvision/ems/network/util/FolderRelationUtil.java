package com.topvision.ems.network.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.network.domain.FolderRelation;

/**
 * 解析folder relation工具类
 * 
 * @author w1992wishes
 * @created @2017年10月21日-下午1:48:23
 *
 */
public class FolderRelationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderRelationUtil.class);

    private static final Comparator<FolderRelation> FOLDER_COMPARATOR = new Comparator<FolderRelation>() {
        @Override
        public int compare(FolderRelation o1, FolderRelation o2) {// 降序，优先级高的在前
            int compare = 0;
            if (o1.getLevel() == null || o2.getLevel() == null) {
                return 0;
            }
            if (o1.getLevel() != o2.getLevel()) {
                // level越小，优先级越高
                compare = o1.getLevel().compareTo(o2.getLevel());
            } else {
                if (o1.getSuperiorId() == o2.getSuperiorId()) {
                    Long folderId1 = o1.getFolderId();
                    Long folderId2 = o2.getFolderId();
                    // 同level，同父地域，folderId越小，优先级越高（在页面展示上越靠上）
                    compare = folderId1.compareTo(folderId2);
                } else {
                    // 同level，不同父地域，父地域folderId越小，优先级越高
                    Long superiorId1 = o1.getSuperiorId();
                    Long superiorId2 = o2.getSuperiorId();
                    compare = superiorId1.compareTo(superiorId2);
                }
            }
            return compare;
        }
    };

    public static FolderRelation getHighestPriorityFolder(List<FolderRelation> folderRelations) {
        if (folderRelations == null || folderRelations.size() == 0) {
            return null;
        } else {
            Collections.sort(folderRelations, FOLDER_COMPARATOR);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("the highest priority folder is [{}]--[{}]", folderRelations.get(0).getName(),
                        folderRelations.get(0).getFolderId());
            }
            return folderRelations.get(0);
        }
    }
}
