Ext.msgBox = function(){
    var msgCt;

    function createBox(t, s){
       // return ['<div class="msg">',
       //         '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
       //         '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
       //         '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
       //         '</div>'].join('');
       return '<div class="msg ' + Ext.baseCSSPrefix + 'border-box"><h3>' + t + '</h3><p>' + s + '</p></div>';
    }
    return {
        msg : function(title, format) {
            // Ensure message container is last in the DOM so it cannot interfere with
            // layout#isValidParent's DOM ordering requirements.
//            if (msgCt) {
//                document.body.appendChild(msgCt.dom);
//            } else {
//                msgCt = Ext.DomHelper.append(document.body, {id:'msg-div'}, true);
//            }
//            var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
//            var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
//            m.hide();
//            m.slideIn('t').ghost("t", { delay: 1500, remove: true});
        },
        remainMsg : function(title, msg, icon) {
//        	Ext.MessageBox.alert(title, msg);
//            Ext.fly('info').dom.value = Ext.MessageBox.INFO;
//            Ext.fly('question').dom.value = Ext.MessageBox.QUESTION;
//            Ext.fly('warning').dom.value = Ext.MessageBox.WARNING;
//            Ext.fly('error').dom.value = Ext.MessageBox.ERROR;
            Ext.MessageBox.show({
                title: title,
                msg: msg,
                buttons: Ext.MessageBox.OK,
                icon: icon
            });
        }
    };
}();