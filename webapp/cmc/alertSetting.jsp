<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<HTML>
<HEAD>
<%@include file="/include/nocache.inc"%>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<%@include file="/include/tabPatch.inc"%>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/zetaframework/IpTextField.js"></script>
<script type="text/javascript">
var tabs = null
var entityId = <s:property value="entityId"/>;
var docsDevEvControlObject = ${docsDevEvControlListObject};
Ext.BLANK_IMAGE_URL = '/images/s.gif';
function alertFilterSetting(){
	if(Zeta$('taggedMode').value.trim() == 0){
		window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.levelchoice);
		return;
	}
	window.top.showOkCancelConfirmDlg(I18N.CMC.title.tip, I18N.CMC.text.setlevelaction, function(type){
		if(type=="cancel"){
			return;
		}
		window.top.showWaitingDlg(I18N.CMC.title.wait, I18N.CMC.text.doinglevelactionset, 'ext-mb-waiting');
		$.ajax({
	  	      url: '/cmc/config/deviceeventcontrol.tv?entityId=' + entityId,
	  	      type: 'post',
	  	      data: jQuery(formChanged).serialize(),
	  	      success: function(response) {
					response = eval("(" + response + ")");
	    	    	if(response.message == "success"){
	    	    		window.parent.closeWaitingDlg();
	    	    		window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.setlevelactionsuccess);
					 }else{
						 window.parent.closeWaitingDlg();
						 window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.setlevelactionsuccess);
					 }
					//window.parent.getFrame("entity-" + cmcId).onRefreshClick();
					cancelClick();
				}, error: function(response) {
					window.parent.closeWaitingDlg();
					window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.setlevelactionsuccess);
				}, cache: false
			});
	});
}
var localnonvolTest;
var trapsTest;
var syslogTest;
var localVolatileTest;
function priorityChange(){
	var i = Zeta$('taggedMode').value.trim();
	$("#localnonvol").attr("checked", false);
	$("#traps").attr("checked", false);
	$("#syslog").attr("checked", false);
	$("#localVolatile").attr("checked", false);
	checkedSetForTrap(docsDevEvControlObject[i-1].localnonvol,docsDevEvControlObject[i-1].traps,docsDevEvControlObject[i-1].syslog,docsDevEvControlObject[i-1].localVolatile);
}
function checkedSetForTrap(localnonvolTest,trapsTest,syslogTest,localVolatileTest){
	if(localnonvolTest == 1){
		/* alert("ok"); */
		$("#localnonvol").attr("checked", true);
	}
	if(trapsTest == 1){
		$("#traps").attr("checked", true);
	}
	if(syslogTest == 1){
		$("#syslog").attr("checked", true);
	}
	if(localVolatileTest == 1){
		$("#localVolatile").attr("checked", true);
	}
}
function trapFilterSetting(){
	window.top.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.waitfornextrelease);
}
function syslogSetting(){
	window.top.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.waitfornextrelease);
}
function clearSyslog(){
	window.top.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.waitfornextrelease);
}
function returnFactory(){
	window.top.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.waitfornextrelease);
}
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
Ext.onReady(function(){
	var w = document.body.clientWidth - 20;
	var h = document.body.clientHeight - 60;
    tabs = new Ext.TabPanel({
        width: w,
        height: h,
        activeTab: 0,
        frame: true,
        items:[
        	{id: 'tab1', contentEl: 'alertFilterTab', title:I18N.CMC.title.alertfilter}
        ]
    });
    tabs.render('tabs');
    Zeta$('buttonPanel').style.display = '';
});
</script>
</HEAD>
<BODY class=POPUP_WND style="padding: 10px">
	<div id="tabs">
		<div id="alertFilterTab" class="x-hide-display">
		<form name="formChanged" id="formChanged">
			<table cellspacing=10>
				<tr>
					<td width=150 style="padding-left: 20px;"><fmt:message bundle='${cmc}' key='CMC.text.warninglevel'/></td>
					<td><div>
							<select id="taggedMode" name="docsDevEvControl.docsDevEvPriority" style="width: 150px;" onchange="priorityChange()">
								<option value="0" selected><fmt:message bundle='${cmc}' key='CMC.text.pleaseselec'/></option>
								<option value="1" <s:if test="docsDevEvPriority==1">selected</s:if>>emergency</option>
								<option value="2" <s:if test="docsDevEvPriority==2"></s:if>>alert</option>
								<option value="3" <s:if test="docsDevEvPriority==3"></s:if>>critical</option>
								<option value="4" <s:if test="docsDevEvPriority==4"></s:if>>error</option>
								<option value="5" <s:if test="docsDevEvPriority==5"></s:if>>warning</option>
								<option value="6" <s:if test="docsDevEvPriority==6"></s:if>>notice</option>
								<option value="7" <s:if test="docsDevEvPriority==7"></s:if>>information</option>
								<option value="8" <s:if test="docsDevEvPriority==8"></s:if>>debug</option>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<td width=200 style="padding-left: 20px;"><fmt:message bundle='${cmc}' key='CMC.text.selectEventProcMode'/></td>
					<td></td>
				</tr>
				<tr>
					<td width=100 style="padding-left: 20px;"><input id=localnonvol class=iptxt name="docsDevEvControl.localnonvol"
						type=checkbox value="1" width=50px>
					</td>
					<td>localnonvol</td>
					<td width=100 style="padding-left: 20px;"><input id=traps class=iptxt name="docsDevEvControl.traps"
						type=checkbox value="1" width=50px>
					</td>
					<td>traps</td>
				</tr>
				<tr>
					<td width=100 style="padding-left: 20px;"><input id=syslog  class=iptxt name="docsDevEvControl.syslog"
						type=checkbox value="1" width=50px>
					</td>
					<td>syslog</td>
					<td width=100 style="padding-left: 20px;"><input id=localVolatile  class=iptxt name="docsDevEvControl.localVolatile"
						type=checkbox value="1" width=50px>
					</td>
					<td>localVolatile</td>
				</tr>
				<tr>
					<td width=100 style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="1" width=50px disabled>
					</td>
					<td>stdInterface</td>
					<td width=100 style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="1" width=50px disabled>
					</td>
					<td>ignore3</td>
				</tr>
				<tr>
					<td width=100 style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="1" width=50px disabled>
					</td>
					<td>ignore4</td>
					<td width=100 style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="1" width=50px disabled>
					</td>
					<td>ignore5</td>
				</tr>
				<tr>
					<td width=100 style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="1" width=50px disabled>
					</td>
					<td>ignore6</td>
					<td width=100 style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="1" width=50px disabled>
					</td>
					<td>ignore7</td>
				</tr>
			</table>
			</form>
			<div id="tab1ButtonPanel" style="padding-left: 380px; padding-top: 23px">
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="alertFilterSetting()"><fmt:message bundle='${cmc}' key='CMC.label.savesetting'/></button>
			</div>
		</div>
		<div id="trapFilterTab" class="x-hide-display">
			<table cellspacing=10>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.cmRegReqFailed'/></td>
					<td style="padding-left: 30px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.cmRegResFailed'/></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px>Syslog Server IP</td>
					<td style="padding-left: 30px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.cmRegAckFailed'/></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DSFResFailed'/></td>
					<td style="padding-left: 30px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DSFResFailed'/></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DSFResFailed'/></td>
					<td style="padding-left: 30px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.BPIInitFailed'/></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.BPKMOperationFailed'/></td>
					<td style="padding-left: 30px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DySecurityOperFailed'/></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DccRequestFailed'/></td>
					<td style="padding-left: 30px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DccResponseFailed'/></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;"><input class=iptxt
						type=checkbox value="" width=50px>
					</td>
					<td width=120px><fmt:message bundle='${cmc}' key='CMC.text.DccAckFailed'/></td>
				</tr>
			</table>
			<div id="tab2ButtonPanel" style="padding-left: 380px; padding-top: 17px">
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="trapFilterSetting()"><fmt:message bundle='${cmc}' key='CMC.label.savesetting'/></button>
			</div>
		</div>
		<div id="syslogSettingTab" class="x-hide-display">
			<table cellspacing=10>
				<tr>
					<td width=150 style="padding-left: 20px;">Syslog Server IP</td>
					<td><div id="syslogServerIp" class=ipTextField name="syslogServerIp" style="width: 150px;">
                          <table cellspacing=0 cellpadding=0>
                              <tr>
                                  <td><input type=text id="syslogServerIp1"
                                      name="syslogServerIp1" maxlength=3
                                      onbeforepaste="ipmask_c('syslogServerIp')"
                                      onkeydown="return ipmask(this, event, 'syslogServerIp2')"
                                      onkeyup="ipmaskup(this, event, 'syslogServerIp2')">
                                  </td>
                                  <td class=dot align=center>.</td>
                                  <td><input type=text id="syslogServerIp2"
                                      name="syslogServerIp2" maxlength=3
                                      onbeforepaste="ipmask_c('syslogServerIp')"
                                      onkeydown="return ipmask(this, event, 'syslogServerIp3')"
                                      onkeyup="ipmaskup(this, event, 'syslogServerIp3')">
                                  </td>
                                  <td class=dot align=center>.</td>
                                  <td><input type=text id="syslogServerIp3" name="syslogServerIp3"
                                      maxlength=3 onbeforepaste="ipmask_c('syslogServerIp')"
                                      onkeydown="return ipmask(this, event, 'syslogServerIp4')"
                                      onkeyup="ipmaskup(this, event, 'syslogServerIp4')">
                                  </td>
                                  <td class=dot align=center>.</td>
                                  <td><input type=text id="syslogServerIp4" name="syslogServerIp4"
                                      maxlength=3 onbeforepaste="ipmask_c('syslogServerIp')"
                                      onkeydown="return ipmask(this, event, 'syslogServerIp4')"
                                      onkeyup="ipmaskup(this, event, 'syslogServerIp')">
                                  </td>
                              </tr>
                          </table>
                    </div></td>
				</tr>
				<tr>
                    <td width=150 style="padding-left: 20px;"><fmt:message bundle='${cmc}' key='CMC.text.wayofthrottle'/></td>
                    <td><div>
                            <select id="Throttle" style="width: 150px;" onchange="">
                                <option value="1">unconstrained</option>
                                <option value="2">maintainBelowThreshold</option>
                                <option value="3">stopAtThreshold</option>
                                <option value="4">inhibited</option>
                            </select>
                        </div>
                    </td>
                </tr>
				<tr>
					<td width=150 style="padding-left: 20px;"><fmt:message bundle='${cmc}' key='CMC.text.intervalofthrottle'/></td>
					<td><input class=iptxt type=text value="" width=150px>
					</td>
				</tr>
				<tr>
					<td width=150 style="padding-left: 20px;"><fmt:message bundle='${cmc}' key='CMC.text.thresholdofthrottle'/></td>
					<td><input class=iptxt type=text value="" width=150px>
					</td>
				</tr>
			</table>
			<div id="tab3ButtonPanel" style="padding-left: 215px; padding-top: 100px">
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="clearSyslog()"><fmt:message bundle='${cmc}' key='CMC.text.emptysyslog'/></button>
				&nbsp;
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="returnFactory()"><fmt:message bundle='${cmc}' key='CMC.text.recoveryfactorystates'/></button>
				&nbsp;
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="syslogSetting()"><fmt:message bundle='${cmc}' key='CMC.label.savesetting'/></button>
			</div>
		</div>
	</div>
	<div id="buttonPanel" align=right
		style="padding-top: 10px; display: none">
		<button class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
	</div>
</BODY>
</HTML>
