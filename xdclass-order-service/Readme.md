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
    // 这里使用 @RequestMapping 与 @PostMapping 注解都是可以的
    @PostMapping(value = "/api/v1/video/saveByFeign")
    // 注意：这里是需要使用 @RequestBody()，并且在调用使用 postMan 调用的时候，
    // 需要指定 Content-Type 为 application/json
    int saveByFeign(@RequestBody() Video video);
}
```

**具体使用**

在 controller 层中注入 VideoService（接口），然后就可以直接调用

### 集成 sentinel

#### sentinel 控制台

sentinel 有一个单独的控制台，可以去官网上面去下载，然后通过命令行启动 [链接地址](https://sentinelguard.io/zh-cn/docs/dashboard.html)

默认的账号、密码都是 sentinel 
```shell
java -Dserver.port=8085 -Dcsp.sentinel.dashboard.server=localhost:8085 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar


nohup java -Dserver.port=8085 -Dcsp.sentinel.dashboard.server=localhost:8085 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.0.jar &
```

#### sentinel 源码集成

pom 文件增加依赖
```xml
 <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

application 配置文件
```yaml
#dashboard: 8080 控制台端口
#port: 9999 本地启的端口，随机选个不能被占用的，与dashboard进行数据交互，会在应用对应的机器上启动一个 Http Server，
# 该 Server 会与 Sentinel 控制台做交互, 若被占用,则开始+1一次扫描

spring:
  cloud:
    sentinel:
      transport:
        dashboard: 127.0.0.1:8080 
        port: 9999
```