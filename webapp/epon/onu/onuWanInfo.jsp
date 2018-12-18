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
	var onuId = "${onuId}";
	var entityId = "${entityId}";
	var onuIndex = "${onuIndex}";
	var baseStore;
	var baseGrid;
	var ssidList = new Array();
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var isNewWorkMode = true;//是否是新版本

	$(function() {
		createBaseInfoList();
		loadWLANConfig();//读取WLAN配置
		bindButtonFunction();//绑定按钮响应
		authLoad();
	});
	
	function authLoad(){
		if(!refreshDevicePower){
			$("#refreshWLANConfig").attr("disabled",true);
			Ext.getCmp("refreshSSIDListButton").setDisabled(true);
		}
		if(!operationDevicePower){
			$("#saveWLANConfig").attr("disabled",true);
		}
	}

	//创建SSID列表;
	function createBaseInfoList() {
		var w = document.body.clientWidth;//多一个滚动条的距离;	
		var h = $(window).height() - 250
		if (h < 200) {
			h = 200;
		}
		var baseColumns = [ {
			header : 'SSID',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'ssid'
		}, {
			header : '@ONU.WAN.name@',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'ssidName'
		}, {
			header : '@ONU.WAN.encryptMode@',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'encryptMode',
			renderer : encryptModeRender
		}, {
			header : '@ONU.WAN.SSIDEnable@',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'ssidEnnable',
			renderer : ennableRender
		}, {
			header : '@ONU.WAN.SSIDBroadcastEnable@',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'ssidBroadcastEnnable',
			renderer : ennableRender
		}, {
			header : '@ONU.WAN.maxUserNumber@',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'ssidMaxUser'
		}, {
			header : '@ONU.WAN.operation@',
			width : (1 / 15) * w,
			sortable : false,
			align : 'center',
			menuDisabled : true,
			dataIndex : 'onuId',
			renderer : operationRender
		} ];

		baseStore = new Ext.data.JsonStore({
			url : ('/onu/getWanSsidList.tv?onuId=' + onuId),
			root : 'data',
			fields : [ 'onuId', 'ssid', 'ssidName', 'encryptMode', 'password',
					'ssidEnnable', 'ssidBroadcastEnnable', 'ssidMaxUser' ]
		});

		var baseCm = new Ext.grid.ColumnModel(baseColumns);
		baseGrid = new Ext.grid.GridPanel({
			id : 'extmoreInfoGridContainer',
			height : h,
			animCollapse : animCollapse,
			trackMouseOver : trackMouseOver,
			viewConfig : {
				forceFit : true
			},
			border : true,
			store : baseStore,
			tbar : [
			/* {
				iconCls : 'bmenu_new',
				id : "addButton",
				xtype : 'button',
				text : '@ONU.WAN.new@',
				handler : addWanSsid
				}, '-',  */
			{
				iconCls : 'bmenu_refresh',
				id : 'refreshSSIDListButton',
				xtype : 'button',
				text : '@ONU.WAN.refresh@',
				handler : refreshSSIDList
			} ],
			cm : baseCm,
			title : '@ONU.WAN.SSIDList@',
			renderTo : 'moreInfodetail-div'
		});
		baseStore.load({
			callback : function(records, options, success) {
				baseStoreCallback()
			}
		});
	}

	function baseStoreCallback() {
		/* if (baseStore.totalLength === 4) {
			Ext.getCmp('addButton').setDisabled(true);
		} else {
			Ext.getCmp('addButton').setDisabled(false);
		} */
		ssidList = new Array();
		for (var i = 0, j = 0; i < baseStore.totalLength; i++, j++) {
			ssidList[j] = baseStore.getAt(i).get('ssid');
		}
	}

	//刷新GRID中数据
	function refreshGrid() {
		baseStore.reload({
			callback : function(records, options, success) {
				baseStoreCallback();
			}
		});
	}

	//读取WLAN配置
	function loadWLANConfig() {
		$.ajax({
			url : '/onu/loadWLANConfig.tv',
			type : 'POST',
			data : {
				onuId : onuId
			},
			dateType : 'json',
			success : function(json) {
				if (json && json.onuWanConfig) {
					$('#workMode').val(json.onuWanConfig.workModeForShow);
					if(json.onuWanConfig.workMode.charAt(0) != "0"){
						isNewWorkMode = false;
					}
					$('#channelWidth').val(json.onuWanConfig.channelWidth);
					$('#channelId').val(json.onuWanConfig.channelId);
					$('#sendPower').val(json.onuWanConfig.sendPower);
					if (json.onuWanConfig.wanEnnable == 1) {
						$('#wanEnnable').attr({
							alt : '1',
							src : '/images/performance/on.png'
						});
					} else {
						$('#wanEnnable').attr({
							alt : '2',
							src : '/images/performance/off.png'
						});
					}
				}
			},
			error : function() {
			},
			cache : false
		});
	}

	//绑定按钮响应
	function bindButtonFunction() {
		//点击on或者off;
		$(".scrollBtn").click(function() {
			var $me = $(this), alt = $me.attr("alt");
			switch (alt) {
			case '1':
				$me.attr({
					alt : '2',
					src : '/images/performance/off.png'
				});
				break;
			case '2':
				$me.attr({
					alt : '1',
					src : '/images/performance/on.png'
				});
				break;
			}
		});
	}

	//开启和关闭图片Render
	function ennableRender(value, p, record) {
		if (value == '1') {
			return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>',"@ONU.WAN.open@");
		} else {
			return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>',"@ONU.WAN.close@");
		}
	}

	//无线加密模式Render
	function encryptModeRender(value, p, record) {
		switch (value) {
		case 0:
			return 'NONE';
		case 1:
			return 'WEP';
		case 2:
			return 'WPA';
		case 3:
			return 'WPA2';
		case 4:
			return 'WPA_WPA2';
		default:
			return 'NONE';
		}
	}

	//开关Render
	function renderIsOpenImg(para) {
		var str;
		if (para === true) {
			str = '<img src="/images/performance/on.png" alt="on" class="scrollBtn" />';
		} else {
			str = '<img src="/images/performance/off.png" alt="off" class="scrollBtn" />';
		}
		return str;
	}

	//操作Render
	function operationRender(value, p, record) {
		return String.format('<a href="#" onclick="modifySsid({0},{1}, {2})">@COMMON.modify@</a>',entityId, onuId, record.data.ssid);
	}

	//新建SSID
	function addWanSsid() {
		window.top.createDialog('addWanSsid', '@ONU.WAN.new@@ONU.WAN.SSID@',650, 380, '/onu/showAddWanSsid.tv?entityId=' + entityId
						+ '&onuId=' + onuId + '&onuIndex=' + onuIndex
						+ '&ssidList=' + ssidList, null, true, true);
	}

	//删除SSID
	function deleteSsid(entityId, onuId, ssid) {
		window.parent.showConfirmDlg(I18N.COMMON.tip,"@ONU.WAN.deleteSSID@",function(button, text) {
			if (button == "yes") {
				window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.deleteSSID@', 'waitingMsg','ext-mb-waiting');
				$.ajax({
					url : '/onu/deleteWanSsid.tv',
					type : 'POST',
					data : {
						entityId : entityId,
						onuIndex : onuIndex,
						onuId : onuId,
						ssid : ssid
					},
					dateType : 'json',
					success : function(response) {
						window.parent.closeWaitingDlg();
						top.afterSaveOrDelete({
								title : I18N.COMMON.tip,
								html : '<b class="orangeTxt">'+ "@ONU.WAN.delete@@ONU.WAN.success@"	+ '</b>'
						});
						refreshGrid();
					},
					error : function() {
						window.parent.closeWaitingDlg();
						window.parent.showMessageDlg(I18N.COMMON.tip,"@ONU.WAN.deleteFailed@");
					},
					cache : false
				});
			}
		});
	}

	//修改SSID
	function modifySsid(entityId, onuId, ssid) {
		window.top.createDialog('modifyWanSsid', '@ONU.WAN.modifySSID@', 650,380, '/onu/showModifyWanSsid.tv?entityId=' + entityId
						+ '&onuId=' + onuId + '&onuIndex=' + onuIndex
						+ '&ssid=' + ssid, null, true, true);
	}

	//刷新SSID列表
	function refreshSSIDList() {
		window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.refresh@@ONU.WAN.SSIDList@', 'waitingMsg','ext-mb-waiting');
		$.ajax({
			url : '/onu/refreshSSIDList.tv',
			type : 'POST',
			data : {
				onuId : onuId,
				entityId : entityId,
				onuIndex : onuIndex
			},
			dateType : 'json',
			success : function(response) {
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : I18N.COMMON.tip,
					html : '<b class="orangeTxt">' + '@ONU.WAN.refresh@@ONU.WAN.success@ ' + '</b>'
				});
				refreshGrid();
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg(I18N.COMMON.tip,'@ONU.WAN.refresh@@ONU.WAN.failed@');
			},
			cache : false
		});
	}

	//保存WLAN配置
	function saveWLANConfig() {
		var workModeForShow = $('#workMode').val();
		//处理位图颠倒问题，新版本workMode位图从左算第一位
		var workMode;
		if(isNewWorkMode){
			switch (workModeForShow) {
	        case "1":
	            workMode = "01";// b
	            break;
	        case "2":
	        	workMode = "02";// g
	            break;
	        case "3":
	        	workMode = "04";// n
	            break;
	        case "4":
	        	workMode = "03";// bg
	            break;
	        case "5":
	        	workMode = "07";// bgn
	            break;
	        }
		}else{
			switch (workModeForShow) {
	        case "1":
	        	workMode = "80";// b
	            break;
	        case "2":
	        	workMode = "40";// g
	            break;
	        case "3":
	        	workMode = "20";// n
	            break;
	        case "4":
	        	workMode = "c0";// bg
	            break;
	        case "5":
	        	workMode = "e0";// bgn
	            break;
	        }
		}
		var channelWidth = $('#channelWidth').val();
		var channelId = $('#channelId').val();
		var sendPower = $('#sendPower').val();
		var wanEnnable = $('#wanEnnable').attr('alt');
		window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.save@@ONU.WAN.WLANConfig@', 'waitingMsg','ext-mb-waiting');
		$.ajax({
			url : '/onu/saveWLANConfig.tv',
			type : 'POST',
			data : {
				'onuWanConfig.workMode' : workMode,
				'onuWanConfig.channelWidth' : channelWidth,
				'onuWanConfig.channelId' : channelId,
				'onuWanConfig.sendPower' : sendPower,
				'onuWanConfig.wanEnnable' : wanEnnable,
				'onuWanConfig.entityId' : entityId,
				'onuWanConfig.onuId' : onuId,
				'onuWanConfig.onuIndex' : onuIndex
			},
			dateType : 'json',
			success : function(json) {
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : I18N.COMMON.tip,
					html : '<b class="orangeTxt">' + '@ONU.WAN.save@@ONU.WAN.success@' + '</b>'
				});
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, '@ONU.WAN.save@@ONU.WAN.failed@');
			},
			cache : false
		});
	}

	//刷新WLAN配置
	function refreshWLANConfig() {
		window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.refresh@@ONU.WAN.WLANConfig@', 'waitingMsg','ext-mb-waiting');
		$.ajax({
			url : '/onu/refreshWLANConfig.tv',
			type : 'POST',
			data : {
				onuId : onuId,
				entityId : entityId,
				onuIndex : onuIndex
			},
			dateType : 'json',
			success : function(json) {
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : I18N.COMMON.tip,
					html : '<b class="orangeTxt">' + '@ONU.WAN.refresh@@ONU.WAN.success@' + '</b>'
				});
				$('#workMode').val(json.onuWanConfig.workModeForShow);
				$('#channelWidth').val(json.onuWanConfig.channelWidth);
				$('#channelId').val(json.onuWanConfig.channelId);
				$('#sendPower').val(json.onuWanConfig.sendPower);
				if (json.onuWanConfig.wanEnnable == 1) {
					$('#wanEnnable').attr({
						alt : '1',
						src : '/images/performance/on.png'
					});
				} else {
					$('#wanEnnable').attr({
						alt : '2',
						src : '/images/performance/off.png'
					});
				}
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg(I18N.COMMON.tip,'@ONU.WAN.refresh@@ONU.WAN.failed@');
			},
			cache : false
		});
	}
