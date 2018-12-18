<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript">
var _RELAYMODE_ = {snooping: 0, l2url: 1, l3url: 2}
var ACTION_CREATE = 1;
var ACTION_MODIFY = 2;
var relayMode = 3;
var entityId = ${entityId};
var cmcId = ${cmcId};
var bundleConfig = ${bundleConfig};
var bundleEndList = ${bundleEndList};
var action = ${action};
var bundleInterface;
var cableSourceVerify;
var policy;
var bundleNum;
/***************************************************************************
 *
 * 初始化
 * 
 ***************************************************************************/
/**
 * 初始化页面数据
 */
function initData(){
    //若不是第一次载入页面，则从window对象中获取保存的数据
    if(window.parent.dhcpRelay && window.parent.dhcpRelay.bundle){
        bundleConfig = window.parent.dhcpRelay.bundle;
    }
    //初始化数据
    if(bundleConfig && bundleConfig.topCcmtsDhcpBundleInterface){
        bundleInterface = bundleConfig.topCcmtsDhcpBundleInterface;
        policy = bundleConfig.topCcmtsDhcpBundlePolicy;
        cableSourceVerify = bundleConfig.cableSourceVerify;
    }else{
        bundleInterface = initBundleIndexInputValue(bundleEndList);
    }
    bundleNum = transferBundleToNumber(bundleInterface);
}
/**
 * 初始化页面表单的值
 */
function initPage(){
    $(":radio[name='cableSourceVerify']").each(function (){
        if(this.value == cableSourceVerify){
            this.checked = true;
        }
    });
    
    $("#relayConfigIndex").val(bundleNum);
    $("#policySelect").val(policy);
    if(action == ACTION_CREATE){
        $("#relayConfigIndex").attr("disabled", false);
    }else{
        $("#relayConfigIndex").attr("disabled", true);
    }
}
/**
 * 获取未使用的bundle的值 ,必须保证list为排好顺序的
 */
function initBundleIndexInputValue(list){
    var result;
    if(list){
        var index = list.length;
        var begin = ["1","0"];
        for(var i = 0; i < list.length; i++){
            var compare = list[i].split(".");
            if(compare.length<2){
                compare.push("0");
            }
            if(parseInt(begin[1]) < parseInt(compare[1])){
                break;
            }
            begin[1] = parseInt(compare[1]) + 1;
        }
        if(begin[1] == "0"){
            result = 1;
        }else{
            result = begin[0]+"."+ begin[1];
        }       
    }else{
        result = 1;
    }
    return "bundle" + result;
}
/*****************************************************************************************
 * 数据校验
 *****************************************************************************************/
//验证输入的Bundle ID格式正确
function isBundleIdInput(val){
    var reg = /^[1-3]\d{1}$|^[1-9]$/;
    return reg.test(val);
}
//验证输入的Bundle ID已经存在，若存在则返回true
function isExitsInList(val){
    for(var i = 0; i < bundleEndList.length; i++){
        if(val == bundleEndList[i]){
            return true;
        }
    }
    return false;
}
//转换Bundle号为数字
function transferBundleToNumber(val){
    if(typeof val == "string"){
        var bundleArray = val.replace("bundle", "").split(".");
        var number;
        if(bundleArray.length > 1){
            number = parseInt(bundleArray[0]) + parseInt(bundleArray[1]);
        }else{
            number = parseInt(bundleArray[0]);
        }
        return number;
    }else{
        return val;
    }
    
}
//将数字转换为Bundle号
function transferNumberToBundle(number){
    var bundle = "bundle1";
    if(number > 1){
        bundle += "." + (number-1);
    }
    return bundle;
}
/*****************************************************************************************
 * 按钮事件方法
 *****************************************************************************************/
function nextClick(){ 
    clickPrepare();
    window.location.assign("/cmc/dhcprelay/showDhcpRelayConfigGiAddrStep.tv?cmcId=" + cmcId + "&entityId=" + entityId+
            "&action=" + action + "&bundleInterface=" + bundleInterface);
}
//在页面点击下一步时，保存页面配置到window对象中
function clickPrepare(){
    cableSourceVerify = $(":radio[name='cableSourceVerify'][checked=true]").val();
    policy = $("#policySelect").val();
    bundleNum = $("#relayConfigIndex").val();
    bundleInterface = transferNumberToBundle(bundleNum);
    var bundle = {
            cmcId: cmcId,
            topCcmtsDhcpBundleInterface: bundleInterface,
            cableSourceVerify: cableSourceVerify,
            topCcmtsDhcpBundlePolicy: policy
    };
    if(!window.parent.dhcpRelay){
        window.parent.dhcpRelay = {};
    }
    window.parent.dhcpRelay.bundle = bundle;
}
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function onBundleIdChange(val){
    var disabled = true;
    if(isBundleIdInput(val) && !isExitsInList(transferNumberToBundle(val))){
        if(val>=1 && val <= 32){
          disabled = false;
        }
    }
    $("#nextStep").attr("disabled", disabled);
}
$(function (){
    initData();
    initPage();
})
</script>
</head>
<body class="openWinBody">
    <div class="openWinHeader">
        <div class="openWinTip">
            <p><b class="orangeTxt">@CMC.label.dhcpRelayConfig@</b></p>
            <p><span id="newMsg">@CMC.text.dhcpRelayPolicy_cable@</span></p>
        </div>
        <div class="rightCirIco wheelCirIco"></div>
    </div>
    <div class="edgeTB10LR20 pT40">
         <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
             <tbody>
                 <tr>
                     <td class="rightBlueTxt" width="200">
                        <label for="relayConfig">Bundle:</label>
                     </td>
                     <td width="210">
                        <input style="width:200px" id="relayConfigIndex" maxlength=36 class="normalInput"
                            toolTip='1-32'  onkeyup="onBundleIdChange(this.value)" />
                     </td>
                     <td>
                        <span id=nameSpan >@CMC.text.dhcpBundleIdSet@</span>
                     </td>
                 </tr>
                 <tr class="darkZebraTr">
                     <td class="rightBlueTxt">
                        <label for="name">@CMC.label.dhcpRelayPolicy@</label>
                     </td>
                     <td>
                        <select style="width:200px" id="policySelect" class="normalSel">
                            <option value=1 selected>primary</option>
                            <option value=2>policy</option>
                            <option value=3>strict</option>
                        </select>
                     </td>
                     <td>
                        <span id=nameSpan >@CMC.text.dhcpGiAddrPolicySetting@</span>
                     </td>
                 </tr>
                 <tr>
                     <td class="rightBlueTxt">
                        <label for="cableSourceVerify">@CMC.label.dhcpCableSourceVerify@: </label>
                     </td>
                     <td colspan="2">
                         <input name='cableSourceVerify' type="radio" value=1 checked /><span class="mR10">@CMC.select.open@</span>
                         <input name='cableSourceVerify' type="radio" value=2 />@CMC.select.close@  
                     </td>
                 </tr>
             </tbody>
         </table>
        <div class="noWidthCenterOuter clearBoth" id="sequenceStep">
             <ol class="upChannelListOl pB20 pT10 noWidthCenter">
                 <li><a id="nextStep"  onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i>@CMC.button.nextStep@</span></a></li>
                 <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@CMC.button.cancel@</span></a></li>
             </ol>
        </div>
        <div class="yellowTip">
            @CMC.text.dhcpRelayPolicyDesc@
        </div>
    </div>
</body>
</Zeta:HTML>