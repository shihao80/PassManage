;!function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;

    $("#codeImg").click(function(){
        var url = $(this).attr("src") + "?time="+(new Date().getTime())
        $(this).attr("src",url);
    })
    var obj = {
    getParameter:function (name) {
        var query = window.location.search.substring(1);
        if(query!=''){
            var vars = query.split("&");
            for (var i=0;i<vars.length;i++) {
                var pair = vars[i].split("=");
                if(pair.length=2){
                    if(pair[0] == name){return pair[1];}
                }
            }
        }
        return null;
    }}
    //登录按钮
    form.on("submit(login)",function(data){
        var _t = $(this);
        var password = $("#password").val();
        var sessionId = $("#sessionId").val();
        var code = $("#code").val();
        if(code ==''){
            layer.msg("请输入验证码");
            return false;
        }
        _t.text("登录中...").attr("disabled",true).addClass("layui-disabled");
        $.post("login",{"username":$("#username").val(),"password":password+"|||"+sessionId+"|||"+code,"time":new Date().getTime()},function (data) {
            if(data.flag){
                var from = obj.getParameter("from")
                if(from!=null && from =='innerLogin'){//来源
                    var redirectUrl = decodeURIComponent(obj.getParameter("redirectUrl"));
                    window.location.href = redirectUrl;
                }else {
                    window.location.href = "admin/rmp/index";
                }
            }else {
                layer.msg(data.desc)
                _t.text("登录").attr("disabled",false).removeClass("layui-disabled");
            }
        })
        return false;
    })

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
}();

