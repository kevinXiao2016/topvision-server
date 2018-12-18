<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	import js.zetaframework.component.NetworkNodeSelector
	module CongfigBackup
</Zeta:Loader>
<head>
<script type="text/javascript">

function closeBtClick(){
	window.parent.closeWindow('configFileImported')
}

function resultRender(v){
	if (v == 0)
		return "<div style='color:green'>@RESOURCES/COMMON.success@</div>"
	else if (v == 1)
		return "<div style='color:red'>@RESOURCES/COMMON.failure@</div>"
	else if (v == 2)
		return "<div style='color:red'>@RESOURCES/telnet.loginFaild@</div>"
	else if (v == 3)
		return "<div style='color:red'>@RESOURCES/telnet.connectFaild@</div>"
	else if (v == 4)
		return "<div style='color:red'>@RESOURCES/telnet.toomanyConnections@</div>"
	else if (v == 5)
		return "<div style='color:red'>@RESOURCES/ftp.connectFaild@</div>"
}

function operationRender(v){
	switch(v){
	case 1: return "@Config.apply@";break;
	case 2: return "@Config.save@";break;
	case 3: return "@Config.backup@";break;
	}
}

function clientIpRender(v){
	return v == null ? "": v;
}

function operatorRender(v,m,r){
	if(r.data.userId == -1){
		return "System";
	}else{
		return v;
	}
}


Ext.onReady(function(){
	var cm = new Array;
    cm[cm.length] = {header : "<div style='text-align:center'>@Config.oltConfigFileImported.deviceName@/IP</div>", align:'left',dataIndex: 'deviceName',  width: 150}
	//cm[cm.length] = {header : "@COMMON.file@", dataIndex: 'fileName', align: 'center',  width: 120}
	cm[cm.length] = {header : "@Config.oltConfigFileImported.operation@",dataIndex: 'operationType',  width: 100,renderer : operationRender}
	cm[cm.length] = {header : "@Config.oltConfigFileImported.operator@",dataIndex: 'username',  width: 70,renderer : operatorRender}
	cm[cm.length] = {header : "@RESOURCES/SYSTEM.clientIpAddress@",dataIndex: 'clientIp',  width: 100,renderer : clientIpRender}
	cm[cm.length] = {header : "@Config.oltConfigFileImported.operationResult@",dataIndex: 'result',  width: 100,renderer : resultRender}
	cm[cm.length] = {header : "@RESOURCES/EVENT.dateHeader@",dataIndex: 'operationTime',  width: 120}
	store = new Ext.data.JsonStore({
		url:'/configBackup/queryHistroyRecords.tv',cache:false,
		root : 'data' ,
		fields: ['username', 'result' ,'deviceName' ,'userId','clientIp' , 'fileName' , 'operationType' , 'operationTime']
	})
	grid = new Ext.grid.GridPanel({
		id : 'Grid',
		title : "@Config.oltConfigFileImported.operationLog@",
		renderTo : 'GridDiv',
		border : true,
		viewConfig:{
			forceFit:true
		},
		cls: 'normalTable pT10',
		height : 340,
		frame : false,
		autoScroll : true,
		store : store,
		columns : cm
	})
    
    window.selector = new NetworkNodeSelector({
        id: 'select_olt',
        renderTo: "oltContainer",
        include8800B : true,
        autoLayout: true
    });
})


function searchBtClick(){
	var data = {};
	var entityId = parseInt($('#select_olt').val());
		data.entityId = entityId;
	var operationType = $("#operationType").val();
		data.operationType = operationType;
	store.load({params:data});
	//store.reload();
}

</script>
</head>
<body class="openWinBody">
	<div class="edge10  pT30">
		<table cellpadding="0" cellspacing="0" rules="none">
			<tr>
				<td class="pR10">@Config.device@:</td>
				<td>
					<div id="oltContainer" class="w150" ></div>
				</td>
				<td class="pL30 pR10">@Config.operationType@:</td>
				<td class="pR5">
					<select id="operationType" class="w120 normalSel" >
						<option value="-1">@Config.select@</option>
						<option value="1">@Config.apply@</option>
						<option value="2">@Config.save@</option>
						<option value="3">@Config.backup@</option>
					</select>
				</td>
				<td>
					<a id="searchBt"  onclick='searchBtClick()' href="javascript:;" class="normalBtn mL4"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a> 
				</td>
			</tr>
		</table>
		<div id="GridDiv"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>