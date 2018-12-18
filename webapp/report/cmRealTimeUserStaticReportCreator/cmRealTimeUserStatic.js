function exportExcelClick(){
	window.location.href="/cmc/report/exportCmRealTimeUserStaticReportToExcel.tv";
}
function queryForData(){
	window.location.href="/cmc/report/showCmRealTimeUserStatic.tv";
}
//打印预览
function onPrintviewClick() {
	showPrintWnd(Zeta$('pageview'));
}
//打印
function onPrintClick() {
	var wnd = window.open();
	showPrintWnd(Zeta$('pageview'), wnd.document);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}
	//window.print();
}

/**
 * 格式化时间
 * @param time
 * @returns {String}
 */
function formatDate(time){
	if(typeof time == 'string') time = parseDate(time);   
	if(time instanceof Date){   
		var y = time.getFullYear();  
		var m = time.getMonth() + 1;  
		var d = time.getDate();  
		var h = (time.getHours()<10) ? ('0' + time.getHours()) : time.getHours();  
		var i = (time.getMinutes()<10) ? ('0' + time.getMinutes()) : time.getMinutes();   
		var s = (time.getSeconds()<10) ? ('0' + time.getSeconds()) : time.getSeconds();   

		if(h>0 || i>0 || s>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;  

		return y + '-' + m + '-' + d;   
	}
	return '';
}