<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<fmt:setBundle basename="com.topvision.ems.cm.resources" var="cm"/>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    PLUGIN  LovCombo
    MODULE  CM
</Zeta:Loader>
<style type="text/css">
#pollTaskName,#pollInterval{width:218px;}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cm.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pollTaskName = '<s:property value="pollTaskName"/>';
var pollInterval = '<s:property value="pollInterval"/>';
var quotaIds = '<s:property value="quotaIds"/>';
var objectIds = '<s:property value="objectIds"/>';
var monitorId = '<s:property value="monitorId"/>';
function cancelClick(){
	window.parent.closeWindow('modifyCmPoll');
}

function isDigit(s) 
{ 
var patrn=/^[0-9]{1,20}$/; 
if (!patrn.exec(s)) return false 
return true 
}

function okClick(){
	var pollTaskName = $("#pollTaskName").val();
	var pollInterval = $("#pollInterval").val();
	var quotaIds = Ext.getCmp("quotaSelect").getCheckedValue();
	//var objectType = $("#objectTypeSelect").val();
	var objectIds = Ext.getCmp("objectSelect").getCheckedValue();
	
	if(pollTaskName == '' | pollTaskName == null){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseEnterPollTaskName);
		$("input#pollTaskName").focus();
		return;
	}
	if(pollTaskName.length>32){
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollTaskNameNotLongerThan32);
		$("input#pollTaskName").focus();
		return false;
	}
	if(pollInterval == '' | pollInterval == null){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseEnterPollInterval);
		$("input#pollInterval").focus();
		return;
	}
	if(!isDigit(pollInterval) || parseInt(pollInterval) < 1 ||  parseInt(pollInterval) > 44640){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseEnterRightPollInterval);
		$("input#pollInterval").focus();
		return;
	}
	if(quotaIds == '' | quotaIds == null){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChoosePollQuota);
		top.afterSaveOrDelete({       
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.pleaseChoosePollQuota + '</b>'    
		});
		return;
	}
	if(objectIds == '' | objectIds == null){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChoosePollObject);
		top.afterSaveOrDelete({       
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.pleaseChoosePollObject + '</b>'    
		});
		return;
	}
	
	$.ajax({
		url: '/cmpoll/modifyCmPoll.tv',
	      type: 'post',
	      data: 'pollTaskName=' + pollTaskName + '&monitorId=' + monitorId +'&pollInterval=' + pollInterval + '&quotaIds=' + quotaIds + '&objectIds=' + objectIds,
	      success: function(response) {
	    		if(response.exist){
    				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollObjectAllreadyInPollTask);
	    			top.afterSaveOrDelete({       
	    				title: I18N.COMMON.tip,        
	    				html: '<b class="orangeTxt">' + I18N.cmPoll.pollObjectAllreadyInPollTask + '</b>'    
	    			});
    			}else if(response.nameExist){
	    			//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollTaskNameAlreadyExist);
	    			top.afterSaveOrDelete({       
	    				title: I18N.COMMON.tip,        
	    				html: '<b class="orangeTxt">' + I18N.cmPoll.pollTaskNameAlreadyExist + '</b>'    
	    			});
	    		}else if(response.success){
	    			top.afterSaveOrDelete({       
	    				title: I18N.COMMON.tip,        
	    				html: '<b class="orangeTxt">' + I18N.COMMON.modifySuccess + '</b>'    
	    			});
	    			/* try{
	    				top.nm3kObj.nm3kSaveOrDelete.showTime = 0;
	    			}catch(err){
	    				
	    			} */
	  	    		window.top.getActiveFrame().onRefreshClick();
	  	    		cancelClick();
	  	    	}else{
	  	    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.modifyFail);
	  	    		cancelClick();
	  	    	}
	    	
	    		
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.modifyFail);
				cancelClick();
			}, cache: false
	});
}

Ext.onReady(function() {
	var quotaCombox = new Ext.ux.form.LovCombo({
		fieldLabel: I18N.cmPoll.pollQuota,
        id : 'quotaSelect',  
        renderTo : "objectTypeSelectCt",
        hideOnSelect : true,  
        editable : false,  
        width: 220,
        store : new Ext.data.JsonStore({  
        	url: "/cmpoll/getCmPollQuota.tv",  
            root: 'data',  
            autoLoad: true,  
            listeners:{
            	load : function(){
            		Ext.getCmp("quotaSelect").setValue(quotaIds);
            	}
            },
            fields: ["quotaId", "quotaDisplayName"]  
        }),
        displayField : 'quotaDisplayName',  
        valueField : 'quotaId',  
        triggerAction : 'all',  
        emptyText : I18N.cmPoll.addQuota,  
        mode : 'local',
        beforeBlur:function(){}
	})
	var objectCombox = new Ext.ux.form.LovCombo({
		fieldLabel: I18N.cmPoll.pollObject,
		renderTo : "objectSelectCt",
        name : 'objectSelect',  
        id : 'objectSelect',  
        hideOnSelect : true,  
        editable : false,  
        width: 220,
        store : new Ext.data.JsonStore({  
        	url: "/cmpoll/loadPollObjectList.tv",  
            root: 'data',  
            autoLoad: true,  
            listeners:{
            	load : function(){
            		Ext.getCmp("objectSelect").setValue(objectIds);
            	}
            },
            fields: ["objectId", "objectName"]  
        }),
        displayField : 'objectName',  
        valueField : 'objectId',  
        triggerAction : 'all',  
        emptyText : I18N.cmPoll.addPollObject,  
        mode : 'local',
        beforeBlur:function(){}
	})
	
	//初始化数据
	$("#pollTaskName").val(pollTaskName);
	$("#pollInterval").val(pollInterval / 60000);
})
</script>
</head>
<body class=openWinBody>
	<div class="openWinHeader">
		<div class="openWinTip">@cmPoll.modifyPollTask@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
	<div class=formtip id=tips style="display: none"></div>
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="rightBlueTxt w200"><label for="pollTaskName">@cmPoll.pollTaskName@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInput" id="pollTaskName" tooltip='<fmt:message bundle='${cm}' key='cmPoll.pollTaskNameNotLongerThan32'/>'/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt"><label for="pollInterval">@cmPoll.pollIntervalMinute@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInput" id="pollInterval" tooltip='<fmt:message bundle='${cm}' key='cmPoll.pollIntervalNotLongerThan31Day'/>'/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label for="ip">@cmPoll.pollObjectType@<font color=red>*</font></label></td>
					<td><div id="objectTypeSelectCt"></div></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt"><label for="ip">@cmPoll.pollObject@<font color=red>*</font></label></td>
					<td><div id="objectSelectCt"></div></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 第三部分，按钮组合 -->
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick();" icon="miniIcoEdit">@COMMON.edit@</Zeta:Button>
		<Zeta:Button onClick="cancelClick();" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>