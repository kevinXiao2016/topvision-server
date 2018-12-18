<%@ page language="java" contentType="text/html;charset=UTF-8" %>
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
    module cmc
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var snmpParam = ${snmpParamJson};
function initData(){
	$("#version").val(snmpParam.version);
	$("#privProtocol").val(snmpParam.privProtocol);
	$("#authProtocol").val(snmpParam.authProtocol);
}
function refreshClisk(){
	 location.reload();
}
Ext.onReady(function(){
	initData();
	versionChange()
})

function saveDataPolicy(){
        	var community = $("#community").val();
        	var writeCommunity = $("#writeCommunity").val();
        	var version = $("#version").val();
        	var userName = $("#userName").val();
        	var authProtocol = $("#authProtocol").val();
        	var authPassword = $("#authPassword").val();
        	var privProtocol = $("#privProtocol").val();
        	var privPassword = $("#privPassword").val();

        	var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/;
        	if(community.length<1){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.communityNotNull);
        		return false;
        	}
        	if(writeCommunity.length<1){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.communityNotNull);
        		return false;
        	}
        	if(community.length>32 || !reg.test(community)){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.communityNotLongerThan32);
        		return false;
        	}
        	if(writeCommunity.length>32 || !reg.test(writeCommunity)){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.communityNotLongerThan32);
        		return false;
        	}
        	
        	if(authPassword.length > 31 || authPassword.length < 8){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.authPwdTip);
        		return false;
        	}
        	if(privPassword.length > 31 || privPassword.length < 8){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.priPwdTip);
        		return false;
        	}
        	
        	if(userName.length > 32){
        		window.top.showMessageDlg(I18N.COMMON.tip, I18N.userNameTip);
        		return false;
        	}
			
        	
        	$.ajax({
      		  url: '/cmts/modifyCmtsSnmpParam.tv?community=' + community + '&writeCommunity=' + writeCommunity
      				  + '&version=' + version + '&cmcId=' + cmcId + '&userName=' + userName + '&authProtocol=' + authProtocol + '&authPassword=' + authPassword
      				+ '&privProtocol=' + privProtocol + '&privPassword=' + privPassword,
        	      type: 'post',
        	      success: function(response) {
        	    	  if(response == "success"){
	      	  	    		window.location.reload();
	      	  	    	top.afterSaveOrDelete({
	      	   				title: '@COMMON.tip@',
	      	   				html: '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
	      	   			});
	      	  	    	}else{
	      	  	    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updatefailure);
	      	  	    	}
      			}, error: function(response) {
      				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updatefailure);
      			}, cache: false
      		});
        }
function versionChange(){
	switch (Zeta$('version').value) {
    case '0':
    case '1':
        $(".v2setting").show();
        $(".v3setting").hide();
        break;
    case '3':
    	$(".v3setting").show();
        $(".v2setting").hide();
        break;
}
}

function authProtocolChanged() {
	var authProtocol = Zeta$('authProtocol').value;
	if(authProtocol == 'NOAUTH'){
		$('#privProtocol').val('NOPRIV');
		$('#privProtocol').attr("disabled",true);
		$("#authPassword").attr("disabled",true);
		$("#privPassword").attr("disabled",true);
	}else{
		$('#privProtocol').attr("disabled",false);
		$("#authPassword").attr("disabled",false);
		$("#privPassword").attr("disabled",false);
	}
}
</script>
</head>
<body class="whiteToBlack">
	<div id=header>
		<%@ include file="entity.inc"%>
	</div>
	<div class=formtip id=tips style="display: none"></div>
	<div class="edge10 pB0 clearBoth">
			<table class="dataTableRows zebra" width="100%" border="1"
				cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="6" class="txtLeftTh">@CMC.title.emssnmpinfo@</th>
					</tr>
				</thead>
			 <tbody>
				 <tr>
					 <td width=120 class="rightBlueTxt">@CMC.text.version@:
					 </td>
					 <td><select id="version" name="version" onchange="versionChange()" class="normalSel" 
                                style="width: 185px">
                       <option value="1" selected >SNMP V2C</option>
                       <option value="3">SNMP V3</option>
                    </select>
                    </td>
					 <td width=120 class="rightBlueTxt v2setting">@CMC.text.rocommunity@:
					 </td>
					 <td class="v2setting"><input id="community" type="text" class="vAlignMidden v2setting normalInput" style="width: 180px" value="${snmpParam.community}"
						tooltip="@communityTip@"/></td>
					 <td width=120 class="rightBlueTxt v2setting">@CMC.text.rwcommunity@:
					 </td>
					 <td class="v2setting"><input id="writeCommunity" type="text" class="vAlignMidden v2setting normalInput" style="width: 180px" value="${snmpParam.writeCommunity}"
						tooltip="@communityTip@"/></td>
	                 <td style='display: none;' width=120 class="rightBlueTxt v3setting">@SNMP.USERNAME@</td>
					<td style='display: none;' class="v3setting"><input style='display: none;width: 180px' id="userName" type="text" tooltip="@userNameTip@"
					value="${snmpParam.username}" class="vAlignMidden v3setting normalInput"/></td>
					<td style='display: none;' width=120 class="rightBlueTxt v3setting">@SNMP.AuthProtocol@</td>
					<td style='display: none;' class="v3setting">
					<select id="authProtocol" class="normalSel"
					onchange="authProtocolChanged();" style="width: 180px">
						<option value="NOAUTH">NOAUTH</option>
						<option value="MD5">MD5</option>
						<option value="SHA">SHA</option>
					</select></td> 
				</tr>
				<tr class="v3setting">
					<td width=120 class="rightBlueTxt v3setting">@SNMP.AuthPassword@</td>
					<td id="emsV3setting2" class=v3setting><Zeta:Password
						width="185" id="authPassword"
						tooltip="@authPwdTip@"
						value="${snmpParam.authPassword}" /></td>
					<td width=120 class="rightBlueTxt v3setting">@SNMP.EncryptioProtocol@</td>
					<td><select id="privProtocol" class="normalSel" style="width: 185px">
						<option value="NOPRIV">NOPRIV</option>
						<option value="CBC-DES">CBC-DES</option>
					</select></td>
					<td width=120 class="rightBlueTxt v3setting">@SNMP.TheEncryptedPassword@</td>
					<td><Zeta:Password width="185" id="privPassword"
						tooltip="@priPwdTip@"
						value="${snmpParam.privPassword}" /></td>
				</tr> 
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl noWidthCenter pT5 pB5">
						         <li><a  onclick="saveDataPolicy()" href="javascript:;" class="normalBtn"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
	</div>
</body>
</Zeta:HTML>