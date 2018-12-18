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
    library Highchart
    PLUGIN Portlet
    PLUGIN Highchart-Ext
    module onu
    import js/nm3k/Nm3kClock
    import epon.onu.onuUniAction
    IMPORT epon/onu/onuDeleteTrap
    css css/white/disabledStyle
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<!-- 内置自定义js -->
<script type="text/javascript">
	var serviceMenu;
	var entityId = "${onu.parentId}";
	var onuId = "${onu.entityId}";
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	
	$(function(){
		var w = DOC.body.clientWidth - 30;
		var h = DOC.body.clientHeight - 100;
		var columns = [
       	    {header: "@ONU.portNum@",dataIndex: 'uniNo'},
       		{header: "@ONU.portStatus@",dataIndex : 'uniOperationStatus',renderer:renderOnline},//, dataIndex: 'docsIfDownChannelFrequencyForunit'},
       		{header: "@UNI.autoNegEn@",dataIndex: 'uniAutoNegotiationEnable',renderer: renderOpen},
       		{header: "@UNI.flowControlEn@",dataIndex: 'flowCtrl',renderer: renderOpen},
       		{header: "@downlink.menu.portEnable@",dataIndex: 'uniAdminStatus',renderer:renderPortAdmin},
       		{header: "@ONU.stasitcEnable@",dataIndex: 'perfStats15minuteEnable',renderer:renderPerf},
       		{header: "PVID",dataIndex: 'bindPvid'},
       		{header: "@ONU.uniMode@",dataIndex: 'profileMode',renderer:renderVlanType},
       		{header: "@COMMON.manu@", width:120 ,renderer : manuRender}
       	];

       	store = new Ext.data.JsonStore({
       	    url: '/onu/loadUniList.tv?onuId=${onuId}',
			autoLoad:true,idProperty: "uniId",
       	    fields: ["entityId","uniId","uniNo","uniOperationStatus","uniAutoNegotiationEnable","flowCtrl","uniAdminStatus",
       	       		"perfStats15minuteEnable","bindPvid","profileMode"]
       	});
       	
       	var cm = new Ext.grid.ColumnModel(columns);
       	grid = new Ext.grid.GridPanel({
       		region: 'center',
       		border: true,
       		store: store, cm: cm,
       		margins:'0px 10px 10px 10px',
       		viewConfig:{
       			forceFit:true
       		},
       		cls:'normalTable',
       		autofill:true,
       		title: "@ONU.uniList@"
       	});
       	
       	new Ext.Viewport({
       		layout: 'border',
       	    items: [{
       	        region: 'north',
       	        height: 80,
       	        border: false,
       	        contentEl: 'topPart'
       	    },grid]
       	});
	});
	
	function authLoad(){
	    if(!refreshDevicePower){
	        $("#refreshBase").attr("disabled",true);
	    }
	}
</script>
    </head>
    <body class="newBody clear-x-panel-body" onload="authLoad()">
	    <div id="topPart">
	        <div id="header">
	            <%@ include file="navigator.inc"%>
	        </div>
				
	        <div class="edge10">
				<ul class="leftFloatUl">
					<li>
						<a id="refreshBase" href="javascript:;" class="normalBtn"  onclick="refreshUniInfo()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a>
					</li>
				</ul>
			</div>
		</div>
	</body>
</Zeta:HTML> 


