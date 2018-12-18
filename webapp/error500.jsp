<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    module platform
</Zeta:Loader>
</head>
<body class="whiteToBlack">
	<div class="edge10">
		<ol class="upChannelListOl">
		     <li><a href="javascript:;" class="normalBtnBig" style="opacity:0;filter:alpha(opacity=0);"><span><i class="miniIcoArrLeft"></i></span></a></li>			
		</ol>				
	</div>
	<div class="horizontalOnePxLine"></div>
	<div class="errorLineBg">
		<div class="error500"></div>
		<div class="error500Txt">
			<p class="pB20"><b class="bigErrorTxt">@errorTitle@</b></p>
            <p>@errorCause1@</p>
            <p>@errorCause2@</p>
            <p>@errorCause3@</p>
		</div>
	</div>
<script type="text/javascript">
	$(function(){
		var w = $(window).width();
		var h = $(window).height();
		if(w < 558 || h <660){
			$(".error500").css("display","none");
			$(".errorLineBg").css("padding-top",40);
		}
	
	})
</script>
</body>
</Zeta:HTML>
