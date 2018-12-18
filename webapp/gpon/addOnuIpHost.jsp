<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module gpon
    IMPORT js/tools/ipText
    IMPORT js/jquery/Nm3kTabBtn
</Zeta:Loader>
<script type="text/javascript">
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var onuIndex = '${onuIndex}';
	var usedHostIpIndex = ${usedHostIpIndex};
	var iPHostValues = ["1","2"]//DHCP值为1，Static值为2;
	$(function(){
		//给隐藏域赋值，控制好布局;
		$("#onuIpHostIndex").val(window.iPHostValues[0]);
		$(".jsHide").css({display:'none'});
		//创建按钮组;
		var tab1 = new Nm3kTabBtn({
		    renderTo:"putOnuHostMode",
		    callBack:"changeTabBtnValue",
		    tabs:["DHCP","STATIC"]
		});
		tab1.init();
	});//end document.ready;
	//改变按钮组时，改变布局，改变隐藏域的值;
	function changeTabBtnValue(index){
		$("#onuHostMode").val(window.iPHostValues[index]);
		switch(index){
		case 0:
			$(".jsHide, #staticBody").css({display:'none'});
			break;
		case 1:
			$(".jsHide, #staticBody").css({display:''});
			break;
		}
	}
    function saveClick(){
    	var onuIpHostIndex = $("#onuIpHostIndex").val(),
    	    onuHostMode = $("#onuHostMode").val(),
    	    hostIpAddr = $("#hostIpAddr").val(),
    	    hostMask = $("#hostMask").val(),
    	    hostGateway = $("#hostGateway").val(),
    	    priDns = $("#priDns").val(),
    	    secondDns = $("#secondDns").val(),
    	    hostVlanPri = $("#hostVlanPri").val(),
    	    hostVlanId = $("#hostVlanId").val(),
    	    reg = /^[0-9]+$/,
    	    flag = false,
    	    ipReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    	    
    	//验证序号格式;
    	if( !reg.test(onuIpHostIndex) || !(onuIpHostIndex > 0 && onuIpHostIndex <= 64) ){
    		$("#onuIpHostIndex").focus();		
	    	return;
    	}
    	//验证序号重复;
    	if( $.inArray(parseInt(onuIpHostIndex, 10), usedHostIpIndex) != -1 ){
    		var msg = String.format('@GPON.usedIndexTip@',onuIpHostIndex);
    		if( usedHostIpIndex.length > 0 ){
    			msg += String.format('<br /><br />@GPON.usedIndex@<br />{0}', usedHostIpIndex.join(", "));
    		}
    		top.showMessageDlg("@COMMON.tip@", msg);
    		return;
    	}
    	//验证VLAN ID;
    	if( !reg.test(hostVlanId) || hostVlanId < 1 || hostVlanId > 4094 ){
    		$("#hostVlanId").focus();
    		return;
    	}
    	switch(onuHostMode){
   		case window.iPHostValues[0]: //DHCP;
   			saveClick2({
   				entityId : entityId,
				onuId : onuId,
				onuIndex : onuIndex,
				onuHostMode : onuHostMode,
				onuIpHostIndex : onuIpHostIndex,
				hostVlanPri : hostVlanPri,
				hostVlanId : hostVlanId
   			})
   			break;
   		case window.iPHostValues[1]: //STATIC;
   			//验证IP地址;
   	    	if( ipReg.test(hostIpAddr)  ){
   	    		var ipFirstInput = hostIpAddr.split(".")[0];
   	    		if( parseInt(ipFirstInput)>=1 && parseInt(ipFirstInput)<=223 && parseInt(ipFirstInput)!=127 ){
   	    			flag = true;
   	    		}
   	    	}
   	    	if(!flag){
   	    		$("#hostIpAddr").focus();
   	    		return;
   	    	} 
   	    	//验证子网掩码;
   	    	if( !ipReg.test(hostMask) || !checkedIpMask(hostMask) ){
   	    		$("#hostMask").focus();
   	    		return;
   	    	}
   	    	//验证网关;
   	    	if( !ipReg.test(hostGateway) ){
   	    		$("#hostGateway").focus();
   	    		return;
   	    	}
   	    	//验证首选DNS;
   	    	if(priDns != ""){
	   	    	if( !ipReg.test(priDns) ){
	   	    		$("#priDns").focus();
	   	    		return;
	   	    	}
   	    	}
   	    	//验证备选DNS;
   	    	if(secondDns != ""){
	   	    	if( !ipReg.test(secondDns) ){
	   	    		$("#secondDns").focus();
	   	    		return;
	   	    	}
   	    	}
   	    	var tempObj = {
	   	    	entityId : entityId,
				onuId : onuId,
				onuIndex : onuIndex,
				onuIpHostIndex : onuIpHostIndex,
				onuHostMode : onuHostMode,
				hostIpAddr : hostIpAddr,
				hostMask : hostMask,
				hostGateway : hostGateway,
				hostVlanPri : hostVlanPri,
				hostVlanId : hostVlanId
	   		}
   	    	if(priDns != ""){
   	    		tempObj.priDns = priDns;
   	    	}
   	    	if(secondDns != ""){
   	    		tempObj.secondDns = secondDns;
   	    	}
   	    	saveClick2(tempObj);
   			break;
    	}
	}
    function saveClick2(data){
    	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/gpon/onu/addOnuIpHost.tv',
			type : 'POST',
			data : data,
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.addOk@</b>'
	       	    });
				try{
					top.getActiveFrame().reloadData();
				}catch(err){}
				cancelClick();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.addEr@");
			},
			cache : false
		});
    }
    
    function cancelClick(){
    	top.closeWindow('addOnuIpHost');
    }
