<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
    import js.tools.ipText static
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = '${entityId}';
var proxyId = '${proxyId}';
var groupNum = '${groupNum}';
var maxGroupNum = '${maxGroupNum}';
var proxyChName = '${proxyChName}';
var proxyName = '${proxyName}';
var proxyMvlanId = '${proxyMvlanId}';//原MVID
//var srcIp = '${srcIp}';
var mvlanIp = '${mvlanIp}';
var maxBW = '${maxBW}';
var modifyFlag = true;
var mvlanList = ${mvlanList};
if(mvlanList.join("") == "false"){
	mvlanList = new Array();
}
var mvlanGrid;
var mvlanStore;
var mvlanData = new Array();
var proxyIdListStr = '<s:property value="proxyIdList" />';
var proxyIdList = new Array();

var mvidList = ${mvidList};
if(mvidList.join("") == "false"){
	mvidList = new Array();
}

var vlanList = new Array();
var needToLoadVlan = true;

function cancelClick(){
	window.parent.closeWindow('igmpProxyDetail');
}

function satVlanList(){
	var newMvlanId = $("#proxyMvlanId").val();
	vlanList.push(newMvlanId);
	window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.createMvlanEr, newMvlanId ))
}

function saveClick(){
	if(groupNum >= maxGroupNum){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.igmpOutRestric);
		return;
	}
	if(!ipIsFilled("mvlanIp")){
		window.parent.showMessageDlg(I18N.COMMON.tip, '@IGMP.ipTip@');
		keyup(6);
		return;
	}
	var newMvlanId = $("#proxyMvlanId").val();
	if(vlanList.indexOf(newMvlanId) == -1){
		window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.createMvlanTip , newMvlanId ), function(type) {
			if (type == 'no'){return;}
			needToLoadVlan = true;
			window.top.createDialog('setVlanName', I18N.IGMP.setVlanProp , 420, 185,
				    '/epon/vlan/showVlanNameJsp.tv?entityId='+entityId+"&vlanIndex="+newMvlanId+"&modifyFlag="+false, null, true, true);
		});
		return;
	}
	var proxyId = $("#proxyId").val();
	var proxyName = $("#proxyName").val();
	var proxyChName = $("#proxyChName").val();
	var maxBW = $("#maxBW").val();
	var mvlanIp = getIpValue("mvlanIp");
	//var srcIp = $("#srcIp1").val() + "." + $("#srcIp2").val() + "." + $("#srcIp3").val() + "." + $("#srcIp4").val();
