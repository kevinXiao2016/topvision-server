<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library zeta
	library jquery
	module mobile
</Zeta:Loader>
<style type="text/css">
.readonlyInput {
	background-color: #DCDCDC;
}
#formChanged input[type="text"]{
	width: 380px;
	padding-left: 5px;
}
</style>

<script type="text/javascript">

	function cancelClick() {
		window.top.closeWindow('defaultPowerLevelConfigDlg');
	}
		
	function saveClick() {
		var object = Zeta$('defaultPowerLevel').value.trim();
		if (object == null || object.length <1 ) {
			window.top.showMessageDlg('@RECYLE.tip@', '@defaultPowerLevelNotNull@');
			return;
		}
		var p = parseFloat(object);
		if (p < 60 || p > 90) {
			window.top.showMessageDlg('@RECYLE.tip@',  '@defaultPowerLevelError@');
			return;
		}

		
		$.ajax({
			url : '/mobile/modifyDefaultPowerLevel.tv',
			type : 'post',
			data : jQuery(formChanged).serialize(),
			success : function(response) {
				top.afterSaveOrDelete({
	   				title: '@RECYLE.tip@',
	   				html: '<b class="orangeTxt">' + '@text.modifySuccessTip@' + '</b>'
	   			});
				cancelClick();
			},
			error : function(response) {
				window.parent.showMessageDlg('@RECYLE.tip@',
						'@text.modifyFailureTip@');
			},
			cache : false
		});
			
		
	}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20 pT20">
		<form name="formChanged" id="formChanged">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt" width="120">
							<span style="color:red;">*</span>
							@defaultPowerLevel@:
						</td>
						<td>							
							<input class="normalInput" type="text" id="defaultPowerLevel" name="defaultPowerLevel"
							value='${defaultPowerLevel}'
							toolTip='@defaultPowerLevelOrder@' />
						</td>
						<td>
							dBuV
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
    	<ol class="upChannelListOl pB0 pT30 noWidthCenter">
    		<li>
    			<a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
    				<span><i class="miniIcoData"></i>@COMMON.save@</span>
    			</a>
    		</li>
         	<li>
         		<a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
         			<span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
         		</a>
         	</li>
		</ol>
	</div>
</body>
</Zeta:HTML>
