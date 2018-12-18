<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CM
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cm.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
	var store = null;
	var grid = null;
	var SelectionModel = null
	function onRefreshClick() {
		store.reload();
	}
	function operationRender(value, cellmate, record) {
		var quotaId = record.data.quotaId;
		return String.format("<a href='javascript:;'  onclick='showQuotaConfig(\"{0}\")'>@cmPoll.thresholdConfig@</a>", quotaId);
	}
	
	function thresholdRangeRender(value, cellmate, record) {
		var quotaId = record.data.quotaId;
		var quotaMax = record.data.quotaMax;
		var quotaMin = record.data.quotaMin;
		return quotaMin + '-' + quotaMax;
	}
	
	function tooHighRangeRender(value, cellmate, record) {
		var quotaId = record.data.quotaId;
		var quotaMax = record.data.quotaMax;
		var tooHigh = record.data.tooHigh;
		var tooHighStatus = record.data.tooHighStatus;
		if(!tooHighStatus){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(tooHigh == quotaMax){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(quotaId == 4){
			if(tooHigh > -15){
				if(quotaMax < 0){
					return tooHigh + '-(' + quotaMax + ')';
				}else{
					return tooHigh + '-' + quotaMax;
				}
			}else{
				return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
			}
		}
		return tooHigh + '-' + quotaMax;
	}
	
	function highRangeRender(value, cellmate, record) {
		var quotaId = record.data.quotaId;
		var high = record.data.high;
		var tooHigh = record.data.tooHigh;
		var highStatus = record.data.highStatus;
		var tooHighStatus = record.data.tooHighStatus;
		var quotaMax = record.data.quotaMax;
		if(!highStatus){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(!tooHighStatus){
			tooHigh = quotaMax
		}
		if(high == tooHigh){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(quotaId == 4){
			if(high > -15){
				if(tooHigh < 0){
					return high + '-(' + tooHigh + ')';
				}else{
					return high + '-' + tooHigh;
				}
			}else{
				return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
			}
		}
		return high + '-' + tooHigh;
	}
	
	function lowerRangeRender(value, cellmate, record) {
		var quotaId = record.data.quotaId;
		var lower = record.data.lower;
		var tooLower = record.data.tooLower;
		var lowerStatus = record.data.lowerStatus;
		var tooLowerStatus = record.data.tooLowerStatus;
		var quotaMin = record.data.quotaMin;
		if(!lowerStatus){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(!tooLowerStatus){
			tooLower = quotaMin
		}
		if(tooLower == lower){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(quotaId == 4){
			if(lower > -15){
				if(lower < 0){
					return tooLower + '-(' + lower + ')';
				}else{
					return tooLower + '-' + lower;
				}
			}else{
				return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
			}
		}
		return tooLower + '-' + lower;
	}
	
	function tooLowerRangeRender(value, cellmate, record) {
		var quotaId = record.data.quotaId;
		var tooLower = record.data.tooLower;
		var quotaMin = record.data.quotaMin;
		var tooLowerStatus = record.data.tooLowerStatus;
		
		if(!tooLowerStatus){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(quotaMin == tooLower){
			return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
		}
		if(quotaId == 4){
			if(tooLower > -15){
				if(tooLower < 0){
					return quotaMin + '-(' + tooLower + ')';
				}else{
					return quotaMin + '-' + tooLower;
				}
			}else{
				return String.format('<img class="switch" title="" src="/images/performance/off2.png"  border=0 align=absmiddle>');
			}
		}
		return quotaMin + '-' + tooLower;
	}
	
	function showQuotaConfig(quotaId) {
		var sm = grid.getSelectionModel();
		var record = sm.getSelected();	
		var quotaId = record.data.quotaId;
		var quotaDisplayName = record.data.quotaDisplayName.replace('%', '%25');
		var quotaMax = record.data.quotaMax;
		var quotaMin = record.data.quotaMin;
		window.top.createDialog('modifyCmPoll', I18N.cmPoll.thresholdConfig, 600, 410,
				'/cmpoll/showConfigPollQuota.tv?quotaId=' + quotaId + '&quotaDisplayName=' + quotaDisplayName 
						+ '&quotaMax=' + quotaMax + '&quotaMin=' + quotaMin, null, true, true);
	}
	
	Ext.onReady(function() {
		var w = document.body.clientWidth;
		var h = document.body.clientHeight;
		SelectionModel =  new Ext.grid.CheckboxSelectionModel({
			header:'<div class="x-grid3-hd-checker"></div>',
			singleSelect:false
		});
		var columns = [ {
			header : '<div class="txtCenter">' + I18N.cmPoll.threshold + '</div>',
			width : w / 4,
			sortable : false,
			align : 'left',
			dataIndex : 'quotaDisplayName'
		}, {
			header : I18N.cmPoll.thresholdPeriod,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'thresholdRange',
			renderer : thresholdRangeRender
		}, {
			header : I18N.cmPoll.tooHighDangerAlertPeriod,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'thresholdRange',
			renderer : tooHighRangeRender
		}, {
			header : I18N.cmPoll.tooHighAlertPeriod,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'thresholdRange',
			renderer : highRangeRender
		}, {
			header : I18N.cmPoll.tooLowAlertPeriod,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'thresholdRange',
			renderer : lowerRangeRender
		}, {
			header : I18N.cmPoll.tooLowDangerAlertPeriod,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'thresholdRange',
			renderer : tooLowerRangeRender
		},{
			header : I18N.Config.oltConfigFileImported.operation,
			width : w / 10,
			sortable : false,
			align : 'center',
			dataIndex : 'op',
			renderer : operationRender
		} ];
		store = new Ext.data.JsonStore({
			url : '/cmpoll/getCmPollQuota.tv',
			root : 'data',
			totalProperty : 'rowCount',
			fields : [ 'quotaId', 'quotaName', 'quotaDisplayName', 'quotaMax', 'quotaMin', 'tooHigh', 'high', 'lower', 'tooLower', 'tooHighStatus', 'highStatus', 'lowerStatus', 'tooLowerStatus']
		});

		var cm = new Ext.grid.ColumnModel(columns);
		var toolbar = [{
			text : I18N.MenuItem.onRefreshClick,
			iconCls : 'bmenu_refresh',
			handler : onRefreshClick
		}];
		grid = new Ext.grid.GridPanel({
			stripeRows:true,
	   		region: "center",
	   		bodyCssClass: 'normalTable',
			id : 'pollTaskGrid',
			sm:SelectionModel,
			border : false,
			region : 'center',
			store : store,
			tbar : toolbar,
			cm : cm,
			loadMask : true, 
			viewConfig : {
				hideGroupedColumn : true,forceFit: true ,enableNoGroups : true
			}
		});
		new Ext.Viewport({
	 	     layout: "border", items: [grid]
	 	});
		store.load();
	})
</script>
</head>
<body></body>
</Zeta:HTML>
