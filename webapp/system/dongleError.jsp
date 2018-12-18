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
<title>@platform/sys.dongleError@</title>
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
</head>
<body class="whiteToBlack">
	<div id="w800">
		<div id="w1600">
			<div id="step0">
				<div class="edge10">
						<div class="zebraTableCaption">
					     <div class="zebraTableCaptionTitle"><span>@platform/sys.dongleError@</span></div>
						    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
						        <tbody>
						            <tr>
						                <td>
											<b class="pL20 blueTxt">@platform/sys.cause@</b>
						                </td>
						            </tr>
                                    <tr class="darkZebraTr">
                                        <td class="withoutBorderBottom">
                                            <span class="pL40">@platform/dongle.error.cause1@</span>
                                        </td>
                                    </tr>
                                    <tr class="darkZebraTr">
                                        <td class="withoutBorderBottom">
                                            <span class="pL40">@platform/dongle.error.cause2@</span>
                                        </td>
                                    </tr>
                                    <tr class="darkZebraTr">
                                        <td class="withoutBorderBottom">
                                            <span class="pL40">@platform/dongle.error.cause3@</span>
                                        </td>
                                    </tr>
						            <tr>
						            	<td class="withoutBorderBottom"></td>
						            </tr>
						            <tr>
						            	<td class="withoutBorderBottom">
						            		<span class="pL20 pR20 orangeTxt">@platform/dongle.error.remark@</span>
						            	</td>
						            </tr>
						        </tbody>
						    </table>
						</div>
					</div>
			</div>
		</div>
	</div>
</body>
</Zeta:HTML>