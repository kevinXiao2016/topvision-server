$(function() {
	// 初始化上传文件控件
	//initUploadComponent();

	// 下载模版按钮逻辑实现
	$('#downLoadFile').bind('click', function() {
		window.location.href = "/entity/downLoadBatchFileTemplate.tv";
	});

	// 初始化设备类型
	initEntityType();

	// 添加SNMP标签逻辑
	refreshSnmpTabs();
	$('#addSnmp').bind('click', showAddSnmp);

	// SNMP参数和PING参数设置输入框逻辑添加
	$("#retryCountSel").val(snmpRetries);
	$("#timeoutInput").bind('change blur keyup', checkedTimeoutInput);
	$("#pingTimeout").bind('change blur keyup', checkedPingTimeout);

	// 导入按钮功能实现
	/*$('#okBtn').bind(
			'click',
			function() {
				// 防止重复点击
				$("#okBt").attr("disabled", true).mouseout();
				setTimeout(function() {
					$("#okBt").attr("disabled", false);
				}, 1500);

				// 校验有没有选择文件
				var ipStr = $("#batchIpText").val();
				if (!ipStr) {
					return window.parent.showMessageDlg(I18N.COMMON.tip,
							I18N.batchTopo.checkExcel);
				}
				// 校验有没有选择设备类型
				var typeList = [];
				$.each($("input:checked"), function(idx, item) {
					typeList.push(item.value);
				});
				var typeStr = typeList.join(",");
				if (!typeStr) {
					window.parent.showMessageDlg(I18N.COMMON.tip,
							I18N.batchTopo.checkDeviceType);
					return;
				}
				// 校验SNMP标签
				if (snmpList.length == 0) {
					window.parent.showMessageDlg(I18N.COMMON.tip,
							I18N.batchTopo.checkSnmpLabel);
					return;
				}

				// 检验SNMP参数和PING参数
				var retryCount = $("#retryCountSel").val();
				var timeoutInput = $("#timeoutInput").val();
				var pingTimeout = $("#pingTimeout").val();
				if (!checkedTimeoutInput()) {
					window.parent.showMessageDlg(I18N.COMMON.tip,
							I18N.batchTopo.checkTheTimeout);
					return;
				}
				if (!checkedPingTimeout()) {
					window.parent.showMessageDlg(I18N.COMMON.tip,
							I18N.batchTopo.checkTheTimeout);
					return;
				}

				// 检查是否可以导入
				$.ajax({
					url : '/entity/checkBatchActionAvailable.tv',
					cache : false,
					success : function(msg) {
						if (msg == "currentTopoBusy") {
							return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.batchTopo.systemTopoBusy);
						} else if (msg == "currentTopoStart") {
							// 可以导入，组织url
							url = '/entity/scanEntity.tv?dwrId=999';
							url += '&entityTypeString=' + typeStr;
							url += '&snmpTabStr=' + Ext.encode(snmpList);
							url += '&retryCount=' + retryCount;
							url += '&timeoutSeconds=' + timeoutInput;
							url += '&pingTimeout=' + pingTimeout;
							flash.upload(url);
						}
					},
					error : function() {
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.batchTopo.execFail);
					}
				});
			});*/
});

// 初始化上传文件控件
function initUploadComponent() {
	window.flash = new TopvisionUpload("chooseFile");
	flash.onSelect = function(obj) {
		fileSize = obj.size;
		chooseFileName = obj.name;
		if (obj.size > 10000000) {
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(
					I18N.batchTopo.fileSize, obj.name));
			return;
		}
		$("#batchIpText").val(chooseFileName);
		flash.doLayout();
	};
	flash.onComplete = function(result) {
		if (!result)
			return;
		result = Ext.decode(result);
		if (result.success) {
			// 文件解析成功，已经开始发现
			var url = '/entity/showScanEntityResult.tv?dwrId=999';
			// 查看是否有非法的IP地址
			if (result.errorIpRow) {
				url += '&errorIpRow=' + result.errorIpRow.join(',');
			}
			window.parent.createDialog("scanResult", I18N.batchTopo.topoResult, 650, 470, 
					"/entity/showScanEntityResult.tv?dwrId=999", null, true, true);
			cancelClick();
		} else if (result.error) {
			// 文件解析失败
			window.parent.showMessageDlg('@COMMON.tip@', result.error);
		}
	}
}

