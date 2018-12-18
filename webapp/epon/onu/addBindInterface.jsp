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
    module onu
    IMPORT js.nm3k.Nm3kTools
</Zeta:Loader>
<script type="text/javascript">
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var connectId = '${connectId}';
	var ckBoxTable;

	//取消按钮
	function cancelClick() {
		window.parent.closeWindow('bindInterface');
	}

	//保存按钮
	function save() {
		var bindInterface = ckBoxTable.getSelectValue();
		window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.bindInterface@', 'waitingMsg', 'ext-mb-waiting');
		$.ajax({
			url : '/onu/saveBindInterface.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				connectId : connectId,
				bindInterface : bindInterface
			},
			dateType : 'text',
			success : function(response) {
				window.parent.closeWaitingDlg();
				if(response == 'success'){
					top.afterSaveOrDelete({
						title : I18N.COMMON.tip,
						html : '<b class="orangeTxt">' + '@ONU.WAN.bind@@ONU.WAN.success@' + '</b>'
					});
					window.parent.getFrame("entity-" + onuId).reloadStore();
					cancelClick();
				}else{
					window.parent.showMessageDlg("@COMMON.tip@",'@ONU.WAN.bind@@ONU.WAN.failed@');
				}
			},
			error : function() {
				window.parent.showMessageDlg("@COMMON.tip@",'@ONU.WAN.bind@@ONU.WAN.failed@');
			},
			cache : false
		});
	}

	$(document).ready(function() {
		var aData = [ {
			label : 'SSID-1',
			checked : false,
			value : 1
		}, {
			label : 'SSID-2',
			checked : false,
			value : 2
		}, {
			label : 'SSID-3',
			checked : false,
			value : 3
		}, {
			label : 'SSID-4',
			checked : false,
			value : 4
		}, {
			label : 'LAN1',
			checked : false,
			value : 5
		}, {
			label : 'LAN2',
			checked : false,
			value : 6
		}, {
			label : 'LAN3',
			checked : false,
			value : 7
		}, {
			label : 'LAN4',
			checked : false,
			value : 8
		}/* , {
			label : 'LAN5',
			checked : false,
			value : 9
		}, {
			label : 'LAN6',
			checked : false,
			value : 10
		}, {
			label : 'LAN7',
			checked : false,
			value : 11
		}, {
			label : 'LAN8',
			checked : false,
			value : 12
		} */ ];

		$.ajax({
			url : '/onu/loadBindInterface.tv',
			type : 'POST',
			data : {
				onuId : onuId,
				connectId : connectId
			},
			dateType : 'json',
			success : function(response) {
				var bindInterface = response.bindInterface;
				for (var i = 0; i < bindInterface.length; i++) {
					var index = bindInterface[i] - 1;
					aData[index].checked = true;
				}
				var alreadyBindInterface = response.alreadyBindInterface;
				for (var i = 0; i < alreadyBindInterface.length; i++) {
					var index = alreadyBindInterface[i] - 1;
					aData[index].disabled = true;
				}
			},
			error : function() {
			},
			complete : function() {
				ckBoxTable = new Nm3kTools.createCheckBoxTable({
					renderId : 'portBind',
					lineNum : 4,
					title : '@ONU.WAN.interfaceList@',
					data : aData
				});
			},
			cache : false
		});
	})
</script>
<title>@ONU.WAN.bindInterface@</title>
</head>
<body class="openWinBody" onload="">
	<div class="edge10 pT20">
		<div id='portBind'></div>
	</div>

	<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
		<ol class="upChannelListOl pB0 pT80 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig"
				onmouseup="save()"> <span><i class="miniIcoData"></i>@COMMON.save@</span>
			</a></li>
			<li><a href="javascript:;" class="normalBtnBig"
				onclick="cancelClick()"> <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
			</a></li>
		</ol>
	</div>
</body>
</Zeta:HTML>
