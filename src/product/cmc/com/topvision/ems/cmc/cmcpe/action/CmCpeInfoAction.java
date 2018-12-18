package com.topvision.ems.cmc.cmcpe.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.cmcpe.domain.CmCpeInfo;
import com.topvision.ems.cmc.cmcpe.service.CmCpeInfoService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author xiaoyue
 * @created @2017年5月20日-上午11:57:09
 *
 */
@Controller("cmCpeInfoAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmCpeInfoAction extends BaseAction {

    private static final long serialVersionUID = 3543500646171797948L;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CmCpeInfoService cmCpeInfoService;
    @Autowired
    private CpeService cpeService;

    private String queryContent;
    private Integer start;
    private Integer limit;

    /**
     * 显示cpe列表
     * 
     * @return
     */
    public String showCmCpeListPage() {

        return SUCCESS;
    }

    /**
     * 1、只支持CM下CPE展示与查询 
     * 2、不显示ONU下CPE展示与查询功能 
     * 3、CPE 列表展示内容依赖于CM CPE轮询，若关闭轮询功能则列表中无显示内容
     * @return
     */
    public String loadCmCpeList() {
        // 判断CM CPE采集是否关闭
        Integer status = cpeService.getCpeCollectConfig().getCpeCollectStatus();
        boolean flag = status > 0 ? true : false;
        JSONObject json = new JSONObject();
        if (!flag) {
            // 封装查询条件
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("start", start);
            queryMap.put("limit", limit);
            queryMap.put("sort", sort);// 排序字段
            queryMap.put("dir", dir);// 排序方向
            if (queryContent != null && !queryContent.equals("")) {
                // mysql中下划线是特殊的，like的时候必须转义
                // sql中使用in操作完全匹配，不能转义
                /*if (queryContent.contains("_")) {
                    queryContent = queryContent.replace("_", "\\_");
                }*/
                queryContent = StringEscapeUtils.escapeSql(queryContent);
                // 分割成字符串数组，去掉元素两端的空格，便于mybatis的in操作
                String[] split = queryContent.split(",");
                List<String> tmp = new ArrayList<String>();
                for (String str : split) {
                    if (str != null && str.length() != 0) {
                        tmp.add(str.trim());
                    }
                }
                queryMap.put("queryContent", tmp);
            }
            List<CmCpeInfo> list = cmCpeInfoService.getCmCpeList(queryMap);
            // 获取符合查询条件的数据总数
            Integer cpeNum = cmCpeInfoService.getCmNum(queryMap);

            json.put("data", list);
            json.put("rowCount", cpeNum);
            writeDataToAjax(json);
        }/* else {
            json.put("data", false);
        }*/
        
        return NONE;
    }

    public int getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
