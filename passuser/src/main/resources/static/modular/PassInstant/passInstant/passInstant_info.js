initload = function () {
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
 * 初始化密钥管理详情对话框
 */
var PassInstantInfoDlg = {
    passInstantInfoData: {}
};

/**
 * 清除数据
 */
PassInstantInfoDlg.clearData = function () {
    this.passInstantInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PassInstantInfoDlg.set = function (key, val) {
    this.passInstantInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PassInstantInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PassInstantInfoDlg.close = function () {
    parent.layer.close(window.parent.PassInstant.layerIndex);
}

/**
 * 收集数据
 */
PassInstantInfoDlg.collectData = function () {
    this
        .set('passId')
        .set('passName')
        .set('passLength')
        .set('passType')
        .set('passChildfir')
        .set('passChildsec')
        .set('passChildthi')
        .set('passExpiry')
        .set('passCreatetime')
        .set('passUserid');
}

/**
 * 提交添加
 */
PassInstantInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax("/passInstant/add", function (data) {
        Feng.success("添加成功!");
        window.parent.PassInstant.table.refresh();
    }, function (data) {
        PassInstantInfoDlg.close();
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.passInstantInfoData);
    PassInstantInfoDlg.close();
    ajax.start();

}

/**
 * 提交修改
 */
PassInstantInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/passInstant/update", function (data) {
        Feng.success("修改成功!");
        PassInstantInfoDlg.close();
        window.parent.PassInstant.table.refresh();

    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.passInstantInfoData);
    ajax.start();
}








