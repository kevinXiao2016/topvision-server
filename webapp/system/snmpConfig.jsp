<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
    import js.tools.ipText
</Zeta:Loader>
<head>
<script>
	//check SNMP port input
	function checkPortInput() {
		var reg = /^[0-9]\d*$/;
		var portValue = $.trim($("#snmpPort").val());
		
		if(portValue == null || portValue ==''){
			top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.snmpPortInput@</b>'
		    });
			//window.parent.showMessageDlg("@sys.tip@","@sys.snmpPortInput@",null,function(){
				$("#snmpPort").focus();
			//});
			return false;
		}else if (reg.exec($("#snmpPort").val())
				&& parseInt($("#snmpPort").val()) <= 65535
				&& parseInt($("#snmpPort").val()) >= 1) {
			return true;
		} else {
			$("#snmpPort").focus();
			return false;
		}
	}
	//check Retries input
	function checkRetriesInput() {
		var reg = /^[0-9]\d*$/;
		var retriesValue = $.trim($("#snmpRetries").val());
		
		if(retriesValue == null || retriesValue ==''){
			top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.RetriesInput@</b>'
		    });
			//window.parent.showMessageDlg("@sys.tip@","@sys.RetriesInput@",null,function(){
				$("#snmpRetries").focus();
			//});
			return false;
		}else if (reg.exec($("#snmpRetries").val())
				&& parseInt($("#snmpRetries").val()) <= 3
				&& parseInt($("#snmpRetries").val()) >= 0) {
			return true;
		} else {
			$("#snmpRetries").focus();
			return false;
		}
	}
	//check Timeout input
	function checkTimeoutInput() {
		var reg = /^[1-9]\d*$/;
		var timeoutValue = $.trim($("#snmpTimeout").val());
		
		if(timeoutValue == null || timeoutValue ==''){
			top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.TimeoutInput@</b>'
		    });
			//window.parent.showMessageDlg("@sys.tip@","@sys.TimeoutInput@",null,function(){
				$("#snmpTimeout").focus();
			//});
			return false;
		}else if (reg.exec($("#snmpTimeout").val())
				&& parseInt($("#snmpTimeout").val()) <= 30000
				&& parseInt($("#snmpTimeout").val()) >= 1000) {
			return true;
		} else {
			$("#snmpTimeout").focus();
			return false;
		}
	}
	
	function checkOidInput() {
		var reg = /^[1-9]\d*(\.\d+)+$/;
		var oidValue = $.trim($("#snmpConnectivityOid").val());
		
		if (reg.exec(oidValue)) {
			return true;
		} else {
			$("#snmpConnectivityOid").focus();
			return false;
		}
	}
	
	//save config
	function okClick() {
		if(checkPortInput() && checkRetriesInput() && checkTimeoutInput() && checkOidInput()){
			window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
			$.ajax({
				url : '/network/saveSnmpConfig.tv',
				type : 'POST',
				data : $("#snmpConfigForm").serialize(),
				success : function(json) {
					window.top.closeWaitingDlg();
					top.afterSaveOrDelete({
				      title: '@sys.tip@',
				      html: '<b class="orangeTxt">@sys.saved@</b>'
				    });
					//window.parent.showMessageDlg("@sys.tip@","@sys.saved@");
					cancelClick();
				},
				error : function(json) {
					window.top.showErrorDlg();
				},
				cache : false
			});
		}
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	Ext.onReady(function() {

	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<form id="snmpConfigForm" name="snmpConfigForm">
		<div class="openWinHeader">
	    <div class="openWinTip">@sys.setSnmpConfig@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <label for="snmpPort">@sys.portConfig@:</label>
	                </td>
	                <td>
						<input style="width: 150px;" id="snmpPort" class="normalInput"
						name="snmpPort" value="${snmpPort}" maxlength="5"
						toolTip="@sys.snmpPortFocus@" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <label for="snmpRetries">@sys.Retries@:</label>
	                </td>
	                <td>
						<input style="width: 150px;" id="snmpRetries" class="normalInput"
						name="snmpRetries" value="${snmpRetries}" maxlength="1"
						toolTip="@sys.RetriesFocus@" />
	                </td>
	            </tr>
	            <tr>
	            	<td class="rightBlueTxt">
	            		<label 	for="snmpTimeout">@sys.Timeout@:</label>
	            	</td>
	            	<td>
						<input style="width: 150px;" id="snmpTimeout" class="normalInput"
						name="snmpTimeout" value="${snmpTimeout}" maxlength="5"
						toolTip="@sys.snmpTimeoutFocus@" />
						(@sys.Unit@:ms)
	            	</td>
	            </tr>
	            <tr>
	            	<td class="rightBlueTxt">
	            		<label 	for="snmpConnectivityOid">@sys.connectOid@:</label>
	            	</td>
	            	<td>
						<input style="width: 150px;" id="snmpConnectivityOid" class="normalInput"
						name="snmpConnectivityOid" value="${snmpConnectivityOid}" maxlength="50"
						toolTip="@sys.snmpConnectivityOidFocus@" />
	            	</td>
	            </tr>
	        </tbody>
	    </table>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="okClick()"  id="snmpSubmit">
		                <span>
		                    <i class="miniIcoData">
		                    </i>
		                   	@sys.save@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
		                <span>
		                	<i class="miniIcoForbid">
		                    </i>
		                    @sys.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>	
	</form>
</body>
</Zeta:HTML>