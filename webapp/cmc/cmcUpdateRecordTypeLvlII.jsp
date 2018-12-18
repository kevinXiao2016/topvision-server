<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
    library ext
    library zeta
    module CMC
    import js.json2
</Zeta:Loader>

<style type="text/css">
table tr{
	height: 26px;
}
.labelTd{
	width: 130px;
	text-align:center;
}
.valueTd{
	width: 200px;
}
.columnSpan{
    display: inline-block;
    margin-right: 20px;
    vertical-align: middle;
    vertical-align: middle;
}
.columnSpan input, .columnSpan label{
    vertical-align: middle;
}
</style>

<script type="text/javascript">
var recordTypeJson = ${recordTypeJson};
var eventLevelStore = null;
var combo = null;

function getLevelNameById(id) {
    var str;
    switch(id){
    case 1:
        str = 'emergency';
        break;
    case 2:
        str = 'alert';
        break;
    case 3:
        str = 'critical';
        break;
    case 4:
        str = 'error';
        break;
    case 5:
        str = 'warning';
        break;
    case 6:
        str = 'notice';
        break;
    case 7:
        str = 'information';
        break;
    case 8:
        str = 'debug';
        break;
    case 28:
        str =  I18N.syslog.noneLevel;
        break;
    }
    return str;
}

function cancleClick() {
    top.closeWindow("updateRecordTypeLvlII");
}

$(function() {
	$("#evPriority").text(getLevelNameById(recordTypeJson.evPriority));
	if(recordTypeJson.local) {
		$('#cb_local').attr('checked', true);
	}
	if(recordTypeJson.traps) {
        $('#cb_traps').attr('checked', true);
    }
	if(recordTypeJson.syslog) {
        $('#cb_syslog').attr('checked', true);
    }
	if(recordTypeJson.localVolatile) {
        $('#cb_localVolatile').attr('checked', true);
    }
});

function update() {
	// 获取记录方式的勾选情况
	var local = $('#cb_local').is(':checked');
	var traps = $('#cb_traps').is(':checked');
	var syslog = $('#cb_syslog').is(':checked');
	var localVolatile = $('#cb_localVolatile').is(':checked');
	
	var data = {
		entityId: recordTypeJson.entityId,
		evPriority: recordTypeJson.evPriority,
		local: local,
		traps: traps,
		syslog: syslog,
		localVolatile: localVolatile
	}
	
	// 下发保存
	top.showWaitingDlg('@CMC.tip.waiting@', '@CMC.tip.savingConfig@');
	$.ajax({
        url: '/cmc/updateSyslogEvtLvlII.tv',
        type: 'POST',
        data: data,
        success: function() {
            //更新成功后
            top.closeWaitingDlg();
            top.afterSaveOrDelete({
                title: '@CMC.tip.tipMsg@',
                html: '<b class="orangeTxt">@syslog.updateSuccess@</b>'
            });
            cancleClick();
        }, error: function() {
        	top.closeWaitingDlg();
        	top.showErrorDlg();
            cancleClick();
        },
        cache: false,
        complete: function (XHR, TS) { 
        	XHR = null 
        }
    });
}

</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="150">@syslog.eventLevel@@COMMON.maohao@</td>
	                <td id="evPriority"></td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">@syslog.recordType@@COMMON.maohao@</td>
	                <td>
	                   <span class="columnSpan">
                           <input type="checkbox" id="cb_local" name="local"/>
                           <label for="cb_local">local</label>
                       </span>
                       <span class="columnSpan">
                           <input type="checkbox" id="cb_traps" name="traps"/>
                           <label for="cb_traps">traps</label>
                       </span>
                       <span class="columnSpan">
                           <input type="checkbox" id="cb_syslog" name="syslog"/>
                           <label for="cb_syslog">syslog</label>
                       </span>
                       <span class="columnSpan">
                           <input type="checkbox" id="cb_localVolatile" name="localVolatile"/>
                           <label for="cb_localVolatile">localVolatile</label>
                       </span>
	                   <!-- <input id="eventLevel" type="text" disabled="disabled" style="width: 150px;"/> -->
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a onclick="update()" id="loadData" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		         <li><a id="cancel" onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>

	
</body>
</Zeta:HTML>