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
    module epon
</Zeta:Loader>
<style type="text/css">
.x-grid3-cell-first{ width:100px !important;}
.x-grid3-cell-last{ width:100px !important;}
</style>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var portIndex = <s:property value = "portIndex"/>;
var direction = <s:property value = "direction"/>;
//0为一级弹出页面；1为二级弹出页面
var aclPortJspFlag = <s:property value = "aclPortJspFlag"/>;
Ext.Ajax.timeout = 600000;
var errorMes = ${aclListDescr};
errorMes = errorMes.join("") == "false" ? ["Has no error message in action!"] : errorMes;

//端口绑定的ACL列表
var portAclGrid;
var portAclStore;
var portAclData = new Array();
var portAclList = ${aclPortAclListParam};
portAclList = (portAclList.join()=="false") ? new Array() : portAclList;
//acl列表
var aclGrid;
var aclStore;
var aclData = new Array();
var aclListParam = ${aclListParam};
aclListParam = (aclListParam.join()=="false") ? new Array() : aclListParam;
var aclListDescr = ${aclListDescr};
aclListDescr = (aclListDescr.join()=="false") ? new Array() : aclListDescr;
var aclAllListData = new Array();
//全局验证用数据
//冲突的ACLlist，结构：[[],[],[4,5],[7,8],[2],[2],[],[3],[3],.....]共4001项,第一项不使用
var conflict = ${aclConflict};
conflict = (conflict.join()=="false") ? new Array() : conflict;
//防止空指针
if(conflict==null || conflict.length==0){
	conflict = new Array();
	for(var i=0; i<4001; i++){
		conflict[i] = new Array();
	}
}
//该板卡下有多少个已经被绑定到端口上的ACL中的范围匹配的个数
var matchRangeNum = <s:property value = "matchRangeNum"/>;
var aclMatchRangeListStr = '<s:property value = "aclMatchRangeFlag"/>';
//带有范围匹配的rule的号，组合方式：16*(aclId-1)+ruleId ，在列表中表示有，用下划线_隔开，默认传“0”
var aclMatchRangeList = new Array();
//带有范围匹配的ACL号
var aclMatchRangeList2 = new Array();
if(aclMatchRangeListStr != "0"){
	aclMatchRangeList = aclMatchRangeListStr.split("_");
	aclMatchRangeList.splice(0,1);
	var tmpNum;
	for(var i=0; i<aclMatchRangeList.length; i++){
		tmpNum = parseInt(aclMatchRangeList[i] / 16) + 1;
		if(aclMatchRangeList2.indexOf(tmpNum) == -1){
			aclMatchRangeList2.unshift(tmpNum);
		}
	}
}
var allRuleList = ${allAclRuleList};
allRuleList = (allRuleList.join()=="false") ? new Array() : allRuleList;
/************
 * 公共方法**
 **********/
//在conflict中移除某个ACL的冲突关系
function removeConflict(s){
	var tmpL1 = conflict[s].length;
	for(var x=0; x<tmpL1; x++){
		var tmp = conflict[conflict[s][x]];
		var tmpL2 = tmp.length;
		for(var y=0; y<tmpL2; y++){
			if(s == tmp[y]){
				tmp.splice(y, 1);
				y--;
			}
		}
	}
}
//检测两个ACL的优先级是否一致
function checkPriortyIsSame(q,p){
	var tmpL = aclListParam.length;
	var qIndex = -1;
	var pIndex = -1;
	for(var x=0; 4*x<tmpL; x++){
		if(aclListParam[4*x] == q){
			qIndex = 4 * x + 2;
		}else if(aclListParam[4*x] == p){
			pIndex = 4 * x + 2;
		}
	}
	if(qIndex!=-1 && pIndex!=-1 && aclListParam[qIndex]==aclListParam[pIndex]){
		return true;
	}
	return false;
}
//通过mibIndex获得num
function getNum(index, s){
    var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
 		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
 		break;
 	case 3: num = (index & 0xFF00) >> 8;//ONU
 		break;
 	case 4: num = index & 0xFF;//UNI
 		break;
 	default : num = index & 0xFF;//最后一位的num
 		break;
 	}
	return num;
}
//通过index获得location
function getLocationByIndex(index){
	var t = (parseInt(index / 65536) * 256) + (index % 256);
 	var loc = getNum(t, 1) + "/" + getNum(t, 2);
 	if(getNum(t, 3) != 0 && getNum(t, 4) != 0){
 		loc = loc + ":" + getNum(t, 3) + "/" + getNum(t, 4);
	}
	return loc;
}

