<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>CCMTS-8800B View</title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/cmc/module/OCPage.js"></script>
<script type="text/javascript" src="/cmc/module/OCEntity.js"></script>
<script type="text/javascript">
//变量定义
var currentId = null;
//var entityId = '<s:property value="entity.entityId"/>';
//var typeId = <s:property value="entity.typeId"/>;
var enableIcon = "/epon/image/checked.gif";
var disableIcon = "/epon/image/unchecked.gif";
var entityList = ${entityList};

var Enable = [I18N.CMC.text.close,I18N.CMC.text.open,I18N.CMC.text.close];
var divCache = new Array();

$(document).ready(function(){
	window.page = new OCPage();
});
</script>
</head>
<body style="margin: 15px; background-color: #fff">
	<div id="loadingMask" style="display: none;">
		<img class="loadingImage" src='/images/gray_loading.gif'
			alt=<fmt:message bundle='${cmc}' key='CMC.label.gettingData'/> />
		<div class="loadingText"><fmt:message bundle='${cmc}' key='CMC.label.gettingData'/></div>
	</div>
	<div id="tishiDiv" style="display: none;"></div>
	<div class=formtip id=tips style="display: none"></div>
	<div>
		<%@ include file="entity.inc"%>
	</div>
	<div id="QuerySegment">
		<div class="queryPonBoard">
			<div align='right'>condition 1:</div>
			<select id="ponSlotList" name="OltSlotAttribute.onuId"
				onChange="locationChanged();"></select>
		</div>
		<div class="queryPonPort">
			<div align='right'><fmt:message bundle='${cmc}' key='CMC.label.pon'/>:</div>
			<select id="slotPonList">
				<option value="-1">condition 2:</option>
			</select>
		</div>
		<div class="queryPonMac">
			<div align='right'>condition 3:</div>
			<input id="mac"
				onfocus="inputFocused('mac', '<fmt:message bundle="${cmc}" key="CMC.tip.inputCorrectMacAddr"/>', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');" onClick="clearOrSetTips(this);"
				value="" maxlength=17 />
		</div>
		<div class="queryPonLLID">
			<div align='right'>condition 4::</div>
			<input id="llid" MaxLength="5"
				onFocus="inputFocused('llid', '<fmt:message bundle="${cmc}" key="CMC.tip.inputInteger"/>', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');" onClick="clearOrSetTips(this);"
				value="" />
		</div>
		<button id=search class='BUTTON75' type="button"
			onMouseOver="this.className='BUTTON_OVER75';"
			onMouseOut="this.className='BUTTON75';"
			onMouseDown="this.className='BUTTON_OVER75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onClick="searchOnu();"><fmt:message bundle='${cmc}' key='entity.alert.queryButton'/></button>
		<button id=refreshOnuInfo class='BUTTON95' type="button"
			onMouseOver="this.className='BUTTON_OVER95';refreshBtOver()"
			onMouseOut="this.className='BUTTON95';refreshBtOut()"
			onMouseDown="this.className='BUTTON_OVER95'"
			onMouseDown="this.className='BUTTON_PRESSED95'"
			onClick="refreshOnuInfo()"><fmt:message bundle='${cmc}' key='CMC.button.refreshCcmt'/></button>
		<div id=refreshText style="display: none;"></div>
	</div>
	<!--  面板图区域 -->
	<div id="PanelSegment">
		<div id="OnuTreePanelContainer">
			<div id="entityTreePanel"></div>
		</div>
		<div id="PanelContainer">
			<div id="entityContainer"></div>
		</div>
		<div id="GridContainer">
			<div id="entityGridContainer"></div>
		</div>
		<div id="entityType"
			style="position: absolute; left: 570px; top: 350px; z-index: 70000"></div>
	</div>
	<!-- ONU滚动条区域  -->
	<div id="ScrollSegment">
		<div id="countOnu"></div>
		<div id="currentOnu"></div>
		<!--向前-->
		<a id="PreA" href="javascript:void(0);"> <img class="PreButton"
			src="image/arrawLeft.png" /> </a>
		<!--向后-->
		<a id="NextA" href="javascript:void(0);"> <img class="NextButton"
			src="image/arrawRight.png" /> </a>
		<div class="imgContainerSegment">
			<!--onuId:onuId属性，arrayIndex：在数组中的位置-->
			<div id="imgContaner_1" class="item" onuId=1 arrayIndex=1>
				<!--将图片以及文字说明放在这里-->
				<img src="image/loader.gif" />
				<div class="caption"></div>
			</div>
			<div id="imgContaner_2" class="item" onuId=2 arrayIndex=2>
				<img src="image/loader.gif" />
				<div class="caption"></div>
			</div>
			<div id="imgContaner_3" class="item" onuId=3 arrayIndex=3>
				<img src="image/loader.gif" />
				<div class="caption"></div>
			</div>
			<div id="imgContaner_4" class="item" onuId=4 arrayIndex=4>
				<img src="image/loader.gif" />
				<div class="caption"></div>
			</div>
			<div id="imgContaner_5" class="item" onuId=5 arrayIndex=5>
				<img src="image/loader.gif" />
				<div class="caption"></div>
			</div>
		</div>
		<!--滚动条-->
		<div id="scroller" class="scroller">
			<!--滚动按钮-->
			<div id="slider" class="slider"></div>
		</div>
		<!-- END OF SCROLL SEGMENT -->
	</div>
