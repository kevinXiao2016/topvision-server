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
    import tools/cmdUtil
    import js.tools.authTool
    import epon.onuDeviceView
    import epon.onu.onuIndexPartition
    import epon.onu.js.onuListTrapAction
    import epon.onu.js.onuListCommon
    import epon.onu.js.onuListRender
    import epon.js.columnUtil
    import js.zetaframework.component.NetworkNodeSelector static
    import js/raphael/raphael
</Zeta:Loader>
</head>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var onuEnvironment = '${onuEnvironment}';//onu 环境 ---纯epon，纯gpon或者混合环境
var onuLinkThresholdJson = '${onuLinkThresholdJson}';
var onuLinkThreshold = JSON.parse( onuLinkThresholdJson );

//查询 
function onSeachClick() {
	var name = $("#nameInput").val();
	var mac = $("#macInput").val();
	var onuEorG = $("#ponPattern").val() == -1?null:$("#ponPattern").val();
	var type = typeCombo.getValue();
	var entityId =  $("#oltSelect").val();
	var slotId = slotCombo.getValue();
	var ponId = ponCombo.getValue();
	var status = $("#statusSelect").val();
	var param = {onuName: name,onuPreType: type, entityId : entityId, macAddress : mac, status: status,
			onuEorG: onuEorG, slotId : slotId, ponId: ponId, start:0,limit:pageSize}; 
	param = Ext.apply(param);
    store.baseParams=param;
    store.load();
}

/**
 * 简单查询
 */
function simpleQuery(){
	var queryContent = $("#queryContent").val();
	store.baseParams={
		queryContent: queryContent,
		start:0,
		limit:pageSize
	};
	store.load({
	    callback: function(){
	    	clearSelect(grid);
		}
	});
}

/**
 * 高级查询
 */
function showAdvanceQuery() {
	var cssSelectors = ['td.noOnuTag', 'tr.noPartition'];
	showCommonAdvanceQuery(cssSelectors);
}

function refresh(callback){
	if(callback){
		store.reload({
			callback : callback
		});
	}else{
		store.reload();
	}
}


$(function(){
	//提示框显示onu最近8条上下线记录
	showOnuOnoffRecordTips("label.onuOnOffRecordsTip");
	//右边栏关联信息
	initAssociatePart();
});

</script>
<body class="whiteToBlack">
	<%@ include file="onuListViewTop.inc"%>
	
	<div id="loading">@ONU.onRefreshOpt@</div>
	
	<%@ include file="onuAssociatedInfo.inc"%>
</body>	
</Zeta:HTML>