/*
**
**var clock6 = new Nm3kClock({
**			renderTo: 'seven',//要渲染到哪个div的id;
**			startTime: 4678915,//开始时间（秒数）;
**			type: 'cir',//类型：cir,grayCalendar,whiteCalendar;
**			language: 'Chinese'//语言，Chinese,English;
**		});
**		clock6.init();
**
**
*/
function Nm3kClock(obj){
	this.renderTo = obj.renderTo; //渲染到某个div的id;
	this.error = ( (typeof(obj.startTime)).toLowerCase() == 'number' && (obj.startTime >= 0) ) ? false : true;
	this.startTime =  this.error ? 0 : obj.startTime;// 开始时间,以秒计算,如果存在错误，那么现实0:0:0:0,否则显示真实的时间;	
	this.movement = null;
	this.type = obj.type;//灰色grayCalendar;
	this.language = "Chinese";//默认语言为中文;
	this.language = (obj.language == "English") ? "English" : "Chinese";
	this.errorTip = obj.errorTip ? obj.errorTip : '@COMMON.nm3kClockErr@'; //如果从设备上获取的时间是-1，则表示没有获取到正确的时间，那么显示一个提示信息;
}
Nm3kClock.prototype.constructor = "Nm3kClock";
Nm3kClock.prototype.init = function(){
	var str = '';
	switch(this.type){
		case "grayCalendar":
			if(this.language == "English"){//如果是英文;
				str+= '<div class="nm3kClockContainerEnglish">';
			}else{//中文;
				str+= '<div class="nm3kClockContainer">';
			};//end if;
			
			str+= '	<ul class="nm3kGrayClock">';
			str+= '		<li class="nm3kGrayClockDay2 dayContainer">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '		<li class="nm3kGrayClockHour">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '		<li class="nm3kGrayClockMin">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '		<li class="nm3kGrayClockSec">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '	</ul>';
			str+= ' <p class="nm3kClockErrorTip"><span>'+ this.errorTip +'</span></p>';
			str+= '</div>';
		break;
		case "whiteCalendar":
			if(this.language == "English"){//如果是英文;
				str += '<div class="nm3kWhiteClockContainerEnglish">';			
			}else{
				str += '<div class="nm3kWhiteClockContainer">';
			}
			str += '	<ul class="nm3kWhiteClock">';
			str += '		<li class="nm3kWhiteClockFirst"></li>';
			str += '		<li class="nm3kWhiteClockDay2 dayContainer">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockHour">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockMin">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockSec">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockLast"></li>';
			str += '	</ul>';
			str+= ' <p class="nm3kClockErrorTip" style="top:-85px;"><span>'+ this.errorTip +'</span></p>';
			str += '</div>';
		break;
		case "cir":
			str += '<div class="nm3kCirClockContainer">';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirDayNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirDaysEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirDays"></div>';
			}
			str += '	</div>';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirHourNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirHoursEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirHours"></div>';
			}
			str += '	</div>';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirMinNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirMinsEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirMins"></div>';
			}
			str += '	</div>';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirSecNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirSecsEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirSecs"></div>';
			}
			str += '	</div>';
			str+= ' <p class="nm3kClockErrorTip" style="top:-60px;"><span>'+ this.errorTip +'</span></p>';
			str += '</div>';
		break;
	}
	$("#"+this.renderTo).html(str);
	if(this.error){//存在错误，不定时加载时间;
		var wrapCss = '';
		switch(this.type){
			case 'grayCalendar':
				wrapCss = '.nm3kGrayClock';
				break;
			case 'cir':
				wrapCss = '.nm3kCirBg';
				break;
			case 'whiteCalendar':
				wrapCss = '.nm3kWhiteClock';
				break;
		}
		var $renderTo = $("#"+this.renderTo);
		$renderTo.find(wrapCss).css({opacity: 0.2});
		$renderTo.find(".nm3kClockErrorTip").css({display: 'block'});
	}else{
		this.movement = window.setInterval(Nm3kClock.createDelegate(this, this.addSeconds),1000);
	}
};// end init;
Nm3kClock.prototype.addSeconds = function(){
	this.startTime++;
	var day = parseInt(this.startTime / 60 / 60 / 24);
	var hour = parseInt((this.startTime / 60 / 60) % 24);
	var mins = parseInt((this.startTime / 60 ) % 60);
	var sec = parseInt((this.startTime) % 60);
	
	var day4 = parseInt(day / 1000);
	var day3 = parseInt(day % 1000 / 100);
	var day2 = parseInt(day % 100 / 10);
	var day1 = parseInt(day % 10);
	var hour2 = parseInt(hour / 10);
	var hour1 = parseInt(hour % 10);
	var min2 = parseInt(mins / 10);
	var min1 = parseInt(mins % 10);
	var sec2 = parseInt(sec / 10);
	var sec1 = parseInt(sec % 10);
	
	switch(this.type){
		case "grayCalendar":
			var secStr = '<div class="nm3kGrayClockNum'+ sec2 +'"></div><div class="nm3kGrayClockNum'+ sec1 +'"></div>';
			$("#"+ this.renderTo +" .nm3kGrayClockSec").html(secStr);
			var minStr = '<div class="nm3kGrayClockNum'+ min2 +'"></div><div class="nm3kGrayClockNum'+ min1 +'"></div>';
			$("#"+ this.renderTo +" .nm3kGrayClockMin").html(minStr);
			var hourStr = '<div class="nm3kGrayClockNum'+ hour2 +'"></div><div class="nm3kGrayClockNum'+ hour1 +'"></div>'; 
			$("#"+ this.renderTo +" .nm3kGrayClockHour").html(hourStr);
			var dayStr = '';
			if(day4 == 0){
				if(day3 == 0){//天数为2位数字;				
					dayStr = '<div class="nm3kGrayClockNum'+ day2 +'"></div><div class="nm3kGrayClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kGrayClockDay2 dayContainer").html(dayStr);
				}else{//天数为3位数字;
					dayStr = '<div class="nm3kGrayClockNum'+ day3 +'"></div><div class="nm3kGrayClockNum'+ day2 +'"></div><div class="nm3kGrayClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kGrayClockDay3 dayContainer").html(dayStr);;
				};//end if day3;
			}else{//天数为4位数字;
					dayStr = '<div class="nm3kGrayClockNum'+ day4 +'"></div><div class="nm3kGrayClockNum'+ day3 +'"></div><div class="nm3kGrayClockNum'+ day2 +'"></div><div class="nm3kGrayClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kGrayClockDay4 dayContainer").html(dayStr);
			};//end if day4;
		break;
		case "whiteCalendar":
			var secStr = '<div class="nm3kWhiteClockNum'+ sec2 +'"></div><div class="nm3kWhiteClockNum'+ sec1 +'"></div>';
			var minStr = '<div class="nm3kWhiteClockNum'+ min2 +'"></div><div class="nm3kWhiteClockNum'+ min1 +'"></div>';
			var hourStr = '<div class="nm3kWhiteClockNum'+ hour2 +'"></div><div class="nm3kWhiteClockNum'+ hour1 +'"></div>'; 
			
			$("#"+ this.renderTo +" .nm3kWhiteClockSec").html(secStr);				
			$("#"+ this.renderTo +" .nm3kWhiteClockMin").html(minStr);
			$("#"+ this.renderTo +" .nm3kWhiteClockHour").html(hourStr);
			
			var dayStr = '';
			if(day4 == 0){
				if(day3 == 0){//天数为2位数字;				
					dayStr = '<div class="nm3kWhiteClockNum'+ day2 +'"></div><div class="nm3kWhiteClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kWhiteClockDay2 dayContainer").html(dayStr);
				}else{//天数为3位数字;
					dayStr = '<div class="nm3kWhiteClockNum'+ day3 +'"></div><div class="nm3kWhiteClockNum'+ day2 +'"></div><div class="nm3kWhiteClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kWhiteClockDay3 dayContainer").html(dayStr);;
				};//end if day3;
			}else{//天数为4位数字;
					dayStr = '<div class="nm3kWhiteClockNum'+ day4 +'"></div><div class="nm3kWhiteClockNum'+ day3 +'"></div><div class="nm3kWhiteClockNum'+ day2 +'"></div><div class="nm3kWhiteClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kWhiteClockDay4 dayContainer").html(dayStr);
			};//end if day4;				
		break;
		case "cir":
			var secStr = '<li class="nm3kCirNum'+ sec2 +'"></li><li class="nm3kCirNum'+ sec1 +'"></li>';
			var minStr = '<li class="nm3kCirNum'+ min2 +'"></li><li class="nm3kCirNum'+ min1 +'"></li>';
			var hourStr = '<li class="nm3kCirNum'+ hour2 +'"></li><li class="nm3kCirNum'+ hour1 +'"></li>'; 				
			
			var parentClsSec = this.getLoading(sec);
			var parentClsMin = this.getLoading(mins);
			var parentClsHour = this.getLoading(hour);
			
			$("#"+ this.renderTo +" .nm3kCirSecNum").html(secStr).parent().attr("class",parentClsSec);
			$("#"+ this.renderTo +" .nm3kCirMinNum").html(minStr).parent().attr("class",parentClsMin);
			$("#"+ this.renderTo +" .nm3kCirHourNum").html(hourStr).parent().attr("class",parentClsHour);
			
			var dayStr = '';
			if(day4 == 0){
				if(day3 == 0){//天数为2位数字;				
					dayStr = '<li class="nm3kCirNum'+ day2 +'"></li><li class="nm3kCirNum'+ day1 +'"></li>';
					$("#"+ this.renderTo +" .nm3kCirDayNum").html(dayStr);
				}else{//天数为3位数字;
					dayStr = '<li class="nm3kCirNum'+ day3 +'"></li><li class="nm3kCirNum'+ day2 +'"></li><li class="nm3kCirNum'+ day1 +'"></li>';
					$("#"+ this.renderTo +" .nm3kCirDayNum").html(dayStr);;
				};//end if day3;
			}else{//天数为4位数字;
					dayStr = '<li class="nm3kCirNum'+ day4 +'"></li><li class="nm3kCirNum'+ day3 +'"></li><li class="nm3kCirNum'+ day2 +'"></li><li class="nm3kCirNum'+ day1 +'"></li>';
					$("#"+ this.renderTo +" .nm3kCirDayNum").html(dayStr);
			};//end if day4;
			if(day != 0){
				$("#"+ this.renderTo +" .nm3kCirDayNum").parent().attr("class","nm3kCirBg1");
			}
		break;
	}
	
	
	
	
};//end addSeconds;
Nm3kClock.prototype.stopRecord = function(){
	clearInterval(this.movement);
	this.movement = null;
};//end stopRecord;
Nm3kClock.prototype.update = function(obj){
	this.error = ( (typeof(obj.startTime)).toLowerCase() == 'number' && (obj.startTime >= 0) ) ? false : true;
	this.startTime =  this.error ? 0 : obj.startTime;
	var wrapCss = '';
	switch(this.type){
		case 'grayCalendar':
			wrapCss = '.nm3kGrayClock';
			break;
		case 'cir':
			wrapCss = '.nm3kCirBg';
			break;
		case 'whiteCalendar':
			wrapCss = '.nm3kWhiteClock';
			break;
	}
	var $renderTo = $("#"+this.renderTo);
	if(this.error){
		this.stopRecord();
		$renderTo.find(wrapCss).css({opacity: 0.2});
		$renderTo.find(".nm3kClockErrorTip").css({display: 'block'});
	}else{
		$renderTo.find(wrapCss).css({opacity: 1});
		$renderTo.find(".nm3kClockErrorTip").css({display: 'none'});	
		if(this.movement == null){
			this.movement = window.setInterval(Nm3kClock.createDelegate(this, this.addSeconds),1000);
		}
	}
};//end update;

Nm3kClock.prototype.getLoading = function(para){
	var parentClassStr = "";
	if(para >= 1 && para <= 7){
		parentClassStr = "nm3kCirBg1";
	}else if(para >= 8 && para <= 22){
		parentClassStr = "nm3kCirBg2";
	}else if(para >= 23 && para <= 37){
		parentClassStr = "nm3kCirBg3";
	}else if(para >= 38 && para <= 59){
		parentClassStr = "nm3kCirBg4";
	}else{
		parentClassStr = "nm3kCirBg";
	};//end if else;
	return parentClassStr;
}
Nm3kClock.createDelegate = function(object, method) {
	var delegate = function(){
		method.call(object);
	}
	 return delegate;
};//end createDelegate;

