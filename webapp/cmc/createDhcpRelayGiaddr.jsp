<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}
#sequenceStep {
    padding-left : 380;position:absolute;top:390px;
}
.display-none{
    display: none;
}
</style>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _RELAY_MODE_ = {snooping: 0, l2: 1, l3: 2};
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6, keep: 2 };
var entityId = ${entityId};
var cmcId = ${cmcId};
var action = ${action};
var giAddrList = ${giAddrArray};
var packetVlan = ${packetVlanArray};
//该bundle的int IP配置
var virtualIpList = ${intIpArray};
//设备的int IP配置，用于冲突检测
var intIpList = ${cmcDhcpIntIpList};
var cmcDhcpBaseConfig = ${cmcDhcpBaseConfig};
var bundleConfig = ${bundleConfig};
var existL3Mode = false;
var existL2SnoopMode = false;
var policy;
var vlan = [0,0,0,0];
var priority = [0,0,0,0];
var preClickUrl;
var bundleInterface = '${bundleInterface}';
var giAddrListTemp = [];
/**********************************************************************************
 * 初始化方法
 **********************************************************************************/
function initData(){
    existL3Mode = cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3 || cmcDhcpBaseConfig.hostMode == _RELAY_MODE_.l3 ||
                  cmcDhcpBaseConfig.mtaMode == _RELAY_MODE_.l3 || cmcDhcpBaseConfig.stbMode == _RELAY_MODE_.l3;
    existL2SnoopMode = cmcDhcpBaseConfig.cmMode != _RELAY_MODE_.l3 || cmcDhcpBaseConfig.hostMode != _RELAY_MODE_.l3 ||
                       cmcDhcpBaseConfig.mtaMode != _RELAY_MODE_.l3 || cmcDhcpBaseConfig.stbMode != _RELAY_MODE_.l3;
    if(window.parent.dhcpRelay.virtualIpList){
        virtualIpList = window.parent.dhcpRelay.virtualIpList;
    }
    if(window.parent.dhcpRelay.primaryIpGiAddr){
        bundleConfig = window.parent.dhcpRelay.primaryIpGiAddr;
    }
    if(window.parent.dhcpRelay.giAddrList){
        giAddrList = window.parent.dhcpRelay.giAddrList;
    }
    if(window.parent.dhcpRelay.packetVlanList){
        packetVlan = window.parent.dhcpRelay.packetVlanList;
    }
    for(var i = 0; i < packetVlan.length; i++){
        var index = packetVlan[i].topCcmtsDhcpDeviceType - 1
        vlan[index] = packetVlan[i].topCcmtsDhcpTagVlan;
        priority[index] = packetVlan[i].topCcmtsDhcpTagPriority;
    }
    policy = window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy;
}
function initPage(){
    initIpInput();
    initGiaddrConfig(policy);
    initPacketVlan();
    initPageValue();
}
//初始化IP输入框
function initIpInput(){
    if(!existL3Mode){
        return ;
    }
    var primaryGiAddr = new ipV4Input("primaryGiAddr","span_primaryGiAddr");
    primaryGiAddr.width(141);
    var primaryGiMask = new ipV4Input("primaryGiMask","span_primaryGiMask");
    primaryGiMask.width(141);
    var cmGiaddr = new ipV4Input("cmGiaddr","span_cmGiaddr");
    cmGiaddr.width(141);
    var cmGimask = new ipV4Input("cmGimask","span_cmGimask");
    cmGimask.width(141);
    var hostGiaddr = new ipV4Input("hostGiaddr","span_hostGiaddr");
    hostGiaddr.width(141);
    var hostGiMask = new ipV4Input("hostGiMask","span_hostMask");
    hostGiMask.width(141);
    var mtaGiaddr = new ipV4Input("mtaGiaddr","span_mtaGiaddr");
    mtaGiaddr.width(141);
    var mtaGiMask = new ipV4Input("mtaGiMask","span_mtaMask");
    mtaGiMask.width(141);
    var stbGiaddr = new ipV4Input("stbGiaddr","span_stbGiaddr");
    stbGiaddr.width(141);
    var stbGiMask = new ipV4Input("stbGiMask","span_stbMask");
    stbGiMask.width(141);
}
//初始化Giaddr输入显示
function initGiaddrConfig(relayPolicy){
    if(!existL3Mode){
        $("#l3Relay-fieldset").addClass("display-none");
        $("#nextStep").attr("disabled", false);
    }else{
        if(relayPolicy == _POLICY_.primary){
            $("#all-giaddr-tr").removeClass("display-none");
        }else if(relayPolicy == _POLICY_.policy){
            $("#cm-giaddr-tr").removeClass("display-none");
        }else{
            if(cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3){
                $("#cm-giaddr-tr").removeClass("display-none");
            }
            if(cmcDhcpBaseConfig.hostMode == _RELAY_MODE_.l3){
                $("#strictHost").removeClass("display-none");
            }
            if(cmcDhcpBaseConfig.mtaMode == _RELAY_MODE_.l3){
                $("#strictMta").removeClass("display-none");
            }
            if(cmcDhcpBaseConfig.stbMode == _RELAY_MODE_.l3){
                $("#strictStb").removeClass("display-none");
            }
        }        
        
    }
}
//初始化PacketVlan输入框显示
function initPacketVlan(){
    if(!existL2SnoopMode){
        $("#l2Relay-fieldset").addClass("display-none");
    }else{
        if(cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3){
            $("#cm_td").addClass("display-none");
        }else{
            $("#cm_td").removeClass("display-none");
        }
        if(cmcDhcpBaseConfig.hostMode == _RELAY_MODE_.l3){
            $("#host_td").addClass("display-none");
        }else{
            $("#host_td").removeClass("display-none");
        }
        if(cmcDhcpBaseConfig.mtaMode == _RELAY_MODE_.l3){
            $("#mta_td").addClass("display-none");
        }else{
            $("#mta_td").removeClass("display-none");
        }
        if(cmcDhcpBaseConfig.stbMode == _RELAY_MODE_.l3){
            $("#stb_td").addClass("display-none");
        }else{
            $("#stb_td").removeClass("display-none");
        }  
    }
}
function initPageValue(){    
    if(policy == _POLICY_.primary){
        if(bundleConfig){
            setIpValue("primaryGiAddr", bundleConfig.virtualPrimaryIpAddr);
            setIpValue("primaryGiMask", bundleConfig.virtualPrimaryIpMask);
        }
    }else if(policy == _POLICY_.policy){
        if(bundleConfig){
            setIpValue("cmGiaddr", bundleConfig.virtualPrimaryIpAddr);
            setIpValue("cmGimask", bundleConfig.virtualPrimaryIpMask);
        }
    }else{
        if(giAddrList){
            for(var i = 0; i < giAddrList.length; i++){
                var giaddr = giAddrList[i];
                if(giaddr && giaddr.topCcmtsDhcpGiAddrDeviceType){
                    switch(giaddr.topCcmtsDhcpGiAddrDeviceType){
                    case 1:
                        setIpValue("cmGiaddr", giaddr.topCcmtsDhcpGiAddress);
                        setIpValue("cmGimask", giaddr.topCcmtsDhcpGiAddrMask);
                        break;
                    case 2:
                        setIpValue("hostGiaddr", giaddr.topCcmtsDhcpGiAddress);
                        setIpValue("hostGiMask", giaddr.topCcmtsDhcpGiAddrMask);
                        break;
                    case 3:
                        setIpValue("mtaGiaddr", giaddr.topCcmtsDhcpGiAddress);
                        setIpValue("mtaGiMask", giaddr.topCcmtsDhcpGiAddrMask);
                        break;
                    case 4:
                        setIpValue("stbGiaddr", giaddr.topCcmtsDhcpGiAddress);
                        setIpValue("stbGiMask", giaddr.topCcmtsDhcpGiAddrMask);
                        break;
                    }
                }
            }   
        }
    }
    var i = 0;
    $("#vlan_table select").each(function (){
        this.value = priority[i];
        i++;
    });
    i = 0;
    $("#vlan_table  input").each(function (){
        if( vlan[i] != 0){
            this.value = vlan[i];
        }        
        i++;
    });
}
/**
 * 改变页面时准备数据
 */
