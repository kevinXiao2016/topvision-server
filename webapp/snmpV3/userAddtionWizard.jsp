<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module snmpV3
    import snmpV3.javascript.UsmWizardLayout
    import snmpV3.javascript.VacmWizardLayout
    import snmpV3.javascript.ViewWizardComboBox
    import snmpV3.javascript.CloneUserCombo
    import snmpV3.javascript.CloneEngineCombo
</Zeta:Loader>
<style type="text/css">

#w800{width: 790px; overflow: hidden; position: relative; top: 0; left: 0; height: 390px;}
#w2400{ width:2400px; height:100%; overflow:hidden; position:absolute; top:0; left:0px;}
#canvasLayout {position:absolute; top:0; left:0; width:800px;}

#groupLayout ,#viewLayout{position: absolute; top:0px; left:800px; width:800px;}
#viewLayout{ left:1600px;}




.useDefaultTipClazz {}
.useDefaultTipClazz div, .useDefaultTipClazz a {display : none;}
select {width: 200px;}


.loadingmask {width: 16px;height: 16px;position: relative;top: 3px;}
.user-type-radio {width : 300px;height: 20px;}
.user-type-radio li{ float:left; width:70px;}
</style>
</head>
<script type="text/javascript">
function moveLeftFn(para){
	$("#w2400").animate({left:para});
}

function gotoAndPlayNext(){
	if( !checkFirstStep()){
		return;
	}
	moveLeftFn(-800);	
}

  var entityId = ${entityId};
  var thisStep = 0;
  var useExistedGroup = true;
  var thisViewMark ,
      isUseNotifyUser;
  Ext.onReady(function(){
	  doUsmWizardLayout();
	  doVacmWizardLayout();
	  doViewWizardLayout();
	  addEventListener();
  });
/***********************************
                             关闭页面
 ************************************/
function closeHandler(){
    window.parent.closeWindow('userAdditionWizard');
}

function addEventListener(){
	$("#privOldKey").keydown(function(e){
		if(e.keyCode == 9){
			nextHandler();
			return false;
		}
	});
}

function saveCfg(){
	if(!checkFirstStep()){
		gotoAndPlay(0);
		thisStep = 0;
		return;
	}else if(!checkSecondStep()){
		gotoAndPlay(1);
        thisStep = 1;
        return;
	}
	var snmpUserName = $("#username").val();
	var snmpAuthProtocol = Ext.getCmp("authCombo").getValue();
	var snmpAuthKeyChange = $("#authenticationKey").val();
	var snmpPrivProtocol = Ext.getCmp("privCombo").getValue();
	var snmpPrivKeyChange = $("#privacyKey").val();
	var snmpGroupName = Ext.getCmp("vacmGroup").getValue();
	if(!useExistedGroup){
		var securityMode = Ext.getCmp("securityMode").getValue();
		var securityLevel = Ext.getCmp("securityLevelCombo").getValue();
		var readView = Ext.getCmp("readViewCombo").getValue();
		var writeView = Ext.getCmp("writeViewCombo").getValue();
		var notifyView = Ext.getCmp("notifyViewCombo").getValue();
	}
	
	var cloneUser = Ext.getCmp("cloneUserCombo").getValue();
	var cloneEngineId = Ext.getCmp("cloneEngineCombo").getValue();
	var authOldKey = $("#authOldKey").val();
	var privOldKey = $("#privOldKey").val();
	var data = {};
	data.entityId = entityId;
	data.snmpUserName = snmpUserName;
	data.snmpAuthProtocol = snmpAuthProtocol;
	data.snmpAuthPwd = snmpAuthKeyChange;
	data.snmpPrivProtocol = snmpPrivProtocol;
	data.snmpPrivPwd = snmpPrivKeyChange;
	data.snmpGroupName = snmpGroupName;
	data.targetUserName = cloneUser;
	data.targetUserEngineId = cloneEngineId;
	data.targetUserAuthPwd = privOldKey;
	data.targetUserPrivPwd = authOldKey;
	data.useExistedGroup = useExistedGroup;
	if(!useExistedGroup){
		//data.snmpSecurityMode = securityMode;
		data.snmpSecurityLevel = securityLevel;
		data.snmpReadView = readView;
		data.snmpWriteView = writeView;
		data.snmpNotifyView = notifyView;
	}
	if(isUseNotifyUser){
		data.snmpUserEngineId = $("#engineId").val();
    }
	window.parent.showWaitingDlg("@COMMON.wait@","@USER.cfgingUsr@");
	$.ajax({
		url: '/snmp/addUser.tv',cache:false,
		data: data,dataType:"json",
		success:function(json){
			if(json.data == "cloneError"){
				window.parent.showMessageDlg("@COMMON.tip@", "@USER.cloneErTip@");
			}else if(json.data == "activateError"){
				window.parent.showMessageDlg("@COMMON.tip@", "@USER.activateEr@");
			}else if(json.data == "deleteUserError"){
				window.parent.showMessageDlg("@COMMON.tip@", "@USER.cloneOk@");
			}else if(json.data == "accessError"){
				window.parent.showMessageDlg("@COMMON.tip@", "@USER.createGroupEr@");
			}else {
				if(json.data == "dbError"){
					// create ok,insert to db error
					return window.parent.showMessageDlg("@COMMON.tip@", "@USER.cfgUsrEr@");
				}else if(json.data == "groupError"){
					// group-user relation create error
					return window.parent.showMessageDlg("@COMMON.tip@", "@USER.cfgUsrEr@");
				}
				window.parent.showMessageDlg("@COMMON.tip@", "@USER.cfgUsrOk@");
				closeHandler();
			}
		},error:function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@USER.cfgUsrEr@");
		}
	});
}

