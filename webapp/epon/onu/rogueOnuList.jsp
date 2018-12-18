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
    import js.zetaframework.component.NetworkNodeSelector static
</Zeta:Loader>
<script type="text/javascript">
	var grid = null;
	var store = null;
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
		var queryContent = $("#queryContent").val();
		var entityId = $("#oltSelect").val();
		var slotId = slotCombo.getValue();
		var ponId = ponCombo.getValue();
		store.baseParams = {
			entityId : entityId == -1 ? null : entityId,
			queryContent : queryContent,
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
	
	Ext.onReady(function() {
		var sm = new Ext.grid.CheckboxSelectionModel({
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
			url : '/onu/rogueonu/loadRogueOnuList.tv',
			root : 'data',
			totalProperty : 'rowCount',
			remoteSort : true,
			fields : ['entityId',"entityIp","name",'onuIndex','onuId','ponId','onuTypeString','uniqueId']
		});
		store.setDefaultSort('name', 'ASC');
		store.load({params : {
			start : 0,limit : pageSize
		}});

		//加载olt对应的slot
		window.slotStore = new Ext.data.JsonStore({
			url : '/onu/getOltSlotList.tv',
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
				{header: '<div class="txtCenter">@COMMON.alias@</div>',dataIndex: 'name',align:'left',sortable:true,renderer:renderName},
				{header: "MAC | SN",dataIndex: 'uniqueId',sortable:true},
				{header: '<div class="txtCenter">@COMMON.desc@</div>',dataIndex:'name',align:'left',sortable:true},
				{header: "@COMMON.type@",dataIndex: 'onuTypeString',sortable:true},
				{header: "@EPON/ONU.belongOlt@",dataIndex: 'entityIp',sortable:true,renderer:function(value,m,record){
					return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+value+'\');">'+value+'</a>';
				}},
				{header: "@COMMON.manu@",width:80,renderer : manuRender, fixed:true}
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
	
	function showEntityDetail(entityId, $name){
		window.parent.addView('entity-' + entityId, $name , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + entityId);
	}
	
	function renderName(value,m,record){
		return String.format('<a href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>', value);
	}
	
	function showOnuInfo(onuId,onuName){
		 window.parent.addView('entity-' + onuId, unescape(onuName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + onuId );
	}
	
	function manuRender(v,m,r){
		return String.format("<a href='javascript:;' onClick='rogueOnuCheck({0},{1},{2});'>CHECK</a>",r.data.entityId, r.data.ponId, r.data.onuIndex)
	}
	
	function rogueOnuCheck(entityId,ponId,onuIndex){
	    window.parent.showWaitingDlg("@COMMON.wait@", "@ONU.rogueOnuCheck@", 'waitingMsg', 'ext-mb-waiting');
	    $.ajax({
	        url:'/onu/rogueonu/rogueOnuCheckList.tv',
	        data: {
	        	entityId : entityId,
	        	ponId : ponId,
	        	onuIndex : onuIndex
	        },dateType:'json',
	        success:function(json){
	        	window.top.closeWaitingDlg();
	        	   top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@ONU.rogueOnuCheckSuc@</b>'
	   			});
	        	store.reload();
	        },
	        error:function(){
	        	window.parent.showMessageDlg("@COMMON.tip@", "@ONU.rogueOnuCheckFail@");
	        },
	        cache:false
	    });
	}
	
</script>
	</head>
	<body class=whiteToBlack>
		<div id="topPart">
			<div class="pT5 pL10">
				<table class="queryTable">
					<tr>
						<td class="rightBlueTxt w100" align="left">@COMMON.alias@/MAC/SN</td>
						<td class="pR10"><input type="text" class="normalInput w150" id="queryContent" /></td>
						<td class="rightBlueTxt">OLT@COMMON.maohao@</td>
						<td class="pR10"><div style="width: 150px" id="oltContainer"></div></td>
						<td colspan="2"></td>
						<td class="rightBlueTxt">@COMMON.slot@@COMMON.maohao@</td>
						<td class="pR10"><input type="text" style="width: 152px" id="slotSelect" /></td>
						<td class="rightBlueTxt">@COMMON.port@@COMMON.maohao@</td>
						<td class="pR10"><input type="text" class="" style="width: 152px" id="ponSelect" /></td>
						<td><a href="javascript:;" class="normalBtn" style="margin-right: 5px;" onclick="onSeachClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</Zeta:HTML>
