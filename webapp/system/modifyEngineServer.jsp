<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	css css/white/disabledStyle
    module platform
    IMPORT js/tools/ipText
</Zeta:Loader>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources" />
<!-- <link rel="stylesheet" type="text/css" href="/css/engineServerStyle.css" /> -->
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>

<script type="text/javascript">
//页面的连接参数全局变量
var engineServer = ${engineServerJson};

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
}

function validate(id){
	var reg;
	//验证名称,必须为3-18位的由数字、字母、下划线组成的字符串
	var name = $("#name").val();
	reg = /^[0-9a-zA-Z_]{3,18}$/;
	if(!reg.test(name)){
		$("#name").focus();
		return false;
	}
	//验证IP地址
	var ip = $("#ip").val(); 
	if(id != 1){
		if(!checkedIpValue(ip) || !checkIsNomalIp(ip)){//必须是ABC类ip地址;
			$("#ip").focus();
			return false;
		}
	}
	//验证端口
	if(id!=1){
	   var port = trim($("#port").val());
	   reg = /^\d{1,5}$/;
	   if(!reg.test(port) || port>3020 || port<3010){
		   $("#port").focus();
		  return false;
	   }
	}
	//验证内存
	if(id!=1){
	   var xmx = trim($("#xmx").val());
	   var xms = trim($("#xms").val());
	   reg = /^\d{1,5}$/;
	   if(!reg.test(xmx) || xmx>65535){
		   $("#xmx").focus();
		   return false;
       }
	   if(!reg.test(xms) || xms>65535){
		   $("#xms").focus();
		   return false;
	   }
	   xmx = parseInt(xmx,10);
	   xms = parseInt(xms,10);
	   if(xms > xmx){
		   window.parent.showMessageDlg("@COMMON.tip@", "@engine.memtip@");
		   return false;
	   }
	}
	//验证备注
	var note = trim($("#note").val());
	var len = note.length+(note.match(/[^\x00-\xff]/g) ||"").length;
	if(len>255){
		$("#note").focus();
		return false;
	}
	return true;
}

//修改分布式采集器
function modifyEngineServer(){
	var id = engineServer.id;
	if(!validate(id)){
		return;
	}
	
	var name = trim($("#name").val());
	var ip = trim($("#ip").val());
	var port = trim($("#port").val());
	var xmx,xms;
	if(id==1){
		xmx = 0;
		xms = 0;
	} else {
	    xmx = trim($("#xmx").val());
	    xms = trim($("#xms").val());
    }
	var note = trim($("#note").val());
	var typeArr = [];
	$(":checked").each(function(){
		var $ch = $(this);
		typeArr.push( $ch.val() );
	})
	if(typeArr.length > 0){
		var type = typeArr.join(",");
	}else{
		window.parent.showMessageDlg("@COMMON.tip@", "@engine.typechoose@");
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@...", "waitingMsg", 600000, false);
	$.ajax({
		url: '/system/modifyEngineServer.tv',
    	type: 'POST',
    	data: {id:id, name:name, ip:ip, port:port, xmx:xmx, xms:xms, note:note, type:type},
   		success: function(response) {
   			if(response == 'engineExists'){
				window.parent.showMessageDlg("@COMMON.tip@", "@engine.engineExists@");
			} else if(response == 'engineMgrDisconnect'){
				window.parent.showMessageDlg('@COMMON.tip@', '@engine.engineMgrDisconnect@');
			} else { 
   			    top.afterSaveOrDelete({
		        title: '@COMMON.tip@',
		        html: '<b class="orangeTxt">@resources/COMMON.modifySuccess@</b>'
		        });
   			    cancleClick();
			}
   		}, error: function() {
   			//TODO
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	}); 
}

function cancleClick(){
	window.parent.getActiveFrame().refreshList();
	window.parent.closeWindow("modifyEngineServer");
}

Ext.onReady(function(){
	//填充输出框的值
	$("#name").attr("value",engineServer.name);
	$("#ip").attr("value",engineServer.ip);
	$("#port").attr("value",engineServer.port);
	$("#xmx").attr("value",engineServer.xmx);
	$("#xms").attr("value",engineServer.xms);
	$("#note").attr("value",engineServer.note);
	
	var id = engineServer.id;
	if(id == 1){
		$("#name").attr("disabled","disabled");
		$("#port").attr("disabled","disabled");
		$("#xmx").attr("disabled","disabled");
		$("#xms").attr("disabled","disabled");
		$("#xmx").attr("value","NA");
		$("#xms").attr("value","NA");
	}
	
	//$("#type").attr("value",engineServer.type);
	var str = engineServer.type;
	var arr = str.split(',');
	if(arr.length > 0){
		for(var i=0,len=arr.length; i<len; i++){
			$(":checkbox[value='"+ arr[i] +"']").attr("checked","checked");
		}
	}
	
});
</script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
.required{ color:#f00;}
</style>
</head>
<body class="openWinBody">
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <label><span class="required">*</span> @engine.name@</label>
	                </td>
	                <td>
	                    <input id="name" type="text" class="normalInput w300" maxlength="18" toolTip="@engine.engineNameRule@" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                  <label><span class="required">*</span> @engine.ipAdress@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="ip" type="text" disabled="disabled" toolTip="@engine.ipRule@" />
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @mibble.portNum@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="port" type="text" toolTip="3010-3020" maxlength="5"/>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.xmx@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="xmx" type="text" maxlength="5" toolTip="0-65535<br />@engine.memtip@"/>
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.xms@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="xms" type="text" maxlength="5"  toolTip="0-65535"/>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                  	@engine.note@
	                </td>
	                <td>
	                    <textarea id="note" rows="" cols="" class="normalInput w300" toolTip="@engine.noteRule@" style="height:60px;"></textarea>
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.type@</label>
	                </td>
	                <td>
	                    <label class="pR10"><input type="checkbox" name="type" value="Default" /> Default</label>
	                    <label class="pR10"><input type="checkbox" name="type" value="Trap" /> Trap</label>
	                    <label class="pR10"><input type="checkbox" name="type" value="CmPoll" /> CmPoll</label>
                        <label class="pR10"><input type="checkbox" name="type" value="Performance" /> Performance</label>
                        <label class="pR10"><input type="checkbox" name="type" value="PNMP" /> PNMP</label>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a onclick="modifyEngineServer()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
		         <li><a onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>

</body>
</Zeta:HTML>