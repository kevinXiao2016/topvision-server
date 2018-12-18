//对输入的表达式进行校验  @auther zhangdongming
function parse(expression,level){
	var NOT_EXIST = -1,
		ANY_MATCH = "*",
		ERROR_EXPRE = 1,//格式错误
		NOT_EXIST_TARGET = 2,
		UN_COMPLETELD = 3,
		PASS = 0;
	var splits = expression.split(new RegExp("[/:]","g"));
	if(level == null){
		//nothing to do
	}else if(level != splits.length){
		return UN_COMPLETELD;
	}
	var $result = PASS;
	for(var i=0;i<splits.length;i++){
		var split = splits[i];
		$result =  isExpre(split,i);
		if($result > 0){
			return $result;
		}
	}
	return $result;

	function isExpre(expr,level){
		if(!expr || expr.search("\w/gi") > -1){
			return ERROR_EXPRE;
		}
		if(expr.search("\\(") == 0){
			if( expr.search(")") == expr.length-1 ){
				expr = expr.replace("(|)","");
			}else{
				return ERROR_EXPRE;
			}
		}
		if(expr == ANY_MATCH ){
			return PASS;
		}
		var l1 = expr.split(",");
		for(var i=0; i<l1.length; i++){
			var seg = l1[i];
			if(/^\d+$/gi.test(seg)){
				continue;
			}
			if(/^[1-9][\d]{0,}[-][1-9][\d]{0,}$/g.test(seg)) {
				continue;
			}
			return ERROR_EXPRE;
		}
		return PASS;
	}
}