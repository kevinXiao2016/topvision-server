<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
    <Zeta:Loader>
        library base
        library ext
        library zeta
        library jquery
        module snmpV3
    </Zeta:Loader>
</head>
<style type="text/css">
#canvasLayout {
    position: absolute;
    left: 0px;
    top: 63px;
}

.configLayout {
    padding: 5 15 0 15;
    margin: 5 15 0 15;
    width: 100%;
}

.configLayout table {
    margin-left: 10px;
    margin-top: 10px;
}

.configLayout table tr {
    margin-top: 15px;
    height: 25px;
}

#bodyLayoutUser {
    position: absolute;
    left: 0px;
    margin-left: 0px;
    width: 400px;
}

#groupLayout {
    position: absolute;
    padding-left:40px;
    left: 480px;
}
#viewLayout{
    position: absolute;
    left: 960px;
}

.lableClazz {
    text-align: right;
    padding-left: 20px;
    width: 110px;
}

.valueClazz {
    padding-left: 5px;
    width: 23px;
}

#bodyLayoutUser .lableClazz{
    padding-left: 0px;
    width: 100px;
}
#bodyLayoutUser .valueClazz{
    width: 70px;
}
#viewLayout .lableClazz {
    width: 80px;   
}

.buttonClazz {
    position: absolute;
    top: 295px;
    left: 300px;
}

button {
    margin-right: 5px;
}

select {
    width: 133px;
}
a, a:hover, a:link{
    color: #3C78B5;
}
.loadingmask {
    width: 16px;
    height: 16px;
    position: relative;
    top: 3px;
}
.dialog-auth-pass{marin-left:15px;margin-top:15px;margin-right:5px;}
</style>
<script type="text/javascript">
  var entityId = ${entityId};
  var thisStep = 0;
  var rawAuthProtocol = '${user.snmpAuthProtocol}';
  var rawPrivProtocol = '${user.snmpPrivProtocol}';
  var engine = '${user.snmpUserEngineId}';
  $(document).ready(function(){
	  addEventListeners();
	  engine = engine.replace(new RegExp(":","gm"),"");
	  $("#engineId").val(engine);
	  $("#authProtocol").val(rawAuthProtocol);
	  $("#privProtocol").val(rawPrivProtocol);
	  //如果是不认证的话,则不能切换认证协议
	  if(rawAuthProtocol == 'NOAUTH'){
		  $("#authLable,#authNewLable,#oldAuthenticationKey,#newAuthenticationKey,#authProtocol").attr("disabled",true);
	  }else{//如果是认证的话,则可以相互切换。@FIXME 目前 MD5-SHA之间底层无法切换
		  $("#authLable,#authNewLable,#oldAuthenticationKey,#newAuthenticationKey").attr("disabled",false);
	  }
	  if(rawPrivProtocol == 'NOPRIV'){
		  $("#privNewLable,#privLable,#oldPrivacyKey,#newPrivacyKey,#privProtocol").attr("disabled",true);
	  }else{
		  $("#privNewLable,#privLable,#oldPrivacyKey,#newPrivacyKey").attr("disabled",false);
	  }
	  content = document.getElementById("inputDialog");
	  content = content.parentNode.removeChild(content);
	  content.style.display = "block";
  });
  
  
/***********************************
                             关闭页面
 ************************************/
function closeHandler(){
    window.parent.closeWindow('userModifactionWizard');
}

function saveHandler(oldAuth,oldPriv, isSkip){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.USER.checkingEngine, 'ext-mb-waiting');
	$.ajax({
		url: '/snmp/getEntitySnmpEngineId.tv',method:'POST',async:false,dataType:'json',
		data: {
			entityId : entityId
		},success:function(json){
			window.parent.closeWaitingDlg();
			var entityEngine = json.engine.replace(new RegExp(":","gm"),"");
			if(engine == entityEngine ){
				_saveHandler_(oldAuth,oldPriv, isSkip);
			}else{
				window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.USER.confirmConfig, function(type) {
                    if (type == 'no') {
                        return;
                    }else{
                    	_saveHandler_(oldAuth,oldPriv, isSkip);
                    }
                });
			}
		},error:function(){
			window.parent.closeWaitingDlg();
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.uptUserEr);
		}
	})
}

