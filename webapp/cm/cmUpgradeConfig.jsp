<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
    module CMC
    plugin DropdownTree
    css css/white/disabledStyle
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
    import cm/javascript/cmUpgradeConfig
</Zeta:Loader>
<style type="text/css">
	body,html{ height:100%;}
	.openLayerOuter{width: 100%; height: 100%;  overflow: hidden; position: absolute; top: 0; left: 0; z-index: 10}
	.openLayerBg{width: 100%; height: 100%; overflow: hidden; background: #F7F7F7; position: absolute; top: 0; left: 0; 
		opacity: 0.8; filter: alpha(opacity=85);}
	.openLayerMainDiv { width: 560px; height: 280px;  overflow: hidden; position: absolute;  top: 100px;  left: 120px;  
		z-index: 11;  background: #F7F7F7;}
    #w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px;}
    #w1600{ width:1600px; position:absolute; top:0; left:0;}
    #step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
    #step1{ left:800px;}
    .mainWindow{ height:370px;}
    .ext-el-mask-msg{ background: #eee; border-color:#ccc;}
    .ext-el-mask-msg div{ background: #fff; border-color: #ccc;}
    /*flash控件由于z-index层级太低而生成到了底下去了*/
    #chooseFileFileUpload{ z-index:11;}
    #selectDeviceTip{ position: absolute; top:69px; right:16px; z-index:2; padding-left:20px; 
    	background:url(../images/help.gif) no-repeat 0px 2px;}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
</script>
</head>
<body class="openWinBody">
    <div id="w800">
        <div id="w1600">
            <div id="step0">
                <div class="noWidthCenterOuter clearBoth">
                    <div class="mainWindow pT10 pL10" id="putGrid1">
                        
                    </div>
                    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
                        <li>
                            <a onclick="nextPage()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoArrRight"></i>@COMMON.next@</span>
                            </a>
                        </li>
                        <li>
                            <a onclick="cancelClick()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                            </a>
                        </li>
                    </ol>
                </div>
            </div>
            <div id="step1">
                <div class="noWidthCenterOuter clearBoth">
                    <div class="mainWindow pT10 pL10">
                        <table cellpadding="0" cellspacing="0" border="0" rules="none">
                        	<tr>
	                        	<td class="rightBlueTxt">@COMMON.entityType@@COMMON.maohao@</td>
                        		<td id="putDeviceType" class="pB2"></td>
                        		<td class="rightBlueTxt">@COMMON.status@@COMMON.maohao@</td>
                        		<td>
	                        		<select class="normalSel w200" id="onlineStatus">
	                        			<option value="">@COMMON.pleaseSelect@</option>
	                        			<option value="1">@COMMON.online@</option>
	                        			<option value="0">@COMMON.offline@</option>
	                        		</select>
                        		</td>
                        		<td rowspan="2" style="padding-left:10px;">
                        			<a href="javascript:;" class="normalBtn" onclick="queryClick()">
                        				<span><i class="miniIcoSearch"></i>@COMMON.query@</span>
                        			</a>
                        		</td>
                        	</tr>
                        	<tr>
                        		<td class="rightBlueTxt">@COMMON.alias@/@COMMON.name@/IP/MAC@COMMON.maohao@</td>
                        		<td>
                        			<input id="aliasNameIpMac" type="text" class="normalInput w200" 
                        			tooltip="@COMMON.anotherName@" />
                        		</td>
                        		<td class="rightBlueTxt pL10">@COMMON.region@@COMMON.maohao@</td>
                        		<td>
                        			<div id="region_tree"></div>
                        		</td>
                        	</tr>
                        </table>
                        <div id="putGrid2" class="pT10"></div>
                    </div>
                    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
                        <li>
                            <a onclick="prevPage()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoArrLeft"></i>@COMMON.prev@</span>
                            </a>
                        </li>
                        <li>
                            <a onclick="applyClick()" href="javascript:;" 
                            	class="normalBtnBig" disabled="disabled" id="applyBtn">
                                <span><i class="miniIcoSave"></i>@COMMON.apply@</span>
                            </a>
                        </li>
                        <li>
                            <a onclick="cancelClick()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                            </a>
                        </li>
                    </ol>
                </div>
                <div id="selectDeviceTip">@tip.selectDeviceTip@</div>
            </div>
        </div>
    </div>
</body>
</Zeta:HTML>