/*
 * @author niejun
 */
var closeManyTabInformed = true;
Ext.ux.TabCloseMenu = function(informed){
	closeTabText = Extux.TabCloseMenu.closeTabText;
	closeOtherTabText = Extux.TabCloseMenu.closeOtherTabText;
	closeAllTabText = Extux.TabCloseMenu.closeAllTabText;
	confirmText = Extux.TabCloseMenu.confirmText;
	closeAllMsg = Extux.TabCloseMenu.closeAllMsg;
	closeOtherMsg = Extux.TabCloseMenu.closeOtherMsg;

    var tabs, menu, ctxItem;
    this.init = function(tp){
        tabs = tp;
        tabs.on('contextmenu', onContextMenu);
    }

    function onContextMenu(ts, item, e){
        if(!menu){ // create context menu on first right click
            menu = new Ext.menu.Menu([{
                id: tabs.id + '-close',
                text: closeTabText,
                handler : function(){
                    tabs.remove(ctxItem);
                }
            },{
                id: tabs.id + '-close-others',
                text: closeOtherTabText,
                handler : function(){
					if (closeManyTabInformed && tabs.items.length > 2) {
						Ext.MessageBox.confirm(confirmText, closeOtherMsg, function(type) {
							if (type == 'yes') {
								tabs.items.each(function(item){
									if(item.closable && item != ctxItem){
										tabs.remove(item);
									}
								});
							}
						});
					} else {
						tabs.items.each(function(item){
							if(item.closable && item != ctxItem){
								tabs.remove(item);
							}
						});
					}
                }
            },{
                id: tabs.id + '-close-all',
                text: closeAllTabText,
                handler : function(){
					if (closeManyTabInformed && tabs.items.length > 1) {
						Ext.MessageBox.confirm(confirmText, closeAllMsg, function(type) {
							if (type == 'yes') {
								tabs.items.each(function(item){
									if(item.closable){
										tabs.remove(item);
									}
								});
							}
						});
					} else {
						tabs.items.each(function(item){
							if(item.closable){
								tabs.remove(item);
							}
						});
					}
                }
            }]);
        }
        ctxItem = item;
        var items = menu.items;
        items.get(tabs.id + '-close').setDisabled(!item.closable);
        var disableOthers = true;
        tabs.items.each(function(){
            if(this != item && this.closable){
                disableOthers = false;
                return false;
            }
        });
        items.get(tabs.id + '-close-others').setDisabled(disableOthers);
		items.get(tabs.id + '-close-all').setDisabled(disableOthers);
        menu.showAt(e.getPoint());
    }
};

Ext.Messanger = function(){
    var msgCt;
	var mObj;
    function createBox(t, s){
        return ['<div class="messanger">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><table cellspacing=0 cellpadding=0><tr><td style="font-weight:bold; font-size:10pt">', t,
				'</td><td align=right><img src="images/close.gif" style="cursor:hand" qtip="¹Ø±Õ" border=0 align=absmiddle onmouseover="this.src=\'images/close_h.gif\'" onmouseout="this.src=\'images/close.gif\'" onclick="alert()"></td></tr><tr><td colspan=2 style="padding-top:10px">',
				s, '</td></tr></table></div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return {
        msg : function(title, format, duration, position, offset){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'messanger-div'}, true);
            }
			msgCt.alignTo(document, 't', [0, 0]);
            var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
			var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
			m.slideIn('t').pause(duration ? duration : 30).ghost("t", {remove:true});
        },

		dispose: function() {
			if(mObj){
				mObj.ghost("t", {remove:true});
			}
			mObj = null;
		}
    };
}();

Ext.tree.ColumnTree = Ext.extend(Ext.tree.TreePanel, {
    lines:false,
    borderWidth: Ext.isBorderBox ? 0 : 2, // the combined left/right border for each cell
    cls:'x-column-tree',
    
    onRender : function(){
        Ext.tree.ColumnTree.superclass.onRender.apply(this, arguments);
        this.headers = this.body.createChild(
            {cls:'x-tree-headers'},this.innerCt.dom);

        var cols = this.columns, c;
        var totalWidth = 0;

        for(var i = 0, len = cols.length; i < len; i++){
             c = cols[i];
             totalWidth += c.width;
             this.headers.createChild({
                 cls:'x-tree-hd ' + (c.cls?c.cls+'-hd':''),
                 cn: {
                     cls:'x-tree-hd-text',
                     html: c.header
                 },
                 style:'width:'+(c.width-this.borderWidth)+'px;'
             });
        }
        this.headers.createChild({cls:'x-clear'});
        // prevent floats from wrapping when clipped
        this.headers.setWidth(totalWidth);
        this.innerCt.setWidth(totalWidth);
    }
});

Ext.tree.ColumnNodeUI = Ext.extend(Ext.tree.TreeNodeUI, {
    focus: Ext.emptyFn, // prevent odd scrolling behavior

    renderElements : function(n, a, targetNode, bulkRender){
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';

        var t = n.getOwnerTree();
        var cols = t.columns;
        var bw = t.borderWidth;
        var c = cols[0];

        var buf = [
             '<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf ', a.cls,'">',
                '<div class="x-tree-col" style="width:',c.width-bw,'px;">',
                    '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
                    '<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow">',
                    '<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on">',
                    '<a hidefocus="on" class="x-tree-node-anchor" href="',a.href ? a.href : "#",'" tabIndex="1" ',
                    a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '>',
                    '<span unselectable="on">', n.text || (c.renderer ? c.renderer(a[c.dataIndex], n, a) : a[c.dataIndex]),"</span></a>",
                "</div>"];
         for(var i = 1, len = cols.length; i < len; i++){
             c = cols[i];

             buf.push('<div class="x-tree-col ',(c.cls?c.cls:''),'" style="width:',c.width-bw,'px;">',
                        '<div class="x-tree-col-text">',(c.renderer ? c.renderer(a[c.dataIndex], n, a) : a[c.dataIndex]),"</div>",
                      "</div>");
         }
         buf.push(
            '<div class="x-clear"></div></div>',
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
            "</li>");

        if(bulkRender !== true && n.nextSibling && n.nextSibling.ui.getEl()){
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin",
                                n.nextSibling.ui.getEl(), buf.join(""));
        }else{
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf.join(""));
        }

        this.elNode = this.wrap.childNodes[0];
        this.ctNode = this.wrap.childNodes[1];
        var cs = this.elNode.firstChild.childNodes;
        this.indentNode = cs[0];
        this.ecNode = cs[1];
        this.iconNode = cs[2];
        this.anchor = cs[3];
        this.textNode = cs[3].firstChild;
    }
});


