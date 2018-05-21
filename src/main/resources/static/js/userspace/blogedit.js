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
    
    // 初始化标签控件
    /**$('.form-control-tag').tagEditor({
        initialTags: [],
        maxTags: 5,
        delimiter: ', ',
        forceLowercase: false,
        animateDelete: 0,
        placeholder: '请输入标签'
    });
    
    $('.form-control-chosen').chosen();
    */




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