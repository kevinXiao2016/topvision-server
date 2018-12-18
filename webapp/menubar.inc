<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.topvision.platform.domain.MenuItem"%>
<jsp:useBean id="menuItems" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="resourceManager" scope="request" class="com.topvision.platform.ResourceManager" />
var myMenu = [];
var vArr = null;
<%	
	boolean alertFilterPower = uc.hasPower("alertFilter");
	boolean alertStrategyPower = uc.hasPower("alertStrategy");
	boolean sysInfoManagementPower = uc.hasPower("sysInfoManagement");
    boolean licenseManagementPower = uc.hasPower("licenseManagement");
    boolean myDeskTopPower = uc.hasPower("myDeskTop");
    boolean systemPower = uc.hasPower("system");
	MenuItem menuItem = null;
	MenuItem childItem = null;
	String mnemonic = null;
	String icon = null;
	int k = 0;
	List<MenuItem> children = null;
	for (int i = 0; i < menuItems.size(); i++) {
	    menuItem = (MenuItem)menuItems.get(i);
	    mnemonic = menuItem.getMnemonic();
	    if (mnemonic == null) {
	        mnemonic = "";
	    }
	    if(menuItem.getItemId() == 15 && !alertStrategyPower && !alertFilterPower){
	    	continue;
	    }
%>
	myMenu[myMenu.length] = [null, getI18NMenuItemString('<%= menuItem.getName() %>')+'(<a href="#" onfocus="this.blur()" style="text-decoration: underline; cursor: default;"><%= mnemonic %></a>)', null, null, null];
<%
		children = menuItem.getChildren();
		int size = (children == null ? 0 : children.size());
		// 垂直一级菜单
		for (int j = 0; j < size; j++) {
		    childItem = (MenuItem) children.get(j);
		    icon = childItem.getIcon();
		    if (icon != null && !"".equals(icon)) {
		        icon = "'" + icon + "'";
		    }
		    String text = resourceManager.getString(childItem.getName());
		    int itemId = childItem.getItemId();
		    if (itemId ==1503  && !alertStrategyPower){
		    	continue;
		    }
		    if (itemId ==1504  && !alertFilterPower){
		    	continue;
		    }
		    //if(itemId == 2021 && !sysInfoPower){
		    //	continue;
		    //}
		    //if((itemId == 714 || itemId == 718) && !eventPower){
		    //	continue;
		    //}
		    //if((itemId == 1029 || itemId == 2021 || itemId == 2025 || itemId == 2009) && !system){
		    //	continue;
		    //}
		    if(itemId == 2021&&!sysInfoManagementPower){
	    		continue;
		    }
		    if(itemId == 2025&&!licenseManagementPower){
		   		continue;
		    }
		    if(itemId == 1506 || itemId == 1530){
		    	continue;
		    }
		    if((itemId == 118||itemId == 119||itemId == 1025)&&!myDeskTopPower){
		    	continue;
		    }
		    if((itemId == 115||itemId == 116||itemId == 1030)&&!systemPower){
		    	continue;
		    }
		        text = "<div style=\"width:150px\">" + text + "</div>";
		    String action = childItem.getAction();
		    if (action == null) {
		    	action = "";
		    }
			if (childItem.getType().equals(MenuItem.TYPE_MENUITEM)) {		    
%>
	vArr = [<%= icon %>, '<%= text %>',  '<%= action %>', null, null];
	myMenu[<%=k%>][myMenu[<%=k%>].length] = vArr;
<%
    		} 
    		if(childItem.getItemId() == 2022){
    		continue;
    		}
    		if (childItem.getType().equals(MenuItem.TYPE_SEPARATOR)) {
%>
	myMenu[<%=k%>][myMenu[<%=k%>].length] = _cmSplit;
<%
    		}
			// 垂直二级菜单
			List<MenuItem> children2 = childItem.getChildren();
			int size2 = (children2 == null ? 0 : children2.size());
			if (size2 == 0) {
			    continue;
			}
			for (int m = 0; m < size2; m++) {
			    MenuItem childItem2 = children2.get(m);
			    icon = childItem2.getIcon();
			    if (icon != null && !"".equals(icon)) {
			        icon = "'" + icon + "'";
			    }			    
			    String text2 = null;
			    if (m == 0) {
			        text2 = "<div style=\"width:100px\">" + resourceManager.getString(childItem2.getName()) + "</div>";
			    } else {
			        text2 = resourceManager.getString(childItem2.getName());
			    }
			    if (childItem2.getType().equals(MenuItem.TYPE_MENUITEM)) {
%>
	vArr[vArr.length] = [<%= icon %>, '<%= text2 %>', '<%= childItem2.getAction() %>', null, null];
<%
			    } else if (childItem2.getType().equals(MenuItem.TYPE_SEPARATOR)) {
%>
	vArr[vArr.length] = _cmSplit;
<%      
			    }
			}
        }
        k = k + 1;
    }
%>