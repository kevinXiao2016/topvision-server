<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
        <%@include file="/include/ZetaUtil.inc"%>
        <Zeta:Loader>
            library ext
            library jquery
            library zeta
            library FileUpload
            module cmc
            CSS css/white/disabledStyle
            CSS js/webupload/webuploader
		    IMPORT js/webupload/webuploader.min
            IMPORT cmc/cmPollImport
        </Zeta:Loader>
        <script type="text/javascript">
        	function downloadTemplate(){
        		window.location.href="/cmpoll/downLoadTemplate.tv";
        	}
            //导入;
            function importClick() {
           	    var fileName = $("#fileInput").val();
           		if( fileName && (fileName.indexOf('.xls') !== -1 || fileName.indexOf('.xlsx') !== -1)){
           		   Ext.getBody().mask("<img src='/images/refreshing.gif' class='loadingmask'/> " + '@network/NAMEIMPORT.readingFile@');
           		   flash.upload('/cmpoll/importSpecifiedCmList.tv?fileName'+fileName);
           		} else {
           			window.parent.showMessageDlg('@COMMON.tip@', '@cmPoll.pleaseSelExcel@');
           		}
            }
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@cmPoll.importManageList@</p>
                <p>@network/NAMEIMPORT.select100MFile@</p>
            </div>
            <div class="rightCirIco upArrCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT70">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr class="withoutBorderBottom">
                    	<td class="rightBlueTxt">@COMMON.required@@CMC.label.selectFile@@COMMON.maohao@</td>
                    	<td width="240">
                    		<input type="text" id="fileNameInput" class="normalInputDisabled floatLeft" value='' disabled />
                    		<div class="btns">
                    			<div id="picker">@CMC.title.importExcel@</div>
                    		</div>
                    	</td>
                    	<td>
                    		<a href="javascript:;" class="normalBtn leftFloat mL4" onclick="downloadTemplate()"><span><i class="miniIcoArrDown"></i>@CMC.title.downloadTemplate@</span></a>
                    	</td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT90 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="startUpload()">
                            <span><i class="miniIcoInport"></i>@resources/COMMON.importAction@</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
                            <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                        </a>
                    </li>
                </ol>
            </div>
        </div>
        <div class="edge10 pT20">
			<div class="yellowTip">
				<b class="orangeTxt">@cm.cmImportTipsTitle@</b>
				<p class="pT10">@cmPoll.tip1@</p>
				<p>@cmPoll.tip2@</p>
				<p>@cmPoll.tip3@</p>
			</div>
		</div>
    </body>
</Zeta:HTML>