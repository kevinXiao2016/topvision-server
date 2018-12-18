<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    css css/white/disabledStyle
    import network.entityAction
    import js.tools.authTool
    import js.zetaframework.component.NetworkNodeSelector static
</Zeta:Loader>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
	var contextMenu = null;
	var entityMenu = null;
	var grid = null;
	var store = null;
	var pagingToolbar = null;
	var sm;
	var pageSize = <%=uc.getPageSize()%>;
	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	
	$(function(){
		//ONU MAC认证失败; 
		var onu_mac_aith_fail = top.PubSub.on(top.OltTrapTypeId.ONU_MAC_AUTH_FAIL, function(data){
			trapStoreReload(data);
		});	
		
		$(window).on('unload', function(){
			top.PubSub.off(top.OltTrapTypeId.ONU_MAC_AUTH_FAIL, onu_mac_aith_fail);
		});
	});
	function trapStoreReload(data){
		if(store){
			store.reload();
		}
	}
	
	
	function renderIndex(value, p, record) {
		var t = parseInt(value / 256) + (value % 256);
		return getNum(t, 1) + "/" + getNum(t, 2);
	}

	function renderAuthTime(value, p, record) {
		var timeString;
		if (value != null && value != -1) {
			timeString = arrive_timer_format(value)
		}
		return timeString;
	}
	function arrive_timer_format(s) {
		var t
		if (s > -1) {
			hour = Math.floor(s / 360000);
			min = Math.floor(s / 6000) % 60;
			sec = Math.floor(s / 100) % 60;
			day = parseInt(hour / 24);
			if (day > 0) {
				hour = hour - 24 * day
				t = day + I18N.COMMON.D + hour + I18N.COMMON.H
			} else {
				t = hour + I18N.COMMON.H
			}
			if (min < 10) {
				t += "0"
			}
			t += min + I18N.COMMON.M
			if (sec < 10) {
				t += "0"
			}
			t += sec + I18N.COMMON.S
		}
		return t + '@COMMON.ago@'
	}

	function getNum(index, s) {
		var num;
		switch (s) {
		case 1:
			num = (index & 0xFF000000) >> 24;//SLOT
			break;
		case 2:
			num = (index & 0xFF0000) >> 16;//PON/SNI
			break;
		case 3:
			num = (index & 0xFF00) >> 8;//ONU
			break;
		case 4:
			num = index & 0xFF;//UNI
			break;
		}
		return num;
	}

	function addOnuAuth() {
		var record = grid.getSelectionModel().getSelected();
		var entityId = record.data.entityId;
		var ponIndex = record.data.ponIndex;
		var onuIndex = record.data.onuIndex;
		var mac = record.data.mac;
		var sn = record.data.sn;
		var password = record.data.password;
		window.top
				.createDialog('addOnuAuth', "@onuAuth.addAuth@", 600, 500,
						'/epon/onuauth/showAddOnuAuth.tv?entityId=' + entityId
								+ '&ponIndex=' + ponIndex + '&onuIndex='
								+ onuIndex + '&mac=' + mac + '&sn=' + sn
								+ '&password=' + password, null, true, true);
	}
	
	function addAuthList(){
		if(sm.getSelections().length==0){
	        window.top.showMessageDlg(I18N.COMMON.tip, "@network/offManagement.selectDevice@");
	        return;
	    }
		
		var rs=sm.getSelections();
	    var entityIds=[];
	    var onuIndexs=[];
	    for(var i = 0; i < rs.length; i++){
	    	entityIds[i]=rs[i].data.entityId;
	    	onuIndexs[i]=rs[i].data.onuIndex;
	    }
		window.top.createDialog('addOnuAuth', "@onuAuth.addAuth@", 850, 500,
				'/epon/onuauth/showAddOnuAuthList.tv?entityIds=' + entityIds + '&onuIndexs=' + onuIndexs, null, true, true);
	}

	function buildPagingToolBar() {
		var pagingToolbar = new Ext.PagingToolbar({
			id : 'extPagingBar',
			pageSize : pageSize,
			store : store,
			displayInfo : true,
			items : [ "-", String.format(I18N.COMMON.displayPerPage, pageSize),
					'-' ],
		    listeners : {
		    	beforeChange : function(){
		    		$("#addBtn").attr({disabled : 'disabled'});
		    	}
		    }
		});
		return pagingToolbar;
	}
	function manuRender(v, m, r) {
		return String
				.format(" <a href='javascript:;' onClick='addOnuAuth()'>@onuAuth.addAuth@</a>");
	}

	function onSeachClick() {
		var mac = $("#macInput").val();
		if (mac != "" && !Validator.isFuzzyMacAddress(mac)) {
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '<b class="orangeTxt">@COMMON.reqValidMac@</b>'
			});
			$("#macInput").focus();
			return;
		}
		var entityId = $("#oltSelect").val();
		var slotId = slotCombo.getValue();
		var ponId = ponCombo.getValue();
		store.baseParams = {
			entityId : entityId,
			mac : mac,
			slotId : slotId,
			ponId : ponId,
			start : 0,
			limit : pageSize
		};
		store.load({
			callback : function(){
				disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
			}
		});
	}

	function refreshClick() {
		var mac = $("#macInput").val();
		var entityId = $("#oltSelect").val();
		var slotId = slotCombo.getValue();
		var ponId = ponCombo.getValue();
		store.baseParams = {
			entityId : entityId,
			mac : mac,
			slotId : slotId,
			ponId : ponId,
			start : 0,
			limit : pageSize
		};
		store.load({callback:function(){
			disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}});
	}
	function disabledToolbarBtn(num){
		var $addBtn = $("#addBtn");
		if(num > 0){
			$addBtn.removeAttr('disabled');
		}else{
			$addBtn.attr({disabled: 'disabled'});
		}
	}
	Ext.onReady(function() {
		sm = new Ext.grid.CheckboxSelectionModel({
		    listeners : {
		        rowselect : function(sm,rowIndex,record){
		            disabledToolbarBtn(grid.getSelectionModel().getSelections().length)
		        },
		        rowdeselect : function(sm,rowIndex,record){
		            disabledToolbarBtn(grid.getSelectionModel().getSelections().length)
		        }
		    }
		});
		var cm
		if (operationDevicePower) {
			cm = new Ext.grid.ColumnModel([
					sm ,
					{
						id : "manageIp",
						header : '<div class="txtCenter">' + I18N.ONU.belongOlt
								+ '</div>',
						dataIndex : 'uplinkDevice',
						align : 'center',
						width : 120
					},{
						id : "onuIndex",
						header : '@COMMON.pon@',
						dataIndex : 'onuIndex',
						align : 'center',
						width : 90,
						renderer : renderIndex
					} ,{
						id : "mac",
						header : 'MAC',
						dataIndex : 'mac',
						align : 'center',
						width : 150
					},  {
						id : "sn",
						header : "SN",
						dataIndex : 'sn',
						align : 'center',
						width : 120
					}, {
						id : "password",
						header : "@onuAuth.password@",
						dataIndex : 'password',
						align : 'center',
						width : 120
					}, {
						id : "lastAuthTime",
						header : "@onuAuth.authTime@",
						dataIndex : 'lastAuthTime',
						align : 'center',
						width : 120,
						renderer : renderAuthTime
					}, {
						header : "@COMMON.manu@",
						width : 120,
						renderer : manuRender
					} ]);
		} else {
			cm = new Ext.grid.ColumnModel([
					sm,
					{
						id : "manageIp",
						header : '<div class="txtCenter">' + I18N.ONU.belongOlt
								+ '</div>',
						dataIndex : 'uplinkDevice',
						align : 'left',
						width : 120
					},{
						id : "onuIndex",
						header : '@COMMON.pon@',
						dataIndex : 'onuIndex',
						align : 'center',
						width : 90,
						renderer : renderIndex
					},{
						id : "mac",
						header : 'MAC',
						dataIndex : 'mac',
						align : 'center',
						width : 150
					}, {
						id : "sn",
						header : "SN",
						dataIndex : 'sn',
						align : 'center',
						width : 120
					}, {
						id : "password",
						header : "@onuAuth.password@",
						dataIndex : 'password',
						align : 'center',
						width : 120
					}, {
						id : "lastAuthTime",
						header : "@onuAuth.authTime@",
						dataIndex : 'lastAuthTime',
						align : 'center',
						width : 120,
						renderer : renderAuthTime
					} ]);
		}

		store = new Ext.data.JsonStore({
			url : '/epon/onuauth/getOnuAuthFailList.tv',
			//idProperty: "onuId",
			root : 'data',
			totalProperty : 'rowCount',
			remoteSort : true,
			fields : [ 'onuId', 'onuIndex', 'uplinkDevice','entityId', 'ponIndex', 'manageIp',
					'mac', 'sn', 'password', 'lastAuthTime' ]
		});
		store.setDefaultSort('entityIp', 'ASC');
		store.load({
			params : {
				start : 0,
				limit : pageSize
			}
		});

		//加载olt对应的slot
		window.slotStore = new Ext.data.JsonStore({
			url : '/onu/getOltEponSlotList.tv',
			fields : [ "slotId", "slotNo" ],
			listeners : {
				load : function(dataStore, records, options) {
					var entityId = $("#oltSelect").val();
					//只有选择了有效的设备才需要这样做
					if (entityId != null && entityId != -1) {
						var record = {
							slotId : -1,
							slotNo : '@COMMON.select@'
						};
						var $record = new dataStore.recordType(record, "-1");
						dataStore.insert(0, [ $record ]);
					}
				}
			}
		});

		window.slotCombo = new Ext.form.ComboBox({
			id : "slotCombox",
			store : slotStore,
			applyTo : "slotSelect",
			mode : 'local',
			valueField : 'slotId',
			displayField : 'slotNo',
			emptyText : "@COMMON.select@",
			selectOnFocus : true,
			typeAhead : true,
			triggerAction : 'all',
			editable : false,
			width : 152,
			enableKeyEvents : true,
			listeners : {
				keydown : function(textField, event) {
					if (event.getKey() == 8) {
						event.stopEvent();
						return;
					}
				}
			}
		});

		//加载slot对应的pon
		window.ponStore = new Ext.data.JsonStore({
			url : '/onu/getOltPonList.tv',
			fields : [ "ponId", "ponNo" ],
			listeners : {
				load : function(dataStore, records, options) {
					var slotValue = slotCombo.getValue();
					//只有选择了有效的板卡才需要这样做
					if (slotValue != null && slotValue != -1) {
						var record = {
							ponId : -1,
							ponNo : '@COMMON.select@'
						};
						var $record = new dataStore.recordType(record, "-1");
						dataStore.insert(0, [ $record ]);
					}
				}
			}
		});

		window.ponCombo = new Ext.form.ComboBox({
			id : "ponCombox",
			store : ponStore,
			applyTo : "ponSelect",
			mode : 'local',
			valueField : 'ponId',
			displayField : 'ponNo',
			emptyText : "@COMMON.select@",
			selectOnFocus : true,
			typeAhead : true,
			triggerAction : 'all',
			editable : false,
			width : 152,
			enableKeyEvents : true,
			listeners : {
				keydown : function(textField, event) {
					if (event.getKey() == 8) {
						event.stopEvent();
						return;
					}
				}
			}
		});
		//当slot改变时加载对应的pon
		slotCombo.on('select', function(comboBox) {
			//先清除加载的pon
			ponCombo.clearValue();
			//加载对应pon
			var slotValue = comboBox.getValue();
			ponStore.load({
				params : {
					slotId : slotValue
				}
			});
		})

		window.selector = new NetworkNodeSelector({
			id : 'oltSelect',
			renderTo : "oltContainer",
			//value : window["entityId"], //@赋值的方式一：配置默认值 
			autoLayout : true,
			listeners : {
				selectChange : function() {
					//olt设备改变时加载对应的slot
					//先清除加载的slot,pon
					slotCombo.clearValue();
					ponCombo.clearValue();
					//加载对应的slot
					var entityId = $("#oltSelect").val();
					slotStore.load({
						params : {
							entityId : entityId
						}
					});
					//这时候删除上次加载的pon口数据,否则上次加载的pon口列表还存在
					ponStore.removeAll();
				}
			}
		});

		grid = new Ext.grid.GridPanel({
			stripeRows : true,
			region : "center",
			bodyCssClass : 'normalTable',
			border : true,
			store : store,
			cm : cm,
			sm : sm,
			bbar : buildPagingToolBar(),
			viewConfig : {
				forceFit : true
			},
			store : store
		});
		var viewPort = new Ext.Viewport({
			layout : "border",
			items : [ grid, {
				region : 'north',
				height : 40,
				contentEl : 'topPart',
				border : false
			} ]
		});

	});
</script>
	</head>
	<body class=whiteToBlack></body>
	<div id="topPart">
		<div class="pT5 pL10">
			<table class="queryTable">
				<tr>
					<td class="rightBlueTxt w30" align="left">MAC:</td>
					<td class="pR10"><input type="text" class="normalInput w150"
						id="macInput" /></td>
					<td class="rightBlueTxt">OLT:</td>
					<td class="pR10">
						<div style="width: 150px" id="oltContainer"></div>
					</td>
					<td colspan="2"></td>
					<td class="rightBlueTxt">@COMMON.slot@:</td>
					<td class="pR10"><input type="text" class=""
						style="width: 152px" id="slotSelect" /></td>
					<td class="rightBlueTxt">@COMMON.pon@:</td>
					<td class="pR10"><input type="text" class=""
						style="width: 152px" id="ponSelect" /></td>
					<td><a href="javascript:;" class="normalBtn"
						style="margin-right: 5px;" onclick="onSeachClick()"><span><i
								class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
					<td><a href="javascript:;" class="normalBtn" id="addBtn" disabled="disabled"
						style="margin-right: 5px;" onclick="addAuthList()"><span><i
								class="miniIcoAdd"></i>@onuAuth.addAuth@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
</Zeta:HTML>
