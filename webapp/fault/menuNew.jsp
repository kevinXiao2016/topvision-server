<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	css css.main
	library Jquery
	library ext
	library zeta
    module fault
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/reset.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<style type="text/css">
.putTabBtns{ height:30px; overflow:hidden; background:#f60;}
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
</style>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>	
<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
</style>
<script type="text/javascript">
var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
var userId = <%= uc.getUser().getUserId() %>;

function showEvent(){
	window.parent.addView("eventView", '@resources/EVENT.eventViewer@', "icoF1", '/fault/showEventJsp.tv');
}
function showAlertDashboard() {
	window.parent.addView("alertDashboard", '@resources/WorkBench.alarmView@', "icoF2", 'fault/dashboard.jsp');
}
function actionSettingClick() {
    window.parent.addView("actionList", '@resources/SYSTEM.alertAction@', "icoF3", "fault/actionList.jsp");
}
function alertSettingClick() {
	window.top.createDialog('modalDlgAlertSet', '@resources/SYSTEM.alertConfig@', 800, 480, 'fault/alertSetting.tv', null, true, true);
}
function alertFilterClick() {
	window.parent.createDialog("modalDlg", '@resources/FAULT.alertFilter@', 600, 400, "fault/showAlertFilter.tv", null, true, true);
}
function alertFilterClickFn(){
	top.addView('alertFilter', '@resources/FAULT.alertFilter@', "icoF5", 'fault/showAlertFilter.tv', false, true)
}

function showCurrentLevelAlert(level){
	window.top.addView('alertViewer', '@resources/EVENT.alarmViewer@', "icoF2", 'alert/showCurrentAlertList.tv?level=' + level,false,true);
}

function showCurrentTypeAlert(typeId){
	window.top.addView('alertViewer', '@resources/EVENT.alarmViewer@', "icoF2", 'alert/showCurrentAlertList.tv?typeId=' + typeId,false,true);
}

function showCurrentEvent(typeId){
	window.top.addView('eventView', '@resources/EVENT.eventViewer@', "icoF1", '/fault/showEventJsp.tv?eventTypeId=' + typeId,false,true);
	//window.parent.addView("eventView", '@resources/EVENT.eventViewer@', "icoF1", '/fault/showEventJsp.tv');
}

//构造告警等级树结构
function showAlertLevel() {	
	treeFloatTip();
	$("#treeLoading").css("display","block");
	//将滚动条拉动到左侧;
	var liFormatStr = '<li><span class="{0}"><a href="javascript:;" onclick="{1}" class="linkBtn" id="{2}">{3}</a></span></li>';
	//清空树
	$('ul#tree').empty();
	$.ajax({
		url: '../fault/loadAllAlertLevel.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(array) {
   			var rootStr = String.format(liFormatStr, 'folder', 'showCurrentLevelAlert('+array[0].level+')', array[0].id, array[0].text);
   			var $li = $(rootStr);
			$li.appendTo($("ul#tree"));
			//输出子节点
			var $ul = $('<ul></ul>').appendTo($li);
			$.each(array[0].children, function(index, alert){
				var liStr = String.format(liFormatStr, alert.iconCls, 'showCurrentLevelAlert('+alert.level+')', alert.id, alert.text);
  				$(liStr).appendTo($ul);
			});
			treeBasicHandle();
   			//$("#sliderLeftBtn").click();
   			$("#treeLoading").css("display","none");
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
} 

//构造告警类型树结构
function showAlertType(){
	$("#treeLoading").css("display","block");
	//将滚动条拉动到左侧;
	$("#sliderRightBtn").click();
	//清空树
	$('ul#tree').empty();
	$.ajax({
		url: '../fault/loadAlertType.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(jsonAlertType) {
   			//循环输出各父节点
   			$.each(jsonAlertType, function(index,rootAlert){
   				bulidTree(rootAlert, '');
   			});
   			//绑定事件
   			$('a.linkBtn').each(function(){
   				//首先清除绑定
   				$(this).unbind();
   				$(this).bind('click', function(){
   					showCurrentTypeAlert($(this).attr("id"));
   				});
   			});
   			treeBasicHandle();
   			treeFloatTip();
   			$("#treeLoading").css("display","none");
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//构造事件类型树结构
function showEventType() {
	//将滚动条拉动到左侧;
	$("#sliderRightBtn").click();
	$("#treeLoading").css("display","block");
	//清空树
	$('ul#tree').empty();
	$.ajax({
		url: '../fault/getAllEventType.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(jsonEventTypeList) {
   			//循环输出各父节点
   			$.each(jsonEventTypeList, function(index,rootAlert){
   				bulidTree(rootAlert, '');
   			});
   			//绑定事件
   			$('a.linkBtn').each(function(){
   				//首先清除绑定
   				$(this).unbind();
   				$(this).bind('click', function(){
   					showCurrentEvent($(this).attr("id"));
   				});
   			});
   			treeBasicHandle();
   			treeFloatTip();
   			$("#treeLoading").css("display","none");
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

$(document).ready(function(){
	//构造告警等级树结构
	if(<%=uc.hasPower("alertViewer")%>){
		showAlertLevel();
		$('a#alertLevel').addClass('tabSelected');
	}else if(!<%=uc.hasPower("alertViewer")%>&&<%=uc.hasPower("eventViewer")%>){
		showEventType();
		$('a#eventType').addClass('tabSelected');
	}
	$("#treeLoading").click(function(){
		$(this).fadeOut();
	})
});
</script>
<%
boolean filterAlertPower = uc.hasPower("alertFilter");
boolean alertPolicyPower = uc.hasPower("alertStrategy");
%>  
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
	<div id="putTabBtns">
		<div id="tabRelative" class="tabRelative">
            <a href="javascript:;" class="tabArrLeft"></a>
            <a href="javascript:;" class="tabArrRight"></a>
            <div class="tabMiddle">
                <table cellpadding="0" cellspacing="0" border="0" rules="none" style="border-collapse: collapse;">
                    <tbody>
                        <tr>
                        	<%if(uc.hasPower("alertViewer")){ %>
                        	<td width="5">
                                <div class="leftEdge5"></div>
                            </td>
                            <td>
                            	<div>
                            		<a id="alertLevel" class="tabSelected" href="javascript:;" onclick="showAlertLevel()"><span>@ALERT.alertLevel@</span></a>
                            	</div>
                            </td>
                            <td width="2">
                                <div class="edge2"></div>
                            </td>
                            <td>
                            	<div>
                            		<a id="alertType" href="javascript:;" onclick="showAlertType()"><span>@ALERT.alertType@</span></a>
                            	</div>
                            </td>
                            <%} %>
                            <%if(uc.hasPower("eventViewer")){ %>
                           <!--  <td width="2">
                                <div class="edge2"></div>
                            </td>
                            <td>
                            	<div>
                            		<a id="eventType" href="javascript:;" onclick="showEventType()"><span>@ALERT.eventType@</span></a>
                            	</div>
                            </td> -->
                            <%} %>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
	</div>
	<div class="putTree" id="putTree">
		<div style="width:100%; overflow:hidden;">
			<ul id="tree" class="filetree">
			</ul>
		</div>
	</div>
	
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<%if(uc.hasPower("eventViewer")){ %>
<!-- 			<li><a href="#" class="icoF1" onclick="showEvent()">@resources/EVENT.eventViewer@</a></li> -->
			<%} %>
			
			<%if(uc.hasPower("alertView")){ %>
			<li><a href="#" class="icoF2" onclick="showAlertDashboard()">@resources/WorkBench.alarmView@</a></li>
			<% } %>
			
<%-- 			<%if (uc.hasPower("alertAction")) {%>	 --%>
<!-- 			<li><a href="#" class="icoF3" onclick="actionSettingClick()">@resources/SYSTEM.alertAction@</a></li>		 -->
<%-- 			<% } %>	 --%>
			
			<%if (uc.hasPower("alertStrategy")) {%>	
			<li><a href="#" class="icoF4" onclick="alertSettingClick()">@resources/MODULE.alertPolicy@</a></li>
			<% } %>
			
			<%-- <%if (uc.hasPower("alertFilter")) {%>	
			<li><a href="#" class="icoF5"  onclick="alertFilterClick()">@resources/FAULT.alertFilter@</a></li>
			<% } %> --%>
			
			<%if (uc.hasPower("alertFilter")) {%>	
			<li><a href="#" class="icoF5"  onclick="alertFilterClickFn()">@resources/FAULT.alertFilter@</a></li>
			<% } %>
			
			<%if (uc.hasPower("alertSound")) {%>
			<li><a href="#" class="icoF6"  onclick="alertSound()">@resources/rolePower.alertSound@</a></li>
			<% } %>
		</ol>
	</div>
	
	<!-- 刷新树时候的loading -->
	<div id="treeLoading" class="treeLoading">
		Loading...
	</div>
 
<script type="text/javascript" src="../js/jquery/dragMiddle.js"></script>	
<script type="text/javascript">
$(window).load(function(){
	var tab1 = new TabLee("tabRelative","tabContent","tabSelected",1);
	tab1.init();
});
$(function(){
	function autoHeight(){
		var h = $(window).height();
		var h1 = $("#putSlider").outerHeight();
		var h2 = 20; //因为#putTree{padding:10px};
		var h3 = $("#threeBoxLine").outerHeight();
		var h4 = $("#putBtn").outerHeight();
		var h5 = $("#putTabBtns").outerHeight();//和其它板块不同，它多一个tab栏;
		var putTreeH = h - h1 - h2 - h3 - h4- h5;
		if(putTreeH > 20){
			$("#putTree").height(putTreeH);
		}	
	};//end autoHeight;
	
	autoHeight();
	$(window).resize(function(){
		autoHeight();
	});//end resize;
	
	//解决ie6的td宽度不正确问题;
	$(".tabMiddle a").each(function(){
		var w = $(this).innerWidth();
		$(this).parent().width(w);		
	})
	
});
/*告警声音*/
function alertSound(){
	window.top.createDialog('alertSound', '@resources/rolePower.alertSound@', 800, 500, '/fault/showAlertSound.tv', null, true, true);	
}
</script>
</body>
</Zeta:HTML>