/* 	var mvlanHasFlag = true;
	for(var y=0; 5*y<mvlanList.length; y++){
		if(parseInt(newMvlanId) == mvlanList[5*y]){
			mvlanHasFlag = false;
		}
	}
	if(mvlanHasFlag){
		window.parent.showMessageDlg(I18N.COMMON.tip, 'mvlan:'+ newMvlanId +'is not had, please create it first!');
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
		return;
	} */
	if(modifyFlag){//进行修改，成功之后修改父页面的gridstore
		var params = {
			entityId : entityId,
			proxyName : proxyName,
			proxyChName : proxyChName,
			multicastMaxBW : maxBW,
			proxyId : proxyId,
			proxyIndex : proxyId,
			proxyMulticastIPAddress : mvlanIp,
			proxyMulticastVID : newMvlanId,
			cmIndex : proxyMvlanId
			//proxySrcIPAddress : srcIp
		};
		var url = '/epon/igmp/modifyIgmpProxyInfo.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SUPPLY.mdfingMvlanIgmpInfo , proxyId ) , 'ext-mb-waiting')
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.mdfMvlanIgmpInfoEr, proxyId ))
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.mdfMvlanIgmpInfoOk, proxyId ))
				var a = window.parent.getWindow("igmpProxy").body.dom.firstChild.contentWindow.proxyData;
				var flagNum = -1;
				for(var k=0; k<a.length; k++){
					if(proxyId == a[k][0]){
						a[k] = [proxyId,proxyChName,proxyName,newMvlanId,"",mvlanIp,maxBW];
						flagNum = k;
					}
				}
				var b = window.parent.getWindow("igmpProxy").body.dom.firstChild.contentWindow.proxyStore;
				b.loadData(a);
				if(flagNum > -1){
					window.parent.getWindow("igmpProxy").body.dom.firstChild.contentWindow.proxyGrid.selModel.selectRow(flagNum,true);
				}
				cancelClick();
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.mdfMvlanIgmpInfoEr, proxyId ))
			},
			params : params
		});
	}else{//进行添加，成功之后修改父页面的gridstore
		for(var h=0; h<proxyIdList.length; h++){
			if(proxyId == proxyIdList[h]){
				R.saveBt.setText( "@COMMON.save@" ).setDisabled(false);
				window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.IGMP.mvlanExist , function(type) {
					if (type == 'no'){
						$("#proxyId").val("")
						return
					}
					modifyFlag = true;
					saveClick();
					return;
				});
			}
		}
		var params = {
			entityId : entityId,
			proxyName : proxyName,
			proxyChName : proxyChName,
			multicastMaxBW : maxBW,
			proxyId : proxyId,
			proxyMulticastIPAddress : mvlanIp,
			proxyMulticastVID : newMvlanId
			//proxySrcIPAddress : srcIp
		};
		var url = '/epon/igmp/addIgmpProxy.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SUPPLY.addingMvlan , proxyId), 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.addMvlanEr , proxyId ))
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.addMvlanOk , proxyId ))
				var a = window.parent.getWindow("igmpProxy").body.dom.firstChild.contentWindow.proxyData;
				a.unshift([proxyId,proxyChName,proxyName,newMvlanId,"",mvlanIp,maxBW]);
				var b = window.parent.getWindow("igmpProxy").body.dom.firstChild.contentWindow.proxyStore;
				b.loadData(a);
				window.parent.getWindow("igmpProxy").body.dom.firstChild.contentWindow.proxyGrid.selModel.selectRow(0,true)
				cancelClick();
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.addMvlanEr , proxyId ))
			},
			params : params
		});
	}
}
function initProxyInfo(){
	$("#proxyId").val(proxyId);
	$("#proxyChName").val(proxyChName);
	$("#proxyName").val(proxyName);
	$("#maxBW").val(maxBW);
	setIpValue("mvlanIp",mvlanIp);
}
function loadMvlanGrid(){
	for(var t=0; 5*t<mvlanList.length; t++){
		mvlanData[t] = [mvlanList[5*t],mvlanList[5*t+1],mvlanList[5*t+2],mvlanList[5*t+3],mvlanList[5*t+4]];
	}
	mvlanStore = new Ext.data.SimpleStore({
		data : mvlanData,
		fields : ['mvlanId','mvlanOtherName','mvlanName','authority','mvlanProxyList']
	});
	mvlanGrid = new Ext.grid.GridPanel({
		id : 'mvlanGrid',
		renderTo : 'mvlanListGrid',
		store : mvlanStore,
		width : 215,
		height : 215,
		frame : false,
		autoScroll : true,
		border : false,
		columns: [{
				header: I18N.IGMP.mvlanId,
				dataIndex: 'mvlanId',
				align: 'center',
				width: 60
			},{
				header: I18N.IGMP.mvlanAlias,
				dataIndex: 'mvlanOtherName',
				align: 'center',
				width: 70
			},{
				header: I18N.IGMP.mvlanName,
				dataIndex: 'mvlanName',
				align: 'center',
				width: 70,
				hidden : true
			},{
				header: I18N.COMMON.authority,
				dataIndex: 'authority',
				align: 'center',
				width: 50,
				hidden : true
			},{
				header: I18N.IGMP.channelList,
				dataIndex: 'mvlanProxyList',
				align: 'center',
				width: 60
			}],
		listeners: {
	        'dblclick':{
	           	fn : dblClickMvlan,
				scope : this
	        }
		}
	});
}
function dblClickMvlan(){
	var tempMvlanId = mvlanGrid.getSelectionModel().getSelected().get('mvlanId');
	$("#proxyMvlanId").val(tempMvlanId);
	keyup(4);
}

