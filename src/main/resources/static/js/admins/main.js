/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {
    $(".blog-menu .list-group-item").click(function(){
        var url = $(this).attr("url");

         $(".blog-menu .list-group-item").removeClass("active");
         $(this).addClass("active");

         $.ajax({
            url:url,
            success:function(data){
                 $("#rightContainer").html(data);
            },
            error:function(){
                alert("error");
             }
         });
    });

    // 选中菜单第一项
    $(".blog-menu .list-group-item:first").trigger("click");

});