function changePagePrepare(){
    giAddrDataPrepare();
    packetVlanDataPrepare();
}
/**
 * Giaddr数据准备
 */
function giAddrDataPrepare(){
    var primaryIp = null;
    if(policy == _POLICY_.primary){
        var primaryAddr = getIpValue("primaryGiAddr");
        var primaryMask = getIpValue("primaryGiMask");
        if(checkedIpValue(primaryAddr) && primaryMask != "0.0.0.0" && primaryAddr != "..."){
            primaryIp = {
                    virtualPrimaryIpAddr: primaryAddr,
                    virtualPrimaryIpMask: primaryMask
            };
        }
    }else if(policy == _POLICY_.policy){
        var policyAddr = getIpValue("cmGiaddr");
        var policyMask = getIpValue("cmGimask");
        if(checkedIpValue(policyAddr) && policyAddr != "0.0.0.0" && primaryAddr != "..."){
            primaryIp = {
                    virtualPrimaryIpAddr: policyAddr,
                    virtualPrimaryIpMask: policyMask
            };
        }     
        
    }else{
        giAddrListTemp = [];
        //CM
        var cmGiAddr;
        var giAddress = getIpValue("cmGiaddr");
        var giAddrMask = getIpValue("cmGimask");
        cmGiAddr = {
                topCcmtsDhcpGiAddress: giAddress,
                topCcmtsDhcpGiAddrMask: giAddrMask,
                topCcmtsDhcpGiAddrDeviceType: 1
        }
        cmGiAddr = generateGiaddr(cmGiAddr);
        if(cmGiAddr != null){
            giAddrListTemp.push(cmGiAddr);
        }
        //host
        var hostGiAddr = {
                topCcmtsDhcpGiAddrDeviceType: 2,
                topCcmtsDhcpGiAddress: getIpValue("hostGiaddr"),
                topCcmtsDhcpGiAddrMask: getIpValue("hostGiMask")
        };
        hostGiAddr = generateGiaddr(hostGiAddr);
        if(hostGiAddr != null){
            giAddrListTemp.push(hostGiAddr);
        }
        //mta
        var mtaGiAddr = {
                topCcmtsDhcpGiAddrDeviceType: 3,
                topCcmtsDhcpGiAddress: getIpValue("mtaGiaddr"),
                topCcmtsDhcpGiAddrMask: getIpValue("mtaGiMask")
        };
        mtaGiAddr = generateGiaddr(mtaGiAddr);
        if(mtaGiAddr != null){
            giAddrListTemp.push(mtaGiAddr);
        }        
        //stb  
        var stbGiAddr = {
                topCcmtsDhcpGiAddrDeviceType: 4,
                topCcmtsDhcpGiAddress: getIpValue("stbGiaddr"),
                topCcmtsDhcpGiAddrMask: getIpValue("stbGiMask")
        };
        stbGiAddr = generateGiaddr(stbGiAddr);
        if(stbGiAddr != null){
            giAddrListTemp.push(stbGiAddr);
        } 
    }
    if(existL3Mode){
        window.parent.dhcpRelay.primaryIpGiAddr = primaryIp;
    }    
    window.parent.dhcpRelay.giAddrList = giAddrListTemp;
    //由于进行设置时，需要使用IP、掩码、类型的数组进行设置，因此将每种中继的IP、掩码、类型也保存到window中
    var giAddrIpArray = [];
    var giAddrMaskArray = [];
    var giAddrTypeArray = [];
    for(var i = 0; i < giAddrListTemp.length; i ++){
        giAddrIpArray.push(giAddrListTemp[i].topCcmtsDhcpGiAddress);
        giAddrMaskArray.push(giAddrListTemp[i].topCcmtsDhcpGiAddrMask);
        giAddrTypeArray.push(giAddrListTemp[i].topCcmtsDhcpGiAddrDeviceType);
    }
    
    window.parent.dhcpRelay.giAddrIpArray = giAddrIpArray;
    window.parent.dhcpRelay.giAddrMaskArray = giAddrMaskArray;
    window.parent.dhcpRelay.giAddrTypeArray = giAddrTypeArray;
}
/**
 * Packet VLAN 数据准备
 */
