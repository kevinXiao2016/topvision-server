<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ page import="com.topvision.ems.mobile.MobileEMSIIIOSVersion"%>
<%@ page import="com.topvision.ems.mobile.MobileEMSIIAndroidVersion"%>
<%@ page import="com.topvision.ems.mobile.MobileMUIIOSVersion"%>
<%@ page import="com.topvision.ems.mobile.MobileMUIAndroidVersion"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY Ext 
	LIBRARY jquery 
	LIBRARY zeta
	IMPORT js.jquery.Nm3kMsg
	IMPORT js.jquery.nm3kPassword
    MODULE platform
</Zeta:Loader>
<title>@logon.title@</title>
<link rel="shortcut icon" href="<%=request.getContextPath()%>images/favicon.ico" type="image/x-icon" />
<style type="text/css">
*{ margin:0; padding:0;}
html,body{ font:12px Verdana, Arial, Helvetica, sans-serif; 
background:#0067AE url(images/loginBgLine.png) repeat-x 0 269px; height:100%; 
*background:#005CA6 url(images/loginBgLine.png) repeat-x 0 269px;
background:#005CA6 url(images/loginBgLine.png) repeat-x 0 269px\9;}
html{ overflow:hidden;}
ul,li{ list-style:none;}
a{ text-decoration:none;}
.wrap{ width:100%; height:100%; overflow:hidden; background:url(images/loginBg.png) no-repeat center center;}
.broadBg{ width:100%; height:478px; overflow:hidden; background:url(images/@COMMON.LoginBg@) no-repeat center 0; position:relative; top:160px;}
.containerLoginBox{ width:583px; height:358px; margin:84px auto 0px; position:relative;}
.loginBox{ width:583px; height:358px; overflow:hidden; position:relative; background:url(images/@COMMOM.LoginBox@) no-repeat;}
.user, .pass{ border:0px solid transparent; background:transparent; position:absolute; top:115px; left:134px; width:268px; height:24px; padding-top:8px; padding-left:36px; font-size:14px; font-family:Verdana; color:#676767;}
.pass{ top:166px;}
#rememberName{ width:20px; height:20px; overflow:hidden; display:block; position:absolute; top:214px; left:132px;}
.nameCheck_false{  background:url(images/loginCheck.png) no-repeat 0 -20px;}
.nameCheck_true{  background:url(images/loginCheck.png) no-repeat 0 0px;}
.loginBtn{ position:absolute; top:252px; left:132px;}
#ieTips{text-align:center; font-size:14px; display:none; color:#000; position:absolute; top:0px; left:0; width:100%; background:#FFFFE1; padding:6px 0px;}
.yellowTipsClose{ width:16px; height:16px; overflow:hidden; background:url(images/yellowTipClose.png) no-repeat; position:absolute; top:6px; right:6px; opacity:0.8; -moz-opacity:0.8; display:block; filter:alpha(opacity=50);}
.yellowTipsClose:hover{opacity:1; -moz-opacity:1; filter:alpha(opacity=100);}
.app{position:absolute; left:16px; bottom:4px; color:#DFDFDF;}
.app a{ color:#DFDFDF;}
.app a:hover{ color:#ff0;}
.copyRightTxt{ color:#fff; position:absolute; bottom:4px; right:0px; -webkit-transform: scale(.916667); font:11px "Microsoft YaHei","微软雅黑";text-shadow:0px 0px 2px #000;"}
</style>

<script type="text/javascript" src="js/jquery/Nm3kMsg.js"></script>
<script type="text/javascript" src="/js/qrcode/qrcode.js"></script>
<script type="text/javascript">
var aboutTitle = '@about.title@';
var appTitle = '@app.title@';
var hasMobilePower = ${hasMobilePower};
var left = 0;
var top = 0;
var isOpenWindow = false;
var androidVersion = '<%=MobileEMSIIAndroidVersion.version%>';
var iosVersion = '<%=MobileEMSIIIOSVersion.version%>';
var language = '@app.logo.type@'; //en 或者 zh
var iosUrl = 'http://ems.top-vision.cn:8110/download/download_app_V2.jsp?version=V'+iosVersion+'&platform=ios&language=' + language;
var androidUrl = 'http://ems.top-vision.cn:8110/download/download_app_V2.jsp?version=V'+androidVersion+'&platform=android&language=' + language;

var terminalAndroidVersion = '<%=MobileMUIAndroidVersion.version%>';
var terminalIosVersion = '<%=MobileMUIIOSVersion.version%>';
var terminalAndroidUrl = 'http://ems.top-vision.cn:8110/terminal/android/V'+ terminalAndroidVersion +'/terminal.apk';
var terminalIosUrl = 'http://ems.top-vision.cn:8110/terminal/ios/V'+ terminalIosVersion +'/terminal.html'

function openWindow() {
    var width = screen.availWidth - 10;
    var height = screen.availHeight - 38;
    var modes = Zeta$N("displayMode");
    var url = 'mainFrame.tv';
    location.href = url;
    /*
    if (modes[0].checked) {
        location.href = url;
    } else {
    	var style = '';
    	if (modes[1].checked) {
	        style = 'menubar=no,toolbar=no,status=no,location=no,' +
	                'resizable=yes,scrollbars=no,width=' + width + 
	                ',height=' + height + ',left=' + left + ',top=' + top;
    	} else {
			style = 'fullscreen,menubar=no,toolbar=no,status=no,location=no,' +
			        'resizable=yes,scrollbars=no';
    	}
        var mainWin = window.open(url, '', style);
        Ext.MessageBox.hide();
        if (mainWin == null) {
        	Ext.MessageBox.show({title: "@COMMON.tip@", msg: "@Login.note9@",
                buttons: Ext.MessageBox.OK, icon: Ext.MessageBox.INFO});
        } else {
        	mainWin.focus();
        	//setTimeout("destroyWindow();", 3000);
        }
    }*/
}
function destroyWindow() {
    try {
    	window.opener = null;
    	window.close();
    } catch (err) {
    }
}

function logonClick() {
    // validator
    var username = Zeta$('userName').value.trim();
    if (username == '') {
        Zeta$('userName').focus();
        return;
    }
    var passwd = Zeta$('passwd').value.trim();
    if (passwd == '') {
        Zeta$('passwd').focus();
        return;
    }
	Zeta$('logonBt').disabled = true;
    sendAjaxRequest('logon.tv', 'POST', {username: username, passwd: passwd},
        loginSuccessCallback, defaultErrorHandler, 'json', false);
}
function defaultFailureCallback(response) {
	Zeta$('logonBt').disabled = false;
    showMessageDlg("@COMMON.error@", "@RESOURCES/COMMON.operationFailure@", 'error');
}
function sendAjaxRequest(url, method, param, sn, fn, dataType, cache) {
	Ext.Ajax.request({url: url, method: 'post', failure: fn, success: sn, params: param});
}
function loginSuccessCallback(response) {
	Zeta$('logonBt').disabled = false;
	var json = Ext.decode(response.responseText);
    if (json.licenseExpired) {
    	showUploadLicense();
    } else {
     	if (json.authenticated) {
            setCookies();
            openWindow();
        } else {
        	var msg = '';
        	if (json.errorCode == 1) {
        		msg = "@Login.note5@";
        	} else if (json.errorCode == 11) {
        		msg = "@Login.note51@";
        	} else if (json.errorCode == 12) {
        		msg = "@Login.note52@";
        		if(json.restCount != null){
        			msg = String.format("@Login.userTryPwdTip@", json.restCount);
            	} else if(json.locked){
            		msg = String.format("@Login.userLocked2@", json.totalCount)
            	}
            } else if (json.errorCode == 13) {
                window.location.href = "system/showSetPassword.tv?userName=admin";
            	return;
        	} else if (json.errorCode == 2) {
        		msg = "@Login.note6@";
        	} else if (json.errorCode == 3) {
        		msg = "@Login.note7@";
        	} else if (json.errorCode == 4) {
        		msg = "@Login.note8@";
        	} else if(json.locked){
        		msg = "@Login.userLocked@"
        	}
        	
            isOpenWindow = true;
            
            loginError("@COMMON.tip@",msg);
            /*
            Ext.MessageBox.show({title: "@COMMON.tip@", msg: msg,
                buttons: Ext.MessageBox.OK, icon: Ext.MessageBox.INFO});
            */
        }
    }
}

function setCookies() {
    if (Zeta$('rememberNameValue').value == "1") {
        setCookieValue('ems.userName', Zeta$('userName').value);
        setCookieValue('ems.rememberName', true);
    } else {
        clearCookieValue('ems.userName');
        setCookieValue('ems.rememberName', false);
    }
}

function cancelClick() {
    window.close();
}

function addEnterKey(e) {
	var event = window.event||e; // for firefox
	if (event.keyCode==KeyEvent.VK_ENTER) {
        if (!isOpenWindow) {
            logonClick();
        } else {
            isOpenWindow = false;
        }
	}
}

function setHomePage(box) {
    box.style.behavior='url(#default#homepage)';
    box.setHomePage(location.href.replace('#', ''));
}

function addFavourite() {
    window.external.addFavorite(location.href.replace('#', ''), appTitle);
}

function showUploadLicense() {
    var win = new Ext.Window({id: 'modalDlg', title: "@RESOURCES/COMMON.license@", 
    	minimizable: false, maximizable: false, closable: true, border:false,
    	width: 800, height: 500, modal: true, plain: false, resizable: false, stateful: false,
        html: '<iframe src="system/showLicense.tv" frameborder=0 width=100% height=100%></ifame>'});
    win.show();
}

function showAbout() {
    var win = new Ext.Window({id:'modalDlg', title: aboutTitle, 
    		minimizable: false, maximizable: false, closable: true, border:false,
            width: 600, height: 370, modal: true, plain: false, resizable: false,stateful: false,
            html:'<iframe src="system/showAbout.tv" frameborder=0 width=100% height=100%></ifame>'});
    win.show(Ext.get('show-btn'));
}
function closeWindow(id) {
    var w = Ext.WindowMgr.get(id);
    if (w != null) {w.close();}
}
function defaultErrorHandler() {
	Zeta$('logonBt').disabled = false;
    Ext.MessageBox.hide();
    Ext.MessageBox.show({title: "@COMMON.error@", msg: "@Login.note4@",
        buttons: Ext.MessageBox.OK, icon: Ext.MessageBox.ERROR});
}
function showMessageDlg(title,msg,type) {
    var icon = (type == 'error' ? Ext.MessageBox.ERROR : (type == 'question' ? Ext.MessageBox.QUESTION : Ext.MessageBox.INFO));
    Ext.MessageBox.show({title:title,msg:msg,
       buttons: Ext.MessageBox.OK,
       icon: icon
   });
}
function getCookieValue(name, defaultValue) {
	return getCookie(name, defaultValue);
}
function setCookieValue(name, value) {
	setCookie(name, value);
}
function clearCookieValue(name) {
	delCookie(name);
}

var userEl, passwdEl;

$(document).ready(function(){
    window.onload =function(){
        var iosqrcode = new QRCode(document.getElementById("iosqrcode"), {
            width : 200,//设置宽高
            height : 200
        });
        iosqrcode.makeCode(iosUrl);
        $("#iosA").attr("href",iosUrl);
        var androidqrcode = new QRCode(document.getElementById("androidqrcode"), {
            width : 200,//设置宽高
            height : 200
        });
        androidqrcode.makeCode(androidUrl);
        $("#androidA").attr("href",androidUrl);
        
        var terminalIos = new QRCode(document.getElementById("terminalIos"), {
            width : 200,//设置宽高
            height : 200
        });
        terminalIos.makeCode(terminalIosUrl);
        $("#terminalIosLink").attr("href",terminalIosUrl);
        var terminalAndroid = new QRCode(document.getElementById("terminalAndroid"), {
            width : 200,//设置宽高
            height : 200
        });
        terminalAndroid.makeCode(terminalAndroidUrl);
        $("#terminalAndroidLink").attr("href",terminalAndroidUrl);
        
        
        
        
        var passwd = new nm3kPassword({
            id : "passwd",
            renderTo : "passwdOuter",
            toolTip : "",
            width : 224,
            value : '',
            firstShowPassword : true,
            disabled : false,
            maxlength : 16
        })
        passwd.init();
    }
});


</script>
</head>
<body onkeydown="addEnterKey(event);">

    <div id="terminalDownload" style="display:none; width:100%; height:100%; position:absolute; top:0px; left:0px; z-index:2; background:rgba(0,0,0,0.65)">
        <div class="downloadMain">
            <div class="box">
                <img src="images/androidIcon.png" class="androidIcon" />
                <p class="txtCenter txt24Tip">@COMMON.downloadAndroid@</p>
                <div id="terminalAndroid" class="middlePic" ></div>
                <p class="txtCenter">@COMMON.loginClick@ <a id="terminalAndroidLink" class="blueLink" target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
            </div>
            <div class="box">
                <img src="images/appleIcon.png" class="appleIcon" />
                <p class="txtCenter txt24Tip">@COMMON.downloadIOS@</p>
                <div id="terminalIos" class="middlePic" ></div>
                <p class="txtCenter">@COMMON.loginClick@ <a id="terminalIosLink" class="blueLink" target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
            </div>
        </div>
    </div>
	<div id="appDownload" style="display:none; width:100%; height:100%; position:absolute; top:0px; left:0px; z-index:2; background:rgba(0,0,0,0.65)">
		<div class="downloadMain">
			<div class="box">
				<img src="images/androidIcon.png" class="androidIcon" />
				<p class="txtCenter txt24Tip">@COMMON.downloadAndroid@</p>
                <div id="androidqrcode" class="middlePic" ></div>
				<p class="txtCenter">@COMMON.loginClick@ <a id="androidA" class="blueLink" target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
			</div>
			<div class="box">
				<img src="images/appleIcon.png" class="appleIcon" />
				<p class="txtCenter txt24Tip">@COMMON.downloadIOS@</p>
                <div id="iosqrcode" class="middlePic" ></div>
                <p class="txtCenter">@COMMON.loginClick@ <a id="iosA"  class="blueLink" target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
			</div>
		</div>
	</div>
	<div class="wrap">
		<div class="broadBg">
		    <div class="containerLoginBox">
		  	   <div class="loginBox">
					<input id="userName" type="text" class="user" />
					<!--<input id="passwd" type="password" class="pass" />-->
					<div id="passwdOuter" class="pass"></div>
					<a href="javascript:;" class="nameCheck_false" id="rememberName"></a>			
					<input id="logonBt" type="image" name="imageField" src="images/@COMMOM.LoginBtn@" width="146" height="43" class="loginBtn" onclick="logonClick()" />
                    <!-- <a href="nm3k.apk" class="app" id="app">@COMMON.app@</a> -->
                    <p class="app"><a class="pR10" href="javascript:;" onclick="showDownloadTerminal()">@COMMON.terminal@</a><a id="app" class="pR20" href="javascript:;" onclick="showDownload('mobile')">@COMMON.appSetUp@</a><a id='flashDownload' href="http://ems.top-vision.cn:8110/spread/download/@Login.flashUrl@.jsp#flashPlayer" target="_blank">@Login.flashDownload@</a></p>
                    <p class="copyRightTxt">@logon.copyrights@</p>
			   </div>			  
			</div>
		</div>		
	</div>
	<div id="ieTips">
		@COMMOM.NotIE@
		<a class="yellowTipsClose" href="javascript:;"></a>
	</div>
	<input id="rememberNameValue" name="displayMode" type="hidden" value="1" />	
<script type="text/javascript">
	$(function(){
		$(".downloadMain").click(function(event){
			event.stopPropagation();
		})
		$("#appDownload, #terminalDownload").click(function(){
			$(this).fadeOut();
		})
		
		//让登陆框居中;
		function autoHeight(){
			var wrapH = $(".wrap").height();
			var boxH = $(".broadBg").height();
			var h = (wrapH - boxH) / 2 - 10;
			if(h < 0 ){
				h = 0;
			}
			$(".broadBg").css("top",h);
			var h2 = h+109;
			$("body,html").css("backgroundPosition","0px "+ h2 +"px");
		}
		autoHeight();
		$(window).resize(function(){
			autoHeight();
		});
		
		Zeta$('rememberNameValue').value = getCookieValue("ems.rememberName", 'true') == 'true' ? 1 : 0;
	    userEl = Zeta$('userName');
	    passwdEl = Zeta$('passwd');
	    userEl.value = getCookieValue("ems.userName", '');
	    
		//判断隐藏域，确定记住用户名是否勾选;
		var v = $("#rememberNameValue").val();
		if(v == "1" || v == 1 || v == "true"){
			$("#rememberName").attr("class","nameCheck_true");
		}else{
			$("#rememberName").attr("class","nameCheck_false");
		};
	    //判断是否显示App下载链接
        if(hasMobilePower){
            $("#app").attr("style","");
        }else{
            $("#app").attr("style","display:none");
        };
        
		//点击勾选框图片;
		$("#rememberName").click(function(){
			if($(this).hasClass("nameCheck_true")){
				$(this).attr("class","nameCheck_false")
				$("#rememberNameValue").val(0);
			}else{
				$(this).attr("class","nameCheck_true");
				$("#rememberNameValue").val(1);
			}
		});
	
		var bro=$.browser;
	    var binfo="";
	    /* if(bro.msie) {binfo="Microsoft Internet Explorer "+bro.version;}
	    if(bro.mozilla) {binfo="Mozilla Firefox "+bro.version;}
	    if(bro.safari) {binfo="Apple Safari "+bro.version;}
	    if(bro.opera) {binfo="Opera "+bro.version;} */
	    
	    /* if(!bro.msie || bro.version != 8){//如果是不是ie8浏览器;
	    	$("#ieTips").fadeIn('slow');   	
	    }
		    
		$(".yellowTipsClose").click(function(){
			$("#ieTips").slideUp();
		}); */
	});//end document.ready;
	
	var MoveObj = {}
	MoveObj.moveStart = 0;
	MoveObj.moveTime = 8;//出错时晃动次数;
	MoveObj.moveSpeed = 40;//出错时晃动速度;
	MoveObj.msg;
	function loginError(paraTitle,paraHtml){
		moveLeft();
				
		if(document.getElementById("errorMsg")){
			MoveObj.msg.update({
				html: paraHtml
			})
		}else{
			MoveObj.msg = new Nm3kMsg({
				id: "errorMsg",
				unique: true,
				html: paraHtml,
				title: paraTitle,
				timeLoading: true
			});
			MoveObj.msg.init();
		}
	}
	function moveLeft(){
		$(".loginBox").animate({left:-10},MoveObj.moveSpeed,function(){
			MoveObj.moveStart++;
			if(MoveObj.moveStart < MoveObj.moveTime){
				moveRight();
			}else{
				$(".loginBox").animate({left:0},MoveObj.moveSpeed);
				MoveObj.moveStart = 0;
			}
		})
	};//end moveLeft;
	
	function moveRight(){
		$(".loginBox").animate({left:10},MoveObj.moveSpeed,function(){
			MoveObj.moveStart++;
			if(MoveObj.moveStart < MoveObj.moveTime){
				moveLeft();
			}else{
				$(".loginBox").animate({left:0},MoveObj.moveSpeed);
				MoveObj.moveStart = 0;
			}
		})
	}
	
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
	
	function showDownload(para){
		$("#appDownload").fadeIn();
		$(".downloadMain").css({display:'block'});
	}
	function showDownloadTerminal(){
	    $("#terminalDownload").fadeIn();
	    $(".downloadMain").css({display:'block'});
	}
</script>
<style type="text/css">
.pass{top: 158px}
#passwdOuter input.normalInput,  #passwdOuter input.normalInputFocus{
        padding: 6px 0px;
        border: none;
        background: transparent;
}

#passwdOuter .nearInputBtn, #passwdOuter .nearInputBtn span{
    background: transparent;
}
#passwdOuter .nearInputBtn{
    padding: 4px 8px 8px 4px;
}

</style>
</body>
</Zeta:HTML> 
