<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    import js.tools.ipText
    module epon
</Zeta:Loader>
<script type="text/javascript">
var ponListObject = ${ponListObject};
var oltPonAuthModeObject = ${oltPonAuthModeObject};
var ponIndex = ${ponIndex}
var entityId = ${entityId}
var entityTypes = ${entityTypes};
var onuIndex = '${onuIndex}'
var mac = '${mac}'
var sn = '${sn}'
var password = '${password}'
var authMode;
function closeClick() {
	window.parent.closeWindow('addOnuAuth');
}

function save(){
	var onuId = Zeta$('onuIndex').value;
	var ponIndex = Zeta$('ponIndex').value;
	var authAction = Zeta$('action').value;
	var authMode = Zeta$('authType').value;
	var onuPreType = Zeta$('entityType').value;
	var mac = Zeta$('mac').value;
	var sn = Zeta$('sn').value;
	var password = Zeta$('password').value;
	var onuLevel = Zeta$('onuLevel').value;
	window.parent.showWaitingDlg("@COMMON.wait@", '@onuAuth.adddingAuth@','waitingMsg','ext-mb-waiting');
    $.ajax({
        url:'/epon/onuauth/addOnuAuth.tv',
        type:'POST',
        data:{entityId:entityId, onuId:onuId,ponIndex:ponIndex,authType:authMode, onuIndex:onuIndex
        		, authAction:authAction, mac:mac, sn:sn,password:password, onuPreType:onuPreType,onuLevel:onuLevel},
        dateType:'json',
        success:function(response) {
        	window.parent.closeWaitingDlg();
            if (response == "success") {
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">' + '@onuAuth.addAuthSuccess@' + '</b>'
       			});
            	if(window.parent.getFrame("onuAuthManage") != null){
            		window.parent.getFrame("onuAuthManage").allStoreReload();
            		//window.parent.getFrame("onuAuthManage").showTab(1);
            	}
            	if(window.parent.getFrame("onuAuthFail") != null){
            		window.parent.getFrame("onuAuthFail").refreshClick();
            	}
            	closeClick();
            } else {
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">' + '@onuAuth.addAuthFail@' + '</b>'
       			});
            }
        },
        error:function() {
        },
        cache:false
    });
}

function renderPon(ponIndex){
	var ponIndex = parseInt(ponIndex / 256) + (ponIndex % 256);
	return ((ponIndex & 0xFF000000) >> 24) + '/' + ((ponIndex & 0xFF0000) >> 16);
}


$(function(){
	var ponIndexPosition = Zeta$('ponIndex');
	if(ponListObject != null){
		var size = ponListObject.length;
		for(var i = 0; i < size; i++){
			var option = document.createElement('option');
            option.value = ponListObject[i];
            option.text = renderPon(ponListObject[i]);
            try {
            	ponIndexPosition.add(option, null);
            } catch(ex) {
            	ponIndexPosition.add(option);
            }
		}
	}
	if(ponIndex > 0){
		Zeta$('ponIndex').value = ponIndex
	}else{
		Zeta$('ponIndex').value = ponListObject[0]
	}
	ponIndexChange();
	authTypeChange()
	if(ponIndex != 0) {
		$('#ponIndex').attr("disabled",true);
	}
	if(mac != null){
		Zeta$('mac').value = mac
	}
	if(sn != null){
		Zeta$('sn').value = sn
	}
	if(password != null){
		Zeta$('password').value = password
	}
	buildEntityTypeSelect();
})

