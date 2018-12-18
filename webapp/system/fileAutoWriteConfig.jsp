<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library zeta
	library jquery
	module platform
    import js.tools.ipText
    import js.tools.numberInput
    css js/tools/css/numberInput
</Zeta:Loader>
<head>
<style type="text/css">
.vertical-middle{
	vertical-align: middle;
}
</style>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script>
	var autoWrite = '${autoWrite}';
	var saveBeforeWrite = '${saveBeforeWrite}';
	var autoWriteTime = '${autoWriteTime}';
	
	//save config
	function okClick() {
			//获得时间
			var getHour = $("#startHour").val();
			if(getHour){
				if(getHour < 10){
					getHour = "0" + getHour;
				}
			}else{
				$("#startHour").attr('tooltip','@sys.autoWriteTimeTip@');
				$("#startHour").focus();
				/* window.parent.showMessageDlg("@sys.tip@","@sys.autoWriteTimeTip@",null, function(){
					$("#startHour").focus();
				}); */
				return;
			}
			var getMinute = $("#startMin").val();
			if(getMinute){
				if(getMinute < 10){
					getMinute = "0" + getMinute;
				}
			}else{
				$("#startMin").attr('tooltip','@sys.autoWriteTimeTip@');
				$("#startMin").focus();
				//window.parent.showMessageDlg("@sys.tip@","@sys.autoWriteTimeTip@",null, function(){
					//$("#startMin").focus();
				//});
				return;
			}
			var getTime = getHour + ":" + getMinute;
			//获取星期选择
			var weekArray = new Array();
			$(":checkbox[name=week]:checked").each(function(i,item){
				weekArray.push(this.value);
			});
			if(weekArray.length == 0){
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@sys.selectWeekPlease@</b>'
	   			});
				return;
			}
			var weekString = weekArray.join(",");
			//拼接成规定的格式
			var writeTime = getTime + "_" + weekString;
			//提交保存
			//window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
			$.ajax({
				url : '/configBackup/saveAutoWriteConfig.tv',
				type : 'POST',
				data : {
					autoWrite :  $("input[name=autoWrite]:checked").val(),
					saveBeforeWrite :  $("input[name=saveBeforeWrite]:checked").val(),
					autoWriteTime : writeTime
				},
				success : function() {
					//window.parent.showMessageDlg("@sys.tip@","@sys.saved@");
					top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@sys.saved@</b>'
	   			});
					cancelClick();
				},
				error : function(json) {
					window.parent.showMessageDlg("@sys.tip@","@sys.saveFaild@");
				},
				cache : false
			});
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	$(function() {
		//构建时间控件
		new NumberInput("startHour", "startHourDiv", 0, 0, 23);
		new NumberInput("startMin", "startMinDiv", 0, 0, 59);
		
		if(autoWrite){
			$("input[name='autoWrite'][value='"+autoWrite+"']").attr("checked",true);
		}
		if(saveBeforeWrite){
			$("input[name='saveBeforeWrite'][value='"+saveBeforeWrite+"']").attr("checked",true);
		}
		//先解析出时间和星期选择
		var array = autoWriteTime.split("_");
		//初始化时间
		var time = array[0];
		$("#startHour").val(Number(time.split(":")[0]));
		$("#startMin").val(Number(time.split(":")[1]));
		//初始化星期选择
		var week = array[1].split(",");
		$('#selectAll').attr('checked',week.length == 7);
		$(":checkbox[name=week]").each(function(i,item){
			if($.inArray(this.value,week) == -1){
				$(this).attr("checked",false);
			}
		});
		
		$('#selectAll').click(function(){
			$(':checkbox[name=week]').attr("checked", this.checked);
		});
		
		$(':checkbox[name=week]').click(function(){
			//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
			var $tmp=$(':checkbox[name=week]');
			//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
			$('#selectAll').attr('checked',$tmp.length==$tmp.filter(':checked').length);
		});

	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@sys.autoWriteConfig@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <label for="autoWrite">@sys.fileAutoWrite@:</label>
	                </td>
	                <td colspan="2">
						<input type="radio" name="autoWrite" value="ON" />@engine.use@ &nbsp;&nbsp;
						<input type="radio" name="autoWrite" checked="checked" value="OFF" />@engine.stop@
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt" width="200">
	                    <label for="saveBeforeWrite">@sys.saveBeforeWrite@:</label>
	                </td>
	                <td>
						<input type="radio" name="saveBeforeWrite" value="ON" />@engine.use@ &nbsp;&nbsp;
						<input type="radio" name="saveBeforeWrite" checked="checked" value="OFF" />@engine.stop@
	                </td>
	            </tr>
	            <tr>
	            	<td class="rightBlueTxt" width="200">
	            		<label 	for="autoWriteTime">@sys.autoWriteTime@:</label>
	            	</td>
	            	<td class="vertical-middle">
						<div id="startHourDiv" style="display: inline-table;" class="timePeriod vertical-middle"></div>
						<span class="spanWidth30 timePeriod mR10 vertical-middle">@label.h@</span>
						<div id="startMinDiv" style="display: inline-table;" class="timePeriod vertical-middle"></div>
						<span class="spanWidth30 timePeriod mR10 vertical-middle">@label.m@</span>
	            	</td>
	            </tr>
	            <tr class="darkZebraTr">
	            	<td class="rightBlueTxt" ><input type="checkbox" id="selectAll" class="timePeriod weekCbx" checked="checked"/><span class="mR5 timePeriod">@sys.selectAll@</span></td>
	            	<td colspan="2">
						<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="1"/><span class="mR5 timePeriod">@sys.Sunday@</span>
	            		<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="2"/><span class="mR5 timePeriod">@sys.Monday@</span>
						<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="3"/><span class="mR5 timePeriod">@sys.Tuesday@</span>
						<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="4"/><span class="mR5 timePeriod">@sys.Wednesday@</span>
						<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="5"/><span class="mR5 timePeriod">@sys.Thursday@</span>
						<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="6"/><span class="mR5 timePeriod">@sys.Friday@</span>
						<input type="checkbox" name="week" class="timePeriod weekCbx" checked="checked" value="7"/><span class="mR5 timePeriod">@sys.Saturday@</span>
	            	</td>
	            </tr>
	        </tbody>
	    </table>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT30 noWidthCenter">
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="okClick()">
		                <span>
		                    <i class="miniIcoData">
		                    </i>
		                   	@sys.save@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
		                <span><i class="miniIcoForbid"></i>
		                    @BUTTON.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>	
</body>
</Zeta:HTML>