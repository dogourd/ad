### ribbon核心组件
|接口|作用|默认值|
|:----|:----|:----|
|IClientConfig|读取配置|DefaultClientConfigImpl|
|IRule|负载均衡规则,选择实例|ZoneAvoidanceRule|
|IPing|筛选掉Ping不通的实例|DummyPing|
|ServerList<Server>|交给Ribbon的实例列表|Ribbon: ConfigurationBasedServerList<br/>SpringCloudAlibaba: NacosServerList|
|ServerListFilter<Server>|过滤掉不符合条件的实例|ZonePreferenceServerListFilter|
|ILoadBalancer|Ribbon入口|ZoneAwareLoadBalancer|
|ServerListUpdater|更新交给Ribbon的List的策略|PollingServerListUpdater|
### Ribbon内置负载均衡规则
|规则名称|特点|
|:----|:----|
|AvailabilityFilteringRule|过滤掉一直连接失败的被标记为circuit tripped的后端server,并过滤掉那些高并发的server或者使用一个AvailabilityPredicate来包含过滤server的逻辑,其实就是检查status里记录的各个server的状态|
|BestAvailableRule|选择一个最小的并发请求的server,逐个考察server,如果server被tripped了,则跳过|
|RandomRule|随机选择一个server|
|ResponseTimeWeightedRule|已废弃,作用同WeightedResponseTimeRule|
|RetryRule|对选定的负载均衡策略机上采用重试机制,在一个配置时间段内,当选择server不成功时,则一直尝试使用subRule的方式选择一个可用的server|
|RoundRobinRule|轮询选择,轮询index,选择index位置对应的server|
|WeightedResponseTimeRule|根据响应时间加权,响应时间越小,权重越小,被选中的可能性越低|
|ZoneAvoidanceRule|复合判断server所在zone的性能和server的可用性来选择zone,在没有zone的情况下,类似于轮询(RoundRobinRule)|

### Ribbon负载均衡策略 代码配置 vs 文件配置
|配置方式|优点|缺点|
|:----|:----|:----|
|代码配置|基于代码,更加灵活|有坑(Spring父子上下文),线上如果修改需要重新打包发布|
|文件配置|上手简单,配置更加直观,线上修改无需重新打包发布,优先级比代码方式更高|极端场景下没有代码配置灵活|

### Ribbon支持的配置项
- 代码中可以在springboot 扫描包以外创建ribbon的配置包并编写配置类,
在配置类中使用@Bean注入相应的Bean如以下几种  
IRule,IPing,ILoadBalancer,ServerList,ServerListFilter
- 属性配置使用clientName.ribbon为前缀配置以下规则  
NFLoadBalancerPingClassName(对应IPing),  
NFLoadBalancerRuleClassName(对应IRule),  
NFLoadBalancerClassName(对应ILoadBalancer),  
NIWSSServerListClassName(对应ServerList),  
NIWSSServerListFilterClassName(对应ServerListFilter)

### Ribbon饥饿加载
默认情况下,ribbon是懒加载的,即只有当使用RestTemplate进行第一次服务之间调用的时候
才会创建被调用服务的RibbonClient, 这样就会造成服务第一次调用过慢的问题。可以在application.yml
文件中配置以下内容来开启Ribbon的饥饿加载,并可以细粒度地指定服务名来告诉Ribbon哪些服务需要饥饿加载  
ribbon.eager-load.enabled=true
ribbon.eager-load.clients=client1,client2

### 扩展Ribbon支持Nacos权重
在Nacos控制台中编辑服务某个实例地权重(0~1之间),值越大被调用的机率则越大。  
Ribbon内置的几个负载均衡规则都是不支持Nacos权重的,此时可以对其进行扩展来支持Nacos权重
ribbon.config.NacosWeightedRule

### 扩展Ribbon支持同集群优先 
场景: 为了容灾,服务分别在南京机房和北京机房各部署一套
需求: 实现南京机房的服务消费者优先调用南京机房的服务提供者
方案: 使用Nacos的cluster(Nacos服务发现的领域模型)     
ribbon.config.NacosSameClusterWeightRule

### 扩展Ribbon基于元数据的版本控制
场景: 服务提供者和服务消费者具有两个版本v1和v2,两个版本之间不兼容
需求: 需要服务消费者可以自动调用对应版本的服务提供者实例   
方案: 使用Nacos的元数据(配置spring.cloud.nacos.discovery.metadata下配置键值对)
ribbon.config.NacosSameVersionWeightRule

### 深入理解Nacos namespace
namespace --> group --> service --> cluster --> instance 

### Ribbon调用存在的问题
1. Ribbon直接使用RestTemplate进行调用, Url作为参数传递导致代码不可读
1. 若URL变得异常复杂,难以维护
1. 微服务适合有快速迭代需求的项目,可能需要频繁修改url参数,难以响应需求的变化
1. IDE难以提供良好的提示,编程体验较差  
[//]: # TODO 慕课网手记,三种基于Nacos权重的负载均衡实现方法