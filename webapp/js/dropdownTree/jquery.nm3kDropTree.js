/*
 * 2015年6月11日 leexiang
 * 基于ztree开发的下拉弹出树，支持快捷过滤;
 * 在使用该文件前，要确保引用了jquery.ztree.all.js 和 jquery.ztree.exhide.js
 */

;(function($){
		$.fn.nm3kDropTree = function(options){
			var opts = $.extend(true, {}, $.fn.nm3kDropTree.defaults, options);  
			opts.settings.renderId = $(this).attr("id");
			opts.subId = $(this).attr("id") + "_nm3kDropTree";//底部弹出的id,自动生成;
			if(opts.subWidth < opts.width){opts.subWidth = opts.width};//下部宽度不能小于上部宽度，否则会很丑;
			
			return this.each(function(){
				var $me = $(this),
				    dom = stringFormat(opts.str, opts.width),
					subDom = stringFormat(opts.subStr, opts.subId, opts.subWidth, opts.inputWidth, opts.searchBtnTxt);
				     
				$me.html( dom );//增加上半部分，下拉框外观;
				$('body').append( subDom );//增加下半部分弹出层;
				var $topDiv = $me.find(".nm3kDropTree");
				var l = $topDiv.offset().left;
				var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
				var $subDiv = $("#" + opts.subId);
				$subDiv.css({"left": l, "top": t});
				if(opts.subHeight != null){
					$subDiv.height(opts.subHeight);
				}
				
				opts.tree = $.fn.zTree.init($subDiv.find("ul.ztree"), opts.settings, opts.dataArr);
				var tree = opts.tree;
				tree.expandAll(true);
				showFirstValue(opts);//显示第一次选择的默认值;
				
				$subDiv.find(".ztreeSearchBtn").click(function(){
					$me = $(this),
					$text = $subDiv.find(":text");
					$me.next().slideToggle(function(){
						if( $text.is(':visible') ){
							$text.focus();
						}
					});
					$me.find("label").toggle();
				});
				$subDiv.find(":text").bind("blur", function(){blurKey(opts)}).bind("change input propertychange", function(){searchNode(opts)});
				$topDiv.bind("click",opts,showSub);//点击上部div,弹出下面弹出框;
				$("html").bind("click",opts,bodyClick);
				$(window).bind("resize",opts,bodyClick);
	
			})
		};//end nm3kDropTree;
		
		$.fn.nm3kDropTree.defaults = {
			tree : null,
			width : 100,              //上部看起来像input的宽度;
			subWidth : 220,           //下拉弹出框的宽度;
			inputWidth : 140,         //过滤输入框的宽度;
			subHeight : null,         //可以不填写;
			subMinHeight : 40,
			firstSelect : 0,         //暂时还未使用该属性，后期扩展;
			searchBtnTxt : 'search', //搜索按钮上的文字;
			firstSelectValue : null, //默认选择的值;
			dataArr : [], //树菜单的data;
			lastValue : '',
			str : '<div class="nm3kDropTree" style="width:{1}px;"><p></p><a class="nm3kDropTreeSelectArr" href="javascript:;"></a></div>',
			subStr : '<div class="nm3kDropTreeSub" id="{1}" style="width:{2}px; display:none;"><a href="javascript:;" class="ztreeSearchBtn nm3k_show"><span class="miniIcoSearch nm3k_show"></span><label style="display:none;" class="nm3k_show">搜索</label></a><ul class="leftFloatUl"><li><input type="text" class="nm3k_show normalInput" style="width:{3}px;" /></li><li><a href="javascript:;" class="nm3k_show normalBtn"><span class="nm3k_show">{4}</span></a></li></ul><ul class="ztree clearBoth"></ul></div>',
			settings : { //从这部分起，就是zTree的内容，请自行查看zTree的api;
				view : {
					selectedMulti: false,
					showIcon: false
				},
				callback : {
					onClick	: function(e, treeId, treeNode){
						e.stopPropagation();
						var txt = treeNode.name,
						    val = treeNode.value,
							setting = this.getZTreeObj(treeId).setting,
						    renderId = setting.renderId,
							$render = $("#" + renderId),
							$subDiv = $("#" + renderId + "_nm3kDropTree");
						
						try{
							txt = treeNode[setting.data.key.name];
						}catch(e){
							
						}

						$render.find(".nm3kDropTree p").text(txt).attr("title",txt);
						$subDiv.css({display : "none"});
						$render.data({name : txt, value : val, isOpen : false});
					}
				}
			}
		};//end defaults;
		
		//搜索节点
		function searchNode(opts) {
			// 取得输入的关键字的值
			var value = $.trim( $("#" + opts.subId).find(":text").val() ),
				keyType = "name", // 按名字查询
				tree = opts.tree;
			
			try{
				keyType = opts.settings.data.key.name;
			}catch(e){}
				
			// 如果和上次一次，就退出不查了。
			if (opts.lastValue === value) {
				return;
			}
			// 保存最后一次
			opts.lastValue = value;
			var nodes = tree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes, tree);
				return;
			}
			hideAllNode(nodes, tree);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			updateNodes(nodeList, tree);
		}
		
		function blurKey(opts) {
			searchNode(opts);
		};
		
		//显示所有节点
		function showAllNode(nodes,tree){			
			//最后一个节点不展开，其余全展开;
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/*if(nodes[i].getParentNode()!=null){
					tree.expandNode(nodes[i],false,false,false,false);
				}else{*/
					tree.expandNode(nodes[i],true,true,false,false);
				//}
				tree.showNode(nodes[i]);
				showAllNode(nodes[i].children, tree);
			}
			//tree.expandAll(true);
		}
		//隐藏所有节点
		function hideAllNode(nodes,tree){			
			nodes = tree.transformToArray(nodes);
			tree.hideNodes(nodes);
		}
		//更新节点状态
		function updateNodes(nodeList,tree) {
			tree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				//展开当前节点的父节点
				tree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					tree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					tree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				tree.showNode(nodeList[i].getParentNode());
				//展开根节点
				tree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
		
		function bodyClick(e){
			var opts = e.data,
			    $renderID = $("#" + opts.settings.renderId),
				$target = $(e.target);

			if( $target.hasClass("nm3k_show") || $target.hasClass("switch") ){
				return;	
			}	
			if( $renderID.data("isOpen") === true ){
				 $renderID.data("isOpen", false);
				var $subDiv = $("#"+ opts.subId);
				$subDiv.css({display:"none"});
			}
		}
		
		function showSub(e){
			if(!e){e = window.event;}		
			e.stopPropagation();
			
			var opts = e.data,
			    $subDiv = $("#" + opts.subId),
				$renderId = $("#" + opts.settings.renderId),
				$input = $subDiv.find(":text");

			if( typeof($renderId.data("isOpen")) == "undefined" || $renderId.data("isOpen") === false ){			
				$input.val("");
				searchNode(opts);
				showAllNode(null,opts.tree);
				
				$subDiv.css({display : "block"});//必须先显示，才能获取正确高度;
				var $topDiv = $("#" + opts.settings.renderId).find(".nm3kDropTree");
				var l = $topDiv.offset().left;
				var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
				var h = $(window).height() - t - 50;
				if(h < opts.subMinHeight){ h = opts.subMinHeight;}
				var treeH = $subDiv.find(".ztree").outerHeight() + 25;			
				if(treeH < h){ h = treeH}
				
				$subDiv.css({display:"block", top: t, left: l, height: h});
				if(opts.subHeight){
					$subDiv.height(opts.subHeight);
				}
				if( $input.is(':visible') ){
					$input.focus();
				}
				$renderId.data("isOpen",true);
				
			}else{
				$renderId.data("isOpen",false);
				$subDiv.css({display:"none"});
			}
		};//end showSub;
		
		function showFirstValue(opts){
			var firstSelectValue = opts.firstSelectValue,
			    tree = opts.tree,
			    $render = $("#" + opts.settings.renderId),
			    dataArr = opts.dataArr,
			    nameSel,  //选中的name;
			    valueSel; //选中的value;
			if(dataArr.length <= 0){return;}
			if(firstSelectValue === null){
				//nameSel = dataArr[0].name;
				valueSel = dataArr[0].value;
			}else{
				valueSel = firstSelectValue;
			}
			var node = tree.getNodeByParam("value", valueSel);//通过value拿到要选的节点;
			tree.selectNode(node,false);//选中节点;
			nameSel = node.name;//通过节点，拿到name属性;
			try{
				nameSel = node[opts.settings.data.key.name];
			}catch(e){}
			
			
			$render.find(".nm3kDropTree p").text(nameSel).attr({title: nameSel});
			$render.data({
				name : nameSel,
				value : valueSel,
				isOpen : false
			});
		};//end showFirstValue;
		
		//stringForamt 函数;
		function stringFormat(str, args){
			if (arguments.length > 1) {
				for (var i = 1; i < arguments.length; i++) {
					if (arguments[i] != undefined) {
						var reg = new RegExp("({[" + i + "]})", "g");
						str = str.replace(reg, arguments[i]);
					}
				}
			}
			return str;	
		};//end stringFormat;
		
	})(jQuery);