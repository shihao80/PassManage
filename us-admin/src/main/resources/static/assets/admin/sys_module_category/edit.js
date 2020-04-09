/**
* 代码为自动生成 Created by www.magicalcoder.com
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:323237052
*/
;!function(){
        var $ = layui.jquery
        ,layer = parent.layer === undefined ? layui.layer : top.layer
        ,form = layui.form;
        var mc_layui_component=youyaboot_all.mc_layui_component
        ,mc_util = youyaboot_all.mc_util
        ,mc_children = youyaboot_all.mc_children
        ,mc_verify = youyaboot_all.mc_verify
        ,mc_constant = youyaboot_all.mc_constant
        ,mc_rmp  = youyaboot_all.mc_rmp
    ;
    var common_config = {
        tableNameRest:"sys_module_category_rest",
        tableName:"sys_module_category",
        moduleName:"sys_module_category",//sys_module的moduleName
        form:{
        }
    }
    var tableNameRest = common_config.tableNameRest;
    var moduleName = common_config.moduleName;
    var obj = {
        render:function () {//入口
            this.ajaxGetEntityDataAndAutoSetFormUiValue();
            this.submit();
        },
        //初始化表单
        ajaxGetEntityDataAndAutoSetFormUiValue : function () {
            var _t = this;
            var identify = mc_util.getParameter('identify')//从url中检测主键
            if(identify!=null && identify!=''){//断定是编辑操作
                $.getJSON('admin/'+tableNameRest+"/get/"+identify,{date:new Date().getTime()},function (data) {//获取此实体详情数据
                    if(data.flag){
                        var entity = data.data
                        if(entity && entity!=null){
                            //把请求到的实体内容自动设置到表单中去
                            mc_util.mappingEntityToFormUiValue($("body"),entity);
                            //禁用外键编辑区域：只有当从更多信息点击进来 才会禁用 单表操作不会执行
                            mc_children.disabledFormParentArea();
                            //权限 注意xxx-edit.html页面刚进来 是被隐藏的(body标签的layui-hide样式) 这样当权限执行完 才进行一一显示
                            mc_rmp.paintBody(moduleName,function () {
                                var verifyData = mc_layui_component.bindMagicalCoderLayUiComponentFromDetail(common_config);//返回富文本的校验 为了能同步隐藏域
                                _t.layuiFormVerify(verifyData);
                            })
                        }else {
                            layer.msg("此条记录已被删除,请重新查询！");
                        }
                    }else {
                        layer.msg("查询失败:"+data.desc);
                    }
                })
            }else {//新增操作
                //禁用外键编辑区域：只有当从更多信息点击进来 才会禁用 单表操作不会执行
                mc_children.disabledFormParentArea();
                //权限
                mc_rmp.paintBody(moduleName,function () {
                    var verifyData =mc_layui_component.bindMagicalCoderLayUiComponentFromDetail(common_config);//返回富文本的校验 为了能同步隐藏域
                    _t.layuiFormVerify(verifyData);
                    $("button[type='reset']").removeClass('layui-hide');//把重置按钮显示处理
                })
            }
        },
        //layui的表单校验规则
        layuiFormVerify:function (verifyData) {//verifyData 里如果由内容则是富文本的校验同步
            var verifyData =
                $.extend(verifyData,{//这里可以自定义layui的校验规则 自行扩展 参考官网文档 https://www.layui.com/doc/modules/form.html

                })
            form.verify(verifyData);
        }
        ,
        //表单提交
        submit : function () {
            //重置按钮把外键的默认值清空
            $("button[type='reset']").click(function(){
                $("."+mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2).val(null).trigger('change');//固定写法 把搜索下拉重置空值
                return true;
            });
            //表单校验 在_bindFormDom方法
            form.on("submit(save)",function(data){
                // 实际使用时的提交信息
                var postData = data.field;
                //开关单独处理
                $('body').find("input[lay-skin='switch']").each(function (index,item) {
                    var name = $(item).attr("name")
                    var field = data.field
                    postData[name] = field[name] ? ((field[name]+''=='on' || field[name]+'' == 'true')  ? 1 : 0):0;//开关请用0或1来设置值
                })
                //清理上次不通过样式提醒
                for(var name in postData){
                    var dom = $("[name='"+name+"']")
                    mc_verify.clearDangerClass(dom)
                }
                //MagicalCoder扩展的表单校验
                for(var name in postData){
                    var dom = $("[name='"+name+"']")
                    var value = postData[name]
                    if(!mc_verify.verify(value,dom)){
                        return false;
                    }
                }
                //弹出loading
                var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
                $.post('admin/'+tableNameRest+'/save',postData,function(res){
                    top.layer.close(index);
                    if(res.flag){
                        parent.layui.$(".search_btn").click()//parent代表父窗口，因为详情页是iframe弹出来的；让列表页重新执行一次搜索
                        //关闭当前层
                        var pidx = parent.layui.layer.index
                        parent.layui.layer.close(pidx-1);
                        if(postData.id==''){
                            top.layer.msg("新增成功:请添加【二级菜单】再配置【角色管理】")
                        }
                    }else {
                        top.layer.msg("保存失败:"+res.desc);
                    }
                })
                return false;
            })
        }
    }
    obj.render();
}();