//查看ACL列表详细信息
function showAclList(){
	if(aclPortJspFlag == 1){
		window.parent.closeWindow('aclList');
	}
	window.parent.createDialog("aclList", I18N.ACL.aclCfgMgmt , 1000, 500, "/epon/acl/showAclList.tv?entityId="+entityId+"&aclPortJspFlag=2",
			 null, true, true);
}

/**********************************
 		 数据操作
 ********************************/
function saveClick(){
	var portLoc = getLocationByIndex(portIndex);
	var tempAclList = new Array();
	for(var i=0; i<portAclData.length; i++){
		tempAclList[i] = portAclData[i][0];
	}
	var portAclListStr = tempAclList.join("q");
	var params = {
		entityId : entityId,
		portIndex : portIndex,
		portAclListStr : portAclListStr,
		direction : direction
	};
	var url = '/epon/acl/modifyAclPort.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.ACL.mdfingBundAcl , portLoc), 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			var tmpL = response.responseText.length;
			var kk = tmpL / 7;
			for(var t=0; t<kk; t++){
				if(response.responseText.substring(7*t,7*t+7) != 'success'){
					if(response.responseText.substring(7*t,7*t+7).split(":")[0] == "no response"){
						return window.parent.showMessageDlg(I18N.COMMON.tip, "cannot connect to device!");
                    }else{
                    	return window.parent.showMessageDlg(I18N.COMMON.tip , String.format(I18N.ACL.mdPortAclEr , portLoc));
                    }
				}
			}
			if(aclPortJspFlag == 1){
				showAclList();
			}
			window.parent.showMessageDlg(I18N.COMMON.tip , String.format(I18N.ACL.mdPortAclOk , portLoc));
			cancelClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip , String.format(I18N.ACL.mdPortAclEr , portLoc));
		},
		params : params
	});
}
//筛选

//检查该ACL中的rule的匹配和action是不是出方向不支持的，返回true表示不支持，false则为支持
function checkedOuterDirectionNoSupport(listIndex){
	var tmpL = allRuleList.length;
	var tmpNumList = new Array();
	var tmpJ = 0;
	for(var a=0; a<tmpL; a++){
		if(allRuleList[a][0][0] == listIndex){
			tmpNumList[tmpJ] = a;
			tmpJ++;
		}
	}
	for(var a=0; a<tmpJ; a++){
		if(allRuleList[tmpNumList[a]][2]!=null && allRuleList[tmpNumList[a]][2].length!=0 && allRuleList[tmpNumList[a]][3].length!=0){
			if(allRuleList[tmpNumList[a]][2].indexOf(3)>-1 || allRuleList[tmpNumList[a]][2].indexOf(6)>-1 || 
					allRuleList[tmpNumList[a]][2].indexOf(7)>-1 || allRuleList[tmpNumList[a]][3].indexOf(4)>-1){
				return true;
			}
		}else if(allRuleList[tmpNumList[a]][3].indexOf(4)>-1){
			return true;
		}
	}
	return false;
}
//检查该ACL中的rule的match是否为空,true表示为空,false表示不为空
function checkedRuleMatchIsNull(listIndex){
	return false;
	//暂时不做判断
	var tmpL = allRuleList.length
	var tmpNumList = new Array()
	var tmpJ = 0
	for(var a=0; a<tmpL; a++){
		if(allRuleList[a][0][0] == listIndex){
			tmpNumList[tmpJ] = a
			tmpJ++
		}
	}
	for(var a=0; a<tmpJ; a++){
		if(allRuleList[tmpNumList[a]][2].length == 0){
			return true
		}
	}
	return false
}


