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
    CSS css/white/disabledStyle
    import epon/onu/onuDeleteTrap
</Zeta:Loader>
<style type="text/css">
#portletTools .x-panel-body {
	background: #fff;
}

.putCir {
	width: 150px;
	height: 170px; /*background:#eee;*/
}

.cirLengend {
	width: 16px;
	height: 16px;
	overflow: hidden;
	display: block;
	float: left;
	margin-right: 2px;
}

.clrLendUl {
	border: 1px solid #ccc;
	margin: 10px 0px;
	background: #fff;
	-moz-border-radius: 5px;
	-khtml-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}
</style>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<script>
	var entityId = "${onu.parentId}";
	var onuId = "${onu.entityId}";
	var baseStore;
	var baseGrid;
	var statusStore;
	var statusGrid;
	var connectIds = new Array();
	var rowCount;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	
	$(function() {
		createBaseInfoList();
		authLoad();
	});
	
	function authLoad(){
		if(!refreshDevicePower){
			$("#refreshButton").attr("disabled",true);
		}
		if(!operationDevicePower){
			$("#clearWanConnectionButton").attr("disabled",true);
			$("#addButton").attr("disabled",true);
		}
	}

	//创建grid
	function createBaseInfoList() {
		var baseColumns = [ {
			header : '<div class="txtCenter">@ONU.WAN.name@</div>',
			width : 270,
			align : 'center',
			dataIndex : 'name'
		}, {
			header : 'WAN1',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value1',
			renderer : wanRender
		}, {
			header : 'WAN2',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value2',
			renderer : wanRender
		}, {
			header : 'WAN3',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value3',
			renderer : wanRender
		}, {
			header : 'WAN4',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value4',
			renderer : wanRender
		}, {
			header : 'WAN5',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value5',
			renderer : wanRender
		}, {
			header : 'WAN6',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value6',
			renderer : wanRender
		}, {
			header : 'WAN7',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value7',
			renderer : wanRender
		}, {
			header : 'WAN8',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'value8',
			renderer : wanRender
		}, {
			header : '@ONU.WAN.connectionInfo@',
			width : 160,
			align : 'center',
			hidden : true,
			dataIndex : 'group'
		} ];

		var reader = new Ext.data.JsonReader({
			totalProperty : "rowCount",
			idProperty : "name",
			root : "data",
			fields : [ {
				name : 'name'
			}, {
				name : 'value1'
			}, {
				name : 'value2'
			}, {
				name : 'value3'
			}, {
				name : 'value4'
			}, {
				name : 'value5'
			}, {
				name : 'value6'
			}, {
				name : 'value7'
			}, {
				name : 'value8'
			}, {
				name : 'group'
			} ]
		});

		baseStore = new Ext.data.GroupingStore({
			url : ('/onu/getWanConnectList.tv?onuId=' + onuId),
			reader : reader,
			groupOnSort : false,
			groupField : 'group'
		});

		baseStore.setDefaultSort('group', 'ASC');

		var groupTpl = '<span name="{gvalue}"></span>{gvalue}({[values.rs.length]} {[values.rs.length > 1 ? "@COMMON.items@" : "@COMMON.item@"]})';

		baseGrid = new Ext.grid.GridPanel({
			region : 'center',
			stripeRows : true,
			id : 'extmoreInfoGridContainer',
			view : new Ext.grid.GroupingView({
				forceFit : true,
				groupTextTpl : groupTpl
			}),
			border : true,
			store : baseStore,
			cls : 'normalTable',
			columns : baseColumns,
			title : 'WAN@ONU.WAN.connectionInfo@'
		});

		new Ext.Viewport({
			layout : 'border',
			border : false,
			items : [ baseGrid, {
				border : false,
				height : 83,
				region : 'north',
				contentEl : 'topPart'
			} ]
		});
		reloadStore();
	}

	//WAN RENDER
	function wanRender(value, p, record) {
		if (value == '' || value == null) {
			return "-"
		}
		if (record.data.name == '@ONU.WAN.operation@') {
			var test = connectIds.contains(value);
			if ( !connectIds.contains(value) ) {
				connectIds.push(value);
			}
			var ss = '<span class="lightGrayTxt">@ONU.WAN.bindInterface@</span> / <span class="lightGrayTxt">@ONU.WAN.modify@</span> / <span class="lightGrayTxt">@ONU.WAN.delete@</span>';
			if(!operationDevicePower){
				return ss;
			}else{
				return String.format(
						'<a href="#" id="bindInterface" onclick="bindInterface({0})">@ONU.WAN.bindInterface@</a> / '
								+ '<a href="#" id="modifyConnect" onclick="modifyConnect({0})">@ONU.WAN.modify@</a> / '
								+ '<a href="#" id="deleteConnect" onclick="deleteConnect({0})">@ONU.WAN.delete@</a>',
						value);
			}
		} else {
			return value;
		}
	}

	//新增WAN连接
	function addWanConnect(connectId) {
		window.top.createDialog('addWanConnect','@ONU.WAN.new@WAN@ONU.WAN.connection@', 800, 520,
				'/onu/showAddWanConnect.tv?entityId=' + entityId + '&onuId='+ onuId, null, true, true);
	}

	//删除WAN连接
	function deleteConnect(connectId) {
		window.parent.showConfirmDlg(I18N.COMMON.tip,"@ONU.WAN.delete@@ONU.WAN.connection@", function(button, text) {
			if (button == "yes") {
				window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.delete@@ONU.WAN.connection@','waitingMsg', 'ext-mb-waiting');
				$.ajax({
					url : '/onu/deleteWanConnect.tv',
					type : 'POST',
					data : {
						entityId : entityId,
						onuId : onuId,
						connectId : connectId
					},
					dateType : 'text',
					success : function(response) {
						window.parent.closeWaitingDlg();
						if(response == 'success'){
							showSuccess("@ONU.WAN.delete@" + "@ONU.WAN.success@");
							connectIds.remove(connectId);
							baseGrid.getColumnModel().setHidden(connectId,true);
							reloadStore();
						}else{
							showError("@ONU.WAN.delete@" + "@ONU.WAN.failed@")
						}
					},
					error : function() {
						showError("@ONU.WAN.delete@" + "@ONU.WAN.failed@")
					},
					cache : false
				});
			}
		});
	}

	//清除WAN连接
	function clearWanConnection() {
		window.parent.showConfirmDlg(I18N.COMMON.tip,"@ONU.WAN.clear@WAN@ONU.WAN.connection@",function(button, text) {
			if (button == "yes") {
				window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.clear@WAN@ONU.WAN.connection@','waitingMsg', 'ext-mb-waiting');
				$.ajax({
					url : '/onu/clearWanConnection.tv',
					type : 'POST',
					data : {
						entityId : entityId,
						onuId : onuId
					},
					dateType : 'text',
					success : function(response) {
						if(response == 'success'){
							window.parent.closeWaitingDlg();
							showSuccess("@ONU.WAN.clear@" + "@ONU.WAN.success@")
							window.location.reload();
						}else{
							showError("@ONU.WAN.clear@"  + "@ONU.WAN.failed@")
						}
					},
					error : function() {
						showError("@ONU.WAN.clear@"	+ "@ONU.WAN.failed@")
					},
					cache : false
				});
			}
		});
	}

	//修改WAN连接
	function modifyConnect(connectId) {
		window.top.createDialog('modifyWanConnect',	'@ONU.WAN.modify@WAN@ONU.WAN.connection@', 800, 520,
				'/onu/showModifyWanConnect.tv?entityId=' + entityId + '&onuId='	+ onuId + '&connectId=' + connectId, null, true, true);
	}

	//刷新WAN连接
	function refreshWanConnection() {
		window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.refresh@WAN@ONU.WAN.connection@', 'waitingMsg','ext-mb-waiting');
		$.ajax({
			url : '/onu/refreshWanConnection.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId
			},
			dateType : 'json',
			success : function(response) {
				window.parent.closeWaitingDlg();
				showSuccess("@ONU.WAN.refresh@" + "@ONU.WAN.success@");
				window.location.reload();
			},
			error : function() {
				window.parent.closeWaitingDlg();
				showError("@ONU.WAN.refresh@" + "@ONU.WAN.failed@");
			},
			cache : false
		});
	}

	//重新加载store
	function reloadStore() {
		baseStore.load({
			callback : function() {
				$("#extmoreInfoGridContainer").find("span[name='001']").parent().parent().css({	display : 'none'});
				var length = connectIds.length;
				if (length == 8) {
					$('#addButton').attr('disabled');
				}
				if (length < 1) {
					//隐藏表格
					baseGrid.addClass("displayNone");
				} else {
					baseGrid.removeClass("displayNone");
				}
				var baseCm = baseGrid.getColumnModel(),
				    firstW = 270,
				    w = $(window).width(),
				    w2 = (w - 270 - 20)/length;
				if(w2 < 50){ w2 = 50};
				for (var i = 0; i < length; i++) {
					baseCm.setHidden(connectIds[i], false);
				}
				baseCm.setColumnWidth(0, firstW);
				//这里之所以写2个for循环是因为，只有将hidden值全部设置正确后，才能正确的设置width;
				//真正的做法不是控制显示隐藏,而是后台生成正确的cm的columns的个数;
				for(var i=0; i<length; i++){
					baseCm.setColumnWidth(connectIds[i], w2);
				}
			}
		});
	}

	//显示成功信息
	function showSuccess(text) {
		top.afterSaveOrDelete({
			title : I18N.COMMON.tip,
			html : '<b class="orangeTxt">' + text + '</b>'
		});
	}

	//显示失败信息
	function showError(text) {
		window.parent.showMessageDlg(I18N.COMMON.tip, text);
	}

	//绑定端口
	function bindInterface(connectId) {
		window.top.createDialog('bindInterface', '@ONU.WAN.bindInterface@',	600, 370, '/onu/showBindInterface.tv?entityId=' + entityId
			+ '&onuId=' + onuId + '&connectId=' + connectId, null,true, true);
	}
</script>
	</head>
	<body class="newBody clear-x-panel-body">
		<div id="topPart">
			<div id="header">
				<%@ include file="navigator.inc"%>
			</div>
			<div class="edgeAndClearFloat">
				<ol class="upChannelListOl pL0 pB0" id="upChannelListOl">
					<li>
						<a href="javascript:;" class="normalBtn" id="clearWanConnectionButton" onclick="clearWanConnection()">
							<span><i class="miniIcoEquipment"></i>@ONU.WAN.clear@WAN@ONU.WAN.connection@</span>
						</a>
					</li>
					<li>
						<a href="javascript:;" class="normalBtn" id="addButton"	onclick="addWanConnect()">
							<span><i class="miniIcoAdd"></i>@ONU.WAN.new@</span>
						</a>
					</li>
					<li>
						<a href="javascript:;" class="normalBtn" id="refreshButton" onclick="refreshWanConnection()">
							<span><i class="miniIcoEquipment"></i>@ONU.WAN.refresh@</span>
						</a>
					</li>
				</ol>
			</div>
		</div>
	</body>
</Zeta:HTML>
