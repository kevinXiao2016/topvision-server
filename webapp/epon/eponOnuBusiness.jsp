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
    import epon.eponOnuBusiness
    import epon.onu.onuIndexPartition
    import epon.onu.js.onuListCommon
    import epon.onu.js.onuListRender
    import epon.js.columnUtil
    import js.zetaframework.component.NetworkNodeSelector static
    import epon.onu.js.onuListTrapAction
    import js/raphael/raphael
</Zeta:Loader>
<script type="text/javascript" src="/js/ext/ux/RowExpander.js"></script>
<link rel="stylesheet" href="/js/ext/ux/RowExpander.css" />
</head>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var onuLinkThresholdJson = '${onuLinkThresholdJson}';
var onuLinkThreshold = JSON.parse( onuLinkThresholdJson );

//查询 
function onSeachClick() {
	var name = $("#nameInput").val();
	var mac = $("#macInput").val();
	var tagId = tagCombo.getValue();
	var type = typeCombo.getValue();
	var entityId =  $("#oltSelect").val();
	var slotId = slotCombo.getValue();
	var ponId = ponCombo.getValue();
	var status = $("#statusSelect").val();
	var param = {onuName: name,onuPreType: type, entityId : entityId, macAddress : mac, status: status,
			tagId: tagId, slotId : slotId, ponId: ponId, start:0,limit:pageSize}; 
	param = Ext.apply(param);
    store.baseParams=param;
    store.load({
    	callback: collapseRow
    });
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
		callback: collapseRow
	});
}

/**
 * 高级查询
 */
function showAdvanceQuery() {
	var cssSelectors = ['td.noPonPattern', 'tr.noPartition'];
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

/**
 * 收起expander
 */
function collapseRow(){
	var rowIndexs = getSelectedRowIndexs(grid);
	if(expander){
		$.each(rowIndexs, function(index, row){
			if(expander.isExpand(row)){
				expander.collapseRow(row);
			}
		});
	}
}

$(function(){
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