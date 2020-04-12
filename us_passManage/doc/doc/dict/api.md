
# dict 字典模块 API
## 1.1 查询字典详情

> **描述**：根据主键查询字典详情。

> **url**：/admin/dict_rest/get/{id}
>
> **method**：GET

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    | id       | Long     | 主键，必填 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |
| 4        | data          | object   | 字典详情对象            |
|          |               |          |                         |
| 字段解释 |               |          |                         |
| 1 | id              | Long          | 主键 |
| 2 | dictCategory              | String          | 字典大类 |
| 3 | code              | String          | 编码 |
| 4 | name              | String          | 名称 |
| 5 | dictDescription              | String          | 描述 |
| 6 | parentId              | Long          | 所属父类 |
| 7 | createTime              | Timestamp          | 创建时间 |
| 8 | updateTime              | Timestamp          | 更新时间 |
| 9 | orderNo              | Byte          | 序号 |

## 1.2 保存字典详情

> **描述**：保存字典详情。

> **url**：/admin/dict_rest/save
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1| id  | Long  | 主键 |
| 2| dictCategory  | String  | 字典大类 |
| 3| code  | String  | 编码 |
| 4| name  | String  | 名称 |
| 5| dictDescription  | String  | 描述 |
| 6| parentId  | Long  | 所属父类 |
| 7| createTime  | Timestamp  | 创建时间 |
| 8| updateTime  | Timestamp  | 更新时间 |
| 9| orderNo  | Byte  | 序号 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |

## 1.3 更新字典详情

> **描述**：根据主键更新字典的任意属性值，确保要更新的参数不能为null。

> **url**：/admin/dict_rest/update/{id}
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1| id  | Long  | 主键 |
| 2| dictCategory  | String  | 字典大类 |
| 3| code  | String  | 编码 |
| 4| name  | String  | 名称 |
| 5| dictDescription  | String  | 描述 |
| 6| parentId  | Long  | 所属父类 |
| 7| createTime  | Timestamp  | 创建时间 |
| 8| updateTime  | Timestamp  | 更新时间 |
| 9| orderNo  | Byte  | 序号 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |

## 1.4 删除一条字典记录

> **描述**：根据主键删除一条字典记录。

> **url**：/admin/dict_rest/delete/{id}
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    | id       | Long     | 主键，必填 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |


## 1.5 批量删除多条字典记录

> **描述**：根据多个主键删除多条字典记录。

> **url**：/admin/dict_rest/batch_delete
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    | ids[]       | Long     | 主键，必填 |
| 2    | ids[]       | Long     | 主键，必填 |
| ...    | ids[]       | Long     | 主键，必填 |
| n    | ids[]       | Long     | 主键，必填 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |


## 1.6 分页查询字典

> **描述**：分页查询字典。

> **url**：/admin/dict_rest/page
>
> **method**：GET

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    |page      |int       |第几页 [1,n)   |
| 2    |limit      |int       |每页多少条 [0,100)   |
| 3    |safeOrderBy      |int       | 排序 例如 数据库字段名称 desc或asc   |
                |4|idFirst|   Long   |主键|
                |5|codeFirst|   String   |编码|

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |
| 4        | data          | array   | 字典详情数组对象            |
|          |               |          |                         |
| 字段解释 |               |          |                         |
| 1 | id              | Long          | 主键 |
| 2 | dictCategory              | String          | 字典大类 |
| 3 | code              | String          | 编码 |
| 4 | name              | String          | 名称 |
| 5 | dictDescription              | String          | 描述 |
| 6 | parentId              | Long          | 所属父类 |
| 7 | createTime              | Timestamp          | 创建时间 |
| 8 | updateTime              | Timestamp          | 更新时间 |
| 9 | orderNo              | Byte          | 序号 |