/**************
 * 加载、定义**
 ************/
//端口未绑定的ACL列表
function loadAclData(){
	for(var p=1; p<4001; p++){
		var tmpL = conflict[p].length;
		for(var q=0; q<tmpL; q++){
			if(!checkPriortyIsSame(p, conflict[p][q])){
				var tmpI = conflict[conflict[p][q]].indexOf(p);
				if(tmpI > -1){
					conflict[conflict[p][q]].splice(tmpI, 1);
				}
				conflict[p].splice(q,1);
				q--;
				tmpL--;
			}
		}
	}
	var tempJ = 0;
	for(var i=0; 4*i<aclListParam.length; i++){
		if(aclListParam[4*i + 1] != 0){
			/* 
			//去掉相关检查
			if((direction==1 && checkedOuterDirectionNoSupport(aclListParam[4*i])) || checkedRuleMatchIsNull(aclListParam[4*i])){
				removeConflict(aclListParam[4*i]);
			}*/
			aclAllListData[aclListParam[4*i]] = new Array();
			aclAllListData[aclListParam[4*i]][0] = aclListParam[4*i + 1];
			aclAllListData[aclListParam[4*i]][1] = aclListParam[4*i + 2];
			aclAllListData[aclListParam[4*i]][2] = aclListParam[4*i + 3];
			aclAllListData[aclListParam[4*i]][3] = aclListDescr[i];
			var tempFlag = true;
			if(portAclList.length!=0 && portAclList.indexOf(aclListParam[4*i])>-1){
				tempFlag = false;
			}
			if(tempFlag){
				var tmpLen = conflict[aclListParam[4*i]].length;
				for(var t=0; t<tmpLen; t++){
					if(portAclList.length!=0 && portAclList.indexOf(conflict[aclListParam[4*i]][t])>-1){
						tempFlag = false;
						break;
					}
				}
			}
			if(tempFlag){
				aclData[tempJ] = new Array();
				aclData[tempJ][0] = aclListParam[4*i];
				aclData[tempJ][1] = aclListParam[4*i + 1];
				aclData[tempJ][2] = aclListParam[4*i + 2];
				aclData[tempJ][3] = aclListParam[4*i + 3];
				aclData[tempJ][4] = aclListDescr[i];
				tempJ++;
			}
		}
	}
}
function loadAclGrid(){
	loadAclData();
	aclStore = new Ext.data.SimpleStore({
		data : aclData,
		fields: ['aclIndex','ruleNum','priority','portNum','descr']
	});
	aclGrid = new Ext.grid.GridPanel({
		id : 'aclGrid',
		cls:"normalTable",stripeRows:true,region: "center",
		title:"@COMMON.portUnbundAcl@",
		renderTo : 'aclListGrid',
		store : aclStore,
		width : 245,
		height : 300,
		autoScroll : true,
		viewConfig: {forceFit: true},
		border : true,
	  	selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners: {
				selectionchange : clickAclRow
			}
		}),
		columns: [{header: "ACL ID",dataIndex: 'aclIndex'},
		          {header: "@COMMON.priority@",dataIndex: 'priority'}]
	});
}
function clickAclRow(){
	if(matchRangeNum < 32&&operationDevicePower){
		$("#joinBt").attr("disabled",false);
	}
}
//端口绑定的ACL列表
function loadPortAclGrid(){
	var j=0;
	if(portAclList!=null && portAclList.length!=0){
		for(var t=0; 4*t<aclListParam.length; t++){
			var tempFlag = false;
			if(portAclList.indexOf(aclListParam[4*t])>-1){
				tempFlag = true;
			}
			if(tempFlag){
				portAclData[j] = [aclListParam[4*t],aclListParam[4*t+1],aclListParam[4*t+2],aclListParam[4*t+3],aclListDescr[t]];
				j++;
			}
		}
	}
	portAclStore = new Ext.data.SimpleStore({
		data : portAclData,
		fields : ['aclIndex','ruleNum','priority','portNum','descr']
	});
	portAclGrid = new Ext.grid.GridPanel({
		cls:"normalTable",stripeRows:true,region: "center",
		id : 'portAclGrid',
		renderTo : 'portAclListGrid',
		title:"@ACL.portBundAcl@",
		store : portAclStore,
		width : 245, height :300,
		autoScroll : true,
		viewConfig: {forceFit: true},
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners: {
				selectionchange : clickPortRow
			}
		}),
		border : true,
		columns: [{header: "ACL ID", dataIndex: 'aclIndex'},
		          {header: "@ACL.priority@", dataIndex: 'priority'}]
	});
}
function clickPortRow(){
	if(operationDevicePower){
		$("#outBt").attr("disabled",false);
	}
}

