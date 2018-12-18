<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=u8lHaOCM6WAmwPwXQFOrb0v9"></script>
<Zeta:Loader>
    library ext
    library jQuery
    library zeta
    module pnmp
    import js/highcharts-4.1.5/highcharts
    import js/highcharts-4.1.5/highcharts-more
    import terminal/pnmp/js/pnmpUtil
    import terminal/pnmp/js/pnmp-cmts-detail
</Zeta:Loader>

<style type="text/css">
.grayBorder{ border: 1px solid #ccc;}
.blackSpan, .redSpan, .yellowSpan, .greenSpan, .graySpan{
	color: #fff;
	border-radius: 8px;
	padding: 2px 4px;
	background: #000;
}
.redSpan{ background: #E83C23;}
.yellowSpan{ background: #f38900;}
.greenSpan{ background: #449d44; }
.graySpan{ background: #ccc;}

#center-container{
	overflow: scroll;
}
#left-container {
	height: 100%;
}

#chart-container {
	height: 250px;
}

#center-container {
	/* background-color: white; */
	height: 100%;
}

#cm-chart-container {
	height: 320px;
}

.center-title {
	font-size: 16px;
	font-weight: bold;
	color:#B3711A;
}

#tip-div{ position:absolute; top:375px; right:0px;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}

.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }

