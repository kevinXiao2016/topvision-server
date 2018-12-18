package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OnuTag;
import com.topvision.ems.epon.onu.service.OnuTagService;
import com.topvision.framework.domain.CommonResponse;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * 日志管理
 * 
 * @author w1992wishes
 * @created @2017年12月21日-下午1:38:38
 *
 */
@Controller("onuTagAction")
public class OnuTagAction extends BaseAction {

    private static final long serialVersionUID = 1890397153906143989L;

    private String pageId;
    private OnuTag onuTag;

    @Autowired
    private OnuTagService onuTagService;

    /**
     * 跳转管理页面
     * 
     * @return
     * @throws IOException
     */
    public String showTagManagementView() throws IOException {
        return SUCCESS;
    }

    /**
     * 加载标签
     * 
     * @return
     */
    public String loadOnuTags() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        List<OnuTag> tags = onuTagService.getOnuTags(queryMap);
        Integer count = onuTagService.getOnuTagsCount();
        CommonResponse cr = new CommonResponse();
        cr.setData(tags);
        cr.setRowCount(count);
        writeDataToAjax(cr);
        return NONE;
    }

    /**
     * 跳转新增标签页面
     * 
     * @return
     */
    public String showOnuTagCreate() {
        return SUCCESS;
    }

    /**
     * 跳转编辑页面
     * 
     * @return
     */
    public String showOnuTagModify() {
        return SUCCESS;
    }

    /**
     * 新增标签
     * 
     * @return
     */
    public String createOnuTag() {
        onuTagService.createOnuTag(onuTag);
        return NONE;
    }

    /**
     * 修改标签
     * 
     * @return
     */
    public String modifyOnuTag() {
        onuTagService.modifyOnuTag(onuTag);
        return NONE;
    }

    /**
     * 删除标签
     * 
     * @return
     */
    public String deleteOnuTag() {
        onuTagService.deleteOnuTag(onuTag);
        return NONE;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public OnuTag getOnuTag() {
        return onuTag;
    }

    public void setOnuTag(OnuTag onuTag) {
        this.onuTag = onuTag;
    }

}
