var withNoALiFormatStr = '<li id="{1}"><span class="{0}">{2}</span></li>';
var isEntityLink = '<li><span><a id="{0}" href="javascript:;" data-parentId="{1}" class="oltLink" data-parent="{3}" data-typeId="{4}">{2}</a></span></li>';
var liFormatStr = '<li id="{2}"><span class="{0}"><a href="javascript:;" onclick="{1}" class="linkBtn" id="{2}" name="{3}" url="{4}" creatorName="{5}">{3}</a></span></li>';
var liFormatStrWithCbx = '<li id={1} class="vertical-middle"><span class="{0}"><input class="alertCbx" id="{1}" type="checkbox" {3}/><a href="javascript:;" class="linkBtn" id="{1}">{2}</a></span></li>';

/**
 * 将rootNode及其子节点添加到id为tree的ul(这是本系统约定的树结构的根结点)下面，创建树结构
 * rootNode的children中包含其子节点信息
 * rootNode需要拥有id, parentId, text, iconCls, children属性
 * @param rootNode
 */
function bulidTree(rootNode, nodeClickMethod, rootUl){
	var curLiStr = '';
	if(rootNode.needATag){
		curLiStr = String.format(liFormatStr, rootNode.iconCls, nodeClickMethod, rootNode.id, rootNode.text, rootNode.url, rootNode.name);
	}else{
		curLiStr = String.format(withNoALiFormatStr, rootNode.iconCls, rootNode.id, rootNode.text);
	}
	var $li = $(curLiStr);
	if((typeof rootUl)=='undefined'){
		$li.appendTo($('ul#tree'));
		recursionFolders(rootNode, nodeClickMethod, 'tree');
	}else{
		$li.appendTo($('ul#'+rootUl));
		recursionFolders(rootNode, nodeClickMethod, rootUl);
	}
}

function recursionFolders(node, nodeClickMethod, rootUl, parentNode){
	//加入当前节点
	addToParentNode(node, nodeClickMethod, rootUl, parentNode);
	//取得当前地域的子节点数组
	var subNodes = node.children;
	//遍历子节点数组
	$.each(subNodes, function(index, subNode){
		//添加当前节点及其子节点
		recursionFolders(subNode, nodeClickMethod, rootUl, node);
	});
}

function addToParentNode(folder, nodeClickMethod, rootUl){
	//获取其父节点所在的li
	//var $parentLi = $("li#"+folder.parentId);
	var $parentLi = $('ul#'+ rootUl).find("li#"+folder.parentId);
	//如果其下不存在ul，则为其添加
	if($parentLi.find('ul').length==0){
		$parentLi.append('<ul></ul>');
	}
	var $ul = $('ul#'+ rootUl).find("li#"+folder.parentId+ " ul").first();
	//为父节点添加当前结点
	var curLiStr = '';
	if(folder.needATag){
		if(folder.needCbx){
			var checked = '';
			if(folder.checked){
				checked='checked="checked"';
			}
			curLiStr = String.format(liFormatStrWithCbx, folder.iconCls, folder.id, folder.text, checked);
		}else{
			curLiStr = String.format(liFormatStr, folder.iconCls, nodeClickMethod, folder.id, folder.text, folder.url, folder.name);
		}
	}else if(folder.entity){ //地域树中间夹着olt设备;
		var parentTxt = "";
		if(arguments[3].text){parentTxt = arguments[3].text;}
		curLiStr = String.format(isEntityLink, folder.id, folder.parentId, folder.text, parentTxt, folder.typeId);
	}else{
		curLiStr = String.format(withNoALiFormatStr, folder.iconCls, folder.id, folder.text);
	}
	$(curLiStr).appendTo($ul);
}

/**
 * 树结构的一些基本动画效果
 */
function treeBasicHandle(){
	//加载树形菜单;
	$("#tree").treeview({ 
		animated: "fast",
		control:"#sliderOuter",
		collapsed: false
	});	//end treeview;
	$("#sliderLeftBtn").click(function(){
		$("#bar").stop().animate({left:0});			
	})
	$("#sliderRightBtn").click(function(){
		$("#bar").stop().animate({left:88});		
	})
	
	$("#sliderLeftBtn").click(function(){
		$("#bar").stop().animate({left:0});			
	})
	$("#sliderRightBtn").click(function(){
		$("#bar").stop().animate({left:88});		
	})
	
	//点击树形节点变橙色背景;
	$(".linkBtn").live("click",function(){
		$(".linkBtn").removeClass("selectedTree");		
		$(this).addClass("selectedTree");
	});//end live;
}

function treeExpand(){
	//加载树形菜单;
	$("#tree").treeview({ 
		animated: "fast",
		control:"#sliderOuter"
	});	//end treeview;
	$("#sliderLeftBtn").click(function(){
		$("#bar").stop().animate({left:0});			
	})
	$("#sliderRightBtn").click(function(){
		$("#bar").stop().animate({left:88});		
	})
	
	$("#sliderLeftBtn").click(function(){
		$("#bar").stop().animate({left:0});			
	})
	$("#sliderRightBtn").click(function(){
		$("#bar").stop().animate({left:88});		
	})
	
	//点击树形节点变橙色背景;
	$(".linkBtn").live("click",function(){
		$(".linkBtn").removeClass("selectedTree");
		$(this).addClass("selectedTree");
	});//end live;
}

/**
 * 树上的浮动指示条
 */
function treeFloatTip(){
	$("#tree a").live("mouseover",function(){
        var $this = $(this);
        var w = $this.offset().left + $this.outerWidth();       
        if( w >= 186){
            var txt = $this.text();
            var tPos = $this.offset().top + 100;
            var o = {text:txt,display:"show",top:tPos};
            top.redTip(o);
        }
    }).live("mouseout",function(){
        var $this = $(this);
        var w = $this.offset().left + $this.outerWidth(); 
        if(w >= 186){
            top.redTip({display:"hide"});
        }
    });
}
