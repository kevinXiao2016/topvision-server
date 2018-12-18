<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<script src="dispersion.json" type="text/javascript" charset="utf-8"></script>
<Zeta:Loader>
    library ext
    library zeta
    module dispersion
    plugin LovCombo
    plugin DropdownTree
    import js.json2
</Zeta:Loader>

<style type="text/css">
body,html{height:100%;overflow:hidden;}
#query-container{overflow:hidden;padding:10px 0 0 5px;}
#queryContent{width: 330px;padding-left: 5px;}
.queryTable td{padding:0 5px 0 0;}
.queryTable a{color:#B3711A;}
#grid-div{position: relative;}
.cirBoxBg{-moz-border-radius: 12px; -o-border-radius: 12px; -webkit-border-radius: 12px; border-radius: 12px;}
</style>

<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityTypes = ${entityTypes};
var queryData = {
	limit: pageSize
};

function showDispersionDetail(opticalNodeId){
	var record = store.getById(opticalNodeId);
	window.parent.addView("dispersionChart"+opticalNodeId, '@dispersion.chart@-'+record.data.opticalNodeName, "icoH1", "/dispersion/showDispersionDetail.tv?opticalNodeId="+opticalNodeId);
}

function query(){
	//生成查询数据
	//地域
	var folderIds = regionTree.getSelectedIds();
	//设备类型
	var deviceType = $('#deviceType').val();
	//设备名称
	var deviceName = $('#deviceName').val();
	//设备IP
	var manageIp = $('#manageIp').val();
	
	queryData = {
		start:0,
		limit: pageSize,
		deviceName: deviceName,
		manageIp: manageIp,
		deviceType: deviceType,
		folderIds: folderIds
	}
	store.baseParams =  queryData;
	store.load();
}

$(function(){
	//构建设备类型查询条件
	buildDeviceTypeCondition();
	
	//构建地域查询条件
	buildFolderCondition();
	
	//构建离散度列表
	bulidGrid();
	
	function buildFolderCondition(){
		regionTree = $('#region_tree').dropdowntree({
			width: 200
		}).data('nm3k.dropdowntree');
	}
	
	function buildDeviceTypeCondition(){
		var $deviceType = $('#deviceType');
		for(var i=0; i<entityTypes.length; i++){
			$deviceType.append(String.format('<option value="{0}">{1}</option>', entityTypes[i].typeId, entityTypes[i].displayName));
		}
	}
	
	function bulidGrid(){
		var cm = new Ext.grid.ColumnModel([
           {header: "@dispersion.opticalnodename@", align:'left', sortable: true, dataIndex: 'opticalNodeName', renderer: renderOpticalName}, 
           {header: "@dispersion.deviceName@", align:'left', sortable: true, dataIndex: 'cmtsName'},
           {header: "@dispersion.deviceType@", align:'left', width:100, sortable: true, dataIndex: 'typeName'},
           {header: "@dispersion.region@", align:'left', sortable: true, dataIndex: 'folderNames'},
           {header: "@resources/COMMON.uplinkDevice@", align:'left', sortable: true, dataIndex: 'manageIp'},
           {header: "@dispersion.channel@", align:'left', sortable: true, dataIndex: 'channelIds', renderer: renderChannel}, 
           {header: "@dispersion.collectTime@", sortable: true, dataIndex: 'collectTime', renderer: renderTime}, 
           {header: "@dispersion.cmNum@", width:60, sortable: true, dataIndex: 'cmNum'}, 
           {header: "@dispersion.upsnr@</br>@dispersion.avg@(dB)", width:70, sortable: true, dataIndex: 'upSnrAvg'}, 
           {header: "@dispersion.upsnr@</br>@dispersion.std@", width:60, sortable: true, dataIndex: 'upSnrStd'}, 
           {header: "@dispersion.uppower@</br>@dispersion.avg@(dBmV)", width:80, sortable: true, dataIndex: 'upPowerAvg'}, 
           {header: "@dispersion.uppower@</br>@dispersion.std@", width:80, sortable: true, dataIndex: 'upPowerStd'}
    	]);
		
		store = new Ext.data.JsonStore({
		    url: '/dispersion/loadDispersions.tv',
		    root: 'data',
		    idProperty: "opticalNodeId",
		    totalProperty: 'rowCount',
		    remoteSort: true,
		    fields: [
		      'opticalNodeId', 'collectTime','cmNum', 'upSnrAvg', 'upSnrStd',  'upPowerAvg', 'upPowerStd',
		      'cmtsName', 'manageIp', 'channelIds', 'folderNames', 'opticalNodeName', 'typeName'
		    ]
		  });
		  //store在翻页之前加上查询参数
		  store.on('beforeload', function() {
		    store.baseParams =  queryData;
		  });
		
		store.load({
			params: queryData
		});
		
		var bbar = new Ext.PagingToolbar({
		    id: 'extPagingBar',
		    pageSize: pageSize,
		    store: store,
		    displayInfo: true,
		    items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", pageSize), '-']
		  });
		
		var grid = new Ext.grid.EditorGridPanel({
		    cls: 'normalTable',
			bodyCssClass : 'normalTable',	
		    renderTo: 'grid-container',
		    border: true,
		    store: store,
		    cm: cm,
		    bbar: bbar,
		    loadMask: true,
		    sm: new Ext.grid.RowSelectionModel({
		      singleSelect: true
		    }),
		    autoScroll: false,
		    viewConfig: {
		      forceFit: true
		    }
	  	});
		
		resize();
		   
	   $(window).bind('resize', function(e){
		   throttle(resize, 50)();
	   });
	   
	   function resize(){
		   $('#grid-container').height($('body').height()-$('#query-container').outerHeight());
		   grid.setSize($('#grid-container').width(),$('#grid-container').height());
	   }
	   
	   function throttle(fn, delay){
		   var timer = null;
		   return function(){
			   var context = this, args = arguments;
			   clearTimeout(timer);
			   timer = setTimeout(function(){
				   fn.apply(context, args);
			   }, delay);
		   }
	   }
		
	  	function renderOpticalName(value, p, record){
	  		//判断是否没有离散度数据
	  		//upSnrAvg 
	  		//upSnrStd
	  		//upPowerAvg
	  		//upPowerStd
	  		var data = record.data,
	  			upSnrAvg = data.upSnrAvg,
	  			upSnrStd = data.upSnrStd,
	  			upPowerAvg = data.upPowerAvg,
	  			upPowerStd = data.upPowerStd;
	  		if(!upSnrAvg && !upSnrStd && !upPowerAvg && !upPowerStd){
	  			return value;
	  		}else{
		  		return String.format('<a href="javascript:showDispersionDetail({0});">{1}</a>', record.data.opticalNodeId, value);
	  		}
	  	}
	  	
	  	function renderChannel(value, p, record){
	  		var module = '<span class="cirBoxBg">{0}</span>'
	  			, ret='',
	  			chls = value.split(',');
	  		for(var i=0, len=chls.length; i<len; i++){
	  			ret += String.format(module, chls[i]);
	  		}
	  		return ret;
	  	}
		
		function renderTime(value, p, record){
			if(value==null){return '-';}
			var date = new Date();
			date.setTime(value.time);
			return Ext.util.Format.date(date, 'Y-m-d H:i:s');
		}
	}
})
</script>
</head>
<body class="bodyGrayBg">
	<!-- 查询DIV -->
	<div id="query-container">
		<table class="queryTable">
			<tr>
				<td class="rightBlueTxt">@dispersion.region@@COMMON.maohao@</td>
				<td>
					<div id="region_tree"></div>
				</td>
				<td class="rightBlueTxt">@dispersion.deviceType@@COMMON.maohao@</td>
				<td>
					<select id="deviceType" class="normalSel" >
						<option value="-1">@dispersion.pls@</option>
					</select>
				</td>
				<td class="rightBlueTxt">@dispersion.deviceName@@COMMON.maohao@</td>
				<td>
					<input type="text" id="deviceName" class="normalInput" />
				</td>
				<td class="rightBlueTxt">@resources/COMMON.uplinkDevice@@COMMON.maohao@</td>
				<td>
					<input type="text" id="manageIp" class="normalInput" />
				</td>
				<td>
					<a  id="advance-query" href="javascript:query();" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
				</td>
			</tr>
		</table>
	</div> 
	<div id="grid-container"></div>
	
</body>
</Zeta:HTML>