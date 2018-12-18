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
<script type="text/javascript">
var _RELAY_MODE_ = {snooping: 0, l2: 1, l3: 2};
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6 };
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6 };
var entityId = ${entityId};
var cmcId = ${cmcId};
var action = ${action};
var bundleInterface = "${bundleInterface}";
var policyGrid = null;
var policyStore = null;
var policyGiaddr = {data: []};
var delIntIp = [];
var addIntIp = [];
var addVirtualIp = [];
var addVirtualIpMask = [];
//该bundle的int IP配置
var virtualIpList = ${intIpArray};
//所有的int Ip配置
var intIpList = ${cmcDhcpIntIpList};
var primaryIp;
var giAddrList;
/**
 * 初始化数据
 */
function initData(){
    primaryIp = window.parent.dhcpRelay.primaryIpGiAddr;
    giAddrList = window.parent.dhcpRelay.giAddrList;
    if(window.parent.dhcpRelay.virtualIpList){
        virtualIpList = window.parent.dhcpRelay.virtualIpList;
    }
    if(primaryIp){
        initPrimaryIp(primaryIp.virtualPrimaryIpAddr, primaryIp.virtualPrimaryIpMask);
    }
    if(giAddrList){
        initGiAddr(giAddrList);
    }
    policyGiaddr.data = virtualIpList;
}
/**
 * 初始化主IP数据
 */
function initPrimaryIp(ip, mask){
    var index = getPrimaryIpIndex();
    if(index == -1){
        var primaryIp = {};
        primaryIp.topCcmtsDhcpIntIpIndex = "0";
        primaryIp.topCcmtsDhcpIntIpAddr = ip;
        primaryIp.topCcmtsDhcpIntIpMask = mask;
        virtualIpList.unshift(primaryIp);
    }else{
        virtualIpList[index].topCcmtsDhcpIntIpAddr = ip;
        virtualIpList[index].topCcmtsDhcpIntIpMask = mask;
    }
}
/**
 * 初始化Giaddr数据
 */
function initGiAddr(giAddrList){
    var firstAdd = true;
    var index = -1;
    var intIP = null;
    for(var i = 0; i < giAddrList.length; i++){
        var giAddrObj = giAddrList[i];
        if(giAddrObj.modifyType == _MODIFY_TYPE_.add){
            if(firstAdd && getPrimaryIpIndex() == -1){
                primaryIp = {
                        virtualPrimaryIpAddr: giAddrObj.topCcmtsDhcpGiAddress,
                        virtualPrimaryIpMask: giAddrObj.topCcmtsDhcpGiAddrMask
                };
                initPrimaryIp(primaryIp.virtualPrimaryIpAddr, primaryIp.virtualPrimaryIpMask);
                firstAdd = false;
            }else{
                index = getIntIpIndex(giAddrObj.topCcmtsDhcpGiAddress);
                if(index == -1){
                    intIp = {
                            topCcmtsDhcpIntIpAddr: giAddrObj.topCcmtsDhcpGiAddress,
                            topCcmtsDhcpIntIpMask: giAddrObj.topCcmtsDhcpGiAddrMask
                    }
                    virtualIpList.push(intIp);                    
                }
            }
        }else if(giAddrObj.modifyType == _MODIFY_TYPE_.modify){
            generateModifyGiaddr(giAddrObj);
        }else if(giAddrObj.modifyType == _MODIFY_TYPE_.del){
            generateDelGiaddr(giAddrObj);
        }
    }
}
function generateModifyGiaddr(giAddrObj){    
    var oldIp = giAddrObj.oldGiAddress;
    var ip = giAddrObj.topCcmtsDhcpGiAddress;
    var mask = giAddrObj.topCcmtsDhcpGiAddrMask;
    var index = getIntIpIndex(oldIp);
    var intIp = {
            topCcmtsDhcpIntIpIndex: null,
            topCcmtsDhcpIntIpAddr: ip,
            topCcmtsDhcpIntIpMask: mask
    };
    
    if(index == -1){
        if(getIntIpIndex(ip) == -1){
            virtualIpList.push(intIp);
        }                
    }else{
        if(isExistInGiAddr(oldIp)){            
            if(getIntIpIndex(intIp.topCcmtsDhcpIntIpAddr) == -1){
                virtualIpList.push(intIp);
            } 
        }else{
            if(virtualIpList[index].topCcmtsDhcpIntIpIndex != null && 
                    virtualIpList[index].topCcmtsDhcpIntIpIndex != undefined){
                delIntIp.push(virtualIpList[index]);
            }
            virtualIpList[index].topCcmtsDhcpIntIpAddr = ip;
            virtualIpList[index].topCcmtsDhcpIntIpMask = mask;
        }
        
        
    }
}
function remove(array, dx)
{
    if(isNaN(dx)||dx>array.length){return false;}
    for(var i=0,n=0;i<array.length;i++)
    {
        if(array[i]!=array[dx])
        {
            array[n++]=array[i]
        }
    }
    array.pop();
    return array;
} 
function generateDelGiaddr(giAddrObj){
    var oldIp = giAddrObj.oldGiAddress;
    var index = getIntIpIndex(oldIp);
    var ip = giAddrObj.topCcmtsDhcpGiAddress;
    if(index != -1){
        if(!isExistInGiAddr(oldIp)){
            if(virtualIpList[index].topCcmtsDhcpIntIpIndex != null && 
                    virtualIpList[index].topCcmtsDhcpIntIpIndex != undefined){
                delIntIp.push(virtualIpList[index]);
            }
            virtualIpList = remove(virtualIpList, index);
        }        
    }
}
function isExistInGiAddr(ip){
    for(var i = 0; i < giAddrList.length; i++){
        if(giAddrList[i].topCcmtsDhcpGiAddress == ip){
            return true;
        }
    }
    return false;
}
/**
 * 获取主IP数组位置，如果不存在返回-1
 */
