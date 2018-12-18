<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}

</style>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<script type="text/javascript">
var _RELAY_MODE_ = {snooping: 0, l2: 1, l3: 2};
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6 };
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6 };
var policyGrid = null;
var policyStore = null;
var policyGiaddr = {data: []};
var server = {data: []};
var delVirtualIp = [];
var delIntIp = [];
var addVirtualIp = [];
var addVirtualIpMask = [];
var virtualIpList = [];
var addVirtualIpGiAddr = [];
var addVirtualIpGiMask = [];
var intIpList = null;
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

function initData(){
	policyGiaddr.data = window.parent.dhcpRelay.virtualIpList;
	intIpList = window.parent.intIpList;
	if(window.parent.dhcpRelay.delVirtualIp){
		delVirtualIp = window.parent.dhcpRelay.delVirtualIp;
		delIntIp = window.parent.dhcpRelay.delIntIp;
	}	
	var giAdrList = window.parent.dhcpRelay.giAddrListTemp;
	if(policyGiaddr.data){
		if(window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr && checkedIpValue(window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr) &&
				window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr != "0.0.0.0"){
			for(var j = 0; j < policyGiaddr.data.length; j++ ){
		        if((typeof policyGiaddr.data[j].topCcmtsDhcpIntIpIndex=="string" && policyGiaddr.data[j].topCcmtsDhcpIntIpIndex == "0") ||
		                (typeof policyGiaddr.data[j].topCcmtsDhcpIntIpIndex=="number" && policyGiaddr.data[j].topCcmtsDhcpIntIpIndex == 0)){
		            index = j;
		            break;
		        }
			}
			if(index < policyGiaddr.data.length){
				policyGiaddr.data[index].topCcmtsDhcpIntIpAddr = 
	                window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr;
	            policyGiaddr.data[index].topCcmtsDhcpIntIpMask = 
	                window.parent.dhcpRelay.bundle.virtualPrimaryIpMask;
	            policyGiaddr.data[index].topCcmtsDhcpIntIpIndex = "0";
			} else{
				var primaryIp = {};
                primaryIp.topCcmtsDhcpIntIpIndex = "0";
                primaryIp.topCcmtsDhcpIntIpAddr = window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr;
                primaryIp.topCcmtsDhcpIntIpMask = window.parent.dhcpRelay.bundle.virtualPrimaryIpMask;
                policyGiaddr.data.unshift(primaryIp);
			}
			
	    }else{
	    	var index = policyGiaddr.data.length;
            for(var j = 0; j < policyGiaddr.data.length; j++ ){
                if(policyGiaddr.data[j].topCcmtsDhcpIntIpIndex == "0" ||
                        policyGiaddr.data[j].topCcmtsDhcpIntIpIndex == 0){
                    index = j;
                    break;
                }
            }
            if(index < policyGiaddr.data.length){
            	remove(policyGiaddr.data, index);
            }            
	    }
	    if(giAdrList){
	        //处理删除GiAddr
	        for(var i = 0; i< giAdrList.length; i++){
	            var giAddr = giAdrList[i];          
	            if(giAddr.modifyTag == _MODIFY_TYPE_.del){
	                var delIndex = -1;
	                for(var j = 0; j < policyGiaddr.data.length; j++ ){
	                    var policyGiAddr = policyGiaddr.data[j];
	                    if(giAddr.topCcmtsDhcpGiAddressOld == policyGiAddr.topCcmtsDhcpIntIpAddr){
	                    	if(policyGiAddr.topCcmtsDhcpIntIpIndex){
	                    		delVirtualIp.push(policyGiAddr.topCcmtsDhcpIntIpAddr);
	                    		delIntIp.push(policyGiAddr);
	                    	}	                        
	                        delIndex = j;       
	                        break;
	                    }
	                }
	                remove(policyGiaddr.data, delIndex);
	            }
	        }
	        //处理修改 GiAddr
	        for(var i = 0; i< giAdrList.length; i++){
	            var giAddr = giAdrList[i];          
	            if(giAddr.modifyTag == _MODIFY_TYPE_.modify){
	                for(var j = 0; j < policyGiaddr.data.length; j++ ){
	                    var policyGiAddr = policyGiaddr.data[j];
	                    if(giAddr.topCcmtsDhcpGiAddressOld == policyGiAddr.topCcmtsDhcpIntIpAddr){
	                    	delVirtualIp.push(policyGiAddr.topCcmtsDhcpIntIpAddr);
	                    	delIntIp.push(policyGiAddr);
	                        addVirtualIpGiAddr.push(giAddr.topCcmtsDhcpGiAddress);
	                        addVirtualIpGiMask.push(giAddr.topCcmtsDhcpGiAddress);
	                        policyGiaddr.data[j].topCcmtsDhcpIntIpAddr = giAddr.topCcmtsDhcpGiAddress;
	                        policyGiaddr.data[j].topCcmtsDhcpIntIpMask = giAddr.topCcmtsDhcpGiAddrMask;
	                        policyGiaddr.data[j].topCcmtsDhcpIntIpIndex = null;
	                        break;
	                    }
	                }
	            }
	        }
	        //处理插入GiAddr
	        for(var i = 0; i< giAdrList.length; i++){
	            var giAddr = giAdrList[i];          
	            if(giAddr.modifyTag == _MODIFY_TYPE_.add){
	                addVirtualIpGiAddr.push(giAddr.topCcmtsDhcpGiAddress);
	                addVirtualIpGiMask.push(giAddr.topCcmtsDhcpGiAddress);
	                var secondaryIp = {};
	                secondaryIp.topCcmtsDhcpIntIpAddr = giAddr.topCcmtsDhcpGiAddress;
	                secondaryIp.topCcmtsDhcpIntIpMask = giAddr.topCcmtsDhcpGiAddrMask;
	                policyGiaddr.data.push(secondaryIp);
	            }	            
	        }
		}	
	}else{
		policyGiaddr.data = [];
		if(giAdrList){
			//处理插入GiAddr
	        for(var i = 0; i< giAdrList.length; i++){
	            var giAddr = giAdrList[i];          
	            if(giAddr.modifyTag == _MODIFY_TYPE_.add){
	                addVirtualIpGiAddr.push(giAddr.topCcmtsDhcpGiAddress);
	                addVirtualIpGiMask.push(giAddr.topCcmtsDhcpGiAddress);
	                var secondaryIp = {};
	                secondaryIp.topCcmtsDhcpIntIpAddr = giAddr.topCcmtsDhcpGiAddress;
	                secondaryIp.topCcmtsDhcpIntIpMask = giAddr.topCcmtsDhcpGiAddrMask;
	                policyGiaddr.data.push(secondaryIp);
	            }
	        }
		}
        if(window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr && checkedIpValue(window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr) && 
        		window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr != "0.0.0.0"){                 
            var primaryIp = {};
            primaryIp.topCcmtsDhcpIntIpIndex = "0";
            primaryIp.topCcmtsDhcpIntIpAddr = window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr;
            primaryIp.topCcmtsDhcpIntIpMask = window.parent.dhcpRelay.bundle.virtualPrimaryIpMask;
            policyGiaddr.data.unshift(primaryIp);
            
        }
	}
	removeSameIp(policyGiaddr.data);
	removeInvalidIp(policyGiaddr.data);
}
function removeSameIp(array){
    for(var i = 0; i < array.length; i ++){
        var r = [];
        for(var j = i + 1; j < array.length; j++){
            if(array[i].topCcmtsDhcpIntIpAddr == array[j].topCcmtsDhcpIntIpAddr){
                r.push(j);
            }
        }
        for(var k = r.length -1; k >= 0; k --){
            remove(array, r[k]);
        }
    }
}

