<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.List"  %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/nocache.inc"%>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module workbench
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>

#control {
	position:absolute;
	top : 205px;
	left: 100px;
	padding-left : 80px;
}

#tree-container {
	z-index : 10000;
}

#background {
	position : absolute;
	left : 15px;
	top : 15px;
	width : 320px;
	height : 180px;
	border : 1px solid gray;
	background-color : white;
}

tr {
	height : 20px;
}

.NAVI_TASK_L {
	width : 20px;
}
.leftFloat{ float:left; width:135px;}
.w180{ padding-bottom:12px; float:left;}

</style>
<script>
var galary = []
$(function(){
	var checked = false //标记是否已选择过
		<% List<String> naviBarOrder = (List<String>)request.getAttribute("customList"); %>
		<% 
		if(uc.hasPower("resourceList")){ 
		%>
			checked = <%= naviBarOrder.contains("WorkBench.entityList") %>
			if(checked){
				$("#entityList").val("true");
			}
			//galary[galary.length] = {functionName : I18N.WorkBench.entityList, functionAction : 'showResource()' , icon : 'icoB2',checked : checked }
		<%
		}if(uc.hasPower("alertView")){ 
		%>
			checked = <%= naviBarOrder.contains("WorkBench.showAlert") %>
			if(checked){
				$("#alertView").val("true");
			}
			//galary[galary.length] = {functionName : I18N.WorkBench.showAlert, functionAction : 'showAlert()' , icon : 'icoF2',checked : checked}
		<% 
		}if(uc.hasPower("topoGraph")){ 
		%>
			checked = <%= naviBarOrder.contains("WorkBench.topo") %>
			if(checked){
				$("#topoGraph").val("true");
			}
			//galary[galary.length] = {functionName :I18N.WorkBench.topo, functionAction : 'showTopology()' , icon : 'icoB13',checked : checked}
		<%  
		}if(uc.hasPower("baiduMap")){
		%>
			checked = <%= naviBarOrder.contains("MODULE.baiduMap") %>
			if(checked){
				$("#baiduMap").val("true");
			}
			//galary[galary.length] = {functionName : I18N.MODULE.googleMap, functionAction : 'showGoogle()' , icon : 'icoC1',checked : checked}
		<%  
		}if(uc.hasPower("deviceView")){
		%>
			checked = <%= naviBarOrder.contains("WorkBench.deviceView") %>
			if(checked){
				$("#deviceView").val("true");
			}
			//galary[galary.length] = {functionName : I18N.WorkBench.deviceView, functionAction : 'showEntityView()' , icon : 'icoB1',checked : checked}
		<%  
		}if(uc.hasPower("networkView")){
		%>
			//Add by Victor@20140512增加IP段展示
            checked = <%= naviBarOrder.contains("WorkBench.ipSegmentView") %>
            if(checked){
                $("#ipSegmentView").val("true");
            }
            //galary[galary.length] = {functionName : I18N.WorkBench.ipSegmentView, functionAction : 'showEntityView()' , icon : 'icoB1',checked : checked}
		<% 
		}if(uc.hasPower("eponPortMonitor")){
		%>
			checked = <%= naviBarOrder.contains("WorkBench.eponPortMonitor") %>
			if(checked){
				$("#eponPortMonitor").val("true");
			}
			//galary[galary.length] = {functionName : I18N.WorkBench.eponPortMonitor, functionAction : 'showEponMonitor()' , icon : '../images/system/department.gif',checked : checked}
		<%  
		}if(uc.hasPower("cmCpeQuery")){ 
		%>
			checked = <%= naviBarOrder.contains("WorkBench.cmcpe") %>
			if(checked){
				$("#cmcpe").val("true");
			}
			//galary[galary.length] = {functionName :I18N.WorkBench.topo, functionAction : 'showTopology()' , icon : 'icoB13',checked : checked}
		<%  
		}
		%>	
			checked = <%= naviBarOrder.contains("WorkBench.attention") %>
			if(checked){
				$("#userAttention").val("true");
			}
			checked = <%= naviBarOrder.contains("WorkBench.global") %>
			if(checked){
				$("#global").val("true");
			}
			//galary[galary.length] = {functionName : I18N.WorkBench.global, functionAction : 'showGlobal()' , icon : 'icoH1',checked : checked}
			
		//系统加载后，判断隐藏域的值，如果是true,则勾选框选中;
		$("#hiddenDiv :hidden").each(function(i){
			if($(this).val() == true || $(this).val() == "true"){
				//$(".blockSpan").eq(i).attr("class","blockSpan withCheck");
				var _id = $(this).attr("id");
				$(".blockSpan").each(function(){
					if($(this).attr("data-id") == _id){
						$(this).attr("class","blockSpan withCheck");
					} 
				})
				
			}
		});//end each;
		
		//点击复选框，选中则改变样式，并且改变隐藏域的值，提交的就是隐藏域的值;
		$(".blockSpan").click(function(){
			var index = Number($(this).attr("name"));//由于这个版本的jquery的index()函数是错误的，所以只好在name上注明是第几个元素;	
			if($(this).hasClass("withoutCheck")){
				$(this).removeClass("withoutCheck").addClass("withCheck");
				$("#hiddenDiv :hidden").eq(index).val("true");
			}else if($(this).hasClass("withCheck")){
				$(this).removeClass("withCheck").addClass("withoutCheck");
				$("#hiddenDiv :hidden").eq(index).val("false");
			}
		});//end click;
			
});

