<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    plugin DateTimeField
    module network
</Zeta:Loader>
<style type="text/css">
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:385px;}
#w2400{ width:2400px; position:absolute; top:0; left:0;}
#step1, #step2, #step3{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step2{ left:800px;}
#step3{ left:1600px;}
</style>
<script type="text/javascript">
var name;
var startTime;
var imageFile;
var versions = ${versions};

var pageSize = <%= uc.getPageSize() %>;
var baseGrid;
var sm;
var baseStore;
var ccWithAgentTypes = ${ccWithAgentTypes};
var oltTypes = ${oltTypes};
var transferType = "";
var dir = "DESC";
var sort = "sysUpTime"

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format("@resources/COMMON.displayPerPage@", pageSize), '-'
    ]});
    return pagingToolbar;
}

function closeClick() {
	window.parent.closeWindow('newJob');
}

function addJob(){
	var name = $("#jobName").val();
	var type = $("#upgradeTime").val();
	var versionIndex = $("#versionName").val();
	var version = versions[versionIndex];
	var versionName = version.versionName;
	var transferType = version.transferType;
	var	subTypes = versions[versionSelectIndex].subTypeString.split(','); //subTypes镜像类型
	var subType = subTypes[$("#subType").val()];
	
	var imageFile = $("#imageFile").val();
	
	if(sm.getSelections().length==0){
        window.top.showMessageDlg('@resources/COMMON.tip@', "@offManagement.selectDevice@");
        return;
    }
	
	var rs=sm.getSelections();
    var entityIds=[];
    for(var i = 0; i < rs.length; i++){
    	entityIds[i]=rs[i].data.entityId;
    }
	
	$.ajax({
		  url: '/upgrade/addUpgradeJob.tv',
	      type: 'POST',
          data:{
        	  "name" : name,
        	  "startTime" : Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s'),
        	  "type" : type,
        	  "transferType" : transferType,
        	  "versionName" : versionName,
        	  "imageFile" : imageFile,
        	  "entityIds" : entityIds.join("$"),
        	  "subType" : subType
          },
			success : function(response) {
				if(response == 'exist'){
					window.top.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.jobExist@");
					gotoAndStop(-800, 2);
					gotoAndStop(0, 1);
					return;
				}
				//刷新父页面的树结构
	   			parent.frames["menuFrame"].buildUpgradeJobTree();
	   			closeClick();
			},
			error : function(response) {
			},
			cache : false
		});
	}
	
function renderSysStatus(value, p, record) {
    if (record.data.status == '1') {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                "@label.online@");
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                "@resources/COMMON.offline@");
    }
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

