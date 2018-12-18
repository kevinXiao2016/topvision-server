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
</Zeta:Loader>
<script type="text/javascript">
	var uniId = "${gponUniAttribute.uniId}",
		entityId = "${gponUniAttribute.entityId}",
		ethAttributePortIndex = "${gponUniAttribute.ethAttributePortIndex}",
		gponOnuUniPvid = "${gponUniAttribute.gponOnuUniPvid}",
		gponOnuUniPri = "${gponUniAttribute.gponOnuUniPri}";
		
	$(function(){
		if(uniId === ''){
			top.showMessageDlg("@COMMON.tip@", "@GPON.withoutPortTip@");
		}
		$("#pri").val(gponOnuUniPri);
	});
	function cancelClick(){
		top.closeWindow("uniVlanConfig");
	}
	function saveClick(){
		var gponOnuUniPri = $("#pri").val(),
			gponOnuUniPvid = $("#pvid").val(),
			gponOnuUniPvidNum = parseInt(gponOnuUniPvid, 10),
			reg = /^[0-9]+$/;
		
		if( !reg.test(gponOnuUniPvid) ||  gponOnuUniPvidNum < 1 || gponOnuUniPvidNum > 4094 ){
			$("#pvid").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
            url: '/gpon/onu/setUniVlanConfig.tv',
            type: 'POST',
            data: {
            	entityId : window.entityId, 
            	uniId : window.uniId, 
            	gponOnuUniPri : gponOnuUniPri, 
            	gponOnuUniPvid　: gponOnuUniPvid
            },
            success: function() {
            	top.nm3kRightClickTips({
    				title: "@COMMON.tip@",
    				html: "@resources/COMMON.saveSuccess@"
    			});
            	try{
            		top.getActiveFrame().reloadData();
            	}catch(Exception){
            		
            	}
            	cancelClick();
            }, error: function() {
            	window.top.showMessageDlg("@COMMON.tip@", "@resources/SYSTEM.saveFailure@");
        },cache: false
        });
	}
</script>
    </head>
    <body class="openWinBody">
    	<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>@onu/EPON.vlanCfg@</p>
		    	<p>@GPON.bandTip@</p>
		    </div>
		    <div class="rightCirIco wheelCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT20">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="180">@GPON.portNum@@COMMON.maohao@</td>
		                <td>${gponUniAttribute.ethAttributePortIndex}</td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">PVID:</td>
		                <td>
		                	<input id="pvid" type="text" class="normalInput w180" toolTip="1-4094" value="${gponUniAttribute.gponOnuUniPvid}" />
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt">@GPON.priority@@COMMON.maohao@</td>
		                <td>
		                	<select class="normalSel w180" id="pri">
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
		            </tr>
		        </tbody>
		    </table>
		    <div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		            <li><a onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		            <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		        </ol>
		    </div>
		</div>
	</body>
</Zeta:HTML> 


