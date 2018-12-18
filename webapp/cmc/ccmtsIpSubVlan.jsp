<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
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
    import cmc.js.cmcIpSubVlan
    css css/white/disabledStyle
</Zeta:Loader>
</head>
<style type="text/css">
	span.normalInputDisabled{ border:none;}
</style>
<script type="text/javascript">
	var viewGrid;
	var newIp;
	var newMask;
	var entityId = '${cmcId}';
	var tpid = '${cmcIpSubVlanScalar.ipSubVlanTpid}';
	var cfi = '${cmcIpSubVlanScalar.topCcmtsIpSubVlanCfi}';
	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	Ext.onReady(function() {
		viewGrid = new CmcIpSubVlanGrid({
			renderTo : "viewGridCont",
			height : 260
		});
		viewGrid.getStore().reload();
		$('#tpid').val(tpid);
		$('#cfi').val(cfi);
		$(".modifyBtn").hide();
		$(".backBtn").hide();
		newIp = new ipV4Input("ip","span1");
		newIp.width(131);
		newMask = new ipV4Input("mask","span2");
		newMask.width(131);
		
		if(!operationDevicePower){
	        $("#tpid").attr("disabled",true);
	        $("#cfi").attr("disabled",true);
	        $("#configBt").attr("disabled",true);
	        setIpv4Disabled("span1",true);
	        setIpv4Disabled("span2",true);
	        $("#addBt").attr("disabled",true);
	        $("#modifyBt").attr("disabled",true);
	        $("#vlanId").attr("disabled",true);
	        $("#vlanPri").attr("disabled",true);
	        $("#backBt").attr("disabled",true);
	    }
	});
	
	/**
	 * 关闭页面
	 */
	function closeHandler() {
		window.parent.closeWindow('ccmtsIpSubVlan');
	}

	function deleteBtClick(cmcId,ip,mask){
		 window.parent.showConfirmDlg("@COMMON.tip@","@VLAN.deleteSubVlanConfirm@", function(type) {
             if (type == 'no'){return;} 
             window.top.showWaitingDlg("@COMMON.wait@", "@VLAN.delSubVlan@", 'ext-mb-waiting');
             $.ajax({
                 url: '/cmcVlan/deleteCmcIpSubVlanCfg.tv',cache:false,
                 data:{
                     cmcId:cmcId,
                     topCmcIpSubVlanIp:ip,
                     topCmcIpSubVlanIpMask:mask
                 },success:function(){
                     //window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.delSubVlanSuc@");
                     window.top.closeWaitingDlg();
                     top.afterSaveOrDelete({
            				title: '@COMMON.tip@',
            				html: '<b class="orangeTxt">@VLAN.delSubVlanSuc@</b>'
            		 });
                     viewGrid.getStore().reload();
                     setIpValue("ip", "");
                     setIpValue("mask", "");
                     $("#vlanId").val("");
                     $("#vlanPri").val(0);
             		 $("input:text").attr("disabled",false);
                     $(".modifyBtn").hide();
                     $(".backBtn").hide();
             		 $(".addBtn").show();
                 },error:function(){
                     window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.delSubVlanFail@");
                 }
             });
         });
     }

    function subVlanConfigClick(){
        var tpid = $("#tpid").val();
        if(!isNum(tpid)){
        	return Zeta$("tpid").focus();
        }
    	window.top.showWaitingDlg("@COMMON.wait@", "@VLAN.updateSubVlan@", 'ext-mb-waiting');
        $.ajax({
            url: '/cmcVlan/modifyCmcIpSubVlanScalar.tv',cache:false,
            data:{
                cmcId: entityId,
                ipSubVlanTpid:$("#tpid").val(),
                topCcmtsIpSubVlanCfi:$("#cfi").val()
            },success:function(){
                //window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.modifySuc@");
                window.top.closeWaitingDlg();
                top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   					html: '<b class="orangeTxt">@VLAN.modifySuc@</b>'
   				});
                viewGrid.getStore().reload();
            },error:function(){
                window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.modifyFail@");
            }
       });
    }
	function addSubVlanConfigClick(){
		var vlanId = $("#vlanId").val();
		var ip = getIpValue("ip");
		var mask = getIpValue("mask");
		var count = viewGrid.getStore().getCount();
		var ipCheck = new IpAddrCheck(ip, mask);
		if (!ipIsFilled("ip")){
			return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.ipInputTip@");
		}
		if (!ipIsFilled("mask") || mask == "0.0.0.0" ){
			return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.maskInputTip@");
		}
		if (!ipCheck.checkMask()){
		    return window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.aclIpMaskError@");
		}
		if(!ipCheck.isNormalTypeIP()){
		    return window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.aclIpOnlyCanBeABC@");
		}
		if(isExistSubnet(ip, mask)){
		    return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.conflictSubVlanIp@");
		}
		if( !checkVlanId() || 4094 < vlanId || vlanId < 1){
			return Zeta$("vlanId").focus();
		}
		if(count >= 50){
			window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.subVlanOutNumber@");
			return ;
		}
        window.top.showWaitingDlg("@COMMON.wait@", "@VLAN.addSubVlanConfirm@", 'ext-mb-waiting');
            $.ajax({
                url: '/cmcVlan/addCmcIpSubVlanCfg.tv',cache:false,
                data:{
                    cmcId: entityId,
                    topCmcIpSubVlanIp:ip,
                    topCmcIpSubVlanIpMask:mask,
                    topCmcIpSubVlanVlanId:vlanId,
                    topCmcIpSubVlanPri:$("#vlanPri").val()
                },success:function(){
                	//window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.addSuc@");
                	window.top.closeWaitingDlg();
                	top.afterSaveOrDelete({
   						title: '@COMMON.tip@',
   						html: '<b class="orangeTxt">@VLAN.addSuc@</b>'
   					});
                    viewGrid.getStore().reload();
                    setIpValue("ip", "");
                    setIpValue("mask", "");
                    $("#vlanId").val("");
                    $("#vlanPri").val(0);
                },error:function(){
                    window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.addFail@");
                }
           });
	}
	
	function isExistSubnet(ip, mask){	    
	    var check = new IpAddrCheck(ip, mask);
	    var result = false;
	    viewGrid.getStore().each(function (){
	        var ipTemp = this.data.topCcmtsIpSubVlanIpIndex;
	        var maskTemp = this.data.topCcmtsIpSubVlanIpMaskIndex;
	        if(check.isSubnetConflict(ipTemp, maskTemp)){
	            result = true;    
	        }
	    });
	    return result;
	}

	function modifySubVlanConfigClick(){
		window.top.showWaitingDlg("@COMMON.wait@", "@VLAN.modifySubVlan@", 'ext-mb-waiting');
        $.ajax({
            url: '/cmcVlan/modifyCmcIpSubVlanCfg.tv',cache:false,
            data:{
                cmcId: entityId,
                topCmcIpSubVlanIp: getIpValue("ip"),
                topCmcIpSubVlanIpMask:getIpValue("mask"),
                topCmcIpSubVlanPri:$("#vlanPri").val()
            },success:function(){
                window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.modifySuc@");
                viewGrid.getStore().reload();
                setIpValue("ip", "");
                setIpValue("mask", "");
                $("#vlanId").val("");
                $("#vlanPri").val(0);
        		$("input:text").attr("disabled", false);
                $(".modifyBtn").hide();
                $(".backBtn").hide();
        		$(".addBtn").show();
            },error:function(){
                window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.modifyFail@");
            }
       });
	}
	function fetchHandler(){
		window.top.showWaitingDlg("@COMMON.wait@", "@VLAN.fetching@", 'ext-mb-waiting');
	    $.ajax({
	        url: '/cmcVlan/refreshCmcIpSubVlanCfg.tv',cache:false,
	        data:{
	            cmcId: entityId
	        },success:function(response){
	            //window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.fetchSuc@");
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   					html: '<b class="orangeTxt">@VLAN.fetchSuc@</b>'
   				});
	            tpid = response.ipSubVlanTpid;
                cfi = response.topCcmtsIpSubVlanCfi;
                $('#tpid').val(tpid);
                $('#cfi').val(cfi);
	            viewGrid.getStore().reload();
	        },error:function(){
	            window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.fetchFail@");
	        }
	    });
	}
	
	function checkVlanId(){
		var reg0 = /^([0-9])+$/;
		var vlanIndex = $("#vlanId").val();
		if(vlanIndex == "" || vlanIndex == null){
			return false;
		}else{
			return reg0.exec(vlanIndex);
		}
	}
	function isNum(m){
		var reg =  /^([A-Fa-f0-9]{1,4})$/
		return reg.test(m)
	}
	function backClick(){
		viewGrid.getStore().reload();
		setIpValue("ip", "");
        setIpValue("mask", "");
        $("#vlanId").val("");
        $("#vlanPri").val(0);
		$("input:text").attr("disabled", false);
        $(".modifyBtn").hide();
        $(".backBtn").hide();
		$(".addBtn").show();
		ipFocus("ip",1);
	}
	function authLoad(){
	    if(!refreshDevicePower){
	        $("#refreshBt").attr("disabled",true);
	    }
	}
