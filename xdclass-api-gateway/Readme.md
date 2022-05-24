## 网关服务

核心是作为一个接口的转发层，在调用实际的服务之前做一层拦截，这里也可以做反向代理

### 集成方式

pom.xml
```xml
<!-- 添加依赖 gateway -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

在配置文件中增加配置
```yaml
spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      routes: # 数组形式
        - id: order-service # 路由唯一标识
          uri: http://127.0.0.1:8000 # 想要转发的地址
          order: 1 # 优先级，数字越小优先级越高
          predicates: # 断言，配置哪个路径才转发
            - Path=/order-server/**
          filters:  # 过滤器，请求在传递过程中通过过滤器修改
            - StripPrefix=1  #去掉第一层前缀
```

上面只能做到单点的转发，做不到负载均衡，下面就需要集成 nacos，通过 nacos 来做转发

pom.xml

增加
```xml
 <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

增加启动类注解
```java
@EnableDiscoveryClient
```

修改配置文件
需要将 gateway 中的 uri 修改为从 nacos 中获取值，还需要增加 nacos 的配置
```yaml
spring:
  application:
    name: api-gateway

  cloud:
    nacos:  # 集成 nacos
      discovery:
        server-addr: 192.168.1.102:8848

    gateway:
      routes: # 数组形式
        - id: order-service # 路由唯一标识
#          uri: http://127.0.0.1:8000 # 想要转发的地址
          uri: lb://xdclass-order-service  # 从nacos获取名称转发,lb是负载均衡轮训策略
          order: 1 # 优先级，数字越小优先级越高
          predicates: # 断言，配置哪个路径才转发
            - Path=/order-server/**
          filters:  # 过滤器，请求在传递过程中通过过滤器修改
            - StripPrefix=1  #去掉第一层前缀

      # 开启网关拉取 nacos 服务
      discovery:
        locator:
          enabled: true
```
