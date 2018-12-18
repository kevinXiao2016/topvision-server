<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
    import js/customColumnModel
    import performance/js/jquery-ui-1.10.3.custom.min
    import tools/cmdUtil
    import epon.onu.OnuDeviceAction
    import network.entityAction
    import epon.resourcesOnu
    import js.tools.authTool
    import epon.onu.onuIndexPartition
    import js.zetaframework.component.NetworkNodeSelector static
    import epon.onu.js.onuListTrapAction
</Zeta:Loader>
<style type="text/css">
#query-container{height: 100px;overflow:hidden;}
#advance-toolbar-div{padding:10px 0 0 5px;}
#simple-toolbar-div{padding:20px 0 0 5px;}
#queryContent{width: 330px;padding-left: 5px;}
.queryTable a{color:#B3711A;}
#partition{width:690px;}
#tip-div{ position: absolute; top:100px; right:10px;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}
.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }
#color-tips {left:5px;top:67px;}
#operation-tips{left:311px;top:67px;}
#suc-num-dd{color: #26B064;}
#fail-num-dd{color: #C07877;}
.yellow-div{height:16px;width:50px;background-color: #DCD345;}
.green-div{height:16px;width:50px;background-color: #ffffff;}
.red-div{height:16px;width:50px;background-color: #C07877;}
#loading {
	padding: 5px 8px 5px 26px;
	border: 1px solid #069;
	background: #F1E087 url('/images/refreshing2.gif') no-repeat 2px center;
	position: absolute;
	z-index: 999;
	top: 0px;
	left: 0px;
	display: none;
	font-weight: bold;
}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var dispatcherInterval = <%= SystemConstants.getInstance().getLongParam("entitySnap.refresh.interval", 60000L) %>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var cameraSwitch = '${cameraSwitch}';
var ONU_10G_TYPE = 40;
var ONU_MTK_TYPE = ["37", "38"];



//查询 
function onSeachClick() {
	var name = $("#nameInput").val();
	var mac = $("#macInput").val();
	//验证MAC地址是否符合模糊匹配的格式(只需要在输入了MAC地址条件时才需要验证)
   /*  if (mac != "" && !Validator.isFuzzyMacAddress(mac)) {
    	top.afterSaveOrDelete({
			title: '@COMMON.tip@',
			html: '<b class="orangeTxt">@COMMON.reqValidMac@</b>'
		});
  		$("#macInput").focus();
  		return;
    } */
	var status = $("#statusSelect").val();
	var type = typeCombo.getValue();
	var entityId =  $("#oltSelect").val();
	var slotId = slotCombo.getValue();
	var ponId = ponCombo.getValue();
	var onuEorG = $("#onuEorGSelect").val() == -1?null:$("#onuEorGSelect").val();
	var onuLevel = $("#onuLevel").val() == -1? null:$("#onuLevel").val();
	var param = {onuName: name,onuPreType: type, entityId : entityId, macAddress : mac, status: status,
			onuEorG: onuEorG,onuLevel:onuLevel,slotId : slotId, ponId: ponId, start:0,limit:pageSize}; 
	param = Ext.apply(param,partitionData);
    store.baseParams=param;
    //在执行完相关操作后去掉grid表头上的复选框选中状态
	grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
    store.load({
    	callback: function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
    		var sm = grid.getSelectionModel();
    		if(sm.getCount() === grid.store.getCount()){
    			sm.selectAll();
    		}
		}
    });
}

function simpleQuery(){
	var queryContent = $("#queryContent").val();
	store.baseParams={
			queryContent: queryContent,
			start:0,
			limit:pageSize
		};
	//在执行完相关操作后去掉grid表头上的复选框选中状态
    store.load({
    	callback: function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
    		var sm = grid.getSelectionModel();
    		if(sm.getCount() === grid.store.getCount()){
    			sm.selectAll();
    		}
		}
    });
}

function clearSelect(){
	return;
	grid.getEl().select('div.x-grid3-row').each(function(i,v){
		v.removeClass('x-grid3-row-selected');
	});
	disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
}

function createBubble(o, power, minPower, maxPower){
	var str = '',
	    firstStr = String.format('<div class="subDashedLine wordBreak">{0}</div>', o.firstHtml),
	    secondStr = String.format('<div class="subDashedLine wordBreak">{0}</div>', o.secondStr);
	    		    
	str += '<div class="bubbleTip" id="bubbleTip">';
	str += '	<div class="bubbleTipArr"></div>';
	str += '	<div class="bubbleBody">';
	str += '		<p class="pT5"><b class="gray555">'+ power +'</b></p>';
	str += 			firstStr;
	str += '		<p class="pT5"><b class="gray555">'+ minPower +'</b></p>';
	str += 			secondStr;
	str += '	</div>';
	str += '</div>';
	return str;
}

