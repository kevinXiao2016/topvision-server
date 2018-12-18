<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
    import js.json2
</Zeta:Loader>
<head>
<script>
	function loadConnectivityStrategy() {
		$.get('/connectivity/loadConnectivityStrategy.tv', function(data) {
			for(var stratgery in data) {
				$('#'+stratgery).attr('checked', data[stratgery] === 1);
			}
		});
	}
	//save config
	function saveConnectivityStratgery() {
		var data = {};
		var cbxs = $('#content-table').find('.strategy-cbx');
		
		if($('#content-table').find('.strategy-cbx:checked').length === 0) {
			top.showMessageDlg('@COMMON.tip@', '@sys.chooseAtLeastOne@');
			return
		}
		
		for(var i=0, len=cbxs.length; i<len; i++) {
			data[cbxs[i].id] = $(cbxs[i]).is(':checked') ? 1 : 0;
		}
		
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
		$.ajax({
			url : '/connectivity/saveConnectivityStrategy.tv',
			type : 'POST',
			data : {
				strategyStr: JSON.stringify(data)	
			},
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
			      title: '@sys.tip@',
			      html: '<b class="orangeTxt">@sys.saved@</b>'
			    });
				cancelClick();
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	Ext.onReady(function() {
		loadConnectivityStrategy();
	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<form id="snmpConfigForm" name="snmpConfigForm">
		<div class="openWinHeader">
	    <div class="openWinTip">@sys.connectivyTip@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table id="content-table" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="100">
	                    <input type="checkbox" id="icmp" class="strategy-cbx"/>
	                </td>
	                <td>
						@connectivity.icmpStrategy@
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <input type="checkbox" id="snmp" class="strategy-cbx"/>
	                </td>
	                <td>
						@connectivity.snmpStrategy@
	                </td>
	            </tr>
	            <tr>
	            	<td class="rightBlueTxt">
	            		<input type="checkbox" id="tcp" class="strategy-cbx"/>
	            	</td>
	            	<td>
						@connectivity.tcpStrategy@
	            	</td>
	            </tr>
	        </tbody>
	    </table>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="saveConnectivityStratgery()"  id="snmpSubmit">
		                <span>
		                    <i class="miniIcoData">
		                    </i>
		                   	@sys.save@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
		                <span>
		                	<i class="miniIcoForbid">
		                    </i>
		                    @sys.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>	
	</form>
</body>
</Zeta:HTML>