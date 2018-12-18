<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module camera
</Zeta:Loader>
<script src="../js/placeHolderHack.js"></script>
<style type="text/css">
#wrapAdvance .x-form-field-wrap{ width:auto !important;}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = '${entity.entityId}';

Ext.onReady(function(){
	//加载slot
	window.slotStore = new Ext.data.JsonStore({
		url : '/camera/getOltSlotList.tv',
		fields : ["slotId", "slotNo"],
		baseParams : { entityId : entityId }
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
		width : 100
	});
	slotStore.load();
	
	//加载pon口
	window.ponStore = new Ext.data.JsonStore({
		url : '/camera/getOltPonList.tv',
		fields : ["ponId", "ponNo"]
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
		width : 100
	});
	//加载onu
	window.onuStore = new Ext.data.JsonStore({
		url : '/camera/getPonOnuList.tv',
		fields : ["onuId", "onuIndex" ,{
			name : "displayOnu",mapping:'onuIndex',convert:function(value){
				return getLocationByIndex(value);
			}
		}]
	});
	
	window.onuCombo = new Ext.form.ComboBox({
		id : "onuCombox",
		store : onuStore,
		applyTo : "onuSelect",
		mode : 'local',
		valueField : 'onuIndex',
		displayField : 'displayOnu',
		emptyText : "@COMMON.select@",
		selectOnFocus : true,
		typeAhead : true,			
		triggerAction : 'all',
		editable : false,
		width : 100
	});
	//当slot变化时加载对应的pon
	slotCombo.on('select', function(comboBox){
		//当清除加载的pon和onu
		ponCombo.clearValue(); 
	  	onuCombo.clearValue(); 
	  	//加载对应的pon
		var slotValue = comboBox.getValue();
		ponStore.load({params: {entityId: entityId, slotId: slotValue}});
		//要将之前加载的onu列表清除
		onuStore.removeAll();
	})
	//当pon口变化时加载对应的onu
	ponCombo.on('select', function(comboBox){
		//先清除加载的onu
		onuCombo.clearValue(); 
		//加载对应的onu
		var ponValue = comboBox.getValue();
		onuStore.load({params: {entityId: entityId, ponId: ponValue}});
	})
	
	//加载球机类型
	window.typeStore = new Ext.data.JsonStore({
		url : '/camera/getAllCameraType.tv',
		fields : ["type"]
	});
	
	window.typeCombo = new Ext.form.ComboBox({
		id : "typeCombox",
		store : typeStore,
		applyTo : "typeSelect",
		mode : 'local',
		valueField : 'type',
		displayField : 'type',
		emptyText : "@COMMON.select@",
		selectOnFocus : true,
		typeAhead : true,			
		triggerAction : 'all',
		width : 100
	});
	typeStore.load();
	
	//加载摄像头列表
	window.dataStore = new Ext.data.JsonStore({
		url : '/camera/loadCameraList.tv',
		fields : ["entityId", "slotIndex", "cameraNo","ponIndex","onuIndex","cameraIndex","mac","ip","eponIndex","cameraType","noteInfo","onuInfo","location"],
		root : 'data',
		totalProperty: 'rowCount',
		baseParams : {
			entityId : entityId
		}
	});
	
	function render(v){
		return v || "<i style='color:gray'>@CAMERA.unPlan@</i>";
	}
	window.colModel = new Ext.grid.ColumnModel([
   		{header:"IP", dataIndex:"ip", width:80},
   		{header:"<div class='txtCenter'>" + "@CAMERA.hungOnu@" + "</div>", dataIndex:"onuInfo",width:60},
   		{header:"MAC", dataIndex:"mac",width:80},
   		{header:"<div class='txtCenter'>" + "@COMMON.location@" + "</div>", dataIndex:"location",renderer: render},
   		{header:"@CAMERA.type@", dataIndex:"cameraType", width:60,renderer: render},
   		{header:"<div class='txtCenter'>" + "@COMMON.note@" + "</div>", dataIndex:"noteInfo", width:120,renderer: render},
   		{header:"@COMMON.manu@", dataIndex:"op", width:80,  renderer : opeartionRender}
   		
   	]);//end cm;

   	window.cameraGrid = new Ext.grid.GridPanel({
		stripeRows:true,
		region: 'center',
		bbar: buildPagingToolBar(),
		cls: 'normalTable',
		tbar: [
		      {text:'@CAMERA.planInfo@',iconCls:'bmenu_arrange',handler: planningInfoFn }, "-",
		      {text:'@CAMERA.phyInfo@',iconCls:'bmenu_equipment',handler: cameraInfoFn}, "-",
		      {text:'@COMMON.fetch@',iconCls:'bmenu_equipment',handler: refreshDataFn}
		],
		store: dataStore,
		cm : colModel,
		viewConfig:{
			forceFit: true
		},
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		})
	});
	dataStore.load({params: {start: 0, limit: pageSize}});
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [cameraGrid, {
    		region: 'north',
    		height: 120,
    		contentEl : 'topPart',
			border: false
        }]
	});
	
});//end document.ready;

