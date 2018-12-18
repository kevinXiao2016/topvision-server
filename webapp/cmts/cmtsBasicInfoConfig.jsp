<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CMC
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = ${ cmcId };
var entityName = '${ entityName }';
var cmcAttribute = ${cmcAttrJson};
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
Ext.onReady(function(){
	initData();
})
function initData(){
	$("#ccName").val(cmcAttribute.topCcmtsSysName);
	$("#entityName").val(entityName);
	$("#ccSysLocation").val(cmcAttribute.topCcmtsSysLocation);
	$("#ccSysContact").val(cmcAttribute.topCcmtsSysContact);
	$("#entityName").focus();
}

function closeClick() {
	window.parent.closeWindow('basicInfoMgmt');
}

//只能输入英文、数字、下划线
//由于输入单引号、双引号时，会导致资源列表的超链接出问题，增加此限制，Added by huangdongsheng
function validateAlias(str){
  var reg = /^[\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/;
  return reg.test(str);
}

//只能输入非空格字符,contact和location校验
function validateLocationInfo(str){
	var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$@#!=+|?<>\[\]\{\}\\`~]+$/
	if(str.indexOf(" ")>-1 || !reg.test(str)){
		return true;
	}else{
		return false;
	}
}

//只能输入非空格字符,contact和location校验
function validateContactInfo(str){
    // 字母、数字、下滑线、.、@
    var reg=/^[\w@.]+$/;
    return reg.test(str);
}
	
function ccBasicInfoConfig() {
	var entityName = $("#entityName").val();
	if( !Validator.isAnotherName(entityName) ){
		$("#entityName").focus();
		return;
	}
	
	
	window.parent.showWaitingDlg(I18N.COMMON.waiting,I18N.CCMTS.renaming, 'ext-mb-waiting');
	$.ajax({
		  url: '/cmts/modifyCmtsBasicInfo.tv?cmcId=' + cmcId + "&entityName=" + entityName ,
  	      type: 'post',
  	      success: function(response) {
	  	    	if(response == "success"){
	  	    		//window.parent.showMessageDlg(I18N.COMMON.tip,I18N.CCMTS.modifyCcmtsBasicInfoSuccess);
	  	    		window.top.closeWaitingDlg();
	  	    		top.afterSaveOrDelete({
	  	   				title: '@COMMON.tip@',
	  	   				html: '<b class="orangeTxt">' + I18N.CCMTS.modifyCcmtsBasicInfoSuccess +'</b>'
	  	   			});
	  	    		window.parent.getFrame("entity-" + cmcId).reload();
	  	    	}else{
	  	    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.modifyCcmtsBasicInfoError);
	  	    	}
                try {
                    window.parent.getActiveFrame().refreshClick();
                } catch(ex) {
                }
	  	    	closeClick();
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.modifyCcmtsBasicInfoError);
			}, cache: false
		});
}
function authLoad(){
	var ids = new Array();
	ids.add("ccName");
	ids.add("entityName");
	ids.add("ccSysLocation");
	ids.add("ccSysContact");
	ids.add("save");
	operationAuthInit(operationDevicePower,ids);
}
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		    <div class="openWinTip">@CCMTS.ccmtsBasicInfoMgmt@</div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT40">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
			<tr>
				<td class="rightBlueTxt" width="120px">
					<label for="entityName">@RESOURCES/COMMON.alias@:</label>
		        </td>
				<td width=200px align="left"><input class="normalInput w150" id="entityName" style="width:250px" name="entityName" maxlength=63 toolTip='@COMMON.anotherName@' />
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt" width="120px">
					<label for="ccName">@CMC.label.labelHardwareName@:</label>
		        </td>
				<td width=200px align="left"><input class="normalInput w150" style="width:250px" id="ccName" name="ccName" disabled />
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt" width="120px">
					<label for="ccSysLocation">@CCMTS.entityLocation@:</label>
		        </td>
				<td width=200px align="left"><input class="normalInput w150" style="width:250px" id="ccSysLocation" name="ccSysLocation" disabled />
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt" width="120px">
					<label for="ccSysContact">@CCMTS.contactPerson@:</label>
		        </td>
				<td width=200px align="left"><input class="normalInput w150" style="width:250px" id="ccSysContact" name="ccSysContact" disabled />
				</td>
			</tr>
		 </tbody>
		    </table>
		    
		    <div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
			        <li>
			            <a href="javascript:;" class="normalBtnBig" onclick="ccBasicInfoConfig()">
			                <span>
			                    <i class="miniIcoData">
			                    </i>
			                   	@COMMON.save@
			                </span>
			            </a>
			        </li>
			        <li>
			            <a href="javascript:;" class="normalBtnBig" onclick="closeClick()">
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