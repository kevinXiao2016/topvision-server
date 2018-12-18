/*********************************************************************
 * nm3kScollBarContainer.js (c) 2013,Topvision Copyrights
 * 
 * @author lixiang
 **********************************************************************/
function nm3kScrollBarContainer(para){
    //configuration
	this.height = para.height ? para.height : 140;
	this.renderTo = para.renderTo;
	this.data = para.data;
	this.offsetY = para.offsetY ? para.offsetY : 0;
	this.movePx = para.movePx ? para.movePx : 10;//每次移动几个像素;
	this.speed = 1; //选项卡移动的间隔;
	this.downAble = false; //点击下了箭头;
	this.movement = null; //是否在执行移动动画;
	this.oldThis = null;
	this.select = para.select;
	this.pages = 0;//共有多少页;
	this.pageNum = 200; //每页最多显示200条数据;
	this.nowPage = 0;//当前第几页;
	//dom pointer
	this.totalElement = $("#nowPageEqCount");
	//initialize
	this.init();
	//listen event
	this.addEventListeners();
}



//nm3kScrollBarContainer.prototype.ITEM_TEMPLATE = '<div class="imgSharden"><a href="javascript:;" class="jsImgOuter" onmousedown="selectedThisImg(this)" onmouseover="overSubName(this)" onmouseout="outSubName(this)"><div class="putSubEqImg"><img src="{icon}" border="0" /></div><div class="jsEquipmentName">{text}</div></a></div>'
nm3kScrollBarContainer.prototype.ITEM_TEMPLATE = '<div index="{index}" class="imgSharden"><a href="javascript:;" class="jsImgOuter" ><div class="putSubEqImg"><img src="{icon}" border="0" /></div><div class="jsEquipmentName">{text}</div></a></div>'
nm3kScrollBarContainer.prototype.createItem = function(index){
    var item = this.data[index];
    	item.index = index+1;
    return String.format(this.ITEM_TEMPLATE, item);
}
nm3kScrollBarContainer.prototype.setTotal = function(sum){
    this.totalElement.text(sum);
}
nm3kScrollBarContainer.prototype.init = function(){
	var str = '';
	str += '<div class="nm3kScrollBarContainer">';
	str += '	<a href="javascript:;" class="nm3kScrollBarContainerArrleft" style="display:none"></a>';
	str += '	<a href="javascript:;" class="nm3kScrollBarContainerArrRight" style="display:none"></a>';
	str += '	<div class="nm3kScollBarMiddle" style="height:'+this.height+'px;">';
	str += '		<table class="nm3kScollBarMiddleTable" cellpadding="0" cellspacing="0" border="0" rules="none">';
	str += '			<tbody>';
	/*str += '				<tr>';
	for(var i=0; i<this.data.length; i++){
	str += '					<td class="nm3kScrollBarTd">';
	str += 						this.createItem(i);
	str += '					<td width="3"><div class="blankThree"></div></td>';
	str += '					</td>';
	}
	*/str += '				</tr>';
	str += '			</tbody>';
	str += '		</table>';
	str += '	</div>';
	str += '</div>';
	str += '<div id="nm3kScrollBarContainerPage"></div>'
	$("#"+this.renderTo).html(str);
	
	

	//点击右侧箭头，table向左移动;
	$("#"+ this.renderTo +" .nm3kScrollBarContainerArrRight").bind("mousedown",this,this.rightArrDown);//点击右侧arrow,通过setTimeout持续向左移动;
	$("#"+ this.renderTo +" .nm3kScrollBarContainerArrRight").bind("mouseup",this,this.stopMove);	
	$("#"+ this.renderTo +" .nm3kScrollBarContainerArrRight").bind("mouseleave",this,this.stopMove);
	
	//点击左侧箭头，table向左移动;
	$("#"+ this.renderTo +" .nm3kScrollBarContainerArrleft").bind("mousedown",this,this.leftArrDown);//点击右侧arrow,通过setTimeout持续向左移动;
	$("#"+ this.renderTo +" .nm3kScrollBarContainerArrleft").bind("mouseup",this,this.stopMove);	
	$("#"+ this.renderTo +" .nm3kScrollBarContainerArrleft").bind("mouseleave",this,this.stopMove);
	
	$( WIN ).bind("resize", this, this.autoWidth);
	$("#"+this.renderTo).find(".nm3kScrollBarContainer").bind("resize",this,this.autoWidth);
	this.correctWidth();
	
	//$("#"+this.renderTo + " .jsImgOuter").bind("mouseover",this,this.overSubName);//移动到a标签上，名字向上移动,显示完整的名字;
	
};

