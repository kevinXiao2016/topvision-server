var DOC = document;
function ipV4Input(id,pNode,onChange){
	this.id = id;
    if (onChange != null && typeof onChange == 'function') {
        this.onChange = onChange;
    } else {
        this.onChange = new Function();
    }
	this.onEnterKey = new Function();
	this.disabled = false;
	ipV4Input.all[id] = this;
	if(pNode){
		if(typeof pNode == "string"){
			var pa = $("#" + pNode);
			pNode = DOC.getElementById(pNode);
			pNode.innerHTML = this.toString();
			pa.find(".ipV4InputClass").css({"font-size":"12px",height:"22px"}).addClass("normalInputIp");
			pa.find(".ipV4InputClass input").css({"border":"0px solid #ccc","font-size":"12px","height":"20px","width":"24px",
				"background":"transparent","text-align":"center"});
		}else if(typeof pNode == "object" && typeof pNode.tagName != 'undefined'){
			pNode.innerHTML = this.toString();
			$(pa).find(".ipV4InputClass").css({"border":"1px solid #8bb8f3","font-size":"12px"});
			$(pa).find(".ipV4InputClass input").css({"border":"0px solid #ccc","font-size":"12px","height":"16px","width":"24px",
				"background":"transparent","text-align":"center"});
		}
	}
}
//获取IP
function getIpValue(id){
	var ip = new Array();
	for(var i=4; i>0; i--){
		if($("#"+id+"_"+i).val() != ""){
			ip.unshift($("#"+id+"_"+i).val());
		}
	}
	return ip.join(".").replace(/[^\d.]/ig, "");
}
//设置IP
function setIpValue(id, ip){
	if(ip!=null){
		ip = ip.replace(/[^\d.]/ig, "");
		if(ip=="" || !checkedIpValue(ip)){
			ip = "..."
		}
		ip = ip.split(".");
		for(var k=3; k>ip.length-1; k--){
			ip[k] = "";
		}
		$("#"+id+"_1").val(ip[0]);
		$("#"+id+"_2").val(ip[1]);
		$("#"+id+"_3").val(ip[2]);
		$("#"+id+"_4").val(ip[3]);
	}
}
//检验IP是否格式正确
function checkedIpValue(ip){
	if(!ip || ip.toString().indexOf(".") == -1){
		return false;
	}
	ip = ip.split(".");
	if(ip.length != 4){
		return false;
	}else{
		for(var k=0; k<4; k++){
			if(ip[k].length==0 || isNaN(ip[k]) || ip[k].length>3 || parseInt(ip[k])<0 || parseInt(ip[k])>255){
				return false
			}
		}
	}
	return true;
}
//检验IP mask是否格式正确
function checkedIpMask(ip){
	if(checkedIpValue(ip)){
		ip = ip.split(".");
		var ip_binary = (parseInt(ip[0]) + 256).toString(2).substring(1) + (parseInt(ip[1]) + 256).toString(2).substring(1)
						+ (parseInt(ip[2]) + 256).toString(2).substring(1) + (parseInt(ip[3]) + 256).toString(2).substring(1);
		if (ip_binary.indexOf("01") == -1){
			return true;
		}
	}
	return false;
}
//检查是否为组播IP，是则返回 true
function checkIsMulticast(ip){
	ip = ip.split(".");
	if(parseInt(ip[0])>=224 && parseInt(ip[0])<=239){
		return true;
	}
	if(ip[0] == 224 && ip[1]==0 && ip[2] < 0){
		return false;
	}
	return false;
}
//igmp组播地址为范围224.0.1.0到239.255.255.255
function checkDDIsMulticast(ip){
	ip = ip.split(".");
	if(parseInt(ip[0], 10)>=224 && parseInt(ip[0], 10)<=239){
		if(ip[0]==224 && ip[1] == 0 && ip[2] == 0){
			return false;
		}
		return true;
	}
	return false;
}
//检查是否为保留IP，是则返回 true
function checkIsReservedIp(ip){	
	ip = ip.split(".");
	if(parseInt(ip[0])==0 || parseInt(ip[0])==127){
		return true;
	}
	if(parseInt(ip[0])==191 && parseInt(ip[1])==255){
		return true;
	}
	if(parseInt(ip[0])==192 && parseInt(ip[1])==0 && parseInt(ip[2])==0){
		return true;
	}
	if(parseInt(ip[0])==223 && parseInt(ip[1])==255 && parseInt(ip[2])==255){
		return true;
	}
	return false;
}
//检查是否为A类、B类、C类Ip，是则返回true
function checkIsNomalIp(ip){
	ip = ip.split(".");
	if(parseInt(ip[0])>=1 && parseInt(ip[0])<=223 && parseInt(ip[0])!=127){
		return true;
	}
	return false;
}
//IP地址是否在同一个网段
function checkIsSameNet(sIp, sMask, dIp, dMask){
  if(ipAAndipB(sIp, sMask) == ipAAndipB(dIp,dMask)){
      return true;
  }
  return false;
}
//检验IP是否填写完整
function ipIsFilled(id){
	for(var a=1; a<5; a++){
		if($("#" + id + "_" + a).val() == ""){
			return false;
		}
	}
	return true;
}
//设置IP输入框是否允许输入，true:可输入
function setIpEnable(id, bEnable){
	var b=!bEnable;
	var el = DOC.getElementById(id); 
	el.disabled= b;
	el.readOnly = b;
	var boxes = document.getElementById(id).getElementsByTagName("input");
	for(var i=0; i<boxes.length; i++){
		boxes[i].disabeld = b;
		boxes[i].readOnly = b;
	}
	if( bEnable == true ){
		$("#"+id).removeClass("normalInputDisabled")
	}else{
		$("#"+id).addClass("normalInputDisabled");
	}
	//document.getElementById(id).className = bEnable ? "ipV4InputClass oneInput" : "ipV4InputClass oneInputDisabled";
}
//设置焦点到IP输入框的位置
function ipFocus(id, index){	//index: 1-4
	index = (isNaN(index) || index < 1 || index > 4) ? 1 : parseFloat(index);
	$("#"+id+"_"+index).focus();
}
//设置每格的宽度
function setIpCellWidth(id, width){
	if(isNaN(width) && width.indexOf("px") > -1){
		width = parseInt(width.substring(0, width.length-2));
	}
	if(isNaN(parseInt(width)) || width < 22){
		width = 24;
	}
	if(id == "allIpInput" || id == "whole" || id == "all"){
		$(".ipV4InputClass input").width(width);
	}else{
		for(var i=1; i<5; i++){
			$("#"+id+"_"+i).width(width);
		}
	}
}
//设置总宽度
function setIpWidth(id, width){
	if(isNaN(width) && width.indexOf("px") > -1){
		width = parseInt(width.substring(0, width.length-2));
	}
	if(id == "allIpInput" || id == "whole" || id == "all"){
		if(!isNaN(parseInt(width)) && width >= 99){
			$(".ipV4InputClass").width(width);
			var cellWidth = (parseInt(width)-11)/4;
			$(".ipV4InputClass input").width(cellWidth);
		}else{
			$(".ipV4InputClass input").width(24);
		}
	}else{
		if(isNaN(parseInt(width)) || width < 99){
			width = 107;
		}
		$("#"+id).width(width);
		var cellWidth = (parseInt(width)-11)/4;
		for(var i=1; i<4; i++){
			$("#"+id+"_"+i).width(cellWidth);
		}
		$("#"+id+"_4").width((width-11)%4+cellWidth);
	}
}
//设置IP输入框的高度
function setIpHeight(id, h){
	if(isNaN(h) && h.indexOf("px") > -1){
		h = parseInt(h.substring(0, h.length-2));
	}
	if(isNaN(parseInt(h)) || h < 10){
		h = 16;
	}
	if(id == "allIpInput" || id == "whole" || id == "all"){
		$(".ipV4InputClass input").css("height", h);
		$(".ipV4InputClass input").css("font-size", h-4);
	}else{
		for(var i=1; i<5; i++){
			$("#"+id+"_"+i).css("height", h);
			$("#"+id+"_"+i).css("font-size", h-4);
		}
	}
}
//设置IP输入框的边框颜色
function setIpBorder(id, bor){
	if(bor == "default"){
		bor = "1px solid #8bb8f3";
	}
	var tmpSearch = (id == "allIpInput" || id == "whole" || id == "all") ? ".ipV4InputClass" : ("#" + id);
	$(tmpSearch).find(".ipV4InputClass").css("border", bor);
}
//设置IP输入框的背景色
function setIpBgColor(id, color){
	if(id == "allIpInput" || id == "whole" || id == "all"){
		$(".ipV4InputClass").css("background-color", color);
		$(".ipV4InputClass input").css("background-color", color);
	}else{
		$("#"+id).css("background-color", color);
	    $("#"+id+"_1").css("background-color", color);
	    $("#"+id+"_2").css("background-color", color);
	    $("#"+id+"_3").css("background-color", color);
	    $("#"+id+"_4").css("background-color", color);
	}
}

