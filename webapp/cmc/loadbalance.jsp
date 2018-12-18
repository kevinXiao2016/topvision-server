<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module cmc
    import cmc.javascript.LoadBalanceLayout
    import cmc.javascript.LoadBalanceASDMACombo
</Zeta:Loader>
<style type="text/css">
v\:* {Behavior: url( #default#VML )}
html,body {overflow: hidden;}
/** 全局配置部分 **/
.LB-GLOBAL-SEG{position: absolute;width: 100%;height: 180px;}
.LB-GLOBAL-TABLE , .LB-GLOBAL-TABLE td{text-align: right; }
.LB-GLOBAL-TABLE input,.LB-GLOBAL-TABLE select {width: 120px; }
.globalLabel {padding-left: 15px;}
/* .LB-GROUPS-SEG{position: absolute;width: 100%;height: 200px;top:235px;}
.LB-EXCMAC-SEG {position: absolute;width: 100%;height: 200px;top:505px;} */
.LB-GROUPS-SEG{position: relative;width: 100%;top:180px;}/*set by the height in LB-GLOBAL-SEG*/
.LB-EXCMAC-SEG {position: relative;width: 100%;top:180px;}
.EXC-MAC-CLAZZ a{font-size: 12px;}
.disable-peroid-time {background-color: #EDF25A;display: inline;height: 20px;position: absolute;top:0px;}
.enable-peroid-time {background-color: #5DF25A;display: inline;height: 20px;position: absolute;top:0px;}
#scaleLine {position: absolute;left: 0px;}
.time-point {position: absolute;font-size: 15px;color: #413E3F;}
.font10{font-size: 9px;color: gray;}
#timeDisplayer {position: absolute;top : 120px;}
#LB-GLOBAL-CONT {padding-top: 10px; }
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var macDisplayFormat = '${macDisplayFormat}';
</script>
<script type="text/javascript">
	var DOC = document,
		WIN = window,
		WIN_WIDTH,
		WIN_HIGHT,
		OFFSET_TIME_AXIAS = 10,
		DAY_TIME_COUNT = 24,
		HOUR_SECONDS = 3600,
		PIXELS_PERF_CHAR = 6;
	var cmcId = '${cmcId}',
		productType=${productType},
		lbGlobalEnabled = ${loadBalanceGlobalCfg.topLoadBalConfigEnable},
		isLBEnabled = lbGlobalEnabled == 1,
		cmcIfIndex = '${loadBalanceGlobalCfg.topLoadBalConfigCmcIndex}';
	var timePolicySections =  ["","@loadbalance.alwaysValid@","@loadbalance.alwaysInvalid@","@loadbalance.periodInvalid@"];
	Ext.onReady( initialize );
	vcEntityKey = 'cmcId';
	var layout = new Layout();
	
	function isDigit(s) 
	{ 
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(s)) return false 
	return true 
	}
	//整数数字验证
	 function ifInteger(number){
	     var reg = /(^[0-9]\d*$)/;///[^0-9^\-]/;
	     return reg.test(number);
	 }
	function initialize(){
		WIN_WIDTH  = WIN.header.offsetWidth;
		WIN_HIGHT  = $(window).height()-180;
		layout.showLoadBalanceGlobal();
		layout.showLoadBalanceGroups(WIN_HIGHT/2);
		layout.showExcMacRange(WIN_HIGHT/2);
		fetchTimePolicy();
		$("#showPolicyEdit").click(function(){
		    window.top.showModalDlg("@loadbalance.timePolicy@",800,450,"/cmc/loadbalance/enterLoadBalPolicyListPage.tv?cmcId="+cmcId+"&productType="+productType,function(){
		    	fetchTimePolicy();
		    });
		});
		$("#method").val(${loadBalanceGlobalCfg.topLoadBalConfigMethod});
		$("#ATDMA2").val(${loadBalanceGlobalCfg.topLoadBalConfigDccInitAtdma});
		$("#ATDMA3").val(${loadBalanceGlobalCfg.topLoadBalConfigDbcInitAtdma});
		$("#SCDMA2").val(${loadBalanceGlobalCfg.topLoadBalConfigDccInitScdma});
		$("#SCDMA3").val(${loadBalanceGlobalCfg.topLoadBalConfigDbcInitScdma});
		//全局开启关闭控制
		//LB_swichter(null,isLBEnabled);
		LB_swichter(null,true);
		$("#periodWeight").val(${loadBalanceGlobalCfg.topLoadBalConfigNumPeriod})
	}
	
	function addLoadBalance(){
		window.top.createDialog("addLoadBalance","@loadbalance.addLB@",640, 460,"/cmc/loadbalance/showLoadBalanceAddition.tv?cmcId="+cmcId,null,null,true,function(){
			window.lb_table.getStore().reload();
		});		
	}
	function deleteBtClick(rid){
		var rec = window.lb_table.getStore().getById(rid);
		window.parent.showConfirmDlg("@COMMON.tip@", String.format("@loadbalance.delGroupFmt@" ,rec.data.devceGrpId), function(type) {
			if(type == 'no'){return;}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saveCfg@", 'ext-mb-waiting');
			$.ajax({
				url:'/cmc/loadbalance/deleteLoadBalanceGroup.tv',cache:false,
				data:{
					cmcId: cmcId, docsLoadBalGrpId: rec.data.docsLoadBalGrpId
				},success:function(){
					//window.parent.showMessageDlg("@COMMON.tip@", String.format("@loadbalance.delGroupOkFmt@",rec.data.devceGrpId));
					window.top.closeWaitingDlg();
					top.afterSaveOrDelete({
			   			title: '@COMMON.tip@',
			   			html: '<b class="orangeTxt">'+String.format("@loadbalance.delGroupOkFmt@",rec.data.devceGrpId)+'</b>'
			   		});
					window.lb_table.getStore().reload();
				},error:function(){
					window.parent.showMessageDlg("@COMMON.tip@", String.format("@loadbalance.delGroupErFmt@",rec.data.devceGrpId));
				}
			});
		});
	}
	
	function modifyBtClick(rid){
		var rec = window.lb_table.getStore().getById(rid);
		var url = "/cmc/loadbalance/showLoadBalanceAddition.tv?grpId="+rec.data.grpId+"&pageAction=2"+"&cmcId="+cmcId;
		window.top.createDialog("addLoadBalance","@loadbalance.modifyLB@",640,460,url,null,null,true,function(){
			window.lb_table.getStore().reload();
		});	
	}
	
	
	function addExcRange(){
		var h = 320;
		window.top.createDialog("addExcRange","@loadbalance.addExcRange@",617,h,"/cmc/loadbalance/showExcMacRangeAddition.tv?cmcId="+cmcId,null,null,true,function(){
			layout.showExcMacRange(WIN_HIGHT/2);
		});		
	}
	
	function deleteExcRange(index,el){
		window.parent.showConfirmDlg("@COMMON.tip@", String.format("@loadbalance.delExcRangeFmt@" , el.alt), function(type) {
			if(type == 'no'){
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saveCfg@", 'ext-mb-waiting');
			$.ajax({
				url: '/cmc/loadbalance/deleteLoadBalanceExcMacRange.tv',cache:false,
				data:{
					cmcId : cmcId, // ifIndex在后端给出，不需要我传
					topLoadBalExcludeCmIndex : index
				},
				success:function(json){
					if(json.success){
						//window.parent.showMessageDlg("@COMMON.tip@", String.format("@loadbalance.delExcRangeOkFmt@",el.alt));
						window.top.closeWaitingDlg();
						top.afterSaveOrDelete({
			   				title: '@COMMON.tip@',
			   				html: '<b class="orangeTxt">'+String.format("@loadbalance.delExcRangeOkFmt@",el.alt)+'</b>'
			   			});
						layout.showExcMacRange(WIN_HIGHT/2);	
					}else{
						window.parent.showMessageDlg("@COMMON.tip@", String.format("@loadbalance.delExcRangeErFmt@",el.alt));						
					}
				},error:function(){
					window.parent.showMessageDlg("@COMMON.tip@", String.format("@loadbalance.delExcRangeErFmt@",el.alt));
				}
			});
			
		});
	}
	
	function fetchTimePolicy(){
		$.ajax({
			url: '/cmc/loadbalance/getLoadBalanceTimePolicy.tv',cache:false,dataType:'json',
			data:'cmcId='+cmcId,
			success:function(json){
				$("#timeDisplayer").empty();
				layout.displayTimePolicy(json);
			},error:function(){}
		});
	}
	
	// 关闭负载均衡后组和排除列表是否可以操作 //
	function LB_swichter(c,bool){
		lbGlobalEnabled = bool ? 1 : 2;
		/* if(!bool){
			$("#LB-GLOBAL-CONT,#addLB,#addExcRange,#showPolicyEdit").attr("disabled",true);
			$("#period,#periodWeight,#method,#moveInterval,#maxMove,#Thresh,#diffThresh,#ATDMA2,#ATDMA3,#SCDMA2,#SCDMA3,#timePolicy").attr("disabled",true);
		}else{
			$("#LB-GLOBAL-CONT,#addLB,#addExcRange,#showPolicyEdit").attr("disabled",false);
			$("#period,#periodWeight,#method,#moveInterval,#maxMove,#Thresh,#diffThresh,#ATDMA2,#ATDMA3,#SCDMA2,#SCDMA3,#timePolicy").attr("disabled",false);
		}
		switchMethod();  */
	}
	
	function showRangeDetail(rangeDetail){
		window.top.createDialog("rangeDetail","@loadbalance.macDetail@",360,240,"/cmc/loadbalance/showMacRangeDetail.tv?rangeDetail="+rangeDetail);	
	}
	
	function saveGlobal(){
		var topLoadBalConfigPeriod = $("#period").val();
		if(!topLoadBalConfigPeriod){
			return $("#period").focus();
		}
		if(isNaN(topLoadBalConfigPeriod) || topLoadBalConfigPeriod < 30 || topLoadBalConfigPeriod > 3600 ||!ifInteger(topLoadBalConfigPeriod)){
			return $("#period").focus();
		}
		var topLoadBalConfigNumPeriod = $("#periodWeight").val();
		if(!topLoadBalConfigNumPeriod){
			return $("#periodWeight").focus();
		}
		var topLoadBalConfigMethod = $("#method").val();
		var topLoadBalConfigMaxMoves = $("#maxMove").val();
		if(!topLoadBalConfigMaxMoves){
			return $("#maxMove").focus();
		}
		if(isNaN(topLoadBalConfigMaxMoves) || topLoadBalConfigMaxMoves < 0 || topLoadBalConfigMaxMoves > 1000 ||!ifInteger(topLoadBalConfigMaxMoves)){
			return $("#maxMove").focus();
		}
		var topLoadBalConfigInterval = $("#moveInterval").val();
		if(!topLoadBalConfigInterval){
			return $("#moveInterval").focus()
		}
		if(isNaN(topLoadBalConfigInterval) || topLoadBalConfigInterval < 0 || topLoadBalConfigInterval > 3600||!ifInteger(topLoadBalConfigInterval) ){
			return $("#moveInterval").focus();
		}
		var topLoadBalConfigTriggerThresh = $("#Thresh").val();
		if(!topLoadBalConfigTriggerThresh){
			return $("#Thresh").focus();
		}
		if(isNaN(topLoadBalConfigTriggerThresh) || topLoadBalConfigTriggerThresh < 0 || topLoadBalConfigTriggerThresh > 100||!ifInteger(topLoadBalConfigTriggerThresh)){
			return $("#Thresh").focus();
		}
		var topLoadBalConfigDiffThresh = $("#diffThresh").val();
		if(!topLoadBalConfigDiffThresh){
			return $("#diffThresh").focus();
		}
		if(isNaN(topLoadBalConfigDiffThresh) || topLoadBalConfigDiffThresh < 0 || topLoadBalConfigDiffThresh > 100||!ifInteger(topLoadBalConfigDiffThresh) ){
			return $("#diffThresh").focus();
		}
		if(!isDigit(topLoadBalConfigDiffThresh) || topLoadBalConfigDiffThresh > 100 || topLoadBalConfigDiffThresh < 0||!ifInteger(topLoadBalConfigDiffThresh)){
			return $("#diffThresh").focus();
		}
		if(parseInt(topLoadBalConfigDiffThresh) > parseInt(topLoadBalConfigTriggerThresh)){
			return $("#diffThresh").focus();
		}
		var initMethod = $("#initMethod").val();
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saveCfg@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmc/loadbalance/modifyLBGlobalConfig.tv',cache:false,
			data : {
				cmcId : cmcId,
				topLoadBalConfigCmcIndex : cmcIfIndex,
				topLoadBalConfigEnable : lbGlobalEnabled,
				topLoadBalConfigPeriod : topLoadBalConfigPeriod,
				topLoadBalConfigNumPeriod : topLoadBalConfigNumPeriod,
				topLoadBalConfigInterval : topLoadBalConfigInterval,
				topLoadBalConfigMethod : topLoadBalConfigMethod,
				topLoadBalConfigMaxMoves : topLoadBalConfigMaxMoves,
				topLoadBalConfigTriggerThresh : topLoadBalConfigTriggerThresh,
				topLoadBalConfigDiffThresh : topLoadBalConfigDiffThresh,
				topLoadBalConfigDccInitAtdma : $("#ATDMA2").val(),
				topLoadBalConfigDbcInitAtdma : $("#ATDMA3").val(),
				topLoadBalConfigDccInitScdma : $("#SCDMA2").val(),
				topLoadBalConfigDbcInitScdma : $("#SCDMA3").val()
			},success:function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.saveGlobalOk@");
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@loadbalance.saveGlobalOk@</b>'
	   			});
			},error:function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.saveGlobalEr@");
			}
		});
	}
	
	function switchMethod(){
		/* var v = $("#method").val();
		if(v == 2){
			$("#period,#periodWeight,#Thresh,#diffThresh").attr("disabled",true);
		}else{
			$("#period,#periodWeight,#Thresh,#diffThresh").attr("disabled",false);
		}
		if(lbGlobalEnabled == 2){
			$("#period,#periodWeight,#Thresh,#diffThresh").attr("disabled",true);
		} */
	}
	
	function fetchHandler(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmc/loadbalance/refreshLoadBalance.tv',cache:false,
			data : {
				cmcId : cmcId
			},success:function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   					html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
   				});
				window.location.reload();
			},error:function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchEr@");
			}
		});
	}
	