nm3kScrollBarContainer.prototype.addEventListeners = function(){
	var o = this;
	var imgItemHandler = function(evt){
		o.imgItemHandler(evt,o);
	}
	$(  ".nm3kScrollBarContainer" ).bind("click", imgItemHandler).bind("mouseover", imgItemHandler).bind("mouseout", imgItemHandler);
}

nm3kScrollBarContainer.prototype.imgItemHandler =  function(evt,_this){
	var $srcElement = evt.target;	
	while( true){
		if(!$srcElement){
			break;
		}
		if($srcElement.className && $srcElement.className == "imgSharden" ){
			if(evt.type == 'click'){
				this.selectedThisImg( $srcElement );
				var $index = $( $srcElement ).attr("index");				
				var $entity = this.data[ $index-1 ];
				this.select($entity , $index);
			}else if(evt.type == 'mouseover'){
				this.overSubName( $srcElement )
			}else if(evt.type == 'mouseout'){
				this.outSubName( $srcElement );
			}
			break;
		}else{
			$srcElement = $srcElement.parentNode;
		}
	}
}


nm3kScrollBarContainer.prototype.selectedThisImg = function( $element ){
	var $this = $( $element );
	var $tbody = $this.parent().parent().parent();
	$tbody.find(".jsImgOuter").removeClass("jsImgOuterHover");
	$this.find(".jsImgOuter").addClass("jsImgOuterHover");
	var indexNum = $this.attr("index");
	while(indexNum > this.pageNum){
		indexNum = indexNum - this.pageNum;
	}
	$("#nowSelelctInput").val(indexNum);
}



//移动到名字上面;
nm3kScrollBarContainer.prototype.overSubName = function( $element ){
	var $this = $( $element ).find(".jsImgOuter");
	var myH = $this.find(".jsEquipmentName").outerHeight();
	var outerH = $this.innerHeight();
	var h = outerH - myH;
	if(h<0){ h=0;}
	$this.find(".jsEquipmentName").stop().animate({top:h},"fast");
}

//从名字上面移开;
nm3kScrollBarContainer.prototype.outSubName = function( $element ){
	var $this = $( $element ).find(".jsImgOuter");
	$this.find(".jsEquipmentName").stop().animate({top:90},"fast");	
}

nm3kScrollBarContainer.prototype.correctWidth = function(){		
	//将箭头移动到合适的高度位置;		
	$("#"+this.renderTo+ " .nm3kScrollBarContainerArrleft ,#"+this.renderTo+" .nm3kScrollBarContainerArrRight").css("top",this.offsetY);
	//计算table的宽度和外侧的宽度;		
	var tableW = $("#"+this.renderTo).find(".nm3kScollBarMiddleTable").outerWidth();		
	var outerDivW = $("#"+ this.renderTo).outerWidth();
	if(tableW <= outerDivW){//不存在滚动条;
		$("#"+this.renderTo).find(".nm3kScrollBarContainerArrleft").css("display","none");
		$("#"+this.renderTo).find(".nm3kScrollBarContainerArrRight").css("display","none");
	}else{//存在滚动条;
		$("#"+this.renderTo).find(".nm3kScrollBarContainerArrleft").css("display","block");
		$("#"+this.renderTo).find(".nm3kScrollBarContainerArrRight").css("display","block");
		var arrW = $("#"+this.renderTo).find(".nm3kScrollBarContainerArrleft").outerWidth();
		var innerW = $("#"+this.renderTo).outerWidth() - arrW - arrW;
		$("#"+this.renderTo).find(".nm3kScollBarMiddle").width(innerW).css({left:arrW});
	}
};

