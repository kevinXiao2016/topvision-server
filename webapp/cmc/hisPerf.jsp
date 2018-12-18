<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<Zeta:Loader>
    library Ext
    import js.ext.ext-basex
    library Zeta
    library jquery
    library highchart 
    module CMC
</Zeta:Loader>
</head>
<body class="whiteToBlack">
    <div id="historyPerf-panel"></div>

	<div id="alert-div">
	    <div id="extGridContainer">
	        <div>
	            <div>
	                <div id="show2" align="center" style="border:2px;border-color:black">
	                    <!--content-->
	                    <div id="container2" style="WIDTH: 800px; HEIGHT: 400px;">
	                        <div class="highcharts-container" id="highcharts-0"
	                             style="position: relative; overflow: hidden; width: 800px; height: 400px; text-align: left; font-family: &quot;Lucida Grande&quot;,&quot;Lucida Sans Unicode&quot;,Verdana,Arial,Helvetica,sans-serif; font-size: 12px; left: 0px; top: 0px;">
	
	                        </div>
	                    </div>
	                    <div style="TEXT-ALIGN: center;WIDTH: 800px; HEIGHT: 100px;color:darkgreen;">
	                        <table style="MARGIN: 0px auto; WIDTH: 580px">
	                            <tbody id="seriesDetail">
	
	                            </tbody>
	                        </table>
	                    </div>
	                    <!--content end--></div>
	            </div>
	        </div>
	    </div>
	</div>
<script type="text/javascript">
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});

var chart;
var chartParam = ${chartParam};
var viewerParam = ${viewerParam};

Ext.onReady(function (){
    var WIN_WIDTH = window.offsetWidth;
    var WIN_HEIGHT = window.offsetHeight;
  //全局配置
    new Ext.Panel({
        renderTo : "historyPerf-panel",
        height : WIN_HEIGHT,
        width : WIN_WIDTH,
        contentEl : "alert-div",
        tbar : [
                {xtype:'label',text:"@PERF.mo.queryTime@:"},
                createTimeTypeSelect(),
                {xtype:'label',text:"@PERF.mo.startTime@:", cls: "tagget-hide"},
                createStartTimeInput(),
                {xtype:'label',text:"@PERF.mo.endLessTime@:", cls: "tagget-hide"},
                createEndTimeInput(),
                {xtype: 'button',text:"@COMMON.query@", width: 20,iconCls:"bmenu_find", handler: queryHisPerf}
        ]
    });
    var startTime;
    var endTime;
    $("#selectTime").val(viewerParam.timeType);
    if (viewerParam.timeType == "Custom") {
        $(".tagget-hide").show();
        startTime = viewerParam.st;
        endTime = viewerParam.et;
    } else {
        $(".tagget-hide").hide();
        startTime = GetDateStr(-1);
        endTime = GetDateStr(0);
    }
    $('#startTime').val(startTime);
    $('#endTime').val(endTime);
    changeChartXAxis(chartParam);
    chart = new Highcharts.Chart(chartParam);
    $.ajax({
        url: '/cmcperf/seriesRead.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:viewerParam,
        success: function(series) {
            $.each(series, function(i, n) {
                chart.addSeries(n, true);
                var html = "<tr style='COLOR: " + n.color + "'> <td><strong>" + n.name + "</strong></td> <td>" + "@NETWORK/ap.chart.max@" + n.max + n.unit + "</td> <td>" + "@NETWORK/ap.chart.avg@" + n.avg + n.unit + "</td> <td>" + "@NETWORK/ap.chart.min@" + n.min + n.unit + "</td> <td>" + "@NETWORK/ap.chart.cur@" + n.cur + n.unit + "</td> </tr>"
                $("#seriesDetail").append(html);
            });
        }
    });
    $("#selectTime").change(function () {
        var selectTime = $(this).val();
        if (selectTime == "Custom") {
            $(".tagget-hide").show();
        } else {
            $(".tagget-hide").hide();
        }
    });
});
function createTimeTypeSelect(){
    var str = '<select id="selectTime">'+
        '<option value="Today" selected="">@PERF.mo.today@</option>' +
        '<option value="Yesterday">@PERF.mo.yesterday@</option>' +
        '<option value="The last two days">@PERF.mo.nearTwoDay@</option>'+
        '<option value="The last seven days">@PERF.mo.near7Day@</option>'+
        '<option value="This month">@PERF.mo.nearMonth@</option>'+
        '<option value="Last month">@PERF.mo.beforeMonth@</option>'+
        '<option value="The last thirty days">@PERF.mo.last30Day@</option>'+
        '<option value="The last three months">@PERF.mo.last3Month@</option>'+
        '<option value="The last twelve months">@PERF.mo.last12Month@</option>'+
        '<option value="This year">@PERF.mo.thisYear@</option>'+
        '<option value="Custom">@PERF.mo.custom@</option>'+
        '</select>';
    return str;
}
function createStartTimeInput(){
    var str = '<input name="startTime"' +
            'onfocus="WdatePicker({maxDate: \'#F{$dp.$D(\\\'endTime\\\')}\', isShowClear: false, readOnly: true, dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"' +
            'class="Wdate tagget-hide"'+
            'id="startTime"' +
            'style="WIDTH: 220px"/>';
    return str;
}
function createEndTimeInput(){
    var str = '<input name="endTime"'+
            'onfocus="WdatePicker({minDate: \'#F{$dp.$D(\\\'startTime\\\')}\', isShowClear: false, readOnly: true, dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"'+
            'class="Wdate tagget-hide"'+
            'id="endTime"'+
            'style="WIDTH: 220px"/>';
    return str;
}
function queryHisPerf(){
    viewerParam.timeType = $("#selectTime").val();
    if (viewerParam.timeType == "Custom") {
        viewerParam.st = $("#startTime").val();
        viewerParam.et = $("#endTime").val();
    } else {
        delete viewerParam.st;
        delete viewerParam.et;
    }
    window.location.href = "/cmcperf/showCmcHisPerf.tv?entityId=" + viewerParam.entityId + "&cmcId=" + viewerParam.cmcId + "&timeType=" + viewerParam.timeType
            + "&perfType=" + viewerParam.perfType + "&st=" + viewerParam.st + "&et=" + viewerParam.et + "&index=" + viewerParam.index;
}

