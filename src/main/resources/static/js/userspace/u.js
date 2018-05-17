"use strict";
// DOM 加载完再执行
$(function(){

    var _pageSize;

    // 根据用户名、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex, pageSize){
        $.ajax({
            url:"/u/"+username+"/blogs",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#keyword").val()
            },
            success:function(data){
                 $("#mainContainer").html(data);
            },
             error : function() {
            	toastr.error("error!");
            }
        })
    }

    // 分页,依赖thymeleaf-bootstrap-paginator.js
	$.tbpage("#mainContainer", function (pageIndex, pageSize) {
	    toastr.error("page");
		getBlogsByName(pageIndex, pageSize);
		_pageSize = pageSize;
	});

    //搜索
    $("#searchBlogs").click(function(){
        getBlogsByName(0,_pageSize);
    });

   // 最新\最热切换事件
   $(".nav-item .nav-link").click(function(){
        var url = $(this).attr("url");

         // 先移除其他的点击样式，再添加当前的点击样式
         $(".nav-item .nav-link").removeClass("active");
         $(this).addClass("active");

         $.ajax({
            url:url+'&async=true',
            success:function(data){
                 $("#mainContainer").html(data);
            },
            error : function() {
            	 toastr.error("error!");
             }
         })

          // 清空搜索框内容
          $("#keyword").val('');

       });

});