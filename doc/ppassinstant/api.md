
# p_pass_instant p_pass_instant模块 API
## 1.1 查询p_pass_instant详情

> **描述**：根据密钥UUID查询p_pass_instant详情。

> **url**：/admin/p_pass_instant_rest/get/{passId}
>
> **method**：GET

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    | id       | Integer     | 密钥UUID，必填 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |
| 4        | data          | object   | p_pass_instant详情对象            |
|          |               |          |                         |
| 字段解释 |               |          |                         |
| 1 | passId              | Integer          | 密钥UUID |
| 2 | passName              | String          | 密钥名称 |
| 3 | passLength              | Integer          | 密钥长度 |
| 4 | passType              | String          | 密钥类型 |
| 5 | passChildfir              | String          | 第一子密钥 |
| 6 | passChildsec              | String          | 第二子密钥 |
| 7 | passChildthi              | String          | 第三子密钥 |
| 8 | passExpiry              | Date          | 密钥有效期 |
| 9 | passCreatetime              | Date          | 密钥上传时间 |
| 10 | passUserid              | Integer          | 密钥使用者 |

## 1.2 保存p_pass_instant详情

> **描述**：保存p_pass_instant详情。

> **url**：/admin/p_pass_instant_rest/save
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1| passId  | Integer  | 密钥UUID |
| 2| passName  | String  | 密钥名称 |
| 3| passLength  | Integer  | 密钥长度 |
| 4| passType  | String  | 密钥类型 |
| 5| passChildfir  | String  | 第一子密钥 |
| 6| passChildsec  | String  | 第二子密钥 |
| 7| passChildthi  | String  | 第三子密钥 |
| 8| passExpiry  | Date  | 密钥有效期 |
| 9| passCreatetime  | Date  | 密钥上传时间 |
| 10| passUserid  | Integer  | 密钥使用者 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |

## 1.3 更新p_pass_instant详情

> **描述**：根据密钥UUID更新p_pass_instant的任意属性值，确保要更新的参数不能为null。

> **url**：/admin/p_pass_instant_rest/update/{passId}
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1| passId  | Integer  | 密钥UUID |
| 2| passName  | String  | 密钥名称 |
| 3| passLength  | Integer  | 密钥长度 |
| 4| passType  | String  | 密钥类型 |
| 5| passChildfir  | String  | 第一子密钥 |
| 6| passChildsec  | String  | 第二子密钥 |
| 7| passChildthi  | String  | 第三子密钥 |
| 8| passExpiry  | Date  | 密钥有效期 |
| 9| passCreatetime  | Date  | 密钥上传时间 |
| 10| passUserid  | Integer  | 密钥使用者 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |

## 1.4 删除一条p_pass_instant记录

> **描述**：根据密钥UUID删除一条p_pass_instant记录。

> **url**：/admin/p_pass_instant_rest/delete/{passId}
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    | id       | Integer     | 密钥UUID，必填 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |


## 1.5 批量删除多条p_pass_instant记录

> **描述**：根据多个密钥UUID删除多条p_pass_instant记录。

> **url**：/admin/p_pass_instant_rest/batch_delete
>
> **method**：POST

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    | ids[]       | Integer     | 密钥UUID，必填 |
| 2    | ids[]       | Integer     | 密钥UUID，必填 |
| ...    | ids[]       | Integer     | 密钥UUID，必填 |
| n    | ids[]       | Integer     | 密钥UUID，必填 |

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |


## 1.6 分页查询p_pass_instant

> **描述**：分页查询p_pass_instant。

> **url**：/admin/p_pass_instant_rest/page
>
> **method**：GET

> **输入**

| 序号 | 字段名称 | 字段类型 | 字段描述     |
| ---- | -------- | -------- | ------------ |
| 1    |page      |int       |第几页 [1,n)   |
| 2    |limit      |int       |每页多少条 [0,100)   |
| 3    |safeOrderBy      |int       | 排序 例如 数据库字段名称 desc或asc   |
                |4|passIdFirst|   Integer   |密钥UUID|
                |5|passNameFirst|   String   |密钥名称|
                |6|passLengthFirst|   Integer   |密钥长度|
                |7|passTypeFirst|   String   |密钥类型|
                |8|passExpiryFirst|   Date   |密钥有效期|
                |9|passExpirySecond|   Date   |密钥有效期|
                |10|passCreatetimeFirst|   Date   |密钥上传时间|
                |11|passCreatetimeSecond|   Date   |密钥上传时间|
                |12|passUseridFirst|   Integer   |密钥使用者|

> **输出**

| 序号     | 字段名称      | 字段类型 | 字段描述                |
| -------- | ------------- | -------- | ----------------------- |
| 1        | flag          | boolean  | 接口调用，成功/失败标识 |
| 2        | code          | int      | 错误码，成功时=0        |
| 3        | desc          | string   | 结果描述                |
| 4        | data          | array   | p_pass_instant详情数组对象            |
|          |               |          |                         |
| 字段解释 |               |          |                         |
| 1 | passId              | Integer          | 密钥UUID |
| 2 | passName              | String          | 密钥名称 |
| 3 | passLength              | Integer          | 密钥长度 |
| 4 | passType              | String          | 密钥类型 |
| 5 | passChildfir              | String          | 第一子密钥 |
| 6 | passChildsec              | String          | 第二子密钥 |
| 7 | passChildthi              | String          | 第三子密钥 |
| 8 | passExpiry              | Date          | 密钥有效期 |
| 9 | passCreatetime              | Date          | 密钥上传时间 |
| 10 | passUserid              | Integer          | 密钥使用者 |
