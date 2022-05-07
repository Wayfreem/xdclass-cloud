## 订单服务

### 集成 Ribbon 的修改

由于调用是需要使用到 `RestTemplate` 类，这里就需要增加一个 负载均衡的注解
```java
@Bean
@LoadBalanced   // 增加的注解
public RestTemplate getRestTemplate(){
    return new RestTemplate();
}
```

使用 restTemplate 调用时，通过调用服务名 调用
```java
Video video =  restTemplate.getForObject("http://xdclass-video-service/api/v1/video/find_by_id?videoId="+videoId, Video.class);
```


### 修改轮询策略

直接在配置文件 application.yml 中增加配置
```yaml
xdclass-video-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```


### 集成 feign 

由于使用 ribbon 的时候，代码存在的问题：不规范，风格不统一，维护性比较差。

什么是Feign

> SpringCloud提供的伪http客户端(本质还是用http)，封装了Http调用流程，更适合面向接口化
 让用Java接口注解的方式调用Http请求.
>
> 不用像Ribbon中通过封装HTTP请求报文的方式调用 Feign默认集成了Ribbon

集成方式

pom 文件增加依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

配置注解

在启动类中增加注解
```java
@EnableFeignClients
```

增加一个接口

```java
 // 订单服务增加接口，服务名称记得和nacos保持一样
@FeignClient(name="xdclass-video-service") 
public interface VideoService{
    
   // 传入参数调用服务，这里需要填写的就是相应的调用路径名
   @GetMapping(value = "/api/v1/video/find_by_id")
   // 调用方法名最好是和 调用方的方法名称一致，方便于排查, 
   // 这里是需要使用注解 @RequestParam("videoId") ，不然是会报错的
   Video findById(@RequestParam("videoId") int videoId); 

    // 使用 Post 方式提交
    @PostMapping(value = "/api/v1/video/saveByFeign")
    // 注意：这里是需要使用 @RequestBody()，并且在调用使用 postMan 调用的时候，
    // 需要指定 Content-Type 为 application/json
    int saveByFeign(@RequestBody() Video video);
}
```

**具体使用**

在 controller 层中注入 VideoService（接口），然后就可以直接调用