</script>
<body class="openWinBody" onload="authLoad()">
	<div id="vlanIpSubVlanConfig" class="edge10 pB0 pT30">
		<table cellspacing=0 cellpadding=0 rules="none">
             <tr>
             	<td class="blueTxt pR5">TPID:</td>
             	<td><span class="pT2 pR2">0x</span></td>
             	<td><input id="tpid" tooltip="@VLAN.tpidTip@" maxlength="4" class="normalInput" /></td>
             	<td class="blueTxt pL20 pR5" >CFI:</td>
             	<td><select id="cfi" style="width:120px" class="normalSel">
                    <option value="0">0</option>
                    <option value="1">1</option>
                </select></td>
                <td style="padding-left:15px;">
                	<a id="configBt" onclick="subVlanConfigClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoSave"></i>@VLAN.apply@</span></a> 
                </td>
             </tr>
     	</table>
	</div>
	<div id="viewGridCont" class="normalTable edge10 pB0"></div>
	
	<div id="showVlanConfig" class="edge10">
		<table cellspacing=0 cellpadding=0 rules="none">
             <tr>
             	<td class="rightBlueTxt pR5" style="height:30px;">IP:</td>
             	<td ><span id="span1"></span></td>
             	<td class="rightBlueTxt pL20 pR5">@VLAN.subMask@:</td>
             	<td ><span id="span2"></span></td>
             	<td style="padding-left:20px;" class='addBtn'>
             		<a id="addBt"  onclick="addSubVlanConfigClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a>
                </td>
                <td style="padding-left:20px;" class='modifyBtn'>
                	<a id="modifyBt" onclick="modifySubVlanConfigClick()" href="javascript:;" class="normalBtn"><span>@COMMON.modify@</span></a>
                </td>
               
             </tr>
             <tr>
             	<td  class="rightBlueTxt pR5">VLAN ID:</td>
             	<td><input class="normalInput" id="vlanId" maxlength="4" tooltip="@VLAN.vlanIdTip@" style = "width:131px"/></td>
             	<td  class="rightBlueTxt pR5">@VLAN.vlanPri@:</td>
             	<td><select id="vlanPri"  style="width: 133px" class="normalSel">
                            <option value="0">0</option>
                        	<option value="1">1</option>
                        	<option value="2">2</option>
                        	<option value="3">3</option>
                        	<option value="4">4</option>
                        	<option value="5">5</option>
                        	<option value="6">6</option>
                        	<option value="7">7</option>
                    </select>
                </td>
                <td style="padding-left:20px;" class='backBtn'>
                	<a id="backBt" onclick="backClick()" href="javascript:;" class="normalBtn"><span>@COMMON.back@</span></a> 
                </td>
             </tr>
     	</table>
     	<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="refreshBt" onclick="fetchHandler()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a id="finishBt" onclick="closeHandler()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>