.yellow-div{height:16px;width:16px;background-color: #f38900;}
.green-div{height:16px;width:16px;background-color: #449d44;}
.red-div{height:16px;width:16px;background-color: #E83C23;}

.red-circle{
	width: 14px;
	height: 14px;
	border-radius: 7px;
	background-color: #E83C23;
}
.yellow-circle{
	width: 14px;
	height: 14px;
	border-radius: 7px;
	background-color: #DCD345;
}
.green-circle{
	width: 14px;
	height: 14px;
	border-radius: 7px;
	background-color: #008000;
}
.badColor {
	color: #E83C23;
}
.marginalColor {
	color: #f38900;
}
.healthColor {
	color: #449d44;
}
</style>

<script type="text/javascript">
var sm, store, grid, bbr;
var map;
var cmcId = '${cmcId}';
var cmcName = '${cmcName}';
var ipAddress = '${ipAddress}';
var maxUpChannelWidth = '${maxUpChannelWidth}'/1000000;
var cityName;
var myGeo = new BMap.Geocoder();
var adds = []; //待显示在地图上的地址
var index = 0; //地址数组的下标，每次reload数据应该重新置0
var thresholdMap = {};
var cmSignalThresholdMap = {};


//重新reload
function reload(){
    buildCorrelationGroup()
    reloadCmData()
}
//重新reload
function reloadCmData(){
	if(store){
		var correlationGroup = $("#correlationGroup").val();
		store.baseParams = {
			cmcId: cmcId,
			correlationGroup: correlationGroup
		};
		store.load({
			callback:function(){
				showSiteOnMap();
				var records = getRecords(store);
				countCmNumByGroup(records);
				generateGroupGraph(records);
		}});
	}
}

// 切换故障分组
function showGroupDetail() {
	//重置地址下标
	index = 0;
	reloadCmData();
}

// start 地图相关
function showMap() {
	map = new BMap.Map("map-container", {minZoom:10,maxZoom:19});
	function myFun(result){
		cityName = result.name;
		map.setCurrentCity(cityName);          // 设置地图显示的城市 此项是必须设置的
		map.centerAndZoom(cityName, 11);  // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		//地图显示气泡
		showSiteOnMap();
	}
	var myCity = new BMap.LocalCity();
	myCity.get(myFun);
}

//地址解析
function bdGEO(){
	var add = adds[index];
	geocodeSearch(add);
	index++;
}
	
function geocodeSearch(add){
	if(index < adds.length -1){
		setTimeout(window.bdGEO,200);
	} 
	myGeo.getPoint(add, function(point){
		if (point) {
			var address = new BMap.Point(point.lng, point.lat);
			map.centerAndZoom(point, 18);
			addMarker(address,new BMap.Label(add,{offset:new BMap.Size(20,-10)}));
		} else{
		}
	}, cityName);
}

//编写自定义函数,创建标注
function addMarker(point,label){
	var marker = new BMap.Marker(point);
	map.addOverlay(marker);
	marker.setLabel(label);
}

function showSiteOnMap(){
	//清除上次的覆盖物
	map.clearOverlays();
	//地址重置
	adds = [];
	index = 0;
	store.each(function(record){
		adds.push(record.data.cmAddress);
	});
	//解析地址
	bdGEO();
}
// end 地图相关

function buildCorrelationGroup() {
    $.ajax({
        type: "GET",
        url: "/pnmp/cmtsreport/loadCorrelationGroup.tv?cmcId=" + cmcId,
        dataType: "json",
        async : false,
        success: function(correlationGroupsArray){
            var correlationGroup = $("#correlationGroup").val();
            if (correlationGroup == null && correlationGroupsArray.length == 0) {
                correlationGroup = -1
            } else if (correlationGroup == null && correlationGroupsArray.length > 0) {
                correlationGroup = -1
            }
            var options = "";
            options += '<option value="-1">' + 'Group-all' + '</option>';
            for(var i = 0; i < correlationGroupsArray.length; i++){
                options += '<option value="' +correlationGroupsArray[i].groupId+ '">' + "Group-" + correlationGroupsArray[i].groupId + '</option>';
            }
            $("#correlationGroup").html(options);
            $("#correlationGroup").val(correlationGroup)
        }
    });
}
	
</script>
	</head>
	<body class="whiteToBlack">
		<div id="center-container">
			<div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB20 pT20 noWidthCenter">
		            <li class="blueTxt" style="padding-top:8px;">
		            	@pnmp.correlationGroup@@COMMON.maohao@
		            </li>
		            <li>
		            	<select id="correlationGroup" class="normalSel" style="height:32px; padding-left: 5px; padding-right:5px;" onchange="showGroupDetail()">
						</select>
					</li>
					<li>
		            	<a id="advance-query" href="javascript:reload();" class="normalBtnBig"><span><i class="miniIcoRefresh"></i>@pnmp.reload@</span></a>
					</li>
		        </ol>
		    </div>
			<div>
				<div id="tab-container" style="width: 100%;height:500px;position: relative;">
					<div class="grayBorder" style="height:500px;width: 49.5%; position:absolute; top:0; left:0; background:#fff;">
						<div id="map-container" style="width:100%; height: 460px;"></div>
						<ul class="leftFloatUl pT10" style="border-top:1px solid #ccc; width:100%">
							<li class="center-title pL5">@pnmp.cmtsInfo@</li>
							<li class="rightBlueTxt pT3 pL10">@pnmp.nameIp@@COMMON.maohao@</li>
							<li id="cmtsInfo" class="pT3"></li>
							<li style="float:right;" class="pT3">
								<label class="blueTxt">@pnmp.statistics@@COMMON.maohao@</label>
								<span id="totalNum" class="blackSpan"></span>
								<span id="badNum" class="redSpan"></span>
								<span id="margialNum" class="yellowSpan"></span> 
								<span id="healthNum" class="greenSpan"></span>
							</li>
						</ul>
					</div>
					<div id="a-container" class="grayBorder" style="height:500px;width: 49.5%; position:absolute; top:0; right:0;">
						<div id="spectrum-respond-chart-container" style="height:49.9%;width: 100%;"></div>
						<div id="tap-coefficients-chart-container" style="height:49.9%;width: 100%;"></div>
					</div>
				</div>
			</div>
			<div id="cm-group-table-container" style="margin-top: 10px;">
			</div>
		</div>
	</body>
</Zeta:HTML>