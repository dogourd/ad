# Spring Cloud -- Hystrix Dashboard
## Hystrix
Hystrix这个组件即断路器,它可以实现服务降级的功能。也就是说定义的服务如果发生错误就
可以通过使用Hystrix定义错误之后的回退, 通常在Spring Cloud工程中使用Hystrix组件
是通过`@HystrixCommand`去实现的, 本项目中Hystrix是结合OpenFeign一起去使用的。
OpenFeign是用来实现微服务之间的调用, 当微服务之间的调用出错之后就会使用到Hystrix
定义的断路(`@FeignClient`中提供了fallback参数指定了降级的实现类) 
#### @HystrixCommand(该注解支持注释方法)
- groupKey   
  该参数指定HystrixCommand所属组的名称默认是被注解方法所属类的名称
- fallbackMethod  
  该参数定义回退方法的名称, 并且要求回退方法必须和注解方法在相同的类中。具体使用方法
  示例在
  [SearchImpl](https://github.com/jaaaar/cloud-ad/blob/master/ad-service/ad-search/src/main/java/top/ezttf/ad/search/impl/SearchImpl.java)中有表示
  其实现是通过在应用类上使用`@EnableCircuitBreaker`,
  该注解会通过AOP拦截所有的具有 `@HystrixCommand`注解的方法,
  并且将方法扔到Hystrix的线程池中。当发生失败时通过发射去调用回退方法实现一个断路的效果。
  即**CircuitBreaker将被@HystrixCommand注释的方法放到Hystrix自己的线程池中并且使用
  try-catch-finally进行包装,
  去通过反射调用fallback指定的方法。**同时Hystrix会在
  线程池中记录它的一些内存信息,
  在内存中保存着一些线程池的执行信息。所以可以使用一个服务
  或者工具读取这些信息并以仪表盘的形式进行展示, 比如成功调用次数, 失败调用次数,
  服务调用频率等等。实际开发中单独使用Hystrix(即使用@HystrixCommand)是很少的
  一般都是在`@FeignClient`中指定fallback即结合feign使用。
  
### Dashboard  