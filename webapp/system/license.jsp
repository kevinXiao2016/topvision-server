<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.license.common.Confs" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
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
	module resources
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css" />
<style type="text/css">
.c-tip{ color: #BBBBBB; position: absolute; }
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px;}
#w1600{ width:1600px; position:absolute; top:0; left:0;}
#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:800px;}
.infoTable{ width:100%;}
.infoTable td{ height: 30px;}
.infoTable td td{ height:auto;}
#putTree{ width:582px; border:1px solid #ccc;  background: #fff; height: 240px;}
.jsInclude span, .jsExclude span{display:inline-block; padding-right:15px; padding:0px 15px 5px 0px;}
</style>
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>

<script type="text/javascript" src="/js/jquery/jquery.fakeUpload.js"></script>

<script type="text/javascript">
var licenseValid = ${licenseValid};

//如果有合法license，并且是单独打开页面，则需要跳转至登录页面
if(window.top == window && licenseValid){
	window.location.href = '/showlogon.tv';
}

function showMessageDlg(title, msg, type,fn) {
	var icon = (type == "error" ? Ext.MessageBox.ERROR : (type == "question" ? Ext.MessageBox.QUESTION : Ext.MessageBox.INFO));
	Ext.MessageBox.show({title: title, msg: msg, buttons: Ext.MessageBox.OK, icon: icon,fn:function(){}});
}

