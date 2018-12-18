<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module snmpV3
    import snmpV3.javascript.ViewWizardComboBox
</Zeta:Loader>
<script type="text/javascript">
  var entityId = ${entityId};
  var thisStep = 0;
  var useExistedGroup = true;
  var securityLv = '${access.snmpSecurityLevel}';
  $(document).ready(function(){
	  if(securityLv != ""){
		  $("#groupHeader").html( "@GROUP.mdfGroup@" );
		  $("#nextBt").html( "@COMMON.modify@" );
		  $("#securityLevel").val(securityLv).attr("disabled",true);
		  $("#group").attr("disabled",true);
	  }
	  var readView = new ViewWizardComboBox({
	        id : "readViewCombo",
	        lazyInit : false,
	        disabled : false,
	        renderTo : "readView",
	        listeners:{
	        	'render':function(){
	        		this.setValue('${access.snmpReadView}');
	        	}
	        }
	    });
	    var writeView = new ViewWizardComboBox({
	        id : "writeViewCombo",
	        lazyInit : false,
	        disabled : false,
	        renderTo : "writeView",
	        listeners:{
                'render':function(){
                    this.setValue('${access.snmpWriteView}');
                }
            }
	    });
	    var notifyView = new ViewWizardComboBox({
	        id : "notifyViewCombo",
	        lazyInit : false,
	        disabled : false,
	        renderTo : "notifyView",
	        listeners:{
                'render':function(){
                    this.setValue('${access.snmpNotifyView}');
                }
            }
	    });
  });
/***********************************
                             关闭页面
 ************************************/
function closeHandler(){
    window.parent.closeWindow('userAdditionWizard');
}


function saveHandler(){
	var snmpGroupName = $("#group").val();
	var snmpSecurityLevel = $("#securityLevel").val();
	var snmpReadView = Ext.getCmp("readViewCombo").getValue();
	var snmpWriteView = Ext.getCmp("writeViewCombo").getValue();
	var snmpNotifyView = Ext.getCmp("notifyViewCombo").getValue();
	if(securityLv != ""){
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.GROUP.mdfingGroup, 'ext-mb-waiting');
		$.ajax({
	        url : '/snmp/modifyAccess.tv',cache:false,
	        data:{
	            entityId: entityId,
	            snmpGroupName : snmpGroupName,
	            snmpSecurityLevel : snmpSecurityLevel,
	            snmpReadView :snmpReadView,
	            snmpWriteView :snmpWriteView,
	            snmpNotifyView :snmpNotifyView
	        },success:function(){
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GROUP.mdfGroupOk);
	            closeHandler.apply(this);
	        },error:function(){
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GROUP.mdfGroupEr);
	        }
	    });
	}else{
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.GROUP.creatingGroup, 'ext-mb-waiting');
		$.ajax({
	        url : '/snmp/addAccess.tv',cache:false,
	        data:{
	            entityId: entityId,
	            snmpGroupName : snmpGroupName,
	            snmpSecurityLevel : snmpSecurityLevel,
	            snmpReadView :snmpReadView,
	            snmpWriteView :snmpWriteView,
	            snmpNotifyView :snmpNotifyView
	        },success:function(){
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GROUP.createGroupOk);
	            closeHandler.apply(this);
	        },error:function(){
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GROUP.createGroupEr);
	        }
	    });
	}
}
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@GROUP.creategroup@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
    <div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows" >
                <tr>
                    <td class="w220 rightBlueTxt">@GROUP.accessName@</td>
                    <td class="valueClazz" colspan="2">
                       <input id="group" maxlength="31"  class="normalInput" value='${access.snmpGroupName}' tooltip="@GROUP.groupNameTip@" />
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt" >@GROUP.securityMode@</td>
                    <td class="valueClazz" colspan=2>
                       <select id="securityMode" class="normalSel w133" >
                           <option>v3</option>
                       </select>
                    </td>
                </tr>
                <tr>
                    <td class="rightBlueTxt" >@GROUP.securityLevel@</td>
                    <td class="valueClazz" colspan=2>
                       <select id="securityLevel" class="normalSel" >
                           <option value="1">NOAUTH_NOPRIV</option>
                           <option value="2">AUTH_NOPRIV</option>
                           <option value="3">AUTH_PRIV</option>
                       </select>
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt">@GROUP.readView@</td>
                    <td class="valueClazz" >
                       <div id="readView"></div>
                    </td>
                </tr>
                <tr>
                    <td class="rightBlueTxt">@GROUP.writeView@</td>
                    <td class="valueClazz" >
                        <div id="writeView"></div>
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt">@GROUP.notifyView@</td>
                    <td class="valueClazz">
                       <div id="notifyView"></div>
                    </td>
                </tr>
            </table>
            </form>
        </div>
     <Zeta:ButtonGroup>
		<Zeta:Button onClick="saveHandler()" icon="miniIcoAdd">@COMMON.add@</Zeta:Button>
		<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>