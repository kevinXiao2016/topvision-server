<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
	<Zeta:Loader>
		library ext
		library jquery
		library zeta
	    module logicinterface
	    IMPORT js/tools/ipText
	</Zeta:Loader>
	<style type="text/css">
	#sidePart p{ padding-bottom:10px; line-height:1.4em;}
	</style>
	<script type="text/javascript">
	var entityId = '${entityId}';
	var ipV4ConfigIndex = '${ipV4ConfigIndex}';
	var ip1, ip2;
	
    $(function(){
    	ip1 = new ipV4Input('addIp','ipV4Addr');
    	ip1.width(200);
    	ip2 = new ipV4Input('addIp2','ipV4NetMask');
    	ip2.width(200);
    });//end document.ready;
		
    function saveClick(){
    	var ipV4AddrType = $("#ipV4AddrType").val();
		var ipV4Addr = ip1.getValue();
		var ipV4NetMask = ip2.getValue();
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/logicinterface/addInterfaceIpV4.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				ipV4ConfigIndex : ipV4ConfigIndex,
				ipV4AddrType : ipV4AddrType,
				ipV4Addr : ipV4Addr,
				ipV4NetMask : ipV4NetMask
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@interface.addIpSuc@</b>'
	       	    });
				top.getActiveFrame().refreshFrameData();
				cancelClick();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@interface.addIpFail@");
			},
			cache : false
		});
	}
    
    function cancelClick(){
		top.closeWindow('addInterfaceIp');
	}
    
	</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	@interface.addTips1@
	    	@interface.addTips2@
	    	@interface.addTips3@
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="180">
	                   	 IP:
	                </td>
	                <td id="ipV4Addr">
	                                         
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                   @interface.mask@:
	                </td>
	                <td id="ipV4NetMask">
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                   	 @interface.type@:
	                </td>
	                <td>
	                     <select class="normalSel w200" id='ipV4AddrType'>
	                     	<option value='0'>@interface.priType@</option>
	                     	<option value='1'>@interface.subType@</option>
	                     </select>                
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
	        <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	            <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
	            <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	        </ol>
	    </div>
	</div>
</body>
</Zeta:HTML>