<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<%@include file="/include/ZetaUtil.inc"%>
<%@include file="../include/meta.inc"%>

<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
</Zeta:Loader>

<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value='entityId'/>; 
var deviceType = '<s:property value='deviceType'/>';
var store = null;
function renderOperation(value, p, record){ 
	if(operationDevicePower){
		 var str ="<a href='javascript:;' onclick='modifyPerf(\""+record.get('perfName')+"\",\""+record.get('thresholdType')+"\",\""+record.get('perfThresholdTypeIndex')+"\",\""+record.get('perfThresholdObject')+"\",\""+record.get('perfThresholdUpper')+"\",\""+record.get('perfThresholdLower')+"\")'>@COMMON.modify@</a>";
	}else{
		var str = "<span class='grayTxt'>@COMMON.modify@</span>"
	}
    return str;
}
function modifyPerf(perfName,thresholdType,perfThresholdTypeIndex,perfThresholdObject,perfThresholdUpper,perfThresholdLower){
	window.top.createDialog('modifyPerfThreshold', "@PERF.port.thresholdEdit@", 600, 370, 'epon/perf/showPerfThresholdModifyJsp.tv?entityId=' + entityId + '&deviceType='+deviceType+'&perfName='+perfName+'&thresholdType='+thresholdType+'&perfThresholdTypeIndex='+perfThresholdTypeIndex+ '&perfThresholdObject='+perfThresholdObject+ '&perfThresholdUpper='+perfThresholdUpper+ '&perfThresholdLower='+perfThresholdLower, null, true, true);
}
Ext.onReady(function(){
	//$("#thresholdType").val(perfType?perfType:'SNI');
	var w = document.body.clientWidth-27;
	var h = document.body.clientHeight - 100;
	var cm = new Ext.grid.ColumnModel([
    	{header: "@PERF.port.thresholdIndex@",dataIndex:"perfName", width:80,sortable:false,align:'left'},
		{header: "@PERF.port.thresholdUpper@",dataIndex:"perfThresholdUpper", width:50,sortable:false,align:'center'},
		{header: "@PERF.port.thresholdLower@",dataIndex:"perfThresholdLower", width:50,sortable:false,align:'center'},
		{header: "@COMMON.manu@",width:30, sortable:false,align:'center',renderer:renderOperation}
     ]);
	store = new Ext.data.JsonStore({
        url: 'epon/perf/loadPerfThresholdByType.tv',
        root: 'data',            
        remoteSort: false,
        fields: ['entityId','perfName','thresholdType', 'perfThresholdTypeIndex', 'perfThresholdObject', 
            {name:'perfThresholdUpper',type:'String'},
            {name:'perfThresholdLower',type:'String'}],
        //sortInfo:{field: 'sucRate', direction: 'ASC'},
        //排序规则  
        sortData: function(field, direction){
            var fn;
            //关键地方,重写排序排序规则
            if("perfThresholdUpper" == field || "perfThresholdLower" == field){
                fn = function(rec1, rec2){
                    var v1 = rec1.get(field),
                        v2 = rec2.get(field);
                    if(!v1 || !v2){
                        return !v1 ? 1 : -1;
                    }else{
                        return v1 == v2 ? 0 : compare(v1,v2) ? 1 : -1;
                    }
                };
                this.data.sort(direction, fn);
            }else{
                direction = direction || 'ASC';
                var st = this.fields.get(field).sortType;
                fn = function(r1, r2){
                    var v1 = st(r1.data[field]), v2 = st(r2.data[field]);
                    return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
                };
                this.data.sort(direction,fn);
            }
        }
    });
	store.load({params: {entityId:entityId,thresholdType:'SNI'}});
	var grid = new Ext.grid.GridPanel({
		renderTo:"show",
		store:store,
        border: false,
		height:340,
		loadMask:{msg:"@COMMON.loading@"},
		cm:cm,
			viewConfig: {
			forceFit: true
		}
	});
    /* var body = new Ext.form.FormPanel({
		id:"hbox",
        renderTo:"hbox",
        height:h,
        width:w,
        layout:{
            type:'hbox',
            align:'stretchmax',
            pack:'start'
        },
        frame:false,
        border:false,
        defaults:{
            layout:'form',
            frame:false,
            border:false
        },
        flex:1,
        items:[
        	{width:10},
            {
		        height:Ext.getBody().getHeight(),
		        width:w,
                layout:{
		            type:'vbox',
		            align:'stretchmax',
		            pack:'start'
		        },
		        defaults:{
		            layout:'fit',
		            frame:false,
		            border:false
		        },
		        flex:1,
		        items:[
					{height:50,
			        contentEl:'search'
					},
		            {
		            	width:w,
		            	height:h,
	        			border:true,
		                flex:1,
		                items:[{el:'show',frame:false,border:false}]
		            },
		            {
		            	contentEl:'okbt'
		            }
		        ]
            },{width:15}
        ]
    });
    new Ext.Viewport({layout:'fit', items:[body]}); */
});
function onRefreshClick() {
	var perfType = $("#thresholdType").val();
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/perf/refreshPerfThreshold.tv?r=' + Math.random(),
  		success : function(text) {
  			if (text.responseText != null && text.responseText != "success") {  
  				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchError@" , 'error');
  			} else {
  				top.closeWaitingDlg();
				 top.nm3kRightClickTips({
	   				title: '@COMMON.tip@',
	   				html: "@COMMON.fetchOk@"
	   			 });
  	           //window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
  	           //window.location.reload();
  	           store.load({params: {entityId:entityId,thresholdType:perfType,deviceType:deviceType}});
  			} 
  		},
  		failure : function() {
  			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchError@");
  		},
  		params : {
  			entityId: entityId
  		}
  	});
}
function cancelClick() {
	window.parent.closeWindow('perfThresholdConfig');	
}
function changeType(){
	var thresholdType = $("#thresholdType").val();
	if(thresholdType == "SNI"){
		store.load({params: {entityId:entityId,thresholdType:'SNI'}});
	}else if(thresholdType == "PON"){
		store.load({params: {entityId:entityId,thresholdType:'PON'}});
	}else if(thresholdType == "ONUPON"){
		store.load({params: {entityId:entityId,thresholdType:'ONUPON'}});
	}else if(thresholdType == "ONUUNI"){
		store.load({params: {entityId:entityId,thresholdType:'ONUUNI'}});
	}else if(thresholdType == "TEMPERATURE"){
		store.load({params: {entityId:entityId,thresholdType:'TEMPERATURE',deviceType:deviceType}});
	}
}
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<ul class="leftFloatUl">
			<li class="blueTxt pT3">@PERF.port.plsSelThresholdType@: </li>
			<li>
				<select id="thresholdType" class="normalSel"
							style="width: 190px; padding-left: 5px;" onchange="changeType()">
							<option value="SNI">@PERF.port.oltSniPort@</option>
							<option value="PON">@PERF.port.oltPonPort@</option>
							<option value="ONUPON">@PERF.port.onuPonPort@</option>
							<option value="ONUUNI">@PERF.port.onuUniPort@</option>
							<option value="TEMPERATURE">@PERF.port.temperature@</option>
				</select>
			</li>
		</ul>
		<div id="show" class="pT10 clearBoth normalTable"></div>
		<div id="okbt" class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id=fetch onclick="onRefreshClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
		     </ol>
		</div>
	</div>

	<div id="hbox"></div>
	<div id="search"></div>
	
	
</body>
</Zeta:HTML>