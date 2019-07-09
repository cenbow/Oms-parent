/*
 * TextLimit - jQuery plugin for counting and limiting characters for input and textarea fields
 * 
 * Example: jQuery("Textarea").textlimit('span.counter',256)
 *
 * $Version: 2011.08.08 +r3
 * edit by lhj 原版无修改
 * 显示文本域字符数。
*/
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
					var length = thelimit - that.value.length;
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
					if($(this).val().length >= thelimit)
						return false;

					var string = clipboardData.getData('text');
					
					var startstr = $(this).val().substr(0,position);
					var endstr = $(this).val().substr(position,$(this).val().length);
					var s = startstr + string + endstr;
					
					if(s.length > thelimit)					
						$(this).val(s.substr(0,thelimit));						
					else
						$(this).val(s);
					updateCounter();
					return false;
					
				});

			//键盘按下事件
			this.keydown (function(e){ 
					if(e.which == 17) isCtrl = true;
					var ctrl_a = (e.which == 65 && isCtrl == true) ? true : false; // CTRL + A
					var ctrl_v = (e.which == 86 && isCtrl == true) ? true : false; // CTRL + V
					var ctrl_c = (e.which == 67 && isCtrl == true) ? true : false; // CTRL + C
					var ctrl_x = (e.which == 88 && isCtrl == true) ? true : false; // CTRL + X

					var select = document.selection.createRange().text == "" ? false : true; //是否有选中的内容
					
				if( this.value.length >= thelimit && e.which != '8' && e.which != '37' && e.which != '38' && e.which != '39' && e.which != '40' && e.which != '46' && ctrl_a == false && ctrl_v == false && ctrl_c == false && ctrl_x == false && select == false)
					e.preventDefault();
			})
			.keyup (function(e){//键盘弹起事件
			    isCtrl = false;
				updateCounter();
				if(this.value.length > thelimit){
					this.value = that.value.substr(0,thelimit);
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