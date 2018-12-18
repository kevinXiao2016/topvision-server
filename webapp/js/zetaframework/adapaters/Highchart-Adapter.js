/***
*   对常见的一些 Highchart配置进行默认处理,比如 creadits。导入导出，国际化等
**/ 
var HIGHCHART_LANG = {
		'zh_CN' : {
			loading: '加载中...',
			months: ['1月', '2月', '3月', '4月', '5月', '6月', '7月','8月', '9月', '10月', '11月', '12月'],
			//shortMonths: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
			shortMonths: ['1月', '2月', '3月', '4月', '5月', '6月', '7月','8月', '9月', '10月', '11月', '12月'],
			weekdays: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
			decimalPoint: '.',
			resetZoom: '重置缩放',
			resetZoomTitle: '重置缩放等级为 1:1',
			thousandsSep: ','
		},
		'en_US' : {
			loading: 'Loading...',
			months: ['January', 'February', 'March', 'April', 'May', 'June', 'July',
					'August', 'September', 'October', 'November', 'December'],
			shortMonths: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
			weekdays: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
			decimalPoint: '.',
			resetZoom: 'Reset zoom',
			resetZoomTitle: 'Reset zoom level 1:1',
			thousandsSep: ','
		}
}

Highcharts.setOptions({
	global: {
        useUTC: false
    },
    lang : HIGHCHART_LANG[lang]
});

//TODO javascript filter做起来的时候再对这里做国际化
if("zh_CN" == lang){
	Highcharts.setOptions({
		xAxis : {
	    	dateTimeLabelFormats : {
	    		second: '%H:%M:%S',
	    		minute: '%H:%M',
	    		hour: '%H:%M',
	    		day: '%b%e日',
	    		week: '%b%e日',
	    		month: '%y \'%b',
	    		year: '%Y'
	    	}
	    }
	});
}	
