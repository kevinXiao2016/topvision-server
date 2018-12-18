<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<link rel="stylesheet" href="/epon/css/onuList.css" />
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
    import js/customColumnModel
    import performance/js/jquery-ui-1.10.3.custom.min
    import epon.onuLinkView
    import js.tools.authTool
    import epon.js.columnUtil
    import epon.onu.onuIndexPartition
    import js.zetaframework.component.NetworkNodeSelector static
    import epon.onu.js.onuListTrapAction
    import epon.onu.js.onuListCommon
    import epon.onu.js.onuListRender
    import js/raphael/raphael
</Zeta:Loader>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var onuEnvironment = '${onuEnvironment}';
var onuLinkThresholdJson = '${onuLinkThresholdJson}';
var onuLinkThreshold = JSON.parse( onuLinkThresholdJson );

function refresh(callback){
	if(callback){
		store.reload({
			callback : callback
		});
	}else{
		store.reload();
	}
}

//查询 
function onSeachClick() {
	var name = $("#nameInput").val();
	var mac = $("#macInput").val();
	var status = $("#statusSelect").val();
	var type = typeCombo.getValue();
	var entityId =  $("#oltSelect").val();
	var slotId = slotCombo.getValue();
	var ponId = ponCombo.getValue();
	var onuEorG = $("#ponPattern").val() == -1?null:$("#ponPattern").val();
	var param = {onuName: name,onuPreType: type, entityId : entityId, macAddress : mac, status: status,
			onuEorG: onuEorG,slotId : slotId, ponId: ponId, start:0,limit:pageSize}; 
	param = Ext.apply(param,partitionData);
    store.baseParams=param;
    //在执行完相关操作后去掉grid表头上的复选框选中状态
	grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
    store.load({
    	callback: function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
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
		}
    });
}

function showAdvanceQuery() {
	var cssSelectors = ['td.noOnuTag'];
	showCommonAdvanceQuery(cssSelectors);
}
	
$(function(){
	showOnuOnoffRecordTips("label.onuOnOffRecordsTip");
	initAssociatePart();
});
</script>
</head>
<body class="whiteToBlack">
	<%@ include file="onuListViewTop.inc"%>
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
	
	<%@ include file="onuAssociatedInfo.inc"%>
</body>	
</Zeta:HTML>
