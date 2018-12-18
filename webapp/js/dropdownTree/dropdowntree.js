+ function($) {
    "use strict";

    // Dropdowntree 类定义
    // ======================
    var Dropdowntree = function(element, options) {
        this.options = $.extend({}, Dropdowntree.DEFAULTS, options);
        this.$element = $(element);
        this.treeId = 'root_ul_' + this.$element.attr('id');
        this.$select = null;
        this.$treeDiv = null;
        this.$tree = null;
        this.$body = null;
        this.$footer = null;
        this.levelMap = null;
        
        //初始化
        this.initTree();
    };

    //控件默认值
    Dropdowntree.DEFAULTS = {
        width: 400,
        multi: true,
        withSelect: true,
        affectChild: false,
        yesAffectChild: false,
        yesAffectParent: false,
        noAffectChild: false,
        noAffectParent: false,
        switchMode: false,
        url: '/topology/fetchLogonUserAuthFolders.tv'
    }

    //下拉树控件初始化方法 
    Dropdowntree.prototype.initTree = function() {
    	var _self = this,
    		_options = _self.options;
    	//生成HTML结构
    	if(_options.withSelect){
    		//如果包含下拉选择部分，则生成相关结构 
    		_self.$element.width(_self.options.width).append('<div class="Nm3kSelect"><p class="contentP"></p><a class="Nm3kSelectArr"></a></div>');
    		//如果多选，则包含快捷勾选部分，否则则不包含
    		if(_options.multi){
    			_self.$element.append('<div class="folderTree" style="position:absolute;display:none;"><div class="folderTree-body"></div><div class="folderTree-footer"></div></div>');
    		}else{
    			_self.$element.append('<div class="folderTree" style="position:absolute;display:none;"><div class="folderTree-body"></div></div>');
    		}
    	}else{
    		//如果多选，则包含快捷勾选部分，否则则不包含
    		if(_options.multi){
    			_self.$element.append('<div class="folderTree"><div class="folderTree-body"></div><div class="folderTree-footer"></div></div>');
    		}else{
    			_self.$element.append('<div class="folderTree"><div class="folderTree-body"></div></div>');
    		}
    	}
    	_self.$select = _self.$element.find('.Nm3kSelect').width(_self.options.width);
    	_self.$treeDiv = _self.$element.find('.folderTree').width(_self.options.width);
    	_self.$body = _self.$treeDiv.find('.folderTree-body');
    	_self.$footer = _self.$treeDiv.find('.folderTree-footer');
    	
        //从后台加载数据并生成相应结构
    	_self.fetchTreeData();

        //bind event to highlight correct tab and tree and footer
        this.bindEvent();
    };

    //获取数据生成DOM结构
    Dropdowntree.prototype.fetchTreeData = function() {
        var _self = this;
        $.ajax({
            url: this.options.url,
            dataType: 'json',
            async: false,
            success: function(folderList) {
            	_self.treeData = folderList;
            },
            error: function() {},
            cache: false
        });
        
        //生成树DOM结构
        if(_self.options.multi){
        	_self.initTreeBody();
        }else{
        	_self.initTreeBody('radio');
        }
        //如果支持多选，则生成footer部分的快捷勾选DOM
        if(_self.options.multi){
        	_self.initTreeFooter();
        	//初始化后，要标注正确的勾选框
        	var _levelMap = _self.levelMap, 
        		totalLevel = _levelMap.level,
        		curLvlNodes,
        		curLvlAllCkd,
        		totalAllCkd = true;
        	for(var i=0; i<=totalLevel; i++){
        		curLvlNodes = _levelMap[i];
        		curLvlAllCkd = true;
        		//遍历当前层级的所有节点，判断是否全部勾选
        		for(var j=0; j<curLvlNodes.length; j++){
        			if(curLvlNodes[j].checked==false){
        				curLvlAllCkd = false;
        				break;
        			}
        		}
        		if(curLvlAllCkd == false){
    				totalAllCkd = false;
    			}else{
    				//勾选当前快捷键
    				_self.$footer.find('input[level="'+i+'"]').attr('checked', true);
    			}
        	}
        	if(totalAllCkd){
    			_self.$footer.find('input[level="all"]').attr('checked', true);
    		}
        }
    };

    Dropdowntree.prototype.initTreeBody = function(chkStyle) {
        var _self = this;
        var yRule = '', nRule = '';
        if(_self.options.yesAffectParent){
        	yRule += 'p';
        }
        if(_self.options.yesAffectChild){
        	yRule += 's';
        }
        if(_self.options.noAffectParent){
        	nRule += 'p';
        }
        if(_self.options.noAffectChild){
        	nRule += 's';
        }
        
        var setting = {
            check: {
                enable: true,
                chkStyle: chkStyle || 'checkbox',
                chkboxType: {
                    "Y": yRule/*_self.options.affectChild ? "s" : ""*/,
                    "N": nRule/*_self.options.affectChild ? "p" : ""*/
                }
            },
            view: {
                dblClickExpand: false
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
        _self.$body.append('<ul id="'+_self.treeId+'" class="ztree"></ul>');
        //init this tree content
        _self.$tree = $.fn.zTree.init($("#"+_self.treeId), setting, _self.treeData);
        //生成树之后，需要生成对应的层级节点map
        generateLevelMap();
        
        function generateLevelMap(){
        	_self.levelMap = {level:0};
        	var allNodes = _self.$tree.transformToArray(_self.$tree.getNodes()),
        		curNode = null,
        		curNodeLevel = 0;
        	for(var i=0, len=allNodes.length; i<len; i++){
        		curNode = allNodes[i];
        		curNodeLevel = curNode.level;
        		if(_self.levelMap.level<curNodeLevel){
        			_self.levelMap.level = curNodeLevel;
        		}
        		if(!_self.levelMap[curNodeLevel]){
        			_self.levelMap[curNodeLevel] = [];
        		}
        		_self.levelMap[curNodeLevel].push(curNode);
        	}
        }

        //被勾选之前触发
        function beforeCheck(treeId, treeNode){
        	if(!_self.options.multi){
        		//如果是单选状态，并且置灰有被勾选的，则不允许此次勾选
        		var checkNodes = _self.getCheckedNodes();
        		for(var i=0, len=checkNodes.length; i<len; i++){
        			if(checkNodes[i].chkDisabled){
        				return false;
        			}
        		}
        		var checked = treeNode.checked;
        		_self.disCheckAllNodes();
        		if(checked){
        			return false;
        		}
        	}else{
        		//根据勾选情况，更新快捷操作
        		var checked = treeNode.checked;
        		if(checked){
        			//当前是勾选状态，则此动作是取消勾选,取消全选和当前等级快捷的勾选
        			_self.$footer.find('input[level="all"]').attr('checked', false);
        			_self.$footer.find('input[level="'+treeNode.level+'"]').attr('checked', false);
        			//如果是切换模式，取消勾选时，取消其所有父节点的勾选
        			if(_self.options.switchMode){
        				var pNode = treeNode.getParentNode();
            			while(pNode && !pNode.chkDisabled){
            				_self.$tree.checkNode(pNode, false);
            				pNode = pNode.getParentNode();
            			}
        			}
        		}else{
        			//当前是非勾选状态，此动作是勾选，判断是否需要勾选当前等级快捷操作及全选checkbox
        			var curLevelNodes = _self.levelMap[treeNode.level], allChecked = true;
        			for(var i=0,len=curLevelNodes.length; i<len; i++){
        				if(curLevelNodes[i].tId == treeNode.tId) continue;
        				if(curLevelNodes[i].checked==false){
        					allChecked = false;
        					break;
        				}
        			}
        			if(allChecked){
        				_self.$footer.find('input[level="'+treeNode.level+'"]').attr('checked', true);
        			}
        			//判断是否需要勾选全选checkbox
        			if(_self.$tree.transformToArray(_self.$tree.getNodes()).length == _self.getCheckedNodes().length+1){
        				_self.$footer.find('input[level="all"]').attr('checked', true);
        			}
        		}
        	}
        }
        
        //被勾选后触发事件
        function onCheck(){
        	if(_self.options.withSelect){
        		//如果有下拉框，则将选中的所有节点名称放入下拉框内
        		_self.$select.find('.contentP').text(_self.getSelectedNames().join(', '));
        	}
        	_self.refresh();
        	if(_self.options.onCheck){
        		_self.options.onCheck();
        	}
        }
    };

    Dropdowntree.prototype.initTreeFooter = function() {
        this.$footer.append('<span class="noWrap"><input type="checkbox" level="all">@BASE/COMMON.selectAll@</span>');
        for (var i = 0; i <= this.levelMap.level; i++) {
        	this.$footer.append(String.format('<span class="noWrap"><input type="checkbox" level="{0}" />@BASE/COMMON.levelRegion@ {1}</span>', i, i+1)); 
        }
    };

    Dropdowntree.prototype.bindEvent = function() {
        var _self = this,
        	_options = this.options,
            $body = this.$body,
            $footer = this.$footer,
            _levelMap = this.levelMap;
        
        if (_options.withSelect){
        	//为下拉绑定打开树/收起树
        	_self.$select.bind('click', function(e){
        		if(_self.$treeDiv.css('display')!='none') {
        			// 显示状态，隐藏
        			_self.$treeDiv.hide();
        		} else {
        			// 隐藏状态，显示
        			var position = _self.$select.position();
          	      	_self.$treeDiv.css({
          	      		left : position.left + "px", 
          	      		top : position.top + _self.$select.outerHeight() - 1 + "px"
          	      	}).slideDown("fast");

          	      	$("body").bind("mousedown", onBodyDown);
        		}
        	})
        }
        if(_options.multi){
        	$footer.delegate('input', 'click', function(e) {
        		var level = $(e.target).attr('level');
        		if (level == 'all') {
        			if ($(e.target).is(':checked')) {
        				_self.checkAllNodes();
        			} else {
        				_self.disCheckAllNodes();
        			}
        		} else {
        			var nodes = _levelMap[level];
        			if ($(e.target).is(':checked')) {
        				var all_cbx_len = $footer.find('input[type="checkbox"]').length,
        					checked_cbx_len = $footer.find('input[type="checkbox"]:checked').length;
        				if (all_cbx_len == checked_cbx_len + 1) {
        					$footer.find('input[type="checkbox"][level="all"]').prop('checked', true);
        				}
        				for(var i=0, len=nodes.length; i<len; i++){
        					_self.$tree.checkNode(nodes[i], true);
        				}
        			} else {
        				for(var i=0, len=nodes.length; i<len; i++){
        					_self.$tree.checkNode(nodes[i], false);
        				}
        				$footer.find('input[type="checkbox"][level="all"]').prop('checked', false);
        			}
        			_self.refresh();
        		}
        	});
        }
        
        function onBodyDown(e) {
    		e = window.event || e; // 兼容IE7
    		var obj = $(e.srcElement || e.target);
    		var regionStr = '#'+_self.$element.attr('id')+', #'+_self.$element.attr('id')+' *';
    		if (!$(obj).is(regionStr)) { 
    			_self.$treeDiv.fadeOut("fast");
    			$("body").unbind("mousedown", onBodyDown);
    		} 
    	}
    };
    
    Dropdowntree.prototype.getCheckedNodes = function() {
    	var allNodes = this.$tree.transformToArray(this.$tree.getNodes());
    	var checkedNodes = [];
    	for(var i = 0, len = allNodes.length; i < len; i++){
    		if(allNodes[i].checked){
    			checkedNodes.push(allNodes[i]);
    		}
    	}
        return checkedNodes;
    }
    
    Dropdowntree.prototype.getNoCheckedNodes = function() {
    	var allNodes = this.$tree.transformToArray(this.$tree.getNodes());
    	var noCheckedNodes = [];
    	for(var i = 0, len = allNodes.length; i < len; i++){
    		if(!allNodes[i].checked){
    			noCheckedNodes.push(allNodes[i]);
    		}
    	}
        return noCheckedNodes;
    }

    /**
     * 获取当前地域树选中的id列表数组
     */
    Dropdowntree.prototype.getSelectedIds = function() {
        var nodes = this.getCheckedNodes(),
            ids = [];
        for (var i = 0, l = nodes.length; i < l; i++) {
            ids.push(nodes[i].id);
        }
        return ids;
    }
    
    /**
     * 获取当前地域树未被选中的id列表数组
     */
    Dropdowntree.prototype.getNotSelectedIds = function() {
        var nodes = this.getNoCheckedNodes(),
            ids = [];
        for (var i = 0, l = nodes.length; i < l; i++) {
            ids.push(nodes[i].id);
        }
        return ids;
    }

    /**
     * 获取当前地域树选中的名称列表数组
     */
    Dropdowntree.prototype.getSelectedNames = function() {
        var nodes = this.getCheckedNodes(),
            names = [];
        for (var i = 0, l = nodes.length; i < l; i++) {
            names.push(nodes[i].name);
        }
        return names;
    }

    /**
     * 设置选择哪些节点
     */
    Dropdowntree.prototype.checkNodes = function(ids) {
    	//选中节点时，需要判断是否是切换地域模式，如果是则在勾选节点时需要勾选所有子节点
    	var checkTypeFlag = false;
    	if(this.options.switchMode){
    		checkTypeFlag = true;
    	}
        for (var i = 0, len = ids.length; i < len; i++) {
            var node = this.$tree.getNodeByParam('id', ids[i]);
            this.$tree.checkNode(node, true, checkTypeFlag);
        }
        this.refresh();
    }
    
    /**
     * 设置部分节点不可勾选
     */
    Dropdowntree.prototype.disableNodes = function(ids) {
    	//首选重置状态
    	this.disCheckAllNodes();
    	var allNodes = this.$tree.transformToArray(this.$tree.getNodes());
    	for (var i = 0, ilen = allNodes.length; i < ilen; i++) {
            this.$tree.setChkDisabled(allNodes[i], false);
        }
    	//置灰指定节点
        for (var j = 0, jlen = ids.length; j < jlen; j++) {
            var node = this.$tree.getNodeByParam('id', ids[j]);
            this.$tree.setChkDisabled(node, true);
        }
        this.refresh();
    }
    
    /**
     * 选择全部节点
     */
    Dropdowntree.prototype.checkAllNodes = function() {
    	this.$tree.checkAllNodes(true);
        this.$footer.find('input[type="checkbox"]').prop('checked', true);
        this.refresh();
    }
    
    /**
     * 取消选择全部节点
     */
    Dropdowntree.prototype.disCheckAllNodes = function() {
    	//this.$tree.checkAllNodes(false); //该方法有bug，有时候会无法生效
    	var allNodes = this.$tree.transformToArray(this.$tree.getNodes());
    	for (var i = 0, ilen = allNodes.length; i < ilen; i++) {
            this.$tree.checkNode(allNodes[i], false);
        }
        this.$footer.find('input[type="checkbox"]').prop('checked', false);
        this.refresh();
    }
    
    Dropdowntree.prototype.refresh = function() {
    	if(this.options.withSelect){
    		//如果有下拉框，则将选中的所有节点名称放入下拉框内
        	this.$select.find('.contentP').text(this.getSelectedNames().join(', '));
    	}
    	if(this.options.multi){
    		//根据当前选中节点的情况更新快捷勾选框的状态
    		var levels = this.levelMap.level,
    			allChecked = true,
    			curLvlAllCkd = true,
    			curLevelNodes = null;
    		
    		for(var curLvl=0; curLvl<=levels; curLvl++){
    			curLevelNodes = this.levelMap[curLvl];
    			curLvlAllCkd = true;
    			for(var i=0, len=curLevelNodes.length;i<len; i++){
    				if(curLevelNodes[i].checked==false){
    					curLvlAllCkd = false;
    					allChecked = false;
    					break;
    				}
    			}
    			//判断当前快捷操作是否需要被勾选
    			this.$footer.find('input[level="'+curLvl+'"]').attr('checked', curLvlAllCkd);
    		}
    		//判断全选是否被勾选
    		this.$footer.find('input[level="all"]').attr('checked', allChecked);
    	}
    }

    // Dropdowntree PLUGIN DEFINITION
    // =======================
    var old = $.fn.dropdowntree;

    $.fn.dropdowntree = function(option) {
        return this.each(function() {
            var $this = $(this);
            var data = $this.data('nm3k.dropdowntree');
            var options = typeof option == 'object' && option

            if (!data) $this.data('nm3k.dropdowntree', (data = new Dropdowntree(this, options)))
            if (typeof option == 'string') data[option]();
        })
    }

    $.fn.dropdowntree.Constructor = Dropdowntree;

    // Dropdowntree NO CONFLICT
    // =================
    $.fn.dropdowntree.noConflict = function() {
        $.fn.dropdowntree = old
        return this
    }

}(jQuery);