function _saveHandler_(oldAuth, oldPriv, isNotSkip,ignore){
	if(!checkFirstStep()){
        return;
    }
    var snmpUserName = $("#username").val();
    var snmpAuthProtocol =$("#authProtocol").val();
    var snmpAuthKeyChange = $("#oldAuthenticationKey").val();
    var snmpNewAuthKeyChange = $("#newAuthenticationKey").val();
    var snmpPrivProtocol = $("#privProtocol").val();
    var snmpPrivKeyChange = $("#oldPrivacyKey").val();
    var snmpNewPrivKeyChange = $("#newPrivacyKey").val();
    var snmpGroupName = $("#group").val();
    
    var data = {};
    data.entityId = entityId;
    //使用原始的engine
    data.snmpUserEngineId = '${user.snmpUserEngineId}';
    data.snmpUserName = snmpUserName;
    data.snmpAuthProtocol = snmpAuthProtocol;
    //如果没有skip则使用旧密码
    if(isNotSkip){
        data.snmpAuthPwd = oldAuth;
        data.snmpPrivPwd = oldPriv;
    }
    if(ignore){
    	data.ignoreUserTest = true; 
    }
    data.snmpNewAuthPwd = snmpNewAuthKeyChange;
    data.snmpPrivProtocol = snmpPrivProtocol;
    data.snmpNewPrivPwd = snmpNewPrivKeyChange;
    data.snmpGroupName = snmpGroupName;
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.USER.uptingUser, 'ext-mb-waiting');
    $.ajax({
        url: '/snmp/modifyUser.tv',cache:false,data: data , dataType:'json',
        success:function(json){
            window.parent.closeWaitingDlg();
            if(json.data == "wrongVersion"){
                return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.v3notOpen);
            }else if(json.data == "wrongPwd" || json.data == "usmStatsWrongDigests"){
                showInputDiaglog();
            }else if(json.data == "valid"){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.uptUserOk);
                closeHandler.apply(window);
            }else{
            	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.USER.terminateAction, function(type){
                    if (type == 'no') {
                        return;
                    }else{
                        _saveHandler_(oldAuth, oldPriv, false, true);
                    }
                });
            }
        },error:function(){
            window.parent.closeWaitingDlg();
            //如果有oldAuth传递进来就表示这是第2次尝试
            if(!oldAuth){
                showInputDiaglog();
            }else{
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.uptUserEr);
            }
        }
    });
}

function showInputDiaglog(){
    window.inputWin = window.parent.createElMessageWindow("passPromt", I18N.USER.oltPassEr, 270,200,content,null,true,false,null,false);
    //使用闭包来完成之间的关联
    window.inputWin.doResult = function(oldAuth,oldPriv,isCanceled){
        if(!isCanceled){
        	saveHandler(oldAuth,oldPriv,true);
        }else{
        	window.parent.closeWaitingDlg();
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.uptUserErDel);
        }
    }
}

function confirmPass(el){
    var promPri = $("#promtPriv",el.parentNode.parentNode)[0];
    var promAut = $("#promtAuth",el.parentNode.parentNode)[0];
    inputWin.doResult(promAut.value,promPri.value,false);
    //置空
    promPri.value = "";
    promAut.value = "";
    if(inputWin){
        //由于多窗口时共同此el示例的 ，所以即便是删除window也不会删除这个EL,输入到其中的内容在下次打开时还会存在
        inputWin.destroy();
        inputWin.close();
    }
}

function cancelPass(el){
    //恢复这个button的默认效果
    el.className = "BUTTON75";
    //处于碎片控件的promtPriv,promtAuth不宜通过DOM找到emptyFn 
    $("#promtPriv",el.parentNode.parentNode)[0].value = "";
    $("#promtAuth",el.parentNode.parentNode)[0].value = "";
    inputWin.doResult(null,null,true);
    if(inputWin){
        //由于多窗口时共同此el示例的 ，所以即便是删除window也不会删除这个EL,输入到其中的内容在下次打开时还会存在
        inputWin.destroy();
        inputWin.close();
    }
}


///////////////  用户名校验 ///////////////////////////
function checkFirstStep(){
	if(!$("#group").val()){
		 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GROUP.groupNameTip, 'error',function(){
             $("#group").focus();
         });
         return false;
	}
    var authPro = $("#authProtocol").val();
    if(authPro != "NOAUTH"){
    	var _a = $("#oldAuthenticationKey").val();
    	var _p = $("#newAuthenticationKey").val();
        if(_a != _p){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.authKeyUnunique, 'error',function(){
                $("#newAuthenticationKey").focus();
            });
        	return false;
        }
    }
    var privPro = $("#privProtocol").val();
    if(privPro != "NOPRIV"){
    	var _a = $("#oldPrivacyKey").val();
        var _p = $("#newPrivacyKey").val();
        if(_a != _p){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.USER.privKeyUnunique, 'error',function(){
                $("#newAuthenticationKey").focus();
            });
            return false;
        }
    }
    return true;
}

function addEventListeners(){
	$("#authProtocol").bind("change",function(){
		var p = $("#authProtocol").val();
		if(p == "NOAUTH"){
			$("#authLable,#authNewLable,#oldAuthenticationKey,#newAuthenticationKey").attr("disabled",true);
			$("#privProtocol").val("NOPRIV").attr("disabled",true);
			$("#privLabel,#privNewLable,#privLable,#oldPrivacyKey,#newPrivacyKey").attr("disabled",true);
		}else{
			$("#authLable,#authNewLable,#oldAuthenticationKey,#newAuthenticationKey").attr("disabled",false);
			$("#privLabel,#privProtocol").attr("disabled",false);
		}
	});
	$("#privProtocol").bind("change",function(){
        var p = $("#privProtocol").val();
        if(p == "NOPRIV"){
            $("#privNewLable,#privLable,#oldPrivacyKey,#newPrivacyKey").attr("disabled",true);
        }else{
        	$("#privNewLable,#privLable,#oldPrivacyKey,#newPrivacyKey").attr("disabled",false);
        }
    });
}