</body>
<!-- ONU滚动条相关数据操作，交互操作方法封装-->
<script>

</script>
<style type="text/css">
#ScrollSegment .PreButton {
	position: absolute;
	left: 60px;
	top: 40px;
	width: 30px;
	height: 30px;
}
#ScrollSegment .NextButton {
	position: absolute;
	left: 700px;
	top: 40px;
	width: 30px;
	height: 30px;
	/**	background: url(image/right.png) no-repeat;**/
}

#ScrollSegment .scaleClass {
	color: #008000;
}
/*	a:hover img{
		filter:alpha(opacity=10);
		background:#00156E;
}*/
#ScrollSegment .imgContainerSegment {
	position: absolute;
	left: 100px;
	top: 0px;
	width: 800px;
	height: 80px;
}

#ScrollSegment .item {
	border: 1px solid transparent;
	width: 90px;
	height: 60px;
}

#ScrollSegment .scroller {
	position: absolute;
	overflow: visible;
	left: 130px;
	top: 100px;
	width: 550px;
	height: 10px;
	background: url(image/scroller.png) repeat-x;
	/*repeat:repeat-x;*/
}

#ScrollSegment .slider {
	position: absolute;
	left: -8px;
	top: -8px;
	width: 16px;
	height: 16px;
	zIndex: 50000;
	background: url(image/slider.png) no-repeat;
}

#ScrollSegment #imgContaner_1 {
	position: absolute;
	left: 30px;
	top: 5px;
}

#ScrollSegment #imgContaner_2 {
	position: absolute;
	left: 140px;
	top: 5px;
}

#ScrollSegment #imgContaner_3 {
	position: absolute;
	left: 250px;
	top: 5px;
}

#ScrollSegment #imgContaner_4 {
	position: absolute;
	left: 360px;
	top: 5px;
}

#ScrollSegment #imgContaner_5 {
	position: absolute;
	left: 470px;
	top: 5px;
}

#ScrollSegment img {
	position: relative;
	left: 18px;
	top: 10px;
	width: 80%;
	height: 80%;
}

#ScrollSegment .caption {
	position: relative;
	/*background:#B8CFEE;*/
	background-image: url("/epon/image/caption.png");
	font-size: 15;
	color: #000080;
	top: 0px;
	left: 0px;
	text-align: center;
	z-index: 10000;
}
/***END OF SCROLLER SEGMENT**/
#QuerySegment {
	width: 920px;
	height: 45px;
	position: absolute;
	left: 15px;
	top: 55px;
	background-color: #ffffff;
	border: 1px solid #6593CF;
}

#PanelSegment {
	width: 920px;
	height: 384px;
	position: absolute;
	left: 10px;
	top: 105px;
}

#ScrollSegment {
	width: 920px;
	height: 120px;
	position: absolute;
	left: 15px;
	top: 490px;
	border: 1px solid #6593CF;
}

#OnuTreePanelContainer {
	width: 178px;
	height: 378px;
	position: absolute;
	left: 4px;
	top: -1px;
}

