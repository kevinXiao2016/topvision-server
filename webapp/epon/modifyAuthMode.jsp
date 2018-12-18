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
	plugin Nm3kDropdownTree
    module epon
    import js/jquery/Nm3kTabBtn
    import gpon.javascript.PonSelector
</Zeta:Loader>
<script type="text/javascript">
var entityId = ${entityId}
var DISPLAY_PON_MODE;
function closeClick() {
	window.parent.closeWindow('modifyAuthMode');
}

function save(){
	if(DISPLAY_PON_MODE == @{GponConstant.PORT_TYPE_GPON}@){
		return batchModifyGponOnuAuthMode();
	}
	var ponIndexs = ponSelector.getSelectedIndexs();
	var authType = Zeta$('authMode').value
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAuth.tip.changeModeConfirm, function(button, text) {
        if (button == "yes") {
        	window.parent.showWaitingDlg("@COMMON.wait@", '@onuAuth.tip.changingMode@',
    				'waitingMsg', 'ext-mb-waiting');
            $.ajax({
                url:'/epon/onuauth/modifyAuthMode.tv',
                type:'POST',
                data:{entityId: entityId, ponIndexs: ponIndexs, authType:authType},
                dataType:'text',
                success:function(response) {
                	window.parent.closeWaitingDlg();
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
function batchModifyGponOnuAuthMode(){
	var ponIndexs = ponSelector.getSelectedIndexs();
	var authType = Zeta$('authMode').value
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAuth.tip.changeModeConfirm, function(button, text) {
        if (button == "yes") {
        	window.parent.showWaitingDlg("@COMMON.wait@", '@onuAuth.tip.changingMode@');
            $.ajax({
                url:'/gpon/onuauth/batchModifyGponOnuAuthMode.tv',
                data:{entityId: entityId, ponIndexs: ponIndexs, authMode: (authType-10) },
                dataType:'text',
                success:function(response) {
                	window.parent.closeWaitingDlg();
                	if (response == "success") {
                       	top.afterSaveOrDelete({
                			title: I18N.COMMON.tip,
                			html: '<b class="orangeTxt">' + I18N.onuAuth.tip.changeModeSuc + '</b>'
                  		});
                       	window.parent.getFrame("onuAuthManage").refreshOltGrid();
                       	closeClick();
                	}else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAuth.tip.changeModeFailed);
                    }
                },
                error:function() {
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAuth.tip.changeModeFailed);
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
	var tab1 = new Nm3kTabBtn({
	    renderTo:"putTab",
	    callBack:"tabClick",
	    selectedIndex : 0,
	    tabs:["GPON","EPON"]
	});
	tab1.init();
	WIN.ponSelector = new PonSelector({
		entityId : @{entityId}@,
		subWidth : 180,
		subHeight: 160,
		renderTo : "pon_tree"
	});
	tabClick(0);
});
function tabClick(index){
	var eponStr = [
			'<option value="1" class="epon-option">@onuAuth.autoMode@</option>',
			'<option value="2" class="epon-option">@onuAuth.mac@</option>',
			'<option value="3" class="epon-option">@onuAuth.mix@</option>',
			'<option value="4" class="epon-option">@onuAuth.sn@</option>',
			'<option value="5" class="epon-option">@onuAuth.snPwdMode@</option>'
	    ].join(''),
		gponStr = [
			'<option value="11" class="gpon-option">@OnuAuth/OnuAuth.snAuth@</option>',
			'<option value="12" class="gpon-option">@OnuAuth/OnuAuth.snPassAuth@</option>',
			'<option value="13" class="gpon-option">@OnuAuth/OnuAuth.loidAuth@</option>',
			'<option value="14" class="gpon-option">@OnuAuth/OnuAuth.loidPassAuth@</option>',
			'<option value="15" class="gpon-option">@OnuAuth/OnuAuth.passAuth@</option>',
			'<option value="16" class="gpon-option">@OnuAuth/OnuAuth.autoAuth@</option>',
			'<option value="17" class="gpon-option">@OnuAuth/OnuAuth.mixAuth@</option>'
	    ].join('');
	if(index == 0){
		DISPLAY_PON_MODE = @{GponConstant.PORT_TYPE_GPON}@;
		//$("#authMode .epon-option").hide();
		//$("#authMode .gpon-option").show();
		$("#authMode").html(gponStr);
		ponSelector.render({type:@{GponConstant.PORT_TYPE_GPON}@});
		$("#authMode").val(11);
	}else{
		DISPLAY_PON_MODE = @{GponConstant.PORT_TYPE_EPON}@;
		//$("#authMode .epon-option").show();
		//$("#authMode .gpon-option").hide();
		$("#authMode").html(eponStr);
		ponSelector.render({type:@{GponConstant.PORT_TYPE_EPON}@});
		$("#authMode").val(1);
	}
}




</script>
<style type="text/css">
/* 用css因此下拉弹出树底部，后期需扩展那个控件 */
.folderTree-footer{ display:none;}

</style>
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
		    	   <td class="rightBlueTxt w130">@OnuAuth/OnuAuth.ponType@@COMMON.maohao@</td>
		    	   <td id="putTab">
					</td>
				</tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt w130">@onuAutoUpg.he.ponPort@@COMMON.maohao@</td>
	                <td class="w220"><div id="pon_tree" style="width: 222px;"></div></td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">@onuAuth.authMode@@COMMON.maohao@</td>
	                <td>
	                	<select id="authMode" name="authMode" class="normalSel" style="width: 222px;">
							
						</select>
	                </td>
	            </tr>	
	       </tbody>
	    </table>
	</div>
	<div class="edgeTB10LR20 pT50">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id="saveButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@COMMON.edit@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>