nm3kScrollBarContainer.prototype.autoWidth = function(e){	
	if(!e) e = window.event;
	var _this = e.data;
	//计算出外部包含选项卡的div的宽度;
	var tabW = $("#"+ _this.renderTo).innerWidth();			
	//计算tabMiddle的宽度,table的宽度，进行比较;
			
	var tableW = $("#"+ _this.renderTo).find(".nm3kScollBarMiddleTable").outerWidth();
	var tabMiddleW = $("#"+ _this.renderTo).outerWidth();
	
	if(tableW > tabW){//要出现左右箭头的情况;
		//alert("要出现左右箭头的")
		//这里还要考虑left的情况;
		var arrW = $("#"+_this.renderTo).find(".nm3kScrollBarContainerArrleft").outerWidth();
		if(tabW < 2*arrW) tabW = 2*arrW;
		$("#"+ _this.renderTo +" .nm3kScrollBarContainerArrleft, #"+ _this.renderTo +" .nm3kScrollBarContainerArrRight").css("display","block");
		var innerW = $("#"+_this.renderTo).outerWidth() - arrW - arrW;
		
		tabMiddleW = tabW - arrW * 2;
		$("#"+ _this.renderTo +" .nm3kScollBarMiddle").width(tabMiddleW).css("left",arrW);
					
		var tableLeftPos = $("#"+ _this.renderTo +" .nm3kScollBarMiddleTable").position().left;
		if(tableLeftPos<0){
			tableLeftPos = -tableLeftPos;
		}
		var tableCanSeeW = $("#"+ _this.renderTo +" .nm3kScollBarMiddleTable").innerWidth() - tableLeftPos;				
		if(tableCanSeeW < tabMiddleW){
			var toRight = $("#"+ _this.renderTo +" .nm3kScollBarMiddleTable").innerWidth() - tabMiddleW;
			$("#"+ _this.renderTo +" .nm3kScollBarMiddleTable").css("left",-toRight);
		};//end if;
	}else{//不出现左右箭头的情况;
		//alert("不出现左右箭头的情况")
		$("#"+ _this.renderTo +" .nm3kScrollBarContainerArrleft, #"+ _this.renderTo +" .nm3kScrollBarContainerArrRight").css("display","none");
		$("#"+ _this.renderTo +" .nm3kScollBarMiddle").width(tabW).css("left",0);
		$("#"+ _this.renderTo +" .nm3kScollBarMiddleTable").css("left",0);
	};//end if else;
};//end autoWidth();

nm3kScrollBarContainer.prototype.rightArrDown = function(e){//点击右侧箭头;
	if(!e) e = window.event;
	var _this = e.data;
	_this.downAble = true;
	_this.movement = setTimeout(createDelegate(_this,_this.rightArrDown2),_this.speed);			
};//end rightArrDown;

nm3kScrollBarContainer.prototype.rightArrDown2 = function(e){
	if(this.movement){
		clearTimeout(this.movement);
	}
	if(this.downAble){
		var leftPos = $("#"+ this.renderTo +" .nm3kScollBarMiddleTable").position().left;
			var newLeftPos = leftPos- this.movePx;
			var tableW = $("#"+ this.renderTo +" .nm3kScollBarMiddleTable").innerWidth();
			var tableMiddleW = $("#"+ this.renderTo +" .nm3kScollBarMiddle").innerWidth();
			var minLeft = tableMiddleW - tableW;
			if(minLeft >= newLeftPos){
				newLeftPos = minLeft;
				$("#"+ this.renderTo +" .nm3kScollBarMiddleTable").css("left",newLeftPos);
				this.movement = null;
				return;
			}
			$("#"+ this.renderTo +" .nm3kScollBarMiddleTable").css("left",newLeftPos);
			this.movement = setTimeout(createDelegate(this,this.rightArrDown2),this.speed);
	}else{
		this.movement = null;
	};//end if;
};//end rightArrDown2;