function packetVlanDataPrepare(){
    var i = 0;
    $("#vlan_table select").each(function (){
        priority[i] = this.value;
        i++;
    });
    i = 0;
    $("#vlan_table input").each(function (){
        if(this.value){
            vlan[i] = this.value;
        }else{
            vlan[i] = 0;
        }
        i++;
    });        
    if(packetVlan && packetVlan.length > 0){
        for(var i  = 0; i < packetVlan.length; i++){
            packetVlan[i].topCcmtsDhcpTagVlan = vlan[packetVlan[i].topCcmtsDhcpDeviceType-1];
            packetVlan[i].topCcmtsDhcpTagPriority = priority[packetVlan[i].topCcmtsDhcpDeviceType-1];
        }
    }else{
        for(var i = 0; i < vlan.length; i++){
            var packet = {};
            packet.topCcmtsDhcpTagVlan = vlan[i];
            packet.topCcmtsDhcpTagPriority = priority[i];
            packet.topCcmtsDhcpDeviceType = i + 1;
            packetVlan.push(packet);
        }
    }
    window.parent.dhcpRelay.vlan = vlan;
    window.parent.dhcpRelay.priotity = priority;
    window.parent.dhcpRelay.packetVlanList = packetVlan;
}
//获取页面载入时的Giaddr地址
function getOldGiAddr(type){
    var giAddr = null;
    for(var i = 0; i < giAddrList.length; i ++){
        if(giAddrList[i].topCcmtsDhcpGiAddrDeviceType == type){
            giAddr = giAddrList[i].topCcmtsDhcpGiAddress;
        }
    }
    if(giAddr != null && (giAddr.trim() == "" || giAddr == "0.0.0.0")){
        giAddr = null;
    }
    return giAddr;
}
//根据giAddr对象生成新的giAddr对象，加入页面操作内容
function generateGiaddr(giAddrObj){
    var topCcmtsDhcpGiAddress = giAddrObj.topCcmtsDhcpGiAddress;
    var type = giAddrObj.topCcmtsDhcpGiAddrDeviceType;
    var oldGiAddress = getOldGiAddr(type);
    if((topCcmtsDhcpGiAddress != "" && topCcmtsDhcpGiAddress != "..." && topCcmtsDhcpGiAddress != "0.0.0.0") 
            && oldGiAddress == null){
        giAddrObj.modifyType = _MODIFY_TYPE_.add;
        giAddrObj.oldGiAddress = oldGiAddress;
    }else if((topCcmtsDhcpGiAddress == "" || topCcmtsDhcpGiAddress == "0.0.0.0" || topCcmtsDhcpGiAddress == "...") 
            && oldGiAddress != null){
        giAddrObj.modifyType = _MODIFY_TYPE_.del;
        giAddrObj.oldGiAddress = oldGiAddress;
    }else if((topCcmtsDhcpGiAddress != "" || topCcmtsDhcpGiAddress != "0.0.0.0" || topCcmtsDhcpGiAddress == "...") 
            && oldGiAddress != null){
        if(topCcmtsDhcpGiAddress != oldGiAddress){
            giAddrObj.modifyType = _MODIFY_TYPE_.modify;
        }else{
            giAddrObj.modifyType = _MODIFY_TYPE_.keep;
        }
        giAddrObj.oldGiAddress = oldGiAddress;
    }else{
        giAddrObj = null;
    }
    return giAddrObj;
}
/*******************************************************************************
 *数据校验 
 *******************************************************************************/
 