function prevHandler(){
	gotoAndPlay(--thisStep);
	if(thisStep == 0){
		$("#prevBt").attr("disabled",true).html("@USER.prev@");
		$("#nextBt").attr("disabled",false).html("@USER.next@");
        $("#newMsg").html("@USER.fulfillBasic@");
	}else if(thisStep == 1){
		$("#prevBt").attr("disabled",false).html("@USER.prev@");
		$("#nextBt").attr("disabled",false).html("USER.createUsr");
		$("#newMsg").html("@USER.fulfillGroup@");
	}
}

function gotoAndPlay(step){
    alert(1)
    $("#canvasLayout").animate({"left": -step*480});
}

function createView(combo){
	thisViewMark = combo;
	var viewName = Ext.getCmp(combo + "Combo").getValue();
	$("#viewName").val(viewName);
	/* $("#prevBt").attr("disabled",false).html("@COMMON.return@");
	$("#nextBt").attr("disabled",true); */
	$("#newMsg").html("@USER.plsCreateGroup@");
	//gotoAndPlay(++thisStep);
	moveLeftFn(-1600);
}

function nextHandler(){
	if(thisStep == 0){
		if( !checkFirstStep()){
			return;
		}
		var _auth = Ext.getCmp("authCombo").getValue();
		var _priv = Ext.getCmp("privCombo").getValue();
		window.thisSecurityLevel = 1;
		if(_auth != "NOAUTH"){
			thisSecurityLevel++;
			if(_priv != "NOPRIV"){
				thisSecurityLevel ++ ;
			}
		}
		var store = Ext.getCmp("vacmGroup").getStore();
		//store.setBaseParam("snmpSecurityLevel",thisSecurityLevel);
		//store.setBaseParam("random",Math.random());
	    store.load({
	    	params : {
	    		snmpSecurityLevel: thisSecurityLevel,
	            random : Math.random()
	    	}
	    });
		Ext.getCmp("securityLevelCombo").setValue(thisSecurityLevel);
		gotoAndPlay(++thisStep);
		$("#prevBt").attr("disabled",false).html("@USER.prev@");
		$("#nextBt").attr("disabled",false).html("@USER.createUsr@");
		$("#newMsg").html("@USER.fulfillGroup@");
	}else if(thisStep == 1){
		saveCfg();
	}
}

