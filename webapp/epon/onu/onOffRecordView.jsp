<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
</style>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
	var onuId = '${onuId}';
	var onuIndex = '${onuIndex}';
	var entityId = '${entityId}';

	//关闭弹出框
	function closeBtClick(){
		window.parent.closeWindow('onuOnOffRecordDlog');
	}
	
	//构建分页工具栏
	function buildPagingToolBar() {
	   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:dataStore,
	       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
	   return pagingToolbar;
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/onu/onoffrecord/loadOnOffRecords.tv',
			fields : ["entityId", "onuIndex", "onuId", "onuType", "onTimeString", "offTimeString", "offReason", "collectTimeString"],
			root : 'data',
			totalProperty: 'rowCount',
			baseParams : {
				onuId : onuId,
				start : 0,
				limit : pageSize
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		 	{header : "@ONU.onTime@",width : 120,align : 'center',dataIndex : 'onTimeString'}, 
		 	{header : "@ONU.offTime@",width : 120,align : 'center',dataIndex : 'offTimeString'}, 
		 	{header : "@ONU.offReason@",width : 120,align : 'center',dataIndex : 'offReason',renderer : renderOffRenson}, 
		 	{header : "@ONU.onOffRecordCollect@", width : 120,align : 'center', dataIndex : 'collectTimeString'}
		]);
		
		window.onOffRecordGrid =  new Ext.grid.GridPanel({
			id : 'onOffRecordGrid',
			title : "@ONU.onOffRecordList@",
			height : 390,
			cls : 'normalTable',
			border : true,
			store : dataStore,
			colModel : colModel,
			bbar : buildPagingToolBar(),
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
		});		
		dataStore.load();
		
	})
	
	//从设备获取数据
	function fetchData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/onu/onoffrecord/refreshOnOffRecords.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				onuIndex : onuIndex
			},
			success : function(response) {
				if (response == "success"){
					window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
					top.afterSaveOrDelete({
						title: '@COMMON.tip@',
				        html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
				    });
					dataStore.reload();
				}else{
					window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
				}
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	
	//渲染下线原因
	function renderOffRenson(value,m,record){
		if (record.data.offTimeString == '' || record.data.offTimeString == '--'){
			return '--';
		}
		var offReason;
		switch(value)
		{
		case 0 : offReason = '--';
			break;
		case 1 : offReason = '@ONU.OFFLINE_POWER_OFF@';
			break;
		case 2 : offReason = '@ONU.OFFLINE_FIBER_BREAK@';
			break;
		case 3 : offReason = '@ONU.OFFLINE_RESET@';
			break;
		case 4 : offReason = '@ONU.OFFLINE_DEREGISTER@';
			break;
		case 5 : offReason = '@ONU.OFFLINE_OTHER@';
			break;
		}
		return offReason;
	}
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="contentGrid"></div>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT5 noWidthCenter">
	     	<li><a id="fetchData" onclick='fetchData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@ONU.fetchDataFromEquiv@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>