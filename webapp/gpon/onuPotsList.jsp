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
    module gpon
    import js/customColumnModel
    import gpon.onuPotsList
    import gpon/customGponOnu
    IMPORT epon/onu/onuDeleteTrap
    css css/white/disabledStyle
</Zeta:Loader>
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
       	    {header: "@GPON.portNum@",sortable:true,dataIndex: 'topSIPPstnUserPotsIdx',align:'center'},
       		{header: "@onu/EPON.portEnable@",sortable:true,dataIndex : 'topOnuIfPotsAdminState',align:'center',renderer:renderPortAdmin},
       		{header: "@GPON.ipIndx@",sortable:true,dataIndex: 'topGponSrvPotsInfoPotsIdx',align:'center',renderer:renderIpIndx},
       		{header: "@GPON.ipAddress@",sortable:true,dataIndex: 'topGponSrvPotsInfoIpIdx',align:'center',renderer:renderIpAddress},
       	    {header: "@GPON.VLANpriority@", width:100, dataIndex: "onuIpHostVlanTagPriority", align: "center",renderer:renderVlanTagPriority},
       	    {header: "<div class=txtCenter>VLAN ID</div>", width:100, dataIndex: "onuIpHostVlanPVid", align: "center",renderer:renderVlanPVid},
       		{header: "@onuvoip.sipServicesDataTemplate@",sortable:true,align:'center',dataIndex: 'topSIPPstnUserSipsrvId'},
       		{header: "@onuvoip.digitalChartTemplate@",sortable:true,align:'center',dataIndex: 'topSIPPstnUserDigitmapId'},
       		{header: "@onuvoip.sipUserphone@",sortable:true,align:'center',dataIndex: 'topSIPPstnUserTelno'},
       		{header: "@onuvoip.callTransferType@",sortable:true,dataIndex: 'topSIPPstnUserForwardType',align:'center',renderer:renderForwardType},
       		{header: "@onuvoip.callTransferNumber@",sortable:true,dataIndex: 'topSIPPstnUserTransferNum',align:'center'},
       		{header: "@onuvoip.callForwardTime@",sortable:true,dataIndex: 'topSIPPstnUserForwardTime',align:'center',renderer:renderForwardTime},
       		{header: "@onuvoip.EncodingMode@",sortable:true,dataIndex: 'topVoIPLineCodec',align:'center',renderer:renderCodec},
       		{header: "@onuvoip.voipServicestate@",sortable:true,dataIndex: 'topVoIPLineServStatus',align:'center',renderer:renderServStatus},
       		{header: "@onuvoip.Sessiontype@",sortable:true,dataIndex: 'topVoIPLineSessType',align:'center',renderer:renderSessType},
       		{header: "@onuvoip.voiplineStatus@",sortable:true,dataIndex: 'topVoIPLineState',align:'center',renderer:renderLineState},
       		{header: "@COMMON.manu@",sortable:true, width:120, renderer: renderUserConfig}
       	];
       	store = new Ext.data.JsonStore({
       	    url: '/gpon/onuvoip/loadGponOnuPotsList.tv?onuId=${onuId}',
       	    autoLoad:true,
			idProperty: "topSIPPstnUserPotsIdx",
       	    fields: ["entityId","onuId","topSIPPstnUserPotsIdx","topOnuIfPotsAdminState","topGponSrvPotsInfoPotsIdx","topGponSrvPotsInfoIpIdx",
       	            "onuIpHostVlanTagPriority","onuIpHostVlanPVid",
       	       		"topSIPPstnUserSipsrvId","topSIPPstnUserDigitmapId","topSIPPstnUserTelno","topSIPPstnUserForwardType",
       	       		"topSIPPstnUserTransferNum","topSIPPstnUserForwardTime","topVoIPLineCodec","topVoIPLineServStatus","topVoIPLineSessType","topVoIPLineState"]
       	});
       	/* var cm = new Ext.grid.ColumnModel({
       		defaults : {
                menuDisabled : false
            },
            columns:columns
       	}); */
       	var cmConfig = CustomColumnModel.init("gponPotsList",columns,{}),
            cm = cmConfig.cm;
            /* sortInfo = cmConfig.sortInfo || {field: 'topSIPPstnUserPotsIdx', direction: 'ASC'};
            
        store.setDefaultSort(sortInfo.field, sortInfo.direction); */
        
       	grid = new Ext.grid.GridPanel({
       		region: 'center',
       		border: true,
       		store: store, 
       		cm: cm,
       		margins:'0px 10px 10px 10px',
       		/* viewConfig:{
       			forceFit:true
       		}, */
       		cls:'normalTable',
       		autofill:true,
       		title: "@GPON.potsList@",
       		enableColumnMove : true,
       		listeners : {
                columnresize: function(){
                    CustomColumnModel.saveCustom('gponPotsList', cm.columns);
                },
                sortchange : function(grid,sortInfo){
                    CustomColumnModel.saveSortInfo('gponPotsList', sortInfo);
                }
            },
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
	function renderUserConfig(){
		return '<a href="javascript:;" onclick="userConfigFn()">@onuvoip.userConfig@</a>';
	}
	function userConfigFn(){
		var r = grid.getSelectionModel().getSelected(),
		    onuId = r.data.onuId,
		    entityId = r.data.entityId,
		    topSIPPstnUserPotsIdx = r.data.topSIPPstnUserPotsIdx,
		    src = String.format("/gpon/onuvoip/showPotsConfigView.tv?onuId={0}&entityId={1}&topSIPPstnUserPotsIdx={2}", onuId, entityId, topSIPPstnUserPotsIdx);
		
		top.createDialog("potsUserConfig", "@onuvoip.userConfig@", 800, 500, src, null, true, true);
	}
	function reloadData(){
		store.reload();
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
						<a id="refreshBase" href="javascript:;" class="normalBtn"  onclick="refreshPotsInfo()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a>
					</li>
				</ul>
			</div>
		</div>
	</body>
</Zeta:HTML> 


