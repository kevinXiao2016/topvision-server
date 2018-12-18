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
		    library FileUpload
		    module CMC
		    CSS css/white/disabledStyle
		</Zeta:Loader>
		<style type="text/css">
			.stepLoading, .setpFail, .stepSuccess{
				display:block; padding:2px 0px 2px 24px; 
				background:url(/images/refreshing2.gif) no-repeat 4px center;
			}
			.setpFail{background:url(/images/wrong.png) no-repeat 4px center;}
			.stepSuccess{ background:url(/images/yes.png) no-repeat 6px center;}
		</style>
		<script type="text/javascript">
			var chooseEntityIds = '${chooseEntityIds}';
		    var cm,columnModels,sm,store,grid;
		    var interval = null;
		    var STEP = {
		    	'0': '@tip.step0@',  //正在初始化;
		    	'1': '@tip.step1@',  //下发配置成功;
		    	'2': '@tip.step2@',  //下发配置失败;
		    	'3': '@tip.step3@',  //升级文件总大小超过限制;
		    	'4': '@tip.step4@',  //正在清除自动升级配置...;
		    	'5': '@tip.step5@',  //正在清除CM升级文件...;
		    	'6': '@tip.step6@',  //清除自动升级配置失败;
		    	'7': '@tip.step7@',  //清除CM升级文件失败;
		    	'8': '@tip.step8@',  //正在下发自动升级配置...;
		    	'9': '@tip.step9@',  //正在上传升级文件;
		        '10':'@tip.step10@'  //版本不支持;
		    }
		    //通过step步骤,展示对应的图标(三种状态：loading,打钩，打叉);
		    function showIcon(step){
		    	switch(step){
		    	case 1:
		    		return 'stepSuccess';
		    	    break;
		    	case 2:
		    	case 3:
		    	case 6:
		    	case 7:
		    	case 10:
		    		return 'setpFail';
		    		break;
		    	default :
		    		return 'stepLoading'
		    		break;
		    	}
		    }
		    
		    Ext.onReady(function(){
		    	//进入页面，先下发配置;
		    	initApply();
		    	
		        columnModels = [
		            {header: "<div class=txtCenter>@COMMON.alias@</div>", width:75, dataIndex: "name", 
		            	align: "left", sortable: false},
		            {header: "MAC", width:35, dataIndex: "mac", align: "center", sortable: false},
		            {header: "<div class=txtCenter>@SERVICE.applyResult@</div>", width:100, dataIndex: "step", 
		            	align: "left", sortable: false, renderer:resultRender}
		        ];
		        cm = new Ext.grid.ColumnModel({
		            defaults : {
		                menuDisabled : true
		            },
		            columns: columnModels
		        });
		        store = new Ext.data.JsonStore({
		            url: "/cmupgrade/loadAutoUpgradeProcess.tv",
		            root: "data",
		            totalProperty: "rowCount",
		            remoteSort: true,
		            fields: ["name","mac","step","result"]
		        });
		        store.load({
		        	callback: function(data){
		        		intervalGetData();
		        	}
		        });
		        // set interval
		        grid = new Ext.grid.GridPanel({
		        	title: '@SERVICE.applyResult@',
		        	paddings: '10 10 0 10',
		        	renderTo: 'putGrid',
		        	height: 360,
		            stripeRows: true,
		            cls: "normalTable",
		            bodyCssClass: "normalTable",
		            region: "center",
		            store: store,
		            cm: cm,
		            viewConfig:{ forceFit: true }
		        });
		    });//end document.ready;
			function cancelClick(){
				window.parent.closeWindow('modalDlg');
			}
			//下发配置;
			function initApply(){
				$.ajax({
					url: '/cmupgrade/applyAutoUpgradeConfig.tv',
					type: 'POST',
					cache: false,
					dataType: 'json', 
					data : {
						chooseEntityIds : window.chooseEntityIds
					},
					success: function(json) {
					},
					error: function() {
						window.top.showErrorDlg();
					}
				});
			}
			//返回结果
			function resultRender(v, o, r){
				return String.format('<label class="{0}">{1}</label>',showIcon(v), window.STEP[v]);
			}
			function intervalGetData(){
				interval = setInterval(function(){
					store.reload({callback:function(){
						if(store.getAt(0).data.result){ //任何一条数据中的result为true均为完成;
							window.clearInterval(interval);
							top.afterSaveOrDelete({
				        		title : '@COMMON.tip@',
				        		html  : '@tip.complete@'
				        	})
						}
					}})
				}, 1000);
			}
		</script>
	</head>
	<body class="openWinBody">
		<div id="putGrid" class="edge10"></div>
		<div class="noWidthCenterOuter clearBoth">
        <ol class="upChannelListOl pB0 pT0 noWidthCenter">
            <li>
            	<a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
            		<span><i class="miniIcoClose"></i>@COMMON.close@</span>
            	</a>
            </li>
        </ol>
    </div>
	</body>
</Zeta:HTML>