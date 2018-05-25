/*!
 * blogedit.html 页面脚本.
 * 
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=blogedit.js

// DOM 加载完再执行
$(function() {
	
	// 初始化 md 编辑器
    $("#md").markdown({
        language: 'zh',
        fullscreen: {
            enable: true
        },
        resize:'vertical',
        localStorage:'md',
        imgurl: 'http://localhost:8081/upload',
        base64url: 'http://localhost:8081/base64'
    });
    
    // 初始化标签
        $('.form-control-tag').tagsInput({
        	'defaultText':'输入标签'

        });
    
    //$('.form-control-chosen').chosen();





    $("#submitBlog").click(function(){
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url:'/u/'+$(this).attr("userName")+"/blogs/edit",
            type:"POST",
            contentType:"application/json; charset=utf-8",
            data:JSON.stringify({"id":Number($('#id').val()),
                        		    	"title": $('#title').val(),
                        		    	"summary": $('#summary').val() ,
                        		    	"catalog":{"id":$('#catalogSelect').val()},
                        		    	"tags":$('.form-control-tag').val(),
                        		    	"content": $('#md').val()}),
            beforeSend:function(request){
                            request.setRequestHeader(csrfHeader,csrfToken);
                       },
            success:function(data){
                if(data.success){
                     window.location= data.body;
                }else{
                     toastr.error(data.message);
                }
            },
            error:function(){
                toastr.error("error!");
              }
        });

    });


});