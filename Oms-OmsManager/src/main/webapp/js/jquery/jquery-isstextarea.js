//一:
/*
 * TextLimit - jQuery plugin for counting and limiting characters for input and textarea fields
 * 
 * Example: jQuery("Textarea").textlimit('span.counter',256)
 *
 * $Version: 2011.08.08 +r3
 * edit by lhj
 * 显示文本域字符数。
*/

var faceObj = null;
var tempFaceObj = faceObj;
function filterFace(str){
	var newstr = str;
	if(tempFaceObj == null) {
		$.ajax({
		   type: "POST",
		   dataType: "json",
		   url: "ccomment/getFaces.ct",
		   data: "",
		   success: function(json){
		     tempFaceObj = json;
		   }
		 });
	}
	
	if(tempFaceObj == null) return str;
	
	$.each(tempFaceObj, function(index, valueObj) { 
				//先替换[]为\\[\\]
				var reg1 = new RegExp("(\\[)", "g"); 
				var temp =  valueObj.code.replace(reg1, "\\[");
				var reg1 = new RegExp("(\\])", "g"); 
				var temp = temp.replace(reg1, "\\]");
	
				var reg = new RegExp("(" + temp + ")", "g"); 
				newstr = newstr.replace(reg, "");
		});
		
	return newstr;
}


(function($) {
	$.fn.extend({
		textlimit:function(counter_el, thelimit){
			var that = this[0];
			var isCtrl = false; 
			var position = 0;
			updateCounter();
			
			//显示剩余的字符数
			function updateCounter(){
				if(typeof that == "object")	
					var length = thelimit - filterFace(that.value).length;
				    if (length < 0)
						length = 0;
					$(counter_el).text("\u60a8\u8fd8\u53ef\u4ee5\u8f93\u5165"+length+"\u4e2a\u5b57\u7b26\u3002");
			};

			//拖拽不起作用
			this.bind("dragenter", function() {	
				return false;
					
			});
			
			//拖拽不起作用
			this.bind("dragover",function(){
				return false;
			});
			
			//鼠标按下事件，记录光标的位置
			this.bind("mousedown",function(){
				 position =$.common.position(this);
			});

			
			//改变文本时显示剩余字符
			this.bind("change",function(){
				 updateCounter();
			});
			
			//粘贴事件，判断当前长度决定全部粘贴、部分粘贴或是取得此动作
			this.bind("paste", function() {	
					if(filterFace($(this).val()).length >= thelimit)
					 return false;
					/*if($(this).val().length >= thelimit)
						return false;

					var string = clipboardData.getData('text');
					
					var startstr = $(this).val().substr(0,position);
					var endstr = $(this).val().substr(position,$(this).val().length);
					var s = startstr + string + endstr;
					
					if(filterFace(s.length) > thelimit)					
						$(this).val(s.substr(0,thelimit));						
					else
						$(this).val(s);
					updateCounter();
					return false;*/
					
				});

			//键盘按下事件
			this.keydown (function(e){ 
					if(e.which == 17) isCtrl = true;
					var ctrl_a = (e.which == 65 && isCtrl == true) ? true : false; // CTRL + A
					var ctrl_v = (e.which == 86 && isCtrl == true) ? true : false; // CTRL + V
					var ctrl_c = (e.which == 67 && isCtrl == true) ? true : false; // CTRL + C
					var ctrl_x = (e.which == 88 && isCtrl == true) ? true : false; // CTRL + X

					var select = document.selection.createRange().text == "" ? false : true; //是否有选中的内容
					
				if( filterFace(this.value).length >= thelimit && e.which != '8' && e.which != '37' && e.which != '38' && e.which != '39' && e.which != '40' && e.which != '46' && ctrl_a == false && ctrl_v == false && ctrl_c == false && ctrl_x == false && select == false)
					e.preventDefault();
			})
			.keyup (function(e){//键盘弹起事件
			    isCtrl = false;
				updateCounter();
				if(filterFace(this.value).length > thelimit){
					//this.value = that.value.substr(0,thelimit);
				}
			});			
		}
}); 
$.common = 
{
	//获取光标的位置和设置光标的位置
	position:function(obj,value){   
        var elem = obj;   
            if (elem&&(elem.tagName=="TEXTAREA"||elem.type.toLowerCase()=="text")) {   
               if($.browser.msie){   
                       var rng;   
                       if(elem.tagName == "TEXTAREA"){    
                            rng = event.srcElement.createTextRange();   
                            rng.moveToPoint(event.x,event.y);   
                       }else{    
                            rng = document.selection.createRange();   
                       }   
                       if( value === undefined ){   
                         rng.moveStart("character",-event.srcElement.value.length);   
                         return  rng.text.length;   
                       }else if(typeof value === "number" ){   
                         var index=this.position();   
                         index>value?( rng.moveEnd("character",value-index)):(rng.moveStart("character",value-index))   
                         rng.select();   
                       }   
                }else{   
                    if( value === undefined ){   
                         return elem.selectionStart;   
                       }else if(typeof value === "number" ){   
                         elem.selectionEnd = value;   
                         elem.selectionStart = value;   
                       }   
                }   
            }else{   
                if( value === undefined )   
                   return undefined;   
            }   
    }   
}

})(jQuery);



