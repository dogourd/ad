### feign核心组件
|接口|作用|默认值|
|:----|:----|:----|
|Feign.Builder|Feign的入口|Feign.Builder|
|Client|Feign底层请求使用的客户端|配合Ribbon时为: LoadBalancerFeignClient<br/>不整合Ribbon时为Feign.Client.Default|
|Contract|契约,注解支持|SpringMvcContract|
|Encoder|编码器,用于将对象转化为HTTP请求消息体|SpringEncoder|
|Decoder|编码器,将响应消息体转化为对象|ResponseEntityDecoder|
|Logger|日志管理器|Slf4jLogger|
|RequestInterceptor|用于为每个请求添加通用逻辑|无|

### Feign自定义的细粒度配置(自定义日志配置)
#### Feign中定义的日志级别
|级别|打印内容|
|:----|:----|
|NONE(默认)|不记录任何日志|
|BASIC|仅记录请求方法、URL、响应状态码以及执行时间|
|HEADERS|在BASIC级别的基础上,记录请求和响应的header|
|FULL|记录请求和响应的header、body和元数据|
- Java代码配置
1. 在@FeignClient注解上设置configuration属性指定FeignClient的配置类
1. 在指定的配置类中使用@Bean注解注入相应日志级别的bean
1. 在springboot配置文件中设置配置类的日志输出级别为debug
- 配置属性配置 
在springboot配置文件中配置
feign.client.config.${service.name}.loggerLevel: FULL
### Feign中的全局配置(配置日志级别)
- Java代码配置
1. 让父子上下文的ComponentScan重叠(强烈不建议)
1. 在启动类上使用@EnableFeignClients注解并配置defaultConfiguration属性
- 配置属性配置
在springboot配置文件中配置
feign.client.config.default.loggerLevel: FULL

### Feign中支持的配置项
- 代码配置  

|配置项|作用|
|:----|:----|
|Feign.Builder|Feign的入口|
|Client|Feign底层用什么去请求|
|Contract|契约,注解支持|
|Encoder|编码器,用于将对象转换为HTTP请求消息体|
|Decoder|解码器,用于将响应消息体转化成对象|
|Logger|日志管理器|
|Logger.Level|指定日志的级别|
|Retryer|指定重试策略|
|ErrorDecoder|指定错误解码器|
|Request.Options|超时时间|
|Collection<RequestInterceptor>|拦截器|
|SetterFactory|用于设置Hystrix的配置属性,Feign整合Hystrix时才会使用|
- 配置属性配置  
feign.client.config:  
　　`<feignName>`:  
　　　　connectTimeout: 5000 # 连接超时时间  
　　　　readTimeout: 5000 # 读取超时时间  
　　　　loggerLevel: FULL # 日志级别  
　　　　errorDecoder: com.example.SimpleErrorDecoder # 错误解码器  
　　　　retryer: com.example.SimpleRetryer # 重试策略  
　　　　requestInterceptors:  
　　　　　　- com.example.FooRequestInterceptor #拦截器  
　　　　# 是否对404错误码解码  
　　　　# 处理逻辑见feign.SynchronousMethodHandler#executeAndDecode  
　　　　decode404: false  
　　　　encoder: com.example.SimpleEncoder # 编码器  
　　　　decoder: com.example.SimpleDecoder # 解码器  
　　　　contract: com.example.SimpleContract # 契约  
### Feign配置最佳实践
- Ribbon配置 vs Feign配置  
Ribbon是一个负载均衡器,帮助我们选择实例,而Feign是一个HTTP client,
帮助我们更加舒服得实现远程HTTP 请求,它们不是一类产品,但是Ribbon和Feign
的配置具有很多共同点,它们**都支持代码方式配置和属性方式配置**,**都支持
细粒度的局部配置以及全局配置**,但是**Ribbon不支持用属性配置的方式配置全局配置**
  
- Feign代码配置 vs Feign属性配置

|配置方式|优点|缺点|
|:----|:----|:----|
|代码配置|基于代码,更加灵活|如果Feign的配置类加了@Configuration注解,则需要注意父子上下文的问题<br/>线上修改需要重新打包发布|
|属性配置|易上手,配置更加直观,<br/>线上修改无需重新打包、发布，优先级更高|极端场景下,没有代码方式配置灵活|

优先级关系(左高右低):   
细粒度的属性配置 > 细粒度的代码配置 > 全局的属性配置 > 全局的代码配置  
- Feign配置最佳实践
1. 尽量使用属性配置,属性配置无法实现时再考虑使用代码配置
1. 在同一个微服务中尽量保持单一性,不要同时使用两种配置方式

### Feign继承

### Feign脱离Ribbon使用
FeignClient中指定name,url  

### RestTemplate Vs Fiegn

|角度|RestTemplate|Feign|
|:----|:----|:----|
|可读性、可维护性|一般|极佳|
|开发体验|欠佳|极佳|
|性能|很好|中等(RestTemplate的50%左右)|
|灵活性|极佳|中等(内置功能可满足绝大多数需求)|

#### 如何选择
- 原则 尽量使用Feign, 杜绝使用RestTemplate
- 事无绝对, 合理选择

### Feign性能优化
- 连接池 (提升15%左右)  
默认情况下Feign是使用HttpURLConnection来进行请求,它是没有连接池
的,但事实上Feign底层,除了可以使用HttpURLConnection发送请求以外,
还支持使用apache的HttpClient以及OKHttp去发送请求,而这两个客户端都是
支持连接池的。  
    1. 加依赖
    ```text
    <dependency>
      <groupId>io.github.openfeign</groupId>
      <artifactId>feign-httpclient<artifactId>
    </dependency>
    ```
    2. 写配置  
    `# 让Feign使用apache httpclient做请求,而不是默认的HttpURLConnection`    
    feign.httpclient.enabled: true  
    `# Feign的最大连接数`    
    feign.httpclient.max-connections: 200  
    `# Feign单个路径(RequestMapping)的最大连接数,需要结合压测来
    尝试将最大连接数和单路径最大连接数调节为最优配置比例`    
    feign.httpclient.max-connections-per-route: 50  
- 日志级别  
Feign默认是不打印日志的,这个日志级别的性能大概是最好的,但是生产环境
如果需要理解请求的具体细节,就需要将日志级别设置为basic,绝对不建议设置为
FULL,因为打印的日志太多了,对性能的损耗相对比较大。

### Feign常见问题总结        