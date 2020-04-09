/**
 *  工具类
 */

;!function(){
    var form = layui.form,$=layui.jquery;
    var mc_util = {
        //获取浏览器入参
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
        },
        escapeHTML: function(a){//layui/table.js已经被改动 无法顺利升级
            if(!a || a==null){
                return '';
            }
            a = "" + a;
            return a.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&apos;");
        },
        autoSetImgUrl:function (inputImg,realSrc) {
            if(typeof realSrc =='undefined' || realSrc+''==''){
                return;
            }
            if(realSrc.indexOf("http://")==0 || realSrc.indexOf("https://")==0){//http开头的地址直接设置
                inputImg.attr("src",realSrc);//图片预览区域
                return;
            }
            //处理额外添加的前缀 因为本地是没这个文件的
            var fileExtraAddPrefix = _GLOBAL.file.fileExtraAddPrefix;
            if(fileExtraAddPrefix!=''){
                var prefix = realSrc.substr(0,fileExtraAddPrefix.length)
                if(prefix == fileExtraAddPrefix){
                    realSrc = realSrc.substring(fileExtraAddPrefix.length)
                }
            }
            realSrc =  _GLOBAL.file.requestPrefix + "/" +realSrc;
            inputImg.attr("src",realSrc);//图片预览区域
        }
        ,
        mappingEntityToFormUiValue : function (elem,entityData) {//映射实体的值到界面元素中
            var body = elem;
            if(entityData){
                for(var key in entityData){
                    var value = entityData[key]
                    if(value != null && typeof value != "undefined"){
                        var input = body.find("[name='"+key+"']");
                        if(input.length<=0){
                            continue;
                        }
                        var laySkin = input.attr("lay-skin")
                        if(laySkin && laySkin == 'switch'){//忽略开关
                            continue;
                        }
                        //外键select2
                        var className = input.attr("class")
                        if(className && className.indexOf("magicalcoder-foreign-select2")!=-1){
                            input.attr("data-value",value);
                            continue;
                        }
                        //设置radio值
                        var type = input.attr("type")
                        if(type && type=='radio' && value+''!=''){
                            body.find("[name='"+key+"'][value='" + value + "']").prop("checked", "checked");
                            continue
                        }
                        //如果有图片或者文件则自动设置图片
                        var inputImg = body.find(".img_"+key);
                        if(inputImg.length>0 && value!=''){
                            this.autoSetImgUrl(inputImg,value)
                        }
                        //其他正常表单
                        input.val(value);
                    }
                }
                //开关单独处理
                body.find("input[lay-skin='switch']").each(function (index,item) {
                    var name = $(item).attr("name")
                    var check = (entityData[name]+''=='true' || entityData[name]+''=='1') ? true:false
                    $(item).prop("checked",check);
                })
                form.render();
            }
        },
        
    }
    youyaboot_all.mc_util = mc_util;
}();
