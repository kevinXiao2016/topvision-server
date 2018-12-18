<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
    library ext
    library zeta
    module network
</Zeta:Loader>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<style type="text/css">
#rear {
	position: absolute;
	top: 382px;
	left: 75px;
	font-size: 25;
}

#scaning {
	position: absolute;
	top: 382px;
	left: 15px;
	font-size: 25;
}

.backRed {
	color: red;
}

.backGreen {
	color: green;
}
</style>
<script type="text/javascript">
	var count = 0;
	var isStop = false;
	var errorIpRows = '${errorIpRows}';
	var proData = new Array();
	var proGrid;
	var store;

	function walk() {
		setTimeout(function() {
			if (isStop)
				return;
			count++;
			if (count % 4 == 0)
				$("#rear").text("");
			else if (count % 4 == 1)
				$("#rear").text(".");
			else if (count % 4 == 2)
				$("#rear").text("..");
			else if (count % 4 == 3)
				$("#rear").text("...");
			walk();
		}, 300);
	}

	$(function() {
		//加载导入数据表格
		loadTheGrid();

		//添加回调，接收导入设备返回的数据
		window.top.addCallback("scanOption", function(entity) {
			var re = new Ext.data.Record();
			re.data = {
				ipAddress : entity.ip,
				typeName : entity.typeName,
				sysName : entity.sysName,
				name : entity.name,
				topoInfo : entity.topoInfo
			};
			store.add(re);
		}, 999);

		//添加回调，接收导入设备结束
		window.top.addCallback("scanFinish", function() {
			isStop = true;
			$("#rear").hide();
			$("#scaning").hide();
			R.stopBT.hide();
			R.finishBT.show();
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.batchTopo.topoFinish);
			try {
				var win = window.parent.getFrame("entitySnap")
				if (win != null) {
					win.store.reload();
				}
				win = window.parent.getFrame("oltList")
				if (win != null) {
					win.store.reload();
				}
			} catch (e) {
			}
		}, 999);

		//隐藏关闭页面按钮
		R.finishBT.hide();
		window.win = window.parent.getWindow("scanResult");
		walk();

	});

	function loadTheGrid() {
		var cm = [ {
			header : I18N.batchTopo.ipAddress,
			dataIndex : 'ipAddress',
			sortable : true
		}, {
			header : I18N.batchTopo.entityType,
			dataIndex : 'typeName',
			sortable : true
		}, {
			header : I18N.batchTopo.entityName,
			dataIndex : 'sysName',
			sortable : true
		}, {
			header : I18N.batchTopo.entityDisplayName,
			dataIndex : 'name',
			sortable : true
		}, {
			header : I18N.batchTopo.entityTopoResult,
			dataIndex : 'topoInfo',
			sortable : true,
			renderer : stateRenderer
		} ];
		store = new Ext.data.SimpleStore({
			root : 'data',
			fields : [ 'ipAddress', 'typeName', 'snmpParamIndex', 'sysName', 'name', 'topoInfo' ]
		});
		proGrid = new Ext.grid.GridPanel({
			stripeRows : true,
			region : "center",
			bodyCssClass : 'normalTable',
			viewConfig : {
				forceFit : true
			},
			id : 'grid',
			renderTo : 'gridDiv',
			border : true,
			frame : false,
			autoScroll : true,
			width : 620,
			height : 350,
			store : store,
			loadMask : {
				msg : I18N.batchTopo.entityIsScaning
			},
			columns : cm
		});
	}
	
	function stateRenderer(v, m, r) {
		if (v == "EntityExists") {
			return "<span style='color:#f00;'>" + I18N.batchTopo.entityExists + "</span>";
		} else if (v == "ReplaceEntity") {
			return "<span style='color:green;'>" + I18N.batchTopo.entityReplace + "</span>";
		} else if (v == "LicenseLimit") {
			return "<span style='color:#f00;'>" + I18N.batchTopo.licenseLimit + "</span>";
		} else if (v == "blackEntity") {
			return "<span style='color:#f00;'>" + I18N.batchTopo.entityForbidDiscovery + "</span>";
		} else {
			return "<span style='color:green;'>" + I18N.batchTopo.entityDiscoverySuccess + "</span>";
		}
	}

	function cancelClick() {
		window.top.closeWindow("scanResult");
	}
	
	function finishTopo() {
		R.stopBT.setText("@batchTopo.stopping@").setDisabled(true);
		$.ajax({
			url : '/entity/stopTopo.tv',
			type : 'GET',
			cache : false
		});
	}
</script>
	</head>
	<body class="openWinBody">
		<div id=gridDiv style='padding-left: 15px; padding-top: 15px;'></div>
		<span id="scaning">@batchTopo.scanning@</span>
		<span id="rear"></span>
		<Zeta:ButtonGroup>
			<Zeta:Button id="stopBT" onClick="finishTopo()">@batchTopo.stopTopo@</Zeta:Button>
			<Zeta:Button id="finishBT" onClick="cancelClick()"
				icon="miniIcoSaveOK">@batchTopo.topoComplete@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>
