<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title>Cm List</title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
    library ext
    library zeta
    plugin DateTimeField
    module cmhistory
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmId = '${cmId}';
var cmIp = '${cmIp}';
var cmMac= '${cmMac}';
var store;
var grid;
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');
Ext.onReady(function() {
	$("#cmIp").html(cmIp);
	$("#cmMac").html(cmMac);
	window.stTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		startDay: 0,
		renderTo:"startTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	window.etTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		renderTo:"endTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	//构造查询方式
	nm3kPickData({
		startTime : stTime,
		endTime : etTime,
		searchFunction : searchClick
	})
	var w = $(window).width();
	var h = $(window).height();
    var	columns = [
		{header: I18N.CM.collectTime,			 width: 120, sortable:false, align: 'center', dataIndex: 'collectTimeString'},  
		{header: I18N.SYSTEM.status, 			 width: 100, sortable:false, align: 'center', dataIndex: 'statusString'},
	    {header: I18N.CCMTS.upStreamChannel,	 width: 90,	 sortable:false, align: 'center', dataIndex: 'upChannelId'},
	    {header: I18N.CM.upChannelFreq+'(MHz)',  width: 150, sortable:false, align: 'center', dataIndex: 'upChannelFreq'},
	    {header: I18N.CCMTS.downStreamChannel, 	 width: 150, sortable:false, align: 'center', dataIndex: 'downChannelId'},
	    {header: I18N.CM.downChannelFreq+'(MHz)',width: 250, sortable:false, align: 'center', dataIndex: 'downChannelFreq'},
	    {header: I18N.CM.upRecvPower+'(dBmV)',   width: 150, sortable:false, align: 'center', dataIndex: 'upRecvPower'},
	    {header: I18N.CM.upSnr+'(dB)', 			 width: 150, sortable:false, align: 'center', dataIndex: 'upSnr'},
	    {header: I18N.CM.downSnr+'(dB)',		 width: 250, sortable:false, align: 'center', dataIndex: 'downSnr'},
	    {header: I18N.CM.upSendPower+'(dBmV)',	 width: 150, sortable:false, align: 'center', dataIndex: 'upSendPower'},
	    {header: I18N.CM.downRecvPower+'(dBmV)', width: 250, sortable:false, align: 'center', dataIndex: 'downRecvPower'}
       	];
	Ext.Ajax.timeout = 90000;  
	store = new Ext.data.JsonStore({
        url: '/cmHistory/loadCmHistoryList.tv?cmId=' + cmId,
        root: 'data',
        totalProperty: 'rowCount',
        remoteSort: false, 
        fields: ['collectTimeString',       'statusString',
                 'upChannelId',    'downChannelId', 'upChannelFreq', 
                 'downChannelFreq', 'upRecvPower',     'upSnr',
                 'downSnr',    'upSendPower',	    'downRecvPower' ]
    });
	var cm = new Ext.grid.ColumnModel(columns);
	grid = new Ext.grid.GridPanel({
		cls: 'normalTable',
		region: 'center',
		border: true, 
		loadMask: true,
		viewConfig:{forceFit:false},
		totalProperty: 'rowCount',
		store: store, 
		bodyCssClass : 'normalTable',	
		cm: cm
	});
	new Ext.Viewport({
	    layout: 'border',
	    items: [
	            {
		    	 region: 'north',
		         cls:'clear-x-panel-body',
		         border: false,
		         margins: '0px',
		         autoHeight:true,
		        	contentEl: 'topPart'
		         },
		         grid
	            ]
	});
	//设置默认查询一周的数据
	stTime.setValue(lastWeek);
	etTime.setValue(currentTime);
	store.setBaseParam('startTime', lastWeek);	
	store.setBaseParam('endTime', currentTime);	
	store.load();
})

function searchClick(){
	//组装查询条件 
	var queryData = {};
	//开始时间
	queryData.startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	//结束时间
	queryData.endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	//校验
	if(queryData.startTime && queryData.endTime && queryData.startTime > queryData.endTime){
		window.parent.showMessageDlg("@COMMON.tip@", "@endTimeLessThanStartTime@");
		return;
	}
	$.extend(store.baseParams, queryData);
	store.load();
}

</script>
</head>
<body class="whiteToBlack">
<div id="topPart">	
	<table class="topSearchTable">
		<tr>
			<td class="rightBlueTxt w70">CM IP:</td>
			<td id="cmIp"></td>
			<td class="rightBlueTxt w70">CM MAC:</td>
			<td id="cmMac"></td>
			<td></td>
		</tr>
		<tr>
			<td class="rightBlueTxt w70">@startTime@</td>
			<td><div id="startTime"></div></td>
			<td class="rightBlueTxt w70">@endTime@</td>
			<td><div id="endTime"></div></td>
			<td colspan="4" style="padding-left:5px;">
				<ol class="upChannelListOl pB0">
					<li>
						<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</li>
				</ol>
			</td>
		</tr>
	</table>
</div>
<div id="grid"></div>
</body>
</Zeta:HTML>