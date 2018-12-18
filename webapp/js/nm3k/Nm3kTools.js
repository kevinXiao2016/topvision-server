var Nm3kTools = {};

/****  创建带有checkbox的table,支持每行有n列,带有全选，全不选的功能; leexiang 2016-04-14;
*   
*  withStatus 如果要在选择的文字后面加个修改状态，那么设置为true,<label />后面会加入一个<span />标签,并将value计入span的alt中,默认false;
*  renderId   表示要render到哪个div中;
*  lineNum    一行显示几个;
*  title      表格的头部文字;
*  data       表示要传入的数据,必须是数组类型，里面有属性label,value,checked(选填);
*  
*  -------------举例   ----------------
*  Nm3kTools.createCheckBoxTable({
*	 renderId : 'putConfig',
*	 lineNum  : 4,
*	 title    : '请选择你要应用的配置',
*	 data     : [{
*		 label   : '端口使能',
*		 checked : true,
*		 value   : '1'
*	 },{
*		 label   : 'UNI VLAN信息',
*		 value   : '2'
*	 }];
*  -------------举例结束   -------------
*  
****/
Nm3kTools.createCheckBoxTable = function(o){
	this.data       = o.data;
	this.title      = o.title;
	this.lineNum    = o.lineNum;
	this.renderId   = o.renderId;
	this.disabled   = o.disabled ? o.disabled : false;
	this.withStatus = o.withStatus ? o.withStatus : true;
	
	var tpl = new Ext.XTemplate(
			'<table class="dataTable zebra zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">',
			'<thead>',
				'<tr>',
					'<th><input type="checkbox" /></th>',
					String.format('<th colspan="{0}" >{1}</th>',this.lineNum*2-1, this.title),
				'</tr>',
			'</thead>',
			'<tbody>',
				'<tpl for=".">',
				String.format('<tpl if="this.showTr(xindex, {0})">',this.lineNum),
						'<tr>',
					'</tpl>',
				    '<td width="30" align="center"><input {[values.disabled===true ? "disabled=\'disabled\'" : ""]} type="checkbox" {[values.checked===true ? "checked=\'checked\'" : ""]} value="{value}" /></td>',
				   	//首先判断是不是最后一个，如果是，则判断最后一个取模是否是0 如果不是0 ，则要计算colspan;
					String.format('<td colspan="{[xcount === xindex ? ((xindex % {0}=== 0) ? "1" :(({0}-xcount%{0})*2+1)) : "1"]}"><label class="pL5">{label}</label><span alt="{value}"></span></td>',this.lineNum),
				'</tpl>',
			'</tbody>',
		'</table>',{
			showTr   : function(index, lineNum){
				if(index % lineNum === 1){
					return true;
				}else{
					return false;
				}
			}
		}
	);
	tpl.overwrite(Ext.get(this.renderId), this.data);
	
	//判断是否全选了，如果全选了，最顶上那个全选按钮也要勾上;
	var $render  = $("#"+this.renderId),
	    $theadCk = $render.find("thead :checkbox"),
	    $tbodyCk = $render.find("tbody :checkbox:not([disabled])"),
	    ckLength = $render.find("tbody :checkbox:checked").length, //一加载好，有几个选中了;
	    ckboxNum = this.data.length; //一共有多少个checkbox;
	if( ckboxNum === ckLength ){
		$theadCk.attr({checked : "checked"});	
	}
	//全选和全不选功能;
	$theadCk.change(function(){
		if( $(this).is(':checked') ){ //全选按钮,选中的时候;
			$tbodyCk.attr({checked : "checked"});
		}else{
			$tbodyCk.removeAttr("checked");
		}
	});
	//点击单个checkbox,如果全选了，顶部全选checkbox也勾选上，如果去除全选了，顶部checkbox也去除选择;
	$tbodyCk.change(function(){
		if( $(this).is(':checked') ){ 
			if(ckboxNum === $render.find("tbody :checkbox:checked").length){
				$theadCk.attr({checked : "checked"});
			}
		}else{
			$theadCk.removeAttr("checked");
		}
	})
}
Nm3kTools.createCheckBoxTable.prototype.getSelectObject = function(){
	var arr     = this.getSelectValue(),
	    arrLen  = arr.length,
	    data    = this.clone(this.data),
	    dataLen = data.length,
	    objArr  = [];
	
	for(var i=0; i<arrLen; i++){
		for(var j=0; j<dataLen; j++){
			if(arr[i] == data[j].value){
				objArr.push(data[j]);
				data[j].checked = true;
				break;
			}
		}
	}
	return objArr;
}
//获取选中value的数组;
Nm3kTools.createCheckBoxTable.prototype.getSelectValue = function(){
	var $ck = $("#"+ this.renderId + " tbody").find(":checked"),
	    checkArr = [];
	$ck.each(function(){
		var v = $(this).val();
		checkArr.push(v);
	})
	return checkArr;
}