//二:
//jquery插件： 在textarea光标处插入文本等。
jQuery.extend( {
	/** 
	 * 清除当前选择内容 
	 */
	unselectContents : function() {
		if (window.getSelection)
			window.getSelection().removeAllRanges();
		else if (document.selection)
			document.selection.empty();
	}
});
jQuery.fn
		.extend( {
			/** 
			 * 选中内容 
			 */
			selectContents : function() {
				$(this)
						.each(
								function(i) {
									var node = this;
									var selection, range, doc, win;
									if ((doc = node.ownerDocument)
											&& (win = doc.defaultView)
											&& typeof win.getSelection != 'undefined'
											&& typeof doc.createRange != 'undefined'
											&& (selection = window
													.getSelection())
											&& typeof selection.removeAllRanges != 'undefined') {
										range = doc.createRange();
										range.selectNode(node);
										if (i == 0) {
											selection.removeAllRanges();
										}
										selection.addRange(range);
									} else if (document.body
											&& typeof document.body.createTextRange != 'undefined'
											&& (range = document.body
													.createTextRange())) {
										range.moveToElementText(node);
										range.select();
									}
								});
			},
			/** 
			 * 初始化对象以支持光标处插入内容 
			 */
			setCaret : function() {
				if (!$.browser.msie)
					return;
				var initSetCaret = function() {
					var textObj = $(this).get(0);
					textObj.caretPos = document.selection.createRange()
							.duplicate();
				};
				$(this).click(initSetCaret).select(initSetCaret).keyup(
						initSetCaret);
			},
			/** 
			 * 在当前对象光标处插入指定的内容 
			 */
			insertAtCaret : function(textFeildValue) {
				var textObj = $(this).get(0);
				if (document.all && textObj.createTextRange && textObj.caretPos) {
					var caretPos = textObj.caretPos;
					caretPos.text = caretPos.text
							.charAt(caretPos.text.length - 1) == '' ? textFeildValue + ''
							: textFeildValue;
				} else if (textObj.setSelectionRange) {
					var rangeStart = textObj.selectionStart;
					var rangeEnd = textObj.selectionEnd;
					var tempStr1 = textObj.value.substring(0, rangeStart);
					var tempStr2 = textObj.value.substring(rangeEnd);
					textObj.value = tempStr1 + textFeildValue + tempStr2;
					textObj.focus();
					var len = textFeildValue.length;
					textObj.setSelectionRange(rangeStart + len, rangeStart
							+ len);
					textObj.blur();
				} else {
					textObj.value += textFeildValue;
				}
			}
		});
		
		/** eg:
		<div><span id="item">TestText</span></div> 
		<div><input id="hello" type="text" style="width: 200px;" /></div> 
		<div><input type="button" value="Inert on input" id="insertA" /></div> 
		<div><textarea id="world" style="width: 200px;height:50px;"></textarea> 
		<div><input type="button" value="Inert on Textarea" id="insertT" /></div> 

		(function($){ 
			$('#hello').setCaret(); 
			$('#insertA').click(function(){ 
				$('#hello').insertAtCaret($('#item').text()); 
			}); 
			
			$('#world').setCaret(); 
			$('#insertT').click(function(){ 
				$('#world').insertAtCaret($('#item').text()); 
			}); 
			
			$('#item').hover( 
				function(){ 
					$(this).selectContents(); 
				}, 
				function(){ 
				$.unselectContents(); 
				}
			); 
		})($); 

        */