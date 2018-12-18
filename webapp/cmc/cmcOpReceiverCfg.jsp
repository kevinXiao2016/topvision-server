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
    module cmc
</Zeta:Loader>
<head>
<script>
var cmcId = ${cmcId};
var opCfgJson = ${opCfgJson};
$(function(){
	//下行链路开关
	$('#outputControl').val(opCfgJson.outputControl);
	//射频衰减量
	$('#outputRFlevelatt').val(opCfgJson.outputRFlevelatt);
	//AGC起控光功率
	$('#outputAGCOrigin').val(opCfgJson.outputAGCOrigin);
	//判断光机类型，给出不同的提示
	if(opCfgJson.dorType==1){
		//如果是A类光机(迈威)
		$('#outputRFlevelatt').attr('tooltip', '@CMC.optical.range1@');
		$('#outputAGCOrigin').attr('tooltip', '@CMC.optical.range2@');
	}else if(opCfgJson.dorType==2){
		//如果是B类光机(万隆)
		$('#outputRFlevelatt').attr('tooltip', '@CMC.optical.range3@');
		$('#outputAGCOrigin').attr('tooltip', '@CMC.optical.range4@');
	}
})

function saveConfig(){
	//获取配置值
	var outputControl = $('#outputControl').val();
	var outputRFlevelatt = $('#outputRFlevelatt').val();
	var outputAGCOrigin = $('#outputAGCOrigin').val();
	
	//校验
	if(!validate()) return;
	
	var data = {
		outputControl: outputControl,
		outputRFlevelatt: outputRFlevelatt,
		outputAGCOrigin: outputAGCOrigin,
		outputIndex: opCfgJson.outputIndex
	}
	
	window.top.showOkCancelConfirmDlg("@COMMON.tip@", "@CMC.tip.sureToModifyConfig@", function (type) {
        if(type=="ok"){
            window.top.showWaitingDlg("@COMMON.waiting@", "@text.configuring@", 'ext-mb-waiting');
            $.ajax({
              url: '/cmc/optical/modifyOpticalReceiverCfg.tv?cmcId='+cmcId,
              type: 'post',
              data: data,
              success: function(response) {
                    if(response == "success"){                  
                        top.afterSaveOrDelete({
                            title: '@COMMON.tip@',
                            html: '<b class="orangeTxt">@text.modifySuccessTip@</b>'
                        });
                        if(window.top.frames['frameentity-'+cmcId]){
	                        window.top.frames['frameentity-'+cmcId].loadBbData();
                        }
                        cancelClick();
                     }else{
                         window.parent.showMessageDlg("@COMMON.tip@", "@text.modifyFailureTip@");
                     }
                }, error: function(response) {
                    window.parent.showMessageDlg("@COMMON.tip@", "@text.modifyFailureTip@");
                }, cache: false
            });
            }
        });
	
	function validate(){
		var reg = /^-?\d{1,2}$/;
		if(!reg.test(outputRFlevelatt)){
			$('#outputRFlevelatt').focus();
			return false;
		}
		if(!reg.test(outputAGCOrigin)){
			$('#outputAGCOrigin').focus();
			return false;
		}
		outputRFlevelatt = parseInt(outputRFlevelatt);
		outputAGCOrigin = parseInt(outputAGCOrigin);
		if(opCfgJson.dorType==1){
			//如果是A类光机(迈威)
			//射频衰减可设置范围为0 ~ 14dB
			if(isNaN(outputRFlevelatt) || outputRFlevelatt < 0 || outputRFlevelatt > 14){
				$('#outputRFlevelatt').focus();
				return false;
			}
			//AGC起始光功率值可设置范围为 -7 ~ +2dB
			if(isNaN(outputAGCOrigin) || outputAGCOrigin < -7 || outputAGCOrigin > 2){
				$('#outputAGCOrigin').focus();
				return false;
			}
			return true;
		}else if(opCfgJson.dorType==2){
			//如果是B类光机(万隆)
			//射频衰减可设置范围为0 ~ 10dB
			if(isNaN(outputRFlevelatt) || outputRFlevelatt < 0 || outputRFlevelatt > 10){
				$('#outputRFlevelatt').focus();
				return false;
			}
			//AGC起始光功率值可设置范围为 -9 ~ -6dB
			if(isNaN(outputAGCOrigin) || outputAGCOrigin < -9 || outputAGCOrigin > -6){
				$('#outputAGCOrigin').focus();
				return false;
			}
			return true;
		}
	}
}
function cancelClick(){
    window.parent.closeWindow('cmcOpReceiverCfg');
} 
<%-- var operationDevicePower= <%=uc.hasPower("operationDevice")%>;


$(function (){
    initData();
    authLoad();
});