#PanelContainer {
	width: 503px;
	height: 378px;
	position: absolute;
	left: 187px;
	top: 2px;
	border: 1px solid #6593CF;
	background-color: #ffffff;
}

#GridContainer {
	width: 231px;
	height: 378px;
	position: absolute;
	left: 693px;
	top: 2px;
	border: 1px solid #6593CF;
	background-color: #ffffff;
}

#entityTreePanel {
	width: 178px;
	height: 378px;
	position: absolute;
	left: 2px;
	top: 3px;
	background-color: #ffffff;
	border: 1px solid #6593CF;
}

#oltDeviceDiv {
	width: 87px;
	height: 103px;
	position: absolute;
	left: 43px; 
	top: 37px; 
}

#entityContainer {
	width: 400px;
	height: 89px;
	position: absolute;
	left: 30px;
	top: 213px
}

#onu_grid {
	width: 230px;
	height: 375px;
	position: absolute;
	left: 1px;
	top: 0px
}

.queryPonBoard {
	position: absolute;
	left: 15px;
	top: 13px;
	width: 105px;
	height: 20px
}

.queryPonBoard div {
	position: absolute;
	left: 0px;
	top: 3px;
	width: 44px;
	height: 20px
}

.queryPonBoard select {
	position: absolute;
	left: 56px;
	top: 0px;
	width: 70px;
	height: 20px
}

.queryPonPort {
	position: absolute;
	left: 139px;
	top: 13px;
	width: 157px;
	height: 20px
}

.queryPonPort div {
	position: absolute;
	left: 0px;
	top: 2px;
	width: 70px;
	height: 20px
}

.queryPonPort select {
	position: absolute;
	left: 82px;
	top: 0px;
	width: 70px;
	height: 20px
}

.queryPonLLID {
	position: absolute;
	left: 281px;
	top: 13px;
	width: 151px;
	height: 20px
}

.queryPonLLID div {
	position: absolute;
	left: 1px;
	top: 3px;
	width: 74px;
	height: 20px
}

.queryPonLLID input {
	position: absolute;
	left: 82px;
	top: 0px;
	width: 72px;
	height: 20px
}

.queryPonMac {
	position: absolute;
	left: 414px;
	top: 13px;
	width: 179px;
	height: 20px
}

.queryPonMac div {
	position: absolute;
	left: 1px;
	top: 2px;
	width: 74px;
	height: 20px
}

.queryPonMac input {
	position: absolute;
	left: 82px;
	top: 0px;
	width: 111px;
	height: 20px
}

#QuerySegment #search {
	position: absolute;
	top: 13px;
	left: 650px;
}

#QuerySegment #refreshOnuInfo {
	position: absolute;
	top: 13px;
	left: 770px;
}

#QuerySegment #refreshText {
	position: absolute;
	top: 16px;
	left: 441px;
}

#loadingMask {
	position: absolute;
	left: 0px;
	top: 35px;
	width: 950px;
	height: 575px;
	background-color: #000000;
	z-index: 1;
	filter: 'alpha(opacity=50)';
	opacity: 0.5;
}

.loadingText {
	position: absolute;
	left: 420px;
	top: 88px;
	font-size: 10;
	color: 'white';
}

.loadingImage {
	position: absolute;
	left: 380px;
	top: 79px;
	height: 32px;
	width: 32px;
}

.enableClass {
	background-image: url('/epon/image/checked.gif') no-repeat
}

.disableClass {
	background-image: url('/epon/image/unchecked.gif') no-repeat
}

.spliterClass {
	position: absolute;
	left:150px;
	top: 52px;
	width: 20px;
	height: 20px;
	backgroundColor: #000;
	z-index:60000;
}
</STYLE>
<script>

/**
 * 阻止事件冒泡
 */
function preventBubble(e) {
	e = e || window.event;
	if(e.stopPropagation) {
		e.stopPropagation();
		//火狐阻止冒泡
	} else {
		e.cancelBubble = true;
		//IE阻止冒泡
	}
}

/**
 * 等待框
 */
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
/**
 * 清理页面效果
 */
function clearPage() {
	while(divCache.length != 0) {
		var divCacheObject = divCache.pop();
		document.getElementById(divCacheObject).style.border = '2px solid transparent';
		//修改上次点击的div的样式
	}
}
/**
 * 时间换算。data原型修改过，故此方法多余
 */
