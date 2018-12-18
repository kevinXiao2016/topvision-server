<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Ext
    library Zeta
    library Jquery
    module  epon
    import js.json2
    import js.tools.ipText
</Zeta:Loader>
<head>
<style type="text/css">
.matchInputBotClass {
	position: absolute;
	left: 212px;
	top: 89px;
	align: 'left';
	z-index : 1;
	}
.matchInputTopClass {
	position: absolute;
	left: 212px;
	top: 45px;
	align: 'left';
	z-index : 1;
	}
</style>

<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var aclIndex = '<s:property value="aclIndex"/>' ? '<s:property value="aclIndex"/>' : 0;
var aclRuleIndex = '<s:property value="aclRuleIndex"/>';
var slotSni = ${slotSniObject};
slotSni = slotSni.join("")=="false" ? new Array() : slotSni;
var modifyFlag = '<s:property value="modifyFlag"/>';//1为修改；0为新增
var aclRuleList = ${aclRuleListParam};
aclRuleList = aclRuleList.join("")=="false" ? new Array() : aclRuleList;
var matchJspList = new Array();//界面规则互斥用的数组
var matchRangeFlag = 0;
//action
var actionList = new Array();//页面action列表
var actionStr = [I18N.ACL.allowDenyMode , I18N.ACL.allowDeny , I18N.ACL.indirectRateSet , I18N.ACL.mirrorSet , I18N.ACL.queneSet ,
				 I18N.ACL.innerCosSet, I18N.ACL.dscpSet , I18N.ACL.tosSet , I18N.ACL.outerVlanSet , I18N.ACL.outerCosSet , I18N.ACL.tpidSet ]
//action param
var actionParamList = [0,0,0,0,0,0,0,0,0,0,0,0];//页面action参数列表
//match
var matchList = new Array();//页面match列表
var matchStr = [I18N.ACL.originMac , I18N.ACL.targetMac , I18N.ACL.outerVlan , I18N.ACL.innerVlan , I18N.ACL.outerPri , I18N.ACL.innerPri ,	I18N.ACL.outerTpid , I18N.ACL.innerTpid , I18N.ACL.etherType , I18N.ACL.originIp , I18N.ACL.targetIp , 
                I18N.ACL.ipMessageType, I18N.ACL.ipProType,	'DSCP','TOS', I18N.ACL.originPort, I18N.ACL.targetPort]
var matchGrid
var matchStore
var matchData = new Array()
//match param
var matchParamList = new Array();//页面match参数列表
var matchParamStr = [[I18N.ACL.originMac  + ':', I18N.ACL.originMac  + ' mask:'],[I18N.ACL.targetMac + ' ', 
	I18N.ACL.targetMac + ' mask:'],[I18N.ACL.outerVlan + ':','  -  '],[ I18N.ACL.innerVlan + ':'],
	[I18N.ACL.outerVlanPrio + ' '],[I18N.ACL.innerVlanPrio + ' '],[I18N.ACL.outerVlan + '  TPID:'],
	[I18N.ACL.innerVlan + ' TPID:'],[I18N.ACL.etherType + ' :'], [I18N.ACL.originIp + ':',I18N.ACL.originIp + ' mask:'],
	[I18N.ACL.targetIp + ':', I18N.ACL.targetIp + '  mask:'],[I18N.ACL.ipMessageFormate + ' '],[I18N.ACL.ipMessageType + ':'],
	[' DSCP:'],[' TOS:'],[I18N.ACL.originPort + ':','  -  '],[I18N.ACL.targetPort + ':','  -  ']]

/************
 * 公用方法 *
 **********/
function changeMacToMaohao(mac){
	if(mac.length == 12){
		var newMac = mac.substring(0,2);
		for(var u=1; u<6; u++){
			newMac += ":" + mac.substring((2*u),(2*u+2));
		}
		mac = newMac;
	}else if(mac.length == 14){
		mac = mac.substring(0,2)+":"+mac.substring(2,7)+":"+mac.substring(7,12)+":"+mac.substring(12,14);
	}
	return mac.replace(/([/\s-.])/g,":").toLocaleUpperCase();
}

/************
 * 加载数据**
 **********/

//match
function loadMatchData(){
	matchData = new Array();
	for(var x=0; x<matchList.length; x++){
		matchData[x] = new Array();
		matchData[x][0] = matchStr[matchList[x]];
		matchData[x][1] = matchParamStr[matchList[x]][0] + matchParamList[matchList[x]][0];
		if(matchParamList[matchList[x]][1]){
			matchData[x][1] = matchData[x][1] + matchParamStr[matchList[x]][1] + matchParamList[matchList[x]][1];
			if(matchParamList[matchList[x]][1] != matchParamList[matchList[x]][0]){
				matchRangeFlag++;
			}
		}
		matchData[x][2] = matchList[x];
	}
}
function loadMatchGrid(){
	matchStore = new Ext.data.SimpleStore({
		data : matchData,
		fields: ['match','matchParam','matchId']
	});
	matchGrid = new Ext.grid.GridPanel({
		id : 'matchGrid',
		renderTo : 'matchGridDiv',
		store : matchStore,
		height : 120,
		frame : false,
		autoScroll : true,
		border : true,
		viewConfig : {
			forceFit:true
		},
		cls: 'normalTable',
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header:  I18N.ACL.matchManner,
				dataIndex: 'match',
				align: 'center',
				width: 100
			},{
				header: I18N.ACL.matchParam,
				dataIndex: 'matchParam',
				align: 'center',
				width: 350
			},{
				header: I18N.COMMON.manu ,
				dataIndex: 'id1',
				align: 'center',
				width: 70,
				renderer : function(value, cellmeta, record) {
					if(modifyFlag == 9){
						//已绑定到端口,只能查看
						return String.format("<a href='javaScript:;' onclick='viewAndEdit({0})'>@COMMON.view@</a>",record.data.matchId)
					}else{
						return String.format("<a href='javaScript:;' onclick='viewAndEdit({0})'>@COMMON.edit@</a> / <a href='javaScript:;' onclick='deleteMatch({0})'>@COMMON.delete@</a>",record.data.matchId)
					}
					//return "<img src='/images/delete.gif' onclick='deleteMatch(\"" + record.data.matchId + "\")'/>";
				}
			}]
		/* listeners: {
			'click' : function(){
				var sel = matchGrid.getSelectionModel().getSelected();
				if(sel != null && sel != 'undefined'){
					var tmpNum = sel.get('matchId');
					matchMoveOn(tmpNum);
				}
			}
		} */
	});
}

function viewAndEdit(matchId){
	matchMoveOn(matchId);
}

function deleteMatch(s){
	if(modifyFlag == 9){
		return;
	}
	var tempNum = -1;
	for(var x=0; x<matchData.length; x++){
		if(matchData[x][2] == s){
			tempNum = x;
		}
	}
	if(tempNum > -1){
		matchData.splice(tempNum, 1);
		matchStore.loadData(matchData);
		$("#match"+s).css("color","blue");
		if(s == 2 || s == 15 || s == 16){
			matchRangeFlag--;
		}
	}
}

