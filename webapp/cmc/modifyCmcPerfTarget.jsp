<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="../include/cssStyle.inc"%>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    css  css.performanceMajorStyle
    import performance.js.performanceMajor
</Zeta:Loader>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
	var cmcPerfTargetCycle = ${cmcPerfTargetCycleObject};
	//版本控制功能
	//var supportFuncs = ${supportFuncs};
	$(document).ready(function(){
		createModifyTopToolbar();
		golbalCbxBindEvent();
		groupExpandOrCollapse();

		for(var key in cmcPerfTargetCycle){
			$("#"+key).val(cmcPerfTargetCycle[key]);
		}
		for(var key in cmcPerfTargetCycle.enableList){
			if(cmcPerfTargetCycle.enableList[key]==1){
				$("#"+key).attr("checked", true).val(1);
			}else{
				$("#"+key).attr("checked", false).val(0);
			}
		}

		$(".numberInput").each(function(){
			$(this).attr("toolTip",'@Tip.collectTimeRule@');
		});
		
		//判断全选checkbox是否选上
		$("tbody").each(function(){
			if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
				$(this).find(".allSelectCheck").attr("checked",true);
			}
		});
		
		//点击提示，提示信息闪烁一下;
		$("#optLinkHelpPng1").click(function(){
			var $tip = $("#yellowTip1"); 
			$tip.fadeTo("fast",0,function(){
				$tip.fadeTo("normal",1);
			})
		})
		$("#optLinkHelpPng2").click(function(){
			var $tip = $("#yellowTip2"); 
			$tip.fadeTo("fast",0,function(){
				$tip.fadeTo("normal",1);
			})
		})
		$("#optLinkHelpPng3").click(function(){
			var $tip = $("#yellowTip3"); 
			$tip.fadeTo("fast",0,function(){
				$tip.fadeTo("normal",1);
			})
		})
        $("#optLinkHelpPng4").click(function(){
            var $tip = $("#yellowTip4"); 
            $tip.fadeTo("fast",0,function(){
                $tip.fadeTo("normal",1);
            })
        })
		//initFuncSupportControl(supportFuncs);
	})
	
	/**
	 * 创建修改页面的顶部工具栏
	 */
	function createModifyTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "toolbar-back",
	        items: [
	           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveModifyOneCmcPerf},
	           createSetTimeButton()
	           ]
	    });
	}

	/**
	 * 保存对单个CCMTS性能指标的修改
	 */
	function saveModifyOneCmcPerf(){
		if(validatePerfValue()==false){return;}
		$.ajax({
			url: '/cmc/perfTarget/modifyCmcPerfTargetById.tv',
	    	type: 'POST',
	    	data: $("#modifyForm").serialize(),
	    	dataType:"json",
	   		success: function(result) {
	   			if(result){
		   			window.parent.showMessageDlg('@COMMON.tip@', "@Tip.modifySuccess@");
	   			}else{
	   				window.parent.showErrorDlg();
	   			}
	   		}, error: function(result) {
	   			window.parent.showErrorDlg();
			}, cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
	}
	
	/**
	 * 初始化版本控制功能，将不支持的功能隐藏
	 */
	function initFuncSupportControl(funcsObj){
	    for(var param in funcsObj){
	        if(funcsObj[param]){
	            $("." + param).css("display", "block");
	        }else{
	            $("." + param).css("display", "none");
	        }
	    }
	}
	
</script>

</head>
<body>
<div id="modifyDiv" class="cover">
	
	<div id="toolbar-back"></div>
	<form id="modifyForm">
		<input type="hidden" id="selectedPerfNames" name="selectedPerfNames" value=""/>
		<input type="hidden" id="entityId" name="entityId" />
		<input type="hidden" id="deviceName" name="deviceName" />
		<div class="contentDiv">
			<table class="contentTable" cellpadding="0" cellspacing="0" rules="all" border="0">
					<tbody>
						<tr class="groupTitle">
							<td colspan="9" class="groupTitle_open">
								<span class="spanWidth100">@Performance.service@</span>
								<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
							</td>
						</tr>
						<tr class="groupContent">
							<td class="rightBlueTxt">@Performance.cpuUsed@:</td>
							<td width="80"><input id="cpuUsed" name="cpuUsed" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td width="70"><input id="cpuUsedEnable" name="cpuUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td class="rightBlueTxt">@Performance.memUsed@:</td>
							<td width="80"><input id="memUsed" name="memUsed" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td width="70"><input id="memUsedEnable" name="memUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td class="rightBlueTxt">@Performance.flashUsed@:</td>
							<td width="80"><input id="flashUsed" name="flashUsed" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td width="70"><input id="flashUsedEnable" name="flashUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						</tr>
						<tr class="groupContent">
							<td class="rightBlueTxt vertical-middle">
								<img id="optLinkHelpPng1" src="/images/performance/Help.png" alt="" class="cursorPointer" />
								<span>@Performance.optLink@:</span>
							</td>
							<td><input id="optLink" name="optLink" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td><input id="optLinkEnable" name="optLinkEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td class="rightBlueTxt vertical-middle" >
								<img id="optLinkHelpPng2" src="/images/performance/Help.png" alt=""  class="cursorPointer" />
								<span class="chnSpan3">@Performance.moduleTemp@:</span>
							</td>
							<td><input id="moduleTemp" name="moduleTemp" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td><input id="moduleTempEnable" name="moduleTempEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td colspan="3"></td>
						</tr>
					</tbody>
					<tbody>
						<tr class="groupTitle">
							<td colspan="9" class="groupTitle_open">
								<span class="spanWidth100">@Performance.flow@</span>
								<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
							</td>
						</tr>
						<tr class="groupContent">
							<td class="rightBlueTxt" width=""><span class="chnSpan1">@Performance.upLinkFlow@:</span></td>
							<td><input id="upLinkFlow" name="upLinkFlow" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td><input id="upLinkFlowEnable" name="upLinkFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td class="rightBlueTxt" width=""><span class="chnSpan2">@Performance.macFlow@:</span></td>
							<td><input id="macFlow" name="macFlow" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td><input id="macFlowEnable" name="macFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td class="rightBlueTxt" ><span class="chnSpan3">@Performance.channelSpeed@:</span></td>
							<td><input id="channelSpeed" name="channelSpeed" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td><input id="channelSpeedEnable" name="channelSpeedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						</tr>
						<tr class="groupContent">
							<td class="rightBlueTxt" colspan="3"><font color=red><span>@Performance.flowTip@</span></font></td>
							<td colspan=6></td>
						</tr>
					</tbody>
					<tbody>
						<tr class="groupTitle">
							<td colspan="9" class="groupTitle_open">
								<span class="spanWidth100">@Performance.signalQuality@</span>
								<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
							</td>
						</tr>
						<tr class="groupContent">
							<td class="rightBlueTxt">@Performance.snr@:</td>
							<td width="80"><input id="snr" name="snr" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td width="70"><input id="snrEnable" name="snrEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td class="rightBlueTxt vertical-middle">
								<img id="optLinkHelpPng3" src="/images/performance/Help.png" alt=""  class="cursorPointer" />
								<span class="chnSpan2">@Performance.ber@:</span>
							</td>
							<td width="80"><input id="ber" name="ber" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
							<td width="70"><input id="berEnable" name="berEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
							<td colspan="3"></td>
						</tr>
					</tbody>
			    <tbody class="opticalReceiverRead">
                    <tr class="groupTitle">
                        <td colspan="9" class="groupTitle_open">
                            <span class="spanWidth100">@Performance.optical@</span>
                            <span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
                        </td>
                    </tr>
                    <tr class="groupContent">
                        <td class="rightBlueTxt vertical-middle" >@Performance.optReceiverPower@:</td>
                        <td><input id="opticalReceiver" name="opticalReceiver" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
                        <td><input id="opticalReceiverEnable" name="opticalReceiverEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
                        <td colspan="6"></td>
                    </tr>
                </tbody>
					<tbody>
					<tr class="groupTitle">
						<td colspan="9" class="groupTitle_open">
							<span class="spanWidth100">CM FLAP</span>
							<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
						</td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt">
							<img id="optLinkHelpPng4" src="/images/performance/Help.png" alt=""  class="cursorPointer" />
							FLAP:
						</td>
						<td width="80"><input id="cmflap" name="cmflap" class="numberInput normalInput w50" maxlength="4"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="cmflapEnable" name="cmflapEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="6"></td>
					</tr>
				</tbody>
				</table>
		</div>
	</form>
	
	<div class="edge10">
		<div class="yellowTip" id="yellowTip">
			<div id="yellowTip1">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.optLinkDetailCC@</b></p>
				<p>
					<span class="spanWidth150">1.@Performance.optTxPower@</span>
					<span class="spanWidth150">2.@Performance.optRePower@</span>
					<span class="spanWidth150">3.@Performance.optCurrent@</span>
					<span class="spanWidth150">4.@Performance.optVoltage@</span>
					<span class="spanWidth150">5.@Performance.optTemp@</span>
				</p>
			</div>
			<div id="yellowTip2" class="pT10">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.moduleTempDetail@</b></p>
					<p>
						<span class="spanWidth200">1.@Performance.usTemp@</span>
						<span class="spanWidth200">2.@Performance.dsTemp@</span>
						<span class="spanWidth200">3.@Performance.insideTemp@</span>
						<span class="spanWidth200">4.@Performance.outsideTemp@</span>
						<!-- <span class="spanWidth150">5.@Performance.powerTemp@</span> -->
					</p>
			</div>
			<div id="yellowTip3" class="pT10">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.berDetail@</b></p>
				<p>
					<span class="spanWidth150">1.@Performance.ccer@</span>
					<span class="spanWidth150">2.@Performance.ucer@</span>
				</p>
			</div>
			<div id="yellowTip4" class="pT10">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.flapDetail@</b></p>
				<p>
					<span class="spanWidth150">1.@Performance.onlineCounter@</span>
					<span class="spanWidth150">2.@Performance.failOnlineCounter@</span>
					<span class="spanWidth150">3.@Performance.powadjgrowth@</span>
					<span class="spanWidth150">4.@Performance.rangingPercent@</span>
				</p>
			</div>
		</div>
	</div>
</div>
</body>
</Zeta:HTML>