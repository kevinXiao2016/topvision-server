<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
</head>
<style type="text/css">
#content-tit{
	background: url("/images/system/contentTit.gif") repeat scroll 0 0 transparent;
	height: 100px;
	border-bottom: 1px solid #B0CFF7;
}
.pB10{ padding-bottom:10px;}
#content-tit span img {position: absolute; top: 10px;right: 5px;}

.tipTable{padding-top: 10px;}
.tipTable td{
	height: 16px;
	color:#333;
}

.basicDl{ width:400px;}
.basicDl dt{ height:23px; margin-top: 22px; float:left; text-indent: 24px;}
.basicDl dd{ width:288px; height:25px; float:left;margin-top: 20px;}

.formDiv{
	margin: 15px 50px;
}
.normalInput{width: 285px; height:20px; padding-left: 4px; padding-top:3px;  border: 1px solid #7F9EB9; background: #FFF;}
.buttonDiv{
	position: absolute;
	bottom: 20px;
	right: 80px;
}
.edge10{ padding:10px;}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
  var cmcId = '${cmcId}';
  $(document).ready(function(){
	  //$("#saveBt").attr("disabled",!operationDevicePower);
	  R.saveBt.setDisabled(!operationDevicePower);
  });
  
  function saveHandler(){
	var st = $("#startMac").val();
	var em = $("#terminateMac").val();
	if(!Validator.isMac(st)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.startMacErrorTip@","tip",function(){
			$("#startMac").focus();
		});
	}else if(Validator.isSpecialMac(st)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.noSpecialMacAddress@","tip",function(){
			$("#startMac").focus();
		});
	}
	if(!Validator.isMac(em)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.termMacErrorTip@","tip",function(){
			$("#terminateMac").focus();
		});
	}else if(Validator.isSpecialMac(em)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.noSpecialMacAddress@","tip",function(){
			$("#terminateMac").focus();
		});
	}
	//将格式统一为默认格式00:00:00:00:00:00
	var formatStMac = Validator.formatMac(st);
	var formatEtMac = Validator.formatMac(em);
	//验证开始MAC地址是否比结束MAC地址小
	if(formatStMac>formatEtMac){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.startMacMustLessThenEnd@","tip",function(){
			$("#startMac").focus();
		});
	}
	
	var range = formatStMac.concat(" ").concat(formatEtMac);
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saveCfg@", 'ext-mb-waiting');
	$.ajax({
		url : '/cmc/loadbalance/addLoadBalanceExcMacRange.tv',cache:false,dataType:'json',
		data: {
			cmcId : cmcId,
			rangeDetail : range
		},success:function(json){
			
			if(json.overLimited){
				window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.excRangeLimited@");
			}else if(json.overlap){
			 	window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.excRangeOverlap@");
			}else if(json.snmpError){
				window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.addExcEr@");
				return false;
			}else{
				//window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.addExcOk@");
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@loadbalance.addExcOk@</b>'
	   			});
				closeHandler();
			}
		},error:function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.addExcEr@");
		}
	});
}
  
/***********************************
                             关闭页面
 ************************************/
function closeHandler(){
    window.parent.closeWindow('addExcRange');
}


</script>
<body class="openWinBody">
    <div class=formtip id=tips style="display: none"></div>
    <div class="openWinHeader" style="height: 100px;">
		<div class="openWinTip">
			<table width="470" class="tipTable" cellpadding="0" cellspacing="0" border="0" rules="none">
	    		<tr>
	    			<td colspan="4"><p class="pB10">@loadbalance.macAddressFormat@:</p></td>
	    		</tr>
	    		<tr>
	    			<td width="105"><font color="red">@loadbalance.maohao@:</font></td>
	    			<td>00:00:00:00:00:00</td>
	    			<td width="78">@loadbalance.hengxian@:</td>
	    			<td>00-00-00-00-00-00</td>
	    		</tr>
	    		<tr>
	    			<td>@loadbalance.space@:</td>
	    			<td>00 00 00 00 00 00</td>
	    			<td>@loadbalance.dot@:</td>
	    			<td>0000.0000.0000</td>
	    		</tr>
	    		<tr>
	    			<td>@loadbalance.noSpace@:</td>
	    			<td>000000000000</td>
	    			<td></td>
	    			<td></td>
	    		</tr>
    		</table>
		</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
    
    	<div class="edge10">
			<table class="mCenter zebraTableRows" >
			<tr>
				<td class="">@loadbalance.startMac@:</td>
				<td><input id="startMac" class="normalInput"/></td>
			</tr>
			<tr>
				<td>@loadbalance.terminateMac@:</td>
				<td><input id="terminateMac" class="normalInput"/></td>
			</tr>
			</table>
    	</div>
    	
    <Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveHandler()" icon="bmenu_new">@BUTTON.add@</Zeta:Button>
		<Zeta:Button onClick="closeHandler()" icon="bmenu_forbid" id="closeBt">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
   <!-- 
    <div class="buttonDiv">
        <BUTTON id="saveBt" class="BUTTON75" onclick="saveHandler()" >@COMMON.add@</BUTTON>
        <BUTTON id="closeBt" class="BUTTON75" onclick="closeHandler()">@COMMON.cancel@</BUTTON>
    </div>
     -->
</body>
</Zeta:HTML>