nm3kScrollBarContainer.prototype.leftArrDown = function(e){//点击右侧箭头;
	if(!e) e = window.event;
	var _this = e.data;
	_this.downAble = true;
	_this.movement = setTimeout(createDelegate(_this,_this.leftArrDown2),_this.speed);			
};//end leftArrDown;

nm3kScrollBarContainer.prototype.leftArrDown2 = function(e){
	if(this.movement){
		clearTimeout(this.movement);
	}
	if(this.downAble){
		var leftPos = $("#"+ this.renderTo +" .nm3kScollBarMiddleTable").position().left;
		var newLeftPos = leftPos + this.movePx;
		if(newLeftPos >= 0){
			newLeftPos = 0;
			$("#"+ this.renderTo +" table").css("left",newLeftPos);
			movement = null;
			return;
		}
		$("#"+ this.renderTo +" .nm3kScollBarMiddleTable").css("left",newLeftPos);
		this.movement = setTimeout(createDelegate(this,this.leftArrDown2),this.speed);
	}else{
		this.movement = null;
	};//end if;
}		

nm3kScrollBarContainer.prototype.stopMove = function(e){//停止移动;
	if(!e) e = window.event;
	var _this = e.data;
	_this.downAble = false;
	if(_this.movement){
		clearTimeout(_this.movement);
	}
};//end stopMove;

//更新内部内容;
nm3kScrollBarContainer.prototype.setData = function(data){
	this.data = data;
	var $tbody = $("#"+this.renderTo).find("table.nm3kScollBarMiddleTable tbody");
	$tbody.empty();
	var str = '';
		str += '<tr>';
	if(this.data.length > this.pageNum){//大于200条数据，那么有分页;
		this.pages = Math.ceil(this.data.length / this.pageNum); 
		this.nowPage = 1;
		var strPage = '';
		for(var i=1; i<this.pages+1; i++){
			strPage += '<a href="javascript:;" class="nm3kScrollBarNum" >'+ i +'</a>';
		}
		$("#nm3kScrollBarContainerPage").html(strPage);//加入分页;
		$("#nm3kScrollBarContainerPage").find("a").bind("click",this,this.changePage);
		$("#nm3kScrollBarContainerPage a:eq(0)").addClass("nm3kScrollBarNumSelected");
		for(var i=0; i<this.pageNum; i++){
			str += '	<td class="nm3kScrollBarTd">';
			str += 		this.createItem(i);
			str += '    </td>';
			str += '	<td width="3"><div class="blankThree"></div></td>';
		}
	}else{//小于200条数据，那么没有分页;
		for(var i=0; i<this.data.length; i++){
			str += '	<td class="nm3kScrollBarTd">';
			str += 		this.createItem(i);
			str += '    </td>';
			str += '	<td width="3"><div class="blankThree"></div></td>';
		}
	}
		str += '</tr>';
	$tbody.html(str);
	this.correctWidth();
	//设置总数
	this.setTotal(this.data.length);//显示一共有多少个设备;
	//设置该页总数;
	if(this.data.length < this.pageNum){
		$("#nowPageNums").text(this.data.length);
	}
	$("#nowSelelctInput").val(this.data.length > 0 ? 1 :0 )
	//默认选中第一个
	$("#"+ this.renderTo).find(".jsImgOuter").eq(0).addClass("jsImgOuterHover");
}
nm3kScrollBarContainer.prototype.changePage = function(e){
	e.data.nowPage = $(this).text();
	$("#nm3kScrollBarContainerPage a").removeClass("nm3kScrollBarNumSelected");
	$(this).addClass("nm3kScrollBarNumSelected")
	$("#nowSelelctInput").val("1");
	var dataArr = e.data.data;
	var newDataArr;
	if(e.data.nowPage == e.data.pages){//如果点击的是最后一页;
		var first = (e.data.nowPage-1) * e.data.pageNum;
		newDataArr = dataArr.slice(first);
		$("#nowPageNums").text(newDataArr.length);
	}else{//点击的不是最后一页;
		$("#nowPageNums").text(e.data.pageNum);
		var first = (e.data.nowPage-1) * e.data.pageNum;
		var last = first + e.data.pageNum;
		//alert(first +"::"+ last)
		newDataArr = dataArr.slice(first,last);
	}
	//alert("newDataArr.length:"+newDataArr.length)
	e.data.setPageData(newDataArr,(e.data.nowPage-1)*e.data.pageNum,e);
}
//点击分页，更新内部内容;
nm3kScrollBarContainer.prototype.setPageData = function(newDataArr,page,e){
	var $tbody = $("#"+this.renderTo).find("table.nm3kScollBarMiddleTable tbody");
	$tbody.empty();
	var str = '';
		str += '<tr>';
		for(var i=0; i<newDataArr.length; i++){
			str += '	<td class="nm3kScrollBarTd">';
			str += 		this.createItem(i + page);
			str += '    </td>';
			str += '	<td width="3"><div class="blankThree"></div></td>';
		}
		str += '</tr>';
	$tbody.html(str);
	this.correctWidth();
	//默认选中第一个
	var _this = e.data;
	var $entity = _this.data[ page ];
	_this.select($entity, 0  );
	$("#"+ this.renderTo).find(".jsImgOuter").eq(0).addClass("jsImgOuterHover");
	$("#"+this.renderTo).find("table.nm3kScollBarMiddleTable").css("left",0);
}
nm3kScrollBarContainer.prototype.selectFirstItem = function(){
    this.selectItem(1);
}