function showLicenseImportResult(resultCode){
	var tip = '';
	if(resultCode==100){
		/* showMsg(I18N.SYSTEM.license.importSuccess,function(){
			window.location.href="/system/showLicense.tv?__times="+new Date().valueOf();
		}); */
		if(top.afterSaveOrDelete){
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : I18N.SYSTEM.license.importSuccess
			});
			window.location.href="/system/showLicense.tv?__times="+new Date().valueOf();
			return;
		}else{
			window.location.href="/system/showlogon.tv?__times="+new Date().valueOf();
			return;
		}
	}else if(resultCode==101){
		tip = I18N.SYSTEM.license.note1;
	}else if(resultCode==102){
		tip = I18N.SYSTEM.license.note2;
	}else if(resultCode==103){
		tip = I18N.SYSTEM.license.note3;
	}else if(resultCode==104){
		tip = I18N.SYSTEM.license.note4;
	}else if(resultCode==-100){
		tip = I18N.SYSTEM.license.note5;
	}
	showMessageDlg('@COMMON.tip@', tip);
}
(function(){
	$(function(){
		//点击展开;
		$(".jsInclude").delegate(".jsOpen","click",function(){
			var $div = $(this).parent().parent();
			$div.empty();
			$div.html($div.data("allData"));
		});
		$(".jsExclude").delegate(".jsOpen","click",function(){
			var $div = $(this).parent().parent();
			$div.empty();
			$div.html($div.data("allData"));
		});
		//调整列表中的格式，目前后台是以#的字符串分隔（最后会多一个# 需要先去掉）;
		//分隔字符，在两侧加上<span>标签;
		$(".jsInclude").each(function(){
			var $me = $(this),
			    text = $me.text();
			if(text.indexOf("#") !== -1){//必须是有#号的
				var newText = text.slice(0,-1),//先去掉最后一个多余的#字符;
				    arr = newText.split("#"),
				    str = '';
				    
				for(var i=0,len=arr.length; i<len; i++){
					str += String.format('<span>{0}</span>',arr[i]);
				}
				$me.html(str);
			}
		});
		$(".jsExclude").each(function(){
			var $me = $(this),
			    text = $me.text();
			if(text.indexOf("#") !== -1){//必须是有#号的
				var newText = text.slice(0,-1),//先去掉最后一个多余的#字符;
				    arr = newText.split("#"),
				    str = '';
				    
				for(var i=0,len=arr.length; i<len; i++){
					str += String.format('<span>{0}</span>',arr[i]);
				}
				$me.html(str);
			}
		});
		
		//table中数据太多，大于10条则收缩起来，显示6条+展开按钮;
		$(".jsInclude").each(function(){
			var $me = $(this),
			    $span = $me.find("span"),
			    len = $span.length;
			
			if(len > 10){
				$me.data({
					allData : $me.html()
				});
				$me.empty();
				for(var i=0; i<=6; i++){
					$me.append($span.eq(i))
				}
				$me.append(String.format('<p class="pT5">@license.all@<b class="blueTxt"> {0} </b>@license.datas@ <a class="jsOpen" href="javascript:;">@license.clickExpand@</a></p>',len));
			}
		});
		$(".jsExclude").each(function(){
				var $me = $(this),
			        $span = $me.find("span"),
			        len = $span.length;
			
				if(len > 10){
					$me.data({
						allData : $me.html()
					});
					$me.empty();
					for(var i=0; i<=6; i++){
						$me.append($span.eq(i))
					}
					$me.append(String.format('<p class="pT5">@license.all@<b class="blueTxt"> {0} </b>@license.datas@ <a class="jsOpen" href="javascript:;">@license.clickExpand@</a></p>',len));
				}
		});
		
		$("#product_key_btn").click(function(){
			window.location.href = '/system/showLicenseApply.tv';
			
		});
		$("#cancel_btn").click(function(){
			window.parent.closeWindow && window.parent.closeWindow('modalDlg');
		});
		$("#ok_btn").click(function(){
			if($(this).hasClass("disabledAlink")){return;}
			var filePath=$("#fake_upload").fakeUpload("getPath");			
			if(filePath){				
				$('#import-form').submit();
			}else{
				showMessageDlg('@COMMON.tip@', '@license.withoutUpload@');
			}
		});
		$("#fake_upload").fakeUpload('init',{
				"tiptext":I18N.SYSTEM.license.note6,
				"width":160,				
				"btntext":"@COMMON.browse@...",
				"checkfn":function(filePath,name){
						if(filePath.indexOf(".lic")==-1){
							showMessageDlg('@COMMON.tip@', '@SYSTEM.license.note7@');
							return false;
						}else{
							$("#ok_btn").removeClass("disabledAlink");
							return true;
						}
				}
		});
	});
})();
</script>
</head>
<body class="whiteToBlack edge10">
	<form id="import-form" method="post" target="ifResp" action="/system/importLicense.tv" enctype="multipart/form-data">
	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@COMMON.license@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
                <td class="rightBlueTxt" width="16%">
					@SYSTEM.license.version@
                </td>
                <td width="16%">
					${licView.version}
                </td>
                <td class="rightBlueTxt" width=16%">
					@SYSTEM.license.type@
                </td>
                <td>
					<c:set var="COMMERCIAL" value="<%=Confs.LICENSE_TYPE_COMMERCIAL %>" />
					<c:set var="TRAIL" value="<%=Confs.LICENSE_TYPE_TRIAL %>" />
					<c:if test="${licView.licenseType==COMMERCIAL}">
					@SYSTEM.license.commercialversion@
					</c:if>
					<c:if test="${licView.licenseType==TRAIL}">
					@SYSTEM.license.trialVersion@
					</c:if>
                </td>
                <td class="rightBlueTxt">
					@SYSTEM.license.expiryDate@：
                </td>
                <td>
					<c:if test="${licView.endDate=='-1'}">
					@SYSTEM.license.unlimited@
					</c:if>
					<c:if test="${licView.endDate!='-1'}">
					${licView.endDate}
					</c:if>
                </td>
            </tr>
            <tr  class="darkZebraTr">
                <td class="rightBlueTxt">
					@SYSTEM.license.userNumlimit@
                </td>
                <td>
					<c:if test="${licView.numberOfUsers=='-1'}">
					@SYSTEM.license.unlimited@
					</c:if>
					<c:if test="${licView.numberOfUsers!='-1'}">
					${licView.numberOfUsers}
					</c:if>
                </td>
                <td class="rightBlueTxt">
					@license.engineNum@
                </td>
                <td>
					<c:if test="${licView.numberOfEngines=='-1'}">
					@SYSTEM.license.unlimited@
					</c:if>
					<c:if test="${licView.numberOfEngines!='-1'}">
					${licView.numberOfEngines}
					</c:if>
                </td>
                <td class="rightBlueTxt">
					@license.emsMobile@
                </td>
                <td>
                	<c:if  test="${licView.mobileSurport==true}" >
					@license.surport@
					</c:if>
					<c:if test="${licView.mobileSurport!=true}">
					@license.unSurport@
					</c:if>
                </td>
            </tr>
            <tr>
            	<td class="rightBlueTxt">
					PNMP：
                </td>
                <td>
                	<c:if  test="${licView.pnmpSurport==true}" >
					@license.surport@
					</c:if>
					<c:if test="${licView.pnmpSurport!=true}">
					@license.unSurport@
					</c:if>
                </td>
                <td class="rightBlueTxt">
					@SYSTEM.license.licensedTo@
                </td>
                <td colspan="5">
					${licView.organisation}
                </td>
            </tr>
		</tbody>
	</table>
	
	<table class="dataTable mT10" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th width="70">@COMMON.name@</th>
				<th width="80">@license.status@</th>
				<th width="40%">@license.surportList@</th>
				<th>@license.unSurportList@</th>
				<th width="100">@license.num@</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${licView.modules}" var="m">
				<c:set var="mName" value="${m.name}" /> 
					<% 
					String mName=(String)pageContext.getAttribute("mName");
					mName=mName.equals("ccmts")?"cmc":mName;
					%>
					<tr>
						<td class="txtCenter"><b class="orangeTxt">${m.displayName}</b></td>
						<td class="txtCenter">
                                  <c:if test="${m.enabled==true}">
                                  @license.enabled@
                                  </c:if>
                                  <c:if test="${m.enabled!=true}">
                                  @license.disabled@
                                  </c:if>
                               </td>
						<td valign="top">
							<div class="edge5 jsInclude">${m.includeStr}</div>
						</td>
						
						<td valign="top">
							<div class="edge5 jsExclude">${m.excludeStr}</div>
						</td>
						
						<td class="txtCenter">
							<c:if test="${m.numberOfEntities=='2147483647' && m.name!='report'}">
							@SYSTEM.license.unlimited@
							</c:if>
							<c:if test="${m.numberOfEntities!='2147483647' && m.name!='report'}">
							${m.numberOfEntities}
							</c:if>
							<c:if test="${m.name=='report'}">
							-
							</c:if>
						</td>
					</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="noWidthCenterOuter clearBoth">
	    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	        <li style="margin-right:20px;">
	            <span class="pT5" id="fake_upload" name="licFile"></span>
	        </li>
	        <Zeta:Button id="ok_btn" icon="miniIcoInport" clazz="disabledAlink">@COMMON.importAction@</Zeta:Button>
	        <Zeta:Button id="product_key_btn" icon="miniIcoLock">@SYSTEM.licenseApplications@</Zeta:Button>
	    </ol>
	</div>
	</form>
<iframe id="if_resp" name="ifResp" width="0" height="0" style="opacity:0.1"></iframe>
</body>
</Zeta:HTML>