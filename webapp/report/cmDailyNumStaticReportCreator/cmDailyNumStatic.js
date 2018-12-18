var stField;
var etField;
var splitButtonTop;

function checkDisable(obj){
	var isDisable = $(obj[0]).hasClass("disabledAlink");
	return !isDisable;
}

var cmcategory = {
	'online':'@report.onLineCmNum@',
	'offline':'@report.offLineCmNum@',
	'otherStatus':'@report.otherStatusCmNum@',
	'allStatus':'@report.allStatusCmNum@'
}

$(document).ready(function(){
	splitButtonTop = new Ext.SplitButton({
		renderTo: 'exportDiv',
	   	//text: '@tip.exportGraph@',
	   	text : "@BUTTON.export@",
	   	iconCls : "bmenu_exportWithSub",
	   	disabled:true,
	   	handler : function(){this.showMenu()},
	   	menu: new Ext.menu.Menu({
	        items: [
                {text: '@tip.png@', handler: exportPngClick},
		        {text: '@tip.jpeg@', handler: exportJpegClick},
		        {text: '@tip.pdf@', handler: exportPdfClick}
	        ]
	   	})
	});
	
	splitButtonBottom = new Ext.SplitButton({
		renderTo: 'exportDiv_',
	   	//text: '@tip.exportGraph@',
		text : "@BUTTON.export@",
	   	iconCls : "bmenu_exportWithSub",
	   	disabled:true,
		handler : function(){this.showMenu()},
	   	menu: new Ext.menu.Menu({
	        items: [
		        {text: '@tip.jpeg@', handler: exportJpegClick},
		        {text: '@tip.png@', handler: exportPngClick},
		        {text: '@tip.pdf@', handler: exportPdfClick}
	        ]
	   	})
	});
	
	stField = new Ext.form.DateField({
	    emptyText:'@report.plsFullfillDate@',
	    blankText:'@report.plsFullfillDate@',
		editable: false,
		format: 'Y-m-d',
		renderTo: 'stratTime',
		width: 170
	});
	
	etField = new Ext.form.DateField({
	    emptyText:'@report.plsFullfillDate@',
	    blankText:'@report.plsFullfillDate@',
		editable: false,
		format: 'Y-m-d',
		renderTo: 'endTime',
		width: 170
	});
	nm3kPickData({
	    startTime : stField,
	    endTime : etField,
	    searchFunction : query,
	    withoutTime : true
	})
	
	//构建查询下拉菜单
	for(var key in rangeList){
		$('<select class="rangeSelect normalSel" id="'+key+'"></select>').hide().appendTo($("#rangeTd"));
		$.each(rangeList[key], function(index, range){
			$('<option value="'+range.id+'">'+range.name+'</option>').appendTo($("#"+key));
		})
	}
});

function optionClick(){
	$("#queryDiv").toggle();
	$("html,body").animate({scrollTop:"0px"},200);
}

function loadRangeDevice(){
	var value = $("#rangeSelect").val();
	if(value!="all"){
		$('#rangeTd').show();
		$('select.rangeSelect').hide();
		$('select#'+value).show();
	}else{
		$('#rangeTd').hide();
	}
}

function query(){
	//获取查询参数
	var startTime = stField.getValue();
	var endTime = etField.getValue();
	var range = $("#rangeSelect").val();
	var rangeDetail = "";
	if(range!="all"){
		rangeDetail = $('#'+range).val();
	}
	
	if(startTime==""){
		/*$("#alarmStartTime").show().effect("shake",{ times:2 }, 300);*/
		stField.focus();
		return false;
	}
	
	if(endTime==""){
		/*$("#alarmEndTime").show().effect("shake",{ times:2 }, 300);*/
		etField.focus();
		return false;
	}
	
	if(startTime>endTime){
        top.afterSaveOrDelete({
            title: '@report.tip@',
                html: '<b class="orangeTxt">@report.conflictPeriod@</b>'
        });
		return false;
	}
	
	if(range!="all" && (rangeDetail=="" || rangeDetail==null)){
		window.top.showMessageDlg("@COMMON.tip@", '@tip.pleaseSelectOne@');
		return false;
	}
	
	optionClick();
	fillQueryConditionDiv(startTime, endTime, range, rangeDetail);
	//查询数据
	generateCmDailyMaxNumGraph(formatDate(startTime)+" 00:00:00", formatDate(endTime)+" 23:59:59", range, rangeDetail);
}

function fillQueryConditionDiv(startTime, endTime, range, rangeDetail){
	$("#staticPeroid").text(formatDate(startTime)+" @report.timeTo@ "+formatDate(endTime));
	var specificRange = "";
	if(rangeDetail!=""){
		var rangeDetailName = $("#"+range).find("option:selected").text();
		specificRange += "(" + rangeDetailName + ")";
	}
	$("#statisRange").text($("#rangeSelect").find("option:selected").text()+ specificRange);
}

