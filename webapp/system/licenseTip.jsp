<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	IMPORT js.jquery.Nm3kMsg
	module resources
</Zeta:Loader>
<title>@platform/sys.licTipPage@</title>
<style type="text/css">
	.c-tip{ color: #BBBBBB; position: absolute; }
	#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px; margin:0 auto;}
	#w1600{ width:1600px; position:absolute; top:0; left:0;}
	#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
	#step1{ left:800px;}
	.infoTable{ width:100%;}
	.infoTable td{ height: 30px;}
	.infoTable td td{ height:auto;}
	#putTree{ width:582px; border:1px solid #ccc;  background: #fff; height: 240px;}
</style>
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css" />
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.fakeUpload.js"></script>
<script type="text/javascript">

function showLicenseImportResult(resultCode){
	var tipStr = '';
	if(resultCode==100){
		tipStr = "@platform/sys.importSuccess@";
		afterSaveOrDelete({
			title : '@COMMON.tip@',
			html : tipStr
		});
		window.location.href="/showlogon.tv?__times="+new Date().valueOf();
		/*showMsg("@platform/sys.importSuccess@",function(){
			window.location.href="/showlogon.tv?__times="+new Date().valueOf();
		});*/
	}else if(resultCode==101){
		tipStr = "@platform/sys.importRepeat@";
	}else if(resultCode==102){
		tipStr = "@platform/sys.importSign@";
	}else if(resultCode==103){
		tipStr = "@platform/sys.importWrongPK@";
	}else if(resultCode==104){
		tipStr = "@platform/sys.expire@";
	}else if(resultCode==-100){
		tipStr = "@platform/sys.serverError@";
	}
	afterSaveOrDelete({
		title : '@COMMON.tip@',
		html : tipStr
	});
}

function showMsg(msg,fn){
	Ext.MessageBox.show({title: "@COMMON.tip@", msg: msg, buttons: Ext.MessageBox.OK, icon: Ext.MessageBox.INFO,fn:fn});
}
var tree = null;
function doOnload(){
	tree = new dhtmlXTreeObject("putTree", "100%", "100%", 0);
	tree.setImagePath("/js/dhtmlx/tree/imgs/dhxtree_skyblue/");
	tree.enableCheckBoxes(1);
	tree.enableThreeStateCheckboxes(true);
	tree.loadXML("/system/loadAllDeviceTypes.tv"); 
}

(function(){
	$(function(){
		/* new Ext.Panel({
			title:"@platform/sys.licTipPage@",
			renderTo: Ext.getBody(),
			contentEl:"lic_tip",
			height:400,
			width:600,
			style:{
				margin:"0 auto"
			}
		}); */
		
		$("#fake_upload").fakeUpload('init',{
			"tiptext":"@platform/sys.importTip@",
			"width":260,
			"btntext":"@platform/sys.browse@",
			"checkfn":function(filePath,name){
					if(filePath.indexOf(".lic")==-1){
						afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : "@platform/sys.selLic@"
						});
						return false;
					}else{
						$("#ok_btn").attr("disabled",false);
						return true;
					}
			}
		});
		
		$("#product_key_btn").click(function(){
			$("#w1600").animate({left:-800},'fast');
			/* var ifObj=$('#if_resp').get(0);
			ifObj.src="/system/getProductKey.tv?__times="+new Date().valueOf(); */
		});
		
		$("#ok_btn").click(function(){
			var filePath=$("#fake_upload").fakeUpload("getPath");
			if(filePath){
				$('#import-form').submit();
			}else{
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '@license.withoutUpload@'
				})
			}
		});
		doOnload();
	});
})();

//返回按钮;
function backFn(){
	$("#w1600").animate({left:0},'fast');
}
//点击确定;
function saveFn(){
	var str = tree.getAllCheckedBranches();
	if(str.length == 0){
		alert('@license.moreThan@');
		return;
	}
	if($("#company").val().trim() == ""){
		alert('@license.companyIsEmpty@');
		return;
	}
	var licenseView = {
		"licenseType" : $("#licenseType").val(),
		"user" : $("#man").val(),
		"phone" : $("#phone").val(),
		"email" : $("#email").val(),
		"selectEntityTypes" : str,
		"organisation" : $("#company").val()
	};
	var ifObj=$('#if_resp').get(0);
	ifObj.src="/system/getProductKey.tv?__times="+new Date().valueOf() + "&licenseType=" + licenseView.licenseType 
			+"&user="+licenseView.user+"&phone="+licenseView.phone+"&email="+licenseView.email+"&selectEntityTypes="+licenseView.selectEntityTypes+"&organisation="+licenseView.organisation;
	$("#w1600").animate({left:0},'fast');
}