ipV4Input.all = {};
ipV4Input.EnabledClassName = "ipV4InputClass oneInput";//启用时class
ipV4Input.DisabledClassName = "ipV4InputClass oneInputDisabled";// 禁用时class
ipV4Input.prototype={
	focus: function(index){	//index: 1-4
		index = (isNaN(index) || index < 1 || index > 4) ? 1 : parseFloat(index);
		$("#"+this.id+"_"+index).focus();
	},	
	setEnable: function(bEnable){
		var b = !bEnable;
		this.disabled=!bEnable;
		var boxes = document.getElementById(this.id).getElementsByTagName("input");
		for(var i=0; i<boxes.length; i++){
			boxes[i].readOnly = b;
		}
		document.getElementById(this.id).className = bEnable ? ipV4Input.EnabledClassName : ipV4Input.DisabledClassName;
	},
	isFilled: function(){
		for(var a=1; a<5; a++){
			if($("#" + this.id + "_" + a).val() == ""){
				return false;
			}
		}
		return true;
	},
	toString: function(){
		var re = String.format("<div id={0} class={1}>", this.id, ipV4Input.EnabledClassName);
		for(var a=1; a<5; a++){
			if(a > 1){
				re += ".";
			}
			var tmpId = this.id + "_" + a;
			re += String.format("<input style='ime-mode:disabled' onkeypress={0} onkeydown={1} onfocus={2} onpaste={3} oninput={4} onchange={5} {6} id={7} {8} />",
				"ipV4Input.evt.keypress(this,event)", "ipV4Input.evt.keydown(this,event)", "ipV4Input.evt.focus(this,event)",
				"ipV4Input.evt.change(this,event)", "ipV4Input.evt.change(this,event)", "ipV4Input.evt.change(this,event)",
				"type='text' size=3 maxlength=3", tmpId, "onkeyup=ipV4Input.evt.keyup(this) onblur=ipV4Input.evt.keyup(this)");
		}
		re += "</div>";
		return re;
	},
	getValue: function(){
		var ip = new Array();
		for(var i=4; i>0; i--){
			if($("#" + this.id+"_"+i).val() != ""){
				ip.unshift($("#"+this.id+"_"+i).val());
			}
		}
		return ip.join(".").replace(/[^\d.]/ig, "");
	},
	setValue: function(ip){
		ip = ip.replace(/[^\d.]/g,"");
		ip = ip.split(".");
		for(var k=3; k>ip.length-1; k--){
			ip[k] = "";
		}
		$("#"+this.id+"_1").val(ip[0]);
		$("#"+this.id+"_2").val(ip[1]);
		$("#"+this.id+"_3").val(ip[2]);
		$("#"+this.id+"_4").val(ip[3]);
	},
	border: function(color){
		if(color == "default"){
			color = "#8bb8f3";
		}
		$("#"+this.id).find(".ipV4InputClass").css("border","1px solid " + color);
	},
	bgColor: function(color){
		for(var a=0; a<5; a++){
			$("#" + this.id + (a ? "_" + a : "")).css("background-color", color);
		}
	},
	height: function(h){
		if(isNaN(h) && h.indexOf("px") > -1){
			h = parseInt(h.substring(0, h.length-2));
		}
		if(isNaN(parseInt(h)) || h < 10){
			h = 16;
		}
		for(var i=1; i<5; i++){
			$("#"+this.id+"_"+i).css("height", h);
			$("#"+this.id+"_"+i).css("font-size", h - 4);
		}
	},
	cellWidth: function(width){
		if(isNaN(width) && width.indexOf("px") > -1){
			width = parseInt(width.substring(0, width.length-2));
		}
		if(isNaN(parseInt(width)) || width < 22){
			width = 24;
		}
		for(var i=1; i<5; i++){
			$("#"+this.id+"_"+i).width(width);
		}
	},
	width: function(width){
		if(isNaN(width) && width.indexOf("px") > -1){
			width = parseInt(width.substring(0, width.length-2));
		}
		if(isNaN(parseInt(width)) || width < 99){
			width = 107;
		}
		$("#" + this.id).width(width);
		var cellWidth = Math.floor( (parseInt(width, 10)-13)/4 );
		for(var i=1; i<5; i++){
			$("#"+this.id+"_"+i).width(cellWidth);
		}
		//$("#"+this.id+"_4").width((width-11)%4+cellWidth);
	},
	setDisabled : function(b){
		var $me = $("#" + this.id);
		if(b){//设置成不可输入;
			$me.addClass("normalInputDisabled");
			$me.find(":text").attr("disabled",true);
		}else{//可以输入;
			$me.removeClass("normalInputDisabled");
			$me.find(":text").attr("disabled",false);
		}
	}
}
ipV4Input.evt = {
	focus: function(obj,evt){
		obj.select();
		
	},
	change: function(obj,evt){
		var v = parseFloat(obj.value);
		if( v >= 0 && v <= 255 ){
			obj.value = v;
		}else{
			obj.value = "";
		}
		ipV4Input.all[ obj.id.replace(/_\d$/,"") ].onChange();
	},
	keyup: function(obj){
		var reg = /^[0-9]+$/g;
		var v = $(obj).val();
		if(v && !reg.test(v)){
			$(obj).val($(obj).val().replace(/[^\d]/g,""));
			v = $(obj).val();
			if(v && !isNaN(v)){
				$(obj).val(parseFloat(v));
			}
		}
	},
	keypress: function(obj,evt){
		var key = evt.charCode || evt.keyCode || obj.which;
		var pos=ipV4Input.evt.getSelection(obj);
		var value=obj.value;
		var c=String.fromCharCode(key);
		if(key>=48 && key<=57){
			value = "" + value.substring(0,pos.start) + c + value.substring(pos.end,value.length);
			if(parseInt(value)<256){
				var id=obj.id;
				var tmpId = /^(.*)_(\d)$/.exec(id);
				var index = tmpId[2];
				var Ip_Id = tmpId[1];
				if(parseInt(value) > 25){
					if(parseInt(index)<4){
						id = id.replace(/(\d)$/,parseInt(index) + 1);
						setTimeout(function(){
							document.getElementById(id).focus();
							document.getElementById(id).select();
						},100);
					}
				}
				setTimeout(function(){
					ipV4Input.all[Ip_Id].onChange();
				},0);
				return true;
			}else{
				if(evt.preventDefault){
					evt.preventDefault();
				}
				evt.returnValue = false;
				return false;
			}
		}else{
			if(evt.preventDefault){
				evt.preventDefault();
			}
			evt.returnValue = false;
		}
	},
	keydown: function(obj,evt){
		var key = evt.charCode || evt.keyCode || obj.which;
		var pos=ipV4Input.evt.getSelection(obj);
		var value=obj.value;
		var c=String.fromCharCode(key);
		var id=obj.id;
		var tmpId = /^(.*)_(\d)$/.exec(id);
		var index = tmpId[2];
		var Ip_Id = tmpId[1];
		switch(key)
		{
		case 13://回车
			ipV4Input.all[Ip_Id].onEnterKey();
			break;
		case 110://.小键盘
		case 190://.主键盘
			if(index < 4){
				if($("#" + id).val() != ""){
					id = id.replace(/(\d)$/, parseInt(index) + 1);
				}
				$("#"+id).focus();
				document.getElementById(id).select();
			}
			break;
		case 38://up
			value = !isNaN(parseInt(value)) ? parseInt(value) : "";
			if(value == ""){
				value = 0;
			}
			obj.value = value == 255 ? 0 : value + 1;
			break;
		case 40://down
			value = !isNaN(parseInt(value)) ? parseInt(value) : "";
			if(value == ""){
				value = 255;
			}
			if(value > 0){
				obj.value = value - 1;
			}
			break;
		case 8://backspace
			if(pos.start > 0){
				return;
			}
		case 37://left
			if(pos.end==0 && index>1){
				id = id.replace(/(\d)$/, parseInt(index) - 1);
				$("#"+id).focus();
				document.getElementById(id).select();
			}
			break;
		case 39://right
			if(pos.start==value.length && index<4){
				id = id.replace(/(\d)$/, parseInt(index) + 1);
				$("#"+id).focus();
				document.getElementById(id).select();
			}
			break;
		}
	},
	//获取选区位置
	getSelection: function(oInput){
		if(oInput.createTextRange){
			var s = document.selection.createRange().duplicate();
			s.moveStart("character", -oInput.value.length);
			var p1 = s.text.length;
			var s = document.selection.createRange().duplicate();
			s.moveEnd("character", oInput.value.length);
			var p2 = oInput.value.lastIndexOf(s.text);
			if(s.text == ""){
				p2 = oInput.value.length;
			}
			return {start:p2,end:p1};
		}else {
			return {start:oInput.selectionStart,end:oInput.selectionEnd};
		}
	}
}
//两个IP的与，一般用于IP及其IP Mask
function ipAAndipB(ipA, ipB){
	if(!checkedIpValue(ipA) || !checkedIpValue(ipB) || ipA == "" || ipB == ""){
		return "0.0.0.0";
	}
	var IPArrayA = ipA.split(".");
	var IPArrayB = ipB.split(".");
	var ip = new Array();
	for(var a=0; a<4; a++){
		ip.push((parseFloat(IPArrayA[a]) & parseFloat(IPArrayB[a])).toString());
	}
	return ip.join(".").replace(/[^\d.]/ig, "");
}
/*************************************
 *      something else              *
 ***********************************/