</script>
<body class="POPUP_WND">
<div class=formtip id=tips style="display: none"></div>
    <table width=100% height=100% cellspacing=0 cellpadding=0>
        <tr>
            <td class=WIZARD-HEADER style="padding-right: 20px;">
                <table width=100%>
                    <tr>
                        <td><span style="font-weight: bold" >@USER.mdfUser@</span>
                            <br> <br>&nbsp;&nbsp;
                            <span id="newMsg" >@USER.mdfUserTip@</span></td>
                        <td align=right><img src="../images/icons/fileList.png"
                            border=0 width=50 height=50></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr height=auto width=100%>
            <td>
                <div id=mibs>
                    <table height=100% id=mibsTable>
                    </table>
                </div>
            </td>
        </tr>
    </table>
    <div id="canvasLayout">
        <!-- USER CONFIG LAYOUT -->
        <div id="bodyLayoutUser" class="configLayout" >
            <table width="410">
                <tr>
                    <td class="lableClazz">Engine ID:</td>
                    <td class="valueClazz" colspan=3>
                        <input id="engineId" disabled type="text" style="width: 250px;" value="" /></td>
                </tr>
                <tr>
                    <td class="lableClazz" >@USER.user@</td>
                    <td class="valueClazz" colspan=3>
                        <input id="username" maxlength=31 disabled type="text" value="${user.snmpUserName}" /></td>
                </tr>
                <tr>
                    <td class="lableClazz">@USER.group@</td>
                    <td class="valueClazz" colspan=3>
                        <Zeta:TextInput id="group" style="width:133px" value="${user.snmpGroupName}" tooltip="@USER.notExistGroup@" /> </td>
                </tr>
                <tr>
                    <td class="lableClazz" >@USER.authPro@</td>
                    <td class="valueClazz" colspan=3>
                    <select id="authProtocol" >
                            <option>NOAUTH</option>
                            <option>MD5</option>
                            <option>SHA</option>
                    </select></td>
                </tr>
                <tr>
                    <td class="lableClazz" id="authLable"  disabled="true">@USER.authPass@</td>
                    <td class="valueClazz">
                        <Zeta:Password width="133px" maxlength="31" disabled="true" id="oldAuthenticationKey" tooltip="@USER.userPassTip2@"/></td>
                    <td class="lableClazz" id="authNewLable" style="width:110px;"  disabled="true">@USER.confirmPass@</td>
                    <td class="valueClazz">
                        <Zeta:Password width="133px"  maxlength="31" disabled="true" id="newAuthenticationKey" tooltip="@USER.userPassTip2@"/></td>
                </tr>
                <tr>
                    <td class="lableClazz" id="privLabel" >@USER.privPro@</td>
                    <td class="valueClazz" colspan=3>
                        <select id="privProtocol" >
                            <option>NOPRIV</option>
                            <option>CBC-DES</option>
                        </select></td>
                </tr>
                <tr>
                    <td class="lableClazz" id="privLable" disabled="true" >@USER.privPass@</td>
                    <td class="valueClazz">
                        <Zeta:Password width="133px"  maxlength="31" disabled="true" id="oldPrivacyKey" tooltip="@USER.userPassTip2@"/></td>
                    <td class="lableClazz" id="privNewLable" disabled="true">@USER.confirmPass@</td>
                    <td class="valueClazz" style="width:130px;" >
                        <Zeta:Password width="133px"  maxlength="31" disabled="true" id="newPrivacyKey" tooltip="@USER.userPassTip2@"/></td>
                </tr>
            </table>
        </div>
     </div>
    <div class="buttonClazz">
        <BUTTON id="prevBt"  class="BUTTON75" onclick="saveHandler()">@COMMON.modify@</BUTTON>
        <BUTTON id="closeBt" class="BUTTON75" onclick="closeHandler()">@COMMON.cancel@</BUTTON>
    </div>
<!-- 弹出对话框内容 -->
<div id="inputDialog" style="padding-left:35px;padding-top:35px;display: none">
    <div>
          <label style="margin-right:5px;" class="dialog-auth-pass">@USER.authPass@</label>
              <input type="password"  maxlength="31" id="promtAuth"/><br><br>
          <label style="margin-right:5px;">@USER.privPass@</label>
              <input type="password"  maxlength="31" id="promtPriv"/>
    </div>
    <div style="margin-top:38px;padding-left:52px;">
        <BUTTON class="BUTTON75" onclick="confirmPass(this)">@COMMON.ok@</BUTTON>
        <span style="width:2px;"></span>
        <BUTTON class="BUTTON75" onclick="cancelPass(this)">@COMMON.cancel@</BUTTON>
    </div>
</div>
</body>
</Zeta:HTML>