//初始化数据
function initData(){
    $("input[name='cmcOpReceiverCfg.outputGainType']").each(function(){
        if(this.value == opCfgJson.outputGainType){
            this.checked = true;
        }
    });
    for(var v in opCfgJson){
        $("#" + v).val(opCfgJson[v]);
    }
}
//权限控制
function authLoad(){
    if(!operationDevicePower){
        $(":input").attr("disabled",true);
        $("#closeButton").attr("disabled",false);
        
    }
}
//修改配置
function okClick(){
    window.top.showOkCancelConfirmDlg("@COMMON.tip@", "@CMC.tip.sureToModifyConfig@", function (type) {
        if(type=="ok"){
            window.top.showWaitingDlg("@COMMON.waiting@", "@text.configuring@", 'ext-mb-waiting');
            $.ajax({
              url: '/cmc/optical/modifyOpticalReceiverCfg.tv?cmcId='+cmcId,
              type: 'post',
              data: jQuery("#formChanged").serialize(),
              success: function(response) {
                    if(response == "success"){                  
                        window.parent.showMessageDlg("@COMMON.tip@", "@text.modifySuccessTip@");
                        cancelClick();
                     }else{
                         window.parent.showMessageDlg("@COMMON.tip@", "@text.modifyFailureTip@");
                     }
                }, error: function(response) {
                    window.parent.showMessageDlg("@COMMON.tip@", "@text.modifyFailureTip@");
                }, cache: false
            });
            }
        });
}
--%>
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edge10">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="200">@CMC.optical.linkSwitch@@COMMON.maohao@</td>
					<td>
						<select class="normalSel w200" id="outputControl">
							<option value="2">@CMC.optical.open@</option>
							<option value="1">@CMC.optical.close@</option>
						</select>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@CMC.optical.levelAttenuation@(dB)@COMMON.maohao@</td>
					<td>
						<input id="outputRFlevelatt" type="text" class="normalInput w200" tooltip=""/>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CMC.optical.agcOrigin@(dBm)@COMMON.maohao@</td>
					<td>
						<input id="outputAGCOrigin" type="text" class="normalInput w200" tooltip=""/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
         <ol class="upChannelListOl pB0 pT20 noWidthCenter">
             <li><a id="cmcSubmit" onclick="saveConfig()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
             <li><a id="closeButton" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
         </ol>
    </div>
    <%-- <div class="edge10">
    <div class="zebraTableCaption">
       <div class="zebraTableCaptionTitle"><span>@CMC.optical.rfConfig@
       </span></div>
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="300">
                            <label for="cmcMDDTime">@CMC.optical.gainType@:</label>
                        </td>
                        <td>
                            <input type="radio" name="cmcOpReceiverCfg.outputGainType" value="1" />@CMC.optical.constantLevel@
                            <input type="radio" name="cmcOpReceiverCfg.outputGainType" value="2" />@CMC.optical.constantGain@
                            
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt">
                            @CMC.optical.agcOrigin@:
                        </td>
                        <td>
                            <input style="width: 150px;" id="outputAGCOrigin" class="normalInput"
                                name="cmcOpReceiverCfg.outputAGCOrigin" maxlength="10" />(dBm)
                        </td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt">@CMC.optical.levelAttenuation@:
                        </td>
                        <td>
                            <input style="width: 150px;" id="outputRFlevelattUnit" class="normalInput"
                                name="cmcOpReceiverCfg.outputRFlevelattUnit"  maxlength="10" />(dB)
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="edge10">
        <div class="zebraTableCaption">
            <div class="zebraTableCaptionTitle"><span>@CMC.optical.abSwitchConfig@
            </span></div>
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="300">
                            <label for="cmcMDDTime">@CMC.optical.switchControl@:</label>
                        </td>
                        <td>
                            <select id="switchControl" class="normalSel w150" name="cmcOpReceiverCfg.switchControl" >
                                <option value="1">@CMC.optical.forcePath@A</option>
                                <option value="2">@CMC.optical.forcePath@B</option>
                                <option value="3">@CMC.optical.preferPath@A</option>
                                <option value="4">@CMC.optical.preferPath@B</option>
                                <option value="5">@CMC.optical.noPrefer@</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt">@CMC.optical.changeThreshold@:
                        </td>
                        <td>
                            <input style="width: 150px;" id="switchThres" class="normalInput"
                                name="cmcOpReceiverCfg.switchThres" maxlength="10" />(dBm)
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
             <ol class="upChannelListOl pB0 pT60 noWidthCenter">
                 <li><a id="cmcSubmit" onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@COMMON.save@</span></a></li>
                 <li><a id="closeButton" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span>@COMMON.close@</span></a></li>
             </ol>
        </div>
    </div> --%>
</body>
</Zeta:HTML>