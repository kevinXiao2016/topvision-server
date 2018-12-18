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
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = ${entityId}
var ponIndex = ${ponIndex};
var authType = ${authType};
function closeClick() {
	window.parent.closeWindow('modifyPonAuthMode');
}

function save(){
    var authType = Zeta$('authMode').value
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAuth.tip.changeModeConfirm, function(button, text) {
        if (button == "yes") {
        	top.showWaitingDlg("@COMMON.wait@", "@EPON/onuAuth.tip.changingMode@");
            $.ajax({
                url:'/epon/onuauth/modifyPonAuthMode.tv',
                type:'POST',
                data:{entityId: entityId, ponIndex: ponIndex, authType:authType},
                dateType:'json',
                success:function(response) {
                	top.closeWaitingDlg();
                    if (response == "success") {
                    	top.afterSaveOrDelete({
               				title: I18N.COMMON.tip,
               				html: '<b class="orangeTxt">' + I18N.onuAuth.tip.changeModeSuc + '</b>'
               			});
                    	window.parent.getFrame("onuAuthManage").refreshOltGrid();
                    	
                    	closeClick();
                    } else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAuth.tip.changeModeFailed);
                    }
                },
                error:function() {
                },
                cache:false
            });
        }
    });
}

function renderPon(ponIndex){
	var ponIndex = parseInt(ponIndex / 256) + (ponIndex % 256);
	return ((ponIndex & 0xFF000000) >> 24) + '/' + ((ponIndex & 0xFF0000) >> 16);
}

$(function(){
	Zeta$('ponIndex').value = renderPon(ponIndex);
	Zeta$('authMode').value = authType;
})

</script>
</head>
<body class="openWinBody">
<div class="formtip" id="tips" style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@onuAuth.modifyAuthMode@</div>
	    <div class="rightCirIco pageCirIco"></div> 
	</div>
	<div class="edgeTB10LR20 pT30">
	    <table class="zebraTableRows" id="mainTb">
	    	<tbody>
	            <tr>
	                <td class="rightBlueTxt w160">@onuAutoUpg.he.ponPort@:</td>
	                <td>
	                	<input class="normalInput modifiedFlag w220 macClass" id="ponIndex" disabled/>
	                </td>
	            </tr>	 
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt  w160">@onuAuth.ponMode@:</td>
	                <td>
	                	<select id="authMode" name="authMode" class="normalSel" style="width: 220px;"">
							<option value="1">@onuAuth.autoMode@</option>
							<option value="2">@onuAuth.mac@</option>
							<option value="3">@onuAuth.mix@</option>
							<option value="4">@onuAuth.sn@</option>
							<option value="5">@onuAuth.snPwdMode@</option>
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