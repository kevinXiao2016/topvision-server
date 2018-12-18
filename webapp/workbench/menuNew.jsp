<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js.tools.ipText
    module workbench
    import js/jquery/jquery.wresize
</Zeta:Loader>
<link href="../css/reset.css" type="text/css" rel="stylesheet"></link>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css" />
<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
#favouriteView-div .x-panel-bwrap,#favouriteView-div .x-tree-root-ct{overflow: visible;}
#favouriteView-div .x-panel-body{overflow:visible !important;}
#favouriteView-div .x-tree-root-ct .x-tree-selected span{ background:#C2C2C2;}
#favouriteView-div .x-tree-node-over span{ background:#eee}

</style>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.workbench.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
.favouriteFolderIcon {
	background-image: url(../images/workbench/folder.gif) !important;
}

.favouriteLinkIcon {
	background-image: url(../images/workbench/link.gif) !important;
}
a:hover{ text-decoration: none;}

</style>
<script type="text/javascript">
var userId = <%= uc.getUser().getUserId() %>;
// my workbench module
var favouriteFolderTree = null;
var favouriteFolderRoot = null;
var favouriteFolderMenu = null;
var openFavouriteFolderItem = null;
var deleteFavouriteFolderItem = null;
var renameFavouriteFolderItem = null;

<%
boolean myDeskTop = uc.hasPower("myDeskTop");
boolean myFolder = uc.hasPower("myFolder");
boolean newDevice = uc.hasPower("newDevice");
%>

