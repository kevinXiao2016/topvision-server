<%@ page language="java" contentType="text/html;charset=UTF-8"%>
 <%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<%@include file="/include/ZetaUtil.inc"%>
		<Zeta:Loader>
		    LIBRARY ext
		    LIBRARY jquery
		    LIBRARY zeta
		    PLUGIN Portlet
		    MODULE fault
		    CSS css/white/disabledStyle
		    import fault.alertSound
		</Zeta:Loader>
		<link href="alertSound.css" type="text/css" rel="stylesheet" />		
		<script type="text/javascript" src="/js/jquery/jquery.fakeUpload.js"></script>
		<script type="text/javascript" src="/js/jQueryFileUpload/ajaxfileupload.js"></script>
		<script type="text/javascript">
		var allSound = ${sounds}; //{id,name,description,deletable}
		//var alertLevels = ${alertLevels}; //{levelId,name(告警名称),soundId,soundName,soundDesc,iconCls(需要自己去拼)}
		</script>
	</head>
	<body class="openWinBody">
		<div class="edge10 pB0">
			<ul class="leftFloatUl outerTab" id="outerTab">
				<li>
					<a href="javascript:;" class="tab tabSel">@ALERT.currentConfig@</a>
				</li>
				<li>
					<a href="javascript:;" class="tab">@ALERT.soundManage@</a>
				</li>
			</ul>
			<div class="tabBody">
				<div id="putConfigTable" class="putConfigTable"></div>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" onclick="closeWin()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
			<div class="tabBody" style="display:none;">
				<div id="putSoundTable" class="putSoundTable"></div>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" onclick="showUploadPage()"><span><i class="miniIcoArrUp"></i>@ALERT.uploadSound@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" onclick="closeWin()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</div>
		<div id="changeSoundPage">
			<div class="edge10 pB0">
				<p class="flagP"><span class="flagInfo">@ALERT.please@ <b id="levelName" class="orangeTxt"></b> @ALERT.selectSound@</span></p>
				<div id="putSelSoundTable" class="putSelSoundTable"></div>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a id="changeSoundSave" href="javascript:;" class="normalBtnBig" disabled="disabled" onclick="saveChange()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" onclick="closeChangeSoundPage()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>	
			<p class="redTips">@ALERT.soundTips@</p>
		</div>
		<div id="uploadPage">
			<div class="openWinHeader">
			    <div class="openWinTip">
			    	<p id="topTip2">@ALERT.soundTip1@</p>
			    	<p id="topTip">@ALERT.soundTip2@</p>
			    	<p>@ALERT.soundTip3@</p>
			    </div>
			    <div class="rightCirIco pageCirIco"></div>
			</div>
	   	 	<div class="edgeTB10LR20 pT80">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				    <tbody>
				        <tr>
				            <td class="rightBlueTxt" width="270">
								@ALERT.selectFile@:<b class="redTxt">*</b>
				            </td>
				            <td>
								<span id="soundFile" name="soundFile"></span>
				            </td>
				        </tr>
				        <tr class="darkZebraTr">
				            <td class="rightBlueTxt">
								@ALERT.description@:<b class="redTxt">*</b>
				            </td>
				            <td>
								<input id="file_decr" type="text" class="normalInput w260" toolTip="@COMMON.anotherName@" />
				            </td>
				        </tr>
				    </tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" id="uploadFile"><span><i class="miniIcoArrUp"></i>@ALERT.upload@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" onclick="closeUploadPage()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</div>
		<audio src="../sounds/level0.wav" id="musicPlayer"></audio>
		<div id="putMusic"></div>
	</body>
</Zeta:HTML>