function removeInvalidIp(array){
    var r = [];
    for(var i = 0 ; i < array.length; i ++){
        if(array[i].topCcmtsDhcpIntIpAddr){
            if(array[i].topCcmtsDhcpIntIpAddr.trim() == ""){
                r.push(i);
            }
        }else{
            r.push(i);
        }
    }
    for(var k = r.length -1; k >= 0; k --){
        remove(array, r[k]);
    }
}

function changePagePrepare(){
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
    window.parent.dhcpRelay.delVirtualIp = delVirtualIp;
    window.parent.dhcpRelay.delIntIp = delIntIp;
    window.parent.dhcpRelay.virtualIpList = virtualIpList;
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
        return false;
    }
    if(!checkedIpMask(mask)){
        return false;
    }
    var ipCheck = new IpAddrCheck(ip, mask);
    if(!ipCheck.isHostIp()){
    	return false;
    }
    if(conflictIp(ip,mask)){
    	return false;
    }
    return true;
}

function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function preClick(){
	changePagePrepare();
    window.location.assign("/cmc/createDhcpRelayStep2.jsp");
}
function nextClick(){
	changePagePrepare();
    window.location.assign('/cmc/createDhcpRelayStep3.jsp');
}
function opeartionRender(value, cellmate, record){
    return String.format("<img src='/images/delete.gif' " + 
            "onclick='removeRecord(\"{0}\")'/>&nbsp;&nbsp;&nbsp;&nbsp;" , record.id);
}
function indexRender(value, cellmate, record){
    var temp = record.data.topCcmtsDhcpIntIpIndex;
    if(temp && temp!=""){
        if(temp === 0 || temp === "0"){
            return "Primary";
        }else{
            return "Secondary";
        }
    }else {
    	return I18N.CMC.text.dhcpNewAdd;
    }
}
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
    var data = {
            topCcmtsDhcpIntIpAddr: giAddr,
            topCcmtsDhcpIntIpMask: giMask
        };
    var p = new policyStore.recordType(data); 
    policyGrid.stopEditing();
    policyStore.insert(index, p);
    policyGrid.startEditing(0, 0);

}
function removeRecord(id){
    var record = policyStore.getById(id);
    var giAddrIndex = record.data.topCcmtsDhcpIntIpIndex;
    if(giAddrIndex){
        delVirtualIp.push(record.data.topCcmtsDhcpIntIpAddr);
        delIntIp.push(record.data);
    }
    policyStore.remove(record);
}
function buildGiAddrInput(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="20">' + I18N.CMC.label.dhcpVirtualIp + '</td>' +
           '<td><span id="span1" style="background-color:#ffffff"></span></td></tr></tbody></table>'
}
function buildGiMaskInput(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="30">' + I18N.CMC.label.iPMask + '</td>' +
           '<td><span id="span2" style="background-color:#ffffff"></span></td></tr></tbody></table>'
}
function createVirtualIpGrid(){
    var w = document.body.clientWidth - 40;
    var h = document.body.clientHeight - 115;
    var columns = [
                  {header: I18N.CMC.label.type, width:w/4-20, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpIndex", renderer: indexRender},
                  {header: I18N.CMC.label.dhcpVirtualIp, width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpAddr"},
                  {header: I18N.CMC.label.iPMask, width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpMask"},
                  {header: I18N.CMC.text.dhcpOperation, width:w/4-10, align:"center", sortable:false, dataIndex:"op", renderer: opeartionRender}
                  ];
    var cm = new Ext.grid.ColumnModel(
        columns
    );
    var toolbar = [
                    buildGiAddrInput(),
                    buildGiMaskInput(),
                   {text: I18N.CMC.text.dhcpAdd, iconCls: "bmenu_new", id:"addGiAddr", handler: addPolicy}
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
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message bundle="${cmc}" key="CMC.label.dhcpRelayConfig"/></b></p>
	    	<p><span id="newMsg"><fmt:message bundle="${cmc}" key="CMC.label.dhcpSettingL2L3VirtualIp"/></span></p>
	    </div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div id="virtualIp-div" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth" id=sequenceStep>
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	         <li><a  id=prevBt onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i><fmt:message bundle="${cmc}" key="CMC.button.preStep"/></span></a></li>
	         <li><a id="nextStep" onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i><fmt:message bundle="${cmc}" key="CMC.button.nextStep"/></span></a></li>
	         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${cmc}" key="CMC.button.cancel"/></span></a></li>
	     </ol>
	</div>
	
	

<div class=formtip id=tips style="display: none"></div>


</BODY>
</HTML>