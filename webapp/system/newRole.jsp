<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
    library ext
    library jquery
    library zeta
    module resources
</Zeta:Loader>
	
<script type="text/javascript">
var superiorId = 0;
var msg = "@SYSTEM.roleIsEmpty@";

function doOnload() {
	setTimeout(function() {
		Zeta$('name').focus();
	}, 500);
}

function doFocusing() {
	Zeta$('name').focus();
}

// �Ƿ�Ϊ�����ַ�
function isSpecialChar(str){
    var reg = /^[<>&$%]+$/;
    if(!reg.test(str)){
     return false;
    }
    return true;
}

function validate(name){
	var reg;
	//验证用户名,必须为3-15位的由数字、字母、下划线、中化线组成的字符串
	//reg = /^[\w-]{3,15}$/;
	reg = /^[-\w\u4e00-\u9fa5]{3,15}$/
	if(!reg.test(name)){
		Zeta$('name').focus();
		return false;
	}
	return true;
}

function okClick() {
	var el = Zeta$('name');
	//名称验证
	if( !validate(el.value) ){return;}
	//各种重复验证
	if(repeatFlag){
		$("#name").focus();
		return; 
	}
	if(Zeta$("note").value.length > 230){
		return window.parent.showMessageDlg("@COMMON.tip@", "@SYSTEM.maxlength230@");
	}
	Ext.Ajax.request({
	       url: 'createRole.tv', 
	       method:'POST', 
	       params:{
	           "role.superiorId":0,
	           "role.name":el.value,
	           "role.note":Zeta$("note").value
	       },
		   success: function(response) {
	            var json = Ext.decode(response.responseText);
	            //现在在blur事件中已经进行了验证,在这里应该不会再有重复的情况出现了
	            if (json.exists) {
	                msg = "@SYSTEM.roleIsExist@";
	                doFocusing();
	                return;
	            }
	            var frame = window.top.getFrame('roleList');
				if (frame != null) {
					window.parent.roleTreeNodeName = el.value;
					frame.onRefreshClick();
					top.afterSaveOrDelete({
				      title: '@COMMON.tip@',
				      html: '<b class="orangeTxt">@SYSTEM.newRoleSuccess@</b>'
				    });
					//window.parent.showMessageDlg("@COMMON.tip@", "@SYSTEM.newRoleSuccess@");
				}
				cancelClick();
				showRoleView();
	            //Zeta$('name').value = '';
	            //Zeta$('note').value = '';
		   },
		   failure: function(response) {
			   window.parent.showErrorDlg();
		   }
		});	
}

function showRoleView() {
    window.parent.addView("roleList", "@SYSTEM.roleManagement@", "roleIcon", "system/showPowerForRole.tv");
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}

//判断角色名称是否已经存在
function isRoleNameExists(){
	var flag = false;
	$.ajax({
		url : 'isRoleNameExist.tv',
		type : 'post',
		data : {
			"role.superiorId":0,
			"role.name":$("#name").val()
		},
		dataType : 'json',
		success : function(json) {
			flag = json.exists
		},
		error : function(json) {
		},
		async : false,
		cache : false
	});
	return flag;
}

var repeatFlag = false;
$(document).ready(function(){
	//为名称输入框绑定事件
	$("input#name").bind('blur',function(){
		repeatFlag = isRoleNameExists();
		if(repeatFlag){
			$("#nameExist").show();
			$("input#name").addClass('normalInputRed');
		}else{
			$("#nameExist").hide();
			$("input#name").removeClass('normalInputRed');
		}
	});
});
</script>
</head>
<body class="openWinBody" onload="doOnload()">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@SYSTEM.role@</b></p>
	    	<p><span id="newMsg">@SYSTEM.createARole@</span></p>
	    </div>
	    <div class="rightCirIco userCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="100">
	                   <label for="name">@COMMON.name1@<font color=red>*</font></label>
	                </td>
	                <td>
	                	<input style="width:304px" id="name" name="role.name" value='' class="normalInput" type=text maxlength=24	  	
							toolTip='@SYSTEM.roleIsEmpty@' />
	                </td>
	                <td><span id="nameExist" style="display:none;" class="orangeTxt">@SYSTEM.roleIsExist@</span>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                   @COMMON.note1@
	                </td>
	                <td>
	                    <textarea id="note" class="normalInput" name="role.note" rows=5 maxlength="230" style="overflow:auto;width:300px; height:120px;"></textarea>
	                </td>
	                <td></td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		        <li>
		            <a href="javascript:;" class="normalBtnBig"  onclick="okClick()">
		                <span>
		                    <i class="miniIcoAdd">
		                    </i>
		                    @COMMON.create@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig"  onclick="cancelClick()">
		                <span>
		                	<i class="miniIcoForbid">
		                    </i>
		                   @COMMON.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>
</body>
</Zeta:HTML>