function initAllData(){
	var ruleIndexTmpFlagNum = -1
	if(aclRuleIndex == "" || aclRuleIndex == null || aclRuleIndex == undefined){
		aclRuleIndex = 0
	}
	var tempRuleIdList = new Array()
	matchJspList = new Array()
	for(var i=0; i<aclRuleList.length; i++){
		tempRuleIdList[i] = aclRuleList[i].topAclRuleIndex
		if(tempRuleIdList[i] == aclRuleIndex){
			ruleIndexTmpFlagNum = i
		}else{
			//TODO 验证用的数组
			matchJspList[i] = new Array()
			matchJspList[i][0] = new Array();//match
			matchJspList[i][1] = new Array();//action
		}
	}
	var position = $("#ruleId")
	for(var j=1; j<17; j++){
		var tempAddFlag = true
		for(var k=0; k<tempRuleIdList.length; k++){
			if(tempRuleIdList[k] == j){
				tempAddFlag = false
			}
		}
		if(tempAddFlag || modifyFlag==1 || modifyFlag==9){
			position.append(String.format("<option value={0}>{0}</option>", j))
		}
	}
	matchRangeFlag = 0
	if(modifyFlag == 1 || modifyFlag == 9){
		$("#ruleId").attr("disabled",true)
		$("#ruleId").val(aclRuleIndex)
		//修改的初始化
		if(ruleIndexTmpFlagNum != -1){
			var k = ruleIndexTmpFlagNum;
			if(aclRuleList[k].topMatchedFieldSelectionSymbol!=null && 
					aclRuleList[k].topMatchedFieldSelectionSymbol.length!=0){
				matchList = aclRuleList[k].topMatchedFieldSelectionSymbol
				actionList = aclRuleList[k].topAclActionList
				actionParamList = aclRuleList[k].topAclActionParameterValueList
				if(matchList.indexOf(2) > -1 && matchParamList[2] != null && matchParamList[2] != undefined){
					if(matchParamList[2][0] != matchParamList[2][1]){
						matchRangeFlag++
					}
				}
				if(matchList.indexOf(15) > -1 && matchParamList[15] != null && matchParamList[15] != undefined){
					if(matchParamList[15][0] != matchParamList[15][1]){
						matchRangeFlag++
					}
				}
				if(matchList.indexOf(16) > -1 && matchParamList[16] != null && matchParamList[16] != undefined){
					if(matchParamList[16][0] != matchParamList[16][1]){
						matchRangeFlag++
					}
				}
			}
			for(var x=0; x<17; x++){
				matchParamList[x] = new Array()
			}
			for(var m=0; m<matchList.length; m++){
				$("#match"+matchList[m]).css("color","green")
				switch(matchList[m]){
					case 0 : matchParamList[0][0] = aclRuleList[k].topMatchedSrcMac;
						matchParamList[0][1] = aclRuleList[k].topMatchedSrcMacMask
						$("#matchParamInput0").val(matchParamList[0][0])
						$("#matchParamInput09").val(matchParamList[0][1])
						break
					case 1 : matchParamList[1][0] = aclRuleList[k].topMatchedDstMac
						matchParamList[1][1] = aclRuleList[k].topMatchedDstMacMask
						$("#matchParamInput1").val(matchParamList[1][0])
						$("#matchParamInput19").val(matchParamList[1][1])
						break
					case 2 : matchParamList[2][0] = aclRuleList[k].topMatchedStartSVid
						matchParamList[2][1] = aclRuleList[k].topMatchedEndSVid
						$("#matchParamInput2").val(matchParamList[2][0])
						$("#matchParamInput29").val(matchParamList[2][1])
						break
					case 3 : matchParamList[3][0] = aclRuleList[k].topMatchedStartCVid;
						//matchParamList[3][1] = aclRuleList[k].topMatchedEndCVid;
						$("#matchParamInput3").val(matchParamList[3][0]);
						//$("#matchParamInput39").val(matchParamList[3][1]);
						break;
					case 4 : matchParamList[4][0] = aclRuleList[k].topMatchedOuterCos;
						$("#matchParamInput4").val(matchParamList[4][0]);
						break;
					case 5 : matchParamList[5][0] = aclRuleList[k].topMatchedInnerCos;
						$("#matchParamInput5").val(matchParamList[5][0]);
						break;
					case 6 : matchParamList[6][0] = "0x"+aclRuleList[k].topMatchedOuterTpid.toString(16);
						$("#matchParamInput6").val(matchParamList[6][0].toString(16));
						break;
					case 7 : matchParamList[7][0] = "0x"+aclRuleList[k].topMatchedInnerTpid.toString(16);
						$("#matchParamInput7").val(matchParamList[7][0].toString(16));
						break;
					case 8 : matchParamList[8][0] = aclRuleList[k].topMatchedEthernetType;
						$("#matchParamInput8").val(matchParamList[8][0]);
						break;
					case 9 : matchParamList[9][0] = aclRuleList[k].topMatchedSrcIP;
						matchParamList[9][1] = aclRuleList[k].topMatchedSrcIPMask;
						setIpValue("matchParamInput9", matchParamList[9][0]);
						setIpValue("matchParamInput99", matchParamList[9][1]);
						break;
					case 10 : matchParamList[10][0] = aclRuleList[k].topMatchedDstIP;
						matchParamList[10][1] = aclRuleList[k].topMatchedDstIPMask;
						setIpValue("matchParamInput10", matchParamList[10][0]);
						setIpValue("matchParamInput109", matchParamList[10][1]);
						break;
					case 11 : matchParamList[11][0] = aclRuleList[k].topMatchedL3ProtocolClass;
						$("#matchParamInput11").val(matchParamList[11][0]);
						break;
					case 12 : matchParamList[12][0] = aclRuleList[k].topMatchedIpProtocol;
						$("#matchParamInput12").val(matchParamList[12][0]);
						break;
					case 13 : matchParamList[13][0] = aclRuleList[k].topMatchedDscp;
						$("#matchParamInput13").val(matchParamList[13][0]);
						break;
					case 14 : matchParamList[14][0] = aclRuleList[k].topMatchedTos;
						$("#matchParamInput14").val(matchParamList[14][0]);
						break;
					case 15 : matchParamList[15][0] = aclRuleList[k].topMatchedStartSrcPort;
						matchParamList[15][1] = aclRuleList[k].topMatchedEndSrcPort;
						$("#matchParamInput15").val(matchParamList[15][0]);
						$("#matchParamInput159").val(matchParamList[15][1]);
						break;
					case 16 : matchParamList[16][0] = aclRuleList[k].topMatchedStartDstPort;
						matchParamList[16][1] = aclRuleList[k].topMatchedEndDstPort;
						$("#matchParamInput16").val(matchParamList[16][0]);
						$("#matchParamInput169").val(matchParamList[16][1]);
						break;
				}
			}
			var temp = [2,2,2,1,1,1,1,1,1,2,2,1,1,1,1,2,2];
			for(var m=0; m<matchList.length; m++){
				var a = matchList[m];
				var b = temp[a];//参数个数
				if(b == 1){
					if(a==8 || a==12){
						var textArray = [];
						$("#matchParamInput"+a + " option").each(function(){
							textArray.push($(this).text().trim());
						})
						var index = $("#matchParamInput"+a).val()-1;
						matchParamList[a][0] = textArray[index];
					}else if(a==11){
						var index = $("#matchParamInput"+a).val();
						$("#matchParamInput"+a + " option").each(function(){
							if(this.value == index){
								matchParamList[a][0] = $(this).text().trim();
							}
						})
					}
					matchData.unshift([matchStr[a], matchParamStr[a][0]+matchParamList[a][0], a]);
				}else if(b == 2){
					if(a==9||a==10){
						matchParamList[a][0] = getIpValue("matchParamInput"+a);
						matchParamList[a][1] = getIpValue("matchParamInput"+a+"9");
					}else {
						matchParamList[a][1] = $("#matchParamInput"+a+"9").val();
						if(a==0 || a==1){
							matchParamList[a][1] = changeMacToMaohao(matchParamList[a][1]);
						}
					}
					matchData.unshift([matchStr[a], matchParamStr[a][0]+matchParamList[a][0]+matchParamStr[a][1]+matchParamList[a][1], a]);
				}
			}
			matchStore.loadData(matchData);
			//初始化action部分表单数据
			actionList = aclRuleList[k].topAclActionList;
			if(actionList!=null && actionList.length!=0){
				//actionParam不论拒绝还是允许模式，均可带值到页面显示
				var tmpParList = aclRuleList[k].topAclActionParameterValueList;
			    actionParamList[2] = tmpParList[0];
				for(var t=5; t<11; t++){
					actionParamList[t-1] = tmpParList[t];
				}
				for(var i=0; i<12; i++){
					if(i == 1){continue;}
					$("#actionCheck"+i).attr("disabled",false);
				}
				for(var y=0; y<actionList.length; y++){
					var ay = actionList[y];
					if(ay == 0 || ay == 1){
						$("#actionCheck0").attr("checked", "checked");
						$("#action0").val(ay)
					}
					$("#actionCheck"+ay).attr("checked","checked");
					$("#action"+ay).val(actionParamList[ay]);
					if(ay == 8){
						$("#action81").attr("disabled", false);
						$("#action81").val(tmpParList[12]);
					}
				}
				for(var i=0; i<12; i++){
					if(i == 1){continue;}
					if($("#actionCheck"+i).attr("checked")){
						$("#action"+i).attr("disabled",false);
						//在初始化数据时控制select和input的样式
						if($("#action"+i).length > 0){
							if($("#action"+i).get(0).tagName == "INPUT"){
								$("#action"+i).removeClass("normalInputDisabled");
							}else if($("#action"+i).get(0).tagName == "SELECT"){
								$("#action"+i).removeClass("normalSelDisabled");
							}
						}
						if(i==2 || i==8){
							$("#action"+i).focus();
							if(i == 8){
								$("#action81").removeClass("normalSelDisabled");
							}
						}
					}else{
						$("#action"+i).attr("disabled",true);
					}
				}
				if($("#actionCheck10").attr("checked")){
					$("#action10").attr("disabled",false);
					$("#action99").attr("disabled",false);
				}else{
					$("#action10").attr("disabled",true);
					$("#action99").attr("disabled",true);
				}
			}
		}
	}
}