//通过index获得location
function getLocationByIndex(index){
	index = index.toString(16);
	//如果首位为0则补0。
	index = '00000'+index;
	index = index.substring(index.length-10);
	var slot = parseInt(index.substring(0,2),16);
	var port = parseInt(index.substring(2,4),16);
	var llid = parseInt(index.substring(4,6),16);
	return slot+"/"+port + ":"+llid;	
}

//构建分页工具栏
function buildPagingToolBar() {
   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:dataStore,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
   return pagingToolbar;
}

//点击规划信息;
function planningInfoFn(){
	window.parent.createDialog("planningInfo", '@CAMERA.planInfo@',  800, 500, "epon/camera/planningInfo.jsp", null, true, true);
}
//硬件信息;
function cameraInfoFn(){
	window.parent.createDialog("cameraInfo", '@CAMERA.phyInfo@',  800, 500, "epon/camera/cameraInfo.jsp", null, true, true);
}

//构建Grid操作栏
function opeartionRender(value, cellmate, record){
	return String.format("<a href='javascript:;' onClick='unregister(\"{0}\")'>@COMMON.unbind@</a> / <a href='javascript:;' onClick='replaceCamera(\"{0}\")'>@CAMERA.replace@</a>",record.id);
}
//解绑定
function unregister(rid){
	var data = dataStore.getById(rid).data;
	window.parent.showConfirmDlg("@COMMON.tip@", String.format("@CAMERA.cfmUnbind@ ",data.ip), function(type) {
        if (type == 'no') return                      
        window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@");
        $.ajax({
            url: '/camera/deleteCameraConfig.tv',type: 'POST',
            data: {
            	entityId : data.entityId,
            	eponIndex : data.eponIndex,
            	cameraIndex : data.cameraIndex
            },
            success: function() {
            	top.closeWaitingDlg();
            	top.nm3kRightClickTips({
    				title: "@COMMON.tip@",
    				html: "@CAMERA.unbindOk@"
    			});
            	dataStore.reload();
            }, error: function(json) {
                top.showMessageDlg("@COMMON.error@", "@CAMERA.unbindEr@");
            }, cache: false
        });
    });
}

//替换
function replaceCamera(){
	var data = cameraGrid.getSelectionModel().getSelected().data;
	var url = "/epon/camera/replaceCamera.jsp?entityId="+entityId+"&eponIndex="+data.eponIndex+"&ip="+data.ip+"&location="+data.location
			+"&cameraNo="+data.cameraNo +"&displayOnu="+data.onuInfo+"&cameraIndex="+data.cameraIndex;
	window.parent.createDialog("bindCamera", '@CAMERA.replaceCamera@',  800, 500, url, null, true, true,function(){
		dataStore.reload();
	});
}

//刷新数据
function refreshDataFn(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/camera/refreshCameraData.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		success : function() {
			top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: "@COMMON.fetchOk@"
			});
			//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
			dataStore.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//显示高级查询
function showAdvanceQuery(){
	var $simple = $("#wrapSimple");
	var $advance = $("#wrapAdvance");
	$simple.animate({left: -980}, "fast", function(){
		$simple.css({display:"none", left:0});
		$advance.css("display","block").css("left",-980).animate({left:0},"fast");
	});
}

//显示快捷查询
function showSimpleQuery(){
	var $simple = $("#wrapSimple");
	var $advance = $("#wrapAdvance");
	$advance.animate({left: -980}, "fast", function(){
		$advance.css({display:"none", left:0});
		$simple.css("display","block").css("left",-980).animate({left:0},"fast");
	});	
}

//快捷查询
function simpleQuery(){
	var queryString = $("#queryContent").val();
	//避免受高级查询的影响,先清除baseParams中的参数
	dataStore.baseParams = {};
	dataStore.setBaseParam('entityId', entityId);
	dataStore.setBaseParam('queryInfo', queryString);
	dataStore.setBaseParam('start', 0);	
	dataStore.setBaseParam('limit', pageSize);
	dataStore.load();
}

