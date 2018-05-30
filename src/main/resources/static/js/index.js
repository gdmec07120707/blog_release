"use strict";

$(function(){

    var _pageSize;

    // 根据用户名、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex,pageSize){
        $.ajax({
            url:"/blogs",
            contentType:'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#indexkeyword").val()
            },
            success:function(data){
               $("#mainContainer").html(data);

               var keyword = $("#indexkeyword").val();

               if(keyword.length>0){
                    $(".nav-item .nav-link").removeClass("active");
               }
            },
            error:function(){
                toastr.error("error!");
            }
        });
    }

    //搜索
    $("#indexsearch").click(function(){
        getBlogsByName(0,_pageSize);
    });

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
    	getBlogsByName(pageIndex, pageSize);
    	_pageSize = pageSize;
    });

    //导航栏最新最热切换
    $(".nav-item .nav-link").click(function(){
        var url = $(this).attr("url");

        //先移除样式，在添加当前的点击样式
        $(".nav-item .nav-link").removeClass("active");
        $(this).addClass("active");

        $.ajax({
           url:url+'&async=true',
           success:function(data){
                $("#mainContainer").html(data);
           },
           error:function(){
             toastr.error("error!");
           }
        });

        // 清空搜索框内容
        $("#indexkeyword").val('');

    });
});