/************
 * 界面逻辑**
 **********/
function initAllDiv(){
	var i;
	//match
	createMatch();
	
	//matchParam
	createMatchParam(1);
	createMatchParam(2);
	//DSCP下拉框
	var position = $("#matchParamInput13");
	for(i=0; i<64; i++){
		position.append(String.format("<option value={0}>{0}</option>", i));
	}
	//TOS下拉框
	var position1 = $("#matchParamInput14");
	for(i=0; i<8; i++){
		position1.append(String.format("<option value={0}>{0}</option>", i));
	}
	for(i=0; i<matchList.length; i++){
		$("#match"+matchList[i]).css("color","green");
	}

	//action
	loadSniData();
	var position2 = $("#action6");
	for(i=0; i<64; i++){
		position2.append(String.format("<option value={0}>{0}</option>", i));
	}
	var position3 = $("#action7");
	for(i=0; i<8; i++){
		position3.append(String.format("<option value={0}>{0}</option>", i));
	}
	//$(".actionClass").attr("disabled",true);
	
}
function loadSniData(){
	var position = $('#action3');
    for (var i = 0; i < slotSni.length; i++) {
    	position.append(String.format("<option value='{0}/{1}'>{0}/{1}</option>", slotSni[i].slotNo, slotSni[i].sniNo));
    }
}
function createMatch(){
	var matchLocX = [ 100, 180, 265, 360, 455, 455, 535, 620, 50, 140, 185, 240, 325, 400, 450, 495, 550];
	for(var i=0; i<matchLocX.length;i++){
		matchLocX[i] = matchLocX[i] + 140
	}
	var matchLocW = [ 75, 80, 90, 90, 90, 75, 80, 80, 85, 40, 50, 80, 70, 45, 40, 50, 60];
	var matchLocY = [ 50, 50, 50, 50, 50, 50, 50, 50, 80, 80, 80, 80, 80, 80, 80, 80, 80];
	var matchLocH = 18;
	for(var x=0; x<17; x++){
		if(x > 4 && x < 8){//5-7不支持:内层COS、内层TPID、外层TPID
			continue;
		}
		$match = $("<div>");
		$match.attr({
			id : 'match' + x,
			title : matchStr[x]
		});
		$match.addClass(x < 8 ? 'matchTopClass' : 'matchBotClass');
		$match.css({
			position : 'absolute',
			top : matchLocY[x],
			left : matchLocX[x],
			width : matchLocW[x],
			height : matchLocH,
			color : '#B3711A',
			textAlign : 'center',
			border : '1px solid #ccc'
		});
		$match.text(matchStr[x]);
		$match.bind('click',function(e){
			matchMoveOn(this.id.substring(5));
		});
		$match.bind('mouseover',function(e){
			$("#"+this.id).css("cursor","pointer");
			$("#"+this.id).css("border","1px solid #666");
		});
		$match.bind('mouseout',function(e){
			$("#"+this.id).css("cursor","default");
			$("#"+this.id).css("border","1px solid #ccc");
		});
		$("#matchDiv").append($match);
	}
}
function createMatchParam(s){
	var heightTmp = 0;
	if(s == 1){//点击第二排，在上面显示
		heightTmp = -25
	}else if(s == 2){//点击第一排，在下面显示
		heightTmp = 15
	}
	$matchParam = $("<div>")
	$matchParam.attr({
		id : 'matchParam' + s,
		title : I18N.ACL.matchParam
	})
	$matchParam.css({
		position : 'absolute',
		display : 'none',
		top : 65 + heightTmp,
		left : 190,
		width : 560,
		height : 35,
		textAlign : 'center',
		border : '1px solid #666',
		background: '#f8f8f8'
	})
	$("#matchDiv").append($matchParam)
}
function matchMoveOn(s){
	$("#matchParam1").hide();
	$("#matchParam2").hide();
	for(var i=0; i<17; i++){
		$("#matchInput"+i).hide();
		$("#match"+i).css("backgroundColor","white");
	}
	if(s<8){
		$(".matchBotClass").hide();
		$(".matchTopClass").show();
		$("#matchParam2").show();
	}else{
		$(".matchTopClass").hide();
		$(".matchBotClass").show();
		$("#matchParam1").show();
	}
	$("#matchInput"+s).show();
	$("#match"+s).css("backgroundColor","#eee");
	//IE输入焦点失去
	if(s == 9 || s == 10){
		ipFocus("matchParamInput"+s, 1);
	}else{
		$("#matchParamInput"+s).focus();
	}
}
function matchParamOk(s,a,b){//s:2上1下; a:index; b:参数个数
	if(modifyFlag == 9){
		window.parent.showMessageDlg("@COMMON.tip@", "@ACL.ruleToPortExist@", null, function(){
			matchParamCancel(s,a);
		});
		return;
	}
	if(!okChecked(a)){
		/* if($("#matchParamInput"+a+"9").length > 0){
			$("#matchParamInput"+a+"9").focus();
		}else{
			$("#matchParamInput"+a).focus();
		} */
		return;
	}
	if(a == 2 || a == 15 || a == 16){
		if(matchRangeFlag > 0 && $("#matchParamInput"+a).val()!=$("#matchParamInput"+a+"9").val()){
			var tmp = new Array();
	        for(var k=0; k<matchData.length; k++){
				if(matchData[k][2]==2 && ($("#matchParamInput2").val()!=$("#matchParamInput29").val() || !$("#matchParamInput29").val())){
					tmp.unshift(2);
				}else if(matchData[k][2]==15 && ($("#matchParamInput15").val()!=$("#matchParamInput159").val() || !$("#matchParamInput159").val())){
					tmp.unshift(15);
				}else if(matchData[k][2]==16 && ($("#matchParamInput16").val()!=$("#matchParamInput169").val() || !$("#matchParamInput169").val())){
					tmp.unshift(16);
				}
		    }
		    if(tmp.length == 1 && tmp.indexOf(a) != -1){
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.matchRangeTip, null, function(){
					matchParamCancel(s,a);
				});
				return;
				/* 
				window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.ACL.matchRangeTip , function(type) {
			        if (type == 'no') {
			        	deleteMatch(a);
			        	matchRangeFlag++;
			            return;
			        }
			        if(a != 2 && tmp.indexOf(2) > -1){
			        	deleteMatch(2);
					}
					if(a != 15 && tmp.indexOf(15) > -1){
						deleteMatch(15);
					}
					if(a != 16 && tmp.indexOf(16) > -1){
						deleteMatch(16);
					}
					matchRangeFlag = 1;
				});
				 */
			}
		} else if ($("#matchParamInput"+a).val()!=$("#matchParamInput"+a+"9").val()){
			matchRangeFlag++;
		}
	}
	if(s == 2){
		$(".matchInputBotClass").hide();
		$(".matchBotClass").show();
	}else if(s == 1){
		$(".matchInputTopClass").hide();
		$(".matchTopClass").show();
	}
	$("#matchParam"+s).hide();
	$("#match"+a).css("backgroundColor","white");
	$("#match"+a).css("color","green");
	matchList.unshift(a);
	matchParamList[a] = new Array();
	if(a==8 || a==12){
		var textArray = [];
		$("#matchParamInput"+a + " option").each(function(){
			textArray.push($(this).text().trim());
		})
		var index = $("#matchParamInput"+a).val()-1;
		matchParamList[a][0] = textArray[index];
	}else if(a==11){
		var index = $("#matchParamInput"+a).val();
		$("#matchParamInput"+a + " option").each(function(){
			if(this.value == index){
				matchParamList[a][0] = $(this).text().trim();
			}
		})
	}else if(a==9 || a==10){
		matchParamList[a][0] = getIpValue("matchParamInput"+a);
		matchParamList[a][1] = getIpValue("matchParamInput"+a+"9");
	}else{
		matchParamList[a][0] = $("#matchParamInput"+a).val();
	}
	if(a==0 || a==1){
		matchParamList[a][0] = changeMacToMaohao(matchParamList[a][0]);
	}
	var tempLength = matchData.length;
	var tempRowNum = -1;
	for(var p=0; p<tempLength; p++){
		if(parseInt(a) == parseInt(matchData[p][2])){
			tempRowNum = p;
		}
	}
	if(tempRowNum != -1){
		matchData.splice(tempRowNum, 1);
	}
	if(b == 1){
		matchData.unshift([matchStr[a], matchParamStr[a][0]+matchParamList[a][0], a]);
	}else if(b == 2){
		if(a!=9 && a!=10){
			matchParamList[a][1] = $("#matchParamInput"+a+"9").val();
		}
		if(a==0 || a==1){
			matchParamList[a][1] = changeMacToMaohao(matchParamList[a][1]);
		}
		matchData.unshift([matchStr[a],matchParamStr[a][0]+matchParamList[a][0]+"&nbsp;&nbsp;"+matchParamStr[a][1]+matchParamList[a][1],a]);
	}
	matchStore.loadData(matchData);
	matchParamList[a][0] = $("#matchParamInput"+a).val();
}
function matchParamCancel(s,a){
	if(s == 2){
		$(".matchInputBotClass").hide();
		$(".matchBotClass").show();
	}else if(s == 1){
		$(".matchInputTopClass").hide();
		$(".matchTopClass").show();
	}
	$("#matchParam"+s).hide();
	$("#match"+a).css("backgroundColor","white");
}
function okChecked(s){
	var id1 = "matchParamInput"+s;
	var id2 = id1 + "9";
	switch (s)
	{
		case 0 : return checkMac(id1) && checkMacMask(id2);
		case 1 : return checkMac(id1) && checkMacMask(id2);
		case 2 : return checkVid(id1) && checkVid(id2) && checkStartEnd(s);
		case 3 : return checkVid(id1);
		case 4 : return true;
		case 5 : return true;
		case 6 : return checkTpid(id1);
		case 7 : return checkTpid(id1);
		case 8 : return true;
		case 9 : return checkIp(id1) && checkIpMask(id2);
		case 10 : return checkIp(id1) && checkIpMask(id2);
		case 12 : return true;
		case 11 : return true;
		case 13 : return true;
		case 14 : return true;
		case 15 : return checkPort(id1) && checkPort(id2) && checkStartEnd(s);
		case 16 : return checkPort(id1) && checkPort(id2) && checkStartEnd(s);
		default : return false;
	}
}
function checkMac(id){
	var input = $("#"+id).val();
	$("#"+id).css("border","1px solid #8bb8f3");
	var reg = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){5})+$/i;
	var reg1 = /^([0-9a-f]{4})(([.][0-9a-f]{4}){2})+$/i;
	var reg2 = /^([0-9a-f]{12})+$/i;
	if(reg.exec(input) && input.length == 17){
		return true;
	}
	if(reg1.exec(input) && input.length == 14){
		return true;
	}
	if(reg2.exec(input) && input.length == 12){
		return true;
	}
	$("#"+id).css("border","1px solid #ff0000");
	$("#"+id).focus();
	return false;
}
function checkMacMask(id){
	var input = $("#"+id).val();
	$("#"+id).css("border","1px solid #8bb8f3");
	var reg = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){0,5})+$/i;
	var reg1 = /^([0-9a-f]{4})(([.][0-9a-f]{4}){2})+$/i;
	var reg2 = /^([0-9a-f]{12})+$/i;
	if(reg.exec(input) && input.length == 17){
		var tempArray = input.split(":")
		var macMask_binary = changeToBinary1(tempArray[0])+
 			                changeToBinary1(tempArray[1])+
							changeToBinary1(tempArray[2])+
							changeToBinary1(tempArray[3])+
							changeToBinary1(tempArray[4])+
							changeToBinary1(tempArray[5]);
		// 格式化输出(补零)
		if (-1 != macMask_binary.indexOf("01")){
			$("#"+id).css("border","1px solid #ff0000")
			$("#"+id).focus();
			return false
		}
		return true
	}
	if(reg1.exec(input) && input.length == 14){
		var tempArray0 = input.substring(0,2);
		var tempArray1 = input.substring(2,4);
		var tempArray2 = input.substring(5,7);
		var tempArray3 = input.substring(7,9);
		var tempArray4 = input.substring(10,12);
		var tempArray5 = input.substring(12,14);
		var tempArray = changeToBinary1(tempArray0)+
					    changeToBinary1(tempArray1)+
						changeToBinary1(tempArray2)+
						changeToBinary1(tempArray3)+
						changeToBinary1(tempArray4)+
						changeToBinary1(tempArray5); 
		if (-1 != tempArray.indexOf("01")){
			$("#"+id).css("border","1px solid #ff0000");
			$("#"+id).focus();
			return false;
		}
		return true;
	}
	if(reg2.exec(input) && input.length == 12){
		var tempArray0 = input.substring(0,2);
		var tempArray1 = input.substring(2,4);
		var tempArray2 = input.substring(4,6);
		var tempArray3 = input.substring(6,8);
		var tempArray4 = input.substring(8,10);
		var tempArray5 = input.substring(10,12);
		var tempArray = changeToBinary1(tempArray0)+ 
						changeToBinary1(tempArray1)+
						changeToBinary1(tempArray2)+
						changeToBinary1(tempArray3)+
						changeToBinary1(tempArray4)+
						changeToBinary1(tempArray5);
		if (-1 != tempArray.indexOf("01")){
			$("#"+id).css("border","1px solid #ff0000");
			$("#"+id).focus();
			return false;
		}
		return true;
	}
	$("#"+id).css("border","1px solid #ff0000");
	$("#"+id).focus();
	return false;
}
function changeToBinary1(os){
	var ten = parseInt(os,16);
	var binary = (parseInt(ten) + 256).toString(2).substring(1);
	return binary;
}
function checkVid(id){
	var input = $("#"+id).val();
	//$("#"+id).css("border","1px solid #8bb8f3");
	var reg = /^([0-9])+$/i;
	if(reg.exec(input) && parseInt(input)>0 && parseInt(input)<4095){
		if($("#"+id).hasClass("normalInputRed")){
			$("#"+id).removeClass("normalInputRed");
		}
		return true;
	}
	//$("#"+id).css("border","1px solid #ff0000");
	$("#"+id).addClass("normalInputRed");
	$("#"+id).focus();
	return false;
}
function checkIp(id){
	var input = getIpValue(id);
	$("#"+id).css("border","1px solid #8bb8f3");
	if(!ipIsFilled(id) || !checkedIpValue(input)){
		$("#"+id).css("border","1px solid #ff0000");
		$("#"+id).focus();
		return false;
	}
	return true;
}
function checkIpMask(id){
	var input = getIpValue(id);
	$("#"+id).css("border","1px solid #8bb8f3");
	if(!ipIsFilled(id) || !checkedIpMask(input)){
		$("#"+id).css("border","1px solid #ff0000");
		$("#"+id).focus();
		return false;
	}
	return true;
}
function checkTpid(id){
	var input = $("#"+id).val();
	var reg1 = /^([0][x][0-9a-f]{4})+$/i;
	var reg2 = /^([0-9a-f]{4})+$/ig;
	if(input == "" || input == null){
		$("#"+id).addClass("normalInputRed");
		$("#"+id).focus();
		return false;
	}else{
		if(reg1.exec(input) || (reg2.exec(input) && input.length<5)){
			if($("#"+id).hasClass("normalInputRed")){
				$("#"+id).removeClass("normalInputRed");
			}
			return true;
		}else{
			$("#"+id).addClass("normalInputRed");
			$("#"+id).focus();
			return false;
		}
	}
}
function checkPort(id){
	var input = $("#"+id).val();
	var reg = /^([0-9]{1,5})+$/i;
	if(input=="" || input == null){
		$("#"+id).addClass("normalInputRed");
		$("#"+id).focus();
		return false;
	}
	if(reg.exec(input) && parseInt(input)>-1 && parseInt(input)<65536){
		if($("#"+id).hasClass("normalInputRed")){
			$("#"+id).removeClass("normalInputRed");
		}
		return true;
	}else{
		$("#"+id).addClass("normalInputRed");
		$("#"+id).focus();
		return false;
	}
}
function checkLimit(id){
	var input = $("#" + id).val();
	//$("#"+id).css("border","1px solid #8bb8f3");
	var reg = /^([0-9]{1,8})+$/i;
	if(input=="" || input == null){
		//$("#"+id).css("border","1px solid #ff0000");
		$("#"+id).addClass("normalInputRed");
		return false;
	}
	if(reg.exec(input) && 63 < parseInt(input) && parseInt(input) < 10000001){
		if($("#"+id).hasClass("normalInputRed")){
			$("#"+id).removeClass("normalInputRed");
		}
		return true;
	}
	//$("#"+id).css("border","1px solid #ff0000");
	$("#"+id).addClass("normalInputRed");
	return false;
}
function clickCheckbox(s){
	if(s == 10){
		if($("#actionCheck10").attr("checked")){
			$("#action10").attr("disabled",false);
			$("#action99").attr("disabled",false);
			actionList.unshift(10); 
		}else{
			$("#action10").attr("disabled",true);
			$("#action99").attr("disabled",true);
			actionList.remove(10);
		}
	}else{
		if($("#actionCheck"+s).attr("checked")){
			$("#action"+s).attr("disabled",false);
			//在点击复选框时控制select和input的样式
			if($("#action"+s).length > 0){
				if($("#action"+s).get(0).tagName == "INPUT"){
					$("#action"+s).removeClass("normalInputDisabled");
				}else if($("#action"+s).get(0).tagName == "SELECT"){
					$("#action"+s).removeClass("normalSelDisabled");
				}
			}
			if(s==2 || s==8){
				$("#action"+s).focus();
				if(s == 8){
					$("#action81").removeClass("normalSelDisabled");
					$("#action81").attr("disabled", false);
					actionList.unshift(12);
				}
			}
			if(s == 0){
				s = parseInt($("#action0").val());
			}
			actionList.unshift(s);
		}else{
			$("#action"+s).attr("disabled",true);
			if(s == 2 || s == 8){
				//$("#action2").css("border","1px solid #ccc");
				$("#action"+s).removeClass("normalInputRed").val("");
			}
			if(s == 8){
				$("#action81").attr("disabled", true);
				$("#action81").addClass("normalSelDisabled");
			}
			//在点击复选框时控制select和input的样式
			if($("#action"+s).length > 0){
				if($("#action"+s).get(0).tagName == "INPUT"){
					$("#action"+s).addClass("normalInputDisabled");
				}else if($("#action"+s).get(0).tagName == "SELECT"){
					$("#action"+s).addClass("normalSelDisabled");
				}
			}
			
			if(s == 0){
				s = parseInt($("#action0").val());
			}
			actionList.remove(s);
		}
	}
}
function action0Change(){
	actionList.remove(0);
	actionList.remove(1);
	s = parseInt($("#action0").val());
	if($("#actionCheck0").attr("checked")){
		actionList.unshift(s);
	}
}
Array.prototype.remove = function(s){
	if(this.indexOf(s) > -1){
		  this.splice(this.indexOf(s), 1);
	}
}
//校验如果match及其参数完全相同，action及其参数不能完全相同 
function checkTheSameRule(){
    var matchList = new Array();//新增或者修改的rule
    matchList = matchList;
    var matchRule = new Array();//匹配match的序号数组
    for(var i=0;i<aclRuleList.length;i++){
        var matchFlag = false;//标示match是否匹配
        if(matchList.sort().toString() == aclRuleList[i].topMatchedFieldSelectionSymbol.sort().toString()){
            for(var j=0;j<matchList.length;j++){
                if(matchList[j] == 0){
                    if($("#matchParamInput0").val() == aclRuleList[i].topMatchedSrcMac ||
                            $("#matchParamInput09").val() == aclRuleList[i].topMatchedSrcMacMask){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 1){
                    if($("#matchParamInput1").val() == aclRuleList[i].topMatchedSrcMac ||
                            $("#matchParamInput19").val() == aclRuleList[i].topMatchedSrcMacMask){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 2){
                	//检测svlan是否有重叠
                    var t1 = $("#matchParamInput2").val();
                    var t2 = $("#matchParamInput29").val();
                    var t3 = aclRuleList[i].topMatchedStartSVid;
                    var t4 = aclRuleList[i].topMatchedEndSVid;
                    if(!(t2<t3||t4<t1)){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 3){
                    if($("#matchParamInput3").val() == aclRuleList[i].topMatchedStartCVid){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 4){
                    if($("#matchParamInput4").val() == aclRuleList[i].topMatchedOuterCos){
                    	matchFlag = true;
                    }
                }
                else if(matchList[j] == 8){
                    if($("#matchParamInput8").val() == aclRuleList[i].topMatchedEthernetType){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 9){
                    //检测是否在同一网段
                    var ip1 = ipAAndipB($("#matchParamInput9").val(),$("#matchParamInput99").val());
                    var ip2 = ipAAndipB(aclRuleList[i].topMatchedSrcIP,aclRuleList[i].topMatchedSrcIPMask);
                    if(ip1 != ip2){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 10){
                    //检测是否在同一网段
                    var ip1 = ipAAndipB($("#matchParamInput10").val(),$("#matchParamInput109").val());
                    var ip2 = ipAAndipB(aclRuleList[i].topMatchedDstIP,aclRuleList[i].topMatchedDstIPMask);
                    if(ip1 != ip2){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 11){
                    if($("#matchParamInput11").val() == aclRuleList[i].topMatchedL3ProtocolClass){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 12){
                    if($("#matchParamInput12").val() == aclRuleList[i].topMatchedIpProtocol){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 13){
                    if($("#matchParamInput13").val() == aclRuleList[i].topMatchedDscp){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 14){
                    if($("#matchParamInput14").val() == aclRuleList[i].topMatchedTos){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 15){
                	//检测srcport是否有重叠
                    var t1 = $("#matchParamInput15").val();
                    var t2 = $("#matchParamInput159").val();
                    var t3 = aclRuleList[i].topMatchedStartSrcPort;
                    var t4 = aclRuleList[i].topMatchedEndSrcPort;
                    if(!(t2<t3||t4<t1)){
                    	matchFlag = true;
                    }
                }else if(matchList[j] == 16){
                    //检测dstport是否有重叠
                    var t1 = $("#matchParamInput16").val();
                    var t2 = $("#matchParamInput169").val();
                    var t3 = aclRuleList[i].topMatchedStartDstPort;
                    var t4 = aclRuleList[i].topMatchedEndDstPort;
                    if(!(t2<t3||t4<t1)){
                    	matchFlag = true;
                    }
                }
            }
        }
        if(matchFlag){
        	matchRule.add(i);
        }
    }
    var actionFlag = false;//标示action是否匹配    
    if(matchRule.length != 0){
        for(var k=0;k<matchRule.length;k++){
			//如果是修改本条数据，不用检测冲突
            if(aclRuleIndex != aclRuleList[matchRule[k]].topAclRuleIndex){
                var tmpA = aclRuleList[matchRule[k]].topAclActionList;
                for(var t=0; t<actionList.length; t++){
                    if((actionList.indexOf(1) > -1 && tmpA.indexOf(0) > -1) ||
                            (actionList.indexOf(0) > -1 && tmpA.indexOf(1) > -1) || checkedAction(aclRuleList[matchRule[k]])){
                    	actionFlag = true;
                    }
                }
            }
        }
    }
    if(actionFlag){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.ruleConflict)
    	return true;
    }else{
		return false;
    }
}
function checkedAction(rule){
	var list = rule.topAclActionList;
	for(var a=0; a<actionList.length; a++){
		var kk = parseInt(actionList[a]);
		if(list.indexOf(kk) > -1){
			var p = rule.topAclActionParameterValueList;
			if(kk == 8){
				if($("#action8").val() == p[8] && $("#action81").val() == p[p.length]){
					return true;
				}
			}else if($("#action" + kk).val() == p[kk]){
				return true;
			}
		}
	}
	return false;
}

Ext.onReady(function(){
	//IP输入框
	var ip1 = new ipV4Input("matchParamInput9", "span1");
	ip1.width(120);
	ip1.onChange = function(){
		checkIp("matchParamInput9");
	}
	var ipMask1 = new ipV4Input("matchParamInput99", "span11");
	ipMask1.width(120);
	ipMask1.onChange = function(){
		checkIpMask("matchParamInput99");
	}
	ipMask1.onEnterKey = function(e){
		matchParamOnEnterKey(9,event.keyCode || e.which);
	}
	var ip2 = new ipV4Input("matchParamInput10", "span2");
	ip2.width(120);
	ip2.onChange = function(){
		checkIp("matchParamInput10");
	}
	var ipMask2 = new ipV4Input("matchParamInput109", "span22");
	ipMask2.width(120);
	ipMask2.onChange = function(){
		checkIpMask("matchParamInput109");
	}
	ipMask2.onEnterKey = function(){
		matchParamOnEnterKey(10,event.keyCode || event.which);
	}
	loadMatchGrid();
	initAllDiv();
	initAllData();
	if(modifyFlag==9){
		$(".matchInputBotClass").attr("disabled",true);
		$(".matchInputTopClass").attr("disabled",true);
		$("#action*").attr("disabled",true);
	}
	//在ACL已经应用到端口的情况下控制不让操作保存按钮,查看的时候灰掉
	if(modifyFlag == 9){
		$("#saveBt").attr("disabled",true).addClass("disabledAlink");
	}
	loadMatchData();
});

function saveClick(){
	//在ACL已经应用到端口的情况下控制不让操作保存按钮
	var $saveBt = $("#saveBt");
	if($saveBt.hasClass("disabledAlink")){
		return;
	}
	//modify lzt
	//底层存在错误信息，网管只需要获得错误码
	/* if(matchRangeFlag > 1){
		window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.matchRangeTip )
		return;
	} */
	/* 
	//命令行可以没有任何匹配规则,改为与命令行一致
	if(matchStore.getCount() <= 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.noMatchRule);
		return;
	} */
	
	if($("#actionCheck2").attr("checked")){
		if(!checkLimit("action2")){
			$("#action2").focus();
			return;
		}
	}
	if($("#actionCheck8").attr("checked")){
		if(!checkVid("action8")){
			$("#action8").focus()
			return
		}
	}
	if($("#actionCheck6").attr("checked") && $("#actionCheck7").attr("checked")){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.exeCosTosTip);
		return;
	}
	if(actionList.length == 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.plsCfgRuleAction);
		return;
	}
	//当设置scos时必须先设置svlan
	if($("#actionCheck9").attr("checked") && !($("#actionCheck8").attr("checked"))){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.outervlanPri )
        return;
	}
	//同一ACL中的规则不能冲突
	if(checkTheSameRule()){
		return;
	}
	matchList = new Array();
	for(var t=0; t<matchData.length; t++){
		matchList.push(matchData[t][2]);
	}
	aclRuleIndex = $("#ruleId").val();
	var matchTmp = matchList;
	/* 
	if(matchTmp.indexOf(4) > -1 && matchTmp.indexOf(2) == -1){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ACL.cfgOuterPriRule )
		return;
	}
	 */
	var matchParamTmp;
	var matchParamTmpList = new Array();
	var actionTmp = actionList.join("_");
	var actionParamTmp = new Array();
	var a = matchParamList;
	var tmp1 = [0,1,2,9,10,15,16];
	var tmp2 = [3,4,5,6,7,8,11,12,13,14];
	//匹配CVID,起始终止相同
	//matchParamTmpList[3] = "x_" + $("#matchParamInput3").val() + ",x_" + $("#matchParamInput3").val();
	for(var x=0; x<tmp1.length; x++){
		if(tmp1[x]==9||tmp1[x]==10){
			matchParamTmpList[tmp1[x]] = "x_" + getIpValue("matchParamInput"+tmp1[x]) + ",x_" + getIpValue("matchParamInput"+tmp1[x]+"9");
		}else{
			matchParamTmpList[tmp1[x]] = "x_" + $("#matchParamInput"+tmp1[x]).val() + ",x_" + $("#matchParamInput"+tmp1[x]+"9").val();
		}
	}
	for(var x=0; x<tmp2.length; x++){
		matchParamTmpList[tmp2[x]] = "x_" + $("#matchParamInput"+tmp2[x]).val();
	}
	
	matchParamTmp = matchParamTmpList.join();
	for(var x=2; x<11; x++){
		if($("#action"+x).val()!=null && $("#action"+x).val()!=""){
			actionParamTmp.push($("#action"+x).val());
		}else{
			actionParamTmp.push(0);
		}
	}
	actionParamTmp.push($("#action81").val());
	if(modifyFlag == 1){
    	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.saving , 'ext-mb-waiting');
    	Ext.Ajax.request({
    		url:"/epon/acl/modifyAclRule.tv?m=" + Math.random(),
    		method:"post",
    		//async: false,
    		success:function(response){
    			if(response.responseText == "success"){
    				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.mdfRuleOk ,null,function(){
    					$("#aclIndexSearch", window.parent.document).focus();
    				});
    				//更新rule列表数据
    				var parent = window.parent.getWindow("aclList").body.dom.firstChild.contentWindow;
    				var listTemp = parent.listGrid.getSelectionModel().getSelected();
    				var rowNum = parent.listStore.indexOf(listTemp);
    				parent.listGrid.getSelectionModel().selectRow(rowNum,true);
    				parent.changeAclList();
    				window.parent.closeWindow('aclRule');
    			}else{
    				/* if(response.responseText.split(":")[0] == "no response"){
                        window.parent.showMessageDlg(I18N.COMMON.tip, "cannot connect to device!");
                    }else{
	    				window.parent.showMessageDlg(I18N.COMMON.tip ,response.responseText);
                    } */
   					window.parent.showMessageDlg(I18N.COMMON.tip ,"@ACL.mdfRuleError@");
    			}
    		},failure:function (response) {
                window.parent.showMessageDlg(I18N.COMMON.tip  ,response.responseText);
            },params : {
      			entityId : entityId,
      			aclIndex : aclIndex,
      			aclRuleIndex : aclRuleIndex,
      			topMatchedFieldSelectionSymbol : matchTmp,
      			matchParasJsonStr : matchParamTmp,
      			aclAction : actionTmp,
      			aclActionPara : actionParamTmp
      		}
      	});
    }else if(modifyFlag == 0){
    	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.saving , 'ext-mb-waiting');
    	$.ajax({
    		url:"/epon/acl/addAclRule.tv?m=" + Math.random(),
    		data: {entityId : entityId,
      			aclIndex : aclIndex,
      			aclRuleIndex : aclRuleIndex,
      			topMatchedFieldSelectionSymbol : matchTmp,
      			matchParasJsonStr : matchParamTmp,
      			aclAction : actionTmp,
      			aclActionPara : actionParamTmp
    		},
    		success:function(text){
    			if(text == "success"){
    				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.addRuleOk , null , function(){
    					$("#aclIndexSearch", window.parent.document).focus();
    				})
    				var pa = window.parent.getWindow("aclList").body.dom.firstChild.contentWindow
    				//修改acl列表数据
    				var listTemp = pa.listGrid.getSelectionModel().getSelected()
    				var rowNum = pa.listStore.indexOf(listTemp)
    				pa.listData[rowNum][1]++
    				pa.listStore.loadData(pa.listData)
    				//增加rule列表数据
    				pa.listGrid.getSelectionModel().selectRow(rowNum,true)
    				pa.changeAclList()
    				window.parent.closeWindow('aclRule')
    			}else{
                    window.parent.showMessageDlg(I18N.COMMON.tip ,I18N.ACL.addRuleFail)
    			}
    		},error:function (text) {
    			window.parent.showMessageDlg(I18N.COMMON.tip ,I18N.ACL.addRuleFail)
            },cache: false
        });
    }
}
function matchParamOnEnterKey(s,e){
	if(e == 13 || e == 32){
		$("#matchParamOkBt"+s).click();
		e.returnValue = false;
	}
}
function rangeFocus(el){
	el.select();
}
function rangeBlur(id){
	if(!$("#" + id + "9").val()){
		$("#" + id + "9").val($("#" + id).val());
	}
}

//检查结束值应该比起始值大
function checkStartEnd(value){
	var $start = $("#matchParamInput" + value);
	var $end = $("#matchParamInput" + value + "9");
	var sValue = parseInt($start.val(),10);
	var eValue = parseInt($end.val(),10);
	if(sValue <= eValue){
		if($end.hasClass("normalInputRed")){
			$end.removeClass("normalInputRed");
		}
		return true;
	}else{
		$end.addClass("normalInputRed");
		return false;
	}
}

function cancelClick() {
    window.parent.closeWindow('aclRule');
}
</script>
</head>
<body class="openWinBody">
	<div class="edge10 pT5">
		<ul class="leftFloatUl pB10">
			<li class="blueTxt pT3">@ACL.rule@ ID@COMMON.maohao@</li>
			<li>
				<select id="ruleId" style="width: 100px;" class="normalSel"></select>
			</li>
		</ul>
		<div class="clearBoth zebraTableCaption" style="height:145px;">
			<div class="zebraTableCaptionTitle"><span>@ACL.matchRule@</span></div>
			<div style="height:30px"></div>
			<div id=matchGridDiv></div>
		</div>
		
		<div class="zebraTableCaption mT10 pT20" >
     		<div class="zebraTableCaptionTitle"><span>@ACL.execAction@</span></div>
     		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		             	<td width="20">
		             		<input id="actionCheck0" type=checkbox style="width:20px;" onclick="clickCheckbox(0)" class="actionClass" />
		             	</td>
		                 <td class="blueTxt" width="80">
							<label for="actionCheck0">@ACL.execMode@:</label>
		                 </td>
		                 <td>
							<select id="action0" style="width:100px;" disabled onchange='action0Change()' class="normalSel normalSelDisabled">
								<option value=1 style='color:green'>@ACL.allowTH@</option>
								<option value=0 style='color:red'>@ACL.denyTH@</option>
							</select>
		                 </td>
		                 
		                 <td width="20">
		                 	<input id="actionCheck11" type=checkbox style="width:20px;" onclick="clickCheckbox(11)" class="actionClass" />
		                 </td>
		                 <td class="blueTxt" width="80">
							<label for="actionCheck11">copyToCpu</label>
		                 </td>
		                 <td>
							&nbsp;
		                 </td>
		                 
		                  <td width="20">
		                  	<input id="actionCheck2" type=checkbox style="width:20px;" onclick="clickCheckbox(2)" class="actionClass" />
		                  </td>
		                 <td class="blueTxt" width="80">
							<label for="actionCheck2">@ACL.indirectRate@:</label>
		                 </td>
		                 <td>
							<input disabled id="action2" type=text style="width: 100px;" class="actionClass normalInput normalInputDisabled"
								toolTip='@ACL.action2Tip@' onkeyup="checkLimit(this.id)" maxlength="8" /> Kbps
		                 </td>
		             </tr>
		            <tr class="darkZebraTr">
		            	<td>
		            		<input id="actionCheck7" type=checkbox style="width:20px;" onclick="clickCheckbox(7)" class="actionClass" />
		            	</td>
		                 <td class="blueTxt">
							<label for="actionCheck7">@ACL.tosSet@:</label>
		                 </td>
		                 <td>
							<select id="action7" disabled style="width: 100px;" class="actionClass normalSel normalSelDisabled"></select>
		                 </td>
		                 
		                 <td>
		            		<input id="actionCheck6" type=checkbox style="width:20px;" onclick="clickCheckbox(6)" class="actionClass" />
		            	</td>
		                 <td class="blueTxt">
		                 	<label for="actionCheck6">@ACL.dscpSet@:</label>
		                 </td>
		                 <td>
							<select id="action6" disabled style="width: 100px;" class="actionClass normalSel normalSelDisabled"></select>
		                 </td>
		                 
		                 <td>
		            		<input id="actionCheck5" type=checkbox style="width:20px;" onclick="clickCheckbox(5)" class="actionClass" />
		            	</td>
		                 <td class="blueTxt">
		                 	<label for="actionCheck5">@ACL.cosSet@:</label>
		                 </td>
		                 <td>
							<select id="action5" disabled style="width: 100px;" class="actionClass normalSel normalSelDisabled">
							    <option value="0">0</option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
							</select>
		                 </td>
		             </tr>
		             <tr>
		             	<td>
		             		<input id="actionCheck8" type=checkbox style="width:20px;" onclick="clickCheckbox(8)" class="actionClass" />
		             	</td>		             	
		                 <td class="blueTxt">
		                 	<label for="actionCheck8">@ACL.outerVlan@:</label>
		                 </td>
		                 
		                 <td>
		                 	<input disabled id="action8" type=text style="width: 100px;" maxlength=4 
								toolTip='@ACL.action8Tip@'  class="actionClass normalInput normalInputDisabled" onkeyup="checkVid(this.id)" />
		                </td>
		                <td></td>
		                <td class="blueTxt">
		                	@COMMON.priority@
		                </td>
		                <td colspan="4">
		                	<select id="action81" disabled style="width:100px;" class="actionClass normalSel normalSelDisabled">
							    <option value="0">0</option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
							</select>
		                </td>
		             </tr>
		         </tbody>
		     </table>
		</div>		
	</div>


	<div id=matchDiv>
		<div id=matchInput0 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width:70px;">@ACL.originMac@:
			</td><td style="width:120px;">
				<input id="matchParamInput0" style="width:120px;" toolTip='@ACL.macTip@' 
					onkeyup="checkMac(this.id)" maxlength=17 />
			</td><td style="width:10px;">
			</td><td style="width:120px;">@ACL.originMacMask@:
			</td><td style="width:120px;">
				<input id="matchParamInput09" style="width:120px;" toolTip='@ACL.maskTip@' 
					onkeyup="checkMacMask(this.id)" maxlength=17 
					onkeydown="matchParamOnEnterKey(0,event.keyCode || e.which)" />
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt0" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,0,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,0)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput1 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width: 90px;">@ACL.targetMac@:
			</td><td style="width: 120px;">
				<input id="matchParamInput1" style="width:120px;" toolTip='@ACL.macTip@' 
					onkeyup="checkMac(this.id)" maxlength=17 />
			</td><td style="width:10px;">
			</td><td style="width:130px;">@ACL.targetMacMask@:
			</td><td style="width:120px;">
				<input id="matchParamInput19" style="width:120px;" toolTip='@ACL.maskTip@' 
					onkeyup="checkMacMask(this.id)" maxlength=17 
					onkeydown="matchParamOnEnterKey(1,event.keyCode || e.which)" />
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt1" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,1,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,1)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput2 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width: 80px;">@ACL.svidStart@:
			</td><td>
				<input id="matchParamInput2" style="width: 90px;" toolTip='@ACL.range4094@' 
					onkeyup="checkVid(this.id)" maxlength="4"/>
			</td><td style="width:30px;">
			</td><td style="width: 80px;">@ACL.svidTermi@:
			</td><td>
				<input id="matchParamInput29" style="width: 90px;" toolTip='@ACL.range4094@' 
					onkeyup="checkVid(this.id);checkStartEnd(2)" maxlength=4 onfocus="rangeFocus(this)"
					onkeydown="matchParamOnEnterKey(2,event.keyCode || e.which)" />
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt2" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,2,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,2)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput3 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width: 80px;">CVID:
			</td><td>
				<input id="matchParamInput3" style="width: 90px;" toolTip='@ACL.range4094@' 
					onkeyup="checkVid(this.id)" maxlength=4 
					onkeydown="matchParamOnEnterKey(3,event.keyCode || e.which)" />
			</td><td style="width:30px;">
			<!-- 
			</td><td style="width: 80px;"><fmt:message bundle="${i18n}" key="ACL.endCVID" />:
			</td><td>
				<input id="matchParamInput39" style="width: 90px;" title='<fmt:message bundle="${i18n}" key="ACL.range4094" />' 
					onkeyup="checkVid(this.id)" maxlength=4 />
			-->
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt3" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,3,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,3)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput4 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width:90px;">@ACL.outerPri@:
			</td><td>
				<select id="matchParamInput4" style="width: 100px;"
					onkeydown="matchParamOnEnterKey(4,event.keyCode || e.which)" >
				    <option value="0">0</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
				</select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt4" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,4,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,4)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput5 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width:90px;">@ACL.innerPri@:
			</td><td>
				<select id="matchParamInput5" style="width: 100px;" onkeydown="matchParamCancel(2,5)">
					<!-- onkeydown="matchParamOnEnterKey(5,event.keyCode || e.which)" -->
				    <option value="0">0</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
				</select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt5" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,5,1)" >&nbsp;&nbsp;
				<!-- onclick="matchParamOk(2,5,1)" -->
				<img title='@COMMON.close@' src="/images/close.gif" onclick="matchParamCancel(2,5)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput6 style="display:none;" class=matchInputBotClass>
			<table>
			<tr><td style="width:100px;">@ACL.outerTpid@:
			</td><td>
				<select id="matchParamInput66" onclick="checkTpid('matchParamInput6')"
		style="position: absolute; left: 93px; top: 0px; width: 100px; height: 20px; clip: rect(0, 100, 20, 82)"
					onchange="matchParamInput6.value=matchParamInput66.value;"
					onmouseup="matchParamInput6.value=matchParamInput66.value;"
					onkeydown="matchParamOnEnterKey(6,event.keyCode || e.which)" >
						<option value="0x8100">0x8100</option>
						<option value="0x9100">0x9100</option>
				</select> 
				<input type="text" id="matchParamInput6" onkeyup="checkTpid(this.id)"
					value="" maxlength=6 toolTip='@ACL.matchParamTip@'
					style="position: absolute; left: 93px; top: 0px; width: 82px; height: 20px"
					onkeydown="matchParamOnEnterKey(6,event.keyCode || e.which)" />
			</td><td style="width:90px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt6" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,6,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,6)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput7 style="display:none;" class=matchInputBotClass>
			<table>  
			<tr><td style="width:100px;">@ACL.innerTpid@:
			</td><td>
				<select id="matchParamInput77" onclick="checkTpid('matchParamInput7')"
		style="position: absolute; left: 93px; top: 0px; width: 100px; height: 20px; clip: rect(0, 100, 20, 82)"
					onchange="matchParamInput7.value=matchParamInput77.value;"
					onmouseup="matchParamInput7.value=matchParamInput77.value;"
					onkeydown="matchParamOnEnterKey(7,event.keyCode || e.which)" >
						<option value="0x8100">0x8100</option>
						<option value="0x9100">0x9100</option>
				</select> 
				<input type="text" id="matchParamInput7" onkeyup="checkTpid(this.id)"
					value="" maxlength=6 toolTip='@ACL.matchParamTip@'
					style="position: absolute; left: 93px; top: 0px; width: 82px; height: 20px"
					onkeydown="matchParamOnEnterKey(7,event.keyCode || e.which)" />
			</td><td style="width:90px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt7" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(2,7,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(2,7)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput8 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:120px;">@ACL.etherType@:
			</td><td>
				<select id="matchParamInput8" style="width: 100px;"
					onkeydown="matchParamOnEnterKey(8,event.keyCode || e.which)" >
				    <option value="1">ip</option>
					<option value="2">arp</option>
					<option value="3">rarp</option>
					<option value="4">snmp</option>
					<option value="5">ipv6</option>
					<option value="6">ppp</option>
					<option value="7">ppp-disc</option>
					<option value="8">ppp-session</option>
					<!-- <option value="9">NULL</option> -->
				</select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt8" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,8,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,8)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput9 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:60px;">@ACL.originIp@:
			</td><td>
				<!-- <input id="matchParamInput9" style="width: 120px;" title='<fmt:message bundle="${i18n}" key="ACL.inputIp" />' 
					onkeyup="checkIp(this.id)" maxlength=15 /> -->
				<span id="span1"></span>
			</td><td style="width:30px;">
			</td><td style="width:100px;">@ACL.originIpMask@:
			</td><td>
				<!-- <input id="matchParamInput99" style="width: 120px;" title='<fmt:message bundle="${i18n}" key="ACL.inputMask" />' 
					onkeyup="checkIpMask(this.id)" maxlength=15 /> -->
				<span id="span11"></span>
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt9" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,9,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,9)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput10 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:70px;">@ACL.targetIp@:
			</td><td>
				<!-- <input id="matchParamInput10" style="width: 120px;" title='<fmt:message bundle="${i18n}" key="ACL.inputIp" />' 
					onkeyup="checkIp(this.id)" maxlength=15 /> -->
				<span id="span2"></span>
			</td><td style="width:30px;">
			</td><td style="width:90px;">@ACL.targetIpMask@:
			</td><td>
				<!-- <input id="matchParamInput109" style="width: 120px;" title='<fmt:message bundle="${i18n}" key="ACL.inputMask" />' 
					onkeyup="checkIpMask(this.id)" maxlength=15 /> -->
				<span id="span22"></span>
			</td><td style="padding-left:10px;">
		<img id="matchParamOkBt10" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,10,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,10)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput11 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:110px;">@ACL.ipMessageType@:
			</td><td>
				<select id="matchParamInput11"
					style="width: 100px;" onkeydown="matchParamOnEnterKey(11,event.keyCode || e.which)" >
						<option value="1">nonIp</option>
						<!-- <option value="2">nonIpv4</option> -->
						<option value="3">ipv4</option>
						<!-- <option value="4">nonIpv6</option> -->
						<option value="5">ipv6</option>
				</select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
		<img id="matchParamOkBt11" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,11,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,11)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput12 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:110px;">@ACL.ipProType@:
			</td><td>
				<select id="matchParamInput12"style="width: 100px;"
					onkeydown="matchParamOnEnterKey(12,event.keyCode || e.which)">
				    <option value="1">icmp</option>
					<option value="2">igmp</option>
					<option value="3">tcp</option>
					<option value="4">egp</option>
					<option value="5">udp</option>
					<!-- <option value="6">NULL </option> -->
				</select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt12" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,12,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,12)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput13 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:70px;">DSCP:
			</td><td>
				<select id="matchParamInput13" style="width: 100px;"
					onkeydown="matchParamOnEnterKey(13,event.keyCode || e.which)"></select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt13" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,13,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,13)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput14 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:70px;">TOS:
			</td><td>
				<select id="matchParamInput14" style="width: 100px;"
					onkeydown="matchParamOnEnterKey(14,event.keyCode || e.which)"></select>
			</td><td style="width:30px;">
			</td><td>
			</td><td>
				
			</td><td style="padding-left:10px;">
				<img id="matchParamOkBt14" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,14,1)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,14)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput15 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:120px;">@ACL.originPortStart@
			</td><td>
				<input id="matchParamInput15" type=text style="width: 80px;" maxlength="5"
					toolTip="@ACL.range65535@" onkeyup="checkPort(this.id)"/>
			</td><td style="width:10px;">
			</td><td style="width:120px;">@ACL.originPortTermi@
			</td><td>
				<input id="matchParamInput159" type=text style="width: 80px;" maxlength=5 onfocus="rangeFocus(this)"
					toolTip="@ACL.range65535@" onkeyup="checkPort(this.id);checkStartEnd(15)" 
					onkeydown="matchParamOnEnterKey(15,event.keyCode || e.which)" />
			</td><td style="padding-left:30px;">
				<img id="matchParamOkBt15" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,15,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,15)">
			</td></tr>
			</table>
		</div>
		<div id=matchInput16 style="display:none;" class=matchInputTopClass>
			<table>
			<tr><td style="width:120px;">@ACL.targetPortStart@
			</td><td>
				<input id="matchParamInput16" type=text style="width: 80px;" maxlength="5"
					toolTip="@ACL.range65535@" onkeyup="checkPort(this.id)" />
			</td><td style="width:10px;">
			</td><td style="width:120px;">@ACL.targetPortTermi@
			</td><td>
				<input id="matchParamInput169" type=text style="width: 80px;" maxlength=5 onfocus="rangeFocus(this)"
					toolTip="@ACL.range65535@" onkeyup="checkPort(this.id);checkStartEnd(16)" 
					onkeydown="matchParamOnEnterKey(16,event.keyCode || e.which)" />
			</td><td style="padding-left:30px;">
				<img id="matchParamOkBt16" alt="@COMMON.confirm@" src="/images/checked.gif" onclick="matchParamOk(1,16,2)">&nbsp;&nbsp;
				<img alt="@COMMON.close@" src="/images/close.gif" onclick="matchParamCancel(1,16)">
			</td></tr>
			</table>
		</div>
	</div>



				<!-- 已经屏蔽的字段 -->
				<table  style="display:none;">
						<tr><td>
							<input id="actionCheck9" type=checkbox style="width:20px;" onclick="clickCheckbox(9)" class="actionClass" />
						</td><td>
							<label for="actionCheck9">@ACL.outerPri@:</label>
						</td><td>
							<select id="action9" disabled style="width:100px;" class="actionClass">
							    <option value="0">0</option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
							</select>
						</td><td><!-- 我只是空白 -->
						</td><td>
							<input id="actionCheck4" type=checkbox style="width:20px;" onclick="clickCheckbox(4)" class="actionClass" />
						</td><td>
							<label for="actionCheck4">@ACL.queneSet@:</label>
						</td><td>
							<select  id="action4" disabled style="width: 100px;"  class="actionClass">
							    <option value="0">0</option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
							</select>
						</td></tr>
						<tr style="display:none;"><td>
							<input id="actionCheck3" type=checkbox style="width:20px;" onclick="clickCheckbox(3)" class="actionClass" />
						</td><td>
							<label for="actionCheck3">@EPON.mirror@: </label>
						</td><td>
							<select disabled id="action3"  style="width: 100px;" class="actionClass"></select>
						</td><td><!-- 我只是空白 -->
						</td><td>
							<input id="actionCheck10" type=checkbox style="width:20px;" onclick="clickCheckbox(10)" class="actionClass" />
						</td><td>
							<label for="actionCheck10">@ACL.tpidSet@: </label>
						</td><td>
							<select id="action99" onclick="checkTpid('action10')" disabled class="actionClass"
								style="position: absolute; left: 172px; top: 433px; width: 100px; height: 20px;clip: rect(0, 100, 20, 82)"
								onchange="action10.value=action99.value;" >
								<option value="0x8100">0x8100</option>
								<option value="0x9100">0x9100</option>
							</select> 
							<input type="text" id="action10" onkeyup="checkTpid(this.id)" class="actionClass"
								maxlength="6" toolTip='@ACL.action10Tip@' disabled
								style="position: absolute; left: 172px; top: 433px; width: 82px; height: 20px;">
						</td></tr>
					<!-- 布局结构的demo -->
						<tr style="display:none;"><td>
						</td><td>
						</td><td>
						</td><td><!-- 我只是空白 -->
						</td><td>
						</td><td>
						</td><td>
						</td></tr>
					</table>
				<!-- 已经屏蔽的字段 -->



	 	<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
		         <li><a id=saveBt onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
</body>
</Zeta:HTML>