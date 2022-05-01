## 来自小滴课堂 Spring Cloud Alibaba 学习

### 使用的技术有 

#### nacos

**linux 中安装 nacos** 

- 下载官方的包，并且解压
- 进入 nacos 的 bin 路径下，执行 nacos 的启动命令：`sh startup.sh -m standalone`
- 需要添加开放端口 8848，以及重新加载防火墙
- 访问地址为：`localhost:8848/nacos`， 用户密码为：nacos/nacos

另外在启动完成之后，可以进入 nacos 路径下的 logs 目录，查看启日志 `tail start.out`

**nacos 代码修改** 
 
在 video-service、order-service、user-service 的 pom 文件中添加依赖

```xml
<!--添加nacos客户端-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

配置 Nacos 的地址

```yml
server:
  port:9000

spring:
  application:
    name: xsclass-video-service
  cloud:
    nacos:
      discovery:
        server-addr:127.0.0.1:8848
```

启动类增加注解  `@EnableDiscoveryClient`

