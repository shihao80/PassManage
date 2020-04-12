
;!function(){
    var form = layui.form,$ = layui.jquery,layer = parent.layer === undefined ? layui.layer : top.layer
    var authtree = youyaboot_all.authtree,
        mc_util = youyaboot_all.mc_util;

    var authTreeId = "#auth-tree"

    var roleId = mc_util.getParameter("roleId");
    if(roleId!=''){
        $.getJSON("admin/rest_rmp/tree_data/"+roleId,{date:new Date().getTime()},function (data) {
            if(data.flag){
                authtree.render(authTreeId,data.data,{inputname: 'authids[]', openall: true})
            }else {
                layer.msg("获取权限树失败:"+data.desc)
            }
        })
    }else {
        layer.msg("哎呀，出错了")
    }

    form.on("submit(save)",function(data){
        var authids = authtree.getChecked(authTreeId);
        $.post('admin/rest_rmp/save_tree_data/'+roleId,{ids:authids},function (data) {
            if(!data.flag){
                layer.msg(data.desc)
            }else {
                layer.msg("授权成功，请退出账号重新登录方可生效！")
            }
        })
        return false;
    })
}();