//闪烁
function flashingById(id,s,t){//s:次数，t：间隔
	if(!isNaN(s) && !isNaN(t)){
		if(s == 0){
			return;
		}else{
			setTimeout(function(){
				s--;
				$("#"+id).fadeOut(t);
				setTimeout(function(){
					$("#"+id).fadeIn(t);
					flashingById(id, s, t);
				}, t);
			}, t);
		}
	}else if(s == "straight"){
		setTimeout(function(){
			$("#"+id).fadeOut(t);
			setTimeout(function(){
				$("#"+id).fadeIn(t);
				flashingById(id, s, t);
			}, t);
		}, t);
	}else{
		s = 3;
		t = 600;
		flashingById(id, s, t);
	}
}
//初始化button
function initButton(s){
	if(s == "all"){
		var tmpX = [75,95,120];
		for(var x=0; x<3; x++){
			initButton(tmpX[x]);
		}
	}else{
		$(".BUTTON"+s).mouseover(function(){
			this.className = 'BUTTON_OVER'+s;
		});
		$(".BUTTON"+s).mouseout(function(){
			this.className = 'BUTTON'+s;
		});
		$(".BUTTON"+s).mousedown(function(){
			this.className = 'BUTTON_PRESSED'+s;
		});
	}
}
//通过id获取元素的位置
function getElPositionById(id){
	var el = document.getElementById(id);
	var x=0;
	while(el) {
		x += el.offsetLeft;
		el = el.offsetParent;
	}
	el = document.getElementById(id);
	var y=0;
	for(var e = el; e; e = e.offsetParent){
		y += e.offsetTop;
	}
	for(e = el.parentNode; e && e != document.body; e=e.parentNode){
		y -= e.scrollTop || 0;
	}
	return [x, y];
}
//中文验证
function isNotChineseStr(str){
	var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d,;!#*$^=])+$/ig;
	if(!reg.exec(str)){
		return false;
	}
	return true;
}
//数字验证
function isOnlyNumStr(str){
	var reg = /^([1-9][0-9]{0,255})+$/ig;
	if(str != '0' && !reg.exec(str)){
		return false;
	}
	return true;
}

function setIpv4Disabled(id,b){
    var $me = $("#" + id);
    if(b){//设置成不可输入;
        $me.addClass("normalInputDisabled");
        $me.find(":text").attr("disabled",true);
    }else{//可以输入;
        $me.removeClass("normalInputDisabled");
        $me.find(":text").attr("disabled",false);
    }
}