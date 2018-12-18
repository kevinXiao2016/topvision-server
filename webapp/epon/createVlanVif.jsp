<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
    import js.tools.ipText static
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var vlanIndex = '<s:property value = "vlanIndex"/>';
var vlanVifFlag = '<s:property value = "vlanVifFlag"/>';//用于标识页面跳转：1为主ip设置，2为新增子ip，3为修改子ip
//vlanVifFlag为3时的参数
var topOltVifSubIpSeqIdx = '<s:property value = "topOltVifSubIpSeqIdx"/>';
var topOltVifSubIpAddr = '<s:property value = "topOltVifSubIpAddr"/>';
var topOltVifSubIpMask = '<s:property value = "topOltVifSubIpMask"/>';

$(function(){
	if(vlanVifFlag == 1){
		var priIpInput = new ipV4Input("priIp", "span1");
		var priMaskInput = new ipV4Input("priMask", "span2");
	}
	if(vlanVifFlag == 2){
		var subIpInput = new ipV4Input("subIp", "span1");
		var subMaskInput = new ipV4Input("subMask", "span2");
	}
	if(vlanVifFlag == 3){
		var subIpInput = new ipV4Input("subIp", "span1");
		var subMaskInput = new ipV4Input("subMask", "span2");
		subIpInput.setValue(topOltVifSubIpAddr);
		subMaskInput.setValue(topOltVifSubIpMask);
		$("#action").html('<span><i class="miniIcoEdit"></i>@COMMON.modify@</span>');
	}
    setIpWidth("all", 150);
    setIpBgColor("all", "white");
});

function updateConfig(){
	if(vlanVifFlag == 1){
		var priIp = getIpValue("priIp");
		var priMask = getIpValue("priMask");
		if(!ipIsFilled('priIp') || !ipIsFilled('priMask')){
			return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.plsInputIp);
		}
		if(!checkedIpMask(priMask)){
			return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.maskEr);
		}
	    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.creatingVI , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/epon/vlan/createVlanVif.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&topOltVifPriIpAddr="+priIp+"&topOltVifPriIpMask="+priMask,
			method:"post",
			//async: false,
			success:function(response){
				if(response.responseText == "success"){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.createVIOk );
					window.parent.getFrame("entity-" + entityId).updataVlanVifFlag(vlanIndex);
					cancelClick();
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip,I18N.VLAN.createVIEr);
				}
			},failure:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.createVIEr );
	        }})
	}else if(vlanVifFlag == 2){
		var subIp = getIpValue("subIp");
		var subMask = getIpValue("subMask");
		if(!ipIsFilled('subIp') || !ipIsFilled('subMask')){
			return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.plsInputIp);
		}
		if(!checkedIpMask(subMask)){
			return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.maskEr);
		}
	    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.addingSubIP , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/epon/vlan/addVlanVifSubIp.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&topOltVifSubIpAddr="+subIp+"&topOltVifSubIpMask="+subMask,
			method:"post",
			//async: false,
			success:function(response){
				if(response.responseText == "success"){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addSubIPOk );
					loadVlanVifSubIp();
/* 					var a = window.parent.getWindow("setVlanVif").body.dom.firstChild.contentWindow.vlanVifSubIpGridData;
					var b = window.parent.getWindow("setVlanVif").body.dom.firstChild.contentWindow.vlanVifSubIpGridStore;
					var c = new Array();
					c.add(1);
					c.add(subIp);
					c.add(subMask);
					a.add(c);
					b.loadData(a); */
					cancelClick();
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip,I18N.VLAN.addSubIPEr);
				}
			},failure:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addSubIPEr );
	        }})
	}else if(vlanVifFlag == 3){
		var subIp = getIpValue("subIp");
		var subMask = getIpValue("subMask");
		if(!ipIsFilled('subIp') || !ipIsFilled('subMask')){
			return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.plsInputIp);
		}
		if(!checkedIpMask(subMask)){
			return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.maskEr);
		}
	    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.mdfingSubIP , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/epon/vlan/modifyVlanVifSubIp.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&topOltVifSubIpAddr="+subIp+"&topOltVifSubIpMask="+subMask+"&topOltVifSubIpSeqIdx="+topOltVifSubIpSeqIdx,
			method:"post",
			//async: false,
			success:function(response){
				if(response.responseText == "success"){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfSubIPOk );
					loadVlanVifSubIp();
					/* var a = window.parent.getWindow("setVlanVif").body.dom.firstChild.contentWindow.vlanVifSubIpGridData;
					var b = window.parent.getWindow("setVlanVif").body.dom.firstChild.contentWindow.vlanVifSubIpGridStore;
					alert(a);
					for(i =0;i<a.length;i++){
						if(a[i][0] == topOltVifSubIpSeqIdx){
							a[i][1] = subIp;
							a[i][2] = subMask;
						}
					}
					b.loadData(a); */
					cancelClick();
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip,I18N.VLAN.mdfSubIPEr);
				}
			},failure:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfSubIPEr );
	        }})
	}

}