function ponIndexChange(){
	var ponIndex = Zeta$('ponIndex').value;
	var action = Zeta$('action').value;
	$.ajax({
        url:'/epon/onuauth/getAuthOnuId.tv',
        type:'POST',
        data:{entityId: entityId, authAction: action, ponIndex: ponIndex},
        type: 'POST',
        async:false,
        cache : false,
        dataType:'json',
        success:function(response) {
        	var onuIndexPosition = Zeta$('onuIndex');
        	for (var i = 1,len = onuIndexPosition.length; i <= len; i++)
            {
        		onuIndexPosition.options[0] = null;
            }
        	if(response == null || response.data == null){
        		createOnuSelect(action, null)
        	}else{
        		createOnuSelect(action, response.data)
        	}
        },
        error:function() {
        },
        cache:false
    });
	
	if(oltPonAuthModeObject != null){
		var size = oltPonAuthModeObject.length;
		for(var i = 0; i < size; i++){
			if(oltPonAuthModeObject[i].ponIndex == Zeta$('ponIndex').value){
				Zeta$('authMode').value = renderAuthMode(oltPonAuthModeObject[i].topPonOnuAuthMode)
				authMode = oltPonAuthModeObject[i].topPonOnuAuthMode;
				if(oltPonAuthModeObject[i].topPonOnuAuthMode == 2){
					Zeta$('authType').value = 1
					$("#authType").attr("disabled",true);
				}else if(oltPonAuthModeObject[i].topPonOnuAuthMode == 4 || oltPonAuthModeObject[i].topPonOnuAuthMode == 5){
					$("#authType").attr("disabled",true);
					Zeta$('authType').value = 2
				}else{
					$("#authType").attr("disabled",false);
				}
			}
		}
	}
	authTypeChange()
}

function actionChange(){
	ponIndexChange()
}

function authTypeChange(){
	var authType = Zeta$('authType').value,
	    $tb = $("#mainTb");
	if(authType == 1){
		$tb.find("tbody").css({display:''});
		$tb.find("tbody:eq(2)").css({display:'none'});
	}else{
		$tb.find("tbody").css({display:''});
		$tb.find("tbody:eq(1)").css({display:'none'});
	}
}

function buildEntityTypeSelect(){
	var deviceTypePosition = Zeta$('entityType');
	for(var i = 0; i < entityTypes.length; i++){
		if(entityTypes[i].typeId == 255 || entityTypes[i].typeId == 13100){
			continue;
		}
        var option = document.createElement('option');
        option.value = entityTypes[i].typeId;
        option.text = entityTypes[i].displayName;
        try {
        	deviceTypePosition.add(option, null);
        } catch(ex) {
        	deviceTypePosition.add(option);
        }
  }
}


function renderAuthMode(authMode){
	if(authMode == 1){
		return "@onuAuth.autoMode@";
	}else if(authMode == 2){
		return "@onuAuth.mac@";
	}else if(authMode == 3){
		return "@onuAuth.mix@";
	}else if(authMode == 4){
		return "@onuAuth.sn@";
	}else if(authMode == 5){
		return "@onuAuth.snPwdMode@";
	}
}

function createOnuSelect(action, onuIds){
	var onuIndexPosition = Zeta$('onuIndex');
	var ponIndex = Zeta$('ponIndex').value;
	for (var i = 1,len = onuIndexPosition.length; i <= len; i++)
    {
		onuIndexPosition.options[0] = null;
    }
	if(onuIds == null){
		if(action == 1){
	   		for(var i = 1; i < 65; i++){
	   			var option = document.createElement('option');
	   	           option.value = i;
	   	           option.text = renderPon(ponIndex) + ":" + i;
	   	           try {
	   	        	   onuIndexPosition.add(option, null);
	   	           } catch(ex) {
	   	        	   onuIndexPosition.add(option);
	   	           }
	   		}
	   	}else{
	   		for(var i = 129; i < 193; i++){
	   			var option = document.createElement('option');
	   	           option.value = i;
	   	           option.text = renderPon(ponIndex) + ":" + i;
	   	           try {
	   	        	   onuIndexPosition.add(option, null);
	   	           } catch(ex) {
	   	        	   onuIndexPosition.add(option);
	   	           }
	   		}
	   	}
	}else{
		if(action == 1){
	   		for(var i = 1; i < 65; i++){
	   			if(onuIds.indexOf(i) > -1){
	   				continue;
	   			}
	   			var option = document.createElement('option');
	   	           option.value = i;
	   	           option.text = renderPon(ponIndex) + ":" + i;
	   	           try {
	   	        	   onuIndexPosition.add(option, null);
	   	           } catch(ex) {
	   	        	   onuIndexPosition.add(option);
	   	           }
	   		}
	   	}else{
	   		for(var i = 129; i < 193; i++){
	   			if(onuIds.indexOf(i) > -1){
	   				continue;
	   			}
	   			var option = document.createElement('option');
	   	           option.value = i;
	   	           option.text = renderPon(ponIndex) + ":" + i;
	   	           try {
	   	        	   onuIndexPosition.add(option, null);
	   	           } catch(ex) {
	   	        	   onuIndexPosition.add(option);
	   	           }
	   		}
	   	}
	}
}

