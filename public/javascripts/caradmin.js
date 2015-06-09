//自定义的功能js，提供相关公共方法,依赖于jquery.js

//表单输入过程中，取消表单错误提示的功能
$("form input").bind('blur', function(){
    var eleName = this.id;
    var obj = $("#" + eleName + "_note");
    if (obj != 'undefined' && null != obj) {
        if (typeof($("#" + eleName).val()) == 'undefined') {
            return;
        }
        if ($("#" + eleName).val().trim().length != 0) {
            obj.html('');
        }
    }
});


/**
 *modal实现的类似alert()的功能
 * @param msg 提示消息
 * @param fun 关闭消息提示窗口的回调函数
 */
var balert = function(msg, fun) {
    var html = '<div class="modal fade" id="userDefineModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
     + '<div class="modal-dialog">'
     +  '<div class="modal-content">'
     +       '<div class="modal-header">'
     +           '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
     +           '<h4 class="modal-title" id="myModalLabel">[提示]</h4>'
     +       '</div>'
     +       '<div class="modal-body">'
     +          ''+msg+''
     +       '</div>'
     +     '<div class="modal-footer">'
     +           '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>'
     +       '</div>'
     +   '</div>'
     +   '</div>'
     + '</div>';
    $("#useForModal").html(html);
    $("#userDefineModal").modal('toggle')

    $('#userDefineModal').on('hidden.bs.modal', function (e) {
        if (fun) {
            fun();
        }
    })
}


/**
 * modal实现的类似confirm的功能
 * @param msg   确认消息
 * @param funName 确定按钮点击时的回调函数
 */
var bconfirm = function(msg, funName) {
    var html = '<div class="modal fade" id="userDefineModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
        + '<div class="modal-dialog">'
        +  '<div class="modal-content">'
        +       '<div class="modal-header">'
        +           '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
        +           '<h4 class="modal-title" id="myModalLabel">[确认操作]</h4>'
        +       '</div>'
        +       '<div class="modal-body">'
        +          ''+msg+''
        +       '</div>'
        +     '<div class="modal-footer">'
        +           '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>'
        +           '<button type="button" class="btn btn-primary" id="userDefineConfirmButton" name="'+funName+'">确定</button>'
        +       '</div>'
        +   '</div>'
        +   '</div>'
        + '</div>';
    $("#useForModal").html(html);
    $("#userDefineModal").modal('toggle')

    $("#userDefineConfirmButton").click(function(){
        funName();
        $("#userDefineModal").modal('toggle')
    });
}

//主页上，左边菜单栏点击时，通过ajax方式去获取网页
$(".sidebar .sidebar-menu .treeview-menu a").click(function(e){
    e.preventDefault();
    var url = this.href;

    showContent(url);
});
//ajax get方式获取网页内容
function showContent(url) {
    $.ajax({
        url : url,
        type : 'GET',
        success : function(data) {
            $("body div.content-wrapper").html('');
            $("body div.content-wrapper").html(data);
        },
        error : function(data) {
            balert("数据加载失败...");
        }
    });
}

function showOrignal(id, obj) {
    $("#" + id).remove();
    var img = new Image();
    img.src = obj.src;
    var h=$("body").height();
    var w=$("body").width();
    $(img).css({"position":"fixed"});
    img.onload = function() {
        var nh=this.naturalHeight;
        var float=nh==0?0:200/nh;
        nh=nh>200?200:nh;
        var nw=this.naturalWidth;
        nw=float>1?nw:nw*float;
        $(this).css({top:(h-nh)/2,left:(w-nw)/2}).attr("id",id).click(function(){
            $(this).remove();
        }).appendTo("body");
    }
}

function disableOrignal(id, obj) {
    $("#" + id).remove();
}