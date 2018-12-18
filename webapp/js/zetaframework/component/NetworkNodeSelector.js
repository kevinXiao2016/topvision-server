function NetworkNodeSelector( param ){
	if( $("#"+this.id).length>0 ){
		return;
	}
	Ext.apply(this, param, {
		ipField: 'ip',
		macField: 'mac',
		nameField: 'name',
		sysNameField: 'sysName',
		entityIdField: 'entityId',
		displayField: 'name',
		include8800B: false
	});
	this.DISP_MAX_LENGTH = 40;
	this.isOpen = false;
	this.selectorId = this.id + "_selector";
	this.subWidth = param.subWidth || 220;//底部弹出窗的宽度;
	if(this.subWidth < this.width){this.subWidth = this.width-10};//下部宽度不能小于上部宽度，否则会很丑,减去10是因为padding:5px;
	this.subMinHeight = param.subMinHeight || 40;//底部弹出框的最小高度,默认为40;
	this.rowNum = param.rowNum || 2; //z每行显示几个,默认显示2个;
	this.firstSelect = param.firstSelect || 0;//默认选择第1个;
	if(typeof this.renderTo == "string"){
		this.$renderTo = $("#" + this.renderTo ) ;
	}else{
		this.$renderTo = $( this.renderTo ) ;
	}
	if(!this.data){
		this.preConstruct.call(this);
	}else{
		this.constructor.call(this);
		this.addEventListeners.call(this);
	}
} 
NetworkNodeSelector.prototype = {
	wrapperTpl : '<div class="Nm3kSelect" style="width:{width}px;"><p></p><a class="Nm3kSelectArr" href="javascript:;"></a><input type="hidden" id="{id}" name="{id}" /></div>',
	constructor : function(){
		var $wrapper = String.format(this.wrapperTpl, this);
		this.$renderTo.html( $wrapper );
		var $selector = this.selector = $("<div>");
		$selector.css({
			position:'absolute',
			left : this.$renderTo.position().left,
			top : this.$renderTo.position().top + 25
		});
		var selectorBulider = '';
		selectorBulider += '<div class="Nm3KSelectSub" id="'+ this.selectorId +'" style="width:'+ this.subWidth +'px; height:'+this.subMinHeight+'display:none;"><div class="getHeight" style="overflow:auto;white-space:normal; word-break:break-all; word-wrap:break-word;">';
		selectorBulider +=	'	<table cellpadding="0" cellspacing="0" rules="all" border="1" bordercolor="#E0E0E0" style="border-collapse:collapse;width:100%;">';
		selectorBulider +=	'		<tbody>';
			var $rowNum = this.rowNum;
			var defaultOption = {};
			defaultOption[ this.sysNameField ] = defaultOption[ this.macField ] = defaultOption[ this.ipField ] = defaultOption[ this.nameField ] = "@base/COMMON.select@";
			defaultOption.entityId = -1;
			this.data.unshift( defaultOption );
			//先把数组除以个数，得到要分成几个tr;
			//var trNum = Math.ceil(this.data.length / $rowNum);
			var $columns = 0;
			for(var i=0; i<this.data.length; i++ ){
				if( i%$rowNum == 0 ){
					selectorBulider += "<tr>";
					$columns++;
				}
				var $entity = this.data[i];
				var $disp = $entity[this.displayField] || $entity.ip;
				var $text = $disp.ellipse(this.DISP_MAX_LENGTH);
				var $option = String.format('<td width="50%"><a class="Nm3kOptionItem" href="javascript:void(0);" value="{0}" title="{6}" ip="{2}" mac="{3}" name="{4}" sysName="{5}">{1}</a></td>', $entity[ this.entityIdField ] || EMPTY, $text || EMPTY, $entity[this.ipField] || EMPTY, $entity[this.macField] || EMPTY , $entity[this.nameField] || EMPTY , $entity[this.sysNameField] || EMPTY, $disp ) ;
				//selectorBulider += '<td><a class="Nm3kOptionItem" href="javascript:void(0);" value="'+ $entity["entityId"] +'">' + $entity[ this.displayField ] + '</a></td>';
				selectorBulider += $option;
				if( i%$rowNum == $rowNum - 1 ){
					selectorBulider += "</tr>";
				}
			}
			this.$columns = $columns;
		selectorBulider +=	'		</tbody>';
		selectorBulider +=	'	</table></div>';
		/// <a href="javascript:void(0);" class="Nm3KSelectDisp" index="mac">MAC</a></div>
		selectorBulider +=	'<div class="lightGrayTxt">@workbench/entity.displayMethod@: <a href="javascript:void(0);" class="Nm3KSelectDisp" index="name">@base/COMMON.alias@</a> / <a href="javascript:void(0);" class="Nm3KSelectDisp" index="ip">IP</a> / <a href="javascript:void(0);" class="Nm3KSelectDisp" index="sysName">@base/COMMON.name@</a>';
		selectorBulider +=	'</div>';
		$selector.html( selectorBulider );
		$( DOC.body ).append( $selector );
		$(".Nm3KSelectDisp[index="+this.displayField+"]").css("color","blue");
	},
	preConstruct : function(){
		var $this = this;
		$.ajax({
			url:'/network/queryAllEntityList.tv',cache:false,dataType:'json',async:false,
			data:{ include8800B: $this.include8800B },
			success:function(json){
				$this.data = json.list;
				$this.displayField = json.displayField ||  $this.displayField;
				$this.constructor.call($this);
				$this.addEventListeners.call( $this );
			}
		});
	},
	setValue : function( $value ){
		this.$renderTo.find("input").val( $value );
		this.selector.find(".Nm3kOptionItem[value='"+$value+"']").parent().click();
	},
	postConstruct : function(){
		var $value = this.value || -1;
		this.setValue( $value );
	},
	doLayout : function(){
		this.selector.css({
			position:'absolute',
			left : this.$renderTo.position().left,
			top : this.$renderTo.position().top + 25
		});
	},
	addEventListeners: function(){
		var $this = this;
		this.$renderTo.bind("click",function(event){
			event.stopPropagation();
			if( $this.autoLayout ){
				$this.doLayout();
			}
			if( $this.$columns > 10 ){
				var h = $(WIN).height() - 80;
				$this.selector.find(".getHeight").height( h );
			}
			//}
			var $target = event.target;
			while( $target.id != $this.renderTo ){
				$target = $target.parentNode;
			}
			if( $this.isOpen ){
				$this.selector.find(".Nm3KSelectSub").hide();
			}else{
				$this.selector.find(".Nm3KSelectSub").show();
			}
			if( !$this.rendered ){
				var $sub = $this.selector.find(".Nm3KSelectSub");
				$sub.width( $sub.find("table").width() + 2 )
				$this.rendered = true;
			}
			$this.isOpen = !$this.isOpen;
		});
		$("html").bind("click",function(){
			$this.selector.find(".Nm3KSelectSub").hide();
			$this.isOpen = false;
		});
		this.selector.find("tbody td").bind("click", function(event){
			event.stopPropagation();
			var $text = $(this).find("a").text();
			$this.selector.find("td").removeClass("selected");
			$(this).addClass("selected");
			var $select = $this.selector.find(".Nm3kSelect");
			$this.$renderTo.find("p").text( $text ).attr("title", $text);
			var $entityValue =  $(this).find("a").attr("value");
			$this.$renderTo.find("input").val( $entityValue );
			$this.selector.find(".Nm3KSelectSub").hide();
			$this.value = $entityValue;
			$this.isOpen = false;
			if( $this.listeners && $this.listeners.selectChange ){
				$this.listeners.selectChange({
					value : $entityValue,
					text : $text
				});
			}
			$this.selected = this;
		});
		this.selector.find(".lightGrayTxt").bind("click",function(event){
			event.stopPropagation();
			var $target = $(event.target);
			if(event.target.className == "Nm3KSelectDisp"){
				var $index = $target.attr("index");
				var $options = $this.selector.find("a.Nm3kOptionItem");
				$.each( $options, function(i,option){
					var $raw = $(option).attr($index);
					var $disp = $raw.ellipse( $this.DISP_MAX_LENGTH ).toString();
					$(option).html( $disp ).attr("title", $raw);
				});
				if( $this.value ){
					var $text = $($this.selected).find("a").attr( $index );
					$this.$renderTo.find("p").html( $text ).attr("title", $text);
				}
				$this.saveUserPreference( $index );
			}
			$(".Nm3KSelectDisp").css("color","gray");
			event.target.style.color = "blue";
		});
		this.postConstruct.call(this);
	},
	saveUserPreference : function(displayField){
		$.ajax({
			url:'/userPreference/saveUserPreference.tv',cache:false,
			data:{
				key : "user.displayField", 
				value : displayField
			}
		});
	}
};