var NM3KVersionControl = {
	controls : {},
	/**缓存type和version的映射可能会导致前后端不一致，所以设备软件升级后需要发送广播消息通知各前段清空该映射.升级后刷新系统也能解决该问题*/
	entityMapper : {},
	/**
	 * 获取指定设备类型、指定设备版本的版本控制信息，有缓存即用缓存
	 */
	getControls: function(typeId, version){
		if(!typeId  || !version || typeId=="null" || version=="null"){
			return;
		}
		var $controls = this.controls;
		var $typeversion = this.getTypeVersionKey(typeId, version);
		if($controls[ $typeversion ] == null){
			this.loadControls(typeId, version);
		}
		return $controls[ $typeversion ];
	},
	
	/**
	 * 获取指定设备类型、指定设备版本的版本控制信息，直接获取
	 */
	loadControls: function(typeId, version) {
		var $controls = this.controls;
		var $typeversion = this.getTypeVersionKey(typeId, version);
		$.ajax({
			url:"/versioncontrol/loadVersionControl.tv",async:false,dataType:'json',
			data:{typeId:typeId,version:version},
			success:function(data){
				$controls[ $typeversion ] = data;
			}
		});
	},
	
	/**
	 * 获取entityId对应设备的设备类型和版本信息
	 */
	getTypeAndVersion: function(entityId){
		if(!entityId){
			return;
		}
		var $o = this;
		var typeAndVersion = $o.entityMapper[entityId];
		if(!typeAndVersion){
			var $entityMapper = this.entityMapper;
			$.ajax({
				url:"/versioncontrol/loadTypeAndVersion.tv",async:false,dataType:'json',
				data:{entityId: entityId},
				success:function(data){
					typeAndVersion = $o.entityMapper[ entityId ] = data;
				}
			});
		}
		return typeAndVersion;
	},
	
	getTypeVersionKey: function(typeId, version) {
		return "$$"+typeId+"$$"+version;
	}
}

/**
 * 对外暴露的工具类
 */
var VersionControl = {
	/**
	 * 提供设备entityId，判断指定功能支持情况，返回数据结构为{disabled}
	 */
	supportNode : function($id, $entityId){
		var $disabled = false, 
			$hidden = false;
		if(V.isTrue($entityId)){
			//兼容 mainwithmenu.jsp等界面
			var $NM3KVersionControl = window.top.NM3KVersionControl;
			if($NM3KVersionControl){
				var $typeAndVersion = $NM3KVersionControl.getTypeAndVersion($entityId);
				$version = $typeAndVersion.version;
				$typeId = $typeAndVersion.typeId;
				var $controls = $NM3KVersionControl.getControls($typeId,$version);
				if($controls){
					var $node = $controls[ $id ];
					if( $node ){
						var $status = $node.status;
						if( $status == "hidden"){
							$hidden = true;
						}else if($status == "disabled"){
							$disabled = true;
						}
					}
				}
			}
		}
		return { disabled: $disabled, hidden : $hidden};
	},
	
	/**
	 * 提供设备类型、版本信息、判断是否支持指定功能
	 */
	support : function( $id, $version, $typeId){
		var $NM3KVersionControl = window.top.NM3KVersionControl;
		if(typeof $version == 'number' || ($version !=null && !$version.contains("\\."))){
			var $entityId = $version;
			var $typeAndVersion = $NM3KVersionControl.getTypeAndVersion($entityId);
			$version = $typeAndVersion.version;
			$typeId = $typeAndVersion.typeId;
		}
		if( $NM3KVersionControl ){
			if( $version ){
				var vcontrols = $NM3KVersionControl.getControls($typeId, $version);
				if(vcontrols){
					var $control = vcontrols[ $id ];
					var $isSupport = true;
					if( $control ){
						var $status = $control.status;
						if( $status == "hidden"){
							$isSupport = false;
						}else if($status == "disabled"){
							$isSupport = false;
						}
					}
					return $isSupport;
				}
			}
		}
		return false;
	},
	
	supportDependsOnSub: function($id, func) {
		var $support = false;
		$.ajax({
			url:"/versioncontrol/loadSupportDependsOnSub.tv",async:false,dataType:'json',
			data:{entityId: $id, functionName: func},
			success:function(result){
				$support = result.support;
			}
		});
		return $support;
	}
}
$(function(){ 
	window.top.addCallback("@{VersionControl.SYNC_VERSION_CALLBACK}@",function(entityId){
		delete NM3KVersionControl.entityMapper[entityId];
	})
});