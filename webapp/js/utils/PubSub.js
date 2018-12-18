var PubSub = {};

(function(ps) {
	// 订阅的主题
	var topics = [];
	var uuid = -1;
	
	/**
	 * 订阅主题
	 * @param {String} topic 主题名称
	 * @param {Function} callback 主题发布时的想要触发调用的方法
	 */
	ps.subscribe = function(topic, callback) {
		if(!topics[topic]) {
			topics[topic] = [];
		}
		
		var id = (++uuid).toString();
		topics[topic].push({
			id: id,
			callback: callback
		});
		
		return id;
	};
	
	/**
	 * 退订主题
	 * @param {String} topic 主题名称
	 * @param {String} id 想退订主题方法的id
	 */
	ps.unsubscribe = function(topic, id) {
		var topic = topics[topic];
		if(!topic) {
			return;
		}
		
		for (var i = 0, len = topic.length; i < len; i++) {
			if (topic[i].id === id) {
				topic.splice(i, 1);
                return id;
            }
		}
	};
	
	/**
	 * 发布主题事件
	 * @param {String} topic 主题名称
	 * @param {Object} data 参数
	 */
	ps.publish = function(topic, data) {
		if (!topics[topic]) {
            return false;
        }

        setTimeout(function () {
            var subscribers = topics[topic],
                len = subscribers ? subscribers.length : 0;

            while (len--) {
            	try{
            		subscribers[len].callback(data);
            	} catch(e) {
            		console && console.log(e);
            	}
            }
        }, 0);
	};
	
	ps.on = ps.subscribe;
	ps.off = ps.unsubscribe;
	ps.emit = ps.publish;
})(PubSub);