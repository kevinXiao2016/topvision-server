<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<head>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
	import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
	//If stopUserWhenErrors is not selected, disable lockTime
	function showLockTime(){
		if(Zeta$('stopUserWhenErrors').checked){
			Zeta$('stopUserWhenErrorNumber').disabled = false;
			//Zeta$('lockTime').disabled = false;
		}else{
			Zeta$('stopUserWhenErrorNumber').disabled = true;
			//Zeta$('lockTime').disabled = true;
		}
	}
	
	//check lockTime inut
	function checkLockTime(){
		var reg = /^[1-9]\d*$/;
		var value = Zeta$('lockTime').value.trim();
		if(value == null || value ==''){
			window.parent.showMessageDlg("@sys.tip@","@sys.lockTimeInput@",null,function(){
				Zeta$('lockTime').focus();
			});
			return false;
		}else if (reg.exec(value)
				&& parseInt(value) <= 720
				&& parseInt(value) >= 15) {
			return true;
		} else {
			Zeta$('lockTime').focus();
			return false;
		}
	}
	//save the config
	function okClick() {
		var data = {};
		//data.allowIpBindLogon = Zeta$('allowIpBindLogon').checked;
		//data.allowRepeatedlyLogon = Zeta$('allowRepeatedlyLogon').checked;
		data.checkPasswdComplex = true;//密码一定是6-16位，已经去除原来1-16位所以一定是true;leexiang_20141121;
		data.stopUserWhenErrors = Zeta$('stopUserWhenErrors').checked;
		data.stopUserWhenErrorNumber =  $("#stopUserWhenErrorNumber").val();
		if(data.stopUserWhenErrors){
			/*if(!checkLockTime()){
				return;
			}*/
			//data.lockTime = Zeta$('lockTime').value;
		}
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@")
		$.ajax({
			url : '../param/updateSecurityCenter.tv',
			type : 'POST',
			data : data,
			dataType:"plain",
			success : function() {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
			      title: '@COMMON.tip@',
			      html: '<b class="orangeTxt">@sys.logonSaved@</b>'
			    });
				//window.parent.showMessageDlg("@sys.tip@","@sys.logonSaved@");
				cancelClick();
			},
			error : function() {
				window.top.closeWaitingDlg();
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	Ext.onReady(function() {
		Zeta$('stopUserWhenErrorNumber').selectedIndex = <s:property value="stopUserWhenErrorNumber"/> - 1;
		//If stopUserWhenErrors is not selected, disable lockTime
		showLockTime();
	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">	
	    <div class="openWinTip">@resources/SYSTEM.systemAccessStrategy@</div>	
	    <div class="rightCirIco flagCirIco"></div>	
	</div>
	<div class="edgeTB10LR20 pB0">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <!-- <tr>
	             	 <td width="60"></td>
	                 <td>
	                 	<input id="allowIpBindLogon" type=checkbox
						<s:if test="allowIpBindLogon">checked</s:if> /><label
						for="allowIpBindLogon">@sys.allowIpBindLogon@</label>
	                 </td>
	             </tr>
	             <tr>
	             	<td></td>
	                 <td>
						<input id="allowRepeatedlyLogon" type=checkbox
						<s:if test="allowRepeatedlyLogon">checked</s:if> /><label
						for="allowRepeatedlyLogon">@sys.multiUser@</label>
	                 </td>
	             </tr>
	              -->
	             <!-- <tr>
	             	<td></td>
	             	<td>
	             		<input id="checkPasswdComplex" type=checkbox
						<s:if test="checkPasswdComplex">checked</s:if> /><label
						for="checkPasswdComplex">@sys.pwdChk@</label>
	             	</td>
	             </tr> -->
	             <tr class="darkZebraTr">
	             	<td></td>
	             	<td>
	             		<input id="stopUserWhenErrors" type=checkbox
						<s:if test="stopUserWhenErrors">checked</s:if> onClick = "showLockTime()" />
						<label>@sys.retryNumber@&nbsp;
						<select
							id="stopUserWhenErrorNumber">
								<%
								    for (int i = 1; i < 6; i++) {
								%>
								<option value="<%=i%>"><%=i%></option>
								<%
								    }
								%>
						</select>@sys.disableUser@</label>
	             	</td>
	             </tr>
	             <!--<tr>
	             	<td></td>
	             	<td>
	             		<label for="lockTime">&nbsp;&nbsp;&nbsp;&nbsp;@sys.lockTime@: </label> 
	             		<input type="text" id="lockTime" name="lockTime" value="${lockTime}" maxlength="3" toolTip="@sys.lockTimeFocus@" class="normalInput w100" />
						<%-- <Zeta:TextInput type = "text" id = "lockTime" name="lockTime" value="${lockTime}" maxlength="3" tooltip="@sys.lockTimeFocus@" /> --%>
						&nbsp;@sys.minutes@
	             	</td>
	             </tr>-->
	         </tbody>
	     </table>
	</div>
	
	<%-- <Zeta:ButtonGroup>
			<Zeta:Button onClick="okClick()" icon="miniIcoSaveOK">@COMMON.ok@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup> --%>
	
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT30 noWidthCenter">
	        <li>
	        	<a href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoData"></i>@sys.save@</span></a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a>
	        </li>
	    </ol>
	</div>
	
</body>
</Zeta:HTML>