/*!
 * blog.html 页面脚本.
 * 
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=blog.js

// DOM 加载完再执行
$(function() {
	$.catalog("#catalog", ".post-content");

	//删除博客
	$(".blog-content-container").on("click",".blog-delete-blog",function(){
	    var csrfToken = $("meta[name='_csrf']").attr("content");
	    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

	    $.ajax({
	        url:$(this).attr("blogUrl"),
	        type:'DELETE',
	        beforeSend:function(request){
	            request.setRequestHeader(csrfHeader,csrfToken);
	        },
	        success:function(data){
	            if(data.success){
	                window.location = data.body;
	            }else{
	                toastr.error(data.message);
	            }
	        },
	        error:function(){
	            toastr.error("error");
	        }
	    });
	});

	//获取评论列表
	function getComment(blogId){
	     // 获取 CSRF Token
         var csrfToken = $("meta[name='_csrf']").attr("content");
         var csrfHeader = $("meta[name='_csrf_header']").attr("content");

         $.ajax({
            url:'/comments',
            type:'GET',
            data:{"blogId":blogId},
            beforeSend:function(request){
                request.setRequestHeader(csrfHeader,csrfToken);
            },
            success:function(data){
               $("#mainContainer").html(data);
            },
            error:function(){
                toastr.error("请求错误");
            }
         });
	}

	//提交评论
	$(".blog-content-container").on("click","#submitComment",function(){
	    // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
           url:'/comments/add',
           type:'GET',
           data:{"blogId":blogId,"commentContent":$('#commentContent').val()},
           success:function(data){
                if(data.success){
                    //清空输入框
                    $('#commentContent').val('');
                    getComment(blogId);
                }else{
                    toastr.success("评论失败"+data.message);
                }
           },
            error:function(){
                  toastr.success("评论失败");
            }
        });
	})

	//删除评论
    $(".blog-content-container").on("click",".blog-delete-comment",function(){
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url:'/comments/'+$(this).attr("commentId")+'?blogId='+blogId,
            type:'DELETE',
            beforeSend:function(request){
                request.setRequestHeader(csrfHeader,csrfToken);
            },
            success:function(data){
                if(data.success){
                    getComment(blogId);
                }else{
                    toastr.error(data.message);
                }
            },
            error:function(){
                toastr.error("发生错误");
            }
        })
    })

	getComment(blogId);
});