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
	//check Count input
	function checkCountInput() {
		var reg = /^\d+$/;
		var retriesvalue = Zeta$('pingCount').value.trim();
		
		if(retriesvalue == null || retriesvalue ==''){
			top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.CountInput@</b>'
		    });
			$("#pingCount").focus();
			/* window.parent.showMessageDlg("@sys.tip@","@sys.CountInput@",null,function(){
				$("#pingCount").focus();
			}); */
			return false;
		}else if (reg.exec($("#pingCount").val())
				&& parseInt($("#pingCount").val()) <= 65535
				&& parseInt($("#pingCount").val()) >= 1) {
			return true;
		} else {
			$("#pingCount").focus();
			return false;
		}
	}
	//check Timeout input
	function checkTimeoutInput() {	
		var reg = /^[0-9]\d*$/;
		var timeoutvalue = Zeta$('pingTimeout').value.trim();
		
		if(timeoutvalue == null || timeoutvalue ==''){
			top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.TimeoutInput@</b>'
		    });
			$("#pingTimeout").focus();
			/* window.parent.showMessageDlg("@sys.tip@","@sys.TimeoutInput@",null,function(){
				$("#pingTimeout").focus();
			}); */
			return false;
		}else if (reg.exec($("#pingTimeout").val())
				&& parseInt($("#pingTimeout").val()) <= 30000
				&& parseInt($("#pingTimeout").val()) >= 1) {
			return true;
		} else {
			$("#pingTimeout").focus();
			return false;
		}
	}
	
	//save Ping config
	function okClick() {
		if(checkCountInput() && checkTimeoutInput()){
			window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
			$.ajax({
				url : 'saveToolPingConfig.tv',
				type : 'POST',
				data : $("#pingConfigForm").serialize(),
				success : function(json) {
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
	<form id="pingConfigForm" name="pingConfigForm">
		<div class="openWinHeader">
		    <div class="openWinTip">@sys.setToolPingConfig@</div>
		    <div class="rightCirIco wheelCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT40">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="200">
							<label for="pingCount">@sys.Count@:</label> <!-- 重试次数 -->
		                </td>
		                <td>
		                    <input id="pingCount" name="pingCount"  value="${pingCount}" maxlength="5" toolTip="@sys.CountFocus@" class="normalInput w150" />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                   <label for="pingTimeout">@sys.Timeout@:</label>
		                </td>
		                <td>
							<input id="pingTimeout" class="normalInput w150"
							name="pingTimeout" value="${pingTimeout}" maxlength="5"
							toolTip="@sys.TimeoutFocus@" />(@sys.Unit@:ms)
		                </td>
		            </tr>
		        </tbody>
		    </table>
		    <div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			    <ol class="upChannelListOl pB0 pT80 noWidthCenter">
			        <li>
			            <a href="javascript:;" class="normalBtnBig" onclick="okClick()">
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
			                	<i class="miniIcoForbid"></i>
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