Ext.onReady(function(){
	$("input").css("border","1px solid #8bb8f3");
	var mvlanIpInput = new ipV4Input("mvlanIp", "ipInputArea");
	mvlanIpInput.width(200);
	mvlanIpInput.onChange = function(){
		keyup(6);
	}
	
	proxyIdList = proxyIdListStr.split("q");
	if(proxyId == 0){
		R.saveBt.setText( "@COMMON.save@" );
		modifyFlag = false;
	}else{
		initProxyInfo();
		$("#proxyId").attr("disabled",true);
	}
	//loadMvlanGrid();
	$("#proxyChName").focus();
	//------ Load Vlan List -----//
	$.ajax({
		 url: '/epon/vlan/loadOltVlanConfigList.tv',
		data : {entityId : entityId},dataType : 'json',cache:false,
		success : function(vlanJson){
			var list_ =  vlanJson.data
			for(var i=0 ; i<list_.length ;i++) {
				vlanList.push(list_[i].vlanIndex)
			}
		},error : function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.loadVlanError);
		}
	});

	var sel = $("#proxyMvlanId");
	var tmpProxyMvlanId = proxyMvlanId;
	if(mvidList.length > 0){
		if(mvidList.indexOf(tmpProxyMvlanId) == -1){
			tmpProxyMvlanId = -1;
			sel.append(String.format("<option value=-1>{0}</option>", I18N.COMMON.select));
		}
		for(var a=0,n=mvidList.length; a<n; a++){
			sel.append(String.format("<option value={0}>{0}</option>", mvidList[a]));
		}
		$("#proxyMvlanId").val(tmpProxyMvlanId);
	}else{
		sel.append(String.format("<option value=-1>{0}</option>", I18N.IGMP.noMvlanTip));
	}

	$("#proxyId").focus();
});

