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
var SNMPV3 = 3,
	SNMPV2 = 1,
	SNMPV1 = 0,
	DEFAULT_HEIGHT = 340,
	MODEV3_HEIGHT = 450;

var pageAction = "<%= request.getParameter("pageAction") %>";
var snmpName = "<%= request.getParameter("snmpName") %>";

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
};
	
function cancel(){
	window.top.closeWindow("popBatchEntityDetail");
}

$(function(){
	//绑定切换事件
	var thisWindow = window.parent.getWindow("popBatchEntityDetail");
	var p = window.parent.getWindow("popBatchEntity").body.dom.firstChild.contentWindow,
		snmpList = p.snmpList;
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
	if(pageAction == 'modify'){
		//获取对应的snmp数据
		for(var i=0; i<snmpList.length; i++){
			if(snmpList[i].name == snmpName){
				snmpTab = snmpList[i];
				break;
			}
		}
		for(var key in snmpTab){
			$('#'+key).val(snmpTab[key]);
		}
		$("#version").trigger('change');
		$('#headerTip').text('@batchTopo.editlabel@');
	}
	
	//为保存绑定事件
	$('#btCreate').bind('click', function(){
		saveConfig();
	})
	
	function saveConfig(){
		//获取数据
		for(var key in snmpTab){
			snmpTab[key] = $('#'+key).val();
		}
		//校验
		if(!validate()){
			return
		}
		if(pageAction == 'add'){
			//如果是添加
			//判断名称有没有重复的
			for(var i=0; i<snmpList.length; i++){
				if(snmpList[i].name == snmpTab.name){
					return window.parent.showMessageDlg("@COMMON.tip@","@batchTopo.checkLabelName@");
				}
			}
			//添加
			snmpTab.id = Math.random();
			snmpList.push(snmpTab);
			p.refreshSnmpTabs();
		}else if(pageAction == 'modify'){
			//如果是修改
			//先判断名称有没有重复的
			for(var i=0; i<snmpList.length; i++){
				if(snmpList[i].id != snmpTab.id && snmpList[i].name == snmpTab.name){
					return window.parent.showMessageDlg("@COMMON.tip@","@batchTopo.checkLabelName@");
				}
			}
			for(var i=0; i<snmpList.length; i++){
				if(snmpList[i].id == id){
					snmpList[i] = snmpTab;
					break;
				}
			}
			p.refreshSnmpTabs();
		}
		cancel();
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
			if(!snmpTab.username){
				$('#username').focus();
				return false;
			}
			//校验认证密码
			if(!snmpTab.authPassword){
				$('#authPassword').focus();
				return false;
			}
			//校验加密密码
			if(!snmpTab.privPassword){
				$('#privPassword').focus();
				return false;
			}
		}
		return true;
	}
})
</script>
</head>
<body class="openWinBody" >
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip" id="headerTip">@batchTopo.addlabel@</div>
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
		                       <!--  <option value="3">SNMP V3</option> -->
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
							<Zeta:Password width="210px" id="authPassword" value="123456" maxlength="32" disabled="true" tooltip="@WORKBENCH/onfocus.pleaseInputauthPass@"/>
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
							<Zeta:Password width="210px" id="privPassword" value="123456" maxlength="32" disabled="true" tooltip="@WORKBENCH/onfocus.pleaseInputprivPass@" />
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
