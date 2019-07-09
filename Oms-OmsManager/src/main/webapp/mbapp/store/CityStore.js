/**
 *城市 
 ****/
Ext.define('MB.store.CityStore', {
    extend: 'Ext.data.Store',
    requires: ['Ext.data.reader.Json'],
    model: 'MB.model.SystemRegionArea',
    proxy: {

		type: 'ajax',
		url: "",
		reader: {
			type:'json'
		}
    }
});
