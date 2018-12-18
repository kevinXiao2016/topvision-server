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
</Zeta:Loader>
<head>
<script>
	//check SNMP port input
	function checkPortInput() {
		var reg = /^[1-9]\d*$/;
		var portValue = $.trim($("#tcpPort").val());
		
		if (reg.exec($("#tcpPort").val())
				&& parseInt($("#tcpPort").val()) <= 65535
				&& parseInt($("#tcpPort").val()) >= 1) {
			return true;
		} else {
			$("#snmpPort").focus();
			return false;
		}
	}

	//check Timeout input
	function checkTimeoutInput() {
		var reg = /^[1-9]\d*$/;
		var timeoutValue = $.trim($("#tcpTimeout").val());
		
		if (reg.exec($("#tcpTimeout").val())
				&& parseInt($("#tcpTimeout").val()) <= 30000
				&& parseInt($("#tcpTimeout").val()) >= 1) {
			return true;
		} else {
			$("#tcpTimeout").focus();
			return false;
		}
	}
	
	//save config
	function okClick() {
		if(checkPortInput() && checkTimeoutInput()){
			window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
			$.ajax({
				url : '/connectivity/savTcpConnectivityConfig.tv',
				type : 'POST',
				data : $("#tcpConfigForm").serialize(),
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
	<form id="tcpConfigForm" name="tcpConfigForm">
		<div class="openWinHeader">
	    <div class="openWinTip">@sys.setTcpConnectConfig@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <label for="tcpPort">@sys.portConfig@:</label>
	                </td>
	                <td>
						<input style="width: 150px;" id="tcpPort" class="normalInput"
						name="tcpPort" value="${tcpPort}" maxlength="5"
						toolTip="@sys.tcpPortFocus@" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	            	<td class="rightBlueTxt">
	            		<label 	for="tcpTimeout">@sys.Timeout@:</label>
	            	</td>
	            	<td>
						<input style="width: 150px;" id="tcpTimeout" class="normalInput"
						name="tcpTimeout" value="${tcpTimeout}" maxlength="5"
						toolTip="@sys.tcpTimeoutFocus@" />
						(@sys.Unit@:ms)
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