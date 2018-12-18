<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resNetwork"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	css css.main
	css css.reset
	library Jquery
	library ext
	library zeta
    module network
    import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
    import js.nm3k.menuNewTreeTip
    import js/tools/treeBuilder
</Zeta:Loader>

<style type="text/css">
.oltLink{ color:#069;}
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
.googleIcon {
	background-image: url(../images/google_new.png) !important;
}
.topoFolderIcon1 {
	background-image: url( ../images/network/topoicon.gif ) !important;
}
span.topoFolderIcon20 {
	background:url(../images/network/mapIco.png) no-repeat;
	background:url(../images/network/mapIco.png) no-repeat !important;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
.topoFolderIcon5 {
	background-image: url( ../images/network/subnet.gif ) !important;
}
.topoFolderIcon6 {
	background-image: url( ../images/network/cloudy16.gif ) !important;
}
.topoFolderIcon7 {
	background-image: url( ../images/network/href.png ) !important;
}
.topoFolderIcon10000 {
	background-image: url( ../images/network/olt_16.gif ) !important;
}
.bmenu_delete {
	background-image: url( ../images/delete.gif ) !important;
}
.bmenu_refresh {
	background-image: url( ../images/refresh.gif ) !important;
}
a:hover{text-decoration: none;}
</style>

<script type="text/javascript">
window.top.addCallback("message",function(m){
	if(m.data === 'refresh') {
		window.location.reload(true);		
	}
});

var USEROBJ_TYPE = { ENTITY: 1 , MAPNODE : 2  , FOLDER: 20, SHAPE: 3, LINK: 4 };
var topoEditPower = <%=uc.hasPower("topoEdit")%>;
var userId = <%=uc.getUser().getUserId()%>;
<%
	boolean googleMap = uc.hasPower("googleMap");//已经去掉了
	boolean topoGraph = uc.hasPower("topoGraph");//拓扑视图
	boolean networkView = uc.hasPower("networkView") && uc.hasSupportProject("hz");//网络视图
	boolean entityList = uc.hasPower("entityList");//设备列表
	googleMap=false;
	int topoTreeClickToOpen = uc.getPreference("core.topoTreeClickToOpen") == null ? 1 : Integer.valueOf(uc.getPreference("core.topoTreeClickToOpen"));
%>
$(document).ready(function(){
	<%if(topoGraph){%> 
		//构建地域树结构
		buildFolderTree();
	<%}else{%>
		//清空树
		$('ul#tree').empty();
		addTopoIpMenu();
		treeExpand();
		treeFloatTip()
	<%}%>
	$("#treeLoading").click(function(){
		$(this).fadeOut();
	})
	
	$("#tree").delegate('a.oltLink','click',function(){
		var $me  = $(this),
		    id   = $me.attr("id"),
		    text = $me.text(),
		    parentId   = $me.attr("data-parentId"),
		    typeId = $me.attr("data-typeId"),
		    activeId   = top.getActiveFrameId(),
		    parentName = $me.attr("data-parent"); 
		
		top.setZetaClickOltLink(id); //将olt的id存在top的变量里面;
		$("#putTree a").removeClass("selectedTree");
		$span = $(this).parent().parent().parent().prev();
		if( $span.get(0).tagName == "SPAN"){
			$span.find("a").addClass("selectedTree");
		};
		//全局个性化设置，默认直接跳转设备视图，可以选择跳转拓扑地域图
        <%if(topoTreeClickToOpen == 1){%> 
            //跳转设备视图
            window.parent.addView('entity-' + id, text , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + id);
        <%}else{%>
            //跳转拓扑地域图
            if( activeId === "topo"+parentId ){
                top.frames["frametopo"+ parentId].goToEntity(id);
            } else{
                window.parent.addView("topo" + parentId, parentName, "topoRegionIcon", "topology/showNewTopoDemo.tv?folderId=" + parentId + '&entityId=' + id, true);
            }
        <%}%>
	});
	$("#tree").delegate('a.linkBtn','click',function(){
		top.setZetaClickOltLink(""); //top存olt的id存在的变量清空;
		openTopoMap($(this).attr("id"), $(this).attr("name"));
	});
});

//拓扑点击olt切换回来的时候，topoDemo.jsp页面获取目前点击了哪个olt,并获取他的id;
function getSlectOneId(){
	var id = $("#tree a.selectOne").attr("id");
	return id;
}

//刷新树结构
function refreshTree(){
	buildFolderTree();
}
//删除状态进行中;
function deleteIng(){
	$("#treeLoading").css("display","block");
	$("#putTree").css("display","none");
}
//删除状态结束;
function endDelete(){
	$("#treeLoading").css("display","none");
	$("#putTree").css("display","block");
}

/**
 * 构建地域树结构
 */
function buildFolderTree(){
	$("#treeLoading").css("display","block");
	//清空树
	$('ul#tree').empty();
	//$('tree-1').empty();
	$.ajax({
		url: '../topology/loadFolder.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(json) {
   			$("#treeLoading").css("display","none");
			bulidTree(json, '');
			//为树绑定事件
			/* $('#tree').find('a.linkBtn').each(function(){
   				$(this).bind('click', function(){
   					openTopoMap($(this).attr("id"), $(this).attr("name"))
   				});
   			}); */
			addTopoIpMenu();//注意这个函数位置不能改变。因为他不参与为树绑定事件，但是参与生成为树控件。
			treeExpand();
			treeFloatTip()
			
			
			/* $("#tree-1").treeview({ 
				animated: "fast",
				control:"#sliderOuter"
			});	//end treeview; */
			$("#sliderLeftBtn").click(function(){
				$("#bar").stop().animate({left:0});			
			})
			$("#sliderRightBtn").click(function(){
				$("#bar").stop().animate({left:88});		
			})
			
			$("#sliderLeftBtn").click(function(){
				$("#bar").stop().animate({left:0});			
			})
			$("#sliderRightBtn").click(function(){
				$("#bar").stop().animate({left:88});		
			})
			
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function openTopoMap(id, name){
	if(id == "1"){
		window.parent.addView("topoFolderDisplay", '@NETWORK.topoView@', "icoB13", "/cmc/showTopoFolderPage.tv");
	}else{
		//window.parent.addView("topo" + id, name, "topoRegionIcon", "topology/getTopoMapByFolderId.tv?folderId=" + id);
		window.parent.addView("topo" + id, name, "topoRegionIcon", "topology/showNewTopoDemo.tv?folderId=" + id);
	}
}

function openGoogleMap(){
	window.parent.addView("ngm", '@NETWORK.googleMap@', "icoC1", "google/showEntityGoogleMap.tv");
}

function openNewTopoFolder() {
    window.parent.createDialog("modalDlg", "@NETWORK.addFolder@", 800, 500, "topology/newTopoFolderJsp.tv?itemId=0", null, true, true);
}

function deleteTopoFolder(){
	window.parent.createDialog("modalDlg", "@NETWORK.deleteAreaFolder@", 800, 500, "topology/showDeleteFolder.tv", null, true, true);
}

function showNewTopoDemo(){
	window.parent.addView("topo10", "newTopoDemo", "topoRegionIcon", "topology/showNewTopoDemo.tv?folderId=10");
}
//网络设备管理;
function batchTopoIpFn(){
	window.parent.addView("batchTopoIp", '@resources/WorkBench.ipSegmentView@', "icoE4", "/cmc/showIpSegmentPage.tv");
}
//分地域展示
function topoFolderDisplay(){
	window.parent.addView("topoFolderDisplay", '@cmc/IPTOPO.topoFolderDisplay@', "icoA3", "/cmc/showTopoFolderPage.tv");
}
//在拓扑图树形菜单里面增加一个菜单，点击事件也不同;
function addTopoIpMenu(){
    $("#tree0").treeview({ 
		animated: "fast",
		control:"#sliderOuter"
	});	//end treeview;
}
function showAllEntities() {
    window.parent.addView("entities", '@resources/menunew.entitylist@', "icoI9", "/network/showEntityList.tv");
}

function showbaiduMap() {
    window.parent.addView("baiduMap123", '@BAIDU.map@', "icoB17", "/baidu/showBaiduMap.tv");
}

function openCmView(){
	window.parent.addView("openCmView", '@resources/menunew.cmvision@', "icoE4", "/cmc/showCmView.tv");
}
</script>
</head>
<body>
	<!-- 滑动收缩栏 -->
	<div class="putSlider" id="putSlider">
		<div class="sliderOuter" id="sliderOuter">
			<a id="sliderLeftBtn" href="javascript:;"></a>
			<div id="slider" class="slider">
				<span id="bar" class="bar"></span>
			</div>
			<a id="sliderRightBtn" href="javascript:;"></a>
		</div>
	</div>	
	<!-- 拓扑地域树 -->
	<div class="putTree" id="putTree">
		<div>
		<div id="topoFolderTree-div" class="clear-x-panel-body">
			<div style="width:100%; ">
				<ul id="tree0" class="filetree">
					<li><span class="icoB17"><a href="javascript:;" onclick="showbaiduMap()" class="linkBtn ">@BAIDU.map@</a></span></li>
					<% if(googleMap){%>
					<li><span class="icoC1"><a href="javascript:;" onclick="openGoogleMap()" class="linkBtn ">@googleMap@</a></span></li>
					<% } %>
					<% if(entityList){%>
					<li><span class="icoI9"><a href="javascript:;" onclick="showAllEntities()" class="linkBtn ">@resources/menunew.entitylist@</a></span></li>
					<% } %>
					<% if(networkView){%>
					<li><span class="icoE4"><a href="javascript:;" onclick="batchTopoIpFn()" class="linkBtn " >@resources/WorkBench.ipSegmentView@</a></span></li> 
					<% } %>
					<% if(uc.hasSupportModule("cmc")){ %>
	    			<li><span class="icoE4"><a href="javascript:;" onclick="openCmView()" class="linkBtn ">@resources/menunew.cmvision@</a></span></li>
	    			<% } %>
				</ul>
				<ul id="tree" class="filetree">
				</ul>
			</div>
		</div>
		</div>
	</div>	
	
	<div id="threeBoxLine"></div>
	<!-- 功能区 -->
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<% if(topoGraph){%>			
			<li><a href="#" class="icoC3" name="" onclick="openNewTopoFolder()">@NETWORK.addFolder@</a></li>	
			<li><a href="#" class="icoC4" name="" onclick="deleteTopoFolder()">@NETWORK.deleteAreaFolder@</a></li>	
			<% }%>	
			<!-- <li><a href="#" class="icoC1" name="" onclick="showNewTopoDemo()">New Topo</a></li> -->
		</ol>
	</div>
	<!-- 刷新树时候的loading -->
	<div id="treeLoading" class="treeLoading">
		Loading...
	</div>
<script type="text/javascript">
	function autoHeight(){
		var h = $(window).height();
		var h1 = $("#putSlider").outerHeight();
		var h2 = 20; 
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
	$(window).wresize(function(){
		throttle(autoHeight,window)
	});//end resize;

	/*
	* override;
	* 由于内蒙项目需要不显示第三级别，因此这里单独override该函数;
	*/
	
	function treeExpand(){
		//加载树形菜单;
		$("#tree").treeview({ 
			animated: "fast",
			control:"#sliderOuter",
			hideLevel : 3
		});	//end treeview;
		$("#sliderLeftBtn").click(function(){
			$("#bar").stop().animate({left:0});			
		})
		$("#sliderRightBtn").click(function(){
			$("#bar").stop().animate({left:88});		
		})
		
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
	}
</script>
</body>
</Zeta:HTML> 
