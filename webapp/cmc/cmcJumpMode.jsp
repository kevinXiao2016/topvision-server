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
            module cmc
            IMPORT js/tools/ipText
        </Zeta:Loader>
        <style type="text/css">
        	.ckBox{ position:relative; top:2px;}
        </style>
        <script type="text/javascript">
        	//直接跳转是1，代理跳转是2，如果没有读到值，默认给1;
        	var jumpMode = '${cmWebJumpModule}' == '' ? 1 : '${cmWebJumpModule}';
        	var natIp = '${natIp}';
        	
        	$(function(){
        		//更新界面布局，直接打开时不需要显示ip输入框;
        		updateLayout(jumpMode);
        		
        		//绑定radios;
        		initBindRadio();
        		
        		//设置正常的选择值和ip;
        		$(':radio[value='+ jumpMode +']').attr({checked: 'checked'});
        	}); //end document.ready;
        	//直接打开不需要显示ip
        	function updateLayout(num){
        		var $tbody = $('#tbody');
        		if(num == 1){
        			$tbody.css({visibility: 'hidden'});
        		}else{
        			$tbody.css({visibility: 'visible'});
        		}
        	}
        	function initBindRadio(){
        		$(':radio[name="ckBox"]').click(function(){
        			updateLayout($(this).val());
        		})
        	}
        	
            //保存;
            function saveClick() {
            	var $ipInput = $('#ipInput');
            	var v = $(':radio[name="ckBox"]:checked').val();
            	if(v == 2){
	            	if(!checkedIpValue($ipInput.val()) || !checkIsNomalIp($ipInput.val())){
	            		$ipInput.focus();
	            		return;
	            	}
            	}
            	top.showWaitingDlg('@COMMON.wait@', '@COMMON.saving@' , 'ext-mb-waiting');
            	$.ajax({
                    url: '/webproxy/configCmWebProxy.tv',
                    data: {
                    	cmWebJumpModule : v,
                    	natIp: (v==2) ? $ipInput.val() : ''
                    },
                    dataType: "text",
                    success: function(text) {
                    	window.top.closeWaitingDlg();
                    	if(text == 'success'){
                    		top.afterSaveOrDelete({
                   			 	title: '@COMMON.tip@',
                   			 	html: '<b class="orangeTxt">@platform/Common.saveSuc@</b>'
                   		 	 });
                    		cancelClick();
                    	}else{
                    		window.parent.showMessageDlg('@COMMON.tip@', '@platform/Common.saveFailed@');
                    	}
                    }, error: function(text) {
                    	window.parent.showMessageDlg('@COMMON.tip@', '@platform/Common.saveFailed@');
                	}, cache: false
                });
            }
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader" style="height:100px;">
            <div class="openWinTip">
                <p><b class="orangeTxt">Nat Server IP @text.suggestAddress@, @COMMON.ipDoNotUse@</b></p>
				<p>0.0.0.0 <span class="pL10">0.0.0.1~0.255.255.255</span></p>
				<p>127.0.0.0~127.255.255.255 (@COMMON.ipDoNotUse2@)<span class="pL10">224.0.0.0~239.255.255.255 (@COMMON.ipDoNotUse3@)</span></p> 
				<p>240.0.0.0~255.255.255.254 (@COMMON.ipDoNotUse4@)<span class="pL10">255.255.255.255</span></p>
            </div>
            <div class="rightCirIco wheelCirIco" style="top:25px;"></div>
        </div>
        <div class="edgeTB10LR20 pT40">
            <table id="mainTable" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                	<tr>
                        <td class="rightBlueTxt" width="190">@COMMON.required@@text.cmManagerJumpMode@@COMMON.maohao@</td>
                        <td width="80" class="noWrap">
                            <label><input value="1" type="radio" name="ckBox" class="ckBox mR2" checked="checked" />@text.openDirectly@</label>
                        </td>
                        <td>
                            <label><input value="2" type="radio" name="ckBox" class="ckBox mR2" />@text.openWebProxy@</label>
                        </td>
                    </tr>
                </tbody>
                <tbody id="tbody">
                	<tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="190">@COMMON.required@NAT Server IP@COMMON.maohao@</td>
                        <td colspan="2">
                        	<input id="ipInput" type="text" class="normalInput w200" toolTip="@text.mustIpAddress@<br>@text.suggestAddress@"  value="${natIp}" />
                        </td>
                    </tr>
                    
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT40 noWidthCenter">
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