function confirm(){
	//var collection = $("input[type=checkbox][checked=true]")
	var names = [];
	var actions = [];
	var icon = [];
	if($("#entityList").val() == "true"){
		names.push("WorkBench.entityList");	
		actions.push("showResource()");
		icon.push("icoB2");
	}
	if($("#alertView").val() == "true"){
		names.push("WorkBench.showAlert");	
		actions.push("showAlert()");
		icon.push("icoF2");
	}
	if($("#topoGraph").val() == "true"){
		names.push("WorkBench.topo");	
		actions.push("showTopology()");
		icon.push("icoB13");
	}
	if($("#baiduMap").val() == "true"){
		names.push("MODULE.baiduMap");	
		actions.push("showBaidu()");
		icon.push("icoC1");
	}
    if($("#deviceView").val() == "true"){
        names.push("WorkBench.deviceView"); 
        actions.push("showEntityView()");
        icon.push("icoB1");
    }
    if($("#ipSegmentView").val() == "true"){
        names.push("WorkBench.ipSegmentView"); 
        actions.push("showIpSegmentView()");
        icon.push("icoE4");
    }
	if($("#global").val() == "true"){
		names.push("WorkBench.global");	
		actions.push("showGlobal()");
		icon.push("icoH1");
	}
	if($("#cmcpe").val() == "true"){
		names.push("WorkBench.cmcpe");	
		actions.push("showCmCpeQuery()");
		icon.push("icoG13");
	}
	if($("#userAttention").val() == "true"){
		names.push("WorkBench.attention");	
		actions.push("showUserAttention()");
		icon.push("icoH15");
	}
	$.ajax({
		url: '/workbench/updateCustomMydesck.tv', cache:false,method:'post',
		data : {functionNames : names.join("-") , functionActions : actions.join("-"), functionIcons : icon.join("-")},
		success:function(){
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@resources/WorkBench.customMyDeskSuccess@</b>'
       	    });
			window.parent.location.reload()
			cancelClick()
		},
		error:function(){
			window.parent.showMessageDlg('@COMMON.tip@','@resources/WorkBench.customMyDeskFail@')
		}
	})
}
function cancelClick(){
	window.parent.closeWindow('customMyDesk');
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip"><b class="orangeTxt">@resources/WorkBench.check@</b>@resources/WorkBench.customItems@
		</div>
		<div class="rightCirIco myDeskCirIco"></div>		
	</div>
	<div class="edgeTB10LR20 dotLine1">
		<div class="customMyDeskOuter">
			<ul class="leftFloatUl">
				<%if(uc.hasPower("resourceList")){ %>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck" name="0" data-id="entityList"></span>
						<span class="pL18 pB2 icoB2 leftFloat">@resources/WorkBench.entityList@</span>
					</div>
				</li>
				<%}%>
				<% if(uc.hasPower("alertView")){%>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck" name="1" data-id="alertView"></span>
						<span class="pL18 pB2 icoF2 leftFloat">@resources/WorkBench.showAlert@</span>
					</div>
				</li>
				<%}%>
				<% if(uc.hasPower("topoGraph")){%>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck"  name="2" data-id="topoGraph"></span>
						<span class="pL18 pB2 icoB13 leftFloat">@resources/SYSTEM.Topology@</span>
					</div>
				</li>
				<%}%>
				<%if(uc.hasPower("baiduMap")){ %>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck"  name="3" data-id="baiduMap"></span>
						<span class="pL18 pB2 icoC1 leftFloat">@resources/WorkBench.baiduMap@</span>
					</div>
				</li>
				<%}%>
				<% if(uc.hasPower("deviceView")){ %>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck"  name="4" data-id="deviceView"></span>
						<span class="pL18 pB2 icoB1 leftFloat">@resources/WorkBench.deviceView@</span>
					</div>
				</li>
				<%}%>
				<% if(uc.hasPower("networkView")){ %>
                <li>
                    <div class="w180">
                        <span class="blockSpan withoutCheck"  name="5" data-id="ipSegmentView"></span>
                        <span class="pL18 pB2 icoE4 leftFloat">@resources/WorkBench.ipSegmentView@</span>
                    </div>
                </li>
				<%}%>
				<% if(uc.hasPower("system")){ %>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck"  name="6" data-id="global"></span>
						<span class="pL18 pB2 icoH1 leftFloat">@resources/COMMON.gmView@</span>
					</div>
				</li>
				<%}%>
				<% if(uc.hasPower("cmCpeQuery") && uc.hasSupportModule("cmc")){ %>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck"  name="7" data-id="cmcpe"></span>
						<span class="pL18 pB2 icoG13 leftFloat">@performance/cmCpe.cmCpeQuery@</span>
					</div>
				</li>
				<%}%>
				<li>
					<div class="w180">
						<span class="blockSpan withoutCheck"  name="8" data-id="userAttention"></span>
						<span class="pL18 pB2 icoH15 leftFloat">@resources/WorkBench.attention@</span>
					</div>
				</li>
			</ul>
		
			
		</div>
		<ol class="upChannelListOl pB0 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig" onclick="confirm()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		</ol>
	</div>
	<div id="hiddenDiv">
		<input type="hidden" id="entityList" name="@resources/WorkBench.entityList@" value="false" />
		<input type="hidden" id="alertView" name="@resources/WorkBench.showAlert@" value="false" />
		<input type="hidden" id="topoGraph" name="@resources/WorkBench.topo@" value="false" />
		<input type="hidden" id="baiduMap" name="@resources/WorkBench.baiduMap@" value="false" />
        <input type="hidden" id="deviceView" name="@resources/WorkBench.deviceView@" value="false" />
        <input type="hidden" id="ipSegmentView" name="@resources/WorkBench.ipSegmentView@" value="false" />
		<input type="hidden" id="global" name="@resources/WorkBench.global@" value="false" />
		<input type="hidden" id="cmcpe" name="@performance/cmCpe.cmCpeQuery@" value="false" />
		<input type="hidden" id="userAttention" name="@resources/WorkBench.attention@" value="false" />
	</div>

</body>
</Zeta:HTML>