// 初始化设备类型
function initEntityType() {
	// 过滤设备类型
	var sysObjectNotUse = [ '1.3.6.1.4.1.32285.11.2.1',
			'1.3.6.1.4.1.32285.11.1.1.1', '1.3.6.1.4.1.32285.11.1.1.1.1',
			'1.3.6.1.4.1.32285.11.2.4', '1.3.6.1.4.1.32285.11.4.21',
			'1.3.6.1.4.1.32285.11.1.1.1.3.1' ];
	if (entityTypes.join("") == 'false') {
		entityTypes = [];
	} else {
		for ( var i = 0; i < entityTypes.length; i++) {
			if (entityTypes[i].sysObjectID
					&& sysObjectNotUse.indexOf(entityTypes[i].sysObjectID) == -1) {
				entityType.push(entityTypes[i]);
			}
		}
	}

	// 填入符合条件的设备类型
	var tb = $('#deviceTypeTb');
	var tplSpan = '<span class="entityTypeSpan"><input onclick="clickCheck()" type="checkbox" class="deviceTypeClass" checked="checked" value="{0}" />{1}</span>';
	for ( var i = 0; i < entityType.length; i++) {
		var o = entityType[i];
		tb.append(String.format(tplSpan, o.sysObjectID, o.displayName));
	}

	// 全选的逻辑
	$('#allCheck').bind('click', function() {
		var $allCheckedItem = $("#allCheck")
		var isChecked = $allCheckedItem.is(":checked");
		if (isChecked) {
			$("#deviceTypeTb").find("input").attr("checked", true);
		} else {
			$("#deviceTypeTb").find("input").attr("checked", false);
		}
	})
}

/**
 * 展示添加SNMP标签页面
 */
function showAddSnmp() {
	if (snmpList.length > 4) {
		return window.parent.showMessageDlg(I18N.COMMON.tip,
				I18N.batchTopo.maxSnmpLabel);
	}

	window.top.createDialog('popBatchEntityDetail', '@batchTopo.addlabel@', 630,
			340,
			'/network/popBatchEntityDetail.jsp?snmpVersion=1&pageAction=add',
			null, true, true, function() {
		$('#addSnmp').attr('disabled', snmpList.length > 4)
	});
}

function refreshSnmpTabs() {
	var p = $("#spanDiv");
	var tplStr = '<span class="deleteSpan">\
			<a class="deleteSpanLink" href="javascript:;" onclick="modifySpan(this)" id="span{0}">{0}</a>\
			<b style="color:#8A8A8A">({1})</b>\
			<a href="javascript:;" class="deleteSpanClose nm3kTip" onclick="deleteSpan(this)" nm3kTip="@batchTopo.deleteLabel@"></a>\
		</span>';
	p.empty();
	for ( var a = 0; a < snmpList.length; a++) {
		p.append(String.format(tplStr, snmpList[a].name,
				verChange(snmpList[a].version)));
	}

	function verChange(v) {
		if (v == 0) {
			return "V1"
		} else if (v == 1) {
			return "V2C"
		} else if (v == 3) {
			return "V3"
		} else {
			return "V2C"
		}
	}
}

function modifySpan(el) {
	// 找到对应的snmpList
	var snmpName = $(el).text();
	window.top.createDialog('popBatchEntityDetail', '@batchTopo.editlabel@', 630,
			340, '/network/popBatchEntityDetail.jsp?snmpName=' + snmpName
					+ '&pageAction=modify', null, true, true);
}

function deleteSpan(el) {
	for ( var i = 0; i < snmpList.length; i++) {
		if (snmpList[i].name == $(el).prev().prev().text()) {
			snmpList.splice(i, 1);
			break;
		}
	}
	$($(el).parent()).remove();
	if ($("#nm3kTip").length > 0) {// 隐藏黑色提示标签;
		$("#nm3kTip").css("display", "none");
	}
	// add by fanzidong, 删掉标签后，添加按钮需要可以点击
	$('#addSnmp').attr('disabled', false)
}

function clickCheck() {
	if ($(".deviceTypeClass:checked").length == $(".deviceTypeClass").length) {
		$("#allCheck").attr("checked", true);
	} else {
		$("#allCheck").attr("checked", false);
	}
}

function cancelClick() {
	window.top.closeWindow("model");
	window.top.closeWindow("popBatchEntity");
}

function checkedTimeoutInput() {
	var pa = $("#timeoutInput");
	pa.css("border", "1px solid #8bb8f3");
	var v = pa.val();
	if (!v) {
		return false;
	}
	if (isNaN(v) || parseFloat(v) < 1000 || parseFloat(v) > 30000
			|| v.indexOf("-") > -1 || v.indexOf(".") > -1) {
		pa.css("border", "1px solid #ff0000");
		return false;
	}
	return true;
}

function checkedPingTimeout() {
	var pa = $("#pingTimeout");
	pa.css("border", "1px solid #8bb8f3");
	var v = pa.val();
	if (!v) {
		return false;
	}
	if (isNaN(v) || parseFloat(v) < 1 || parseFloat(v) > 30000
			|| v.indexOf("-") > -1 || v.indexOf(".") > -1) {
		pa.css("border", "1px solid #ff0000");
		return false;
	}
	return true;
}