/*设置状态[正在应用];
 * 
 * {
 *     value //通过value来查找,checkbox的value一定是唯一的; 
 *     text  //设置状态文字,正在应用;应用成功;失败 99;
 *     cls   //设置样式，蓝色 blueLabel 绿色greenLabel 红色redLabel 闪烁 animated infinite flash
 * }
 * 
 */
Nm3kTools.createCheckBoxTable.prototype.setStatus = function(o){
	var $ck = $("#"+ this.renderId + " tbody span[alt='"+ o.value +"']");
	if(o.text){ $ck.text(o.text); }
	if(o.cls){ $ck.attr("class",o.cls)}
}
/*
 * 设置checkbox的disabled属性;
 */
Nm3kTools.createCheckBoxTable.prototype.setDisable = function(b){
	var $ck = $("#"+ this.renderId).find(":checkbox");
	if(b){
		$ck.attr({disabled : 'disabled'});
	}else{
		$ck.removeAttr('disabled');
	}
}
Nm3kTools.createCheckBoxTable.prototype.clone = function(obj){  
    var o;  
    if(typeof obj == "object"){  
        if(obj === null){  
            o = null;  
        }else{  
            if(obj instanceof Array){  
                o = [];  
                for(var i = 0, len = obj.length; i < len; i++){  
                    o.push(this.clone(obj[i]));  
                }  
            }else{  
                o = {};  
                for(var k in obj){  
                    o[k] = this.clone(obj[k]);  
                }  
            }  
        }  
    }else{  
        o = obj;  
    }  
    return o;  
}  
//end Nm3kTools.createCheckBoxTable;

Nm3kTools.tools = {
    firstStart : 0, //从第0位开始匹配，就算是匹配成功了，后面的也需要匹配;
    matchArr : [],  //匹配中的放入数组;
    searchReplace : function(str, matchStr, tag){ //str 长字符串, matchStr 要匹配的字符串, tag 要替换成的标签，比如<label /> 则只需要输入'label'; 
        var index = str.indexOf(matchStr, this.firstStart),
            matchLen = matchStr.length
            len = str.length;
    	
        if(index >= 0){
    	    var str2 = str.slice(this.firstStart, index);
            var str3 = str.slice(index + matchLen, len);
            this.matchArr.push(str2);
            return this.searchReplace(str3, matchStr, tag);
        }else{//匹配结束;
            var matchArr = this.matchArr;
		    if(matchArr.length >= 1) { //存在匹配项
		        matchArr.push(str);
            var str4 = '';
		    for (var i = 0; i < matchArr.length - 1; i++) {
		        str4 = String.format(str4 + matchArr[i] + "<{0}>" + matchStr + "</{0}>", tag);
		    }
		        str4 += str;
		        this.resetFn();
		        return str4;
			}else{
			    this.resetFn();
				return str;
			}
		}
    },
    resetFn : function(){
    	this.matchArr.length = 0;
    },
    /**
     * 将字符串'false'和''（空）转换成布尔型false，将字符串'true'转换成布尔型true
     * 
     * @param  {string}  需要转换的字符串
     * @return {boolean} 返回布尔型
     */
    StringToBoolean : function(str){
    	switch(str){
    	case 'false':
    	case '':
    		return false;
    		break;
    	case 'true':
    		return true;
    		break;
    	default : 
    		return false;
    		break;
    	}
    }
}









