<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module cmc
</Zeta:Loader>
<head>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var channelIndex = '<s:property value="channelIndex"/>';
var cmcMac = '<s:property value="cmcMac"/>';
var channelId = '<s:property value="channelId"/>';

function renderPercentFormat(value, p, record){
	if(value){
		return value + '%';
	}else{
		return '-';
	}
}

function renderPolicy(value, p, record){
	switch(value){
		case 1: return "@CMC.GP.freqOnly@";
		case 2: return "@CMC.GP.widthOnly@";
		case 3: return "@CMC.GP.modulationOnly@";
		case 4: return "@CMC.GP.freqWidthOnly@";
		case 5: return "@CMC.GP.priority@";
		default: return "-";
	}
}

function renderSelect1st(value, p, record){
	switch(value){
		case 1: return "@CMC.GP.modulation@";
		case 2: return "@CMC.GP.width@";
		default: return "-";
	}
}

function renderSelect2st(value, p, record){
	switch(value){
		case 1: return "@CMC.GP.modulation@";
		case 2: return "@CMC.GP.width@";
		case 3: return "@CMC.GP.frequency@"
		default: return "-";
	}
}

function renderSelect3st(value, p, record){
	switch(value){
		case 1: return "@CMC.GP.modulation@";
		case 2: return "@CMC.GP.frequency@";
		default: return "-";
	}
}

//close the dialog
function cancelClick() {
	window.parent.closeWindow('chnlHopHis');
}

//get channel HopHis from device
function refreshHopHis(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/ccmtsspectrumgp/refreshGpHopHisFromDevice.tv?r=' + Math.random(),
		data : {
			entityId : entityId,
			channelIndex : channelIndex,
			cmcMac : cmcMac,
			channelId : channelId
		},
		success : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
			window.location.reload();
		},
		error : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		}
	});
}

//clear channel HopHis
function clearClick(){
	window.top.showWaitingDlg("@COMMON.wait@", "@CMC.GP.clearing@", 'ext-mb-waiting');
	$.ajax({
		url : '/ccmtsspectrumgp/deleteGroupHopHis.tv?r=' + Math.random(),
		data : {
			entityId : entityId,
			channelIndex : channelIndex,
			cmcMac : cmcMac,
			channelId : channelId
		},
		success : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@CMC.GP.clearSuccess@");
			window.location.reload();
		},
		error : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@CMC.GP.clearFail@");
		}
	});
}

$(function(){
	window.dataStore = new Ext.data.JsonStore({
		url : '/ccmtsspectrumgp/loadGroupHopHisList.tv',
		baseParams : {
			entityId : entityId,
			channelIndex : channelIndex
		},
		fields :  ['entityId','channelIndex','cmcMac','chnlId','hisIndex','hisSelect1st','hisSelect2st','hisSelect3st','hisPolicy','hisGroupId',
		           'hisMaxHop','hisFrequency','hisWidth','hisPower','hisSnr','hisCorrect','hisUnCorrect','lastHopTimeYMD','lastHopTime']	
	});
	dataStore.setDefaultSort('hisIndex', 'ASC');
	//load data from proxy
	dataStore.load();

	window.colModel = new Ext.grid.ColumnModel([
       	{header: "@CMC.GP.hisIndex@", width: 50, sortable: true, align: 'center', dataIndex: 'hisIndex'},	
  		{header: "@CMC.GP.groupId@", width: 60, sortable: false, align: 'center', dataIndex: 'hisGroupId'},
  		{header: "@CMC.GP.gpCorrect@", width: 60, sortable:false, align : 'center', dataIndex: 'hisCorrect',renderer: renderPercentFormat},
  		{header: "@CMC.GP.gpUnCorrect@", width: 70, sortable:false, align : 'center', dataIndex: 'hisUnCorrect',renderer: renderPercentFormat},
  		{header: "@CMC.GP.hopPolicy@", width: 110, sortable: false, align: 'center', dataIndex: 'hisPolicy',renderer: renderPolicy},
  		{header: "@CMC.GP.Priority1st@", width: 90, sortable:false, align : 'center', dataIndex: 'hisSelect1st',renderer: renderSelect1st},		
       	{header: "@CMC.GP.Priority2st@", width: 90, sortable:false, align: 'center', dataIndex: 'hisSelect2st',renderer: renderSelect2st},
       	{header: "@CMC.GP.Priority3st@", width: 90, sortable:false, align: 'center', dataIndex: 'hisSelect3st',renderer: renderSelect3st},
  		{header: "@CMC.GP.lastHopTime@", width: 140, sortable:false, align: 'center', dataIndex: 'lastHopTime'}
   	]);
	
	window.trapServerGrid = new Ext.grid.GridPanel({
		id : 'hophisGrid',
		renderTo : 'hophisRecord',
		height : 380,
		width : 766,
		store : dataStore,
		bodyCssClass:'normalTable',
		colModel : colModel,
		viewConfig: {
	        forceFit: true
		}
	});
});
</script>
</head>
<body class="openWinBody">
	<div style="padding:16px">
	
		<div id="hophisRecord" ></div>
		
		<div class="noWidthCenterOuter clearBoth">
			 <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				<li><a href="javascript:;" class="normalBtnBig" onclick="refreshHopHis();"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="clearClick();"><span><i class="miniIcoEquipment"></i>@CMC.GP.clearHopHis@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span>@COMMON.close@</span></a></li>
			</ol>
		</div>
	
	</div>
</body>
</Zeta:HTML>