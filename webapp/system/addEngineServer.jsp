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
    module platform
    IMPORT js/tools/ipText
</Zeta:Loader>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources" />
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>

<script type="text/javascript">
	var linkStatusCombo = null;
	var adminStatusCombo = null;

	//除去字符串前后的空格
	function trim(s) {
		s = s.replace(/^\s+/, "");
		return s.replace(/\s+$/, "");
	}

	//添加分布式采集器时的验证函数
	function validate() {
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
		if(!checkedIpValue(ip) || !checkIsNomalIp(ip)){//必须是ABC类ip地址;
			$("#ip").focus();
			return false;
		}
		//验证端口
		var port = trim($("#port").val());
		reg = /^\d{1,5}$/;
		if(!reg.test(port) || port>3020 || port<3010){
			$("#port").focus();
			return false;
		}
		//验证内存
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
		//验证备注
		var note = trim($("#note").val());
		var len = note.length+(note.match(/[^\x00-\xff]/g) ||"").length;
		if(len>255){
			$("#note").focus();
			return false;
		}
		return true;
	}

	//添加分布式采集器
	function addEngineServer() {
		if (!validate()) {
			return;
		}

		var name = trim($("#name").val());
		var ip = trim($("#ip").val());
		var port = trim($("#port").val());
		var xmx = trim($("#xmx").val());
		var xms = trim($("#xms").val());
		var note = trim($("#note").val());
		var typeArr = [];
		var type;
		$(":checked").each(function(){
			var $ch = $(this);
			typeArr.push( $ch.val() );
		})
		if(typeArr.length > 0){
		    type = typeArr.join(",");
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@engine.typechoose@");
			return;
		}
		$.ajax({
			url : '/system/addEngineServer.tv',
			type : 'POST',
			data : {
				name : name,
				ip : ip,
				port : port,
				xmx  : xmx,
				xms  : xms,
				note : note,
				type : type
			},
			success : function(response) {
				if(response == 'engineExists'){
					window.parent.showMessageDlg("@COMMON.tip@", "@engine.engineExists@");
				} else {
				    top.afterSaveOrDelete({
			        title: '@COMMON.tip@',
			        html: '<b class="orangeTxt">@resources/COMMON.addSuccess@</b>'
			        });
					cancleClick();
				}
			},
			error : function() {
				window.parent.showErrorDlg();
			},
			cache : false,
			complete : function(XHR, TS) {
				XHR = null
			}
		});
	}

	function cancleClick() {
		window.parent.getActiveFrame().refreshList();
		window.parent.closeWindow("addEngineServer");
	}

</script>

<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
.required{ color:#f00;}
</style>
</head>
<body class="openWinBody">
	<div class="openWinHeader" style="height:80px;">
		<div class="openWinTip">
			<p><b class="orangeTxt">@COMMON.ipDoNotUse@</b></p>
			<p>
                0.0.0.0
                <span class="pL20">
                    0.0.0.1~0.255.255.255
                </span>
            </p>
			<p>
                127.0.0.0~127.255.255.255（@COMMON.ipDoNotUse2@）
                <span class="pL20">
                    224.0.0.0~239.255.255.255（@COMMON.ipDoNotUse3@）
                </span>
            </p>
			<p>
                240.0.0.0~255.255.255.254（@COMMON.ipDoNotUse4@）
                <span class="pL20">
                    255.255.255.255
                </span>
            </p>
		</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT5">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="220">
	                    <label><span class="required">*</span>@engine.name@</label>
	                </td>
	                <td>
	                    <input id="name" type="text" class="normalInput w300" toolTip='@engine.engineNameRule@' maxlength="18" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.ipAdress@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="ip" type="text" toolTip="@engine.ipRule@" />
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @mibble.portNum@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="port" type="text" toolTip="3010-3020" maxlength="5" value="3010"/>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.xmx@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="xmx" type="text" toolTip="0-65535<br />@engine.memtip@" maxlength="5" value="1000"/> M
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.xms@</label>
	                </td>
	                <td>
	                    <input class="normalInput w300" id="xms" type="text" toolTip="0-65535" maxlength="5" value="500"/> M
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">@engine.note@</td>
	                <td>
	                    <textarea class="normalInput w300" style="height:80px;" id="note" rows="" cols="" toolTip="@engine.noteRule@"></textarea>
	                </td>
	            </tr>
	             <tr class="withoutBorderBottom">
	                <td class="rightBlueTxt">
	                    <label><span class="required">*</span> @engine.type@</label>
	                </td>
	                <td>
	                    <label class="pR10"><input type="checkbox" name="type" checked="checked" value="Default" /> Default</label>
	                    <label class="pR10"><input type="checkbox" name="type" checked="checked" value="Trap" /> Trap</label>
	                    <label class="pR10"><input type="checkbox" name="type" checked="checked" value="CmPoll" /> CmPoll</label>
                        <label class="pR10"><input type="checkbox" name="type" checked="checked" value="Performance" /> Performance</label>
                        <label class="pR10"><input type="checkbox" name="type" checked="checked" value="PNMP" /> PNMP</label>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
		         <li><a onclick="addEngineServer()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		         <li><a onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@mibble.cancel@</span></a></li>
		     </ol>
		</div>
	</div>


</body>
</Zeta:HTML>