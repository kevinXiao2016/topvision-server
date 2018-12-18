<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library zeta
    module cmc
</Zeta:Loader>
<style type="text/css">
	.buttonClazz {
		padding-left: 150px;margin-top: 10px;
	}
</style>
</head>
<script type="text/javascript">
  var rangeDetail = '${rangeDetail}';
  Ext.onReady(function(){
	  var data = new Array;
	  var list = rangeDetail.split(",")
	  for(var i=0;i<list.length;i++){
		  var range = list[i].trim().split("--");
		  //去除 ""
		  for(var j=0; j<range.length;j++){
			  if(range[j]==""){
				  delete range[j];
			  }
		  }
		  data.push(range);
	  }
	  
	  new Ext.grid.GridPanel({
			store: new Ext.data.ArrayStore({
				data : data,
				fields  :["startMac","terminateMac"]
			}),
			renderTo:"rangeListTable",
			height : 170,
			border: true,
	        columns: [
	        	{header: "@loadbalance.startMac@", width:162, dataIndex: 'startMac'},
	        	{header: "@loadbalance.terminateMac@", width:162,  dataIndex: 'terminateMac'}
	        ]
		});
  });
/***********************************
                             关闭页面
 ************************************/
function closeHandler(){
    window.parent.closeWindow('rangeDetail');
}


</script>
<body class="POPUP_WND">
    <div id="rangeListTable"></div>
    
    <div class="buttonClazz">
        <BUTTON id="closeBt" class="BUTTON75" onclick="closeHandler()">@COMMON.close@</BUTTON>
    </div>
</body>
</Zeta:HTML>