/**
 * 检查是否与IntIP冲突
 * ip:giaddr, mask: 掩码, isPrimary:是否为主IP，页面载入时的该设备类型的中继地址
 */
function isExistInIntIp(ip, mask, isPrimary, oldIp){
    var check = new IpAddrCheck(ip, mask);
    if(!isPrimary && (getIntIpIndex(ip) != -1 ||getIntIpIndex(oldIp) != -1)){
        return false;
    }
    if(intIpList){
        for(var i = 0; i < intIpList.length; i++){
            var initIp = intIpList[i];
            //如果是Pirmary IP，与本bundle的配置进行比较时则跳过
            if(isPrimary && initIp.topCcmtsDhcpBundleInterface == bundleInterface 
                    && (initIp.topCcmtsDhcpIntIpIndex == null || initIp.topCcmtsDhcpIntIpIndex == 0)){
                continue;
            }            
            if(check.isSubnetConflict(initIp.topCcmtsDhcpIntIpAddr, initIp.topCcmtsDhcpIntIpMask)){
                return true;
            }
        }
    }
    return false;
}
/**
 * 检查页面输入是否与存在的配置冲突
 */
function checkSubnetConflict(){
    var result = false;
    var ip;
    var mask;
    if(policy == _POLICY_.primary){
        ip = getIpValue("primaryGiAddr");
        mask = getIpValue("primaryGiMask");
        result = isExistInIntIp(ip, mask, true);
    }else if(policy == _POLICY_.policy){
        ip = getIpValue("cmGiaddr");
        mask = getIpValue("cmGimask");
        result = isExistInIntIp(ip, mask, true);
    }else{
        for(var i = 0; i< giAddrListTemp.length; i++){
            var ipTmp = giAddrListTemp[i].topCcmtsDhcpGiAddress;
            var maskTmp = giAddrListTemp[i].topCcmtsDhcpGiAddrMask;
            var oldTmp = giAddrListTemp[i].oldGiAddress;
            if(isExistInIntIp(ipTmp, maskTmp, false, oldTmp)){
                ip = ipTmp;
                mask = maskTmp;
                result = true;
                break;
            }
        }
    }
    if(result){
        window.parent.showMessageDlg("@COMMON.tip@", String.format("@CMC.tip.dhcpConflictWithOther@", ip + "/" + mask));
    }
    return result;
}
/**
 * 获取IP地址的数组位置，如果不存在返回-1
 */