function loadVlanVifSubIp(){
    $.ajax({
        url: '/epon/vlan/loadVlanVifSubIp.tv',
        type: 'POST',
        async: false,
        data: "entityId=" + entityId +"&vlanIndex="+vlanIndex+"&num=" + Math.random(),
        dataType:"json",
        success: function(json) {
			var a = window.parent.getWindow("setVlanVif").body.dom.firstChild.contentWindow.vlanVifSubIpGridData;
			var b = window.parent.getWindow("setVlanVif").body.dom.firstChild.contentWindow.vlanVifSubIpGridStore;
			a = new Array();
			for(i=0;i < json.length ; i++){
				a[i]= new Array();
				a[i][0] = json[i].topOltVifSubIpSeqIdx;
				a[i][1] = json[i].topOltVifSubIpAddr;
				a[i][2] = json[i].topOltVifSubIpMask;
			}
			b.loadData(a);
        }, error: function(json) {
    }, cache: false
    });
}

function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}

function cancelClick() {
	if(vlanVifFlag == 1){
	    window.parent.closeWindow('createVlanVif');
	}else if(vlanVifFlag == 2){
		window.parent.closeWindow('addVlanVifSubIp');
	}else if(vlanVifFlag == 3){
		window.parent.closeWindow('modifyVlanVifSubIp');
	}
}
</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">
				<s:if test="vlanVifFlag ==1">@VLAN.VIMasterIPSet@</s:if>
				<s:elseif test="vlanVifFlag ==2">@VLAN.VISubIPSet@</s:elseif>
				<s:elseif test="vlanVifFlag ==3">@VLAN.VISubIPSet@</s:elseif>
			</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="withoutBorderBottom">
						<s:if test="vlanVifFlag ==1">@VLAN.masterIP@:</s:if>
						<s:elseif test="vlanVifFlag ==2">@VLAN.subIpAddr@:</s:elseif> 
						<s:elseif test="vlanVifFlag ==3">@VLAN.subIpAddr@:</s:elseif> 
						<font color=red>*</font></td>
					<td class="withoutBorderBottom"><span id="span1"></span></td>
					<td class="withoutBorderBottom">
						<s:if test="vlanVifFlag ==1">@VLAN.masterIPMask@:</s:if>
						<s:elseif test="vlanVifFlag ==2">@VLAN.subIpMask@:</s:elseif> 
						<s:elseif test="vlanVifFlag ==3">@VLAN.subIpMask@:</s:elseif>
						<font color=red>*</font></td>
					<td class="withoutBorderBottom"><span id="span2"></span></td>
				</tr>
			</table>
		</div>
		
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	        <li>
	        	<a href="javascript:;" class="normalBtnBig" id="action"  onclick="updateConfig()"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a>
	        </li>
	    </ol>
		</div>
		
	</body>
</Zeta:HTML>