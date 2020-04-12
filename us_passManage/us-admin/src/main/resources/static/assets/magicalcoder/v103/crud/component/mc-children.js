/**
 *  更多信息：定义子表界面的一些通用处理
 */

;!function(){
     element = layui.element;
     $ = layui.jquery,
     mc_util = youyaboot_all.mc_util,
     mc_constant = youyaboot_all.mc_constant,
     laytpl = layui.laytpl;
    //从url构建外键 允许不传
     mcForeignName = mc_util.getParameter('mcForeignName')//外键名称
     mcForeignValue = mc_util.getParameter('mcForeignValue')//外键值


    var mc_children = {
        //外键就靠这2个参数确认是否来自更多信息
        buildForeignParam : function () {
            if(mcForeignName!=''){
                return "&mcForeignName="+mcForeignName+"&mcForeignValue="+mcForeignValue;
            }
            return "";
        },
        //更多信息点击进来的 如果出现外键关联表，应当禁用掉外键表区域，因为外键表不允许编辑
        _disableForeignDom : function (name,foreignSelect2Class,dataValueSet) {
            var foreignDoms = $("[name='"+name+"']")
            if(foreignDoms.length<=0){
                return;
            }
            foreignDoms.each(function (idx, item) {
                var foreignDom = $(item)
                if(foreignDom && foreignDom.hasClass(foreignSelect2Class)){//select2
                    var dataValue = foreignDom.attr("data-value") || '';
                    if(dataValue == ''){//dataValue在列表页 table内是没有值的
                        var option = '<option selected="selected" value="'+mcForeignValue+'">'+mcForeignValue+'</option>'
                        foreignDom.append(option)
                        foreignDom.val(mcForeignValue);
                        if(dataValueSet){
                            foreignDom.attr("data-value",mcForeignValue);
                        }
                    }
                }else {
                    foreignDom.val(mcForeignValue);
                }
                foreignDom.attr("disabled","disabled")
            })

        },
        //查询区域 考虑禁用掉外键
        disabledFromQueryParentArea : function () {
            if(mcForeignName && mcForeignName!=''){
                $("button[type='reset']").hide();//重置按钮禁用
                //查询条件
                this._disableForeignDom(mcForeignName+"First",mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2,true)
                this._disableForeignDom(mcForeignName+"Second",mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2,true)
                return true;
            }
            return false;
        },
        //表格区域 考虑禁用掉外键
        disabledTableParentArea : function () {
            if(mcForeignName && mcForeignName!=''){
                //查询条件
                this._disableForeignDom(mcForeignName,mc_constant.MC_LAY_CLASS_NAME.TABLE.foreign_select2,false)
            }
        },
        //表单区域 考虑禁用掉外键
        disabledFormParentArea : function () {
            if(mcForeignName && mcForeignName!=''){
                //查询条件
                $("button[type='reset']").hide();//重置按钮禁用
                this._disableForeignDom(mcForeignName,mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2,true)
            }
        },
        //构造子表面板内容
        buildTabTemplate : function (tableName,identify,childrenPage) {
            var html = laytpl('<div class="layui-tab mcChildrenPage" lay-filter="tabClick">' +
                '                  <ul class="layui-tab-title">' +
                '{{# layui.each(d.childrenPage,function(index,item){  }}'+
                '                       <li {{# if(index==0){ }}class="layui-this" {{# } }}>{{ item.tabTitle }}</li>' +
                '{{#  }); }}'+
                '               </ul>' +
                '        <div class="layui-tab-content">' +
                '{{# layui.each(d.childrenPage,function(index,item){  }}'+
                '   {{# if(index==0){ }}'+
                '            <div class="layui-tab-item layui-show">' +
                '                <iframe class="childrenIframe" src="{{ item.url }}?mcForeignName={{ item.mcForeignName }}&mcForeignValue={{ d.identify }} "></iframe>' +
                '            </div>' +
                '   {{# }else{ }}'+
                '            <div class="layui-tab-item">' +
                '                <iframe class="childrenIframe" futureSrc="{{ item.url }}?mcForeignName={{ item.mcForeignName }}&mcForeignValue={{ d.identify }} "></iframe>' +
                '            </div>' +
                '   {{# } }}'+
                '{{#  }); }}'+
                '    </div></div>').render({tableName:tableName,identify:identify,childrenPage:childrenPage});
            return html;
        },
        //更多信息 标签页点击
        tabClickEvent : function () {
            var clicked = {}
            element.on('tab(tabClick)',function (data) {
                if(data.index!=0){
                    var index = data.index +""
                    if(!clicked[index]){
                        clicked[index]=true
                        var frame = $(".mcChildrenPage").find('.childrenIframe').eq(index)
                        // console.log(frame.attr("futureSrc"))
                        var src = frame.attr("src")
                        if(!src || src==''){
                            frame.attr("src",frame.attr("futureSrc"))
                        }
                    }
                }
            })
        }
    }
    youyaboot_all.mc_children = mc_children;
}();
