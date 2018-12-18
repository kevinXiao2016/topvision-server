<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY jquery
    LIBRARY zeta
    MODULE epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${entityId}';
var aclIndex = '${aclIndex}';
var ruleLeaveNum = '${ruleLeaveNum}';
var descr = '${descr}';

//修改ACL列表的描述、优先级
function saveClick(){
	var $desc = $("#descr").val();
	var $aclPriority = $("#aclPriority").val();
	var params = {
		entityId : entityId,
		aclIndex : aclIndex,
		descr : $desc,
		priority : $aclPriority,
		ruleNo : ruleLeaveNum
	};
	window.top.showWaitingDlg(I18N.COMMON.wait , I18N.ACL.mdfingAclProp )
	$.ajax({
		url : '/epon/acl/modifyAclList.tv',data : params,cache:false,
		success : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.mdfAclPropOk )
			var pa = window.parent.getWindow("aclList").body.dom.firstChild.contentWindow;
			var listData = pa.listData,
				listStore = pa.listStore,
				listDataSplit = pa.listDataSplit,
				listGrid = pa.listGrid;
			var $length = listData.length;
			for(var p=0; p<$length; p++){
				if(listData[p][0] == aclIndex){
					listData[p][2] = $aclPriority;
					listData[p][4] = $desc;
					listDataSplit[p][2] = $aclPriority;
					listDataSplit[p][4] = $desc;
					listStore.loadData(listDataSplit);
					listGrid.getSelectionModel().selectRow(p,true);
					break;
				}
			}
			cancelClick();
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip , response.responseText);
		}
	});
}
function checkedAclId(){
	var aclIndexTemp = $("#aclIndex").val();
	$("#aclIndex").css("border","1px solid #8bb8f3");
	reg = /^([1-9][0-9]{0,3})+$/ig;
	if(aclIndexTemp=="" || aclIndexTemp==null || aclIndexTemp==undefined || !reg.exec(aclIndexTemp) || parseInt(aclIndexTemp)>4000){
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
	R.saveBt.setDisabled( false );
	if(!checkedDescr()){
		R.saveBt.setDisabled( true );
		$("#descr").focus();
	}
	if(!checkedAclId()){
		R.saveBt.setDisabled( true );
		$("#aclIndex").focus();
	}
}

$(function(){
	if(aclIndex > 0){
		$("#aclIndex").val(aclIndex);
		$("#descr").html(descr);
		R.saveBt.setDisabled( false );
	}
	$("#aclPriority").val( ${priority} )	
});

function cancelClick(){
	window.parent.closeWindow('editAcl');
}

</script>
</head>
<body class="openWinBody">


<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@ACL.eidtTip@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10 pT20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt w180">ACL ID@COMMON.maohao@</td>
	                 <td>
						<input id="aclIndex" maxlength=4 class="normalInput w200" value="${aclIndex}" disabled="disabled"/>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">@COMMON.desc@@COMMON.maohao@</td>
	                 <td>
						<textarea id=descr rows=3 style="width:196px; height:40px;" class="normalInput" onkeyup="keyup()" >description</textarea>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt withoutBorderBottom">@ACL.priority@@COMMON.maohao@</td>
	                 <td class="withoutBorderBottom">
						<select id="aclPriority" class="normalSel w60" >
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
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>