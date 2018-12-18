<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module epon
</Zeta:Loader>
<head>
    <script type="text/javascript" src="/js/tools/authTool.js"></script>
    <script type="text/javascript">
    	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
   		var entityId = '<s:property value="entityId"/>';
        var codeMaskGrid
        var codeMaskStore
        var instanceMaskGrid
        var instanceMaskStore
		Ext.onReady(function() {
	        codeMaskStore = new Ext.data.Store({
	            proxy: new Ext.data.HttpProxy({
	                url: '/epon/alert/loadOltAlertCodeMask.tv?entityId=' + entityId + '&r=' + Math.random()
	            }),
	            reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{name: 'topAlarmCodeMaskIndex'}, {name: 'alarmSeverity'}, {name: 'alarmName'}])),
	            sortInfo: {field: 'topAlarmCodeMaskIndex', direction: 'ASC'}
	        });
	        codeMaskStore.load();
	        codeMaskGrid = new Ext.grid.GridPanel({
	            renderTo: 'codeMaskGrid',title:'@SERVICE.typeMaskRule@',
	            
	            height: 360,  width: 384,  border: true, autoScroll: true,
	            columns: [{
	                header: I18N.SERVICE.eventId , dataIndex: 'topAlarmCodeMaskIndex', width: 100, align: 'center',sortable : true
	            }, {
	            	header: I18N.SERVICE.eventName , dataIndex: 'alarmName', width: 200, align: 'center',sortable : true
	            },  {
	            	header: I18N.COMMON.manu , dataIndex: 'id', width: 65, align: 'center',sortable : true, renderer: function(value, cellmeta, record) {
	            		if(operationDevicePower){
		            		return '<a href="javascript:;" onclick="deleteCodeMask(' + record.data.topAlarmCodeMaskIndex + ')">@COMMON.del@</a>';
	            			//return '<img src="/images/delete.gif" onclick="deleteCodeMask(' + record.data.topAlarmCodeMaskIndex + ')"/>';
	            		}else{
	            			return '<span class="disabledTxt">@COMMON.del@</span>';	            			
	            			//return "<img src='/images/noDelete.gif'/>";
	            		}
	            	}
	            }],
	            sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
	            store: codeMaskStore
	        });
	        
	        instanceMaskStore = new Ext.data.Store({
	        	proxy: new Ext.data.HttpProxy({
	        		url: '/epon/alert/loadOltAlertInstanceMask.tv?entityId=' + entityId + '&r=' + Math.random()
	        	}),
	        	reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{name: 'instanceName'},{name: 'slotNo'}, {name: 'instanceType'}, {name: 'topAlarmInstanceMaskIndex5Byte'}])),
	        	sortInfo: {field: 'instanceName', direction: 'ASC'}
	        });
	        instanceMaskStore.load();
	        instanceMaskGrid = new Ext.grid.GridPanel({
	        	renderTo: 'instanceMaskGrid',
	        	title:'	@SERVICE.entityMaskRule@',
	        	height: 360,
	        	width: 384,
	        	border: true,
	        	autoScroll: true,
	        	columns: [{
	        		header: I18N.SERVICE.eventSubs , dataIndex: 'instanceName', width: 100, align: 'center',renderer: function(value, cellmeta, record) {
	        			if(record.data.instanceType == 'SLOT' && record.data.instanceName == 0){
	        				return record.data.slotNo; 
	        			}else{
	        				return record.data.instanceName; 
		        		}
	            	}
	        	}, {
	        		header: I18N.SERVICE.eventSubsType , dataIndex: 'instanceType', width: 100, align: 'center',sortable : true
	        	}, {
	        		header: I18N.COMMON.manu , dataIndex: 'id', width: 90, align: 'center', renderer: function(value, cellmeta, record) {
	        			if(operationDevicePower){
	        				return '<a href="javascript:;" onclick="deleteInstanceMask(' + record.data.topAlarmInstanceMaskIndex5Byte + ')">@COMMON.del@</a>'; 
	        				//return '<img src="/images/delete.gif" onclick="deleteInstanceMask(' + record.data.topAlarmInstanceMaskIndex5Byte + ')"/>';
	        			}else{
	        				return '<span class="disabledTxt">@COMMON.del@</span>';	 
	        				//return "<img src='/images/noDelete.gif'/>";
	        			}
	            	}
	        	}],
	        	sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
	        	store: instanceMaskStore
	        });
		});
		function cancelClick() {
			window.parent.closeWindow('oltAlertMask')
		}
		function addCodeMask() {
			window.parent.createDialog("oltAlertCodeMask", I18N.SERVICE.entityTypeMask , 800, 500, "/epon/oltAlertCodeMask.jsp?entityId=" + entityId, null, true, true);
		}
		function deleteCodeMask(code) {
			window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.cfmDelRule ,code), function(type) {
				if (type == 'no') {
					return
				}
				window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SERVICE.delingMaskRule , code ), 'ext-mb-waiting');
				$.ajax({
					url : '/epon/alert/deleteOltAlertCodeMask.tv',
					type: 'POST',
	                data: "entityId=" + entityId + "&codeMaskIndex=" + code,
					success : function(text) {
						if (text == "success") {
							top.closeWaitingDlg();
							 top.nm3kRightClickTips({
				   				title: I18N.COMMON.tip,
				   				html: String.format(I18N.SERVICE.delMaskRuleOk ,code)
				   			 });
							//window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.delMaskRuleOk ,code))
							codeMaskStore.load();
						} else {
							window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.delMaskRuleError ,code))
						}
					},error : function() {
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.delMaskRuleError ,code))
					}
				})
			})
		}
		function addInstanceMask() {
			window.parent.createDialog("oltAlertInstanceMask", I18N.SERVICE.eventSubsMask , 800, 500, "/epon/oltAlertInstanceMask.jsp?entityId=" + entityId, null, true, true);
		}
		function deleteInstanceMask(index) {
			window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.cfmDelEntRule , function(type) {
				if (type == 'no') {
					return;
				}
				window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.delingEntRule , 'ext-mb-waiting');
				$.ajax({
					url : '/epon/alert/deleteOltAlertInstanceMask.tv',
					type: 'POST',
	                data: "entityId=" + entityId + "&instanceMaskIndex=" +index,
					success : function(text) {
						if (text == 'success') {
							top.closeWaitingDlg();
							 top.nm3kRightClickTips({
				   				title: I18N.COMMON.tip,
				   				html: I18N.SERVICE.delEntRuleOk
				   			 });
							//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.delEntRuleOk)
							instanceMaskStore.load();
						} else {
							window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.delEntRuleEr)
						}
					},error : function() {
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.delEntRuleEr)
					}
				})
			})
		}
		function refreshMaskDataFromFacility() {
			window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching , 'ext-mb-waiting');
			$.ajax({
				url : '/epon/alert/refreshMaskDataFromFacility.tv',
				type: 'POST',
                data: "entityId=" + entityId,
				success : function(text) {
					if(text == 'success'){
						top.closeWaitingDlg();
						 top.nm3kRightClickTips({
			   				title: I18N.COMMON.tip,
			   				html: I18N.COMMON.fetchOk
			   			 });
						//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk)
						window.location.reload();
					}else{
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchBad)
					}
				},error : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchBad)
				}
			})
		}
		function authLoad(){
			var ids = new Array();
			ids.add("addCodeMask");
			ids.add("addInstanceMask");
			operationAuthInit(operationDevicePower,ids);
		}
    </script>
</head>
<body class="openWinBody" onload="authLoad();">
	<div class="edge10">
		<table cellpadding="0" cellspacing="0" border="0" rules="none">
			<tr>
				<td>
					<div id="codeMaskGrid" class="normalTable"></div>
				</td>
				<td width="10"><span style="width:10px; height:10px; display:block; overflow:hidden;"></span></td>
				<td>
					<div id="instanceMaskGrid" class="normalTable"></div>
				</td>
			</tr>
		</table>
		<div class="noWidthCenterOuter">
			<ol class="upChannelListOl pB0 pT20 noWidthCenter" >
				<li><a href="javascript:;" class="normalBtnBig" onclick="refreshMaskDataFromFacility()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
				<li><a href="javascript:;" id="addCodeMask" class="normalBtnBig" onclick="addCodeMask()"><span><i class="miniIcoAdd"></i>@SERVICE.addTypeMask@</span></a></li>
				<li><a href="javascript:;" id="addInstanceMask" class="normalBtnBig" onclick="addInstanceMask()"><span><i class="miniIcoAdd"></i>@SERVICE.addEntityMask@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>			
			</ol>
		</div>
	</div>
</body>
</Zeta:HTML>