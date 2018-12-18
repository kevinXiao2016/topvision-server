<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
	module demo
	import js.FileUpload.TopvisionUpload
</Zeta:Loader>
<style type="text/css">
.TD_SEPRETOR{width: 10px;}
#previewText{width: 500px;height: 150px;overflow: scroll;}
</style>
<script type="text/javascript">
	/***********************************************
						程序入口
	***********************************************/
	$( DOC ).ready( initialize );
	function initialize(){
		var flash = new TopvisionUpload("flash");
		flash.onSelect = function(file){
			$("#fileIpt").val(file.name);
		}
		flash.onSecurityError = function(){
			alert("error!");
		}
		flash.onIOError = function(){
			alert("@demo.tranferEr@");
		}
		flash.onComplete = function(){
			alert("complete!");
		}
		
		flash.onData = function(text){
			$("#previewText").text(text)
		}
		
		 $("#flash").mouseover(function(){
			//this.className = "BUTTON_OVER75";
		}).mouseout(function(){
			//this.className = "BUTTON75";
		});  
		$("#upload").click(function(){
			flash.upload("www.baidu.com");
		});
		$("#preview").click(function(){
			flash.load();
		});
	}
</script>
</head>
<body class="openWinBody">
	<!-- 内容部分和按钮部分一定要分开设计居中布局 -->
	<div class="edgeTB10LR20">
		<table class="mCenter tdEdged4">
			<!-- 上面2级已经确定了这个table在页面中居中布局，table里面的部分自由发挥即可  -->
			<tbody>
				<tr>
					<td>@demo.default@:</td>
					<td>
						<!--  input 和 a 标签一定要 并排放在一起，并且是 float 的-->
						<input type="text" class="normalInput floatLeft" style="width: 300px;" id="fileIpt" />
						<a href="javascript:;" id="flash" class="nearInputBtn"><span>@demo.browser@</span></a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
		
	<!-- 按钮部分和内容部分一定要分开设计居中布局 -->
	<div class="noWidthCenterOuter clearBoth">
		<div>
			<ol class="pB0 pT10 noWidthCenter upChannelListOl">
				<li>
					<a href="javascript:;" class="normalBtnBig" id="flash"
						onclick="okClick()"><span><i class="miniIcoSaveOK"></i>@demo.preview@</span></a>
				</li>
				<li>
					<a href="javascript:;" class="normalBtnBig" id="flash"
						onclick="okClick()"><span><i class="miniIcoSaveOK"></i>@demo.upload@</span></a>
				</li>
			</ol>
			<ol  class="pB0 pT10 noWidthCenter upChannelListOl">
				<li>
					<div id="previewText" ></div>
				</li>
			</ol>
		</div>
	</div>
	
</body>
</Zeta:HTML>