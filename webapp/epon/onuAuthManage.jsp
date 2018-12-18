<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	plugin Nm3kDropdownTree
	module epon
	import js.jquery.Nm3kTabBtn
	import js/jquery/jquery.wresize
	import js.zetaframework.component.NetworkNodeSelector
	import epon.onuAuthGrid
	import epon.onuAuthManage
	import js.jquery.dragMiddle
	import gpon.javascript.PonSelector
</Zeta:Loader>
<style type="text/css">
	body,html{ height:100%;}
	#openLayer, .openLayer{ width:100%; height:100%; position:absolute; z-index:999; top:0; left:0; display:none;}
	.topEdge5{position:relative; top:5px;}
	.topEdge8{ position:relative; top:8px;}
	.topEdge10{ position:relative; top:10px;}
	.mR10{ margin-right:10px !important;}
	.mR20{ margin-right:20px !important;}
	.whiteTabUl li{ margin-right:0;}
	#putTab{ position:relative; margin-right:6px;}
	.openLayerSide, .openLayerLine, .openLayerMain{ top:47px;}
	#addDiv{ position:absolute; top:100px; left:100px; z-index:2; width:600px;}
	#addDivBg{ width:100%; height:100%; overflow:hidden; position:absolute; top:0; left:0;opacity: 0.8; filter: alpha(opacity=85);background: #F7F7F7; z-index:1;}
</style>
<script type="text/javascript" src="/js/ext/ux/RowExpander.js"></script>
<link rel="stylesheet" href="/js/ext/ux/RowExpander.css" />
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
pageSize = <%=uc.getPageSize()%>;
var oltStore = null;
var oltGrid = null;
var onuAuthStore = null;
var onuAuthGrid = null;
var onuAuthFailStore = null;
var onuAuthFailGrid = null;
var lastRow = null;
var globalEntityId
var nowTab;
var displayModule = {
	EPON_ONU_AUTH : 1,
	GPON_ONU_AUTH : 2,
	GPON_AUTO_FIND : 3
}
	
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
$(function(){
	//ONU删除;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){
		trapRefreshPage(data);
	});
	//非法ONU注册;
	var onu_illegal_reg = top.PubSub.on(top.OltTrapTypeId.ONU_ILLEGAL_REG, function(data){
		trapRefreshPage(data);
	}); 
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
		top.PubSub.off(top.OltTrapTypeId.ONU_ILLEGAL_REG, onu_illegal_reg);
	});
});
function trapRefreshPage(data){
	setTimeout(function(){
		if(oltStore != null){
			oltStore.reload();
		}	
		if(onuAuthStore != null){
			onuAuthStore.reload();
		}
		if(onuAuthFailStore != null){
			onuAuthFailStore.reload();
		}
	},10000)
}

