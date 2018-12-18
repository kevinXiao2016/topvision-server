<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../network/entityProperty.js"></script>
<script type="text/javascript" src="../fault/TipTextArea.js"></script>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var folderId = <s:property value="folderId"/>;
var entityId = <s:property value="entityId"/>;
var entityIp = '<s:property value="entity.ip"/>';
var entityName = '<s:property value="entity.name"/>';
var icon48 = '/images/<s:property value="entity.icon48"/>';
var sourceType = '<s:property value="sourceType"/>';
$(document).ready(function () {
    // 如果是在【孤立的设备】或【脱管的设备】中点击设备属性，则不显示【固定节点位置】
    if (sourceType) {
        $('#fixed').hide();
        $('#fixedText').hide();
    }
    
   	var t1 = new TipTextArea({
   		id: "url"
   	});
   	t1.init();
   	var t2 = new TipTextArea({
   		id: "inputNote"
   	});
   	t2.init();
});

function isValidUrl(s){
   // var regex = new RegExp("^(http[s]?:\\/\\/(www\\.)?|ftp:\\/\\/(www\\.)?|www\\.){1}([0-9A-Za-z-\\.@:%_\+~#=]+)+");
  	var regex = /^(http:\/\/)[a-zA-Z\d\-_\[\]()\/\.:@%+~=]{1,64}$/
    return regex.test(s);
}

function authLoad(){
	if(!operationDevicePower){
		$("#nameInFolder").attr("disabled",true);
	}
}
</script>
</head>
<body class="sideMapBg" onload="authLoad()">
	<div class="edge5">
	<form name="entityForm">
		<input type=hidden name="entityId" value="<s:property value="entityId"/>" /> 
		<input type=hidden name="entity.entityId" value="<s:property value="entityId"/>" />
		<input type=hidden name="entity.folderId" value="<s:property value="folderId"/>" />
	<s:if test="folderId >= 0">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		     <thead>
		         <tr>
		             <th colspan="2" class="txtLeftTh">@NETWORK.nodeProperty@</th>
		         </tr>
		     </thead>
		     <tbody>
		         <tr>
		             <td class="rightBlueTxt" width="96"> 
		             	@NETWORK.typeHeader@:
		             </td>
		             <td>
		             	<s:property value="entity.typeName" />
		             </td>
		         </tr>
		          <tr>
		             <td class="rightBlueTxt"> 
		             	@NETWORK.alias@: <font color=red>*</font>
		             </td>
		             <td>
		             	<input id="nameInFolder" type=text class="normalInput" toolTip='@COMMON.anotherName.br@'
											name="entity.nameInFolder"  maxlength=63
											value="<s:property value="entity.nameInFolder"/>"
											style="width: 200px" />
		             </td>
		         </tr>
		          <tr>
		             <td class="rightBlueTxt"> 
		             	@NETWORK.href@:
		             </td>
		             <td style="padding:2px 0px;">
		             	<textarea id="url" maxLength="64" class="normalInput" name="entity.url" toolTip='@topoLabel.http@<br />@topoLabel.noteTip@'
												rows=3 style="overflow: auto; width: 200px; height:50px; "><s:property value="entity.url" /></textarea>
		             </td>
		         </tr>
		          <tr>
		             <td class="rightBlueTxt"> 
		             	@NETWORK.note@:
		             </td>
		             <td style="padding:2px 0px;">
		             	<textarea id="inputNote" maxLength="128" class="normalInput" name="entity.note" rows=3 toolTip='@topoLabel.noteTip128@'
												style="overflow: auto; width: 200px; height:50px;"><s:property value="entity.note" /></textarea>
		             </td>
		         </tr>
		          <tr>
		             <td class="rightBlueTxt"> 
		             
		             </td>
		             <td style="padding:4px 0px 10px 0px;">
						<a id="selectBt" onclick="onSaveClick();" href="javascript:;" class="normalBtn"><span><i class="miniIcoEdit"></i>@NETWORK.modifyProperty@</span></a>
		             </td>
		         </tr>
		         <tr>
		             <td class="rightBlueTxt"> 
		             	@NETWORK.icon@:
		             </td>
		             <td>
		             		<img id="entityIcon" src="<s:property value="entity.icon"/>" border=0/>
		             </td>
		         </tr>
		         <tr>
		             <td class="rightBlueTxt"> 
		             
		             </td>
		             <td style="padding:4px 0px 10px 0px;">
		             	<ul class="leftFloatUl">
		             		<li>
		             			<a id="changeBt" onclick="changeIcon()" href="javascript:;" class="normalBtn"><span><i class="miniIcoEdit"></i>@NETWORK.modifyIcon@</span></a>
		             		</li>
		             		<li>
		             			<a id="resetBt" onclick="resetIcon()" href="javascript:;" class="normalBtn"><span><i class="miniIcoBack"></i>@NETWORK.resetIcon@</span></a>
		             		</li>
		             	</ul>
		             </td>
		         </tr>
		        <%--  <tr>
		             <td class="rightBlueTxt"> 
		             
		             </td>
		             <td>
		             	<input id="fixed" type=checkbox <s:if test="entity.fixed">checked</s:if>
						onclick="fixNodeLocation(this);" /><label for="fixed" id="fixedText">@NETWORK.fixNodeLocation@</label>
		             </td>
		         </tr> --%>
		     </tbody>
		</table>
	</s:if>


		
				
				
				
		</form>
	</div>
</body>
</Zeta:HTML>
