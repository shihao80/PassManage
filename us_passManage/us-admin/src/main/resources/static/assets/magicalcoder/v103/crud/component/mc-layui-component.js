/**
 *  结合layui的所有控件 统一处理
 *  修改控件值 触发ajax update操作
 *  v 0.0.1
 */

;!function(){
    var form = layui.form,$ = layui.jquery,
    layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        slider = layui.slider,
        rate = layui.rate,
        colorpicker = layui.colorpicker,
        layedit = layui.layedit,
        laydate = layui.laydate,
        mc_verify = youyaboot_all.mc_verify,
        mc_constant = youyaboot_all.mc_constant,
        mc_util = youyaboot_all.mc_util,
        mc_select2 = youyaboot_all.mc_select2 ;
//自定义验证规则
    var mc_layui_component = {
        //绘制详情页layui各种组件 并且会预设初始值
        bindMagicalCoderLayUiComponentFromDetail:function (config) {
            var _t = this;
            this.config = config;
            //寻找所有日期控件 自动注册
            this._bindDate("."+mc_constant.MC_LAY_CLASS_NAME.FORM.laydate);
            //寻找所有图片上传
            this._bindUpload("."+mc_constant.MC_LAY_CLASS_NAME.FORM.layupload);
            //外键下拉
            $("."+mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2).each(function (index,item) {
                _t._bindForeignSelect2(config.tableNameRest,$(this),{width:"100%"},config);
            })
            //批量自动初始化外键值
            mc_select2.batchTryToAjaxSetSelect2Content($("."+mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2))
            //寻找滑块
            this._bindSlider("."+mc_constant.MC_LAY_CLASS_NAME.FORM.slider);
            //寻找颜色选择器
            this._bindColorPicker("."+mc_constant.MC_LAY_CLASS_NAME.FORM.color_picker);
            //寻找评星
            this._bindRate("."+mc_constant.MC_LAY_CLASS_NAME.FORM.rate);
            //寻找代码修饰器
            this._bindCode("."+mc_constant.MC_LAY_CLASS_NAME.FORM.code);

            //寻找所有的富文本控件
            var formVerifyEditData = this._bindEdit("."+mc_constant.MC_LAY_CLASS_NAME.FORM.layedit);
            return formVerifyEditData;
        },
        //绘制table中layui各种组件 并且会预设初始值
        bindMagicalCoderLayUiComponentFromTable:function (config,failCallBack) {
            this.config = config;
            var _t = this;
            var configData = {width:"200px",allowClear:false}
            $("."+mc_constant.MC_LAY_CLASS_NAME.TABLE.single_select2).each(function (index,item) {
                _t._bindSingleSelect2(config.tableNameRest,$(this),configData,failCallBack,config);
            })
            $("."+mc_constant.MC_LAY_CLASS_NAME.TABLE.foreign_select2).each(function (index,item) {
                _t._bindForeignSelect2(config.tableNameRest,$(this),configData,failCallBack,config);
            })
            //批量自动初始化外键值
            mc_select2.batchTryToAjaxSetSelect2Content($("."+mc_constant.MC_LAY_CLASS_NAME.TABLE.foreign_select2))
            this._bindSwitchAjaxUpdate("."+mc_constant.MC_LAY_CLASS_NAME.TABLE.lay_switch,failCallBack);
            this._bindDate("."+mc_constant.MC_LAY_CLASS_NAME.TABLE.lay_date,failCallBack);
            this._bindInputText('.'+mc_constant.MC_LAY_CLASS_NAME.TABLE.text,failCallBack);
            this._bindRadio('.'+mc_constant.MC_LAY_CLASS_NAME.TABLE.radio,failCallBack);

        },
        //绘制查询区域layui各种组件 并且会预设初始值 一般也就只有日期跟外键 其他的暂不考虑了 您也可以扩展
        bindMagicalCoderLayUiComponentFromQuery:function (config) {
            this.config = config;
            var _t = this;
            //绑定外键查询
            $("."+mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2).each(function (index,item) {
                _t._bindForeignSelect2(config.tableNameRest,$(this),{width:"resolve",allowClear:true},null,config);
            })
            //批量自动初始化外键值
            mc_select2.batchTryToAjaxSetSelect2Content($("."+mc_constant.MC_LAY_CLASS_NAME.FORM.foreign_select2))
            //绑定日期
            this._bindDate("."+mc_constant.MC_LAY_CLASS_NAME.FORM.laydate);
        },
        _bindRadio : function (elem,clickFailCallback) {
            var tableNameRest = this.config.tableNameRest;
            $(elem).each(function (index, item) {
                var name = $(item).attr("name")//由于radio name不能重复 所以用_+行号来区分
                var arr = name.split("_");
                name = arr[0]
                var id = $(item).attr("id")
                var filter = $(item).attr("lay-filter")
                var select = name ? name : id
                form.on('radio('+filter+')', function(domData){
                    var _t = $(this)
                    var identify = _t.attr("data-identify")
                    if(identify && identify!=''){//ajax更新结果
                        var value = domData.elem.value;
                        var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                        var postData = {}//必须设置主键
                        postData[select] = value;
                        if(!mc_verify.verify(value,$(item))){
                            return;
                        }
                        $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                            layer.close(index);
                            if(data.flag){
                                layer.msg("修改成功！");
                            }else {
                                layer.msg("修改失败:"+data.desc);
                                if(clickFailCallback){
                                    clickFailCallback()
                                }
                            }
                            form.render('radio');//这一步必须执行 否则改了不生效
                        })
                    }
                })
            })
        },
        //普通输入框 输入完成离开事件
        _bindInputText : function (inputElem,failCallBack) {
            var tableNameRest = this.config.tableNameRest;
            $(inputElem).each(function (index,item) {//必须1个个绑定 不支持单利
                var originValue = ''
                $(item).focus(function () {
                    originValue =$(this).val()
                })
                $(item).blur(function () {
                    var newValue = $(this).val()
                    if(originValue != newValue){//需要更新
                        var identify = $(item).attr("data-identify")
                        if(identify && identify!=''){
                            var name = $(item).attr("name")
                            var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                            var postData = {}
                            postData[name] = newValue;
                            if(!mc_verify.verify(newValue,$(item))){
                                return;
                            }
                            $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                                layer.close(index);
                                if(data.flag){
                                    layer.msg("修改成功！");
                                }else {
                                    layer.msg("修改失败:"+data.desc);
                                    $(item).val(originValue)//还原
                                }
                            })
                        }
                    }
                })
            })
        },
        //綁定日期控件
        _bindDate : function (elem,doneFailCallback) {
            var tableNameRest = this.config.tableNameRest;
            var _t = this;
            $(elem).each(function (index,item) {//必须1个个绑定 不支持单利
                var id = $(item).attr("id")
                var name = $(item).attr("name")
                var formConfig = {}
                $.extend(true,formConfig,_t.config.form);
                var formRenderConfig = {}
                if(formConfig[name]){
                    formRenderConfig = formConfig[name].renderConfig
                }else {//可能是查询区域
                    var realName = null;
                    var dataName = $(item).attr("data-name")
                    if(dataName && dataName.length>0){
                        realName = dataName
                    }else {//老代码兼容
                        if(name.lastIndexOf("First")!=-1){
                            realName = name.substring(0,name.lastIndexOf("First"))
                        }
                        if(name.lastIndexOf("Second")!=-1){
                            realName = name.substring(0,name.lastIndexOf("Second"))
                        }
                    }
                    if(realName!=null){
                        if(formConfig[realName]){
                            formRenderConfig = formConfig[realName].renderConfig
                        }
                    }
                }
                var domRenderConfig = _t.iteratorAttr({},item);//dom元素上面的配置
                var options = $.extend(true,domRenderConfig,formRenderConfig)// 以dom属性配置优先 其次读取js配置

                var select = id ? "#"+id : "[name='"+name+"']"//列表页有id 所以不会批量绑定
                var identify = $(item).attr("data-identify")
                var value = $(item).val()
                if(value && value!=''){//智能点 默认给当前时间做初始值
                    var reg=/^(\d{4})[-\/].*/;
                    if(!reg.test(value)){//非日期字符串格式 尝试解析字符串
                        try {
                            if(value.toUpperCase()=='CURRENT_TIMESTAMP'){
                                value = new Date()
                            }else {
                                value = eval(value)
                            }
                        }catch (e){
                            value = ''
                        }
                    }
                }
                $.extend(options,{
                    elem : options['elem'] || select,
                    type : options['type'] || 'datetime',
                    value : options['value']|| value,
                    format : options['format'] || 'yyyy-MM-dd HH:mm:ss',
                    trigger : options['trigger'] || 'click',
                    done : options['done'] || function (value,date,endDate) {
                        if(identify && identify!=''){
                            var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                            var postData = {}//必须设置主键
                            postData[name] = value;
                            if(!mc_verify.verify(value,$(item))){
                                return;
                            }
                            $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                                layer.close(index);
                                if(data.flag){
                                    layer.msg("修改成功！");
                                }else {
                                    layer.msg("修改失败！"+data.desc);
                                    if(doneFailCallback){
                                        doneFailCallback()
                                    }
                                }
                            })
                        }
                    }
                })
                laydate.render(options);
            })
        },
        //绑定文件上传组件
        _bindUpload : function (elem,doneFailCallback) {
            var _t = this;
            $(elem).each(function (index,item) {
                var name = $(item).attr("name")
                var inputName = name.replace("FileUpload","")//一个FileUpload后面有个input 主要是设置input的值
                var options = _t._options(item);
                var inputNameItem = $("input[name='"+inputName+"']");

                $.extend(options,{
                    elem: options['elem'] || $(item),
                    url: options['url'] || 'admin/common_file_rest/upload',
                    method: options['method'] || "post",
                    accept: options['accept'] || "file",//允许上传普通文件 ，去掉则只支持图片类型
                    before: options['before'] || function (obj) {
                        layer.load()
                    },
                    done: options['done'] || function(res, index, upload){
                        layer.closeAll('loading')
                        if(res.flag){
                            var src = res.data.src;
                            inputNameItem.val(src);
                            mc_util.autoSetImgUrl($(".img_"+inputName),src);//图片预览区域
                        }else {
                            layer.alert("上传失败:"+res.desc)
                            if(doneFailCallback){
                                doneFailCallback();
                            }
                        }
                    },
                    error: options['error'] || function(index, upload){
                        layer.closeAll('loading')
                        layer.alert("上传失败，请重试")
                        if(doneFailCallback){
                            doneFailCallback();
                        }
                    }
                })
                upload.render(options);
            })
        },
        _bindSingleSelect2 : function (tableNameRest,$elem,select2ConfigData,selectChangeFailCallback,config) {
            mc_select2.bindTableSingleSelect2(tableNameRest,$elem,select2ConfigData,selectChangeFailCallback,config);
        },
        _bindForeignSelect2 : function (tableNameRest,$elem,select2ConfigData,selectChangeFailCallback,config) {
            mc_select2.bindForeignSelect2ByDomId(tableNameRest,$elem,select2ConfigData,selectChangeFailCallback,config);
        },
        _bindSwitchAjaxUpdate : function (elem,doneFailCallback) {//表格中出现也得用 form.on(switch)监听 因为表格暂时没这个处理方法
            var tableNameRest = this.config.tableNameRest;
            $(elem).each(function (index, item) {
                var name = $(item).attr("name")
                var id = $(item).attr("id")
                var filter = $(item).attr("lay-filter")
                var select = name ? name : id
                form.on('switch('+filter+')', function(domData){
                    var _t = $(this)
                    var identify = _t.attr("data-identify")
                    if(identify && identify!=''){//ajax更新结果
                        var targetChecked = domData.elem.checked;
                        var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                        var postData = {}//必须设置主键
                        var value = targetChecked?1:0
                        postData[select] = value;
                        if(!mc_verify.verify(value,$(item))){
                            return;
                        }
                        $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                            layer.close(index);
                            if(data.flag){
                                _t.prop("checked", targetChecked);
                                layer.msg("修改成功！");
                            }else {
                                layer.msg("修改失败:"+data.desc);
                                _t.prop("checked", !targetChecked);
                            }
                            form.render('checkbox');//这一步必须执行 否则改了不生效
                        })
                    }
                })
            })
        },
        //绑定富文本 并返回校验同步框 供表单提交使用
        _bindEdit : function (elem) {
            var formVerifyEditData = {}
            $(elem).each(function (index,item) {
                var id = $(item).attr("id")
                //创建一个编辑器
                var editIndex = layedit.build(id,{
                    height : 300,
                    uploadImage : {
                        type : "post",
                        url : "admin/common_file_rest/upload?from=layedit"
                    }
                });
                //require 校验富文本编辑框时 同步一下隐藏的textarea值 只有在这里同步才生效 VerifyEdit 参见edit.html lay-verify
                formVerifyEditData[id+"VerifyEdit"] = function(val){
                    layedit.sync(editIndex);
                }
            })
            return formVerifyEditData;
        },
        _options:function(item){
            var name = $(item).attr("name")
            var domRenderConfig = this.iteratorAttr({},item);//dom元素上面的配置
            var formConfig = {}//js控制的配置
            $.extend(true,formConfig,this.config.form);//深拷贝
            var formRenderConfig = formConfig[name] ? formConfig[name].renderConfig : {}
            var options = $.extend(true,domRenderConfig,formRenderConfig)// 以dom属性配置优先 其次读取js配置
            return options;
        },
        //绑定滑块 不建议在列表页使用
        _bindSlider : function (elem) {
            var _t = this
            $(elem).each(function (index,item) {//必须1个个绑定 不支持单利
                var name = $(item).attr("name")
                var inputName = name.replace("Slider","")//一个slider前面有个input 主要是设置input的值
                var options = _t._options(item);
                var inputNameItem = $("input[name='"+inputName+"']");
                var value = inputNameItem.val()
                $.extend(options,{
                    elem : options['elem'] || $(item),
                    value : options['value']|| value,//取对应的输入框
                    change : options['change'] || function (value) {
                        inputNameItem.val(value)
                    }
                })
                slider.render(options);
            })
        },
        //颜色选择器
        _bindColorPicker : function (elem,doneFailCallback) {
            var tableNameRest = this.config.tableNameRest;
            var _t = this;
            $(elem).each(function (index,item) {//必须1个个绑定 不支持单利
                var name = $(item).attr("name")
                var inputName = name.replace("ColorPicker","")//一个ColorPicker前面有个input 主要是设置input的值
                var options = _t._options(item);
                var inputNameItem = $("input[name='"+inputName+"']");
                var value = inputNameItem.val()
                var identify = $(item).attr("data-identify")
                $.extend(options,{
                    elem : options['elem'] || select,
                    format : options['format']|| 'hex',
                    color : options['color']|| value,
                    done : options['done'] || function (color) {
                        $(item).val(color)
                        if(identify && identify!=''){
                            var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                            var postData = {}//必须设置主键
                            postData[name] = color;
                            if(!mc_verify.verify(color,$(item))){
                                return;
                            }
                            $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                                layer.close(index);
                                if(data.flag){
                                    layer.msg("修改成功！");
                                }else {
                                    layer.msg("修改失败！"+data.desc);
                                    if(doneFailCallback){
                                        doneFailCallback()
                                    }
                                }
                            })
                        }
                    }
                })
                colorpicker.render(options);
            })
        },
        //评分
        _bindRate : function (elem,doneFailCallback) {
            var tableNameRest = this.config.tableNameRest;
            var _t = this;
            $(elem).each(function (index,item) {//必须1个个绑定 不支持单利
                var name = $(item).attr("name")
                var inputName = name.replace("Rate","")//一个ColorPicker前面有个input 主要是设置input的值
                var options = _t._options(item);
                var inputNameItem = $("input[name='"+inputName+"']");
                var value = inputNameItem.val()
                var identify = $(item).attr("data-identify")
                
                $.extend(options,{
                    elem : options['elem'] || $(item),
                    length : options['length']|| 5,
                    value : options['value']|| value,
                    theme : options['theme']|| '#FFB800',
                    half : options['half']|| false,
                    text : options['text']|| false,
                    readonly : options['readonly']|| false,
                    choose : options['choose'] || function (val) {
                        inputNameItem.val(val)
                        if(identify && identify!=''){
                            var index = layer.msg('修改中，请稍候',{icon: 16,time:true,shade:0.8});
                            var postData = {}//必须设置主键
                            postData[name] = val;
                            if(!mc_verify.verify(val,$(item))){
                                return;
                            }
                            $.post('admin/'+tableNameRest+'/update/'+identify,postData,function (data) {
                                layer.close(index);
                                if(data.flag){
                                    layer.msg("修改成功！");
                                }else {
                                    layer.msg("修改失败！"+data.desc);
                                    if(doneFailCallback){
                                        doneFailCallback()
                                    }
                                }
                            })
                        }
                    }
                })
                rate.render(options);
            })
        },
        //代码修饰器
        _bindCode : function (elem,doneFailCallback) {
            var _t = this;
            $(elem).each(function (index,item) {//必须1个个绑定 不支持单利
                var name = $(item).attr("name")
                var inputName = name.replace("Code","")//一个ColorPicker前面有个input 主要是设置input的值
                var options = _t._options(item);
                var inputNameItem = $("textarea[name='"+inputName+"']");
                var value = inputNameItem.val()
                
                $(elem).html(value);
                $.extend(options,{
                    elem : elem,
                    title : options['title']|| '',
                    height : options['height']|| '200px',
                    encode : options['encode']|| true,
                    skin : options['skin']|| 'notepad',
                    about : options['about']|| false
                })
                layui.code(options);
            })
        },
        //把layui组件html标签上的配置读取出来然后设置
        iteratorAttr:function (renderConfig,dom) {
            var attrs = dom.attributes;
            for(var i=0;i<attrs.length;i++){
                var attr = attrs[i];
                var name = attr.name;
                var value = attr.value;
                if(name.indexOf("mc-")==0){
                    name = name.replace("mc-attr-",'')
                    name = name.replace("mc-event-",'')
                    value=='true'?value=true:value=value;
                    value=='false'?value=false:value=value;
                    renderConfig[this.htmlAttrNameToTuoFeng(name)]=value
                }
            }
            return renderConfig;
        },
        htmlAttrNameToTuoFeng:function (name) {//userName -> user-name
            var arr = name.split("-")
            var newArr = []
            for(var i in arr){
                if(i!=0){
                    if(arr[i]!=''){
                        newArr.push(this.firstCharToUpLower(arr[i]));
                    }
                }else {
                    newArr.push(arr[i]);
                }
            }
            return newArr.join('');
        },
        firstCharToUpLower:function (name) {//首字母大写
            var arr = name.split("");
            arr[0] = arr[0].toUpperCase();
            return arr.join('')
        },
        _bindLayer:function () {
            var _t = this;
            $(".magicalcoder-layer").each(function (idx, item) {
                $(this).next().hide()//先隐藏起来
                $(this).click(function () {
                    var config = _t.iteratorAttr({elem: $(item)}, item);
                    var type = config.type;
                    if(type+''==1){
                        config.content = $(this).next();
                    }
                    if(config.btn){
                        config.btn = config.btn.split(",")
                    }
                    if(config.area){
                        config.area = config.area.split(",")
                    }
                    layer.open(config)
                })
            })
        }
        ,
        magicalcoderMjeRender:function () {//解析编码操作的配置
            var scriptIdAttrName = "magicalcoder-mje-script-id";
            $("["+scriptIdAttrName+"]").each(function (idx, item) {
                if(!$(this).is("code")){
                    var value = $(this).attr(scriptIdAttrName);
                    var scriptCodeDome = $("["+scriptIdAttrName+"='script-"+value+"']");
                    if(scriptCodeDome.length>0){
                        var scriptJson = scriptCodeDome.html()
                        if(scriptJson!=''){
                            var jsonObj = JSON.parse(scriptJson);
                            var event = jsonObj.event;
                            var executeList = jsonObj.execute;
                            $(this).bind(event,function () {
                                for(var i=0;i<executeList.length;i++){
                                    var execute = executeList[i];
                                    var executeType = execute.executeType;
                                    switch (executeType) {
                                        case 'show':
                                            $(execute.target).show();
                                            break;
                                        case 'hide':
                                            $(execute.target).hide();
                                            break;
                                        case 'toggle':
                                            $(execute.target).toggle();
                                            break;
                                        case 'remove':
                                            $(execute.target).remove();
                                            break;
                                        case 'redirect':
                                            window.location.href = execute.target;
                                            break;
                                        case 'reload':
                                            window.location.reload();
                                            break;
                                        case 'fadeIn':
                                            $(execute.target).fadeIn();
                                            break;
                                        case 'fadeOut':
                                            $(execute.target).fadeOut();
                                            break;
                                        case 'fadeToggle':
                                            $(execute.target).fadeToggle();
                                            break;
                                        case 'slideDown':
                                            $(execute.target).slideDown();
                                            break;
                                        case 'slideUp':
                                            $(execute.target).slideUp();
                                            break;
                                        case 'slideToggle':
                                            $(execute.target).slideToggle();
                                            break;
                                        case 'html':
                                            $(execute.target).html('');
                                            break;
                                        case 'val':
                                            $(execute.target).val('');
                                            break;
                                        case 'submit':
                                            $(execute.target).submit();
                                            break;
                                        default:
                                            console.log("未知类型"+executeType);
                                            break;
                                    }
                                }

                            })
                        }
                    }
                }
            })
        }
    }

    youyaboot_all.mc_layui_component = mc_layui_component;

}();
