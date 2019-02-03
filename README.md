# 基于SpringCloud微服务架构, 广告系统设计与实现
慕课网学习, 基于Spring Cloud实现的Maven多模块广告系统设计微服务。

## ad-eureka模块
使用eureka在项目中微服务的注册与发现.
## ad-gateway模块
使用zuul在项目中作为网关, 提供动态路由、监控、弹性、安全等边缘服务。
## ad-service/ad-common模块
搭建项目的全局统一配置, 包括统一响应, 统一异常。
## ad-service/ad-dump模块
负责项目启动进行广告数据全量数据的文件导出。
## ad-service/ad-search模块
广告检索服务, 实现广告数据的全量索引, 增量索引加载功能(投递到redis / kafka), 实现媒体方检索功能。
## ad-service/ad-sponsor模块
广告投放模块, 实现广告主对于广告数据的一些增删改查的需求。