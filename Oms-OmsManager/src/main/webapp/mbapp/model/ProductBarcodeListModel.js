Ext.define('MB.model.ProductBarcodeListModel', {
	extend: 'Ext.data.Model',
	fields: [
	        { name: 'id'},
			{ name: 'goodsSn'},
			{ name: 'colorCode'},
			{ name: 'colorName'},
			{ name: 'sizeCode'},
			{ name: 'sizeName'},
			{ name: 'skuSn'},
			{ name: 'custumCode'},
			{ name: 'barcode'},
			{ name: 'specPrice'},
			{ name: 'saleCount'},
			{ name: 'colorSeries'},
			{ name: 'sellerCode'},
			{ name: 'businessBarcode'}
	]
});