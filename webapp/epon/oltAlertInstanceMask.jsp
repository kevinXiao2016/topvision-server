<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module epon
</Zeta:Loader>
<head>
    <script type="text/javascript">
    	var entityId = <%=request.getParameter("entityId")%>;
    	var slots = new Array();
    	var slotsCount = 0;
    	var ports = new Array();
    	var portsCount = 0;
    	var fans = new Array();
    	var fansCount = 0;
    	var powers = new Array();
    	var powersCount = 0;
    	var onus = new Array();
    	var onusCount = 0;
    	var unis = new Array();
    	var unisCount = 0;
    	Ext.onReady(function() {
    		Ext.Ajax.request({
    			url: '/epon/alert/loadOltAlertAvailableInstance.tv?entityId=' + entityId + '&r=' + Math.random(),
    			success: function(json) {
    				var instanceJson = Ext.decode(json.responseText);
    				var firstSlotNo;
    				var slotNo;
    				var firstPortType;
    				var firstPonNo;
    				var firstOnuNo;
    				for (var key in instanceJson) {
    					var instanceTypeTmp = instanceJson[key].instanceType;
    					var instanceStateTmp = instanceJson[key].instanceState;
    					var instanceSlotNo = instanceJson[key].slotNo;
    					var instanceIndexTmp = instanceJson[key].instanceIndex;
    					var tmpObj = new instanceObj(instanceTypeTmp, instanceStateTmp, instanceIndexTmp,instanceSlotNo);
    					if (instanceTypeTmp == 'slot') {
			                slots[slotsCount] = tmpObj;
			                if (firstSlotNo == undefined) {
			                	firstSlotNo = instanceStateTmp.split('_')[0];
			                }
			                slotNo = instanceSlotNo;
			                slotsCount++;
			            } else if (instanceTypeTmp == 'pon' || instanceTypeTmp == 'sni') {
			                ports[portsCount] = tmpObj;
			                if (instanceTypeTmp == 'pon' && firstPonNo == undefined) {
			                	firstPonNo = instanceStateTmp.split('_')[0] + '_' + instanceStateTmp.split('_')[1];
			                }
			                portsCount++;
			            } else if (instanceTypeTmp == 'fan') {
			                fans[fansCount] = tmpObj;
			                fansCount++;
			            } else if (instanceTypeTmp == 'power') {
			                powers[powersCount] = tmpObj;
			                powersCount++;
			            } else if (instanceTypeTmp == 'onu') {
			                onus[onusCount] = tmpObj;
			                onusCount++;
			            } else if (instanceTypeTmp == 'uni') {
			                unis[unisCount] = tmpObj;
			                unisCount++;
			            }
    				}
    				// 槽位初始化
    				for (var i = 0; i < slots.length; i++) {
    					if (slots[i]) {
    						$('#slot').append('<option value="' + slots[i].iIndex + '">' + slots[i].iSlotNo + '</option>');
    					}
    				}
    				for (var i = 0; i < fans.length; i++) {
    					if (fans[i]) {
    						$('#slot').append('<option value="' + fans[i].iIndex + '">' + 'FAN' + (fans[i].iState.split('_')[0] - 18) + '</option>');
    					}
    				}
    				for (var i = 0; i < powers.length; i++) {
    					if (powers[i]) {
    						$('#slot').append('<option value="' + powers[i].iIndex + '">' + 'PWR' + (powers[i].iState.split('_')[0] - 18 - fans.length) + '</option>');
    					}
    				}
    				// 端口初始化
    				for (var i = 0; i < ports.length; i++) {
    					if (ports[i] && ports[i].iState.split('_')[0] == firstSlotNo) {
    						if (firstPortType == undefined) {
    							firstPortType = ports[i].iType;
    						}
    						$('#port').append('<option value="' + ports[i].iIndex + '">' + ports[i].iType + '' + ports[i].iState.split('_')[1] + '</option>');
    					}
    				}
    				// ONU和UNI初始化
    				if (firstPortType == 'sni') {
    					$('#onu').attr('disabled', true);
    					$('#uni').attr('disabled', true);
    				} else {
    					// ONU初始化
    					for (var i = 0; i < onus.length; i++) {
    						if (onus[i] && onus[i].iState.split('_')[0] == firstPonNo.split('_')[0] && onus[i].iState.split('_')[1] == firstPonNo.split('_')[1]) {
    							$('#onu').append('<option value="' + onus[i].iIndex + '">' + onus[i].iType + '' + onus[i].iState.split('_')[2] + '</option>');
    							if (firstOnuNo == undefined) {
    								firstOnuNo = onus[i].iState.slice(0, onus[i].iState.lastIndexOf('_'));
    							}
    						}
    					}
    					// UNI初始化
    					for (var i = 0; i < unis.length; i++) {
    						if (unis[i] && firstOnuNo == unis[i].iState.slice(0, unis[i].iState.lastIndexOf('_'))) {
    							$('#uni').append('<option value="' + unis[i].iIndex + '">' + unis[i].iType + '' + unis[i].iState.split('_')[3] + '</option>');
    						}
    					}
    				}
    			}
    		});
    		// 点选槽位时联动
    		$('#slot').change(function() {
    			var selectedSlot = $('#slot').find('option:selected').text()
    			if (selectedSlot.indexOf('FAN') > -1 || selectedSlot.indexOf('PWR') > -1) {
    				// 选择电源或风扇时,端口、ONU、UNI禁用。
    				$('#port').empty()
    				$('#port').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#port').attr('disabled', true)
    				$('#onu').empty()
    				$('#onu').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#onu').attr('disabled', true)
    				$('#uni').empty()
    				$('#uni').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#uni').attr('disabled', true)
    			} else {
    				// 选择板卡时，清空原有内容。
    				$('#port').empty()
    				$('#port').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#port').attr('disabled', true)
    				$('#onu').empty()
    				$('#onu').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#onu').attr('disabled', true)
    				$('#uni').empty()
    				$('#uni').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#uni').attr('disabled', true)
    				$.each(ports, function(i, n) {
    					if (ports[i].iState.split('_')[0] == selectedSlot) {
    						$('#port').attr('disabled', false);
    						$('#port').append('<option value="' + ports[i].iIndex + '">' + ports[i].iType + '' + ports[i].iState.split('_')[1] + '</option>');
    					}
    				})
    			}
    		});
    		// 点选端口时联动
    		$('#port').change(function() {
    			var selectedPort = $('#port').find('option:selected').text();
    			if (selectedPort == I18N.SERVICE.optionAll || selectedPort.indexOf('sni') > -1) {
    				// 选全部或SNI口时，ONU、UNI禁用。
    				$('#onu').attr('disabled', true);
    				$('#uni').attr('disabled', true);
    			} else {
					// 选PON口时，清空原有ONU。
    				$('#onu').empty();
    				$('#onu').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#onu').attr('disabled', true);
    				$('#uni').empty();
    				$('#uni').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#uni').attr('disabled', true);
    				$.each(onus, function (i, n) {
    					if (onus[i].iState.split('_')[1] == selectedPort.slice(3)) {
    						$('#onu').attr('disabled', false);
    						$('#onu').append('<option value="' + onus[i].iIndex + '">' + onus[i].iType + '' + onus[i].iState.split('_')[2] + '</option>');
    					}
    				});
    			}
    		});
    		// 点选ONU时联动
    		$('#onu').change(function() {
    			var selectedOnu = $('#onu').find('option:selected').text();
    			if (selectedOnu == I18N.SERVICE.optionAll ) {
    				// 选择全部时，UNI禁用
    				$('#uni').attr('disabled', true);
    			} else {
    				// 选择单个ONU时，清空原有UNI。
    				$('#uni').empty();
    				$('#uni').append('<option value="0">'+ I18N.SERVICE.optionAll +'</option>')
    				$('#uni').attr('disabled', true);
    				$.each(unis, function(i, n) {
    					if (unis[i].iState.split('_')[2] == selectedOnu.slice(3)) {
    						$('#uni').attr('disabled', false);
    						$('#uni').append('<option value="' + unis[i].iIndex + '">' + unis[i].iType + '' + unis[i].iState.split('_')[3] + '</option>');
    					}
    				});
    			}
    		});
    	});
    	function addInstanceMask() {
    		var finalIndex;
    		if ($('#uni').find('option:selected').text() != I18N.SERVICE.optionAll ) {
    			finalIndex = $('#uni').val();
    		} else if ($('#onu').find('option:selected').text() != I18N.SERVICE.optionAll ) {
    			finalIndex = $('#onu').val();
    		} else if ($('#port').find('option:selected').text() != I18N.SERVICE.optionAll ) {
    			finalIndex = $('#port').val();
    		} else {
    			finalIndex = $('#slot').val();
    		}
    		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.addingAlertMaskRule , 'ext-mb-waiting');
    		Ext.Ajax.request({
    			url: '/epon/alert/addOltAlertInstanceMask.tv?r=' + Math.random(),
    			params: {
    				entityId: entityId,
    				instanceMaskIndex: finalIndex,
    				instanceMaskEnable: 1
    			},
    			success: function(response) {
    				if (response.responseText) {
    					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addAlertMaskRuleEr )
    				} else {
    					top.closeWaitingDlg();
						 top.nm3kRightClickTips({
			   				title: I18N.COMMON.tip,
			   				html: I18N.SERVICE.addAlertMaskRuleOk
			   			 });
    					//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addAlertMaskRuleOk )
	    				window.parent.getWindow("oltAlertMask").body.dom.firstChild.contentWindow.instanceMaskStore.load();
	    				cancelClick();
    				}
    			},
    			failure: function() {
    				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addAlertMaskRuleEr )
    			}
    		});
    	}
    	function cancelClick() {
    		window.parent.closeWindow('oltAlertInstanceMask');
    	}
    	// 实体对象
    	function instanceObj(instanceType, instanceState, instanceIndex,instanceSlotNo) {
    		this.iType = instanceType;
    		this.iState = instanceState;
    		this.iSlotNo = instanceSlotNo;
    		this.iIndex = instanceIndex;
    	}
    </script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">
		</div>
		<div class="rightCirIco pageCirIco"></div>		
	</div>	
	<div class="edgeTB10LR20 pT40">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="rightBlueTxt" width="300">@EPON.slot@:</td>
				<td>
					<select id="slot" class="normalSel w160"></select>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt" >@EPON.port@:</td>
				<td>
					<select id="port" class="normalSel w160">
					<option value="0">@SERVICE.optionAll@</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt" >ONU:</td>
				<td>
					<select id="onu"  class="normalSel w160">
						<option value="0">@SERVICE.optionAll@</option>
					</select>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt" >UNI:</td>
				<td>
					<select id="uni" class="normalSel w160">
						<option value="0">@SERVICE.optionAll@</option>
					</select>
				</td>
			</tr>
		</table>
		<div class="noWidthCenterOuter">
			<ol class="upChannelListOl pB0 pT80 noWidthCenter">
				<li><a href="javascript:;" class="normalBtnBig" onclick="addInstanceMask()"><span><i class="miniIcoAdd"></i>@SERVICE.add@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			</ol>
		</div>
	</div>	
	
</body>
</Zeta:HTML>