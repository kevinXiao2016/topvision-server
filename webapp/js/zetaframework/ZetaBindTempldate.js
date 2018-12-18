var ZetaBindTempldate = function(params){
	
	/**
	  {
		BLOCK-port : {
			attributes : { portEnabled : false },
			title 
			url : null,
			validateHandler : this.emptyFn
		},
		BLOCK-uniblan : {
			attributes : { pvid : 5,vlanmode : 1}
		},
		BLOCK-rate : {
			attributes : { inEnabled : true,cir : 1,cbs : 2 }
		},
		BLOCK-workMode : {
			....
		}
	  }	
	**/
	var CACHE = {},
		requestQuene = {},
		baseParams = {},
		emptyFn = function(){};

	/**
	*  deep copy
	**/
	this.bind = function(params,cache){
		cache = cache == null ? CACHE : cache;
		for(var $param in params){
			if (typeof params[$param] == 'object') {
				var $object = cache[$param] = {};
				this.bind(params[$param],$object);
			} else {
				cache[$param] = params[$param];
			}
		}
	};
	
	this.reset = function(data){
		for(var $param in data){
			if(typeof data[$param] != "object"){
				baseParams[$param] = data[$param];
				continue;
			}
			$attributes = CACHE[$param].attributes;
			if($attributes == null){
				$attributes = CACHE[$param].attributes = {};
			}
			var $blockdata = data[$param];
			for(var $attribute in $blockdata){
				$attributes[$attribute] = $blockdata[$attribute];
				/**update data in page*/
				this.update($attribute,$blockdata[$attribute] == null?"":$blockdata[$attribute]);
			}
		}
	}
	
	/**
	 * 更新界面中的配置值
	 */
	this.update = function($id,$value){
		var $element = DOC.get($id);
		switch($element.tagName){
			case "DIV" :$element.innerText = $value; break;
			case "INPUT":
				var $type = $element.type.toUpperCase();
				if($type == "CHECKBOX"){
					$element.checked = $value;
				}else if($type == "TEXT" || $type == "PASSWORD"){
					$element.value = $value;
				}
				break;
			case "A":break;
			case "SELECT":$($element).val($value);break;
			case "IMG":
				this.renderOnOff({
					id:$id, value:$value
				});
				break;
		}
	}

	this.bindRequestUrl = function(blockname,requestUrl){
		if(params[blockname] != null){
			params[blockname].url = requestUrl;
		}
	};

	 /**
	  * 加载on off开关;
	  */
	 this.renderOnOff = function(obj){
		 var $img = $("#" + obj.id);
		 if(obj.value){
			 $img.attr({src : '../../images/speOn.png', alt : 'on'});
		 }else{
			 $img.attr({src : '../../images/speOff.png', alt : 'off'});
		 }
	 }
	
	/**
	 * @{params} 附加参数,区别于界面可见的配置
	 * @{overridResultHandler} 覆盖默认的结果处理
	 * 
	 */
	this.commit = function(params, overridResultHandler,message,callback){
		message = message || "@NM3K/zetaBindTeamplate.applying@";
		if(typeof params == 'function'){
			overridResultHandler = params;
		}else if(params != null && typeof params == 'object'){
			for(var param in params){
				baseParams[param] = params[param];
			}
		}
		/***reset requestQuene***/
		requestQuene = {};
		var length = 0;
		for( var $param in CACHE ){
			//var $data = data[$param];
			var $data = this.returnCompareData($param,CACHE[$param].attributes);
			/** $data is data object */
			if($data != null){
				if(this._validate(CACHE[$param],$data)){
					requestQuene[$param] = $data;
					length++;
				}else{
					return;
				}
			}
		}
		
		var successList = [];
		var faildList = [];
		var paramCount = 0;
		window.top.showWaitingDlg("@COMMON.wait@", message);
		for(var $param in requestQuene){
			paramCount++;
			var data = requestQuene[$param];
			var config = CACHE[$param];
			/**overide request params*/
			for(var param in baseParams){
				if(data[param] == null){
					data[param] = baseParams[param];
				}
			}
			sendRequest(config.url,data,config.title);
		};
		if(paramCount == 0){
			window.parent.showMessageDlg("@BASE/COMMON.tip@","@NM3K/zetaBindTeamplate.nochange@");
		}
		
		function futureCheck(){
			var $length = successList.length+faildList.length;
			if(length == $length){
				if(typeof overridResultHandler == 'function'){
					return overridResultHandler(successList,faildList);
				}
				var message = "";
				for(var i=0; i<successList.length;i++){
					message += successList[i] + " <font color='green'>@BASE/COMMON.success@</font><br>";
				}
				for(var i=0; i<faildList.length;i++){
					message += faildList[i] + " <font color='red'>@BASE/COMMON.fail@</font><br>";
				}
				window.parent.showMessageDlg("@BASE/COMMON.tip@",message);
				callback && callback();
			}
		}
		
		function sendRequest(url,data,title){
			$.ajax({
				url:url,data:data,cache:false,
				success:function(){
					successList.add(title);
					futureCheck();
				},error:function(){
					faildList.add(title);
					futureCheck();
				}
			});
		}
	};

	/**
	*   validate current config ,and compare
	*	@{blockname} : allow null,validate the assigned block,if null,validate all block
	*   return @{result} : validate result		
	**/
	this.returnCompareData = function(blockname,attributes){
		var $data = {};
		if(this._compare(attributes,$data)){
			return null;
		}
		return $data;
	};

	this.retriveValue = function($id){
		var $element = DOC.get($id);
		var $jo = $($element);
		switch($element.tagName){
			case "DIV" :return $element.innerText;
			case "INPUT":
				var $type = $element.type.toUpperCase();
				if($type == "CHECKBOX"){
					return $jo.is(":checked");
				}else if($type == "TEXT" || $type == "PASSWORD"){
					return $element.value;
				}
				break;
			case "A":break;
			case "SELECT":return $jo.val();
			case "IMG": return $jo.attr("alt") == "on";
		}
	}
	
	/**
	* 检查前后的数据是否一致,如果一致则允许发送请求
	* validate 和check是两码事
	**/
	this._compare = function(attributes,returnData){
		var needCommit = true;
		for(var $attribute in attributes){
			var $value = this.retriveValue($attribute);
			returnData[$attribute] = $value;
			if( attributes[$attribute] == $value){
				continue;
			}
			/*只要有一个数据不同,则表示需要提交*/
			needCommit =  false;
		}
		return needCommit;
	};

	this._validate = function(config,data){
		var $handler = config.validate;
		if($handler != null){
			return $handler(data);
		}
		return true;
	};
	
	/**
	 * 仅仅验证界面数据是否符合要求
	 */
	this.validate = function(){
		for(var $blockName in CACHE){
			if (typeof CACHE[$blockName] == 'object') {
				var $blockData = this.returnConfig($blockName);
				if(!this._validate(CACHE[$blockName], $blockData)){
					return false;
				}
			} 
		}
		return true;
	}
	
	/**
	 * 提供一种获取某块配置的对象
	 * @{blockName} 块名称
	 */
	this.returnConfig = function(blockName){
		var $data = {};
		var $block = CACHE[blockName];
		for(var $attribute in $block.attributes){
			var $value = this.retriveValue($attribute);
			$data[$attribute] = $value;
		}
		return {
			attributes : $data,
			url:$block.url,
			title:$block.title
		};
	}
	
	if(params != null){
		this.bind(params,null);
	}
	
	this.getConfig = function(config){
		return CACHE[config];
	}
	
	return this;
	
}