Ext.onReady(function(){
	loadAclGrid();
	loadPortAclGrid();
	var portLoc = getLocationByIndex(portIndex);
	var tempLoc = "";
	for(var i=0; i<portLoc.length; i++){
		tempLoc = tempLoc + portLoc.substring(i,i+1) + "";
	}
	$("#portName").html("<font  class='rightBlueTxt'>@COMMON.port@</font>:"+ tempLoc);
	if(direction==0){
		$("#portDirection").html( "@ACL.indirect@" );
	}else if(direction==1){
		$("#portDirection").html( "@ACL.outdirect@" );
	}
	if(aclPortJspFlag == 0){
		$("#showAclListBt").show();
	}else if(aclPortJspFlag == 1){
		$("#showAclListBt").hide();
	}
	/* $("#errorMesBt").click(showErrorMes); */
	window.onerror = function(){
		arglen = arguments.length;
	    var errorMsg = "arguments.length:" + arglen;
	    for(var i=0; i<arglen; i++){
	    	errorMsg += "<br>arguments[" + i + "]: " + arguments[i];
    	}
    	if(errorMes.indexOf(errorMsg) == -1){
	    	errorMes.push(errorMsg);
	    }
		return true;
	}
	
	//操作权限控制
	if(!operationDevicePower){
	    R.saveBt.setDisabled(true);
	}
});	//end of Ext.onReady

