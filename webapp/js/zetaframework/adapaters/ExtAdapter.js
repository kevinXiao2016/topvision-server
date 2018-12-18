/////////////////////////////////////////////////////////////
/// 					EXT ADAPTER					/////////
/////////////////////////////////////////////////////////////
(function(){
	
	/**
	 * 判断类是否在当前文档中存在
	 * @param {class} clazz
	 */
	function checkClass(clazz){
		try{
			return typeof eval(clazz) == 'function'; 
		}catch(exception){
			return false;
		}
	}
	
	// private function
	var _listen_ = function (){
	    //表格的适配
	    try{
	        Ext.grid.GridPanel.prototype.enableColumnMove = false;
	    }catch(e){}
	    // 菜单栏的适配
		if( checkClass("Ext.menu.Menu") ){
			//禁止菜单出现滚动条
			Ext.menu.Menu.prototype.enableScrolling = false;
			//点击非叶子菜单时，菜单不消失
			Ext.menu.Menu.prototype.ignoreParentClicks = true;
			//***  Adapater For MenuItem *******//
			//var itemAdapater = Ext.menu.Item.prototype.initComponent;
			var proxyRender = Ext.menu.Item.prototype.onRender;
			Ext.override(Ext.menu.Item,{
				onRender : function(container, position){
					if(this.disabled){
						
					}else if( this.id ){
						if( typeof VersionControl == 'undefined' ){
							
						}else if(this.entityId != null){
							var $support = VersionControl.support(this.id,this.entityId);
							this.hidden = !$support;
						}else if(typeof CONTROL_ENTITYID != 'undefined'){
							var $suppot =  VersionControl.supportNode(this.id, CONTROL_ENTITYID);
							if($suppot){
								if( typeof $suppot.disabled != 'undefined' ){
									this.disabled = $suppot.disabled;
								}
								if( typeof $suppot.hidden != 'undefined' ){
									this.hidden = $suppot.hidden;
								}
							}
						}
					}
					proxyRender.apply( this, arguments );
			    }
			});
		}
		if( checkClass("Ext.tree.TreeNode") ){
			var proxyTreeRender = Ext.tree.TreeNode.prototype.render;
			Ext.override( Ext.tree.TreeNode, {
				render : function(container, position){
					if(this.disabled){
						
					}else if( this.id && typeof CONTROL_ENTITYID != 'undefined'){
						if(this.entityId != null){
							var $support = VersionControl.support(this.id,this.entityId);
							this.hidden = !$support;
						}else if( typeof VersionControl != 'undefined' && typeof CONTROL_ENTITYID != 'undefined' ){
							var $suppot =  VersionControl.supportNode(this.id,CONTROL_ENTITYID);
							if($suppot){
								if(typeof $suppot.disabled != 'undefined' ){
									this.disabled = $suppot.disabled;
								}
								if(typeof $suppot.hidden != 'undefined' ){
									this.hidden = $suppot.hidden;
								}
							}
						}
					}
					proxyTreeRender.apply( this, arguments );
			    },
			    setIcon : function( icon ){
			    	this.getUI().getIconEl().src = icon;
			    }
			});
		}
		//列的菜单重写，不应该使用默认的
		if( checkClass("Ext.grid.Column") ){
			//禁止表头列默认有menu的配置
			//FIXME 如果使用的 columns 配置项，这个貌似是不生效的
			var proto = Ext.grid.Column.prototype; 
			proto.sortable = true;
			proto.menuDisabled = true;
			proto.align = "center";
			Ext.grid.ColumnModel.prototype.sortable = true;
		}
		
		//FIXME Function扩展。window有回调的话，这个回调方法必须是经过Ext包装过的
		//TODO grid的drag，mouseover等效果一起处理
		if( checkClass("Ext.grid.RowSelectionModel") ){
			Ext.grid.RowSelectionModel.prototype.clearSelections = function(fast){
			    if(this.isLocked()){
			        return;
			    }
			    if(fast !== true){
			        var ds = this.grid.store;
			        var s = this.selections;
			        if(s.length > 1000){
			        	if(typeof $ == "undefined"){
			        		throw new Error("多选的数据过于庞大，请加载Jquery库。有问题请联系zhangdongming@dvt.dvt.com");
			        	}
			        	$(".x-grid3-row ").removeClass("x-grid3-row-selected");
			        	s.clear();
		        	}else{
		        		s.each(function(r){
				            this.deselectRow(ds.indexOfId(r.id));
				        }, this);
		        	}
			    }else{
			        this.selections.clear();
			    }
			    this.last = false;
			};
		}
	}
	if( checkClass("Ext.grid.GridView") ){
		var _layout_ = Ext.grid.GridView.prototype.layout;
		Ext.grid.GridView.prototype.layout = function(){
			if(this.forceFit){
		         this.scroller.setStyle('overflow-x', 'hidden');
		     }else{
		     	this.scroller.setStyle('overflow-x', 'auto');
		     }
			_layout_.apply( this );
		}

		
	}
	
	if( checkClass( "Ext.grid.PropertyGrid" ) ){
		Ext.grid.PropertyGrid.prototype.setSource = function(source) {
			delete this.propStore.store.sortInfo;
			this.propStore.setSource(source);
		}; 
	}
	
	
	if ((typeof Range !== "undefined") && !Range.prototype.createContextualFragment) {
	    Range.prototype.createContextualFragment = function(html) {
	        var frag = document.createDocumentFragment(), 
	        div = document.createElement("div");
	        frag.appendChild(div);
	        div.outerHTML = html;
	        return frag;
	    };
	}
	
	//分组的菜单栏，点击箭头，下拉菜单menu上多了2个没什么用的items，将其都去掉。
	if( checkClass("Ext.grid.GroupingView") ){
		Ext.grid.GroupingView.prototype.renderUI = function(){
		    Ext.grid.GroupingView.superclass.renderUI.call(this);
		    this.mainBody.on('mousedown', this.interceptMouse, this);		  
		}
	}
	 
	//在ie8中ext的树形菜单会滚动到右侧，通过去掉focus解决此问题;
	if( checkClass("Ext.tree.TreeNodeUI") ){
		Ext.tree.TreeNodeUI.prototype.onSelectedChange = function(state){
	        if(state){
	            //this.focus();
	            this.addClass("x-tree-selected");
	        }else{
	            
	            this.removeClass("x-tree-selected");
	        }
	    }
		Ext.tree.TreeNodeUI.prototype.onContextMenu = function(e){
	        if (this.node.hasListener("contextmenu") || this.node.getOwnerTree().hasListener("contextmenu")) {
	            e.preventDefault();
	            //this.focus();
	            this.fireEvent("contextmenu", this.node, e);
	        }
		}
	}


	/** 解决gridpanel分页在当前处于 $max - 1 页时，若后端数据减少，那么当前页>$max，可以一直点击下一步的问题。当前的处理策略时允许往后点击一步，但是如果大于总页数，则"下一步","last"按钮置灰 */
	if(checkClass("Ext.PagingToolbar")){
		Ext.override( Ext.PagingToolbar, {
			refreshText : null,
			onLoad : function(store, r, o){
		        if(!this.rendered){
		            this.dsLoaded = [store, r, o];
		            return;
		        }
		        var p = this.getParams();
		        this.cursor = (o.params && o.params[p.start]) ? o.params[p.start] : 0;
		        var d = this.getPageData(), ap = d.activePage, ps = d.pages;
		        
		        this.afterTextItem.setText(String.format(this.afterPageText, d.pages));
		        this.inputItem.setValue(ap);
		        this.first.setDisabled(ap <= 1);
		        this.prev.setDisabled(ap <= 1);
		        this.next.setDisabled(ap >= ps);
		        this.last.setDisabled(ap >= ps);
		        this.refresh.enable();
		        this.updateInfo();
		        this.fireEvent('change', this, d);
		    }
		});
	}
	if(checkClass("Ext.tree.TreeEventModel")){
		Ext.override( Ext.tree.TreeEventModel, {
			getNode : function(e){
        			var t;
			        if(t = e.getTarget('.x-tree-node-el', 10)){
			            var id = Ext.fly(t, '_treeEvents').getAttribute('tree-node-id', 'ext');
			            /* 为了支持ie9树菜单 */
			            if(!id){
			                id = t.getAttribute("ext:tree-node-id");
			            }
			            /* 为了支持ie9树菜单 */
			            if(id){
			                return this.tree.getNodeById(id);
			            }
			        }
			        return null;
			    }
		});
		
	}
	/** 解决本地分页时执行loaddata操作会加载所有的数据而不是第一页的数据的问题 */
	if ( checkClass( "Ext.data.Store" ) ) {
		//这样可以比较动态的控制,就算程序中出现自定义的子类Store也可以控制到，但是这样毫无疑问会增加初始化时的性能开销
		for(var $key in Ext.data ){
			var $class = Ext.data[$key];
			if( $class.prototype instanceof Ext.data.Store ){
				var $function = $class.prototype.loadData;
				//TODO 这里可能会造成闭包引用问题,所以需要多验证
				$class.prototype.loadData = function( data ,  append ){
					//如果存在DataProxy
					if( this.proxy && checkClass("Ext.ux.data.PagingMemoryProxy") ){
						if( this.proxy instanceof Ext.ux.data.PagingMemoryProxy){
							if( append ){
								this.proxy.data.add( data );
							}else{
								this.proxy.data = data;
							}
							var $limit;
							if( this.baseParams ){
								var $limit = this.baseParams.limit || WIN["pageSize"] || 25;
							}else if( WIN["pageSize"]  ){
								var $limit = WIN["pageSize"] || 25;
							}
							//如果是本地分页的store,那么执行loaddata操作后只加载第一页的数据而不是全部的数据
							return this.load({ start : 0,limit : $limit });
						}
					}
					$function.apply( this,arguments );
				}
			}
		};
	}
	if(checkClass("Ext.PagingToolbar")){
		Ext.PagingToolbar.prototype.cls = "extPagingBar";
	}
	
	if(checkClass("Ext.grid.GridPanel")){
		var $clazz = Ext.grid.GridPanel;
		
		if(checkClass("Ext.grid.CheckboxSelectionModel")){
			var $initComponent = $clazz.prototype.initComponent;
			$clazz.prototype.initComponent = function(){
				$initComponent.call(this);
				if(this.selModel && this.selModel instanceof Ext.grid.CheckboxSelectionModel){
					var $this = this;
					if(this.store){
						this.store.on("load",function(){
							var gridEl = $this.getEl();//得到表格的EL对象
							var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
							hd.removeClass('x-grid3-hd-checker-on');
						});
					}
				}
			}
			
			function onMonuseDown(e, t){
				if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
					e.stopEvent();
					var row = e.getTarget('.x-grid3-row');

					// mouseHandled flag check for a duplicate selection (handleMouseDown) call
					if(!this.mouseHandled && row){
						//alert(this.grid.store.getCount());
						var gridEl = this.grid.getEl();//得到表格的EL对象
						var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
						var index = row.rowIndex;
						if(this.isSelected(index)){
							this.deselectRow(index);
							var isChecked = hd.hasClass('x-grid3-hd-checker-on');
							//判断头部的全选CheckBox框是否选中，如果是，则删除
							if(isChecked){
								hd.removeClass('x-grid3-hd-checker-on');
							}
						}else{
							this.selectRow(index, true);
							//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
							if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
								hd.addClass('x-grid3-hd-checker-on');
							};
						}
					}
				}
				this.mouseHandled = false;
			}

			function onHdMouseDown(e, t){
				/**
				*大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
				*由原来的t.className修改为t.className.split(' ')[0]
				*为什么呢？这个是我在快速点击头部全选CheckBox时，
				*操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
				*去全选或者全选不能实现
				*/
				if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
				    e.stopEvent();
				    var hd = Ext.fly(t.parentNode);
				    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				    if(isChecked){
				        hd.removeClass('x-grid3-hd-checker-on');
				        this.clearSelections();
				    }else{
				        hd.addClass('x-grid3-hd-checker-on');
				        this.selectAll();
				    }
				}
			}
			
			
			Ext.override(Ext.grid.CheckboxSelectionModel,{
			    onMouseDown : onMonuseDown,
				onHdMouseDown : onHdMouseDown
			});
		}
	}
	
	// listen
	if(typeof Ext == 'object'){
	    Ext.BLANK_IMAGE_URL = '/images/s.gif';
	    _listen_();
	}
})() 