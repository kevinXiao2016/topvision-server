<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library ext
	library jquery	
	plugin DateTimeField
	IMPORT js/jquery/Nm3kTabBtn
</Zeta:Loader>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/disabledStyle.css" />
<style type="text/css">
#startTime .x-form-field-wrap{width:auto !important;}
#endTime .x-form-field-wrap{width:auto !important;} 
.searchWithDateTimeWrap{ padding:6px; border:1px solid #ccc; position:absolute; top:0; left:0; z-index:99999; background:#fefeff;}
.searchWithDateTimeWrap dd, .searchWithDateTimeWrap dt{ display:inline;}
.searchWithDateTimeWrap dt{ color:#ccc; padding:0px 2px;}
.searchWithDateTimeWrap dd a{ padding:2px 6px;}

</style>
</head>
<body class="edge10 whiteToBlack" >
	<a class="nearInputBtn"	href="javascript:;" onclick="msg2()" ><span>按钮</span></a>

	<table cellpadding="0" cellspacing="0" class="topSearchTable">
		<tbody>
			<tr>
				<td class="rightBlueTxt" width="100">性能指标组:</td>
				<td width="180">
					<select style="width:160px;" class="normalSel">
						<option>服务质量</option>
					</select>
				</td>
				<td class="rightBlueTxt" width="100">性能指标:</td>
				<td width="180">
					<select style="width:160px;" class="normalSel">
						<option>服务质量</option>
					</select>
				</td>
				<td rowspan="2">
					<a href="#" class="normalBtn" onclick="alertShow()"><span><i class="miniIcoSearch"></i>查询</span></a>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">
					起始时间:			
				</td>
				<td>
					<div id="startTime" style="width:160px;"></div>
				</td>
				<td class="rightBlueTxt">结束时间:</td>
				<td>
					<div id="endTime" style="width:160px;"></div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">
					快捷选择:			
				</td>
				<td colspan="4">
					<div id="test"></div>
				</td>
			</tr>
		</tbody>
	</table>	
	<p class="orangeTxt edge10 txtCenter"></p>
	
	
	<div style="margin-top:250px; border-top:1px solid #ccc; padding-top:20px;">
		<table cellpadding="0" cellspacing="0" class="topSearchTable">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="100">性能指标组:</td>
					<td width="180">
						<select style="width:160px;" class="normalSel">
							<option>服务质量</option>
						</select>
					</td>
					<td class="rightBlueTxt" width="100">性能指标:</td>
					<td width="180">
						<select style="width:160px;" class="normalSel">
							<option>服务质量</option>
						</select>
					</td>
					<td rowspan="2">
						<a href="#" class="normalBtn normalBtnWithDateTime" onclick="searchFn()"><span><i class="miniIcoSearch"></i><b>查询</b></span></a>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">
						起始时间:			
					</td>
					<td>
						<div id="startTime2" style="width:160px;"></div>
					</td>
					<td class="rightBlueTxt">结束时间:</td>
					<td>
						<div id="endTime2" style="width:160px;"></div>
					</td>
				</tr>
				
			</tbody>
		</table>
		<p class="orangeTxt2 edge10 txtCenter"></p>
	</div>
	
	<script type="text/javascript">
	var nm3kDateTimeTabBtn;
	function addDateTimeTabBtn(){
		nm3kDateTimeTabBtn = new Nm3kTabBtn({
		    renderTo:"test",
		    callBack:"msg",
		    tabs:["今天","昨天","本周","本月","自定义"]
		});
		nm3kDateTimeTabBtn.init();
	}
	function msg(num){
		var $tr = $("#" + nm3kDateTimeTabBtn.renderTo).parent().parent().prev();
		var stClockTime = " 00:00:00";
		var etClockTime = " 23:59:59";
		var sTimeStr,eTimeStr;
		switch(num){
			case 4:
				$tr.css({"display": ""})
			break;
			case 0:
				sTimeStr = getDateStr(0) + stClockTime;
				eTimeStr = getDateStr(0) + etClockTime;
				stTime.setValue(sTimeStr);	
				etTime.setValue(eTimeStr);
				//$tr.css({"display": "none"});
			break;
			case 1:
				sTimeStr = getDateStr(-1) + stClockTime;
				eTimeStr = getDateStr(-1) + etClockTime;
				stTime.setValue(sTimeStr);	
				etTime.setValue(eTimeStr);	
				//$tr.css({"display": "none"})
			break;
			case 2:
				sTimeStr = getWeekStartDate() + stClockTime;
				eTimeStr = getWeekEndDate() + etClockTime;
				stTime.setValue(sTimeStr);	
				etTime.setValue(eTimeStr);
				//$tr.css({"display": "none"})
			break;
			case 3:
				sTimeStr = getMonthStartDate() + stClockTime;
				eTimeStr = getMonthEndDate() + etClockTime;
				stTime.setValue(sTimeStr);	
				etTime.setValue(eTimeStr);	
				//$tr.css({"display": "none"})
			break;
		}
	}
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
	//获得本周的开端日期 
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
	}
	//获得本周的停止日期 
	function getWeekEndDate() { 
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
	}
	
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
	
	
	
	
	
	
	
	
	
	function alertShow(){
		var str = '你查询的开始时间：' + stTime.value.format("yyyy-mm-dd HH:MM:ss") + " ,结束时间：" + etTime.value.format("yyyy-mm-dd HH:MM:ss");
		$(".orangeTxt").text(str);
	}
	
	
	
	
	
	
	
	
	var stTime,etTime;
	function createTime(){
		var stClockTime = " 00:00:00";
		var etClockTime = " 23:59:59";
		var sTimeStr = getDateStr(0) + stClockTime;
		var eTimeStr = getDateStr(0) + etClockTime;
		
		
		var lastMonth = new Date(), current = new Date();
		lastMonth.setTime(lastMonth.getTime()-(7*24*60*60*1000));
		stTime = new Ext.ux.form.DateTimeField({
			width:160,
			value : sTimeStr,
			maxValue:current,
			editable: false,
			renderTo:"startTime",
			emptyText:'@Tip.pleaseEnterTime@',
		    blankText:'@Tip.pleaseEnterTime@',
		    listeners: {
		    	"select": function(){
		    		//修改结束时间的最小值限制
		    		etTime.setMinValue(stTime.getValue());
		    		//如果此时开始时间小于等于结束时间，则去掉所有的错误提示
					var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss"),
						endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
		    		if(startTime<=endTime){
		    			stTime.removeClass('x-form-invalid');
		    			etTime.removeClass('x-form-invalid');
		    		}
		    		$("#test a:last").click();
		    		
				}
			}
		});
		etTime = new Ext.ux.form.DateTimeField({
			width:160,
			value : eTimeStr,
			minValue:lastMonth,
			editable: false,
			renderTo:"endTime",
			emptyText:'@Tip.pleaseEnterTime@',
		    blankText:'@Tip.pleaseEnterTime@',
		    listeners: {
				"select": function(){
					//修改结束时间的最大值限制
					stTime.setMaxValue(etTime.getValue());
					//如果此时开始时间小于等于结束时间，则去掉所有的错误提示
					var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss"),
						endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
		    		if(startTime<=endTime){
		    			stTime.removeClass('x-form-invalid');
		    			etTime.removeClass('x-form-invalid');
		    		}
		    		$("#test a:last").click();
				}
			}
		});
	};//end createTime;
	var stTime2,etTime2;
	function createTime2(){
		var stClockTime = " 00:00:00";
		var etClockTime = " 23:59:59";
		var sTimeStr = getDateStr(0) + stClockTime;
		var eTimeStr = getDateStr(0) + etClockTime;
		
		
		var lastMonth = new Date(), current = new Date();
		lastMonth.setTime(lastMonth.getTime()-(7*24*60*60*1000));
		stTime2 = new Ext.ux.form.DateTimeField({
			width:160,
			value : sTimeStr,
			maxValue:current,
			editable: false,
			renderTo:"startTime2",
			emptyText:'@Tip.pleaseEnterTime@',
		    blankText:'@Tip.pleaseEnterTime@'
		   
		});
		etTime2 = new Ext.ux.form.DateTimeField({
			width:160,
			value : eTimeStr,
			minValue:lastMonth,
			editable: false,
			renderTo:"endTime2",
			emptyText:'@Tip.pleaseEnterTime@',
		    blankText:'@Tip.pleaseEnterTime@'
		    
		});
	};//end createTime;
	
	
	$(function(){
		createTime();
		createTime2();
		addDateTimeTabBtn();
		
		$("a.normalBtnWithDateTime").live("mouseenter", function(){
			var $me = $(this),
			w = $me.outerWidth(),
			h = $me.outerHeight(),
			l = $me.position().left,
			t = $me.position().top,
			$showDiv = $("#searchWithDateTimeWrap");
			if($showDiv.length < 1){
				var str = '<div id="searchWithDateTimeWrap" class="searchWithDateTimeWrap">';
				    str +=    '<dl>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">今天</a></dd>';
				    str +=        '<dt> | </dt>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">昨天</a></dd>';
				    str +=        '<dt> | </dt>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">本周</a></dd>';
				    str +=        '<dt> | </dt>';
				    str +=        '<dd><a href="javascript:;" class="yellowLink">本月</a></dd>';
				    str +=    '</dl>';
				    str += '</div>';
				$("body").append(str);
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
				var num = $me.parent().index("dd");
				var stClockTime = " 00:00:00";
				var etClockTime = " 23:59:59";
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
						sTimeStr = getWeekStartDate() + stClockTime;
						eTimeStr = getWeekEndDate() + etClockTime;
						stTime2.setValue(sTimeStr);	
						etTime2.setValue(eTimeStr);
					break;
					case 3:
						sTimeStr = getMonthStartDate() + stClockTime;
						eTimeStr = getMonthEndDate() + etClockTime;
						stTime2.setValue(sTimeStr);	
						etTime2.setValue(eTimeStr);	
					break;
				};//end switch;
				var str = '你查询的开始时间：' + stTime2.value.format("yyyy-mm-dd HH:MM:ss") + " ,结束时间：" + etTime2.value.format("yyyy-mm-dd HH:MM:ss");
				$(".orangeTxt2").text(str);
				$("#searchWithDateTimeWrap").css({display:"none"})
			}
		})
	})
	
	
	function searchFn(){
		var str = '你查询的开始时间：' + stTime2.value.format("yyyy-mm-dd HH:MM:ss") + " ,结束时间：" + etTime2.value.format("yyyy-mm-dd HH:MM:ss");
		$(".orangeTxt2").text(str);
		$("#searchWithDateTimeWrap").css({display:"none"})
	}
	
	
	</script>
</body>
</Zeta:HTML>
