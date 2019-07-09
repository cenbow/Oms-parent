Ext.define('MB.model.ProductBarcodeList', {
	extend: 'Ext.data.Model',
	fields: [
			{ name: 'goodsSn', type: 'string'},
			{ name: 'colorCode', type: 'string' },
			{ name: 'colorName', type: 'string' },
			{ name: 'sizeCode', type: 'string' },
			{ name: 'sizeName', type: 'string' },
			{ name: 'custumCode', type: 'string' },
			{ name: 'barcode', type: 'string' },
			{ name : 'sizeChild' },
			{ name: 'currSizeCode', type: 'string' },
			{ name : 'colorChild' },
			{ name: 'currColorCode', type: 'string' },
			{ name : 'useCard'}
	]
});		