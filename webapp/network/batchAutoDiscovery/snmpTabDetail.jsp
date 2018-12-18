<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module network
</Zeta:Loader>
<style type="text/css">
.v3setting{display: none;}
tr,td{text-align: left;}
tr,td:first-child{color:#0267B7; text-align: right;}
</style>
<script type="text/javascript">
var snmpTab = {
	id: null,
	name: null,
	readCommunity: null,
	writeCommunity: null,
	version: null,
	username: null,
	authProtocol: null,
	authPassword: null,
	privProtocol: null,
	privPassword: null
}, action = '${action}', snmpTabJson = '${snmpTabJson}';
var SNMPV3 = 3,
	SNMPV2 = 1,
	SNMPV1 = 0,
	DEFAULT_HEIGHT = 340,
	MODEV3_HEIGHT = 450;
function getQueryStringRegExp(name){
    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i"); 
    if (reg.test(location.href)){
    	return unescape(RegExp.$2.replace(/\+/g, " ")); 
    }
    return "";
};

function cancel(){
	window.top.closeWindow("snmpTabDetail");
}

$(function(){
	//绑定事件
	var thisWindow = window.parent.getWindow("snmpTabDetail");
	$("#version").bind('change',function(){
		var version = $("#version").val();
		if( version == SNMPV3 ){
			$(".v2setting").hide();
			$(".v3setting").show();
			thisWindow.setHeight( MODEV3_HEIGHT );
		}else{
			$(".v3setting").hide();
			$(".v2setting").show();
			thisWindow.setHeight( DEFAULT_HEIGHT );
		}
	});
	//加载数据
	window.action = getQueryStringRegExp('action');
	if(action==='modify'){
		snmpTab = $.parseJSON(snmpTabJson);
		//填充数据
		for(var key in snmpTab){
			$('#'+key).val(snmpTab[key]);
		}
		$("#version").trigger('change');
		$('#topHeaderTip').text('@batchTopo.batchHeaderModify@')
	}
	
	var saveConfig = function(){
		//获取数据
		for(var key in snmpTab){
			snmpTab[key] = $('#'+key).val();
		}
		//校验
		if(!validate()){
			return
		}
		
		var url = (action==='add') ? '/batchautodiscovery/addSnmpTab.tv' : '/batchautodiscovery/modifySnmpTab.tv';
		$.post(url, snmpTab,function(response){
			//刷新父页面的SNMP标签
			window.top.getActiveFrame().refreshSnmpTabs();
			cancel();
		});
	}
	
	function validate(){
		//校验标签名称,名称不能为空或包含英文字母下划线、数字外的字符,最多32位
		var name = snmpTab.name,
			nameReg = /^\w{1,32}$/;
		//校验标签名称
		if(snmpTab.name==null || !nameReg.test(name)){
			$('#name').focus();
			return false;
		}
		if(snmpTab.version == SNMPV2){
			//校验读共同体
			if(!snmpTab.readCommunity){
				$('#readCommunity').focus();
				return false;
			}
			//校验写共同体
			if(!snmpTab.writeCommunity){
				$('#writeCommunity').focus();
				return false;
			}
		}else if(snmpTab.version == SNMPV3){
			//校验用户名
			if(!snmpTab.username || !nameReg.test(snmpTab.username)){
				$('#username').focus();
				return false;
			}
			//校验认证密码
			if(snmpTab.authPassword.length<8 || snmpTab.authPassword.length>31){
				$('#authPassword').focus();
				return false;
			}
			//校验加密密码
			if(snmpTab.privPassword.length<8 || snmpTab.privPassword.length>31){
				$('#privPassword').focus();
				return false;
			}
		}
		return true;
	}
	
	$('#btCreate').bind('click', function(){
		saveConfig();
	})
})
</script>
</head>
<body class="openWinBody" >
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip" id="topHeaderTip">@batchTopo.batchHeader@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<form onsubmit="return false;">
			<input type="hidden" id="id"/>
			<table class="mCenter zebraTableRows" >
					<tr class="darkZebraTr">
						<td style="width: 200px;"><label for="name">@batchTopo.labelName@<font color=red>*</font></label></td>
						<td>
							<input id="name" maxlength="20" class="normalInput" style="width: 210px" tooltip="@batchTopo.labelNameTip@" />
						</td>
					</tr>
					<tr>
						<td><label for="snmpVersion">@batchTopo.snmpVersion@<font color=red>*</font></label></td>
						<td>
							<select id="version"  class="normalSel" style="width: 210px">
		                        <option value="1">SNMP V2C</option>
		                        <option value="3">SNMP V3</option>
		                    </select>
						</td>
					</tr>
					<tr class="darkZebraTr v2setting">
						<td><label for="community">@WORKBENCH/td.community@<font color=red>*</font></label></td>
						<td>
							<Zeta:Password width="210px" id="readCommunity" maxlength="32" tooltip="@WORKBENCH/onfocus.pleaseInputCommunity@" />
						</td>
					</tr>
					<tr class="v2setting">
						<td><label for="community">@WORKBENCH/td.rwCommunity@<font color=red>*</font></label></td>
						<td>
							<Zeta:Password width="210px" id="writeCommunity" name="writeCommunity" maxlength="32" tooltip="@WORKBENCH/onfocus.pleaseInputWriteCommunity@" />
						</td>
					</tr>
					<tr class="v3setting darkZebraTr">
						<td><label for="username">@WORKBENCH/td.username@<font color=red>*</font></label>
						</td>
						<td><input id="username" maxlength="32" class="normalInput w440"
							style="width: 210px;border:1px solid #8bb8f3;" tooltip="@WORKBENCH/onfocus.pleaseInputUserName@" />
						</td>
					</tr>
					<tr class=v3setting>
						<td ><label for="authProtocol">@WORKBENCH/td.authProtocol@<font color=red>*</font></label>
						</td>
						<td > 
							<select style="width: 210px;" class="normalSel" id="authProtocol"  onchange="authProtocolChange();">
			                        <option value="NOAUTH">NOAUTH</option>
			                        <option value="MD5">MD5</option>
			                        <option value="SHA">SHA</option>
			                </select>
						</td>
					</tr>
					<tr class="v3setting darkZebraTr">
						<td ><label for="authPass">@WORKBENCH/td.authPass@<font color=red>*</font></label>
						</td>
						<td>
							<Zeta:Password width="210px" id="authPassword" value="12345678" maxlength="31" disabled="false" tooltip="@WORKBENCH/onfocus.pleaseInputauthPass@"/>
						</td>
					</tr>
					<tr class="v3setting">
						<td><label for="privProtocol">@WORKBENCH/td.privProtocol@<font color=red>*</font></label>
						</td>
						<td >
							<select style="width: 210px;" class="normalSel" id="privProtocol" onchange="privProtocolChange();">
		                        <option value="NOPRIV">NOPRIV</option>
		                        <option value="CBC-DES">CBC-DES</option>
			                </select>
						</td>
					</tr>
					<tr class="v3setting darkZebraTr">
						<td ><label for="privPass">@WORKBENCH/td.privPass@<font color=red>*</font></label>
						</td>
						<td>
							<Zeta:Password width="210px" id="privPassword" value="12345678" maxlength="31" disabled="false" tooltip="@WORKBENCH/onfocus.pleaseInputprivPass@" />
						</td>
					</tr>
			</table>
		</form>
	</div>
	
	<!-- 第三部分，按钮组合 -->
	<Zeta:ButtonGroup>
		<Zeta:Button icon="miniIcoAdd" id="btCreate" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancel()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	<script type="text/javascript">
		$(function(){
			$("a").attr("tabindex",1000)
		})
	</script>
</body>
</Zeta:HTML>
