<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    plugin DropdownTree
    import js.tools.ipText
    module workbench
    css css.white.disabledStyle
</Zeta:Loader>
<style type="text/css">
tr,td{text-align: left;}
tr,td:first-child{color:#0267B7; text-align: right;}
.v3setting{display:none;}
#treeDemo{
  width: 210px;
  display: none;
  position: absolute;
	z-index: 100;
}
.folderTree .folderTree-body{
	height: 200px;
}
</style>
<script type="text/javascript">
var folderId = <%= request.getParameter("folderId") %>;
var regionTree;

$(function(){
	regionTree = $('#region_tree').dropdowntree({
		multi: false,
		width: 210
	}).data('nm3k.dropdowntree');
})

/**
 * 是否超过了许可证规定的设备数量
 */
function entityOutofLimit(){
    var ret=false;
    $.ajax({
        url:"/entity/entityOutofLimit.tv",
        cache:false,
        async:false,
        type:"post",
        dataType:"json",
        success:function(resp){
            if(resp.success){
                ret=resp.outofLimit;
            }else{
                window.top.showMessageDlg("@COMMON.tip@", resp.msg);
            }
        }
    });
    return ret;
}

//由于输入单引号、双引号时，会导致资源列表的超链接出问题，增加此限制，Added by huangdongsheng
function validateAlias(str){
    var reg = /^[\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/;
    return reg.test(str);
}

function okClick() {
	var requetData = {};
	var version = $("#snmpVersion").val();
	var ip = getIpValue("newIp"),
	    ipFirstInput = parseInt($("#newIp").find(":text").eq(0).val(),10);
    if (!checkedIpValue(ip)) {
		//window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.ipIsInvalid@");
		top.afterSaveOrDelete({        
			title: '@COMMON.tip@',        
			html: '<b class="orangeTxt">@RESOURCES/WorkBench.ipIsInvalid@</b>'    
		});
		return;
    }
    if(!ipIsFilled("newIp")){
		//window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.ipNotNull@");
		top.afterSaveOrDelete({        
			title: '@COMMON.tip@',        
			html: '<b class="orangeTxt">@RESOURCES/WorkBench.ipNotNull@</b>'    
		});
		return;
	}
	if (ip == '0.0.0.0' || ip == '255.255.255.255' || ipFirstInput === 0 || ipFirstInput === 127 || ipFirstInput >= 224) {
		//window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.ipNotLike@" + ip + '!');
		top.afterSaveOrDelete({        
			title: '@COMMON.tip@',        
			html: '<b class="orangeTxt">@RESOURCES/WorkBench.ipNotLike@</b>' + ip 
		});
		return;
	}

	/* var name = Zeta$('name').value.trim();
	if( name == null || name.trim() == '' || name.trim().length > 63 || !validateAlias(name.trim())){
		return Zeta$('name').focus();
	} */
	var name = $("#name").val();
	if( !Validator.isAnotherName(name) ){
		$("#name").focus();
		return;
	}
	
	var cmt = Zeta$('community').value.trim();
	var rwCmt = Zeta$('rwcommunity').value.trim();
	var username = Zeta$('username').value.trim();
	var authPro = Zeta$('authProtocol').value.trim();
	var authPass = Zeta$('authPass').value.trim();
	var privPro = Zeta$('privProtocol').value.trim();
	var privPass = Zeta$('privPass').value.trim();
	if(parseInt(version) !=3){
		if (cmt == '') {
			//window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.communityNotNull@");
			return Zeta$("community").focus();
		}
		if(rwCmt == ''){
			return Zeta$("rwcommunity").focus();
		}
	}else{
		if(username == ''){
			return Zeta$("username").focus();
		}
		if(authPro != 'NOAUTH'){
			if(authPass == '' || authPass.length < 8){
				return Zeta$("authPass").focus();
			}
		}
		if(privPro != 'NOPRIV'){
			if(privPass == '' || privPass.length < 8){
				return Zeta$("privPass").focus();
			}
		}
	}
	
	//必须选择地域
	var selectedFolderId = regionTree.getSelectedIds();
	if(!selectedFolderId.length){
		window.top.showMessageDlg('@COMMON.tip@', '@pleaseSelectFolder@');
		return
	}

	switch(parseInt(version)){
	case 0://snmpv1
	case 1://snmpv2
		requetData = {
			'entity.ip': ip,
			'entity.name': Zeta$('name').value.trim(),
			'folderId': selectedFolderId || folderId,
			'snmpParam.community': Zeta$('community').value.trim(),
			'snmpParam.version':version,
			'snmpParam.writeCommunity': Zeta$('rwcommunity').value.trim()
		}
		break;
	case 3://snmpv3
		requetData = {
			'entity.ip': ip, 
			'entity.name': Zeta$('name').value.trim(),
			'folderId': selectedFolderId || folderId,
			'snmpParam.username': Zeta$('username').value.trim(),
			'snmpParam.authProtocol':Zeta$('authProtocol').value.trim(),
			'snmpParam.authPassword': Zeta$('authPass').value.trim(),
			'snmpParam.privProtocol':Zeta$('privProtocol').value.trim(),
			'snmpParam.privPassword': Zeta$('privPass').value.trim(),
			'snmpParam.version':version
		}
		break;
	}

	window.top.showWaitingDlg("@COMMON.wait@", "@RESOURCES/WorkBench.topoDeviceType@");
	$.ajax({url: 'newEntity.tv', type: 'POST',
		data:requetData,
        beforeSend:function(){
        },
		success: function(json) {
			if (json.exist) {
				window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.deviceExist@");
			} else if(json.licenseLimit){
				window.top.showMessageDlg("@COMMON.tip@", '@entity.outofLimit@');
			} else {
				try{
					top.afterSaveOrDelete({        
						title: '@COMMON.tip@',        
						html: '<b class="orangeTxt">@RESOURCES/COMMON.newSuccess@</b>'    
					});
					window.top.closeWaitingDlg();
					window.top.getActiveFrame().onRefreshClick();
					window.top.getActiveFrame().timeOutRefresh();
				}catch(e){}
				cancelClick();
            }
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'json', cache: false});
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}
function addEnterKey(e) {
	var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
        $("#okBt").focus();
    	//okClick();
    }
}
Ext.onReady(function(){
	var newIp = new ipV4Input("newIp","span1");
	newIp.width(210);
	
});
function snmpChanged() {
	var hei = 456;
	var tmpW = window.parent.getWindow("modalDlg");
    switch (Zeta$('snmpVersion').value) {
        case '0':
        case '1':
        	$(".v2setting").show();
            $(".v3setting").hide();
            tmpW.setHeight(hei);
        	tmpW.body.setHeight(hei);
            break;
        case '3':
        	tmpW.setHeight(hei);
        	tmpW.body.setHeight(hei+100);
        	$(".v2setting").hide();
            $(".v3setting").show();
            authProtocolChange();
            privProtocolChange();
            break;
    }
}
function authProtocolChange() {
	var authProtocol = Zeta$('authProtocol').value;
	if(authProtocol == "NOAUTH"){
		$("#authPass").attr("disabled",true);
		$("#authPass").val('12345678');
		$("#privPass").attr("disabled",true);
		$("#privPass").val('12345678');
		$("#v3AuthPass").attr("disabled","true");
		$("#v3PrivPass").attr("disabled","true");
	}else{
		$("#authPass").attr("disabled",false);
		$("#authPass").parent().find(":input").attr("disabled",false);
		$("#authPass").val('');
		$("#privPass").attr("disabled",false);
		$("#privPass").val('');
		$("#v3AuthPass").removeAttr("disabled");
		$("#v3PrivPass").removeAttr("disabled");
	}
}
function privProtocolChange(){
	var privProtocol = Zeta$('privProtocol').value;
	if(privProtocol == "NOPRIV"){
		$("#privPass").val('12345678');
		$("#privPass").attr("disabled",true);
		$("#v3PrivPass").attr("disabled","true");
	}else{
		$("#privPass").val('');
		$("#privPass").attr("disabled",false);
		$("#privPass").parent().find(":input").attr("disabled",false);
		$("#v3PrivPass").removeAttr("disabled");
	}
}
function snmpVersionChanged(el){
	$("#snmpVersion").val(el.value);
}

</script>
</head>
<body class="openWinBody" onkeydown="addEnterKey(event || e)">
	<!-- <div id="treeDemo" class="folderTree">
      <div class="folderTree-body"></div>
    </div> -->
    
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader" style="height:100px;">
		<div class="openWinTip" style="margin-right:50px;">
			<p>@td.createDevice@ <b class="orangeTxt">@COMMON.ipDoNotUse@</b></p>
			<p>0.0.0.0 <span class="pL10">0.0.0.1~0.255.255.255</span></p>
			<p>127.0.0.0~127.255.255.255 (@COMMON.ipDoNotUse2@)<span class="pL10">224.0.0.0~239.255.255.255 (@COMMON.ipDoNotUse3@)</span></p> 
			<p>240.0.0.0~255.255.255.254 (@COMMON.ipDoNotUse4@)<span class="pL10">255.255.255.255</span></p>
		</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows" >
					<tr>
						<td width="200">
							<label for="ip">@td.ipAddress@@COMMON.maohao@<font color=red>*</font></label>
						</td>
						<td>
							<span id="span1"></span>
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td><label for="name">@td.deviceName@@COMMON.maohao@<font color=red>*</font></label></td>
						<td>
							<input id="name" maxlength="63" class="normalInput" style="width: 210px" tooltip="@COMMON.anotherName@" />
						</td>
					</tr>
					<tr>
						<td><label for="folder">@region@@COMMON.maohao@<font color=red>*</font></label></td>
						<td>
							<div id="region_tree"></div>
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td><label for="snmpVersion">@td.snmpVersion@@COMMON.maohao@<font color=red>*</font></label></td>
						<td><select id="snmpVersion"  class="normalSel" style="width: 210px"  onchange="snmpChanged();">
			                        <option value="1">SNMP V2C</option>
			                        <option value="3">SNMP V3</option>
			                    </select>
						</td>
					</tr>
					<tr class="v2setting">
						<td><label for="community">@td.community@@COMMON.maohao@<font color=red>*</font></label></td>
						<td>
							<Zeta:Password width="210px" id="community" value="public" maxlength="32" tooltip="@onfocus.pleaseInputCommunity@" />
						</td>
					</tr>
					<tr class=" darkZebraTr v2setting">
						<td><label for="community">@td.rwCommunity@@COMMON.maohao@<font color=red>*</font></label></td>
						<td>
							<Zeta:Password width="210px" id="rwcommunity" name="rwcommunity" value="private" maxlength="32" tooltip="@onfocus.pleaseInputWriteCommunity@" />
						</td>
					</tr>
					<tr class="v3setting">
						<td><label for="username">@td.username@@COMMON.maohao@<font color=red>*</font></label>
						</td>
						<td><input id="username" maxlength="32" class="normalInput w440"
							style="width: 210px;border:1px solid #8bb8f3;" tooltip="@onfocus.pleaseInputUserName@" />
						</td>
					</tr>
					<tr class="v3setting darkZebraTr" >
						<td ><label for="authProtocol">@td.authProtocol@@COMMON.maohao@<font color=red>*</font></label>
						</td>
						<td > 
							<select style="width: 210px;" class="normalSel" id="authProtocol"  onchange="authProtocolChange();">
			                        <option value="NOAUTH">NOAUTH</option>
			                        <option value="MD5">MD5</option>
			                        <option value="SHA">SHA</option>
			                </select>
						</td>
					</tr>
					<tr class="v3setting">
						<td ><label for="authPass">@td.authPass@@COMMON.maohao@<font color=red>*</font></label>
						</td>
						<td>
							<Zeta:Password width="210px" id="authPass"  maxlength="32" tooltip="@onfocus.pleaseInputauthPass@"/>
						</td>
					</tr>
					<tr class="v3setting darkZebraTr">
						<td><label for="privProtocol">@td.privProtocol@@COMMON.maohao@<font color=red>*</font></label>
						</td>
						<td >
							<select style="width: 210px;" class="normalSel" id="privProtocol" onchange="privProtocolChange();">
		                        <option value="NOPRIV">NOPRIV</option>
		                        <option value="CBC-DES">CBC-DES</option>
			                </select>
						</td>
					</tr>
					<tr class="v3setting">
						<td ><label for="privPass">@td.privPass@@COMMON.maohao@<font color=red>*</font></label></td>
						<td>
							<Zeta:Password width="210px" id="privPass" maxlength="32"  tooltip="@onfocus.pleaseInputprivPass@" />
						</td>
					</tr>
			</table>
		</form>
	</div>
	
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick()" icon="miniIcoAdd">@COMMON.create@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	<script type="text/javascript">
		$(function(){
			$("a").attr("tabindex",1000)
		})
	</script>
</body>
</Zeta:HTML>
