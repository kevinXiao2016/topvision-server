<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module oltdhcp
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
	body{ max-width: 840px;}
	.card{width: 160px; height: 218px; background: #fff; float:left; margin-right: 10px; margin-bottom: 10px; overflow:hidden; padding: 15px 10px;
		-webkit-border-radius: 5px;
		   -moz-border-radius: 5px;
		    -ms-border-radius: 5px;
		     -o-border-radius: 5px;
		        border-radius: 5px;
		 box-shadow:0px 2px 10px rgba(0, 0, 0, 0.1 );
	}
	.card .grayTxt{ padding:22px 0px 0px; }
	.card .num{ font-size: 16px; font-weight: 700; font-family: Verdana, Geneva, sans-serif; color: #666;}
	
	.fz16{ font-size: 16px; line-height: 1em; font-weight: 700;}
</style>
<script type="text/javascript">
    var entityId = '${entityId}';
    var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
    $(function(){
    	initData(function(){
    		createCard(arguments[0]);
    	});
    });
    function createCard(data){
    	console.log(data)
    	var tpl = new Ext.XTemplate(
  		    '<tpl for=".">',
  		        '<div class="card">',
  		        	'<p class="blueTxt fz16">{title}</p>',
  		        	'<tpl if="typeof(receive) == &quot;number&quot;">',
  		        		'<p class="grayTxt">@oltdhcp.statistics.receive@</p>',
  		        		'<p class="num">{receive}</p>',
  		        	'</tpl>',
  		        	'<tpl if="typeof(send) == &quot;number&quot;">',
		        		'<p class="grayTxt">@oltdhcp.statistics.send@</p>',
		        		'<p class="num">{send}</p>',
		        	'</tpl>',
		        	'<tpl if="typeof(discard) == &quot;number&quot;">',
	        			'<p class="grayTxt">@oltdhcp.statistics.drop@</p>',
	        			'<p class="num">{discard}</p>',
	        		'</tpl>',
  		        '</div>',
  		    '</tpl></p>'
  		);
  		tpl.overwrite($('#putCard').get(0), data);
    }
    
    function initData(callback){
    	var data = [];
    	$.ajax({
			url : '/epon/oltdhcp/loadOltPppoeStatistics.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			dateType : 'json',
			async: false,
			success : function(json) {
				if (json) {
					data = adjustmentData(json);
				}
			},
			error : function() {},
			cache : false,
			complete: function(){
				callback(data)
			}
		});
    }
    function adjustmentData(json){
    	var data = [{
        	title: 'PPPoE+<br />@oltdhcp.statistics.messages@',
        	receive: json.topOltPppoeStatReceive,
        	send: json.topOltPppoeStatTransmit,
        	discard: json.topOltPppoeStatDrop
        }];
    	return data;
    }
    
    function refreshBtn(){
    	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.statistics.refresh@....", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/refreshOltPppoeStatistics.tv",
	        method:"post",
	        data : {
				entityId : entityId
			},
	        dataType:'text',
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">@oltdhcp.statistics.refresh@@oltdhcp.success@！</b>'
	            });
	            window.location.reload();
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.statistics.refresh@@oltdhcp.fail@！");
	        }
	    });
    }
    
    function clearBtn(){
    	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.statistics.clear@....", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/clearPppoeDhcpStatistics.tv",
	        method:"post",
	        data : {
				entityId : entityId
			},
	        dataType:'text',
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">@oltdhcp.statistics.clear@@oltdhcp.success@！</b>'
	            });
	            window.location.reload();
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.statistics.clear@@oltdhcp.fail@！");
	        }
	    });
    }
    
    function authLoad() {
    	//权限控制
    	if(!refreshDevicePower) {
    		$("#refreshBtn").attr("disabled",true);
    	}
		if(!operationDevicePower) {
			$("#saveBtn").attr("disabled",true);
    	}
    }
</script>

</head>
    <body class="grayBg edge10 pT0" onload="authLoad()">
    	<div id="putCard"></div>
    	<ol class="upChannelListOl pB0 pT20 noWidthCenter clearBoth">
		    <li><a id="refreshBtn" href="javascript:;" onclick="refreshBtn()" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.fetch@</span></a></li>
		    <li><a id="saveBtn" href="javascript:;" onclick="clearBtn()" class="normalBtnBig"><span><i class="miniIcoForbid"></i>清除统计信息</span></a></li>
		</ol>
    </body>
</Zeta:HTML>