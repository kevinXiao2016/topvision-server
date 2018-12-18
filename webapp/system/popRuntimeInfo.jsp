<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module resources
</Zeta:Loader>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	var w = document.body.clientWidth - 20;
	var h = document.body.clientHeight - 60;
	if (w < 300) {
		w = 300;
	}
	if (h < 100) {
		h = 100;
	}	

    /* var tabs = new Ext.TabPanel({
        renderTo: 'tabs',
        width: w,
        height: 400,
        activeTab: 0,
        frame: true,
        defaults:{autoHeight: true},
        items:[
        	{contentEl: 'generalTab', title: I18N.SYSTEM.baseInfo}
        ]
    });  */
});
function restartClick() {
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.note8, function (type) {
		if (type == "cancel") {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.SYSTEM.note9);
		$.ajax({url: 'restartSystem.tv', type: 'GET',
		success: function() {
			startTimer();
		},
		error: function() {
			window.top.showErrorDlg();
		},
		dataType: 'plain', cache: false});
	});
}
var timer = null;
function connectTest() {
	$.ajax({url: '../network/ping.tv', type: 'GET',
	success: function() {
		stopTimer();
		window.top.closeWaitingDlg();
	   	window.top.location.href = 'showLogon.tv';
	},
	error: function() {},
	dataType: 'plain', cache: false});	
	
}
function startTimer() {
	if (timer == null) {
		timer = setInterval("connectTest()", 5000);
	}
	window.top.stopEventDispatcher();
}
function stopTimer() {
	if (timer != null) {
		clearInterval();
		timer = null;
	}
}
function closeClick() {
	window.top.closeWindow('modalDlg');
}

$(function(){	
	$("table.zebraTableRows tbody tr:odd").addClass("darkZebraTr");
});//end document.ready;
</script>	  
</head><body class="openWinBody">
	<div >
		<div class="edge10">
			<div class="zebraTableCaption">
		     <div class="zebraTableCaptionTitle"><span>@cmc/text.baseInfo@</span></div>
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">	     	
		         <tbody>
                     <tr>
                         <td class="rightBlueTxt">@runTimeInfo.startTime@</td>
                         <td><%= SystemConstants.getStartTime() + "(" + SystemConstants.getDuration((String)pageContext.getAttribute("lang")) + ")" %></td>
                     </tr>
		             <tr>
		                 <td class="rightBlueTxt">@runTimeInfo.maxMemory@</td>
		                 <td><%= com.topvision.framework.common.NumberUtils.getByteLength(Runtime.getRuntime().maxMemory()) %></td>
		             </tr>
		             <tr >
		                 <td class="rightBlueTxt">@runTimeInfo.freeMemory@</td>
		                 <td><%= com.topvision.framework.common.NumberUtils.getByteLength(Runtime.getRuntime().freeMemory()) %></td>
		             </tr>
		             <tr>
		                 <td class="rightBlueTxt">@runTimeInfo.totalMemory@</td>
		                 <td><%= com.topvision.framework.common.NumberUtils.getByteLength(Runtime.getRuntime().totalMemory()) %></td>
		             </tr>
		             <tr>
		                 <td class="rightBlueTxt">@runTimeInfo.availableProcessors@</td>
		                 <td><%= Runtime.getRuntime().availableProcessors() %></td>
		             </tr>
                     <tr>
                         <td colspan="2">
                            <b class="pL40 orangeTxt">@runTimeInfo.systemEnvironments@</b>
                         </td>
                     </tr>
		              <%
					    	java.util.Map map = System.getenv();
					    	for (java.util.Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
					    	    java.util.Map.Entry e = (java.util.Map.Entry)  iter.next();
				
					    %>
					        <tr>
					            <td class="rightBlueTxt"><%= e.getKey() %></td>
					            <td class="wordBreak"><%= e.getValue() %></td>
					        </tr>
					    <%
					    	}
					    %>
						    <tr>
						    	<td colspan=2><b class="pL40 orangeTxt">@runTimeInfo.systemProperties@</b></td>
						    </tr>
					    <%
					    	java.util.Properties props = System.getProperties();
					    	for (java.util.Iterator iter = props.entrySet().iterator(); iter.hasNext();) {
					    	    java.util.Map.Entry e = (java.util.Map.Entry)  iter.next();
				
					    %>
					        <tr>
					            <td class="rightBlueTxt"><%= e.getKey() %></td>
					            <td  class="wordBreak"><%= e.getValue() %></td>
					        </tr>
					    <%
					    	}
					    %>
		         </tbody>
		     </table>
			</div>
		</div>
	</div>
	<%-- <Zeta:ButtonGroup>
	        <Zeta:Button onClick="closeClick()" icon="miniIcoWrong">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup> --%>
<div id="tabs">
<div id="generalTab" class="x-hide-display">
	
</div>
<div id="jobTab" class="x-hide-display">
	<div style="width:760px; height: 480px; overflow:auto">
	<table border="1">
	    <s:iterator value="jobList" id="job">
	        <tr>
	            <td><s:property value="group"/></td><td><s:property value="name"/></td><td><s:property value="nextFireTime"/></td>
	        </tr>
	    </s:iterator>
	</table>
	</div>
</div>
<div id="performanceTab" class="x-hide-display">
</div>
</div>
</body></Zeta:HTML>