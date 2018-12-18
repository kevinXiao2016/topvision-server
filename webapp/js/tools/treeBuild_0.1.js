var treeBuild = {};

treeBuild.liStrWithoutCbx = '<li id={0}><span class="{2}">{1}</span></li>';
treeBuild.liStrWithCbx = '<li id={0}><input type="checkbox" {3}/><span class="{2}">{1}</span></li>';

treeBuild.generateTree = function(rootId, treeNode, needCbx, cbxAttribute){
	//遍历并递归插入其子节点
	recursionFolders(treeNode);
	
	function recursionFolders(treeNode){
		//加入当前节点
		insertCurrentNode(treeNode);
		//取得当前节点的子节点数组
		var subNodes = treeNode.children;
		//遍历子节点数组并进行添加
		$.each(subNodes, function(index, subNode){
			//添加当前节点及其子节点
			recursionFolders(subNode);
		});
	}
	
	function insertCurrentNode(treeNode){
		//如果该节点存在父节点，则添加置其父节点下，不存在则添加置根节点下
		var parentId = (treeNode.parentId==-1) ? rootId : "tree_"+treeNode.parentId;
		
		//获取该父节点下的ul节点
		//1.该父节点本身就是ul，那么直接在其下添加即可
		//2.该父节点是li，需要判断其下是否有ul节点，没有则添加，有则添加在其下
		
		//获取其父节点所在元素
		var $parentDom = $("#"+parentId);
		
		if($parentDom[0].nodeName==='LI'){
			//如果其下不存在ul，则为其添加
			if($parentDom.find('ul').length===0){
				$parentDom.append('<ul></ul>');
			}
			//获取该父节点下的ul节点
			$parentDom = $parentDom.find('ul').first();
		}
		//在该节点下添加当前节点
		var currenrNodeLiStr = '';
		if(needCbx){
			currenrNodeLiStr = String.format(treeBuild.liStrWithCbx, "tree_"+treeNode.id, treeNode.text, treeNode.iconCls, treeNode[cbxAttribute] ? 'checked="checked"' : '');
		}else{
			currenrNodeLiStr = String.format(treeBuild.liStrWithoutCbx, "tree_"+treeNode.id, treeNode.text, treeNode.iconCls);
		}
		$(currenrNodeLiStr).appendTo($parentDom);
	}
}
