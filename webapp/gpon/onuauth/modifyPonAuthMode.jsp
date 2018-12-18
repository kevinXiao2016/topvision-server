<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    module OnuAuth
</Zeta:Loader>
<script type="text/javascript">
var entityId = ${entityId}
var ponIndex = ${ponIndex};
function closeClick() {
	window.parent.closeWindow('modifyPonAuthMode');
}

function save(){
	top.showConfirmDlg("@COMMON.tip@", "@EPON/onuAuth.tip.changeModeConfirm@", function(type) {
        if (type == 'no') {
        	return;
        }
        top.showWaitingDlg("@COMMON.wait@", "@EPON/onuAuth.tip.changingMode@");
        $.ajax({
            url : "/gpon/onuauth/modifyOnuAuthMode.tv",
            data: 'authMode=' + $("#authMode").val() + '&entityId=' + entityId + '&ponIndex=' + ponIndex,
            success : function() {
            	top.closeWaitingDlg();
            	top.nm3kRightClickTips({
    				title: "@COMMON.tip@", html: "@EPON/onuAuth.tip.changeModeSuc@"
    			});
            	closeClick();
            },
            error : function() {
                top.showMessageDlg("@COMMON.tip@", "@EPON/onuAuth.tip.changeModeFailed@");
            }
        });
    });
}

function renderPon(ponIndex){
	var ponIndex = parseInt(ponIndex / 256) + (ponIndex % 256);
	return ((ponIndex & 0xFF000000) >> 24) + '/' + ((ponIndex & 0xFF0000) >> 16);
}

$(function(){
	Zeta$('ponIndex').value = renderPon(ponIndex);
	$("#authMode").val(${authMode});
})

</script>
</head>
<body class="openWinBody">
<div class="formtip" id="tips" style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@EPON/onuAuth.modifyAuthMode@</div>
	    <div class="rightCirIco pageCirIco"></div> 
	</div>
	<div class="edgeTB10LR20 pT30">
	    <table class="zebraTableRows" id="mainTb">
	    	<tbody>
	            <tr>
	                <td class="rightBlueTxt w160">@EPON/onuAutoUpg.he.ponPort@@COMMON.maohao@</td>
	                <td>
	                	<input class="normalInput modifiedFlag w220 macClass" id="ponIndex" disabled/>
	                </td>
	            </tr>	 
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt  w160">@OnuAuth.authMode@@COMMON.maohao@</td>
	                <td>
	                	<select id="authMode" name="authMode" class="normalSel" style="width: 220px;"">
							<option value="1">@OnuAuth.snAuth@</option>
	                        <option value="2">@OnuAuth.snPassAuth@</option>
	                        <option value="3">@OnuAuth.loidAuth@</option>
	                        <option value="4">@OnuAuth.loidPassAuth@</option>
	                        <option value="5">@OnuAuth.passAuth@</option>
	                        <option value="6">@OnuAuth.autoAuth@</option>
	                        <option value="7">@OnuAuth.mixAuth@</option>
						</select>
	                </td>
	            </tr>	
	       </tbody>
	    </table>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id="saveButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@COMMON.modify@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>