function keyup(s){
	/* 	if(s == 4){
		var newMvlanId = $("#proxyMvlanId").val();
		var tempNumber = 0;
		mvlanStore.filterBy(function(record){
			if(record.get('mvlanId') == newMvlanId){
				tempNumber++;
			}
			return record.get('mvlanId') == newMvlanId;
		});
		if(tempNumber == 0){
			mvlanStore.filterBy(function(record){
				return true;
			});
		}
	} */
	if(checkedInput(s)){
		R.saveBt.setDisabled(false);
	}else{
		R.saveBt.setDisabled(true);
	}
}
function checkedInput(s){
	var proxyIdTmp = $("#proxyId").val();
	//var proxyChName = $("#proxyChName").val();
	var proxyName = $("#proxyName").val();
	var newMvlanId = $("#proxyMvlanId").val();
	//var srcIp = $("#srcIp").val();
	var mvlanIp = getIpValue("mvlanIp");
	var maxBW = $("#maxBW").val();
	var reg1 = /^([1-9][0-9]{0,3})+$/i;
	var reg2 = /^([a-z0-9/\s-_])+$/ig;
	var reg3 = /^([0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3})+$/ig;
	var reg4 = /^([1-9][0-9]{0,8})+$/ig;

	var tmpL = [1,3,4,6,7];//2,5,8暂不支持
	var tmplll = tmpL.indexOf(s);
	if(tmplll){
		tmpL.splice(tmplll, 1);
		tmpL.unshift(s);
	}
	for(var i=0, il=tmpL.length; i<il; i++){
		switch(tmpL[i]){
		case 1:
			$("#proxyId").css("border","1px solid #8bb8f3");
			if(proxyIdTmp=="" || proxyIdTmp==null || !reg1.exec(proxyIdTmp) || parseInt(proxyIdTmp)>2000){
				$("#proxyId").css("border","1px solid #FF0000");
				return false;
			}
			break;
		case 2://不支持,已屏蔽
			/* $("#proxyChName").css("border","1px solid #8bb8f3");
			if(proxyChName=="" || proxyChName==null || proxyChName.length>150){
				$("#proxyChName").css("border","1px solid #FF0000");
				return false;
			} */	
			break;
		case 3:
			$("#proxyName").css("border","1px solid #8bb8f3");
			if(proxyName=="" || proxyName==null || !reg2.exec(proxyName)){
				$("#proxyName").css("border","1px solid #FF0000");
				return false;
			}
			break;
		case 4:
			if(newMvlanId=="" || newMvlanId==null || !reg1.exec(newMvlanId) || parseInt(newMvlanId)>4094){
				return false;
			}
			break;
		case 5://不支持,已屏蔽
			/* $("#srcIp").css("border","1px solid #8bb8f3");
			var tempIp1 = srcIp.split(".")[0];
			var tempIp2 = srcIp.split(".")[1];
			var tempIp3 = srcIp.split(".")[2];
			var tempIp4 = srcIp.split(".")[3];
			if(srcIp=="" || srcIp==null || !reg3.exec(srcIp) || srcIp.length>15 || srcIp.length<7 
					|| (srcIp.split(".")[4]!=undefined && srcIp.split(".")[4]!=null)
					|| parseInt(tempIp1)>255 || parseInt(tempIp2)>255 || parseInt(tempIp3)>255 || parseInt(tempIp4)>255){
				$("#srcIp").css("border","1px solid #FF0000");
				return false;
			} */
			break;
		case 6:
			setIpBorder("mvlanIp","#8bb8f3");
			if(!ipIsFilled("mvlanIp")){
				setIpBorder("mvlanIp","#ff0000");
				return false;
			}
			var tmpIp = getIpValue("mvlanIp");
			if(!checkMvlanIpRange(tmpIp)){
				isContinue++;
				var p = $("#mvlanIpTip");
				p.css("color", "black");
				setTimeout(function(){
					isContinue++;
					tipColorChange(p, 0, isContinue);
				}, 1000);
				return false;
			}
			break;
		case 7:
			$("#maxBW").css("border","1px solid #8bb8f3");
			if(maxBW=="" || maxBW==null || !reg4.exec(maxBW) || parseInt(maxBW)>1000000){
				$("#maxBW").css("border","1px solid #FF0000");
				return false;
			}
			break;
		case 8://不支持,已屏蔽
			/* if(!checkedManyIp()){
				return false;
			} */
			break;
		default:
			return false;
		}
	}
	return true;
}
/* function checkedManyIp(){
	for(var i=1; i<5; i++){
		//if($("#srcIp"+i).val()=="" || $("#srcIp"+i).val()==null || $("#srcIp"+i).val()==undefined){
		//	return false;
		//}
		if($("#mvlanIp"+i).val()=="" || $("#mvlanIp"+i).val()==null || $("#mvlanIp"+i).val()==undefined){
			return false;
		}
	}
	return true;
} */
var isContinue = 0;
function checkMvlanIpRange(tmpIp){
	var s = tmpIp.split(".");
	var I = 0;
	for(var a=0; a<4; a++){
		var t = 1;
		for(var b=0; b<3-a; b++){
			t *= 256;
		}
		I += parseFloat(s[a]) * t;
	}
	if(I < 3758096640 || I > 4009754623){
		return false;
	}
	return true;
}
function tipColorChange(p, s, f){
	if(s > 203){
		return;
	}else if(s < 20){
		s = 20;
	}else{
		s += 4;
	}
	setTimeout(function(){
		if(isContinue == f){
			var col = "#" + s.toString(16) + s.toString(16) + s.toString(16);
			p.css("color", col);
			tipColorChange(p, s, f);
		}
	}, 200);
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#cancelBt").attr("disabled",false);
		setIpEnable("ipInputArea",false);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@IGMP.newMd@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="rightBlueTxt w200">@IGMP.mvlanId@</td>
					<td><input id="proxyId" type=text class="normalInput w200"
						maxlength=4 tooltip='@IGMP.proxyIdTip@'
						onkeyup=keyup(1) /></td>
				</tr>
				<tr style="display:none">
					<td class="rightBlueTxt">@IGMP.mvlanAlias@</td>
					<td ><input id="proxyChName" type=text class="normalInput w200"
						tooltip='@IGMP.plsInputMvlanAlias@' onkeyup=keyup(2) /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@IGMP.mvlanName@</td>
					<td><input id="proxyName" type=text  class="normalInput w200"
						tooltip='@IGMP.plsInputMvlanName@' onkeyup=keyup(3) /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@IGMP.mvlanVID@</td>
					<td ><select id="proxyMvlanId" class="normalSel w200" onchange='keyup(4)'></select></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@IGMP.mvlanIp@</td>
					<td ><span id="ipInputArea"></span></td>
				</tr>
				<tr>
					<td colspan=2 class="txtCenter"><span id='mvlanIpTip' style='color: #cccccc;'>@IGMP.ipRange@:224.0.1.0 ~ 238.255.255.255</span></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@IGMP.maxRestricBD@</td>
					<td><input id="maxBW" type=text class="normalInput w200" tooltip='@IGMP.maxRestricBDTip@' maxlength=9 onkeyup=keyup(7) />Kbps
					</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSaveOK" disabled="true">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>