/*
 * Ext JS Library 2.2.1
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.History
 * @extends Ext.util.Observable
 * History management component that allows you to register arbitrary tokens that signify application
 * history state on navigation actions.  You can then handle the history {@link #change} event in order
 * to reset your application UI to the appropriate state when the user navigates forward or backward through
 * the browser history stack.
 * @singleton
 */
Ext.History = (function () {
    var iframe, hiddenField;
    var ready = false;
    var currentToken;

    function getHash() {
        var href = top.location.href, i = href.indexOf("#");
        return i >= 0 ? href.substr(i + 1) : null;
    }

    function doSave() {
        hiddenField.value = currentToken;
    }

    function handleStateChange(token) {
        currentToken = token;
        Ext.History.fireEvent('change', token);
    }

    function updateIFrame (token) {
        var html = ['<html><body><div id="state">',token,'</div></body></html>'].join('');
        try {
            var doc = iframe.contentWindow.document;
            doc.open();
            doc.write(html);
            doc.close();
            return true;
        } catch (e) {
            return false;
        }
    }

    function checkIFrame() {
        if (!iframe.contentWindow || !iframe.contentWindow.document) {
            setTimeout(checkIFrame, 10);
            return;
        }

        var doc = iframe.contentWindow.document;
        var elem = doc.getElementById("state");
        var token = elem ? elem.innerText : null;

        var hash = getHash();

        setInterval(function () {

            doc = iframe.contentWindow.document;
            elem = doc.getElementById("state");

            var newtoken = elem ? elem.innerText : null;

            var newHash = getHash();

            if (newtoken !== token) {
                token = newtoken;
                handleStateChange(token);
                top.location.hash = token;
                hash = token;
                doSave();
            } else if (newHash !== hash) {
                hash = newHash;
                updateIFrame(newHash);
            }

        }, 50);

        ready = true;

        Ext.History.fireEvent('ready', Ext.History);
    }

    function startUp() {
        currentToken = hiddenField.value ? hiddenField.value : getHash();
        
        if (Ext.isIE) {
            checkIFrame();
        } else {
            var hash = getHash();
            setInterval(function () {
                var newHash = getHash();
                if (newHash !== hash) {
                    hash = newHash;
                    handleStateChange(hash);
                    doSave();
                }
            }, 50);
            ready = true;
            Ext.History.fireEvent('ready', Ext.History);
        }
    }

    return {
        /**
         * The id of the hidden field required for storing the current history token.
         * @type String
         * @property
         */
        fieldId: 'x-history-field',
        /**
         * The id of the iframe required by IE to manage the history stack.
         * @type String
         * @property
         */
        iframeId: 'x-history-frame',
        
        events:{},

        /**
         * Initialize the global History instance.
         * @param {Boolean} onReady (optional) A callback function that will be called once the history
         * component is fully initialized.
         * @param {Object} scope (optional) The callback scope
         */
        init: function (onReady, scope) {
            if(ready) {
                Ext.callback(onReady, scope, [this]);
                return;
            }
            if(!Ext.isReady){
                Ext.onReady(function(){
                    Ext.History.init(onReady, scope);
                });
                return;
            }
            hiddenField = Ext.getDom(Ext.History.fieldId);
			if (Ext.isIE) {
                iframe = Ext.getDom(Ext.History.iframeId);
            }
            this.addEvents('ready', 'change');
            if(onReady){
                this.on('ready', onReady, scope, {single:true});
            }
            startUp();
        },

        /**
         * Add a new token to the history stack. This can be any arbitrary value, although it would
         * commonly be the concatenation of a component id and another id marking the specifc history
         * state of that component.  Example usage:
         * <pre><code>
// Handle tab changes on a TabPanel
tabPanel.on('tabchange', function(tabPanel, tab){
    Ext.History.add(tabPanel.id + ':' + tab.id);
});
</code></pre>
         * @param {String} token The value that defines a particular application-specific history state
         * @param {Boolean} preventDuplicates When true, if the passed token matches the current token
         * it will not save a new history step. Set to false if the same state can be saved more than once
         * at the same history stack location (defaults to true).
         */
        add: function (token, preventDup) {
            if(preventDup !== false){
                if(this.getToken() == token){
                    return true;
                }
            }
            if (Ext.isIE) {
                return updateIFrame(token);
            } else {
                top.location.hash = token;
                return true;
            }
        },

        /**
         * Programmatically steps back one step in browser history (equivalent to the user pressing the Back button).
         */
        back: function(){
            history.go(-1);
        },

        /**
         * Programmatically steps forward one step in browser history (equivalent to the user pressing the Forward button).
         */
        forward: function(){
            history.go(1);
        },

        /**
         * Retrieves the currently-active history token.
         * @return {String} The token
         */
        getToken: function() {
            return ready ? currentToken : getHash();
        }
    };
})();
Ext.apply(Ext.History, new Ext.util.Observable());