表结构：
    字典表：dict
    功能：用于常用全局参数配置共享 系统配置 一组类目等
    字段：
        code:保证唯一 一般是要保存在各个库里的值 友好方便看 而不用id
        name:要对外展示的友好值
        dictCategory:字段大类 分组概念 用来聚类code属于同一类
    注意：
    1 普通下拉框配置 不建议使用字典表 会限制前端发挥
    2 可以配置一些类目等
    3 可以配置一些全局常量 比如一些开关

    调用方式：
    1 注入
    @Resource
    DictService dictService;
    2 调用  以下2个接口是有缓存的 600秒
    dictService.getCacheDictList(dictCategory)
    dictService.getCacheDict(code)