///////////////  用户名校验 ///////////////////////////
function checkFirstStep(){
	if(!$("#username").val()){
		window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptUsr@", function(){
			$("#username").focus();	
		});
		return false;
	}
	if(isUseNotifyUser && !$("#engineId").val()){
		window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptUsr@", function(){
            $("#engineId").focus(); 
        });
        return false;
	}
	var authPro = Ext.getCmp("authCombo").getValue();
	if(authPro != "NOAUTH"){
		if(!$("#authenticationKey").val()){
			window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptAuth@", function(){
	            $("#authenticationKey").focus();
	        });
	        return false;
		}
    }
	var privPro = Ext.getCmp("privCombo").getValue();
    if(privPro != "NOPRIV"){
        if(!$("#privacyKey").val()){
        	window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptPriv@",function(){
                $("#privacyKey").focus();
            });
            return false;
        }
        //如果只有priv没有auth也不允许
        if(authPro == "NOAUTH"){
        	window.parent.showMessageDlg("@COMMON.tip@", "@USER.noAuthTip@", function(){
        		Ext.getCmp("authCombo").focus();
        	});
            return false;
        }
    }
    var cloneUser =  Ext.getCmp("cloneUserCombo").getValue();
    if(cloneUser == ""){
        window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptCloneUsr@", function(){
            $("#cloneUser").focus();
        });
        return false;
    }
    var cloneEngineId = Ext.getCmp("cloneEngineCombo").getValue();
    if(cloneEngineId == ""){
        window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptCloneId@", function(){
            $("#cloneEngineId").focus();
        });
        return false;
    }
    var authOldKey = $("#authOldKey").val();
    if(authOldKey == ""){
        window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptCloneAuth@", function(){
            $("#authOldKey").focus();
        });
        return false;
    }
    var privOldKey = $("#privOldKey").val();
    if(privOldKey == ""){
        window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptClonePriv@", function(){
            $("#privOldKey").focus();
        });
        return false;
    }
    return true;
}
function checkSecondStep(){
	var group = Ext.getCmp("vacmGroup");
	if(!group.getValue()){
		 window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptGroup@", function(){
			 group.focus();
		
         });
		 return false;
	}else{
		//如果存在的话还要校验视图是否都已经创建完毕，如果没有填写，给提示
		var read = Ext.getCmp("readViewCombo").getValue();
		var write = Ext.getCmp("writeViewCombo").getValue();
		var notify = Ext.getCmp("notifyViewCombo").getValue();
		if(read == "" && write == "" && notify == ""){
		}
	}
	return true;
}

/////验证subtree格式是否满足要求
function checkSubtreeMask(string){
	var reg = /^[\d*.]+$/ig;
    var reg2 = /^[.*]{0,1}[.*]+$/ig;
    if(!reg.exec(string) ||  string.length > 127 || string.indexOf("..") > -1 || reg2.exec(string)){
        window.parent.showMessageDlg("@COMMON.tip@", "@VIEW.wrongSubtree@", function(){
            $("#subtree").focus();
        });
        return false;
    }
    var tmp = string.split("*.");
    for(var a=0; a<tmp.length; a++){
        if(tmp[a].indexOf("*") > -1){
            if(a == tmp.length - 1 && tmp[a].indexOf("*") == tmp[a].length - 1){
                continue;
            }
            window.parent.showMessageDlg("@COMMON.tip@", "@VIEW.wrongSubtree2@", function(){
                $("#subtree").focus();
            });
            return false;
        }
    }
    tmp = string.split(".*");
    for(a=0; a<tmp.length; a++){
        if(tmp[a].indexOf("*") > -1){
            window.parent.showMessageDlg("@COMMON.tip@", "@VIEW.wrongSubtree2@", function(){
                $("#subtree").focus();
            });
            return false;
        }
    }
    tmp = string.split(".");
    for(a=0; a<tmp.length; a++){
        if(typeof tmp[a] == 'string' && tmp[a].length > 10){
            window.parent.showMessageDlg(I18N.COMMON.tip, "@VIEW.subtreeNodeLong@" ,'tip', function(){
                $("#subtree").focus();
            });
            return false;
        }
    }
    return true;
}

///////////////////// 立即添加视图  //////////////////////////////
function addView(){
	var viewName = $("#viewName").val();
	var _subtree = $("#subtree").val();
	var viewMode = Ext.getCmp("viewModeCombo").getValue();
	if(!_subtree){
		 window.parent.showMessageDlg("@COMMON.tip@", "@USER.plsIptFilter@", function(){
             $("#subtree").focus();
         });
		 return;
	}
	if(!checkSubtreeMask(_subtree)){
		return;
	}
	var rear = _subtree.substring(_subtree.length-1);
	if(rear == "."){
        window.parent.showMessageDlg("@COMMON.tip@",  "@VIEW.filterWrong@",'error',function(){
            $("#subtree").focus();
        });
        return;
    }
	window.parent.showWaitingDlg("@COMMON.wait@",String.format("@USER.cfgingViewFormat@", viewName));
	$.ajax({
		url:'/snmp/addView.tv',cache:false,
		data:{
			entityId : entityId,
			snmpViewName : viewName,
			snmpViewSubtree : _subtree,
			snmpViewMode : viewMode
		},success:function(){
			window.parent.showMessageDlg("@COMMON.tip@", String.format("@USER.createViewOk@", viewName),function(){
				Ext.getCmp("readViewCombo").getStore().reload();
				Ext.getCmp("writeViewCombo").getStore().reload();
				Ext.getCmp("notifyViewCombo").getStore().reload();
				//Ext.getCmp("readViewCombo").fireEvent("blur");
				//Ext.getCmp("writeViewCombo").fireEvent("blur");
				//Ext.getCmp("notifyViewCombo").fireEvent("blur");
				$("#"+thisViewMark+"Tip").hide();
				prevHandler();
			});
		},error : function(){
			 window.parent.showMessageDlg("@COMMON.tip@", String.format("@USER.createViewEr@", viewName));
		}
	});
}
function willUseNotifyUser(bool){
	window.isUseNotifyUser = bool;
	if(bool){
		$("#engineIdItem").show();
	}else{
		$("#engineIdItem").hide();
	}
}