</script>
	</head>
	<body class="newBody clear-x-panel-body">
		<div id="header">
			<%@ include file="navigator.inc"%>
		</div>
		<div class="edge10">
			<div class="jsTabPart clearBoth">
				<p class="flagP">
					<span class="flagInfo">@ONU.WAN.WLANConfig@</span>
				</p>
				<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6"></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="140">@ONU.WAN.workMode@@COMMON.maohao@</td>
							<td>
								<select id="workMode" class="normalSel w150">
									<option value="1">802.11b</option>
									<option value="2">802.11g</option>
									<option value="3">802.11n</option>
									<option value="4">802.11bg</option>
									<option value="5">802.11bgn</option>
								</select>
							</td>
							<td class="rightBlueTxt" width="140">@ONU.WAN.channelWidth@@COMMON.maohao@</td>
							<td>
								<select id="channelWidth" class="normalSel w150">
									<option value="1">20MHz</option>
									<!-- <option value="2">40MHz-</option> -->
									<option value="3">40MHz+</option>
								</select>
							</td>
							<td class="rightBlueTxt" width="140">@ONU.WAN.channelId@@COMMON.maohao@</td>
							<td><select id="channelId" class="normalSel w150">
									<option value="0">@ONU.WAN.auto@</option>
									<option value="1">1</option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8">8</option>
									<option value="9">9</option>
									<option value="10">10</option>
									<option value="11">11</option>
									<option value="12">12</option>
									<option value="13">13</option>
							</select></td>
						</tr>
						<tr>
							<td class="rightBlueTxt" width="140">@ONU.WAN.TransmissionPowerMode@@COMMON.maohao@</td>
							<td>
								<select id="sendPower" class="normalSel w150">
									<option value="1">@ONU.WAN.Efficient@</option>
									<option value="2">@ONU.WAN.Standard@</option>
									<option value="3">@ONU.WAN.ThroughWall@</option>
								</select>
							</td>
							<td class="rightBlueTxt" width="140">@ONU.WAN.WLANEnable@@COMMON.maohao@</td>
							<td width="150">
								<img id='wanEnnable' src="../../images/performance/on.png" alt="1" class="scrollBtn" />
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="6">
								<div class="noWidthCenterOuter clearBoth">
									<ol class="upChannelListOl pB5 pT5 noWidthCenter">
										<li>
											<a id="refreshWLANConfig" onclick="refreshWLANConfig()" href="javascript:;"class="normalBtn">
												<span><i class="miniIcoRefresh"></i>@ONU.WAN.refresh@</span>
											</a>
										</li>
										<li>
											<a id="saveWLANConfig" onclick="saveWLANConfig()" href="javascript:;" class="normalBtn">
												<span><i class="miniIcoSave"></i>@ONU.WAN.save@</span>
											</a>
										</li>
									</ol>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="paddingLR10 clearBoth pT10">
			<div class="whiteTabContent">
				<div class="containerLegent">
					<div id="moreInfodetail-div" class="normalTable extZebra"></div>
				</div>
			</div>
		</div>
	</body>
</Zeta:HTML>
