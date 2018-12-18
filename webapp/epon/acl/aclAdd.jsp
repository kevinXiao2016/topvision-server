<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE epon
</Zeta:Loader>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var entityId = <s:property value="entityId"/>;
var tempAclId = '<s:property value="tempAclId" />';
tempAclId = tempAclId=="" ? "" : parseInt(tempAclId);
var aclFlagList = ${aclFlagList};
aclFlagList = aclFlagList.join("") == "false" ? new Array() : aclFlagList;

function saveClick(){
	if(!checkedAclId() || !checkedDescr()){
		$("#aclIndex").focus();
		$("#saveBt").mouseout();
		return;
	}
	var aclIndexTemp = parseInt($("#aclIndex").val());
	var descrTemp = $("#descr").val();
	var priorityTemp = $("#aclPriority").val();
	for(var i=0; i<aclFlagList.length; i++){
		if(aclIndexTemp == aclFlagList[i]){
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.ACL.aclExistTip,aclIndexTemp))
			$("#aclIndex").focus();
			$("#saveBt").mouseout();
			return;
		}
	}
	//进行添加
	var params = {
		entityId : entityId,
		aclIndex : aclIndexTemp,
		descr : descrTemp,
		priority : priorityTemp
	};
	var url = '/epon/acl/addAclList.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.ACL.addingAcl,aclIndexTemp ), 'ext-mb-waiting')
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				if(response.responseText.split(":")[0] == "no response"){
                    window.parent.showMessageDlg(I18N.COMMON.tip, "cannot connect to device!");
                }else{
					window.parent.showMessageDlg(I18N.COMMON.tip , response.responseText);
                }
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip , String.format(I18N.ACL.addAclOk , aclIndexTemp ));
			//添加成功后更改父页面数据
			var pa = window.parent.getWindow("aclList").body.dom.firstChild.contentWindow;
			var tempData = new Array();
			pa.portData[aclIndexTemp] = new Array();
			tempData = [aclIndexTemp, 0, priorityTemp, 0, descrTemp];
			pa.ruleLeaveNum = 0;
			pa.portLeaveNum[aclIndexTemp] = 0;
			pa.aclIdFlag = aclIndexTemp;
			pa.listData.unshift(tempData);
			pa.afterAddAcl();
			/* var rec = new Ext.data.Record.create(['aclIndex','ruleNum','priority','portNum','descr']);
			var record = new rec({
				aclIndex : aclIndexTemp,
				ruleNum : 0, 
				priority : priorityTemp,
				portNum : 0,
				descr : descrTemp
			})
			pa.listStore.insert(0,record); */
			//pa.listGrid.getSelectionModel().selectRow(0,true);
			pa.portStore.loadData(pa.portData[aclIndexTemp]);
			pa.ruleData = new Array();
			pa.ruleStore.loadData(pa.ruleData);
			pa.modifyByPort();
			pa.aclFlagList.unshift(aclIndexTemp);
			//pa.$("#aclIndexSearch").val("");
			pa.$("#descr").val(descrTemp);
			pa.$("#aclPriority").val(priorityTemp);
			pa.$("#aclRuleNum").html("<font color=red>0</font> / 16");
			pa.$("#aclIndexText").html("ACL"+"@COMMON.maohao@" + aclIndexTemp);
			cancelClick();
		},
		failure : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip , response.responseText);
		},
		params : params
	});
}
function checkedAclId(){
	var aclIndexTemp = $("#aclIndex").val();
	$("#aclIndex").css("border","1px solid #8bb8f3");
	reg = /^([1-9][0-9]{0,3})+$/ig;
	if(aclIndexTemp=="" || aclIndexTemp==null || aclIndexTemp==undefined || !reg.exec(aclIndexTemp) || parseInt(aclIndexTemp)>2000){
		$("#aclIndex").css("border","1px solid #FF0000");
		return false;
	}
	return true;
}
function checkedDescr(){
	var tempDescr = $("#descr").val();
	$("#descr").css("border","1px solid #8bb8f3");
	var reg = /^([a-z._\(\)\[\]\-\s\d])+$/ig;
	if(tempDescr.length > 63 || tempDescr=="" || tempDescr==null || !reg.exec(tempDescr)){
		$("#descr").css("border","1px solid #FF0000");
		return false;
	}
	return true;
}
function keyup(){
	$("#saveBt").attr("disabled",false);
	if(!checkedDescr()){
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
		$("#descr").focus();
	}
	if(!checkedAclId()){
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
		$("#aclIndex").focus();
	}
}

Ext.onReady(function(){
	if(tempAclId > 0){
		$("#aclIndex").val(tempAclId);
		$("#descr").html("ACL_" + tempAclId);
		$("#saveBt").attr("disabled",false);
	}
	$("#aclIndex").focus();
	
	/************************
	 * test ipV4Input script*
	 ************************/
	/* var ip1 = new ipV4Input("ip1", "span1");
	var ip2 = new ipV4Input("ip2", "span2"); */
	//end test
	
});

function cancelClick(){
	window.parent.getWindow("aclList").body.dom.firstChild.contentWindow.$("#aclIndexSearch").focus();
	window.parent.closeWindow('aclAdd');
}

</script>
</head>
<body class="openWinBody">
	<div class="edge10 pT20">
		<div class="zebraTableCaption">
	     	<div class="zebraTableCaptionTitle"><span>@ACL.aclBasicProp@</span></div>
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		                 <td class="rightBlueTxt" width="180">
							ACL ID：
		                 </td>
		                 <td>
							<input id="aclIndex" maxlength=4 style="width:200px;" onkeyup=keyup() class="normalInput"
								 tooltip='@ACL.aclIndexTip@' />
		                 </td>
		             </tr>
		             <tr class="darkZebraTr">
		                 <td class="rightBlueTxt">
							@COMMON.desc@:
		                 </td>
		                 <td>
							<textarea id=descr rows=3 style="width:196px; height:40px;" class="normalInput" onkeyup=keyup() >description</textarea>
		                 </td>
		             </tr>
		             <tr>
		                 <td class="rightBlueTxt withoutBorderBottom">
							@ACL.priority@:
		                 </td>
		                 <td class="withoutBorderBottom">
							<select id="aclPriority" style="width:60px;" class="normalSel" >
								<option value=1>1</option>
								<option value=2>2</option>
								<option value=3>3</option>
								<option value=4>4</option>
								<option value=5>5</option>
								<option value=6>6</option>
								<option value=7>7</option>
								<option value=8>8</option>
								<option value=9>9</option>
								<option value=10>10</option>
								<option value=11>11</option>
								<option value=12>12</option>
								<option value=13>13</option>
								<option value=14>14</option>
								<option value=15>15</option>
								<option value=16>16</option>
							</select>
		                 </td>
		             </tr>
		         </tbody>
		     </table>
		 </div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a  id=saveBt onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@ACL.AddAcl@</span></a></li>
		         <li><a onclick="cancelClick()" id=cancelBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
		     </ol>
		</div>
		
	</div>
	

	<!-- test ipV4Input 
	<span id="span1"></span>
	<input type="button" value="1" onclick='aaaa()' />
	<span id="span2"></span>
	-->
</body>
</Zeta:HTML>