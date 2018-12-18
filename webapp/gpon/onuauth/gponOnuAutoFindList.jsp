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
    module OnuAuth
    css css/white/disabledStyle
    import js.zetaframework.component.NetworkNodeSelector static
</Zeta:Loader>
<script type="text/javascript">
	var grid = null;
	var store = null;
	var sm;
	var pageSize =<%=uc.getPageSize()%>;

	function buildPagingToolBar() {
		return new Ext.PagingToolbar({
			id : 'extPagingBar',
			pageSize : pageSize,
			store : store,
			displayInfo : true,
			items : [ "-", String.format("@COMMON.displayPerPage@", pageSize),'-' ],
		    listeners : {
		    	beforeChange : function(){
		    		$("#addBtn").attr({disabled : 'disabled'});
		    	}
		    }
		});
	}

	function onSeachClick() {
		var sn = $("#snInput").val();
		var entityId = $("#oltSelect").val();
		var slotId = slotCombo.getValue();
		var ponId = ponCombo.getValue();
		store.baseParams = {
			entityId : entityId == -1 ? null : entityId,
			sn : sn,
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

	function disabledToolbarBtn(num){
		var $addBtn = $("#addBtn");
		if(num > 0){
			$addBtn.removeAttr('disabled');
		}else{
			$addBtn.attr({disabled: 'disabled'});
		}
	}
	
	function addGponAuthList(){
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
		window.top.createDialog('addGponOnuAuth', "@OnuAuth.joinAuth@", 850, 500,
				'/gpon/onuauth/showAddGponOnuAuthList.tv?entityIds=' + entityIds + '&onuIndexs=' + onuIndexs, null, true, true);
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

		store = new Ext.data.JsonStore({
			url : '/gpon/onuauth/queryGponOnuAutoFind.tv',
			root : 'data',
			totalProperty : 'rowCount',
			remoteSort : true,
			fields : ['entityId',"entityName","ponOnuAuthenMode","location",'onuIndex','autoFindTime',"findTime",'hardwareVersion','loid','loidPassword','onuType','password','serialNumber','softwareVersion']
		});
		store.setDefaultSort('entityId', 'ASC');
		store.load({params : {
			start : 0,limit : pageSize
		}});

		//加载olt对应的slot
		window.slotStore = new Ext.data.JsonStore({
			url : '/onu/getOltGponSlotList.tv',
			fields : [ "slotId", "slotNo" ],
			listeners : {
				load : function(dataStore, records, options) {
					var entityId = $("#oltSelect").val();
					//只有选择了有效的设备才需要这样做
					if (entityId != null && entityId != -1) {
						var record = {
							slotId : -1,slotNo : '@COMMON.select@'
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
		});

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
					slotStore.load({params : {entityId : entityId}});
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
			columns: [
			    sm,
				{header: "@EPON/ONU.belongOlt@",dataIndex: 'entityName',renderer:function(value,m,record){
					return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+value+'\');">'+value+'</a>';
				}},
				{header: "@OnuAuth.onu.logicalLocation@",dataIndex: 'location'},
				{header: "<div class='txtCenter'>@OnuAuth.onuType@</div>",dataIndex: 'onuType', align:'left'},
				{header: "@OnuAuth.authMode@",dataIndex: 'ponOnuAuthenMode',renderer:renderGponAuthMode},
				{header: "GPON SN",dataIndex: 'serialNumber',renderer:snRender},
				{header: "@COMMON.password@",dataIndex: 'password',renderer:contentRender},
				{header: "LOID",dataIndex: 'loid',renderer:contentRender},
				{header: "LOID@COMMON.password@",dataIndex: 'loidPassword',renderer:contentRender},
				{header: "<div class='txtCenter'>@OnuAuth.software.version@</div>",dataIndex: 'softwareVersion', align:'left'},
				{header: "<div class='txtCenter'>@OnuAuth.hardware.version@</div>",dataIndex: 'hardwareVersion', align:'left'},
				{header: "@OnuAuth.lastAuthTime@",dataIndex: 'autoFindTime',width:120,sortable:true,renderer:function(v,m,r){return r.data.findTime;}},
				{header: "@COMMON.manu@",dataIndex: 'location',width:80,renderer: autoFindHandlerRender}
			],
			sm : sm,
			bbar : buildPagingToolBar(),
			viewConfig : {forceFit : true},
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
	
	function renderGponAuthMode(gponauthmode){
	    switch(gponauthmode){
		    case 1:return "@OnuAuth.snAuth@";
		    case 2:return "@OnuAuth.snPassAuth@";
		    case 3:return "@OnuAuth.loidAuth@";
		    case 4:return "@OnuAuth.loidPassAuth@";
		    case 5:return "@OnuAuth.passAuth@";
		    case 6:return "@OnuAuth.autoAuth@";
		    case 7:return "@OnuAuth.mixAuth@";
	    }
	}
	function autoFindHandlerRender(v,m,r){
		if(operationDevicePower){
			var tmpl = "<a href='javascript:;' onClick='addAutoFindToGponAuth({entityId},{onuIndex})'>@OnuAuth.joinAuth@</a> ";
			return String.format( tmpl, r.data );
		}else{
			return "--";
		}
	}
	function contentRender(v){
		return v || "--";
	}
	function snRender(v){
		return v.replaceAll(":","");
	}
	function addAutoFindToGponAuth(entityId,onuIndex){
		top.createDialog('addGponOnuAuth', "@OnuAuth.addAuth@", 600, 400, '/gpon/onuauth/showAddGponOnuAuth.tv?entityId=' + entityId+"&onuIndex="+onuIndex, null, true, true,function(){
			store.reload();
		});
	}
	function showEntityDetail(entityId, $name){
		window.parent.addView('entity-' + entityId, $name , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + entityId);
	}
	
</script>
	</head>
	<body class=whiteToBlack>
		<div id="topPart">
			<div class="pT5 pL10">
				<table class="queryTable">
					<tr>
						<td class="rightBlueTxt w30" align="left">SN@COMMON.maohao@</td>
						<td class="pR10"><input type="text" class="normalInput w150" id="snInput" /></td>
						<td class="rightBlueTxt">OLT@COMMON.maohao@</td>
						<td class="pR10"><div style="width: 150px" id="oltContainer"></div></td>
						<td colspan="2"></td>
						<td class="rightBlueTxt">@COMMON.slot@@COMMON.maohao@</td>
						<td class="pR10"><input type="text" style="width: 152px" id="slotSelect" /></td>
						<td class="rightBlueTxt">@COMMON.port@@COMMON.maohao@</td>
						<td class="pR10"><input type="text" class="" style="width: 152px" id="ponSelect" /></td>
						<td><a href="javascript:;" class="normalBtn" style="margin-right: 5px;" onclick="onSeachClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
						<!-- <td><a href="javascript:;" class="normalBtn" id="addBtn" disabled="disabled" style="margin-right: 5px;" onclick="addGponAuthList()"><span><i class="miniIcoAdd"></i>@OnuAuth.joinAuth@</span></a></td> -->
					</tr>
				</table>
			</div>
		</div>
	</body>
</Zeta:HTML>
