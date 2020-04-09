/**
* 业务树组件
 * 请根据您自己的业务结构 稍微改造下 init myDataToZtreeData ztreeDataToMyData方法即可
* by www.magicalcoder.com
 * 如何初始化复选框树 new BaseTree({treeId:"cityTree",chkboxType:{ "Y" : "ps", "N" : "ps" }})
 * 然后初始化单选按钮树 new BaseTree({treeId:"cityTree",chkStyle:"radio",radioType:"all",defaultSelectIds: [entity.cityStructureId]})
 * 页面请引入
 *  样式<link rel="stylesheet" th:href="@{/assets/magicalcoder/v103/ztree3/css/zTreeStyle/zTreeStyle.css}" media="all" />
 *  js      <script>
             var jQuery = layui.jquery;
         </script>
         <script type="text/javascript"  th:src="@{assets/magicalcoder/v103/ztree3/js/jquery.ztree.all.js}"></script>
         <script type="text/javascript"  th:src="@{/assets/magicalcoder/v103/crud/component/mc-base-tree.js}"></script>

 详情页保存：

 html:
 <div class="layui-row">
     <div class="layui-col-xs6 layui-col-md2">
        <ul id="departmentTree" class="ztree"></ul>
     </div>
     <div class="layui-col-xs6 layui-col-md10">
        <form></form>
     </div>
 </div>

js:
 var treeCallback = {
            //我的数据转换成树
            myDataToZtreeData:function (dbEntity) {
                return {id:dbEntity.id,pId:dbEntity.parentId,open:false,name:dbEntity.name,deptNo:dbEntity.deptNo};
            },
            //树的数据转换成我的数据
            ztreeDataToMyData:function (ztreeNode) {
                return {id:ztreeNode.id,name:ztreeNode.name,parentId:ztreeNode.pId,open:ztreeNode.open,deptNo:ztreeNode.deptNo};
            }
        };

 var departmentTree ;
 if(entity && entity!=null){
                            departmentTree = new BaseTree({
                                treeId:"departmentTree",
                                chkStyle:"radio",
                                radioType:"all",
                                defaultSelectIds: [entity.deptId],
                                ajaxUrl:"admin/system_mbyl_department_rest/tree_data",
                                callback: treeCallback
                            })
                            ....
                            }

 else {//新增操作

                departmentTree = new BaseTree({
                    treeId:"departmentTree",
                    chkStyle:"radio",
                    radioType:"all",
                    ajaxUrl:"admin/system_mbyl_department_rest/tree_data",
                    callback: treeCallback
                })
...
}
 //表单校验 在_bindFormDom方法
 form.on("submit(save)",function(data){
                //看看选中树的值
                var treeDate = departmentTree.getSingleRadioSelectNodeData();
                if(treeDate.length>0){
                    var node = treeDate[0];
                    $("[name='deptId']").val(node.id)
                    data.field.deptId=node.id;
                }else {
                    layer.msg("请选择所属部门")
                    return false;
                }

}


列表页查询：
html:class="layui-input"不能少
 <input type="hidden" class="layui-input" data-name="deptIds" name="deptIds" placeholder="部门">

 js
 var departmentTree = new BaseTree({
        treeId:"departmentTree",
        chkboxType:{ "Y" : "ps", "N" : "ps" },
        ajaxUrl:"admin/system_mbyl_department_rest/tree_data",
        callback:{
            //我的数据转换成树
            myDataToZtreeData:function (dbEntity) {
                return {id:dbEntity.id,pId:dbEntity.parentId,open:false,name:dbEntity.name,deptNo:dbEntity.deptNo};
            },
            //树的数据转换成我的数据
            ztreeDataToMyData:function (ztreeNode) {
                return {id:ztreeNode.id,name:ztreeNode.name,parentId:ztreeNode.pId,open:ztreeNode.open,deptNo:ztreeNode.deptNo};
            }
        }
    })
 //搜索
 $(".search_btn").click(function(){
                var data = departmentTree.getFullCheckedNodesData();
                var departmentIds = []
                for(var i=0;i<data.length;i++){
                    departmentIds.push(data[i].id);
                }
                $("[name='deptIds']").val(departmentIds.join(","));
                _t.layTable().refreshTableFromPageOne();
            });
java端
 deptIds = coverBlankToNull(deptIds);
 if(deptIds!=null){
            deptIds = ","+deptIds+",";//查询精准
        }
 query.put("deptIds",deptIds);

 sqlmap:
 <!-- 扩展查询 这是个极其高效的查询手段 而不用in语句--> oracle做法 不同数据库更换不同concat语句
 <if test="deptIds!=null ">
 AND #{deptIds}  LIKE '%,' || DEPT_ID || ',%'
 </if>
 *
*/
var $ = layui.jquery;
var layer = layui.layer;
function BaseTree(params) {
    this.params = {
        treeId:"",//页面放置一个 <ul id="cityTree" class="ztree"></ul>标签即可
        chkboxType:"",//{ "Y" : "ps", "N" : "ps" } 级联
        chkStyle:"",//radio
        radioType:"",//all level
        defaultSelectIds:[],//默认选择的id
        ajaxUrl:"",//初始化的数据的url
        ajaxParams:function(){//动态获取入参的
            return {date:new Date().getTime()}
        },
        callback:{
            //我的数据转换成树
            myDataToZtreeData:function (dbEntity) {
                return {id:dbEntity.id,pId:dbEntity.parentId,open:false,name:dbEntity.cityName,myAttr:dbEntity.myAttr};
            },
            //树的数据转换成我的数据
            ztreeDataToMyData:function (ztreeNode) {
                return {id:ztreeNode.id,cityName:ztreeNode.name,parentId:ztreeNode.pId,open:ztreeNode.open,myAttr:ztreeNode.myAttr};
            },
            /*点击节点回调事件*/
            checkNode:function () {

            }
        }
    }
    $.extend(true,this.params,params);
    this.baseTreeObj = null;
    this.treeSetting = null;
    this.settings();
    this.render();
}