function arrive_timer_format(s) {
	var t;
	s = s * 1000;
	if(s > -1) {
		hour = Math.floor(s / 3600000);
		min = Math.floor(s / 60000) % 60;
		sec = Math.floor(s / 1000) % 60;
		day = parseInt(hour / 24);
		if(day > 0) {
			hour = hour - 24 * day;
			t = day + I18N.CMC.label.day + hour + I18N.CMC.label.hour;
		} else {
			t = hour + I18N.CMC.label.hour;
		}
		if(min < 10) {
			t += "0";
		}
		t += min + I18N.CMC.label.minutes;
		if(sec < 10) {
			t += "0";
		}
		t += sec + I18N.CMC.label.seconds;
	}
	return t;
}

/**
 * Mac地址有效性检查
 */
function checkOnuMac() {
	var reg0 = /^([0-9a-f]{2})(([/\s][0-9a-f]{2}){5})+$/i;
	var reg1 = /^([0-9a-f]{2})(([/-][0-9a-f]{2}){5})+$/i;
	var reg2 = /^([0-9a-f]{2})(([/:][0-9a-f]{2}){5})+$/i;
	var reg4 = /^([0-9a-f]{12})+$/i;
	var reg5 = /^([0-9a-f]{4})(([.][0-9a-f]{4}){2})+$/i;
	if($("#mac").val() == "") {
		macStatusFlag = 1;
		return true;
	}
	if((reg0.exec($("#mac").val()) || reg1.exec($("#mac").val()) || reg2.exec($("#mac").val())) && $("#mac").val().length == 17) {
		macStatusFlag = 2;
		return true;
	}
	if(reg5.exec($("#mac").val()) && $("#mac").val().length == 14){
		macStatusFlag = 2;
		return true;
	}
	if(reg4.exec($("#mac").val()) && $("#mac").val().length == 12) {
		macStatusFlag = 2;
		return true;
	}
	return false;
}
//通过mibIndex获得num
function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
		break;
	case 3: num = (index & 0xFF00) >> 8;//ONU
		break;
	case 4: num = index & 0xFF;//UNI
		break;
	}
	return num;
}
//通过index获得location
function getLocationByIndex(index, type){
	var tmp = index.toString(16);
	var t = tmp.substring(0, tmp.length-4) + "" + tmp.substring(tmp.length-2, tmp.length);
	t = parseInt(t, 16);
	var loc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
	if(type == "uni"){
		loc = loc + "/" + getNum(t, 4);
	}
	return loc;
}


Date.prototype.dateDiff = function(date, flag) {
	var msCount;
	var diff = this.getTime() - date.getTime();
	switch (flag) {
		case "ms":
			msCount = 1;
			break;
		case "s":
			msCount = 1000;
			break;
		case "m":
			msCount = 60 * 1000;
			break;
		case "h":
			msCount = 60 * 60 * 1000;
			break;
		case "d":
			msCount = 24 * 60 * 60 * 1000;
			break;
	}
	return Math.floor(diff / msCount);
};
			
function refreshBtOver(){
	var tempPonId = $("#slotPonList").val();
	$("#search").hide();
	$(".queryPonMac").hide();
	$(".queryPonLLID").hide();
	if(tempPonId == -1){
		$("#refreshText").show().html(I18N.CMC.tip.getOnuInfoNeedLongTime);
	}else{
		$("#refreshText").show().html(I18N.CMC.tip.getOnuInfo);
	}
}
function refreshBtOut(){
	$("#refreshText").hide();
	$("#search, .queryPonMac, .queryPonLLID").show();
}

function createRefreshMask(){
	$("#loadingMask").show();
	$(".loadingText").html("<p style='color:white'><fmt:message bundle='${cmc}' key='CMC.label.gettingData'/></p>");
	$("#loadingMask").attr("style","z-index:100000");
}

function removeMask(){
	$("#loadingMask").animate({
		opacity:0.5
	},{
		speed: 'slow',
		complete :function(){
			$("#loadingMask").animate({
				opacity:0
			},{
				speed: 'slow',
				complete :function(){
					$("#loadingMask").css("zIndex",1);
					$("#loadingMask").hide();
				}
			});
		}
	});
}
</script>
</html>