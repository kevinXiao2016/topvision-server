/**
 *
 */
package com.topvision.ems.portal.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.domain.TreeEntity;
import com.topvision.framework.web.dhtmlx.DefaultDhtmlxHandler;
import com.topvision.framework.web.dhtmlx.DhtmlxTreeOutputter;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.PortletCategory;
import com.topvision.platform.domain.PortletItem;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.PortletService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.service.UserService;

/**
 * @author niejun
 */
@Controller("portalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PortalAction extends BaseAction {
    private static final long serialVersionUID = -5462108621709964008L;
    private static Logger logger = LoggerFactory.getLogger(PortalAction.class);
    @Autowired
    private UserService userService = null;
    @Autowired
    private PortletService portletService = null;
    @Autowired
    private UserPreferencesService userPreferencesService;

    private List<PortletItem> portletItems = null;
    private PortletItem portletItem = null;
    private long column = 2;
    private long itemId;
    private int row;
    private int col;
    private List<Long> itemIds;
    private String module;
    private boolean refresh;
    // 用于保存用户算定义的桌面视图
    private String desktopLeft;
    private String desktopRight;

    public String loadPortletItemsByUserId() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        final ResourceManager resourceManager = ResourceManager
                .getResourceManager("com.topvision.ems.resources.resources");
        try {
            List<PortletCategory> categorys = portletService.getPortletCategory();
            List<PortletItem> items = portletService.getPortletItem();
            // 不支持epon模块的时候，去掉pon口流量排名和sni口流量排名统计
            if (!uc.hasSupportModule("olt")) {
                for (PortletItem portletItem : items) {
                    if (portletItem.getName().equalsIgnoreCase("PortletCategory.getTopSniLoading")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.getTopPonLoading")) {
                        items.remove(portletItem);
                    }
                }
            }
            if (!uc.hasSupportModule("cmc")) {
                for (PortletItem portletItem : items) {
                    if (portletItem.getName().equalsIgnoreCase("PortletCategory.getTopLowNoise")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.channelUsed")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.berRate")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.cmFlapIns")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.cmcOptical")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.getTopCcUsers")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.getTopUpChnUsers")
                            || portletItem.getName().equalsIgnoreCase("PortletCategory.getTopDownChnUsers")) {
                        items.remove(portletItem);
                    }
                }
            }
            List<TreeEntity> temp = new ArrayList<TreeEntity>();
            temp.addAll(categorys);
            temp.addAll(items);

            List<PortletItem> myItems = portletService.getPortletItem(uc.getUserId());
            final Set<String> hashes = new HashSet<String>();
            int size = myItems == null ? 0 : myItems.size();
            for (int i = 0; i < size; i++) {
                hashes.add(String.valueOf(myItems.get(i).getItemId()));
            }
            DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(temp) {
                @Override
                public Element buildElement(Object obj) {
                    TreeEntity item = (TreeEntity) obj;
                    String id = item.getId();
                    Element el = new DefaultElement("item");
                    el.addAttribute("id", id);
                    el.addAttribute("text", resourceManager.getString(item.getText()));
                    el.addAttribute("open", "1");
                    el.addAttribute("im0", "portlet.gif");
                    el.addAttribute("im1", "portlet.gif");
                    el.addAttribute("im2", "portlet.gif");
                    if (hashes.contains(id)) {
                        el.addAttribute("checked", "1");
                    }
                    return el;
                }
            };
            HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
            DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        } catch (Exception ex) {
            logger.error("Load PortletItem By userId:", ex);
            throw ex;
        }
        return NONE;
    }

    public String modifyMyDesktop() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        try {
            UserPreferences p = new UserPreferences();
            p.setUserId(uc.getUserId());
            p.setName("column");
            p.setModule("mydesktop");
            p.setValue(String.valueOf(column));
            if (uc.getUserPreferencesMap().containsKey("mydesktop.column")) {
                userService.updatePreferences(p);
            } else {
                userService.insertPreferences(p);
            }
            portletService.savePortletItem(uc.getUserId(), itemIds);
            uc.getUserPreferencesMap().put("mydesktop.column", String.valueOf(column));
        } catch (Exception ex) {
            logger.error("Modify My Desktop.", ex);
        }
        return NONE;
    }

    public String removePortletItem() throws Exception {
        return NONE;
    }

    public String showMydesktop() throws Exception {
        try {
            UserContext uc = (UserContext) getSession().get(UserContext.KEY);
            ResourceManager resourceManager = ResourceManager
                    .getResourceManager("com.topvision.ems.resources.resources");
            column = uc.getUserPreferencesMap().getInt("mydesktop.column", 2);
            portletItems = portletService.getPortletItem(uc.getUserId());
            for (int i = 0; i < portletItems.size(); i++) {
                portletItems.get(i).setName(resourceManager.getString(portletItems.get(i).getName()));
            }
            // 获取用户保存的自定义桌面视图
            UserPreferences userPre = new UserPreferences();
            userPre.setModule("myDesktopView");
            userPre.setUserId(uc.getUserId());
            Properties desktopView = userPreferencesService.getModulePreferences(userPre);
            desktopLeft = desktopView.getProperty("desktopLeft");
            desktopRight = desktopView.getProperty("desktopRight");
        } catch (Exception ex) {
            logger.error("Show my desktop.", ex);
        }
        return SUCCESS;
    }

    public String showPopPortletItems() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        column = uc.getUserPreferencesMap().getInt("mydesktop.column", 2);
        return SUCCESS;
    }

    public String updatePortletPosition() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        try {
            PortletItem portletItem = new PortletItem();
            portletItem.setGridX(row);
            portletItem.setGridY(col);
            portletItem.setUserId(uc.getUserId());
            portletItem.setItemId(itemId);
            portletService.updatePortletItem(portletItem);
        } catch (Exception ex) {
            logger.error("Update portlet position.", ex);
        }
        return NONE;
    }

    /**
     * 保存用户设置的桌面视图
     * 
     * @author flackyang
     * @since 2013-11-09
     * @return
     */
    public String saveDesktopView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties desktopView = new Properties();
        desktopView.setProperty("desktopLeft", desktopLeft);
        desktopView.setProperty("desktopRight", desktopRight);
        userPreferencesService.batchSaveModulePreferences("myDesktopView", uc.getUserId(), desktopView);
        return NONE;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setColumn(long column) {
        this.column = column;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setPortletItem(PortletItem portletItem) {
        this.portletItem = portletItem;
    }

    public void setPortletItems(List<PortletItem> portletItems) {
        this.portletItems = portletItems;
    }

    public void setPortletService(PortletService portletService) {
        this.portletService = portletService;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String showDashboard() throws Exception {
        return SUCCESS;
    }

    public int getCol() {
        return col;
    }

    public long getColumn() {
        return column;
    }

    public long getItemId() {
        return itemId;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public String getModule() {
        return module;
    }

    public PortletItem getPortletItem() {
        return portletItem;
    }

    public List<PortletItem> getPortletItems() {
        return portletItems;
    }

    public int getRow() {
        return row;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public String getDesktopLeft() {
        return desktopLeft;
    }

    public void setDesktopLeft(String desktopLeft) {
        this.desktopLeft = desktopLeft;
    }

    public String getDesktopRight() {
        return desktopRight;
    }

    public void setDesktopRight(String desktopRight) {
        this.desktopRight = desktopRight;
    }

}
