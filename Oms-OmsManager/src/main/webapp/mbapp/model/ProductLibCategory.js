Ext.define('MB.model.ProductLibCategory', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'catId'},
        { name: 'catCode', type: 'string' },
        { name: 'catName', type: 'string' }
    ]
});