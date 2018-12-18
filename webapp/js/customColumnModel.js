/**
 * 为指定的columnModel和grid加上保存习惯的功能
 * 
 * 避免id重复或混乱，将使用过的id列举在这里;
 * ==================设备管理板块=====================
 * 资源列表 : entitySnapList
 * cm列表 : cmlist
 * CPE列表 ： cmcpelist
 * OLT列表 : oltlist
 * OLT端口信息下 sni端口信息 : oltSniList    pon端口信息 : oltPonList
 * ONU设备列表 : onulist
 * CMTS列表 : cmclist
 * bsr设备内的cm列表页面 : cmts_cmlist
 * 类B型设备内的cm列表页面 ： ccmts_cmlist
 * ==================拓扑视图板块=====================
 * 拓扑图列表查看 : topoDetailView
 * 设备列表 : entityList
 * ==================故障管理板块=====================
 * 当前告警 : currentAlertList
 * 历史告警 : historyAlertList
 * 告警过滤 : alertFilter
 * 
 */
var CustomColumnModel = (function() {
	var defaultColumnConfig = {
		header : '',
		width : 100,
		sortable : true,
		align : 'left',
		dataIndex : '',
		renderer : null
	};
	
	var formatColumns = function(columns){
		var _saveColumns = [], curCol;
		for ( var i = 0, len = columns.length; i < len; i++) {
			curCol = columns[i];
			if (curCol.id != 'checker' && !curCol.expandRow) {
				_saveColumns.push({
					header : curCol.header,
					hidden : curCol.hidden || false,
					order : i,
					width : curCol.width
				});
			}
		}
		return _saveColumns;
	}

	var saveCustom = function(id, columns) {
		var _saveColumns = formatColumns(columns);
		$.post('/system/saveCustomColumns.tv', {
			id : id,
			columns : Ext.encode(_saveColumns)
		});
	}
	
	var saveSortInfo = function(id, sortInfo){
		$.post('/system/saveCustomSortInfo.tv', {
			id : id,
			sortInfo : Ext.encode(sortInfo)
		});
	}

	var getColumnModelConfig = function(id) {
		var cmConfig = null;
		$.ajax({
			url : '/system/getColumnModelConfig.tv',
			async : false,
			data : {
				id : id
			},
			success : function(json) {
				if (json) {
					cmConfig = {
						columns: json.columns,
						sortInfo: json.sortInfo
					}
				}
			}
		});
		
		
		return cmConfig;
	}
	
	//第一次进入，显示提示信息;
	function showFirstTip(){
		if($("#firstTip").length == 0){
			var str = '<div id="firstTip" class="jsTip"></div><img class="firstTipImg jsTip" src="../../images/cmListNew_tip_@COMMON.language2@.png" />'
			$('body').append(str)
		}
	}
	
	var updateColumns = function(default_columns, columns){
		var cur_column, exist;
		//遍历每一个列，更新width，hidden，order属性
		for(var i=0, len=default_columns.length; i<len; i++){
			cur_column = default_columns[i];
			exist = false;
			for(var j=0, jLen=columns.length; j<jLen; j++){
				// add by fanzidong @20170621 fixed列宽度不使用个性化覆盖, 以便及时更新
				if(cur_column.fixed) {
					var _width = cur_column.width;
				}
				if(cur_column.header == columns[j].header){
					Ext.apply(cur_column, columns[j]);
					exist = true;
					if(cur_column.fixed) {
						cur_column.width = _width;
					}
					break;
				}
			}
			//如果该列没有保存过，表明是后来加的，需要保持现在所在的位置
			if(!exist){
				cur_column.order = i;
				//将所有order>=i的全部+1
				Ext.each(columns, function(v){
					if(v.order>=i){
						++v.order;
					}
				});
			}
		}
		//根据order进行排序
		default_columns.sort(function(a, b){
			return a.order - b.order;
		});
	}

	var pub = {
		// 初始化支持用户自定义保存的columnModel
		// 传入的列信息结构为:{header, width, sortable, align, dataIndex, renderer}
		init : function(id, default_columns, options) {
			//点击黑色背景上提示，提示消失;
			$(".jsTip").live("click",function(){
				$(".jsTip").fadeOut();
				top.fadeCmlistTip();
			})
			// 检查该用户有没有自定义该columnModel的显示
			var cmConfig = getColumnModelConfig(id);
			//如果没有保存过对该columnModel的配置，需要告知该功能提示，并直接保存
			if (!cmConfig.columns) {
				saveCustom(id, default_columns);
				// 添加提示
				top.pageHasHelp() && showFirstTip();
			}else{
				updateColumns(default_columns, cmConfig.columns);
			}
			if (options && options.expander) {
				default_columns.unshift(options.expander);
			}
			if(options && options.sm){
				default_columns.unshift(options.sm);
			}
			var cm = new Ext.grid.ColumnModel({
				defaults : {
					menuDisabled : false
				},
				listeners : {
					hiddenchange : function(cm) {
						saveCustom(id, cm.columns)
					},
					columnmoved : function(cm) {
						saveCustom(id, cm.columns)
					},
					widthchange: function(cm, colIndex, width){
						saveCustom(id, cm.columns)
					}
				},
				columns : default_columns
			});
			return {
				cm: cm,
				sortInfo: cmConfig.sortInfo
			};
		},
		addCustom : function(id, cm) {
			cm.on('hiddenchange', function() {
				saveCustom(id, cm.columns)
			});
			cm.on('columnmoved', function() {
				saveCustom(id, cm.columns)
			});
			cm.on('widthchange', function() {
				saveCustom(id, cm.columns)
			});
		},
		saveCustom: saveCustom,
		saveSortInfo: saveSortInfo
	};
	return pub;
})();
