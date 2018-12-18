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
    module cmc
</Zeta:Loader>
<style type="text/css">
	#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px;}
	#w1600{ width:1600px; position:absolute; top:0; left:0;}
	#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
	#step1{ left:800px;}
	#putTree{ padding:5px; border:1px solid #ccc; background:#f1f1f1; height:300px; margin:10px 0px 0px 10px;}
	.x-tree-node-leaf .x-tree-node-icon{ background:url(../../css/white/tree/folder-open.gif) no-repeat;}
</style>
<script type="text/javascript">
	var name = '${entity.name}';
	var entityId = '${entityId}';
	var pageId = '${pageId}';
	var softVersion = '${cmcAttribute.topCcmtsSysSwVersion}';
	var replaceCmcId;
	var pageId = 'framecmcList';
	
	var cm,sm,grid,store,tree,treeStore;
	Ext.onReady(function(){
		sm = new Ext.grid.CheckboxSelectionModel({
			header : '<div></div>',
			singleSelect : true
		}); 
		cm = new Ext.grid.ColumnModel([
		    sm,
			{id:'entityName',header: '<div class="txtCenter">@COMMON.alias@</div>', sortable:true, align : 'left', dataIndex: 'name'},
	        {header: '<div class="txtCenter">@CCMTS.uplinkdevice@</div>' , width: 100, sortable:true, align : 'left', dataIndex: 'entityIp'},
	        {header: '@CCMTS.macAddress@', width: 120, sortable:true,align: 'center', dataIndex: 'mac', renderer: renderMac},
	        {header: '<div class="txtCenter">@COMMON.name@</div>', width: 80, sortable:true,align: 'left', dataIndex: 'entityName'},
	        {header: '@CMC.label.status@', width:50 , sortable:true, align : 'center', dataIndex: 'status',renderer: renderSysStatus, menuDisabled: true},
	        {header: '<div class="txtCenter">@CCMTS.softVersion@</div>', width:150 , sortable:true,align : 'left', dataIndex: 'softVersion',renderer:addCellTooltip},
	        {header: '@CCMTS.createTime@',width:150, sortable:true,dataIndex:'createTime'}
		]);
		//[{"createTime":"2016-03-30 11:31:40.0","entityId":0,"entityIp":172,"entityName":"","mac":"00:24:68:50:4F:E7","name":"Topvision","softVersion":"V2.1.1.26;430B3-V2.0.0.7","status":0}]
		store = new Ext.data.JsonStore({
	        url: '/cmc/replace/loadCmcReplaceList.tv?cmcId='+entityId,
	        remoteSort: true,
	        autoLoad : true,
	        fields: ['entityId','name','entityIp','mac','entityName','status','softVersion','createTime']
	    });
		store.setDefaultSort('createTime', 'ASC');
		//store.load();
		grid = new Ext.grid.GridPanel({
	        cm     : cm,
			sm     : sm,
	        cls    : 'normalTable',
	        store  : store,
	        border : false,
	        title  : '@CCMTS.replace.chooseEntity@' + name,
	        width  : 780,
	        height : 360,
	        renderTo   : 'putGrid',
	        stripeRows : true,
	        viewConfig : {forceFit:true}
	    });
	    
	
	});//end document.ready;
	function renderMac(value, p, record){
		if(record.data.mac == "" || record.data.mac == null){
			return "--";
		}else{
			return record.data.mac;
		}
	}
	function renderSysStatus(value, p, record) {
		if (record.data.status == '1') {
	        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
	                I18N.CMC.label.online);
	    } else {
	        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
	                I18N.CMC.label.offline);
	    }
	}
	function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
	    var cellValue = data;
	    if(cellValue == "" || cellValue == null){
	    	return "--";
	    }
	    metadata.attr = ' ext:qtip="' + cellValue + '"';
	    return cellValue;
	}
	//取消;
	function cancelClick() {
		window.top.closeWindow('replaceCmcI');
	}
	//下一页;
	function gotoNextPage(){
		if(sm.getSelections().length == 1){
			var selectVersion = sm.getSelections()[0].data.softVersion;
			//版本是否一致问题;
			if(softVersion === selectVersion){ //版本一致;
				nextPage();
				loadTreeData();
			}else{//版本不一致
				top.showConfirmDlg('@COMMON.tip@','@CCMTS.replace.versionDifTip@', function(para){
					if(para === 'yes'){
						nextPage();
						loadTreeData();
					}
				});	
			}	
		}else{
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html  : '@CCMTS.replace.chooseOneEntity@'
			})
		}
	}
	function nextPage(){
		$("#w1600").animate({left:-800},'fast');
	}
	//上一页;
	function prevPage(){
		$("#w1600").animate({left:0},'fast');
	}
	//加载树菜单的数据;
	function loadTreeData(){
	    replaceCmcId = sm.getSelections()[0].data.entityId;
		$.ajax({
			url     : '/cmc/replace/loadCmcReplaceConfigFile.tv?cmcId='+entityId,
		    cache   : false,
		    dataType: 'json',
			success : function(json) {
				if(tree){
					tree.destroy();
					tree = undefined;
				}
				if(json === null){
					$("#putTree").html('@CCMTS.replace.noConfigData@');
				}else{
					treeStore = [json];
					createTree();
					$("#withoutChildTip").empty();
					if(json.children && json.children.length === 0){
						$("#withoutChildTip").append('<p class="pL10 pT10">@CCMTS.replace.noConfigData@</p>');
					}
				}
				
			},
			error : function() {
			
			}
		});
	};
	function createTree(){
		tree = new Ext.tree.TreePanel({
		    renderTo: 'putTree',
		    useArrows: true,
		    autoScroll: true,
		    height:300,
		    animate: true,
		    containerScroll: true,
		    border: false,
		    onlyLeafCheckable: false,
		    rootVisible: false,
		    root: {
		        text: 'root',
		        expanded : true,
		        children : treeStore
		    },
		    listeners : {
		    	checkchange : function(node,checked){
		    		if(checked){//如果是选中;
		    			var ckArr = tree.getChecked(),
		    			    len = ckArr.length;
		    			//选中了多个,先都清空，然后将选中的那个再勾上;
		    			if(len > 1){
		    				for(var i=0; i<len; i++){
		    					ckArr[i].getUI().toggleCheck(false);
		    				}
		    				node.getUI().toggleCheck(true);
		    			}
		    		}
		    	}
		    }
		});
	}
	
	//window.parent.showConfirmDlg(I18N.COMMON.tip, msg, function(button, text) {
	//点击替换按钮;
	function replaceClick(){
		//tree底下本来就没有东西可选; 不存在配置文件，将无法同步配置，是否继续替换？
		if(treeStore[0].children && treeStore[0].children.length === 0){
			top.showConfirmDlg('@COMMON.tip@','@CCMTS.replace.nofile@', function(button, text){	
				if(button === 'yes'){
					replaceClickSave("");
				}
			});	
			
		}else{
			if( tree.getChecked().length === 0 ){ //文件夹下有数据，但是没有勾选任何文件;
				top.showConfirmDlg('@COMMON.tip@','@CCMTS.replace.nofilechoose@', function(button, text){
					if(button === 'yes'){
						replaceClickSave("");
					}
				});
			}else{ //底下有文件，也做出了选择;
				var ckValue =  tree.getChecked()[0].parentNode.text +"/" + tree.getChecked()[0].text;
				replaceClickSave(ckValue);
			}
		}
	};	
	function replaceClickSave(ckValue){
		top.showWaitingDlg('@COMMON.tip@', '@CCMTS.replace.replacing@');
		$.ajax({
	        url: '/cmc/replace/replaceCmcI.tv',
	        data:{
	        	cmcId : entityId,
	        	replaceCmcId : replaceCmcId,
	        	configFile : ckValue
	        },
	        type: 'post',
	        success: function(response) {
		        window.top.closeWaitingDlg();
		        if(response == 'success'){
		        	 top.afterSaveOrDelete({
			   				title: '@COMMON.tip@',
			   				html: '<b class="orangeTxt">@CCMTS.replace.success@</b>'
			   			});
				        top.frames[pageId].refreshClick();
				        cancelClick();
		        } else if(response == 'configSyncError'){
		            window.parent.showMessageDlg('@COMMON.error@', '@CCMTS.replace.error@');
		        } else if(response == 'entityResetError'){
		        	window.parent.showMessageDlg('@COMMON.error@', '@CCMTS.replace.error@');
		        } else {
		            window.parent.showMessageDlg('@COMMON.error@', '@CCMTS.replace.error@');
		        } 
	        },
	        error: function(response) {
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg('@COMMON.error@', '@CCMTS.replace.error@');
	        },
	        cache: false
	    });
	}
	
</script>
</head>
<body class="openWinBody">
	<div id="w800">
		<div id="w1600">
			<div id="step0">
				<div id="putGrid" class="pT10 pL10"></div>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
				         <li><a onclick="gotoNextPage()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i>@CCMTS.replace.next@</span></a></li>
				         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
			
			<div id="step1">
				<div class="noWidthCenterOuter clearBoth">
					 <div id="withoutChildTip"></div>
					 <div id="putTree" class="clear-x-panel-body"></div>
					 <p class="pT10 pL10">@CCMTS.replace.reset@</p>
				     <ol class="upChannelListOl pB0 pT20 noWidthCenter">                                                            
				         <li><a onclick="prevPage()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@CCMTS.replace.previous@</span></a></li>
				         <li><a onclick="replaceClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoTwoArr"></i>@CCMTS.replace.replace@</span></a></li>
				         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</div>
	</div>
</body>
</Zeta:HTML>