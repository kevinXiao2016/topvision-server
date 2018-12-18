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
		    import js/jquery/nm3kPassword
			CSS css/white/disabledStyle
		</Zeta:Loader>
		<style type="text/css">
		    a.saveBtn {
				filter: alpha(opacity=40); /*IE滤镜，透明度40%*/
			    -moz-opacity: 0.4; /*Firefox私有，透明度40%*/
			    opacity: 0.4; /*其他，透明40%*/
				pointer-events: none;
			}
			#encryptMode{
				display: none;
			}
		</style>
        <script type="text/javascript">
        	var entityId = '${entityId}';
        	var onuIndex = '${onuIndex}';
	        var onuId = '${onuId}';
	  		var ssid = '${ssid}';
	  		var pageId = '${pageId}';
	  		var passwordField;
	  		var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	  		
	        $(document).ready(function() {
	    		passwordField = new nm3kPassword({
	    			id : "passwordField",
	    			renderTo : 'passwordTd',
	    			width : 122,
	    			value : '',
	    			firstShowPassword : true,
	    			disabled : true,
	    			maxlength : 64,
	    			toolTip : '@ONU.WAN.SSIDPasswordTip@'
	    		});
	    		passwordField.init();
	    		$.ajax({
	    			url : '/onu/getWanSsid.tv',
	    			type : 'POST',
	    			data : {
	    				onuId : onuId,
	    				ssid : ssid
	    			},
	    			dateType : 'json',
	    			success : function(json) {
	    				if (json) {
	    					$('#ssidName').val(json.ssidName);
	    					$('#passwordField').val(json.password);
	    					$('#encryptMode').val(json.encryptMode);
	    					changePasswordField(json.encryptMode);
	    				}
	    			},
	    			error : function() {
	    			},
	    			cache : false
	    		});
	    		authLoad();
	    	});
	        
	        function authLoad(){
	    		if(!operationDevicePower){
	    			$('a#saveBtn').addClass('saveBtn');
	    		}
	    	}
	        
	        function changePasswordField(encryptMode) {
	    		//TODO 常量表示业务相关参数
	    		if (encryptMode == null) {
	    			passwordField.setDisabled(true);
	    			passwordField.setValue('');
	    			return;
	    		}
	    		if (encryptMode == 0) {
	    			passwordField.setDisabled(true);
	    			passwordField.setValue('');
	    		} else if (encryptMode == 1) {//WEP 5个字符
	    			passwordField.setDisabled(false);
	    			passwordField.setMaxlength(5);
	    			passwordField.setToolTip('@ONU.WAN.SSIDPasswordTip@');
	    		} else {//其他密码模式
	    			passwordField.setDisabled(false);
	    			passwordField.setMaxlength(63);
	    			passwordField.setToolTip('@ONU.WAN.SSIDPasswordTip2@');
	    		}
	    	}
	        
	      	//验证
	    	function validate() {
	    		if ($('#ssidName').val().length > 31||$('#ssidName').val().length == 0) {
	    			$('#ssidName').focus();
	    			return false;
	    		}
	    		var encryptMode = $('#encryptMode').val();
	    		if (encryptMode == 0) {
	    		} else if (encryptMode == 1) {//WEP 5个字符
	    			if ($('#passwordField').val().length != 5) {
	    				$('#passwordField').focus();
	    				return false;
	    			}
	    		} else {
	    			if ($('#passwordField').val().length < 8 || $('#passwordField').val().length > 63) {
	    				$('#passwordField').focus();
	    				return false;
	    			}
	    		}
	    		return true;
	    	}
	      
	            //保存;
            function saveClick() {
            	if (!validate()) {
        			return;
        		}
        		var ssidName = $('#ssidName').val();
        		var password = $('#passwordField').val();
        		window.parent.showWaitingDlg("@COMMON.wait@", '@ONU.WAN.modify@SSID','waitingMsg', 'ext-mb-waiting');
        		$.ajax({
        			url : '/onulist/modifyWifiPassord.tv',
        			type : 'POST',
        			data : {
        				'onuWanSsid.entityId' : entityId,
        				'onuWanSsid.onuId' : onuId,
        				'onuWanSsid.ssid' : ssid,
        				'onuWanSsid.ssidName' : ssidName,
        				'onuWanSsid.password' : password,
        				'onuWanSsid.onuIndex' : onuIndex
        			},
        			dateType : 'json',
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
        					window.parent.showMessageDlg("@COMMON.tip@", "@ONU.WAN.save@@ONU.WAN.failed@");
        				}
        			},
        			error : function() {
        				window.parent.closeWaitingDlg();
        				window.parent.showMessageDlg("@COMMON.tip@", "@ONU.WAN.save@@ONU.WAN.failed@");
        			},
        			cache : false
        		});
            }
            //取消;
            function cancelClick() {
                top.closeWindow("showOnuWifiModifyView");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@ONU.modifySSidNamePassword@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT60">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">@ONU.WAN.name@@COMMON.maohao@</td>
                        <td>
                            <input class="normalInput w165" id="ssidName" maxlength=31 tooltip='@ONU.ssidNameTip@' />
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">@ONU.WAN.wirelssPassword@@COMMON.maohao@</td>
                        <td id="passwordTd"></td>
                        <td id="encryptMode"></td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT60 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" id="saveBtn" onclick="saveClick()">
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