</script>
</head>
<body class="whiteToBlack">
	<div id=header><%@ include file="entity.inc"%></div>
	<div class=formtip id=tips style="display: none"></div>
	<div class="LB-GLOBAL-SEG" id="LB_GLOBAL_PANEL"></div>
	<div id="LB-GLOBAL-CONT">
		<table class="LB-GLOBAL-TABLE">
				<tr>
					<td class="globalLabel rightBlueTxt">@COMMON.period@:</td>
					<td><input id="period" tooltip="@loadbalance.periodTip@" value="${loadBalanceGlobalCfg.topLoadBalConfigPeriod}"/></td>
					<td class="globalLabel rightBlueTxt">@loadbalance.periodWeight@:</td>
					<td>
						<select id="periodWeight">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="4">4</option>
						</select>
					</td>
					<td class="globalLabel rightBlueTxt">@loadbalance.method@:</td>
						<td><select id="method" onchange="switchMethod();">
							<option value="1">@loadbalance.activeMethod@</option>
							<option value="2">@loadbalance.staticMethod@</option>
						</select></td>
					<td class="globalLabel rightBlueTxt">@loadbalance.moveInterval@:</td>
					<td><input id="moveInterval" tooltip="@loadbalance.moveIntervalTip@" value="${loadBalanceGlobalCfg.topLoadBalConfigInterval}"/></td>
					<td class="globalLabel rightBlueTxt">@loadbalance.maxMove@:</td>
					<td><input id="maxMove" tooltip="@loadbalance.maxMoveTip@" value="${loadBalanceGlobalCfg.topLoadBalConfigMaxMoves}" /></td>
				</tr>
				<tr>
					<td class="globalLabel rightBlueTxt">@loadbalance.thresh@:</td>
					<td><input id="Thresh" tooltip="@loadbalance.threshTip@" value="${loadBalanceGlobalCfg.topLoadBalConfigTriggerThresh}"/></td>
					<td class="globalLabel rightBlueTxt">@loadbalance.diffThresh@:</td>
					<td><input id="diffThresh" tooltip="@loadbalance.diffThreshTipNew@" value="${loadBalanceGlobalCfg.topLoadBalConfigDiffThresh}" /></td>
					<td class="rightBlueTxt">ATDMA2.0:</td>
					<td><select id="ATDMA2">
						<option value="1">@loadbalance.broadcastInit@</option>
						<option value="2">@loadbalance.unicastInit@</option>
						<option value="3">@loadbalance.init@</option>
						<option value="4">@loadbalance.direct@</option>
					</select></td>
					<td class="rightBlueTxt">ATDMA3.0:</td>
					<td><select id="ATDMA3">
						<option value="1">@loadbalance.broadcastInit@</option>
						<option value="2">@loadbalance.unicastInit@</option>
						<option value="3">@loadbalance.init@</option>
						<option value="4">@loadbalance.direct@</option>
					</select></td>
					<td class="rightBlueTxt">SCDMA2.0:</td>
					<td><select id="SCDMA2">
						<option value="1">@loadbalance.broadcastInit@</option>
						<option value="2">@loadbalance.unicastInit@</option>
						<option value="3">@loadbalance.init@</option>
					</select></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">SCDMA3.0:</td>
					<td><select id="SCDMA3">
						<option value="1">@loadbalance.broadcastInit@</option>
						<option value="2">@loadbalance.unicastInit@</option>
						<option value="3">@loadbalance.init@</option>
					</select></td>
					<td class="globalLabel rightBlueTxt">@loadbalance.timePolicy@:</td>
					<td style="text-align: left;"><div id="timePolicy"></div></td>
					<td colspan="4" style="text-align: left;padding-left: 15px;">
						<a id="showPolicyEdit" href="javascript:;" class="normalBtn"><span><i class="miniIcoEdit"></i>@loadbalance.modifytimePolicy@</span></a>
						<!-- <button id="showPolicyEdit" style="cursor:pointer;">@loadbalance.modifytimePolicy@</button></td> -->
				</tr>
			</table>
			<div id="timeDisplayer"></div>
	</div>
	<div>&nbsp;</div>
	<div class="LB-GROUPS-SEG" id="LBGridPanel"></div>
	<div class="LB-EXCMAC-SEG" id="excludeMacPanel"></div>
</body>
</Zeta:HTML>
