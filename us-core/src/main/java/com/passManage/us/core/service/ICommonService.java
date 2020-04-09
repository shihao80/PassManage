package com.passManage.us.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这里提供了很多好用的单表增删改查接口，可以在子业务直接调用
 * @param <E> 实体类类型
 * @param <P> 主键类型
 */
public interface ICommonService<E,P> {
    /**
     * 根据主键获取实体
     * @param id 主键
     * @return
     */
    E getModel(P id);

    /**
     * 根据主键获取实体，不查询ignoreFields字段
     * @param id
     * @param ignoreFields 被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    E getModelIgnore(P id, String... ignoreFields);

    /**
     * 根据主键获取实体，只查询notIgnoreFields字段
     * @param id
     * @param notIgnoreFields  不被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    E getModelNotIgnore(P id, String... notIgnoreFields);

    /**
     * 调用mybatis selectOne 如果查询返回超过1条 就会发生异常 请自行处理
     * @param query 入参 调用MapUtil构造
     * @return
     */
    E selectOneModelWillThrowException(Map<String, Object> query);

    /**
     * 调用mybatis selectOne 如果查询返回超过1条 就会发生异常 请自行处理 不查询ignoreFields字段
     * @param query query 入参 调用MapUtil构造
     * @param ignoreFields  被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    E selectOneModelWillThrowExceptionIgnore(Map<String, Object> query, String... ignoreFields);

    /**
     * 调用mybatis selectOne 如果查询返回超过1条 就会发生异常 请自行处理 只查询ignoreFields字段
     * @param query 入参 调用MapUtil构造
     * @param notIgnoreFields
     * @return
     */
    E selectOneModelWillThrowExceptionNotIgnore(Map<String, Object> query, String... notIgnoreFields);
    /**
     * 获取满足条件的第一条数据
     * @param query 入参 调用MapUtil构造
     * @return
     */
    E selectFirstModel(Map<String, Object> query);

    /**
     * 获取满足条件的第一条数据
     * @param query
     * @param ignoreFields  被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    E selectFirstModelIgnore(Map<String, Object> query, String... ignoreFields);

    /**
     * 获取满足条件的第一条数据
     * @param query
     * @param notIgnoreFields  不被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    E selectFirstModelNotIgnore(Map<String, Object> query, String... notIgnoreFields);
    /**
     * 查询实体集合
     * @param query :分页参数 start limit2个放入query
     * @return
     */
    List<E> getModelList(Map<String, Object> query);

    /**
     * 查询实体集合
     * @param query :分页参数 start limit2个放入query
     * @param ignoreFields  被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    List<E> getModelListIgnore(Map<String, Object> query, String... ignoreFields);

    /**
     * 查询实体集合
     * @param query :分页参数 start limit2个放入query
     * @param notIgnoreFields 不被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    List<E> getModelListNotIgnore(Map<String, Object> query, String... notIgnoreFields);

    /**
     * 查询实体集合
     * @param idList 主键集合
     * @return
     */
    List<E> getModelInList(Set<P> idList);

    /**
     * 查询实体集合
     * @param idList 主键集合
     * @param ignoreFields   被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    List<E> getModelInListIgnore(Set<P> idList, String ... ignoreFields);

    /**
     * 查询实体集合
     * @param idList 主键集合
     * @param notIgnoreFields 不被忽略的字段 比如 id,goodsName,createTime
     * @return
     */
    List<E> getModelInListNotIgnore(Set<P> idList, String ... notIgnoreFields);

    /**
     * 查询实体集合的个数
     * @param query
     * @return
     */
    int getModelListCount(Map<String, Object> query);

    /**
     * 保存实体 保证Model 主键为空 完全按照实体值进行插入 null也会插入数据库字段
     * @param model
     * @return 成功执行的sql条数
     */
    int insertModel(E model);
    /**
     * 保存实体 保证Model 主键为空  如果属性为null则会插入数据库默认值
     * @param model
     * @return null
     */
    int insertModelWithoutNull(E model);
    /**
     *  批量插入实体，保证list中的实体 主键为空 完全按照实体值进行插入 null也会插入数据库字段 批量自动把实体主键设置
     * @param list
     * return 成功执行的sql条数
     */
    int batchInsertModel(List<E> list);
    /**
     *  批量插入实体，保证list中的实体 主键为空 如果属性为null则会插入数据库默认值 批量自动把实体主键设置
     * @param list
     * return  成功执行的sql条数
     */
    int batchInsertModelWithoutNull(List<E> list);
    /**
     * 智能保存实体
     * @param model
     * @return 成功执行的sql条数
     */
    int replaceModel(E model);
    /**
     * 智能保存实体
     * @param model
     * @return 成功执行的sql条数
     */
    int replaceModelWithoutNull(E model);
    /**
     *  智能批量保存实体，批量自动把实体主键设置
     * @param list
     * @retrun 成功执行的sql条数
     */
    int batchReplaceModel(List<E> list);
    /**
     *  智能批量保存实体 批量自动把实体主键设置
     * @param list
     * @retrun 成功执行的sql条数
     */
    int batchReplaceModelWithoutNull(List<E> list);
    /**
     * 更新实体 null值会字段设置数据库默认字段
     * 保证实体中的主键不为空
     * @param model
     * @return 成功执行的sql条数
     */
    int updateModel(E model);
    /**
     * 更新实体 null字段忽略更新
     * 保证实体中的主键不为空
     * @param model
     * @return 成功执行的sql条数
     */
    int updateModelWithoutNull(E model);
    /**
     * 批量更新实体 保证list中的实体 主键不为空
     * @param list
     * @return 成功执行的sql条数
     */
    int batchUpdateModel(List<E> list);
    /**
     * 批量更新实体 保证list中的实体 主键不为空
     * @param list
     * @return 成功执行的sql条数
     */
    int batchUpdateModelWithoutNull(List<E> list);
    /**
     * 根据主键删除实体
     * @param id
     * @return 成功执行的sql条数
     */
    int deleteModel(P id);
    /**
     * 删除实体集合 使用不当 就会造成整个表清空，务必保证query的字段对应的值存在
     * @param query 如果字段值不为空 将作为查询条件
     * @return 成功执行的sql条数
     */
    int deleteModel(Map<String, Object> query);
    /**
     * 根据主键list 批量删除实体
     * @param idList
     * @return 成功执行的sql条数
     */
    int deleteModel(Set<P> idList);
    /**
     * 清空实体表
     */
    int truncateModel();


    //已重构名称请不要再使用下面的方法
    @Deprecated
    int batchDeleteModel(Set<P> idList);
    @Deprecated
    int deleteModelList(Map<String, Object> query);

}
