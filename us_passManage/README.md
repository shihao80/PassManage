当前版本 - v101

us是us系列框架，默认数据库引擎采用mysql,如需要其他数据库，请至www.magicalcoder.com下载对应引擎
配置模板时 请设置 模板版本=us_mysql_xxx

相关技术栈：
后端：springboot2.x +mybatis+redis+mysql
前端：layui2.4.5+jquery
工具：magicalcoder前后端代码生成器+magicaldrag前端布局器

1 如何启动
    管理后台：us-admin
        
        安装环境
            框架需要redis服务，请自行下载或者双击project/redis/redis-启动.bat
        配置环境
           us\us-admin\src\main\resources\application-publish.yml
           请根据实际情况配置 mysql redis
        导入数据库
            us\us.sql 导入上面配置的数据库
        运行
            us\us-admin\src\main\java\com\magicalcoder\us\admin\YouyaBootAdminApplication.java
            main函数即可启动
        
        也可以打成jar启动也行
        方式1 java -jar us-admin.jar
        方式2 nohup>nohupGps java -jar us-admin.jar --spring.profiles.active=deploy 2>&1 &
        
        启动成功后 浏览器访问
        http://localhost:18080/us-admin
            默认 admin/admin登陆
        
2 声明
    us 为开源项目，会持续不断的更新，在使用过程中遇到问题 欢迎联系作者 QQ 799374340

3 常见问题：
    1 验证码无法使用 null报错 解决方法：请更换最新jdk1.8版本 建议多尝试几个版本的 即可解决

无缝简单升级方法 
1 升级java 
    更新us最新包，把us-core覆盖您本地，前提是您未修改us-core已经存在的类
2  升级js html
    更新us最新包，把
     us-admin\src\main\resources\static\assets\magicalcoder\vxxx 
     us-admin\src\main\resources\templates\magicalcoder\vxxx
     覆盖您本地
3 升级模板
    magicalcoder软件包soft\vms\us_xxx会发布最新版本模板，更换至最新模板即可

当然如果您不希望进行任何升级 无需改动任何内容

版本更新记录：您可以根据记录来升级您当前的版本 查看版本在 /us_mysql/README.md 如果不想升级 请做好老版本vms下的模板备份
#v101 - 2019/1/15
## 1 支持excel导出功能 
## 2 重构js模块，之前版本限制太多，现在完全开放，更大程度满足扩展性
## 变更文件记录  您可以根据此文件来进行手动更新老版本 使用idea文件差异（Ctrl选中2文件夹 Ctrl+D）对比即可完成更新
        modified:   pom.xml
        modified:   us-core/pom.xml
        modified:   us-core/src/main/java/com/magicalcoder/us/core/common/dto/KeyValue.java
        modified:   us-core/src/main/java/com/magicalcoder/us/core/service/CommonRestController.java
        new file:   us-core/src/main/java/com/magicalcoder/us/core/utils/POIUtil.java
        new folder: us-admin/src/main/resources/templates/magicalcoder/v101/
        new folder: us-admin/src/main/resources/static/assets/magicalcoder/v101/

## 开发文档
    ##1 如何自定义新页面 并配置跳转规则
    我们拿goods_category表来讲解
    templates/goodscategory/goodscategory-edit.html
    templates/goodscategory/goodscategory-list.html
    注意goodscategory文件夹要小写
    
    URL规则/admin/page_v2/{tableName}/{editOrList}
    跳转规则由 AdminRmpController.mapping来定制规则
        return {tableName}.replaceAll("_","") +"/"+ {tableName}.replaceAll("_","") +"-"+ {editOrList};
    示例：/admin/page_v2/goods_category/list
    goods_category中的_会被替换成空 然后映射到对应文件夹 和对应是页面

##开发技巧
    修改静态文件 如果不重启 立即生效：使用idea开发 修改完 按快捷键 ctrl+F9 即可完成热部署，此方法仅限静态资源热部署 修改java不一定成功
    层级太多不好调试：由于采用iframe方式，确实有这种问题，您可以直接在浏览器打开 例如http://localhost:18080/us-admin/admin/page_v2/xxx/list 这种方式开发单独的页面 会更加清晰
