/**
 *  封装所有场所关于select2的用法
 * v 0.0.1
 * https://select2.org/data-sources/ajax
 * $elem 都是一个元素 外面已经控制好了
 * pageConfig是列表页js 详情页js的 CONFIG
 * {
 *     event:{
            select2Change:function (elem,name,value) {//切换select2回调事件

            }
        }
 * }
 */

;!function(){
    var $ = layui.jquery,layer = parent.layer === undefined ? layui.layer : top.layer,
        mc_verify = youyaboot_all.mc_verify
        ;

    var mc_select2 = {
        listToMap:function (fieldName, list) {
            var map = {}
            if(list && list.length>0){
                for(var i=0;i<list.length;i++){
                    var obj = list[i];
                    var value = obj[fieldName]+"";//保证字符串
                    map[value] = obj;
                }
            }
            return map;
        },
        //是不是select结构 如果不是 就不要用select2
        isSelectElem:function($elem){
            if(!$elem.is("select")){//不是下拉框 就不要初始化
                return false;
            }
            return true;
        },
        //表格内的普通单选按钮自动提交
        bindTableSingleSelect2 : function (tableNameRest,$elem,configData,selectChangeFailCallback,pageConfig) {
            configData = configData || {width:"150px",allowClear:false}
            var placeholder = $elem.attr("placeholder") || '搜索'
            $elem.select2({placeholder:placeholder,width:configData.width,allowClear:configData.allowClear});
            this._bindTableSelect2AjaxChangeValueEvent(tableNameRest,$elem,selectChangeFailCallback,pageConfig)
        },

        //寻找所有控件 自动注册 https://select2.org/data-sources/ajax
        bindForeignSelect2ByDomId : function (tableNameRest,$elem,configData,selectChangeFailCallback,pageConfig) {
            configData = configData || {allowClear:true,width:"150px"}
            this._buildForeignSelect2($elem,configData)
            this._bindTableSelect2AjaxChangeValueEvent(tableNameRest,$elem,selectChangeFailCallback,pageConfig)
        },
        _buildForeignSelect2 : function ($elem,configData) {
            var _this = this;
            if(!this.isSelectElem($elem)){
                return;
            }
            var placeholder = $elem.attr("placeholder") || '搜索'
            $elem.select2({
                allowClear : configData.allowClear || true,//必须设置placeholder 否则报错
                placeholder: placeholder,
                width : configData.width || "150px",
                delay : 500,//等待500ms才触发
                ajax : {
                    url:function (params) {
                        var dataUrl = $(this).attr("data-url");
                        var dataId = $(this).attr("data-id");
                        var fields = $(this).attr("data-text-fields");
                        var extra = {dataId : dataId,fields:fields}
                        params.extra = extra
                        return dataUrl;
                    },
                    data : function (params) {//查询参数
                        //params能拿到搜索框数据
                        var limit = $(this).attr("data-limit")
                        limit = limit ? parseInt(limit) : 20;
                        var keyword = params.term
                        if(keyword && keyword!=''){
                            return {"keyword":keyword,"limit":limit}
                        }else {
                            return {"limit":limit}
                        }
                    },
                    processResults : function (data,params,item) {
                        if(data.flag){
                            var dataId = params.extra.dataId;
                            var fields = params.extra.fields;
                            var list = data.data;
                            var result = _this._buildSelect2IdTextData(list,dataId,fields)
                            return {"results":result}
                        }else {
                            layer.msg("获取数据失败:"+data.desc)
                        }
                        return {"results":[]}
                    }
                }
            });
        },
        //需要重置默认值 当data-value存在时才会尝试
        batchTryToAjaxSetSelect2Content : function ($elems) {
            var _this = this;
            var map = {}
            //不确定$elem的元素数量
            $elems.each(function(idx,item){
                var selectDom = $(item)
                var value = selectDom.attr("data-value");//决定是否进行初始化 编辑页使用
                if(value && value!=''){
                    var dataUrl = selectDom.attr("data-url");
                    var dataId = selectDom.attr("data-id");
                    var fields = selectDom.attr("data-text-fields");
                    //归类 url是归同一组
                    var group = map[dataUrl];
                    var mapItem = {selectDom:selectDom,uniqueField:dataId,uniqueValue:value,showTextFields:fields};
                    if(group == null || typeof group == 'undefined'){
                        group = [mapItem]
                        map[dataUrl] = group;
                    }else {
                        group.push(mapItem)
                    }
                }
            })

            //开始拆分组 一组一组的去后端获取数据
            for(var dataUrl in map){
                var group = map[dataUrl];
                var uniqueValues = []//不要重复
                var existUniqueMap = {}
                for(var i =0;i<group.length;i++){
                    var v = group[i].uniqueValue;
                    if(v+''!='' && existUniqueMap[v]+""!="1"){//去重传后端主键ids
                        uniqueValues.push(v)
                        existUniqueMap[v]="1";
                    }
                }
                var firstItem = group[0];//取第一个额外数据
                //这是个批量获取的接口
                _this._ajaxUpdateSelectText(group,dataUrl,firstItem,uniqueValues);
            }
        },
        _ajaxUpdateSelectText:function (group,dataUrl,firstItem,uniqueValues) {//异步编码 要采用栈顶逻辑 避免出现共享变量 导致异步代码里 都是同一个最新值
            var firstUniqueField = firstItem.uniqueField;
            var _this = this;
            $.getJSON(dataUrl,{uniqueField:firstUniqueField,uniqueValue:uniqueValues,date:new Date().getTime()},function (data) {
                if(data.flag){
                    var entityList = data.data;
                    var entityMap = _this.listToMap(firstUniqueField,entityList);
                    //一个个的去更新
                    for(var i =0;i<group.length;i++){
                        var mapItem = group[i];
                        var uniqueField = mapItem.uniqueField;//data-id
                        var uniqueValue = mapItem.uniqueValue;//data-value
                        var showTextFields = mapItem.showTextFields;//data-text-fields
                        var entity = entityMap[uniqueValue];
                        var selectDom = mapItem.selectDom;
                        var selectItemData = _this._buildItemTextData(entity,uniqueField,showTextFields);
                        if(_this.isSelectElem(selectDom)){//正常select2
                            var option = new Option(selectItemData.text,selectItemData.id, true, true);
                            selectDom.append(option);
                        }else {//其他情况
                            if(selectDom.is("input") || selectDom.is("textarea")){
                                selectDom.val(selectItemData.text)
                            }else {
                                selectDom.html(selectItemData.text)
                            }
                        }
                    }
                }
            })
        }
        ,
        _buildItemTextData:function (entity,uniqueField,showTextFields) {
            var id = entity[uniqueField]
            var textArr = []
            if(showTextFields!=''){
                var arr = showTextFields.split(",");
                for(var k=0;k<arr.length;k++){
                    textArr.push(entity[arr[k]])
                }
            }else {
                textArr.push(id)
            }
            return {id:id,text:textArr.join("||")};
        }
        ,
        _buildSelect2IdTextData : function (list,uniqueField,showTextFields) {
            var results = []
            if(list){
                for(var j =0;j<list.length;j++){
                    var entity = list[j];
                    results.push(this._buildItemTextData(entity,uniqueField,showTextFields))
                }
            }
            return results;
        },
        _triggerCallback:function (elem,value,tableNameRest,selectChangeFailCallback,pageConfig) {
            elem.attr("data-value",value);//实时更新此字段属性 保持一致 TODO待测试 彻底解决外键的不确定性 延迟性问题
            //处理回调方法
            var pageConfigDefault = {
                event:{
                    select2Change:function (elem,name,value) {//切换select2回调事件

                    }
                }
            }
            $.extend(true,pageConfigDefault,pageConfig||{})
            pageConfigDefault.event.select2Change(elem,elem.attr("name"),value);

            if(value==''){
                return;
            }
            // ajax更改数据
            var identify = elem.attr("data-identify")
            if(identify && identify!=''){
                var name = elem.attr("name")
                var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                var postData = {}//必须设置主键
                postData[name] = value;
                if(!mc_verify.verify(value,elem)){
                    return;
                }
                $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                    layer.close(index);
                    if(data.flag){
                        layer.msg("修改成功！");
                    }else {
                        layer.msg("修改失败:"+data.desc);
                        if(selectChangeFailCallback){
                            selectChangeFailCallback()
                        }
                    }
                })
            }
        }
        ,
        //表格中select2改变值 触发事件
        _bindTableSelect2AjaxChangeValueEvent : function (tableNameRest,$elem,selectChangeFailCallback,pageConfig) {
            if(!this.isSelectElem($elem)){
                return;
            }
            var _t = this;
            $elem.on('select2:clear',function (e) {
                _t._triggerCallback($(this),'',tableNameRest,selectChangeFailCallback,pageConfig)
            })
            //是select2 正常处理
            $elem.on('select2:select', function (e) {
                _t._triggerCallback($(this),e.params.data.id,tableNameRest,selectChangeFailCallback,pageConfig)
            });
        }
    }
    youyaboot_all.mc_select2 = mc_select2;
}();
