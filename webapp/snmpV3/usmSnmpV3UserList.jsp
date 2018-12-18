<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module snmpV3
    import snmpV3.javascript.SnmpV3UserGrid
</Zeta:Loader>
</head>
<style type="text/css">
.dialog-auth-pass {marin-left:15px;margin-top:15px;margin-right:5px;}
</style>
<script type="text/javascript">
var userGrid, content;
var entityId = '${entityId}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
Ext.onReady(function(){
	userGrid = new SnmpV3UserGrid({
	    stripeRows:true,region: "center",bodyCssClass: 'normalTable',
		renderTo : "userGridCont",
		height: 392
	});
	userGrid.getStore().reload();
	content = document.getElementById("inputDialog");
	content = content.parentNode.removeChild(content);
	content.style.display = "block";
});

/**
 * 添加SNMP V3
 */
function addUserHandler(){
	window.parent.createDialog('userAdditionWizard', "@USER.addV3User@",  800, 500, '/snmp/showUserAddtionWizard.tv?entityId=' + entityId, null, true, true,function(){
		userGrid.getStore().reload();
	});
}

/**
 * 关闭页面
 */
function closeHandler(){
	window.parent.closeWindow('usmSnmpV3UserList');
}

function fetchHandler(){
	window.parent.showWaitingDlg("@COMMON.wait@","@COMMON.fetching@");
	$.ajax({
	    url: '/snmp/refreshSnmpV3Config.tv',cache:false,
	    data:{
	    	entityId: entityId
	    },success:function(){
	    	window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
	    	userGrid.getStore().reload();
	    },error:function(){
	    	window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchEr@");
	    }
	});
}

function tryUserValid(el,id){
	window.errorMsg = '<img  src="../images/fault/trap_off.png" border=0 align=absmiddle />@USER.testInvalid@';
	window.validMsg = '<img  src="../images/fault/trap_on.png" border=0 align=absmiddle />@USER.testValid@';
	//$el = $("#"+id+"Test").parent();
	$el = $(el).parent().parent().next().children();
	$el.html('<img title="@USER.testing@" src="../images/blue_loading.gif" width=16 height=16 border=0 align=absmiddle>');
	var rec = userGrid.getStore().getById(id).data;
    $.ajax({
        url: '/snmp/tryUserValid.tv',cache:false,dataType:'json',
        data:{
            entityId : entityId,
            snmpUserName: rec.snmpUserName,
            snmpUserEngineId : rec.snmpUserEngineId
        },success:function(json){
        	switch(json.data){
	        	case 'valid':
	        		$el.html(validMsg);
	        		break;
	        	case 'timeout':
	        		$el.html('<div style="color:red;">@USER.testTimeout@</div>');
	        		break;
	        	case 'wrongPwd': 
	        	case 'usmStatsWrongDigests':
	        		//只有是认证模式时才需要测试
	        		if(rec.snmpPrivProtocol != "NOAUTH"){
	        			showInputDiaglog(rec.snmpUserName,rec.snmpUserEngineId,$el);
	        			return;
	        		} 
	        		$el.html(errorMsg);
	                break;
	        	case 'wrongVersion':
	        		$el.html('<div style="color:red;">@USER.v3Unopen@</div>');
	        		break;
	            default:
	            	$el.html('<div style="color:red;">@USER.unknown@</div>');
        	}
        },error:function(){
            $el.html(errorMsg);
        }
    });
}

function tryAgain(snmpUserName,snmpUserEngineId,_a,_p,$el){
	$.ajax({
        url: '/snmp/tryUserValid.tv',cache:false,dataType:'json',
        data:{
            entityId : entityId,
            snmpUserName: snmpUserName,
            snmpAuthPwd : _a,
            snmpPrivPwd : _p,
            snmpUserEngineId : snmpUserEngineId
        },success:function(json){
        	if(json.data == 'valid'){
        		$el.html(validMsg);
        	}else{
        		$el.html(errorMsg);
        	}
        },error:function(){
        	$el.html(errorMsg);
        }
    });
}

function showInputDiaglog(snmpUserName,snmpUserEngineId,$el){
	window.inputWin = window.parent.createElMessageWindow("passPromt", "@USER.oltPassEr@", 270,200,content,null,true,false,null,false);
	//使用闭包来完成之间的关联
	window.inputWin.doResult = function(_a,_p,isCanceled){
		if(!isCanceled){
			tryAgain(snmpUserName,snmpUserEngineId,_a,_p,$el);
		}else{
			$el.html('<div style="color:red;">@USER.invalidPass@</div>');
		}
	}
}

function confirmPass(el){
	var promPri = $("#promtPriv",el.parentNode.parentNode)[0];
	var promAut = $("#promtAuth",el.parentNode.parentNode)[0]
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


/**
 * 修改SNMP V3 用户
 */
function modifyBtClick(id) {
    var rec = userGrid.getStore().getById(id).data;
    var url = String.format("/snmp/showModifySnmpV3User.tv?entityId={0}&snmpUserName={1}&snmpUserEngineId={2}",entityId, rec.snmpUserName, rec.snmpUserEngineId);
    window.parent.createDialog('userModifactionWizard', "@USER.mdfV3User@", 480, 360,url, null,true, true,function(){
    	userGrid.getStore().reload();
    });
}

/**
 * 删除用户
 */
function deleteBtClick(id){
    var rec = userGrid.getStore().getById(id).data;
    //TODO 当前使用的组不应该被删除
    window.parent.showConfirmDlg("@COMMON.confirm@", String.format("@USER.cfmDelUsr@", rec.snmpUserName), function(type) {
        if (type == 'no'){return;} 
        window.parent.showWaitingDlg("@COMMON.wait@","@COMMON.deleting@");
        $.ajax({
            url: '/snmp/deleteUser.tv',cache:false,
            data:{
                entityId: entityId,
                snmpUserName: rec.snmpUserName,
                snmpUserEngineId: rec.snmpUserEngineId,
                snmpGroupName : rec.snmpGroupName
            },success:function(){
            	window.parent.showMessageDlg("@COMMON.tip@", String.format("@USER.delUsrOk@", rec.snmpUserName));
                userGrid.getStore().reload();
            },error:function(){
            	window.parent.showMessageDlg("@COMMON.tip@", String.format("@USER.delUsrEr@", rec.snmpUserName));
            }
        });
    });
}

function authLoad(){
	if(!operationDevicePower){
		R.addUserBt.setDisabled(true);
	}
}
</script>
<body class="openWinBody" onload="authLoad()">
<div class="edge10">
	<div id="userGridCont"></div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="addUserBt" onClick="addUserHandler()" icon="miniIcoAdd">@USER.addUser@</Zeta:Button>
		<Zeta:Button id="refreshBt" onClick="fetchHandler()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="finishBt"  onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</div>

<!-- 弹出对话框内容 -->
<div id="inputDialog" style="padding-left:35px;padding-top:35px;display: none">
    <div>
		<table>
			<tr>
				<td><label>@USER.authPass@</label></td>
				<td><Zeta:Password width="133px" maxlength="31" id="promtAuth" /></td>
			</tr>
			<tr>
				<td><label >@USER.privPass@</label></td>
				<td><Zeta:Password width="133px" maxlength="31" id="promtPriv" /></td>
			</tr>
		</table>
	</div>
    <div style="margin-top:38px;padding-left:52px;">
        <button class="BUTTON75" onclick="confirmPass(this)">@COMMON.ok@</button>
        <span style="width:2px;"></span>
        <button class="BUTTON75" onclick="cancelPass(this)" >@COMMON.cancel@</button>
    </div>
</div>
</body>
</Zeta:HTML>