function buildWorkbenchModule() {
	if (favouriteFolderTree == null) {
		favouriteFolderTree = new Ext.tree.TreePanel({ 
			  animate:true, 
			  border:false, 
			  lines:true, 
			  rootVisible:false, 
			  enableDD:false, 
			  autoScroll : true, 
			  animCollapse: true,
			  loader: new Ext.tree.TreeLoader({
				  dataUrl:"../workbench/loadFavouriteFolder.tv"
		      })
		});
		
		favouriteFolderRoot = new Ext.tree.AsyncTreeNode({text:"Favourite Folder Tree", id:"favouriteFolderSource"});
		favouriteFolderTree.setRootNode(favouriteFolderRoot);
		favouriteFolderTree.render('favouriteView-div');
		favouriteFolderRoot.expand();
		favouriteFolderTree.on("click", function (n) {
			if (n.attributes.type == 1) {
				window.parent.addView("favouriteLink" + n.id, this.getNodeById(n.id).text, "favouriteLinkIcon", n.attributes.url.replace(/http:\/\/[\w\.-]+:3000\//, ''));
			}
		});
		favouriteFolderTree.on("contextmenu", function (n, e) {
			if (this.getSelectionModel().isSelected(n) == false) {
				this.getSelectionModel().select(n);
			}
			if (favouriteFolderMenu == null) {
				openFavouriteFolderItem = new Ext.menu.Item({text: '<font style=\"font-weight:bold\">@font.open@</font>', handler : onOpenFavouriteLinkClick});
				deleteFavouriteFolderItem = new Ext.menu.Item({text: I18N.MAIN.deleteFolder, iconCls:"bmenu_delete", handler: onDeleteFavouriteFolderClick});
				renameFavouriteFolderItem = new Ext.menu.Item({text: I18N.COMMON.rename, handler:onRenameFavouriteFolderClick});
				favouriteFolderMenu = new Ext.menu.Menu({id:"favouriteFolderMenu", minWidth: 150, items:[
					//openFavouriteFolderItem, {text: '@resources/MODULE.newFolder@', handler:newFavouriteFolder}, "-",
					deleteFavouriteFolderItem, renameFavouriteFolderItem, "-",
					{text: I18N.MenuItem.onRefreshClick, iconCls:"bmenu_refresh", handler:refreshFavouriteFolder}, "-",
					{text: I18N.COMMON.property, handler:onPropertyFavouriteFolderClick}]});
			}
			/* if (n.attributes.type == 1) {
				openFavouriteFolderItem.enable();
			} else {
				openFavouriteFolderItem.disable();
			}	 */		
			if (n.attributes.userId == userId) {
				deleteFavouriteFolderItem.enable();
				renameFavouriteFolderItem.enable();
			} else {
				deleteFavouriteFolderItem.disable();
				renameFavouriteFolderItem.disable();
			}
			var p = e.getPoint();
			if(p[0] > 85){p[0] = 85}//让右键菜单不超出屏幕，这样左侧就不会有滚动条。
			favouriteFolderMenu.showAt(p);
		});
		favouriteFolderTree.on("beforemovenode", function (tree, node, oldParent, newParent, index) {
			if (node.id <= 4) {
				return false;
			}
			var sb = new Zeta$StringBuffer('');
			sb.append("favouriteFolder.folderId=");
			sb.append(node.id);
			if(newParent.id == 'favouriteFolderSource'){
				newParent.id = 0*1;
				}
			sb.append("&favouriteFolder.superiorId=");
			sb.append(newParent.id);
			sb.append('&favouriteFolder.path=');
			sb.append(newParent.attributes.folderPath);
			sb.append(node.id);
			sb.append('/');
			Ext.Ajax.request({url: "workbench/moveFavouriteFolder.tv",
				params: sb.toString(),
				success:function (response) {
				}, failure:function () {
					window.parent.showErrorDlg();
				}
			});
			return true;
		});
		setExpanderVisible("favouriteView", "favouriteExpander",
			getCookieValue(userName + "favouriteExpander", 'true') == 'true');
	}
}
function doOnResize() {
// 	if (isFirefox) {
// 		var el = Zeta$('favouriteView-div'); 
// 		el.style.width = document.body.clientWidth;
// 		el.style.height = document.body.clientHeight - 120;
// 	}
}


function getSelectedTreeNodeId(tree) {
	var modal = tree.getSelectionModel();
	if (modal != null) {
		var n = modal.getSelectedNode();
		if (n != null) {
			return n.id;
		}
	}
	return null;
}
function getSelectedTreeNode(tree) {
	var modal = tree.getSelectionModel();
	if (modal != null) {
		return modal.getSelectedNode();
	}
	return null;
}

function showExpander(view, expander) {
	var el = Zeta$(view);
	var visible = (el.style.display == "");
	visible = !visible;
	setExpanderVisible(view, expander, visible);
	setCookieValue(userName + expander, visible);
}
function setExpanderVisible(view, expander, visible) {
	/* Zeta$(view).style.display = visible ? "" : "none";
	var o = Zeta$(expander);
	o.className = visible ? "NAVI_EXPANDER_UP" : "NAVI_EXPANDER_DOWN";
	o.title = visible ? I18N.WorkBench.NAVI_EXPANDER_UP : I18N.WorkBench.NAVI_EXPANDER_DOWN; */
}
/* cookie */
function getCookieValue(name, defaultValue) {
	return getCookie(name, defaultValue);
}
function setCookieValue(name, value) {
	setCookie(name, value);
}
function clearCookieValue(name) {
	delCookie(name);
}

/////////////////////////////////////////////////////
///////////////  ACTION   ///////////////////////////
/////////////////////////////////////////////////////
function customMyDesk(){
	window.parent.createDialog("customMyDesk",I18N.WorkBench.customMyDesk,600,370,'/workbench/showCustomMyDesk.tv')
}
function refreshFavouriteFolder() {
	if (favouriteFolderRoot != null) {
		favouriteFolderRoot.reload();
	}
}

function newFavouriteLink() {
	var itemId = getSelectedTreeNodeId(favouriteFolderTree);
	if (itemId == null) {
		itemId = "0";
	}
	window.top.createDialog("modalDlg", I18N.MODULE.newLink, 600, 370, "workbench/newFavouriteLinkJsp.tv?folderId=" + itemId, null, true, true);
}
function addDevice() {
    window.top.addView('batchTopo', '@a.batchAddEntity@', 'icoB13', '/batchautodiscovery/showBatchTopo.tv');
	//window.parent.createDialog("modalDlg", I18N.MODULE.newEquipment,  660, 480, "entity/showBatchNewEntity.tv", null, true, true);
}
function newFavouriteFolder() {
	var itemId = getSelectedTreeNodeId(favouriteFolderTree);
	if (itemId == null) {
		itemId = "0";
	}
	window.top.createDialog("modalDlg", '@resources/MODULE.newFolder@', 600, 370, "workbench/newFavouriteFolderJsp.tv?itemId=" + itemId, null, true, true);
}
function onOpenFavouriteLinkClick() {
	var n = getSelectedTreeNode(favouriteFolderTree);
	if (n != null) {
		window.top.addView("favouriteLink" + n.id, favouriteFolderTree.getNodeById(n.id).text, "favouriteLinkIcon", n.attributes.url);
	}
}
function onDeleteFavouriteFolderClick() {
	var n = getSelectedTreeNode(favouriteFolderTree);
	if (n != null) {
		window.parent.showConfirmDlg(I18N.COMMON.tip, 
			(n.attributes.type == 0 ? I18N.WorkBench.confirmDeleteFavorites : I18N.WorkBench.confirmDeleteLink), function (type) {
			if (type == "no") {
				return;
			}
			Ext.Ajax.request({url: "../workbench/deleteFavouriteFolder.tv",
				params: {folderPath: n.attributes.folderPath},
				success: refreshFavouriteFolder,
				failure:function () {
					window.parent.showErrorDlg();
				}
			});
		});
	}
}
function onRenameFavouriteFolderClick() {
	window.top.showInputDlg(I18N.COMMON.rename, I18N.COMMON.newName, renameFavouriteFolderCallback);
}
function renameFavouriteFolderCallback(type, text) {
	if (type == "cancel" || text.trim() == "") {
		return;
	}
	var reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{1,32}$/;
	if( !reg.test(text) ){
		top.afterSaveOrDelete({
			title : '@COMMON.fail@',
			html : '<b class="orangeTxt">@input.max32@</b>'
		});
		return;
	}
	
	var node = getSelectedTreeNode(favouriteFolderTree);
	Ext.Ajax.request({url:"../workbench/renameFavouriteFolder.tv", method:'post',
		success:function (response) {node.setText(text);},
		failure:function () {showErrorDlg();},
		params:{folderId:node.id, name:text}
	});
}

function onPropertyFavouriteFolderClick() {
	var n = getSelectedTreeNode(favouriteFolderTree);
	if (n != null) {
		window.top.createDialog("modalDlg", n.text, 600, 370, "workbench/showFavouriteFolder.tv?folderId=" + n.id, null, true, true);
	}
}
function showMyDesktop() {
	window.parent.addView("mydesktop", I18N.a.myWorkBench, "mydesktopIcon", "portal/showMydesktop.tv");
}
function showResource(){
	window.parent.addView("entitySnap", I18N.WorkBench.entityList, "tabIcoB2", "network/entitySnapList.jsp");
} 
function showAlert(){
	window.parent.addView("alertDashboard", I18N.WorkBench.alarmView, "icoF2", 'fault/dashboard.jsp');
}
function showTopology(){
	//window.parent.addView("topo10", I18N.WorkBench.topology10, "topoRegionIcon", "topology/showNewTopoDemo.tv?folderId=10" );
	window.parent.addView("topoFolderDisplay", "@network/NETWORK.topoView@", "icoB13", "/cmc/showTopoFolderPage.tv");
}
function showGoogle(){
	window.parent.addView("ngm", I18N.WorkBench.googleMap, "icoC1", "google/showEntityGoogleMap.tv");
}
function showBaidu() {
    window.parent.addView("baiduMap", '@resources/WorkBench.baiduMap@', "tabIcoB2", "/baidu/showBaiduMap.tv");
}
function showCmCpeQuery(){
	window.parent.addView("CmCpeQuery", '@resources/WorkBench.cmcpe@', "icoG13", "/cmCpe/showCmCpeQuery.tv",null,true);
}
function showEntityView(){
    window.parent.addView("networkdashboard", I18N.WorkBench.deviceView, "icoB1", "/network/showDeviceViewJsp.tv");
}
function showIpSegmentView(){
    window.parent.addView("batchTopoIp", '@resources/WorkBench.ipSegmentView@', "icoE4", "/cmc/showIpSegmentPage.tv");
}
function showEponMonitor(){
	window.parent.addView("EponPortMonitor", I18N.WorkBench.eponPortMonitor, "eponDeviceMonitorIcon", "/performance/monitor.jsp",null,true);
}
function showGlobal(){
	window.parent.addView("gmview", I18N.COMMON.gmView, "icoH1", "system/gmView.jsp");
}
function showMyDesktop() {
	window.parent.addView("mydesktop", I18N.COMMON.myDesktop, "mydesktopIcon", "portal/showMydesktop.tv");
}
function showUserAttention() {
	window.parent.addView("userAttention", '@resources/WorkBench.attention@', "icoH15", "/entity/showUserAttention.tv");
}
function changeLanguage(lang){
    $.ajax({
        url: '/include/changeLanguage.tv?userId='+userId+'&lang='+lang+'&r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        success: function() {
        }
    });
    window.parent.location.reload(); ;
} 
/////////////////////////////////////////////////
//////////////  ENTRY   /////////////////////////
/////////////////////////////////////////////////
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
	<%if(myFolder){%>
		buildWorkbenchModule();
	<%}%>
});
</script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>	
</head>
<body>
	<div class="putSlider" id="putSlider">
		<div class="sliderOuter" id="sliderOuter">
			<a id="sliderLeftBtn" href="javascript:;"></a>
			<div id="slider" class="slider">
				<span id="bar" class="bar"></span>
			</div>
			<a id="sliderRightBtn" href="javascript:;"></a>
		</div>
	</div>
	<div class="putTree" id="putTree">
		<div style="width:100%; overflow:visible;">
		<%if (myDeskTop){%>
			<ul id="tree" class="filetree">
				<li><span class="icoA1"><a href="javascript:;" class="linkBtn" onclick="showMyDesktop()" >@a.myWorkBench@</a></span></li>
				<s:iterator value="customerList">
					<s:if test = 'icon == "icoG13"'>
						<% if(uc.hasPower("cmCpeQuery") && uc.hasSupportModule("cmc")){ %>
							<li><span class='<s:property value="icon"/>'>
									<a href="javascript:;" class="linkBtn" onclick='<s:property value="functionAction" />' name=""><s:property value="displayName" /></a>
							</span></li>
						<%} %>
					</s:if>
					<s:else>
						<li><span class='<s:property value="icon"/>'>
								<a href="javascript:;" class="linkBtn" onclick='<s:property value="functionAction" />' name=""><s:property value="displayName" /></a>
						</span></li>
					</s:else>
				</s:iterator>
			</ul>
		<%}if (myFolder){%>
			<div id="favouriteView-div" class="clear-x-panel-body"></div>
		<%}%>
		</div>
	</div>
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">		
		<ol class="icoBOl">		
			<%if (newDevice){%>
				<li><a class="icoB13" onclick="addDevice();" href="#">@a.batchAddEntity@</a></li>
			<%}if (myFolder){%>
				<li><a class="icoA4" onclick="newFavouriteLink();" href="#">@a.createLink@</a></li>
				<li><a class="icoA5" onclick="newFavouriteFolder();return false;" href="#">@a.createFavorites@</a></li>
			<%}if (myDeskTop){%>
				<li><a class="icoA6" onclick="customMyDesk();return false;" href="#">@a.customMyDesk@</a></li>
			<%}%>
		</ol>
	</div>
	<script type="text/javascript">
	$(function(){
		//加载树形菜单;
		$("#tree").treeview({ 
			animated: "fast",
			control:"#sliderOuter"
		});	//end treeview;
		$("#sliderLeftBtn").click(function(){
			$("#bar").stop().animate({left:0});
			if(window.favouriteFolderTree){
				window.favouriteFolderTree.collapseAll();
			}
		})
		$("#sliderRightBtn").click(function(){
			$("#bar").stop().animate({left:88});	
			if(window.favouriteFolderTree){
				window.favouriteFolderTree.expandAll();
			}
		})
		
		//点击树形节点变橙色背景;
		$(".linkBtn").live("click",function(){
			$(".linkBtn").removeClass("selectedTree");
			$(this).addClass("selectedTree");
		});//end live;
		
	});
	
	function autoHeight(){
		var h = $(window).height();
		var h1 = $("#putSlider").outerHeight();
		var h2 = 20; //因为#putTree{padding:10px};
		var h3 = $("#threeBoxLine").outerHeight();
		var h4 = $("#putBtn").outerHeight();
		var putTreeH = h - h1 - h2 - h3 - h4;
		if(putTreeH > 20){
			$("#putTree").height(putTreeH);
		}	
	};//end autoHeight;
	
	autoHeight();
	
	//resize事件增加函数节流;
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	$(window).resize(function(){
		throttle(autoHeight,window)
	});//end resize;
	
	</script>
</body>
</Zeta:HTML>
