

var questionDataStoreList = null;

var questionDataUrl = basePath + "/custom/define/customDefineList.spmvc";
createAjaxDataBySyn("questionData", questionDataUrl, doSuccessfun, doFailurefun, {"type":3,"display":1}, 100000);

function doSuccessfun(id, response, opts) {
	if("questionData"==id){
		var respText = Ext.JSON.decode(response.responseText);
		var data = respText.data;
		var arr = [];
		for(var i=0; i<data.length; i++){
			var o = {};
			o.code = data[i].code;
			o.name = data[i].name;
			arr.push(o);
		}
		
		questionDataStoreList =arr;
		
	} else if("logisType"== id){
		
		var respText = Ext.JSON.decode(response.responseText);
		var data = respText.data;
		
		if(respText.success == "true"){
	
			if (data.batchNormalQuestion == 1) {
				selectQType.push({v: '0', n: '一般问题单'});
			}
			if (data.batchTaskOutStock == 1 || data.batchTasklogstcQust == 1){
				selectQType.push({v: '1', n: '物流问题单'});
			}
			
			if (data.batchTasklogstcQust == 1){
				mylogistics_question_type.push({code:"37" ,name:"短拣"});
			}
			if (data.batchTaskOutStock == 1) {
				mylogistics_question_type.push({code:"38" ,name:"短配"});
			}
			
		}
		
		
	}else{
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){
			questionDataStore.load();
		}else{
			errorMsg("结果", respText.msg);
		}
	}

}

function doFailurefun(id,response,opts) {
	var respText = Ext.JSON.decode(response.responseText);
	errorMsg("结果", respText.msg);
}

//问题单原因
Ext.define("MB.store.QuestionStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderCustomDefine",
	data: questionDataStoreList
});