var nm3kObj = {};//用来记录nm3kMsg控件弹出的框;
function afterSaveOrDelete(o){
	var showtime = 1000;
	var nTime = o.showTime ? o.showTime : showtime;
	var autoHide = o.autoHide ? o.autoHide : true;
	if($("#nm3kSaveOrDelete").length == 0){
		nm3kObj.nm3kSaveOrDelete = new Nm3kMsg({
			title: o.title,
			html: o.html,
			okBtnTxt: "@resources/COMMON.ok@",
			okBtn : true,
			timeLoading: true,
			unique: true,
			showTime : nTime,
			autoHide : o.autoHide,
			id: "nm3kSaveOrDelete"		
		});
		nm3kObj.nm3kSaveOrDelete.init();
	}else{
		nm3kObj.nm3kSaveOrDelete.update({
			html: o.html,
			title: o.title
		})
	}	
};//end afterSaveOrDelete;
</script>
</head>
<body class="whiteToBlack">
	<form id="import-form" method="post" target="ifResp" action="/system/importLicense.tv" enctype="multipart/form-data">
	<div id="w800">
		<div id="w1600">
			<div id="step0">
				<div class="edge10">
						<div class="zebraTableCaption">
					     <div class="zebraTableCaptionTitle"><span>@platform/sys.licTipPage@</span></div>
						    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
						        <tbody>
						            <tr>
						                <td>
											<b class="pL20 blueTxt">@platform/sys.cause@:</b>
						                </td>
						            </tr>
						            <tr class="darkZebraTr">
						                <td class="withoutBorderBottom">
						                	<span class="pL40">@platform/sys.causeOne@</span>
						                </td>
						            </tr>
                                     <tr class="darkZebraTr">
                                        <td>
                                            <span class="pL40">@platform/sys.causeTwo@</span>
                                        </td>
                                    </tr>
                                     <tr class="darkZebraTr">
                                        <td>
                                            <span class="pL40">@platform/sys.causeThree@</span>
                                        </td>
                                    </tr>
						            <tr>
						            	<td class="withoutBorderBottom"></td>
						            </tr>
						            <tr>
						            	<td class="withoutBorderBottom">
						            		<span class="pL20 pR20 orangeTxt">@platform/sys.importNotice@</span>
						            	</td>
						            </tr>
						        </tbody>
						    </table>
						</div>
						<div class="noWidthCenterOuter clearBoth">
						    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
						        <li style="margin-right:20px;">
						            <span class="pT5" id="fake_upload" name="licFile"></span>
						        </li>
						        <Zeta:Button id="ok_btn" icon="miniIcoInport" clazz="disabledAlink">@COMMON.importAction@</Zeta:Button>
						        <Zeta:Button id="product_key_btn" icon="miniIcoLock">@SYSTEM.licenseApplications@</Zeta:Button>
						    </ol>
						</div>
					</div>
			</div>
			<div id="step1">
				<div class="edge10 pT20">
				    <table cellpadding="0" cellspacing="0" rules="none" style="border-collapse: collapse;" class="infoTable">
				        <tr>
				        	<td class="rightBlueTxt" width="120">
				        		@license.type@
				        	</td>
				        	<td width="230">
				        		<select id="licenseType" class="normalSel" style="width:242px;">
				        			<option value="trial">@license.trial@</option>
				        			<option value="commercial">@license.commercial@</option>
				        		</select>
				        	</td>
				        	<td class="rightBlueTxt" width="100">
				        		@license.contact@
				        	</td>
				        	<td>
				        		<input id="man" type="text" class="normalInput w240" />
				        	</td>
				        </tr>
				        <tr>
				        	<td class="rightBlueTxt">
				        		@license.phone@
				        	</td>
				        	<td>
				        		<input id="phone" type="text" class="normalInput w240" />
				        	</td>
				        	<td class="rightBlueTxt">
				        		@license.email@
				        	</td>
				        	<td>
				        		<input id="email" type="text" class="normalInput w240" />
				        	</td>
				        </tr>
				        <tr>
				        	<td class="rightBlueTxt">@license.company@</td>
				        	<td colspan="3">
				        		<input id="company" type="text" class="normalInput" style="width:582px;" />
				        	</td>
				        </tr>
				        <tr>
				        	<td class="rightBlueTxt" valign="top">@license.module@</td>
				        	<td colspan="3">
				        		<div id="putTree">
				        			
				        		</div>
				        	</td>
				        </tr>
				    </table>
			    </div>
			    <div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" onclick="backFn()"><span><i class="miniIcoArrLeft"></i>@COMMON.back@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" onclick="saveFn()"><span><i class="miniIcoSaveOK"></i>@license.offlineApplication@</span></a></li>
				     </ol>
				</div>
			</div>
		</div>
	</div>
			<%-- <div id="lic_tip">
					<table>
						<tr>
							<td width="35%" height="280px" align="center"><img src="../images/license.gif" /></td>
							<td width="65%">
								<div>@platform/sys.cause@</div>
								<div style="margin-top: 20px;">
									<div style="margin-left: 30px;">@platform/sys.causeOne@</div>
									<div style="margin-left: 30px;margin-top: 5px;">@platform/sys.causeTwo@</div>
								</div>
								<div style="margin-top: 50px;line-height: 20px;color: red;">
					            @platform/sys.importNotice@
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<form id="import-form" method="post" target="ifResp" action="/system/importLicense.tv" enctype="multipart/form-data">
								<div style="border-top: 1px solid #C2C2C2;padding-top: 30px;padding-right:10px;">
									<span id="fake_upload" name="licFile"></span>
									&nbsp;
									<input id="ok_btn" type="button" class="BUTTON75" style="vertical-align: top;" value="@platform/sys.import@" disabled="disabled" />
									&nbsp;
									<input id="product_key_btn" type="button" style="vertical-align: top;"  class="BUTTON95" value="@platform/sys.getPK@" />
								</div>
								</form>
							</td>
						</tr>
					</table>
					</div> --%>


</form>
<iframe id="if_resp" name="ifResp" width="0" height="0"></iframe>
</body>
</Zeta:HTML>