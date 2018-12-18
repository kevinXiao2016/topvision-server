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
    module network
</Zeta:Loader>
<script type="text/javascript">
	var name = '${entity.name}';
	var entityId = '${entityId}';
	var pageId = '${pageId}';
	var changeEmsName=<%=uc.hasPower("changeEmsName")%>;
	// var contact='${entity.contact}';
	// var location='${entity.location}';
	// var note='${note}';

	Ext.onReady(function() {
		if(changeEmsName){
			$('#name').removeClass("normalInputDisabled");
            $('#name').addClass("normalInput");
            $('#name').attr("disabled",false);
		}else{
			$('#name').removeClass("normalInput");
            $('#name').addClass("normalInputDisabled");
            $('#name').attr("disabled",true);
		}
	})
	function cancelClick() {
		window.top.closeWindow('renameEntity');
	}
	//只能输入英文、数字、下划线和左斜线
	function validate(str) {
		var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d,;!#*$^=])+$/ig;
		return reg.test(str);
	}
	//只能输入英文、数字、下划线
	//由于输入单引号、双引号时，会导致资源列表的超链接出问题，增加此限制，Added by huangdongsheng
	function validateAlias(str) {
		var reg = /^[\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/;
		return reg.test(str);
	}
	function save() {
		/* name = Zeta$('name').value.trim();
		if( !name  || !V.isAlias(name, null, "${entity.parentId}") ){
			$('#name').focus();
			//window.parent.showMessageDlg('@COMMON.tip@', '@NETWORK.modifyNmNameTip@');
			return;
		} */
		name = $("#name").val();
		if (!Validator.isAnotherName(name)) {
			$("#name").focus();
			return;
		}
		var location = $("#location").val();
		if (location != null && location.length != 0
				&& !Validator.isLocationName(location)) {
			$("#location").focus();
			return;
		}
		var contact = $("#contact").val();
		if (contact != null && contact.length != 0
				&& !Validator.isContactName(contact)) {
			$("#contact").focus();
			return;
		}
		var note = $("#note").val();
		if (note != null && note.length != 0 && !Validator.isNoteName(note)) {
			$("#note").focus();
			return;
		}
		//window.parent.showWaitingDlg('@COMMON.waiting@','@NETWORK.renaming@', 'ext-mb-waiting');
		$
				.ajax({
					url : '/entity/changeEntityInfoByEntityId.tv',
					data : {
						entityId : entityId,
						name : name,
						location : location,
						contact : contact,
						note : note
					},
					type : 'post',
					dataType : "json",
					success : function(response) {
						//window.top.closeWaitingDlg();
						top
								.afterSaveOrDelete({
									title : '@COMMON.tip@',
									html : '<b class="orangeTxt">@resources/COMMON.modifySuccess@</b>'
								});
						window.parent.getFrame(pageId).changeEntityName(
								entityId, name);
						cancelClick();
					},
					error : function(response) {
						window.parent.showMessageDlg('@COMMON.error@',
								'@NETWORK.renameError@');
					},
					cache : false
				});
	}
</script>
	</head>
	<body class="openWinBody">
		<div class="openWinHeader">
			<!-- 	    <div class="openWinTip">@NETWORK.modifyNmName@</div> -->
			<div class="openWinTip">@NETWORK.modifyDeviceInfo@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT30">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0"
				border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt  w160">@NETWORK.alias@:</td>
						<td ><input
							class="normalInput modifiedFlag w200" id="name"
							value="${entity.name}" maxlength="63"
							toolTip='@COMMON.anotherName@' /></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt  w160">@NETWORK.location@:</td>
						<td ><input
							class="normalInput modifiedFlag w200" id="location"
							value="${entity.location}" maxlength="127"
							toolTip='@COMMON.locationTip@' /></td>
					</tr>
					<tr>
						<td class="rightBlueTxt  w160">@NETWORK.contact@:</td>
						<td ><input
							class="normalInput modifiedFlag w200" id="contact"
							value="${entity.contact}" maxlength="63"
							toolTip='@COMMON.contactTip@' /></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt  w160">@NETWORK.note@:</td>
						<td ><input
							class="normalInput modifiedFlag w200" id="note"
							value="${entity.note}" maxlength="127"
							toolTip='@COMMON.noteTip@' />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="save()" icon="miniIcoData">@BUTTON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick();" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>