function joinClick(){
	if(!aclGrid.getSelectionModel().getSelected()){
		return;
	}
	var aclTempId = aclGrid.getSelectionModel().getSelected().get('aclIndex');
	var tempIndex = aclGrid.getStore().indexOf(aclGrid.getSelectionModel().getSelected());
	if(tempIndex == -1){
		return;
	}
	//加入绑定的列表
	portAclData[portAclData.length] = aclData[tempIndex];
	//从未绑定的列表中移出
	aclData.splice(tempIndex, 1);
	//如果有冲突关系
	if(conflict[aclTempId].length > 0){
		var tmpList = new Array();
		//找出冲突关系的data
		for(var q=0; q<conflict[aclTempId].length; q++){
			for(var k=0; k<aclData.length; k++){
				if(aclData[k][0] == conflict[aclTempId][q]){
					tmpList.push(k);
				}
			}
		}
		//从未绑定的列表中移出其冲突的acl
		var tmpLen = tmpList.length;
		if(tmpLen>0){
			$("#conflictText").show();
			for(var f=0; f<tmpLen; f++){
				aclData.splice(tmpList[f] - f, 1);
			}
		}
	}
	//排序后load
	portAclData.sort(function(a,b){
		return a[0] - b[0];
	})
	aclStore.loadData(aclData);
	portAclStore.loadData(portAclData);
	$("#joinBt").attr("disabled",true);
	$("#joinBt").mouseout();
	$("#outBt").attr("disabled",true);
	//$("#saveBt").attr("disabled",false);
	if(aclMatchRangeList2.indexOf(aclTempId) > -1){
		matchRangeNum++;
	}
	if(matchRangeNum > 31){
		$("#rangeText").text(I18N.ACL.ponBundAcl).show();
	}
}
function outClick(){
	if(!portAclGrid.getSelectionModel().getSelected()){
		return;
	}
	var portAclTempId = portAclGrid.getSelectionModel().getSelected().get('aclIndex');
	var tempIndex = -1;
	for(var i=0; i<portAclData.length; i++){
		if(portAclData[i][0] == portAclTempId){
			tempIndex = i;
			break;
		}
	}
	if(tempIndex == -1){
		return;
	}
	aclData[aclData.length] = portAclData[tempIndex];
	portAclData.splice(tempIndex, 1);
	//检查是否移出的acl有冲突关系
	var tmpLength = conflict[portAclTempId].length
	if(tmpLength > 0){
		//如果有冲突关系的，在未绑定列表中同时加入其冲突关系的ACL的信息
		for(var w=0; w<tmpLength; w++){
			for(var t=0; 4*t<aclListParam.length; t++){
				if(aclListParam[4*t] == conflict[portAclTempId][w]){
					var tmpFlag = true;
					for(var r=0; r<portAclData.length; r++){
						if(conflict[aclListParam[4*t]].indexOf(portAclData[r][0]) > -1){
							tmpFlag = false;
							break;
						}
					}
					if(tmpFlag){
						aclData.unshift([aclListParam[4*t], aclListParam[4*t+1], aclListParam[4*t+2], aclListParam[4*t+3], aclListDescr[t]]);
					}
				}
			}
		}
	}
	aclData.sort(function(a,b){
		return a[0] - b[0];
	});
	aclStore.loadData(aclData);
	portAclStore.loadData(portAclData);
	$("#outBt").attr("disabled",true);
	$("#outBt").mouseout();
	$("#joinBt").attr("disabled",true);
	//$("#saveBt").attr("disabled",false);
	if(aclMatchRangeList2.indexOf(portAclTempId) > -1){
		matchRangeNum--;
	}
	if(matchRangeNum < 32){
		rangeTextClick();
	}
}
function conflictTextClick(){
	$("#conflictText").hide();
}
function rangeTextClick(){
	$("#rangeText").hide();
}
function showErrorMes(){
	window.parent.showMessageDlg(I18N.COMMON.tip , errorMes.join("<br><br>"));
}

function cancelClick(){
	window.parent.closeWindow('aclPort');
}
</script>
</head>
<body class=openWinBody>
	<div>
		<table>
				<tr>
					<td id="portName" class="w100 txtCenter"></td>
					<td  class="rightBlueTxt">@ACL.direct@:</td>
					<td id="portDirection"></td>
					<td style="text-align: center;" class="w200">
						<button id=showAclListBt class=BUTTON95 style="dispaly:none;" onclick="showAclList()">@ACL.showAclList@</button>
					</td>
				</tr>
		</table>
	</div>

	<table width=100% cellspacing=0 cellpadding=0>
		<tr><td><div id=portAclListGrid></div></td>
		<td>
			<table>
				<tr style="height:35px;" valign="top"><td>
					<font id=conflictText style="display:none;color:blue;margin-left:5px;" 
						onclick="conflictTextClick()">@ACL.conflictTextContent@</font>
					<font id=rangeText style="display:none;color:blue;margin-left:5px;" onclick="rangeTextClick()">@ACL.rangeTextContent@</font>
				</td></tr>
				<tr style="height:25px;" valign="bottom"><td style="width:85px;" align=center>
					<button id=joinBt class=BUTTON75 disabled onclick="joinClick()"><< @ACL.moveIn@</button>
				</td></tr>
				<tr valign="top"><td style="width:85px;" align=center>
					<button id=outBt class=BUTTON75 disabled style="margin-top:10px;" onclick="outClick()">@ACL.moveOut@ >></button>
				</td></tr>
			</table>
		</td>
		<td>
			<table>
				<tr><td>
					<div id=aclListGrid></div>
				</td></tr>
			</table>
		</td></tr>
	</table>
	<!-- <button id=errorMesBt style='position:absolute;top:380;left:15;width:20px;filter:alpha(opacity=0);'></button> -->
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>