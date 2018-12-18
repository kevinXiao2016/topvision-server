<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/include/nocache.inc"%>
<%@ include file="/include/cssStyle.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
var cmcId = '${cmcId}';
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function saveClick(){
	var vlanId = $("#vlanId").val().trim();
	if(!checkVlanId()){
		return Zeta$("vlanId").focus();
	}
	showWaitingDlg('@COMMON.wait@', '@VLAN.addingVlan@' , 'ext-mb-waiting');
	$.ajax({
        url: '/cmcVlan/addCmcVlan.tv',
        data: "cmcId=" + cmcId +"&topCcmtsVlanIndex=" + vlanId +"&num=" + Math.random(),
        dataType:"text",
        success: function(text) {
        	if(text == 'vlanExist'){
        		 window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.exist@',vlanId));
        	}else{
        		 //window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.addSuc@')
        		 top.afterSaveOrDelete({
        				title: '@COMMON.tip@',
        				html: '<b class="orangeTxt">@VLAN.addSuc@</b>'
        		 }); 
        		 window.top.closeWaitingDlg();
        		 cancelClick();
        	}
        }, error: function(text) {
        	window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.addVlanFail@')
    }, cache: false
    });
}
function checkVlanId(){
	var reg1 = /^([0-9])+$/;
	var vlanIndex = $("#vlanId").val().trim();
	if(!reg1.test(vlanIndex)){
		return false;
	}
	if(vlanIndex == "" || vlanIndex == null || parseInt(vlanIndex) < 1 || parseInt(vlanIndex) > 4094){
		return false;
	}
	return true;
}	
function cancelClick() {
    window.parent.closeWindow('addCmcVlan');
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@VLAN.vlanIdTip@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20 pT60">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt withoutBorderBottom" width="200">
	                    VLAN ID:
	                </td>
	                <td class="withoutBorderBottom">
	                    <input type=text id="vlanId" name="vlanId" value="" maxlength=4 toolTip='@VLAN.vlanIdTip@'
						class="normalInput"  style="width: 180px;" />
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a onclick="saveClick()" id=saveBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a onclick="cancelClick()" id=cancelBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>


	
</body>
</Zeta:HTML>