//选中某一个，如果有滚动条，则滚动到最左侧，长度不够的情况就滚动到显示最后一个;
nm3kScrollBarContainer.prototype.selectItem = function(num){
    num -= 1;
    $("#"+ this.renderTo +" .nm3kScrollBarTd").find(".jsImgOuter").removeClass("jsImgOuterHover");
    $("#"+ this.renderTo +" .nm3kScrollBarTd").find(".jsImgOuter").eq(num).addClass("jsImgOuterHover");    
    if($("#"+ this.renderTo +" a.nm3kScrollBarContainerArrleft").css("display") == "none"){return};//没有出现箭头，那么不许滚动;
    var l = $("#"+ this.renderTo +" .nm3kScrollBarTd").eq(num).position().left;   
    var tableW = $("#"+ this.renderTo +" .nm3kScollBarMiddleTable").outerWidth();
    var restW = tableW - l;//从个数num到末尾的宽度;
    var visibleW = $("#"+ this.renderTo +" .nm3kScollBarMiddle").outerWidth();
            
    if(restW > visibleW){
        $("#"+ this.renderTo +" .nm3kScollBarMiddleTable").animate({left:-l},"fast");
    }else{//移动到显示出最后一个在最右侧;
        var l2 = tableW - visibleW;
        $("#"+ this.renderTo +" .nm3kScollBarMiddleTable").animate({left:-l2},"fast");
    }
}

//修改某一个的文字,num从0开始;
nm3kScrollBarContainer.prototype.setTxt = function(obj){	
	this.txt = obj.txt;
	this.num = obj.num;
	$("#"+this.renderTo).find(".jsEquipmentName").eq(this.num).text(this.txt);
}
//修改选中项的文字;
nm3kScrollBarContainer.prototype.modifySelectedTxt = function(txt){
	$("#"+this.renderTo).find(".jsImgOuterHover .jsEquipmentName").text(txt);
}

function createDelegate(object, method) {
	var delegate = function(){
		method.call(object);
	}
    return delegate;
};//end createDelegate;