function generateCmDailyMaxNumGraph(startTime, endTime, range, rangeDetail){
	window.top.showWaitingDlg("@COMMON.wait@", "@report.stasticing@", 'ext-mb-waiting');
	$.getJSON('/cmc/report/loadCmDailyNumStaticGraphData.tv?stTime='+startTime+'&etTime='+endTime+'&range='+range+'&rangeDetail='+rangeDetail, 
			function(json) {
		//更新时间
		var date = new Date();
		$("#queryTime").text(formatDate(date));
		
		var seriesOptions = [];
		var i = 0;
		for(var key in json){
			//根据key(index)获取对应的下拉列表名称   				
			seriesOptions[i++] = {
   					name: cmcategory[key],
   					key : key,
   					data: json[key],
	   				dataGrouping: {
						enabled: false
					}
   			};
		}
		
		Highcharts.setOptions({
			global: {
				useUTC: false
			},
			lang:{
				contextButtonTitle: (lang='zh_CN')?'@graph.contextMenu@':'Chart context menu',
				printChart: (lang='zh_CN')?'@report.printGraph@':'Print chart',
				downloadJPEG: (lang='zh_CN')?'@graph.downloadJpeg@':'Download JPEG image',
				downloadPNG: (lang='zh_CN')?'@graph.downloadPng@':'Download PNG image',
				downloadPDF: (lang='zh_CN')?'@graph.downloadPdf@':'Download PDF document',
				downloadSVG: (lang='zh_CN')?'@graph.downloadSvg@':'Download SVG vector image'
			}
		});
		
		chart = new Highcharts.Chart({
			chart: {  
                renderTo: 'highStockDiv',  
                type: 'spline',
                zoomType: 'x',
                resetZoomButton: {
                    theme: {
                        fill: 'white',
                        stroke: 'silver',
                        r: 0,
                        states: {
                            hover: {
                                fill: '#41739D',
                                style: {
                                    color: 'white'
                                }
                            }
                        }
                    }
                }
            }, 
            credits:{enabled: false},
            title: {
                text: '@report.cmDailyNumStaticReport@',
                x: -20 //center
            },
            subtitle:{
            	text: "(" + startTime+" @report.timeTo@ "+endTime + ")",
                x: -20 //center
            },
            xAxis: {
		    	type: 'datetime',
		    	//x轴时间的格式化
                dateTimeLabelFormats: {
                    second: '%Y-%m-%d<br/>%H:%M:%S',
                    minute: '%Y-%m-%d<br/>%H:%M',
                    hour: '%Y-%m-%d<br/>%H:%M',
                    day: '%Y<br/>%m-%d',
                    week: '%Y<br/>%m-%d',
                    month: '%Y-%m',
                    year: '%Y'
                }
		    },
		    yAxis: {
		    	title: {
                    text: '@report.cmNum@'
                },
		    	allowDecimals:false,
		    	min:0
		    },
		    exporting:{  
                filename:'@report.cmDailyNumStaticReport@-'+formatDay(new Date())
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            },
            tooltip:{
            	shared:true,
            	dateTimeLabelFormats:{
            		second: '%Y-%m-%d %H:%M:%S',
                    minute: '%Y-%m-%d %H:%M',
                    hour: '%Y-%m-%d %H:%M',
                    day: '%Y-%m-%d',
                    week: '%Y-%m-%d',
                    month: '%Y-%m-%d',
                    year: '%Y-%m-%d'
            	}
            },
            series: seriesOptions
        });
		window.parent.closeWaitingDlg();
		
		//使导出按钮盒打印按钮可用
		splitButtonTop.setDisabled(false);
		splitButtonBottom.setDisabled(false);
		$("#printLink").removeClass("disabledAlink");
	});
}

//打印
function onPrintClick() {
	if(chart!=null){
		chart.print();
	}
}
//导出图片
function exportJpegClick(){
	exportClick('image/jpeg');
}
function exportPngClick(){
	exportClick('image/png');
}
function exportPdfClick(){
	exportClick('application/pdf');
}

function exportClick(type) {
	if(chart!=null){
		var options = {};
		options.type=type;
		chart.exportChart(options);
	}
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
		if(m < 10){ m = "0"+ m };
		var d = time.getDate();
		if(d < 10){ d = "0"+d };
		var h = (time.getHours()<10) ? ('0' + time.getHours()) : time.getHours();  
		var i = (time.getMinutes()<10) ? ('0' + time.getMinutes()) : time.getMinutes();   
		var s = (time.getSeconds()<10) ? ('0' + time.getSeconds()) : time.getSeconds();   

		if(h>0 || i>0 || s>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;  

		return y + '-' + m + '-' + d;   
	}
	return '';
}

function formatDay(time){
	if(typeof time == 'string') time = parseDate(time);   
	if(time instanceof Date){   
		var y = time.getFullYear();  
		var m = time.getMonth() + 1;  
		var d = time.getDate();  

		return y + '-' + m + '-' + d;   
	}
	return '';
}

function closeLoading(){
	$("#loading").fadeOut('slow');
}

function queryYesterday(){
	//var today = new Date();
	//var todayStart = today.format("Y") +"-" + today.format("m") + "-" + today.format("d");
	//etField.setValue(todayStart); 
	var today = new Date(),
		_24hourAgo = new Date(today - 24 * 3600 * 1000);
	var yesterdayStart = _24hourAgo.format("Y") +"-" +_24hourAgo.format("m") + "-" + _24hourAgo.format("d");
	stField.setValue(yesterdayStart);
	etField.setValue(yesterdayStart);
}