<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
	<%@include file="../include/ZetaUtil.inc"%>
	<Zeta:Loader>
		library ext
	    library jquery
	    library zeta
	    module cmc
	    import js.EntityType
	    import cm.util.cmUtil
	</Zeta:Loader>
	<style type="text/css">
		body,html{ height:100%;}
	</style>
	<script type="text/javascript">
		Ext.QuickTips.init();
		var pageSize = <%= uc.getPageSize() %>;
		var grid = null;
		var store = null;
		var pagingToolbar = null;
		var folderId = '${folderId}';
		var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
		
		$(function(){
			var cm =  new Ext.grid.ColumnModel([
	            {header: "<div class='txtCenter'>IP</div>", width: 120, sortable: true, align: 'center', dataIndex: 'displayIp', renderer: renderIp}, 
	            {header: "MAC", width: 120, sortable: true, align: 'center', dataIndex: 'statusMacAddress', renderer: renderMac}, 
	            {header: "@CMC.title.status@", width: 60, sortable: false, align: 'center', dataIndex: 'statusValue', renderer: CmUtil.statusValueRender}, 
	            {header: "<div class='txtCenter'>@CMCPE.upDevice@</div>", width: 120, sortable: true, align: 'center', dataIndex: 'cmcName', renderer:renderCmc}, 
	            {header: "@upchannel@", width: 60, sortable: true, align: 'center', dataIndex: 'upChannelId', renderer: renderUpChannel}, 
	            {header: "@CCMTS.downStreamChannel@", width: 60, sortable: true, align: 'center', dataIndex: 'downChannelId',renderer: renderDownChannel}, 
	            {header: '@CMC.title.Alias@', sortable: true, align: 'center', dataIndex: 'cmAlias'}, 
	            {header: '@CMC.title.usage@', sortable: true, align: 'center', dataIndex: 'cmClassified'}
			]);
		   
			store = new Ext.data.JsonStore({
			    url: '/cmtopofolder/loadCmListByFolder.tv',
			    root: 'data',
			    totalProperty: 'rowCount',
			    remoteSort: true,
			    fields: ['displayIp','statusMacAddress','statusValue','cmcName','upChannelId','downChannelId','cmAlias','cmClassified','cmcId','cmcDeviceStyle','cmcName','cmcIp']
			});
		         
		   	grid = new Ext.grid.GridPanel({
		   		stripeRows:true,
		   		region: 'center',
		   		bodyCssClass: 'normalTable',
		   		border:false,
		   		store: store,
		   		cm : cm,
		   		bbar: buildPagingToolBar(),
		   		viewConfig:{
		   			forceFit: true
		   		}
		   	});
		  	//默认选中根结点并加载所有的数据
			loadData(folderId);
		  	
			new Ext.Viewport({
			    layout: 'border',
			    items: [grid]
			});
		});

		
		//*****-------------Render方法---------------
		function renderIp(value, p, record) {
	 		if (value === null || value === "" || value === "noSuchObject" || value === "noSuchInstance" || value === "0.0.0.0" || value==='--') {
		  		return "--"
	  		} else if(record.data.statusValue != '6'&&record.data.statusValue != '21'&&record.data.statusValue != '26'&&record.data.statusValue != '27'&&record.data.statusValue != '30'&&record.data.statusValue != '31') {
		  		return value;
	  		} else {
				//cm单机Web的跳转
				return String.format('<a href="http://{0}" target="_blank">{0}</a>', value);
	  		}
		}

		function renderMac(value, p, record) {
			if (value != "") {
			    return String.format('<a href="javascript:;" onclick="showCmDetail(\'{0}\')">{0}</a>', value);
			} else {
			   return value;
			}
		}
		
		function renderCmc(value, p, record) {
			if (value != "") {
				var cmcNameWithIpFormatStr = '<a href="javascript:;" onclick="showCmcSnap({2},\'{0}\', {3})" class="yellowLink" title="{0}">{1}({0})</a>';
			    return String.format(cmcNameWithIpFormatStr,  record.data.cmcName,  record.data.cmcIp, record.data.cmcId, record.data.cmcDeviceStyle);
			} else {
			   return value;
			}
		}
		
		function showCmcSnap(cmcId, entityName, cmcDeviceStyle) {
			  if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
			    window.top.addView('entity-' + cmcId, entityName, 'entityTabIcon',
			      '/cmc/showCmcPortal.tv?cmcId=' + cmcId + "&productType=" + cmcDeviceStyle);
			  } else if (EntityType.isCcmtsWithoutAgentType(cmcDeviceStyle)){
			    window.top.addView('entity-' + cmcId, entityName, 'entityTabIcon',
			      '/cmc/showCmcPortal.tv?cmcId=' + cmcId);
			  }else{
				  window.top.addView('entity-' + cmcId, entityName, 'entityTabIcon', '/cmts/showCmtsPortal.tv?cmcId=' + cmcId)
			  }
			}

		function renderUpChannel(value, p, record) {
			  return String.format('<span class="cirBoxBg">{0}</span>', record.data.upChannelId);
		}

		function renderDownChannel(value, p, record) {
			  return String.format('<span class="cirBoxBg">{0}</span>', record.data.downChannelId);
		}
		//-------------Render方法---------------*****
		
		function showCmDetail(macAddr) {
			var sm = grid.getSelectionModel(),
			    record = sm.getSelected();
			if (record == null) {
				return;
			}
			var cmMac = macAddr || record.data.statusMacAddress;
			window.top.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
		}
		
		function loadData(folderId){
			store.setBaseParam("folderId", folderId);
			store.load({params:{start: 0, limit: pageSize}});
		}
		
		function buildPagingToolBar() {
			var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
		       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
		                       		]});
		    return pagingToolbar;
		}
		
		function refresh(){
			store.reload();
		}
	</script>
</head>

<body class="bodyWH100percent bodyGrayBg">
</body>
</Zeta:HTML>
