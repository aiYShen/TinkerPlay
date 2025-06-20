# TinkerPlay

#### 介绍
从系统设计规划-->编码开发-->测试调试-->规划部署-->运维支持-->程序调优-->迭代改进的一体化角度出发! 学习、温习、记录工作生活中遇到的常规性需求解决思路和案例、集成各种中间件的思路和案例、各种处理策略、算法基础、设计模式基础、测试知识点的代码库.

#### 架构
JAVA_21 + springboot_3.4.7 + mybatis_3.5.19 + redis_7.0.9 + rabbitmq_3.11.10 + ELK_7.17.3 等, maven_3.9.9构建


#### 配置文件application.yml说明

1.  多数据源配置
2.  redis配置及配置类
3.  rabbitmq配置及配置类
4.  mybatis配置
5.  swagger配置及配置类
6.  Flyway配置及配置类
7.  Minio配置及配置类
8.  nacos配置
9.  druid配置
10. springdoc配置及配置类
11. elasticsearch配置及配置类
12. flink配置
13. ....等

#### TinkerPlay内容

1.  常规springboot项目的搭建
2.  AOP自定义日志格式打印
3.  AOP参数判空配置
4.  单表批量插入性能测试
5.  各种工具类编写、收集、完善、测试: json数据消除转义工具类、MAC地址获取工具类、生成token工具类、解析Token工具类、调用其他接口工具类、数据库连接工具类、
                  读取Excel工具类,解析和生成二维码图片工具类等等...
6.  redis缓存管理器: 缓存失效时间和缓存的key自定义
7.  自定义注解
8.  常规增删改查、批量插入等
9.  集成redis、rabbitmq等配置
10. JDBC连接池,连接工具学习
11. 构建者模式构建JDBC数据库连接对象
12. AES加解密功能:加密返回数据和解密参数
13. 延时双删机制
14. 温习自定义异常,自定义注解,自定义redis配置文件等
15. 文件上传,支持多文件上传
16. 自定义定时任务功能,Springboot自带定时任务注解等    
17. 熟悉学习基础算法,收集网络上大佬们的算法:
        一. 递归算法及其案例: 文件递归搜索
        二. 冒泡排序
        三. 分页算法
        .....
18. 模拟数据结构: 
        一. 堆栈数据结构
        二. 二叉树数据结构
        三. 红黑树数据结构
        四. BTree
        .....
19. 集成Flyway,及其配置类 自动升级数据库脚本
20. 集成swagger,及其配置类
21. Java爬虫,爬取页面
22. 新增Sm2非对称加密工具类
23. 流式查询读取400万数据量,分析异常数据,写入25万异常数据    
24. java多线程基础温习
25. 温习单例模式
26. 学习镜像部署   
27. 新增字段脱敏显示功能
28. 根据定制好的简单PDF模板生成输出单页pdf文件    
29. 多线程分片处理,提高并发性探索
30. 集成开源的分页工具并测试
31. 完善普通文件上传功能;集成MinIO对象存储（OSS）,部署MinIO,开发新的上传,下载,预览等功能
32. yml配置文件敏感信息加密 
33. 梳理一健打包项目为镜像到Linux自建镜像仓库
34. 集成整合ELK,实现基本使用    
35. 集成第三方开源的ChatGPT包,封装一下玩一下(ChatGPT账号访问api权限已不足)
36. 集成Nacos配置中心使用   
37. 玩一下Flink 
38. 非阻塞IO-NIO对比传统IO
39. 结合腾讯云短信发送API,玩一下发送短信
40. 集成kafka,研究一下
41. 拦截器和过滤器
42. MQ异步广播消息
43. 搞一个工具,读取.sql文件,执行里面的DDL语句,多个数据库环境,执行记录写入Excel表格
44. 完善日志配置: 日志输出格式,日志分割规则,日志常规配置等
45. 测试git提交
46. 框架版本全面升级:JAVA_1.8 + springboot_2.7.5 + mybatis_3.4.6 + redis_7.0.9 + rabbitmq_3.11.10 + ELK_7.17.3 等,maven_3.8.4构建
    到 JAVA_20 + springboot_3.2.0 + mybatis_3.5.15 + redis_7.0.9 + rabbitmq_3.11.10 + ELK_7.17.3 等,maven_3.9.6构建
47. 排查整体结构中存在的问题并解决
48. 解决集成flyway自定义的数据源bean会覆盖baomidou的数据源bean,导致多数据源不生效的问题
49. JDK20 升级到 JDK21
50. 开启JDK21的ZGC日志记录(--add-opens
    java.base/java.util=ALL-UNNAMED
    -Xms100M
    -Xmx2G
    -XX:ReservedCodeCacheSize=64m
    -XX:InitialCodeCacheSize=32m
    -XX:+UseZGC
    -XX:ConcGCThreads=2
    -XX:ParallelGCThreads=3
    -XX:ZCollectionInterval=60
    -XX:ZAllocationSpikeTolerance=2
    -Xlog:safepoint,classhisto*=trace,age*,gc*=info:file=E:\aiyuns\TinkerPlay\logs\TinkerPlay\gc-%t.log:time,tid,tags:filecount=5,filesize=50m),
     尝试调优!!!
51. 现有功能全部适配SpringBoot_3.2.0版本,测试并发现问题及处理
52. 配合拦截器,实现限流的基本思路和原理
53. 程序加载词向量模型, 判断汉语语句相似度Demo
54. 尝试玩下地理信息相关的Java开源包及前端开发包(com.esri.arcgisruntim, org.geotools, org.gdal, org.locationtech.jts, JavaFX)
55. 优化项目结构:模块化-新增flink- job模块, 方便单独打包
56. Spring Ai 1.0.0正式版发布,尝试集成并学习使用它
57. 开发风格以后遵循Google的Java编码规范
58. ..............................

#### 部署内容
Linux:
    docker部署:
            1. nacos_v2.1.0 配置中心
            2. redis_v7.0.9 缓存数据库
            3. harbor_v1.10.10 私有镜像仓库
            4. nginx_v1.23.3 转发代理
            5. apisix_v3.0.0-alpine 网关
            6. elasticsearch_v7.17.3 搜索和分析引擎,logstash_v7.17.3 服务器端数据处理管道,kibana_v7.17.3 数据可视化
            7. rabbitmq_v3.11.10 消息队列
            8. kafka_v2.8.1 分布式事件流平台
            9. zookeeper_v3.8.1 集中式服务
            10. clickhouse_v24.8.4.13    数据库
            11. mysql_v8.0.32   数据库
    普通部署:
            12. flink_v1.17.0 分布式处理框架
Windows:
            13. minio对象存储桶
            14. mysql_v8.0.11   数据库
Mac: 
            15. clickhouse_v24.8.4.13    数据库
            16. mysql_v8.0.36   数据库
            17. flink_v1.20.0 分布式处理框架

#### 说明
供小白TinkerPlay!

#### 总结
**Knowledge is power!**