BaseTree.prototype.render = function(){
    this.init();
}

/**
 * 把我的数据转换成ztree的数据
 * @param dbEntity
 * @returns {{id: number, pId: number, open: boolean, name: *, myAttr: 自定义数据}}
 */
BaseTree.prototype.myDataToZtreeData = function(dbEntity){
    return this.params.callback.myDataToZtreeData(dbEntity);
    // return {id:parseInt(dbEntity.id),pId:parseInt(dbEntity.parentId),open:false,name:dbEntity.cityName,myAttr:dbEntity.myAttr};
}
/**
 * 把ztree数据转换成我的数据库数据
 * @param ztreeNode
 * @returns {{id: *, cityName: *, parentId: number | *, open: *, myAttr: *}}
 */
BaseTree.prototype.ztreeDataToMyData = function(ztreeNode){
    return this.params.callback.ztreeDataToMyData(ztreeNode);
    // return {id:ztreeNode.id,cityName:ztreeNode.name,parentId:ztreeNode.pId,open:ztreeNode.open,myAttr:ztreeNode.myAttr};
}

BaseTree.prototype.settings = function () {
    var _t = this;
    this.treeSetting = {
        check: {
            enable: true,
            /*chkboxType : { "Y" : "ps", "N" : "ps" },*/
            /*chkStyle: "radio",
            radioType:"all"*/
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: function (e, treeId, treeNode, clickFlag) {
                _t.baseTreeObj.checkNode(treeNode, !treeNode.checked, true);
                _t.params.callback.checkNode()
            },
            onCheck:function (event, treeId, treeNode) {
                _t.params.callback.checkNode()
            }
        }
    };

    if(this.params.chkboxType!=''){
        this.treeSetting.check.chkboxType = this.params.chkboxType;
    }
    if(this.params.chkStyle!=''){
        this.treeSetting.check.chkStyle = this.params.chkStyle;
    }
    if(this.params.radioType!=''){
        this.treeSetting.check.radioType = this.params.radioType;
    }


}

BaseTree.prototype.dataAdapter = function(dbData){
    var defaultSelectIdMap = {}
    for(var i =0;i<this.params.defaultSelectIds.length;i++){
        defaultSelectIdMap[this.params.defaultSelectIds[i]+""] = "";
    }

    var zNodes = []
    if(typeof dbData != 'undefined'){
        for(var i=0;i<dbData.length;i++){
            var city = dbData[i];
            var item = this.myDataToZtreeData(city)
            if(city.nodeTopclass=='1' ){
                item.open = true;
            }
            var checked = typeof defaultSelectIdMap[item.id+""] != 'undefined';
            if(checked){
                item.checked = checked;
                item.open = true;
            }
            zNodes.push(item);
        }
    }
    return zNodes;
}
/**
 * ajax获取管理后台城市表的所有数据 请保证是个pid,parentId的表 直接简单返回list即可 无需嵌套的结构 适当更换url成您自己的
 */
BaseTree.prototype.init = function () {
    var _t = this;
    var ajaxParams = this.params.ajaxParams();
    ajaxParams._date = new Date().getTime();
    $.getJSON(_t.params.ajaxUrl,ajaxParams,function (data) {
        if(data.flag){
            var list = data.data;
            $.fn.zTree.init($("#"+_t.params.treeId), _t.treeSetting, _t.dataAdapter(list));
            _t.baseTreeObj = $.fn.zTree.getZTreeObj(_t.params.treeId);
        }else {
            layer.msg(data.desc)
        }
    })

}

BaseTree.prototype.destroy = function(){
    $.fn.zTree.destroy(this.params.treeId);
}
//重建这棵树
BaseTree.prototype.refresh = function(){
    this.destroy();
    this.init();
}

/*如果是一个radio*/
BaseTree.prototype.getSingleRadioSelectNodeAndChilrenData = function () {
    var radioNodes = this.baseTreeObj.getCheckedNodes(true);
    if(radioNodes!=null && typeof radioNodes !='undefined'){
        var node = radioNodes[0];
        return this.baseTreeObj.transformToArray(node);
    }

    return [];
}
/*如果是radio 获取选中的这些radio的数据*/
BaseTree.prototype.getSingleRadioSelectNodeData = function () {
    var data = []
    var radioNodes = this.baseTreeObj.getCheckedNodes(true);
    if(radioNodes!=null && typeof radioNodes !='undefined'){
        for(var i=0;i<radioNodes.length;i++){
            var node = radioNodes[i];
            data.push(this.ztreeDataToMyData(node))
        }
    }
    return data;
}

/*如果是checkbox 获取选择的所有节点数据*/
BaseTree.prototype.getFullCheckedNodesData = function () {
    var data = []
    var checkedNodes = this.baseTreeObj.getCheckedNodes(true);
    if(checkedNodes!=null && typeof checkedNodes !='undefined'){
        for(var i=0;i<checkedNodes.length;i++){
            var node = checkedNodes[i];
            var status = node.getCheckStatus();
            if(status.checked && !status.half){
                data.push(this.ztreeDataToMyData(node))
            }
        }
    }
    return data;
}