function allStoreReload(){
	if(oltStore != null){
		oltStore.reload();
	}	
	if(onuAuthStore != null){
		onuAuthStore.reload();
	}
	if(onuAuthFailStore != null){
		onuAuthFailStore.reload();
	}
}
</script>
</head>
<body class="whiteToBlack">
	<div id = "oltAuthManage"></div>
	<div id="openLayer" class="openLayer channelOpenLayer">
		<div class="edge10">
			<ol class="upChannelListOl">
			     <li class="mR10"><a onclick="goBack()" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrLeft"></i>@VLAN.ret@</span></a></li>
			   	 <li><div id="putTab"></div></li>
			   	 <li><span class="topEdge5">@PERF.ponPort@@COMMON.maohao@</span></li>
			     <li style="padding-right:8px;">	   
			     	<select id="ponIndex" name="ponIndex" class="normalSel " style="width: 120px;" onChange="ponIndexChange()">
						<option value="0">@ONU.select@</option>
					</select>
				 </li>
				 <li><input id="macAuth" type="checkbox" class="topEdge5" onClick="checkBoxClick();"/></li>
			     <li class="mR20"><span class="topEdge5" id="macAuthTip">@onuAuth.mac@</span></li>
			     
				 <li><input id="snAuth" type="checkbox" class="topEdge5" onClick="checkBoxClick();"/></li>
				 <li class="mR20"><span class="topEdge5" id="snAuthTip">@onuAuth.sn@</span></li>
				 
			     <li id="addOnuAuthGlobalBt"><a onclick="addOnuAuthGlobal()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@onuAuth.addAuth@</span></a></li>
			     
			</ol>	
		</div>	
		<div class="horizontalOnePxLine"></div>
		<div class="openLayerSide" id="openLayerSide">
			<ul class="openLayerSideUl" id="oltTree">
			</ul>
		</div>
		<div class="openLayerLine" id="openLayerLine"></div>
		<div class="openLayerMain" id="openLayerMain">
			<div id="onuAuthManage"> </div>
			<div id="onuAuthFail"> </div>
		</div>	
	</div>
	<div id="openLayer-autofind" class="openLayer channelOpenLayer">
		<div class="edge10">
			<ol>
			     <li class="mR10"><a onclick="goBack()" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrLeft"></i>@VLAN.ret@</span></a></li>
			     <table>
			     	<tr>
					     <td><span class="pL10">@PERF.ponPort@@COMMON.maohao@</span></td>
					     <td><div id="gponAutoFindIndex"></div></td>
					     <td><select id="gponAutoFindIndexSel" class="normalSel w120" <%-- onchange="changeAutoFindSel()" --%>></select></td>
			    		 <!--改进需求-->
			    		 <td><span class="pL10">@OnuAuth/OnuAuth.onuType@@COMMON.maohao@</span></td>
			    		 <td><select id="onuType"class="normalSel w120">
			    		 </select></td>
			    		 <td><span class="pL10">@onuAuth.authMode@@COMMON.maohao@</span></td>
			    		 <td><select id="authMode" class="normalSel w120">
			    		 	<option value="-1">@ONU.select@</option>
			    		 	<option value="1" class="gpon-option">@OnuAuth/OnuAuth.snAuth@</option>
			    		 	<option value="2" class="gpon-option">@OnuAuth/OnuAuth.snPassAuth@</option>
			    		 	<option value="3" class="gpon-option">@OnuAuth/OnuAuth.loidAuth@</option>
							<option value="4" class="gpon-option">@OnuAuth/OnuAuth.loidPassAuth@</option>
							<option value="5" class="gpon-option">@OnuAuth/OnuAuth.passAuth@</option>
							<option value="6" class="gpon-option">@OnuAuth/OnuAuth.autoAuth@</option>
							<option value="7" class="gpon-option">@OnuAuth/OnuAuth.mixAuth@</option>
			    		 </select></td>
			    		 <td><span class="pL10">@onuAuth.gPONSN@@COMMON.maohao@</span></td>
			    		 <td><input id="sn" class="normalInput w120" type="text"/></td>
			    		 <td><a id="simple-query" href="javascript:simpleQuery();" class="normalBtn" onclick=""><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
			    	</tr>
			    </table>
			</ol>	
		</div>	
		<div class="horizontalOnePxLine"></div>
		<div class="openLayerSide">
			<ul class="openLayerSideUl" id="oltTreeAutoFind">
			</ul>
		</div>
		<div class="openLayerLine"></div>
		<div class="openLayerMain" id="autofind-openLayerMain">
			<div id="gponOnuAutoFind"> </div>
		</div>
	</div>
	
	<div id="openLayer-gponauth" class="openLayer channelOpenLayer">
		<div class="edge10">
			<ol>
			     <li class="mR10"><a onclick="goBack()" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrLeft"></i>@VLAN.ret@</span></a></li>
			     <table>
			     	<tr>
			     		<td><span class="pL10">@PERF.ponPort@@COMMON.maohao@</span></td>
			     		<td><div id="gponIndex"></div></td>
			     		<td><select id="gponIndexSel" class="normalSel w120" onchange="changeGponIndexSel()"></select></td>
			     		<td><li id="addOnuAuthGlobalBt"><a  onclick="showAddGponOnuAuth()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@onuAuth.addAuth@</span></a></li></td>
			     	</tr>
			     </table>
			</ol>
		</div>
		<div class="horizontalOnePxLine"></div>
		<div class="openLayerSide" id="gponauth-openLayerSide">
			<ul class="openLayerSideUl" id="oltTreeGponAuth">
			</ul>
		</div>
		<div class="openLayerLine" id="gponauth-openLayerLine"></div>
		<div class="openLayerMain" id="gponauth-openLayerMain">
			<div id="gponOnuAuthManage"> </div>
		</div>	
	</div>
	
</body>
</Zeta:HTML>