//高级查询
function advanceQuery(){
	var slotId = slotCombo.getValue();
	var ponId = ponCombo.getValue();
	var onuIndex = onuCombo.getValue();
	var type = typeCombo.getValue();
	var ip = $("#ipInput").val();
	var mac = $("#macInput").val();
	var location = $("#locationInput").val();
	var note = $("#noteInput").val();
	if(!Validator.isFuzzyIpAddress(ip) && ip){
		top.nm3kRightClickTips({
			title: "@COMMON.error@",
			html: "@COMMON.reqValidIp@"
		});
		//return top.showMessageDlg("@COMMON.error@", "@COMMON.reqValidIp@",function(){
			$("#ipInput").focus();
		//});
	}
	if(!Validator.isFuzzyMacAddress(mac) && mac){
		top.nm3kRightClickTips({
			title: "@COMMON.error@",
			html: "@COMMON.reqValidMac@"
		});
		//return top.showMessageDlg("@COMMON.error@", "@COMMON.reqValidMac@",function(){
			$("#macInput").focus();
		//});
	}
	//避免受快捷查询的影响,先清除baseParams中的参数
	dataStore.baseParams = {};
	dataStore.setBaseParam('entityId', entityId);
	dataStore.setBaseParam('slotId', slotId);
	dataStore.setBaseParam('ponId', ponId);
	dataStore.setBaseParam('eponIndex', onuIndex);
	dataStore.setBaseParam('ip', ip);
	dataStore.setBaseParam('mac', mac);
	dataStore.setBaseParam('location', location);
	dataStore.setBaseParam('cameraType', type);
	dataStore.setBaseParam('noteInfo', note);
	dataStore.setBaseParam('start', 0);	
	dataStore.setBaseParam('limit', pageSize);
	dataStore.load();
}

</script>
</head>
<body class="whiteToBlack">
	<!--头部菜单开始 -->
	<div id="topPart">
	<%@ include file="/epon/inc/navigator.inc"%>
		<div id="wrapSimple" class="pT20" style="width:980px; overflow:hidden; position:absolute; top:33px; left:0;">
			<div class="edge10">
				<table class="queryTable">
					<tr>
						<td><input type="text" class="normalInput w300" id="queryContent" placeHolder="@CAMERA.placeHolder@"/></td>
						<td><a id="simpleQuery" href="javascript:;" class="normalBtn" style="margin-right:5px;" onclick="simpleQuery()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
						<td><a href="javascript:;" id="addVanceQuery" class="yellowLink" onclick="showAdvanceQuery()">@CAMERA.advancedQuery@</a></td>
					</tr>
				</table>
			</div>
		</div>
		<div id="wrapAdvance" class="displayNone" style="width:980px; overflow:hidden; position:absolute; top:33px; left:0;">
			<div class="pT5 pL10">
				<table class="queryTable">
					<tr>
						<td class="rightBlueTxt">@COMMON.slot@@COMMON.maohao@</td>
						<td class="pR10">
							<div class="w100">
								<input type="text" id="slotSelect" />
							</div>
						</td>
						<td class="rightBlueTxt">@COMMON.pon@@COMMON.maohao@</td>
						<td class="pR10">
							<div class="w100">
								<input type="text" id="ponSelect" />
							</div>
						</td>
						<td class="rightBlueTxt">@CAMERA.hungOnu@@COMMON.maohao@</td>
						<td class="pR10">
							<div class="w100">
								<input type="text" id="onuSelect" />
							</div>
						</td>
						<td class="rightBlueTxt">IP@COMMON.maohao@</td>
						<td class="pR10">
							<input class="normalInput w100" id="ipInput"/>
						</td>
						<td valign="middle" rowspan="3" width="160">
							<ul class="floatLeftUl">
								<li>
									<a href="javascript:;" class="normalBtn" style="margin-right:5px;" onclick="advanceQuery()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
								</li>
								<li style="padding-top:4px;">
									<a href="javascript:;" class="yellowLink" onclick="showSimpleQuery()">@CAMERA.fastQuery@</a>
								</li>
							</ul>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">MAC@COMMON.maohao@</td>
						<td class="pR10">
							<input class="normalInput w100" id="macInput"/>
						</td>
						<td class="rightBlueTxt">@COMMON.location@@COMMON.maohao@</td>
						<td class="pR10">
							<input class="normalInput w100" id="locationInput"/>
						</td>
						<td class="rightBlueTxt">@CAMERA.type@@COMMON.maohao@</td>
						<td class="pR10">
							<div class="w100">
								<input type="text" id="typeSelect" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@COMMON.note@@COMMON.maohao@</td>
						<td class="pR10" colspan="7">
							<input class="normalInput" style="width:266px;" id="noteInput"/>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
    <div id="gridPart"> </div>
</body>
</Zeta:HTML>
