<%@ page language="java" contentType="text/html;charset=UTF-8"%>
 <%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<%@include file="/include/ZetaUtil.inc"%>
		<Zeta:Loader>
		    LIBRARY ext
		    LIBRARY jquery
		    LIBRARY zeta
		    module fault
		    CSS css/white/disabledStyle
    		CSS js/dropdownTree/css/zTreeStyle/zTreeStyle
    		IMPORT js/dropdownTree/jquery.ztree.all-3.5.min
    		IMPORT js/dropdownTree/jquery.ztree.exhide-3.5.min
    		IMPORT js/nm3k/Nm3kTools
		</Zeta:Loader>
	</head>
	<style type="text/css">
		/****************************************
		
		        由于该页面只用到check没有用到select，
		        因此通过样式将select选中状态隐藏起来
		        
		*****************************************/
		
		.ztree li a{ cursor: default;}
		.ztree li a:hover { color:#333}
		.ztree li a.curSelectedNode {padding-top:1px; background-color:transparent; color:black; height:16px; border:0px #FFB951 solid; opacity:1;}
		.ztree li a label{background:#ffff00;}
	</style>
	<script type="text/javascript">
	    var tbar, tree = null;
	    
	    Ext.onReady(function(){ 
	    	tbar = new Ext.Toolbar({
	    		renderTo : 'putTbar',
	            items : [{
	            	xtype : 'tbspacer',
	            	width : 10
	            },{
	                text : '@COMMON.save@',
	                iconCls : 'bmenu_data',
	                handler : saveClick
	            },'-',{
	            	text : '@COMMON.refresh@',
	            	iconCls : 'bmenu_refresh',
	            	handler : function(){
	            		window.location.href = window.location.href;
	            	}
	            },'-',{
	            	text : '@COMMON.selectAll@',
	            	iconCls : 'bmenu_correct',
	            	handler : function(){
	            		checkAllFn(true);
	            		setAttentionNum();
	            	}
	            },{
	            	text : '@COMMON.selectNone@',
	            	iconCls : 'bmenu_delete',
	            	handler : function(){
	            		checkAllFn(false);
	            		setAttentionNum();
	            	}
	            },'-',{
	            	xtype: 'tbtext',
	            	text : '@COMMON.highlightSearch@:'
	            },{
	            	xtype : 'component',
	            	html : '<input id="searchInput" type="text" class="normalInput w160" onkeyup="fastSearch()" />'
	            }]
	        });
	    	viewPort = new Ext.Viewport({
	            layout: "border",
	            items: [{
	            	region : 'north',
	            	height : 37,
	            	border : false,
	            	contentEl : 'putTbar'
	            },{
	            	region : 'center',
	            	autoScroll : true,
	            	border : false,
	            	contentEl : 'putMain'
	            }]
	        });
	    	init();
	    });//end document.ready;
	    function init(){
	    	var setting = {
	    		view : {
	    			showLine : true,
	    			showIcon : false,
	    			selectedMulti: false,
	    			txtSelectedEnable : false
	    		},
	    		check : {
	    			enable : true
	    		},
	    		data :　{
		    		key : {
		    			name : 'text'
		    		}
	    		},
	    		callback : {
	    			onCheck : function(){
	    				setAttentionNum();
	    			}
	    		}
	    	};
	    	$.ajax({
	    		url: '../fault/loadAlertType.tv',
	        	type: 'POST',
	        	dataType:"json",
	        	async: 'false',
	       		success: function(array) {
	       			//注意：如果某些节点不需要获取选中的值，请设置nocheck属性为true;
	       			addNocheck(array);
	       			var zNodes = array;
	       			tree = $.fn.zTree.init($('#tree'), setting, zNodes);
	       			tree.expandAll(true);
	       			setAttentionNum();
	       		},
	       		error : function(){
	       			$("#tree li").text('@ALERT.loadErr@');
	       		}
	    	});
	    }; //end init;
	    //将所有父节点加上nocheck属性;
	    function addNocheck(arr){
	    	$.each(arr, function(i, v){
	    		//数据中没有children,也会传children为空的数组过来，所有不得不多判断length;
	    		if(v.children && v.children.length>0){ 
	    			v.nocheck = true;
	    			addNocheck(v.children);
	    		}
	    	});
	    }
	    
	    function fastSearch(){
	    	if(tree != null){
	    		var v = $("#searchInput").val();
	    		//先清空树上的label标签;
	    		$("#tree a").each(function(){
	    			var $span = $(this).find('span:last'),
	    			    reg = /<label>/g,
	    			    reg2 = /<\/label>/g,
	    			    html = $span.html();
	    			if(reg.test(html)){
	    				html = html.replace(reg, '');
	    				html = html.replace(reg2, '');
	    				$span.html(html);
	    			}
	    		});
	    		$("#tree a").each(function(){
       				var $span = $(this).find('span:last');
       				var txt = $span.text();
       				if(v.length == 0){return}
       				var newTxt = Nm3kTools.tools.searchReplace(txt, v, 'label');
       				$span.html(newTxt);
       			});
	    	}
	    }
	    //设置关注了几种告警;
	    function setAttentionNum(){
	    	if(tree != null){
	    		var num = tree.getCheckedNodes(true).length;
	    		$("#attentionTip b").text(num);
	    	}
	    }
	    //保存;
	    function saveClick(){
	    	if(tree != null){
	    		var ckArr = tree.getCheckedNodes(true);
	    		var checkArr = []; 
	    		$.each(ckArr, function(i, v){
	    			checkArr.push(v['id']);
	    		});
	    		if(checkArr.length > 0){
	    			$.ajax({
	    	    		url: '/alert/updateUserAlertType.tv',
	    	    		data :　{
	    	    			alertTypeIds : checkArr
	    	    		},
	    	        	type: 'POST',
	    	        	dataType:"json",
	    	        	async: 'false',
	    	       		success: function(json) {
	    	       			setAttentionNum();
	    	       			var opts = {
		    	       			title : '@COMMON.tip@',
		    					html : '<p><b class="orangeTxt">@resources/COMMON.saveSuccess@</b></p>'
		    	       		}
	    	       			//如果已经打开过
	    	       			if(top.frames['frameattentionAlert']){
	    	       				opts.showTime = 4000;
	    	       				opts.html += '<p class=blueTxt>@ALERT.saveSuccessTip@</p>';
	    	       			}
	    	       			top.afterSaveOrDelete(opts);
	    	       		},
	    	       		error : function(json) {
	    					window.top.showErrorDlg();
	    				}
	    			});
	    		}else{
	    			top.showMessageDlg('@COMMON.tip@','@ALERT.noChecked@');
	    		}
	    	}	
	    }
	    //全选、全不选;
	    function checkAllFn(b){
	    	if(tree !== null){
	    		tree.checkAllNodes(b);
	    	}
	    }
	</script>
	<body class="whiteToBlack">
		<div id="putTbar"></div>
    	<div id="putMain">
    		<p id="attentionTip" class="blueTxt pT5 pL10">@ALERT.selectAttenNum@</p>
    		<ul id="tree" class="ztree">
    			<li>@ALERT.loading@</li>
    		</ul>
    	</div>
	</body>
</Zeta:HTML>