</script>
</head>
<body class="openWinBody">
<div class="formtip" id="tips" style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@onuAuth.addAuth@</div>
	    <div class="rightCirIco pageCirIco"></div> 
	</div>
	<div class="edgeTB10LR20 pT10">
	    <table class="zebraTableRows" id="mainTb">
	    	<tbody>
	            <tr>
	                <td class="rightBlueTxt w160">@VLAN.ponPort@@COMMON.maohao@</td>
	                <td>
	                	<select id="ponIndex" name="ponIndex" class="normalSel" style="width: 220px;" onchange="ponIndexChange()">
						</select>
	                </td>
	            </tr>
	            <tr   class="darkZebraTr">
	                <td class="rightBlueTxt w160">@onuAutoUpg.pro.onuLocation@@COMMON.maohao@</td>
	                <td>
	                	<select id="onuIndex" name="onuIndex" class="normalSel" style="width: 220px;" onchange="">
						</select>
	                </td>
	            </tr>	 
	            <tr>
	                <td class="rightBlueTxt  w160">@onuAuth.ponMode@@COMMON.maohao@</td>
	                <td>
	                	<input class="normalInput modifiedFlag w220 macClass" id="authMode" disabled/>
	                </td>
	            </tr>	
	            
	            <tr  class="darkZebraTr">
	                <td class="rightBlueTxt w160">@onuAuth.authType@@COMMON.maohao@</td>
	                <td>
	                    <select id="authType" name="authType" class="normalSel" style="width: 220px;" onchange="authTypeChange()">
							<option value="1">@onuAuth.mac@</option>
							<option value="2">@onuAuth.sn@</option>
						</select>
	                </td>
	            </tr>	
	            
	            <tr>
	                <td class="rightBlueTxt w160">@onuAuth.preType@@COMMON.maohao@</td>
	                <td>
	                	<select id="entityType" name="entityType" class="normalSel macClass" style="width: 220px;">
							<option value="0">NONE</option>
						</select>
	                </td>
	            </tr> 
	       </tbody>
	       <tbody>
	            <tr  class="darkZebraTr">
	                <td class="rightBlueTxt w160 macClass">MAC@COMMON.maohao@</td>
	                <td>
	                	<input class="normalInput modifiedFlag w220 macClass" id="mac"/>
	                </td>
	            </tr>	
	            
	            <tr>
	                <td class="rightBlueTxt w160 macClass">@onuAuth.action@@COMMON.maohao@</td>
	                <td>
	                	<select id="action" name="action" class="normalSel macClass" style="width: 220px;" onchange="actionChange()">
							<option value="1">@onuAuth.permit@</option>
							<option value="2">@onuAuth.reject@</option>
						</select>
	                </td>
	            </tr>	
	        </tbody>
	        <tbody>    
	             <tr  class="darkZebraTr">
	                <td class="rightBlueTxt w160 snClass">SN@COMMON.maohao@</td>
	                <td>
	                	<input class="normalInput modifiedFlag w220 snClass" id="sn"/>
	                </td>
	            </tr>	  
	            
	            <tr>
	                <td class="rightBlueTxt w160 snClass">@onuAuth.password@@COMMON.maohao@</td>
	                <td>
	                	<input class="normalInput modifiedFlag w220 snClass" id="password"/>
	                </td>
	            </tr>	
	        </tbody>
	         <tbody>    
	             <tr  class="darkZebraTr">
	                <td class="rightBlueTxt w160 snClass">服务等级：</td>
	                <td>
	                	<select id="onuLevel" name="onuLevel" style="width: 220px;" >
							<option value="0">缺省</option>
							<option value="1">重要ONU</option>
							<option value="2">普通MDU</option>
							<option value="3">普通SFU</option>
						</select>
	                </td>
	            </tr>	  
	        </tbody>              
	    </table>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id="saveButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@DHCPRELAY.add@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>