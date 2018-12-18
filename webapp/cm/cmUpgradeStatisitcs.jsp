<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<title></title>
		<%@include file="/include/ZetaUtil.inc"%>
		<Zeta:Loader>
			library Jquery
		    library ext
		    library zeta
		    library highchart
		    module CMC
		</Zeta:Loader>
		<script type="text/javascript">
			var tbar, viewPort, chartsObj={};
			Ext.onReady(function() {
				tbar = new Ext.Toolbar({
					renderTo : 'putTbar',
					items : [{
						xtype : 'tbspacer',
						width : 10
					},{
						xtype : 'tbtext',
						text  : '@COMMON.type@@COMMON.maohao@'
					},{
						xtype : 'component',
						html  : '<select id="typeSel" type="text" class="normalSel w150 mR5"></select>' 
					},{
						text : '@COMMON.search@',
						iconCls : 'bmenu_find',
						handler : queryClick
					},'-',{
						text : '@COMMON.refresh@',
						iconCls : 'bmenu_refresh',
						handler : refreshClick
					}]
				});	
				viewPort = new Ext.Viewport({
					layout : 'border',
					defaults : {
						border : false
					},
					listeners : {
						afterrender : function(){
							getColumnsData(2,'@CMC.text.loading@');
						}
					},
					items : [{
						region : 'north',
						contentEl : 'putTbar'
					},{
						region : 'center',
						contentEl : 'main',
						autoScroll : true
					}]
				});

			});//document.ready;
			//刷新;
			function refreshClick(){
				window.top.showConfirmDlg('@COMMON.tip@','@COMMON.waitingLongTime@',function(value){
				    if(value != 'yes'){
				    	return;
				    }
				    $(".jsShowDiv").remove();
					getColumnsData(1,'@CM.loadAllCmVersion@');
				});
			}
			//获取柱状图的数据;
			//num为1去数据库刷新，num为2设备刷新;
			function getColumnsData(num, tip){
				Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', tip));
				$.ajax({
					url: '/cmupgrade/loadCmVersionStatics.tv',
					data: {
						cmVersionInfoType: num
					},
					type: 'POST',
					cache: false,
					dataType: 'json', 
					success: function(json) {
						Ext.getBody().unmask();
						var returnObj = getTransformData(json);
						//创建类型下拉框;
						createTypeSel(returnObj.selectData);
						//创建柱子图;
						createColumns(returnObj.arr);
					},
					error: function() {
						Ext.getBody().unmask();
						window.top.showErrorDlg();
					}
				});
			}
			/** 
			 * 
			 * 后台得到的数据格式是 **********************************************
			 * {类型1 : { 版本 1 ： 9(台), 版本 2 ： 11},
			 *  类型2 : { ...}}
			 * 
			 * 需要转换成 *******************************************************
			 * [{
		     *     createDivId : 'column1', //创建一个div，并且dom中不能有这个id;
			 *	   title : 'ModuleNum 1', 
			 *	   xAxis_categories : ['版本 1','版本 2','版本 3','版本 4','版本 5'],
			 *	   series_data : [1,2,3,3,5]
			 * },{ ... }]
			 ******************************************************************
			 *
			 * 同时tbar中类型，也需要用到数据，组成下拉框;
			 */
			function getTransformData(obj){
				var returnObj = {
					arr : [],       //图表要用到的数据;
					selectData : [] //下拉框要用到的数据;
				};
				
				for(i in obj){
					returnObj.selectData.push(i.toString());
					var newObj = {};
					newObj.createDivId = i;
					newObj.title = i;
					var xAxis_categories = [];
					var series_data = [];
					for(j in obj[i]){
						xAxis_categories.push(j);
						series_data.push(obj[i][j]);
					}
					newObj.xAxis_categories = xAxis_categories;
					newObj.series_data = series_data;
					returnObj.arr.push(newObj);
				}
				return returnObj;
			}
			//创建类型下拉框;
			function createTypeSel(arr){
				var str = '<option value="-1">@COMMON.all@</option>';
				$.each(arr, function(i, v){
					str += String.format('<option value="{0}">{0}</option>', v);
				});
				$("#typeSel").html(str);
			}
			//加载多个柱子;
			function createColumns(arr){
				$.each(arr, function(i, v){
					createColumn(v);
				});
			}
			//加载一个柱子;
			function createColumn(obj){
				var div = String.format('<div id="{0}" class="jsShowDiv mB20"></div>', obj.createDivId);
				$("#main").append(div);
				window.chartsObj['column_'+ obj.createDivId] = new Highcharts.Chart({
			        chart: {
			        	renderTo: obj.createDivId, 
			        	animation : false,
			            type: 'column'
			        },
			        title: {
			            text: obj.title
			        },
			        credits: {enabled : false},
			        xAxis: {
			        	allowDecimals: false,
		            	title : false,
		            	categories:  obj.xAxis_categories,
			            labels: {
			                style: {
			                    fontSize: '13px',
			                    fontFamily: 'Verdana, sans-serif'
			                }
			            }
			        },
			        yAxis: {
			            min: 0,
			            title: false
			        },
			        legend: {
			            enabled: false
			        },
			        tooltip: {
			        	formatter : function(){
			        		return String.format('{0}: <b>{1}@base/UNIT.tai@</b>', this.x, this.y);
			        	}
			        },
			        series: [{
			        	color: '#058DC7',
			            name: 'null',
			            data: obj.series_data,
			            dataLabels: {
			                align: 'center',
			                color: '#000000',
	                        enabled: true,
			                verticalAlign: 'bottom',
	                        formatter: function() {
	                            return  this.y > 0 ? this.y + " @base/UNIT.tai@" : null;
	                        }
			            }
			        }]
			    });
			}
			//先将main下所有的div都隐藏,在显示选中的;
			function queryClick(para){
				var v = $("#typeSel").val();
				if(v === '-1'){
					$(".jsShowDiv").css({display: 'block'});
				}else{
					$(".jsShowDiv").css({display: 'none'});
					$("#main").find("#"+v).css({display: 'block'});
				}
			}
			
		</script>
	</head>
	<body class="whiteToBlack">
		<div id="putTbar"></div>
		<div id="main">
						
		</div>
	</body>
</Zeta:HTML>