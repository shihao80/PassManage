/**
 * 密钥管理管理初始化
 */
var PassInstant = {
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    class : "layui-table",
};

PassInstant.initload = function () {
    var $ax = function (url, success, error) {
        this.url = url;
        this.type = "post";
        this.data = {};
        this.dataType = "json";
        this.async = false;
        this.success = success;
        this.error = error;
    };
    $ax.prototype = {
        start: function () {
            var me = this;

            if (this.url.indexOf("?") == -1) {
                this.url = this.url + "?jstime=" + new Date().getTime();
            } else {
                this.url = this.url + "&jstime=" + new Date().getTime();
            }

            $.ajax({
                type: this.type,
                url: this.url,
                dataType: this.dataType,
                async: this.async,
                data: this.data,
                beforeSend: function (data) {

                },
                success: function (data) {
                    me.success(data);
                },
                error: function (data) {
                    me.error(data);
                }
            });
        },

        set: function (key, value) {
            if (typeof key == "object") {
                for (var i in key) {
                    if (typeof i == "function")
                        continue;
                    this.data[i] = key[i];
                }
            } else {
                this.data[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
            }
            return this;
        },

        setData: function (data) {
            this.data = data;
            return this;
        },

        clear: function () {
            this.data = {};
            return this;
        }
    };

    window.$ax = $ax;
}
/**
 * 下载密钥
 */
PassInstant.downloadPassInstant = function(thisObj) {
    var passId = $(thisObj).parents("tr").find(".laytable-cell-1-0-0").text();
    //提交信息
    var url = "/passInstant/download/"+passId;
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.appendTo('body').submit().remove();
}
/**
 * 更新密钥
 */
PassInstant.updatePassInstant = function(thisObj) {
    var passId = $(thisObj).parents("tr").find(".laytable-cell-1-0-0").text();
    //提交信息
    var ajax = new $ax("/passInstant/update/"+passId, function () {
        Feng.success("更新成功!");
        window.parent.PassInstant.table.refresh();
    }, function (data) {
        Feng.error("更新失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.passInstantInfoData);
    ajax.start();
}
/**
 * 初始化表格的列
 */
PassInstant.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '密钥UUID', field: 'passId', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥名称', field: 'passName', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥长度', field: 'passLength', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥类型', field: 'passType', visible: true, align: 'center', valign: 'middle'},
            {title: '第一子密钥', field: 'passChildfir', visible: true, align: 'center', valign: 'middle'},
            {title: '第二子密钥', field: 'passChildsec', visible: true, align: 'center', valign: 'middle'},
            {title: '第三子密钥', field: 'passChildthi', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥有效期', field: 'passExpiry', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥上传时间', field: 'passCreatetime', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥用户', field: 'passUserid', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PassInstant.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PassInstant.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加密钥管理
 */
PassInstant.openAddPassInstant = function () {
    var index = layer.open({
        type: 2,
        title: '添加密钥管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/passInstant/passInstant_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看密钥管理详情
 */
PassInstant.openPassInstantDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '密钥管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/passInstant/passInstant_update/' + PassInstant.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除密钥管理
 */
PassInstant.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/passInstant/delete", function (data) {
            Feng.success("删除成功!");
            PassInstant.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("passInstantId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询密钥管理列表
 */
PassInstant.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PassInstant.table.refresh({query: queryData});
};

initPass = function () {
    var defaultColunms = PassInstant.initColumn();
    var table = new BSTable(PassInstant.id, "/passInstant/list", defaultColunms);
    table.setPaginationType("client");
    PassInstant.table = table.init();
    PassInstant.initload();
}