function getIntIpIndex(ip){
    for(var i = 0; i < virtualIpList.length; i++){
        if(virtualIpList[i].topCcmtsDhcpIntIpAddr == ip){
            return i;
        }
    }
    return -1;
}
/**
 * 验证输入的值是否为Vlan的合法值
 * 返回值 :true : 合法 ，false :非法
 *
 */
function checkVlanValue(val){
    if(val==null || val.trim()=="" || !isNaN(val) && (val>=1 && val <=4094)){
        return true;
    }else{
        return false;
    }
}
/**
 * 验证VLAN 输入框
 */
function onVlanInputCheck(){
    var valid = true;
    $("#vlan_table input").each(function (){
        if(!checkVlanValue(this.value)){
            valid = false;
            $(this).focus();
        }
    });
    return valid;
}
/**
 * 验证输入的 主IP地址（CM的中继/中继地址）格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onPrimaryIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(!existL3Mode){
        return true;
    }
    if(policy == _POLICY_.primary){
        ip = getIpValue("primaryGiAddr");
        mask = getIpValue("primaryGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if(ipIsFilled("primaryGiAddr") && ipIsFilled("primaryGiMask") && 
                checkedIpMask(mask) && ipCheck.isHostIp()){
            valid =  true;
        }
    }else if(policy == _POLICY_.policy){
        ip = getIpValue("cmGiaddr");
        mask = getIpValue("cmGimask");
        ipCheck = new IpAddrCheck(ip, mask);
        if(ipIsFilled("cmGiaddr") && ipIsFilled("cmGimask") && checkedIpMask(mask)
                && ipCheck.isHostIp()){
            valid =  true;
        }
    }else{
        if(cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3){
            ip = getIpValue("cmGiaddr");
            mask = getIpValue("cmGimask");
            ipCheck = new IpAddrCheck(ip, mask);
            if(ipIsFilled("cmGiaddr") && ipIsFilled("cmGimask") && checkedIpMask(mask)
                    && ipCheck.isHostIp()){
                valid =  true;
            }
        }else{
            valid =  true;
        }
    }
    if(!valid){
        window.parent.showMessageDlg("@COMMON.tip@", ip + "/" + mask + "@route.ipFormatError@");
    }
    return valid;
}
/**
 * 验证输入的 HOST的中继地址格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onHostIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(policy == _POLICY_.strict){
        ip = getIpValue("hostGiaddr");
        mask = getIpValue("hostGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if((getIpValue("hostGiaddr") == "" && getIpValue("hostGiMask") == "") || 
                (ipIsFilled("hostGiaddr") && ipIsFilled("hostGiMask") && checkedIpMask(getIpValue("hostGiMask")) 
                        && ipCheck.isHostIp())){
            valid =  true;
        }
    }else{
        valid =  true;
    }
    if(!valid){
        window.parent.showMessageDlg("@COMMON.tip@", ip + "/" + mask + "@route.ipFormatError@");
    }
    return valid;
}
/**
 * 验证输入的 MTA的中继地址格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onMtaIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(policy == _POLICY_.strict){
        ip = getIpValue("mtaGiaddr");
        mask = getIpValue("mtaGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if((getIpValue("mtaGiaddr") == "" && getIpValue("mtaGiMask") == "") || 
                (ipIsFilled("mtaGiaddr") && ipIsFilled("mtaGiMask") && checkedIpMask(getIpValue("mtaGiMask"))
                        && ipCheck.isHostIp())){
            valid =  true;
        }
    }else{
        valid =  true;
    }
    if(!valid){
        window.parent.showMessageDlg("@COMMON.tip@", ip + "/" + mask + "@route.ipFormatError@");
    }
    return valid;
}
/**
 * 验证输入的 STB的中继地址格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onStbIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(policy == _POLICY_.strict){
        ip = getIpValue("stbGiaddr");
        mask = getIpValue("stbGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if((getIpValue("stbGiaddr") == "" && getIpValue("stbGiMask") == "") || 
                (ipIsFilled("stbGiaddr") && ipIsFilled("stbGiMask") && checkedIpMask(getIpValue("stbGiMask"))
                        && ipCheck.isHostIp())){
            valid =  true;
        }
    }else{
        valid =  true;
    }
    if(!valid){
        window.parent.showMessageDlg("@COMMON.tip@", ip + "/" + mask + "@route.ipFormatError@");
    }
    return valid;
}

function onIpAndMaskInputCheck(){
    if(!onPrimaryIpCheck()){
        return false;
    }
    if(!onHostIpCheck()){
        return false;
    }
    if(!onMtaIpCheck()){
        return false;
    }
    if(!onStbIpCheck()){
        return false;
    }
    if(checkSubnetConflict()){
        return false;
    }
    return true;
}
/********************************************************************************
 * 按钮事件动作
 ********************************************************************************/
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function preClick(){
    changePagePrepare();
    if(existL3Mode && !onIpAndMaskInputCheck()){
        return;
    }
    if(existL2SnoopMode && !onVlanInputCheck()){
        return;
    }
    
    window.location.assign('cmc/dhcprelay/showDhcpRelayConfigBaseStep.tv?entityId='+entityId + 
            '&cmcId=' + cmcId + '&action=' + action + "&bundleInterface=" + bundleInterface );
}