/**
 * 修改X轴时间显示，将日期显示到0点时刻
 */
function changeChartXAxis(o){
    if(o){
        var xAxis = [{
            max: o.etLong,
            min: o.stLong,
            minRange: 3600000,
            type: "datetime",
            labels: {
                formatter: function(){
                    var utc_time = this.value;
                    var date = new Date(utc_time);
                    var hour = date.getHours();
                    var minute = date.getMinutes();
                    var second = date.getSeconds();
                    if(hour == 0 && minute == 0 && second == 0){
                        return Highcharts.dateFormat("%m-%d", utc_time);                      

                    }else{
                        return Highcharts.dateFormat("%H:%M", utc_time);
                    }
                }
            }
            
        }]
        o.xAxis = xAxis;
    }
}
function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate() + AddDayCount);
    var r = [];
    r.push(format(dd.getFullYear(), '0000'));
    r.push('-');
    r.push(format(dd.getMonth() + 1, '00'));
    r.push('-');
    r.push(format(dd.getDate(), '00'));
    r.push(' ');
    r.push(format(dd.getHours(), '00'));
    r.push(':');
    r.push(format(dd.getMinutes(), '00'));
    r.push(':');
    r.push(format(dd.getSeconds(), '00'));
    return r.join('');
}

function format(number, pattern) {
    var str = number.toString();
    var strInt;
    var strFloat;
    var formatInt;
    var formatFloat;
    if (/\./g.test(pattern)) {
        formatInt = pattern.split('.')[0];
        formatFloat = pattern.split('.')[1];
    } else {
        formatInt = pattern;
        formatFloat = null;
    }
    if (/\./g.test(str)) {
        if (formatFloat != null) {
            var tempFloat = Math.round(parseFloat('0.' + str.split('.')[1]) * Math.pow(10, formatFloat.length)) / Math.pow(10, formatFloat.length);
            strInt = (Math.floor(number) + Math.floor(tempFloat)).toString();
            strFloat = /\./g.test(tempFloat.toString()) ? tempFloat.toString().split('.')[1] : '0';
        } else {
            strInt = Math.round(number).toString();
            strFloat = '0';
        }
    } else {
        strInt = str;
        strFloat = '0';
    }
    if (formatInt != null) {
        var outputInt = '';
        var zero = formatInt.match(/0*$/)[0].length;
        var comma = null;
        if (/,/g.test(formatInt)) {
            comma = formatInt.match(/,[^,]*/)[0].length - 1;
        }
        var newReg = new RegExp('(\\d{' + comma + '})', 'g');

        if (strInt.length < zero) {
            outputInt = new Array(zero + 1).join('0') + strInt;
            outputInt = outputInt.substr(outputInt.length - zero, zero)
        } else {
            outputInt = strInt;
        }

        var outputInt = outputInt.substr(0, outputInt.length % comma) + outputInt.substring(outputInt.length % comma).replace(newReg, (comma != null ? ',' : '') + '$1')
        outputInt = outputInt.replace(/^,/, '');
        strInt = outputInt;
    }
    if (formatFloat != null) {
        var outputFloat = '';
        var zero = formatFloat.match(/^0*/)[0].length;

        if (strFloat.length < zero) {
            outputFloat = strFloat + new Array(zero + 1).join('0');
                //outputFloat        = outputFloat.substring(0,formatFloat.length);
            var outputFloat1 = outputFloat.substring(0, zero);
            var outputFloat2 = outputFloat.substring(zero, formatFloat.length);
            outputFloat = outputFloat1 + outputFloat2.replace(/0*$/, '');
        } else {
            outputFloat = strFloat.substring(0, formatFloat.length);
        }
        strFloat = outputFloat;
    } else {
        if (pattern != '' || (pattern == '' && strFloat == '0')) {
            strFloat = '';
        }
    }
    return strInt + (strFloat == '' ? '' : '.' + strFloat);
}
</script>
</body>
</Zeta:HTML>
