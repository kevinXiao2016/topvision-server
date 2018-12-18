<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<title><fmt:message bundle='${cmc}' key='CMC.title.configinfoerror'/></title>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
$(function(){
	$("input").css("border", "1px solid #8bb8f3");
	new ipV4Input("emsIpAddress","span9").setValue("${entity.ip}");
	setIpWidth("all", 180);
	
	$("#saveBtn").click(function(){
		var _data={};
		_data.entityId="${entity.entityId}";
		_data.emsIpAddress=getIpValue("emsIpAddress");
		_data.serverSnmpVersion=$("#snmpVersion").val();
		_data.readCommunity=$("#readCommunity").val();
		_data.writeCommunity=$("#writeCommunity").val();
		
		$.ajax({
			url : "/config/saveServerSnmp.tv", 
			cache : false, 
			method: 'post' ,
			data : _data,
			success : function(){
				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.setsuccess);
			},
			error:function(e){
				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.setfailed);
			}
		});
	});
});
</script>
</head>
<body>
<div style="padding:10px;">
<fieldset>
    <legend><fmt:message bundle='${cmc}' key='CMC.title.emssnmpinfo'/></legend>
    <table cellspacing="5" width="100%">
        <tr>
            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.emsip'/>:</td>
            <td>
                <span id="span9"></span>
            </td>
            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.version'/>:</td>
            <td>
            <select id="snmpVersion"  style="width: 180px">
                <option value="1">V2C</option>
            </select>
            </td>
            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.rocommunity'/>:</td>
            <td>
            <input style="width: 180px" id="readCommunity" value="${snmpParam.community}">
            </td>
        </tr>
        <tr>
            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.rwcommunity'/>:</td>
            <td colspan="5">
                <input type="text" style="width: 180px" id="writeCommunity" value="${snmpParam.writeCommunity}">
            </td>
        </tr>
    </table>
</fieldset>
</div>

<div align="right" style="padding: 10px;">
    <button class="BUTTON75" id="saveBtn"><fmt:message bundle='${cmc}' key='CMC.label.savesetting'/></button>
</div>
</body>
</html>