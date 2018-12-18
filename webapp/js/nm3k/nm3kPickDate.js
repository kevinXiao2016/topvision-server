/************************************************
 * 
 * 
 * 快速选择日期
 * 要求必须有2个ext的DateTimeField控件,和查询的函数
 * 传入firstDayOfWeek : 1 的话一周开始日期为星期一
 * <a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick=""><span><i class="miniIcoSearch"></i><b>查询</b></span></a>
 * 
 * 
/************************************************/
var nm3kPickData;
(function(){
	var dlDom = [
	    '<dl>',
	        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.today@</a></dd>',
	        '<dt> | </dt>',
	        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.yesterday@</a></dd>',
	        '<dt> | </dt>',
	        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.thisWeek@</a></dd>',
	        '<dt> | </dt>',
	        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.thisMonth@</a></dd>',
	    '</dl>'
	].join("");
	
	nm3kPickData = function(o){
		if( !o.startTime || !o.endTime || !o.searchFunction ){
			//alert("参数传递错误");
			return;
		}
		var stTime2 = o.startTime;
		var etTime2 = o.endTime;
		var firstDayOfWeek = top.firstDayOfWeek ? top.firstDayOfWeek-1 : 0;////////////////////////////////////这里读取top层的一个变量;
		$("a.normalBtnWithDateTime").live("mouseenter", function(){
			var $me = $(this),
			w = $me.outerWidth(),
			h = $me.outerHeight(),
			l = $me.offset().left,
			t = $me.offset().top,
			$showDiv = $("#searchWithDateTimeWrap");
			if($showDiv.length < 1){
				var str = '<div id="searchWithDateTimeWrap" class="searchWithDateTimeWrap">';
				    str += dlDom;
				    /*str +=    '<dl>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.today@</a></dd>';
				    str +=        '<dt> | </dt>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.yesterday@</a></dd>';
				    str +=        '<dt> | </dt>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.thisWeek@</a></dd>';
				    str +=        '<dt> | </dt>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">@NM3K/nm3kPickDate.thisMonth@</a></dd>';
				    str +=    '</dl>';*/
				    str += '</div>';
				$("body").append(str);
			}else{
				$showDiv.empty().html(dlDom);
			}	
			var openW = $("#searchWithDateTimeWrap").outerWidth();
			$("#searchWithDateTimeWrap").css({
				left : l - openW + w,
				top : t + h - 1,
				display: "block"
			})
		}).live("mouseleave", function(){
			$("#searchWithDateTimeWrap").css({"display":"none"});
		});
		$("#searchWithDateTimeWrap").live("mouseenter",function(){
			$(this).css({display:"block"});
		}).live("mouseleave",function(){
			$(this).css({display:"none"});
		})
		$("#searchWithDateTimeWrap").live("click",function(e){
			var $me = $(e.target);
			if($me.get(0).tagName == "A"){
				var num = $("#searchWithDateTimeWrap dd").index($me.parent());
				var stClockTime = " 00:00:00";
				var etClockTime = " 23:59:59";
				if(o.withoutTime){
					stClockTime = "";
					etClockTime = "";
				}
				var sTimeStr,eTimeStr;
				switch(num){
					case 0:
						sTimeStr = getDateStr(0) + stClockTime;
						eTimeStr = getDateStr(0) + etClockTime;
						stTime2.setValue(sTimeStr);	
						etTime2.setValue(eTimeStr);
					break;
					case 1:
						sTimeStr = getDateStr(-1) + stClockTime;
						eTimeStr = getDateStr(-1) + etClockTime;
						stTime2.setValue(sTimeStr);	
						etTime2.setValue(eTimeStr);	
					break;
					case 2:
						sTimeStr = getWeekStartDateAndEndDate(firstDayOfWeek)["startTime"] + stClockTime;
						eTimeStr = getWeekStartDateAndEndDate(firstDayOfWeek)["endTime"] + etClockTime;
						stTime2.setValue(sTimeStr);	
						etTime2.setValue(eTimeStr);
					break;
					case 3:
						sTimeStr = getMonthStartDate() + stClockTime;
						//eTimeStr = getMonthEndDate() + etClockTime;
						eTimeStr = getDateStr(0) + etClockTime;
						stTime2.setValue(sTimeStr);	
						etTime2.setValue(eTimeStr);	
					break;
				};//end switch;
				o.searchFunction.call(window);
				$("#searchWithDateTimeWrap").css({display:"none"})
			}
		});
	};//end function;
	
	
	
	
	
	/*******************************************************************
	 * 
	 * 通用部分，获取一些时间段，函数来源于网络
	 * 
	 *******************************************************************/
	//获取今天，昨天，明天;
	function getDateStr(AddDayCount) {
	    var dd = new Date();
	    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;//获取当前月份的日期
	    if(m.toString().length == 1){m = "0"+m;}
	    var d = dd.getDate();
	    if(d.toString().length == 1){d = "0"+d;}
	    return y+"-"+m+"-"+d;
	}
	//格局化日期：yyyy-MM-dd 
	function formatDate(date) { 
		var myyear = date.getFullYear(); 
		var mymonth = date.getMonth()+1; 
		var myweekday = date.getDate();
		if(mymonth < 10){ 
			mymonth = "0" + mymonth; 
		} 
		if(myweekday < 10){ 
			myweekday = "0" + myweekday; 
		} 
		return (myyear+"-"+mymonth + "-" + myweekday); 
	}
	
	function getWeekStartDateAndEndDate(startWeekDay){
		 var startWeekDay = startWeekDay || 0;
	     var currentDate = new Date(),
	       currentDayOfWeek = currentDate.getDay(),
	       startDate = new Date();
	     if(currentDayOfWeek<startWeekDay){
	       //如果当前日小于开始日，则统计上周startDay-本周当前日
	       startDate.setTime(startDate.getTime()-(currentDayOfWeek-startWeekDay+7)*24*3600*1000);
	     }else{
	       //如果当前日大于等于startDay，则统计本周startDay至当前日
	       startDate.setTime(startDate.getTime()-(currentDayOfWeek-startWeekDay)*24*3600*1000);
	     }
	     startDate.setHours(0);
	     startDate.setMinutes(0);
	     startDate.setSeconds(0);
	     startDate.setMilliseconds(0);
	     return {
	       startTime : formatDate(startDate),
	       endTime : formatDate(currentDate)
	     };
	}
	/*//获得本周的开端日期 
	function getWeekStartDate() { 
		var now = new Date(); //当前日期 
		var nowDayOfWeek = now.getDay(); //今天本周的第几天 
		var nowDay = now.getDate(); //当前日 
		var nowMonth = now.getMonth(); //当前月 
		var nowYear = now.getYear(); //当前年 
		nowYear += (nowYear < 2000) ? 1900 : 0; //
	
		var lastMonthDate = new Date(); //上月日期 
		lastMonthDate.setDate(1); 
		lastMonthDate.setMonth(lastMonthDate.getMonth()-1); 
		var lastYear = lastMonthDate.getYear(); 
		var lastMonth = lastMonthDate.getMonth();
		var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek); 
		return formatDate(weekStartDate); 
	}*/
	
	//获得本周的停止日期 
	/*function getWeekEndDate() { 
		var now = new Date(); //当前日期 
		var nowDayOfWeek = now.getDay(); //今天本周的第几天 
		var nowDay = now.getDate(); //当前日 
		var nowMonth = now.getMonth(); //当前月 
		var nowYear = now.getYear(); //当前年 
		nowYear += (nowYear < 2000) ? 1900 : 0; //
	
		var lastMonthDate = new Date(); //上月日期 
		lastMonthDate.setDate(1); 
		lastMonthDate.setMonth(lastMonthDate.getMonth()-1); 
		var lastYear = lastMonthDate.getYear(); 
		var lastMonth = lastMonthDate.getMonth();
		var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek)); 
		return formatDate(weekEndDate); 
	}*/
	
	//获得本月的开端日期 
	function getMonthStartDate(){ 
		var now = new Date(); //当前日期 
		var nowDayOfWeek = now.getDay(); //今天本周的第几天 
		var nowDay = now.getDate(); //当前日 
		var nowMonth = now.getMonth(); //当前月 
		var nowYear = now.getYear(); //当前年 
		nowYear += (nowYear < 2000) ? 1900 : 0; //
		var monthStartDate = new Date(nowYear, nowMonth, 1); 
		return formatDate(monthStartDate); 
	}
	//获得本月的停止日期 
	function getMonthEndDate(){ 
		var now = new Date(); //当前日期 
		var nowDayOfWeek = now.getDay(); //今天本周的第几天 
		var nowDay = now.getDate(); //当前日 
		var nowMonth = now.getMonth(); //当前月 
		var nowYear = now.getYear(); //当前年 
		nowYear += (nowYear < 2000) ? 1900 : 0; //
		var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth)); 
		return formatDate(monthEndDate); 
	}
	//获得某月的天数 
	function getMonthDays(myMonth){ 
		var now = new Date(); //当前日期 
		var nowDayOfWeek = now.getDay(); //今天本周的第几天 
		var nowDay = now.getDate(); //当前日 
		var nowMonth = now.getMonth(); //当前月 
		var nowYear = now.getYear(); //当前年 
		nowYear += (nowYear < 2000) ? 1900 : 0; 
		var monthStartDate = new Date(nowYear, myMonth, 1); 
		var monthEndDate = new Date(nowYear, myMonth + 1, 1); 
		var days = (monthEndDate - monthStartDate)/(1000 * 60 * 60 * 24); 
		return days; 
	}
})();