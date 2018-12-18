<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module network
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/reset.css" />
<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/@RESOURCES/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@RESOURCES/COMMON.closeIcon@.png) no-repeat;}
</style>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css" />
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript">	
var cameraSwitch = '${cameraSwitch}';
	$(function(){
		//加载树形菜单;
		$("#tree").treeview({ 
			animated: "fast",
			control:"#sliderOuter"
		});	//end treeview;
		$("#sliderLeftBtn").click(function(){
			$("#bar").stop().animate({left:0});			
		})
		$("#sliderRightBtn").click(function(){
			$("#bar").stop().animate({left:88});		
		})
		
		//点击树形节点变橙色背景;
		$(".linkBtn").live("click",function(){
			$(".linkBtn").removeClass("selectedTree");
			$(this).addClass("selectedTree");
		});//end live;
		
		var nm3k = {};
		nm3k.resizeNum = 0;
		nm3k.cartoon = null;
		//自适应高度;
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
		
		//resize事件增加 函数节流;
		function throttle(method, context){
			clearTimeout(method.tId);
			method.tId = setTimeout(function(){
				method.call(context);
			},100);
		}
				
		autoHeight();
		$(window).resize(function(){
			//autoHeight();
			throttle(autoHeight,window);
		});//end resize;
		if(cameraSwitch != 'on'){
	    	$('#camera').hide();
	    }
	});//end document.ready;
	
	//点击设备视图;
	function showNetworkDashboard() {
	    window.parent.addView("networkdashboard", '@a.title.entityView@', "tabIcoB1", "/network/showDeviceViewJsp.tv", null, true);
	}
	//点击资源列表;
	function showAllEntitySnap() {
	    window.parent.addView("entitySnap", '@a.title.resourceList@', "tabIcoB2", "/network/showEntitySnapList.tv");
	}
	//点击OLT设备列表;
	function showOltSnap(){
		window.parent.addView("oltList", '@a.title.oltList@', "tabIcoB3", "../epon/entityList.jsp");
	}
	//点击ONU设备列表;
	function showOnuSnap(){
		window.parent.addView("onuDeviceList", '@a.title.onuList@', "tabIcoB4", "/onulist/showOnuDeviceView.tv");
	}
	
	//点击ONU链路视图;
	function showOnuLinkView(){
		window.parent.addView("onuLinkView", '@a.title.onuLinkList@', "icoG14", "/onulist/showOnuLinkView.tv");
	}
	
	//拆分后的EPON onu业务视图
	function showEponBusiness(){
		window.parent.addView("eponBusiness", '@a.title.eponBusiness@', "icoG3", "/onulist/showEponOnuBusinessView.tv");
	}
	
	//拆分后的GPON onu业务视图
	function showGponBusiness(){
		window.parent.addView("gponBusiness", '@a.title.gponBusiness@', "icoG3", "/onulist/showGponOnuBusinessView.tv");
	}
	//点击摄像头列表;
	function showCameraList(){
		window.parent.addView("cameraList", '@CAMERA/CAMERA.cameraList@', "tabIcoB16", "camera/showGlobalCameraList.tv");
	}
	//点击CCMTS列表;
	function showCmcSnap(){
		window.parent.addView("cmcList", '@a.title.cmtsList@', "tabIcoB5", "cmc/showAllCmcList.tv");
	}
	//点击CMTS列表;
    function showCmtsSnap(){
        window.parent.addView("cmtsList", '@a.title.cmtsList@', "tabIcoB5", "/cmts/showAllCmtsList.tv");
    }
	function showCmSnapNew() {
	    window.parent.addView("cmListNew", '@a.title.cmList@', "tabIcoB6", "/cmlist/showCmListPage.tv");
	}
	//点击CPE列表
    function showCmCpeSnap() {
        window.parent.addView("cmCpeList", '@a.title.cmCpeList@', "tabIcoB6", "/cmCpeInfo/showCmCpeListPage.tv");
    }
	//点击CM导入信息 ;
	function showCmImportInfo(){
		window.parent.addView("CmImportInfo", '@a.title.cmImportInfo@', "tabIcoB7", "cm/showCmImportList.tv");
	}
	/************************************
            查看CM\CPE综合查询页面
    *************************************/
    function showCmCpeQuery(){
        window.parent.addView("CmCpeQuery", "@a.title.cmCpeQuery@", "icoG13", "/cmCpe/showCmCpeQuery.tv");
    }
        
    /************************************
                    查看终端实时定位
    *************************************/
    function showTerminalLocate(){
        window.parent.addView("CmLocate", "@a.title.cmLocate@" , "icoB13", "/network/showTerminalLocation.tv");
    }
	//点击新建设备;
	function addDevice() {
    	window.parent.createDialog("modalDlg", '@NETWORK.addEntity@',  600, 500, "entity/showNewEntity.tv?folderId=10", null, true, true);
	}
	//点击批量拓扑;
	function batchDevice() {
		window.top.addView('batchTopo', '@NETWORK.batchAddEntity@', 'icoB13', '/batchautodiscovery/showBatchTopo.tv');
    	//window.parent.createDialog("popBatchEntity", '@NETWORK.topoConfig@',  800, 500, "entity/showBatchNewEntity.tv", null, true, true);
	}
	
	//点击导入设备;
	function importDevice() {
    	window.parent.createDialog("popBatchEntity", '@NETWORK.importEntity@',  800, 550, "entity/showBatchNewEntity.tv", null, true, true);
	}
	
	function importDeviceInfo() {
    	window.parent.createDialog("importName", '@NETWORK.importDeviceInfo@',  800, 500, "entity/import/showEntityNameImport.tv", null, true, true);
	}
	
	function exportEntity() {
		window.parent.addView("exportEntity", '@NAMEEXPORT.exportEntity@', "icoB15", "/export/showEntityExport.tv");
	}
	
	var userId = <%=uc.getUser().getUserId()%>;
	<%
	boolean newEquipment = uc.hasPower("newDevice");
	boolean importName = uc.hasPower("importName");
	boolean exportEntity = uc.hasPower("exportEntity");
	%>
	var topoFolderTree = null;
	var topoFolderMenu = null;
	var deleteFolderItem = null;
	var renameFolderItem = null;
	var moveFolderItem = null;
	var propertyFolderItem = null;
	var NodeArray=new Array(); 
	
	var operationDevicePower = <%= uc.hasPower("operationDevice") %>;
	
	function authLoad(){
	    if(!operationDevicePower){
	        $("#addDevice").attr("disabled",true);
	        $("#batchDevice").attr("disabled",true);
	    }
	}
