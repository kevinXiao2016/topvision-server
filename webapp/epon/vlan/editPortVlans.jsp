<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
    import epon.vlan.oltVlanUtil
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${entityId}';
var portIndexsStr = '${portIndexsStr}';
var taggedVlanStr = '${taggedVlanStr}';
var unTaggedVlanStr = '${unTaggedVlanStr}';
var vlanList;

$(function(){
	$('#taggedVlans').val(taggedVlanStr);
	$('#unTaggedVlans').val(unTaggedVlanStr);
	//获取该OLT的VLAN列表，用于校验
	loadVlanList();
})

/**
 * 加载该设备下VLAN列表
 */
function loadVlanList(){
    $.ajax({
        url: '/epon/vlanList/loadVlanList.tv',
        type: 'POST',
        data: {
            entityId: entityId
        },
        dataType: "json",
        success: function(vlanJson) {
            vlanList = vlanJson;
        },
        error: function(vlanJson) {
            window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanLoadError@');
        },
        cache: false,
        complete : function(XHR, TS) {
            XHR = null
        }
    });
}

/**
 * 判断指定VLAN是否已经创建
 * @param vlanId
 * @returns {Boolean} 是否已经创建
 */
function isVlanExist(vlanId){
    var exist = false;
    for(var i=0; i< vlanList.length; i++){
        if(vlanList[i].vlanIndex == vlanId){
            exist = true;
            break;
        }
    }
    return exist;
}

function saveSniVlans(){
	var taggedVlans = $('#taggedVlans').val(),
		unTaggedVlans = $('#unTaggedVlans').val();
	if(taggedVlans && !isVlanIdStrValid(taggedVlans)){
		$('#taggedVlans').focus();
		return;
	}
	if(unTaggedVlans && !isVlanIdStrValid(unTaggedVlans)){
		$('#unTaggedVlans').focus();
		return;
	}
	//将输入框转换成数组形式
	var taggedVlanArray = convertVlanStrToArray(taggedVlans),
		unTaggedVlanArray = convertVlanStrToArray(unTaggedVlans);
	
	//判断VLAN是否存在
	var noExistVlanIds = [];
	$.each(taggedVlanArray, function(i, vlanId){
		if(!isVlanExist(vlanId)){
			noExistVlanIds.push(vlanId);
		}
	});
	$.each(unTaggedVlanArray, function(i, vlanId){
        if(!isVlanExist(vlanId)){
            noExistVlanIds.push(vlanId);
        }
    });
	if(noExistVlanIds.length){
		return top.showMessageDlg('@COMMON.tip@', String.format('@VLAN.vlanCountTip@', convertVlanArrayToAbbr(noExistVlanIds)));
	}
	
	//判断是否有冲突
	var confilctCheckRet = vlanModeConflictCheck(taggedVlans, unTaggedVlans);
	if(confilctCheckRet.conflict){
		return top.showMessageDlg('@COMMON.tip@', '@VLAN.vlanConflict@: </br>' + convertVlanArrayToAbbr(confilctCheckRet.conflictIds));
	}
	
	//window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.editBelongVlaning@', 'ext-mb-waiting');
	
	top.executeLongRequeset({
        url: "/epon/vlanList/modifyPortVlans.tv",
        message: '@VLAN.editBelongVlaning@',
        data: {
            entityId: entityId,
            portIndexsStr: portIndexsStr,
            taggedVlanStr: taggedVlanArray.join(','),
            unTaggedVlanStr: unTaggedVlanArray.join(',')
        },
        requestHandler: function(){
        	//top.closeWaitingDlg();
            top.afterSaveOrDelete({        
                title: '@COMMON.tip@',        
                html: '<b class="orangeTxt">@VLAN.editBelongVlanSuccess@</b>'    
            });
            cancelClick();
        },
        requestErrorHandler: function(json){
        	top.showMessageDlg('@COMMON.tip@', '@VLAN.editVlanFailed@');
        }
    });
	
	/* $.ajax({
		url : "/epon/vlanList/modifyPortVlans.tv",
		data: {
			entityId: entityId,
			portIndexsStr: portIndexsStr,
			taggedVlanStr: taggedVlanArray.join(','),
			unTaggedVlanStr: unTaggedVlanArray.join(',')
		},
		dataType : "json",
		cache : false,
		success : function(json) {
			top.closeWaitingDlg();
			top.afterSaveOrDelete({        
				title: '@COMMON.tip@',        
				html: '<b class="orangeTxt">@VLAN.editBelongVlanSuccess@</b>'    
			});
			cancelClick();
		},
		error : function(json) {
			top.showMessageDlg('@COMMON.tip@', json.error || '@VLAN.editVlanFailed@');
		}
		
	}); */
}

function cancelClick(){
	 top.closeWindow("editVlans");
 }
</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@VLAN.editBelongsVlan@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt w150">TAGGED VLAN:</td>
					<td><input type=text id="taggedVlans" maxlength=31 class="normalInput w300" tooltip="@VLAN.batchStrRule@" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt w150">UNTAGGED VLAN:</td>
					<td><input type=text id="unTaggedVlans" maxlength=31 class="normalInput w300" tooltip="@VLAN.batchStrRule@" /></td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="saveSniVlans()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>