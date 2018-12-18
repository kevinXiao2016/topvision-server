<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
            module onu
		    CSS css/white/disabledStyle
		    import js/jquery/nm3kPassword
        </Zeta:Loader>
        <style type="text/css">
			#ipModeSelect, #serviceModeSelect{
				display: none;
			}
		</style>
        <script type="text/javascript">
	        var entityId = '${entityId}';
	    	var onuIndex = '${onuIndex}';
	        var onuId = '${onuId}';
	        var connectId = '${connectId}';
	  		var pageId = '${pageId}';
	  		var passwordField;
	  		
	  		$(document).ready(function() {
	  			pppoePasswordField = new nm3kPassword({
	  				id : "pppoePasswordField",
	  				renderTo : 'pppoePasswordTd',
	  				width : 140,
	  				value : '',
	  				firstShowPassword : true,
	  				disabled : false,
	  				maxlength : 15,
	  				toolTip : '@ONU.WAN.PPPoEPasswordTip@'
	  			});
	  			pppoePasswordField.init();

	  			$.ajax({
	  				url : '/onu/loadWanConnection.tv',
	  				type : 'POST',
	  				data : {
	  					onuId : onuId,
	  					connectId : connectId
	  				},
	  				dateType : 'json',
	  				success : function(json) {
	  					if (json) {
	  						$('#pppoeUserName').val(json.pppoeUserName);
	  						$('#pppoePasswordField').val(json.pppoePassword);
	  						$('#ipModeSelect').val(json.ipMode);
	  						ipModeSelectChanged(json.ipMode);
	  					}
	  				},
	  				error : function() {
	  				},
	  				cache : false
	  			});
	  		})
	  		
	  		//IP分配模式,修改之后，联动
			function ipModeSelectChanged(ipMode) {
				if(ipMode == 3){
					//pppoe
					$('#pppoeUserName').removeAttr('disabled').removeClass("normalInputDisabled");
					pppoePasswordField.setDisabled(false);
				} else {
					//pppoe
				    $('#pppoeUserName').attr('disabled', true).addClass("normalInputDisabled").val("");
					pppoePasswordField.setDisabled(true);
					pppoePasswordField.setValue('');
				}
			}
	  		
	  		//验证
	  		function validate() {
	  			if ($('#pppoeUserName').val().length > 31||$('#pppoeUserName').val().length==0) {
  					$('#pppoeUserName').focus();
  					return false;
  				}
  				if ($('#pppoePasswordField').val().length > 15||$('#pppoePasswordField').val().length==0) {
  					$('#pppoePasswordField').focus();
  					return false;
  				}
	  			return true;
	  		}

	  		//保存按钮
	  		function saveClick() {
	  			if (!validate()) {
	  				return;
	  			}
	  			var pppoeUserName = $('#pppoeUserName').val();
	  			var pppoePassword = $('#pppoePasswordField').val();
	  			var ipMode = $('#ipModeSelect').val();

	  			window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.modify@WAN@ONU.WAN.connection@', 'waitingMsg','ext-mb-waiting');
	  			$.ajax({
	  				url : '/onulist/modifyWanPPPoEPassord.tv',
	  				type : 'POST',
	  				data : {
	  					'onuWanConnect.entityId' : entityId,
	  					'onuWanConnect.onuId' : onuId,
	  					'onuWanConnect.onuIndex': onuIndex,
	  					'onuWanConnect.connectId' : connectId,
	  					'onuWanConnect.pppoeUserName' : pppoeUserName,
	  					'onuWanConnect.pppoePassword' : pppoePassword,
	  					'onuWanConnect.ipMode' : ipMode
	  				},
	  				dateType : 'text',
	  				success : function(response) {
	  					window.parent.closeWaitingDlg();
	  					if(response == 'success'){
	  						top.afterSaveOrDelete({
	  							title : I18N.COMMON.tip,
	  							html : '<b class="orangeTxt">' + '@ONU.WAN.save@@ONU.WAN.success@' + '</b>'
	  						});
	  						window.parent.getFrame(pageId).refresh();
	  						window.parent.getFrame(pageId).reOnuLocate();
	  						cancelClick();
	  					}else{
	  						window.parent.showMessageDlg("@COMMON.tip@", '@ONU.WAN.save@@ONU.WAN.failed@');
	  					}
	  				},
	  				error : function() {
	  					window.parent.showMessageDlg("@COMMON.tip@", '@ONU.WAN.save@@ONU.WAN.failed@');
	  				},
	  				cache : false
	  			});
	  		}
	  		
            //取消;
            function cancelClick() {
                top.closeWindow("showOnuWanModifyView");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@ONU.modifyPPPoENamePassword@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT60">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">PPPoE@ONU.WAN.username@@COMMON.maohao@</td>
                        <td>
                            <input id="pppoeUserName" type="text" class="w180 normalInput normalInputDisabled" maxlength = 31 disabled="disabled" tooltip='@ONU.WAN.PPPoEUsernameTip@' />
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">PPPoE@ONU.WAN.password@@COMMON.maohao@</td>
                        <td id="pppoePasswordTd"></td>
                        <td id="ipModeSelect"></td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT60 noWidthCenter">
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
        </div>
    </body>
</Zeta:HTML>