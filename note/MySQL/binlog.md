- MySQL有很多日志比如错误日志, 更新日志(老版本MySQL), 二进制日志(binlog), 查询日志, 慢查询日志。在默认情况下系统仅仅会打开错误日志。
关闭了其他的日志以达到尽可能减少IO损耗, 提高系统性能的目的。

- **什么是binlog**  
  **二进制日志, 记录对数据发生或潜在发生更改的SQL语句, 并以二进制形式保存在磁盘中。**  
  在重要一点的实际的应用场景中, 都需要打开binlog开关。因为这是MySQL很多存储引擎进行**增量备份**的基础。是MySQL实现复制的基本条件。
  比如说两个集群, 主集群对应用程序提供服务, 需要不断地把数据同步到从集群中(master-->slave), 这个过程就是通过 binlog 去实现的。
  
- **binlog的作用是什么**  
  **两个主要作用**    
   1.复制: MySQL的master-slave协议, 让slave可以通过监听binlog实现数据复制, 达到数据一致性的目的。  
   2.恢复和审计: 通过mysqlbinlog工具恢复数据。
  
- **binlog相关的变量**

  |变量名称|变量含义|相关语句|  
  |:----  |:----  |:----  |  
  |log_bin|binlog开关|查看变量: SHOW VARIABLES LIKE 'log_bin';|  
  |binlog_format|binlog日志格式|查看变量: SHOW VARIABLES LIKE 'binlog_format';| 
     
- **binlog日志的三种格式**  

  |格式类型|格式特性|
  |:----|:----|
  |ROW|仅保存被修改细节, 不记录SQL语句上下文相关信息|
  |STATEMENT|每一条会修改数据的SQL都会记录到binlog中|
  |MIXED|以上两种level混合使用|

    优点:  
    　　ROW: binlog中可以不记录执行的SQL语句的上下文相关信息, 即可以不记录具体执行的SQL语句是什么, 只记录**哪条数据被修改成什么样子**,
    不会因为某些语法(比如函数, 触发器...)复制而出现问题。  
    　　STATEMENT: 每一条修改数据的SQL会记录到binlog中, slave端再根据sql语句进行重现。不会产生大量的binlog数据。
    　　MIXED: ROW和STATEMENT的两种level的混合使用。会根据不同的情况使用ROW模式或STATEMENT模式, 保证binlog的记录可以被正确的表达。  

    缺点:  
    　　ROW: **每行数据的修改**的修改都会记录下来, update这种语句也会被记录下来。即更新多少条语句就会产生多少个事件。最终导致binlog文件
    过大, 由于binlog最终会存储为本地文件进行保存。而数据的复制需要通过网络进行传输。文件过大则会在传输上消耗更多的时间的带宽。  
    　　STATEMENT: 为了让SQL可以在slave端正确地重现, 需要记录SQL执行的上下文信息。在复制某些特殊的函数或功能时可能会出现问题(如sleep函数)。

- **管理binlog的相关SQL语句**
  
  |SQL语句|语句含义|
  |:----|:----|
  |SHOW MASTER LOGS;|查看所有binlog的日志列表|
  |SHOW MASTER STATUS;|查看最后一个binlog日志的编号名称, 及最后一个时间结束的位置(pos)|
  |FLUSH LOGS;|刷新binlog, 此刻开始产生一个新编号的binlog日志文件|
  |RESET MASTER;|清空所有的binlog日志|
  
- **查看binlog相关的SQL语句**
　　`SHOW BINLOG EVENTS [IN 'log.name'] [FROM pos] [LIMIT [OFFSET,] ROW_COUNT];`

  |SQL语句|语句含义|
  |:----|:----|
  |SHOW BINLOG EVENTS;|查看第一个binlog日志|
  |SHOW BINLOG EVENTS IN 'binlog.000030';|查看指定的Binlog日志|
  |SHOW BINLOG EVENTS IN 'binlog.000030' FROM 931;|从指定的位置, 查看指定的binlog日志|
  |SHOW BINLOG EVENTS IN 'binlog.000030' FROM 931 LIMIT 2;|从指定的位置开始, 查看指定的binlog日志, 限制查询的条数|
  |SHOW BINLOG EVENTS IN 'binlog.000030' FROM 931 LIMIT 1, 2;|从指定的位置开始, 带有偏移, 查看指定的binlog日志, 限制查询的条数|

- **binlog中的event_type** (https://dev.mysql.com/doc/internals/en/event-classes-and-types.html)
    - QUERY_EVENT: 与数据无关的操作, begin、drop table、truncate table等  
    - TABLE_MAP_EVENT: 记录下一操作所对应的表信息, 存储了**数据库名**和**表名**
    - XID_EVENT: 标记事务提交
    - WRITE_ROWS_EVENT: 插入数据, 即INSERT操作
    - UPDATE_ROWS_EVENT: 更新数据, 即UPDATE操作
    - DELETE_ROWS_EVENT: 删除数据, 即DELETE操作

- **查询数据表中列索引和列名的对应关系**
    ```
    SELECT table_schema, table_name, column_name, ordinal_position
    FROM information_schema.columns 
    WHERE table_schema = '${db.name}' 
    AND table_name = '${table.name}';   
    ```    