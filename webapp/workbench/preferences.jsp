<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js.tools.ipText
    module workbench
</Zeta:Loader>
<style type="text/css">
.vertical-middle{
	vertical-align: middle;
}
.vertical-middle input, .vertical-middle select{
	vertical-align: middle;
}
.vertical-middle input[type="radio"]{
	margin-left: 10px;
}
.zebraTableRows td{
	height: 25px;
}
.x-form-field-wrap .x-form-trigger{ height:23px;}
</style>
<script type="text/javascript" src="../js/ext/ux/SliderTip.js"></script>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
var tabbed = <s:property value="userContext.tabbed"/>;
var startPage = '<s:property value="userContext.startPage"/>';
var pageSize = <s:property value="userContext.pageSize"/>;
var macDisplayStyle = '<s:property value="userContext.macDisplayStyle"/>';
var weekStartDay = '<s:property value="userContext.weekStartDay"/>';
var hostAddr = '<%=request.getRemoteAddr()%>';
var bindIpValue = '<s:property value="userContext.user.bindIp"/>';
var tipShowTime = '<s:property value="userContext.tipShowTime"/>';
var topoTreeDisplayDevice = '<s:property value="userContext.topoTreeDisplayDevice"/>';
var topoTreeClickToOpen = '<s:property value="userContext.topoTreeClickToOpen"/>';

	function cancelClick() {
		window.top.ZetaCallback = {
			type : null
		};
		window.top.hideModalDlg();
	}
	
	function defaultFailureCallback(response) {
		window.top.showErrorDlg();
	}
	
	function defaultSuccessCallback(response) {
		var displayField = $("#entityDisplayField").val();
		$.ajax({
			url:'/userPreference/saveUserPreference.tv',cache:false,async:false,
			data:{
				key : "user.displayField", 
				value : displayField
			}
		});
		//告警框不关闭，修改间隔时间后再发出告警声音时间;
		top.oAlertSoundInterval.soudInterval = parseInt($("#soundTimeInterval").val(), 10) * 60000;
		
		//修改全局的每周开始时间
		window.top.firstDayOfWeek = parseInt($('#weekStartDaySel').val());
		//window.top.closeManyTabInformed = window.top.alarmWhenCloseManyTab = Zeta$('alarmWhenCloseManyTab').checked;
		window.top.switchWhenNewTab = Zeta$('switchWhenNewTab').checked;
		//window.top.notifyWhenMsg = Zeta$('notifyWhenMsg').checked;
		window.top.tabMaxLimit = Zeta$('tabMaxLimit').checked;
		window.top.entityDisplayField = displayField;
		/* var el = Zeta$('styleName');
		var style = el.options[el.selectedIndex].value; */
		top.afterSaveOrDelete({
	      title: '@COMMON.tip@',
	      html: '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
	    });
		/*if (cssStyleName != style) {
			window.top.location = "../mainFrame.tv?standalone="
					+ window.top.standalone;
		 } else {
			cancelClick();
		} */
		cancelClick();
	}
	
	function okClick() {
		if(!checkPageSize()){
			return;
		}
		var v = combo.getValue();
		if(v > 9999){
			Zeta$('pageSizeArea').focus();
			return;
		}
		// IP地址有效性验证
		var ipInput = getIpValue('bindIpInput');
		if (ipInput !='' && !ipIsFilled("bindIpInput")) {
			top.afterSaveOrDelete({
		      title: '@COMMON.tip@',
		      html: '<b class="orangeTxt">@WorkBench.ipInputTip@</b>'
		    });
			//window.top.showMessageDlg(I18N.COMMON.tip,I18N.WorkBench.ipInputTip);
			return;
		}
		if(ipInput != null && ipInput != ""){
			if(checkBindIp(ipInput)){
				window.top.showMessageDlg("@COMMON.tip@", ipInput + " "+ "@WorBench.alreadyBind@", null , function(){
					ipFocus('bindIpInput', 1);
				});
				return false;
			}
		}
		var time = $("#soundTimeInterval").val();
		var reg = /\d{1,2}/;
		var flag = false;
		if(reg.test(time)){
			time = parseInt(time, 10);
			if(time >= 1 && time <= 60){
				flag = true;
			}
		}
		if(!flag){
			$("#soundTimeInterval").focus();
			return;
		}else{
			Zeta$('hid14').value = $("#soundTimeInterval").val();
		}
		
		var ok = Zeta$('okBt');
		ok.disabled = true;
		$("#okBt").mouseout();
/* 		Zeta$('hid1').value = Zeta$('tabbed').checked;
		Zeta$('hid2').value = Zeta$('alarmWhenCloseManyTab').checked; */
		Zeta$('hid3').value = Zeta$('switchWhenNewTab').checked;
		//Zeta$('hid4').value = Zeta$('notifyWhenMsg').checked;
		Zeta$('hid5').value = Zeta$('tabMaxLimit').checked;
		//Zeta$('hid6').value = Zeta$('inputTip').checked;
		Zeta$('hid7').value = combo.getValue();
		Zeta$('hid8').value = getIpValue('bindIpInput');
		
		//设置MAC地址格式化的值
		var letterCase = "";
		if($('#upperCase').attr("checked")){
		    letterCase="U";
		}else{
		    letterCase="D";
		}
		$('#hid9').val($('#macDisplayStyleSelect').val()+"#"+letterCase);
		
		//设置提示框显示持续时间
		$('#hid10').val($('#tipShowTime').val());
		top.tipShowTime = $('#tipShowTime').val();
		
		//设置每周起始日
		$('#hid11').val($('#weekStartDaySel').val());
		//设置自动刷新时间
		var autoRefreshTime = $('#autoRefreshTime').val();
		var topNumber = $('#topNumber').val();
		if(checkAutoRefreshTime(autoRefreshTime)){
			$('#hid12').val(autoRefreshTime);
		}else{
			$('#autoRefreshTime').focus();
			ok.disabled = false;
			return false;
		}
		if(checkTopNumber(topNumber)){
			$('#hid13').val(topNumber);
		}else{
			$('#topNumber').focus();
			ok.disabled = false;
			return false;
		}
		//设置拓扑导航树是否显示设备
		$('#hid15').val($('#topoTreeDisplayDevice').val());
		//设置拓扑导航树点击设备跳转位置
		$('#hid16').val($('#topoTreeClickToOpen').val());
		
		$.ajax({
			url : 'saveGeneralPreferences.tv',
			type : 'POST',
			data : jQuery(preferencesForm).serialize(),
			success : defaultSuccessCallback,
			error : function() {
				ok.disabled = false;
				defaultFailureCallback();
			},
			dataType : 'plain',
			cache : false
		});
	}
	function enableTabbed(box) {
		if (box.checked) {
			//Zeta$('alarmWhenCloseManyTab').disabled = false;
			Zeta$('switchWhenNewTab').disabled = false;
			Zeta$('tabMaxLimit').disabled = false;
		} else {
			//Zeta$('alarmWhenCloseManyTab').disabled = true;
			Zeta$('switchWhenNewTab').disabled = true;
			Zeta$('tabMaxLimit').disabled = true;
		}
	}

	function bindClick() {
		setIpValue('bindIpInput',hostAddr);
	}
	
	function checkBindIp(ip){
		var alreadyBind = false;
		$.ajax({
			url : 'hasBindIp.tv',
			type : 'post',
			data : {
				bindIp : ip
			},
			success : function(json) {
				alreadyBind = json.exists;
			},
			error : defaultFailureCallback,
			dataType : 'json',
			cache : false,
			async : false
		});
		return alreadyBind;
	}

	function unbindClick() {
		setIpValue('bindIpInput','');
	}
	
	function doOnload() {
		/* var el = Zeta$('styleName');
		var options = el.options;
		for ( var i = 0; i < options.length; i++) {
			if (options[i].value == cssStyleName) {
				el.selectedIndex = i;
				break;
			}
		} */
	}
	 
	//check pageSize input
	function checkPageSize() {
		var value = combo.getValue();
		var reg = /^[1-9]\d*$/;		
		if (reg.exec(value)) {
				return true;
		} else {
			Zeta$('pageSizeArea').focus();
			return false;
		}
	}
	//检查自动刷新时间 
	function checkAutoRefreshTime(value){
		var g = /^[1-9]\d*$/;
	    if(g.test(value) && value >= 3 && value <= 86400){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	function checkTopNumber(value){
		var reg = /^[1-9]\d*$/;		
		if(reg.exec(value) && value >= 1 && value <= 100){
			return true;
		}else{
			return false;
		}
	}
	

	Ext.onReady(function() {
		//doOnload();
		$("#entityDisplayField").val('${displayField}' || "name")
		//ComboBox for pageSize selected
		window.combo = new Ext.form.ComboBox({
			id : "pageSizeCombo",
			applyTo : "pageSizeArea",
			mode : 'local',
			store : new Ext.data.ArrayStore({
				fields : [ 'pageSize' ],
				data : [ [ 25 ], [ 50 ], [ 75 ], [ 100 ], [ 150 ], [ 200 ] ]
			}),
			valueField : 'pageSize',
			displayField : 'pageSize',
			value : pageSize,
			selectOnFocus : true,
			typeAhead : true,			
			triggerAction : 'all'
		});
		$('#tipShowTime').val(tipShowTime);		
	});
	
	$(document).ready(function(){
	    //解析出MAC地址规则
	    var macRule = macDisplayStyle.split("#");
	    if(macRule!=null && macRule.length ==3){
		    $('#macDisplayStyleSelect').val(macRule[0]+"#"+macRule[1]);
		    //获取MAC字母应当大写还是小写
	        if(macRule[2]=="U"){
		        $('#upperCase').attr("checked", true)
	        }else{
	            $('#lowerCase').attr("checked", true)
	        }
	    }
	    //填充
	    $('#weekStartDaySel').val(weekStartDay);
	    $('#topoTreeDisplayDevice').val(topoTreeDisplayDevice);
	    $('#topoTreeClickToOpen').val(topoTreeClickToOpen);
	});
</script>
</head>
<body class="openWinbody">
	<form id="preferencesForm" name=preferencesForm method="post" onsubmit="return false;">
		<input type=hidden id="hid1" name="generalPreferences.tabbed"
			value="<s:property value="userContext.tabbed"/>" /> 
		<input type=hidden id="hid2" name="generalPreferences.alarmWhenCloseManyTab"
			value="<s:property value="userContext.alarmWhenCloseManyTab"/>" />
		<input type=hidden id="hid3" name="generalPreferences.switchWhenNewTab"
			value="<s:property value="userContext.switchWhenNewTab"/>" /> 
		<input type=hidden id="hid4" name="generalPreferences.notifyWhenMsg"
			value="<s:property value="userContext.notifyWhenMsg"/>" /> 
		<input type=hidden id="hid5" name="generalPreferences.tabMaxLimit"
			value="<s:property value="userContext.tabMaxLimit"/>" /> 
		<input type=hidden id="hid6" name="generalPreferences.displayInputTip"
			value="<s:property value="userContext.displayInputTip"/>" /> 
		<input type=hidden id="hid7" name="generalPreferences.pageSize"
			value="<s:property value="userContext.pageSize"/>" />
		<input type=hidden id="hid8" name="bindIp" 
			value="<s:property value="userContext.user.bindIp"/>" />
		<input type="hidden" id="hid9" name="generalPreferences.macDisplayStyle" 
			value="<s:property value="userContext.macDisplayStyle"/>"/>
		<input type="hidden" id="hid10" name="generalPreferences.tipShowTime" 
			value="<s:property value="userContext.tipShowTime"/>"/>
		<input type="hidden" id="hid11" name="generalPreferences.weekStartDay" 
			value="<s:property value="userContext.weekStartDay"/>"/>
		<input type="hidden" id="hid12" name="generalPreferences.autoRefreshTime" 
			value="<s:property value="userContext.autoRefreshTime"/>"/>
		<input type="hidden" id="hid13" name="generalPreferences.topNumber" 
			value="<s:property value="userContext.topNumber"/>"/>
		<input type="hidden" id="hid14" name="generalPreferences.soundTimeInterval" 
			value="<s:property value="userContext.soundTimeInterval"/>"/>
		<input type="hidden" id="hid15" name="generalPreferences.topoTreeDisplayDevice" 
           value="<s:property value="userContext.topoTreeDisplayDevice"/>"/>
        <input type="hidden" id="hid16" name="generalPreferences.topoTreeClickToOpen" 
           value="<s:property value="userContext.topoTreeClickToOpen"/>"/>
		<input type="hidden" id="styleName" name="generalPreferences.styleName" value="white" />
		
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	@label.changeOptions@								
			</div>
		    <div class="rightCirIco wheelCirIco"></div>
		</div>
		<div class="edgeTB5LR20 ">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		           <!--  <tr>
		                <td class="rightBlueTxt" >
		                </td>
		                <td colspan="2">
		                    <input id=tabbed type=checkbox
							<s:if test="userContext.tabbed">checked</s:if>
							onclick="enableTabbed(this)" name="tabbed" /><label
							for="tabbed">@label.enableTabbed@</label>
		                </td>
		            </tr>
		            <tr class="darkZebraTr">		
		            	<td></td>            	
		                <td colspan="2">
		                    <input
							id=alarmWhenCloseManyTab type=checkbox
							<s:if test="userContext.alarmWhenCloseManyTab">checked</s:if>
							name="alarmWhenCloseManyTab"
							<s:if test="!userContext.tabbed">disabled</s:if> /><label
							for="alarmWhenCloseManyTab">@label.alarmWhenCloseManyTab@</label>
		                </td>
		            </tr> -->
		            <tr>
		            	<td></td>
		            	<td colspan="2">
		            		<input id=switchWhenNewTab
							type=checkbox
							<s:if test="userContext.switchWhenNewTab">checked</s:if>
							name="switchWhenNewTab"
							<s:if test="!userContext.tabbed">disabled</s:if> /><label
							for="switchWhenNewTab">@label.switchWhenNewTab@</label>
		            	</td>
		            </tr>
		            <tr class="darkZebraTr">
		            	<td></td>
		            	<td colspan="2">
		            		<input id=tabMaxLimit
							type=checkbox <s:if test="userContext.tabMaxLimit">checked</s:if>
							name="tabMaxLimit"
							<s:if test="!userContext.tabbed">disabled</s:if> /><label
							for="tabMaxLimit">@label.tabMaxLimit@</label>
		            	</td>
		            </tr>
		            <%-- <tr>
		            	<td></td>
		            	<td class="">@td.styleName@</td>
		            	<td>
		            		<select id="styleName" name="generalPreferences.styleName" style="width: 160px">
								<option value="white">@WorkBench.styleName.gray@</option>
							</select>
		            	</td>
		            </tr> --%>
		            <tr>
		            	<td width="40"></td>
		            	<td width="180" class="">@td.defaultPagesize@:</td>
		            	<td>
		            		<input id="pageSizeArea" type="text" name="pageSize" class="normalInput" style="width: 134px" maxlength="4"  />
							<script type="text/javascript">
								$("#pageSizeArea").attr("toolTip","@resources/WorkBench.pageSizeTip@");
							</script>
		            	</td>
		            </tr>
		            <tr class="darkZebraTr">
		            	<td width="40"></td>
		            	<td width="180" class="">@entity.displayMethod@:</td>
		            	<td>
		            		<select id="entityDisplayField" name="displayField" class="normalSel w160">
		            			<option value="ip">IP</option>
		            			<!-- <option value="mac">MAC</option> -->
		            			<option value="name" selected="selected">@COMMON.alias@</option>
		            			<option value="sysName" >@COMMON.name@</option>
		            			<option value="ip_name" >@USERPRE.IPAlias@</option>
		            			<option value="name_ip" >@USERPRE.AliasIP@</option>
		            		</select>
		            	</td>
		            </tr>
		            <tr>
		            	<td width="40"></td>
		            	<td width="180" class="">@label.macAddress@:</td>
		            	<td class="vertical-middle">
		            		<select id="macDisplayStyleSelect" name="displayField" class="normalSel w160">
		            			<option value="6#M">XX:XX:XX:XX:XX:XX</option>
		            			<option value="6#H">XX-XX-XX-XX-XX-XX</option>
		            			<option value="6#K">XX XX XX XX XX XX</option>
		            			<option value="3#D">XXXX.XXXX.XXXX</option>
		            			<option value="3#H">XXXX-XXXX-XXXX</option>
		            			<option value="1#W">XXXXXXXXXXXX</option>
		            		</select>
		            		<input type="radio" name="letterCase" checked="checked" id="upperCase" value="U"/>@label.upperCase@
		            		<input type="radio" name="letterCase" id="lowerCase" value="D"/>@label.lowerCase@
		            	</td>
		            </tr>
		            <tr class="darkZebraTr">
		            	<td width="40"></td>
		            	<td width="180" class="">@label.tipShowTime@:</td>
		            	<td>
		            		<!-- <input id="tipShowTime" type="text" name="tipShowTime" class="normalInput" style="width: 134px" maxlength="3"  /> -->
		            		<select id="tipShowTime" name="tipShowTime" class="normalSel w160">
		            			<option value="5">@label.fast@</option>
		            			<option value="10">@label.medium@</option>
		            			<option value="20">@label.slow@</option>
		            		</select>
		            	</td>
		            </tr>
		            
		            <tr>
		            	<td></td>
		            	<td>@td.ipLoginActive@</td>
		            	<td>
		            		<span id="bindIpArea" class="floatLeft pR2">
								<script type="text/javascript">
									//bindIP input
									var newIp = new ipV4Input("bindIpInput", "bindIpArea");
									newIp.width(160);
									newIp.height(18);
									setIpValue('bindIpInput',bindIpValue);
								</script>
							</span>
							<span class="floatLeft pR2">
								<input
								type=button class=BUTTON75
								value="@onMouseOut.bind@"
								onclick="bindClick();" /> 
							</span>
							<span class="floatLeft pR2">
								<input
								<s:if test="userContext.user.ipLoginActive==0">disabled</s:if>
								type="button" class="BUTTON75" onclick="unbindClick();"
								value="@onMouseOut.unbind@" />
							</span>
							
		            	</td>
		            </tr>
		            <tr class="darkZebraTr">
		            	<td></td>
		            	<td>@label.customWeekStart@:</td>
		            	<td >
		            		<select id="weekStartDaySel" class="normalSel">
		            			<option value="1">@label.sunday@</option>
		            			<option value="2">@label.monday@</option>
		            		</select>
		            	</td>
		            </tr>
		            <tr>
		            	<td></td>
		            	<td>@USERPRE.autoRefreshTime@:</td>
		            	<td >
		            		<input id="autoRefreshTime" type="text" class="normalInput w160" value="${userContext.autoRefreshTime}" toolTip="@USERPRE.refreshTip@"/> (@CALENDAR.Sec@)
		            	</td>
		            </tr>
		             <tr class="darkZebraTr">
		            	<td></td>
		            	<td>@USERPRE.topNumberLabel@:</td>
		            	<td >
		            		<input id="topNumber" type="text" maxlength=3 class="normalInput w160" value="${userContext.topNumber}" toolTip="@USERPRE.topNumber@"/>
		            	</td>
		            </tr>
		            <tr>
		            	<td></td>
		            	<td>@label.alarmTimeInterval@</td>
		            	<td>
		            		<input id="soundTimeInterval" type="text" class="normalInput w160" value="${userContext.soundTimeInterval}" maxlength="2" value="1" toolTip="@label.alermTimeIntervalTip@" /> @label.alarmTimeInterval2@
		            	</td>
		            </tr>
		            <tr class="darkZebraTr">
                        <td></td>
                        <td>@label.topoTreeDisplayDevice@:</td>
                        <td>
                            <select id="topoTreeDisplayDevice" class="normalSel">
                                <option value="1">@label.show@</option>
                                <option value="2">@label.hide@</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>@label.topoTreeClickToOpen@:</td>
                        <td>
                            <select id="topoTreeClickToOpen" class="normalSel">
                                <option value="1">@label.snapshot@</option>
                                <option value="2">@label.graph@</option>
                            </select>
                        </td>
                    </tr>
		        </tbody>
		    </table>
		    <div class="noWidthCenterOuter clearBoth">
			    <ol class="upChannelListOl pB0 pT5 noWidthCenter">
			        <li>
			            <a href="javascript:;" class="normalBtnBig"  id="okBt" onclick="okClick()">
			                <span>
			                    <i class="miniIcoData">
			                    </i>
			                    @button.save@
			                </span>
			            </a>
			        </li>
			        <li>
			            <a href="javascript:;" class="normalBtnBig"  onclick="cancelClick()">
			                <span>
			                	 <i class="miniIcoForbid">
			                    </i>
			                    @button.cancel@
			                </span>
			            </a>
			        </li>
			    </ol>
			</div>
		</div>
	</form>
</body>
</Zeta:HTML>