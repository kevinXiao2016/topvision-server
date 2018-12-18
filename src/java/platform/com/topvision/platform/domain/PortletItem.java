/**
 *
 */
package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

/**
 * @author kelers
 */
@Alias("portletItem")
public class PortletItem extends BaseEntity implements TreeEntity, com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -7426931799750320183L;

    public static final byte PLAIN_TEXT = 0;
    public static final byte RICH_TEXT = 1;
    public static final byte IMG = 2;
    public static final byte IFRAME = 3;
    public static final byte GRID = 4;

    private long userId;
    private long itemId;
    private long categoryId;
    private String name;

    private String url = "../images/security48x48.gif";

    private int gridX = 100;

    private int gridY = 100;

    private String param;

    private boolean refreshable;

    private int refreshInterval = 5 * 60 * 1000;

    private boolean closable;

    private boolean settingable;

    private byte type = IMG;

    private String loadingText = null;

    private String icon = null;

    public long getCategoryId() {
        return categoryId;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public String getIcon() {
        return icon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getId()
     */
    @Override
    public String getId() {
        return String.valueOf(itemId);
    }

    public long getItemId() {
        return itemId;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public String getName() {
        return name;
    }

    public String getParam() {
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getParentId()
     */
    @Override
    public String getParentId() {
        return String.valueOf(categoryId);
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getText()
     */
    @Override
    public String getText() {
        return name;
    }

    public byte getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isClosable() {
        return closable;
    }

    public boolean isRefreshable() {
        return refreshable;
    }

    public boolean isSettingable() {
        return settingable;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void setSettingable(boolean settingable) {
        this.settingable = settingable;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PortletItem [userId=");
		builder.append(userId);
		builder.append(", itemId=");
		builder.append(itemId);
		builder.append(", categoryId=");
		builder.append(categoryId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", url=");
		builder.append(url);
		builder.append(", gridX=");
		builder.append(gridX);
		builder.append(", gridY=");
		builder.append(gridY);
		builder.append(", param=");
		builder.append(param);
		builder.append(", refreshable=");
		builder.append(refreshable);
		builder.append(", refreshInterval=");
		builder.append(refreshInterval);
		builder.append(", closable=");
		builder.append(closable);
		builder.append(", settingable=");
		builder.append(settingable);
		builder.append(", type=");
		builder.append(type);
		builder.append(", loadingText=");
		builder.append(loadingText);
		builder.append(", icon=");
		builder.append(icon);
		builder.append("]");
		return builder.toString();
	}

}