function showPowerTips(label, flag){
	$(label).live("mouseenter",function(e){
		var $me = $(this),
		    $tr = $me.parent().parent().parent(),
		    $bubbleTip = $("#bubbleTip"),
		    xpos =  $me.offset().left- 376,
			ypos = $me.offset().top,
		    o = {};
		var t1=$tr.find(label + ".first")
		o.firstHtml = $tr.find(label + ".first").html();
		var t2=$tr.find(label + ".second")
		o.secondStr = $tr.find(label + ".second").html();
		
		var str = "";
		if(flag == "ONU"){
			str = createBubble(o,"@ONU.RecivePowerRate@","@ONU.MinRecivePowerRate@","@ONU.MaxRecivePowerRate@");
		}else if(flag == "CATV"){
			str = createBubble(o,"@ONU.CATVRecivePowerRate@","@ONU.CATVMinRecivePowerRate@","@ONU.CATVMaxRecivePowerRate@");
		}
		if($bubbleTip.length >= 1){
			$bubbleTip.remove();	  
		}
		$("body").append(str);
		$bubbleTip = $("#bubbleTip");//需要重新获取一次;
		$bubbleTip.css({left : xpos, top : ypos});
		
		var h = $(window).height(),
		    h2 = h - ypos;
		var outH=$bubbleTip.outerHeight();
		if( h2 > $bubbleTip.outerHeight() ){
			$bubbleTip.find(".bubbleTipArr").css("top",0);	
		}else{
			$bubbleTip.find(".bubbleTipArr").css("bottom",0).addClass("bubbleTipArr2");
			$bubbleTip.css({
				top : ypos - $bubbleTip.outerHeight() + 10
			});
		}		
		if( $(this).hasClass("first") ){
			$bubbleTip.find("b").eq(0).attr("class","orangeTxt");
		}else if( $(this).hasClass("second") ){
			$bubbleTip.find("b").eq(1).attr("class","orangeTxt");
		}
	}).live("mouseleave",function(){
		var $bubbleTip = $("#bubbleTip");
		$bubbleTip.remove();
	});
}

//提示框显示光接收功率，最低光接收功率，最好光接收功率
showPowerTips("label.showBubbleTipOnu", "ONU");
//提示框显示CATV光接收功率，最低光接收功率，最好光接收功率
showPowerTips("label.showBubbleTipCATV", "CATV");

</script>
</head>
<body class="whiteToBlack">
	<div id="topPart">
		<!-- 查询DIV -->
		<div id="query-container">
			<div id="simple-toolbar-div">
				<table class="queryTable">
					<tr>
						<td><input type="text" class="normalInput" id="queryContent"
							placeHolder="@ONU.queryTip@" maxlength="63" /></td>
						<td><a id="simple-query" href="javascript:;"
							class="normalBtn" onclick="simpleQuery()"><span><i
									class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
						<td><a href="#" id="advance-query-link"
							onclick="showAdvanceQuery()">@ONU.advanceSearch@</a></td>
					</tr>
				</table>
			</div>
			<div id="advance-toolbar-div" style="display: none;">
				<table class="queryTable">
					<tr>
						<td class="rightBlueTxt">@COMMON.alias@:</td>
						<td class="pR10"><input type="text" class="normalInput"
							style="width: 150px" id="nameInput" maxlength="63" /></td>
						<td class="rightBlueTxt w50">MAC/SN:</td>
						<td class="pR10"><input type="text" class="normalInput w150"
							id="macInput" /></td>
						<td class="rightBlueTxt w80">@ONU.type2@:</td>
						<td>
							<select class="normalSel w120" id='onuEorGSelect'>
								<option value="-1">@COMMON.select@</option>
								<option value="G">GPON</option>
								<option value="E">EPON</option>
							</select>
						</td>
						<td class="rightBlueTxt w80">@ONU.ServiceLevel@:</td>
						<td>
							<select class="normalSel w120" id='onuLevel'>
								<option value="-1">@COMMON.select@</option>
								<option value="0">@ONU.Default@</option>
								<option value="1">@ONU.ImportantONU@</option>
								<option value="2">@ONU.CommonMDU@</option>
								<option value="3">@ONU.CommonSFU@</option>
							</select>
						</td>
						<td class="rightBlueTxt w50">@ONU.type@:</td>
						<td class="pR10"><input type="text" style="width: 150px"
							id="typeSelect" /></td>
						<td colspan="1"></td>
						<td valign="middle" rowspan="3" >
							<ul class="leftFloatUl">
								<li>
									<a href="javascript:;" class="normalBtn"style="margin-right: 5px;" onclick="onSeachClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></li>
								<li>
									<a href="javascript:;" style="position: relative;top:4px;" onclick="showSimpleQuery()"><span>@ccm/CCMTS.CmList.quickSearch@</span></a>
								</li>
							</ul>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">OLT:</td>
						<td class="pR10">
							<div style="width: 152px" id="oltContainer"></div>
						</td>
						<td class="rightBlueTxt">@COMMON.slot@:</td>
						<td class="pR10"><input type="text" class="" style="width: 150px" id="slotSelect" /></td>
						<td class="rightBlueTxt">@COMMON.pon@:</td>
						<td class="pR10"><input type="text" class="" style="width: 124px" id="ponSelect" /></td>
						<td class="rightBlueTxt pR10">@COMMON.status@:</td>
						<td>
							<select type="text" class="normalSel"  id="statusSelect">
							<option value="-1">@COMMON.all@</option>
							<option value="1">@COMMON.online@</option>
							<option value="2">@COMMON.offline@</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CMC/CM.partition@:</td>
						<td colspan="7"><div id="partition"></div></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div id="tip-div" class="thetips">
		<dl id="color-tips" style="margin-right:5px; padding:3px;">
			<dd class="mR2 yellow-div"></dd>
			<dd class="mR10">@COMMON.onHandling@</dd>
			<dd class="mR2 green-div"></dd>
			<dd class="mR10">@COMMON.success@</dd>
			<dd class="mR2 red-div"></dd>
			<dd class="mR10">@COMMON.fail@</dd>
		</dl>
		<dl id="operation-tips" style="padding:4px;" class="thetips">
			<dd class="mR2">@COMMON.sucNum@:</dd>
			<dd class="mR10" id="suc-num-dd">0</dd>
			<dd class="mR2">@COMMON.failNum@:</dd>
			<dd class="mR2" id="fail-num-dd">0</dd>
		</dl>
	</div>
	
	<div id="loading">@ONU.onRefreshOpt@</div>
</body>	
</Zeta:HTML>