</script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>	
</head>
<body onload = "authLoad();">
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
		<div style="width:100%; overflow:hidden;">
			<ul id="tree" class="filetree">
				<%if(uc.hasPower("deviceView")){ %>
				<li><span class="icoB1"><a href="javascript:;" class="linkBtn" onclick="showNetworkDashboard()">@a.title.entityView@</a></span></li>
				<%} %>
				<%if(uc.hasPower("resourceList")){ %>
				<li><span class="icoB2"><a href="javascript:;" class="linkBtn" onclick="showAllEntitySnap()">@a.title.resourceList@</a></span></li>
				<%} %>
			<% if(uc.hasSupportModule("olt")||uc.hasSupportModule("onu")){%>
				<%if(uc.hasPower("oltList")||uc.hasPower("onuList")||uc.hasPower("onuLinkList")||uc.hasPower("eponBusiness")||uc.hasPower("gponBusiness")) {%>
				<li>
					<span class="folder">@a.title.eponList@</span>
					<ul>
						<%if(uc.hasSupportModule("olt") && uc.hasPower("oltList")){ %>						
						<li><span class="icoB3"><a href="javascript:;" class="linkBtn" onclick="showOltSnap()">@a.title.oltList@</a></span></li>
						<%} %>
						<%if(uc.hasSupportModule("onu")){ %>
								<%if(uc.hasPower("onuList")){ %>
									<li><span class="icoB4"><a href="javascript:;" class="linkBtn" onclick="showOnuSnap()">@a.title.onuList@</a></span></li>
								<%} %>
								<%if(uc.hasPower("onuLinkList")){ %>
									<li><span class="icoG14"><a href="javascript:;" class="linkBtn" onclick="showOnuLinkView()">@a.title.onuLinkList@</a></span></li>
								<%} %>
								<%if(uc.hasPower("eponBusiness")){ %>
									<li><span class="icoG3"><a href="javascript:;" class="linkBtn" onclick="showEponBusiness()">@a.title.eponBusiness@</a></span></li>
								<%} %>
								<%if(uc.hasPower("gponBusiness")){ %>
									<li><span class="icoG3"><a href="javascript:;" class="linkBtn" onclick="showGponBusiness()">@a.title.gponBusiness@</a></span></li>
								<%} %>
						<%} %>
						<li id="camera"><span class="icoB16"><a href="javascript:;" class="linkBtn" onclick="showCameraList()">@CAMERA/CAMERA.cameraList@</a></span></li>
					</ul>					
				</li>
				<%} %>
			<% } %>
			<% if(uc.hasSupportModule("cmc")){%>
				<%if(uc.hasPower("cmtsList")||uc.hasPower("ccmtsList")||uc.hasPower("cmList")||uc.hasPower("cmImportInfo")){ %>
				<li>
					<span class="folder">@a.title.ccList@</span>
					<ul>
	                    <%-- <% if (uc.hasSupportModule("cmts")) {%>
	                    	<%if(uc.hasPower("cmtsList")){ %>				
						    <li><span class="icoB5"><a href="javascript:;" class="linkBtn" onclick="showCmtsSnap()">@a.title.cmtsList@</a></span></li>
	                    	<%} %>
	                    <% } %> --%>
	                    <%if(uc.hasPower("ccmtsList")){ %>	
	                    <li><span class="icoB5"><a href="javascript:;" class="linkBtn" onclick="showCmcSnap()">@a.title.ccmtsList@</a></span></li>
						<%} %>
						<%if(uc.hasPower("cmList")){ %>	
						<li><span class="icoB6"><a href="javascript:;" class="linkBtn" onclick="showCmSnapNew()" >@a.title.cmList@</a></span></li>
						<%} %>
						<%if(uc.hasPower("cmCpeList")){ %>   
                        <li><span class="icoB6"><a href="javascript:;" class="linkBtn" onclick="showCmCpeSnap()" >@a.title.cmCpeList@</a></span></li>
                        <%} %>
						<%if(uc.hasPower("cmImportInfo")){ %>	
						<li><span class="icoB7"><a href="javascript:;" class="linkBtn" onclick="showCmImportInfo()">@a.title.cmImportInfo@</a></span></li>					
						<%} %>
                        <%if(uc.hasPower("cmCpeQuery")){ %>
                        <li><span class="icoG13"><a href="javascript:;" class="linkBtn" onclick="showCmCpeQuery()">@a.title.cmCpeQuery@</a></span></li>
                        <%} %>
                        <li><span class="icoB13"><a href="javascript:;" class="linkBtn" onclick="showTerminalLocate()">@a.title.cmLocate@</a></span></li>
					</ul>					
				</li>
				<%} %>
			<% } %>
			</ul>
		</div>
	</div>
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<% if (newEquipment) { %>
			<li><a href="#" class="icoB12" id="addDevice" onclick="addDevice()">@NETWORK.addEntity@</a></li>
			<li><a href="#" class="icoB13" id="batchDevice" onclick="batchDevice()">@NETWORK.batchAddEntity@</a></li>
			<li><a href="#" class="icoB18" id="importDevice" onclick="importDevice()">@NETWORK.importEntity@</a></li>
			<% } %>
						
			<% if (importName) { %>
			<li><a href="#" class="icoB14" name="" onclick="importDeviceInfo()">@NETWORK.importDeviceInfo@</a></li>
			<% }if (exportEntity) { %>
			<li><a href="#" class="icoB15" name="" onclick="exportEntity()">@NAMEEXPORT.exportEntity@</a></li>
			<% } %>
		</ol>
	</div>
</body>
</Zeta:HTML>