</script>
</head>
    <body class="openWinBody">
    	<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>@GPON.maxIpHost@</p>
		    	<p>@GPON.ipTip@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT60">
			<div style="height:200px;">
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			        	<tr>
			        		<td class="rightBlueTxt">@GPON.disMode@@COMMON.maohao@</td>
			                <td id="putOnuHostMode" colspan="3"></td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt" width="146">@COMMON.required@@GPON.index@@COMMON.maohao@</td>
			                <td width="190">
			                   <input id='onuIpHostIndex' type="text" class="normalInput w180" maxlength="2" toolTip="@GPON.indexTip@" />
			                </td>
			                <td width="130" class="rightBlueTxt"><div class="jsHide">@COMMON.required@@GPON.ipAddress@@COMMON.maohao@</div></td>
			                <td>
			                   <div class="jsHide"><input id='hostIpAddr' type="text" class="normalInput w180" toolTip="@GPON.ipTip@" /></div>
			                </td>
			            </tr>
			        </tbody>
			        <tbody id="staticBody" style="display:none;">
			            <tr>
			                <td class="rightBlueTxt">@COMMON.required@@GPON.subNetMask@@COMMON.maohao@</td>
			                <td>
			                   <input id='hostMask' type="text" class="normalInput w180" />
			                </td>
			                <td class="rightBlueTxt">@COMMON.required@@GPON.gateWay@@COMMON.maohao@</td>
			                <td>
			                   <input id='hostGateway' type="text" class="normalInput w180" />
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">@GPON.PreferredDNS@@COMMON.maohao@</td>
			                <td>
			                   <input id='priDns' type="text" class="normalInput w180" />
			                </td>
			                <td class="rightBlueTxt">@GPON.AlternativeDNS@@COMMON.maohao@</td>
			                <td>
			                   <input id='secondDns' type="text" class="normalInput w180" />
			                </td>
			            </tr>
			        </tbody>
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt">@COMMON.required@@GPON.VLANpriority@@COMMON.maohao@</td>
			                <td>
			                   <select class="normalSel w182" id='hostVlanPri'>
		                     		<option value='0'>0</option>
		                     		<option value='1'>1</option>
		                     		<option value='2'>2</option>
		                     		<option value='3'>3</option>
		                     		<option value='4'>4</option>
		                     		<option value='5'>5</option>
		                     		<option value='6'>6</option>
		                     		<option value='7'>7</option>
		                     	</select> 
			                </td>
			                <td class="rightBlueTxt">@COMMON.required@VLAN ID:</td>
			                <td>
			                   <input id='hostVlanId' type="text" class="normalInput w180" toolTip="1-4094" maxlength="4" />
			                </td>
			            </tr>
			        </tbody>
			    </table>
			</div>
		    <div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		            <li><a onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		            <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		        </ol>
		    </div>
		</div>
		<input type="hidden" id="onuHostMode" name="onuHostMode" value="1" />
    </body>
</Zeta:HTML>