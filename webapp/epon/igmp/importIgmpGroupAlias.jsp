<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
    library zeta
    library FileUpload
    library ext
    module igmpconfig
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
    IMPORT epon.igmp.ImportIgmpGroupAlias
</Zeta:Loader>
<style type="text/css">
.rightConner{ position: absolute; bottom:10px; right:10px;}
</style>
<script type="text/javascript">
var entityId = '${entityId}';
var successList= [],errorList= [],parseErrorList = [];
var showData,spareData = new Array(); //显示数据
var baseGrid ;
$( DOC ).ready(function(){
    /* WIN.flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        var chooseFileName = obj.name;
        if(obj.size > 100*1024*1024){
            window.parent.showMessageDlg('@COMMON.tip@', String.format('@network/NAMEIMPORT.select100MFile@', obj.name));
            return;
        }else{
            $("#fileInput").val(chooseFileName);
        }
    }; */
    
    newCreateUpload();
    
    
    
    renderResultTable();
    registerCallback();
});


</script>
</head>
	<body class=openWinBody>
		<div id="step1">
			<div class=formtip id=tips style="display: none"></div>
			<div class="openWinHeader">
				<div class="openWinTip">@IGMP.batchImportAlias@</div>
				<div class="rightCirIco folderCirIco"></div>
			</div>

			<div class="edge10 pT5">
				<form onsubmit="return false;">
					<table class="mCenter zebraTableRows">
						<tr>
							<td width="260" class="rightBlueTxt"><label id="fileLabel"
								style='width: 60px;'>@network/NAMEIMPORT.selectFile@:</label></td>
							<td width="300">
								<input id="fileInput" class="normalInputDisabled floatLeft w220" type="text" disabled="disabled" />
								<div class="btns">
	                    			<div id="picker">@COMMON.browse@</div>
	                    		</div> 
							</td>
							<td>
								<a href="javascript:downloadTemplate();" class="normalBtn"><span><i class="miniIcoArrDown"></i>@network/NAMEEXPORT.templateDownload@</span></a>
							</td>
						</tr>
					</table>
				</form>
			</div>

			<Zeta:ButtonGroup>
				<li>
					<a onclick="importClick()" href="javascript:;" class="normalBtnBig"><span><i class=miniIcoInport></i>@COMMON.import@</span></a>
				</li>
				<%-- <Zeta:Button onClick="okBtClick()" icon="miniIcoInport">@COMMON.import@</Zeta:Button> --%>
				<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.close@</Zeta:Button>
			</Zeta:ButtonGroup>
			<div class="edge10 pT20">
				<div class="yellowTip">
					<b class="orangeTxt">@IGMP.import.tooltip.1@:</b>
					<p class="pT10">1:@IGMP.import.tooltip.2@</p>
					<p>2:@IGMP.import.tooltip.3@</p>
					<p>3:@IGMP.import.tooltip.4@</p>
					<p>4:@IGMP.import.tooltip.5@:@COMMON.anotherName@</p>
					<p>5:@IGMP.import.tooltip.6@</p>
				</div>
			</div>
		</div>
		<!-- 导入结果 -->
		<div id="step2" style="display:none; position:absolute; top:0; left:0; background:#f7f7f7; width:100%; height:470px; overflow:hidden;">
			<div>
				<div class="openWinHeader">
					<div class="openWinTip">
						<p id="successTip">
							@COMMON.success@:<b id="sucFont" style="font: 18px Georgia, Arial; margin-right: 30px; color: green;">0</b>
							@COMMON.error@:<b style="font: 18px Georgia, Arial; color: red;" id="failFont" color="red">0</b>
						</p>
					</div>
					<div class="rightCirIco pageCirIco"></div>
				</div>
			</div>

			<div id="putGridPanel" class="edge10 clearBoth"></div>
			
			<Zeta:ButtonGroup>
				<Zeta:Button onClick="showSuccessList()" icon="miniIcoSaveOK">@IGMP.importSuccess@</Zeta:Button>
				<Zeta:Button onClick="showErrorData()" icon="miniIcoWrong">@IGMP.importFaild@</Zeta:Button>
				<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
			</Zeta:ButtonGroup>
		</div>
	</body>
</Zeta:HTML>