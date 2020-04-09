
;!function(){
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer;

    //判断是否web端打开
    if(!/http(s*):\/\//.test(location.href)){
        layer.alert("请先将项目部署到 localhost 下再进行访问【建议通过tomcat、webstorm、hb等方式运行，不建议通过iis方式运行】，否则部分数据将无法显示");
    }else{    //判断是否处于锁屏状态【如果关闭以后则未关闭浏览器之前不再显示】
        if(window.sessionStorage.getItem("lockcms") != "true" && window.sessionStorage.getItem("showNotice") != "true"){
            showNotice();
        }
    }

    //判断是否设置过头像，如果设置过则修改顶部、左侧和个人资料中的头像，否则使用默认头像
    if(window.sessionStorage.getItem('userFace') &&  $(".userAvatar").length > 0){
        $("#userFace").attr("src",window.sessionStorage.getItem('userFace'));
        $(".userAvatar").attr("src",$(".userAvatar").attr("src").split("images/")[0] + "images/" + window.sessionStorage.getItem('userFace').split("images/")[1]);
    }else{
        $("#userFace").attr("src","../../images/face.jpg");
    }

    //公告层
    function showNotice(){
        layer.open({
            type: 1,
            title: "系统公告",
            area: '300px',
            shade: 0.8,
            id: 'LAY_layuipro',
            btn: ['火速围观'],
            moveType: 1,
            content: '<div style="padding:15px 20px; text-align:justify; line-height: 22px; text-indent:2em;border-bottom:1px solid #e2e2e2;"><p class="layui-red">默认密码admin/admin</p></pclass></p> </div>',
            success: function(layero){
                var btn = layero.find('.layui-layer-btn');
                btn.css('text-align', 'center');
                btn.on("click",function(){
                    tipsShow();
                });
            },
            cancel: function(index, layero){
                tipsShow();
            }
        });
    }
    function tipsShow(){
        window.sessionStorage.setItem("showNotice","true");
        if($(window).width() > 432){  //如果页面宽度不足以显示顶部“系统公告”按钮，则不提示
            layer.tips('系统公告躲在了这里', '#userInfo', {
                tips: 3,
                time : 1000
            });
        }
    }

    //锁屏
    function lockPage(){
        layer.open({
            title : false,
            type : 1,
            content : '<div class="admin-header-lock" id="lock-box">'+
                            '<!--<div class="admin-header-lock-img"><img src="images/face.jpg" class="userAvatar"/></div>-->'+
                            '<!--<div class="admin-header-lock-name" id="lockUserName"></div>-->'+
                            '<div class="input_btn">'+
                                '<input type="password" class="admin-header-lock-input layui-input" autocomplete="off" placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />'+
                                '<button class="layui-btn" id="unlock">解锁</button>'+
                            '</div>'+
                            '<p>请输入登陆密码，否则不会解锁成功</p>'+
                        '</div>',
            closeBtn : 0,
            shade : 0.9,
            success : function(){
                //判断是否设置过头像，如果设置过则修改顶部、左侧和个人资料中的头像，否则使用默认头像
                if(window.sessionStorage.getItem('userFace') &&  $(".userAvatar").length > 0){
                    $(".userAvatar").attr("src",$(".userAvatar").attr("src").split("images/")[0] + "images/" + window.sessionStorage.getItem('userFace').split("images/")[1]);
                }
            }
        })
        $(".admin-header-lock-input").focus();
    }
    $(".lockcms").on("click",function(){
        window.sessionStorage.setItem("lockcms",true);
        lockPage();
    })
    // 判断是否显示锁屏
    if(window.sessionStorage.getItem("lockcms") == "true"){
        lockPage();
    }
    // 解锁
    $("body").on("click","#unlock",function(){
        if($(this).siblings(".admin-header-lock-input").val() == ''){
            layer.msg("请输入解锁密码！");
            $(this).siblings(".admin-header-lock-input").focus();
        }else{
            var password = $(this).siblings(".admin-header-lock-input").val()
            $.getJSON('admin/rest_rmp/check_password',{password:password},function (data) {
                if(data.flag){
                    if(data.data){
                        window.sessionStorage.setItem("lockcms",false);
                        $(this).siblings(".admin-header-lock-input").val('');
                        layer.closeAll("page");
                    }else {
                        layer.msg("密码错误，请重新输入！");
                        $(this).siblings(".admin-header-lock-input").val('').focus();
                    }
                }else {
                    layer.msg("网络异常！");
                }
            })
        }
    });
    $(document).on('keydown', function(event) {
        var event = event || window.event;
        if(event.keyCode == 13) {
            $("#unlock").click();
        }
    });

    //退出
    $(".signOut").click(function(){
        window.sessionStorage.removeItem("menu");
        menu = [];
        window.sessionStorage.removeItem("curmenu");
    })

}();
