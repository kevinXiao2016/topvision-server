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
    module onu
    import epon.onu.onuUniAction
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
       	    {header: "@onu/ONU.portNum@",dataIndex: 'ethAttributePortIndex'},
       		{header: "@onu/ONU.portStatus@",dataIndex : 'ethOperationStatus',renderer:renderOnline},
       		{header: "@onu/downlink.menu.portEnable@",dataIndex: 'ethAdminStatus',renderer:renderPortAdmin},
       		{header: "@onu/ONU.stasitcEnable@",dataIndex: 'ethPerfStats15minuteEnable',renderer:renderPerf},
       		{header: "@epon/REALTIME.negoMode@",dataIndex: 'ethDuplexRate',renderer:renderDuplexRate},
       		{header: "PVID",dataIndex: 'gponOnuUniPvid'},
       		{header: "@onu/ACL.priority@",dataIndex: 'gponOnuUniPri'},
       		{header: "@COMMON.manu@", width:120, renderer: renderVlanConfig}
       	];
       	store = new Ext.data.JsonStore({
       	    url: '/gpon/onu/loadGponOnuUniList.tv?onuId=${onuId}',
			autoLoad:true,idProperty: "uniId",
       	    fields: ["entityId","uniId","ethAttributePortIndex","ethAdminStatus","ethOperationStatus",
       	       		"ethDuplexRate","ethPerfStats15minuteEnable","gponOnuUniPvid","gponOnuUniPri"]
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
       		title: "@onu/ONU.uniList@"
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
	function renderVlanConfig(){
		return '<a href="javascript:;" onclick="vlanConfigFn()">@onu/EPON.vlanCfg@</a> / <a href="javascript:;" onclick="showMacFilter()">@gpon/GPON.macFilter@</a>';
	}
	function vlanConfigFn(){
		var r = grid.getSelectionModel().getSelected(),
		    uniId = r.data.uniId,
		    uniNo = r.data.ethAttributePortIndex,
		    gponOnuUniPvid = r.data.gponOnuUniPvid,
		    gponOnuUniPri = r.data.gponOnuUniPri,
		    src = String.format("/gpon/onu/showUniVlanView.tv?uniId={0}&uniNo={1}&gponOnuUniPvid={2}&gponOnuUniPri={3}&entityId={4}", uniId, uniNo, gponOnuUniPvid, gponOnuUniPri, window.entityId);
		
		top.createDialog("uniVlanConfig", "@onu/EPON.vlanCfg@", 600, 370, src, null, true, true);
	}
	function reloadData(){
		store.reload();
	}
	function showMacFilter(){
		var r = grid.getSelectionModel().getSelected(),
			uniNo = r.data.ethAttributePortIndex,
            uniId = r.data.uniId;
		var src = String.format('/gpon/unifiltermac/showGponUniFilterMac.tv?uniId={0}&onuId={1}&uniNo={2}', uniId, window.onuId, uniNo);
		top.createDialog("modalDlg", "@gpon/GPON.macFilter@", 800, 500, src, null, true, true);
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


