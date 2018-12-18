/****************************
*******	快捷查询  ***********
*****************************/
function queryLast24H(){
	var today = new Date();
	etField.setValue(today);
	var _24hourAgo = new Date(today - 24 * 3600 * 1000);
	stField.setValue(_24hourAgo);
}

function queryYesterday(){
	var today = new Date();
	var todayStart = today.format("Y") +"-" + today.format("m") + "-" + today.format("d");
	etField.setValue(todayStart); 
	var _24hourAgo = new Date(today - 24 * 3600 * 1000);
	var yesterdayStart = _24hourAgo.format("Y") +"-" +_24hourAgo.format("m") + "-" + _24hourAgo.format("d");
	stField.setValue(yesterdayStart);
}

function queryThisWeek(weekStartDay){
	var today = new Date();
	//周几
	var dayNum = today.format('w');
	var week = new Date(today - (dayNum-weekStartDay) * 24 * 3600 * 1000);
	var weekStart = week.format("Y") +"-" + week.format("m") + "-" + week.format("d");
	stField.setValue(weekStart);
	etField.setValue(today);
}

function queryThisMonth(){
	var today = new Date();
	var dayNum = today.format('j');
	var month = new Date(today - (dayNum-1) * 24 * 3600 * 1000);
	var monthStart = month.format("Y") +"-" + month.format("m") + "-" + month.format("d");
	stField.setValue(monthStart);
	etField.setValue(today);
}

function queryLastMonth(){
	var today = new Date();
	var dayNum = today.format('j');
	var month = new Date(today - (dayNum-1) * 24 * 3600 * 1000);
	var monthStart = month.format("Y") +"-" + month.format("m") + "-" + month.format("d");
	var dt = Date.parseDate(monthStart, "Y-m-d");
    var lastMonthEnd = new Date(dt - 1000);
	etField.setValue(lastMonthEnd);
	var lastMonthDay = lastMonthEnd.format('j');
	var lastMonth = new Date(lastMonthEnd - (lastMonthDay-1) * 24 * 3600 * 1000);
	var lastMonthStart = lastMonth.format("Y") +"-" + lastMonth.format("m") + "-" + lastMonth.format("d");
	stField.setValue(lastMonthStart);
}
/****************************
*******	快捷查询  ***********
*****************************/
function showPrintWnd(obj, doc) {
    if(doc == null) {
        var wnd = window.open();
        doc = wnd.document;
    } else {
        doc.open();
    }
    var formatHtml = obj.innerHTML.replace(/href="#"/g, '');
    formatHtml = formatHtml.replace(/onclick="([^"]*)"/g, '');
    doc.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">');
    doc.write('<html>');
    doc.write('<head>');
    doc.write('<title><s:property value="title"/></title>');
    doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
    doc.write('<link rel="stylesheet" type="text/css" href="/css/report.css"/>');
    Zeta$NoHeaderAndFooter(doc);
    doc.write('</head>');
    doc.write('<body style="margin:50px auto;"><div class="report-content-div">');
    doc.write(formatHtml);
    doc.write('</div></body>');
    doc.write('</html>');
    doc.close();
}