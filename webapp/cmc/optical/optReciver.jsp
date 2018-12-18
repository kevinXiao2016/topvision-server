<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css">
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import /js/json2
    import js.jquery.Nm3kTabBtn
    import /cmc/optical/cfa-structure
    import /cmc/optical/cfb-structure
    import /cmc/optical/cfc-structure
    import /cmc/optical/cfd-structure
    import /cmc/optical/ef-structure
    import /cmc/optical/ep-structure
    import /cmc/optical/ff-structure
    import /cmc/optical/optreciver
    css css.white.disabledStyle
</Zeta:Loader>
<style type="text/css">
.column-span {
    display: inline-block;
    margin-right: 20px;
    vertical-align: middle;
    font-size: 12px;
}
.column-span input, 
.column-span label {
    vertical-align: middle;
}
.column-span input, 
.column-span label {
    vertical-align: middle;
}
.hide{
    display: "";
}
#cover-container, 
#schematic-container{
    background-repeat: no-repeat;
}
#test{
    position: absolute;
    left: 200px;
    top: 200px;
}
</style>
<script type="text/javascript">
var cmcId = '${cmcId}';
var topCcmtsSysDorType = '${cmcAttribute.topCcmtsSysDorType}';
var cmcIndex = '${cmcAttribute.cmcIndex}';
vcEntityKey = 'cmcId';
</script>
</head>
<body>
    <div id="header">
        <div id="device-tab-container">
	        <%@ include file="/cmc/entity.inc"%>
        </div>
        
        <div id="tab-container" class="edge10"></div>
    </div>
    
    <div id="center-container" style="height:100%;">
        <!-- 表单 -->
        <div class="pT0 clearBoth tabBody" id="form-container" style="height:100%;">
            <form id="data-form">
		        <div id="structure"></div>
		    </form>
		    
		    <div id="operation-div" class="clearBoth pT10 pL10 pR10 pD10" style="display: none;">
		      <div class="noWidthCenterOuter clearBoth">
		          <ol class="noWidthCenterOl pB0 pT10 noWidthCenter">
		              <li>
			             <a id="load" class="normalBtnBig hide" href="javascript:;"  onclick="getDataFromDevice()">
			                 <span><i class="miniIcoEquipment"></i>@BUTTON.fetch@</span>
			             </a>
		              </li>
		              <li>
			            <a id="save" class="normalBtnBig hide" href="javascript:;"  onclick="saveOptData()">
			                <span><i class="miniIcoData"></i>@BUTTON.save@</span>
			            </a>
		              </li>
		              <li>
			            <a id="reset" class="normalBtnBig hide" href="javascript:;"  onclick="reset()">
			                <span><i class="miniIcoBack"></i>@opt.restore@</span>
			            </a>
		              </li>
		              <li>
			            <a id="update" class="normalBtnBig hide" href="javascript:;"  onclick="showUpgrade()">
			                <span><i class="miniIcoArrUp"></i>@opt.firmwareUpdate@</span>
			            </a>
		              </li>
		          </ol>
		      </div>
		    </div>
        </div>
        
        <!-- 原理框图 -->
        <div class="pT0 clearBoth tabBody" id="schematic-container" style="display:none;">
        </div>
        
        <!-- 盖板图 -->
        <div class="pT0 clearBoth tabBody" id="cover-container" style="display:none;position: relative;">
        </div>
        
    </div>
</body>
</Zeta:HTML> 
