<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	IMPORT js.jquery.Nm3kMsg
	module resources
</Zeta:Loader>
<title>NM3000</title>
<style type="text/css">
.c-tip{ color: #BBBBBB; position: absolute; }
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px; margin:0 auto;}
#w1600{ width:1600px; position:absolute; top:0; left:0;}
#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:800px;}
.infoTable{ width:100%;}
.infoTable td{ height: 30px;}
.infoTable td td{ height:auto;}
#putTree{ width:582px; border:1px solid #ccc;  background: #fff; height: 240px;}
</style>
</head>
<body class="whiteToBlack">
	<div id="w800">
		<div id="w1600">
			<div id="step0">
				<div class="edge10">
						<div class="zebraTableCaption" style="padding-top:50px;">
					     <div class="zebraTableCaptionTitle"><span>@COMMON.setFirstPw@</span></div>
						    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
						        <tbody>
						            <tr>
						                <td colspan="2" align="center" class="withoutBorderBottom" style="padding-bottom: 20px;">
											<b class="orangeTxt">@COMMON.firstPwTip@</b>
						                </td>
						            </tr>
						            <tr>
						                <td class="rightBlueTxt" width="250">
						                	@SYSTEM.userName@:
						                </td>
						                <td>
						                	<input id="user" type="text" class="normalInputDisabled w240" readonly="readonly" value="${userName}"/>
						                </td>
						            </tr>
						             <tr  class="darkZebraTr">
						             	<td class="rightBlueTxt">
						                	@SYSTEM.passWord@
						                </td>
						                <td>
											<input id="pw" type="password" class="normalInput w240" toolTip="@SYSTEM.newPasswordNotNull@<br>@SYSTEM.createUser.note1@" />
						                </td>
						            </tr>
						            <tr>
						             	<td class="rightBlueTxt">
						                	@SYSTEM.confirmPassword@
						                </td>
						                <td>
											<input id="confirmPw" type="password" class="normalInput w240" toolTip="@SYSTEM.confirmPasswordNotNull@<br>@SYSTEM.passwordNote5@<br />@SYSTEM.createUser.note1@" />
						                </td>
						            </tr>
						        </tbody>
						    </table>
						    <div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB20 pT40 noWidthCenter">
						         <li><a href="javascript:;" class="normalBtnBig" onclick="saveFn()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
						         <li><a href="javascript:;" class="normalBtnBig" onclick="backFn()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
						     </ol>
						</div>
						</div>
						
					</div>
			</div>
			<div id="step1">
				
			</div>
		</div>
	</div>
<script type="text/javascript">
	var nm3kObj = {};//用来记录nm3kMsg控件弹出的框;
	function afterSaveOrDelete(o){
		var showtime = 1000;
		var nTime = o.showTime ? o.showTime : showtime;
		var autoHide = o.autoHide ? o.autoHide : true;
		if($("#nm3kSaveOrDelete").length == 0){
			nm3kObj.nm3kSaveOrDelete = new Nm3kMsg({
				title: o.title,
				html: o.html,
				okBtnTxt: "@resources/COMMON.ok@",
				okBtn : true,
				timeLoading: true,
				unique: true,
				showTime : nTime,
				autoHide : o.autoHide,
				id: "nm3kSaveOrDelete"		
			});
			nm3kObj.nm3kSaveOrDelete.init();
		}else{
			nm3kObj.nm3kSaveOrDelete.update({
				html: o.html,
				title: o.title
			})
		}	
	};//end afterSaveOrDelete;
	
	//返回登陆页面;
	function backFn(){
		window.location.href = "showlogon.tv";
	}
	
	
	//登陆验证;
	function saveFn(){
		var user = $("#user").val(),
		    pw = $("#pw").val(),
		    pw2 = $("#confirmPw").val();
		
		var reg2 = /^[a-zA-Z\d\u4e00-\u9fa5-_]{6,16}$/;	
		if( !reg2.test(pw) ){ //验证密码;
			$("#pw").focus();
			return;
		}
		if( !reg2.test(pw2) ){ //验证确认密码;
			$("#confirmPw").focus();
			return;
		}
		if(pw !== pw2){ //两次密码不一致;
			$("#pw").focus();
			var tip = "@SYSTEM.passwordNote5@";
			afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : tip
			})
			return;
		}
		$.ajax({
			url: '/setPasswd.tv', type: 'GET',
			data: {
				username : user,passwd : pw
			},
			success: function() {
				window.location.href = "/mainFrame.tv";
			},
			error: function(json) {
				
			},
			cache: false
		});
	}
</script>
</body>
</Zeta:HTML>
