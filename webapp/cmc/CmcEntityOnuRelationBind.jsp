<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<title><fmt:message bundle='${cmc}' key='CMC.title.EntityCCMTSRelation'/></title>

<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css" />
<!-- 自定义js引入 -->
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script>
<!-- 内置css定义 -->
<style>
.entityIcon {
	background-image: url(../images/network/entity.gif) !important;
	valign: middle;
}

.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoLeafIcon {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoRegionIcon {
	background-image: url(../images/network/region.gif) !important;
}

.ipTextField input {
	ime-mode: disabled;
	width: 47px;
	border: 0px;
	text-align: center;
}
</style>
<!-- 内置自定义js -->
<script type="text/javascript">
/*******************************************************
 * 变量定义及其初始化
 *******************************************************/
 var uniIdListString =  '<s:property value="uniIdListString"/>';
 var cmcNotRelatedOnu =  ${cmcList};
 var onuId =  '<s:property value="onuId"/>';

/*******************************************************
 * 执行语句包括onReady/onload的执行语句，其后为onReady的方法定义
 *******************************************************/
function doOnload(){
	uniIdListString = uniIdListString.slice(0,uniIdListString.length - 1)
	var uniIdList = uniIdListString.split(",");//分割成uniId$uniRealIndex格式

	var position = Zeta$('ccmts');
	var option = document.createElement('option');
	option.value = "";
	option.text = I18N.CMC.text.pleaseselec;
	try {
		position.add(option, null);
    } catch(ex) {
    	position.add(option);
    }

	for(var i=0;i<cmcNotRelatedOnu.length;i++){
	    $.each(cmcNotRelatedOnu[i],function(x,o){
	    	var option = document.createElement('option');
			option.value = o.cmcEntityId;
			option.text = o.cmcIp;
			try {
				position.add(option, null);
	        } catch(ex) {
	        	position.add(option);
	        } 
	    });
	}
	var position = Zeta$('mainuni');
	var option = document.createElement('option');
	option.value = "";
	option.text = I18N.CMC.text.pleaseselec;
	try {
		position.add(option, null);
    } catch(ex) {
    	position.add(option);
    }
	for(var i = 0; i < uniIdList.length; i++) {
		var option = document.createElement('option');
		option.value = uniIdList[i].slice(0,1);
		option.text = "uni" + uniIdList[i].slice(2,3)
		try {
			position.add(option, null);
        } catch(ex) {
        	position.add(option);
        }
	}

	var position = Zeta$('backupuni');
	var option = document.createElement('option');
	option.value = "";
	option.text = I18N.CMC.text.pleaseselec;
	try {
		position.add(option, null);
    } catch(ex) {
    	position.add(option);
    }
	for(var i = 0; i < uniIdList.length; i++) {
		var option = document.createElement('option');
		option.value = uniIdList[i].slice(0,1);
		option.text = "uni" + uniIdList[i].slice(2,3)
		try {
			position.add(option, null);
        } catch(ex) {
        	position.add(option);
        }
	}
}
/*******************************************************
 * 方法定义：操作，交互，菜单等归类组织在一起
 *******************************************************/
 function relatedCmc() {
	if($('#mainuni').val() == $('#backupuni').val()){
		window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.error);
		}
	var uniIdMain = $('#mainuni').val();
	var uniIdBack = $('#backupuni').val();
	var entityId = $('#ccmts').val();
 	$.ajax({
		url: '/cmc/relateCmcToOnu.tv?r=' + Math.random(),
		type: 'POST',
		dateType: 'json',
		data: {
			'onuId': onuId,
			'entityId': entityId,
			'uniIdMain': uniIdMain,
			'uniIdBack': uniIdBack
		},
		cache: false,
		success: function(json) {
			alert(333);
			alert(json.exist);
		},
		error: function(error) {
			alert(error);
		}
	});
 	cancelClick()
 }
 function cancelClick() {
 	window.parent.closeWindow("relateCMC");
 }
</script>
</head>
<body class=POPUP_WND style="margin: 13px;" onload="doOnload()">
	<div class=formtip id=tips style="display: none"></div>
	<fieldset style='width: 320; height: 110; background-color: #ffffff;'>
		<div>
			&nbsp;<b><fmt:message bundle='${cmc}' key='CMC.label.uni'/>:</b>
		</div>
		<table cellspacing=10 cellpadding=0>
			<tr>
				<td width=80px height=20 valign=top align="right"><fmt:message bundle='${cmc}' key='CMC.label.SelectCCMTS'/>:&nbsp;&nbsp;</td>
				<td height=20 width="100px" align="center"><select id="ccmts"
					style="width: 210px">
				</select></td>
			</tr>
			<tr>
				<td width=80px height=20 valign=top align="right"><fmt:message bundle='${cmc}' key='CMC.label.MainPort'/>:&nbsp;&nbsp;</td>
				<td height=20 width="100px" align="center"><select id="mainuni"
					style="width: 210px">
				</select></td>
			</tr>
			<tr>
				<td width=80px height=20 valign=top align="right"><fmt:message bundle='${cmc}' key='CMC.label.BackupPort'/>:&nbsp;&nbsp;</td>
				<td height=20 width="100px" align="center"><select
					id="backupuni" style="width: 210px">
				</select></td>
			</tr>
		</table>
	</fieldset>
	<div style="margin-top: 5px" style="margin-left:145px">
		<button id=saveBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="relatedCmc()"><fmt:message bundle='${cmc}' key='CMC.label.Relation'/></button>
		&nbsp;&nbsp;
		<button id=cancelBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
	</div>
</body>
</html>