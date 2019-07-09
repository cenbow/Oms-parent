Ext.define('MB.store.CountryStore', {
    extend: 'Ext.data.Store',
    requires: ['Ext.data.reader.Json'],
    model: 'MB.model.SystemRegionArea',
    proxy: {
  
    	
		type: 'ajax',
		url: basePath + "custom/orderInfo/getArea?type=0",
		reader: {
			type:'json'
		}
    }
});


/*var countryStore = new Ext.data.Store({
	model:"MB.Area",
	proxy:{
		type: 'ajax',
		url: getAreaUrl + "?type=0",
		reader: {
			type:'json'
		}
	}
});*/