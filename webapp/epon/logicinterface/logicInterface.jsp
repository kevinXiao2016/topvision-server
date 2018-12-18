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
	    module logicinterface
	    IMPORT js/jquery/Nm3kTabBtn
	    IMPORT /epon/logicinterface/logicInterface
	</Zeta:Loader>
	<style type="text/css">
		body,html,#openLayer{ width:100%; height:100%; overflow:hidden;}
		#openLayer{ background:#efefef; position:absolute; top:0; left:0; z-index:9;}
	</style>
	<script type="text/javascript">
		var entityId = '${entityId}';
		var interfaceType = '${interfaceType}';
		var cm,sm,store,grid,viewPort,tbar,bbar;
		var pageSize = <%= uc.getPageSize() %>;
		var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
		Ext.onReady(function(){
			var tab1 = new Nm3kTabBtn({
			    renderTo:"topPart",
			    callBack:"clickTabBtn",
			    selectedIndex:interfaceType - 1,
			    tabs:["@interface.vlanIf@","@interface.loopBack@"]
			});
			tab1.init();
			
			var toolbarItems1 = [{
	        	xtype : 'tbtext',
	        	text  :'@interface.name@/@interface.desc@:'
	        },{
	        	xtype : 'component',
	        	html  : '<input type="text" id="name" class="normalInput w100" />'
	        },{
	        	xtype : 'tbspacer',
	        	width : 16
	        }];
			var toolbarItems2 = [{
				xtype : 'tbtext',
				text  : '@interface.adminStatus@:'
			},{
				xtype : 'component',
				html  : '<select id="adminStatus" class="normalSel w80 mT5 mB5"><option value="-1">@interface.selectStatus@</option><option value="1">@interface.adminStatusUp@</option><option value="2">@interface.adminStatusDown@</option></select>'
			},{
				xtype : 'tbspacer',
				width : 16
			},{
				xtype : 'tbtext',
				text  : '@interface.operaStatus@:'
			},{
				xtype : 'component',
				html  : '<select id="operaStatus" class="normalSel w80"><option value="-1">@interface.selectStatus@</option><option value="1">UP</option><option value="2">DOWN</option></select>'
			}]
			var toolbarItems3 = [{
	        	xtype : 'tbspacer',
	        	width : 6
	        },{
	        	text : '@COMMON.search@',
	        	iconCls : 'bmenu_find',
	        	handler : searchFn
	        },'-',{
	        	text : '@BUTTON.add@',
	        	iconCls : 'bmenu_new',
	        	handler : addFn
	        },{
	        	text : '@COMMON.delete@',
	        	iconCls : 'bmenu_delete',
	        	handler : batchDel
	        },{
	        	text : '@COMMON.fetch@',
	        	iconCls : 'bmenu_equipment',
	        	handler : refreshLogic
	    	}];
			
			var temp = toolbarItems1;
			if(interfaceType != 2){
				temp = $.merge(toolbarItems1, toolbarItems2)
			}
			var toolbarItems = $.merge(temp, toolbarItems3);
			
			tbar = new Ext.Toolbar({
			   items: toolbarItems
			});

			sm = new Ext.grid.CheckboxSelectionModel();
			cm = new Ext.grid.ColumnModel({
				defaults : {
					menuDisabled : false
				},
				columns: [
				    sm,
					{header: '<div class="txtCenter">@interface.name@</div>', align: 'left', dataIndex: 'interfaceName',sortable:true},
					{header: '<div class="txtCenter">@interface.desc@</div>', align: 'left', dataIndex: 'interfaceDesc',sortable:true},
					{header: "@interface.adminStatus@", width: 70, dataIndex: 'interfaceAdminStatus',renderer:renderAdminStatus,sortable:true},
					{header: "@interface.operaStatus@", width: 70, dataIndex: 'interfaceOperateStatus',renderer:renderOperaStatus,sortable:true},
					{header: "@interface.mac@", width: 120, dataIndex: 'interfaceMac',sortable:true},
					{header: "@interface.opera@", width: 120,renderer : manuRender}
				]
			});
			store = new Ext.data.JsonStore({
			    url: '/epon/logicinterface/loadLogicInterfaceList.tv',
			    root: 'data', 
			    totalProperty: 'rowCount',
			    remoteSort: true,
			    fields: ['entityId','interfaceType','interfaceId','interfaceIndex','interfaceName','interfaceDesc',
			             'interfaceAdminStatus','interfaceOperateStatus','interfaceMac']
			});
			bbar = new Ext.PagingToolbar({
			    id: 'extPagingBar',
			    pageSize: pageSize,
			    store: store,
			    displayInfo: true,
			    items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", pageSize), '-']
			 });
			grid = new Ext.grid.GridPanel({
			    stripeRows:true,
			    cls:"normalTable edge10 pT0",
			    bodyCssClass: 'normalTable',
		   		region: "center",
			    id: 'extGridContainer', 
			    viewConfig:{ forceFit: true },
				store: store,
				tbar : tbar,
				bbar : bbar,
				cm: cm, 
				sm: sm
			});
			viewPort = new Ext.Viewport({
			     layout: "border",
			     items: [grid,
			         {region:'north',
			         height:46,
			         contentEl :'topPart',
			         border: false,
			         cls:'clear-x-panel-body'
			     }]
			}); 
			store.load({params:{entityId: entityId,interfaceType:interfaceType,start: 0, limit: pageSize}});
			store.baseParams={
				entityId: entityId,
				interfaceType:interfaceType,
				start: 0, 
				limit: pageSize
			}
		}); //end document.ready;
		
		
		
		//查询
		function searchFn(){
			var name = $("#name").val();
			var adminStatus = $("#adminStatus").val();
			var operaStauts =  $("#operaStatus").val();
		    store.load({params:{entityId : entityId, interfaceType : interfaceType,name : name, interfaceAdminStatus: adminStatus,
				interfaceOperateStatus: operaStauts, start:0,limit:pageSize}});		
		}
		
		//添加;
		function addFn(){
			 window.top.createDialog('addLogicInterface', "@interface.addInterface@", 600, 370, '/epon/logicinterface/addLogicInterfaceView.tv?entityId='+entityId + '&interfaceType=' + interfaceType);
		}
		
		//添加;
		function editFn(entityId,interfaceType,interfaceId){
			 window.top.createDialog('editLogicInterface', "@interface.editInterface@", 600, 370, '/epon/logicinterface/editLogicInterfaceView.tv?entityId='+entityId + '&interfaceType=' + interfaceType + '&interfaceId='+interfaceId);
		}
		
		//删除
		function deleteFn(entityId,interfaceType,interfaceId){
			window.parent.showConfirmDlg("@COMMON.tip@", "@interface.delInterfaceConfirm@", function(type) {
				if (type == 'no') {
					return;
			}
			window.top.showWaitingDlg('@COMMON.wait@', '@interface.delInterface@', 'ext-mb-waiting');
			$.ajax({
				url : '/epon/logicinterface/deleteLogicInterface.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					interfaceType : interfaceType,
					interfaceId : interfaceId
				},
				success : function() {
					top.afterSaveOrDelete({
		       	      	title: "@COMMON.tip@",
		       	      	html: '<b class="orangeTxt">@interface.delInterfaceSuccess@</b>'
		       	    });
					store.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@interface.delInterfaceFail@");
				},
				cache : false
			});
			});
		}
		
		function batchDel(){
			var sm = grid.getSelectionModel();
			var selections = sm.getSelections();
			if (sm != null && sm.hasSelection()) {
				window.parent.showConfirmDlg('@COMMON.tip@', '@interface.delInterfaceConfirm@', function (type, text) {
					if (type == "no") {
						return;
					}
					var interfaceIds = []
					var selections = sm.getSelections()
					for (var i = 0; i < selections.length; i++) {
						interfaceIds[i] = selections[i].data.interfaceId
					}
					window.top.showWaitingDlg('@COMMON.wait@', '@interface.delInterface@', 'ext-mb-waiting');
					$.ajax({
						url : '/epon/logicinterface/deleteLogicInterfaceList.tv',
						type : 'POST',
						data : {
							entityId : entityId,
							interfaceType : interfaceType,
							interfaceIds : interfaceIds
						},
						success : function() {
							top.afterSaveOrDelete({
				       	      	title: "@COMMON.tip@",
				       	      	html: '<b class="orangeTxt">@interface.delInterfaceSuccess@</b>'
				       	    });
							store.reload();
						},
						error : function(json) {
							window.parent.showMessageDlg("@COMMON.tip@", "@interface.delInterfaceFail@");
						},
						cache : false
					});
				})
			} else {
				window.parent.showMessageDlg('@COMMON.tip@', '@interface.delNoSelect@');
			}
		}
		
		function reloadData(){
			store.reload();
		}
		
		//从设备获取数据
		function refreshLogic(){
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/logicinterface/refreshLogicInterface.tv',
				type : 'POST',
				data : {
					entityId : entityId
				},
				success : function() {
					top.afterSaveOrDelete({
		       	      	title: "@COMMON.tip@",
		       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
		       	    });
					store.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
				},
				cache : false
			});
		}
		
		function showLogicInterfaceIp(entityId,interfaceType,interfaceId){
			var src = '/epon/logicinterface/showInterfaceIpV4ConfigView.tv?entityId='+entityId + '&interfaceType=' + interfaceType + '&interfaceId=' + interfaceId;
			$("#openFrame").attr({src : src});
			$("#openLayer").stop().show();
		}
		
		//关闭弹出层（通常是跨iframe调用）
		function closeOpenLayer(){
			$("#openLayer").stop().hide();
		}
		function manuRender(v,m,r){
			var str1 = String.format("<a href='javascript:;' onClick='showLogicInterfaceIp({0},{1},{2})'>@interface.interfaceIp@</a>",
					   r.data.entityId, r.data.interfaceType, r.data.interfaceId),
			    str2 = String.format("<a href='javascript:;'  onClick='editFn({0},{1},{2})'>@COMMON.edit@</a>",
			    		r.data.entityId, r.data.interfaceType, r.data.interfaceId),
			    str3 = String.format("<a href='javascript:;'  onClick='deleteFn({0},{1},{2})'>@COMMON.delete@</a>",
			    		r.data.entityId, r.data.interfaceType, r.data.interfaceId);
			if(r.data.interfaceType != 3){
				return str1 + " / "+ str2 + " / "+ str3;
			}else{
				return str1 + " / "+ str3;
			}
		}
		
		function refreshFrameData(){
			window.frames["openFrame"].reloadData();
		}
	</script>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge10 pB0"></div>
	<div id="openLayer" style="display:none;">
		<iframe id="openFrame" name="openFrame" frameborder="0" width="100%" height="100%" scrolling="auto" src=""></iframe>
	</div>
</body>
</Zeta:HTML>