$(function() {
	Ext.QuickTips.init();
	var minDate = new Date();
	startTime = new Ext.ux.form.DateTimeField({
		width:223,
		editable: false,
	    minValue: minDate,
	    renderTo: 'startTime'
	});
	
	sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = $(window).width() - 240;
	var baseColumns = [
	                   sm,
	            {header: "@resources/EVENT.statusHeader@", width: w * 0.1, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'status', renderer: renderSysStatus},
	           	{header: '<div class="txtCenter">@COMMON.alias@</div>',  width: w * 0.2, align: 'left', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'name'},
	           	{header: "@batchUpgrade.manageIp@", width: 100, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'ip'},
	           	{header: "MAC",  width: 110, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'mac'},
	           	{header: "@batchTopo.entityType@", width: w * 0.2, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'typeName'},
	           	{header: "@batchUpgrade.softVersion@",  width: w * 0.3, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'softwareVersion', renderer:addCellTooltip},
	           	{header: "@batchtopo.createtime@",  width: w * 0.2, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'sysUpTime'}
	           	];
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/upgrade/getEntity.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['entityId', 'name','ip', 'mac','typeId', 'softwareVersion',
	             'status','createTime', 'sysUpTime', 'typeName']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	
	baseGrid = new Ext.grid.GridPanel({
		cls: 'normalTable',
		loadMask: true,
		border: true, 
		renderTo: 'entityGrid',
		width: 780,
		height: 270,
		store: baseStore, 
		margins: "0px",
		sm: sm,
		cm: baseCm,
		region:'center',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	//baseStore.load({params:{start:0, limit: pageSize}});
	
	$("#startTimeTr").hide()
	
	$("#upgradeTime").change(function(){
        if(this.value==0){
        	$("#startTimeTr").hide()
        }else{
        	$("#startTimeTr").show()
        }
    });
	
	 var versionPosition = Zeta$('versionName');
	 for(var i = 0; i < versions.length; i++){
         var option = document.createElement('option');
         option.value = i;
         option.text = versions[i].versionName;
         try {
        	 versionPosition.add(option, null);
         } catch(ex) {
        	 versionPosition.add(option);
         }
	  }
	 $("#stepInfo").html(step1)
})

var step1 = "<b class='orangeTxt'>@batchUpgrade.step1@@COMMON.maohao@</b>@batchUpgrade.step1Tip@",
    step2 = "<b class='orangeTxt'>@batchUpgrade.step2@@COMMON.maohao@</b>@batchUpgrade.step2Tip@",
    step3 = "<b class='orangeTxt'>@batchUpgrade.step3@@COMMON.maohao@</b>@batchUpgrade.step3Tip@";

function gotoAndStop(num, toStep){
	if(toStep == 1){
		$("#stepInfo").html(step1)
	}
	if(toStep == 2){
		var jobName = $("#jobName").val();
		if(jobName == "" || (jobName != "" && !V.isAnotherName(jobName, null))) {
			$("#stepInfo").html(step1)
			$("#jobName").focus();
			return;
		}
		var upgradeTime = $("#upgradeTime").val();
		var upgradeTimeString = "";
		if(upgradeTime == 0){
			upgradeTimeString = "@batchUpgrade.upgradeImmediately@"
		}else{
			upgradeTimeString = Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s');
			if(upgradeTimeString == ""){
				window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.chooseTimingTime@");
				return;
			}
		}
		$("#stepInfo").html(step2 + "<br />" + "@batchUpgrade.jobName@：" + jobName  +  "<span class='pL20'>@batchUpgrade.upgradeTime@@COMMON.maohao@<span>" + upgradeTimeString);
	}
	if(toStep == 3){
		var jobName = $("#jobName").val();
		var upgradeTime = $("#upgradeTime").val();
		var upgradeTimeString = "";
		if(upgradeTime == 0){
			upgradeTimeString = "@batchUpgrade.upgradeImmediately@"
		}else{
			upgradeTimeString = Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s');
		}
		
		var versionName = $("#versionName").val();
		if(versionName == -1){
			window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.pleaseChooseUpgradeVersion@");
			return;
		}
		var	subTypes = versions[versionSelectIndex].subTypeString.split(','); //subTypes镜像类型
		if(subTypes.length > 1 && $('#subType').val() < 0){
			window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.pleaseChooseImageType@");
			return;
		}
		var versionDisplayName = versions[versionName].versionName;
		var str = step3 + "<br />" + "@batchUpgrade.jobName@：" + jobName  +  "<span class='pL20'>@batchUpgrade.upgradeTime@@COMMON.maohao@<span>" + upgradeTimeString + "<br />" + "@batchUpgrade.upgradeVersion@：" + versionDisplayName;
		$("#stepInfo").html(str);
		//根据第2步中选择的镜像类型来决定下拉框中的设备类型
		
		transferType = versions[versionSelectIndex].transferType;
		
		var deviceTypePosition = Zeta$('entityType');
		deviceTypePosition.length = 0
		
		var defaultOption = document.createElement('option');
		defaultOption.value = 0;
		defaultOption.text = '@sendConfig.pleaseChoose@'
        deviceTypePosition.add(defaultOption, null);
		var typeDisplayNames = versions[versionSelectIndex].typeDisplayNames;
		if(transferType == 'ftp'){
			for(var i = 0; i < oltTypes.length; i++){
		        var option = document.createElement('option');
		        option.value = oltTypes[i].typeId;
		        option.text = oltTypes[i].displayName;
		        try {
		        	deviceTypePosition.add(option, null);
		        } catch(ex) {
		        	deviceTypePosition.add(option);
		        }
		  }
		}else{
			for(var i = 0; i < ccWithAgentTypes.length; i++){
		        var option = document.createElement('option');
                var types = typeDisplayNames.split(",");
		        if($.inArray(ccWithAgentTypes[i].displayName,types) < 0){
		        	continue;
		        }
		        option.value = ccWithAgentTypes[i].typeId;
		        option.text = ccWithAgentTypes[i].displayName;
		        try {
		        	deviceTypePosition.add(option, null);
		        } catch(ex) {
		        	deviceTypePosition.add(option);
		        }
		  }
		}
		query();
	}
	$("#w2400").animate({left:num},'fast');
}

function query(){
    var name = $("#entityName").val();
	var ip = $("#ip").val();
	var mac = $("#mac").val();
	var typeId = $("#entityType").val();
	var entityType;
	var versionIndex = $("#versionName").val();
	var version = versions[versionIndex];
	var versionName = version.versionName;
	if(transferType == 'tftp'){
		entityType = 30000;
	}
	if(transferType == 'ftp'){
		entityType = 10000;
	}
	
    baseStore.on("beforeload", function() {
    	baseStore.baseParams = {name: name, ip: ip, mac: mac, typeId: typeId, transferType: transferType, ersionName: versionName, entityType: entityType, start:0,limit:pageSize};
    });
    
    baseStore.load({params: {name: name, ip: ip, mac: mac, typeId: typeId, transferType: transferType, versionName: versionName, entityType: entityType, start:0,limit:pageSize}});
    
}

var filePath; //升级镜像，路径
var versionSelectIndex; //升级版本选择框，所选择的index
var imageSelectIndex; //镜像类型选择框，所选择的index

function versionNameChange(){
	$('#imageFile').val('');
	var strTemplate = '<option value="{0}">{1}</option>';
	versionSelectIndex = $('#versionName').val();	
	if(versionSelectIndex == -1){
		$('#subTypeTr').hide();
		$('#imageFileTr').hide();
		return;
	}
	var	subTypes = versions[versionSelectIndex].subTypeString.split(','); //subTypes镜像类型
	if(subTypes.length == 1 && subTypes[0] == ""){ //如果不存在subType镜像类型
		var versionName = versions[versionSelectIndex].versionName;
		$('#subTypeTr').hide(); 
		if(versions[versionSelectIndex].transferType == 'ftp'){
			filePath = '/UpgradeBin/' +  versionName + '/' + versionName + '.bin';
		}else if(versions[versionSelectIndex].transferType == 'tftp'){
			filePath = versionName + '.bin';
		}
		$('#imageFile').val(filePath);
	}else{//如果存在subType镜像类型
		$('#subTypeTr').show(); 
		var subTypeOption = $('#subType');
		subTypeOption.empty().append(String.format(strTemplate, -1, '@sendConfig.pleaseChoose@'));
		for(var i = 0; i < subTypes.length; i++){
			subTypeOption.append(String.format(strTemplate, i, subTypes[i]));
		}
	}
	$('#imageFileTr').show();
}

function subTypeChange(){
	$('#imageFile').val('');
	var imageSelectIndex = $('#subType').val();
	if(imageSelectIndex >= 0){
		var subTypeStr = document.getElementById('subType').options[document.getElementById("subType").selectedIndex].text;
		if(versions[versionSelectIndex].transferType == 'ftp'){
			filePath = '/UpgradeBin/' +  versions[versionSelectIndex].versionName + '/' + versions[versionSelectIndex].versionName + '-' + subTypeStr +  '.bin';
		}else if(versions[versionSelectIndex].transferType == 'tftp'){
			filePath = versions[versionSelectIndex].versionName + '-' + subTypeStr + '.bin';
		}
		$('#imageFile').val(filePath);
	}
}

</script>
</head>
	<body class="openWinBody">
		<div class="openWinHeader">
			<div class="openWinTip" id="stepInfo"></div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div id="w800">
			<div id="w2400">
				<div id="step1">
					<div class="edgeTB10LR20 pT10">
					     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
					         <tbody>
					             <tr>
					                 <td class="rightBlueTxt" width="280">
					                     <label for="jobName">@batchUpgrade.jobName@<span class="redTxt">*</span></label>
					                 </td>
					                 <td>
					                     <input class="w220 normalInput" type="text" id="jobName" tooltip='@NETWORK.queryNmNameTip@' />
					                 </td>
					             </tr>
					             <tr class="darkZebraTr">
									<td class="rightBlueTxt">
										<label for="upgradeTime">@batchUpgrade.upgradeTime@<span class="redTxt">*</span></label></td>
									<td>
										<select style="width:223px;" id="upgradeTime" class="normalSel">
						                	<option value="0" selected>@batchUpgrade.upgradeImmediately@</option>
						                	<option value="1">@batchUpgrade.timingUpgrade@</option>
						            	</select>
									</td>
								</tr>
								<tr id="startTimeTr">
									<td class="rightBlueTxt">
										<label for="startTime">@batchUpgrade.timingTime@<span class="redTxt">*</span></label>
									</td>
									<td>
										<div id="startTime"></div>
									</td>
								</tr>
					         </tbody>
					     </table>
					     <div class="noWidthCenterOuter clearBoth pT50">
					     	<ol class="upChannelListOl pB0 p130 noWidthCenter">
					     		<li><a  onclick="gotoAndStop(-800, 2)" id="nextButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i>@resources/SYSTEM.nextStep@</span></a></li>
					        	<li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@resources/COMMON.cancel@</span></a></li>
					    	 </ol>
						</div>
					</div>
					
				</div>
				<div id="step2">
					<div class="edgeTB10LR20 pT10">
						<table class="zebraTableRows">
								<tr>
									<td class="rightBlueTxt w280"><label for="versionName">@batchUpgrade.upgradeVersion@<span class="redTxt">*</span></label></td>
									<td>
										<select id="versionName" class="normalSel" style="width: 220px;" onchange="versionNameChange()">
											<option value="-1">@sendConfig.pleaseChoose@</option>
										</select>
									</td>
								</tr>
								<tr id="subTypeTr" class="darkZebraTr"  style="display:none;">
									<td class="rightBlueTxt w280"><label for="subType">@batchUpgrade.imageType@<span class="redTxt">*</span></label></td>
									<td>
										<select id="subType" class="normalSel" style="width: 220px;" onchange="subTypeChange()">
											<option value="-1">@sendConfig.pleaseChoose@</option>
										</select>
									</td>
								</tr>
								<tr id="imageFileTr"   style="display:none;">
									<td class="rightBlueTxt"><label for="imageFile">@batchUpgrade.upgradeImage@<span class="redTxt">*</span></label></td>
									<td><input class="w220 normalInput" type="text" id="imageFile" disabled/></td>
								</tr>
							</table>
					</div>
					<div class="noWidthCenterOuter clearBoth pT50">
				     	<ol class="upChannelListOl pB0 p130 noWidthCenter">
				     		<li><a  onclick="gotoAndStop(0, 1)" id="nextButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@resources/SYSTEM.upStep@</span></a></li>
				     		<li><a  onclick="gotoAndStop(-1600, 3)" id="nextButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i>@resources/SYSTEM.nextStep@</span></a></li>
				        	<li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@resources/COMMON.cancel@</span></a></li>
				    	 </ol>
					</div>
				</div>
				<div id="step3">
					<div id="topPart" class="edge10">
						<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
							<tr>
								<td class="rightBlueTxt w30">@NETWORK.alias@:</td>
								<td width="100">
									<input type="text" id="entityName" name="entityName" class="normalInput w100" />
								</td>
								<td class="rightBlueTxt w50">IP:</td>
								<td width="100">
									<input type="text" id="ip" name="ip" class="normalInput w100" />
								</td>
								<td class="rightBlueTxt w50">MAC:</td>
								<td width="100">
									<input type="text" id="mac" name="mac" class="normalInput w100" />
								</td>
								<td class="rightBlueTxt">@batchTopo.entityType@:</td>
									<td>
									<select id="entityType" class="normalSel w180">
										<option value="0">@sendConfig.pleaseChoose@</option>
									</select>
								</td>
								<td class="edge10">    
									<a href="javascript:;" class="normalBtn" onclick="query()"><span><i class="miniIcoSearch"></i>@resources/COMMON.query@</span></a>                  
								</td>
							</tr>
						</table>
						<div id="entityGrid"></div>
						<div class="noWidthCenterOuter clearBoth pT20">
					     	<ol class="upChannelListOl pB0 p130 noWidthCenter">
					     		<li><a  onclick="gotoAndStop(-800, 2)" id="nextButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@resources/SYSTEM.upStep@</span></a></li>
					        	<li><a  onclick="addJob()" id="saveButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@resources/COMMON.finish@</span></a></li>
					        	<li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@resources/COMMON.cancel@</span></a></li>
					    	 </ol>
						</div>
					</div>
					
					
				</div>
				
			</div>
		</div>
	</body>
</Zeta:HTML>