function getPrimaryIpIndex(){
    for(var i = 0; i < virtualIpList.length; i++){
        if(virtualIpList[i].topCcmtsDhcpIntIpIndex == 0 || virtualIpList[i].topCcmtsDhcpIntIpIndex == "0"){
            return i;
        }
    }
    return -1;
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
 * 改变页面时，将数据写到window中
 */
function changePagePrepare(){
    virtualIpList = [];
	policyStore.each(function(){
        virtualIpList.push(this.data);
        if(!this.data.topCcmtsDhcpIntIpIndex){
        	if(this.data.topCcmtsDhcpIntIpIndex != "0" || this.data.topCcmtsDhcpIntIpIndex != 0){
	            addVirtualIp.push(this.data.topCcmtsDhcpIntIpAddr);
	            addVirtualIpMask.push(this.data.topCcmtsDhcpIntIpMask);
        	}
        }
    });
    window.parent.dhcpRelay.addVirtualIp = addVirtualIp;
    window.parent.dhcpRelay.addVirtualIpMask = addVirtualIpMask;
    window.parent.dhcpRelay.delIntIp = delIntIp;
    var delIntIpArray = [];
    for(var i = 0; i < delIntIp.length; i++){
        delIntIpArray.push(delIntIp[i].topCcmtsDhcpIntIpAddr);
    }
    window.parent.dhcpRelay.delVirtualIp = delIntIpArray;
    window.parent.dhcpRelay.virtualIpList = virtualIpList;
    var index = getPrimaryIpIndex();
    if(index != -1){
        var virtualIp = virtualIpList[index];
        //更新primaryIp
        window.parent.dhcpRelay.primaryIp = {
                virtualPrimaryIpAddr: virtualIp.topCcmtsDhcpIntIpAddr,
                virtualPrimaryIpMask: virtualIp.topCcmtsDhcpIntIpMask
        };
    }else{
        window.parent.dhcpRelay.primaryIp = null;
    }
}
/************************************************************************************************************
 * 数据校验
 ************************************************************************************************************/
function isExistInStore(ip, mask, intIps){
    var ipCheck = new IpAddrCheck(ip, mask);
    if(intIps && intIps.length>0){
        for(var i = 0; i < intIps.length; i++){
            if(ipCheck.isSubnetConflict(intIps[i].data.topCcmtsDhcpIntIpAddr, intIps[i].data.topCcmtsDhcpIntIpMask)){
                return true;
            }
        }
    }
    return false;
}

function isExistInIntIp(ip, mask, intIps){
	var ipCheck = new IpAddrCheck(ip, mask);
	if(intIps && intIps.length>0){
		for(var i = 0; i < intIps.length; i++){
			if(ipCheck.isSubnetConflict(intIps[i].topCcmtsDhcpIntIpAddr, intIps[i].topCcmtsDhcpIntIpMask)){
				return true;
			}
		}
	}
	return false;
}
function conflictIp(ip,mask){
	var pageIntIpList = policyStore.data.items;
	if(isExistInStore(ip, mask, pageIntIpList)){
		return true;
	}
	if(isExistInIntIp(ip, mask, delIntIp)){
		return false;
	}
	if(isExistInIntIp(ip, mask, intIpList)){
        return true;
    }
	return false;
}
function ipInputValid(ip,mask){
    if(!ipIsFilled("giAddr") || !ipIsFilled("giMask")){
        window.parent.showMessageDlg("@COMMON.tip@", "@route.ipFormatError@");
        return false;
    }
    if(!checkedIpMask(mask)){
        window.parent.showMessageDlg("@COMMON.tip@", "@route.ipFormatError@");
        return false;
    }
    var ipCheck = new IpAddrCheck(ip, mask);
    if(!ipCheck.isHostIp()){
        window.parent.showMessageDlg("@COMMON.tip@", "@route.ipFormatError@");
    	return false;
    }
    if(conflictIp(ip,mask)){
        window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.dhcpIntIpConflict@");
    	return false;
    }
    return true;
}

function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function preClick(){
	changePagePrepare();
	window.location.assign('cmc/dhcprelay/showDhcpRelayConfigGiAddrStep.tv?entityId='+entityId + 
            '&cmcId=' + cmcId + '&action=' + action + "&bundleInterface=" + bundleInterface );
}
function nextClick(){
	changePagePrepare();
	window.location.assign('cmc/dhcprelay/showDhcpRelayConfigServerStep.tv?entityId='+entityId + 
            '&cmcId=' + cmcId + '&action=' + action + "&bundleInterface=" + bundleInterface );
}
/**
 * 检查是否有对应IP地址的GiAddr
 */
function isGiAddrRef(val){
    var valid = false;
    for(var i = 0; i < giAddrList.length; i++){
        if(giAddrList[i].topCcmtsDhcpGiAddress == val){
            valid = true;
            break;
        }
    }
    if(primaryIp && primaryIp.virtualPrimaryIpAddr == val){
        valid = true;
    }
    return valid;
}
function hasSecondaryIp(){
    var valid = false;
    for(var i = 0; i < virtualIpList.length; i ++){
        var index = virtualIpList[i].topCcmtsDhcpIntIpIndex;
        if(index == null || index == undefined || index === "" || index > 0){
            valid = true;
            break;
        } 
    }
    return valid;
}
function opeartionRender(value, cellmate, record){
    var isPrimary;
    var index = record.data.topCcmtsDhcpIntIpIndex;
    var ip = record.data.topCcmtsDhcpIntIpAddr;
    if(index == "0"){
        isPrimary = true;
    }else{
        isPrimary = false;
    }
    
    //1.如果有中继地址（GiAddr）引用该地址则不允许删除
    //2.如果为主IP地址，并且有1个及以上的Secondary IP地址，则不允许删除主IP
    if(isGiAddrRef(ip) || (isPrimary && hasSecondaryIp())){
        return "<span class='cccGrayTxt'>@COMMON.del@</span>";
    }else{
        return String.format("<a href='javascript:;' " + 
                "onclick='removeRecord(\"{0}\")'>@COMMON.del@</a>" , record.id);
    }
}
function indexRender(value, cellmate, record){
    var temp = record.data.topCcmtsDhcpIntIpIndex;
    if(temp == null || temp == undefined || (typeof temp == "string" && temp == "")){
        return "@CMC.text.dhcpNewAdd@";
    }else if(temp === 0 || temp === "0"){
        return "Primary";
    }else{
        return "Secondary";
    }
}
//添加虚拟IP地址
function addPolicy(){
    var giAddr = getIpValue("giAddr");
    var giMask = getIpValue("giMask");
    var index = policyStore.getCount();
    if(index >= 5){
    	return;
    }
    if(!ipInputValid(giAddr,giMask)){
        return;
    }
    var data;
    if(getPrimaryIpIndex() == -1){
        data = {
                topCcmtsDhcpIntIpIndex: "0",
                topCcmtsDhcpIntIpAddr: giAddr,
                topCcmtsDhcpIntIpMask: giMask
            };
    }else{
        data = {
                topCcmtsDhcpIntIpAddr: giAddr,
                topCcmtsDhcpIntIpMask: giMask
            };
    }
    virtualIpList.push(data);
    policyStore.reload();
}
function removeRecord(id){
    var record = policyStore.getById(id);
    var giAddrIndex = record.data.topCcmtsDhcpIntIpIndex;
    var ip = record.data.topCcmtsDhcpIntIpAddr;
    if(giAddrIndex){
        delIntIp.push(record.data);
    }
    var index = getIntIpIndex();
    remove(virtualIpList, index);
    policyStore.reload();
}
function buildGiAddrInput(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="20">' + "@CMC.label.dhcpVirtualIp@" + '</td>' +
           '<td><span id="span1" style="background-color:#ffffff"></span></td></tr></tbody></table>'
}
function buildGiMaskInput(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="30">' + "@CMC.label.iPMask@" + '</td>' +
           '<td><span id="span2" style="background-color:#ffffff"></span></td></tr></tbody></table>'
}
function createVirtualIpGrid(){
    var w = document.body.clientWidth - 40;
    var h = document.body.clientHeight - 115;
    var columns = [
                  {header: "@CMC.label.type@", width:w/4-20, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpIndex", renderer: indexRender},
                  {header: "@CMC.label.dhcpVirtualIp@", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpAddr"},
                  {header: "@CMC.label.iPMask@", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpMask"},
                  {header: "@CMC.text.dhcpOperation@", width:w/4-10, align:"center", sortable:false, dataIndex:"op", renderer: opeartionRender}
                  ];
    var cm = new Ext.grid.ColumnModel(
        columns
    );
    var toolbar = [
                    buildGiAddrInput(),
                    buildGiMaskInput(),
                   {text: "@CMC.text.dhcpAdd@", iconCls: "bmenu_new", id:"addGiAddr", handler: addPolicy}
                   ];
    policyStore = new Ext.data.JsonStore({
        root:'data',
        pruneModifiedRecords: false,
        proxy:new Ext.data.MemoryProxy(policyGiaddr),
        fields:['tag',
                'topCcmtsDhcpIntIpIndex', 
                'topCcmtsDhcpIntIpAddr', 
                'topCcmtsDhcpIntIpMask'
                ]
    });
    policyGrid  = new Ext.grid.EditorGridPanel({ 
        bodyCssClass:'normalTable',
        height: 290,
        trackMouseOver: false, 
        border: true, 
        store: policyStore,
        clicksToEdit: 1,
        tbar: toolbar,
        viewConfig:{
        	forceFit:true
        },
        cm: cm,
        autofill:true,
        renderTo: 'virtualIp-div'
    });
    policyStore.load();
    var giAddr = new ipV4Input("giAddr","span1");
    giAddr.width(141);
    var giMask = new ipV4Input("giMask","span2");
    giMask.width(141);
    
}
Ext.onReady(function (){
	initData();
    createVirtualIpGrid();
});
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@CMC.label.dhcpRelayConfig@</b></p>
	    	<p><span id="newMsg">@CMC.label.dhcpSettingL2L3VirtualIp@</span></p>
	    </div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div id="virtualIp-div" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth" id=sequenceStep>
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	         <li><a  id=prevBt onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@CMC.button.preStep@</span></a></li>
	         <li><a id="nextStep" onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i>@CMC.button.nextStep@</span></a></li>
	         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@CMC.button.cancel@</span></a></li>
	     </ol>
	</div>
	
	

<div class=formtip id=tips style="display: none"></div>


</body>
</Zeta:HTML>