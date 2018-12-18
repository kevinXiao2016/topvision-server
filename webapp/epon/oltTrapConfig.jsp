<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module epon
	css css/white/disabledStyle
</Zeta:Loader>
<head>
	<title>@SERVICE.trapCfg@</title>
	<script type="text/javascript" src="/js/tools/authTool.js"></script>
	<script type="text/javascript">
		var entityId = <s:property value="entityId"/>;
		var trapConfigStore;
		var trapConfigGrid;
		var focusFlag = "";
		var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
		var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
		Ext.onReady(function() {
			loadGrid();
		});
		function addTrapServer() {
			window.parent.createDialog("oltTrapConfigAdd", I18N.SERVICE.trapCfgAdd , 800, 500, "/epon/oltTrapConfigAdd.jsp?entityId=" + entityId, null, true, true);
		}
		function loadGrid(){
			if(trapConfigGrid != null && trapConfigGrid != undefined){
				$("#trapConfigGrid").empty();
			}
			trapConfigStore = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({
					url: '/epon/alert/loadOltTrapConfig.tv?r=' + Math.random()
				}),
				baseParams: {entityId: entityId},
				reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{name: 'addrTAddress'},{name:'eponManagementAddrName'}, {name: 'addrTPort'}, {name: 'eponManagementAddrCommunity'}]))
			});
			
			trapConfigStore.load();
			trapConfigGrid = new Ext.grid.GridPanel({
				renderTo: 'trapConfigGrid',
				bodyCssClass: 'normalTable',
				height: 380,
				viewConfig:{
					forceFit:true
				},
				autoScroll: true,
				columns: [{
					header: I18N.SERVICE.trapServerName , dataIndex: 'eponManagementAddrName', width: 160, align: 'center'
				}, {
					header: I18N.SERVICE.trapServerAddr , dataIndex: 'addrTAddress', width: 160, align: 'center'
				}, {
					header: I18N.SERVICE.portNum , dataIndex: 'addrTPort', width: 100, align: 'center'
				}, {
					header: I18N.SERVICE.commity , dataIndex: 'eponManagementAddrCommunity', width: 100, align: 'center'
				}, {
					header: I18N.COMMON.manu , dataIndex: 'id', width: 70, align: 'center', renderer: function(value, cellmeta, record) {
						if(operationDevicePower){
							return '<a href="javascript:;" onclick="deleteTrapServer()">@COMMON.del@</a>'
							//return "<img src='/images/delete.gif' onclick='deleteTrapServer()' />";
						}else{
							//return "<img src='/images/noDelete.gif'/>"
							return '<span class="disabledTxt">@COMMON.del@</span>';
						}
					}
				}],
				sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
				store: trapConfigStore,
				listeners : {
					'viewready' : function(){
						if(focusFlag.indexOf("#") != -1){
							setTimeout(function(){
								var tmpIp = focusFlag.split("#")[0];
								var tmpPort = focusFlag.split("#")[1];
								var tmpI = -1;
								var tmpL = trapConfigStore.data.items.length;
								for(var x=0; x<tmpL; x++){
									if(trapConfigStore.data.items[x].json.addrTAddress == tmpIp && trapConfigStore.data.items[x].json.addrTPort == tmpPort){
										tmpI = x;
									}
								}
								if(tmpI != -1){
									trapConfigGrid.getSelectionModel().selectRow(tmpI, true);
									trapConfigGrid.getView().focusRow(tmpI, false);
								}
								focusFlag = "";
							}, 1000);
						}
					}
				}
			});
		}
		function deleteTrapServer() {
			setTimeout(function(){
				var sel = trapConfigGrid.getSelectionModel().getSelected();
				if(sel){
					var name = sel.data.eponManagementAddrName;
				}else{
					return;
				}
				window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.cfmDelTrap , function(type) {
					if (type == 'no') {
						return;
					}
					window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.delingTrap , 'ext-mb-waiting');
					$.ajax({
						url : '/epon/alert/deleteOltTrapConfig.tv?r=' + Math.random(),
						data: "entityId="+ entityId + "&trapNameString=" + name +"&num=" + Math.random(),
						success : function(text) {
							if(text == 'success'){
								top.closeWaitingDlg();
								 top.nm3kRightClickTips({
					   				title: I18N.COMMON.tip,
					   				html: I18N.SERVICE.delTrapOk
					   			 });
								//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.delTrapOk)
								trapConfigStore.reload();
					        }else{
					        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.delTrapError);
						    }
						},error : function() {
							window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.delTrapError)
						}
					});
				});
			}, 150);
		}
		function refreshTrapConfigFromFacility() {
			window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
			$.ajax({
				url : '/epon/alert/refreshTrapConfigFromFacility.tv?r=' + Math.random(),
				data: "entityId="+ entityId +"&num=" + Math.random(),
				success : function(text) {
					if(text == 'success'){
						top.closeWaitingDlg();
						 top.nm3kRightClickTips({
			   				title: I18N.COMMON.tip,
			   				html: I18N.COMMON.fetchOk
			   			 });
						//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk );
						window.location.reload()
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchBad);
				    }
				},error : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchBad)
				}
			});
		}
		function cancelClick() {
			window.parent.closeWindow('oltTrapConfig')
		}
		//设备操作权限---------------------------------
		function authLoad(){
			var ids = new Array();
			//ids.add("refreshTrapConfigFromFacility");
			if(!operationDevicePower){
			    $("#addTrapServer").attr("disabled",true);
			}
		    if(!refreshDevicePower){
		        $("#refreshTrapConfigFromFacility").attr("disabled",true);
		    }
		}
		//-----------------------------------------------
	</script>
</head>
<body class="openWinBody" onload="authLoad()">
	<div class="edge10">
		<div id="trapConfigGrid" class="normalTable"></div>
		<ol class="upChannelListOl pB0 pT10 noWidthCenter">
			<li><a id="refreshTrapConfigFromFacility" href="javascript:;" class="normalBtnBig" onclick="refreshTrapConfigFromFacility()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
			<li><a id="addTrapServer" href="javascript:;" class="normalBtnBig" onclick="addTrapServer()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
			<li><a id="cancelBt" href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
		</ol>
	</div>
</body>
</Zeta:HTML>