function nextClick(){
    changePagePrepare();
    if(existL3Mode && !onIpAndMaskInputCheck()){
        return;
    }
    if(existL2SnoopMode && !onVlanInputCheck()){
        return;
    }
    window.location.assign('cmc/dhcprelay/showDhcpRelayConfigIntIpStep.tv?entityId='+entityId + 
            '&cmcId=' + cmcId + '&action=' + action + "&bundleInterface=" + bundleInterface );
}
/**
 * 页面渲染
 */
Ext.onReady(function (){    
    initData();
    initPage();
});
</script>
</head>
<body class="openWinBody">
    <div class="openWinHeader">
        <div class="openWinTip">
            <p><b class="orangeTxt">@CMC.label.dhcpRelayConfig@</b></p>
            <p><span id="newMsg">@CMC.text.dhcpRelayL2L3Config@</span></p>
        </div>
        <div class="rightCirIco pageCirIco"></div>
    </div>  

    <div class=formtip id=tips style="display: none"></div>
    <div class="edge10 pT20">
        <div class="zebraTableCaption" id="l3Relay-fieldset">
            <div class="zebraTableCaptionTitle"><span>@CMC.text.dhcpL3RelayConfig@</span></div>
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                <tr id="all-giaddr-tr" class="display-none">
                    <td class="rightBlueTxt" width="160">
                        @CMC.label.dhcpGiaddr@:
                    </td>
                    <td width="141">
                        <span id="span_primaryGiAddr" ></span>
                    </td>
                    <td class="rightBlueTxt" width="50">
                        @CMC.label.iPMask@:
                    </td>
                    <td>
                       <span id="span_primaryGiMask" ></span>
                    </td>
                </tr>
                <tr id="cm-giaddr-tr" class="display-none">
                    <td class="rightBlueTxt" width="160">CM:</td>
                    <td width="141">
                       <span id="span_cmGiaddr" ></span>
                    </td>
                    <td class="rightBlueTxt" width="50">
                       @CMC.label.iPMask@:
                    </td>
                    <td>
                       <span id="span_cmGimask" ></span>
                    </td>
                </tr>
                <tr id="strictHost" class="display-none">
                    <td class="rightBlueTxt" width="160">HOST:</td>
                    <td>
                       <span id="span_hostGiaddr" ></span>
                    </td>
                    <td class="rightBlueTxt" width="50">
                       @CMC.label.iPMask@:
                    </td>
                    <td>
                       <span id="span_hostMask" ></span>
                    </td>
                </tr> 
                <tr id="strictMta" class="display-none">
                    <td class="rightBlueTxt" width="160">MTA:</td>
                    <td>
                       <span id="span_mtaGiaddr"></span>
                    </td>
                    <td class="rightBlueTxt" width="50">
                       @CMC.label.iPMask@:
                    </td>
                    <td>
                       <span id="span_mtaMask" ></span>
                    </td>
                </tr>   
                <tr id="strictStb" class="display-none">
                    <td class="rightBlueTxt" width="160">STB:</td>
                    <td>
                       <span id="span_stbGiaddr"></span>
                    </td>
                    <td class="rightBlueTxt" width="50">
                       @CMC.label.iPMask@:
                    </td>
                    <td>
                       <span id="span_stbMask"></span>
                    </td>
                </tr>  
                </tbody>     
            </table>
        </div>
        <div class="zebraTableCaption" id="l2Relay-fieldset">
            <div class="zebraTableCaptionTitle"><span>@CMC.text.dhcpL2RelayConfig@</span></div>
            <table id="vlan_table" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tbody>
                <tr id="cm_td" class="display-none">
                    <td class="rightBlueTxt" width="160">CM:</td>
                    <td width="141"><input style="width:141px" id=cmVlanTag maxlength=36 class="normalInput" toolTip="1-4094" />
                    </td>
                    <td class="rightBlueTxt" width="50">
                        @CMC.label.vlanPriority@:
                    </td>
                    <td>
                    <select style="width:141px" id="cmPriority">
                        <option value=0 >0</option>
                        <option value=1>1</option>
                        <option value=2>2</option>
                        <option value=3>3</option>
                        <option value=4>4</option>
                        <option value=5>5</option>
                        <option value=6>6</option>
                        <option value=7>7</option>
                    </select>
                    </td>
                </tr>
                <tr id="host_td" class="display-none" >
                     <td class="rightBlueTxt" width="160">HOST:</td>
                     <td width="141"><input style="width:141px" id=hostVlanTag maxlength=36 
                        class="normalInput" toolTip="1-4094"/>
                     </td>
                     <td class="rightBlueTxt" width="50">
                        @CMC.label.vlanPriority@:
                     </td>
                     <td>
                     <select style="width:141px" id="hostPriority">
                         <option value=0>0</option>
                         <option value=1>1</option>
                         <option value=2>2</option>
                         <option value=3>3</option>
                         <option value=4>4</option>
                         <option value=5>5</option>
                         <option value=6>6</option>
                         <option value=7>7</option>
                     </select>
                     </td>
                  </tr> 
                <tr id="mta_td" class="display-none">
                    <td class="rightBlueTxt" width="160">MTA:</td>
                    <td width="141"><input style="width:141px" id=mtaVlanTag maxlength=36 
                        class="normalInput" toolTip="1-4094"/>
                    </td>
                    <td class="rightBlueTxt" width="50">
                        @CMC.label.vlanPriority@:
                    </td>
                    <td>
                    <select style="width:141px" id="mtaPriority">
                        <option value=0>0</option>
                        <option value=1>1</option>
                        <option value=2>2</option>
                        <option value=3>3</option>
                        <option value=4>4</option>
                        <option value=5>5</option>
                        <option value=6>6</option>
                        <option value=7>7</option>
                    </select>
                    </td>
                 </tr>
                 <tr id="stb_td" class="display-none">
                     <td class="rightBlueTxt" width="160">STB:</td>
                     <td width="141"><input style="width:141px" id=stbVlanTag maxlength=36
                        class="normalInput" toolTip="1-4094"/>
                     </td>
                     <td class="rightBlueTxt" width="50">
                        @CMC.label.vlanPriority@:
                     </td>
                     <td>
                     <select style="width:141px" id="stbPriority">
                         <option value=0>0</option>
                         <option value=1>1</option>
                         <option value=2>2</option>
                         <option value=3>3</option>
                         <option value=4>4</option>
                         <option value=5>5</option>
                         <option value=6>6</option>
                         <option value=7>7</option>
                     </select>
                     </td>
                </tr> 
            </tbody>      
            </table>
        </div>
    </div>

    <div class="noWidthCenterOuter clearBoth" id=sequenceStep>
         <ol class="upChannelListOl pB0 pT10 noWidthCenter">
             <li><a  id=prevBt onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@CMC.button.preStep@</span></a></li>
             <li><a id="nextStep" onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i>@CMC.button.nextStep@</span></a></li>
             <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@CMC.button.cancel@</span></a></li>
         </ol>
    </div>
</body>
</Zeta:HTML>