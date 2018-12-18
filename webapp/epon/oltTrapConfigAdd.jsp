<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module epon
</Zeta:Loader>
<head>
	<title>@SERVICE.addTrapCfg@</title>
	<script type="text/javascript" src="/js/tools/ipText.js"></script>
	<script type="text/javascript">
		var entityId = <%=request.getParameter("entityId")%>;
		function addTrapServer() {
			var trapServerIp = getIpValue('newIp');
			var trapServerName = $('#trapServerName').val();
			var trapPort = $('#trapPort').val();
			var trapCommunity = $('#trapCommunity').val();
			// IP地址有效性验证
			if (!ipIsFilled("newIp")) {
				window.top.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.ipNotNULL)
				return
			}
			
			// ip校验
			if (!top.IpUtil.isValidDeviceIp(trapServerIp)) {
				return window.top.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.ipNotBe , trapServerIp));
			}
			var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d,;!#*$^=])+$/ig;
			if(trapServerName != null || trapServerName !=""){
				if(!reg.exec(trapServerName)){
					$('#trapServerName').focus()
					return
				}
			}else{
				trapServerName = trapServerIp
			}
			
			//服务器名称是否重复验证
			var trapConfigStore = window.parent.getFrame("oltTrapConfig").trapConfigStore;
			var repeatFlag = false;
			trapConfigStore.each(function(record){
				if(record.data.eponManagementAddrName == trapServerName){
					repeatFlag = true;
					return false;
				}
			});
			if(repeatFlag){
				window.top.showMessageDlg(I18N.COMMON.tip,I18N.SERVICE.trapNameRepeat,null,function(){
					$('#trapServerName').focus();
				});
				return;
			}
			
			// 端口号有效性验证
			if(isNaN(trapPort)){
				$('#trapPort').focus();
				return;
			}
			if(trapPort != 162){
				if (parseInt(trapPort) < 1025 || parseInt(trapPort)> 65534) {
					$('#trapPort').focus();
					return;
				}
			}
			
			// 共同体名有效性验证
			var reg = /^[a-zA-Z\d\~\!\@\#\$\%\^\&\*\+\;\,\?\=\|\<\>\`\{\}\-_\'\"\[\]()\/\.:\\]{1,63}$/;
			if (trapCommunity == '' || !reg.test(trapCommunity) || trapCommunity.length > 63 ) {
				$('#trapCommunity').focus();
				return;
			}
			
			//设备最多只支持20个trap配置
			if(trapConfigStore.getCount() < 20){
				window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.addingTrapCfg , 'ext-mb-waiting');
				Ext.Ajax.request({
					url: '/epon/alert/addOltTrapConfig.tv?r=' + Math.random(),
					params: {
						entityId: entityId,
						trapServerIp: trapServerIp,
						trapNameString:trapServerName,
						trapPort: trapPort,
						trapCommunity: trapCommunity
					},
					success: function(response) {
						var json = Ext.decode(response.responseText);
						if (json && json.message) {
							if(json.message == "repeat"){
								window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.trapCfged)
								var parent = window.parent.getWindow("oltTrapConfig").body.dom.firstChild.contentWindow;
								parent.focusFlag = trapServerIp + "#" + trapPort;
								parent.loadGrid();
								cancelClick();
								return;
							}
							window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
						} else {
							top.closeWaitingDlg();
							 top.nm3kRightClickTips({
				   				title: I18N.COMMON.tip,
				   				html: I18N.SERVICE.addTrapCfgOk
				   			 });
							//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addTrapCfgOk)
		    				window.parent.getWindow("oltTrapConfig").body.dom.firstChild.contentWindow.trapConfigStore.load();
		    				cancelClick();
						}
					},
					failure: function() {
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addTrapCfgError)
					}
				});
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.trapNumLimit);
				return;
			}
		}
		function cancelClick() {
			window.parent.closeWindow('oltTrapConfigAdd');
		}
		Ext.onReady(function(){
			var newIp = new ipV4Input("newIp", "span1");
			newIp.width(200);
			setTimeout(function(){ipFocus("newIp",1);}, 500);
		});
	</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader" style="height:100px;">
		<div class="openWinTip" style="margin-right:50px;">
            <p><b class="orangeTxt">@COMMON.ipDoNotUse@</b></p>
            <p>0.0.0.0 <span class="pL10">0.0.0.1~0.255.255.255</span></p>
            <p>127.0.0.0~127.255.255.255 (@COMMON.ipDoNotUse2@)<span class="pL10">224.0.0.0~239.255.255.255 (@COMMON.ipDoNotUse3@)</span></p> 
            <p>240.0.0.0~255.255.255.254 (@COMMON.ipDoNotUse4@)<span class="pL10">255.255.255.255</span></p>
        </div>
		<div class="rightCirIco wheelCirIco"></div>		
	</div>
	<div class="edgeTB10LR20 pT40">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="rightBlueTxt" width="300">@SERVICE.trapServerIp@:</td>
				<td><span id="span1"></span></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@SERVICE.trapServerName@:</td>
				<td>
					<input id="trapServerName" type="text" value=""
					class="normalInput w200" toolTip='@SERVICE.rangeaz@'
					 maxlength="32"/>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@SERVICE.portNum2@:</td>
				<td>
					<input id="trapPort" type="text" value="162" 
					class="normalInput w200"
					toolTip='@SERVICE.range65534@' maxlength="5" />
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@SERVICE.commity@:</td>
				<td>
					<input id="trapCommunity" type="text" value="public"
					class="normalInput w200"
					toolTip='@SERVICE.range63@' />
				</td>
			</tr>			
		</table>
		<ol class="upChannelListOl pB0 pT80 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig" onclick="addTrapServer()"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@BUTTON.cancel@</span></a></li>			
		</ol>
	</div>
	
</body>
</Zeta:HTML>