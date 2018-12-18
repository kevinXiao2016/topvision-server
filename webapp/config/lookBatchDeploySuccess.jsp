<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module batchdeploy
</Zeta:Loader>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityName = '${entityName}';
var recordTime = '${recordTime}';

Ext.onReady(function(){
	var cm = new Ext.grid.ColumnModel([
		{header:"<div class='txtCenter'>@configTarget@</div>", dataIndex:"target"}
	]);//end cm;
	
	var store = new Ext.data.JsonStore({
		url:"/batchdeploy/loadSuccess.tv?batchDeployId=${batchDeployId}",
		fields: ["target"]
	});
	store.load();
	
	var grid = new Ext.grid.GridPanel({
		renderTo:"putTable",
		height:300,
		stripeRows:true,
		enableColumnMove: false,
		enableColumnResize: true,
		margins:"10px 10px 10px 10px",
		/* bbar: new Ext.PagingToolbar({
			pageSize: pageSize,
			store: store,
			displayInfo: true,
			emptyMsg: "@noresult@",
			cls: 'extPagingBar'
		}), */
		cls: 'normalTable',
		store: store,
		cm : cm,
		viewConfig:{
			forceFit: true
		}
	});	
});//end document.ready;

function cancelclick(){
	top.closeWindow("lookBatchDeploySuccess");
}
</script>
<style type="text/css">
</style>
</head>
<body class="whiteToBlack">
	<div class="openWinHeader" style="height: 80px">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@configDevice@@COMMON.maohao@</b>${entityName}</p>
	    	<p class="pT5"><b class="orangeTxt">@configTime@@COMMON.maohao@</b>${recordTime}</p>
	    </div>
	    <div class="rightCirIco wheelCirIco" style="top:15px"></div>
	</div>
	<div class="edge10" id="putTable"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
	         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelclick();"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>