</script>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">@USER.userBasicInfo@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
		
	<div id="w800">
		<div id="w2400">
			<div id="canvasLayout">
				<!-- USER CONFIG LAYOUT -->
				<div id="bodyLayoutUser"  class="pT20 pL10">
					<table id="table1" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">						
						<tr>
							<td class="rightBlueTxt">@USER.user@</td>
							<td colspan="3">
								<input id="username" class="normalInput" maxlength="31" tooltip="@USER.userNameTip@" style="width:200px;" />
							</td>
						</tr>
						<tr>
		                    <td class="rightBlueTxt">@USER.userType@</td>
		                    <td colspan=3>
		                    	<ul class="user-type-radio">
			                      	<li><input type="radio" name="userType" onclick="willUseNotifyUser(true);"/>@USER.notifyUser@</li>
			                      	<li><input type="radio" name="userType" checked="checked" onclick="willUseNotifyUser(false);" />@USER.accessUser@</li>
		                      	</ul>
		                    </td>
		                </tr>
						<tr style="display: none;" id="engineIdItem">
		                    <td class="rightBlueTxt">Engine ID:</td>
		                    <td  colspan=3>
		                       <input id="engineId" class="normalInput"  type="text" style="width:200px;" tooltip="@USER.plsIptTrapeServer@" />
		                    </td>
		                </tr>
						<tr>
							<td class="rightBlueTxt">@USER.authPro@</td>
							<td >
								<select id="authProtocol" style="width:200px;">
										<option>NOAUTH</option>
										<option>MD5</option>
										<option>SHA</option>
								</select></td>
							<td class="rightBlueTxt" id="authLable">@USER.authPass@</td>
							<td >
							    <Zeta:Password width="200px" disabled="true" id="authenticationKey"  maxlength="31" tooltip="@USER.userPassTip@" /></td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@USER.privPro@</td>
							<td class="valueClazz">
								<select id="privProtocol" style="width:200px;">
										<option>NOPRIV</option>
										<option>CBC-DES</option>
								</select></td>
							<td class="rightBlueTxt" id="privLable">@USER.privPass@</td>
							<td >
							   <Zeta:Password  width="200px" disabled="true" id="privacyKey"  maxlength="31"  tooltip="@USER.userPassTip@"/></td>
						</tr>
						<tr>
		                    <td class="rightBlueTxt" id="cloneUserLable">@USER.cloneUsr@</td>
		                    <td  colspan=3>
		                        <div id="cloneUser"></div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="rightBlueTxt" id="cloneUserEngine">@USER.cloneEngine@</td>
		                    <td  colspan=3>
		                        <div id="cloneEngineId"></div>
		                    </td>
		                </tr>
		                <tr>
		                    <td id="authOldLable" class="rightBlueTxt" width="110">@USER.authPass@</td>
		                    <td class="valueClazz">
		                        <Zeta:Password width="200px" id="authOldKey"  maxlength="31" tooltip="@USER.authOldTip@"/></td>
		                    <td id="privOldLable" class="rightBlueTxt">@USER.privPass@</td>
		                    <td class="valueClazz">
		                        <Zeta:Password width="200px" id="privOldKey"  maxlength="31" tooltip="@USER.privOldTip@"/></td>
		                </tr>		                
					</table>
					<div class="noWidthCenterOuter clearBoth" >
						<ol class="upChannelListOl pT20 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig disabledAlink"><span><i class="miniIcoArrLeft"></i>@COMMON.prev@</span></a></li>	
						<li><a href="javascript:;" class="normalBtnBig" onclick="gotoAndPlayNext()"><span><i class="miniIcoArrRight"></i>@COMMON.next@</span></a></li>			
						<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
						</ol>
					</div>					
				</div>
			
			
			
			
			<!-- GROUP CONFIG LAYOUT -->
			<div id="groupLayout">
				<div class="edge10 pT20">
					<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td class="rightBlueTxt" width="260">@GROUP.accessName@:</td>
							<td width="220">
							   <div id="group"></div>
							</td>
							<td class="useDefaultTipClazz">
								<div id="groupTip">@GROUP.groupTip@:</div>
							</td>
						</tr>
						 <tr class="darkZebraTr">
							<td class="rightBlueTxt" >@GROUP.securityMode@</td>
							<td class="" colspan=2>
							   <select id="securityMode">
		                           <option>v3</option>
		                       </select>
							</td>
						</tr>
						<tr>
		                    <td class="rightBlueTxt" >@GROUP.securityLevel@:</td>
		                    <td class="" colspan=2>
		                       <select id="securityLevel">
		                           <option value="1">NOAUTH_NOPRIV</option>
		                           <option value="2">AUTH_NOPRIV</option>
		                           <option value="3">AUTH_PRIV</option>
		                       </select>
		                    </td>
		                </tr>
						<tr class="darkZebraTr">
		                    <td class="rightBlueTxt"   >@GROUP.readView@</td>
		                    <td class="" >
		                       <div id="readView"></div>
		                    </td>
		                    <td class="useDefaultTipClazz">
		                        <a id="readViewTip" href="javascript:void(0)"  onclick="createView('readView');">@USER.clickMe@</a>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="rightBlueTxt"  >@GROUP.writeView@</td>
		                    <td class="" >
		                       <div id="writeView"></div>
		                    </td>
		                    <td class="">
		                        <a id="writeViewTip" href="javascript:void(0)" onclick="createView('writeView');" class="yellowLink">@USER.clickMe@</a>
		                    </td>
		                </tr>
		                <tr class="darkZebraTr">
		                    <td class="rightBlueTxt" >@GROUP.notifyView@</td>
		                    <td class="" >
		                       <div id="notifyView"></div>
		                    </td>
		                    <td>
		                        <a id="notifyViewTip" href="javascript:void(0)" onclick="createView('notifyView');" class="yellowLink">@USER.clickMe@</a>
		                    </td>
		                </tr>
					</table>
				</div>
				<div class="noWidthCenterOuter clearBoth" >
					<ol class="upChannelListOl pT40 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig" onclick="moveLeftFn(0)"><span><i class="miniIcoArrLeft"></i>@COMMON.prev@</span></a></li>	
						<li><a href="javascript:;" class="normalBtnBig"  onclick="saveCfg();"><span><i class="miniIcoUser"></i>@USER.createUsr@</span></a></li>			
						<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
					</ol>
				</div>
			</div>
			<!-- GROUP CONFIG LAYOUT -->
	        <div id="viewLayout">
	        	<div class="edge10 pT40">
		            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		                <tr>
		                    <td class="rightBlueTxt" width="260">@VIEW.viewName@</td>
		                    <td>
		                       <input id="viewName"  class="normalInputDisabled w200" readonly="true" disabled="true" />
		                    </td>
		                </tr>
		               <tr class="darkZebraTr">
		                    <td class="rightBlueTxt">@VIEW.filterCondition@</td>
		                    <td>
		                       <input id="subtree"   class="normalInput w200"  tooltip="@VIEW.filterConditionTip@"/>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="rightBlueTxt" >@VIEW.execAction@</td>
		                    <td>
		                       <select id="viewMode">
		                           <option value="1" >@VIEW.include@</option>
		                           <option value="2">@VIEW.exclude@</option>
		                       </select>
		                    </td>
		                </tr>
		            </table>		            
		            <div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB20 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="moveLeftFn(-800)"><span><i class="miniIcoArrLeft"></i>@COMMON.prev@</span></a></li>
					         <li><a id="addView" href="javascript:;" class="normalBtnBig" onclick="addView()"><span><i class="miniIcoSaveOK"></i>@VIEW.createView@</span></a></li>				         
					     </ol>
					</div>
		            <div class="yellowTip mT10" >@VIEW.viewNotation@</div>
	            </div>
	        </div>
		</div>
		</div>
	</div>
</body>
</Zeta:HTML>