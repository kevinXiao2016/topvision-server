Topvision.RegionCombo = Ext.extend(Ext.form.ComboBoxTree,{
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        Topvision.RegionCombo.superclass.constructor.call(this, {
        	triggerAction : 'all',
        	hiddenField: 'folderId',
			tree: new Ext.tree.TreePanel({
                root: new Ext.tree.AsyncTreeNode({}),
                rootVisible: false,
                border: false,
                dataUrl: '/system/loadAllRegion.tv',
                listeners: {
                    beforeload: function (n) { if (n) { this.getLoader().baseParams.id = n.attributes.id; } }
                }
            })
        });
        if(typeof this.handler == 'function'){
        	this.on("keyup",this.handler);
        	this.on("select",this.handler);
        }
    }
});

