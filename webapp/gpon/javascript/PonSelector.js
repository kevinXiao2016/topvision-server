(function(){
	WIN.PonSelector = function(config){
		var o = this;
		this.width = 220;
    	this.subWidth = 220;
		Ext.apply(this,config);
		
		function beforeCheck(treeId, treeNode){
			//最多选择四个
			if(!treeNode.checked){
				var selectNodes = o.tree.subTree.getCheckedNodes(true);
				if(typeof o.maxSize != 'undefined'){
					if(selectNodes.length >= o.maxSize){
						return false;
					}
				}else if(o.singleSelect){
					//o.tree.subTree.cancelSelectedNode();
					//o.tree.subTree.checkNode(treeNode,true,true);
					var nodes = o.tree.subTree.getCheckedNodes();
					if (nodes.length>0) {
						$.each(nodes,function(i,node){
							o.tree.subTree.checkNode(node, false);
						});
					}
				}
			}
			return true;
		}
		
		function onCheck(e, treeId, treeNode) {
			var nodes = o.tree.subTree.getCheckedNodes(true);
			showSelectedNodes(nodes);
		}

		function showSelectedNodes(nodes){
			v = "";
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			var portObj = $("#"+o.renderTo+" p");
			portObj.text(v);
			$("#" + o.tree.id).val(v);
			if(o.afterClick){
				o.afterClick(nodes);
			}
		}
		
		var setting = {
			check: {
				enable: true,
				chkboxType: {"Y":"", "N":""}
			},
			view: {
				dblClickExpand: false,
				nameIsHTML: true,
				showIcon : true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeCheck: beforeCheck,
				onCheck: onCheck
			}
		};
		
		this.setValue = function(ponIndex){
			var allNodes = o.tree.subTree.getNodes();
			if(allNodes == null){return };
			//循环所有子节点
			$.each(allNodes, function(index, firstLevel){
				$.each(firstLevel.children, function(i, node){
					if(node.id==ponIndex){
						o.tree.subTree.checkNode(node, true);
						showSelectedNodes([node]);
						//return false;
					}else{
						o.tree.subTree.checkNode(node, false);
					}
				})
			});
			if(ponIndex == null){
				var portObj = $("#"+o.renderTo+" p");
				portObj.text("");
				$("#" + o.tree.id).val("");
			}
			
			/*var root = o.tree.subTree.getNodes()[0];
			if(root == null){
				return;
			}
			var nodes = root.children;
			$.each(nodes,function(i,node){
				if(node.id==ponIndex){
					o.tree.subTree.checkNode(node, true);
					showSelectedNodes([node]);
					//return false;
				}else{
					o.tree.subTree.checkNode(node, false);
				}
			});
			if(ponIndex == null){
				var portObj = $("#"+o.renderTo+" p");
				portObj.text("");
				$("#" + o.tree.id).val("");
			}*/
		}
		
		// 新增此方法，为修改时设置之前选中的值
		this.setSelectValues = function(arr){
			var root = o.tree.subTree.getNodes()[0];
			if(root == null){
				return;
			}
			var nodes = root.children;
			var temp = [];
			$.each(nodes,function(i,node){
				$.each(arr, function(j, v){
					if(node.id == v){
						o.tree.subTree.checkNode(node, true);
						temp.push(node);
					}
				});
			});
			showSelectedNodes(temp);
		}
		
		this.getSelectedIndexs =  function () {
		    var nodes = o.tree.subTree.getCheckedNodes(true),
	        ids = [];
		    for (var i = 0, l = nodes.length; i < l; i++) {
		    	ids.push(nodes[i].id);
		    }
		    return ids.join(",");
		}
		
		this.render = function(config){
			Ext.apply(this,config);
			$("#"+this.renderTo).empty();
			if(o.singleSelect){
				setting.check.chkStyle = "radio";
			}
			$.ajax({
		       url: "/gpon/onuauth/loadGponPorts.tv?entityId="+this.entityId,
			   data:{portType:this.type || @{GponConstant.PORT_TYPE_GPON}@ },dataType: 'json',
		       success: function(jsonData) {
					var zNodes = jsonData,
					    treeData = {
							id : o.renderTo+"-dropdown-tree",
					    	renderTo : o.renderTo,
					    	width : o.width,
					    	subWidth : o.subWidth,
					    	setting:  setting,
					    	treeData : zNodes
						};
					if(o.subHeight){ treeData.subHeight = o.subHeight};
					o.tree = new Nm3kDropDownTree(treeData);
					o.tree.init();
					if(o.readyCallback){
						o.readyCallback(o.entityId, jsonData);
					}
		       },
		       error: function() {},
		       cache: false
			});
		}
	}
	
	var JQUERY_FN_ZTREE = $.fn.zTree,
		JQUERY_FN_ZTREE_Z = JQUERY_FN_ZTREE._z,
		JQUERY_FN_ZTREE_CONST_ID = JQUERY_FN_ZTREE.consts.id;
	JQUERY_FN_ZTREE_Z.view.makeDOMNodeIcon = function(html, setting, node) {
		var nameStr = JQUERY_FN_ZTREE_Z.data.getNodeName(setting, node),
		name = setting.view.nameIsHTML ? nameStr : nameStr.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
		if(node.nm3kTip){
			html.push("<span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.ICON,
					"' nm3kTip="+node.nm3kTip+" treeNode", JQUERY_FN_ZTREE_CONST_ID.ICON," class='nm3kTip ", this.makeNodeIcoClass(setting, node),
					"' style='", this.makeNodeIcoStyle(setting, node), "'></span><span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.SPAN,
					"'>",name,"</span>");
		}else{
			html.push("<span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.ICON,
					"' title='' treeNode", JQUERY_FN_ZTREE_CONST_ID.ICON," class='", this.makeNodeIcoClass(setting, node),
					"' style='", this.makeNodeIcoStyle(setting, node), "'></span><span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.SPAN,
					"'>",name,"</span>");
		}
	};

})();
	
