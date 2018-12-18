//  在告警查看器中，一次只能查看一种告警类型(通过typeId);
//  在关注告警中，需要一次查看多个告警类型(通过typeIdList);
//  因此，循环数据，把父节点的value值从单个typeId改成typeIdList；
function changeParentNodeValue(arr){
	$.each(arr, function(i, v){
		if(!(typeof(v.value) instanceof Array)){
			v.value = [];
		}
		if(v.children && v.children.length > 0){ //存在子集;
			addParentNodeValue(v.children, v);	
			changeParentNodeValue(v.children);
		}else{ //不存在子集;
			v.value.push(v.id);
		}
	});
}
function addParentNodeValue(arr, node){
	$.each(arr, function(i, v){
		if(v.children && v.children.length > 0){ 
			addParentNodeValue(v.children, node)
		}else{
			node.value.push(v.id);
		}
	});
}
//将所有Array类型的value值，转换为逗号隔开的数组;
function valueToString(arr){
	$.each(arr, function(i, v){
		v.value = v.value.join(',');
		if(v.children && v.children.length > 0){
			valueToString(v.children);
		}
	});
}