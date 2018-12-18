<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<style type="text/css">
.fileElement{position:absolute;filter:alpha(opacity=0);width:75px;height:22px;cursor:hand;left:355;}
#hwSel,#fileSel{display:none;position:absolute;width:200;z-index:100;border:1px solid #cccccc;background-color:white;overflow-y:auto;'}
.SUCCESS {color:green;font-weight:bold;}
.FAILUER {color:red;font-weight:bold;}
.UPDATING {color: orange;font-weight:bold;}

</style>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE EPON
</Zeta:Loader>
<script type="text/javascript">
var entityId = ${entityId};
var slotNoFirst = ${slotNo};
var profileIdFirst = ${profileId};
var proList = ${profileIdList};
if(proList.join("") == "false"){
	proList = new Array();
}
var slotNoList = ${ponOnuProfileList};
if(slotNoList.join("") == "false"){
	slotNoList = new Array();
}

//[[recordId, profileId, slotNo, statTime, onuStat<int>, 0], [], []...]
var recordData = new Array();
var recordStore;
var recordGrid;

function loadRecordData(data){
	recordData = new Array();
	if(data){
		for(var a=0,n=data.length; a<n; a++){
			var da = data[a];
			recordData.push([da.recordId, da.profileId, da.slotNo, da.upgRecordStartTime, da.upgRecordOnuStatList,da.upgRecordOnuList, 0]);
		}
	}
	recordStore.loadData(recordData);
	setTimeout(function(){
		selChange();
	}, 700);
}
function loadRecordGrid(){
	var cm = [{header: "@onuAutoUpg.he.recordId@",  dataIndex: 'recordId',  width:55},
	         {header: "@onuAutoUpg.he.proId@",  dataIndex: 'profileId',  width:55},
	         {header: "@onuAutoUpg.he.ponSlot@", dataIndex: 'slotNo',  width:55},
	         {header: "@onuAutoUpg.he.startTime@",  dataIndex: 'startTime',  width:140},
	         {header: "@onuAutoUpg.he.upgStat@", dataIndex: 'onuStat', width:290, renderer: onuStatRender},
	         {header: "@COMMON.manu@", dataIndex: 'tmp', hidden:true, width:60, renderer: cancelHandler}];
	recordStore = new Ext.data.SimpleStore({
		data : recordData,
		fields: ['recordId', 'profileId', 'slotNo', 'startTime', 'onuStat','onuList','tmp']
	});
	recordGrid = new Ext.grid.GridPanel({
		stripeRows:true,cls:"normalTable edge10",bodyCssClass: 'normalTable',
		id : 'RecordGrid',
		title : I18N.onuAutoUpg.tit.upgingOnuPro,
		renderTo : 'recordGridDiv',
		border : true,
		frame : false,
		autoScroll : true,
		viewConfig:{ forceFit: true },
		width : 580,
		height : 340,
		store : recordStore,
		columns : cm,
		selModel : new Ext.grid.RowSelectionModel({singleSelect : true}),
		listeners : {
			'viewready' : function(){
				initProSel();
				initSlotSel();
				//refreshClick();
			}
		}
	});
}
function onuStatRender(states, c, record){
	//只显示成功个数,失败个数,正在升级中个数
	var sc = states.count(3),
		fc = states.count(1,4),
		ic = states.count(2);
	var result = "@onuAutoUpg.mes.success@<font class='SUCCESS'>{0}</font>@COMMON.ge@  @onuAutoUpg.mes.fail@<font class='FAILUER'>{1}</font>@COMMON.ge@  @onuAutoUpg.mes.updating@<font class='UPDATING'>{2}</font>@COMMON.ge@";
	return String.format(result,sc,fc,ic);
}
function cancelHandler(v, c, record){
	if(operationDevicePower){
		return "<img src='/images/delete.gif' style='margin-left:2;' onmouseover='cancelMouse(this, 1)' " +
			"onmouseout='cancelMouse(this, 2)' onclick='cancelHand()' title='" + I18N.onuAutoUpg.tit.cancelRecord + "'>";
	}else{
		return "<img src='/images/deleteDisable.gif' style='margin-left:2;' title='" + I18N.onuAutoUpg.tit.cancelRecord + "'>";
	}
}
function cancelMouse(el, s){
	$(el).css("margin-left", s);
}
function cancelHand(){
	var data = recordGrid.getSelectionModel().getSelected().data;
	window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.cancelingUpg);
	$.ajax({
		url : '/onu/cancelOnuAutoUpg.tv',
		data : 'entityId=' + entityId + '&slotNo=' + data.slotNo + '&recordId=' + data.recordId,
		success : function(response) {
			//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.cancelUpgSuc);
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@onuAutoUpg.mes.modifyProSuc@</b>'
       	    });
			setTimeout(function(){
				refreshClick();
				setTimeout(function(){
					refreshClick();
				}, 3000);
			}, 3000);
		},error : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.cancelUpgFailed);
		}
	});
}

Ext.onReady( loadRecordGrid );
function initProSel(){
	var p = $("#proSel");
	var le = proList.length;
	if(le < 1){
		p.empty();
	}
	if(le > 0){
		for(var a=0; a<le; a++){
			p.append(String.format("<option value={0}>{0}</option>", proList[a]));
		}
		if(profileIdFirst > 0){
			p.val(profileIdFirst);
		}
	}else{
		p.append("<option value=-7>" + I18N.onuAutoUpg.tit.noPro + "</option>");
		$("#wrongTip").show();
	}
}
function initSlotSel(){
	var p = $("#slotSel");
	var le = slotNoList.length;
	if(le < 2){
		p.empty();
	}
	if(le > 0){
		for(var a=0; a<le; a++){
			p.append(String.format("<option value={0}>{0}</option>", slotNoList[a]));
		}
		if(slotNoFirst > 0){
			p.val(slotNoFirst);
		}
	}else{
		p.append("<option value=-7>" + I18N.onuAutoUpg.tit.noSlot + "</option>");
		$("#wrongTip").show();
	}
}
function selChange(){
	var pro = parseInt($("#proSel").val());
	var slo = parseInt($("#slotSel").val());
	recordStore.filterBy(function(record){
		//EMS-15179 record.data.slotNo=0时说明只有一块板卡，这时不用过滤record.data.slotNo == 0 ? true : slo == record.data.slotNo
		return (pro == 0 || pro == record.data.profileId) && (slo == 0 || record.data.slotNo == 0 ? true : slo == record.data.slotNo);
	});
}
function refreshClick(){
	window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching);
	$.ajax({
		url : '/onu/refreshOnuAutoUpging.tv?entityId=' + entityId,cache:false,
		success : function(json) {
			//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			var data = json;
			loadRecordData(data);
			selChange();
		},error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
		}
	});
}
function cancelClick(){
	window.parent.closeWindow('onuAutoUpgRecord');
}
</script>
</head>
<body class=openWinBody>
	<div class="edge10">
		<table>
			<tr>
				<td class="rightBlueTxt w50">@onuAutoUpg.he.ponSlot@:</td>
				<td><select id="slotSel" onchange='selChange()' class="normalSel w60">
					<option value=0>@COMMON.all@</option>
				</select></td>
				<td class="rightBlueTxt w80">@onuAutoUpg.he.proId@:</td>
				<td><select id="proSel" onchange='selChange()' class="normalSel w60">
						<option value="0">@COMMON.all@</option>
				</select></td>
				<td><span id="wrongTip" style='display:none;color:red;width:auto;'>@onuAutoUpg.pro.noSomething@</span></td>
			</tr>
		</table>
		<div id=recordGridDiv></div>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>