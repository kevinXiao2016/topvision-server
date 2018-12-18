//当ext的加载数据的dataGrid 加载完之后调用，实现隔行变色;
	function extZebra(){
		$(".extZebra").each(function(){
			$(this).find(".x-grid3-row:even").addClass("light");
		});//end each;
	};
	
	//移动到ext的dataGrid 上，变成白色高亮;
	$(function(){
		$(".extZebra .x-grid3-row").live("mouseover",function(){
			$(this).addClass("gridSelected");
		}).live("mouseout",function(){
			$(this).removeClass("gridSelected");
		});//end live;
	});//end document.ready;